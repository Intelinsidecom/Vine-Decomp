package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.internal.zzcn;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzcu extends NativeContentAd {
    private final List<NativeAd.Image> zzyp = new ArrayList();
    private final zzct zzyr;
    private final zzco zzys;

    public zzcu(zzct zzctVar) {
        zzcn zzcnVarZzdH;
        this.zzyr = zzctVar;
        try {
            List images = this.zzyr.getImages();
            if (images != null) {
                Iterator it = images.iterator();
                while (it.hasNext()) {
                    zzcn zzcnVarZzd = zzd(it.next());
                    if (zzcnVarZzd != null) {
                        this.zzyp.add(new zzco(zzcnVarZzd));
                    }
                }
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to get image.", e);
        }
        try {
            zzcnVarZzdH = this.zzyr.zzdH();
        } catch (RemoteException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to get icon.", e2);
        }
        zzco zzcoVar = zzcnVarZzdH != null ? new zzco(zzcnVarZzdH) : null;
        this.zzys = zzcoVar;
    }

    @Override // com.google.android.gms.ads.formats.NativeContentAd
    public CharSequence getAdvertiser() {
        try {
            return this.zzyr.getAdvertiser();
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to get attribution.", e);
            return null;
        }
    }

    @Override // com.google.android.gms.ads.formats.NativeContentAd
    public CharSequence getBody() {
        try {
            return this.zzyr.getBody();
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to get body.", e);
            return null;
        }
    }

    @Override // com.google.android.gms.ads.formats.NativeContentAd
    public CharSequence getCallToAction() {
        try {
            return this.zzyr.getCallToAction();
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to get call to action.", e);
            return null;
        }
    }

    @Override // com.google.android.gms.ads.formats.NativeContentAd
    public CharSequence getHeadline() {
        try {
            return this.zzyr.getHeadline();
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to get headline.", e);
            return null;
        }
    }

    @Override // com.google.android.gms.ads.formats.NativeContentAd
    public List<NativeAd.Image> getImages() {
        return this.zzyp;
    }

    @Override // com.google.android.gms.ads.formats.NativeContentAd
    public NativeAd.Image getLogo() {
        return this.zzys;
    }

    zzcn zzd(Object obj) {
        if (obj instanceof IBinder) {
            return zzcn.zza.zzt((IBinder) obj);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.ads.formats.NativeAd
    /* renamed from: zzdE, reason: merged with bridge method [inline-methods] */
    public com.google.android.gms.dynamic.zzd zzaJ() {
        try {
            return this.zzyr.zzdE();
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed to retrieve native ad engine.", e);
            return null;
        }
    }
}
