package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class ItemPosition$$Parcelable$Creator$$21 implements Parcelable.Creator<ItemPosition$$Parcelable> {
    private ItemPosition$$Parcelable$Creator$$21() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ItemPosition$$Parcelable createFromParcel(Parcel parcel$$251) {
        return new ItemPosition$$Parcelable(parcel$$251);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ItemPosition$$Parcelable[] newArray(int size) {
        return new ItemPosition$$Parcelable[size];
    }
}
