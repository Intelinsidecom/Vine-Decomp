package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class StringBasedTypeConverter<T> implements TypeConverter<T> {
    public abstract String convertToString(T t);

    public abstract T getFromString(String str);

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public T parse(JsonParser jsonParser) throws IOException {
        return getFromString(jsonParser.getValueAsString(null));
    }

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public void serialize(T object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStringField(fieldName, convertToString(object));
    }
}
