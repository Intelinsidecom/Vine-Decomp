package io.realm.internal.async;

import io.realm.exceptions.RealmException;
import io.realm.internal.Keep;

@Keep
/* loaded from: classes.dex */
public class BadVersionException extends RealmException {
    public BadVersionException(String detailMessage) {
        super(detailMessage);
    }

    public BadVersionException(String detailMessage, Throwable exception) {
        super(detailMessage, exception);
    }
}
