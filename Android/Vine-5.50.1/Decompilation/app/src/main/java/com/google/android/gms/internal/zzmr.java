package com.google.android.gms.internal;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: classes2.dex */
public class zzmr extends Fragment implements DialogInterface.OnCancelListener {
    private static final GoogleApiAvailability zzagU = GoogleApiAvailability.getInstance();
    private boolean mStarted;
    private boolean zzagV;
    private ConnectionResult zzagX;
    private zzmk zzagZ;
    private int zzagW = -1;
    private final Handler zzagY = new Handler(Looper.getMainLooper());
    private final SparseArray<zza> zzaha = new SparseArray<>();

    private class zza implements GoogleApiClient.OnConnectionFailedListener {
        public final int zzahb;
        public final GoogleApiClient zzahc;
        public final GoogleApiClient.OnConnectionFailedListener zzahd;

        public zza(int i, GoogleApiClient googleApiClient, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            this.zzahb = i;
            this.zzahc = googleApiClient;
            this.zzahd = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener(this);
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            writer.append((CharSequence) prefix).append("GoogleApiClient #").print(this.zzahb);
            writer.println(":");
            this.zzahc.dump(prefix + "  ", fd, writer, args);
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
        public void onConnectionFailed(ConnectionResult result) {
            zzmr.this.zzagY.post(zzmr.this.new zzb(this.zzahb, result));
        }

        public void zzpC() {
            this.zzahc.unregisterConnectionFailedListener(this);
            this.zzahc.disconnect();
        }
    }

    private class zzb implements Runnable {
        private final int zzahf;
        private final ConnectionResult zzahg;

        public zzb(int i, ConnectionResult connectionResult) {
            this.zzahf = i;
            this.zzahg = connectionResult;
        }

        @Override // java.lang.Runnable
        public void run() throws PackageManager.NameNotFoundException {
            if (!zzmr.this.mStarted || zzmr.this.zzagV) {
                return;
            }
            zzmr.this.zzagV = true;
            zzmr.this.zzagW = this.zzahf;
            zzmr.this.zzagX = this.zzahg;
            if (this.zzahg.hasResolution()) {
                try {
                    this.zzahg.startResolutionForResult(zzmr.this.getActivity(), ((zzmr.this.getActivity().getSupportFragmentManager().getFragments().indexOf(zzmr.this) + 1) << 16) + 1);
                    return;
                } catch (IntentSender.SendIntentException e) {
                    zzmr.this.zzpA();
                    return;
                }
            }
            if (zzmr.zzagU.isUserResolvableError(this.zzahg.getErrorCode())) {
                GooglePlayServicesUtil.showErrorDialogFragment(this.zzahg.getErrorCode(), zzmr.this.getActivity(), zzmr.this, 2, zzmr.this);
            } else {
                if (this.zzahg.getErrorCode() != 18) {
                    zzmr.this.zza(this.zzahf, this.zzahg);
                    return;
                }
                final Dialog dialogZza = zzmr.zzagU.zza(zzmr.this.getActivity(), zzmr.this);
                zzmr.this.zzagZ = zzmk.zza(zzmr.this.getActivity().getApplicationContext(), new zzmk() { // from class: com.google.android.gms.internal.zzmr.zzb.1
                    @Override // com.google.android.gms.internal.zzmk
                    protected void zzpv() {
                        zzmr.this.zzpA();
                        dialogZza.dismiss();
                    }
                });
            }
        }
    }

