package co.vine.android.api.response;

import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class ParsingTypeConverter<T> implements TypeConverter<T> {
    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public void serialize(T object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        throw new UnsupportedOperationException();
    }
}
