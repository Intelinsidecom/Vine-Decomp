package com.google.android.exoplayer.text;

import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;

/* loaded from: classes.dex */
public final class CaptionStyleCompat {
    public static final CaptionStyleCompat DEFAULT = new CaptionStyleCompat(-1, ViewCompat.MEASURED_STATE_MASK, 0, 0, -1, null);
    public final int backgroundColor;
    public final int edgeColor;
    public final int edgeType;
    public final int foregroundColor;
    public final Typeface typeface;
    public final int windowColor;

    public CaptionStyleCompat(int foregroundColor, int backgroundColor, int windowColor, int edgeType, int edgeColor, Typeface typeface) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.windowColor = windowColor;
        this.edgeType = edgeType;
        this.edgeColor = edgeColor;
        this.typeface = typeface;
    }
}
