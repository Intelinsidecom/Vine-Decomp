package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class ClientEvents$$JsonObjectMapper extends JsonMapper<ClientEvents> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ClientEvents parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ClientEvents _parse(JsonParser jsonParser) throws IOException {
        ClientEvents instance = new ClientEvents();
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

    public static void parseField(ClientEvents instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("events".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<ClientEvent> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    ClientEvent value1 = ClientEvent$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.events = collection1;
                return;
            }
            instance.events = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ClientEvents object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ClientEvents object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<ClientEvent> lslocalevents = object.events;
        if (lslocalevents != null) {
            jsonGenerator.writeFieldName("events");
            jsonGenerator.writeStartArray();
            for (ClientEvent element1 : lslocalevents) {
                if (element1 != null) {
                    ClientEvent$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
