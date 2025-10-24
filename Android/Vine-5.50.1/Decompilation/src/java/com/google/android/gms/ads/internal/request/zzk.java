package com.google.android.gms.ads.internal.request;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface zzk extends IInterface {

    public static abstract class zza extends Binder implements zzk {

        /* renamed from: com.google.android.gms.ads.internal.request.zzk$zza$zza, reason: collision with other inner class name */
        private static class C0026zza implements zzk {
            private IBinder zzoo;

            C0026zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zzoo;
            }

            @Override // com.google.android.gms.ads.internal.request.zzk
            public void zzb(AdResponseParcel adResponseParcel) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.ads.internal.request.IAdResponseListener");
                    if (adResponseParcel != null) {
                        parcelObtain.writeInt(1);
                        adResponseParcel.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zzoo.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.ads.internal.request.IAdResponseListener");
        }

        public static zzk zzY(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.internal.request.IAdResponseListener");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzk)) ? new C0026zza(iBinder) : (zzk) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.ads.internal.request.IAdResponseListener");
                    zzb(data.readInt() != 0 ? AdResponseParcel.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.ads.internal.request.IAdResponseListener");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void zzb(AdResponseParcel adResponseParcel) throws RemoteException;
}
