package com.google.android.gms.ads.internal.purchase;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.view.PointerIconCompat;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.zzgb;
import com.google.android.gms.internal.zzgd;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zze extends zzgd.zza implements ServiceConnection {
    private final Activity mActivity;
    private zzb zzEB;
    zzh zzEC;
    private zzk zzEE;
    private Context zzEJ;
    private zzgb zzEK;
    private zzf zzEL;
    private zzj zzEM;
    private String zzEN = null;

    public zze(Activity activity) {
        this.mActivity = activity;
        this.zzEC = zzh.zzy(this.mActivity.getApplicationContext());
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0037 A[Catch: RemoteException -> 0x003f, all -> 0x004d, Merged into TryCatch #1 {all -> 0x004d, RemoteException -> 0x003f, blocks: (B:5:0x0006, B:7:0x0011, B:9:0x0016, B:12:0x0021, B:15:0x0037, B:18:0x0040), top: B:23:0x0006 }, TRY_ENTER, TRY_LEAVE] */
    @Override // com.google.android.gms.internal.zzgd
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onActivityResult(int r6, int r7, android.content.Intent r8) {
        /*
            r5 = this;
            r4 = 0
            r0 = 1001(0x3e9, float:1.403E-42)
            if (r6 != r0) goto L36
            r0 = 0
            com.google.android.gms.ads.internal.purchase.zzi r1 = com.google.android.gms.ads.internal.zzp.zzbH()     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            int r1 = r1.zzd(r8)     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            r2 = -1
            if (r7 != r2) goto L37
            com.google.android.gms.ads.internal.zzp.zzbH()     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            if (r1 != 0) goto L37
            com.google.android.gms.ads.internal.purchase.zzk r2 = r5.zzEE     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            java.lang.String r3 = r5.zzEN     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            boolean r2 = r2.zza(r3, r7, r8)     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            if (r2 == 0) goto L21
            r0 = 1
        L21:
            com.google.android.gms.internal.zzgb r2 = r5.zzEK     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            r2.recordPlayBillingResolution(r1)     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            android.app.Activity r1 = r5.mActivity     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            r1.finish()     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            com.google.android.gms.internal.zzgb r1 = r5.zzEK     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            java.lang.String r1 = r1.getProductId()     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            r5.zza(r1, r0, r7, r8)     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            r5.zzEN = r4
        L36:
            return
        L37:
            com.google.android.gms.ads.internal.purchase.zzh r2 = r5.zzEC     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            com.google.android.gms.ads.internal.purchase.zzf r3 = r5.zzEL     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            r2.zza(r3)     // Catch: android.os.RemoteException -> L3f java.lang.Throwable -> L4d
            goto L21
        L3f:
            r0 = move-exception
            java.lang.String r0 = "Fail to process purchase result."
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(r0)     // Catch: java.lang.Throwable -> L4d
            android.app.Activity r0 = r5.mActivity     // Catch: java.lang.Throwable -> L4d
            r0.finish()     // Catch: java.lang.Throwable -> L4d
            r5.zzEN = r4
            goto L36
        L4d:
            r0 = move-exception
            r5.zzEN = r4
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.purchase.zze.onActivityResult(int, int, android.content.Intent):void");
    }

    @Override // com.google.android.gms.internal.zzgd
    public void onCreate() {
        GInAppPurchaseManagerInfoParcel gInAppPurchaseManagerInfoParcelZzc = GInAppPurchaseManagerInfoParcel.zzc(this.mActivity.getIntent());
        this.zzEM = gInAppPurchaseManagerInfoParcelZzc.zzEx;
        this.zzEE = gInAppPurchaseManagerInfoParcelZzc.zzrm;
        this.zzEK = gInAppPurchaseManagerInfoParcelZzc.zzEv;
        this.zzEB = new zzb(this.mActivity.getApplicationContext());
        this.zzEJ = gInAppPurchaseManagerInfoParcelZzc.zzEw;
        if (this.mActivity.getResources().getConfiguration().orientation == 2) {
            this.mActivity.setRequestedOrientation(zzp.zzbz().zzhd());
        } else {
            this.mActivity.setRequestedOrientation(zzp.zzbz().zzhe());
        }
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
        this.mActivity.bindService(intent, this, 1);
    }

    @Override // com.google.android.gms.internal.zzgd
    public void onDestroy() {
        this.mActivity.unbindService(this);
        this.zzEB.destroy();
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName name, IBinder service) throws IntentSender.SendIntentException {
        this.zzEB.zzN(service);
        try {
            this.zzEN = this.zzEE.zzfN();
            Bundle bundleZzb = this.zzEB.zzb(this.mActivity.getPackageName(), this.zzEK.getProductId(), this.zzEN);
            PendingIntent pendingIntent = (PendingIntent) bundleZzb.getParcelable("BUY_INTENT");
            if (pendingIntent == null) {
                int iZzd = zzp.zzbH().zzd(bundleZzb);
                this.zzEK.recordPlayBillingResolution(iZzd);
                zza(this.zzEK.getProductId(), false, iZzd, null);
                this.mActivity.finish();
            } else {
                this.zzEL = new zzf(this.zzEK.getProductId(), this.zzEN);
                this.zzEC.zzb(this.zzEL);
                Integer num = 0;
                Integer num2 = 0;
                Integer num3 = 0;
                this.mActivity.startIntentSenderForResult(pendingIntent.getIntentSender(), PointerIconCompat.TYPE_CONTEXT_MENU, new Intent(), num.intValue(), num2.intValue(), num3.intValue());
            }
        } catch (IntentSender.SendIntentException | RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Error when connecting in-app billing service", e);
            this.mActivity.finish();
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName name) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("In-app billing service disconnected.");
        this.zzEB.destroy();
    }

    protected void zza(String str, boolean z, int i, Intent intent) {
        if (this.zzEM != null) {
            this.zzEM.zza(str, z, i, intent, this.zzEL);
        }
    }
}
