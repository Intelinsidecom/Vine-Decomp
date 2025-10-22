package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ApplicationState$$JsonObjectMapper extends JsonMapper<ApplicationState> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public ApplicationState parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static ApplicationState _parse(JsonParser jsonParser) throws IOException {
        ApplicationState instance = new ApplicationState();
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

    public static void parseField(ApplicationState applicationState, String str, JsonParser jsonParser) throws IOException {
        if ("ab_connected".equals(str)) {
            applicationState.abConnected = jsonParser.getValueAsBoolean();
            return;
        }
        if ("active_experiments".equals(str)) {
            applicationState.activeExperiments = ExperimentData$$JsonObjectMapper._parse(jsonParser);
            return;
        }
        if ("application_status".equals(str)) {
            applicationState.applicationStatus = jsonParser.getValueAsString(null);
            return;
        }
        if ("edition".equals(str)) {
            applicationState.edition = jsonParser.getValueAsString(null);
            return;
        }
        if ("fb_connected".equals(str)) {
            applicationState.facebookConnected = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("last_launch_timestamp".equals(str)) {
            applicationState.lastLaunchTimestamp = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Double.valueOf(jsonParser.getValueAsDouble()) : null;
            return;
        }
        if ("logged_in_user_id".equals(str)) {
            applicationState.loggedInUserId = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
            return;
        }
        if ("num_drafts".equals(str)) {
            applicationState.numDrafts = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
        } else if ("tw_connected".equals(str)) {
            applicationState.twitterConnected = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
        } else if ("video_cache_size".equals(str)) {
            applicationState.videoCacheSize = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(ApplicationState object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(ApplicationState object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeBooleanField("ab_connected", object.abConnected);
        if (object.activeExperiments != null) {
            jsonGenerator.writeFieldName("active_experiments");
            ExperimentData$$JsonObjectMapper._serialize(object.activeExperiments, jsonGenerator, true);
        }
        if (object.applicationStatus != null) {
            jsonGenerator.writeStringField("application_status", object.applicationStatus);
        }
        if (object.edition != null) {
            jsonGenerator.writeStringField("edition", object.edition);
        }
        if (object.facebookConnected != null) {
            jsonGenerator.writeBooleanField("fb_connected", object.facebookConnected.booleanValue());
        }
        if (object.lastLaunchTimestamp != null) {
            jsonGenerator.writeNumberField("last_launch_timestamp", object.lastLaunchTimestamp.doubleValue());
        }
        if (object.loggedInUserId != null) {
            jsonGenerator.writeNumberField("logged_in_user_id", object.loggedInUserId.longValue());
        }
        if (object.numDrafts != null) {
            jsonGenerator.writeNumberField("num_drafts", object.numDrafts.longValue());
        }
        if (object.twitterConnected != null) {
            jsonGenerator.writeBooleanField("tw_connected", object.twitterConnected.booleanValue());
        }
        if (object.videoCacheSize != null) {
            jsonGenerator.writeNumberField("video_cache_size", object.videoCacheSize.longValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
