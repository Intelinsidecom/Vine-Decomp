package co.vine.android.api.response;

import co.vine.android.api.response.VineSourceResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineSourceResponse$Record$$JsonObjectMapper extends JsonMapper<VineSourceResponse.Record> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineSourceResponse.Record parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineSourceResponse.Record _parse(JsonParser jsonParser) throws IOException {
        VineSourceResponse.Record instance = new VineSourceResponse.Record();
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

    public static void parseField(VineSourceResponse.Record instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("contentType".equals(fieldName)) {
            instance.contentType = jsonParser.getValueAsInt();
            return;
        }
        if ("description".equals(fieldName)) {
            instance.description = jsonParser.getValueAsString(null);
            return;
        }
        if ("postId".equals(fieldName)) {
            instance.postId = jsonParser.getValueAsLong();
        } else if ("thumbnailUrl".equals(fieldName)) {
            instance.thumbnailUrl = jsonParser.getValueAsString(null);
        } else if ("username".equals(fieldName)) {
            instance.username = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineSourceResponse.Record object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineSourceResponse.Record object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("contentType", object.contentType);
        if (object.description != null) {
            jsonGenerator.writeStringField("description", object.description);
        }
        jsonGenerator.writeNumberField("postId", object.postId);
        if (object.thumbnailUrl != null) {
            jsonGenerator.writeStringField("thumbnailUrl", object.thumbnailUrl);
        }
        if (object.username != null) {
            jsonGenerator.writeStringField("username", object.username);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
