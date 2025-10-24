package co.vine.android.api.response;

import co.vine.android.api.response.FoursquareResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class FoursquareResponse$Icon$$JsonObjectMapper extends JsonMapper<FoursquareResponse.Icon> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public FoursquareResponse.Icon parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static FoursquareResponse.Icon _parse(JsonParser jsonParser) throws IOException {
        FoursquareResponse.Icon instance = new FoursquareResponse.Icon();
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

    public static void parseField(FoursquareResponse.Icon instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("prefix".equals(fieldName)) {
            instance.prefix = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(FoursquareResponse.Icon object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(FoursquareResponse.Icon object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.prefix != null) {
            jsonGenerator.writeStringField("prefix", object.prefix);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
