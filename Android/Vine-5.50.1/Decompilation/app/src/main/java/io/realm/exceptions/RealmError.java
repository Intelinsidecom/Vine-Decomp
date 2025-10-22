package io.realm.exceptions;

import io.realm.internal.Keep;

@Keep
/* loaded from: classes.dex */
public class RealmError extends Error {
    public RealmError(String detailMessage) {
        super(detailMessage);
    }
}
