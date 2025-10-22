package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class DeviceData$$Parcelable$Creator$$18 implements Parcelable.Creator<DeviceData$$Parcelable> {
    private DeviceData$$Parcelable$Creator$$18() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public DeviceData$$Parcelable createFromParcel(Parcel parcel$$234) {
        return new DeviceData$$Parcelable(parcel$$234);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public DeviceData$$Parcelable[] newArray(int size) {
        return new DeviceData$$Parcelable[size];
    }
}
