package co.vine.android.api.response;

import co.vine.android.api.response.VineRepostResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineRepostResponse$Response$$JsonObjectMapper extends JsonMapper<VineRepostResponse.Response> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineRepostResponse.Response parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineRepostResponse.Response _parse(JsonParser jsonParser) throws IOException {
        VineRepostResponse.Response instance = new VineRepostResponse.Response();
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

    public static void parseField(VineRepostResponse.Response instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("postId".equals(fieldName)) {
            instance.postId = jsonParser.getValueAsLong();
        } else if ("repostId".equals(fieldName)) {
            instance.repostId = jsonParser.getValueAsLong();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineRepostResponse.Response object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineRepostResponse.Response object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("postId", object.postId);
        jsonGenerator.writeNumberField("repostId", object.repostId);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
