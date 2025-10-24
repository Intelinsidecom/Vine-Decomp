package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineShortPost$$JsonObjectMapper extends JsonMapper<VineShortPost> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineShortPost parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineShortPost _parse(JsonParser jsonParser) throws IOException {
        VineShortPost instance = new VineShortPost();
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

    public static void parseField(VineShortPost instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("postId".equals(fieldName)) {
            instance.postId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
        } else if ("thumbnailUrl".equals(fieldName)) {
            instance.thumbnailUrl = jsonParser.getValueAsString(null);
        } else if ("type".equals(fieldName)) {
            instance.type = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineShortPost object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineShortPost object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.postId != null) {
            jsonGenerator.writeNumberField("postId", object.postId.longValue());
        }
        if (object.thumbnailUrl != null) {
            jsonGenerator.writeStringField("thumbnailUrl", object.thumbnailUrl);
        }
        if (object.type != null) {
            jsonGenerator.writeStringField("type", object.type);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
