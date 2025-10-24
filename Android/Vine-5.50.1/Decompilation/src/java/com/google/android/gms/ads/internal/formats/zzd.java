package com.google.android.gms.ads.internal.formats;

import android.os.Bundle;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.internal.zzcn;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzha;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzd extends zzcr.zza implements zzh.zza {
    private Bundle mExtras;
    private Object zzpK = new Object();
    private String zzxA;
    private zzcn zzxB;
    private String zzxC;
    private double zzxD;
    private String zzxE;
    private String zzxF;
    private zza zzxG;
    private zzh zzxH;
    private String zzxy;
    private List<zzc> zzxz;

    public zzd(String str, List list, String str2, zzcn zzcnVar, String str3, double d, String str4, String str5, zza zzaVar, Bundle bundle) {
        this.zzxy = str;
        this.zzxz = list;
        this.zzxA = str2;
        this.zzxB = zzcnVar;
        this.zzxC = str3;
        this.zzxD = d;
        this.zzxE = str4;
        this.zzxF = str5;
        this.zzxG = zzaVar;
        this.mExtras = bundle;
    }

    @Override // com.google.android.gms.internal.zzcr
    public void destroy() {
        this.zzxy = null;
        this.zzxz = null;
        this.zzxA = null;
        this.zzxB = null;
        this.zzxC = null;
        this.zzxD = 0.0d;
        this.zzxE = null;
        this.zzxF = null;
        this.zzxG = null;
        this.mExtras = null;
        this.zzpK = null;
        this.zzxH = null;
    }

    @Override // com.google.android.gms.internal.zzcr
    public String getBody() {
        return this.zzxA;
    }

    @Override // com.google.android.gms.internal.zzcr
    public String getCallToAction() {
        return this.zzxC;
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public String getCustomTemplateId() {
        return "";
    }

    @Override // com.google.android.gms.internal.zzcr
    public Bundle getExtras() {
        return this.mExtras;
    }

    @Override // com.google.android.gms.internal.zzcr
    public String getHeadline() {
        return this.zzxy;
    }

    @Override // com.google.android.gms.internal.zzcr
    public List getImages() {
        return this.zzxz;
    }

    @Override // com.google.android.gms.internal.zzcr
    public String getPrice() {
        return this.zzxF;
    }

    @Override // com.google.android.gms.internal.zzcr
    public double getStarRating() {
        return this.zzxD;
    }

    @Override // com.google.android.gms.internal.zzcr
    public String getStore() {
        return this.zzxE;
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public void zzb(zzh zzhVar) {
        synchronized (this.zzpK) {
            this.zzxH = zzhVar;
        }
    }

    @Override // com.google.android.gms.internal.zzcr
    public zzcn zzdD() {
        return this.zzxB;
    }

    @Override // com.google.android.gms.internal.zzcr
    public com.google.android.gms.dynamic.zzd zzdE() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzxH);
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public String zzdF() {
        return "2";
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh.zza
    public zza zzdG() {
        return this.zzxG;
    }
}
