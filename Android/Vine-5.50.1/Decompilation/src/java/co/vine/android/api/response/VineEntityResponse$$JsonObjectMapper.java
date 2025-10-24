package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineEntityResponse$$JsonObjectMapper extends JsonMapper<VineEntityResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineEntityResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineEntityResponse _parse(JsonParser jsonParser) throws IOException {
        VineEntityResponse instance = new VineEntityResponse();
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

    public static void parseField(VineEntityResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("id".equals(fieldName)) {
            instance.id = jsonParser.getValueAsLong();
            return;
        }
        if ("link".equals(fieldName)) {
            instance.link = jsonParser.getValueAsString(null);
            return;
        }
        if ("range".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<Integer> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    Integer value1 = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Integer.valueOf(jsonParser.getValueAsInt());
                    collection1.add(value1);
                }
                instance.range = collection1;
                return;
            }
            instance.range = null;
            return;
        }
        if ("title".equals(fieldName)) {
            instance.title = jsonParser.getValueAsString(null);
        } else if ("type".equals(fieldName)) {
            instance.type = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineEntityResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineEntityResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("id", object.id);
        if (object.link != null) {
            jsonGenerator.writeStringField("link", object.link);
        }
        List<Integer> lslocalrange = object.range;
        if (lslocalrange != null) {
            jsonGenerator.writeFieldName("range");
            jsonGenerator.writeStartArray();
            for (Integer element1 : lslocalrange) {
                if (element1 != null) {
                    jsonGenerator.writeNumber(element1.intValue());
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (object.title != null) {
            jsonGenerator.writeStringField("title", object.title);
        }
        if (object.type != null) {
            jsonGenerator.writeStringField("type", object.type);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
