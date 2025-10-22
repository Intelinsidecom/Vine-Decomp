package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ActivityDetails$$JsonObjectMapper extends JsonMapper<ActivityDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ActivityDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ActivityDetails _parse(JsonParser jsonParser) throws IOException {
        ActivityDetails instance = new ActivityDetails();
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

    public static void parseField(ActivityDetails activityDetails, String str, JsonParser jsonParser) throws IOException {
        if ("activity_id".equals(str)) {
            activityDetails.activityId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
        } else if ("activity_type".equals(str)) {
            activityDetails.activityType = jsonParser.getValueAsString(null);
        } else if ("n_more".equals(str)) {
            activityDetails.nMore = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ActivityDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ActivityDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.activityId != null) {
            jsonGenerator.writeNumberField("activity_id", object.activityId.longValue());
        }
        if (object.activityType != null) {
            jsonGenerator.writeStringField("activity_type", object.activityType);
        }
        if (object.nMore != null) {
            jsonGenerator.writeNumberField("n_more", object.nMore.intValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
