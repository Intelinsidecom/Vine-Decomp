package co.vine.android.api;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineEndlessLikesRecord$$JsonObjectMapper extends JsonMapper<VineEndlessLikesRecord> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineEndlessLikesRecord parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineEndlessLikesRecord _parse(JsonParser jsonParser) throws IOException {
        VineEndlessLikesRecord instance = new VineEndlessLikesRecord();
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

    public static void parseField(VineEndlessLikesRecord instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("count".equals(fieldName)) {
            instance.count = jsonParser.getValueAsInt();
            return;
        }
        if ("endTime".equals(fieldName)) {
            instance.endTime = (float) jsonParser.getValueAsDouble();
        } else if ("level".equals(fieldName)) {
            instance.level = (float) jsonParser.getValueAsDouble();
        } else if ("startTime".equals(fieldName)) {
            instance.startTime = (float) jsonParser.getValueAsDouble();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineEndlessLikesRecord object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineEndlessLikesRecord object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("count", object.count);
        jsonGenerator.writeNumberField("endTime", object.endTime);
        jsonGenerator.writeNumberField("level", object.level);
        jsonGenerator.writeNumberField("startTime", object.startTime);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
