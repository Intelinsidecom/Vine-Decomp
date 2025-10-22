package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class TwitterUser$$Parcelable$Creator$$34 implements Parcelable.Creator<TwitterUser$$Parcelable> {
    private TwitterUser$$Parcelable$Creator$$34() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TwitterUser$$Parcelable createFromParcel(Parcel parcel$$362) {
        return new TwitterUser$$Parcelable(parcel$$362);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TwitterUser$$Parcelable[] newArray(int size) {
        return new TwitterUser$$Parcelable[size];
    }
}
