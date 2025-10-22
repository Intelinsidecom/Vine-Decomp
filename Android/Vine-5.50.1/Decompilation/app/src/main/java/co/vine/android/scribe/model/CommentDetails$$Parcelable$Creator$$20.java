package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class CommentDetails$$Parcelable$Creator$$20 implements Parcelable.Creator<CommentDetails$$Parcelable> {
    private CommentDetails$$Parcelable$Creator$$20() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CommentDetails$$Parcelable createFromParcel(Parcel parcel$$246) {
        return new CommentDetails$$Parcelable(parcel$$246);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CommentDetails$$Parcelable[] newArray(int size) {
        return new CommentDetails$$Parcelable[size];
    }
}
