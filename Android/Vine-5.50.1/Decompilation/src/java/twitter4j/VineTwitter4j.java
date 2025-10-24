package twitter4j;

import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.BasicAuthorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.OAuthSupport;
import twitter4j.auth.RequestToken;
import twitter4j.auth.VineAuthorizationFactory;
import twitter4j.auth.VineOauthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponseEvent;
import twitter4j.internal.http.XAuthAuthorization;

/* loaded from: classes.dex */
public class VineTwitter4j extends TwitterImpl {
    @Override // twitter4j.TwitterImpl, twitter4j.TwitterBase
    public /* bridge */ /* synthetic */ void createFriendship(long j) throws TwitterException {
        super.createFriendship(j);
    }

    @Override // twitter4j.TwitterImpl, twitter4j.TwitterBase
    public /* bridge */ /* synthetic */ void createFriendship(String str) throws TwitterException {
        super.createFriendship(str);
    }

    @Override // twitter4j.TwitterBaseImpl
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.TwitterBase
    public /* bridge */ /* synthetic */ Configuration getConfiguration() {
        return super.getConfiguration();
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public /* bridge */ /* synthetic */ RequestToken getOAuthRequestToken() throws TwitterException {
        return super.getOAuthRequestToken();
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public /* bridge */ /* synthetic */ RequestToken getOAuthRequestToken(String str) throws TwitterException {
        return super.getOAuthRequestToken(str);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public /* bridge */ /* synthetic */ RequestToken getOAuthRequestToken(String str, String str2) throws TwitterException {
        return super.getOAuthRequestToken(str, str2);
    }

    @Override // twitter4j.TwitterBaseImpl
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public /* bridge */ /* synthetic */ void setOAuthAccessToken(AccessToken accessToken) {
        super.setOAuthAccessToken(accessToken);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public /* bridge */ /* synthetic */ void setOAuthConsumer(String str, String str2) {
        super.setOAuthConsumer(str, str2);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.TwitterBase
    public /* bridge */ /* synthetic */ void shutdown() {
        super.shutdown();
    }

    @Override // twitter4j.TwitterImpl, twitter4j.TwitterBaseImpl
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    VineTwitter4j(Configuration conf, Authorization auth) {
        super(conf, auth);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken() throws TwitterException {
        AccessToken oauthAccessToken;
        Authorization auth = getAuthorization();
        if (auth instanceof BasicAuthorization) {
            BasicAuthorization basicAuth = (BasicAuthorization) auth;
            Authorization auth2 = VineAuthorizationFactory.getInstance(this.conf);
            if (auth2 instanceof VineOauthAuthorization) {
                this.auth = auth2;
                oauthAccessToken = ((VineOauthAuthorization) auth2).getOAuthAccessToken(basicAuth.getUserId(), basicAuth.getPassword());
            } else {
                throw new IllegalStateException("consumer key / secret combination not supplied.");
            }
        } else if (auth instanceof XAuthAuthorization) {
            XAuthAuthorization xauth = (XAuthAuthorization) auth;
            this.auth = xauth;
            OAuthAuthorization oauthAuth = new OAuthAuthorization(this.conf);
            oauthAuth.setOAuthConsumer(xauth.getConsumerKey(), xauth.getConsumerSecret());
            oauthAccessToken = oauthAuth.getOAuthAccessToken(xauth.getUserId(), xauth.getPassword());
        } else {
            oauthAccessToken = getOAuth().getOAuthAccessToken();
        }
        this.screenName = oauthAccessToken.getScreenName();
        this.id = oauthAccessToken.getUserId();
        return oauthAccessToken;
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        return super.getOAuthAccessToken(oauthVerifier);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        return super.getOAuthAccessToken(requestToken);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return super.getOAuthAccessToken(requestToken, oauthVerifier);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        return super.getOAuthAccessToken(screenName, password);
    }

    private OAuthSupport getOAuth() {
        if (!(this.auth instanceof OAuthSupport)) {
            throw new IllegalStateException("OAuth consumer key/secret combination not supplied");
        }
        return (OAuthSupport) this.auth;
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.internal.http.HttpResponseListener
    public void httpResponseReceived(HttpResponseEvent event) {
    }
}
