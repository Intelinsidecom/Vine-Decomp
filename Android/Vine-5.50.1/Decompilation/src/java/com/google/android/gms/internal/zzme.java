package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.internal.zzlx;
import com.google.android.gms.internal.zzmi;
import com.google.android.gms.signin.internal.AuthAccountResult;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

/* loaded from: classes2.dex */
public class zzme implements zzmh {
    private final Context mContext;
    private final GoogleApiAvailability zzaeL;
    private final Api.zza<? extends zzsc, zzsd> zzaeM;
    private final zzmi zzafD;
    private ConnectionResult zzafF;
    private int zzafG;
    private int zzafJ;
    private zzsc zzafM;
    private int zzafN;
    private boolean zzafO;
    private boolean zzafP;
    private com.google.android.gms.common.internal.zzp zzafQ;
    private boolean zzafR;
    private boolean zzafS;
    private final com.google.android.gms.common.internal.zzf zzafT;
    private final Map<Api<?>, Integer> zzafU;
    private final Lock zzafz;
    private int zzafH = 0;
    private boolean zzafI = false;
    private final Bundle zzafK = new Bundle();
    private final Set<Api.zzc> zzafL = new HashSet();
    private ArrayList<Future<?>> zzafV = new ArrayList<>();

    private static class zza extends com.google.android.gms.signin.internal.zzb {
        private final WeakReference<zzme> zzafX;

        zza(zzme zzmeVar) {
            this.zzafX = new WeakReference<>(zzmeVar);
        }

        @Override // com.google.android.gms.signin.internal.zzb, com.google.android.gms.signin.internal.zze
        public void zza(final ConnectionResult connectionResult, AuthAccountResult authAccountResult) {
            final zzme zzmeVar = this.zzafX.get();
            if (zzmeVar == null) {
                return;
            }
            zzmeVar.zzafD.zza(new zzmi.zza(zzmeVar) { // from class: com.google.android.gms.internal.zzme.zza.1
                @Override // com.google.android.gms.internal.zzmi.zza
                public void zzpc() {
                    zzmeVar.zzf(connectionResult);
                }
            });
        }
    }

    private static class zzb extends zzt.zza {
        private final WeakReference<zzme> zzafX;

        zzb(zzme zzmeVar) {
            this.zzafX = new WeakReference<>(zzmeVar);
        }

        @Override // com.google.android.gms.common.internal.zzt
        public void zzb(final ResolveAccountResponse resolveAccountResponse) {
            final zzme zzmeVar = this.zzafX.get();
            if (zzmeVar == null) {
                return;
            }
            zzmeVar.zzafD.zza(new zzmi.zza(zzmeVar) { // from class: com.google.android.gms.internal.zzme.zzb.1
                @Override // com.google.android.gms.internal.zzmi.zza
                public void zzpc() {
                    zzmeVar.zza(resolveAccountResponse);
                }
            });
        }
    }

    private class zzc extends zzi {
        private zzc() {
            super();
        }

        @Override // com.google.android.gms.internal.zzme.zzi
        public void zzpc() {
            zzme.this.zzafM.zza(zzme.this.zzafQ, zzme.this.zzafD.zzafp.zzagq, new zza(zzme.this));
        }
    }

    private static class zzd implements GoogleApiClient.zza {
        private final WeakReference<zzme> zzafX;
        private final Api<?> zzafm;
        private final int zzafn;

