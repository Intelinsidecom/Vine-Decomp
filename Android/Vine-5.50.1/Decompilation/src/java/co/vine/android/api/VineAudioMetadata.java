package co.vine.android.api;

import android.os.Parcelable;

/* loaded from: classes.dex */
public abstract class VineAudioMetadata implements Parcelable {
    public abstract String getAlbumArtUrl();

    public abstract String getArtistName();

    public abstract boolean getHasAudioTrackTimeline();

    public abstract String getOrigin();

    public abstract long getTrackId();

    public abstract String getTrackName();

    public static VineAudioMetadata create(long trackId, String artistName, String trackName, String albumArtUrl, String origin, boolean hasAudioTrackTimeline) {
        if (albumArtUrl == null) {
            albumArtUrl = "";
        }
        if (origin == null) {
            origin = "";
        }
        return new AutoParcel_VineAudioMetadata(trackId, artistName, trackName, albumArtUrl, origin, hasAudioTrackTimeline);
    }
}
