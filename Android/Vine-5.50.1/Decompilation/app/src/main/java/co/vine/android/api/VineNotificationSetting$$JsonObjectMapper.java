package co.vine.android.api;

import co.vine.android.api.VineNotificationSetting;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineNotificationSetting$$JsonObjectMapper extends JsonMapper<VineNotificationSetting> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineNotificationSetting parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineNotificationSetting _parse(JsonParser jsonParser) throws IOException {
        VineNotificationSetting instance = new VineNotificationSetting();
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

    public static void parseField(VineNotificationSetting instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("choices".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineNotificationSetting.Choice> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineNotificationSetting.Choice value1 = VineNotificationSetting$Choice$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.choices = collection1;
                return;
            }
            instance.choices = null;
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
    public void serialize(VineNotificationSetting object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineNotificationSetting object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<VineNotificationSetting.Choice> lslocalchoices = object.choices;
        if (lslocalchoices != null) {
            jsonGenerator.writeFieldName("choices");
            jsonGenerator.writeStartArray();
            for (VineNotificationSetting.Choice element1 : lslocalchoices) {
                if (element1 != null) {
                    VineNotificationSetting$Choice$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
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
