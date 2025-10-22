package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.response.DateStringToMilliseconds;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import twitter4j.conf.PropertyConfiguration;

@JsonObject
/* loaded from: classes.dex */
public class VineLike implements Parcelable {
    public static final Parcelable.Creator<VineLike> CREATOR = new Parcelable.Creator<VineLike>() { // from class: co.vine.android.api.VineLike.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineLike createFromParcel(Parcel in) {
            return new VineLike(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineLike[] newArray(int size) {
            return new VineLike[size];
        }
    };

    @JsonField(name = {"avatarUrl"})
    public String avatarUrl;

    @JsonField(name = {"blocked"})
    public Integer blocked;

    @JsonField(name = {"created"}, typeConverter = DateStringToMilliseconds.class)
    public long created;

    @JsonField(name = {"following"})
    public Integer following;

    @JsonField(name = {"likeId"})
    public long likeId;

    @JsonField(name = {"location"})
    public String location;

    @JsonField(name = {"postId"})
    public long postId;

    @JsonField(name = {PropertyConfiguration.USER})
    public VineUser user;

    @JsonField(name = {"userId"})
    public long userId;

    @JsonField(name = {"username"})
    public String username;

    @JsonField(name = {"verified"})
    public int verified;

    public VineLike() {
    }

    public VineLike(Parcel in) {
        this.postId = in.readLong();
        this.avatarUrl = in.readString();
        this.location = in.readString();
        this.username = in.readString();
        this.created = in.readLong();
        this.likeId = in.readLong();
        this.userId = in.readLong();
        this.verified = in.readInt();
        this.user = (VineUser) in.readParcelable(VineUser.class.getClassLoader());
    }

    public static VineLike getMeLike(long postId, long userId, String username) {
        VineLike meLike = new VineLike();
        meLike.likeId = -2147483648L;
        meLike.postId = postId;
        meLike.userId = userId;
        meLike.username = username;
        return meLike;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.postId);
        out.writeString(this.avatarUrl);
        out.writeString(this.location);
        out.writeString(this.username);
        out.writeLong(this.created);
        out.writeLong(this.likeId);
        out.writeLong(this.userId);
        out.writeInt(this.verified);
        out.writeParcelable(this.user, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineLike vineLike = (VineLike) o;
        if (this.created == vineLike.created && this.likeId == vineLike.likeId && this.postId == vineLike.postId && this.userId == vineLike.userId && this.verified == vineLike.verified) {
            if (this.avatarUrl == null ? vineLike.avatarUrl != null : !this.avatarUrl.equals(vineLike.avatarUrl)) {
                return false;
            }
            if (this.location == null ? vineLike.location != null : !this.location.equals(vineLike.location)) {
                return false;
            }
            if (this.user == null ? vineLike.user != null : !this.user.equals(vineLike.user)) {
                return false;
            }
            if (this.username != null) {
                if (this.username.equals(vineLike.username)) {
                    return true;
                }
            } else if (vineLike.username == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = (int) (this.postId ^ (this.postId >>> 32));
        return (((((((((((((((result * 31) + (this.avatarUrl != null ? this.avatarUrl.hashCode() : 0)) * 31) + (this.location != null ? this.location.hashCode() : 0)) * 31) + (this.username != null ? this.username.hashCode() : 0)) * 31) + ((int) (this.created ^ (this.created >>> 32)))) * 31) + ((int) (this.likeId ^ (this.likeId >>> 32)))) * 31) + ((int) (this.userId ^ (this.userId >>> 32)))) * 31) + this.verified) * 31) + (this.user != null ? this.user.hashCode() : 0);
    }
}
