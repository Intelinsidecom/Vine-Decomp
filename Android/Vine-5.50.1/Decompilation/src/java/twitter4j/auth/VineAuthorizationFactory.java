package twitter4j.auth;

import twitter4j.conf.Configuration;

/* loaded from: classes.dex */
public class VineAuthorizationFactory {
    public static Authorization getInstance(Configuration conf) {
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (consumerKey != null && consumerSecret != null) {
            VineOauthAuthorization oauth2 = new VineOauthAuthorization(conf);
            String accessToken = conf.getOAuthAccessToken();
            String accessTokenSecret = conf.getOAuthAccessTokenSecret();
            if (accessToken != null && accessTokenSecret != null) {
                oauth2.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
            }
            return oauth2;
        }
        String screenName = conf.getUser();
        String password = conf.getPassword();
        if (screenName == null || password == null) {
            return null;
        }
        Authorization auth = new BasicAuthorization(screenName, password);
        return auth;
    }
}
