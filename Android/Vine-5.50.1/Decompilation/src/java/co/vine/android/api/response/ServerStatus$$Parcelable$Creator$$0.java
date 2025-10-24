package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class ServerStatus$$Parcelable$Creator$$0 implements Parcelable.Creator<ServerStatus$$Parcelable> {
    private ServerStatus$$Parcelable$Creator$$0() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ServerStatus$$Parcelable createFromParcel(Parcel parcel$$4) {
        return new ServerStatus$$Parcelable(parcel$$4);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ServerStatus$$Parcelable[] newArray(int size) {
        return new ServerStatus$$Parcelable[size];
    }
}
