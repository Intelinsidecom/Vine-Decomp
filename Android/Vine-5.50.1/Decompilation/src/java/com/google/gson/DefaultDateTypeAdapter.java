package com.google.gson;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes.dex */
final class DefaultDateTypeAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private final DateFormat enUsFormat;
    private final DateFormat iso8601Format;
    private final DateFormat localFormat;

    DefaultDateTypeAdapter() {
        this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
    }

    DefaultDateTypeAdapter(String datePattern) {
        this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
    }

    public DefaultDateTypeAdapter(int dateStyle, int timeStyle) {
        this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), DateFormat.getDateTimeInstance(dateStyle, timeStyle));
    }

    DefaultDateTypeAdapter(DateFormat enUsFormat, DateFormat localFormat) {
        this.enUsFormat = enUsFormat;
        this.localFormat = localFormat;
        this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        this.iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        JsonPrimitive jsonPrimitive;
        synchronized (this.localFormat) {
            String dateFormatAsString = this.enUsFormat.format(src);
            jsonPrimitive = new JsonPrimitive(dateFormatAsString);
        }
        return jsonPrimitive;
    }

    @Override // com.google.gson.JsonDeserializer
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        Date date = deserializeToDate(json);
        if (typeOfT != Date.class) {
            if (typeOfT == Timestamp.class) {
                return new Timestamp(date.getTime());
            }
            if (typeOfT == java.sql.Date.class) {
                return new java.sql.Date(date.getTime());
            }
            throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
        }
        return date;
    }

    private Date deserializeToDate(JsonElement json) {
        Date date;
        synchronized (this.localFormat) {
            try {
                date = this.localFormat.parse(json.getAsString());
            } catch (ParseException e) {
                try {
                    date = this.enUsFormat.parse(json.getAsString());
                } catch (ParseException e2) {
                    try {
                        date = this.iso8601Format.parse(json.getAsString());
                    } catch (ParseException e3) {
                        throw new JsonSyntaxException(json.getAsString(), e3);
                    }
                }
            }
        }
        return date;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DefaultDateTypeAdapter.class.getSimpleName());
        sb.append('(').append(this.localFormat.getClass().getSimpleName()).append(')');
        return sb.toString();
    }
}
