package co.vine.android.api.response;

import co.vine.android.api.response.ComplaintMenuOptionResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ComplaintMenuOptionResponse$ComplaintMenuData$Choice$$JsonObjectMapper extends JsonMapper<ComplaintMenuOptionResponse.ComplaintMenuData.Choice> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ComplaintMenuOptionResponse.ComplaintMenuData.Choice parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ComplaintMenuOptionResponse.ComplaintMenuData.Choice _parse(JsonParser jsonParser) throws IOException {
        ComplaintMenuOptionResponse.ComplaintMenuData.Choice instance = new ComplaintMenuOptionResponse.ComplaintMenuData.Choice();
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

    public static void parseField(ComplaintMenuOptionResponse.ComplaintMenuData.Choice instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("confirmation".equals(fieldName)) {
            instance.confirmation = jsonParser.getValueAsString(null);
        } else if ("title".equals(fieldName)) {
            instance.title = jsonParser.getValueAsString(null);
        } else if ("value".equals(fieldName)) {
            instance.value = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ComplaintMenuOptionResponse.ComplaintMenuData.Choice object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ComplaintMenuOptionResponse.ComplaintMenuData.Choice object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.confirmation != null) {
            jsonGenerator.writeStringField("confirmation", object.confirmation);
        }
        if (object.title != null) {
            jsonGenerator.writeStringField("title", object.title);
        }
        if (object.value != null) {
            jsonGenerator.writeStringField("value", object.value);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
