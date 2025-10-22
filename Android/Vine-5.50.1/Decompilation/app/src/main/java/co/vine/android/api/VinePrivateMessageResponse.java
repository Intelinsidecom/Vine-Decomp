package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class VinePrivateMessageResponse implements Parcelable {
    public static final Parcelable.Creator<VinePrivateMessageResponse> CREATOR = new Parcelable.Creator<VinePrivateMessageResponse>() { // from class: co.vine.android.api.VinePrivateMessageResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePrivateMessageResponse createFromParcel(Parcel in) {
            return new VinePrivateMessageResponse(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePrivateMessageResponse[] newArray(int size) {
            return new VinePrivateMessageResponse[size];
        }
    };
    public final long conversationId;
    public final VineError error;
    public final long messageId;
    public final int networkType;
    public final VineRecipient recipient;
    public final String shareUrl;
    public final String thumbnailUrl;
    public final String videoUrl;

    public VinePrivateMessageResponse(VineRecipient recipient, long conversationId, long messageId, String videoUrl, String thumbnailUrl, String shareUrl, VineError error, int networkType) {
        this.recipient = recipient;
        this.conversationId = conversationId;
        this.messageId = messageId;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.shareUrl = shareUrl;
        this.error = error;
        this.networkType = networkType;
    }

    public VinePrivateMessageResponse(Parcel in) {
        this.recipient = (VineRecipient) in.readParcelable(VineRecipient.class.getClassLoader());
        this.conversationId = in.readLong();
        this.messageId = in.readLong();
        this.videoUrl = in.readString();
        this.thumbnailUrl = in.readString();
        this.shareUrl = in.readString();
        this.error = (VineError) in.readParcelable(VineError.class.getClassLoader());
        this.networkType = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.recipient, flags);
        out.writeLong(this.conversationId);
        out.writeLong(this.messageId);
        out.writeString(this.videoUrl);
        out.writeString(this.thumbnailUrl);
        out.writeString(this.shareUrl);
        out.writeParcelable(this.error, flags);
        out.writeInt(this.networkType);
    }
}
