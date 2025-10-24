package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.util.Assertions;

/* loaded from: classes.dex */
public abstract class Chunk implements Loader.Loadable {
    protected final DataSource dataSource;
    public final DataSpec dataSpec;
    public final Format format;
    public final int parentId;
    public final int trigger;
    public final int type;

    public abstract long bytesLoaded();

    public Chunk(DataSource dataSource, DataSpec dataSpec, int type, int trigger, Format format, int parentId) {
        this.dataSource = (DataSource) Assertions.checkNotNull(dataSource);
        this.dataSpec = (DataSpec) Assertions.checkNotNull(dataSpec);
        this.type = type;
        this.trigger = trigger;
        this.format = format;
        this.parentId = parentId;
    }
}
