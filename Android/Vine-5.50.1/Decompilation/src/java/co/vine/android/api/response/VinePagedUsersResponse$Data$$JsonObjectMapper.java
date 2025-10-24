package co.vine.android.api.response;

import co.vine.android.api.VineUser;
import co.vine.android.api.response.VinePagedUsersResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VinePagedUsersResponse$Data$$JsonObjectMapper extends JsonMapper<VinePagedUsersResponse.Data> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VinePagedUsersResponse.Data parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VinePagedUsersResponse.Data _parse(JsonParser jsonParser) throws IOException {
        VinePagedUsersResponse.Data instance = new VinePagedUsersResponse.Data();
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

    public static void parseField(VinePagedUsersResponse.Data instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("records".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineUser> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineUser value1 = (VineUser) LoganSquare.typeConverterFor(VineUser.class).parse(jsonParser);
                    collection1.add(value1);
                }
                instance.items = collection1;
                return;
            }
            instance.items = null;
            return;
        }
        PagedDataResponse$$JsonObjectMapper.parseField(instance, fieldName, jsonParser);
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VinePagedUsersResponse.Data object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VinePagedUsersResponse.Data object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<VineUser> lslocalrecords = object.items;
        if (lslocalrecords != null) {
            jsonGenerator.writeFieldName("records");
            jsonGenerator.writeStartArray();
            for (VineUser element1 : lslocalrecords) {
                if (element1 != null) {
                    LoganSquare.typeConverterFor(VineUser.class).serialize(element1, "lslocalrecordsElement", false, jsonGenerator);
                }
            }
            jsonGenerator.writeEndArray();
        }
        PagedDataResponse$$JsonObjectMapper._serialize(object, jsonGenerator, false);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
