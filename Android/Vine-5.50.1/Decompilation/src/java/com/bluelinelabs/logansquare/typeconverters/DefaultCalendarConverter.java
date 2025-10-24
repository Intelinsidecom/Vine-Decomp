package com.bluelinelabs.logansquare.typeconverters;

import java.text.DateFormat;

/* loaded from: classes.dex */
public class DefaultCalendarConverter extends CalendarTypeConverter {
    private DateFormat mDateFormat = new DefaultDateFormatter();

    @Override // com.bluelinelabs.logansquare.typeconverters.CalendarTypeConverter
    public DateFormat getDateFormat() {
        return this.mDateFormat;
    }
}
