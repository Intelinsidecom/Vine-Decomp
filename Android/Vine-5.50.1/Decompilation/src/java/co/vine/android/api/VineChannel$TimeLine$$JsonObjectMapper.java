package co.vine.android.api;

import co.vine.android.api.VineChannel;
import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineChannel$TimeLine$$JsonObjectMapper extends JsonMapper<VineChannel.TimeLine> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineChannel.TimeLine parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineChannel.TimeLine _parse(JsonParser jsonParser) throws IOException {
        VineChannel.TimeLine instance = new VineChannel.TimeLine();
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

    public static void parseField(VineChannel.TimeLine instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("records".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<TimelineItem> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    TimelineItem value1 = (TimelineItem) LoganSquare.typeConverterFor(TimelineItem.class).parse(jsonParser);
                    collection1.add(value1);
                }
                instance.items = collection1;
                return;
            }
            instance.items = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineChannel.TimeLine object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineChannel.TimeLine object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        List<TimelineItem> lslocalrecords = object.items;
        if (lslocalrecords != null) {
            jsonGenerator.writeFieldName("records");
            jsonGenerator.writeStartArray();
            for (TimelineItem element1 : lslocalrecords) {
                if (element1 != null) {
                    LoganSquare.typeConverterFor(TimelineItem.class).serialize(element1, "lslocalrecordsElement", false, jsonGenerator);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
