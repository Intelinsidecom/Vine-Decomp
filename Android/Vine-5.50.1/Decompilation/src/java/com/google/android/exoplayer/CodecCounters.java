package com.google.android.exoplayer;

/* loaded from: classes.dex */
public final class CodecCounters {
    public int codecInitCount;
    public int codecReleaseCount;
    public int droppedOutputBufferCount;
    public int maxConsecutiveDroppedOutputBufferCount;
    public int outputBuffersChangedCount;
    public int outputFormatChangedCount;
    public int renderedOutputBufferCount;
    public int skippedOutputBufferCount;

    public synchronized void ensureUpdated() {
    }
}
