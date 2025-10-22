package com.bluelinelabs.logansquare.typeconverters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

/* loaded from: classes.dex */
public interface TypeConverter<T> {
    T parse(JsonParser jsonParser) throws IOException;

    void serialize(T t, String str, boolean z, JsonGenerator jsonGenerator) throws IOException;
}
