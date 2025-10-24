package twitter4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.auth.BasicAuthorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.OAuthSupport;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpResponseEvent;
import twitter4j.internal.http.HttpResponseListener;
import twitter4j.internal.http.XAuthAuthorization;
import twitter4j.internal.json.z_T4JInternalFactory;
import twitter4j.internal.json.z_T4JInternalJSONImplFactory;

/* loaded from: classes.dex */
abstract class TwitterBaseImpl implements Serializable, TwitterBase, OAuthSupport, HttpResponseListener {
    private static final long serialVersionUID = -3812176145960812140L;
    protected Authorization auth;
    protected Configuration conf;
    protected z_T4JInternalFactory factory;
    protected transient HttpClientWrapper http;
    protected transient String screenName = null;
    protected transient long id = 0;

    TwitterBaseImpl(Configuration conf, Authorization auth) {
        this.conf = conf;
        this.auth = auth;
        init();
    }

    private void init() {
        if (this.auth == null) {
            String consumerKey = this.conf.getOAuthConsumerKey();
            String consumerSecret = this.conf.getOAuthConsumerSecret();
            if (consumerKey != null && consumerSecret != null && !this.conf.isApplicationOnlyAuthEnabled()) {
                OAuthAuthorization oauth2 = new OAuthAuthorization(this.conf);
                String accessToken = this.conf.getOAuthAccessToken();
                String accessTokenSecret = this.conf.getOAuthAccessTokenSecret();
                if (accessToken != null && accessTokenSecret != null) {
                    oauth2.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                this.auth = oauth2;
            }
        }
        this.http = new HttpClientWrapper(this.conf);
        this.http.setHttpResponseListener(this);
        setFactory();
    }

    protected void setFactory() {
        this.factory = new z_T4JInternalJSONImplFactory(this.conf);
    }

    @Override // twitter4j.internal.http.HttpResponseListener
    public void httpResponseReceived(HttpResponseEvent event) {
    }

    @Override // twitter4j.TwitterBase
    public final Authorization getAuthorization() {
        return this.auth;
    }

    @Override // twitter4j.TwitterBase
    public Configuration getConfiguration() {
        return this.conf;
    }

    @Override // twitter4j.TwitterBase
    public void shutdown() {
        if (this.http != null) {
            this.http.shutdown();
        }
    }

    protected final void ensureAuthorizationEnabled() {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for the detail.");
        }
    }

    protected final void ensureOAuthEnabled() {
        if (!(this.auth instanceof OAuthAuthorization)) {
            throw new IllegalStateException("OAuth required. Authentication credentials are missing. See http://twitter4j.org/en/configuration.html for the detail.");
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.putFields();
        out.writeFields();
        out.writeObject(this.conf);
        out.writeObject(this.auth);
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        stream.readFields();
        this.conf = (Configuration) stream.readObject();
        this.auth = (Authorization) stream.readObject();
        this.http = new HttpClientWrapper(this.conf);
        this.http.setHttpResponseListener(this);
        setFactory();
    }

    @Override // twitter4j.auth.OAuthSupport
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret) {
        if (consumerKey == null) {
            throw new NullPointerException("consumer key is null");
        }
        if (consumerSecret == null) {
            throw new NullPointerException("consumer secret is null");
        }
        if (this.auth == null) {
            if (!this.conf.isApplicationOnlyAuthEnabled()) {
                OAuthAuthorization oauth2 = new OAuthAuthorization(this.conf);
                oauth2.setOAuthConsumer(consumerKey, consumerSecret);
                this.auth = oauth2;
            }
        } else if (this.auth instanceof BasicAuthorization) {
            XAuthAuthorization xauth = new XAuthAuthorization((BasicAuthorization) this.auth);
            xauth.setOAuthConsumer(consumerKey, consumerSecret);
            this.auth = xauth;
        } else if (this.auth instanceof OAuthAuthorization) {
            throw new IllegalStateException("consumer key/secret pair already set.");
        }
    }

    @Override // twitter4j.auth.OAuthSupport
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null);
    }

    @Override // twitter4j.auth.OAuthSupport
    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl);
    }

    @Override // twitter4j.auth.OAuthSupport
    public RequestToken getOAuthRequestToken(String callbackUrl, String xAuthAccessType) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl, xAuthAccessType);
    }

    @Override // twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken() throws TwitterException {
        AccessToken oauthAccessToken;
        Authorization auth = getAuthorization();
        if (auth instanceof BasicAuthorization) {
            BasicAuthorization basicAuth = (BasicAuthorization) auth;
            Authorization auth2 = AuthorizationFactory.getInstance(this.conf);
            if (auth2 instanceof OAuthAuthorization) {
                this.auth = auth2;
                oauthAccessToken = ((OAuthAuthorization) auth2).getOAuthAccessToken(basicAuth.getUserId(), basicAuth.getPassword());
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

    @Override // twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        AccessToken oauthAccessToken;
        oauthAccessToken = getOAuth().getOAuthAccessToken(oauthVerifier);
        this.screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    @Override // twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        AccessToken oauthAccessToken;
        OAuthSupport oauth2 = getOAuth();
        oauthAccessToken = oauth2.getOAuthAccessToken(requestToken);
        this.screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    @Override // twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return getOAuth().getOAuthAccessToken(requestToken, oauthVerifier);
    }

    @Override // twitter4j.auth.OAuthSupport
    public synchronized void setOAuthAccessToken(AccessToken accessToken) {
        getOAuth().setOAuthAccessToken(accessToken);
    }

    @Override // twitter4j.auth.OAuthSupport
    public synchronized AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        return getOAuth().getOAuthAccessToken(screenName, password);
    }

    private OAuthSupport getOAuth() {
        if (!(this.auth instanceof OAuthSupport)) {
            throw new IllegalStateException("OAuth consumer key/secret combination not supplied");
        }
        return (OAuthSupport) this.auth;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwitterBaseImpl)) {
            return false;
        }
        TwitterBaseImpl that = (TwitterBaseImpl) o;
        if (this.auth == null ? that.auth != null : !this.auth.equals(that.auth)) {
            return false;
        }
        if (!this.conf.equals(that.conf)) {
            return false;
        }
        if (this.http != null) {
            if (this.http.equals(that.http)) {
                return true;
            }
        } else if (that.http == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.conf.hashCode();
        return (((result * 31) + (this.http != null ? this.http.hashCode() : 0)) * 31) + (this.auth != null ? this.auth.hashCode() : 0);
    }

    public String toString() {
        return "TwitterBase{conf=" + this.conf + ", http=" + this.http + ", auth=" + this.auth + '}';
    }
}
