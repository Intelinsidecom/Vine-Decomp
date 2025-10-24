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

/* loaded from: classes.dex */
public final class VineSingleNotification$$JsonObjectMapper extends JsonMapper<VineSingleNotification> {
    protected static final DateStringToMilliseconds DATE_STRING_TO_MILLISECONDS = new DateStringToMilliseconds();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineSingleNotification parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineSingleNotification _parse(JsonParser jsonParser) throws IOException {
        VineSingleNotification instance = new VineSingleNotification();
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

    public static void parseField(VineSingleNotification instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("avatarUrl".equals(fieldName)) {
            instance.avatarUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("body".equals(fieldName)) {
            instance.comment = jsonParser.getValueAsString(null);
            return;
        }
        if ("conversationId".equals(fieldName)) {
            instance.conversationId = jsonParser.getValueAsLong();
            return;
        }
        if ("avatarUrl".equals(fieldName)) {
            instance.conversationRowId = jsonParser.getValueAsLong();
            return;
        }
        if ("created".equals(fieldName)) {
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
        if ("notificationId".equals(fieldName)) {
            instance.notificationId = jsonParser.getValueAsLong();
            return;
        }
        if ("notificationTypeId".equals(fieldName)) {
            instance.notificationTypeId = jsonParser.getValueAsInt();
            return;
        }
        if ("onboard".equals(fieldName)) {
            instance.onboard = jsonParser.getValueAsString(null);
            return;
        }
        if ("postId".equals(fieldName)) {
            instance.postId = jsonParser.getValueAsLong();
            return;
        }
        if ("recipientUserId".equals(fieldName)) {
            instance.recipientUserId = jsonParser.getValueAsLong();
            return;
        }
        if ("thumbnailUrl".equals(fieldName)) {
            instance.thumbnailUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("title".equals(fieldName)) {
            instance.title = jsonParser.getValueAsString(null);
            return;
        }
        if ("unreadMessageCount".equals(fieldName)) {
            instance.unreadMessageCount = jsonParser.getValueAsLong();
            return;
        }
        if ("url".equals(fieldName)) {
            instance.url = jsonParser.getValueAsString(null);
        } else if ("userId".equals(fieldName)) {
            instance.userId = jsonParser.getValueAsLong();
        } else if ("verified".equals(fieldName)) {
            instance.verified = jsonParser.getValueAsBoolean();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineSingleNotification object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineSingleNotification object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.avatarUrl != null) {
            jsonGenerator.writeStringField("avatarUrl", object.avatarUrl);
        }
        if (object.getComment() != null) {
            jsonGenerator.writeStringField("body", object.getComment());
        }
        jsonGenerator.writeNumberField("conversationId", object.conversationId);
        jsonGenerator.writeNumberField("avatarUrl", object.conversationRowId);
        DATE_STRING_TO_MILLISECONDS.serialize(Long.valueOf(object.getCreatedAt()), "created", true, jsonGenerator);
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
        jsonGenerator.writeNumberField("notificationId", object.getNotificationId());
        jsonGenerator.writeNumberField("notificationTypeId", object.notificationTypeId);
        if (object.onboard != null) {
            jsonGenerator.writeStringField("onboard", object.onboard);
        }
        jsonGenerator.writeNumberField("postId", object.postId);
        jsonGenerator.writeNumberField("recipientUserId", object.recipientUserId);
        if (object.thumbnailUrl != null) {
            jsonGenerator.writeStringField("thumbnailUrl", object.thumbnailUrl);
        }
        if (object.title != null) {
            jsonGenerator.writeStringField("title", object.title);
        }
        jsonGenerator.writeNumberField("unreadMessageCount", object.unreadMessageCount);
        if (object.url != null) {
            jsonGenerator.writeStringField("url", object.url);
        }
        jsonGenerator.writeNumberField("userId", object.userId);
        jsonGenerator.writeBooleanField("verified", object.verified);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
