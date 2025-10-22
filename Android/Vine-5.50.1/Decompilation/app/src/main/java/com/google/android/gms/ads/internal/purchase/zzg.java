package com.google.android.gms.ads.internal.purchase;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.zzgf;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class zzg extends zzgf.zza implements ServiceConnection {
    private Context mContext;
    zzb zzEB;
    private String zzEH;
    private zzf zzEL;
    private boolean zzER;
    private int zzES;
    private Intent zzET;

    public zzg(Context context, String str, boolean z, int i, Intent intent, zzf zzfVar) {
        this.zzER = false;
        this.zzEH = str;
        this.zzES = i;
        this.zzET = intent;
        this.zzER = z;
        this.mContext = context;
        this.zzEL = zzfVar;
    }

    @Override // com.google.android.gms.internal.zzgf
    public void finishPurchase() {
        int iZzd = zzp.zzbH().zzd(this.zzET);
        if (this.zzES == -1 && iZzd == 0) {
            this.zzEB = new zzb(this.mContext);
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
            com.google.android.gms.common.stats.zzb.zzrz().zza(this.mContext, intent, this, 1);
        }
    }

    @Override // com.google.android.gms.internal.zzgf
    public String getProductId() {
        return this.zzEH;
    }

    @Override // com.google.android.gms.internal.zzgf
    public Intent getPurchaseData() {
        return this.zzET;
    }

    @Override // com.google.android.gms.internal.zzgf
    public int getResultCode() {
        return this.zzES;
    }

    @Override // com.google.android.gms.internal.zzgf
    public boolean isVerified() {
        return this.zzER;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName name, IBinder service) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("In-app billing service connected.");
        this.zzEB.zzN(service);
        String strZzaq = zzp.zzbH().zzaq(zzp.zzbH().zze(this.zzET));
        if (strZzaq == null) {
            return;
        }
        if (this.zzEB.zzh(this.mContext.getPackageName(), strZzaq) == 0) {
            zzh.zzy(this.mContext).zza(this.zzEL);
        }
        com.google.android.gms.common.stats.zzb.zzrz().zza(this.mContext, this);
        this.zzEB.destroy();
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName name) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("In-app billing service disconnected.");
        this.zzEB.destroy();
    }
}
