package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public final class zzdj implements zzdl {
    private void zzb(zzjn zzjnVar, Map<String, String> map) {
        String str = map.get("label");
        String str2 = map.get("start_label");
        String str3 = map.get("timestamp");
        if (TextUtils.isEmpty(str)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("No label given for CSI tick.");
            return;
        }
        if (TextUtils.isEmpty(str3)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("No timestamp given for CSI tick.");
            return;
        }
        try {
            long jZzc = zzc(Long.parseLong(str3));
            if (TextUtils.isEmpty(str2)) {
                str2 = "native:view_load";
            }
            zzjnVar.zzhL().zza(str, str2, jZzc);
        } catch (NumberFormatException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Malformed timestamp for CSI tick.", e);
        }
    }

    private long zzc(long j) {
        return (j - com.google.android.gms.ads.internal.zzp.zzbB().currentTimeMillis()) + com.google.android.gms.ads.internal.zzp.zzbB().elapsedRealtime();
    }

    private void zzc(zzjn zzjnVar, Map<String, String> map) {
        String str = map.get("value");
        if (TextUtils.isEmpty(str)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("No value given for CSI experiment.");
            return;
        }
        zzch zzchVarZzdt = zzjnVar.zzhL().zzdt();
        if (zzchVarZzdt == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("No ticker for WebView, dropping experiment ID.");
        } else {
            zzchVarZzdt.zzd("e", str);
        }
    }

    private void zzd(zzjn zzjnVar, Map<String, String> map) {
        String str = map.get("name");
        String str2 = map.get("value");
        if (TextUtils.isEmpty(str2)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("No value given for CSI extra.");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("No name given for CSI extra.");
            return;
        }
        zzch zzchVarZzdt = zzjnVar.zzhL().zzdt();
        if (zzchVarZzdt == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("No ticker for WebView, dropping extra parameter.");
        } else {
            zzchVarZzdt.zzd(str, str2);
        }
    }

    @Override // com.google.android.gms.internal.zzdl
    public void zza(zzjn zzjnVar, Map<String, String> map) {
        String str = map.get("action");
        if ("tick".equals(str)) {
            zzb(zzjnVar, map);
        } else if ("experiment".equals(str)) {
            zzc(zzjnVar, map);
        } else if ("extra".equals(str)) {
            zzd(zzjnVar, map);
        }
    }
}
