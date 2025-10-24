package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class AutoParcel_VineAudioMetadata extends VineAudioMetadata {
    private final String albumArtUrl;
    private final String artistName;
    private final boolean hasAudioTrackTimeline;
    private final String origin;
    private final long trackId;
    private final String trackName;
    public static final Parcelable.Creator<AutoParcel_VineAudioMetadata> CREATOR = new Parcelable.Creator<AutoParcel_VineAudioMetadata>() { // from class: co.vine.android.api.AutoParcel_VineAudioMetadata.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineAudioMetadata createFromParcel(Parcel in) {
            return new AutoParcel_VineAudioMetadata(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineAudioMetadata[] newArray(int size) {
            return new AutoParcel_VineAudioMetadata[size];
        }
    };
    private static final ClassLoader CL = AutoParcel_VineAudioMetadata.class.getClassLoader();

    AutoParcel_VineAudioMetadata(long trackId, String artistName, String trackName, String albumArtUrl, String origin, boolean hasAudioTrackTimeline) {
        this.trackId = trackId;
        if (artistName == null) {
            throw new NullPointerException("Null artistName");
        }
        this.artistName = artistName;
        if (trackName == null) {
            throw new NullPointerException("Null trackName");
        }
        this.trackName = trackName;
        if (albumArtUrl == null) {
            throw new NullPointerException("Null albumArtUrl");
        }
        this.albumArtUrl = albumArtUrl;
        if (origin == null) {
            throw new NullPointerException("Null origin");
        }
        this.origin = origin;
        this.hasAudioTrackTimeline = hasAudioTrackTimeline;
    }

    @Override // co.vine.android.api.VineAudioMetadata
    public long getTrackId() {
        return this.trackId;
    }

    @Override // co.vine.android.api.VineAudioMetadata
    public String getArtistName() {
        return this.artistName;
    }

    @Override // co.vine.android.api.VineAudioMetadata
    public String getTrackName() {
        return this.trackName;
    }

    @Override // co.vine.android.api.VineAudioMetadata
    public String getAlbumArtUrl() {
        return this.albumArtUrl;
    }

    @Override // co.vine.android.api.VineAudioMetadata
    public String getOrigin() {
        return this.origin;
    }

    @Override // co.vine.android.api.VineAudioMetadata
    public boolean getHasAudioTrackTimeline() {
        return this.hasAudioTrackTimeline;
    }

    public String toString() {
        return "VineAudioMetadata{trackId=" + this.trackId + ", artistName=" + this.artistName + ", trackName=" + this.trackName + ", albumArtUrl=" + this.albumArtUrl + ", origin=" + this.origin + ", hasAudioTrackTimeline=" + this.hasAudioTrackTimeline + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VineAudioMetadata)) {
            return false;
        }
        VineAudioMetadata that = (VineAudioMetadata) o;
        return this.trackId == that.getTrackId() && this.artistName.equals(that.getArtistName()) && this.trackName.equals(that.getTrackName()) && this.albumArtUrl.equals(that.getAlbumArtUrl()) && this.origin.equals(that.getOrigin()) && this.hasAudioTrackTimeline == that.getHasAudioTrackTimeline();
    }

    public int hashCode() {
        int h = 1 * 1000003;
        return (((((((((((int) (h ^ ((this.trackId >>> 32) ^ this.trackId))) * 1000003) ^ this.artistName.hashCode()) * 1000003) ^ this.trackName.hashCode()) * 1000003) ^ this.albumArtUrl.hashCode()) * 1000003) ^ this.origin.hashCode()) * 1000003) ^ (this.hasAudioTrackTimeline ? 1231 : 1237);
    }

    private AutoParcel_VineAudioMetadata(Parcel in) {
        this(((Long) in.readValue(CL)).longValue(), (String) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), ((Boolean) in.readValue(CL)).booleanValue());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Long.valueOf(this.trackId));
        dest.writeValue(this.artistName);
        dest.writeValue(this.trackName);
        dest.writeValue(this.albumArtUrl);
        dest.writeValue(this.origin);
        dest.writeValue(Boolean.valueOf(this.hasAudioTrackTimeline));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
