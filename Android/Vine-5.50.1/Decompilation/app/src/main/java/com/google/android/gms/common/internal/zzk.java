package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public final class zzk implements Handler.Callback {
    private final Handler mHandler;
    private final zza zzajZ;
    private final ArrayList<GoogleApiClient.ConnectionCallbacks> zzaka = new ArrayList<>();
    final ArrayList<GoogleApiClient.ConnectionCallbacks> zzakb = new ArrayList<>();
    private final ArrayList<GoogleApiClient.OnConnectionFailedListener> zzakc = new ArrayList<>();
    private volatile boolean zzakd = false;
    private final AtomicInteger zzake = new AtomicInteger(0);
    private boolean zzakf = false;
    private final Object zzpK = new Object();

    public interface zza {
        boolean isConnected();

        Bundle zznQ();
    }

    public zzk(Looper looper, zza zzaVar) {
        this.zzajZ = zzaVar;
        this.mHandler = new Handler(looper, this);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        if (msg.what != 1) {
            Log.wtf("GmsClientEvents", "Don't know how to handle message: " + msg.what, new Exception());
            return false;
        }
        GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) msg.obj;
        synchronized (this.zzpK) {
            if (this.zzakd && this.zzajZ.isConnected() && this.zzaka.contains(connectionCallbacks)) {
                connectionCallbacks.onConnected(this.zzajZ.zznQ());
            }
        }
        return true;
    }

    public void registerConnectionCallbacks(GoogleApiClient.ConnectionCallbacks listener) {
        zzx.zzy(listener);
        synchronized (this.zzpK) {
            if (this.zzaka.contains(listener)) {
                Log.w("GmsClientEvents", "registerConnectionCallbacks(): listener " + listener + " is already registered");
            } else {
                this.zzaka.add(listener);
            }
        }
        if (this.zzajZ.isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, listener));
        }
    }

    public void registerConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener listener) {
        zzx.zzy(listener);
        synchronized (this.zzpK) {
            if (this.zzakc.contains(listener)) {
                Log.w("GmsClientEvents", "registerConnectionFailedListener(): listener " + listener + " is already registered");
            } else {
                this.zzakc.add(listener);
            }
        }
    }

    public void unregisterConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener listener) {
        zzx.zzy(listener);
        synchronized (this.zzpK) {
            if (!this.zzakc.remove(listener)) {
                Log.w("GmsClientEvents", "unregisterConnectionFailedListener(): listener " + listener + " not found");
            }
        }
    }

    public void zzbV(int i) {
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), "onUnintentionalDisconnection must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.zzpK) {
            this.zzakf = true;
            ArrayList arrayList = new ArrayList(this.zzaka);
            int i2 = this.zzake.get();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) it.next();
                if (!this.zzakd || this.zzake.get() != i2) {
                    break;
                } else if (this.zzaka.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnectionSuspended(i);
                }
            }
            this.zzakb.clear();
            this.zzakf = false;
        }
    }

    public void zzk(Bundle bundle) {
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), "onConnectionSuccess must only be called on the Handler thread");
        synchronized (this.zzpK) {
            zzx.zzaa(!this.zzakf);
            this.mHandler.removeMessages(1);
            this.zzakf = true;
            zzx.zzaa(this.zzakb.size() == 0);
            ArrayList arrayList = new ArrayList(this.zzaka);
            int i = this.zzake.get();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) it.next();
                if (!this.zzakd || !this.zzajZ.isConnected() || this.zzake.get() != i) {
                    break;
                } else if (!this.zzakb.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(bundle);
                }
            }
            this.zzakb.clear();
            this.zzakf = false;
        }
    }

    public void zzl(ConnectionResult connectionResult) {
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), "onConnectionFailure must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.zzpK) {
            ArrayList arrayList = new ArrayList(this.zzakc);
            int i = this.zzake.get();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = (GoogleApiClient.OnConnectionFailedListener) it.next();
                if (!this.zzakd || this.zzake.get() != i) {
                    return;
                }
                if (this.zzakc.contains(onConnectionFailedListener)) {
                    onConnectionFailedListener.onConnectionFailed(connectionResult);
                }
            }
        }
    }

    public void zzqA() {
        this.zzakd = false;
        this.zzake.incrementAndGet();
    }

    public void zzqB() {
        this.zzakd = true;
    }
}
