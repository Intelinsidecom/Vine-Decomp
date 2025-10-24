package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class VineAudioMetadataResponse {

    @JsonField(name = {"origin"})
    public String origin;

    @JsonField(name = {"track"})
    public Track track;

    @JsonField(name = {"trackId"})
    public long trackId;

    @JsonObject
    public static class Track {

        @JsonField(name = {"albumArtUrl"})
        public String albumArtUrl;

        @JsonField(name = {"artistName"})
        public String artistName;

        @JsonField(name = {"hasAudioTrackTimeline"})
        public boolean hasAudioTrackTimeline;

        @JsonField(name = {"trackId"})
        public long trackId;

        @JsonField(name = {"trackName"})
        public String trackName;
    }
}
