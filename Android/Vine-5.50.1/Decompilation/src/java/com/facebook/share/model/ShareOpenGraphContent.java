package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.model.ShareOpenGraphAction;

/* loaded from: classes2.dex */
public final class ShareOpenGraphContent extends ShareContent<ShareOpenGraphContent, Object> {
    public static final Parcelable.Creator<ShareOpenGraphContent> CREATOR = new Parcelable.Creator<ShareOpenGraphContent>() { // from class: com.facebook.share.model.ShareOpenGraphContent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareOpenGraphContent createFromParcel(Parcel in) {
            return new ShareOpenGraphContent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareOpenGraphContent[] newArray(int size) {
            return new ShareOpenGraphContent[size];
        }
    };
    private final ShareOpenGraphAction action;
    private final String previewPropertyName;

    ShareOpenGraphContent(Parcel in) {
        super(in);
        this.action = new ShareOpenGraphAction.Builder().readFrom(in).build();
        this.previewPropertyName = in.readString();
    }

    public ShareOpenGraphAction getAction() {
        return this.action;
    }

    public String getPreviewPropertyName() {
        return this.previewPropertyName;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.share.model.ShareContent, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeParcelable(this.action, 0);
        out.writeString(this.previewPropertyName);
    }
}
