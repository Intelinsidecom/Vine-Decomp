package co.vine.android.api.response;

import co.vine.android.api.response.VineEditions;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineEditions$Edition$$JsonObjectMapper extends JsonMapper<VineEditions.Edition> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineEditions.Edition parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineEditions.Edition _parse(JsonParser jsonParser) throws IOException {
        VineEditions.Edition instance = new VineEditions.Edition();
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

    public static void parseField(VineEditions.Edition instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("editionId".equals(fieldName)) {
            instance.editionId = jsonParser.getValueAsString(null);
        } else if ("name".equals(fieldName)) {
            instance.name = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineEditions.Edition object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineEditions.Edition object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.editionId != null) {
            jsonGenerator.writeStringField("editionId", object.editionId);
        }
        if (object.name != null) {
            jsonGenerator.writeStringField("name", object.name);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
