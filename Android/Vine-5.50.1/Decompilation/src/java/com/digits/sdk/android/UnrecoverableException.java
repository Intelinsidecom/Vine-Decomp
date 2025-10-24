package com.digits.sdk.android;

/* loaded from: classes.dex */
public class UnrecoverableException extends DigitsException {
    public UnrecoverableException(String message) {
        super(message);
    }

    public UnrecoverableException(String message, int error, AuthConfig config) {
        super(message, error, config);
    }
}
