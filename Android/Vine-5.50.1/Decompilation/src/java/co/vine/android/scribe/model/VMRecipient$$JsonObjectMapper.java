package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public final class VMRecipient$$JsonObjectMapper extends JsonMapper<VMRecipient> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VMRecipient parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VMRecipient _parse(JsonParser jsonParser) throws IOException {
        VMRecipient instance = new VMRecipient();
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

    public static void parseField(VMRecipient instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("is_email".equals(fieldName)) {
            instance.isEmail = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
        } else if ("is_phone".equals(fieldName)) {
            instance.isPhone = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
        } else if (PropertyConfiguration.USER.equals(fieldName)) {
            instance.user = UserDetails$$JsonObjectMapper._parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VMRecipient object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VMRecipient object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.isEmail != null) {
            jsonGenerator.writeBooleanField("is_email", object.isEmail.booleanValue());
        }
        if (object.isPhone != null) {
            jsonGenerator.writeBooleanField("is_phone", object.isPhone.booleanValue());
        }
        if (object.user != null) {
            jsonGenerator.writeFieldName(PropertyConfiguration.USER);
            UserDetails$$JsonObjectMapper._serialize(object.user, jsonGenerator, true);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
