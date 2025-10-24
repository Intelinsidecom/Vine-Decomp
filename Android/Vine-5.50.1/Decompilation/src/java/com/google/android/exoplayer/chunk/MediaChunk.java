package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.Assertions;

/* loaded from: classes.dex */
public abstract class MediaChunk extends Chunk {
    public final int chunkIndex;
    public final long endTimeUs;
    public final long startTimeUs;

    public MediaChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, long startTimeUs, long endTimeUs, int chunkIndex) {
        this(dataSource, dataSpec, trigger, format, startTimeUs, endTimeUs, chunkIndex, -1);
    }

    public MediaChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, long startTimeUs, long endTimeUs, int chunkIndex, int parentId) {
        super(dataSource, dataSpec, 1, trigger, format, parentId);
        Assertions.checkNotNull(format);
        this.startTimeUs = startTimeUs;
        this.endTimeUs = endTimeUs;
        this.chunkIndex = chunkIndex;
    }
}
