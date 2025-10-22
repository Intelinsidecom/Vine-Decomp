package co.vine.android.api;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class TwitterUser$$JsonObjectMapper extends JsonMapper<TwitterUser> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public TwitterUser parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static TwitterUser _parse(JsonParser jsonParser) throws IOException {
        TwitterUser instance = new TwitterUser();
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

    public static void parseField(TwitterUser instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("default_profile_image".equals(fieldName)) {
            instance.defaultProfileImage = jsonParser.getValueAsBoolean();
            return;
        }
        if ("description".equals(fieldName)) {
            instance.description = jsonParser.getValueAsString(null);
            return;
        }
        if ("location".equals(fieldName)) {
            instance.location = jsonParser.getValueAsString(null);
            return;
        }
        if ("name".equals(fieldName)) {
            instance.name = jsonParser.getValueAsString(null);
            return;
        }
        if ("profile_image_url".equals(fieldName)) {
            instance.profileUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("screen_name".equals(fieldName)) {
            instance.screenName = jsonParser.getValueAsString(null);
        } else if ("url".equals(fieldName)) {
            instance.url = jsonParser.getValueAsString(null);
        } else if ("id".equals(fieldName)) {
            instance.userId = jsonParser.getValueAsLong();
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(TwitterUser object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(TwitterUser object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeBooleanField("default_profile_image", object.defaultProfileImage);
        if (object.description != null) {
            jsonGenerator.writeStringField("description", object.description);
        }
        if (object.location != null) {
            jsonGenerator.writeStringField("location", object.location);
        }
        if (object.name != null) {
            jsonGenerator.writeStringField("name", object.name);
        }
        if (object.profileUrl != null) {
            jsonGenerator.writeStringField("profile_image_url", object.profileUrl);
        }
        if (object.screenName != null) {
            jsonGenerator.writeStringField("screen_name", object.screenName);
        }
        if (object.url != null) {
            jsonGenerator.writeStringField("url", object.url);
        }
        jsonGenerator.writeNumberField("id", object.userId);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
