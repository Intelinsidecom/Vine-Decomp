package com.google.android.gms.ads.internal;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzjn;

@zzha
/* loaded from: classes.dex */
public class zze {
    private zza zzpo;
    private boolean zzpp;
    private boolean zzpq;

    public interface zza {
        void zzq(String str);
    }

    @zzha
    public static class zzb implements zza {
        private final zzie.zza zzpr;
        private final zzjn zzps;

        public zzb(zzie.zza zzaVar, zzjn zzjnVar) {
            this.zzpr = zzaVar;
            this.zzps = zzjnVar;
        }

        @Override // com.google.android.gms.ads.internal.zze.zza
        public void zzq(String str) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("An auto-clicking creative is blocked");
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https");
            builder.path("//pagead2.googlesyndication.com/pagead/gen_204");
            builder.appendQueryParameter("id", "gmob-apps-blocked-navigation");
            if (!TextUtils.isEmpty(str)) {
                builder.appendQueryParameter("navigationURL", str);
            }
            if (this.zzpr != null && this.zzpr.zzJL != null && !TextUtils.isEmpty(this.zzpr.zzJL.zzGS)) {
                builder.appendQueryParameter("debugDialog", this.zzpr.zzJL.zzGS);
            }
            zzp.zzbx().zzc(this.zzps.getContext(), this.zzps.zzhF().afmaVersion, builder.toString());
        }
    }

    public zze() {
        this.zzpq = zzbz.zzvn.get().booleanValue();
    }

    public zze(boolean z) {
        this.zzpq = z;
    }

    public void recordClick() {
        this.zzpp = true;
    }

    public void zza(zza zzaVar) {
        this.zzpo = zzaVar;
    }

    public boolean zzbg() {
        return !this.zzpq || this.zzpp;
    }

    public void zzp(String str) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Action was blocked because no click was detected.");
        if (this.zzpo != null) {
            this.zzpo.zzq(str);
        }
    }
}
