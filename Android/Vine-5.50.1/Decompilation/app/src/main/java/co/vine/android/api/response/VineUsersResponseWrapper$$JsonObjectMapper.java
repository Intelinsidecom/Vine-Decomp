package co.vine.android.api.response;

import co.vine.android.api.VineUser;
import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineUsersResponseWrapper$$JsonObjectMapper extends JsonMapper<VineUsersResponseWrapper> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineUsersResponseWrapper parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineUsersResponseWrapper _parse(JsonParser jsonParser) throws IOException {
        VineUsersResponseWrapper instance = new VineUsersResponseWrapper();
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

    public static void parseField(VineUsersResponseWrapper instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("data".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineUser> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineUser value1 = (VineUser) LoganSquare.typeConverterFor(VineUser.class).parse(jsonParser);
                    collection1.add(value1);
                }
                instance.data = collection1;
                return;
            }
            instance.data = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineUsersResponseWrapper object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineUsersResponseWrapper object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<VineUser> lslocaldata = object.data;
        if (lslocaldata != null) {
            jsonGenerator.writeFieldName("data");
            jsonGenerator.writeStartArray();
            for (VineUser element1 : lslocaldata) {
                if (element1 != null) {
                    LoganSquare.typeConverterFor(VineUser.class).serialize(element1, "lslocaldataElement", false, jsonGenerator);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
