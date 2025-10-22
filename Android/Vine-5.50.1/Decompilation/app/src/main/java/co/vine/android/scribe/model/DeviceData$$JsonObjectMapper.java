package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class DeviceData$$JsonObjectMapper extends JsonMapper<DeviceData> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public DeviceData parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static DeviceData _parse(JsonParser jsonParser) throws IOException {
        DeviceData instance = new DeviceData();
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

    public static void parseField(DeviceData deviceData, String str, JsonParser jsonParser) throws IOException {
        if ("battery_level".equals(str)) {
            deviceData.batteryLevel = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
            return;
        }
        if ("brightness".equals(str)) {
            deviceData.brightness = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
            return;
        }
        if ("browser".equals(str)) {
            deviceData.browser = jsonParser.getValueAsString(null);
            return;
        }
        if ("browser_version".equals(str)) {
            deviceData.browserVersion = jsonParser.getValueAsString(null);
            return;
        }
        if ("bytes_available".equals(str)) {
            deviceData.bytesAvailable = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
            return;
        }
        if ("bytes_free".equals(str)) {
            deviceData.bytesFree = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
            return;
        }
        if ("device_model".equals(str)) {
            deviceData.deviceModel = jsonParser.getValueAsString(null);
            return;
        }
        if ("device_name".equals(str)) {
            deviceData.deviceName = jsonParser.getValueAsString(null);
            return;
        }
        if ("location".equals(str)) {
            deviceData.gpsData = GPSData$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("internet_access_type".equals(str)) {
            deviceData.internetAccessType = jsonParser.getValueAsString(null);
            return;
        }
        if ("language_codes".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList arrayList = new ArrayList();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    arrayList.add(jsonParser.getValueAsString(null));
                }
                deviceData.languageCodes = arrayList;
                return;
            }
            deviceData.languageCodes = null;
            return;
        }
        if ("manufacturer".equals(str)) {
            deviceData.manufacturer = jsonParser.getValueAsString(null);
            return;
        }
        if ("orientation".equals(str)) {
            deviceData.orientation = jsonParser.getValueAsString(null);
            return;
        }
        if ("os".equals(str)) {
            deviceData.os = jsonParser.getValueAsString(null);
            return;
        }
        if ("os_version".equals(str)) {
            deviceData.osVersion = jsonParser.getValueAsString(null);
            return;
        }
        if ("other_audio_is_playing".equals(str)) {
            deviceData.otherAudioIsPlaying = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
        } else if ("radio_details".equals(str)) {
            deviceData.radioDetails = MobileRadioDetails$$JsonObjectMapper._parse(jsonParser);
        } else if ("timezone".equals(str)) {
            deviceData.timezone = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(DeviceData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(DeviceData object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.batteryLevel != null) {
            jsonGenerator.writeNumberField("battery_level", object.batteryLevel.doubleValue());
        }
        if (object.brightness != null) {
            jsonGenerator.writeNumberField("brightness", object.brightness.doubleValue());
        }
        if (object.browser != null) {
            jsonGenerator.writeStringField("browser", object.browser);
        }
        if (object.browserVersion != null) {
            jsonGenerator.writeStringField("browser_version", object.browserVersion);
        }
        if (object.bytesAvailable != null) {
            jsonGenerator.writeNumberField("bytes_available", object.bytesAvailable.longValue());
        }
        if (object.bytesFree != null) {
            jsonGenerator.writeNumberField("bytes_free", object.bytesFree.longValue());
        }
        if (object.deviceModel != null) {
            jsonGenerator.writeStringField("device_model", object.deviceModel);
        }
        if (object.deviceName != null) {
            jsonGenerator.writeStringField("device_name", object.deviceName);
        }
        if (object.gpsData != null) {
            jsonGenerator.writeFieldName("location");
            GPSData$$JsonObjectMapper._serialize(object.gpsData, jsonGenerator, true);
        }
        if (object.internetAccessType != null) {
            jsonGenerator.writeStringField("internet_access_type", object.internetAccessType);
        }
        List<String> lslocallanguage_codes = object.languageCodes;
        if (lslocallanguage_codes != null) {
            jsonGenerator.writeFieldName("language_codes");
            jsonGenerator.writeStartArray();
            for (String element1 : lslocallanguage_codes) {
                if (element1 != null) {
                    jsonGenerator.writeString(element1);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (object.manufacturer != null) {
            jsonGenerator.writeStringField("manufacturer", object.manufacturer);
        }
        if (object.orientation != null) {
            jsonGenerator.writeStringField("orientation", object.orientation);
        }
        if (object.os != null) {
            jsonGenerator.writeStringField("os", object.os);
        }
        if (object.osVersion != null) {
            jsonGenerator.writeStringField("os_version", object.osVersion);
        }
        if (object.otherAudioIsPlaying != null) {
            jsonGenerator.writeBooleanField("other_audio_is_playing", object.otherAudioIsPlaying.booleanValue());
        }
        if (object.radioDetails != null) {
            jsonGenerator.writeFieldName("radio_details");
            MobileRadioDetails$$JsonObjectMapper._serialize(object.radioDetails, jsonGenerator, true);
        }
        if (object.timezone != null) {
            jsonGenerator.writeStringField("timezone", object.timezone);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
