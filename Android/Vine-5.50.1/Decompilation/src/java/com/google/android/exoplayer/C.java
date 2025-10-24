package com.google.android.exoplayer;

import android.support.v4.view.PointerIconCompat;
import com.google.android.exoplayer.util.Util;

/* loaded from: classes.dex */
public final class C {
    public static final int CHANNEL_OUT_7POINT1_SURROUND;

    static {
        CHANNEL_OUT_7POINT1_SURROUND = Util.SDK_INT < 23 ? PointerIconCompat.TYPE_GRAB : 6396;
    }
}
