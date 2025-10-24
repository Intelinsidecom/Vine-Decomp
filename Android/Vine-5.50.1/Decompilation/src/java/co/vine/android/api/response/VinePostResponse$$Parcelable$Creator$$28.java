package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class VinePostResponse$$Parcelable$Creator$$28 implements Parcelable.Creator<VinePostResponse$$Parcelable> {
    private VinePostResponse$$Parcelable$Creator$$28() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VinePostResponse$$Parcelable createFromParcel(Parcel parcel$$328) {
        return new VinePostResponse$$Parcelable(parcel$$328);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VinePostResponse$$Parcelable[] newArray(int size) {
        return new VinePostResponse$$Parcelable[size];
    }
}
