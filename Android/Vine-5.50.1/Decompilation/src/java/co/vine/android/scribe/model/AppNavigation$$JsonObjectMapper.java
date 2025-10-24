package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* loaded from: classes.dex */
public final class AppNavigation$$JsonObjectMapper extends JsonMapper<AppNavigation> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public AppNavigation parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static AppNavigation _parse(JsonParser jsonParser) throws IOException {
        AppNavigation instance = new AppNavigation();
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

    public static void parseField(AppNavigation instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("capture_source_section".equals(fieldName)) {
            instance.captureSourceSection = jsonParser.getValueAsString(null);
            return;
        }
        if ("filtering".equals(fieldName)) {
            instance.filtering = jsonParser.getValueAsString(null);
            return;
        }
        if ("new_search_view".equals(fieldName)) {
            instance.isNewSearchView = jsonParser.getValueAsBoolean();
            return;
        }
        if ("search_query".equals(fieldName)) {
            instance.searchQuery = jsonParser.getValueAsString(null);
            return;
        }
        if ("section".equals(fieldName)) {
            instance.section = jsonParser.getValueAsString(null);
            return;
        }
        if ("subview".equals(fieldName)) {
            instance.subview = jsonParser.getValueAsString(null);
            return;
        }
        if ("timeline_api_url".equals(fieldName)) {
            instance.timelineApiUrl = jsonParser.getValueAsString(null);
        } else if ("ui_element".equals(fieldName)) {
            instance.ui_element = jsonParser.getValueAsString(null);
        } else if ("view".equals(fieldName)) {
            instance.view = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(AppNavigation object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(AppNavigation object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.captureSourceSection != null) {
            jsonGenerator.writeStringField("capture_source_section", object.captureSourceSection);
        }
        if (object.filtering != null) {
            jsonGenerator.writeStringField("filtering", object.filtering);
        }
        jsonGenerator.writeBooleanField("new_search_view", object.isNewSearchView);
        if (object.searchQuery != null) {
            jsonGenerator.writeStringField("search_query", object.searchQuery);
        }
        if (object.section != null) {
            jsonGenerator.writeStringField("section", object.section);
        }
        if (object.subview != null) {
            jsonGenerator.writeStringField("subview", object.subview);
        }
        if (object.timelineApiUrl != null) {
            jsonGenerator.writeStringField("timeline_api_url", object.timelineApiUrl);
        }
        if (object.ui_element != null) {
            jsonGenerator.writeStringField("ui_element", object.ui_element);
        }
        if (object.view != null) {
            jsonGenerator.writeStringField("view", object.view);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
