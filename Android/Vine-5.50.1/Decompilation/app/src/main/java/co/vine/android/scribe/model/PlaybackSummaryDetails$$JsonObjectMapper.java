package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class PlaybackSummaryDetails$$JsonObjectMapper extends JsonMapper<PlaybackSummaryDetails> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public PlaybackSummaryDetails parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static PlaybackSummaryDetails _parse(JsonParser jsonParser) throws IOException {
        PlaybackSummaryDetails instance = new PlaybackSummaryDetails();
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

    public static void parseField(PlaybackSummaryDetails playbackSummaryDetails, String str, JsonParser jsonParser) throws IOException {
        if ("playback_interruptions".equals(str)) {
            playbackSummaryDetails.playbackInterruptions = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("time_spent_buffering".equals(str)) {
            playbackSummaryDetails.timeSpentBuffering = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? new Float(jsonParser.getValueAsDouble()) : null;
            return;
        }
        if ("time_spent_paused".equals(str)) {
            playbackSummaryDetails.timeSpentPaused = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? new Float(jsonParser.getValueAsDouble()) : null;
            return;
        }
        if ("time_spent_playing".equals(str)) {
            playbackSummaryDetails.timeSpentPlaying = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? new Float(jsonParser.getValueAsDouble()) : null;
        } else if ("video_end_time".equals(str)) {
            playbackSummaryDetails.videoEndTime = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? new Float(jsonParser.getValueAsDouble()) : null;
        } else if ("video_start_time".equals(str)) {
            playbackSummaryDetails.videoStarttime = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? new Float(jsonParser.getValueAsDouble()) : null;
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(PlaybackSummaryDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(PlaybackSummaryDetails object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.playbackInterruptions != null) {
            jsonGenerator.writeNumberField("playback_interruptions", object.playbackInterruptions.intValue());
        }
        if (object.timeSpentBuffering != null) {
            jsonGenerator.writeNumberField("time_spent_buffering", object.timeSpentBuffering.floatValue());
        }
        if (object.timeSpentPaused != null) {
            jsonGenerator.writeNumberField("time_spent_paused", object.timeSpentPaused.floatValue());
        }
        if (object.timeSpentPlaying != null) {
            jsonGenerator.writeNumberField("time_spent_playing", object.timeSpentPlaying.floatValue());
        }
        if (object.videoEndTime != null) {
            jsonGenerator.writeNumberField("video_end_time", object.videoEndTime.floatValue());
        }
        if (object.videoStarttime != null) {
            jsonGenerator.writeNumberField("video_start_time", object.videoStarttime.floatValue());
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
