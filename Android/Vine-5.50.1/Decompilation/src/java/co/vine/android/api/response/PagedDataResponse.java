package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

@JsonObject
/* loaded from: classes.dex */
public abstract class PagedDataResponse {

    @JsonField(name = {"anchor"})
    public String anchor;

    @JsonField(name = {"count"})
    public int count;

    @JsonField(name = {"nextPage"})
    public int nextPage;

    @JsonField(name = {"previousPage"}, typeConverter = PreviousPageTypeConverter.class)
    public int previousPage = -1;

    @JsonField(name = {"size"})
    public int size;

    public static final class PreviousPageTypeConverter extends ParsingTypeConverter<Integer> {
        @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
        public Integer parse(JsonParser jsonParser) throws IOException {
            String json = jsonParser.getValueAsString();
            return Integer.valueOf(json == null ? -1 : Integer.valueOf(json).intValue());
        }
    }
}
