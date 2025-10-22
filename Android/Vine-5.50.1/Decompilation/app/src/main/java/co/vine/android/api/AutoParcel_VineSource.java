package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class AutoParcel_VineSource extends VineSource {
    private final int contentType;
    private final String description;
    private final float duration;
    private final float offsetInPosts;
    private final float offsetInSource;
    private final long postId;
    private final String sourcePostId;
    private final String thumbnailUrl;
    private final String username;
    public static final Parcelable.Creator<AutoParcel_VineSource> CREATOR = new Parcelable.Creator<AutoParcel_VineSource>() { // from class: co.vine.android.api.AutoParcel_VineSource.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineSource createFromParcel(Parcel in) {
            return new AutoParcel_VineSource(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineSource[] newArray(int size) {
            return new AutoParcel_VineSource[size];
        }
    };
    private static final ClassLoader CL = AutoParcel_VineSource.class.getClassLoader();

    AutoParcel_VineSource(int contentType, long postId, String username, String description, String thumbnailUrl, float duration, String sourcePostId, float offsetInPosts, float offsetInSource) {
        this.contentType = contentType;
        this.postId = postId;
        if (username == null) {
            throw new NullPointerException("Null username");
        }
        this.username = username;
        if (description == null) {
            throw new NullPointerException("Null description");
        }
        this.description = description;
        if (thumbnailUrl == null) {
            throw new NullPointerException("Null thumbnailUrl");
        }
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        if (sourcePostId == null) {
            throw new NullPointerException("Null sourcePostId");
        }
        this.sourcePostId = sourcePostId;
        this.offsetInPosts = offsetInPosts;
        this.offsetInSource = offsetInSource;
    }

    @Override // co.vine.android.api.VineSource
    public int getContentType() {
        return this.contentType;
    }

    @Override // co.vine.android.api.VineSource
    public long getPostId() {
        return this.postId;
    }

    @Override // co.vine.android.api.VineSource
    public String getUsername() {
        return this.username;
    }

    @Override // co.vine.android.api.VineSource
    public String getDescription() {
        return this.description;
    }

    @Override // co.vine.android.api.VineSource
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    @Override // co.vine.android.api.VineSource
    public float getDuration() {
        return this.duration;
    }

    @Override // co.vine.android.api.VineSource
    public String getSourcePostId() {
        return this.sourcePostId;
    }

    @Override // co.vine.android.api.VineSource
    public float getOffsetInPosts() {
        return this.offsetInPosts;
    }

    @Override // co.vine.android.api.VineSource
    public float getOffsetInSource() {
        return this.offsetInSource;
    }

    public String toString() {
        return "VineSource{contentType=" + this.contentType + ", postId=" + this.postId + ", username=" + this.username + ", description=" + this.description + ", thumbnailUrl=" + this.thumbnailUrl + ", duration=" + this.duration + ", sourcePostId=" + this.sourcePostId + ", offsetInPosts=" + this.offsetInPosts + ", offsetInSource=" + this.offsetInSource + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VineSource)) {
            return false;
        }
        VineSource that = (VineSource) o;
        return this.contentType == that.getContentType() && this.postId == that.getPostId() && this.username.equals(that.getUsername()) && this.description.equals(that.getDescription()) && this.thumbnailUrl.equals(that.getThumbnailUrl()) && Float.floatToIntBits(this.duration) == Float.floatToIntBits(that.getDuration()) && this.sourcePostId.equals(that.getSourcePostId()) && Float.floatToIntBits(this.offsetInPosts) == Float.floatToIntBits(that.getOffsetInPosts()) && Float.floatToIntBits(this.offsetInSource) == Float.floatToIntBits(that.getOffsetInSource());
    }

    public int hashCode() {
        int h = 1 * 1000003;
        return (((((((((((((((int) (((h ^ this.contentType) * 1000003) ^ ((this.postId >>> 32) ^ this.postId))) * 1000003) ^ this.username.hashCode()) * 1000003) ^ this.description.hashCode()) * 1000003) ^ this.thumbnailUrl.hashCode()) * 1000003) ^ Float.floatToIntBits(this.duration)) * 1000003) ^ this.sourcePostId.hashCode()) * 1000003) ^ Float.floatToIntBits(this.offsetInPosts)) * 1000003) ^ Float.floatToIntBits(this.offsetInSource);
    }

    private AutoParcel_VineSource(Parcel in) {
        this(((Integer) in.readValue(CL)).intValue(), ((Long) in.readValue(CL)).longValue(), (String) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), ((Float) in.readValue(CL)).floatValue(), (String) in.readValue(CL), ((Float) in.readValue(CL)).floatValue(), ((Float) in.readValue(CL)).floatValue());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Integer.valueOf(this.contentType));
        dest.writeValue(Long.valueOf(this.postId));
        dest.writeValue(this.username);
        dest.writeValue(this.description);
        dest.writeValue(this.thumbnailUrl);
        dest.writeValue(Float.valueOf(this.duration));
        dest.writeValue(this.sourcePostId);
        dest.writeValue(Float.valueOf(this.offsetInPosts));
        dest.writeValue(Float.valueOf(this.offsetInSource));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
