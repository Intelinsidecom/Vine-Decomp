package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class TimingDetails$$Parcelable$Creator$$6 implements Parcelable.Creator<TimingDetails$$Parcelable> {
    private TimingDetails$$Parcelable$Creator$$6() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TimingDetails$$Parcelable createFromParcel(Parcel parcel$$162) {
        return new TimingDetails$$Parcelable(parcel$$162);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TimingDetails$$Parcelable[] newArray(int size) {
        return new TimingDetails$$Parcelable[size];
    }
}
