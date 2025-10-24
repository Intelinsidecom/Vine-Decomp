package co.vine.android.api.response;

import co.vine.android.api.response.SignUpResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class SignUpResponse$Response$$JsonObjectMapper extends JsonMapper<SignUpResponse.Response> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public SignUpResponse.Response parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static SignUpResponse.Response _parse(JsonParser jsonParser) throws IOException {
        SignUpResponse.Response instance = new SignUpResponse.Response();
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

    public static void parseField(SignUpResponse.Response instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("edition".equals(fieldName)) {
            instance.edition = jsonParser.getValueAsString(null);
        } else if ("key".equals(fieldName)) {
            instance.key = jsonParser.getValueAsString(null);
        } else if ("userId".equals(fieldName)) {
            instance.userId = jsonParser.getValueAsLong();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(SignUpResponse.Response object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(SignUpResponse.Response object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.edition != null) {
            jsonGenerator.writeStringField("edition", object.edition);
        }
        if (object.key != null) {
            jsonGenerator.writeStringField("key", object.key);
        }
        jsonGenerator.writeNumberField("userId", object.userId);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
