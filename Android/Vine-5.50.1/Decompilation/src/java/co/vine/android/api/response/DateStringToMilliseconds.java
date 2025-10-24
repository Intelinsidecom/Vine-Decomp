package co.vine.android.api.response;

import co.vine.android.util.DateTimeUtil;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

/* loaded from: classes.dex */
public final class DateStringToMilliseconds extends ParsingTypeConverter<Long> {
    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public Long parse(JsonParser jsonParser) throws IOException {
        String date = jsonParser.getValueAsString();
        if (date == null) {
            return 0L;
        }
        return Long.valueOf(DateTimeUtil.getTimeInMsFromString(date, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
    }
}
