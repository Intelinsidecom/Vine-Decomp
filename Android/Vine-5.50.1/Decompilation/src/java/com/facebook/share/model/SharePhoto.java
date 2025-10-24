package com.facebook.share.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.model.ShareMedia;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public final class SharePhoto extends ShareMedia {
    public static final Parcelable.Creator<SharePhoto> CREATOR = new Parcelable.Creator<SharePhoto>() { // from class: com.facebook.share.model.SharePhoto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharePhoto createFromParcel(Parcel in) {
            return new SharePhoto(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharePhoto[] newArray(int size) {
            return new SharePhoto[size];
        }
    };
    private final Bitmap bitmap;
    private final String caption;
    private final Uri imageUrl;
    private final boolean userGenerated;

    private SharePhoto(Builder builder) {
        super(builder);
        this.bitmap = builder.bitmap;
        this.imageUrl = builder.imageUrl;
        this.userGenerated = builder.userGenerated;
        this.caption = builder.caption;
    }

    SharePhoto(Parcel in) {
        super(in);
        this.bitmap = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());
        this.imageUrl = (Uri) in.readParcelable(Uri.class.getClassLoader());
        this.userGenerated = in.readByte() != 0;
        this.caption = in.readString();
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Uri getImageUrl() {
        return this.imageUrl;
    }

    public boolean getUserGenerated() {
        return this.userGenerated;
    }

    public String getCaption() {
        return this.caption;
    }

    @Override // com.facebook.share.model.ShareMedia, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.share.model.ShareMedia, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeParcelable(this.bitmap, 0);
        out.writeParcelable(this.imageUrl, 0);
        out.writeByte((byte) (this.userGenerated ? 1 : 0));
        out.writeString(this.caption);
    }

    public static final class Builder extends ShareMedia.Builder<SharePhoto, Builder> {
        private Bitmap bitmap;
        private String caption;
        private Uri imageUrl;
        private boolean userGenerated;

        public Builder setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder setImageUrl(Uri imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setUserGenerated(boolean userGenerated) {
            this.userGenerated = userGenerated;
            return this;
        }

        public Builder setCaption(String caption) {
            this.caption = caption;
            return this;
        }

        Uri getImageUrl() {
            return this.imageUrl;
        }

        Bitmap getBitmap() {
            return this.bitmap;
        }

        public SharePhoto build() {
            return new SharePhoto(this);
        }

        @Override // com.facebook.share.model.ShareMedia.Builder
        public Builder readFrom(SharePhoto model) {
            return model == null ? this : ((Builder) super.readFrom((Builder) model)).setBitmap(model.getBitmap()).setImageUrl(model.getImageUrl()).setUserGenerated(model.getUserGenerated()).setCaption(model.getCaption());
        }

        public Builder readFrom(Parcel parcel) {
            return readFrom((SharePhoto) parcel.readParcelable(SharePhoto.class.getClassLoader()));
        }

        public static void writeListTo(Parcel out, List<SharePhoto> photos) {
            ArrayList arrayList = new ArrayList();
            for (SharePhoto photo : photos) {
                arrayList.add(photo);
            }
            out.writeTypedList(arrayList);
        }

        public static List<SharePhoto> readListFrom(Parcel in) {
            ArrayList arrayList = new ArrayList();
            in.readTypedList(arrayList, SharePhoto.CREATOR);
            return arrayList;
        }
    }
}
