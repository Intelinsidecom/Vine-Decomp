package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public final class ShareOpenGraphObject extends ShareOpenGraphValueContainer<ShareOpenGraphObject, Object> {
    public static final Parcelable.Creator<ShareOpenGraphObject> CREATOR = new Parcelable.Creator<ShareOpenGraphObject>() { // from class: com.facebook.share.model.ShareOpenGraphObject.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareOpenGraphObject createFromParcel(Parcel in) {
            return new ShareOpenGraphObject(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareOpenGraphObject[] newArray(int size) {
            return new ShareOpenGraphObject[size];
        }
    };

    ShareOpenGraphObject(Parcel in) {
        super(in);
    }
}
