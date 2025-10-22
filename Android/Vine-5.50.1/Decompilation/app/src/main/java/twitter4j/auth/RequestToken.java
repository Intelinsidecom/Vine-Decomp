package twitter4j.auth;

import java.io.Serializable;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.http.HttpResponse;

/* loaded from: classes.dex */
public final class RequestToken extends OAuthToken implements Serializable {
    private static final long serialVersionUID = -8214365845469757952L;
    private final Configuration conf;

    /* renamed from: oauth, reason: collision with root package name */
    private OAuthSupport f0oauth;

    @Override // twitter4j.auth.OAuthToken
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // twitter4j.auth.OAuthToken
    public /* bridge */ /* synthetic */ String getParameter(String str) {
        return super.getParameter(str);
    }

    @Override // twitter4j.auth.OAuthToken
    public /* bridge */ /* synthetic */ String getToken() {
        return super.getToken();
    }

    @Override // twitter4j.auth.OAuthToken
    public /* bridge */ /* synthetic */ String getTokenSecret() {
        return super.getTokenSecret();
    }

    @Override // twitter4j.auth.OAuthToken
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // twitter4j.auth.OAuthToken
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    RequestToken(HttpResponse res, OAuthSupport oauth2) throws TwitterException {
        super(res);
        this.conf = ConfigurationContext.getInstance();
        this.f0oauth = oauth2;
    }

    public RequestToken(String token, String tokenSecret) {
        super(token, tokenSecret);
        this.conf = ConfigurationContext.getInstance();
    }

    RequestToken(String token, String tokenSecret, OAuthSupport oauth2) {
        super(token, tokenSecret);
        this.conf = ConfigurationContext.getInstance();
        this.f0oauth = oauth2;
    }

    public String getAuthorizationURL() {
        return this.conf.getOAuthAuthorizationURL() + "?oauth_token=" + getToken();
    }

    public String getAuthenticationURL() {
        return this.conf.getOAuthAuthenticationURL() + "?oauth_token=" + getToken();
    }
}
