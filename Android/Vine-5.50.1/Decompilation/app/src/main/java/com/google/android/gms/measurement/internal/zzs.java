package com.google.android.gms.measurement.internal;

import android.content.Context;
import com.google.android.gms.internal.zznl;
import java.lang.Thread;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/* loaded from: classes.dex */
public class zzs extends zzw {
    private zzc zzaTT;
    private zzc zzaTU;
    private final BlockingQueue<FutureTask<?>> zzaTV;
    private final BlockingQueue<FutureTask<?>> zzaTW;
    private final Thread.UncaughtExceptionHandler zzaTX;
    private final Thread.UncaughtExceptionHandler zzaTY;
    private final Object zzaTZ;
    private final Semaphore zzaUa;
    private volatile boolean zzaUb;

    private final class zza<V> extends FutureTask<V> {
        private final String zzaUc;

        zza(Runnable runnable, String str) {
            super(runnable, null);
            com.google.android.gms.common.internal.zzx.zzy(str);
            this.zzaUc = str;
        }

        @Override // java.util.concurrent.FutureTask
        protected void setException(Throwable error) {
            zzs.this.zzzz().zzBl().zzj(this.zzaUc, error);
            super.setException(error);
        }
    }

    private final class zzb implements Thread.UncaughtExceptionHandler {
        private final String zzaUc;

        public zzb(String str) {
            com.google.android.gms.common.internal.zzx.zzy(str);
            this.zzaUc = str;
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public synchronized void uncaughtException(Thread thread, Throwable error) {
            zzs.this.zzzz().zzBl().zzj(this.zzaUc, error);
        }
    }

    private final class zzc extends Thread {
        private final Object zzaUe;
        private final BlockingQueue<FutureTask<?>> zzaUf;

        public zzc(String str, BlockingQueue<FutureTask<?>> blockingQueue) {
            com.google.android.gms.common.internal.zzx.zzy(str);
            this.zzaUe = new Object();
            this.zzaUf = blockingQueue;
            setName(str);
        }

        private void zza(InterruptedException interruptedException) {
            zzs.this.zzzz().zzBm().zzj(getName() + " was interrupted", interruptedException);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                FutureTask<?> futureTaskPoll = this.zzaUf.poll();
                if (futureTaskPoll == null) {
                    synchronized (this.zzaUe) {
                        if (this.zzaUf.peek() == null && !zzs.this.zzaUb) {
                            try {
                                this.zzaUe.wait(30000L);
                            } catch (InterruptedException e) {
                                zza(e);
                            }
                        }
                    }
                    synchronized (zzs.this.zzaTZ) {
                        if (this.zzaUf.peek() == null) {
                            break;
                        }
                    }
                } else {
                    futureTaskPoll.run();
                }
            }
            zzs.this.zzaUa.release();
            zzs.this.zzaTZ.notifyAll();
            if (this == zzs.this.zzaTT) {
                zzs.this.zzaTT = null;
            } else if (this == zzs.this.zzaTU) {
                zzs.this.zzaTU = null;
            } else {
                zzs.this.zzzz().zzBl().zzez("Current scheduler thread is neither worker nor network");
            }
        }

        public void zzeQ() {
            synchronized (this.zzaUe) {
                this.zzaUe.notifyAll();
            }
        }
    }

    zzs(zzt zztVar) {
        super(zztVar);
        this.zzaTZ = new Object();
        this.zzaUa = new Semaphore(2);
        this.zzaTV = new LinkedBlockingQueue();
        this.zzaTW = new LinkedBlockingQueue();
        this.zzaTX = new zzb("Thread death: Uncaught exception on worker thread");
        this.zzaTY = new zzb("Thread death: Uncaught exception on network thread");
    }

    private void zza(FutureTask<?> futureTask) {
        synchronized (this.zzaTZ) {
            this.zzaTV.add(futureTask);
            if (this.zzaTT == null) {
                this.zzaTT = new zzc("Measurement Worker", this.zzaTV);
                this.zzaTT.setUncaughtExceptionHandler(this.zzaTX);
                this.zzaTT.start();
            } else {
                this.zzaTT.zzeQ();
            }
        }
    }

    private void zzb(FutureTask<?> futureTask) {
        synchronized (this.zzaTZ) {
            this.zzaTW.add(futureTask);
            if (this.zzaTU == null) {
                this.zzaTU = new zzc("Measurement Network", this.zzaTW);
                this.zzaTU.setUncaughtExceptionHandler(this.zzaTY);
                this.zzaTU.start();
            } else {
                this.zzaTU.zzeQ();
            }
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public void zzAR() {
        if (Thread.currentThread() != this.zzaTU) {
            throw new IllegalStateException("Call expected from network thread");
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzm zzAS() {
        return super.zzAS();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzz zzAT() {
        return super.zzAT();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzae zzAU() {
        return super.zzAU();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzs zzAV() {
        return super.zzAV();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzr zzAW() {
        return super.zzAW();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ com.google.android.gms.measurement.internal.zzc zzAX() {
        return super.zzAX();
    }

    public void zzg(Runnable runnable) throws IllegalStateException {
        zzje();
        com.google.android.gms.common.internal.zzx.zzy(runnable);
        zza(new zza(runnable, "Task exception on worker thread"));
    }

    public void zzh(Runnable runnable) throws IllegalStateException {
        zzje();
        com.google.android.gms.common.internal.zzx.zzy(runnable);
        zzb(new zza(runnable, "Task exception on network thread"));
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zziR() {
        super.zziR();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public void zziS() {
        if (Thread.currentThread() != this.zzaTT) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zznl zziT() {
        return super.zziT();
    }

    @Override // com.google.android.gms.measurement.internal.zzw
    protected void zzir() {
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzo zzzz() {
        return super.zzzz();
    }
}
