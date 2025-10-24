package twitter4j.internal.http;

import java.io.Serializable;
import twitter4j.auth.Authorization;
import twitter4j.auth.BasicAuthorization;

/* loaded from: classes.dex */
public class XAuthAuthorization implements Serializable, Authorization {
    private static final long serialVersionUID = -6082451214083464902L;
    private BasicAuthorization basic;
    private String consumerKey;
    private String consumerSecret;

    public XAuthAuthorization(BasicAuthorization basic) {
        this.basic = basic;
    }

    @Override // twitter4j.auth.Authorization
    public String getAuthorizationHeader(HttpRequest req) {
        return this.basic.getAuthorizationHeader(req);
    }

    public String getUserId() {
        return this.basic.getUserId();
    }

    public String getPassword() {
        return this.basic.getPassword();
    }

    public String getConsumerKey() {
        return this.consumerKey;
    }

    public String getConsumerSecret() {
        return this.consumerSecret;
    }

    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    @Override // twitter4j.auth.Authorization
    public boolean isEnabled() {
        return this.basic.isEnabled();
    }
}
