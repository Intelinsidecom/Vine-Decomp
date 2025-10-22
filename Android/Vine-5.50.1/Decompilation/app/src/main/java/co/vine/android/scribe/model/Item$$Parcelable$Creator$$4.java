package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Item$$Parcelable$Creator$$4 implements Parcelable.Creator<Item$$Parcelable> {
    private Item$$Parcelable$Creator$$4() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public Item$$Parcelable createFromParcel(Parcel parcel$$152) {
        return new Item$$Parcelable(parcel$$152);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public Item$$Parcelable[] newArray(int size) {
        return new Item$$Parcelable[size];
    }
}
