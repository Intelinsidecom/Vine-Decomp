package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class ActivityDetails$$Parcelable$Creator$$17 implements Parcelable.Creator<ActivityDetails$$Parcelable> {
    private ActivityDetails$$Parcelable$Creator$$17() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ActivityDetails$$Parcelable createFromParcel(Parcel parcel$$225) {
        return new ActivityDetails$$Parcelable(parcel$$225);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ActivityDetails$$Parcelable[] newArray(int size) {
        return new ActivityDetails$$Parcelable[size];
    }
}
