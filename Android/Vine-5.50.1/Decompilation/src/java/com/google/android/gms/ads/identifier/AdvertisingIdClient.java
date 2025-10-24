package com.google.android.gms.ads.identifier;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.util.Log;
import com.google.ads.AdRequest;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.stats.zzb;
import com.google.android.gms.internal.zzav;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class AdvertisingIdClient {
    private static boolean zzoN = false;
    private final Context mContext;
    com.google.android.gms.common.zza zzoH;
    zzav zzoI;
    boolean zzoJ;
    Object zzoK;
    zza zzoL;
    final long zzoM;

    public static final class Info {
        private final String zzoS;
        private final boolean zzoT;

        public Info(String advertisingId, boolean limitAdTrackingEnabled) {
            this.zzoS = advertisingId;
            this.zzoT = limitAdTrackingEnabled;
        }

        public String getId() {
            return this.zzoS;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.zzoT;
        }

        public String toString() {
            return "{" + this.zzoS + "}" + this.zzoT;
        }
    }

    static class zza extends Thread {
        private WeakReference<AdvertisingIdClient> zzoO;
        private long zzoP;
        CountDownLatch zzoQ = new CountDownLatch(1);
        boolean zzoR = false;

        public zza(AdvertisingIdClient advertisingIdClient, long j) {
            this.zzoO = new WeakReference<>(advertisingIdClient);
            this.zzoP = j;
            start();
        }

        private void disconnect() {
            AdvertisingIdClient advertisingIdClient = this.zzoO.get();
            if (advertisingIdClient != null) {
                advertisingIdClient.finish();
                this.zzoR = true;
            }
        }

        public void cancel() {
            this.zzoQ.countDown();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                if (this.zzoQ.await(this.zzoP, TimeUnit.MILLISECONDS)) {
                    return;
                }
                disconnect();
            } catch (InterruptedException e) {
                disconnect();
            }
        }

        public boolean zzaM() {
            return this.zzoR;
        }
    }

    public AdvertisingIdClient(Context context) {
        this(context, 30000L);
    }

    public AdvertisingIdClient(Context context, long timeoutInMillis) {
        this.zzoK = new Object();
        zzx.zzy(context);
        this.mContext = context;
        this.zzoJ = false;
        this.zzoM = timeoutInMillis;
    }

    public static Info getAdvertisingIdInfo(Context context) throws GooglePlayServicesRepairableException, IllegalStateException, GooglePlayServicesNotAvailableException, IOException {
        AdvertisingIdClient advertisingIdClient = new AdvertisingIdClient(context, -1L);
        try {
            advertisingIdClient.zzb(false);
            return advertisingIdClient.getInfo();
        } finally {
            advertisingIdClient.finish();
        }
    }

    public static void setShouldSkipGmsCoreVersionCheck(boolean shouldSkipGmsCoreVersionCheck) {
        zzoN = shouldSkipGmsCoreVersionCheck;
    }

    static zzav zza(Context context, com.google.android.gms.common.zza zzaVar) throws IOException {
        try {
            return zzav.zza.zzb(zzaVar.zzor());
        } catch (InterruptedException e) {
            throw new IOException("Interrupted exception");
        } catch (Throwable th) {
            throw new IOException(th);
        }
    }

    private void zzaL() {
        synchronized (this.zzoK) {
            if (this.zzoL != null) {
                this.zzoL.cancel();
                try {
                    this.zzoL.join();
                } catch (InterruptedException e) {
                }
            }
            if (this.zzoM > 0) {
                this.zzoL = new zza(this, this.zzoM);
            }
        }
    }

    static com.google.android.gms.common.zza zzp(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException, PackageManager.NameNotFoundException, IOException {
        try {
            context.getPackageManager().getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, 0);
            if (zzoN) {
                Log.d(AdRequest.LOGTAG, "Skipping gmscore version check");
                switch (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)) {
                    case 0:
                    case 2:
                        break;
                    case 1:
                    default:
                        throw new IOException("Google Play services not available");
                }
            } else {
                try {
                    GooglePlayServicesUtil.zzac(context);
                } catch (GooglePlayServicesNotAvailableException th) {
                    throw new IOException(th);
                }
            }
            com.google.android.gms.common.zza zzaVar = new com.google.android.gms.common.zza();
            Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
            intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
            try {
                if (zzb.zzrz().zza(context, intent, zzaVar, 1)) {
                    return zzaVar;
                }
                throw new IOException("Connection failure");
            } finally {
                IOException iOException = new IOException(th);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new GooglePlayServicesNotAvailableException(9);
        }
    }

    protected void finalize() throws Throwable {
        finish();
        super.finalize();
    }

    public void finish() {
        zzx.zzcy("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.mContext == null || this.zzoH == null) {
                return;
            }
            try {
                if (this.zzoJ) {
                    zzb.zzrz().zza(this.mContext, this.zzoH);
                }
            } catch (IllegalArgumentException e) {
                Log.i("AdvertisingIdClient", "AdvertisingIdClient unbindService failed.", e);
            }
            this.zzoJ = false;
            this.zzoI = null;
            this.zzoH = null;
        }
    }

    public Info getInfo() throws IOException {
        Info info;
        zzx.zzcy("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.zzoJ) {
                zzx.zzy(this.zzoH);
                zzx.zzy(this.zzoI);
                info = new Info(this.zzoI.getId(), this.zzoI.zzc(true));
            } else {
                synchronized (this.zzoK) {
                    if (this.zzoL == null || !this.zzoL.zzaM()) {
                        throw new IOException("AdvertisingIdClient is not connected.");
                    }
                }
                try {
                    zzb(false);
                    if (!this.zzoJ) {
                        throw new IOException("AdvertisingIdClient cannot reconnect.");
                    }
                    zzx.zzy(this.zzoH);
                    zzx.zzy(this.zzoI);
                    try {
                        info = new Info(this.zzoI.getId(), this.zzoI.zzc(true));
                    } catch (RemoteException e) {
                        Log.i("AdvertisingIdClient", "GMS remote exception ", e);
                        throw new IOException("Remote exception");
                    }
                } catch (Exception e2) {
                    throw new IOException("AdvertisingIdClient cannot reconnect.", e2);
                }
            }
        }
        zzaL();
        return info;
    }

    public void start() throws GooglePlayServicesRepairableException, IllegalStateException, GooglePlayServicesNotAvailableException, IOException {
        zzb(true);
    }

    protected void zzb(boolean z) throws GooglePlayServicesRepairableException, IllegalStateException, GooglePlayServicesNotAvailableException, IOException {
        zzx.zzcy("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.zzoJ) {
                finish();
            }
            this.zzoH = zzp(this.mContext);
            this.zzoI = zza(this.mContext, this.zzoH);
            this.zzoJ = true;
            if (z) {
                zzaL();
            }
        }
    }
}
