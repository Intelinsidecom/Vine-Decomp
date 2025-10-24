package co.vine.android.api.response;

import co.vine.android.api.response.FoursquareResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class FoursquareResponse$Location$$JsonObjectMapper extends JsonMapper<FoursquareResponse.Location> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public FoursquareResponse.Location parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static FoursquareResponse.Location _parse(JsonParser jsonParser) throws IOException {
        FoursquareResponse.Location instance = new FoursquareResponse.Location();
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

    public static void parseField(FoursquareResponse.Location instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("address".equals(fieldName)) {
            instance.address = jsonParser.getValueAsString(null);
            return;
        }
        if ("cc".equals(fieldName)) {
            instance.cc = jsonParser.getValueAsString(null);
            return;
        }
        if ("city".equals(fieldName)) {
            instance.city = jsonParser.getValueAsString(null);
            return;
        }
        if ("country".equals(fieldName)) {
            instance.country = jsonParser.getValueAsString(null);
            return;
        }
        if ("distance".equals(fieldName)) {
            instance.distance = jsonParser.getValueAsDouble();
            return;
        }
        if ("lat".equals(fieldName)) {
            instance.lat = jsonParser.getValueAsDouble();
            return;
        }
        if ("lng".equals(fieldName)) {
            instance.lng = jsonParser.getValueAsDouble();
        } else if ("postalCode".equals(fieldName)) {
            instance.postalCode = jsonParser.getValueAsString(null);
        } else if ("state".equals(fieldName)) {
            instance.state = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(FoursquareResponse.Location object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(FoursquareResponse.Location object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.address != null) {
            jsonGenerator.writeStringField("address", object.address);
        }
        if (object.cc != null) {
            jsonGenerator.writeStringField("cc", object.cc);
        }
        if (object.city != null) {
            jsonGenerator.writeStringField("city", object.city);
        }
        if (object.country != null) {
            jsonGenerator.writeStringField("country", object.country);
        }
        jsonGenerator.writeNumberField("distance", object.distance);
        jsonGenerator.writeNumberField("lat", object.lat);
        jsonGenerator.writeNumberField("lng", object.lng);
        if (object.postalCode != null) {
            jsonGenerator.writeStringField("postalCode", object.postalCode);
        }
        if (object.state != null) {
            jsonGenerator.writeStringField("state", object.state);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
