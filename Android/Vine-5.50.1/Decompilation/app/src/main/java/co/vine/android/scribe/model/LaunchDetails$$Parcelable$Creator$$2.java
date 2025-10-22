package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class LaunchDetails$$Parcelable$Creator$$2 implements Parcelable.Creator<LaunchDetails$$Parcelable> {
    private LaunchDetails$$Parcelable$Creator$$2() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LaunchDetails$$Parcelable createFromParcel(Parcel parcel$$68) {
        return new LaunchDetails$$Parcelable(parcel$$68);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LaunchDetails$$Parcelable[] newArray(int size) {
        return new LaunchDetails$$Parcelable[size];
    }
}
