package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ClientEvent$$JsonObjectMapper extends JsonMapper<ClientEvent> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ClientEvent parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ClientEvent _parse(JsonParser jsonParser) throws IOException {
        ClientEvent instance = new ClientEvent();
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

    public static void parseField(ClientEvent instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("app_state".equals(fieldName)) {
            instance.appState = ApplicationState$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("client_id".equals(fieldName)) {
            instance.clientId = jsonParser.getValueAsString(null);
            return;
        }
        if ("device_data".equals(fieldName)) {
            instance.deviceData = DeviceData$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("event_details".equals(fieldName)) {
            instance.eventDetails = EventDetails$$JsonObjectMapper._parse(jsonParser);
        } else if ("event_type".equals(fieldName)) {
            instance.eventType = jsonParser.getValueAsString(null);
        } else if ("navigation".equals(fieldName)) {
            instance.navigation = AppNavigation$$JsonObjectMapper._parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ClientEvent object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ClientEvent object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.appState != null) {
            jsonGenerator.writeFieldName("app_state");
            ApplicationState$$JsonObjectMapper._serialize(object.appState, jsonGenerator, true);
        }
        if (object.clientId != null) {
            jsonGenerator.writeStringField("client_id", object.clientId);
        }
        if (object.deviceData != null) {
            jsonGenerator.writeFieldName("device_data");
            DeviceData$$JsonObjectMapper._serialize(object.deviceData, jsonGenerator, true);
        }
        if (object.eventDetails != null) {
            jsonGenerator.writeFieldName("event_details");
            EventDetails$$JsonObjectMapper._serialize(object.eventDetails, jsonGenerator, true);
        }
        if (object.eventType != null) {
            jsonGenerator.writeStringField("event_type", object.eventType);
        }
        if (object.navigation != null) {
            jsonGenerator.writeFieldName("navigation");
            AppNavigation$$JsonObjectMapper._serialize(object.navigation, jsonGenerator, true);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
