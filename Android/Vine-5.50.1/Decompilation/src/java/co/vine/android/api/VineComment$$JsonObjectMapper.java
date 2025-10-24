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
public final class VineComment$$JsonObjectMapper extends JsonMapper<VineComment> {
    protected static final DateStringToMilliseconds DATE_STRING_TO_MILLISECONDS = new DateStringToMilliseconds();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineComment parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineComment _parse(JsonParser jsonParser) throws IOException {
        VineComment instance = new VineComment();
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

    public static void parseField(VineComment instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("avatarUrl".equals(fieldName)) {
            instance.avatarUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("comment".equals(fieldName)) {
            instance.comment = jsonParser.getValueAsString(null);
            return;
        }
        if ("commentId".equals(fieldName)) {
            instance.commentId = jsonParser.getValueAsLong();
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
        if ("location".equals(fieldName)) {
            instance.location = jsonParser.getValueAsString(null);
            return;
        }
        if ("created".equals(fieldName)) {
            instance.timestamp = DATE_STRING_TO_MILLISECONDS.parse(jsonParser).longValue();
            return;
        }
        if ("twitterScreenname".equals(fieldName)) {
            instance.twitterScreenname = jsonParser.getValueAsString(null);
            return;
        }
        if ("twitterVerified".equals(fieldName)) {
            instance.twitterVerified = jsonParser.getValueAsBoolean();
            return;
        }
        if (PropertyConfiguration.USER.equals(fieldName)) {
            instance.user = (VineUser) LoganSquare.typeConverterFor(VineUser.class).parse(jsonParser);
            return;
        }
        if ("userId".equals(fieldName)) {
            instance.userId = jsonParser.getValueAsLong();
        } else if ("username".equals(fieldName)) {
            instance.username = jsonParser.getValueAsString(null);
        } else if ("verified".equals(fieldName)) {
            instance.verified = jsonParser.getValueAsBoolean();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineComment object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineComment object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.avatarUrl != null) {
            jsonGenerator.writeStringField("avatarUrl", object.avatarUrl);
        }
        if (object.comment != null) {
            jsonGenerator.writeStringField("comment", object.comment);
        }
        jsonGenerator.writeNumberField("commentId", object.commentId);
        List<VineEntity> lslocalentities = object.entities;
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
        if (object.location != null) {
            jsonGenerator.writeStringField("location", object.location);
        }
        DATE_STRING_TO_MILLISECONDS.serialize(Long.valueOf(object.timestamp), "created", true, jsonGenerator);
        if (object.twitterScreenname != null) {
            jsonGenerator.writeStringField("twitterScreenname", object.twitterScreenname);
        }
        jsonGenerator.writeBooleanField("twitterVerified", object.twitterVerified);
        if (object.user != null) {
            LoganSquare.typeConverterFor(VineUser.class).serialize(object.user, PropertyConfiguration.USER, true, jsonGenerator);
        }
        jsonGenerator.writeNumberField("userId", object.userId);
        if (object.username != null) {
            jsonGenerator.writeStringField("username", object.username);
        }
        jsonGenerator.writeBooleanField("verified", object.verified);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
