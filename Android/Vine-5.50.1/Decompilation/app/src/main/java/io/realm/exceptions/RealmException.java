package io.realm.exceptions;

import io.realm.internal.Keep;

@Keep
/* loaded from: classes.dex */
public class RealmException extends RuntimeException {
    public RealmException(String detailMessage) {
        super(detailMessage);
    }

    public RealmException(String detailMessage, Throwable exception) {
        super(detailMessage, exception);
    }
}
