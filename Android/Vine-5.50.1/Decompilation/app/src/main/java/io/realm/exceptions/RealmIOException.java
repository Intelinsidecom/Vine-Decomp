package io.realm.exceptions;

import io.realm.internal.Keep;

@Keep
/* loaded from: classes.dex */
public class RealmIOException extends RuntimeException {
    public RealmIOException(Throwable cause) {
        super(cause);
    }

    public RealmIOException() {
    }

    public RealmIOException(String message) {
        super(message);
    }

    public RealmIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
