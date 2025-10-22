package com.google.ads;

import android.content.Context;
import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avutil;

@Deprecated
/* loaded from: classes.dex */
public final class AdSize {
    public static final int AUTO_HEIGHT = -2;
    public static final int FULL_WIDTH = -1;
    public static final int LANDSCAPE_AD_HEIGHT = 32;
    public static final int LARGE_AD_HEIGHT = 90;
    public static final int PORTRAIT_AD_HEIGHT = 50;
    private final com.google.android.gms.ads.AdSize zzaJ;
    public static final AdSize SMART_BANNER = new AdSize(-1, -2, "mb");
    public static final AdSize BANNER = new AdSize(avutil.AV_PIX_FMT_YUVJ411P, 50, "mb");
    public static final AdSize IAB_MRECT = new AdSize(300, 250, "as");
    public static final AdSize IAB_BANNER = new AdSize(468, 60, "as");
    public static final AdSize IAB_LEADERBOARD = new AdSize(728, 90, "as");
    public static final AdSize IAB_WIDE_SKYSCRAPER = new AdSize(avcodec.AV_CODEC_ID_CDXL, 600, "as");

    public AdSize(int width, int height) {
        this(new com.google.android.gms.ads.AdSize(width, height));
    }

    private AdSize(int width, int height, String type) {
        this(new com.google.android.gms.ads.AdSize(width, height));
    }

    public AdSize(com.google.android.gms.ads.AdSize adSize) {
        this.zzaJ = adSize;
    }

    public boolean equals(Object other) {
        if (other instanceof AdSize) {
            return this.zzaJ.equals(((AdSize) other).zzaJ);
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.google.ads.AdSize findBestSize(com.google.ads.AdSize... r12) {
        /*
            r11 = this;
            r3 = 0
            r10 = 1065353216(0x3f800000, float:1.0)
            if (r12 != 0) goto L6
        L5:
            return r3
        L6:
            r1 = 0
            int r5 = r11.getWidth()
            int r6 = r11.getHeight()
            int r7 = r12.length
            r0 = 0
            r4 = r0
        L12:
            if (r4 >= r7) goto L5
            r2 = r12[r4]
            int r0 = r2.getWidth()
            int r8 = r2.getHeight()
            boolean r9 = r11.isSizeAppropriate(r0, r8)
            if (r9 == 0) goto L3b
            int r0 = r0 * r8
            float r0 = (float) r0
            int r8 = r5 * r6
            float r8 = (float) r8
            float r0 = r0 / r8
            int r8 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
            if (r8 <= 0) goto L30
            float r0 = r10 / r0
        L30:
            int r8 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r8 <= 0) goto L3b
            r1 = r2
        L35:
            int r2 = r4 + 1
            r4 = r2
            r3 = r1
            r1 = r0
            goto L12
        L3b:
            r0 = r1
            r1 = r3
            goto L35
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.ads.AdSize.findBestSize(com.google.ads.AdSize[]):com.google.ads.AdSize");
    }

    public int getHeight() {
        return this.zzaJ.getHeight();
    }

    public int getHeightInPixels(Context context) {
        return this.zzaJ.getHeightInPixels(context);
    }

    public int getWidth() {
        return this.zzaJ.getWidth();
    }

    public int getWidthInPixels(Context context) {
        return this.zzaJ.getWidthInPixels(context);
    }

    public int hashCode() {
        return this.zzaJ.hashCode();
    }

    public boolean isAutoHeight() {
        return this.zzaJ.isAutoHeight();
    }

    public boolean isCustomAdSize() {
        return false;
    }

    public boolean isFullWidth() {
        return this.zzaJ.isFullWidth();
    }

    public boolean isSizeAppropriate(int width, int height) {
        int width2 = getWidth();
        int height2 = getHeight();
        return ((float) width) <= ((float) width2) * 1.25f && ((float) width) >= ((float) width2) * 0.8f && ((float) height) <= ((float) height2) * 1.25f && ((float) height) >= ((float) height2) * 0.8f;
    }

    public String toString() {
        return this.zzaJ.toString();
    }
}
