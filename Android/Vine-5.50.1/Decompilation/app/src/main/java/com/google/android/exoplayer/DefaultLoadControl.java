package com.google.android.exoplayer;

import android.os.Handler;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.NetworkLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public final class DefaultLoadControl implements LoadControl {
    private final Allocator allocator;
    private int bufferState;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private boolean fillingBuffers;
    private final float highBufferLoad;
    private final long highWatermarkUs;
    private final HashMap<Object, LoaderState> loaderStates;
    private final List<Object> loaders;
    private final float lowBufferLoad;
    private final long lowWatermarkUs;
    private long maxLoadStartPositionUs;
    private boolean streamingPrioritySet;
    private int targetBufferSize;

    public interface EventListener {
        void onLoadingChanged(boolean z);
    }

    public DefaultLoadControl(Allocator allocator) {
        this(allocator, null, null);
    }

    public DefaultLoadControl(Allocator allocator, Handler eventHandler, EventListener eventListener) {
        this(allocator, eventHandler, eventListener, 15000, 30000, 0.2f, 0.8f);
    }

    public DefaultLoadControl(Allocator allocator, Handler eventHandler, EventListener eventListener, int lowWatermarkMs, int highWatermarkMs, float lowBufferLoad, float highBufferLoad) {
        this.allocator = allocator;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.loaders = new ArrayList();
        this.loaderStates = new HashMap<>();
        this.lowWatermarkUs = lowWatermarkMs * 1000;
        this.highWatermarkUs = highWatermarkMs * 1000;
        this.lowBufferLoad = lowBufferLoad;
        this.highBufferLoad = highBufferLoad;
    }

    @Override // com.google.android.exoplayer.LoadControl
    public void register(Object loader, int bufferSizeContribution) {
        this.loaders.add(loader);
        this.loaderStates.put(loader, new LoaderState(bufferSizeContribution));
        this.targetBufferSize += bufferSizeContribution;
    }

    @Override // com.google.android.exoplayer.LoadControl
    public void unregister(Object loader) {
        this.loaders.remove(loader);
        LoaderState state = this.loaderStates.remove(loader);
        this.targetBufferSize -= state.bufferSizeContribution;
        updateControlState();
    }

    @Override // com.google.android.exoplayer.LoadControl
    public void trimAllocator() {
        this.allocator.trim(this.targetBufferSize);
    }

    @Override // com.google.android.exoplayer.LoadControl
    public Allocator getAllocator() {
        return this.allocator;
    }

    @Override // com.google.android.exoplayer.LoadControl
    public boolean update(Object loader, long playbackPositionUs, long nextLoadPositionUs, boolean loading) {
        int loaderBufferState = getLoaderBufferState(playbackPositionUs, nextLoadPositionUs);
        LoaderState loaderState = this.loaderStates.get(loader);
        boolean loaderStateChanged = (loaderState.bufferState == loaderBufferState && loaderState.nextLoadPositionUs == nextLoadPositionUs && loaderState.loading == loading) ? false : true;
        if (loaderStateChanged) {
            loaderState.bufferState = loaderBufferState;
            loaderState.nextLoadPositionUs = nextLoadPositionUs;
            loaderState.loading = loading;
        }
        int currentBufferSize = this.allocator.getTotalBytesAllocated();
        int bufferState = getBufferState(currentBufferSize);
        boolean bufferStateChanged = this.bufferState != bufferState;
        if (bufferStateChanged) {
            this.bufferState = bufferState;
        }
        if (loaderStateChanged || bufferStateChanged) {
            updateControlState();
        }
        return currentBufferSize < this.targetBufferSize && nextLoadPositionUs != -1 && nextLoadPositionUs <= this.maxLoadStartPositionUs;
    }

    private int getLoaderBufferState(long playbackPositionUs, long nextLoadPositionUs) {
        if (nextLoadPositionUs == -1) {
            return 0;
        }
        long timeUntilNextLoadPosition = nextLoadPositionUs - playbackPositionUs;
        if (timeUntilNextLoadPosition <= this.highWatermarkUs) {
            return timeUntilNextLoadPosition < this.lowWatermarkUs ? 2 : 1;
        }
        return 0;
    }

    private int getBufferState(int currentBufferSize) {
        float bufferLoad = currentBufferSize / this.targetBufferSize;
        if (bufferLoad > this.highBufferLoad) {
            return 0;
        }
        return bufferLoad < this.lowBufferLoad ? 2 : 1;
    }

    private void updateControlState() {
        boolean loading = false;
        boolean haveNextLoadPosition = false;
        int highestState = this.bufferState;
        for (int i = 0; i < this.loaders.size(); i++) {
            LoaderState loaderState = this.loaderStates.get(this.loaders.get(i));
            loading |= loaderState.loading;
            haveNextLoadPosition |= loaderState.nextLoadPositionUs != -1;
            highestState = Math.max(highestState, loaderState.bufferState);
        }
        this.fillingBuffers = !this.loaders.isEmpty() && (loading || haveNextLoadPosition) && (highestState == 2 || (highestState == 1 && this.fillingBuffers));
        if (this.fillingBuffers && !this.streamingPrioritySet) {
            NetworkLock.instance.add(0);
            this.streamingPrioritySet = true;
            notifyLoadingChanged(true);
        } else if (!this.fillingBuffers && this.streamingPrioritySet && !loading) {
            NetworkLock.instance.remove(0);
            this.streamingPrioritySet = false;
            notifyLoadingChanged(false);
        }
        this.maxLoadStartPositionUs = -1L;
        if (this.fillingBuffers) {
            for (int i2 = 0; i2 < this.loaders.size(); i2++) {
                Object loader = this.loaders.get(i2);
                long loaderTime = this.loaderStates.get(loader).nextLoadPositionUs;
                if (loaderTime != -1 && (this.maxLoadStartPositionUs == -1 || loaderTime < this.maxLoadStartPositionUs)) {
                    this.maxLoadStartPositionUs = loaderTime;
                }
            }
        }
    }

    private void notifyLoadingChanged(final boolean loading) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer.DefaultLoadControl.1
                @Override // java.lang.Runnable
                public void run() {
                    DefaultLoadControl.this.eventListener.onLoadingChanged(loading);
                }
            });
        }
    }

    private static class LoaderState {
        public final int bufferSizeContribution;
        public int bufferState = 0;
        public boolean loading = false;
        public long nextLoadPositionUs = -1;

        public LoaderState(int bufferSizeContribution) {
            this.bufferSizeContribution = bufferSizeContribution;
        }
    }
}
