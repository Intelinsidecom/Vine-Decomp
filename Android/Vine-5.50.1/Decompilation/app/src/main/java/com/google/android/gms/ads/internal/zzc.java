package com.google.android.gms.ads.internal;

import android.content.Context;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zze;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzci;
import com.google.android.gms.internal.zzcl;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzfs;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.internal.zzjn;

@zzha
/* loaded from: classes.dex */
public abstract class zzc extends zzb implements zzg, zzfs {
    public zzc(Context context, AdSizeParcel adSizeParcel, String str, zzew zzewVar, VersionInfoParcel versionInfoParcel, zzd zzdVar) {
        super(context, adSizeParcel, str, zzewVar, versionInfoParcel, zzdVar);
    }

    @Override // com.google.android.gms.ads.internal.zzg
    public void recordClick() {
        onAdClicked();
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.zzg
    public void recordImpression() {
        zza(this.zzoZ.zzqW, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected zzjn zza(zzie.zza zzaVar, zze zzeVar) {
        zzjn zzjnVar;
        View nextView = this.zzoZ.zzqS.getNextView();
        if (nextView instanceof zzjn) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Reusing webview...");
            zzjn zzjnVar2 = (zzjn) nextView;
            zzjnVar2.zza(this.zzoZ.context, this.zzoZ.zzqV, this.zzoU);
            zzjnVar = zzjnVar2;
        } else {
            if (nextView != 0) {
                this.zzoZ.zzqS.removeView(nextView);
            }
            zzjn zzjnVarZza = zzp.zzby().zza(this.zzoZ.context, this.zzoZ.zzqV, false, false, this.zzoZ.zzqQ, this.zzoZ.zzqR, this.zzoU, this.zzpc);
            if (this.zzoZ.zzqV.zztX == null) {
                zzb(zzjnVarZza.getView());
            }
            zzjnVar = zzjnVarZza;
        }
        zzjnVar.zzhC().zzb(this, this, this, this, false, this, null, zzeVar, this);
        zzjnVar.zzaJ(zzaVar.zzJK.zzGF);
        return zzjnVar;
    }

    @Override // com.google.android.gms.internal.zzfs
    public void zza(int i, int i2, int i3, int i4) {
        zzaU();
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void zza(zzcl zzclVar) {
        zzx.zzcx("setOnCustomRenderedAdLoadedListener must be called on the main UI thread.");
        this.zzoZ.zzrk = zzclVar;
    }

    @Override // com.google.android.gms.ads.internal.zza
    protected void zza(final zzie.zza zzaVar, final zzch zzchVar) {
        if (zzaVar.errorCode != -2) {
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.zzc.1
                @Override // java.lang.Runnable
                public void run() {
                    zzc.this.zzb(new zzie(zzaVar, null, null, null, null, null, null));
                }
            });
            return;
        }
        if (zzaVar.zzqV != null) {
            this.zzoZ.zzqV = zzaVar.zzqV;
        }
        if (!zzaVar.zzJL.zzGN) {
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.zzc.2
                @Override // java.lang.Runnable
                public void run() {
                    if (zzaVar.zzJL.zzGW && zzc.this.zzoZ.zzrk != null) {
                        zzci zzciVar = new zzci(zzc.this, zzaVar.zzJL.zzDE != null ? zzp.zzbx().zzaz(zzaVar.zzJL.zzDE) : null, zzaVar.zzJL.body);
                        zzc.this.zzoZ.zzrp = 1;
                        try {
                            zzc.this.zzoZ.zzrk.zza(zzciVar);
                            return;
                        } catch (RemoteException e) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call the onCustomRenderedAdLoadedListener.", e);
                        }
                    }
                    final zze zzeVar = new zze();
                    zzjn zzjnVarZza = zzc.this.zza(zzaVar, zzeVar);
                    zzeVar.zza(new zze.zzb(zzaVar, zzjnVarZza));
                    zzjnVarZza.setOnTouchListener(new View.OnTouchListener() { // from class: com.google.android.gms.ads.internal.zzc.2.1
                        @Override // android.view.View.OnTouchListener
                        public boolean onTouch(View v, MotionEvent event) {
                            zzeVar.recordClick();
                            return false;
                        }
                    });
                    zzjnVarZza.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.gms.ads.internal.zzc.2.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            zzeVar.recordClick();
                        }
                    });
                    zzc.this.zzoZ.zzrp = 0;
                    zzc.this.zzoZ.zzqU = zzp.zzbw().zza(zzc.this.zzoZ.context, zzc.this, zzaVar, zzc.this.zzoZ.zzqQ, zzjnVarZza, zzc.this.zzpd, zzc.this, zzchVar);
                }
            });
            return;
        }
        this.zzoZ.zzrp = 0;
        this.zzoZ.zzqU = zzp.zzbw().zza(this.zzoZ.context, this, zzaVar, this.zzoZ.zzqQ, null, this.zzpd, this, zzchVar);
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.zza
    protected boolean zza(zzie zzieVar, zzie zzieVar2) {
        if (this.zzoZ.zzbQ() && this.zzoZ.zzqS != null) {
            this.zzoZ.zzqS.zzbW().zzaC(zzieVar2.zzGS);
        }
        return super.zza(zzieVar, zzieVar2);
    }

    @Override // com.google.android.gms.internal.zzfs
    public void zzbe() {
        zzaS();
    }

    @Override // com.google.android.gms.ads.internal.zzg
    public void zzc(View view) {
        this.zzoZ.zzro = view;
        zzb(new zzie(this.zzoZ.zzqX, null, null, null, null, null, null));
    }
}
