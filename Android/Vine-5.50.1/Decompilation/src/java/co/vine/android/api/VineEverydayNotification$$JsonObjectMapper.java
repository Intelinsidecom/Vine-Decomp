package co.vine.android.api;

import co.vine.android.api.response.DateStringToMilliseconds;
import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public final class VineEverydayNotification$$JsonObjectMapper extends JsonMapper<VineEverydayNotification> {
    protected static final DateStringToMilliseconds DATE_STRING_TO_MILLISECONDS = new DateStringToMilliseconds();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineEverydayNotification parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineEverydayNotification _parse(JsonParser jsonParser) throws IOException {
        VineEverydayNotification instance = new VineEverydayNotification();
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

    public static void parseField(VineEverydayNotification instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("anchor".equals(fieldName) || "anchorStr".equals(fieldName)) {
            instance.anchor = jsonParser.getValueAsString(null);
            return;
        }
        if ("backAnchor".equals(fieldName)) {
            instance.backAnchor = jsonParser.getValueAsString(null);
            return;
        }
        if ("body".equals(fieldName)) {
            instance.comment = jsonParser.getValueAsString(null);
            return;
        }
        if ("commentText".equals(fieldName)) {
            instance.commentText = jsonParser.getValueAsString(null);
            return;
        }
        if ("lastActivityTime".equals(fieldName)) {
            instance.createdAt = DATE_STRING_TO_MILLISECONDS.parse(jsonParser).longValue();
            return;
        }
        if ("entities".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<VineEntity> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    VineEntity value1 = (VineEntity) LoganSquare.typeConverterFor(VineEntity.class).parse(jsonParser);
                    collection1.add(value1);
                }
                instance.entities = collection1;
                return;
            }
            instance.entities = null;
            return;
        }
        if ("isNew".equals(fieldName)) {
            instance.isNew = jsonParser.getValueAsBoolean();
            return;
        }
        if ("link".equals(fieldName)) {
            instance.link = jsonParser.getValueAsString(null);
            return;
        }
        if ("activityId".equals(fieldName)) {
            instance.notificationId = jsonParser.getValueAsLong();
            return;
        }
        if ("shortBody".equals(fieldName)) {
            instance.shortBody = jsonParser.getValueAsString(null);
        } else if ("type".equals(fieldName)) {
            instance.type = jsonParser.getValueAsString(null);
        } else if (PropertyConfiguration.USER.equals(fieldName)) {
            instance.user = (VineUser) LoganSquare.typeConverterFor(VineUser.class).parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineEverydayNotification object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineEverydayNotification object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.anchor != null) {
            jsonGenerator.writeStringField("anchor", object.anchor);
        }
        if (object.backAnchor != null) {
            jsonGenerator.writeStringField("backAnchor", object.backAnchor);
        }
        if (object.getComment() != null) {
            jsonGenerator.writeStringField("body", object.getComment());
        }
        if (object.commentText != null) {
            jsonGenerator.writeStringField("commentText", object.commentText);
        }
        DATE_STRING_TO_MILLISECONDS.serialize(Long.valueOf(object.getCreatedAt()), "lastActivityTime", true, jsonGenerator);
        List<VineEntity> lslocalentities = object.getEntities();
        if (lslocalentities != null) {
            jsonGenerator.writeFieldName("entities");
            jsonGenerator.writeStartArray();
            for (VineEntity element1 : lslocalentities) {
                if (element1 != null) {
                    LoganSquare.typeConverterFor(VineEntity.class).serialize(element1, "lslocalentitiesElement", false, jsonGenerator);
                }
            }
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeBooleanField("isNew", object.isNew);
        if (object.link != null) {
            jsonGenerator.writeStringField("link", object.link);
        }
        jsonGenerator.writeNumberField("activityId", object.getNotificationId());
        if (object.shortBody != null) {
            jsonGenerator.writeStringField("shortBody", object.shortBody);
        }
        if (object.type != null) {
            jsonGenerator.writeStringField("type", object.type);
        }
        if (object.user != null) {
            LoganSquare.typeConverterFor(VineUser.class).serialize(object.user, PropertyConfiguration.USER, true, jsonGenerator);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
