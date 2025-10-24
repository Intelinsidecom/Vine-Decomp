package com.bluelinelabs.logansquare;

/* loaded from: classes.dex */
public class NoSuchMapperException extends RuntimeException {
    public NoSuchMapperException(Class cls, Exception e) {
        super("Class " + cls.getCanonicalName() + " could not be mapped to a JSON object. Perhaps it hasn't been annotated with @JsonObject?", e);
    }
}
