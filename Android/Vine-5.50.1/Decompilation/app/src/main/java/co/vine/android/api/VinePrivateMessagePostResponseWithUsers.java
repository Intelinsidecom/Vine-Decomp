package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class VinePrivateMessagePostResponseWithUsers implements Parcelable {
    public static final Parcelable.Creator<VinePrivateMessagePostResponseWithUsers> CREATOR = new Parcelable.Creator<VinePrivateMessagePostResponseWithUsers>() { // from class: co.vine.android.api.VinePrivateMessagePostResponseWithUsers.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePrivateMessagePostResponseWithUsers createFromParcel(Parcel in) {
            return new VinePrivateMessagePostResponseWithUsers(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePrivateMessagePostResponseWithUsers[] newArray(int size) {
            return new VinePrivateMessagePostResponseWithUsers[size];
        }
    };
    public final VinePost post;
    public final ArrayList<VineRecipient> recipients;
    public final ArrayList<VinePrivateMessageResponse> responses;
    public final ArrayList<VineUser> users;

    public VinePrivateMessagePostResponseWithUsers(ArrayList<VinePrivateMessageResponse> responses, ArrayList<VineRecipient> recipients, ArrayList<VineUser> users, VinePost post) {
        this.responses = responses;
        this.recipients = recipients;
        this.users = users;
        this.post = post;
    }

    public VinePrivateMessagePostResponseWithUsers(Parcel in) {
        this.responses = (ArrayList) in.readSerializable();
        this.recipients = (ArrayList) in.readSerializable();
        this.users = (ArrayList) in.readSerializable();
        this.post = (VinePost) in.readParcelable(VinePost.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(this.responses);
        out.writeSerializable(this.recipients);
        out.writeSerializable(this.users);
        out.writeParcelable(this.post, flags);
    }
}
