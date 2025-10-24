package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class LongBasedTypeConverter<T> implements TypeConverter<T> {
    public abstract long convertToLong(T t);

    public abstract T getFromLong(long j);

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public T parse(JsonParser jsonParser) throws IOException {
        return getFromLong(jsonParser.getValueAsLong());
    }

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public void serialize(T object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeNumberField(fieldName, convertToLong(object));
    }
}
