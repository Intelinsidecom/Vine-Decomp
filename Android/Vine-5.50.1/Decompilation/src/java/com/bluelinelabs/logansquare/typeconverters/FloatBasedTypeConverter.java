package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class FloatBasedTypeConverter<T> implements TypeConverter<T> {
    public abstract float convertToFloat(T t);

    public abstract T getFromFloat(float f);

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public T parse(JsonParser jsonParser) throws IOException {
        return getFromFloat((float) jsonParser.getValueAsDouble());
    }

    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public void serialize(T object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeNumberField(fieldName, convertToFloat(object));
    }
}
