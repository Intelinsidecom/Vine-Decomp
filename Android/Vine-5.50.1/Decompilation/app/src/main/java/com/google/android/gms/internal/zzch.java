package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzch {
    boolean zzwK;
    private String zzxd;
    private zzcf zzxe;
    private zzch zzxf;
    private final List<zzcf> zzxb = new LinkedList();
    private final Map<String, String> zzxc = new LinkedHashMap();
    private final Object zzpK = new Object();

    public zzch(boolean z, String str, String str2) {
        this.zzwK = z;
        this.zzxc.put("action", str);
        this.zzxc.put("ad_format", str2);
    }

    public void zzR(String str) {
        if (this.zzwK) {
            synchronized (this.zzpK) {
                this.zzxd = str;
            }
        }
    }

    public boolean zza(zzcf zzcfVar, long j, String... strArr) {
        synchronized (this.zzpK) {
            for (String str : strArr) {
                this.zzxb.add(new zzcf(j, str, zzcfVar));
            }
        }
        return true;
    }

    public boolean zza(zzcf zzcfVar, String... strArr) {
        if (!this.zzwK || zzcfVar == null) {
            return false;
        }
        return zza(zzcfVar, com.google.android.gms.ads.internal.zzp.zzbB().elapsedRealtime(), strArr);
    }

    public zzcf zzb(long j) {
        if (this.zzwK) {
            return new zzcf(j, null, null);
        }
        return null;
    }

    public void zzc(zzch zzchVar) {
        synchronized (this.zzpK) {
            this.zzxf = zzchVar;
        }
    }

    public void zzd(String str, String str2) {
        zzcb zzcbVarZzgM;
        if (!this.zzwK || TextUtils.isEmpty(str2) || (zzcbVarZzgM = com.google.android.gms.ads.internal.zzp.zzbA().zzgM()) == null) {
            return;
        }
        synchronized (this.zzpK) {
            zzcbVarZzgM.zzP(str).zza(this.zzxc, str, str2);
        }
    }

    public zzcf zzdu() {
        return zzb(com.google.android.gms.ads.internal.zzp.zzbB().elapsedRealtime());
    }

    public void zzdv() {
        synchronized (this.zzpK) {
            this.zzxe = zzdu();
        }
    }

    public String zzdw() {
        String string;
        StringBuilder sb = new StringBuilder();
        synchronized (this.zzpK) {
            for (zzcf zzcfVar : this.zzxb) {
                long time = zzcfVar.getTime();
                String strZzdr = zzcfVar.zzdr();
                zzcf zzcfVarZzds = zzcfVar.zzds();
                if (zzcfVarZzds != null && time > 0) {
                    sb.append(strZzdr).append('.').append(time - zzcfVarZzds.getTime()).append(',');
                }
            }
            this.zzxb.clear();
            if (!TextUtils.isEmpty(this.zzxd)) {
                sb.append(this.zzxd);
            } else if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            string = sb.toString();
        }
        return string;
    }

    public zzcf zzdx() {
        zzcf zzcfVar;
        synchronized (this.zzpK) {
            zzcfVar = this.zzxe;
        }
        return zzcfVar;
    }

    Map<String, String> zzn() {
        Map<String, String> mapZza;
        synchronized (this.zzpK) {
            zzcb zzcbVarZzgM = com.google.android.gms.ads.internal.zzp.zzbA().zzgM();
            mapZza = (zzcbVarZzgM == null || this.zzxf == null) ? this.zzxc : zzcbVarZzgM.zza(this.zzxc, this.zzxf.zzn());
        }
        return mapZza;
    }
}
