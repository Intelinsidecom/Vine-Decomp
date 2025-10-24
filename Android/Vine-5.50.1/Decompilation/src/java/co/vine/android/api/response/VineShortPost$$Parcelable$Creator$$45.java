package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class VineShortPost$$Parcelable$Creator$$45 implements Parcelable.Creator<VineShortPost$$Parcelable> {
    private VineShortPost$$Parcelable$Creator$$45() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineShortPost$$Parcelable createFromParcel(Parcel parcel$$427) {
        return new VineShortPost$$Parcelable(parcel$$427);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineShortPost$$Parcelable[] newArray(int size) {
        return new VineShortPost$$Parcelable[size];
    }
}
