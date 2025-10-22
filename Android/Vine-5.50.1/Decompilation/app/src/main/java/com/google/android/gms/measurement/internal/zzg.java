package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
import java.util.Iterator;

/* loaded from: classes.dex */
public class zzg {
    final String mName;
    final String zzaRd;
    final String zzaSC;
    final long zzaSD;
    final EventParams zzaSE;
    final long zzacS;

    zzg(zzt zztVar, String str, String str2, String str3, long j, long j2, Bundle bundle) {
        com.google.android.gms.common.internal.zzx.zzcG(str2);
        com.google.android.gms.common.internal.zzx.zzcG(str3);
        this.zzaRd = str2;
        this.mName = str3;
        this.zzaSC = TextUtils.isEmpty(str) ? null : str;
        this.zzacS = j;
        this.zzaSD = j2;
        if (this.zzaSD != 0 && this.zzaSD > this.zzacS) {
            zztVar.zzzz().zzBm().zzez("Event created with reverse previous/current timestamps");
        }
        this.zzaSE = zza(zztVar, bundle);
    }

    private zzg(zzt zztVar, String str, String str2, String str3, long j, long j2, EventParams eventParams) {
        com.google.android.gms.common.internal.zzx.zzcG(str2);
        com.google.android.gms.common.internal.zzx.zzcG(str3);
        com.google.android.gms.common.internal.zzx.zzy(eventParams);
        this.zzaRd = str2;
        this.mName = str3;
        this.zzaSC = TextUtils.isEmpty(str) ? null : str;
        this.zzacS = j;
        this.zzaSD = j2;
        if (this.zzaSD != 0 && this.zzaSD > this.zzacS) {
            zztVar.zzzz().zzBm().zzez("Event created with reverse previous/current timestamps");
        }
        this.zzaSE = eventParams;
    }

    private EventParams zza(zzt zztVar, Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            return new EventParams(new Bundle());
        }
        Bundle bundle2 = new Bundle(bundle);
        Iterator<String> it = bundle2.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next == null) {
                it.remove();
            } else {
                Object objZzk = zztVar.zzAU().zzk(next, bundle2.get(next));
                if (objZzk == null) {
                    it.remove();
                } else {
                    zztVar.zzAU().zza(bundle2, next, objZzk);
                }
            }
        }
        return new EventParams(bundle2);
    }

    public String toString() {
        return "Event{appId='" + this.zzaRd + "', name='" + this.mName + "', params=" + this.zzaSE + '}';
    }

    zzg zza(zzt zztVar, long j) {
        return new zzg(zztVar, this.zzaSC, this.zzaRd, this.mName, this.zzacS, j, this.zzaSE);
    }
}
