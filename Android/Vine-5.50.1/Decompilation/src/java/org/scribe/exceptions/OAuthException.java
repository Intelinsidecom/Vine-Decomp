package org.scribe.exceptions;

/* loaded from: classes.dex */
public class OAuthException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public OAuthException(String message, Exception e) {
        super(message, e);
    }

    public OAuthException(String message) {
        super(message, null);
    }
}
