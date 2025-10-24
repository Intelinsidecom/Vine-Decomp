package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/* loaded from: classes.dex */
public abstract class DateTypeConverter implements TypeConverter<Date> {
    private final ThreadLocal<DateFormat> mDateFormat = new ThreadLocal<DateFormat>() { // from class: com.bluelinelabs.logansquare.typeconverters.DateTypeConverter.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public DateFormat initialValue() {
            return DateTypeConverter.this.getDateFormat();
        }
    };

    public abstract DateFormat getDateFormat();

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public Date parse(JsonParser jsonParser) throws IOException {
        String dateString = jsonParser.getValueAsString(null);
        if (dateString == null) {
            return null;
        }
        try {
            return this.mDateFormat.get().parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public void serialize(Date object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (object != null) {
            jsonGenerator.writeStringField(fieldName, this.mDateFormat.get().format(object));
        } else {
            jsonGenerator.writeFieldName(fieldName);
            jsonGenerator.writeNull();
        }
    }
}
