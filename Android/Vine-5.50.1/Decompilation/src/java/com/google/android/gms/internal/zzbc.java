package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzbb;
import java.util.concurrent.Future;

@zzha
/* loaded from: classes.dex */
public class zzbc {

    private static class zza<JavascriptEngine> extends zzjb<JavascriptEngine> {
        JavascriptEngine zzst;

        private zza() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v0, types: [JavascriptEngine, com.google.android.gms.internal.zzbb, com.google.android.gms.internal.zzbd] */
    public zzbb zza(Context context, VersionInfoParcel versionInfoParcel, final zza<zzbb> zzaVar, zzan zzanVar) {
        final ?? zzbdVar = new zzbd(context, versionInfoParcel, zzanVar);
        zzaVar.zzst = zzbdVar;
        zzbdVar.zza(new zzbb.zza() { // from class: com.google.android.gms.internal.zzbc.2
            @Override // com.google.android.gms.internal.zzbb.zza
            public void zzcr() {
                zzaVar.zzf(zzbdVar);
            }
        });
        return zzbdVar;
    }

    public Future<zzbb> zza(final Context context, final VersionInfoParcel versionInfoParcel, final String str, final zzan zzanVar) {
        final zza zzaVar = new zza();
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzbc.1
            @Override // java.lang.Runnable
            public void run() {
                zzbc.this.zza(context, versionInfoParcel, (zza<zzbb>) zzaVar, zzanVar).zzt(str);
            }
        });
        return zzaVar;
    }
}
