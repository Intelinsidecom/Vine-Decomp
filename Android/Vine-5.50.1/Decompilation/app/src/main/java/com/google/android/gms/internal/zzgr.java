package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.internal.zzgq;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzjo;

@zzha
/* loaded from: classes.dex */
public class zzgr extends zzgm implements zzjo.zza {
    zzgr(Context context, zzie.zza zzaVar, zzjn zzjnVar, zzgq.zza zzaVar2) {
        super(context, zzaVar, zzjnVar, zzaVar2);
    }

    @Override // com.google.android.gms.internal.zzgm
    protected void zzfP() {
        if (this.zzFd.errorCode != -2) {
            return;
        }
        this.zzps.zzhC().zza(this);
        zzfW();
        com.google.android.gms.ads.internal.util.client.zzb.v("Loading HTML in WebView.");
        this.zzps.loadDataWithBaseURL(com.google.android.gms.ads.internal.zzp.zzbx().zzaz(this.zzFd.zzDE), this.zzFd.body, "text/html", "UTF-8", null);
    }

    protected void zzfW() {
    }
}
