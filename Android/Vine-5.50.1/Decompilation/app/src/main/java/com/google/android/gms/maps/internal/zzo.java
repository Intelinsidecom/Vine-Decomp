package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface zzo extends IInterface {

    public static abstract class zza extends Binder implements zzo {

        /* renamed from: com.google.android.gms.maps.internal.zzo$zza$zza, reason: collision with other inner class name */
        private static class C0105zza implements zzo {
            private IBinder zzoo;

            C0105zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zzoo;
            }

            @Override // com.google.android.gms.maps.internal.zzo
            public boolean onMyLocationButtonClick() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.IOnMyLocationButtonClickListener");
                    this.zzoo.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static zzo zzcK(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnMyLocationButtonClickListener");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzo)) ? new C0105zza(iBinder) : (zzo) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.maps.internal.IOnMyLocationButtonClickListener");
                    boolean zOnMyLocationButtonClick = onMyLocationButtonClick();
                    reply.writeNoException();
                    reply.writeInt(zOnMyLocationButtonClick ? 1 : 0);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IOnMyLocationButtonClickListener");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean onMyLocationButtonClick() throws RemoteException;
}
