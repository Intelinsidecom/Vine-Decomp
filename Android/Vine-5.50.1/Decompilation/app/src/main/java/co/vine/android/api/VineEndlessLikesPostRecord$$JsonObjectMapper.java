package co.vine.android.api;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineEndlessLikesPostRecord$$JsonObjectMapper extends JsonMapper<VineEndlessLikesPostRecord> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineEndlessLikesPostRecord parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineEndlessLikesPostRecord _parse(JsonParser jsonParser) throws IOException {
        VineEndlessLikesPostRecord instance = new VineEndlessLikesPostRecord();
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

    public static void parseField(VineEndlessLikesPostRecord instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("time".equals(fieldName)) {
            instance.time = (float) jsonParser.getValueAsDouble();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineEndlessLikesPostRecord object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineEndlessLikesPostRecord object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("time", object.time);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
