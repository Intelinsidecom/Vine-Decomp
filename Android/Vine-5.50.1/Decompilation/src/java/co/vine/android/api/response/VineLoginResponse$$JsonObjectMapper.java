package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineLoginResponse$$JsonObjectMapper extends JsonMapper<VineLoginResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineLoginResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineLoginResponse _parse(JsonParser jsonParser) throws IOException {
        VineLoginResponse instance = new VineLoginResponse();
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

    public static void parseField(VineLoginResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("data".equals(fieldName)) {
            instance.data = VineLoginResponse$LoginResponse$$JsonObjectMapper._parse(jsonParser);
        } else if ("success".equals(fieldName)) {
            instance.success = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Boolean.valueOf(jsonParser.getValueAsBoolean());
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineLoginResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineLoginResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.data != null) {
            jsonGenerator.writeFieldName("data");
            VineLoginResponse$LoginResponse$$JsonObjectMapper._serialize(object.data, jsonGenerator, true);
        }
        if (object.success != null) {
            jsonGenerator.writeBooleanField("success", object.success.booleanValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
