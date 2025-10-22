package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlx;
import com.google.android.gms.internal.zzmm;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

/* loaded from: classes2.dex */
public class zzma implements zzmm {
    private final Context mContext;
    private final Looper zzaeK;
    private final zzmg zzafp;
    private final zzmi zzafq;
    private final zzmi zzafr;
    private final Api.zzb zzafu;
    private Bundle zzafv;
    private final Lock zzafz;
    private final Map<Api.zzc<?>, zzmi> zzafs = new ArrayMap();
    private final Set<zzmp> zzaft = Collections.newSetFromMap(new WeakHashMap());
    private ConnectionResult zzafw = null;
    private ConnectionResult zzafx = null;
    private final AtomicInteger zzafy = new AtomicInteger(0);
    private int zzafA = 0;

    public zzma(Context context, zzmg zzmgVar, Lock lock, Looper looper, GoogleApiAvailability googleApiAvailability, Map<Api.zzc<?>, Api.zzb> map, com.google.android.gms.common.internal.zzf zzfVar, Map<Api<?>, Integer> map2, Api.zza<? extends zzsc, zzsd> zzaVar, ArrayList<zzlz> arrayList) {
        this.mContext = context;
        this.zzafp = zzmgVar;
        this.zzafz = lock;
        this.zzaeK = looper;
        Api.zzb zzbVar = null;
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        for (Api.zzc<?> zzcVar : map.keySet()) {
            Api.zzb zzbVar2 = map.get(zzcVar);
            zzbVar = zzbVar2.zzmJ() ? zzbVar2 : zzbVar;
            if (zzbVar2.zzmn()) {
                arrayMap.put(zzcVar, zzbVar2);
            } else {
                arrayMap2.put(zzcVar, zzbVar2);
            }
        }
        this.zzafu = zzbVar;
        if (arrayMap.isEmpty()) {
            throw new IllegalStateException("CompositeGoogleApiClient should not be used without any APIs that require sign-in.");
        }
        ArrayMap arrayMap3 = new ArrayMap();
        ArrayMap arrayMap4 = new ArrayMap();
        for (Api<?> api : map2.keySet()) {
            Api.zzc<?> zzcVarZzoA = api.zzoA();
            if (arrayMap.containsKey(zzcVarZzoA)) {
                arrayMap3.put(api, map2.get(api));
            } else {
                if (!arrayMap2.containsKey(zzcVarZzoA)) {
                    throw new IllegalStateException("Each API in the apiTypeMap must have a corresponding client in the clients map.");
                }
                arrayMap4.put(api, map2.get(api));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        Iterator<zzlz> it = arrayList.iterator();
        while (it.hasNext()) {
            zzlz next = it.next();
            if (arrayMap3.containsKey(next.zzafm)) {
                arrayList2.add(next);
            } else {
                if (!arrayMap4.containsKey(next.zzafm)) {
                    throw new IllegalStateException("Each ClientCallbacks must have a corresponding API in the apiTypeMap");
                }
                arrayList3.add(next);
            }
        }
        this.zzafq = new zzmi(context, this.zzafp, lock, looper, googleApiAvailability, arrayMap2, null, arrayMap4, null, arrayList3, new zzmm.zza() { // from class: com.google.android.gms.internal.zzma.1
            @Override // com.google.android.gms.internal.zzmm.zza
            public void zzbz(int i) {
                zzma.this.zzafz.lock();
                try {
                    zzma.this.zza(zzma.this.zzafr, i);
                } finally {
                    zzma.this.zzafz.unlock();
                }
            }

            @Override // com.google.android.gms.internal.zzmm.zza
            public void zze(ConnectionResult connectionResult) {
                zzma.this.zzafz.lock();
                try {
                    zzma.this.zzafw = connectionResult;
                    zzma.this.zzoW();
                } finally {
                    zzma.this.zzafz.unlock();
                }
            }

            @Override // com.google.android.gms.internal.zzmm.zza
            public void zzi(Bundle bundle) {
                zzma.this.zzafz.lock();
                try {
                    zzma.this.zzh(bundle);
                    zzma.this.zzafw = ConnectionResult.zzadR;
                    zzma.this.zzoW();
                } finally {
                    zzma.this.zzafz.unlock();
                }
            }
        });
        this.zzafr = new zzmi(context, this.zzafp, lock, looper, googleApiAvailability, arrayMap, zzfVar, arrayMap3, zzaVar, arrayList2, new zzmm.zza() { // from class: com.google.android.gms.internal.zzma.2
            @Override // com.google.android.gms.internal.zzmm.zza
            public void zzbz(int i) {
                zzma.this.zzafz.lock();
                try {
                    zzma.this.zza(zzma.this.zzafq, i);
                } finally {
                    zzma.this.zzafz.unlock();
                }
            }

            @Override // com.google.android.gms.internal.zzmm.zza
            public void zze(ConnectionResult connectionResult) {
                zzma.this.zzafz.lock();
                try {
                    zzma.this.zzafx = connectionResult;
                    zzma.this.zzoW();
                } finally {
                    zzma.this.zzafz.unlock();
                }
            }

            @Override // com.google.android.gms.internal.zzmm.zza
            public void zzi(Bundle bundle) {
                zzma.this.zzafz.lock();
                try {
                    zzma.this.zzafx = ConnectionResult.zzadR;
                    zzma.this.zzoW();
                } finally {
                    zzma.this.zzafz.unlock();
                }
            }
        });
        Iterator it2 = arrayMap2.keySet().iterator();
        while (it2.hasNext()) {
            this.zzafs.put((Api.zzc) it2.next(), this.zzafq);
        }
        Iterator it3 = arrayMap.keySet().iterator();
        while (it3.hasNext()) {
            this.zzafs.put((Api.zzc) it3.next(), this.zzafr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zza(zzmi zzmiVar, int i) {
        if (this.zzafy.getAndIncrement() % 2 == 1) {
            this.zzafp.zzbz(i);
        }
        zzmiVar.onConnectionSuspended(i);
        this.zzafx = null;
        this.zzafw = null;
    }

    private void zzc(ConnectionResult connectionResult) {
        switch (this.zzafA) {
            case 2:
                this.zzafp.zze(connectionResult);
            case 1:
                zzoY();
                break;
            default:
                Log.wtf("CompositeGAC", "Attempted to call failure callbacks in CALLBACK_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new Exception());
                break;
        }
        this.zzafA = 0;
    }

    private boolean zzc(zzlx.zza<? extends Result, ? extends Api.zzb> zzaVar) {
        Object objZzoA = zzaVar.zzoA();
        com.google.android.gms.common.internal.zzx.zzb(this.zzafs.containsKey(objZzoA), "GoogleApiClient is not configured to use the API required for this call.");
        return this.zzafs.get(objZzoA).equals(this.zzafr);
    }

    private static boolean zzd(ConnectionResult connectionResult) {
        return connectionResult != null && connectionResult.isSuccess();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzh(Bundle bundle) {
        if (this.zzafv == null) {
            this.zzafv = bundle;
        } else if (bundle != null) {
            this.zzafv.putAll(bundle);
        }
    }

    private void zzoV() {
        this.zzafx = null;
        this.zzafw = null;
        this.zzafq.connect();
        this.zzafr.connect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzoW() {
        if (zzd(this.zzafw)) {
            if (zzd(this.zzafx) || zzoZ()) {
                zzoX();
                return;
            }
            if (this.zzafx != null) {
                if (this.zzafA == 1) {
                    zzoY();
                    return;
                } else {
                    zzc(this.zzafx);
                    this.zzafq.disconnect();
                    return;
                }
            }
            return;
        }
        if (this.zzafw != null && zzd(this.zzafx)) {
            this.zzafr.disconnect();
            zzc(this.zzafw);
        } else {
            if (this.zzafw == null || this.zzafx == null) {
                return;
            }
            ConnectionResult connectionResult = this.zzafw;
            if (this.zzafr.zzagM < this.zzafq.zzagM) {
                connectionResult = this.zzafx;
            }
            zzc(connectionResult);
        }
    }

    private void zzoX() {
        switch (this.zzafA) {
            case 2:
                this.zzafp.zzi(this.zzafv);
            case 1:
                zzoY();
                break;
            default:
                Log.wtf("CompositeGAC", "Attempted to call success callbacks in CALLBACK_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new Exception());
                break;
        }
        this.zzafA = 0;
    }

    private void zzoY() {
        Iterator<zzmp> it = this.zzaft.iterator();
        while (it.hasNext()) {
            it.next().zzmI();
        }
        this.zzaft.clear();
    }

    private boolean zzoZ() {
        return this.zzafx != null && this.zzafx.getErrorCode() == 4;
    }

    private PendingIntent zzpa() {
        if (this.zzafu == null) {
            return null;
        }
        return PendingIntent.getActivity(this.mContext, this.zzafp.getSessionId(), this.zzafu.zzmK(), 134217728);
    }

    @Override // com.google.android.gms.internal.zzmm
    public void connect() {
        this.zzafA = 2;
        zzoV();
    }

    @Override // com.google.android.gms.internal.zzmm
    public void disconnect() {
        this.zzafx = null;
        this.zzafw = null;
        this.zzafA = 0;
        this.zzafq.disconnect();
        this.zzafr.disconnect();
        zzoY();
    }

    @Override // com.google.android.gms.internal.zzmm
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.append((CharSequence) prefix).append("authClient").println(":");
        this.zzafr.dump(prefix + "  ", fd, writer, args);
        writer.append((CharSequence) prefix).append("unauthClient").println(":");
        this.zzafq.dump(prefix + "  ", fd, writer, args);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0024  */
    @Override // com.google.android.gms.internal.zzmm
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isConnected() {
        /*
            r2 = this;
            r0 = 1
            java.util.concurrent.locks.Lock r1 = r2.zzafz
            r1.lock()
            com.google.android.gms.internal.zzmi r1 = r2.zzafq     // Catch: java.lang.Throwable -> L26
            boolean r1 = r1.isConnected()     // Catch: java.lang.Throwable -> L26
            if (r1 == 0) goto L24
            boolean r1 = r2.zzoU()     // Catch: java.lang.Throwable -> L26
            if (r1 != 0) goto L1e
            boolean r1 = r2.zzoZ()     // Catch: java.lang.Throwable -> L26
            if (r1 != 0) goto L1e
            int r1 = r2.zzafA     // Catch: java.lang.Throwable -> L26
            if (r1 != r0) goto L24
        L1e:
            java.util.concurrent.locks.Lock r1 = r2.zzafz
            r1.unlock()
            return r0
        L24:
            r0 = 0
            goto L1e
        L26:
            r0 = move-exception
            java.util.concurrent.locks.Lock r1 = r2.zzafz
            r1.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzma.isConnected():boolean");
    }

    @Override // com.google.android.gms.internal.zzmm
    public <A extends Api.zzb, R extends Result, T extends zzlx.zza<R, A>> T zza(T t) {
        if (t.zzoQ() == 1) {
            throw new IllegalStateException("ReportingServices.getReportingState is not supported with SIGN_IN_MODE_OPTIONAL.");
        }
        if (!zzc((zzlx.zza<? extends Result, ? extends Api.zzb>) t)) {
            return (T) this.zzafq.zza((zzmi) t);
        }
        if (!zzoZ()) {
            return (T) this.zzafr.zza((zzmi) t);
        }
        t.zzx(new Status(4, null, zzpa()));
        return t;
    }

    @Override // com.google.android.gms.internal.zzmm
    public <A extends Api.zzb, T extends zzlx.zza<? extends Result, A>> T zzb(T t) {
        if (!zzc((zzlx.zza<? extends Result, ? extends Api.zzb>) t)) {
            return (T) this.zzafq.zzb((zzmi) t);
        }
        if (!zzoZ()) {
            return (T) this.zzafr.zzb((zzmi) t);
        }
        t.zzx(new Status(4, null, zzpa()));
        return t;
    }

    public boolean zzoU() {
        return this.zzafr.isConnected();
    }
}
