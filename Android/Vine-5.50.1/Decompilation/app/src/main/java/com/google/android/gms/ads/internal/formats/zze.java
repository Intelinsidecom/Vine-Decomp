package com.google.android.gms.ads.internal.formats;

import android.os.Bundle;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.internal.zzcn;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzha;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zze extends zzct.zza implements zzh.zza {
    private Bundle mExtras;
    private Object zzpK = new Object();
    private String zzxA;
    private String zzxC;
    private zza zzxG;
    private zzh zzxH;
    private zzcn zzxI;
    private String zzxJ;
    private String zzxy;
    private List<zzc> zzxz;

    public zze(String str, List list, String str2, zzcn zzcnVar, String str3, String str4, zza zzaVar, Bundle bundle) {
        this.zzxy = str;
        this.zzxz = list;
        this.zzxA = str2;
        this.zzxI = zzcnVar;
        this.zzxC = str3;
        this.zzxJ = str4;
        this.zzxG = zzaVar;
        this.mExtras = bundle;
    }

    @Override // com.google.android.gms.internal.zzct
    public void destroy() {
        this.zzxy = null;
        this.zzxz = null;
        this.zzxA = null;
        this.zzxI = null;
        this.zzxC = null;
        this.zzxJ = null;
        this.zzxG = null;
        this.mExtras = null;
        this.zzpK = null;
        this.zzxH = null;
    }

    @Override // com.google.android.gms.internal.zzct
    public String getAdvertiser() {
        return this.zzxJ;
    }

    @Override // com.google.android.gms.internal.zzct
    public String getBody() {
        return this.zzxA;
    }

    @Override // com.google.android.gms.internal.zzct
    public String getCallToAction() {
        return this.zzxC;
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public String getCustomTemplateId() {
        return "";
    }

    @Override // com.google.android.gms.internal.zzct
    public Bundle getExtras() {
        return this.mExtras;
    }

    @Override // com.google.android.gms.internal.zzct
    public String getHeadline() {
        return this.zzxy;
    }

    @Override // com.google.android.gms.internal.zzct
    public List getImages() {
        return this.zzxz;
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public void zzb(zzh zzhVar) {
        synchronized (this.zzpK) {
            this.zzxH = zzhVar;
        }
    }

    @Override // com.google.android.gms.internal.zzct
    public com.google.android.gms.dynamic.zzd zzdE() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzxH);
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public String zzdF() {
        return "1";
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public zza zzdG() {
        return this.zzxG;
    }

    @Override // com.google.android.gms.internal.zzct
    public zzcn zzdH() {
        return this.zzxI;
    }
}
