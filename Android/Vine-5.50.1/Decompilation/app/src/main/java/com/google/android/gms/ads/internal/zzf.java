package com.google.android.gms.ads.internal;

import android.content.Context;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzjn;
import com.google.android.gms.internal.zzjo;

@zzha
/* loaded from: classes.dex */
public class zzf extends zzc {
    private boolean zzpt;

    public zzf(Context context, AdSizeParcel adSizeParcel, String str, zzew zzewVar, VersionInfoParcel versionInfoParcel, zzd zzdVar) {
        super(context, adSizeParcel, str, zzewVar, versionInfoParcel, zzdVar);
    }

    private AdSizeParcel zzb(zzie.zza zzaVar) {
        AdSize adSizeZzcL;
        if (zzaVar.zzJL.zztZ) {
            return this.zzoZ.zzqV;
        }
        String str = zzaVar.zzJL.zzGQ;
        if (str != null) {
            String[] strArrSplit = str.split("[xX]");
            strArrSplit[0] = strArrSplit[0].trim();
            strArrSplit[1] = strArrSplit[1].trim();
            adSizeZzcL = new AdSize(Integer.parseInt(strArrSplit[0]), Integer.parseInt(strArrSplit[1]));
        } else {
            adSizeZzcL = this.zzoZ.zzqV.zzcL();
        }
        return new AdSizeParcel(this.zzoZ.context, adSizeZzcL);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean zzb(zzie zzieVar, zzie zzieVar2) {
        boolean z;
        if (zzieVar2.zzGN) {
            try {
                com.google.android.gms.dynamic.zzd view = zzieVar2.zzBq.getView();
                if (view == null) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("View in mediation adapter is null.");
                    z = false;
                } else {
                    View view2 = (View) com.google.android.gms.dynamic.zze.zzp(view);
                    View nextView = this.zzoZ.zzqS.getNextView();
                    if (nextView != 0) {
                        if (nextView instanceof zzjn) {
                            ((zzjn) nextView).destroy();
                        }
                        this.zzoZ.zzqS.removeView(nextView);
                    }
                    try {
                        zzb(view2);
                    } catch (Throwable th) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not add mediation view to view hierarchy.", th);
                        z = false;
                    }
                }
                return z;
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not get View from mediation adapter.", e);
                return false;
            }
        }
        if (zzieVar2.zzJG != null && zzieVar2.zzDC != null) {
            zzieVar2.zzDC.zza(zzieVar2.zzJG);
            this.zzoZ.zzqS.removeAllViews();
            this.zzoZ.zzqS.setMinimumWidth(zzieVar2.zzJG.widthPixels);
            this.zzoZ.zzqS.setMinimumHeight(zzieVar2.zzJG.heightPixels);
            zzb(zzieVar2.zzDC.getView());
        }
        if (this.zzoZ.zzqS.getChildCount() > 1) {
            this.zzoZ.zzqS.showNext();
        }
        if (zzieVar != null) {
            View nextView2 = this.zzoZ.zzqS.getNextView();
            if (nextView2 instanceof zzjn) {
                ((zzjn) nextView2).zza(this.zzoZ.context, this.zzoZ.zzqV, this.zzoU);
            } else if (nextView2 != 0) {
                this.zzoZ.zzqS.removeView(nextView2);
            }
            this.zzoZ.zzbP();
        }
        this.zzoZ.zzqS.setVisibility(0);
        z = true;
        return z;
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void setManualImpressionsEnabled(boolean enabled) {
        zzx.zzcx("setManualImpressionsEnabled must be called from the main thread.");
        this.zzpt = enabled;
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.client.zzs
    public void showInterstitial() {
        throw new IllegalStateException("Interstitial is NOT supported by BannerAdManager.");
    }

    @Override // com.google.android.gms.ads.internal.zzc
    protected zzjn zza(zzie.zza zzaVar, zze zzeVar) {
        if (this.zzoZ.zzqV.zztZ) {
            this.zzoZ.zzqV = zzb(zzaVar);
        }
        return super.zza(zzaVar, zzeVar);
    }

    @Override // com.google.android.gms.ads.internal.zzc, com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.zza
    public boolean zza(zzie zzieVar, final zzie zzieVar2) {
        if (!super.zza(zzieVar, zzieVar2)) {
            return false;
        }
        if (this.zzoZ.zzbQ() && !zzb(zzieVar, zzieVar2)) {
            zzf(0);
            return false;
        }
        zza(zzieVar2, false);
        if (this.zzoZ.zzbQ()) {
            if (zzieVar2.zzDC != null) {
                if (zzieVar2.zzJE != null) {
                    this.zzpb.zza(this.zzoZ.zzqV, zzieVar2);
                }
                if (zzieVar2.zzcb()) {
                    this.zzpb.zza(this.zzoZ.zzqV, zzieVar2).zza(zzieVar2.zzDC);
                } else {
                    zzieVar2.zzDC.zzhC().zza(new zzjo.zzb() { // from class: com.google.android.gms.ads.internal.zzf.1
                        @Override // com.google.android.gms.internal.zzjo.zzb
                        public void zzbh() {
                            zzf.this.zzpb.zza(zzf.this.zzoZ.zzqV, zzieVar2).zza(zzieVar2.zzDC);
                        }
                    });
                }
            }
        } else if (this.zzoZ.zzro != null && zzieVar2.zzJE != null) {
            this.zzpb.zza(this.zzoZ.zzqV, zzieVar2, this.zzoZ.zzro);
        }
        return true;
    }

    @Override // com.google.android.gms.ads.internal.zzb
    protected boolean zzaW() {
        boolean z = true;
        if (!zzp.zzbx().zza(this.zzoZ.context.getPackageManager(), this.zzoZ.context.getPackageName(), "android.permission.INTERNET")) {
            com.google.android.gms.ads.internal.client.zzl.zzcN().zza(this.zzoZ.zzqS, this.zzoZ.zzqV, "Missing internet permission in AndroidManifest.xml.", "Missing internet permission in AndroidManifest.xml. You must have the following declaration: <uses-permission android:name=\"android.permission.INTERNET\" />");
            z = false;
        }
        if (!zzp.zzbx().zzJ(this.zzoZ.context)) {
            com.google.android.gms.ads.internal.client.zzl.zzcN().zza(this.zzoZ.zzqS, this.zzoZ.zzqV, "Missing AdActivity with android:configChanges in AndroidManifest.xml.", "Missing AdActivity with android:configChanges in AndroidManifest.xml. You must have the following declaration within the <application> element: <activity android:name=\"com.google.android.gms.ads.AdActivity\" android:configChanges=\"keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize\" />");
            z = false;
        }
        if (!z && this.zzoZ.zzqS != null) {
            this.zzoZ.zzqS.setVisibility(0);
        }
        return z;
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public boolean zzb(AdRequestParcel adRequestParcel) {
        return super.zzb(zze(adRequestParcel));
    }

    AdRequestParcel zze(AdRequestParcel adRequestParcel) {
        if (adRequestParcel.zztv == this.zzpt) {
            return adRequestParcel;
        }
        return new AdRequestParcel(adRequestParcel.versionCode, adRequestParcel.zztq, adRequestParcel.extras, adRequestParcel.zztr, adRequestParcel.zzts, adRequestParcel.zztt, adRequestParcel.zztu, adRequestParcel.zztv || this.zzpt, adRequestParcel.zztw, adRequestParcel.zztx, adRequestParcel.zzty, adRequestParcel.zztz, adRequestParcel.zztA, adRequestParcel.zztB, adRequestParcel.zztC, adRequestParcel.zztD, adRequestParcel.zztE, adRequestParcel.zztF);
    }
}
