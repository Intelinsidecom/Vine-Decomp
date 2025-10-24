package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class MosaicDetails$$JsonObjectMapper extends JsonMapper<MosaicDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public MosaicDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static MosaicDetails _parse(JsonParser jsonParser) throws IOException {
        MosaicDetails instance = new MosaicDetails();
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

    public static void parseField(MosaicDetails instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("link".equals(fieldName)) {
            instance.link = jsonParser.getValueAsString(null);
        } else if ("mosaic_type".equals(fieldName)) {
            instance.mosaicType = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(MosaicDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(MosaicDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.link != null) {
            jsonGenerator.writeStringField("link", object.link);
        }
        if (object.mosaicType != null) {
            jsonGenerator.writeStringField("mosaic_type", object.mosaicType);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
