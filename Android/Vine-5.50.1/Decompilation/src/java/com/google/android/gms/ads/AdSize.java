package com.google.android.gms.ads;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzl;
import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avutil;
import com.googlecode.javacv.cpp.opencv_core;

/* loaded from: classes.dex */
public final class AdSize {
    private final int zzov;
    private final int zzow;
    private final String zzox;
    public static final AdSize BANNER = new AdSize(avutil.AV_PIX_FMT_YUVJ411P, 50, "320x50_mb");
    public static final AdSize FULL_BANNER = new AdSize(468, 60, "468x60_as");
    public static final AdSize LARGE_BANNER = new AdSize(avutil.AV_PIX_FMT_YUVJ411P, 100, "320x100_as");
    public static final AdSize LEADERBOARD = new AdSize(728, 90, "728x90_as");
    public static final AdSize MEDIUM_RECTANGLE = new AdSize(300, 250, "300x250_as");
    public static final AdSize WIDE_SKYSCRAPER = new AdSize(avcodec.AV_CODEC_ID_CDXL, 600, "160x600_as");
    public static final AdSize SMART_BANNER = new AdSize(-1, -2, "smart_banner");
    public static final AdSize FLUID = new AdSize(-3, -4, "fluid");

    public AdSize(int width, int height) {
        this(width, height, (width == -1 ? "FULL" : String.valueOf(width)) + "x" + (height == -2 ? "AUTO" : String.valueOf(height)) + "_as");
    }

    AdSize(int width, int height, String formatString) {
        if (width < 0 && width != -1 && width != -3) {
            throw new IllegalArgumentException("Invalid width for AdSize: " + width);
        }
        if (height < 0 && height != -2 && height != -4) {
            throw new IllegalArgumentException("Invalid height for AdSize: " + height);
        }
        this.zzov = width;
        this.zzow = height;
        this.zzox = formatString;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AdSize)) {
            return false;
        }
        AdSize adSize = (AdSize) other;
        return this.zzov == adSize.zzov && this.zzow == adSize.zzow && this.zzox.equals(adSize.zzox);
    }

    public int getHeight() {
        return this.zzow;
    }

    public int getHeightInPixels(Context context) {
        switch (this.zzow) {
            case opencv_core.CV_StsNoMem /* -4 */:
            case opencv_core.CV_StsInternal /* -3 */:
                return -1;
            case -2:
                return AdSizeParcel.zzb(context.getResources().getDisplayMetrics());
            default:
                return zzl.zzcN().zzb(context, this.zzow);
        }
    }

    public int getWidth() {
        return this.zzov;
    }

    public int getWidthInPixels(Context context) {
        switch (this.zzov) {
            case opencv_core.CV_StsNoMem /* -4 */:
            case opencv_core.CV_StsInternal /* -3 */:
                return -1;
            case -2:
            default:
                return zzl.zzcN().zzb(context, this.zzov);
            case -1:
                return AdSizeParcel.zza(context.getResources().getDisplayMetrics());
        }
    }

    public int hashCode() {
        return this.zzox.hashCode();
    }

    public boolean isAutoHeight() {
        return this.zzow == -2;
    }

    public boolean isFluid() {
        return this.zzov == -3 && this.zzow == -4;
    }

    public boolean isFullWidth() {
        return this.zzov == -1;
    }

    public String toString() {
        return this.zzox;
    }
}
