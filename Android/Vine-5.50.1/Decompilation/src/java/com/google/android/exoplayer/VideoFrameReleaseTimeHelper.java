package com.google.android.exoplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Choreographer;
import android.view.WindowManager;

@TargetApi(16)
/* loaded from: classes.dex */
public final class VideoFrameReleaseTimeHelper {
    private long adjustedLastFrameTimeNs;
    private long frameCount;
    private boolean haveSync;
    private long lastFramePresentationTimeUs;
    private long pendingAdjustedFrameTimeNs;
    private long syncFramePresentationTimeNs;
    private long syncUnadjustedReleaseTimeNs;
    private final boolean useDefaultDisplayVsync;
    private final long vsyncDurationNs;
    private final long vsyncOffsetNs;
    private final VSyncSampler vsyncSampler;

    public VideoFrameReleaseTimeHelper() {
        this(-1.0f, false);
    }

    public VideoFrameReleaseTimeHelper(Context context) {
        this(getDefaultDisplayRefreshRate(context), true);
    }

    private VideoFrameReleaseTimeHelper(float defaultDisplayRefreshRate, boolean useDefaultDisplayVsync) {
        this.useDefaultDisplayVsync = useDefaultDisplayVsync;
        if (useDefaultDisplayVsync) {
            this.vsyncSampler = VSyncSampler.getInstance();
            this.vsyncDurationNs = (long) (1.0E9d / defaultDisplayRefreshRate);
            this.vsyncOffsetNs = (this.vsyncDurationNs * 80) / 100;
        } else {
            this.vsyncSampler = null;
            this.vsyncDurationNs = -1L;
            this.vsyncOffsetNs = -1L;
        }
    }

    public void enable() {
        this.haveSync = false;
        if (this.useDefaultDisplayVsync) {
            this.vsyncSampler.addObserver();
        }
    }

    public void disable() {
        if (this.useDefaultDisplayVsync) {
            this.vsyncSampler.removeObserver();
        }
    }

    public long adjustReleaseTime(long framePresentationTimeUs, long unadjustedReleaseTimeNs) {
        long framePresentationTimeNs = framePresentationTimeUs * 1000;
        long adjustedFrameTimeNs = framePresentationTimeNs;
        long adjustedReleaseTimeNs = unadjustedReleaseTimeNs;
        if (this.haveSync) {
            if (framePresentationTimeUs != this.lastFramePresentationTimeUs) {
                this.frameCount++;
                this.adjustedLastFrameTimeNs = this.pendingAdjustedFrameTimeNs;
            }
            if (this.frameCount >= 6) {
                long averageFrameDurationNs = (framePresentationTimeNs - this.syncFramePresentationTimeNs) / this.frameCount;
                long candidateAdjustedFrameTimeNs = this.adjustedLastFrameTimeNs + averageFrameDurationNs;
                if (isDriftTooLarge(candidateAdjustedFrameTimeNs, unadjustedReleaseTimeNs)) {
                    this.haveSync = false;
                } else {
                    adjustedFrameTimeNs = candidateAdjustedFrameTimeNs;
                    adjustedReleaseTimeNs = (this.syncUnadjustedReleaseTimeNs + adjustedFrameTimeNs) - this.syncFramePresentationTimeNs;
                }
            } else if (isDriftTooLarge(framePresentationTimeNs, unadjustedReleaseTimeNs)) {
                this.haveSync = false;
            }
        }
        if (!this.haveSync) {
            this.syncFramePresentationTimeNs = framePresentationTimeNs;
            this.syncUnadjustedReleaseTimeNs = unadjustedReleaseTimeNs;
            this.frameCount = 0L;
            this.haveSync = true;
            onSynced();
        }
        this.lastFramePresentationTimeUs = framePresentationTimeUs;
        this.pendingAdjustedFrameTimeNs = adjustedFrameTimeNs;
        if (this.vsyncSampler != null && this.vsyncSampler.sampledVsyncTimeNs != 0) {
            long snappedTimeNs = closestVsync(adjustedReleaseTimeNs, this.vsyncSampler.sampledVsyncTimeNs, this.vsyncDurationNs);
            return snappedTimeNs - this.vsyncOffsetNs;
        }
        return adjustedReleaseTimeNs;
    }

    protected void onSynced() {
    }

    private boolean isDriftTooLarge(long frameTimeNs, long releaseTimeNs) {
        long elapsedFrameTimeNs = frameTimeNs - this.syncFramePresentationTimeNs;
        long elapsedReleaseTimeNs = releaseTimeNs - this.syncUnadjustedReleaseTimeNs;
        return Math.abs(elapsedReleaseTimeNs - elapsedFrameTimeNs) > 20000000;
    }

    private static long closestVsync(long releaseTime, long sampledVsyncTime, long vsyncDuration) {
        long snappedBeforeNs;
        long snappedAfterNs;
        long vsyncCount = (releaseTime - sampledVsyncTime) / vsyncDuration;
        long snappedTimeNs = sampledVsyncTime + (vsyncDuration * vsyncCount);
        if (releaseTime <= snappedTimeNs) {
            snappedBeforeNs = snappedTimeNs - vsyncDuration;
            snappedAfterNs = snappedTimeNs;
        } else {
            snappedBeforeNs = snappedTimeNs;
            snappedAfterNs = snappedTimeNs + vsyncDuration;
        }
        long snappedAfterDiff = snappedAfterNs - releaseTime;
        long snappedBeforeDiff = releaseTime - snappedBeforeNs;
        if (snappedAfterDiff < snappedBeforeDiff) {
            return snappedAfterNs;
        }
        long snappedAfterNs2 = snappedBeforeNs;
        return snappedAfterNs2;
    }

    private static float getDefaultDisplayRefreshRate(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService("window");
        return manager.getDefaultDisplay().getRefreshRate();
    }

    private static final class VSyncSampler implements Handler.Callback, Choreographer.FrameCallback {
        private static final VSyncSampler INSTANCE = new VSyncSampler();
        private Choreographer choreographer;
        private final HandlerThread choreographerOwnerThread = new HandlerThread("ChoreographerOwner:Handler");
        private final Handler handler;
        private int observerCount;
        public volatile long sampledVsyncTimeNs;

        public static VSyncSampler getInstance() {
            return INSTANCE;
        }

        private VSyncSampler() {
            this.choreographerOwnerThread.start();
            this.handler = new Handler(this.choreographerOwnerThread.getLooper(), this);
            this.handler.sendEmptyMessage(0);
        }

        public void addObserver() {
            this.handler.sendEmptyMessage(1);
        }

        public void removeObserver() {
            this.handler.sendEmptyMessage(2);
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long vsyncTimeNs) {
            this.sampledVsyncTimeNs = vsyncTimeNs;
            this.choreographer.postFrameCallbackDelayed(this, 500L);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    createChoreographerInstanceInternal();
                    break;
                case 1:
                    addObserverInternal();
                    break;
                case 2:
                    removeObserverInternal();
                    break;
            }
            return true;
        }

        private void createChoreographerInstanceInternal() {
            this.choreographer = Choreographer.getInstance();
        }

        private void addObserverInternal() {
            this.observerCount++;
            if (this.observerCount == 1) {
                this.choreographer.postFrameCallback(this);
            }
        }

        private void removeObserverInternal() {
            this.observerCount--;
            if (this.observerCount == 0) {
                this.choreographer.removeFrameCallback(this);
                this.sampledVsyncTimeNs = 0L;
            }
        }
    }
}
