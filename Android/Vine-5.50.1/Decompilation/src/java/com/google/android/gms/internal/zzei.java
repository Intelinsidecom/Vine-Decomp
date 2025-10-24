package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzbb;
import com.google.android.gms.internal.zzjg;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzei {
    private final Context mContext;
    private final String zzAg;
    private zzb<zzbb> zzAh;
    private zzb<zzbb> zzAi;
    private zze zzAj;
    private int zzAk;
    private final VersionInfoParcel zzpI;
    private final Object zzpK;

    /* renamed from: com.google.android.gms.internal.zzei$1, reason: invalid class name */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ zze zzAl;

        /* renamed from: com.google.android.gms.internal.zzei$1$1, reason: invalid class name and collision with other inner class name */
        class C00591 implements zzbb.zza {
            final /* synthetic */ zzbb zzAn;

            C00591(zzbb zzbbVar) {
                this.zzAn = zzbbVar;
            }

            @Override // com.google.android.gms.internal.zzbb.zza
            public void zzcr() {
                zzip.zzKO.postDelayed(new Runnable() { // from class: com.google.android.gms.internal.zzei.1.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (zzei.this.zzpK) {
                            if (AnonymousClass1.this.zzAl.getStatus() == -1 || AnonymousClass1.this.zzAl.getStatus() == 1) {
                                return;
                            }
                            AnonymousClass1.this.zzAl.reject();
                            zzip.runOnUiThread(new Runnable() { // from class: com.google.android.gms.internal.zzei.1.1.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    C00591.this.zzAn.destroy();
                                }
                            });
                            com.google.android.gms.ads.internal.util.client.zzb.v("Could not receive loaded message in a timely manner. Rejecting.");
                        }
                    }
                }, zza.zzAv);
            }
        }

        AnonymousClass1(zze zzeVar) {
            this.zzAl = zzeVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            final zzbb zzbbVarZza = zzei.this.zza(zzei.this.mContext, zzei.this.zzpI);
            zzbbVarZza.zza(new C00591(zzbbVarZza));
            zzbbVarZza.zza("/jsLoaded", new zzdl() { // from class: com.google.android.gms.internal.zzei.1.2
                @Override // com.google.android.gms.internal.zzdl
                public void zza(zzjn zzjnVar, Map<String, String> map) {
                    synchronized (zzei.this.zzpK) {
                        if (AnonymousClass1.this.zzAl.getStatus() == -1 || AnonymousClass1.this.zzAl.getStatus() == 1) {
                            return;
                        }
                        zzei.this.zzAk = 0;
                        zzei.this.zzAh.zzc(zzbbVarZza);
                        AnonymousClass1.this.zzAl.zzg(zzbbVarZza);
                        zzei.this.zzAj = AnonymousClass1.this.zzAl;
                        com.google.android.gms.ads.internal.util.client.zzb.v("Successfully loaded JS Engine.");
                    }
                }
            });
            final zziy zziyVar = new zziy();
            zzdl zzdlVar = new zzdl() { // from class: com.google.android.gms.internal.zzei.1.3
                @Override // com.google.android.gms.internal.zzdl
                public void zza(zzjn zzjnVar, Map<String, String> map) {
                    synchronized (zzei.this.zzpK) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaG("JS Engine is requesting an update");
                        if (zzei.this.zzAk == 0) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Starting reload.");
                            zzei.this.zzAk = 2;
                            zzei.this.zzeh();
                        }
                        zzbbVarZza.zzb("/requestReload", (zzdl) zziyVar.get());
                    }
                }
            };
            zziyVar.set(zzdlVar);
            zzbbVarZza.zza("/requestReload", zzdlVar);
            if (zzei.this.zzAg.endsWith(".js")) {
                zzbbVarZza.zzs(zzei.this.zzAg);
            } else if (zzei.this.zzAg.startsWith("<html>")) {
                zzbbVarZza.zzu(zzei.this.zzAg);
            } else {
                zzbbVarZza.zzt(zzei.this.zzAg);
            }
            zzip.zzKO.postDelayed(new Runnable() { // from class: com.google.android.gms.internal.zzei.1.4
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (zzei.this.zzpK) {
                        if (AnonymousClass1.this.zzAl.getStatus() == -1 || AnonymousClass1.this.zzAl.getStatus() == 1) {
                            return;
                        }
                        AnonymousClass1.this.zzAl.reject();
                        zzip.runOnUiThread(new Runnable() { // from class: com.google.android.gms.internal.zzei.1.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                zzbbVarZza.destroy();
                            }
                        });
                        com.google.android.gms.ads.internal.util.client.zzb.v("Could not receive loaded message in a timely manner. Rejecting.");
                    }
                }
            }, zza.zzAu);
        }
    }

    static class zza {
        static int zzAu = 60000;
        static int zzAv = 10000;
    }

    public interface zzb<T> {
        void zzc(T t);
    }

    public static class zzc<T> implements zzb<T> {
        @Override // com.google.android.gms.internal.zzei.zzb
        public void zzc(T t) {
        }
    }

    public static class zzd extends zzjh<zzbe> {
        private final zze zzAw;
        private boolean zzAx;
        private final Object zzpK = new Object();

        public zzd(zze zzeVar) {
            this.zzAw = zzeVar;
        }

        public void release() {
            synchronized (this.zzpK) {
                if (this.zzAx) {
                    return;
                }
                this.zzAx = true;
                zza(new zzjg.zzc<zzbe>() { // from class: com.google.android.gms.internal.zzei.zzd.1
                    @Override // com.google.android.gms.internal.zzjg.zzc
                    /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
                    public void zzc(zzbe zzbeVar) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("Ending javascript session.");
                        ((zzbf) zzbeVar).zzcs();
                    }
                }, new zzjg.zzb());
                zza(new zzjg.zzc<zzbe>() { // from class: com.google.android.gms.internal.zzei.zzd.2
                    @Override // com.google.android.gms.internal.zzjg.zzc
                    /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
                    public void zzc(zzbe zzbeVar) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("Releasing engine reference.");
                        zzd.this.zzAw.zzek();
                    }
                }, new zzjg.zza() { // from class: com.google.android.gms.internal.zzei.zzd.3
                    @Override // com.google.android.gms.internal.zzjg.zza
                    public void run() {
                        zzd.this.zzAw.zzek();
                    }
                });
            }
        }
    }

    public static class zze extends zzjh<zzbb> {
        private zzb<zzbb> zzAi;
        private final Object zzpK = new Object();
        private boolean zzAz = false;
        private int zzAA = 0;

        public zze(zzb<zzbb> zzbVar) {
            this.zzAi = zzbVar;
        }

        public zzd zzej() {
            final zzd zzdVar = new zzd(this);
            synchronized (this.zzpK) {
                zza(new zzjg.zzc<zzbb>() { // from class: com.google.android.gms.internal.zzei.zze.1
                    @Override // com.google.android.gms.internal.zzjg.zzc
                    /* renamed from: zza, reason: merged with bridge method [inline-methods] */
                    public void zzc(zzbb zzbbVar) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("Getting a new session for JS Engine.");
                        zzdVar.zzg(zzbbVar.zzcq());
                    }
                }, new zzjg.zza() { // from class: com.google.android.gms.internal.zzei.zze.2
                    @Override // com.google.android.gms.internal.zzjg.zza
                    public void run() {
                        com.google.android.gms.ads.internal.util.client.zzb.v("Rejecting reference for JS Engine.");
                        zzdVar.reject();
                    }
                });
                com.google.android.gms.common.internal.zzx.zzaa(this.zzAA >= 0);
                this.zzAA++;
            }
            return zzdVar;
        }

        protected void zzek() {
            synchronized (this.zzpK) {
                com.google.android.gms.common.internal.zzx.zzaa(this.zzAA >= 1);
                com.google.android.gms.ads.internal.util.client.zzb.v("Releasing 1 reference for JS Engine");
                this.zzAA--;
                zzem();
            }
        }

        public void zzel() {
            synchronized (this.zzpK) {
                com.google.android.gms.common.internal.zzx.zzaa(this.zzAA >= 0);
                com.google.android.gms.ads.internal.util.client.zzb.v("Releasing root reference. JS Engine will be destroyed once other references are released.");
                this.zzAz = true;
                zzem();
            }
        }

        protected void zzem() {
            synchronized (this.zzpK) {
                com.google.android.gms.common.internal.zzx.zzaa(this.zzAA >= 0);
                if (this.zzAz && this.zzAA == 0) {
                    com.google.android.gms.ads.internal.util.client.zzb.v("No reference is left (including root). Cleaning up engine.");
                    zza(new zzjg.zzc<zzbb>() { // from class: com.google.android.gms.internal.zzei.zze.3
                        @Override // com.google.android.gms.internal.zzjg.zzc
                        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
                        public void zzc(final zzbb zzbbVar) {
                            zzip.runOnUiThread(new Runnable() { // from class: com.google.android.gms.internal.zzei.zze.3.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    zze.this.zzAi.zzc(zzbbVar);
                                    zzbbVar.destroy();
                                }
                            });
                        }
                    }, new zzjg.zzb());
                } else {
                    com.google.android.gms.ads.internal.util.client.zzb.v("There are still references to the engine. Not destroying.");
                }
            }
        }
    }

    public zzei(Context context, VersionInfoParcel versionInfoParcel, String str) {
        this.zzpK = new Object();
        this.zzAk = 1;
        this.zzAg = str;
        this.mContext = context.getApplicationContext();
        this.zzpI = versionInfoParcel;
        this.zzAh = new zzc();
        this.zzAi = new zzc();
    }

    public zzei(Context context, VersionInfoParcel versionInfoParcel, String str, zzb<zzbb> zzbVar, zzb<zzbb> zzbVar2) {
        this(context, versionInfoParcel, str);
        this.zzAh = zzbVar;
        this.zzAi = zzbVar2;
    }

    private zze zzeg() {
        zze zzeVar = new zze(this.zzAi);
        zzip.runOnUiThread(new AnonymousClass1(zzeVar));
        return zzeVar;
    }

    protected zzbb zza(Context context, VersionInfoParcel versionInfoParcel) {
        return new zzbd(context, versionInfoParcel, null);
    }

    protected zze zzeh() {
        final zze zzeVarZzeg = zzeg();
        zzeVarZzeg.zza(new zzjg.zzc<zzbb>() { // from class: com.google.android.gms.internal.zzei.2
            @Override // com.google.android.gms.internal.zzjg.zzc
            /* renamed from: zza, reason: merged with bridge method [inline-methods] */
            public void zzc(zzbb zzbbVar) {
                synchronized (zzei.this.zzpK) {
                    zzei.this.zzAk = 0;
                    if (zzei.this.zzAj != null && zzeVarZzeg != zzei.this.zzAj) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("New JS engine is loaded, marking previous one as destroyable.");
                        zzei.this.zzAj.zzel();
                    }
                    zzei.this.zzAj = zzeVarZzeg;
                }
            }
        }, new zzjg.zza() { // from class: com.google.android.gms.internal.zzei.3
            @Override // com.google.android.gms.internal.zzjg.zza
            public void run() {
                synchronized (zzei.this.zzpK) {
                    zzei.this.zzAk = 1;
                    com.google.android.gms.ads.internal.util.client.zzb.v("Failed loading new engine. Marking new engine destroyable.");
                    zzeVarZzeg.zzel();
                }
            }
        });
        return zzeVarZzeg;
    }

    public zzd zzei() {
        zzd zzdVarZzej;
        synchronized (this.zzpK) {
            if (this.zzAj == null || this.zzAj.getStatus() == -1) {
                this.zzAk = 2;
                this.zzAj = zzeh();
                zzdVarZzej = this.zzAj.zzej();
            } else if (this.zzAk == 0) {
                zzdVarZzej = this.zzAj.zzej();
            } else if (this.zzAk == 1) {
                this.zzAk = 2;
                zzeh();
                zzdVarZzej = this.zzAj.zzej();
            } else {
                zzdVarZzej = this.zzAk == 2 ? this.zzAj.zzej() : this.zzAj.zzej();
            }
        }
        return zzdVarZzej;
    }
}
