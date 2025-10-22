package co.vine.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class SynchronizedDateFormat extends SimpleDateFormat {
    private static final long serialVersionUID = 6612933786679648650L;

    public SynchronizedDateFormat() {
    }

    public SynchronizedDateFormat(String pattern, Locale locale) {
        super(pattern, locale);
    }

    @Override // java.text.DateFormat
    public Date parse(String s) throws ParseException {
        Date date;
        synchronized (this) {
            date = super.parse(s);
        }
        return date;
    }

    @Override // java.text.SimpleDateFormat
    public void applyPattern(String s) {
        synchronized (this) {
            super.applyPattern(s);
        }
    }
}
