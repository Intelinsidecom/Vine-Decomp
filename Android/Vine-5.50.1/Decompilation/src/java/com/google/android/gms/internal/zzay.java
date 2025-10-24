package com.google.android.gms.internal;

import android.content.Context;
import android.view.View;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzaz;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

@zzha
/* loaded from: classes.dex */
public class zzay implements zzba {
    private final VersionInfoParcel zzpI;
    private final Object zzpK = new Object();
    private final WeakHashMap<zzie, zzaz> zzrG = new WeakHashMap<>();
    private final ArrayList<zzaz> zzrH = new ArrayList<>();
    private final Context zzrI;
    private final zzei zzrJ;

    public zzay(Context context, VersionInfoParcel versionInfoParcel, zzei zzeiVar) {
        this.zzrI = context.getApplicationContext();
        this.zzpI = versionInfoParcel;
        this.zzrJ = zzeiVar;
    }

    public zzaz zza(AdSizeParcel adSizeParcel, zzie zzieVar) {
        return zza(adSizeParcel, zzieVar, zzieVar.zzDC.getView());
    }

    public zzaz zza(AdSizeParcel adSizeParcel, zzie zzieVar, View view) {
        return zza(adSizeParcel, zzieVar, new zzaz.zzd(view, zzieVar));
    }

    public zzaz zza(AdSizeParcel adSizeParcel, zzie zzieVar, com.google.android.gms.ads.internal.formats.zzh zzhVar) {
        return zza(adSizeParcel, zzieVar, new zzaz.zza(zzhVar));
    }

    public zzaz zza(AdSizeParcel adSizeParcel, zzie zzieVar, zzbh zzbhVar) {
        zzaz zzazVar;
        synchronized (this.zzpK) {
            if (zzd(zzieVar)) {
                zzazVar = this.zzrG.get(zzieVar);
            } else {
                zzazVar = new zzaz(this.zzrI, adSizeParcel, zzieVar, this.zzpI, zzbhVar, this.zzrJ);
                zzazVar.zza(this);
                this.zzrG.put(zzieVar, zzazVar);
                this.zzrH.add(zzazVar);
            }
        }
        return zzazVar;
    }

    @Override // com.google.android.gms.internal.zzba
    public void zza(zzaz zzazVar) {
        synchronized (this.zzpK) {
            if (!zzazVar.zzch()) {
                this.zzrH.remove(zzazVar);
                Iterator<Map.Entry<zzie, zzaz>> it = this.zzrG.entrySet().iterator();
                while (it.hasNext()) {
                    if (it.next().getValue() == zzazVar) {
                        it.remove();
                    }
                }
            }
        }
    }

    public boolean zzd(zzie zzieVar) {
        boolean z;
        synchronized (this.zzpK) {
            zzaz zzazVar = this.zzrG.get(zzieVar);
            z = zzazVar != null && zzazVar.zzch();
        }
        return z;
    }

    public void zze(zzie zzieVar) {
        synchronized (this.zzpK) {
            zzaz zzazVar = this.zzrG.get(zzieVar);
            if (zzazVar != null) {
                zzazVar.zzcf();
            }
        }
    }

    public void zzf(zzie zzieVar) {
        synchronized (this.zzpK) {
            zzaz zzazVar = this.zzrG.get(zzieVar);
            if (zzazVar != null) {
                zzazVar.stop();
            }
        }
    }

    public void zzg(zzie zzieVar) {
        synchronized (this.zzpK) {
            zzaz zzazVar = this.zzrG.get(zzieVar);
            if (zzazVar != null) {
                zzazVar.pause();
            }
        }
    }

    public void zzh(zzie zzieVar) {
        synchronized (this.zzpK) {
            zzaz zzazVar = this.zzrG.get(zzieVar);
            if (zzazVar != null) {
                zzazVar.resume();
            }
        }
    }
}
