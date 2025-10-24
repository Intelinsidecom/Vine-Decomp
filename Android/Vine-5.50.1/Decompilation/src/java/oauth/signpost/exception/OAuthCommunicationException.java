package oauth.signpost.exception;

/* loaded from: classes.dex */
public class OAuthCommunicationException extends OAuthException {
    private String responseBody;

    public OAuthCommunicationException(Exception cause) {
        super("Communication with the service provider failed: " + cause.getLocalizedMessage(), cause);
    }
}
