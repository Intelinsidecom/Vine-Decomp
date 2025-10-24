package com.google.android.exoplayer.hls;

/* loaded from: classes.dex */
public abstract class HlsPlaylist {
    public final String baseUri;
    public final int type;

    protected HlsPlaylist(String baseUri, int type) {
        this.baseUri = baseUri;
        this.type = type;
    }
}
