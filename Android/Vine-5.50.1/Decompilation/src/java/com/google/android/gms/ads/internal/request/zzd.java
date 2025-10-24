package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.request.zzc;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.zzbs;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzhc;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zzjg;

@zzha
/* loaded from: classes.dex */
public abstract class zzd implements zzc.zza, zzir<Void> {
    private final zzjg<AdRequestInfoParcel> zzGi;
    private final zzc.zza zzGj;
    private final Object zzpK = new Object();

    @zzha
    public static final class zza extends zzd {
        private final Context mContext;

        public zza(Context context, zzjg<AdRequestInfoParcel> zzjgVar, zzc.zza zzaVar) {
            super(zzjgVar, zzaVar);
            this.mContext = context;
        }

        @Override // com.google.android.gms.ads.internal.request.zzd, com.google.android.gms.internal.zzir
        public /* synthetic */ Void zzfR() {
            return super.zzfR();
        }

        @Override // com.google.android.gms.ads.internal.request.zzd
        public void zzge() {
        }

        @Override // com.google.android.gms.ads.internal.request.zzd
        public zzj zzgf() {
            return zzhc.zza(this.mContext, new zzbs(zzbz.zzvg.get()), zzhb.zzgn());
        }
    }

    @zzha
    public static class zzb extends zzd implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        private Context mContext;
        private zzjg<AdRequestInfoParcel> zzGi;
        private final zzc.zza zzGj;
        protected zze zzGm;
        private boolean zzGn;
        private VersionInfoParcel zzpI;
        private final Object zzpK;

        public zzb(Context context, VersionInfoParcel versionInfoParcel, zzjg<AdRequestInfoParcel> zzjgVar, zzc.zza zzaVar) {
            Looper mainLooper;
            super(zzjgVar, zzaVar);
            this.zzpK = new Object();
            this.mContext = context;
            this.zzpI = versionInfoParcel;
            this.zzGi = zzjgVar;
            this.zzGj = zzaVar;
            if (zzbz.zzvF.get().booleanValue()) {
                this.zzGn = true;
                mainLooper = zzp.zzbJ().zzhk();
            } else {
                mainLooper = context.getMainLooper();
            }
            this.zzGm = new zze(context, mainLooper, this, this, this.zzpI.zzLG);
            connect();
        }

        protected void connect() {
            this.zzGm.zzqp();
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
        public void onConnected(Bundle connectionHint) {
            zzfR();
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
        public void onConnectionFailed(ConnectionResult result) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Cannot connect to remote service, fallback to local instance.");
            zzgg().zzfR();
            Bundle bundle = new Bundle();
            bundle.putString("action", "gms_connection_failed_fallback_to_local");
            zzp.zzbx().zzb(this.mContext, this.zzpI.afmaVersion, "gmob-apps", bundle, true);
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
        public void onConnectionSuspended(int cause) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Disconnected from remote ad request service.");
        }

        @Override // com.google.android.gms.ads.internal.request.zzd, com.google.android.gms.internal.zzir
        public /* synthetic */ Void zzfR() {
            return super.zzfR();
        }

        @Override // com.google.android.gms.ads.internal.request.zzd
        public void zzge() {
            synchronized (this.zzpK) {
                if (this.zzGm.isConnected() || this.zzGm.isConnecting()) {
                    this.zzGm.disconnect();
                }
                Binder.flushPendingCommands();
                if (this.zzGn) {
                    zzp.zzbJ().zzhl();
                    this.zzGn = false;
                }
            }
        }

        @Override // com.google.android.gms.ads.internal.request.zzd
        public zzj zzgf() {
            zzj zzjVarZzgj;
            synchronized (this.zzpK) {
                try {
                    zzjVarZzgj = this.zzGm.zzgj();
                } catch (DeadObjectException | IllegalStateException e) {
                    zzjVarZzgj = null;
                }
            }
            return zzjVarZzgj;
        }

        zzir zzgg() {
            return new zza(this.mContext, this.zzGi, this.zzGj);
        }
    }

    public zzd(zzjg<AdRequestInfoParcel> zzjgVar, zzc.zza zzaVar) {
        this.zzGi = zzjgVar;
        this.zzGj = zzaVar;
    }

    @Override // com.google.android.gms.internal.zzir
    public void cancel() {
        zzge();
    }

    boolean zza(zzj zzjVar, AdRequestInfoParcel adRequestInfoParcel) {
        try {
            zzjVar.zza(adRequestInfoParcel, new zzg(this));
            return true;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service.", e);
            zzp.zzbA().zzb((Throwable) e, true);
            this.zzGj.zzb(new AdResponseParcel(0));
            return false;
        } catch (NullPointerException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e2);
            zzp.zzbA().zzb((Throwable) e2, true);
            this.zzGj.zzb(new AdResponseParcel(0));
            return false;
        } catch (SecurityException e3) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e3);
            zzp.zzbA().zzb((Throwable) e3, true);
            this.zzGj.zzb(new AdResponseParcel(0));
            return false;
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", th);
            zzp.zzbA().zzb(th, true);
            this.zzGj.zzb(new AdResponseParcel(0));
            return false;
        }
    }

    @Override // com.google.android.gms.ads.internal.request.zzc.zza
    public void zzb(AdResponseParcel adResponseParcel) {
        synchronized (this.zzpK) {
            this.zzGj.zzb(adResponseParcel);
            zzge();
        }
    }

    @Override // com.google.android.gms.internal.zzir
    /* renamed from: zzfO, reason: merged with bridge method [inline-methods] */
    public Void zzfR() {
        final zzj zzjVarZzgf = zzgf();
        if (zzjVarZzgf == null) {
            this.zzGj.zzb(new AdResponseParcel(0));
            zzge();
        } else {
            this.zzGi.zza(new zzjg.zzc<AdRequestInfoParcel>() { // from class: com.google.android.gms.ads.internal.request.zzd.1
                @Override // com.google.android.gms.internal.zzjg.zzc
                public void zzc(AdRequestInfoParcel adRequestInfoParcel) {
                    if (zzd.this.zza(zzjVarZzgf, adRequestInfoParcel)) {
                        return;
                    }
                    zzd.this.zzge();
                }
            }, new zzjg.zza() { // from class: com.google.android.gms.ads.internal.request.zzd.2
                @Override // com.google.android.gms.internal.zzjg.zza
                public void run() {
                    zzd.this.zzge();
                }
            });
        }
        return null;
    }

    public abstract void zzge();

    public abstract zzj zzgf();
}
