package co.vine.android.api.response;

import co.vine.android.api.response.ComplaintMenuOptionResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class ComplaintMenuOptionResponse$ComplaintMenuData$$JsonObjectMapper extends JsonMapper<ComplaintMenuOptionResponse.ComplaintMenuData> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ComplaintMenuOptionResponse.ComplaintMenuData parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ComplaintMenuOptionResponse.ComplaintMenuData _parse(JsonParser jsonParser) throws IOException {
        ComplaintMenuOptionResponse.ComplaintMenuData instance = new ComplaintMenuOptionResponse.ComplaintMenuData();
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

    public static void parseField(ComplaintMenuOptionResponse.ComplaintMenuData instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("choices".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<ComplaintMenuOptionResponse.ComplaintMenuData.Choice> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    ComplaintMenuOptionResponse.ComplaintMenuData.Choice value1 = ComplaintMenuOptionResponse$ComplaintMenuData$Choice$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.choices = collection1;
                return;
            }
            instance.choices = null;
            return;
        }
        if ("prompt".equals(fieldName)) {
            instance.prompt = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ComplaintMenuOptionResponse.ComplaintMenuData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ComplaintMenuOptionResponse.ComplaintMenuData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<ComplaintMenuOptionResponse.ComplaintMenuData.Choice> lslocalchoices = object.choices;
        if (lslocalchoices != null) {
            jsonGenerator.writeFieldName("choices");
            jsonGenerator.writeStartArray();
            for (ComplaintMenuOptionResponse.ComplaintMenuData.Choice element1 : lslocalchoices) {
                if (element1 != null) {
                    ComplaintMenuOptionResponse$ComplaintMenuData$Choice$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (object.prompt != null) {
            jsonGenerator.writeStringField("prompt", object.prompt);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
