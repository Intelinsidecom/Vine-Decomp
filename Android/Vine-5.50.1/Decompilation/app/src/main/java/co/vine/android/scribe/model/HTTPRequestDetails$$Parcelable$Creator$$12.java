package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class HTTPRequestDetails$$Parcelable$Creator$$12 implements Parcelable.Creator<HTTPRequestDetails$$Parcelable> {
    private HTTPRequestDetails$$Parcelable$Creator$$12() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public HTTPRequestDetails$$Parcelable createFromParcel(Parcel parcel$$196) {
        return new HTTPRequestDetails$$Parcelable(parcel$$196);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public HTTPRequestDetails$$Parcelable[] newArray(int size) {
        return new HTTPRequestDetails$$Parcelable[size];
    }
}
