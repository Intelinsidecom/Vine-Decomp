package com.google.android.gms.internal;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import com.google.android.gms.ads.formats.NativeAd;

@zzha
/* loaded from: classes.dex */
public class zzco extends NativeAd.Image {
    private final Drawable mDrawable;
    private final Uri mUri;
    private final double zzxx;
    private final zzcn zzyn;

    public zzco(zzcn zzcnVar) {
        com.google.android.gms.dynamic.zzd zzdVarZzdC;
        Uri uri = null;
        this.zzyn = zzcnVar;
        try {
            zzdVarZzdC = this.zzyn.zzdC();
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to get drawable.", e);
        }
        Drawable drawable = zzdVarZzdC != null ? (Drawable) com.google.android.gms.dynamic.zze.zzp(zzdVarZzdC) : null;
        this.mDrawable = drawable;
        try {
            uri = this.zzyn.getUri();
        } catch (RemoteException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to get uri.", e2);
        }
        this.mUri = uri;
        double scale = 1.0d;
        try {
            scale = this.zzyn.getScale();
        } catch (RemoteException e3) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to get scale.", e3);
        }
        this.zzxx = scale;
    }

    @Override // com.google.android.gms.ads.formats.NativeAd.Image
    public Drawable getDrawable() {
        return this.mDrawable;
    }

    @Override // com.google.android.gms.ads.formats.NativeAd.Image
    public double getScale() {
        return this.zzxx;
    }

    @Override // com.google.android.gms.ads.formats.NativeAd.Image
    public Uri getUri() {
        return this.mUri;
    }
}
