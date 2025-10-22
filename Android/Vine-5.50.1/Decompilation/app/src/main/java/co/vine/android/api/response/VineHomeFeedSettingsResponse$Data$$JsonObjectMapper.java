package co.vine.android.api.response;

import co.vine.android.api.VineHomeFeedSetting;
import co.vine.android.api.VineHomeFeedSetting$$JsonObjectMapper;
import co.vine.android.api.response.VineHomeFeedSettingsResponse;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineHomeFeedSettingsResponse$Data$$JsonObjectMapper extends JsonMapper<VineHomeFeedSettingsResponse.Data> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineHomeFeedSettingsResponse.Data parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineHomeFeedSettingsResponse.Data _parse(JsonParser jsonParser) throws IOException {
        VineHomeFeedSettingsResponse.Data instance = new VineHomeFeedSettingsResponse.Data();
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

    public static void parseField(VineHomeFeedSettingsResponse.Data instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("records".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineHomeFeedSetting> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineHomeFeedSetting value1 = VineHomeFeedSetting$$JsonObjectMapper._parse(jsonParser);
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
    public void serialize(VineHomeFeedSettingsResponse.Data object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineHomeFeedSettingsResponse.Data object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<VineHomeFeedSetting> lslocalrecords = object.items;
        if (lslocalrecords != null) {
            jsonGenerator.writeFieldName("records");
            jsonGenerator.writeStartArray();
            for (VineHomeFeedSetting element1 : lslocalrecords) {
                if (element1 != null) {
                    VineHomeFeedSetting$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
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
