package com.google.android.gms.internal;

import android.content.Context;
import android.os.Build;
import java.util.LinkedHashMap;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzca {
    private Context mContext;
    private String zzrD;
    private boolean zzwK = zzbz.zzvL.get().booleanValue();
    private String zzwL = zzbz.zzvM.get();
    private Map<String, String> zzwM = new LinkedHashMap();

    public zzca(Context context, String str) {
        this.mContext = null;
        this.zzrD = null;
        this.mContext = context;
        this.zzrD = str;
        this.zzwM.put("s", "gmob_sdk");
        this.zzwM.put("v", "3");
        this.zzwM.put("os", Build.VERSION.RELEASE);
        this.zzwM.put("sdk", Build.VERSION.SDK);
        this.zzwM.put("device", com.google.android.gms.ads.internal.zzp.zzbx().zzhb());
        this.zzwM.put("app", context.getApplicationContext() != null ? context.getApplicationContext().getPackageName() : context.getPackageName());
        zzhi zzhiVarZzE = com.google.android.gms.ads.internal.zzp.zzbD().zzE(this.mContext);
        this.zzwM.put("network_coarse", Integer.toString(zzhiVarZzE.zzIM));
        this.zzwM.put("network_fine", Integer.toString(zzhiVarZzE.zzIN));
    }

    Context getContext() {
        return this.mContext;
    }

    String zzbY() {
        return this.zzrD;
    }

    boolean zzdn() {
        return this.zzwK;
    }

    String zzdo() {
        return this.zzwL;
    }

    Map<String, String> zzdp() {
        return this.zzwM;
    }
}
