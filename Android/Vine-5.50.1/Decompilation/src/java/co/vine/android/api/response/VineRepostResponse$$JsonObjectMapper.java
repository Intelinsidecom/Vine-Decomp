package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineRepostResponse$$JsonObjectMapper extends JsonMapper<VineRepostResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineRepostResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineRepostResponse _parse(JsonParser jsonParser) throws IOException {
        VineRepostResponse instance = new VineRepostResponse();
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

    public static void parseField(VineRepostResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("data".equals(fieldName)) {
            instance.data = VineRepostResponse$Response$$JsonObjectMapper._parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineRepostResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineRepostResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.data != null) {
            jsonGenerator.writeFieldName("data");
            VineRepostResponse$Response$$JsonObjectMapper._serialize(object.data, jsonGenerator, true);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
