package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.common.api.Releasable;
import java.util.HashMap;

@zzha
/* loaded from: classes.dex */
public abstract class zzdw implements Releasable {
    protected zzjn zzps;

    public zzdw(zzjn zzjnVar) {
        this.zzps = zzjnVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String zzab(String str) {
        switch (str) {
        }
        return "internal";
    }

    public abstract void abort();

    @Override // com.google.android.gms.common.api.Releasable
    public void release() {
    }

    public abstract boolean zzZ(String str);

    protected void zza(final String str, final String str2, final int i) {
        com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzdw.2
            @Override // java.lang.Runnable
            public void run() {
                HashMap map = new HashMap();
                map.put("event", "precacheComplete");
                map.put("src", str);
                map.put("cachedSrc", str2);
                map.put("totalBytes", Integer.toString(i));
                zzdw.this.zzps.zzb("onPrecacheEvent", map);
            }
        });
    }

    protected void zza(final String str, final String str2, final int i, final int i2, final boolean z) {
        com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzdw.1
            @Override // java.lang.Runnable
            public void run() {
                HashMap map = new HashMap();
                map.put("event", "precacheProgress");
                map.put("src", str);
                map.put("cachedSrc", str2);
                map.put("bytesLoaded", Integer.toString(i));
                map.put("totalBytes", Integer.toString(i2));
                map.put("cacheReady", z ? "1" : "0");
                zzdw.this.zzps.zzb("onPrecacheEvent", map);
            }
        });
    }

    protected void zza(final String str, final String str2, final String str3, final String str4) {
        com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzdw.3
            @Override // java.lang.Runnable
            public void run() {
                HashMap map = new HashMap();
                map.put("event", "precacheCanceled");
                map.put("src", str);
                if (!TextUtils.isEmpty(str2)) {
                    map.put("cachedSrc", str2);
                }
                map.put("type", zzdw.this.zzab(str3));
                map.put("reason", str3);
                if (!TextUtils.isEmpty(str4)) {
                    map.put("message", str4);
                }
                zzdw.this.zzps.zzb("onPrecacheEvent", map);
            }
        });
    }

    protected String zzaa(String str) {
        return com.google.android.gms.ads.internal.client.zzl.zzcN().zzaE(str);
    }
}
