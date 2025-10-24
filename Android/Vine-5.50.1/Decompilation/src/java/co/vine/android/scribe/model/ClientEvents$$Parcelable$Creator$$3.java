package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class ClientEvents$$Parcelable$Creator$$3 implements Parcelable.Creator<ClientEvents$$Parcelable> {
    private ClientEvents$$Parcelable$Creator$$3() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ClientEvents$$Parcelable createFromParcel(Parcel parcel$$129) {
        return new ClientEvents$$Parcelable(parcel$$129);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ClientEvents$$Parcelable[] newArray(int size) {
        return new ClientEvents$$Parcelable[size];
    }
}
