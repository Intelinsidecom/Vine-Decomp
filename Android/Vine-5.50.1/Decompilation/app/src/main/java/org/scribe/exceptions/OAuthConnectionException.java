package org.scribe.exceptions;

/* loaded from: classes.dex */
public class OAuthConnectionException extends OAuthException {
    public OAuthConnectionException(Exception e) {
        super("There was a problem while creating a connection to the remote service.", e);
    }
}
