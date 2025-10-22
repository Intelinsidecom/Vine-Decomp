package co.vine.android.api.response;

import co.vine.android.api.response.FoursquareResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class FoursquareResponse$Response$$JsonObjectMapper extends JsonMapper<FoursquareResponse.Response> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public FoursquareResponse.Response parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static FoursquareResponse.Response _parse(JsonParser jsonParser) throws IOException {
        FoursquareResponse.Response instance = new FoursquareResponse.Response();
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

    public static void parseField(FoursquareResponse.Response instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("venues".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<FoursquareResponse.Venue> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    FoursquareResponse.Venue value1 = FoursquareResponse$Venue$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.venues = collection1;
                return;
            }
            instance.venues = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(FoursquareResponse.Response object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(FoursquareResponse.Response object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<FoursquareResponse.Venue> lslocalvenues = object.venues;
        if (lslocalvenues != null) {
            jsonGenerator.writeFieldName("venues");
            jsonGenerator.writeStartArray();
            for (FoursquareResponse.Venue element1 : lslocalvenues) {
                if (element1 != null) {
                    FoursquareResponse$Venue$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
