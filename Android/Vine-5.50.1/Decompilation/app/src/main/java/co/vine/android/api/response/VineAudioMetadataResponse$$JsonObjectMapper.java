package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineAudioMetadataResponse$$JsonObjectMapper extends JsonMapper<VineAudioMetadataResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineAudioMetadataResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineAudioMetadataResponse _parse(JsonParser jsonParser) throws IOException {
        VineAudioMetadataResponse instance = new VineAudioMetadataResponse();
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

    public static void parseField(VineAudioMetadataResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("origin".equals(fieldName)) {
            instance.origin = jsonParser.getValueAsString(null);
        } else if ("track".equals(fieldName)) {
            instance.track = VineAudioMetadataResponse$Track$$JsonObjectMapper._parse(jsonParser);
        } else if ("trackId".equals(fieldName)) {
            instance.trackId = jsonParser.getValueAsLong();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineAudioMetadataResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineAudioMetadataResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.origin != null) {
            jsonGenerator.writeStringField("origin", object.origin);
        }
        if (object.track != null) {
            jsonGenerator.writeFieldName("track");
            VineAudioMetadataResponse$Track$$JsonObjectMapper._serialize(object.track, jsonGenerator, true);
        }
        jsonGenerator.writeNumberField("trackId", object.trackId);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
