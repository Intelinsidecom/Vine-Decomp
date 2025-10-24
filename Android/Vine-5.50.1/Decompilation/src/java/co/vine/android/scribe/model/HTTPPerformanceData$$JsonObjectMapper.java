package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class HTTPPerformanceData$$JsonObjectMapper extends JsonMapper<HTTPPerformanceData> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public HTTPPerformanceData parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static HTTPPerformanceData _parse(JsonParser jsonParser) throws IOException {
        HTTPPerformanceData instance = new HTTPPerformanceData();
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

    public static void parseField(HTTPPerformanceData hTTPPerformanceData, String str, JsonParser jsonParser) throws IOException {
        if ("bytes_rcvd".equals(str)) {
            hTTPPerformanceData.bytesReceived = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
            return;
        }
        if ("bytes_sent".equals(str)) {
            hTTPPerformanceData.bytesSent = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
            return;
        }
        if ("duration".equals(str)) {
            hTTPPerformanceData.duration = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
            return;
        }
        if ("duration_to_first_byte".equals(str)) {
            hTTPPerformanceData.durationToFirstByte = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
        } else if ("duration_to_request_sent".equals(str)) {
            hTTPPerformanceData.durationToRequestSent = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
        } else if ("start_timestamp".equals(str)) {
            hTTPPerformanceData.startTimestamp = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(HTTPPerformanceData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(HTTPPerformanceData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.bytesReceived != null) {
            jsonGenerator.writeNumberField("bytes_rcvd", object.bytesReceived.longValue());
        }
        if (object.bytesSent != null) {
            jsonGenerator.writeNumberField("bytes_sent", object.bytesSent.longValue());
        }
        if (object.duration != null) {
            jsonGenerator.writeNumberField("duration", object.duration.doubleValue());
        }
        if (object.durationToFirstByte != null) {
            jsonGenerator.writeNumberField("duration_to_first_byte", object.durationToFirstByte.doubleValue());
        }
        if (object.durationToRequestSent != null) {
            jsonGenerator.writeNumberField("duration_to_request_sent", object.durationToRequestSent.doubleValue());
        }
        if (object.startTimestamp != null) {
            jsonGenerator.writeNumberField("start_timestamp", object.startTimestamp.doubleValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
