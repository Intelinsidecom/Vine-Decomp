package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.mobileapptracker.MATEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class EventDetails$$JsonObjectMapper extends JsonMapper<EventDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public EventDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static EventDetails _parse(JsonParser jsonParser) throws IOException {
        EventDetails instance = new EventDetails();
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

    public static void parseField(EventDetails instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("alert".equals(fieldName)) {
            instance.alert = AlertDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("http_performance_data".equals(fieldName)) {
            instance.httpPerformanceData = HTTPPerformanceData$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("http_request_details".equals(fieldName)) {
            instance.httpRequestDetails = HTTPRequestDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("items".equals(fieldName)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<Item> collection1 = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    Item value1 = Item$$JsonObjectMapper._parse(jsonParser);
                    collection1.add(value1);
                }
                instance.items = collection1;
                return;
            }
            instance.items = null;
            return;
        }
        if ("launch".equals(fieldName)) {
            instance.launch = LaunchDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("playback_summary".equals(fieldName)) {
            instance.playbackSummary = PlaybackSummaryDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if (MATEvent.SHARE.equals(fieldName)) {
            instance.share = ShareDetails$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("timestamp".equals(fieldName)) {
            instance.timestamp = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
        } else if ("timing".equals(fieldName)) {
            instance.timing = TimingDetails$$JsonObjectMapper._parse(jsonParser);
        } else if ("video_import_details".equals(fieldName)) {
            instance.videoImportDetails = VideoImportDetails$$JsonObjectMapper._parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(EventDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(EventDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.alert != null) {
            jsonGenerator.writeFieldName("alert");
            AlertDetails$$JsonObjectMapper._serialize(object.alert, jsonGenerator, true);
        }
        if (object.httpPerformanceData != null) {
            jsonGenerator.writeFieldName("http_performance_data");
            HTTPPerformanceData$$JsonObjectMapper._serialize(object.httpPerformanceData, jsonGenerator, true);
        }
        if (object.httpRequestDetails != null) {
            jsonGenerator.writeFieldName("http_request_details");
            HTTPRequestDetails$$JsonObjectMapper._serialize(object.httpRequestDetails, jsonGenerator, true);
        }
        List<Item> lslocalitems = object.items;
        if (lslocalitems != null) {
            jsonGenerator.writeFieldName("items");
            jsonGenerator.writeStartArray();
            for (Item element1 : lslocalitems) {
                if (element1 != null) {
                    Item$$JsonObjectMapper._serialize(element1, jsonGenerator, true);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (object.launch != null) {
            jsonGenerator.writeFieldName("launch");
            LaunchDetails$$JsonObjectMapper._serialize(object.launch, jsonGenerator, true);
        }
        if (object.playbackSummary != null) {
            jsonGenerator.writeFieldName("playback_summary");
            PlaybackSummaryDetails$$JsonObjectMapper._serialize(object.playbackSummary, jsonGenerator, true);
        }
        if (object.share != null) {
            jsonGenerator.writeFieldName(MATEvent.SHARE);
            ShareDetails$$JsonObjectMapper._serialize(object.share, jsonGenerator, true);
        }
        if (object.timestamp != null) {
            jsonGenerator.writeNumberField("timestamp", object.timestamp.doubleValue());
        }
        if (object.timing != null) {
            jsonGenerator.writeFieldName("timing");
            TimingDetails$$JsonObjectMapper._serialize(object.timing, jsonGenerator, true);
        }
        if (object.videoImportDetails != null) {
            jsonGenerator.writeFieldName("video_import_details");
            VideoImportDetails$$JsonObjectMapper._serialize(object.videoImportDetails, jsonGenerator, true);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
