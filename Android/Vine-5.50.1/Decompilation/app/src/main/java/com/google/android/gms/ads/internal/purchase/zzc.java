package com.google.android.gms.ads.internal.purchase;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.zzgg;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzil;
import com.google.android.gms.internal.zzip;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzc extends zzil implements ServiceConnection {
    private Context mContext;
    private zzgg zzAe;
    private boolean zzEA;
    private zzb zzEB;
    private zzh zzEC;
    private List<zzf> zzED;
    private zzk zzEE;
    private final Object zzpK;

    public zzc(Context context, zzgg zzggVar, zzk zzkVar) {
        this(context, zzggVar, zzkVar, new zzb(context), zzh.zzy(context.getApplicationContext()));
    }

    zzc(Context context, zzgg zzggVar, zzk zzkVar, zzb zzbVar, zzh zzhVar) {
        this.zzpK = new Object();
        this.zzEA = false;
        this.zzED = null;
        this.mContext = context;
        this.zzAe = zzggVar;
        this.zzEE = zzkVar;
        this.zzEB = zzbVar;
        this.zzEC = zzhVar;
        this.zzED = this.zzEC.zzg(10L);
    }

    private void zze(long j) {
        do {
            if (!zzf(j)) {
                com.google.android.gms.ads.internal.util.client.zzb.v("Timeout waiting for pending transaction to be processed.");
            }
        } while (!this.zzEA);
    }

    private boolean zzf(long j) throws InterruptedException {
        long jElapsedRealtime = 60000 - (SystemClock.elapsedRealtime() - j);
        if (jElapsedRealtime <= 0) {
            return false;
        }
        try {
            this.zzpK.wait(jElapsedRealtime);
        } catch (InterruptedException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("waitWithTimeout_lock interrupted");
        }
        return true;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName name, IBinder service) {
        synchronized (this.zzpK) {
            this.zzEB.zzN(service);
            zzfJ();
            this.zzEA = true;
            this.zzpK.notify();
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName name) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("In-app billing service disconnected.");
        this.zzEB.destroy();
    }

    @Override // com.google.android.gms.internal.zzil
    public void onStop() {
        synchronized (this.zzpK) {
            com.google.android.gms.common.stats.zzb.zzrz().zza(this.mContext, this);
            this.zzEB.destroy();
        }
    }

    protected void zza(final zzf zzfVar, String str, String str2) {
        final Intent intent = new Intent();
        zzp.zzbH();
        intent.putExtra("RESPONSE_CODE", 0);
        zzp.zzbH();
        intent.putExtra("INAPP_PURCHASE_DATA", str);
        zzp.zzbH();
        intent.putExtra("INAPP_DATA_SIGNATURE", str2);
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.purchase.zzc.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (zzc.this.zzEE.zza(zzfVar.zzEP, -1, intent)) {
                        zzc.this.zzAe.zza(new zzg(zzc.this.mContext, zzfVar.zzEQ, true, -1, intent, zzfVar));
                    } else {
                        zzc.this.zzAe.zza(new zzg(zzc.this.mContext, zzfVar.zzEQ, false, -1, intent, zzfVar));
                    }
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("Fail to verify and dispatch pending transaction");
                }
            }
        });
    }

    @Override // com.google.android.gms.internal.zzil
    public void zzbp() {
        synchronized (this.zzpK) {
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
            com.google.android.gms.common.stats.zzb.zzrz().zza(this.mContext, intent, this, 1);
            zze(SystemClock.elapsedRealtime());
            com.google.android.gms.common.stats.zzb.zzrz().zza(this.mContext, this);
            this.zzEB.destroy();
        }
    }

    protected void zzfJ() throws ClassNotFoundException {
        if (this.zzED.isEmpty()) {
            return;
        }
        HashMap map = new HashMap();
        for (zzf zzfVar : this.zzED) {
            map.put(zzfVar.zzEQ, zzfVar);
        }
        String str = null;
        while (true) {
            Bundle bundleZzi = this.zzEB.zzi(this.mContext.getPackageName(), str);
            if (bundleZzi == null || zzp.zzbH().zzd(bundleZzi) != 0) {
                break;
            }
            ArrayList<String> stringArrayList = bundleZzi.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String> stringArrayList2 = bundleZzi.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            ArrayList<String> stringArrayList3 = bundleZzi.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
            String string = bundleZzi.getString("INAPP_CONTINUATION_TOKEN");
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= stringArrayList.size()) {
                    break;
                }
                if (map.containsKey(stringArrayList.get(i2))) {
                    String str2 = stringArrayList.get(i2);
                    String str3 = stringArrayList2.get(i2);
                    String str4 = stringArrayList3.get(i2);
                    zzf zzfVar2 = (zzf) map.get(str2);
                    if (zzfVar2.zzEP.equals(zzp.zzbH().zzap(str3))) {
                        zza(zzfVar2, str3, str4);
                        map.remove(str2);
                    }
                }
                i = i2 + 1;
            }
            if (string == null || map.isEmpty()) {
                break;
            } else {
                str = string;
            }
        }
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            this.zzEC.zza((zzf) map.get((String) it.next()));
        }
    }
}
