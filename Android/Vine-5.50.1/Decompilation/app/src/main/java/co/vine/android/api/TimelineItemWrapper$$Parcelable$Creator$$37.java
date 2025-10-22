package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class TimelineItemWrapper$$Parcelable$Creator$$37 implements Parcelable.Creator<TimelineItemWrapper$$Parcelable> {
    private TimelineItemWrapper$$Parcelable$Creator$$37() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TimelineItemWrapper$$Parcelable createFromParcel(Parcel parcel$$385) {
        return new TimelineItemWrapper$$Parcelable(parcel$$385);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TimelineItemWrapper$$Parcelable[] newArray(int size) {
        return new TimelineItemWrapper$$Parcelable[size];
    }
}
