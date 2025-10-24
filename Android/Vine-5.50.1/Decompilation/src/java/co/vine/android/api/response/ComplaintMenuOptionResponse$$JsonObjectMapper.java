package co.vine.android.api.response;

import co.vine.android.api.response.ComplaintMenuOptionResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class ComplaintMenuOptionResponse$$JsonObjectMapper extends JsonMapper<ComplaintMenuOptionResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ComplaintMenuOptionResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ComplaintMenuOptionResponse _parse(JsonParser jsonParser) throws IOException {
        ComplaintMenuOptionResponse instance = new ComplaintMenuOptionResponse();
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

    public static void parseField(ComplaintMenuOptionResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("data".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                HashMap<String, ComplaintMenuOptionResponse.ComplaintMenuData> map1 = new HashMap<>();
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    String key1 = jsonParser.getText();
                    jsonParser.nextToken();
                    if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
                        map1.put(key1, null);
                    } else {
                        map1.put(key1, ComplaintMenuOptionResponse$ComplaintMenuData$$JsonObjectMapper._parse(jsonParser));
                    }
                }
                instance.data = map1;
                return;
            }
            instance.data = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ComplaintMenuOptionResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ComplaintMenuOptionResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        Map<String, ComplaintMenuOptionResponse.ComplaintMenuData> lslocaldata = object.data;
        if (lslocaldata != null) {
            jsonGenerator.writeFieldName("data");
            jsonGenerator.writeStartObject();
            for (Map.Entry<String, ComplaintMenuOptionResponse.ComplaintMenuData> entry1 : lslocaldata.entrySet()) {
                jsonGenerator.writeFieldName(entry1.getKey().toString());
                if (entry1.getValue() != null) {
                    ComplaintMenuOptionResponse$ComplaintMenuData$$JsonObjectMapper._serialize(entry1.getValue(), jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndObject();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
