package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class ClientEvent$$Parcelable$Creator$$1 implements Parcelable.Creator<ClientEvent$$Parcelable> {
    private ClientEvent$$Parcelable$Creator$$1() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ClientEvent$$Parcelable createFromParcel(Parcel parcel$$63) {
        return new ClientEvent$$Parcelable(parcel$$63);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ClientEvent$$Parcelable[] newArray(int size) {
        return new ClientEvent$$Parcelable[size];
    }
}
