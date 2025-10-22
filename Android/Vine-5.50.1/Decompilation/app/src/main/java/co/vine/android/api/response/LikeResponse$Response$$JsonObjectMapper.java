package co.vine.android.api.response;

import co.vine.android.api.response.LikeResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class LikeResponse$Response$$JsonObjectMapper extends JsonMapper<LikeResponse.Response> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public LikeResponse.Response parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static LikeResponse.Response _parse(JsonParser jsonParser) throws IOException {
        LikeResponse.Response instance = new LikeResponse.Response();
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

    public static void parseField(LikeResponse.Response instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("likeId".equals(fieldName)) {
            instance.likeId = jsonParser.getValueAsLong();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(LikeResponse.Response object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(LikeResponse.Response object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("likeId", object.likeId);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
