package co.vine.android.api;

import android.text.TextUtils;
import co.vine.android.api.response.DateStringToMilliseconds;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.video.VideoKey;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineSingleNotification implements VineNotification {

    @JsonField(name = {"avatarUrl"})
    public String avatarUrl;

    @JsonField(name = {"body"})
    public String comment;

    @JsonField(name = {"conversationId"})
    public long conversationId;

    @JsonField(name = {"avatarUrl"})
    public long conversationRowId;

    @JsonField(name = {"created"}, typeConverter = DateStringToMilliseconds.class)
    public long createdAt;

    @JsonField(name = {"entities"})
    public ArrayList<VineEntity> entities;
    public int messageCount;

    @JsonField(name = {"notificationId"})
    public long notificationId;

    @JsonField(name = {"notificationTypeId"})
    public int notificationTypeId;

    @JsonField(name = {"onboard"})
    public String onboard;

    @JsonField(name = {"postId"})
    public long postId;
    public String prettyComment;

    @JsonField(name = {"recipientUserId"})
    public long recipientUserId;

    @JsonField(name = {"thumbnailUrl"})
    public String thumbnailUrl;

    @JsonField(name = {"title"})
    public String title;

    @JsonField(name = {"unreadMessageCount"})
    public long unreadMessageCount;

    @JsonField(name = {"url"})
    public String url;

    @JsonField(name = {"userId"})
    public long userId;
    public String username;

    @JsonField(name = {"verified"})
    public boolean verified;
    public ArrayList<ImageKey> imageKeys = new ArrayList<>();
    public ArrayList<VideoKey> videoKeys = new ArrayList<>();

    public boolean isMessaging() {
        return this.notificationTypeId == 18 || this.notificationTypeId == 25;
    }

    public String toString() {
        if (this.notificationTypeId == 33) {
            return "favorite_user";
        }
        if (!TextUtils.isEmpty(this.onboard)) {
            return "onboarding_" + this.onboard;
        }
        if (isMessaging()) {
            return "message";
        }
        if (this.postId != 0) {
            return "post";
        }
        return "activity";
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
