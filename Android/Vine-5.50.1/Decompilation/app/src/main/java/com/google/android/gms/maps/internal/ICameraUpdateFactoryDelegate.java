package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/* loaded from: classes.dex */
public interface ICameraUpdateFactoryDelegate extends IInterface {

    public static abstract class zza extends Binder implements ICameraUpdateFactoryDelegate {

        /* renamed from: com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate$zza$zza, reason: collision with other inner class name */
        private static class C0082zza implements ICameraUpdateFactoryDelegate {
            private IBinder zzoo;

            C0082zza(IBinder iBinder) {
                this.zzoo = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zzoo;
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd newCameraPosition(CameraPosition cameraPosition) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    if (cameraPosition != null) {
                        parcelObtain.writeInt(1);
                        cameraPosition.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zzoo.transact(7, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd newLatLng(LatLng latLng) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    if (latLng != null) {
                        parcelObtain.writeInt(1);
                        latLng.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zzoo.transact(8, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd newLatLngBounds(LatLngBounds bounds, int padding) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    if (bounds != null) {
                        parcelObtain.writeInt(1);
                        bounds.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeInt(padding);
                    this.zzoo.transact(10, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd newLatLngBoundsWithSize(LatLngBounds bounds, int width, int height, int padding) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    if (bounds != null) {
                        parcelObtain.writeInt(1);
                        bounds.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeInt(width);
                    parcelObtain.writeInt(height);
                    parcelObtain.writeInt(padding);
                    this.zzoo.transact(11, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd newLatLngZoom(LatLng latLng, float zoom) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    if (latLng != null) {
                        parcelObtain.writeInt(1);
                        latLng.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeFloat(zoom);
                    this.zzoo.transact(9, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd scrollBy(float xPixel, float yPixel) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    parcelObtain.writeFloat(xPixel);
                    parcelObtain.writeFloat(yPixel);
                    this.zzoo.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd zoomBy(float amount) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    parcelObtain.writeFloat(amount);
                    this.zzoo.transact(5, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd zoomByWithFocus(float amount, int screenFocusX, int screenFocusY) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    parcelObtain.writeFloat(amount);
                    parcelObtain.writeInt(screenFocusX);
                    parcelObtain.writeInt(screenFocusY);
                    this.zzoo.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd zoomIn() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    this.zzoo.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd zoomOut() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    this.zzoo.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate
            public com.google.android.gms.dynamic.zzd zoomTo(float zoom) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    parcelObtain.writeFloat(zoom);
                    this.zzoo.transact(4, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbs(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static ICameraUpdateFactoryDelegate zzcs(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof ICameraUpdateFactoryDelegate)) ? new C0082zza(iBinder) : (ICameraUpdateFactoryDelegate) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarZoomIn = zoomIn();
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarZoomIn != null ? zzdVarZoomIn.asBinder() : null);
                    return true;
                case 2:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarZoomOut = zoomOut();
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarZoomOut != null ? zzdVarZoomOut.asBinder() : null);
                    return true;
                case 3:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarScrollBy = scrollBy(data.readFloat(), data.readFloat());
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarScrollBy != null ? zzdVarScrollBy.asBinder() : null);
                    return true;
                case 4:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarZoomTo = zoomTo(data.readFloat());
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarZoomTo != null ? zzdVarZoomTo.asBinder() : null);
                    return true;
                case 5:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarZoomBy = zoomBy(data.readFloat());
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarZoomBy != null ? zzdVarZoomBy.asBinder() : null);
                    return true;
                case 6:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarZoomByWithFocus = zoomByWithFocus(data.readFloat(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarZoomByWithFocus != null ? zzdVarZoomByWithFocus.asBinder() : null);
                    return true;
                case 7:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarNewCameraPosition = newCameraPosition(data.readInt() != 0 ? CameraPosition.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarNewCameraPosition != null ? zzdVarNewCameraPosition.asBinder() : null);
                    return true;
                case 8:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarNewLatLng = newLatLng(data.readInt() != 0 ? LatLng.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarNewLatLng != null ? zzdVarNewLatLng.asBinder() : null);
                    return true;
                case 9:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarNewLatLngZoom = newLatLngZoom(data.readInt() != 0 ? LatLng.CREATOR.createFromParcel(data) : null, data.readFloat());
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarNewLatLngZoom != null ? zzdVarNewLatLngZoom.asBinder() : null);
                    return true;
                case 10:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarNewLatLngBounds = newLatLngBounds(data.readInt() != 0 ? LatLngBounds.CREATOR.createFromParcel(data) : null, data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarNewLatLngBounds != null ? zzdVarNewLatLngBounds.asBinder() : null);
                    return true;
                case 11:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    com.google.android.gms.dynamic.zzd zzdVarNewLatLngBoundsWithSize = newLatLngBoundsWithSize(data.readInt() != 0 ? LatLngBounds.CREATOR.createFromParcel(data) : null, data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(zzdVarNewLatLngBoundsWithSize != null ? zzdVarNewLatLngBoundsWithSize.asBinder() : null);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    com.google.android.gms.dynamic.zzd newCameraPosition(CameraPosition cameraPosition) throws RemoteException;

    com.google.android.gms.dynamic.zzd newLatLng(LatLng latLng) throws RemoteException;

    com.google.android.gms.dynamic.zzd newLatLngBounds(LatLngBounds latLngBounds, int i) throws RemoteException;

    com.google.android.gms.dynamic.zzd newLatLngBoundsWithSize(LatLngBounds latLngBounds, int i, int i2, int i3) throws RemoteException;

    com.google.android.gms.dynamic.zzd newLatLngZoom(LatLng latLng, float f) throws RemoteException;

    com.google.android.gms.dynamic.zzd scrollBy(float f, float f2) throws RemoteException;

    com.google.android.gms.dynamic.zzd zoomBy(float f) throws RemoteException;

    com.google.android.gms.dynamic.zzd zoomByWithFocus(float f, int i, int i2) throws RemoteException;

    com.google.android.gms.dynamic.zzd zoomIn() throws RemoteException;

    com.google.android.gms.dynamic.zzd zoomOut() throws RemoteException;

    com.google.android.gms.dynamic.zzd zoomTo(float f) throws RemoteException;
}
