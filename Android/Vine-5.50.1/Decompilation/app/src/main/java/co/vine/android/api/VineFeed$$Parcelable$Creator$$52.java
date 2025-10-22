package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class VineFeed$$Parcelable$Creator$$52 implements Parcelable.Creator<VineFeed$$Parcelable> {
    private VineFeed$$Parcelable$Creator$$52() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineFeed$$Parcelable createFromParcel(Parcel parcel$$464) {
        return new VineFeed$$Parcelable(parcel$$464);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public VineFeed$$Parcelable[] newArray(int size) {
        return new VineFeed$$Parcelable[size];
    }
}
