package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzk;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.common.internal.zzs;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public abstract class zzj<T extends IInterface> implements Api.zzb, zzk.zza {
    public static final String[] zzajS = {"service_esmobile", "service_googleme"};
    private final Context mContext;
    final Handler mHandler;
    private final Account zzSo;
    private final Set<Scope> zzVH;
    private final Looper zzaeK;
    private final GoogleApiAvailability zzaeL;
    private final com.google.android.gms.common.internal.zzf zzafT;
    private final zzl zzajH;
    private zzs zzajI;
    private GoogleApiClient.zza zzajJ;
    private T zzajK;
    private final ArrayList<zzj<T>.zzc<?>> zzajL;
    private zzj<T>.zze zzajM;
    private int zzajN;
    private final GoogleApiClient.ConnectionCallbacks zzajO;
    private final GoogleApiClient.OnConnectionFailedListener zzajP;
    private final int zzajQ;
    protected AtomicInteger zzajR;
    private final Object zzpK;

    private abstract class zza extends zzj<T>.zzc<Boolean> {
        public final int statusCode;
        public final Bundle zzajT;

        protected zza(int i, Bundle bundle) {
            super(true);
            this.statusCode = i;
            this.zzajT = bundle;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: zzc, reason: merged with bridge method [inline-methods] */
        public void zzv(Boolean bool) {
            if (bool == null) {
                zzj.this.zzb(1, null);
                return;
            }
            switch (this.statusCode) {
                case 0:
                    if (zzqv()) {
                        return;
                    }
                    zzj.this.zzb(1, null);
                    zzk(new ConnectionResult(8, null));
                    return;
                case 10:
                    zzj.this.zzb(1, null);
                    throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                default:
                    zzj.this.zzb(1, null);
                    zzk(new ConnectionResult(this.statusCode, this.zzajT != null ? (PendingIntent) this.zzajT.getParcelable("pendingIntent") : null));
                    return;
            }
        }

        protected abstract void zzk(ConnectionResult connectionResult);

        protected abstract boolean zzqv();

        protected void zzqw() {
        }
    }

    final class zzb extends Handler {
        public zzb(Looper looper) {
            super(looper);
        }

        private void zza(Message message) {
            zzc zzcVar = (zzc) message.obj;
            zzcVar.zzqw();
            zzcVar.unregister();
        }

        private boolean zzb(Message message) {
            return message.what == 2 || message.what == 1 || message.what == 5 || message.what == 6;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (zzj.this.zzajR.get() != msg.arg1) {
                if (zzb(msg)) {
                    zza(msg);
                    return;
                }
                return;
            }
            if ((msg.what == 1 || msg.what == 5 || msg.what == 6) && !zzj.this.isConnecting()) {
                zza(msg);
                return;
            }
            if (msg.what == 3) {
                ConnectionResult connectionResult = new ConnectionResult(msg.arg2, null);
                zzj.this.zzajJ.zza(connectionResult);
                zzj.this.onConnectionFailed(connectionResult);
                return;
            }
            if (msg.what == 4) {
                zzj.this.zzb(4, null);
                if (zzj.this.zzajO != null) {
                    zzj.this.zzajO.onConnectionSuspended(msg.arg2);
                }
                zzj.this.onConnectionSuspended(msg.arg2);
                zzj.this.zza(4, 1, (int) null);
                return;
            }
            if (msg.what == 2 && !zzj.this.isConnected()) {
                zza(msg);
            } else if (zzb(msg)) {
                ((zzc) msg.obj).zzqx();
            } else {
                Log.wtf("GmsClient", "Don't know how to handle message: " + msg.what, new Exception());
            }
        }
    }

    protected abstract class zzc<TListener> {
        private TListener mListener;
        private boolean zzajV = false;

        public zzc(TListener tlistener) {
            this.mListener = tlistener;
        }

        public void unregister() {
            zzqy();
            synchronized (zzj.this.zzajL) {
                zzj.this.zzajL.remove(this);
            }
        }

        protected abstract void zzqw();

        public void zzqx() {
            TListener tlistener;
            synchronized (this) {
                tlistener = this.mListener;
                if (this.zzajV) {
                    Log.w("GmsClient", "Callback proxy " + this + " being reused. This is not safe.");
                }
            }
            if (tlistener != null) {
                try {
                    zzv(tlistener);
                } catch (RuntimeException e) {
                    zzqw();
                    throw e;
                }
            } else {
                zzqw();
            }
            synchronized (this) {
                this.zzajV = true;
            }
            unregister();
        }

        public void zzqy() {
            synchronized (this) {
                this.mListener = null;
            }
        }

        protected abstract void zzv(TListener tlistener);
    }

    public static final class zzd extends zzr.zza {
        private zzj zzajW;
        private final int zzajX;

        public zzd(zzj zzjVar, int i) {
            this.zzajW = zzjVar;
            this.zzajX = i;
        }

        private void zzqz() {
            this.zzajW = null;
        }

        @Override // com.google.android.gms.common.internal.zzr
        public void zza(int i, IBinder iBinder, Bundle bundle) {
            zzx.zzb(this.zzajW, "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzajW.zza(i, iBinder, bundle, this.zzajX);
            zzqz();
        }

        @Override // com.google.android.gms.common.internal.zzr
        public void zzb(int i, Bundle bundle) {
            zzx.zzb(this.zzajW, "onAccountValidationComplete can be called only once per call to validateAccount");
            this.zzajW.zza(i, bundle, this.zzajX);
            zzqz();
        }
    }

    public final class zze implements ServiceConnection {
        private final int zzajX;

        public zze(int i) {
            this.zzajX = i;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName component, IBinder binder) {
            zzx.zzb(binder, "Expecting a valid IBinder");
            zzj.this.zzajI = zzs.zza.zzaS(binder);
            zzj.this.zzbU(this.zzajX);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName component) {
            zzj.this.mHandler.sendMessage(zzj.this.mHandler.obtainMessage(4, this.zzajX, 1));
        }
    }

    protected class zzf implements GoogleApiClient.zza {
        public zzf() {
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.zza
        public void zza(ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                zzj.this.zza((zzp) null, zzj.this.zzVH);
            } else if (zzj.this.zzajP != null) {
                zzj.this.zzajP.onConnectionFailed(connectionResult);
            }
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.zza
        public void zzb(ConnectionResult connectionResult) {
            throw new IllegalStateException("Legacy GmsClient received onReportAccountValidation callback.");
        }
    }

    protected final class zzg extends zzj<T>.zza {
        public final IBinder zzajY;

        public zzg(int i, IBinder iBinder, Bundle bundle) {
            super(i, bundle);
            this.zzajY = iBinder;
        }

        protected void zzk(ConnectionResult connectionResult) {
            if (zzj.this.zzajP != null) {
                zzj.this.zzajP.onConnectionFailed(connectionResult);
            }
            zzj.this.onConnectionFailed(connectionResult);
        }

        protected boolean zzqv() throws RemoteException {
            try {
                String interfaceDescriptor = this.zzajY.getInterfaceDescriptor();
                if (!zzj.this.zzgi().equals(interfaceDescriptor)) {
                    Log.e("GmsClient", "service descriptor mismatch: " + zzj.this.zzgi() + " vs. " + interfaceDescriptor);
                    return false;
                }
                IInterface iInterfaceZzW = zzj.this.zzW(this.zzajY);
                if (iInterfaceZzW == null || !zzj.this.zza(2, 3, (int) iInterfaceZzW)) {
                    return false;
                }
                Bundle bundleZznQ = zzj.this.zznQ();
                if (zzj.this.zzajO != null) {
                    zzj.this.zzajO.onConnected(bundleZznQ);
                }
                return true;
            } catch (RemoteException e) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }
    }

    protected final class zzh extends zzj<T>.zza {
        public zzh() {
            super(0, null);
        }

        protected void zzk(ConnectionResult connectionResult) {
            zzj.this.zzajJ.zza(connectionResult);
            zzj.this.onConnectionFailed(connectionResult);
        }

        protected boolean zzqv() {
            zzj.this.zzajJ.zza(ConnectionResult.zzadR);
            return true;
        }
    }

    protected final class zzi extends zzj<T>.zza {
        public zzi(int i, Bundle bundle) {
            super(i, bundle);
        }

        protected void zzk(ConnectionResult connectionResult) {
            zzj.this.zzajJ.zzb(connectionResult);
            zzj.this.onConnectionFailed(connectionResult);
        }

        protected boolean zzqv() {
            zzj.this.zzajJ.zzb(ConnectionResult.zzadR);
            return true;
        }
    }

    protected zzj(Context context, Looper looper, int i, com.google.android.gms.common.internal.zzf zzfVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, zzl.zzat(context), GoogleApiAvailability.getInstance(), i, zzfVar, (GoogleApiClient.ConnectionCallbacks) zzx.zzy(connectionCallbacks), (GoogleApiClient.OnConnectionFailedListener) zzx.zzy(onConnectionFailedListener));
    }

    protected zzj(Context context, Looper looper, zzl zzlVar, GoogleApiAvailability googleApiAvailability, int i, com.google.android.gms.common.internal.zzf zzfVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zzpK = new Object();
        this.zzajL = new ArrayList<>();
        this.zzajN = 1;
        this.zzajR = new AtomicInteger(0);
        this.mContext = (Context) zzx.zzb(context, "Context must not be null");
        this.zzaeK = (Looper) zzx.zzb(looper, "Looper must not be null");
        this.zzajH = (zzl) zzx.zzb(zzlVar, "Supervisor must not be null");
        this.zzaeL = (GoogleApiAvailability) zzx.zzb(googleApiAvailability, "API availability must not be null");
        this.mHandler = new zzb(looper);
        this.zzajQ = i;
        this.zzafT = (com.google.android.gms.common.internal.zzf) zzx.zzy(zzfVar);
        this.zzSo = zzfVar.getAccount();
        this.zzVH = zza(zzfVar.zzqb());
        this.zzajO = connectionCallbacks;
        this.zzajP = onConnectionFailedListener;
    }

    private Set<Scope> zza(Set<Scope> set) {
        Set<Scope> setZzb = zzb(set);
        if (setZzb == null) {
            return setZzb;
        }
        Iterator<Scope> it = setZzb.iterator();
        while (it.hasNext()) {
            if (!set.contains(it.next())) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        return setZzb;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zza(int i, int i2, T t) {
        boolean z;
        synchronized (this.zzpK) {
            if (this.zzajN != i) {
                z = false;
            } else {
                zzb(i2, t);
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzb(int i, T t) {
        zzx.zzab((i == 3) == (t != null));
        synchronized (this.zzpK) {
            this.zzajN = i;
            this.zzajK = t;
            zzc(i, t);
            switch (i) {
                case 1:
                    zzqo();
                    break;
                case 2:
                    zzqn();
                    break;
                case 3:
                    zzqm();
                    break;
            }
        }
    }

    private void zzqn() {
        if (this.zzajM != null) {
            Log.e("GmsClient", "Calling connect() while still connected, missing disconnect() for " + zzgh());
            this.zzajH.zzb(zzgh(), this.zzajM, zzql());
            this.zzajR.incrementAndGet();
        }
        this.zzajM = new zze(this.zzajR.get());
        if (this.zzajH.zza(zzgh(), this.zzajM, zzql())) {
            return;
        }
        Log.e("GmsClient", "unable to connect to service: " + zzgh());
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzajR.get(), 9));
    }

    private void zzqo() {
        if (this.zzajM != null) {
            this.zzajH.zzb(zzgh(), this.zzajM, zzql());
            this.zzajM = null;
        }
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void disconnect() {
        this.zzajR.incrementAndGet();
        synchronized (this.zzajL) {
            int size = this.zzajL.size();
            for (int i = 0; i < size; i++) {
                this.zzajL.get(i).zzqy();
            }
            this.zzajL.clear();
        }
        zzb(1, null);
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        int i;
        T t;
        synchronized (this.zzpK) {
            i = this.zzajN;
            t = this.zzajK;
        }
        writer.append((CharSequence) prefix).append("mConnectState=");
        switch (i) {
            case 1:
                writer.print("DISCONNECTED");
                break;
            case 2:
                writer.print("CONNECTING");
                break;
            case 3:
                writer.print("CONNECTED");
                break;
            case 4:
                writer.print("DISCONNECTING");
                break;
            default:
                writer.print("UNKNOWN");
                break;
        }
        writer.append(" mService=");
        if (t == null) {
            writer.println("null");
        } else {
            writer.append((CharSequence) zzgi()).append("@").println(Integer.toHexString(System.identityHashCode(t.asBinder())));
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    @Override // com.google.android.gms.common.api.Api.zzb, com.google.android.gms.common.internal.zzk.zza
    public boolean isConnected() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzajN == 3;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzajN == 2;
        }
        return z;
    }

    protected void onConnectionFailed(ConnectionResult result) {
    }

    protected void onConnectionSuspended(int cause) {
    }

    protected abstract T zzW(IBinder iBinder);

    protected void zza(int i, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5, i2, -1, new zzi(i, bundle)));
    }

    protected void zza(int i, IBinder iBinder, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, i2, -1, new zzg(i, iBinder, bundle)));
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void zza(GoogleApiClient.zza zzaVar) {
        this.zzajJ = (GoogleApiClient.zza) zzx.zzb(zzaVar, "Connection progress callbacks cannot be null.");
        zzb(2, null);
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void zza(zzp zzpVar) {
        try {
            this.zzajI.zza(new zzd(this, this.zzajR.get()), new ValidateAccountRequest(zzpVar, (Scope[]) this.zzVH.toArray(new Scope[this.zzVH.size()]), this.mContext.getPackageName(), zzqt()));
        } catch (DeadObjectException e) {
            Log.w("GmsClient", "service died");
            zzbT(1);
        } catch (RemoteException e2) {
            Log.w("GmsClient", "Remote exception occurred", e2);
        }
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void zza(zzp zzpVar, Set<Scope> set) {
        try {
            GetServiceRequest getServiceRequestZzj = new GetServiceRequest(this.zzajQ).zzcA(this.mContext.getPackageName()).zzj(zzlU());
            if (set != null) {
                getServiceRequestZzj.zzd(set);
            }
            if (zzmn()) {
                getServiceRequestZzj.zzc(zzpY()).zzc(zzpVar);
            } else if (zzqu()) {
                getServiceRequestZzj.zzc(this.zzSo);
            }
            this.zzajI.zza(new zzd(this, this.zzajR.get()), getServiceRequestZzj);
        } catch (DeadObjectException e) {
            Log.w("GmsClient", "service died");
            zzbT(1);
        } catch (RemoteException e2) {
            Log.w("GmsClient", "Remote exception occurred", e2);
        }
    }

    protected Set<Scope> zzb(Set<Scope> set) {
        return set;
    }

    public void zzbT(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, this.zzajR.get(), i));
    }

    protected void zzbU(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6, i, -1, new zzh()));
    }

    protected void zzc(int i, T t) {
    }

    protected abstract String zzgh();

    protected abstract String zzgi();

    protected Bundle zzlU() {
        return new Bundle();
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public boolean zzmJ() {
        return false;
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public Intent zzmK() {
        throw new UnsupportedOperationException("Not a sign in API");
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public boolean zzmn() {
        return false;
    }

    @Override // com.google.android.gms.common.internal.zzk.zza
    public Bundle zznQ() {
        return null;
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public IBinder zzoC() {
        if (this.zzajI == null) {
            return null;
        }
        return this.zzajI.asBinder();
    }

    public final Account zzpY() {
        return this.zzSo != null ? this.zzSo : new Account("<<default account>>", "com.google");
    }

    protected final String zzql() {
        return this.zzafT.zzqe();
    }

    protected void zzqm() {
    }

    public void zzqp() {
        int iIsGooglePlayServicesAvailable = this.zzaeL.isGooglePlayServicesAvailable(this.mContext);
        if (iIsGooglePlayServicesAvailable == 0) {
            zza(new zzf());
            return;
        }
        zzb(1, null);
        this.zzajJ = new zzf();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzajR.get(), iIsGooglePlayServicesAvailable));
    }

    protected final void zzqr() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    public final T zzqs() throws DeadObjectException {
        T t;
        synchronized (this.zzpK) {
            if (this.zzajN == 4) {
                throw new DeadObjectException();
            }
            zzqr();
            zzx.zza(this.zzajK != null, "Client is connected but service is null");
            t = this.zzajK;
        }
        return t;
    }

    protected Bundle zzqt() {
        return null;
    }

    public boolean zzqu() {
        return false;
    }
}
