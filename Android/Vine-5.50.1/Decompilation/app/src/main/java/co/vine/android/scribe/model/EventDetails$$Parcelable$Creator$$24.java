package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class EventDetails$$Parcelable$Creator$$24 implements Parcelable.Creator<EventDetails$$Parcelable> {
    private EventDetails$$Parcelable$Creator$$24() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public EventDetails$$Parcelable createFromParcel(Parcel parcel$$304) {
        return new EventDetails$$Parcelable(parcel$$304);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public EventDetails$$Parcelable[] newArray(int size) {
        return new EventDetails$$Parcelable[size];
    }
}
