package com.google.android.gms.ads.internal.formats;

import android.support.v4.util.SimpleArrayMap;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.internal.zzcn;
import com.google.android.gms.internal.zzcv;
import com.google.android.gms.internal.zzha;
import java.util.Arrays;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzf extends zzcv.zza implements zzh.zza {
    private final Object zzpK = new Object();
    private final zza zzxG;
    private zzh zzxH;
    private final String zzxK;
    private final SimpleArrayMap<String, zzc> zzxL;
    private final SimpleArrayMap<String, String> zzxM;

    public zzf(String str, SimpleArrayMap<String, zzc> simpleArrayMap, SimpleArrayMap<String, String> simpleArrayMap2, zza zzaVar) {
        this.zzxK = str;
        this.zzxL = simpleArrayMap;
        this.zzxM = simpleArrayMap2;
        this.zzxG = zzaVar;
    }

    @Override // com.google.android.gms.internal.zzcv
    public List<String> getAvailableAssetNames() {
        int i = 0;
        String[] strArr = new String[this.zzxL.size() + this.zzxM.size()];
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzxL.size(); i3++) {
            strArr[i2] = this.zzxL.keyAt(i3);
            i2++;
        }
        while (i < this.zzxM.size()) {
            strArr[i2] = this.zzxM.keyAt(i);
            i++;
            i2++;
        }
        return Arrays.asList(strArr);
    }

    @Override // com.google.android.gms.internal.zzcv, com.google.android.gms.ads.internal.formats.zzh.zza
    public String getCustomTemplateId() {
        return this.zzxK;
    }

    @Override // com.google.android.gms.internal.zzcv
    public void performClick(String assetName) {
        synchronized (this.zzpK) {
            if (this.zzxH == null) {
                com.google.android.gms.ads.internal.util.client.zzb.e("Attempt to call performClick before ad initialized.");
            } else {
                this.zzxH.zza(assetName, null, null, null);
            }
        }
    }

    @Override // com.google.android.gms.internal.zzcv
    public void recordImpression() {
        synchronized (this.zzpK) {
            if (this.zzxH == null) {
                com.google.android.gms.ads.internal.util.client.zzb.e("Attempt to perform recordImpression before ad initialized.");
            } else {
                this.zzxH.recordImpression();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzcv
    public String zzS(String str) {
        return this.zzxM.get(str);
    }

    @Override // com.google.android.gms.internal.zzcv
    public zzcn zzT(String str) {
        return this.zzxL.get(str);
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public void zzb(zzh zzhVar) {
        synchronized (this.zzpK) {
            this.zzxH = zzhVar;
        }
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public String zzdF() {
        return "3";
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public zza zzdG() {
        return this.zzxG;
    }
}
