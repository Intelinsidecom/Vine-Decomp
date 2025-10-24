package co.vine.android.api.response;

import co.vine.android.api.response.PagedDataResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

/* loaded from: classes.dex */
public final class PagedDataResponse$$JsonObjectMapper {
    protected static final PagedDataResponse.PreviousPageTypeConverter PREVIOUS_PAGE_TYPE_CONVERTER = new PagedDataResponse.PreviousPageTypeConverter();

    public static void parseField(PagedDataResponse instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("anchor".equals(fieldName)) {
            instance.anchor = jsonParser.getValueAsString(null);
            return;
        }
        if ("count".equals(fieldName)) {
            instance.count = jsonParser.getValueAsInt();
            return;
        }
        if ("nextPage".equals(fieldName)) {
            instance.nextPage = jsonParser.getValueAsInt();
        } else if ("previousPage".equals(fieldName)) {
            instance.previousPage = PREVIOUS_PAGE_TYPE_CONVERTER.parse(jsonParser).intValue();
        } else if ("size".equals(fieldName)) {
            instance.size = jsonParser.getValueAsInt();
        }
    }

    public static void _serialize(PagedDataResponse object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.anchor != null) {
            jsonGenerator.writeStringField("anchor", object.anchor);
        }
        jsonGenerator.writeNumberField("count", object.count);
        jsonGenerator.writeNumberField("nextPage", object.nextPage);
        PREVIOUS_PAGE_TYPE_CONVERTER.serialize(Integer.valueOf(object.previousPage), "previousPage", true, jsonGenerator);
        jsonGenerator.writeNumberField("size", object.size);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
