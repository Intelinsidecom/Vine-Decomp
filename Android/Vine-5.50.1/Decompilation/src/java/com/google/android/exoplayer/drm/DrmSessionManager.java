package com.google.android.exoplayer.drm;

import android.annotation.TargetApi;
import android.media.MediaCrypto;

@TargetApi(16)
/* loaded from: classes.dex */
public interface DrmSessionManager {
    void close();

    Exception getError();

    MediaCrypto getMediaCrypto();

    int getState();

    void open(DrmInitData drmInitData);

    boolean requiresSecureDecoderComponent(String str);
}
