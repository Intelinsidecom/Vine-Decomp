package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class TagDetails$$JsonObjectMapper extends JsonMapper<TagDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public TagDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static TagDetails _parse(JsonParser jsonParser) throws IOException {
        TagDetails instance = new TagDetails();
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

    public static void parseField(TagDetails instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("tag_id".equals(fieldName)) {
            instance.tagId = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(TagDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(TagDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.tagId != null) {
            jsonGenerator.writeStringField("tag_id", object.tagId);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
