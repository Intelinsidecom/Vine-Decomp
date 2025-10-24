package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class GeneralError$$JsonObjectMapper extends JsonMapper<GeneralError> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public GeneralError parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static GeneralError _parse(JsonParser jsonParser) throws IOException {
        GeneralError instance = new GeneralError();
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

    public static void parseField(GeneralError instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("code".equals(fieldName)) {
            instance.code = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
        } else if ("data".equals(fieldName)) {
            instance.data = jsonParser.getValueAsString(null);
        } else if ("error".equals(fieldName)) {
            instance.error = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(GeneralError object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(GeneralError object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.code != null) {
            jsonGenerator.writeNumberField("code", object.code.intValue());
        }
        if (object.data != null) {
            jsonGenerator.writeStringField("data", object.data);
        }
        if (object.error != null) {
            jsonGenerator.writeStringField("error", object.error);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
