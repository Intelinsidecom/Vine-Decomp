package co.vine.android.api.response;

import co.vine.android.api.response.FoursquareResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class FoursquareResponse$Category$$JsonObjectMapper extends JsonMapper<FoursquareResponse.Category> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public FoursquareResponse.Category parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static FoursquareResponse.Category _parse(JsonParser jsonParser) throws IOException {
        FoursquareResponse.Category instance = new FoursquareResponse.Category();
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

    public static void parseField(FoursquareResponse.Category instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("icon".equals(fieldName)) {
            instance.icon = FoursquareResponse$Icon$$JsonObjectMapper._parse(jsonParser);
        } else if ("shortName".equals(fieldName)) {
            instance.shortName = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(FoursquareResponse.Category object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(FoursquareResponse.Category object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.icon != null) {
            jsonGenerator.writeFieldName("icon");
            FoursquareResponse$Icon$$JsonObjectMapper._serialize(object.icon, jsonGenerator, true);
        }
        if (object.shortName != null) {
            jsonGenerator.writeStringField("shortName", object.shortName);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
