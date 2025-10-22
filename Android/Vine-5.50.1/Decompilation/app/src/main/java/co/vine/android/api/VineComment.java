package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.response.DateStringToMilliseconds;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;
import twitter4j.conf.PropertyConfiguration;

@JsonObject
/* loaded from: classes.dex */
public class VineComment implements Parcelable {
    public static final Parcelable.Creator<VineComment> CREATOR = new Parcelable.Creator<VineComment>() { // from class: co.vine.android.api.VineComment.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineComment createFromParcel(Parcel in) {
            return new VineComment(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineComment[] newArray(int size) {
            return new VineComment[size];
        }
    };

    @JsonField(name = {"avatarUrl"})
    public String avatarUrl;

    @JsonField(name = {"comment"})
    public String comment;

    @JsonField(name = {"commentId"})
    public long commentId;

    @JsonField(name = {"entities"})
    public ArrayList<VineEntity> entities;

    @JsonField(name = {"location"})
    public String location;

    @JsonField(name = {"created"}, typeConverter = DateStringToMilliseconds.class)
    public long timestamp;
    public ArrayList<VineEntity> transientEntities;

    @JsonField(name = {"twitterScreenname"})
    public String twitterScreenname;

    @JsonField(name = {"twitterVerified"})
    public boolean twitterVerified;

    @JsonField(name = {PropertyConfiguration.USER})
    public VineUser user;

    @JsonField(name = {"userId"})
    public long userId;

    @JsonField(name = {"username"})
    public String username;

    @JsonField(name = {"verified"})
    public boolean verified;

    public VineComment() {
    }

    public VineComment(Parcel in) {
        this.commentId = in.readLong();
        this.comment = in.readString();
        this.avatarUrl = in.readString();
        this.user = (VineUser) in.readParcelable(VineUser.class.getClassLoader());
        this.timestamp = in.readLong();
        this.location = in.readString();
        this.userId = in.readLong();
        this.username = in.readString();
        this.verified = in.readInt() == 1;
        this.entities = (ArrayList) in.readSerializable();
        this.twitterScreenname = in.readString();
        this.twitterVerified = in.readInt() == 1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.commentId);
        dest.writeString(this.comment);
        dest.writeString(this.avatarUrl);
        dest.writeParcelable(this.user, flags);
        dest.writeLong(this.timestamp);
        dest.writeString(this.location);
        dest.writeLong(this.userId);
        dest.writeString(this.username);
        dest.writeInt(this.verified ? 1 : 0);
        dest.writeSerializable(this.entities);
        dest.writeString(this.twitterScreenname);
        dest.writeInt(this.twitterVerified ? 1 : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineComment that = (VineComment) o;
        if (this.commentId == that.commentId && this.timestamp == that.timestamp && this.userId == that.userId && this.verified == that.verified) {
            if (this.avatarUrl == null ? that.avatarUrl != null : !this.avatarUrl.equals(that.avatarUrl)) {
                return false;
            }
            if (this.comment == null ? that.comment != null : !this.comment.equals(that.comment)) {
                return false;
            }
            if (this.entities == null ? that.entities != null : !this.entities.equals(that.entities)) {
                return false;
            }
            if (this.location == null ? that.location != null : !this.location.equals(that.location)) {
                return false;
            }
            if (this.user == null ? that.user != null : !this.user.equals(that.user)) {
                return false;
            }
            if (this.username == null ? that.username != null : !this.username.equals(that.username)) {
                return false;
            }
            if (this.twitterScreenname == null ? that.twitterScreenname != null : !this.twitterScreenname.equals(that.twitterScreenname)) {
                return false;
            }
            return this.twitterVerified == that.twitterVerified;
        }
        return false;
    }

    public int hashCode() {
        int result = (int) (this.commentId ^ (this.commentId >>> 32));
        return (((((((((((((((((((((((result * 31) + (this.comment != null ? this.comment.hashCode() : 0)) * 31) + (this.avatarUrl != null ? this.avatarUrl.hashCode() : 0)) * 31) + (this.user != null ? this.user.hashCode() : 0)) * 31) + ((int) (this.timestamp ^ (this.timestamp >>> 32)))) * 31) + (this.location != null ? this.location.hashCode() : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)))) * 31) + (this.username != null ? this.username.hashCode() : 0)) * 31) + (this.verified ? 1 : 0)) * 31) + (this.entities != null ? this.entities.hashCode() : 0)) * 31) + (this.verified ? 1 : 0)) * 31) + (this.twitterScreenname != null ? this.twitterScreenname.hashCode() : 0)) * 31) + (this.twitterVerified ? 1 : 0);
    }
}
