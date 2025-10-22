package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.gms.internal.zzfp;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzfq extends zzfr implements zzdl {
    private final Context mContext;
    DisplayMetrics zzCA;
    private float zzCB;
    int zzCC;
    int zzCD;
    private int zzCE;
    int zzCF;
    int zzCG;
    int zzCH;
    int zzCI;
    private final zzbr zzCz;
    private final zzjn zzps;
    private final WindowManager zzrR;

    public zzfq(zzjn zzjnVar, Context context, zzbr zzbrVar) {
        super(zzjnVar);
        this.zzCC = -1;
        this.zzCD = -1;
        this.zzCF = -1;
        this.zzCG = -1;
        this.zzCH = -1;
        this.zzCI = -1;
        this.zzps = zzjnVar;
        this.mContext = context;
        this.zzCz = zzbrVar;
        this.zzrR = (WindowManager) context.getSystemService("window");
    }

    private void zzeF() {
        this.zzCA = new DisplayMetrics();
        Display defaultDisplay = this.zzrR.getDefaultDisplay();
        defaultDisplay.getMetrics(this.zzCA);
        this.zzCB = this.zzCA.density;
        this.zzCE = defaultDisplay.getRotation();
    }

    private void zzeK() {
        int[] iArr = new int[2];
        this.zzps.getLocationOnScreen(iArr);
        zzf(com.google.android.gms.ads.internal.client.zzl.zzcN().zzc(this.mContext, iArr[0]), com.google.android.gms.ads.internal.client.zzl.zzcN().zzc(this.mContext, iArr[1]));
    }

    private zzfp zzeN() {
        return new zzfp.zza().zzr(this.zzCz.zzdd()).zzq(this.zzCz.zzde()).zzs(this.zzCz.zzdi()).zzt(this.zzCz.zzdf()).zzu(this.zzCz.zzdg()).zzeE();
    }

    @Override // com.google.android.gms.internal.zzdl
    public void zza(zzjn zzjnVar, Map<String, String> map) {
        zzeI();
    }

    void zzeG() {
        this.zzCC = com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(this.zzCA, this.zzCA.widthPixels);
        this.zzCD = com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(this.zzCA, this.zzCA.heightPixels);
        Activity activityZzhx = this.zzps.zzhx();
        if (activityZzhx == null || activityZzhx.getWindow() == null) {
            this.zzCF = this.zzCC;
            this.zzCG = this.zzCD;
        } else {
            int[] iArrZzg = com.google.android.gms.ads.internal.zzp.zzbx().zzg(activityZzhx);
            this.zzCF = com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(this.zzCA, iArrZzg[0]);
            this.zzCG = com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(this.zzCA, iArrZzg[1]);
        }
    }

    void zzeH() {
        if (this.zzps.zzaP().zztW) {
            this.zzCH = this.zzCC;
            this.zzCI = this.zzCD;
        } else {
            this.zzps.measure(0, 0);
            this.zzCH = com.google.android.gms.ads.internal.client.zzl.zzcN().zzc(this.mContext, this.zzps.getMeasuredWidth());
            this.zzCI = com.google.android.gms.ads.internal.client.zzl.zzcN().zzc(this.mContext, this.zzps.getMeasuredHeight());
        }
    }

    public void zzeI() {
        zzeF();
        zzeG();
        zzeH();
        zzeL();
        zzeM();
        zzeK();
        zzeJ();
    }

    void zzeJ() {
        if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(2)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Dispatching Ready Event.");
        }
        zzam(this.zzps.zzhF().afmaVersion);
    }

    void zzeL() {
        zza(this.zzCC, this.zzCD, this.zzCF, this.zzCG, this.zzCB, this.zzCE);
    }

    void zzeM() {
        this.zzps.zzb("onDeviceFeaturesReceived", zzeN().toJson());
    }

    public void zzf(int i, int i2) {
        zzc(i, i2 - (this.mContext instanceof Activity ? com.google.android.gms.ads.internal.zzp.zzbx().zzj((Activity) this.mContext)[0] : 0), this.zzCH, this.zzCI);
        this.zzps.zzhC().zze(i, i2);
    }
}
