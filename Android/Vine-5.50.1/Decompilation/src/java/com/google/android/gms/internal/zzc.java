package com.google.android.gms.internal;

import android.os.Process;
import com.google.android.gms.internal.zzb;
import java.util.concurrent.BlockingQueue;

/* loaded from: classes.dex */
public class zzc extends Thread {
    private static final boolean DEBUG = zzs.DEBUG;
    private final BlockingQueue<zzk<?>> zzh;
    private final BlockingQueue<zzk<?>> zzi;
    private final zzb zzj;
    private final zzn zzk;
    private volatile boolean zzl = false;

    public zzc(BlockingQueue<zzk<?>> blockingQueue, BlockingQueue<zzk<?>> blockingQueue2, zzb zzbVar, zzn zznVar) {
        this.zzh = blockingQueue;
        this.zzi = blockingQueue2;
        this.zzj = zzbVar;
        this.zzk = zznVar;
    }

    public void quit() {
        this.zzl = true;
        interrupt();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws InterruptedException, SecurityException, IllegalArgumentException {
        if (DEBUG) {
            zzs.zza("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(10);
        this.zzj.zza();
        while (true) {
            try {
                final zzk<?> zzkVarTake = this.zzh.take();
                zzkVarTake.zzc("cache-queue-take");
                if (zzkVarTake.isCanceled()) {
                    zzkVarTake.zzd("cache-discard-canceled");
                } else {
                    zzb.zza zzaVarZza = this.zzj.zza(zzkVarTake.zzh());
                    if (zzaVarZza == null) {
                        zzkVarTake.zzc("cache-miss");
                        this.zzi.put(zzkVarTake);
                    } else if (zzaVarZza.zzb()) {
                        zzkVarTake.zzc("cache-hit-expired");
                        zzkVarTake.zza(zzaVarZza);
                        this.zzi.put(zzkVarTake);
                    } else {
                        zzkVarTake.zzc("cache-hit");
                        zzm<?> zzmVarZza = zzkVarTake.zza(new zzi(zzaVarZza.data, zzaVarZza.zzg));
                        zzkVarTake.zzc("cache-hit-parsed");
                        if (zzaVarZza.zzc()) {
                            zzkVarTake.zzc("cache-hit-refresh-needed");
                            zzkVarTake.zza(zzaVarZza);
                            zzmVarZza.zzai = true;
                            this.zzk.zza(zzkVarTake, zzmVarZza, new Runnable() { // from class: com.google.android.gms.internal.zzc.1
                                @Override // java.lang.Runnable
                                public void run() throws InterruptedException {
                                    try {
                                        zzc.this.zzi.put(zzkVarTake);
                                    } catch (InterruptedException e) {
                                    }
                                }
                            });
                        } else {
                            this.zzk.zza(zzkVarTake, zzmVarZza);
                        }
                    }
                }
            } catch (InterruptedException e) {
                if (this.zzl) {
                    return;
                }
            }
        }
    }
}
