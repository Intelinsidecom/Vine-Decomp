package com.google.android.exoplayer;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.PriorityHandlerThread;
import com.google.android.exoplayer.util.TraceUtil;
import com.google.android.exoplayer.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
final class ExoPlayerImplInternal implements Handler.Callback {
    private long elapsedRealtimeUs;
    private final List<TrackRenderer> enabledRenderers;
    private final Handler eventHandler;
    private final Handler handler;
    private long lastSeekPositionMs;
    private final long minBufferUs;
    private final long minRebufferUs;
    private boolean playWhenReady;
    private volatile long positionUs;
    private boolean rebuffering;
    private boolean released;
    private MediaClock rendererMediaClock;
    private TrackRenderer rendererMediaClockSource;
    private TrackRenderer[] renderers;
    private final int[] selectedTrackIndices;
    private final MediaFormat[][] trackFormats;
    private int customMessagesSent = 0;
    private int customMessagesProcessed = 0;
    private int state = 1;
    private volatile long durationUs = -1;
    private volatile long bufferedPositionUs = -1;
    private final StandaloneMediaClock standaloneMediaClock = new StandaloneMediaClock();
    private final AtomicInteger pendingSeekCount = new AtomicInteger();
    private final HandlerThread internalPlaybackThread = new PriorityHandlerThread("ExoPlayerImplInternal:Handler", -16);

    public ExoPlayerImplInternal(Handler eventHandler, boolean playWhenReady, int[] selectedTrackIndices, int minBufferMs, int minRebufferMs) {
        this.eventHandler = eventHandler;
        this.playWhenReady = playWhenReady;
        this.minBufferUs = minBufferMs * 1000;
        this.minRebufferUs = minRebufferMs * 1000;
        this.selectedTrackIndices = Arrays.copyOf(selectedTrackIndices, selectedTrackIndices.length);
        this.enabledRenderers = new ArrayList(selectedTrackIndices.length);
        this.trackFormats = new MediaFormat[selectedTrackIndices.length][];
        this.internalPlaybackThread.start();
        this.handler = new Handler(this.internalPlaybackThread.getLooper(), this);
    }

    public long getCurrentPosition() {
        return this.pendingSeekCount.get() > 0 ? this.lastSeekPositionMs : this.positionUs / 1000;
    }

    public long getBufferedPosition() {
        if (this.bufferedPositionUs == -1) {
            return -1L;
        }
        return this.bufferedPositionUs / 1000;
    }

    public long getDuration() {
        if (this.durationUs == -1) {
            return -1L;
        }
        return this.durationUs / 1000;
    }

    public void prepare(TrackRenderer... renderers) {
        this.handler.obtainMessage(1, renderers).sendToTarget();
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.handler.obtainMessage(3, playWhenReady ? 1 : 0, 0).sendToTarget();
    }

    public void seekTo(long positionMs) {
        this.lastSeekPositionMs = positionMs;
        this.pendingSeekCount.incrementAndGet();
        this.handler.obtainMessage(6, Util.getTopInt(positionMs), Util.getBottomInt(positionMs)).sendToTarget();
    }

    public void stop() {
        this.handler.sendEmptyMessage(4);
    }

    public void sendMessage(ExoPlayer.ExoPlayerComponent target, int messageType, Object message) {
        this.customMessagesSent++;
        this.handler.obtainMessage(9, messageType, 0, Pair.create(target, message)).sendToTarget();
    }

