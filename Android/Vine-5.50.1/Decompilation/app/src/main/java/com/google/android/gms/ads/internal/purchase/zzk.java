package com.google.android.gms.ads.internal.purchase;

import android.content.Intent;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zzk {
    private final String zzux;

    public zzk(String str) {
        this.zzux = str;
    }

    public boolean zza(String str, int i, Intent intent) {
        if (str == null || intent == null) {
            return false;
        }
        String strZze = zzp.zzbH().zze(intent);
        String strZzf = zzp.zzbH().zzf(intent);
        if (strZze == null || strZzf == null) {
            return false;
        }
        if (!str.equals(zzp.zzbH().zzap(strZze))) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Developer payload not match.");
            return false;
        }
        if (this.zzux == null || zzl.zzc(this.zzux, strZze, strZzf)) {
            return true;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("Fail to verify signature.");
        return false;
    }

    public String zzfN() {
        return zzp.zzbx().zzha();
    }
}
