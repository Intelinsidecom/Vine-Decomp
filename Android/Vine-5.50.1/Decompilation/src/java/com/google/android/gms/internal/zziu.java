package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.internal.zzm;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zziu {
    private static zzl zzLk;
    private static final Object zzqf = new Object();
    public static final zza<Void> zzLl = new zza() { // from class: com.google.android.gms.internal.zziu.1
        @Override // com.google.android.gms.internal.zziu.zza
        /* renamed from: zzhj, reason: merged with bridge method [inline-methods] */
        public Void zzgc() {
            return null;
        }

        @Override // com.google.android.gms.internal.zziu.zza
        /* renamed from: zzi, reason: merged with bridge method [inline-methods] */
        public Void zzh(InputStream inputStream) {
            return null;
        }
    };

    public interface zza<T> {
        T zzgc();

        T zzh(InputStream inputStream);
    }

    private static class zzb<T> extends zzk<InputStream> {
        private final zza<T> zzLp;
        private final zzm.zzb<T> zzaG;

        public zzb(String str, final zza<T> zzaVar, final zzm.zzb<T> zzbVar) {
            super(0, str, new zzm.zza() { // from class: com.google.android.gms.internal.zziu.zzb.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.google.android.gms.internal.zzm.zza
                public void zze(zzr zzrVar) {
                    zzbVar.zzb(zzaVar.zzgc());
                }
            });
            this.zzLp = zzaVar;
            this.zzaG = zzbVar;
        }

        @Override // com.google.android.gms.internal.zzk
        protected zzm<InputStream> zza(zzi zziVar) {
            return zzm.zza(new ByteArrayInputStream(zziVar.data), zzx.zzb(zziVar));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.zzk
        /* renamed from: zzj, reason: merged with bridge method [inline-methods] */
        public void zza(InputStream inputStream) {
            this.zzaG.zzb(this.zzLp.zzh(inputStream));
        }
    }

    private class zzc<T> extends zzjb<T> implements zzm.zzb<T> {
        private zzc() {
        }

        @Override // com.google.android.gms.internal.zzm.zzb
        public void zzb(T t) {
            super.zzf(t);
        }
    }

    public zziu(Context context) {
        zzLk = zzR(context);
    }

    private static zzl zzR(Context context) {
        zzl zzlVar;
        synchronized (zzqf) {
            if (zzLk == null) {
                zzLk = zzac.zza(context.getApplicationContext());
            }
            zzlVar = zzLk;
        }
        return zzlVar;
    }

    public <T> zzje<T> zza(String str, zza<T> zzaVar) {
        zzc zzcVar = new zzc();
        zzLk.zze(new zzb(str, zzaVar, zzcVar));
        return zzcVar;
    }

    public zzje<String> zza(final String str, final Map<String, String> map) {
        final zzc zzcVar = new zzc();
        zzLk.zze(new zzab(str, zzcVar, new zzm.zza() { // from class: com.google.android.gms.internal.zziu.2
            @Override // com.google.android.gms.internal.zzm.zza
            public void zze(zzr zzrVar) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Failed to load URL: " + str + "\n" + zzrVar.toString());
                zzcVar.zzb((zzc) null);
            }
        }) { // from class: com.google.android.gms.internal.zziu.3
            @Override // com.google.android.gms.internal.zzk
            public Map<String, String> getHeaders() throws com.google.android.gms.internal.zza {
                return map == null ? super.getHeaders() : map;
            }
        });
        return zzcVar;
    }
}
