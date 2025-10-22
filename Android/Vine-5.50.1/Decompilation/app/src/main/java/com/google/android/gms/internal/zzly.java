package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes.dex */
public abstract class zzly<R extends Result> extends PendingResult<R> {
    private boolean zzL;
    private volatile R zzaeT;
    protected final zza<R> zzafe;
    private ResultCallback<? super R> zzafg;
    private volatile boolean zzafh;
    private boolean zzafi;
    private com.google.android.gms.common.internal.zzq zzafj;
    private Integer zzafk;
    private volatile zzms<R> zzafl;
    private final Object zzafd = new Object();
    private final CountDownLatch zzpy = new CountDownLatch(1);
    private final ArrayList<PendingResult.zza> zzaff = new ArrayList<>();

    /* loaded from: classes2.dex */
    public static class zza<R extends Result> extends Handler {
        public zza() {
            this(Looper.getMainLooper());
        }

        public zza(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Pair pair = (Pair) msg.obj;
                    zzb((ResultCallback) pair.first, (Result) pair.second);
                    break;
                case 2:
                    ((zzly) msg.obj).zzy(Status.zzafa);
                    break;
                default:
                    Log.wtf("BasePendingResult", "Don't know how to handle message: " + msg.what, new Exception());
                    break;
            }
        }

        public void zza(ResultCallback<? super R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }

        protected void zzb(ResultCallback<? super R> resultCallback, R r) {
            try {
                resultCallback.onResult(r);
            } catch (RuntimeException e) {
                zzly.zzd(r);
                throw e;
            }
        }

        public void zzoS() {
            removeMessages(2);
        }
    }

    @Deprecated
    protected zzly(Looper looper) {
        this.zzafe = new zza<>(looper);
    }

    private R get() {
        R r;
        synchronized (this.zzafd) {
            com.google.android.gms.common.internal.zzx.zza(this.zzafh ? false : true, "Result has already been consumed.");
            com.google.android.gms.common.internal.zzx.zza(isReady(), "Result is not ready.");
            r = this.zzaeT;
            this.zzaeT = null;
            this.zzafg = null;
            this.zzafh = true;
        }
        zzoR();
        return r;
    }

    private void zzc(R r) {
        this.zzaeT = r;
        this.zzafj = null;
        this.zzpy.countDown();
        Status status = this.zzaeT.getStatus();
        if (this.zzafg != null) {
            this.zzafe.zzoS();
            if (!this.zzL) {
                this.zzafe.zza(this.zzafg, get());
            }
        }
        Iterator<PendingResult.zza> it = this.zzaff.iterator();
        while (it.hasNext()) {
            it.next().zzu(status);
        }
        this.zzaff.clear();
    }

    public static void zzd(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                Log.w("BasePendingResult", "Unable to release " + result, e);
            }
        }
    }

    public void cancel() {
        synchronized (this.zzafd) {
            if (this.zzL || this.zzafh) {
                return;
            }
            if (this.zzafj != null) {
                try {
                    this.zzafj.cancel();
                } catch (RemoteException e) {
                }
            }
            zzd(this.zzaeT);
            this.zzafg = null;
            this.zzL = true;
            zzc((zzly<R>) zzc(Status.zzafb));
        }
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this.zzafd) {
            z = this.zzL;
        }
        return z;
    }

    public final boolean isReady() {
        return this.zzpy.getCount() == 0;
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public final void setResultCallback(ResultCallback<? super R> callback) {
        com.google.android.gms.common.internal.zzx.zza(!this.zzafh, "Result has already been consumed.");
        synchronized (this.zzafd) {
            com.google.android.gms.common.internal.zzx.zza(this.zzafl == null, "Cannot set callbacks if then() has been called.");
            if (isCanceled()) {
                return;
            }
            if (isReady()) {
                this.zzafe.zza(callback, get());
            } else {
                this.zzafg = callback;
            }
        }
    }

    @Override // com.google.android.gms.common.api.PendingResult
    public final void zza(PendingResult.zza zzaVar) {
        com.google.android.gms.common.internal.zzx.zza(!this.zzafh, "Result has already been consumed.");
        com.google.android.gms.common.internal.zzx.zzb(zzaVar != null, "Callback cannot be null.");
        synchronized (this.zzafd) {
            if (isReady()) {
                zzaVar.zzu(this.zzaeT.getStatus());
            } else {
                this.zzaff.add(zzaVar);
            }
        }
    }

    public final void zzb(R r) {
        synchronized (this.zzafd) {
            if (this.zzafi || this.zzL) {
                zzd(r);
                return;
            }
            com.google.android.gms.common.internal.zzx.zza(!isReady(), "Results have already been set");
            com.google.android.gms.common.internal.zzx.zza(this.zzafh ? false : true, "Result has already been consumed");
            zzc((zzly<R>) r);
        }
    }

    protected abstract R zzc(Status status);

    @Override // com.google.android.gms.common.api.PendingResult
    public Integer zzoL() {
        return this.zzafk;
    }

    protected void zzoR() {
    }

    public final void zzy(Status status) {
        synchronized (this.zzafd) {
            if (!isReady()) {
                zzb(zzc(status));
                this.zzafi = true;
            }
        }
    }
}
