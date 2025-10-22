package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ServerStatus$$JsonObjectMapper extends JsonMapper<ServerStatus> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ServerStatus parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ServerStatus _parse(JsonParser jsonParser) throws IOException {
        ServerStatus instance = new ServerStatus();
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

    public static void parseField(ServerStatus instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("message".equals(fieldName)) {
            instance.message = jsonParser.getValueAsString(null);
            return;
        }
        if ("staticTimelineUrl".equals(fieldName)) {
            instance.staticTimelineUrl = jsonParser.getValueAsString(null);
        } else if ("status".equals(fieldName)) {
            instance.status = jsonParser.getValueAsString(null);
        } else if ("uploadType".equals(fieldName)) {
            instance.uploadType = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ServerStatus object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ServerStatus object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.message != null) {
            jsonGenerator.writeStringField("message", object.message);
        }
        if (object.staticTimelineUrl != null) {
            jsonGenerator.writeStringField("staticTimelineUrl", object.staticTimelineUrl);
        }
        if (object.status != null) {
            jsonGenerator.writeStringField("status", object.status);
        }
        if (object.uploadType != null) {
            jsonGenerator.writeStringField("uploadType", object.uploadType);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
