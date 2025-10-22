package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.measurement.AppMeasurementService;
import com.google.android.gms.measurement.internal.zzl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class zzz extends zzw {
    private final zza zzaUS;
    private zzl zzaUT;
    private Boolean zzaUU;
    private final zze zzaUV;
    private final zzaa zzaUW;
    private final List<Runnable> zzaUX;
    private final zze zzaUY;

    protected class zza implements ServiceConnection, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        private volatile boolean zzaVa;
        private volatile zzn zzaVb;

        protected zza() {
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
        public void onConnected(Bundle connectionHint) {
            com.google.android.gms.common.internal.zzx.zzcx("MeasurementServiceConnection.onConnected");
            synchronized (this) {
                this.zzaVa = false;
                try {
                    final zzl zzlVarZzqs = this.zzaVb.zzqs();
                    this.zzaVb = null;
                    zzz.this.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzz.zza.3
                        @Override // java.lang.Runnable
                        public void run() throws IllegalStateException {
                            if (zzz.this.isConnected()) {
                                return;
                            }
                            zzz.this.zzzz().zzBq().zzez("Connected to remote service");
                            zzz.this.zza(zzlVarZzqs);
                        }
                    });
                } catch (DeadObjectException | IllegalStateException e) {
                    this.zzaVb = null;
                }
            }
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
        public void onConnectionFailed(ConnectionResult result) {
            com.google.android.gms.common.internal.zzx.zzcx("MeasurementServiceConnection.onConnectionFailed");
            zzz.this.zzzz().zzBm().zzj("Service connection failed", result);
            synchronized (this) {
                this.zzaVa = false;
                this.zzaVb = null;
            }
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
        public void onConnectionSuspended(int cause) throws IllegalStateException {
            com.google.android.gms.common.internal.zzx.zzcx("MeasurementServiceConnection.onConnectionSuspended");
            zzz.this.zzzz().zzBq().zzez("Service connection suspended");
            zzz.this.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzz.zza.4
                @Override // java.lang.Runnable
                public void run() throws IllegalStateException {
                    zzz.this.onServiceDisconnected(new ComponentName(zzz.this.getContext(), (Class<?>) AppMeasurementService.class));
                }
            });
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder binder) {
            com.google.android.gms.common.internal.zzx.zzcx("MeasurementServiceConnection.onServiceConnected");
            synchronized (this) {
                this.zzaVa = false;
                if (binder == null) {
                    zzz.this.zzzz().zzBl().zzez("Service connected with null binder");
                    return;
                }
                final zzl zzlVarZzdi = null;
                try {
                    String interfaceDescriptor = binder.getInterfaceDescriptor();
                    if ("com.google.android.gms.measurement.internal.IMeasurementService".equals(interfaceDescriptor)) {
                        zzlVarZzdi = zzl.zza.zzdi(binder);
                        zzz.this.zzzz().zzBr().zzez("Bound to IMeasurementService interface");
                    } else {
                        zzz.this.zzzz().zzBl().zzj("Got binder with a wrong descriptor", interfaceDescriptor);
                    }
                } catch (RemoteException e) {
                    zzz.this.zzzz().zzBl().zzez("Service connect failed to get IMeasurementService");
                }
                if (zzlVarZzdi == null) {
                    try {
                        com.google.android.gms.common.stats.zzb.zzrz().zza(zzz.this.getContext(), zzz.this.zzaUS);
                    } catch (IllegalArgumentException e2) {
                    }
                } else {
                    zzz.this.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzz.zza.1
                        @Override // java.lang.Runnable
                        public void run() throws IllegalStateException {
                            if (zzz.this.isConnected()) {
                                return;
                            }
                            zzz.this.zzzz().zzBq().zzez("Connected to service");
                            zzz.this.zza(zzlVarZzdi);
                        }
                    });
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(final ComponentName name) throws IllegalStateException {
            com.google.android.gms.common.internal.zzx.zzcx("MeasurementServiceConnection.onServiceDisconnected");
            zzz.this.zzzz().zzBq().zzez("Service disconnected");
            zzz.this.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzz.zza.2
                @Override // java.lang.Runnable
                public void run() throws IllegalStateException {
                    zzz.this.onServiceDisconnected(name);
                }
            });
        }

        public void zzA(Intent intent) {
            zzz.this.zziS();
            Context context = zzz.this.getContext();
            com.google.android.gms.common.stats.zzb zzbVarZzrz = com.google.android.gms.common.stats.zzb.zzrz();
            synchronized (this) {
                if (this.zzaVa) {
                    zzz.this.zzzz().zzBr().zzez("Connection attempt already in progress");
                } else {
                    this.zzaVa = true;
                    zzbVarZzrz.zza(context, intent, zzz.this.zzaUS, 129);
                }
            }
        }

        public void zzCa() {
            zzz.this.zziS();
            Context context = zzz.this.getContext();
            synchronized (this) {
                if (this.zzaVa) {
                    zzz.this.zzzz().zzBr().zzez("Connection attempt already in progress");
                    return;
                }
                if (this.zzaVb != null) {
                    zzz.this.zzzz().zzBr().zzez("Already awaiting connection attempt");
                    return;
                }
                this.zzaVb = new zzn(context, Looper.getMainLooper(), com.google.android.gms.common.internal.zzf.zzas(context), this, this);
                zzz.this.zzzz().zzBr().zzez("Connecting to remote service");
                this.zzaVa = true;
                this.zzaVb.zzqp();
            }
        }
    }

    protected zzz(zzt zztVar) {
        super(zztVar);
        this.zzaUX = new ArrayList();
        this.zzaUW = new zzaa(zztVar.zziT());
        this.zzaUS = new zza();
        this.zzaUV = new zze(zztVar) { // from class: com.google.android.gms.measurement.internal.zzz.1
            @Override // com.google.android.gms.measurement.internal.zze
            public void run() {
                zzz.this.zzjs();
            }
        };
        this.zzaUY = new zze(zztVar) { // from class: com.google.android.gms.measurement.internal.zzz.2
            @Override // com.google.android.gms.measurement.internal.zze
            public void run() {
                zzz.this.zzzz().zzBm().zzez("Tasks have been queued for a long time");
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onServiceDisconnected(ComponentName name) throws IllegalStateException {
        zziS();
        if (this.zzaUT != null) {
            this.zzaUT = null;
            zzzz().zzBr().zzj("Disconnected from device MeasurementService", name);
            zzBY();
        }
    }

    private boolean zzBW() {
        List<ResolveInfo> listQueryIntentServices = getContext().getPackageManager().queryIntentServices(new Intent(getContext(), (Class<?>) AppMeasurementService.class), 65536);
        return listQueryIntentServices != null && listQueryIntentServices.size() > 0;
    }

    private boolean zzBX() {
        zziS();
        zzje();
        if (zzAX().zzka()) {
            return true;
        }
        Intent intent = new Intent("com.google.android.gms.measurement.START");
        intent.setComponent(new ComponentName(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, "com.google.android.gms.measurement.service.MeasurementBrokerService"));
        com.google.android.gms.common.stats.zzb zzbVarZzrz = com.google.android.gms.common.stats.zzb.zzrz();
        zzzz().zzBr().zzez("Checking service availability");
        if (!zzbVarZzrz.zza(getContext(), intent, new ServiceConnection() { // from class: com.google.android.gms.measurement.internal.zzz.7
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder binder) {
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
            }
        }, 0)) {
            return false;
        }
        zzzz().zzBr().zzez("Service available");
        return true;
    }

    private void zzBY() throws IllegalStateException {
        zziS();
        zzjG();
    }

    private void zzBZ() throws IllegalStateException {
        zziS();
        zzzz().zzBr().zzj("Processing queued up service tasks", Integer.valueOf(this.zzaUX.size()));
        Iterator<Runnable> it = this.zzaUX.iterator();
        while (it.hasNext()) {
            zzAV().zzg(it.next());
        }
        this.zzaUX.clear();
        this.zzaUY.cancel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zza(zzl zzlVar) throws IllegalStateException {
        zziS();
        com.google.android.gms.common.internal.zzx.zzy(zzlVar);
        this.zzaUT = zzlVar;
        zzjr();
        zzBZ();
    }

    private void zzi(Runnable runnable) throws IllegalStateException {
        zziS();
        if (isConnected()) {
            runnable.run();
            return;
        }
        if (this.zzaUX.size() >= zzAX().zzAH()) {
            zzzz().zzBl().zzez("Discarding data. Max runnable queue size reached");
            return;
        }
        this.zzaUX.add(runnable);
        if (!this.zzaQX.zzBI()) {
            this.zzaUY.zzt(60000L);
        }
        zzjG();
    }

    private void zzjG() throws IllegalStateException {
        zziS();
        zzje();
        if (isConnected()) {
            return;
        }
        if (this.zzaUU == null) {
            this.zzaUU = zzAW().zzBx();
            if (this.zzaUU == null) {
                zzzz().zzBr().zzez("State of service unknown");
                this.zzaUU = Boolean.valueOf(zzBX());
                zzAW().zzap(this.zzaUU.booleanValue());
            }
        }
        if (this.zzaUU.booleanValue()) {
            zzzz().zzBr().zzez("Using measurement service");
            this.zzaUS.zzCa();
            return;
        }
        if (zzBW() && !this.zzaQX.zzBI()) {
            zzzz().zzBr().zzez("Using local app measurement service");
            Intent intent = new Intent("com.google.android.gms.measurement.START");
            intent.setComponent(new ComponentName(getContext(), (Class<?>) AppMeasurementService.class));
            this.zzaUS.zzA(intent);
            return;
        }
        if (!zzAX().zzkb()) {
            zzzz().zzBl().zzez("Not in main process. Unable to use local measurement implementation. Please register the AppMeasurementService service in the app manifest");
        } else {
            zzzz().zzBr().zzez("Using direct local measurement implementation");
            zza(new zzu(this.zzaQX, true));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzjr() {
        zziS();
        this.zzaUW.start();
        if (this.zzaQX.zzBI()) {
            return;
        }
        this.zzaUV.zzt(zzAX().zzkv());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzjs() {
        zziS();
        if (isConnected()) {
            zzzz().zzBr().zzez("Inactivity, disconnecting from AppMeasurementService");
            disconnect();
        }
    }

    public void disconnect() {
        zziS();
        zzje();
        try {
            com.google.android.gms.common.stats.zzb.zzrz().zza(getContext(), this.zzaUS);
        } catch (IllegalArgumentException e) {
        } catch (IllegalStateException e2) {
        }
        this.zzaUT = null;
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public boolean isConnected() {
        zziS();
        zzje();
        return this.zzaUT != null;
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zzAR() {
        super.zzAR();
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
    public /* bridge */ /* synthetic */ zzc zzAX() {
        return super.zzAX();
    }

    protected void zzBS() throws IllegalStateException {
        zziS();
        zzje();
        zzi(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzz.6
            @Override // java.lang.Runnable
            public void run() {
                zzl zzlVar = zzz.this.zzaUT;
                if (zzlVar == null) {
                    zzz.this.zzzz().zzBl().zzez("Discarding data. Failed to send app launch");
                    return;
                }
                try {
                    zzlVar.zza(zzz.this.zzAS().zzex(zzz.this.zzzz().zzBs()));
                    zzz.this.zzjr();
                } catch (RemoteException e) {
                    zzz.this.zzzz().zzBl().zzj("Failed to send app launch to AppMeasurementService", e);
                }
            }
        });
    }

    protected void zzBV() throws IllegalStateException {
        zziS();
        zzje();
        zzi(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzz.3
            @Override // java.lang.Runnable
            public void run() {
                zzl zzlVar = zzz.this.zzaUT;
                if (zzlVar == null) {
                    zzz.this.zzzz().zzBl().zzez("Failed to send measurementEnabled to service");
                    return;
                }
                try {
                    zzlVar.zzb(zzz.this.zzAS().zzex(zzz.this.zzzz().zzBs()));
                    zzz.this.zzjr();
                } catch (RemoteException e) {
                    zzz.this.zzzz().zzBl().zzj("Failed to send measurementEnabled to AppMeasurementService", e);
                }
            }
        });
    }

    protected void zza(final UserAttributeParcel userAttributeParcel) throws IllegalStateException {
        zziS();
        zzje();
        zzi(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzz.5
            @Override // java.lang.Runnable
            public void run() {
                zzl zzlVar = zzz.this.zzaUT;
                if (zzlVar == null) {
                    zzz.this.zzzz().zzBl().zzez("Discarding data. Failed to set user attribute");
                    return;
                }
                try {
                    zzlVar.zza(userAttributeParcel, zzz.this.zzAS().zzex(zzz.this.zzzz().zzBs()));
                    zzz.this.zzjr();
                } catch (RemoteException e) {
                    zzz.this.zzzz().zzBl().zzj("Failed to send attribute to AppMeasurementService", e);
                }
            }
        });
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zziR() {
        super.zziR();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zziS() {
        super.zziS();
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