    public synchronized void blockingSendMessage(ExoPlayer.ExoPlayerComponent target, int messageType, Object message) {
        if (this.released) {
            Log.w("ExoPlayerImplInternal", "Sent message(" + messageType + ") after release. Message ignored.");
        } else {
            int messageNumber = this.customMessagesSent;
            this.customMessagesSent = messageNumber + 1;
            this.handler.obtainMessage(9, messageType, 0, Pair.create(target, message)).sendToTarget();
            while (this.customMessagesProcessed <= messageNumber) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public synchronized void release() {
        if (!this.released) {
            this.handler.sendEmptyMessage(5);
            while (!this.released) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            this.internalPlaybackThread.quit();
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case 1:
                    prepareInternal((TrackRenderer[]) msg.obj);
                    z = true;
                    break;
                case 2:
                    incrementalPrepareInternal();
                    z = true;
                    break;
                case 3:
                    setPlayWhenReadyInternal(msg.arg1 != 0);
                    z = true;
                    break;
                case 4:
                    stopInternal();
                    z = true;
                    break;
                case 5:
                    releaseInternal();
                    z = true;
                    break;
                case 6:
                    seekToInternal(Util.getLong(msg.arg1, msg.arg2));
                    z = true;
                    break;
                case 7:
                    doSomeWork();
                    z = true;
                    break;
                case 8:
                    setRendererSelectedTrackInternal(msg.arg1, msg.arg2);
                    z = true;
                    break;
                case 9:
                    sendMessageInternal(msg.arg1, msg.obj);
                    z = true;
                    break;
            }
            return z;
        } catch (ExoPlaybackException e) {
            Log.e("ExoPlayerImplInternal", "Internal track renderer error.", e);
            this.eventHandler.obtainMessage(4, e).sendToTarget();
            stopInternal();
            return true;
        } catch (RuntimeException e2) {
            Log.e("ExoPlayerImplInternal", "Internal runtime error.", e2);
            this.eventHandler.obtainMessage(4, new ExoPlaybackException(e2, true)).sendToTarget();
            stopInternal();
            return true;
        }
    }

    private void setState(int state) {
        if (this.state != state) {
            this.state = state;
            this.eventHandler.obtainMessage(2, state, 0).sendToTarget();
        }
    }

    private void prepareInternal(TrackRenderer[] renderers) throws ExoPlaybackException {
        resetInternal();
        this.renderers = renderers;
        Arrays.fill(this.trackFormats, (Object) null);
        for (int i = 0; i < renderers.length; i++) {
            MediaClock mediaClock = renderers[i].getMediaClock();
            if (mediaClock != null) {
                Assertions.checkState(this.rendererMediaClock == null);
                this.rendererMediaClock = mediaClock;
                this.rendererMediaClockSource = renderers[i];
            }
        }
        setState(2);
        incrementalPrepareInternal();
    }

    private void incrementalPrepareInternal() throws ExoPlaybackException {
        long operationStartTimeMs = SystemClock.elapsedRealtime();
        boolean prepared = true;
        for (int rendererIndex = 0; rendererIndex < this.renderers.length; rendererIndex++) {
            TrackRenderer renderer = this.renderers[rendererIndex];
            if (renderer.getState() == 0) {
                int state = renderer.prepare(this.positionUs);
                if (state == 0) {
                    renderer.maybeThrowError();
                    prepared = false;
                }
            }
        }
        if (!prepared) {
            scheduleNextOperation(2, operationStartTimeMs, 10L);
            return;
        }
        long durationUs = 0;
        boolean allRenderersEnded = true;
        boolean allRenderersReadyOrEnded = true;
        for (int rendererIndex2 = 0; rendererIndex2 < this.renderers.length; rendererIndex2++) {
            TrackRenderer renderer2 = this.renderers[rendererIndex2];
            int rendererTrackCount = renderer2.getTrackCount();
            MediaFormat[] rendererTrackFormats = new MediaFormat[rendererTrackCount];
            for (int trackIndex = 0; trackIndex < rendererTrackCount; trackIndex++) {
                rendererTrackFormats[trackIndex] = renderer2.getFormat(trackIndex);
            }
            this.trackFormats[rendererIndex2] = rendererTrackFormats;
            if (rendererTrackCount > 0) {
                if (durationUs != -1) {
                    long trackDurationUs = renderer2.getDurationUs();
                    if (trackDurationUs == -1) {
                        durationUs = -1;
                    } else if (trackDurationUs != -2) {
                        durationUs = Math.max(durationUs, trackDurationUs);
                    }
                }
                int trackIndex2 = this.selectedTrackIndices[rendererIndex2];
                if (trackIndex2 >= 0 && trackIndex2 < rendererTrackFormats.length) {
                    renderer2.enable(trackIndex2, this.positionUs, false);
                    this.enabledRenderers.add(renderer2);
                    allRenderersEnded = allRenderersEnded && renderer2.isEnded();
                    allRenderersReadyOrEnded = allRenderersReadyOrEnded && rendererReadyOrEnded(renderer2);
                }
            }
        }
        this.durationUs = durationUs;
        if (allRenderersEnded && (durationUs == -1 || durationUs <= this.positionUs)) {
            this.state = 5;
        } else {
            this.state = allRenderersReadyOrEnded ? 4 : 3;
        }
        this.eventHandler.obtainMessage(1, this.state, 0, this.trackFormats).sendToTarget();
        if (this.playWhenReady && this.state == 4) {
            startRenderers();
        }
        this.handler.sendEmptyMessage(7);
    }

    private boolean rendererReadyOrEnded(TrackRenderer renderer) {
        boolean z = false;
        if (renderer.isEnded()) {
            return true;
        }
        if (!renderer.isReady()) {
            return false;
        }
        if (this.state == 4) {
            return true;
        }
        long rendererDurationUs = renderer.getDurationUs();
        long rendererBufferedPositionUs = renderer.getBufferedPositionUs();
        long minBufferDurationUs = this.rebuffering ? this.minRebufferUs : this.minBufferUs;
        if (minBufferDurationUs <= 0 || rendererBufferedPositionUs == -1 || rendererBufferedPositionUs == -3 || rendererBufferedPositionUs >= this.positionUs + minBufferDurationUs || (rendererDurationUs != -1 && rendererDurationUs != -2 && rendererBufferedPositionUs >= rendererDurationUs)) {
            z = true;
        }
        return z;
    }

    private void setPlayWhenReadyInternal(boolean playWhenReady) throws ExoPlaybackException {
        try {
            this.rebuffering = false;
            this.playWhenReady = playWhenReady;
            if (!playWhenReady) {
                stopRenderers();
                updatePositionUs();
            } else if (this.state == 4) {
                startRenderers();
                this.handler.sendEmptyMessage(7);
            } else if (this.state == 3) {
                this.handler.sendEmptyMessage(7);
            }
        } finally {
            this.eventHandler.obtainMessage(3).sendToTarget();
        }
    }

    private void startRenderers() throws ExoPlaybackException {
        this.rebuffering = false;
        this.standaloneMediaClock.start();
        for (int i = 0; i < this.enabledRenderers.size(); i++) {
            this.enabledRenderers.get(i).start();
        }
    }

    private void stopRenderers() throws ExoPlaybackException {
        this.standaloneMediaClock.stop();
        for (int i = 0; i < this.enabledRenderers.size(); i++) {
            ensureStopped(this.enabledRenderers.get(i));
        }
    }

    private void updatePositionUs() {
        if (this.rendererMediaClock != null && this.enabledRenderers.contains(this.rendererMediaClockSource) && !this.rendererMediaClockSource.isEnded()) {
            this.positionUs = this.rendererMediaClock.getPositionUs();
            this.standaloneMediaClock.setPositionUs(this.positionUs);
        } else {
            this.positionUs = this.standaloneMediaClock.getPositionUs();
        }
        this.elapsedRealtimeUs = SystemClock.elapsedRealtime() * 1000;
    }

    private void doSomeWork() throws ExoPlaybackException {
        TraceUtil.beginSection("doSomeWork");
        long operationStartTimeMs = SystemClock.elapsedRealtime();
        long bufferedPositionUs = this.durationUs != -1 ? this.durationUs : Long.MAX_VALUE;
        boolean allRenderersEnded = true;
        boolean allRenderersReadyOrEnded = true;
        updatePositionUs();
        for (int i = 0; i < this.enabledRenderers.size(); i++) {
            TrackRenderer renderer = this.enabledRenderers.get(i);
            renderer.doSomeWork(this.positionUs, this.elapsedRealtimeUs);
            allRenderersEnded = allRenderersEnded && renderer.isEnded();
            boolean rendererReadyOrEnded = rendererReadyOrEnded(renderer);
            if (!rendererReadyOrEnded) {
                renderer.maybeThrowError();
            }
            allRenderersReadyOrEnded = allRenderersReadyOrEnded && rendererReadyOrEnded;
            if (bufferedPositionUs != -1) {
                long rendererDurationUs = renderer.getDurationUs();
                long rendererBufferedPositionUs = renderer.getBufferedPositionUs();
                if (rendererBufferedPositionUs == -1) {
                    bufferedPositionUs = -1;
                } else if (rendererBufferedPositionUs != -3 && (rendererDurationUs == -1 || rendererDurationUs == -2 || rendererBufferedPositionUs < rendererDurationUs)) {
                    bufferedPositionUs = Math.min(bufferedPositionUs, rendererBufferedPositionUs);
                }
            }
        }
        this.bufferedPositionUs = bufferedPositionUs;
        if (allRenderersEnded && (this.durationUs == -1 || this.durationUs <= this.positionUs)) {
            setState(5);
            stopRenderers();
        } else if (this.state == 3 && allRenderersReadyOrEnded) {
            setState(4);
            if (this.playWhenReady) {
                startRenderers();
            }
        } else if (this.state == 4 && !allRenderersReadyOrEnded) {
            this.rebuffering = this.playWhenReady;
            setState(3);
            stopRenderers();
        }
        this.handler.removeMessages(7);
        if ((this.playWhenReady && this.state == 4) || this.state == 3) {
            scheduleNextOperation(7, operationStartTimeMs, 10L);
        } else if (!this.enabledRenderers.isEmpty()) {
            scheduleNextOperation(7, operationStartTimeMs, 1000L);
        }
        TraceUtil.endSection();
    }

    private void scheduleNextOperation(int operationType, long thisOperationStartTimeMs, long intervalMs) {
        long nextOperationStartTimeMs = thisOperationStartTimeMs + intervalMs;
        long nextOperationDelayMs = nextOperationStartTimeMs - SystemClock.elapsedRealtime();
        if (nextOperationDelayMs <= 0) {
            this.handler.sendEmptyMessage(operationType);
        } else {
            this.handler.sendEmptyMessageDelayed(operationType, nextOperationDelayMs);
        }
    }

    private void seekToInternal(long positionMs) throws ExoPlaybackException {
        try {
            if (positionMs != this.positionUs / 1000) {
                this.rebuffering = false;
                this.positionUs = positionMs * 1000;
                this.standaloneMediaClock.stop();
                this.standaloneMediaClock.setPositionUs(this.positionUs);
                if (this.state != 1 && this.state != 2) {
                    for (int i = 0; i < this.enabledRenderers.size(); i++) {
                        TrackRenderer renderer = this.enabledRenderers.get(i);
                        ensureStopped(renderer);
                        renderer.seekTo(this.positionUs);
                    }
                    setState(3);
                    this.handler.sendEmptyMessage(7);
                }
            }
        } finally {
            this.pendingSeekCount.decrementAndGet();
        }
    }

    private void stopInternal() {
        resetInternal();
        setState(1);
    }

    private void releaseInternal() {
        resetInternal();
        setState(1);
        synchronized (this) {
            this.released = true;
            notifyAll();
        }
    }

    private void resetInternal() {
        this.handler.removeMessages(7);
        this.handler.removeMessages(2);
        this.rebuffering = false;
        this.standaloneMediaClock.stop();
        if (this.renderers != null) {
            for (int i = 0; i < this.renderers.length; i++) {
                TrackRenderer renderer = this.renderers[i];
                stopAndDisable(renderer);
                release(renderer);
            }
            this.renderers = null;
            this.rendererMediaClock = null;
            this.rendererMediaClockSource = null;
            this.enabledRenderers.clear();
        }
    }

    private void stopAndDisable(TrackRenderer renderer) {
        try {
            ensureStopped(renderer);
            if (renderer.getState() == 2) {
                renderer.disable();
            }
        } catch (ExoPlaybackException e) {
            Log.e("ExoPlayerImplInternal", "Stop failed.", e);
        } catch (RuntimeException e2) {
            Log.e("ExoPlayerImplInternal", "Stop failed.", e2);
        }
    }

    private void release(TrackRenderer renderer) {
        try {
            renderer.release();
        } catch (ExoPlaybackException e) {
            Log.e("ExoPlayerImplInternal", "Release failed.", e);
        } catch (RuntimeException e2) {
            Log.e("ExoPlayerImplInternal", "Release failed.", e2);
        }
    }

    private <T> void sendMessageInternal(int what, Object obj) throws ExoPlaybackException {
        try {
            Pair<ExoPlayer.ExoPlayerComponent, Object> targetAndMessage = (Pair) obj;
            ((ExoPlayer.ExoPlayerComponent) targetAndMessage.first).handleMessage(what, targetAndMessage.second);
            if (this.state != 1 && this.state != 2) {
                this.handler.sendEmptyMessage(7);
            }
            synchronized (this) {
                this.customMessagesProcessed++;
                notifyAll();
            }
        } catch (Throwable th) {
            synchronized (this) {
                this.customMessagesProcessed++;
                notifyAll();
                throw th;
            }
        }
    }

    private void setRendererSelectedTrackInternal(int rendererIndex, int trackIndex) throws ExoPlaybackException {
        TrackRenderer renderer;
        int rendererState;
        if (this.selectedTrackIndices[rendererIndex] != trackIndex) {
            this.selectedTrackIndices[rendererIndex] = trackIndex;
            if (this.state != 1 && this.state != 2 && (rendererState = (renderer = this.renderers[rendererIndex]).getState()) != 0 && rendererState != -1 && renderer.getTrackCount() != 0) {
                boolean isEnabled = rendererState == 2 || rendererState == 3;
                boolean shouldEnable = trackIndex >= 0 && trackIndex < this.trackFormats[rendererIndex].length;
                if (isEnabled) {
                    if (!shouldEnable && renderer == this.rendererMediaClockSource) {
                        this.standaloneMediaClock.setPositionUs(this.rendererMediaClock.getPositionUs());
                    }
                    ensureStopped(renderer);
                    this.enabledRenderers.remove(renderer);
                    renderer.disable();
                }
                if (shouldEnable) {
                    boolean playing = this.playWhenReady && this.state == 4;
                    boolean joining = !isEnabled && playing;
                    renderer.enable(trackIndex, this.positionUs, joining);
                    this.enabledRenderers.add(renderer);
                    if (playing) {
                        renderer.start();
                    }
                    this.handler.sendEmptyMessage(7);
                }
            }
        }
    }

    private void ensureStopped(TrackRenderer renderer) throws ExoPlaybackException {
        if (renderer.getState() == 3) {
            renderer.stop();
        }
    }
}
