package co.vine.android.api.response;

import co.vine.android.api.response.VineAudioMetadataResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineAudioMetadataResponse$Track$$JsonObjectMapper extends JsonMapper<VineAudioMetadataResponse.Track> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineAudioMetadataResponse.Track parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineAudioMetadataResponse.Track _parse(JsonParser jsonParser) throws IOException {
        VineAudioMetadataResponse.Track instance = new VineAudioMetadataResponse.Track();
        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            parseField(instance, fieldName, jsonParser);
            jsonParser.skipChildren();
        }
        return instance;
    }

    public static void parseField(VineAudioMetadataResponse.Track instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("albumArtUrl".equals(fieldName)) {
            instance.albumArtUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("artistName".equals(fieldName)) {
            instance.artistName = jsonParser.getValueAsString(null);
            return;
        }
        if ("hasAudioTrackTimeline".equals(fieldName)) {
            instance.hasAudioTrackTimeline = jsonParser.getValueAsBoolean();
        } else if ("trackId".equals(fieldName)) {
            instance.trackId = jsonParser.getValueAsLong();
        } else if ("trackName".equals(fieldName)) {
            instance.trackName = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineAudioMetadataResponse.Track object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineAudioMetadataResponse.Track object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.albumArtUrl != null) {
            jsonGenerator.writeStringField("albumArtUrl", object.albumArtUrl);
        }
        if (object.artistName != null) {
            jsonGenerator.writeStringField("artistName", object.artistName);
        }
        jsonGenerator.writeBooleanField("hasAudioTrackTimeline", object.hasAudioTrackTimeline);
        jsonGenerator.writeNumberField("trackId", object.trackId);
        if (object.trackName != null) {
            jsonGenerator.writeStringField("trackName", object.trackName);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
