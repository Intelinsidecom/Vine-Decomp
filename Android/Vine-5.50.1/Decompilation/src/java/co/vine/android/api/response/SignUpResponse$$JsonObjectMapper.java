package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class SignUpResponse$$JsonObjectMapper extends JsonMapper<SignUpResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public SignUpResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static SignUpResponse _parse(JsonParser jsonParser) throws IOException {
        SignUpResponse instance = new SignUpResponse();
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

    public static void parseField(SignUpResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("data".equals(fieldName)) {
            instance.data = SignUpResponse$Response$$JsonObjectMapper._parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(SignUpResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(SignUpResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.data != null) {
            jsonGenerator.writeFieldName("data");
            SignUpResponse$Response$$JsonObjectMapper._serialize(object.data, jsonGenerator, true);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
