package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineLoopSubmissionResponse$$JsonObjectMapper extends JsonMapper<VineLoopSubmissionResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineLoopSubmissionResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineLoopSubmissionResponse _parse(JsonParser jsonParser) throws IOException {
        VineLoopSubmissionResponse instance = new VineLoopSubmissionResponse();
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

    public static void parseField(VineLoopSubmissionResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("nextAfter".equals(fieldName)) {
            instance.mSubmissionInterval = jsonParser.getValueAsInt();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineLoopSubmissionResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineLoopSubmissionResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("nextAfter", object.mSubmissionInterval);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
