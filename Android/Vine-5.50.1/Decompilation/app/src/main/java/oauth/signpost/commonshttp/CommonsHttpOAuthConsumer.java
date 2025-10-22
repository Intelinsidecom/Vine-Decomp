package oauth.signpost.commonshttp;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;

/* loaded from: classes.dex */
public class CommonsHttpOAuthConsumer extends AbstractOAuthConsumer {
    private static final long serialVersionUID = 1;

    public CommonsHttpOAuthConsumer(String consumerKey, String consumerSecret) {
        super(consumerKey, consumerSecret);
    }

    @Override // oauth.signpost.AbstractOAuthConsumer
    protected HttpRequest wrap(Object request) {
        if (!(request instanceof org.apache.http.HttpRequest)) {
            throw new IllegalArgumentException("This consumer expects requests of type " + org.apache.http.HttpRequest.class.getCanonicalName());
        }
        return new HttpRequestAdapter((HttpUriRequest) request);
    }
}
