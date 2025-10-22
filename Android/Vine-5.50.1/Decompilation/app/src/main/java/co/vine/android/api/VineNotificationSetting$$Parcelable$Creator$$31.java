package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class VineNotificationSetting$$Parcelable$Creator$$31 implements Parcelable.Creator<VineNotificationSetting$$Parcelable> {
    private VineNotificationSetting$$Parcelable$Creator$$31() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineNotificationSetting$$Parcelable createFromParcel(Parcel parcel$$343) {
        return new VineNotificationSetting$$Parcelable(parcel$$343);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineNotificationSetting$$Parcelable[] newArray(int size) {
        return new VineNotificationSetting$$Parcelable[size];
    }
}
