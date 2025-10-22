package org.scribe.exceptions;

/* loaded from: classes.dex */
public class OAuthSignatureException extends OAuthException {
    private static final long serialVersionUID = 1;

    public OAuthSignatureException(String stringToSign, Exception e) {
        super(String.format("Error while signing string: %s", stringToSign), e);
    }
}