        public zzd(zzme zzmeVar, Api<?> api, int i) {
            this.zzafX = new WeakReference<>(zzmeVar);
            this.zzafm = api;
            this.zzafn = i;
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.zza
        public void zza(ConnectionResult connectionResult) {
            zzme zzmeVar = this.zzafX.get();
            if (zzmeVar == null) {
                return;
            }
            com.google.android.gms.common.internal.zzx.zza(Looper.myLooper() == zzmeVar.zzafD.zzafp.getLooper(), "onReportServiceBinding must be called on the GoogleApiClient handler thread");
            zzmeVar.zzafz.lock();
            try {
                if (zzmeVar.zzbA(0)) {
                    if (!connectionResult.isSuccess()) {
                        zzmeVar.zzb(connectionResult, this.zzafm, this.zzafn);
                    }
                    if (zzmeVar.zzpd()) {
                        zzmeVar.zzpe();
                    }
                }
            } finally {
                zzmeVar.zzafz.unlock();
            }
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.zza
        public void zzb(ConnectionResult connectionResult) {
            zzme zzmeVar = this.zzafX.get();
            if (zzmeVar == null) {
                return;
            }
            com.google.android.gms.common.internal.zzx.zza(Looper.myLooper() == zzmeVar.zzafD.zzafp.getLooper(), "onReportAccountValidation must be called on the GoogleApiClient handler thread");
            zzmeVar.zzafz.lock();
            try {
                if (zzmeVar.zzbA(1)) {
                    if (!connectionResult.isSuccess()) {
                        zzmeVar.zzb(connectionResult, this.zzafm, this.zzafn);
                    }
                    if (zzmeVar.zzpd()) {
                        zzmeVar.zzpg();
                    }
                }
            } finally {
                zzmeVar.zzafz.unlock();
            }
        }
    }

    private class zze extends zzi {
        private final Map<Api.zzb, GoogleApiClient.zza> zzagd;

        public zze(Map<Api.zzb, GoogleApiClient.zza> map) {
            super();
            this.zzagd = map;
        }

        @Override // com.google.android.gms.internal.zzme.zzi
        public void zzpc() {
            int iIsGooglePlayServicesAvailable = zzme.this.zzaeL.isGooglePlayServicesAvailable(zzme.this.mContext);
            if (iIsGooglePlayServicesAvailable != 0) {
                final ConnectionResult connectionResult = new ConnectionResult(iIsGooglePlayServicesAvailable, null);
                zzme.this.zzafD.zza(new zzmi.zza(zzme.this) { // from class: com.google.android.gms.internal.zzme.zze.1
                    @Override // com.google.android.gms.internal.zzmi.zza
                    public void zzpc() {
                        zzme.this.zzi(connectionResult);
                    }
                });
                return;
            }
            if (zzme.this.zzafO) {
                zzme.this.zzafM.connect();
            }
            for (Api.zzb zzbVar : this.zzagd.keySet()) {
                zzbVar.zza(this.zzagd.get(zzbVar));
            }
        }
    }

    private class zzf extends zzi {
        private final ArrayList<Api.zzb> zzagg;

        public zzf(ArrayList<Api.zzb> arrayList) {
            super();
            this.zzagg = arrayList;
        }

        @Override // com.google.android.gms.internal.zzme.zzi
        public void zzpc() {
            if (zzme.this.zzafD.zzafp.zzagq.isEmpty()) {
                zzme.this.zzafD.zzafp.zzagq = zzme.this.zzpl();
            }
            Iterator<Api.zzb> it = this.zzagg.iterator();
            while (it.hasNext()) {
                it.next().zza(zzme.this.zzafQ, zzme.this.zzafD.zzafp.zzagq);
            }
        }
    }

    private class zzg implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        private zzg() {
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
        public void onConnected(Bundle connectionHint) {
            zzme.this.zzafM.zza(new zzb(zzme.this));
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
        public void onConnectionFailed(ConnectionResult result) {
            zzme.this.zzafz.lock();
            try {
                if (zzme.this.zzh(result)) {
                    zzme.this.zzpj();
                    zzme.this.zzph();
                } else {
                    zzme.this.zzi(result);
                }
            } finally {
                zzme.this.zzafz.unlock();
            }
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
        public void onConnectionSuspended(int cause) {
        }
    }

    private class zzh extends zzi {
        private final ArrayList<Api.zzb> zzagg;

        public zzh(ArrayList<Api.zzb> arrayList) {
            super();
            this.zzagg = arrayList;
        }

        @Override // com.google.android.gms.internal.zzme.zzi
        public void zzpc() {
            Iterator<Api.zzb> it = this.zzagg.iterator();
            while (it.hasNext()) {
                it.next().zza(zzme.this.zzafQ);
            }
        }
    }

    private abstract class zzi implements Runnable {
        private zzi() {
        }

        @Override // java.lang.Runnable
        public void run() {
            zzme.this.zzafz.lock();
            try {
                if (Thread.interrupted()) {
                    return;
                }
                zzpc();
            } catch (RuntimeException e) {
                zzme.this.zzafD.zza(e);
            } finally {
                zzme.this.zzafz.unlock();
            }
        }

        protected abstract void zzpc();
    }

    public zzme(zzmi zzmiVar, com.google.android.gms.common.internal.zzf zzfVar, Map<Api<?>, Integer> map, GoogleApiAvailability googleApiAvailability, Api.zza<? extends zzsc, zzsd> zzaVar, Lock lock, Context context) {
        this.zzafD = zzmiVar;
        this.zzafT = zzfVar;
        this.zzafU = map;
        this.zzaeL = googleApiAvailability;
        this.zzaeM = zzaVar;
        this.zzafz = lock;
        this.mContext = context;
    }

    private void zzZ(boolean z) {
        if (this.zzafM != null) {
            if (this.zzafM.isConnected() && z) {
                this.zzafM.zzDL();
            }
            this.zzafM.disconnect();
            this.zzafQ = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zza(ResolveAccountResponse resolveAccountResponse) {
        if (zzbA(0)) {
            ConnectionResult connectionResultZzqI = resolveAccountResponse.zzqI();
            if (connectionResultZzqI.isSuccess()) {
                this.zzafQ = resolveAccountResponse.zzqH();
                this.zzafP = true;
                this.zzafR = resolveAccountResponse.zzqJ();
                this.zzafS = resolveAccountResponse.zzqK();
                zzpe();
                return;
            }
            if (!zzh(connectionResultZzqI)) {
                zzi(connectionResultZzqI);
            } else {
                zzpj();
                zzpe();
            }
        }
    }

    private boolean zza(int i, int i2, ConnectionResult connectionResult) {
        if (i2 != 1 || zzg(connectionResult)) {
            return this.zzafF == null || i < this.zzafG;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzb(ConnectionResult connectionResult, Api<?> api, int i) {
        if (i != 2) {
            int priority = api.zzoy().getPriority();
            if (zza(priority, i, connectionResult)) {
                this.zzafF = connectionResult;
                this.zzafG = priority;
            }
        }
        this.zzafD.zzagJ.put(api.zzoA(), connectionResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zzbA(int i) {
        if (this.zzafH == i) {
            return true;
        }
        Log.i("GoogleApiClientConnecting", this.zzafD.zzafp.zzpt());
        Log.wtf("GoogleApiClientConnecting", "GoogleApiClient connecting is in step " + zzbB(this.zzafH) + " but received callback for step " + zzbB(i), new Exception());
        zzi(new ConnectionResult(8, null));
        return false;
    }

    private String zzbB(int i) {
        switch (i) {
            case 0:
                return "STEP_GETTING_SERVICE_BINDINGS";
            case 1:
                return "STEP_VALIDATING_ACCOUNT";
            case 2:
                return "STEP_AUTHENTICATING";
            case 3:
                return "STEP_GETTING_REMOTE_SERVICE";
            default:
                return "UNKNOWN";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzf(ConnectionResult connectionResult) {
        if (zzbA(2)) {
            if (connectionResult.isSuccess()) {
                zzph();
            } else if (!zzh(connectionResult)) {
                zzi(connectionResult);
            } else {
                zzpj();
                zzph();
            }
        }
    }

    private boolean zzg(ConnectionResult connectionResult) {
        return connectionResult.hasResolution() || this.zzaeL.zzbu(connectionResult.getErrorCode()) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zzh(ConnectionResult connectionResult) {
        if (this.zzafN != 2) {
            return this.zzafN == 1 && !connectionResult.hasResolution();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzi(ConnectionResult connectionResult) {
        zzpk();
        zzZ(!connectionResult.hasResolution());
        this.zzafD.zzj(connectionResult);
        if (!this.zzafI) {
            this.zzafD.zzagN.zze(connectionResult);
        }
        this.zzafI = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zzpd() {
        this.zzafJ--;
        if (this.zzafJ > 0) {
            return false;
        }
        if (this.zzafJ < 0) {
            Log.i("GoogleApiClientConnecting", this.zzafD.zzafp.zzpt());
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", new Exception());
            zzi(new ConnectionResult(8, null));
            return false;
        }
        if (this.zzafF == null) {
            return true;
        }
        this.zzafD.zzagM = this.zzafG;
        zzi(this.zzafF);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzpe() {
        if (this.zzafJ != 0) {
            return;
        }
        if (!this.zzafO) {
            zzph();
        } else if (this.zzafP) {
            zzpf();
        }
    }

    private void zzpf() {
        ArrayList arrayList = new ArrayList();
        this.zzafH = 1;
        this.zzafJ = this.zzafD.zzagp.size();
        for (Api.zzc<?> zzcVar : this.zzafD.zzagp.keySet()) {
            if (!this.zzafD.zzagJ.containsKey(zzcVar)) {
                arrayList.add(this.zzafD.zzagp.get(zzcVar));
            } else if (zzpd()) {
                zzpg();
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        this.zzafV.add(zzmj.zzpz().submit(new zzh(arrayList)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzpg() {
        this.zzafH = 2;
        this.zzafD.zzafp.zzagq = zzpl();
        this.zzafV.add(zzmj.zzpz().submit(new zzc()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzph() {
        ArrayList arrayList = new ArrayList();
        this.zzafH = 3;
        this.zzafJ = this.zzafD.zzagp.size();
        for (Api.zzc<?> zzcVar : this.zzafD.zzagp.keySet()) {
            if (!this.zzafD.zzagJ.containsKey(zzcVar)) {
                arrayList.add(this.zzafD.zzagp.get(zzcVar));
            } else if (zzpd()) {
                zzpi();
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        this.zzafV.add(zzmj.zzpz().submit(new zzf(arrayList)));
    }

    private void zzpi() {
        this.zzafD.zzpx();
        zzmj.zzpz().execute(new Runnable() { // from class: com.google.android.gms.internal.zzme.1
            @Override // java.lang.Runnable
            public void run() {
                zzme.this.zzaeL.zzaj(zzme.this.mContext);
            }
        });
        if (this.zzafM != null) {
            if (this.zzafR) {
                this.zzafM.zza(this.zzafQ, this.zzafS);
            }
            zzZ(false);
        }
        Iterator<Api.zzc<?>> it = this.zzafD.zzagJ.keySet().iterator();
        while (it.hasNext()) {
            this.zzafD.zzagp.get(it.next()).disconnect();
        }
        this.zzafD.zzagN.zzi(this.zzafK.isEmpty() ? null : this.zzafK);
        if (this.zzafI) {
            this.zzafI = false;
            disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzpj() {
        this.zzafO = false;
        this.zzafD.zzafp.zzagq = Collections.emptySet();
        for (Api.zzc<?> zzcVar : this.zzafL) {
            if (!this.zzafD.zzagJ.containsKey(zzcVar)) {
                this.zzafD.zzagJ.put(zzcVar, new ConnectionResult(17, null));
            }
        }
    }

    private void zzpk() {
        Iterator<Future<?>> it = this.zzafV.iterator();
        while (it.hasNext()) {
            it.next().cancel(true);
        }
        this.zzafV.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<Scope> zzpl() {
        if (this.zzafT == null) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet(this.zzafT.zzqa());
        Map<Api<?>, zzf.zza> mapZzqc = this.zzafT.zzqc();
        for (Api<?> api : mapZzqc.keySet()) {
            if (!this.zzafD.zzagJ.containsKey(api.zzoA())) {
                hashSet.addAll(mapZzqc.get(api).zzVH);
            }
        }
        return hashSet;
    }

    @Override // com.google.android.gms.internal.zzmh
    public void begin() {
        this.zzafD.zzagJ.clear();
        this.zzafI = false;
        this.zzafO = false;
        this.zzafF = null;
        this.zzafH = 0;
        this.zzafN = 2;
        this.zzafP = false;
        this.zzafR = false;
        HashMap map = new HashMap();
        boolean z = false;
        for (Api<?> api : this.zzafU.keySet()) {
            Api.zzb zzbVar = this.zzafD.zzagp.get(api.zzoA());
            int iIntValue = this.zzafU.get(api).intValue();
            boolean z2 = (api.zzoy().getPriority() == 1) | z;
            if (zzbVar.zzmn()) {
                this.zzafO = true;
                if (iIntValue < this.zzafN) {
                    this.zzafN = iIntValue;
                }
                if (iIntValue != 0) {
                    this.zzafL.add(api.zzoA());
                }
            }
            map.put(zzbVar, new zzd(this, api, iIntValue));
            z = z2;
        }
        if (z) {
            this.zzafO = false;
        }
        if (this.zzafO) {
            this.zzafT.zza(Integer.valueOf(this.zzafD.zzafp.getSessionId()));
            zzg zzgVar = new zzg();
            this.zzafM = (zzsc) this.zzaeM.zza(this.mContext, this.zzafD.zzafp.getLooper(), this.zzafT, this.zzafT.zzqg(), zzgVar, zzgVar);
        }
        this.zzafJ = this.zzafD.zzagp.size();
        this.zzafV.add(zzmj.zzpz().submit(new zze(map)));
    }

    @Override // com.google.android.gms.internal.zzmh
    public void connect() {
        this.zzafI = false;
    }

    @Override // com.google.android.gms.internal.zzmh
    public void disconnect() {
        Iterator<zzlx.zza<?, ?>> it = this.zzafD.zzafp.zzagj.iterator();
        while (it.hasNext()) {
            zzlx.zza<?, ?> next = it.next();
            if (next.zzoQ() != 1) {
                next.cancel();
                it.remove();
            }
        }
        if (this.zzafF == null && !this.zzafD.zzafp.zzagj.isEmpty()) {
            this.zzafI = true;
            return;
        }
        zzpk();
        zzZ(true);
        this.zzafD.zzj(null);
    }

    @Override // com.google.android.gms.internal.zzmh
    public void onConnected(Bundle connectionHint) {
        if (zzbA(3)) {
            if (connectionHint != null) {
                this.zzafK.putAll(connectionHint);
            }
            if (zzpd()) {
                zzpi();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzmh
    public void onConnectionSuspended(int cause) {
        zzi(new ConnectionResult(8, null));
    }

    @Override // com.google.android.gms.internal.zzmh
    public <A extends Api.zzb, R extends Result, T extends zzlx.zza<R, A>> T zza(T t) {
        this.zzafD.zzafp.zzagj.add(t);
        return t;
    }

    @Override // com.google.android.gms.internal.zzmh
    public void zza(ConnectionResult connectionResult, Api<?> api, int i) {
        if (zzbA(3)) {
            zzb(connectionResult, api, i);
            if (zzpd()) {
                zzpi();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzmh
    public <A extends Api.zzb, T extends zzlx.zza<? extends Result, A>> T zzb(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
