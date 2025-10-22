package co.vine.android.api;

import co.vine.android.api.VineHomeFeedSetting;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineHomeFeedSetting$$JsonObjectMapper extends JsonMapper<VineHomeFeedSetting> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineHomeFeedSetting parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineHomeFeedSetting _parse(JsonParser jsonParser) throws IOException {
        VineHomeFeedSetting instance = new VineHomeFeedSetting();
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

    public static void parseField(VineHomeFeedSetting instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("choices".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineHomeFeedSetting.Choice> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineHomeFeedSetting.Choice value1 = VineHomeFeedSetting$Choice$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.choices = collection1;
                return;
            }
            instance.choices = null;
            return;
        }
        if ("description".equals(fieldName)) {
            instance.description = jsonParser.getValueAsString(null);
            return;
        }
        if ("boolean".equals(fieldName)) {
            instance.isBooleanSetting = jsonParser.getValueAsBoolean();
            return;
        }
        if ("name".equals(fieldName)) {
            instance.name = jsonParser.getValueAsString(null);
            return;
        }
        if ("section".equals(fieldName)) {
            instance.section = jsonParser.getValueAsString(null);
        } else if ("title".equals(fieldName)) {
            instance.title = jsonParser.getValueAsString(null);
        } else if ("value".equals(fieldName)) {
            instance.value = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineHomeFeedSetting object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineHomeFeedSetting object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<VineHomeFeedSetting.Choice> lslocalchoices = object.choices;
        if (lslocalchoices != null) {
            jsonGenerator.writeFieldName("choices");
            jsonGenerator.writeStartArray();
            for (VineHomeFeedSetting.Choice element1 : lslocalchoices) {
                if (element1 != null) {
                    VineHomeFeedSetting$Choice$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (object.description != null) {
            jsonGenerator.writeStringField("description", object.description);
        }
        jsonGenerator.writeBooleanField("boolean", object.isBooleanSetting);
        if (object.name != null) {
            jsonGenerator.writeStringField("name", object.name);
        }
        if (object.section != null) {
            jsonGenerator.writeStringField("section", object.section);
        }
        if (object.title != null) {
            jsonGenerator.writeStringField("title", object.title);
        }
        if (object.value != null) {
            jsonGenerator.writeStringField("value", object.value);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
