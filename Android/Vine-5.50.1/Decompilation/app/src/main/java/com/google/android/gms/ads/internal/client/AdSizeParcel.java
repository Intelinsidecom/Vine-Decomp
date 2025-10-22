package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.Parcel;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class AdSizeParcel implements SafeParcelable {
    public static final zzi CREATOR = new zzi();
    public final int height;
    public final int heightPixels;
    public final int versionCode;
    public final int width;
    public final int widthPixels;
    public final String zztV;
    public final boolean zztW;
    public final AdSizeParcel[] zztX;
    public final boolean zztY;
    public final boolean zztZ;
    public boolean zzua;

    public AdSizeParcel() {
        this(5, "interstitial_mb", 0, 0, true, 0, 0, null, false, false, false);
    }

    AdSizeParcel(int versionCode, String formatString, int height, int heightPixels, boolean isInterstitial, int width, int widthPixels, AdSizeParcel[] supportedAdSizes, boolean isNative, boolean isFluid, boolean isNativeExpress) {
        this.versionCode = versionCode;
        this.zztV = formatString;
        this.height = height;
        this.heightPixels = heightPixels;
        this.zztW = isInterstitial;
        this.width = width;
        this.widthPixels = widthPixels;
        this.zztX = supportedAdSizes;
        this.zztY = isNative;
        this.zztZ = isFluid;
        this.zzua = isNativeExpress;
    }

    public AdSizeParcel(Context context, AdSize adSize) {
        this(context, new AdSize[]{adSize});
    }

    public AdSizeParcel(Context context, AdSize[] adSizes) {
        int i;
        AdSize adSize = adSizes[0];
        this.versionCode = 5;
        this.zztW = false;
        this.zztZ = adSize.isFluid();
        if (this.zztZ) {
            this.width = AdSize.BANNER.getWidth();
            this.height = AdSize.BANNER.getHeight();
        } else {
            this.width = adSize.getWidth();
            this.height = adSize.getHeight();
        }
        boolean z = this.width == -1;
        boolean z2 = this.height == -2;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (z) {
            if (zzl.zzcN().zzU(context) && zzl.zzcN().zzV(context)) {
                this.widthPixels = zza(displayMetrics) - zzl.zzcN().zzW(context);
            } else {
                this.widthPixels = zza(displayMetrics);
            }
            double d = this.widthPixels / displayMetrics.density;
            int i2 = (int) d;
            i = d - ((double) ((int) d)) >= 0.01d ? i2 + 1 : i2;
        } else {
            int i3 = this.width;
            this.widthPixels = zzl.zzcN().zza(displayMetrics, this.width);
            i = i3;
        }
        int iZzc = z2 ? zzc(displayMetrics) : this.height;
        this.heightPixels = zzl.zzcN().zza(displayMetrics, iZzc);
        if (z || z2) {
            this.zztV = i + "x" + iZzc + "_as";
        } else if (this.zztZ) {
            this.zztV = "320x50_mb";
        } else {
            this.zztV = adSize.toString();
        }
        if (adSizes.length > 1) {
            this.zztX = new AdSizeParcel[adSizes.length];
            for (int i4 = 0; i4 < adSizes.length; i4++) {
                this.zztX[i4] = new AdSizeParcel(context, adSizes[i4]);
            }
        } else {
            this.zztX = null;
        }
        this.zztY = false;
        this.zzua = false;
    }

    public AdSizeParcel(AdSizeParcel adSize, AdSizeParcel[] supportedAdSizes) {
        this(5, adSize.zztV, adSize.height, adSize.heightPixels, adSize.zztW, adSize.width, adSize.widthPixels, supportedAdSizes, adSize.zztY, adSize.zztZ, adSize.zzua);
    }

    public static int zza(DisplayMetrics displayMetrics) {
        return displayMetrics.widthPixels;
    }

    public static int zzb(DisplayMetrics displayMetrics) {
        return (int) (zzc(displayMetrics) * displayMetrics.density);
    }

    private static int zzc(DisplayMetrics displayMetrics) {
        int i = (int) (displayMetrics.heightPixels / displayMetrics.density);
        if (i <= 400) {
            return 32;
        }
        return i <= 720 ? 50 : 90;
    }

    public static AdSizeParcel zzt(Context context) {
        return new AdSizeParcel(5, "320x50_mb", 0, 0, false, 0, 0, null, true, false, false);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzi.zza(this, out, flags);
    }

    public AdSize zzcL() {
        return com.google.android.gms.ads.zza.zza(this.width, this.height, this.zztV);
    }

    public void zzi(boolean z) {
        this.zzua = z;
    }
}
