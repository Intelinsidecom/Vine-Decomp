package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

/* loaded from: classes.dex */
public abstract class CalendarTypeConverter implements TypeConverter<Calendar> {
    private final ThreadLocal<DateFormat> mDateFormat = new ThreadLocal<DateFormat>() { // from class: com.bluelinelabs.logansquare.typeconverters.CalendarTypeConverter.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public DateFormat initialValue() {
            return CalendarTypeConverter.this.getDateFormat();
        }
    };

    public abstract DateFormat getDateFormat();

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public Calendar parse(JsonParser jsonParser) throws IOException {
        String dateString = jsonParser.getValueAsString(null);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.mDateFormat.get().parse(dateString));
            return calendar;
        } catch (ParseException e) {
            return null;
        }
    }

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public void serialize(Calendar object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStringField(fieldName, this.mDateFormat.get().format(Long.valueOf(object.getTimeInMillis())));
    }
}
