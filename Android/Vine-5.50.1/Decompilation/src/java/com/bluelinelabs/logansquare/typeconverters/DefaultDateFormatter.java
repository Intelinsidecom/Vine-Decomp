package com.bluelinelabs.logansquare.typeconverters;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class DefaultDateFormatter extends SimpleDateFormat {
    public DefaultDateFormatter() {
        super("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private String getFixedInputString(String input) {
        if (input != null) {
            return input.replaceAll("Z$", "+0000");
        }
        return null;
    }

    @Override // java.text.DateFormat
    public Date parse(String string) throws ParseException {
        return super.parse(getFixedInputString(string));
    }

    @Override // java.text.DateFormat, java.text.Format
    public Object parseObject(String string, ParsePosition position) {
        return super.parseObject(getFixedInputString(string), position);
    }

    @Override // java.text.Format
    public Object parseObject(String string) throws ParseException {
        return super.parseObject(getFixedInputString(string));
    }

    @Override // java.text.SimpleDateFormat, java.text.DateFormat
    public Date parse(String string, ParsePosition position) {
        return super.parse(getFixedInputString(string), position);
    }
}
