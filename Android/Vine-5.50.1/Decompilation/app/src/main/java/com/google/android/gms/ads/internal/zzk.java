package com.google.android.gms.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Window;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.overlay.AdOverlayInfoParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzdp;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzil;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.internal.zzjn;

@zzha
/* loaded from: classes.dex */
public class zzk extends zzc implements zzdp {
    protected transient boolean zzpR;
    private boolean zzpS;
    private float zzpT;
    private String zzpU;

    @zzha
    private class zza extends zzil {
        private final String zzpV;

        public zza(String str) {
            this.zzpV = str;
        }

        @Override // com.google.android.gms.internal.zzil
        public void onStop() {
        }

        @Override // com.google.android.gms.internal.zzil
        public void zzbp() {
            zzp.zzbx().zzf(zzk.this.zzoZ.context, this.zzpV);
        }
    }

    @zzha
    private class zzb extends zzil {
        private final String zzpV;
        private final Bitmap zzpX;

        public zzb(Bitmap bitmap, String str) {
            this.zzpX = bitmap;
            this.zzpV = str;
        }

        @Override // com.google.android.gms.internal.zzil
        public void onStop() {
        }

        @Override // com.google.android.gms.internal.zzil
        public void zzbp() {
            InterstitialAdParameterParcel interstitialAdParameterParcel = new InterstitialAdParameterParcel(zzk.this.zzoZ.zzqa, zzk.this.zzbn(), zzk.this.zzoZ.zzqa ? zzp.zzbx().zza(zzk.this.zzoZ.context, this.zzpX, this.zzpV) : false ? this.zzpV : null, zzk.this.zzpS, zzk.this.zzpT);
            int requestedOrientation = zzk.this.zzoZ.zzqW.zzDC.getRequestedOrientation();
            if (requestedOrientation == -1) {
                requestedOrientation = zzk.this.zzoZ.zzqW.orientation;
            }
            final AdOverlayInfoParcel adOverlayInfoParcel = new AdOverlayInfoParcel(zzk.this, zzk.this, zzk.this, zzk.this.zzoZ.zzqW.zzDC, requestedOrientation, zzk.this.zzoZ.zzqR, zzk.this.zzoZ.zzqW.zzGS, interstitialAdParameterParcel);
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.zzk.zzb.1
                @Override // java.lang.Runnable
                public void run() {
                    zzp.zzbv().zza(zzk.this.zzoZ.context, adOverlayInfoParcel);
                }
            });
        }
    }

    public zzk(Context context, AdSizeParcel adSizeParcel, String str, zzew zzewVar, VersionInfoParcel versionInfoParcel, zzd zzdVar) {
        super(context, adSizeParcel, str, zzewVar, versionInfoParcel, zzdVar);
        this.zzpR = false;
        this.zzpU = "background" + hashCode() + ".png";
    }

    private void zzb(Bundle bundle) {
        zzp.zzbx().zzb(this.zzoZ.context, this.zzoZ.zzqR.afmaVersion, "gmob-apps", bundle, false);
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.client.zzs
    public void showInterstitial() {
        zzx.zzcx("showInterstitial must be called on the main UI thread.");
        if (this.zzoZ.zzqW == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("The interstitial has not loaded.");
            return;
        }
        if (zzbz.zzws.get().booleanValue()) {
            String packageName = this.zzoZ.context.getApplicationContext() != null ? this.zzoZ.context.getApplicationContext().getPackageName() : this.zzoZ.context.getPackageName();
            if (!this.zzpR) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("It is not recommended to show an interstitial before onAdLoaded completes.");
                Bundle bundle = new Bundle();
                bundle.putString("appid", packageName);
                bundle.putString("action", "show_interstitial_before_load_finish");
                zzb(bundle);
            }
            if (!zzp.zzbx().zzP(this.zzoZ.context)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("It is not recommended to show an interstitial when app is not in foreground.");
                Bundle bundle2 = new Bundle();
                bundle2.putString("appid", packageName);
                bundle2.putString("action", "show_interstitial_app_not_in_foreground");
                zzb(bundle2);
            }
        }
        if (this.zzoZ.zzbR()) {
            return;
        }
        if (this.zzoZ.zzqW.zzGN) {
            try {
                this.zzoZ.zzqW.zzBq.showInterstitial();
                return;
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not show interstitial.", e);
                zzbo();
                return;
            }
        }
        if (this.zzoZ.zzqW.zzDC == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("The interstitial failed to load.");
            return;
        }
        if (this.zzoZ.zzqW.zzDC.zzhG()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("The interstitial is already showing.");
            return;
        }
        this.zzoZ.zzqW.zzDC.zzD(true);
        if (this.zzoZ.zzqW.zzJE != null) {
            this.zzpb.zza(this.zzoZ.zzqV, this.zzoZ.zzqW);
        }
        Bitmap bitmapZzQ = this.zzoZ.zzqa ? zzp.zzbx().zzQ(this.zzoZ.context) : null;
        if (zzbz.zzwI.get().booleanValue() && bitmapZzQ != null) {
            new zzb(bitmapZzQ, this.zzpU).zzfR();
            return;
        }
        InterstitialAdParameterParcel interstitialAdParameterParcel = new InterstitialAdParameterParcel(this.zzoZ.zzqa, zzbn(), null, false, 0.0f);
        int requestedOrientation = this.zzoZ.zzqW.zzDC.getRequestedOrientation();
        if (requestedOrientation == -1) {
            requestedOrientation = this.zzoZ.zzqW.orientation;
        }
        zzp.zzbv().zza(this.zzoZ.context, new AdOverlayInfoParcel(this, this, this, this.zzoZ.zzqW.zzDC, requestedOrientation, this.zzoZ.zzqR, this.zzoZ.zzqW.zzGS, interstitialAdParameterParcel));
    }

    @Override // com.google.android.gms.ads.internal.zzc
    protected zzjn zza(zzie.zza zzaVar, zze zzeVar) {
        zzjn zzjnVarZza = zzp.zzby().zza(this.zzoZ.context, this.zzoZ.zzqV, false, false, this.zzoZ.zzqQ, this.zzoZ.zzqR, this.zzoU, this.zzpc);
        zzjnVarZza.zzhC().zzb(this, null, this, this, zzbz.zzwa.get().booleanValue(), this, this, zzeVar, null);
        zzjnVarZza.zzaJ(zzaVar.zzJK.zzGF);
        return zzjnVarZza;
    }

    @Override // com.google.android.gms.internal.zzdp
    public void zza(boolean z, float f) {
        this.zzpS = z;
        this.zzpT = f;
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.zza
    public boolean zza(AdRequestParcel adRequestParcel, zzch zzchVar) {
        if (this.zzoZ.zzqW == null) {
            return super.zza(adRequestParcel, zzchVar);
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("An interstitial is already loading. Aborting.");
        return false;
    }

    @Override // com.google.android.gms.ads.internal.zzb
    protected boolean zza(AdRequestParcel adRequestParcel, zzie zzieVar, boolean z) {
        if (this.zzoZ.zzbQ() && zzieVar.zzDC != null) {
            zzp.zzbz().zzf(zzieVar.zzDC);
        }
        return this.zzoY.zzbr();
    }

    @Override // com.google.android.gms.ads.internal.zzc, com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.zza
    public boolean zza(zzie zzieVar, zzie zzieVar2) {
        if (!super.zza(zzieVar, zzieVar2)) {
            return false;
        }
        if (!this.zzoZ.zzbQ() && this.zzoZ.zzro != null && zzieVar2.zzJE != null) {
            this.zzpb.zza(this.zzoZ.zzqV, zzieVar2, this.zzoZ.zzro);
        }
        return true;
    }

    @Override // com.google.android.gms.ads.internal.zza
    protected boolean zzaS() {
        zzbo();
        return super.zzaS();
    }

    @Override // com.google.android.gms.ads.internal.zza
    protected boolean zzaV() {
        if (!super.zzaV()) {
            return false;
        }
        this.zzpR = true;
        return true;
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.overlay.zzg
    public void zzaY() {
        recordImpression();
        super.zzaY();
    }

    protected boolean zzbn() {
        Window window;
        if (!(this.zzoZ.context instanceof Activity) || (window = ((Activity) this.zzoZ.context).getWindow()) == null || window.getDecorView() == null) {
            return false;
        }
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        window.getDecorView().getGlobalVisibleRect(rect, null);
        window.getDecorView().getWindowVisibleDisplayFrame(rect2);
        return (rect.bottom == 0 || rect2.bottom == 0 || rect.top != rect2.top) ? false : true;
    }

    public void zzbo() {
        new zza(this.zzpU).zzfR();
        if (this.zzoZ.zzbQ()) {
            this.zzoZ.zzbN();
            this.zzoZ.zzqW = null;
            this.zzoZ.zzqa = false;
            this.zzpR = false;
        }
    }

    @Override // com.google.android.gms.internal.zzdp
    public void zzd(boolean z) {
        this.zzoZ.zzqa = z;
    }
}
