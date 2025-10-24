package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class UserDetails$$Parcelable$Creator$$23 implements Parcelable.Creator<UserDetails$$Parcelable> {
    private UserDetails$$Parcelable$Creator$$23() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public UserDetails$$Parcelable createFromParcel(Parcel parcel$$261) {
        return new UserDetails$$Parcelable(parcel$$261);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public UserDetails$$Parcelable[] newArray(int size) {
        return new UserDetails$$Parcelable[size];
    }
}
