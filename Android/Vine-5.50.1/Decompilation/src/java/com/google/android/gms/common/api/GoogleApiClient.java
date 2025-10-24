package com.google.android.gms.common.api;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.zzad;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzlx;
import com.google.android.gms.internal.zzlz;
import com.google.android.gms.internal.zzmg;
import com.google.android.gms.internal.zzmr;
import com.google.android.gms.internal.zzsa;
import com.google.android.gms.internal.zzsc;
import com.google.android.gms.internal.zzsd;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public abstract class GoogleApiClient {
    private static final Set<GoogleApiClient> zzaez = Collections.newSetFromMap(new WeakHashMap());

    public static final class Builder {
        private final Context mContext;
        private Account zzSo;
        private String zzTz;
        private int zzaeC;
        private View zzaeD;
        private String zzaeE;
        private FragmentActivity zzaeH;
        private OnConnectionFailedListener zzaeJ;
        private Looper zzaeK;
        private zzsd zzaeP;
        private final Set<Scope> zzaeA = new HashSet();
        private final Set<Scope> zzaeB = new HashSet();
        private final Map<Api<?>, zzf.zza> zzaeF = new ArrayMap();
        private final Map<Api<?>, Api.ApiOptions> zzaeG = new ArrayMap();
        private int zzaeI = -1;
        private GoogleApiAvailability zzaeL = GoogleApiAvailability.getInstance();
        private Api.zza<? extends zzsc, zzsd> zzaeM = zzsa.zzTp;
        private final ArrayList<ConnectionCallbacks> zzaeN = new ArrayList<>();
        private final ArrayList<OnConnectionFailedListener> zzaeO = new ArrayList<>();

        public Builder(Context context) {
            this.mContext = context;
            this.zzaeK = context.getMainLooper();
            this.zzTz = context.getPackageName();
            this.zzaeE = context.getClass().getName();
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static <C extends Api.zzb, O> C zza(Api.zza<C, O> zzaVar, Object obj, Context context, Looper looper, zzf zzfVar, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return (C) zzaVar.zza(context, looper, zzfVar, obj, connectionCallbacks, onConnectionFailedListener);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static <C extends Api.zzd, O> zzad zza(Api.zze<C, O> zzeVar, Object obj, Context context, Looper looper, zzf zzfVar, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzad(context, looper, zzeVar.zzoD(), connectionCallbacks, onConnectionFailedListener, zzfVar, zzeVar.zzp(obj));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void zza(zzmr zzmrVar, GoogleApiClient googleApiClient) {
            zzmrVar.zza(this.zzaeI, googleApiClient, this.zzaeJ);
        }

        private void zzd(final GoogleApiClient googleApiClient) {
            zzmr zzmrVarZza = zzmr.zza(this.zzaeH);
            if (zzmrVarZza == null) {
                new Handler(this.mContext.getMainLooper()).post(new Runnable() { // from class: com.google.android.gms.common.api.GoogleApiClient.Builder.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (Builder.this.zzaeH.isFinishing() || Builder.this.zzaeH.getSupportFragmentManager().isDestroyed()) {
                            return;
                        }
                        Builder.this.zza(zzmr.zzb(Builder.this.zzaeH), googleApiClient);
                    }
                });
            } else {
                zza(zzmrVarZza, googleApiClient);
            }
        }

        private GoogleApiClient zzoI() {
            Api.zzb zzbVarZza;
            Api<?> api;
            zzf zzfVarZzoH = zzoH();
            Api<?> api2 = null;
            Map<Api<?>, zzf.zza> mapZzqc = zzfVarZzoH.zzqc();
            ArrayMap arrayMap = new ArrayMap();
            ArrayMap arrayMap2 = new ArrayMap();
            ArrayList arrayList = new ArrayList();
            Api<?> api3 = null;
            for (Api<?> api4 : this.zzaeG.keySet()) {
                Api.ApiOptions apiOptions = this.zzaeG.get(api4);
                int i = mapZzqc.get(api4) != null ? mapZzqc.get(api4).zzaju ? 1 : 2 : 0;
                arrayMap.put(api4, Integer.valueOf(i));
                zzlz zzlzVar = new zzlz(api4, i);
                arrayList.add(zzlzVar);
                if (api4.zzoB()) {
                    Api.zze<?, O> zzeVarZzoz = api4.zzoz();
                    Api<?> api5 = zzeVarZzoz.getPriority() == 1 ? api4 : api3;
                    zzbVarZza = zza(zzeVarZzoz, apiOptions, this.mContext, this.zzaeK, zzfVarZzoH, zzlzVar, zzlzVar);
                    api = api5;
                } else {
                    Api.zza<?, O> zzaVarZzoy = api4.zzoy();
                    Api<?> api6 = zzaVarZzoy.getPriority() == 1 ? api4 : api3;
                    zzbVarZza = zza((Api.zza<Api.zzb, O>) zzaVarZzoy, (Object) apiOptions, this.mContext, this.zzaeK, zzfVarZzoH, (ConnectionCallbacks) zzlzVar, (OnConnectionFailedListener) zzlzVar);
                    api = api6;
                }
                arrayMap2.put(api4.zzoA(), zzbVarZza);
                if (!zzbVarZza.zzmJ()) {
                    api4 = api2;
                } else if (api2 != null) {
                    throw new IllegalStateException(api4.getName() + " cannot be used with " + api2.getName());
                }
                api3 = api;
                api2 = api4;
            }
            if (api2 != null) {
                if (api3 != null) {
                    throw new IllegalStateException(api2.getName() + " cannot be used with " + api3.getName());
                }
                zzx.zza(this.zzSo == null, "Must not set an account in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead", api2.getName());
                zzx.zza(this.zzaeA.equals(this.zzaeB), "Must not set scopes in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead.", api2.getName());
                zzx.zza(this.zzaeP == null, "Must not call requestServerAuthCode in GoogleApiClient.Builder when using %s. Call requestServerAuthCode in GoogleSignInOptions.Builder instead.", api2.getName());
            }
            return new zzmg(this.mContext, new ReentrantLock(), this.zzaeK, zzfVarZzoH, this.zzaeL, this.zzaeM, arrayMap, this.zzaeN, this.zzaeO, arrayMap2, this.zzaeI, zzmg.zza(arrayMap2.values(), true), arrayList);
        }

        public Builder addApi(Api<? extends Api.ApiOptions.NotRequiredOptions> api) {
            zzx.zzb(api, "Api must not be null");
            this.zzaeG.put(api, null);
            List<Scope> listZzn = api.zzoy().zzn(null);
            this.zzaeB.addAll(listZzn);
            this.zzaeA.addAll(listZzn);
            return this;
        }

        public GoogleApiClient build() {
            zzx.zzb(!this.zzaeG.isEmpty(), "must call addApi() to add at least one API");
            GoogleApiClient googleApiClientZzoI = zzoI();
            synchronized (GoogleApiClient.zzaez) {
                GoogleApiClient.zzaez.add(googleApiClientZzoI);
            }
            if (this.zzaeI >= 0) {
                zzd(googleApiClientZzoI);
            }
            return googleApiClientZzoI;
        }

        public zzf zzoH() {
            if (this.zzaeG.containsKey(zzsa.API)) {
                zzx.zza(this.zzaeP == null, "SignIn.API can't be used in conjunction with requestServerAuthCode.");
                this.zzaeP = (zzsd) this.zzaeG.get(zzsa.API);
            }
            return new zzf(this.zzSo, this.zzaeA, this.zzaeF, this.zzaeC, this.zzaeD, this.zzTz, this.zzaeE, this.zzaeP != null ? this.zzaeP : zzsd.zzbbH);
        }
    }

    public interface ConnectionCallbacks {
        void onConnected(Bundle bundle);

        void onConnectionSuspended(int i);
    }

    public interface OnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    public interface ServerAuthCodeCallbacks {

        public static class CheckResult {
            private Set<Scope> zzVH;
            private boolean zzaeR;

            public boolean zzoJ() {
                return this.zzaeR;
            }

            public Set<Scope> zzoK() {
                return this.zzVH;
            }
        }

        CheckResult onCheckServerAuthorization(String str, Set<Scope> set);

        boolean onUploadServerAuthCode(String str, String str2);
    }

    public interface zza {
        void zza(ConnectionResult connectionResult);

        void zzb(ConnectionResult connectionResult);
    }

    public abstract void connect();

    public void connect(int signInMode) {
        throw new UnsupportedOperationException();
    }

    public abstract void disconnect();

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public Looper getLooper() {
        throw new UnsupportedOperationException();
    }

    public abstract void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);

    public abstract void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);

    public <C extends Api.zzb> C zza(Api.zzc<C> zzcVar) {
        throw new UnsupportedOperationException();
    }

    public <A extends Api.zzb, R extends Result, T extends zzlx.zza<R, A>> T zza(T t) {
        throw new UnsupportedOperationException();
    }

    public <A extends Api.zzb, T extends zzlx.zza<? extends Result, A>> T zzb(T t) {
        throw new UnsupportedOperationException();
    }
}
