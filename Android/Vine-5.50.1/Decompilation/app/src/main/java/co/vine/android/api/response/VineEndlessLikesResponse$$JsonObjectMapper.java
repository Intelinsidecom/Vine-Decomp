package co.vine.android.api.response;

import co.vine.android.api.VineEndlessLikesRecord;
import co.vine.android.api.VineEndlessLikesRecord$$JsonObjectMapper;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineEndlessLikesResponse$$JsonObjectMapper extends JsonMapper<VineEndlessLikesResponse> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineEndlessLikesResponse parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineEndlessLikesResponse _parse(JsonParser jsonParser) throws IOException {
        VineEndlessLikesResponse instance = new VineEndlessLikesResponse();
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

    public static void parseField(VineEndlessLikesResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("data".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineEndlessLikesRecord> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineEndlessLikesRecord value1 = VineEndlessLikesRecord$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.data = collection1;
                return;
            }
            instance.data = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineEndlessLikesResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineEndlessLikesResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<VineEndlessLikesRecord> lslocaldata = object.data;
        if (lslocaldata != null) {
            jsonGenerator.writeFieldName("data");
            jsonGenerator.writeStartArray();
            for (VineEndlessLikesRecord element1 : lslocaldata) {
                if (element1 != null) {
                    VineEndlessLikesRecord$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
