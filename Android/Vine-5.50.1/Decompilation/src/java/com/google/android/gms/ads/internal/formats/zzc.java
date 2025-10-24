package com.google.android.gms.ads.internal.formats;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import com.google.android.gms.internal.zzcn;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zzc extends zzcn.zza {
    private final Uri mUri;
    private final Drawable zzxw;
    private final double zzxx;

    public zzc(Drawable drawable, Uri uri, double d) {
        this.zzxw = drawable;
        this.mUri = uri;
        this.zzxx = d;
    }

    @Override // com.google.android.gms.internal.zzcn
    public double getScale() {
        return this.zzxx;
    }

    @Override // com.google.android.gms.internal.zzcn
    public Uri getUri() throws RemoteException {
        return this.mUri;
    }

    @Override // com.google.android.gms.internal.zzcn
    public com.google.android.gms.dynamic.zzd zzdC() throws RemoteException {
        return com.google.android.gms.dynamic.zze.zzB(this.zzxw);
    }
}
