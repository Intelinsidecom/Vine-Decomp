package co.vine.android.api;

import co.vine.android.api.response.VineShortPost;
import co.vine.android.api.response.VineShortPost$$JsonObjectMapper;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineBylineAction$$JsonObjectMapper extends JsonMapper<VineBylineAction> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineBylineAction parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineBylineAction _parse(JsonParser jsonParser) throws IOException {
        VineBylineAction instance = new VineBylineAction();
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

    public static void parseField(VineBylineAction instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("actionIconUrl".equals(fieldName)) {
            instance.actionIconUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("actionTitle".equals(fieldName)) {
            instance.actionTitle = jsonParser.getValueAsString(null);
            return;
        }
        if ("description".equals(fieldName)) {
            instance.description = jsonParser.getValueAsString(null);
            return;
        }
        if ("detailedDescription".equals(fieldName)) {
            instance.detailedDescription = jsonParser.getValueAsString(null);
            return;
        }
        if ("records".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineShortPost> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineShortPost value1 = VineShortPost$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.records = collection1;
                return;
            }
            instance.records = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineBylineAction object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineBylineAction object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.actionIconUrl != null) {
            jsonGenerator.writeStringField("actionIconUrl", object.actionIconUrl);
        }
        if (object.actionTitle != null) {
            jsonGenerator.writeStringField("actionTitle", object.actionTitle);
        }
        if (object.description != null) {
            jsonGenerator.writeStringField("description", object.description);
        }
        if (object.detailedDescription != null) {
            jsonGenerator.writeStringField("detailedDescription", object.detailedDescription);
        }
        List<VineShortPost> lslocalrecords = object.records;
        if (lslocalrecords != null) {
            jsonGenerator.writeFieldName("records");
            jsonGenerator.writeStartArray();
            for (VineShortPost element1 : lslocalrecords) {
                if (element1 != null) {
                    VineShortPost$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
