package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class ExperimentData$$JsonObjectMapper extends JsonMapper<ExperimentData> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ExperimentData parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ExperimentData _parse(JsonParser jsonParser) throws IOException {
        ExperimentData instance = new ExperimentData();
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

    public static void parseField(ExperimentData instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("experiment_values".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<ExperimentValue> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    ExperimentValue value1 = ExperimentValue$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.experimentValues = collection1;
                return;
            }
            instance.experimentValues = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ExperimentData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ExperimentData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<ExperimentValue> lslocalexperiment_values = object.experimentValues;
        if (lslocalexperiment_values != null) {
            jsonGenerator.writeFieldName("experiment_values");
            jsonGenerator.writeStartArray();
            for (ExperimentValue element1 : lslocalexperiment_values) {
                if (element1 != null) {
                    ExperimentValue$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
