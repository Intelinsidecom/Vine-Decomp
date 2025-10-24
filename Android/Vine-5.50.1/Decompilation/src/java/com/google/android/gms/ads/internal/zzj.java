package com.google.android.gms.ads.internal;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzv;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzcx;
import com.google.android.gms.internal.zzcy;
import com.google.android.gms.internal.zzcz;
import com.google.android.gms.internal.zzda;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zzj extends zzq.zza {
    private final Context mContext;
    private NativeAdOptionsParcel zzpE;
    private zzv zzpG;
    private final String zzpH;
    private final VersionInfoParcel zzpI;
    private zzcx zzpN;
    private zzcy zzpO;
    private final zzew zzpd;
    private com.google.android.gms.ads.internal.client.zzo zzpz;
    private SimpleArrayMap<String, zzda> zzpQ = new SimpleArrayMap<>();
    private SimpleArrayMap<String, zzcz> zzpP = new SimpleArrayMap<>();

    public zzj(Context context, String str, zzew zzewVar, VersionInfoParcel versionInfoParcel) {
        this.mContext = context;
        this.zzpH = str;
        this.zzpd = zzewVar;
        this.zzpI = versionInfoParcel;
    }

    @Override // com.google.android.gms.ads.internal.client.zzq
    public void zza(NativeAdOptionsParcel nativeAdOptionsParcel) {
        this.zzpE = nativeAdOptionsParcel;
    }

    @Override // com.google.android.gms.ads.internal.client.zzq
    public void zza(zzcx zzcxVar) {
        this.zzpN = zzcxVar;
    }

    @Override // com.google.android.gms.ads.internal.client.zzq
    public void zza(zzcy zzcyVar) {
        this.zzpO = zzcyVar;
    }

    @Override // com.google.android.gms.ads.internal.client.zzq
    public void zza(String str, zzda zzdaVar, zzcz zzczVar) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Custom template ID for native custom template ad is empty. Please provide a valid template id.");
        }
        this.zzpQ.put(str, zzdaVar);
        this.zzpP.put(str, zzczVar);
    }

    @Override // com.google.android.gms.ads.internal.client.zzq
    public void zzb(com.google.android.gms.ads.internal.client.zzo zzoVar) {
        this.zzpz = zzoVar;
    }

    @Override // com.google.android.gms.ads.internal.client.zzq
    public void zzb(zzv zzvVar) {
        this.zzpG = zzvVar;
    }

    @Override // com.google.android.gms.ads.internal.client.zzq
    public com.google.android.gms.ads.internal.client.zzp zzbm() {
        return new zzi(this.mContext, this.zzpH, this.zzpd, this.zzpI, this.zzpz, this.zzpN, this.zzpO, this.zzpQ, this.zzpP, this.zzpE, this.zzpG);
    }
}
