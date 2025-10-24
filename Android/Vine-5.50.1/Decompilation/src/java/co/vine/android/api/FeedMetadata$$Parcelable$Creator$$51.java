package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class FeedMetadata$$Parcelable$Creator$$51 implements Parcelable.Creator<FeedMetadata$$Parcelable> {
    private FeedMetadata$$Parcelable$Creator$$51() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public FeedMetadata$$Parcelable createFromParcel(Parcel parcel$$459) {
        return new FeedMetadata$$Parcelable(parcel$$459);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public FeedMetadata$$Parcelable[] newArray(int size) {
        return new FeedMetadata$$Parcelable[size];
    }
}
