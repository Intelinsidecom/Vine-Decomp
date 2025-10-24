package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class GPSData$$JsonObjectMapper extends JsonMapper<GPSData> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public GPSData parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static GPSData _parse(JsonParser jsonParser) throws IOException {
        GPSData instance = new GPSData();
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

    public static void parseField(GPSData instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("altitude".equals(fieldName)) {
            instance.altitude = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
            return;
        }
        if ("horizontal_accuracy".equals(fieldName)) {
            instance.horizontalAccuracy = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
            return;
        }
        if ("latitude".equals(fieldName)) {
            instance.latitude = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
        } else if ("longitude".equals(fieldName)) {
            instance.longitude = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
        } else if ("vertical_accuracy".equals(fieldName)) {
            instance.verticalAccuracy = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(GPSData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(GPSData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.altitude != null) {
            jsonGenerator.writeNumberField("altitude", object.altitude.doubleValue());
        }
        if (object.horizontalAccuracy != null) {
            jsonGenerator.writeNumberField("horizontal_accuracy", object.horizontalAccuracy.doubleValue());
        }
        if (object.latitude != null) {
            jsonGenerator.writeNumberField("latitude", object.latitude.doubleValue());
        }
        if (object.longitude != null) {
            jsonGenerator.writeNumberField("longitude", object.longitude.doubleValue());
        }
        if (object.verticalAccuracy != null) {
            jsonGenerator.writeNumberField("vertical_accuracy", object.verticalAccuracy.doubleValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
