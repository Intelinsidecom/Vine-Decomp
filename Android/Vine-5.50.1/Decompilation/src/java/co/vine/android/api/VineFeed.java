package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class VineFeed implements Parcelable, TimelineItem {
    public static final Parcelable.Creator<VineFeed> CREATOR = new Parcelable.Creator<VineFeed>() { // from class: co.vine.android.api.VineFeed.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineFeed createFromParcel(Parcel in) {
            return new VineFeed(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineFeed[] newArray(int size) {
            return new VineFeed[size];
        }
    };
    public String backgroundColor;
    public VinePost coverPost;
    public String description;
    public long feedId;
    public FeedMetadata feedMetadata;
    public String link;
    public String secondaryColor;
    public String title;
    public String type;
    public VineUser user;
    public long userId;

    public VineFeed() {
    }

    public VineFeed(VineFeedBuilder builder) {
        this.feedId = builder.feedId;
        this.title = builder.title;
        this.description = builder.description;
        this.backgroundColor = builder.backgroundColor;
        this.secondaryColor = builder.secondaryColor;
        this.user = builder.user;
        this.userId = builder.userId;
        this.type = builder.type;
        this.coverPost = builder.coverPost;
        this.feedMetadata = builder.feedMetadata;
        this.link = builder.link;
    }

    private VineFeed(Parcel in) {
        ClassLoader cl = getClass().getClassLoader();
        this.feedId = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.backgroundColor = in.readString();
        this.secondaryColor = in.readString();
        this.user = (VineUser) in.readParcelable(cl);
        this.userId = in.readLong();
        this.type = in.readString();
        this.link = in.readString();
        this.coverPost = (VinePost) in.readParcelable(cl);
        this.feedMetadata = (FeedMetadata) in.readParcelable(cl);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.feedId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.backgroundColor);
        dest.writeString(this.secondaryColor);
        dest.writeParcelable(this.user, flags);
        dest.writeLong(this.userId);
        dest.writeString(this.type);
        dest.writeString(this.link);
        dest.writeParcelable(this.coverPost, flags);
        dest.writeParcelable(this.feedMetadata, flags);
    }

    @Override // co.vine.android.api.TimelineItem
    public long getId() {
        return this.feedId;
    }

    @Override // co.vine.android.api.TimelineItem
    public TimelineItemType getType() {
        return TimelineItemType.FEED;
    }

    public static class VineFeedBuilder {
        public String backgroundColor;
        public VinePost coverPost;
        public String description;
        public long feedId;
        public FeedMetadata feedMetadata;
        public String link;
        public String secondaryColor;
        public String title;
        public String type;
        public VineUser user;
        public long userId;

        public VineFeedBuilder feedId(long feedId) {
            this.feedId = feedId;
            return this;
        }

        public VineFeedBuilder title(String title) {
            this.title = title;
            return this;
        }

        public VineFeedBuilder description(String description) {
            this.description = description;
            return this;
        }

        public VineFeedBuilder backgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public VineFeedBuilder secondaryColor(String secondaryColor) {
            this.secondaryColor = secondaryColor;
            return this;
        }

        public VineFeedBuilder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public VineFeedBuilder type(String type) {
            this.type = type;
            return this;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        int result = (int) (this.feedId ^ (this.feedId >>> 32));
        return (((((((((((((((((result * 31) + (this.title != null ? this.title.hashCode() : 0)) * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + (this.backgroundColor != null ? this.backgroundColor.hashCode() : 0)) * 31) + (this.secondaryColor != null ? this.secondaryColor.hashCode() : 0)) * 31) + (this.user != null ? this.user.hashCode() : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)))) * 31) + (this.type != null ? this.type.hashCode() : 0)) * 31) + (this.feedMetadata != null ? this.feedMetadata.hashCode() : 0)) * 31) + (this.coverPost != null ? this.coverPost.hashCode() : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineFeed vineFeed = (VineFeed) o;
        return this.feedId == vineFeed.feedId && !this.title.equals(vineFeed.title) && this.description.equals(vineFeed.description) && this.backgroundColor.equals(vineFeed.backgroundColor) && this.secondaryColor.equals(vineFeed.secondaryColor) && this.user.equals(vineFeed.user) && this.userId == vineFeed.userId && this.type.equals(vineFeed.type) && this.feedMetadata.equals(vineFeed.feedMetadata) && this.coverPost.equals(vineFeed.coverPost);
    }
}
