package com.google.android.gms.iid;

import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.iid.zzb;

/* loaded from: classes2.dex */
public class MessengerCompat implements Parcelable {
    public static final Parcelable.Creator<MessengerCompat> CREATOR = new Parcelable.Creator<MessengerCompat>() { // from class: com.google.android.gms.iid.MessengerCompat.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: zzeF, reason: merged with bridge method [inline-methods] */
        public MessengerCompat createFromParcel(Parcel parcel) {
            IBinder strongBinder = parcel.readStrongBinder();
            if (strongBinder != null) {
                return new MessengerCompat(strongBinder);
            }
            return null;
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: zzhd, reason: merged with bridge method [inline-methods] */
        public MessengerCompat[] newArray(int i) {
            return new MessengerCompat[i];
        }
    };
    Messenger zzaKq;
    zzb zzaKr;

    private final class zza extends zzb.zza {
        Handler handler;

        zza(Handler handler) {
            this.handler = handler;
        }

        @Override // com.google.android.gms.iid.zzb
        public void send(Message msg) throws RemoteException {
            msg.arg2 = Binder.getCallingUid();
            this.handler.dispatchMessage(msg);
        }
    }

    public MessengerCompat(Handler handler) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.zzaKq = new Messenger(handler);
        } else {
            this.zzaKr = new zza(handler);
        }
    }

    public MessengerCompat(IBinder target) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.zzaKq = new Messenger(target);
        } else {
            this.zzaKr = zzb.zza.zzcd(target);
        }
    }

    public static int zzc(Message message) {
        return Build.VERSION.SDK_INT >= 21 ? zzd(message) : message.arg2;
    }

    private static int zzd(Message message) {
        return message.sendingUid;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object otherObj) {
        if (otherObj == null) {
            return false;
        }
        try {
            return getBinder().equals(((MessengerCompat) otherObj).getBinder());
        } catch (ClassCastException e) {
            return false;
        }
    }

    public IBinder getBinder() {
        return this.zzaKq != null ? this.zzaKq.getBinder() : this.zzaKr.asBinder();
    }

    public int hashCode() {
        return getBinder().hashCode();
    }

    public void send(Message message) throws RemoteException {
        if (this.zzaKq != null) {
            this.zzaKq.send(message);
        } else {
            this.zzaKr.send(message);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (this.zzaKq != null) {
            out.writeStrongBinder(this.zzaKq.getBinder());
        } else {
            out.writeStrongBinder(this.zzaKr.asBinder());
        }
    }
}
