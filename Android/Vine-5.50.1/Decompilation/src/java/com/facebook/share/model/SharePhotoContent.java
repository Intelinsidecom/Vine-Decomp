package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.model.SharePhoto;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public final class SharePhotoContent extends ShareContent<SharePhotoContent, Object> {
    public static final Parcelable.Creator<SharePhotoContent> CREATOR = new Parcelable.Creator<SharePhotoContent>() { // from class: com.facebook.share.model.SharePhotoContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharePhotoContent createFromParcel(Parcel in) {
            return new SharePhotoContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharePhotoContent[] newArray(int size) {
            return new SharePhotoContent[size];
        }
    };
    private final List<SharePhoto> photos;

    SharePhotoContent(Parcel in) {
        super(in);
        this.photos = Collections.unmodifiableList(SharePhoto.Builder.readListFrom(in));
    }

    public List<SharePhoto> getPhotos() {
        return this.photos;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        SharePhoto.Builder.writeListTo(out, this.photos);
    }
}
