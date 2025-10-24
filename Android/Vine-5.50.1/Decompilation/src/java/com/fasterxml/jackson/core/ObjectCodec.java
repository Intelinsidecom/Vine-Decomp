package com.fasterxml.jackson.core;

import java.io.IOException;

/* loaded from: classes2.dex */
public abstract class ObjectCodec extends TreeCodec {
    public abstract void writeValue(JsonGenerator jsonGenerator, Object obj) throws IOException;
}
