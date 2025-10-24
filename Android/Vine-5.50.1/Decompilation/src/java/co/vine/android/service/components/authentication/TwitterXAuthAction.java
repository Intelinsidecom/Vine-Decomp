package co.vine.android.service.components.authentication;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import co.vine.android.R;
import co.vine.android.api.VineLogin;
import co.vine.android.client.TwitterVineApp;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import com.mobileapptracker.MATEvent;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.VineTwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.BasicAuthorization;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public final class TwitterXAuthAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        int statusCode;
        Bundle b = request.b;
        String reasonPhrase = null;
        String username = b.getString("username");
        String password = b.getString("pass");
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TwitterVineApp.API_KEY);
        builder.setOAuthConsumerSecret(TwitterVineApp.API_SECRET);
        Twitter twitter = new VineTwitterFactory(builder.build()).getInstance(new BasicAuthorization(username, password));
        Resources res = request.context.getResources();
        try {
            AccessToken aToken = twitter.getOAuthAccessToken();
            String screenName = aToken.getScreenName();
            String token = aToken.getToken();
            String tokenSecret = aToken.getTokenSecret();
            if (screenName != null && token != null && tokenSecret != null) {
                VineLogin login = new VineLogin(null, aToken.getScreenName(), token, aToken.getTokenSecret(), aToken.getUserId(), null);
                b.putParcelable(MATEvent.LOGIN, login);
                statusCode = HttpResponseCode.OK;
            } else {
                statusCode = HttpResponseCode.INTERNAL_SERVER_ERROR;
                reasonPhrase = res.getString(R.string.error_xauth);
                CrashUtil.log("Twitter access_token response contained empty params. screenName={},token={}, tokenSecret={}", screenName, token, tokenSecret);
            }
        } catch (TwitterException e) {
            if (e.getStatusCode() == 401) {
                statusCode = HttpResponseCode.UNAUTHORIZED;
                if (Util.isXauth2FactorError(e)) {
                    reasonPhrase = res.getString(R.string.error_auth_2_factor_error);
                } else {
                    reasonPhrase = res.getString(R.string.error_auth_username_pass);
                }
            } else if (e.isCausedByNetworkIssue()) {
                statusCode = HttpResponseCode.INTERNAL_SERVER_ERROR;
                reasonPhrase = res.getString(R.string.error_auth_check_internet_connection);
            } else {
                statusCode = HttpResponseCode.INTERNAL_SERVER_ERROR;
                reasonPhrase = res.getString(R.string.error_xauth);
            }
        } finally {
            b.putString("pass", null);
        }
        Bundle ret = new Bundle();
        ret.putInt("statusCode", statusCode);
        if (!TextUtils.isEmpty(reasonPhrase)) {
            ret.putString("reasonPhrase", reasonPhrase);
        }
        ret.putInt("executionCode", 1);
        return new VineServiceActionResult(ret);
    }
}
