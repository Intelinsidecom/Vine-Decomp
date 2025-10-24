package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineTagResponse$$JsonObjectMapper extends JsonMapper<VineTagResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineTagResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineTagResponse _parse(JsonParser jsonParser) throws IOException {
        VineTagResponse instance = new VineTagResponse();
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

    public static void parseField(VineTagResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("postCount".equals(fieldName)) {
            instance.postCount = jsonParser.getValueAsLong();
        } else if ("tagId".equals(fieldName)) {
            instance.tagId = jsonParser.getValueAsLong();
        } else if ("tag".equals(fieldName)) {
            instance.tagName = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineTagResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineTagResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("postCount", object.postCount);
        jsonGenerator.writeNumberField("tagId", object.tagId);
        if (object.tagName != null) {
            jsonGenerator.writeStringField("tag", object.tagName);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
