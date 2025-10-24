package co.vine.android.api;

import co.vine.android.api.VineNotificationSetting;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineNotificationSetting$Choice$$JsonObjectMapper extends JsonMapper<VineNotificationSetting.Choice> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineNotificationSetting.Choice parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineNotificationSetting.Choice _parse(JsonParser jsonParser) throws IOException {
        VineNotificationSetting.Choice instance = new VineNotificationSetting.Choice();
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

    public static void parseField(VineNotificationSetting.Choice instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("title".equals(fieldName)) {
            instance.title = jsonParser.getValueAsString(null);
        } else if ("value".equals(fieldName)) {
            instance.value = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineNotificationSetting.Choice object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineNotificationSetting.Choice object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
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
