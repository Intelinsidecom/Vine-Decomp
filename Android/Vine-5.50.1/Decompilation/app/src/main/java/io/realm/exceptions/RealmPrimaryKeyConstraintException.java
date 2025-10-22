package io.realm.exceptions;

import io.realm.internal.Keep;

@Keep
/* loaded from: classes.dex */
public class RealmPrimaryKeyConstraintException extends RuntimeException {
    public RealmPrimaryKeyConstraintException(String message) {
        super(message);
    }
}
