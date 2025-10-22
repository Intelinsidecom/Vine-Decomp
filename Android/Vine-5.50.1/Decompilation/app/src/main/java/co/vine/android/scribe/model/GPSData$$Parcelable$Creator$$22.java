package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class GPSData$$Parcelable$Creator$$22 implements Parcelable.Creator<GPSData$$Parcelable> {
    private GPSData$$Parcelable$Creator$$22() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public GPSData$$Parcelable createFromParcel(Parcel parcel$$256) {
        return new GPSData$$Parcelable(parcel$$256);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public GPSData$$Parcelable[] newArray(int size) {
        return new GPSData$$Parcelable[size];
    }
}
