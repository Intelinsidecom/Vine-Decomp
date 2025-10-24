package com.google.android.gms.ads.internal;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzp;
import com.google.android.gms.ads.internal.client.zzv;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzcx;
import com.google.android.gms.internal.zzcy;
import com.google.android.gms.internal.zzcz;
import com.google.android.gms.internal.zzda;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzip;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzi extends zzp.zza {
    private final Context mContext;
    private final zzcx zzpA;
    private final zzcy zzpB;
    private final SimpleArrayMap<String, zzda> zzpC;
    private final SimpleArrayMap<String, zzcz> zzpD;
    private final NativeAdOptionsParcel zzpE;
    private final zzv zzpG;
    private final String zzpH;
    private final VersionInfoParcel zzpI;
    private WeakReference<zzn> zzpJ;
    private final zzew zzpd;
    private final com.google.android.gms.ads.internal.client.zzo zzpz;
    private final Object zzpK = new Object();
    private final List<String> zzpF = zzbk();

    zzi(Context context, String str, zzew zzewVar, VersionInfoParcel versionInfoParcel, com.google.android.gms.ads.internal.client.zzo zzoVar, zzcx zzcxVar, zzcy zzcyVar, SimpleArrayMap<String, zzda> simpleArrayMap, SimpleArrayMap<String, zzcz> simpleArrayMap2, NativeAdOptionsParcel nativeAdOptionsParcel, zzv zzvVar) {
        this.mContext = context;
        this.zzpH = str;
        this.zzpd = zzewVar;
        this.zzpI = versionInfoParcel;
        this.zzpz = zzoVar;
        this.zzpB = zzcyVar;
        this.zzpA = zzcxVar;
        this.zzpC = simpleArrayMap;
        this.zzpD = simpleArrayMap2;
        this.zzpE = nativeAdOptionsParcel;
        this.zzpG = zzvVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> zzbk() {
        ArrayList arrayList = new ArrayList();
        if (this.zzpB != null) {
            arrayList.add("1");
        }
        if (this.zzpA != null) {
            arrayList.add("2");
        }
        if (this.zzpC.size() > 0) {
            arrayList.add("3");
        }
        return arrayList;
    }

    @Override // com.google.android.gms.ads.internal.client.zzp
    public String getMediationAdapterClassName() {
        synchronized (this.zzpK) {
            if (this.zzpJ == null) {
                return null;
            }
            zzn zznVar = this.zzpJ.get();
            return zznVar != null ? zznVar.getMediationAdapterClassName() : null;
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzp
    public boolean isLoading() {
        synchronized (this.zzpK) {
            if (this.zzpJ == null) {
                return false;
            }
            zzn zznVar = this.zzpJ.get();
            return zznVar != null ? zznVar.isLoading() : false;
        }
    }

    protected void runOnUiThread(Runnable runnable) {
        zzip.zzKO.post(runnable);
    }

    protected zzn zzbl() {
        return new zzn(this.mContext, AdSizeParcel.zzt(this.mContext), this.zzpH, this.zzpd, this.zzpI);
    }

    @Override // com.google.android.gms.ads.internal.client.zzp
    public void zzf(final AdRequestParcel adRequestParcel) {
        runOnUiThread(new Runnable() { // from class: com.google.android.gms.ads.internal.zzi.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (zzi.this.zzpK) {
                    zzn zznVarZzbl = zzi.this.zzbl();
                    zzi.this.zzpJ = new WeakReference(zznVarZzbl);
                    zznVarZzbl.zzb(zzi.this.zzpA);
                    zznVarZzbl.zzb(zzi.this.zzpB);
                    zznVarZzbl.zza(zzi.this.zzpC);
                    zznVarZzbl.zza(zzi.this.zzpz);
                    zznVarZzbl.zzb(zzi.this.zzpD);
                    zznVarZzbl.zza(zzi.this.zzbk());
                    zznVarZzbl.zzb(zzi.this.zzpE);
                    zznVarZzbl.zza(zzi.this.zzpG);
                    zznVarZzbl.zzb(adRequestParcel);
                }
            }
        });
    }
}
