package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineActivityCounts$$JsonObjectMapper extends JsonMapper<VineActivityCounts> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineActivityCounts parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineActivityCounts _parse(JsonParser jsonParser) throws IOException {
        VineActivityCounts instance = new VineActivityCounts();
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

    public static void parseField(VineActivityCounts instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("messages".equals(fieldName)) {
            instance.messages = jsonParser.getValueAsInt();
        } else if ("notifications".equals(fieldName)) {
            instance.notifications = jsonParser.getValueAsInt();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineActivityCounts object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineActivityCounts object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("messages", object.messages);
        jsonGenerator.writeNumberField("notifications", object.notifications);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
