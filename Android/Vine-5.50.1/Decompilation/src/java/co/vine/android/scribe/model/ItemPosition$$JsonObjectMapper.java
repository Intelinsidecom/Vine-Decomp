package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ItemPosition$$JsonObjectMapper extends JsonMapper<ItemPosition> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ItemPosition parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ItemPosition _parse(JsonParser jsonParser) throws IOException {
        ItemPosition instance = new ItemPosition();
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

    public static void parseField(ItemPosition instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("offset".equals(fieldName)) {
            instance.offset = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Integer.valueOf(jsonParser.getValueAsInt());
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ItemPosition object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ItemPosition object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.offset != null) {
            jsonGenerator.writeNumberField("offset", object.offset.intValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
