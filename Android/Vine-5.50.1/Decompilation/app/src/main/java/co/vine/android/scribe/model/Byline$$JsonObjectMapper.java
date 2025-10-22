package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class Byline$$JsonObjectMapper extends JsonMapper<Byline> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public Byline parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static Byline _parse(JsonParser jsonParser) throws IOException {
        Byline instance = new Byline();
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

    public static void parseField(Byline instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("action_icon_url".equals(fieldName)) {
            instance.actionIconUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("action_title".equals(fieldName)) {
            instance.actionTitle = jsonParser.getValueAsString(null);
            return;
        }
        if ("body".equals(fieldName)) {
            instance.body = jsonParser.getValueAsString(null);
            return;
        }
        if ("description".equals(fieldName)) {
            instance.description = jsonParser.getValueAsString(null);
            return;
        }
        if ("detailed_description".equals(fieldName)) {
            instance.detailedDescription = jsonParser.getValueAsString(null);
            return;
        }
        if ("icon_url".equals(fieldName)) {
            instance.iconUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("post_ids".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<Long> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    Long value1 = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Long.valueOf(jsonParser.getValueAsLong());
                    collection1.add(value1);
                }
                instance.postIds = collection1;
                return;
            }
            instance.postIds = null;
            return;
        }
        if ("user_ids".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<Long> collection12 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    Long value12 = jsonParser.getCurrentToken() == JsonToken.VALUE_NULL ? null : Long.valueOf(jsonParser.getValueAsLong());
                    collection12.add(value12);
                }
                instance.userIds = collection12;
                return;
            }
            instance.userIds = null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(Byline object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(Byline object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.actionIconUrl != null) {
            jsonGenerator.writeStringField("action_icon_url", object.actionIconUrl);
        }
        if (object.actionTitle != null) {
            jsonGenerator.writeStringField("action_title", object.actionTitle);
        }
        if (object.body != null) {
            jsonGenerator.writeStringField("body", object.body);
        }
        if (object.description != null) {
            jsonGenerator.writeStringField("description", object.description);
        }
        if (object.detailedDescription != null) {
            jsonGenerator.writeStringField("detailed_description", object.detailedDescription);
        }
        if (object.iconUrl != null) {
            jsonGenerator.writeStringField("icon_url", object.iconUrl);
        }
        List<Long> lslocalpost_ids = object.postIds;
        if (lslocalpost_ids != null) {
            jsonGenerator.writeFieldName("post_ids");
            jsonGenerator.writeStartArray();
            for (Long element1 : lslocalpost_ids) {
                if (element1 != null) {
                    jsonGenerator.writeNumber(element1.longValue());
                }
            }
            jsonGenerator.writeEndArray();
        }
        List<Long> lslocaluser_ids = object.userIds;
        if (lslocaluser_ids != null) {
            jsonGenerator.writeFieldName("user_ids");
            jsonGenerator.writeStartArray();
            for (Long element12 : lslocaluser_ids) {
                if (element12 != null) {
                    jsonGenerator.writeNumber(element12.longValue());
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