    public static zzmr zza(FragmentActivity fragmentActivity) {
        com.google.android.gms.common.internal.zzx.zzcx("Must be called from main thread of process");
        try {
            zzmr zzmrVar = (zzmr) fragmentActivity.getSupportFragmentManager().findFragmentByTag("GmsSupportLifecycleFrag");
            if (zzmrVar == null || zzmrVar.isRemoving()) {
                return null;
            }
            return zzmrVar;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFrag is not a SupportLifecycleFragment", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zza(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFrag", "Unresolved error while connecting client. Stopping auto-manage.");
        zza zzaVar = this.zzaha.get(i);
        if (zzaVar != null) {
            zzbE(i);
            GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = zzaVar.zzahd;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
        zzpA();
    }

    public static zzmr zzb(FragmentActivity fragmentActivity) {
        zzmr zzmrVarZza = zza(fragmentActivity);
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (zzmrVarZza != null) {
            return zzmrVarZza;
        }
        zzmr zzmrVar = new zzmr();
        supportFragmentManager.beginTransaction().add(zzmrVar, "GmsSupportLifecycleFrag").commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
        return zzmrVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzpA() {
        int i = 0;
        this.zzagV = false;
        this.zzagW = -1;
        this.zzagX = null;
        if (this.zzagZ != null) {
            this.zzagZ.unregister();
            this.zzagZ = null;
        }
        while (true) {
            int i2 = i;
            if (i2 >= this.zzaha.size()) {
                return;
            }
            this.zzaha.valueAt(i2).zzahc.connect();
            i = i2 + 1;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.zzaha.size()) {
                return;
            }
            this.zzaha.valueAt(i2).dump(prefix, fd, writer, args);
            i = i2 + 1;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:4:0x0005  */
    @Override // android.support.v4.app.Fragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onActivityResult(int r5, int r6, android.content.Intent r7) {
        /*
            r4 = this;
            r0 = 1
            r1 = 0
            switch(r5) {
                case 1: goto L19;
                case 2: goto Lc;
                default: goto L5;
            }
        L5:
            r0 = r1
        L6:
            if (r0 == 0) goto L29
            r4.zzpA()
        Lb:
            return
        Lc:
            com.google.android.gms.common.GoogleApiAvailability r2 = com.google.android.gms.internal.zzmr.zzagU
            android.support.v4.app.FragmentActivity r3 = r4.getActivity()
            int r2 = r2.isGooglePlayServicesAvailable(r3)
            if (r2 != 0) goto L5
            goto L6
        L19:
            r2 = -1
            if (r6 == r2) goto L6
            if (r6 != 0) goto L5
            com.google.android.gms.common.ConnectionResult r0 = new com.google.android.gms.common.ConnectionResult
            r2 = 13
            r3 = 0
            r0.<init>(r2, r3)
            r4.zzagX = r0
            goto L5
        L29:
            int r0 = r4.zzagW
            com.google.android.gms.common.ConnectionResult r1 = r4.zzagX
            r4.zza(r0, r1)
            goto Lb
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzmr.onActivityResult(int, int, android.content.Intent):void");
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        zza(this.zzagW, new ConnectionResult(13, null));
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.zzagV = savedInstanceState.getBoolean("resolving_error", false);
            this.zzagW = savedInstanceState.getInt("failed_client_id", -1);
            if (this.zzagW >= 0) {
                this.zzagX = new ConnectionResult(savedInstanceState.getInt("failed_status"), (PendingIntent) savedInstanceState.getParcelable("failed_resolution"));
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("resolving_error", this.zzagV);
        if (this.zzagW >= 0) {
            outState.putInt("failed_client_id", this.zzagW);
            outState.putInt("failed_status", this.zzagX.getErrorCode());
            outState.putParcelable("failed_resolution", this.zzagX.getResolution());
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        this.mStarted = true;
        if (this.zzagV) {
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.zzaha.size()) {
                return;
            }
            this.zzaha.valueAt(i2).zzahc.connect();
            i = i2 + 1;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onStop() {
        int i = 0;
        super.onStop();
        this.mStarted = false;
        while (true) {
            int i2 = i;
            if (i2 >= this.zzaha.size()) {
                return;
            }
            this.zzaha.valueAt(i2).zzahc.disconnect();
            i = i2 + 1;
        }
    }

    public void zza(int i, GoogleApiClient googleApiClient, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        com.google.android.gms.common.internal.zzx.zzb(googleApiClient, "GoogleApiClient instance cannot be null");
        com.google.android.gms.common.internal.zzx.zza(this.zzaha.indexOfKey(i) < 0, "Already managing a GoogleApiClient with id " + i);
        this.zzaha.put(i, new zza(i, googleApiClient, onConnectionFailedListener));
        if (!this.mStarted || this.zzagV) {
            return;
        }
        googleApiClient.connect();
    }

    public void zzbE(int i) {
        zza zzaVar = this.zzaha.get(i);
        this.zzaha.remove(i);
        if (zzaVar != null) {
            zzaVar.zzpC();
        }
    }
}
