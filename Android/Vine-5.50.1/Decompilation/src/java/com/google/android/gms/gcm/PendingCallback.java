package com.google.android.gms.gcm;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class PendingCallback implements Parcelable {
    public static final Parcelable.Creator<PendingCallback> CREATOR = new Parcelable.Creator<PendingCallback>() { // from class: com.google.android.gms.gcm.PendingCallback.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: zzeA, reason: merged with bridge method [inline-methods] */
        public PendingCallback createFromParcel(Parcel parcel) {
            return new PendingCallback(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: zzgX, reason: merged with bridge method [inline-methods] */
        public PendingCallback[] newArray(int i) {
            return new PendingCallback[i];
        }
    };
    final IBinder zzaiT;

    public PendingCallback(Parcel in) {
        this.zzaiT = in.readStrongBinder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStrongBinder(this.zzaiT);
    }
}
