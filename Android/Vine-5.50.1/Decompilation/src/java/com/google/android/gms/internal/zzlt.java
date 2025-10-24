package com.google.android.gms.internal;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.clearcut.LogEventParcelable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlv;
import com.google.android.gms.internal.zzlx;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class zzlt implements com.google.android.gms.clearcut.zzb {
    private static final Object zzadD = new Object();
    private static final zze zzadE = new zze();
    private static final long zzadF = TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
    private GoogleApiClient zzYC;
    private final zza zzadG;
    private final Object zzadH;
    private long zzadI;
    private final long zzadJ;
    private ScheduledFuture<?> zzadK;
    private final Runnable zzadL;
    private final zznl zzqD;

    public interface zza {
    }

    public static class zzb implements zza {
    }

    static abstract class zzc<R extends Result> extends zzlx.zza<R, zzlu> {
        public zzc(GoogleApiClient googleApiClient) {
            super(com.google.android.gms.clearcut.zza.zzTo, googleApiClient);
        }
    }

    final class zzd extends zzc<Status> {
        private final LogEventParcelable zzadN;

        zzd(LogEventParcelable logEventParcelable, GoogleApiClient googleApiClient) {
            super(googleApiClient);
            this.zzadN = logEventParcelable;
        }

        public boolean equals(Object rhs) {
            if (rhs instanceof zzd) {
                return this.zzadN.equals(((zzd) rhs).zzadN);
            }
            return false;
        }

        public String toString() {
            return "MethodImpl(" + this.zzadN + ")";
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.zzlx.zza
        public void zza(zzlu zzluVar) throws RemoteException {
            zzlv.zza zzaVar = new zzlv.zza() { // from class: com.google.android.gms.internal.zzlt.zzd.1
                @Override // com.google.android.gms.internal.zzlv
                public void zzv(Status status) {
                    zzd.this.zzb((zzd) status);
                }
            };
            try {
                zzlt.zza(this.zzadN);
                zzluVar.zza(zzaVar, this.zzadN);
            } catch (Throwable th) {
                Log.e("ClearcutLoggerApiImpl", "MessageNanoProducer " + this.zzadN.zzadB.toString() + " threw: " + th.toString());
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.zzly
        /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
        public Status zzc(Status status) {
            return status;
        }
    }

    private static final class zze {
        private int mSize;

        private zze() {
            this.mSize = 0;
        }

        public boolean zza(long j, TimeUnit timeUnit) throws InterruptedException {
            boolean z;
            long jCurrentTimeMillis = System.currentTimeMillis();
            long jConvert = TimeUnit.MILLISECONDS.convert(j, timeUnit);
            synchronized (this) {
                while (true) {
                    if (this.mSize == 0) {
                        z = true;
                        break;
                    }
                    if (jConvert <= 0) {
                        z = false;
                        break;
                    }
                    wait(jConvert);
                    jConvert -= System.currentTimeMillis() - jCurrentTimeMillis;
                }
            }
            return z;
        }

        public synchronized void zzop() {
            this.mSize++;
        }

        public synchronized void zzoq() {
            if (this.mSize == 0) {
                throw new RuntimeException("too many decrements");
            }
            this.mSize--;
            if (this.mSize == 0) {
                notifyAll();
            }
        }
    }

    public zzlt() {
        this(new zzno(), zzadF, new zzb());
    }

    public zzlt(zznl zznlVar, long j, zza zzaVar) {
        this.zzadH = new Object();
        this.zzadI = 0L;
        this.zzadK = null;
        this.zzYC = null;
        this.zzadL = new Runnable() { // from class: com.google.android.gms.internal.zzlt.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (zzlt.this.zzadH) {
                    if (zzlt.this.zzadI <= zzlt.this.zzqD.elapsedRealtime() && zzlt.this.zzYC != null) {
                        Log.i("ClearcutLoggerApiImpl", "disconnect managed GoogleApiClient");
                        zzlt.this.zzYC.disconnect();
                        zzlt.this.zzYC = null;
                    }
                }
            }
        };
        this.zzqD = zznlVar;
        this.zzadJ = j;
        this.zzadG = zzaVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void zza(LogEventParcelable logEventParcelable) {
        if (logEventParcelable.zzadB != null && logEventParcelable.zzadA.zzbqD.length == 0) {
            logEventParcelable.zzadA.zzbqD = logEventParcelable.zzadB.zzon();
        }
        if (logEventParcelable.zzadC != null && logEventParcelable.zzadA.zzbqK.length == 0) {
            logEventParcelable.zzadA.zzbqK = logEventParcelable.zzadC.zzon();
        }
        logEventParcelable.zzady = zztk.toByteArray(logEventParcelable.zzadA);
    }

    private zzd zzb(GoogleApiClient googleApiClient, LogEventParcelable logEventParcelable) {
        zzadE.zzop();
        zzd zzdVar = new zzd(logEventParcelable, googleApiClient);
        zzdVar.zza(new PendingResult.zza() { // from class: com.google.android.gms.internal.zzlt.2
            @Override // com.google.android.gms.common.api.PendingResult.zza
            public void zzu(Status status) {
                zzlt.zzadE.zzoq();
            }
        });
        return zzdVar;
    }

    @Override // com.google.android.gms.clearcut.zzb
    public PendingResult<Status> zza(GoogleApiClient googleApiClient, LogEventParcelable logEventParcelable) {
        zza(logEventParcelable);
        return googleApiClient.zza((GoogleApiClient) zzb(googleApiClient, logEventParcelable));
    }

    @Override // com.google.android.gms.clearcut.zzb
    public boolean zza(GoogleApiClient googleApiClient, long j, TimeUnit timeUnit) {
        try {
            return zzadE.zza(j, timeUnit);
        } catch (InterruptedException e) {
            Log.e("ClearcutLoggerApiImpl", "flush interrupted");
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
