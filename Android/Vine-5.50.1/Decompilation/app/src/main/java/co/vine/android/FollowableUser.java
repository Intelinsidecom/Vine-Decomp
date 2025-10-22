package co.vine.android;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.VineUser;

/* loaded from: classes.dex */
public class FollowableUser extends VineUser {
    public static final Parcelable.Creator<FollowableUser> CREATOR = new Parcelable.Creator<FollowableUser>() { // from class: co.vine.android.FollowableUser.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FollowableUser createFromParcel(Parcel in) {
            return new FollowableUser(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FollowableUser[] newArray(int size) {
            return new FollowableUser[size];
        }
    };
    boolean isTwitterFollow;

    public FollowableUser(Parcel in) {
        super(in);
        this.isTwitterFollow = false;
        this.isTwitterFollow = in.readInt() == 1;
    }

    @Override // co.vine.android.api.VineUser, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(this.isTwitterFollow ? 1 : 0);
    }
}
