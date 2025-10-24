package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class UserDetails$$JsonObjectMapper extends JsonMapper<UserDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public UserDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static UserDetails _parse(JsonParser jsonParser) throws IOException {
        UserDetails instance = new UserDetails();
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

    public static void parseField(UserDetails userDetails, String str, JsonParser jsonParser) throws IOException {
        if ("following".equals(str)) {
            userDetails.following = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
        } else if ("user_id".equals(str)) {
            userDetails.userId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(UserDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(UserDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.following != null) {
            jsonGenerator.writeBooleanField("following", object.following.booleanValue());
        }
        if (object.userId != null) {
            jsonGenerator.writeNumberField("user_id", object.userId.longValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
