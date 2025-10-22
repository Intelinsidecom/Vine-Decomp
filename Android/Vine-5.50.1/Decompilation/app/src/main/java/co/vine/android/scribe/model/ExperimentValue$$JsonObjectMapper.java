package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ExperimentValue$$JsonObjectMapper extends JsonMapper<ExperimentValue> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ExperimentValue parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ExperimentValue _parse(JsonParser jsonParser) throws IOException {
        ExperimentValue instance = new ExperimentValue();
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

    public static void parseField(ExperimentValue instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("key".equals(fieldName)) {
            instance.key = jsonParser.getValueAsString(null);
        } else if ("value".equals(fieldName)) {
            instance.value = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ExperimentValue object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ExperimentValue object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.key != null) {
            jsonGenerator.writeStringField("key", object.key);
        }
        if (object.value != null) {
            jsonGenerator.writeStringField("value", object.value);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
