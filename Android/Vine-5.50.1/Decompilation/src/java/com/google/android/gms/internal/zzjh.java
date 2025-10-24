package com.google.android.gms.internal;

import com.google.android.gms.internal.zzjg;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@zzha
/* loaded from: classes.dex */
public class zzjh<T> implements zzjg<T> {
    protected T zzLX;
    private final Object zzpK = new Object();
    protected int zzAk = 0;
    protected final BlockingQueue<zzjh<T>.zza> zzLW = new LinkedBlockingQueue();

    class zza {
        public final zzjg.zzc<T> zzLY;
        public final zzjg.zza zzLZ;

        public zza(zzjg.zzc<T> zzcVar, zzjg.zza zzaVar) {
            this.zzLY = zzcVar;
            this.zzLZ = zzaVar;
        }
    }

    public int getStatus() {
        return this.zzAk;
    }

    public void reject() {
        synchronized (this.zzpK) {
            if (this.zzAk != 0) {
                throw new UnsupportedOperationException();
            }
            this.zzAk = -1;
            Iterator it = this.zzLW.iterator();
            while (it.hasNext()) {
                ((zza) it.next()).zzLZ.run();
            }
            this.zzLW.clear();
        }
    }

    @Override // com.google.android.gms.internal.zzjg
    public void zza(zzjg.zzc<T> zzcVar, zzjg.zza zzaVar) {
        synchronized (this.zzpK) {
            if (this.zzAk == 1) {
                zzcVar.zzc(this.zzLX);
            } else if (this.zzAk == -1) {
                zzaVar.run();
            } else if (this.zzAk == 0) {
                this.zzLW.add(new zza(zzcVar, zzaVar));
            }
        }
    }

    @Override // com.google.android.gms.internal.zzjg
    public void zzg(T t) {
        synchronized (this.zzpK) {
            if (this.zzAk != 0) {
                throw new UnsupportedOperationException();
            }
            this.zzLX = t;
            this.zzAk = 1;
            Iterator it = this.zzLW.iterator();
            while (it.hasNext()) {
                ((zza) it.next()).zzLY.zzc(t);
            }
            this.zzLW.clear();
        }
    }
}
