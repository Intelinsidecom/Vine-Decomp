package io.fabric.sdk.android.services.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
class AdvertisingInfoServiceStrategy implements AdvertisingInfoStrategy {
    private final Context context;

    public AdvertisingInfoServiceStrategy(Context context) {
        this.context = context.getApplicationContext();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v0, types: [io.fabric.sdk.android.services.common.AdvertisingInfoServiceStrategy$1] */
    /* JADX WARN: Type inference failed for: r6v1 */
    /* JADX WARN: Type inference failed for: r6v10 */
    /* JADX WARN: Type inference failed for: r6v11 */
    /* JADX WARN: Type inference failed for: r6v12 */
    /* JADX WARN: Type inference failed for: r6v2 */
    /* JADX WARN: Type inference failed for: r6v3 */
    /* JADX WARN: Type inference failed for: r6v5 */
    /* JADX WARN: Type inference failed for: r6v6 */
    /* JADX WARN: Type inference failed for: r6v7 */
    /* JADX WARN: Type inference failed for: r6v8 */
    /* JADX WARN: Type inference failed for: r6v9 */
    @Override // io.fabric.sdk.android.services.common.AdvertisingInfoStrategy
    public AdvertisingInfo getAdvertisingInfo() throws PackageManager.NameNotFoundException {
        AdvertisingInfo advertisingInfo = 0;
        advertisingInfo = 0;
        advertisingInfo = 0;
        advertisingInfo = 0;
        advertisingInfo = 0;
        advertisingInfo = 0;
        advertisingInfo = 0;
        advertisingInfo = 0;
        advertisingInfo = 0;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Fabric.getLogger().d("Fabric", "AdvertisingInfoServiceStrategy cannot be called on the main thread");
        } else {
            try {
                this.context.getPackageManager().getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, 0);
                AdvertisingConnection advertisingConnection = new AdvertisingConnection();
                Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
                try {
                    try {
                    } catch (Exception e) {
                        Fabric.getLogger().w("Fabric", "Exception in binding to Google Play Service to capture AdvertisingId", e);
                    } finally {
                        this.context.unbindService(advertisingConnection);
                    }
                    if (this.context.bindService(intent, advertisingConnection, 1)) {
                        AdvertisingInterface advertisingInterface = new AdvertisingInterface(advertisingConnection.getBinder());
                        advertisingInfo = new AdvertisingInfo(advertisingInterface.getId(), advertisingInterface.isLimitAdTrackingEnabled());
                    } else {
                        Fabric.getLogger().d("Fabric", "Could not bind to Google Play Service to capture AdvertisingId");
                    }
                } catch (Throwable th) {
                    Fabric.getLogger().d("Fabric", "Could not bind to Google Play Service to capture AdvertisingId", th);
                }
            } catch (PackageManager.NameNotFoundException e2) {
                Fabric.getLogger().d("Fabric", "Unable to find Google Play Services package name");
            } catch (Exception e3) {
                Fabric.getLogger().d("Fabric", "Unable to determine if Google Play Services is available", e3);
            }
        }
        return advertisingInfo;
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        private final LinkedBlockingQueue<IBinder> queue;
        private boolean retrieved;

        private AdvertisingConnection() {
            this.retrieved = false;
            this.queue = new LinkedBlockingQueue<>(1);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) throws InterruptedException {
            try {
                this.queue.put(service);
            } catch (InterruptedException e) {
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            this.queue.clear();
        }

        public IBinder getBinder() {
            if (this.retrieved) {
                Fabric.getLogger().e("Fabric", "getBinder already called");
            }
            this.retrieved = true;
            try {
                return this.queue.poll(200L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                return null;
            }
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        private final IBinder binder;

        public AdvertisingInterface(IBinder binder) {
            this.binder = binder;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.binder;
        }

        public String getId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            String id = null;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.binder.transact(1, data, reply, 0);
                reply.readException();
                id = reply.readString();
            } catch (Exception e) {
                Fabric.getLogger().d("Fabric", "Could not get parcel from Google Play Service to capture AdvertisingId");
            } finally {
                reply.recycle();
                data.recycle();
            }
            return id;
        }

        public boolean isLimitAdTrackingEnabled() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            boolean limitAdTracking = false;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                data.writeInt(1);
                this.binder.transact(2, data, reply, 0);
                reply.readException();
                limitAdTracking = reply.readInt() != 0;
            } catch (Exception e) {
                Fabric.getLogger().d("Fabric", "Could not get parcel from Google Play Service to capture Advertising limitAdTracking");
            } finally {
                reply.recycle();
                data.recycle();
            }
            return limitAdTracking;
        }
    }
}
