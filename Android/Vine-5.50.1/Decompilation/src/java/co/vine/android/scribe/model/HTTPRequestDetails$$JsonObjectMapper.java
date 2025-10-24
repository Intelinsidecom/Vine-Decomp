package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class HTTPRequestDetails$$JsonObjectMapper extends JsonMapper<HTTPRequestDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public HTTPRequestDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static HTTPRequestDetails _parse(JsonParser jsonParser) throws IOException {
        HTTPRequestDetails instance = new HTTPRequestDetails();
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

    public static void parseField(HTTPRequestDetails instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("api_error".equals(fieldName)) {
            instance.apiError = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("http_status".equals(fieldName)) {
            instance.httpStatus = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("method".equals(fieldName)) {
            instance.method = jsonParser.getValueAsString(null);
            return;
        }
        if ("network_error".equals(fieldName)) {
            instance.networkError = jsonParser.getValueAsString(null);
        } else if ("os_error_details".equals(fieldName)) {
            instance.osErrorDetails = jsonParser.getValueAsString(null);
        } else if ("url".equals(fieldName)) {
            instance.url = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(HTTPRequestDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(HTTPRequestDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.apiError != null) {
            jsonGenerator.writeNumberField("api_error", object.apiError.intValue());
        }
        if (object.httpStatus != null) {
            jsonGenerator.writeNumberField("http_status", object.httpStatus.intValue());
        }
        if (object.method != null) {
            jsonGenerator.writeStringField("method", object.method);
        }
        if (object.networkError != null) {
            jsonGenerator.writeStringField("network_error", object.networkError);
        }
        if (object.osErrorDetails != null) {
            jsonGenerator.writeStringField("os_error_details", object.osErrorDetails);
        }
        if (object.url != null) {
            jsonGenerator.writeStringField("url", object.url);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
