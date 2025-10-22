package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.model.VineTag;
import java.util.ArrayList;
import java.util.List;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class VinePostData implements Parcelable {
    public static final Parcelable.Creator<VinePostData> CREATOR = new Parcelable.Creator<VinePostData>() { // from class: co.vine.android.api.VinePostData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePostData createFromParcel(Parcel in) {
            return new VinePostData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePostData[] newArray(int size) {
            return new VinePostData[size];
        }
    };
    public ArrayList<VineAudioMetadata> audioMetadata;
    public String avatarUrl;
    public VineByline byline;
    public VinePagedData<VineComment> comments;
    public int commentsCount;
    public long created;
    public String description;
    public long descriptionEditableTill;
    public double duration;
    public ArrayList<VineEntity> entities;
    public boolean following;
    public String foursquareVenueId;
    public boolean hidden;
    public boolean isLast;
    public long lastRefresh;
    public VinePagedData<VineLike> likes;
    public int likesCount;
    public String location;
    public VineLongform longform;
    public long loops;
    public int metadataFlags;
    public long myRepostId;
    public boolean onFire;
    public String orderId;
    public boolean originalFollowingState;
    public int postFlags;
    public long postId;
    public boolean remixDisabled;
    public VineRepost repost;
    public int revinersCount;
    public String shareUrl;
    public ArrayList<VineSource> sources;
    public String tag;
    public ArrayList<VineTag> tags;
    public String thumbnailLowUrl;
    public String thumbnailMedUrl;
    public String thumbnailUrl;
    protected List<VineVideoUrlTier> tieredUrls;
    public long timelineItemId;
    public VineUser user;
    public int userBackgroundColor;
    public long userId;
    public String username;
    public double velocity;
    public VineVenue venueData;
    public String videoLowUrl;
    public String videoPreview;
    public String videoUrl;

    public VinePostData() {
    }

    public VinePostData(Parcel in) {
        ClassLoader cl = getClass().getClassLoader();
        this.created = in.readLong();
        this.metadataFlags = in.readInt();
        this.isLast = in.readInt() == 1;
        this.postFlags = in.readInt();
        this.likesCount = in.readInt();
        this.revinersCount = in.readInt();
        this.commentsCount = in.readInt();
        this.userId = in.readLong();
        this.postId = in.readLong();
        this.timelineItemId = in.readLong();
        this.myRepostId = in.readLong();
        this.orderId = in.readString();
        this.tag = in.readString();
        this.foursquareVenueId = in.readString();
        this.description = in.readString();
        this.avatarUrl = in.readString();
        this.location = in.readString();
        this.videoLowUrl = in.readString();
        this.videoUrl = in.readString();
        this.videoPreview = in.readString();
        this.username = in.readString();
        this.shareUrl = in.readString();
        this.thumbnailLowUrl = in.readString();
        this.thumbnailMedUrl = in.readString();
        this.thumbnailUrl = in.readString();
        this.comments = (VinePagedData) in.readParcelable(cl);
        this.likes = (VinePagedData) in.readParcelable(cl);
        this.user = (VineUser) in.readParcelable(cl);
        this.venueData = (VineVenue) in.readSerializable();
        this.entities = in.readArrayList(VineEntity.class.getClassLoader());
        this.audioMetadata = in.readArrayList(VineAudioMetadata.class.getClassLoader());
        this.sources = in.readArrayList(VineSource.class.getClassLoader());
        this.repost = (VineRepost) in.readSerializable();
        this.userBackgroundColor = in.readInt();
        this.loops = in.readLong();
        this.velocity = in.readDouble();
        this.duration = in.readDouble();
        this.lastRefresh = in.readLong();
        this.onFire = in.readInt() == 1;
        this.byline = (VineByline) in.readParcelable(cl);
        this.following = in.readInt() == 1;
        this.descriptionEditableTill = in.readLong();
        this.hidden = in.readInt() == 1;
        this.tieredUrls = new ArrayList();
        in.readTypedList(this.tieredUrls, VineVideoUrlTier.CREATOR);
        this.longform = (VineLongform) Parcels.unwrap(in.readParcelable(VineLongform.class.getClassLoader()));
        this.remixDisabled = in.readInt() == 1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.created);
        out.writeInt(this.metadataFlags);
        out.writeInt(this.isLast ? 1 : 0);
        out.writeInt(this.postFlags);
        out.writeInt(this.likesCount);
        out.writeInt(this.revinersCount);
        out.writeInt(this.commentsCount);
        out.writeLong(this.userId);
        out.writeLong(this.postId);
        out.writeLong(this.timelineItemId);
        out.writeLong(this.myRepostId);
        out.writeString(this.orderId);
        out.writeString(this.tag);
        out.writeString(this.foursquareVenueId);
        out.writeString(this.description);
        out.writeString(this.avatarUrl);
        out.writeString(this.location);
        out.writeString(this.videoLowUrl);
        out.writeString(this.videoUrl);
        out.writeString(this.videoPreview);
        out.writeString(this.username);
        out.writeString(this.shareUrl);
        out.writeString(this.thumbnailLowUrl);
        out.writeString(this.thumbnailMedUrl);
        out.writeString(this.thumbnailUrl);
        out.writeParcelable(this.comments, flags);
        out.writeParcelable(this.likes, flags);
        out.writeParcelable(this.user, flags);
        out.writeSerializable(this.venueData);
        out.writeList(this.entities);
        out.writeList(this.audioMetadata);
        out.writeList(this.sources);
        out.writeSerializable(this.repost);
        out.writeInt(this.userBackgroundColor);
        out.writeLong(this.loops);
        out.writeDouble(this.velocity);
        out.writeDouble(this.duration);
        out.writeLong(this.lastRefresh);
        out.writeInt(this.onFire ? 1 : 0);
        out.writeParcelable(this.byline, flags);
        out.writeInt(this.following ? 1 : 0);
        out.writeLong(this.descriptionEditableTill);
        out.writeInt(this.hidden ? 1 : 0);
        out.writeTypedList(this.tieredUrls);
        out.writeParcelable(Parcels.wrap(this.longform), flags);
        out.writeInt(this.remixDisabled ? 1 : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VinePostData vinePost = (VinePostData) o;
        if (this.postId == vinePost.postId && this.timelineItemId == vinePost.timelineItemId && this.myRepostId == vinePost.myRepostId && this.commentsCount == vinePost.commentsCount && this.created == vinePost.created && this.metadataFlags == vinePost.metadataFlags && this.isLast == vinePost.isLast && this.likesCount == vinePost.likesCount && this.revinersCount == vinePost.revinersCount && this.postFlags == vinePost.postFlags && this.userId == vinePost.userId) {
            if (this.avatarUrl == null ? vinePost.avatarUrl != null : !this.avatarUrl.equals(vinePost.avatarUrl)) {
                return false;
            }
            if (this.comments == null ? vinePost.comments != null : !this.comments.equals(vinePost.comments)) {
                return false;
            }
            if (this.description == null ? vinePost.description != null : !this.description.equals(vinePost.description)) {
                return false;
            }
            if (this.entities == null ? vinePost.entities != null : !this.entities.equals(vinePost.entities)) {
                return false;
            }
            if (this.audioMetadata == null ? vinePost.audioMetadata != null : !this.audioMetadata.equals(vinePost.audioMetadata)) {
                return false;
            }
            if (this.sources == null ? vinePost.sources != null : !this.sources.equals(vinePost.sources)) {
                return false;
            }
            if (this.foursquareVenueId == null ? vinePost.foursquareVenueId != null : !this.foursquareVenueId.equals(vinePost.foursquareVenueId)) {
                return false;
            }
            if (this.likes == null ? vinePost.likes != null : !this.likes.equals(vinePost.likes)) {
                return false;
            }
            if (this.location == null ? vinePost.location != null : !this.location.equals(vinePost.location)) {
                return false;
            }
            if (this.orderId == null ? vinePost.orderId != null : !this.orderId.equals(vinePost.orderId)) {
                return false;
            }
            if (this.tag == null ? vinePost.tag != null : !this.tag.equals(vinePost.tag)) {
                return false;
            }
            if (this.shareUrl == null ? vinePost.shareUrl != null : !this.shareUrl.equals(vinePost.shareUrl)) {
                return false;
            }
            if (this.tags == null ? vinePost.tags != null : !this.tags.equals(vinePost.tags)) {
                return false;
            }
            if (this.thumbnailUrl == null ? vinePost.thumbnailUrl != null : !this.thumbnailUrl.equals(vinePost.thumbnailUrl)) {
                return false;
            }
            if (this.thumbnailLowUrl == null ? vinePost.thumbnailLowUrl != null : !this.thumbnailLowUrl.equals(vinePost.thumbnailLowUrl)) {
                return false;
            }
            if (this.thumbnailMedUrl == null ? vinePost.thumbnailMedUrl != null : !this.thumbnailMedUrl.equals(vinePost.thumbnailMedUrl)) {
                return false;
            }
            if (this.user == null ? vinePost.user != null : !this.user.equals(vinePost.user)) {
                return false;
            }
            if (this.username == null ? vinePost.username != null : !this.username.equals(vinePost.username)) {
                return false;
            }
            if (this.venueData == null ? vinePost.venueData != null : !this.venueData.equals(vinePost.venueData)) {
                return false;
            }
            if (this.videoLowUrl == null ? vinePost.videoLowUrl != null : !this.videoLowUrl.equals(vinePost.videoLowUrl)) {
                return false;
            }
            if (this.videoUrl == null ? vinePost.videoUrl != null : !this.videoUrl.equals(vinePost.videoUrl)) {
                return false;
            }
            if (this.videoPreview == null ? vinePost.videoPreview != null : !this.videoPreview.equals(vinePost.videoPreview)) {
                return false;
            }
            if (this.repost == null ? vinePost.repost != null : !this.repost.equals(vinePost.repost)) {
                return false;
            }
            if (this.userBackgroundColor == vinePost.userBackgroundColor && this.loops == vinePost.loops && this.velocity == vinePost.velocity && this.duration == vinePost.duration && this.lastRefresh == vinePost.lastRefresh && this.onFire == vinePost.onFire && this.byline == vinePost.byline && this.following == vinePost.following && this.descriptionEditableTill == vinePost.descriptionEditableTill && this.hidden == vinePost.hidden) {
                if (this.tieredUrls == null ? vinePost.tieredUrls != null : !this.tieredUrls.equals(vinePost.tieredUrls)) {
                    return false;
                }
                if (this.longform == null ? vinePost.longform != null : !this.longform.equals(vinePost.longform)) {
                    return false;
                }
                return this.remixDisabled == vinePost.remixDisabled;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = (int) (this.created ^ (this.created >>> 32));
        return (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((result * 31) + this.metadataFlags) * 31) + (this.isLast ? 1 : 0)) * 31) + this.postFlags) * 31) + ((int) (this.userId ^ (this.userId >>> 32)))) * 31) + ((int) (this.postId ^ (this.postId >>> 32)))) * 31) + ((int) (this.timelineItemId ^ (this.timelineItemId >>> 32)))) * 31) + ((int) (this.myRepostId ^ (this.myRepostId >>> 32)))) * 31) + this.likesCount) * 31) + this.revinersCount) * 31) + this.commentsCount) * 31) + (this.orderId != null ? this.orderId.hashCode() : 0)) * 31) + (this.tag != null ? this.tag.hashCode() : 0)) * 31) + (this.foursquareVenueId != null ? this.foursquareVenueId.hashCode() : 0)) * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + (this.avatarUrl != null ? this.avatarUrl.hashCode() : 0)) * 31) + (this.location != null ? this.location.hashCode() : 0)) * 31) + (this.videoLowUrl != null ? this.videoLowUrl.hashCode() : 0)) * 31) + (this.videoUrl != null ? this.videoUrl.hashCode() : 0)) * 31) + (this.username != null ? this.username.hashCode() : 0)) * 31) + (this.shareUrl != null ? this.shareUrl.hashCode() : 0)) * 31) + (this.thumbnailUrl != null ? this.thumbnailUrl.hashCode() : 0)) * 31) + (this.tags != null ? this.tags.hashCode() : 0)) * 31) + (this.entities != null ? this.entities.hashCode() : 0)) * 31) + (this.audioMetadata != null ? this.audioMetadata.hashCode() : 0)) * 31) + (this.sources != null ? this.sources.hashCode() : 0)) * 31) + (this.comments != null ? this.comments.hashCode() : 0)) * 31) + (this.likes != null ? this.likes.hashCode() : 0)) * 31) + (this.user != null ? this.user.hashCode() : 0)) * 31) + (this.venueData != null ? this.venueData.hashCode() : 0)) * 31) + (this.repost != null ? this.repost.hashCode() : 0)) * 31) + this.userBackgroundColor) * 31) + ((int) (this.loops ^ (this.loops >>> 32)))) * 31) + ((int) (this.velocity * 3600.0d))) * 31) + ((int) (this.duration * 3600.0d))) * 31) + ((int) (this.lastRefresh ^ (this.lastRefresh >>> 32)))) * 31) + (this.onFire ? 1 : 0)) * 31) + (this.byline != null ? this.byline.hashCode() : 0)) * 31) + (this.following ? 1 : 0)) * 31) + ((int) (this.descriptionEditableTill ^ (this.descriptionEditableTill >>> 32)))) * 31) + (this.hidden ? 1 : 0)) * 31) + (this.tieredUrls != null ? this.tieredUrls.hashCode() : 0)) * 31) + (this.longform != null ? this.longform.hashCode() : 0)) * 31) + (this.thumbnailLowUrl != null ? this.thumbnailLowUrl.hashCode() : 0)) * 31) + (this.thumbnailMedUrl != null ? this.thumbnailMedUrl.hashCode() : 0)) * 31) + (this.videoPreview != null ? this.videoPreview.hashCode() : 0)) * 31) + (this.remixDisabled ? 1 : 0);
    }
}
