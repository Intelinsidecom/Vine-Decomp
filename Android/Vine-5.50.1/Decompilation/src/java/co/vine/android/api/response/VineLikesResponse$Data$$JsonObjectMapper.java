package co.vine.android.api.response;

import co.vine.android.api.VineLike;
import co.vine.android.api.VineLike$$JsonObjectMapper;
import co.vine.android.api.response.VineLikesResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineLikesResponse$Data$$JsonObjectMapper extends JsonMapper<VineLikesResponse.Data> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineLikesResponse.Data parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineLikesResponse.Data _parse(JsonParser jsonParser) throws IOException {
        VineLikesResponse.Data instance = new VineLikesResponse.Data();
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

    public static void parseField(VineLikesResponse.Data instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("records".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineLike> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineLike value1 = VineLike$$JsonObjectMapper._parse(jsonParser);
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
    public void serialize(VineLikesResponse.Data object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineLikesResponse.Data object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<VineLike> lslocalrecords = object.items;
        if (lslocalrecords != null) {
            jsonGenerator.writeFieldName("records");
            jsonGenerator.writeStartArray();
            for (VineLike element1 : lslocalrecords) {
                if (element1 != null) {
                    VineLike$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
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
