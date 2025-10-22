package com.google.android.gms.internal;

import android.net.TrafficStats;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import java.util.concurrent.BlockingQueue;

/* loaded from: classes.dex */
public class zzg extends Thread {
    private final zzb zzj;
    private final zzn zzk;
    private volatile boolean zzl = false;
    private final BlockingQueue<zzk<?>> zzy;
    private final zzf zzz;

    public zzg(BlockingQueue<zzk<?>> blockingQueue, zzf zzfVar, zzb zzbVar, zzn zznVar) {
        this.zzy = blockingQueue;
        this.zzz = zzfVar;
        this.zzj = zzbVar;
        this.zzk = zznVar;
    }

    private void zzb(zzk<?> zzkVar) {
        if (Build.VERSION.SDK_INT >= 14) {
            TrafficStats.setThreadStatsTag(zzkVar.zzg());
        }
    }

    private void zzb(zzk<?> zzkVar, zzr zzrVar) {
        this.zzk.zza(zzkVar, zzkVar.zzb(zzrVar));
    }

    public void quit() {
        this.zzl = true;
        interrupt();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws InterruptedException, SecurityException, IllegalArgumentException {
        Process.setThreadPriority(10);
        while (true) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            try {
                zzk<?> zzkVarTake = this.zzy.take();
                try {
                    zzkVarTake.zzc("network-queue-take");
                    if (zzkVarTake.isCanceled()) {
                        zzkVarTake.zzd("network-discard-cancelled");
                    } else {
                        zzb(zzkVarTake);
                        zzi zziVarZza = this.zzz.zza(zzkVarTake);
                        zzkVarTake.zzc("network-http-complete");
                        if (zziVarZza.zzB && zzkVarTake.zzw()) {
                            zzkVarTake.zzd("not-modified");
                        } else {
                            zzm<?> zzmVarZza = zzkVarTake.zza(zziVarZza);
                            zzkVarTake.zzc("network-parse-complete");
                            if (zzkVarTake.zzr() && zzmVarZza.zzag != null) {
                                this.zzj.zza(zzkVarTake.zzh(), zzmVarZza.zzag);
                                zzkVarTake.zzc("network-cache-written");
                            }
                            zzkVarTake.zzv();
                            this.zzk.zza(zzkVarTake, zzmVarZza);
                        }
                    }
                } catch (zzr e) {
                    e.zza(SystemClock.elapsedRealtime() - jElapsedRealtime);
                    zzb(zzkVarTake, e);
                } catch (Exception e2) {
                    zzs.zza(e2, "Unhandled exception %s", e2.toString());
                    zzr zzrVar = new zzr(e2);
                    zzrVar.zza(SystemClock.elapsedRealtime() - jElapsedRealtime);
                    this.zzk.zza(zzkVarTake, zzrVar);
                }
            } catch (InterruptedException e3) {
                if (this.zzl) {
                    return;
                }
            }
        }
    }
}
