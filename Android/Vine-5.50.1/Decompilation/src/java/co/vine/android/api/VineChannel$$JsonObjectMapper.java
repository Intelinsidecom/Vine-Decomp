package co.vine.android.api;

import co.vine.android.api.VineChannel;
import co.vine.android.api.response.DateStringToMilliseconds;
import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineChannel$$JsonObjectMapper extends JsonMapper<VineChannel> {
    protected static final DateStringToMilliseconds DATE_STRING_TO_MILLISECONDS = new DateStringToMilliseconds();
    protected static final VineChannel.ColorTypeConverter COLOR_TYPE_CONVERTER = new VineChannel.ColorTypeConverter();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineChannel parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineChannel _parse(JsonParser jsonParser) throws IOException {
        VineChannel instance = new VineChannel();
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

    public static void parseField(VineChannel instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("backgroundColor".equals(fieldName)) {
            instance.backgroundColor = COLOR_TYPE_CONVERTER.parse(jsonParser);
            return;
        }
        if ("channel".equals(fieldName)) {
            instance.channel = jsonParser.getValueAsString(null);
            return;
        }
        if ("channelId".equals(fieldName)) {
            instance.channelId = jsonParser.getValueAsLong();
            return;
        }
        if ("created".equals(fieldName)) {
            instance.created = DATE_STRING_TO_MILLISECONDS.parse(jsonParser).longValue();
            return;
        }
        if ("description".equals(fieldName)) {
            instance.description = jsonParser.getValueAsString(null);
            return;
        }
        if ("exploreRetinaIconFullUrl".equals(fieldName)) {
            instance.exploreRetinaIconFullUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("following".equals(fieldName)) {
            instance.following = jsonParser.getValueAsBoolean();
            return;
        }
        if ("fontColor".equals(fieldName)) {
            instance.fontColor = COLOR_TYPE_CONVERTER.parse(jsonParser);
            return;
        }
        if ("iconFullUrl".equals(fieldName)) {
            instance.iconFullUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("retinaIconFullUrl".equals(fieldName)) {
            instance.retinaIconFullUrl = jsonParser.getValueAsString(null);
            return;
        }
        if ("secondaryColor".equals(fieldName)) {
            instance.secondaryColor = COLOR_TYPE_CONVERTER.parse(jsonParser);
        } else if ("showRecent".equals(fieldName)) {
            instance.showRecent = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
        } else if ("timeline".equals(fieldName)) {
            instance.timeline = VineChannel$TimeLine$$JsonObjectMapper._parse(jsonParser);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineChannel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineChannel object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        COLOR_TYPE_CONVERTER.serialize(object.backgroundColor, "backgroundColor", true, jsonGenerator);
        if (object.channel != null) {
            jsonGenerator.writeStringField("channel", object.channel);
        }
        jsonGenerator.writeNumberField("channelId", object.channelId);
        DATE_STRING_TO_MILLISECONDS.serialize(Long.valueOf(object.created), "created", true, jsonGenerator);
        if (object.description != null) {
            jsonGenerator.writeStringField("description", object.description);
        }
        if (object.exploreRetinaIconFullUrl != null) {
            jsonGenerator.writeStringField("exploreRetinaIconFullUrl", object.exploreRetinaIconFullUrl);
        }
        jsonGenerator.writeBooleanField("following", object.following);
        COLOR_TYPE_CONVERTER.serialize(object.fontColor, "fontColor", true, jsonGenerator);
        if (object.iconFullUrl != null) {
            jsonGenerator.writeStringField("iconFullUrl", object.iconFullUrl);
        }
        if (object.retinaIconFullUrl != null) {
            jsonGenerator.writeStringField("retinaIconFullUrl", object.retinaIconFullUrl);
        }
        COLOR_TYPE_CONVERTER.serialize(object.secondaryColor, "secondaryColor", true, jsonGenerator);
        if (object.showRecent != null) {
            jsonGenerator.writeBooleanField("showRecent", object.showRecent.booleanValue());
        }
        if (object.timeline != null) {
            jsonGenerator.writeFieldName("timeline");
            VineChannel$TimeLine$$JsonObjectMapper._serialize(object.timeline, jsonGenerator, true);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
