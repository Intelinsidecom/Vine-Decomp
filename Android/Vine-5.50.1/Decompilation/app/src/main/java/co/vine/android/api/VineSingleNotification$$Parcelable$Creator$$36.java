package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class VineSingleNotification$$Parcelable$Creator$$36 implements Parcelable.Creator<VineSingleNotification$$Parcelable> {
    private VineSingleNotification$$Parcelable$Creator$$36() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineSingleNotification$$Parcelable createFromParcel(Parcel parcel$$374) {
        return new VineSingleNotification$$Parcelable(parcel$$374);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineSingleNotification$$Parcelable[] newArray(int size) {
        return new VineSingleNotification$$Parcelable[size];
    }
}
