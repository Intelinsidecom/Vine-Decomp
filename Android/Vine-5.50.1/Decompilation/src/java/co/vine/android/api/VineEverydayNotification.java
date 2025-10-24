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
public class VineEverydayNotification implements Parcelable, VineNotification {
    public static final Parcelable.Creator<VineEverydayNotification> CREATOR = new Parcelable.Creator<VineEverydayNotification>() { // from class: co.vine.android.api.VineEverydayNotification.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineEverydayNotification createFromParcel(Parcel in) {
            return new VineEverydayNotification(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineEverydayNotification[] newArray(int size) {
            return new VineEverydayNotification[size];
        }
    };

    @JsonField(name = {"anchor", "anchorStr"})
    public String anchor;

    @JsonField(name = {"backAnchor"})
    public String backAnchor;

    @JsonField(name = {"body"})
    public String comment;

    @JsonField(name = {"commentText"})
    public String commentText;

    @JsonField(name = {"lastActivityTime"}, typeConverter = DateStringToMilliseconds.class)
    public long createdAt;

    @JsonField(name = {"entities"})
    public ArrayList<VineEntity> entities;
    public int followStatus;

    @JsonField(name = {"isNew"})
    public boolean isNew;

    @JsonField(name = {"link"})
    public String link;
    public VineMilestone milestone;

    @JsonField(name = {"activityId"})
    public long notificationId;
    public VinePost post;

    @JsonField(name = {"shortBody"})
    public String shortBody;

    @JsonField(name = {"type"})
    public String type;

    @JsonField(name = {PropertyConfiguration.USER})
    public VineUser user;

    public VineEverydayNotification() {
    }

    public VineEverydayNotification(String comment, long notificationId, String type, VinePost post, VineUser user, ArrayList<VineEntity> entities, VineMilestone milestone, long createdAt, boolean isNew, String anchor, String backAnchor, String link, String shortBody, String commentText) {
        this.comment = comment;
        this.notificationId = notificationId;
        this.type = type;
        this.entities = entities;
        this.user = user;
        this.post = post;
        this.milestone = milestone;
        this.createdAt = createdAt;
        this.isNew = isNew;
        this.anchor = anchor;
        this.backAnchor = backAnchor;
        this.link = link;
        this.shortBody = shortBody;
        this.commentText = commentText;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public VineEverydayNotification(Parcel in) {
        ClassLoader cl = getClass().getClassLoader();
        this.comment = in.readString();
        this.notificationId = in.readLong();
        this.type = in.readString();
        this.post = (VinePost) in.readParcelable(cl);
        this.user = (VineUser) in.readParcelable(cl);
        this.entities = (ArrayList) in.readSerializable();
        this.followStatus = in.readInt();
        this.milestone = (VineMilestone) in.readParcelable(cl);
        this.isNew = in.readInt() == 1;
        this.anchor = in.readString();
        this.backAnchor = in.readString();
        this.link = in.readString();
        this.shortBody = in.readString();
        this.commentText = in.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getComment());
        out.writeLong(getNotificationId());
        out.writeString(this.type);
        out.writeParcelable(this.post, flags);
        out.writeParcelable(this.user, flags);
        out.writeSerializable(getEntities());
        out.writeInt(this.followStatus);
        out.writeParcelable(this.milestone, flags);
        out.writeInt(this.isNew ? 1 : 0);
        out.writeString(this.anchor);
        out.writeString(this.backAnchor);
        out.writeString(this.link);
        out.writeString(this.shortBody);
        out.writeString(this.commentText);
    }

    @Override // co.vine.android.api.VineNotification
    public long getCreatedAt() {
        return this.createdAt;
    }

    @Override // co.vine.android.api.VineNotification
    public String getComment() {
        return this.comment;
    }

    @Override // co.vine.android.api.VineNotification
    public ArrayList<VineEntity> getEntities() {
        return this.entities;
    }

    @Override // co.vine.android.api.VineNotification
    public long getNotificationId() {
        return this.notificationId;
    }
}
