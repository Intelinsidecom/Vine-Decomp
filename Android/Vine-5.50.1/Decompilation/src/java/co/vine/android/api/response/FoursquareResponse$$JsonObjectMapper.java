package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class FoursquareResponse$$JsonObjectMapper extends JsonMapper<FoursquareResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public FoursquareResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static FoursquareResponse _parse(JsonParser jsonParser) throws IOException {
        FoursquareResponse instance = new FoursquareResponse();
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

    public static void parseField(FoursquareResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("response".equals(fieldName)) {
            instance.response = FoursquareResponse$Response$$JsonObjectMapper._parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(FoursquareResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(FoursquareResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.response != null) {
            jsonGenerator.writeFieldName("response");
            FoursquareResponse$Response$$JsonObjectMapper._serialize(object.response, jsonGenerator, true);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
