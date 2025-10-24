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
public final class FoursquareResponse$Venue$$JsonObjectMapper extends JsonMapper<FoursquareResponse.Venue> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public FoursquareResponse.Venue parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static FoursquareResponse.Venue _parse(JsonParser jsonParser) throws IOException {
        FoursquareResponse.Venue instance = new FoursquareResponse.Venue();
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

    public static void parseField(FoursquareResponse.Venue instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("categories".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<FoursquareResponse.Category> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    FoursquareResponse.Category value1 = FoursquareResponse$Category$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.categories = collection1;
                return;
            }
            instance.categories = null;
            return;
        }
        if ("id".equals(fieldName)) {
            instance.id = jsonParser.getValueAsString(null);
        } else if ("location".equals(fieldName)) {
            instance.location = FoursquareResponse$Location$$JsonObjectMapper._parse(jsonParser);
        } else if ("name".equals(fieldName)) {
            instance.name = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(FoursquareResponse.Venue object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(FoursquareResponse.Venue object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<FoursquareResponse.Category> lslocalcategories = object.categories;
        if (lslocalcategories != null) {
            jsonGenerator.writeFieldName("categories");
            jsonGenerator.writeStartArray();
            for (FoursquareResponse.Category element1 : lslocalcategories) {
                if (element1 != null) {
                    FoursquareResponse$Category$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (object.id != null) {
            jsonGenerator.writeStringField("id", object.id);
        }
        if (object.location != null) {
            jsonGenerator.writeFieldName("location");
            FoursquareResponse$Location$$JsonObjectMapper._serialize(object.location, jsonGenerator, true);
        }
        if (object.name != null) {
            jsonGenerator.writeStringField("name", object.name);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
