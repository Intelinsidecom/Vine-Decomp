package io.realm.internal;

@Keep
/* loaded from: classes.dex */
public class OutOfMemoryError extends Error {
    public OutOfMemoryError() {
    }

    public OutOfMemoryError(String message) {
        super(message);
    }

    public OutOfMemoryError(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfMemoryError(Throwable cause) {
        super(cause);
    }
}
