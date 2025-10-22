package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class VineChannel$$Parcelable$Creator$$33 implements Parcelable.Creator<VineChannel$$Parcelable> {
    private VineChannel$$Parcelable$Creator$$33() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineChannel$$Parcelable createFromParcel(Parcel parcel$$357) {
        return new VineChannel$$Parcelable(parcel$$357);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineChannel$$Parcelable[] newArray(int size) {
        return new VineChannel$$Parcelable[size];
    }
}
