package com.google.android.gms.ads.internal.purchase;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzgb;
import com.google.android.gms.internal.zzha;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

@zzha
/* loaded from: classes.dex */
public class zzd extends zzgb.zza {
    private Context mContext;
    private String zzEH;
    private ArrayList<String> zzEI;
    private String zzrD;

    public zzd(String str, ArrayList<String> arrayList, Context context, String str2) {
        this.zzEH = str;
        this.zzEI = arrayList;
        this.zzrD = str2;
        this.mContext = context;
    }

    @Override // com.google.android.gms.internal.zzgb
    public String getProductId() {
        return this.zzEH;
    }

    @Override // com.google.android.gms.internal.zzgb
    public void recordPlayBillingResolution(int billingResponseCode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (billingResponseCode == 0) {
            zzfK();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("google_play_status", String.valueOf(billingResponseCode));
        map.put("sku", this.zzEH);
        map.put("status", String.valueOf(zzB(billingResponseCode)));
        LinkedList linkedList = new LinkedList();
        Iterator<String> it = this.zzEI.iterator();
        while (it.hasNext()) {
            linkedList.add(zza(it.next(), map));
        }
        zzp.zzbx().zza(this.mContext, this.zzrD, linkedList);
    }

    @Override // com.google.android.gms.internal.zzgb
    public void recordResolution(int resolution) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (resolution == 1) {
            zzfK();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("status", String.valueOf(resolution));
        map.put("sku", this.zzEH);
        LinkedList linkedList = new LinkedList();
        Iterator<String> it = this.zzEI.iterator();
        while (it.hasNext()) {
            linkedList.add(zza(it.next(), map));
        }
        zzp.zzbx().zza(this.mContext, this.zzrD, linkedList);
    }

    protected int zzB(int i) {
        if (i == 0) {
            return 1;
        }
        if (i == 1) {
            return 2;
        }
        return i == 4 ? 3 : 0;
    }

    protected String zza(String str, HashMap<String, String> map) {
        String str2;
        String packageName = this.mContext.getPackageName();
        try {
            str2 = this.mContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Error to retrieve app version", e);
            str2 = "";
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime() - zzp.zzbA().zzgL().zzgV();
        for (String str3 : map.keySet()) {
            str = str.replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", str3), String.format("$1%s$2", map.get(str3)));
        }
        return str.replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", "sessionid"), String.format("$1%s$2", zzp.zzbA().getSessionId())).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", "appid"), String.format("$1%s$2", packageName)).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", "osversion"), String.format("$1%s$2", String.valueOf(Build.VERSION.SDK_INT))).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", "sdkversion"), String.format("$1%s$2", this.zzrD)).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", "appversion"), String.format("$1%s$2", str2)).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", "timestamp"), String.format("$1%s$2", String.valueOf(jElapsedRealtime))).replaceAll(String.format("(?<!@)((?:@@)*)@%s(?<!@)((?:@@)*)@", "[^@]+"), String.format("$1%s$2", "")).replaceAll("@@", "@");
    }

    void zzfK() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.mContext.getClassLoader().loadClass("com.google.ads.conversiontracking.IAPConversionReporter").getDeclaredMethod("reportWithProductId", Context.class, String.class, String.class, Boolean.TYPE).invoke(null, this.mContext, this.zzEH, "", true);
        } catch (ClassNotFoundException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Google Conversion Tracking SDK 1.2.0 or above is required to report a conversion.");
        } catch (NoSuchMethodException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Google Conversion Tracking SDK 1.2.0 or above is required to report a conversion.");
        } catch (Exception e3) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to report a conversion.", e3);
        }
    }
}
