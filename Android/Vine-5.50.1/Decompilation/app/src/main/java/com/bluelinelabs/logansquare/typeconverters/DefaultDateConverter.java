package com.bluelinelabs.logansquare.typeconverters;

import java.text.DateFormat;

/* loaded from: classes.dex */
public class DefaultDateConverter extends DateTypeConverter {
    private DateFormat mDateFormat = new DefaultDateFormatter();

    @Override // com.bluelinelabs.logansquare.typeconverters.DateTypeConverter
    public DateFormat getDateFormat() {
        return this.mDateFormat;
    }
}
