package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.auth.api.signin.internal.zzn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.AuthAccountRequest;
import com.google.android.gms.common.internal.BinderWrapper;
import com.google.android.gms.common.internal.ResolveAccountRequest;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzsc;
import com.google.android.gms.internal.zzsd;
import com.google.android.gms.signin.internal.zzd;
import com.google.android.gms.signin.internal.zzf;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/* loaded from: classes2.dex */
public class zzi extends zzj<zzf> implements zzsc {
    private final com.google.android.gms.common.internal.zzf zzafT;
    private Integer zzajt;
    private final Bundle zzbbG;
    private final boolean zzbbX;

    private static class zza extends zzd.zza {
        private final zzsd zzaeP;
        private final ExecutorService zzbbY;

        public zza(zzsd zzsdVar, ExecutorService executorService) {
            this.zzaeP = zzsdVar;
            this.zzbbY = executorService;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public GoogleApiClient.ServerAuthCodeCallbacks zzDN() throws RemoteException {
            return this.zzaeP.zzDN();
        }

        @Override // com.google.android.gms.signin.internal.zzd
        public void zza(final String str, final String str2, final zzf zzfVar) throws RemoteException {
            this.zzbbY.submit(new Runnable() { // from class: com.google.android.gms.signin.internal.zzi.zza.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfVar.zzat(zza.this.zzDN().onUploadServerAuthCode(str, str2));
                    } catch (RemoteException e) {
                        Log.e("SignInClientImpl", "RemoteException thrown when processing uploadServerAuthCode callback", e);
                    }
                }
            });
        }

        @Override // com.google.android.gms.signin.internal.zzd
        public void zza(final String str, final List<Scope> list, final zzf zzfVar) throws RemoteException {
            this.zzbbY.submit(new Runnable() { // from class: com.google.android.gms.signin.internal.zzi.zza.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        GoogleApiClient.ServerAuthCodeCallbacks.CheckResult checkResultOnCheckServerAuthorization = zza.this.zzDN().onCheckServerAuthorization(str, Collections.unmodifiableSet(new HashSet(list)));
                        zzfVar.zza(new CheckServerAuthResult(checkResultOnCheckServerAuthorization.zzoJ(), checkResultOnCheckServerAuthorization.zzoK()));
                    } catch (RemoteException e) {
                        Log.e("SignInClientImpl", "RemoteException thrown when processing checkServerAuthorization callback", e);
                    }
                }
            });
        }
    }

    public zzi(Context context, Looper looper, boolean z, com.google.android.gms.common.internal.zzf zzfVar, Bundle bundle, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 44, zzfVar, connectionCallbacks, onConnectionFailedListener);
        this.zzbbX = z;
        this.zzafT = zzfVar;
        this.zzbbG = bundle;
        this.zzajt = zzfVar.zzqh();
    }

    public zzi(Context context, Looper looper, boolean z, com.google.android.gms.common.internal.zzf zzfVar, zzsd zzsdVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, ExecutorService executorService) {
        this(context, looper, z, zzfVar, zza(zzsdVar, zzfVar.zzqh(), executorService), connectionCallbacks, onConnectionFailedListener);
    }

    public static Bundle zza(zzsd zzsdVar, Integer num, ExecutorService executorService) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", zzsdVar.zzDM());
        bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", zzsdVar.zzmy());
        bundle.putString("com.google.android.gms.signin.internal.serverClientId", zzsdVar.zzmB());
        if (zzsdVar.zzDN() != null) {
            bundle.putParcelable("com.google.android.gms.signin.internal.signInCallbacks", new BinderWrapper(new zza(zzsdVar, executorService).asBinder()));
        }
        if (num != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", num.intValue());
        }
        bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", zzsdVar.zzDO());
        bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", zzsdVar.zzmA());
        bundle.putBoolean("com.google.android.gms.signin.internal.waitForAccessTokenRefresh", zzsdVar.zzDP());
        return bundle;
    }

    @Override // com.google.android.gms.internal.zzsc
    public void connect() {
        zza(new zzj.zzf());
    }

    @Override // com.google.android.gms.internal.zzsc
    public void zzDL() {
        try {
            zzqs().zzjL(this.zzajt.intValue());
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when clearAccountFromSessionStore is called");
        }
    }

    @Override // com.google.android.gms.internal.zzsc
    public void zza(zzp zzpVar, Set<Scope> set, zze zzeVar) {
        zzx.zzb(zzeVar, "Expecting a valid ISignInCallbacks");
        try {
            zzqs().zza(new AuthAccountRequest(zzpVar, set), zzeVar);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when authAccount is called");
            try {
                zzeVar.zza(new ConnectionResult(8, null), new AuthAccountResult(8, null));
            } catch (RemoteException e2) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onAuthAccount should be executed from the same process, unexpected RemoteException.", e);
            }
        }
    }

    @Override // com.google.android.gms.internal.zzsc
    public void zza(zzp zzpVar, boolean z) {
        try {
            zzqs().zza(zzpVar, this.zzajt.intValue(), z);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when saveDefaultAccount is called");
        }
    }

    @Override // com.google.android.gms.internal.zzsc
    public void zza(zzt zztVar) {
        zzx.zzb(zztVar, "Expecting a valid IResolveAccountCallbacks");
        try {
            Account accountZzpY = this.zzafT.zzpY();
            zzqs().zza(new ResolveAccountRequest(accountZzpY, this.zzajt.intValue(), "<<default account>>".equals(accountZzpY.name) ? zzn.zzae(getContext()).zzmW() : null), zztVar);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when resolveAccount is called");
            try {
                zztVar.zzb(new ResolveAccountResponse(8));
            } catch (RemoteException e2) {
                Log.wtf("SignInClientImpl", "IResolveAccountCallbacks#onAccountResolutionComplete should be executed from the same process, unexpected RemoteException.", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.zzj
    /* renamed from: zzdX, reason: merged with bridge method [inline-methods] */
    public zzf zzW(IBinder iBinder) {
        return zzf.zza.zzdW(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgh() {
        return "com.google.android.gms.signin.service.START";
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgi() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected Bundle zzlU() {
        if (!getContext().getPackageName().equals(this.zzafT.zzqd())) {
            this.zzbbG.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzafT.zzqd());
        }
        return this.zzbbG;
    }

    @Override // com.google.android.gms.common.internal.zzj, com.google.android.gms.common.api.Api.zzb
    public boolean zzmn() {
        return this.zzbbX;
    }
}
