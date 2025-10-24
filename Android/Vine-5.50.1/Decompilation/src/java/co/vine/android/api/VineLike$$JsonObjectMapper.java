package co.vine.android.api;

import co.vine.android.api.response.DateStringToMilliseconds;
import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public final class VineLike$$JsonObjectMapper extends JsonMapper<VineLike> {
    protected static final DateStringToMilliseconds DATE_STRING_TO_MILLISECONDS = new DateStringToMilliseconds();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineLike parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineLike _parse(JsonParser jsonParser) throws IOException {
        VineLike instance = new VineLike();
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

    public static void parseField(VineLike instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("avatarUrl".equals(fieldName)) {
            instance.avatarUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("blocked".equals(fieldName)) {
            instance.blocked = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("created".equals(fieldName)) {
            instance.created = DATE_STRING_TO_MILLISECONDS.parse(jsonParser).longValue();
            return;
        }
        if ("following".equals(fieldName)) {
            instance.following = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("likeId".equals(fieldName)) {
            instance.likeId = jsonParser.getValueAsLong();
            return;
        }
        if ("location".equals(fieldName)) {
            instance.location = jsonParser.getValueAsString(null);
            return;
        }
        if ("postId".equals(fieldName)) {
            instance.postId = jsonParser.getValueAsLong();
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
            instance.verified = jsonParser.getValueAsInt();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineLike object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineLike object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.avatarUrl != null) {
            jsonGenerator.writeStringField("avatarUrl", object.avatarUrl);
        }
        if (object.blocked != null) {
            jsonGenerator.writeNumberField("blocked", object.blocked.intValue());
        }
        DATE_STRING_TO_MILLISECONDS.serialize(Long.valueOf(object.created), "created", true, jsonGenerator);
        if (object.following != null) {
            jsonGenerator.writeNumberField("following", object.following.intValue());
        }
        jsonGenerator.writeNumberField("likeId", object.likeId);
        if (object.location != null) {
            jsonGenerator.writeStringField("location", object.location);
        }
        jsonGenerator.writeNumberField("postId", object.postId);
        if (object.user != null) {
            LoganSquare.typeConverterFor(VineUser.class).serialize(object.user, PropertyConfiguration.USER, true, jsonGenerator);
        }
        jsonGenerator.writeNumberField("userId", object.userId);
        if (object.username != null) {
            jsonGenerator.writeStringField("username", object.username);
        }
        jsonGenerator.writeNumberField("verified", object.verified);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
