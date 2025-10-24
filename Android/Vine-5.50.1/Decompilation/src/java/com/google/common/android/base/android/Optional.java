package com.google.common.android.base.android;

import java.io.Serializable;

/* loaded from: classes.dex */
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID = 0;

    public abstract T get();

    public abstract boolean isPresent();

    public abstract T orNull();

    public static <T> Optional<T> absent() {
        return Absent.withType();
    }

    public static <T> Optional<T> of(T reference) {
        return new Present(Preconditions.checkNotNull(reference));
    }

    public static <T> Optional<T> fromNullable(T nullableReference) {
        return nullableReference == null ? absent() : new Present(nullableReference);
    }

    Optional() {
    }
}
