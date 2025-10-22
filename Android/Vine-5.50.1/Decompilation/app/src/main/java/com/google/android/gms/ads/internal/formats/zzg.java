package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.ads.internal.zzn;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzfa;
import com.google.android.gms.internal.zzfb;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzjn;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzg extends zzh {
    private Object zzpK;
    private zzfa zzxN;
    private zzfb zzxO;
    private final zzn zzxP;
    private zzh zzxQ;
    private boolean zzxR;

    private zzg(Context context, zzn zznVar, zzan zzanVar) {
        super(context, zznVar, null, zzanVar, null, null, null);
        this.zzxR = false;
        this.zzpK = new Object();
        this.zzxP = zznVar;
    }

    public zzg(Context context, zzn zznVar, zzan zzanVar, zzfa zzfaVar) {
        this(context, zznVar, zzanVar);
        this.zzxN = zzfaVar;
    }

    public zzg(Context context, zzn zznVar, zzan zzanVar, zzfb zzfbVar) {
        this(context, zznVar, zzanVar);
        this.zzxO = zzfbVar;
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh
    public void recordImpression() {
        zzx.zzcx("recordImpression must be called on the main UI thread.");
        synchronized (this.zzpK) {
            zzn(true);
            if (this.zzxQ != null) {
                this.zzxQ.recordImpression();
            } else {
                try {
                    if (this.zzxN != null && !this.zzxN.getOverrideClickHandling()) {
                        this.zzxN.recordImpression();
                    } else if (this.zzxO != null && !this.zzxO.getOverrideClickHandling()) {
                        this.zzxO.recordImpression();
                    }
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to call recordImpression", e);
                }
            }
            this.zzxP.recordImpression();
        }
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh
    public zzb zza(View.OnClickListener onClickListener) {
        return null;
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh
    public void zza(View view, Map<String, WeakReference<View>> map, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3) {
        zzx.zzcx("performClick must be called on the main UI thread.");
        synchronized (this.zzpK) {
            if (this.zzxQ == null) {
                try {
                    if (this.zzxN != null && !this.zzxN.getOverrideClickHandling()) {
                        this.zzxN.zzc(com.google.android.gms.dynamic.zze.zzB(view));
                    }
                    if (this.zzxO != null && !this.zzxO.getOverrideClickHandling()) {
                        this.zzxN.zzc(com.google.android.gms.dynamic.zze.zzB(view));
                    }
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to call performClick", e);
                }
            }
            this.zzxQ.zza(view, map, jSONObject, jSONObject2, jSONObject3);
            this.zzxP.onAdClicked();
        }
    }

    public void zzc(zzh zzhVar) {
        synchronized (this.zzpK) {
            this.zzxQ = zzhVar;
        }
    }

    public boolean zzdI() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzxR;
        }
        return z;
    }

    public zzh zzdJ() {
        zzh zzhVar;
        synchronized (this.zzpK) {
            zzhVar = this.zzxQ;
        }
        return zzhVar;
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh
    public zzjn zzdK() {
        return null;
    }

    @Override // com.google.android.gms.ads.internal.formats.zzh
    public void zzh(View view) {
        synchronized (this.zzpK) {
            this.zzxR = true;
            try {
                if (this.zzxN != null) {
                    this.zzxN.zzd(com.google.android.gms.dynamic.zze.zzB(view));
                } else if (this.zzxO != null) {
                    this.zzxO.zzd(com.google.android.gms.dynamic.zze.zzB(view));
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to call prepareAd", e);
            }
            this.zzxR = false;
        }
    }
}
