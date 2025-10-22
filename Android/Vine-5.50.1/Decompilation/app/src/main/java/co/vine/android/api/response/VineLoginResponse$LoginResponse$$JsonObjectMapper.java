package co.vine.android.api.response;

import co.vine.android.api.response.VineLoginResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineLoginResponse$LoginResponse$$JsonObjectMapper extends JsonMapper<VineLoginResponse.LoginResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineLoginResponse.LoginResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineLoginResponse.LoginResponse _parse(JsonParser jsonParser) throws IOException {
        VineLoginResponse.LoginResponse instance = new VineLoginResponse.LoginResponse();
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

    public static void parseField(VineLoginResponse.LoginResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("avatarUrl".equals(fieldName)) {
            instance.avatarUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("edition".equals(fieldName)) {
            instance.edition = jsonParser.getValueAsString(null);
            return;
        }
        if ("key".equals(fieldName)) {
            instance.key = jsonParser.getValueAsString(null);
        } else if ("userId".equals(fieldName)) {
            instance.userId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
        } else if ("username".equals(fieldName)) {
            instance.username = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineLoginResponse.LoginResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineLoginResponse.LoginResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.avatarUrl != null) {
            jsonGenerator.writeStringField("avatarUrl", object.avatarUrl);
        }
        if (object.edition != null) {
            jsonGenerator.writeStringField("edition", object.edition);
        }
        if (object.key != null) {
            jsonGenerator.writeStringField("key", object.key);
        }
        if (object.userId != null) {
            jsonGenerator.writeNumberField("userId", object.userId.longValue());
        }
        if (object.username != null) {
            jsonGenerator.writeStringField("username", object.username);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
