package co.vine.android.api;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineLongform$$JsonObjectMapper extends JsonMapper<VineLongform> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineLongform parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineLongform _parse(JsonParser jsonParser) throws IOException {
        VineLongform instance = new VineLongform();
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

    public static void parseField(VineLongform instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("aspectRatio".equals(fieldName)) {
            instance.aspectRatio = (float) jsonParser.getValueAsDouble();
            return;
        }
        if ("duration".equals(fieldName)) {
            instance.duration = (float) jsonParser.getValueAsDouble();
            return;
        }
        if ("longformId".equals(fieldName)) {
            instance.longformId = jsonParser.getValueAsString(null);
        } else if ("thumbnailUrl".equals(fieldName)) {
            instance.thumbnailUrl = jsonParser.getValueAsString(null);
        } else if ("videoUrl".equals(fieldName)) {
            instance.videoUrl = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineLongform object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineLongform object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("aspectRatio", object.aspectRatio);
        jsonGenerator.writeNumberField("duration", object.duration);
        if (object.longformId != null) {
            jsonGenerator.writeStringField("longformId", object.longformId);
        }
        if (object.thumbnailUrl != null) {
            jsonGenerator.writeStringField("thumbnailUrl", object.thumbnailUrl);
        }
        if (object.videoUrl != null) {
            jsonGenerator.writeStringField("videoUrl", object.videoUrl);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
