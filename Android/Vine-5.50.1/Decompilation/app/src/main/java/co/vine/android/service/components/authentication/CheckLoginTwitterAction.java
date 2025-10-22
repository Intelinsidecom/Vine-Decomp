package co.vine.android.service.components.authentication;

import android.os.Bundle;
import android.text.TextUtils;
import co.vine.android.R;
import co.vine.android.api.VineError;
import co.vine.android.api.VineKnownErrors;
import co.vine.android.api.VineLogin;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VineUser;
import co.vine.android.client.TwitterVineApp;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.util.ConsoleLoggers;
import com.mobileapptracker.MATEvent;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import twitter4j.VineTwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public final class CheckLoginTwitterAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        String reasonPhrase;
        Bundle b = request.b;
        String username = b.getString("username");
        String tokenKey = b.getString("key");
        String tokenSecret = b.getString("secret");
        long userId = b.getLong("t_id");
        if (request.service.getTwitter() == null) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TwitterVineApp.API_KEY).setOAuthConsumerSecret(TwitterVineApp.API_SECRET).setOAuthAccessToken(tokenKey).setOAuthAccessTokenSecret(tokenSecret);
            request.service.setTwitter(new VineTwitterFactory(builder.build()).getInstance());
        }
        if (!TextUtils.isEmpty(tokenKey) && !TextUtils.isEmpty(tokenSecret)) {
            String pathSegment = b.getBoolean("reactivate", false) ? "reactivate" : "authenticate";
            StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", pathSegment, "twitter");
            ArrayList<BasicNameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("twitterOauthToken", tokenKey));
            params.add(new BasicNameValuePair("twitterOauthSecret", tokenSecret));
            VineParserReader vp = VineParserReader.createParserReader(5);
            NetworkOperation op = request.networkFactory.createPostRequest(request.context, url, params, vp, request.api).execute();
            if (op.isOK()) {
                b.putBoolean("a_exists", true);
                VineLogin vl = (VineLogin) vp.getParsedObject();
                vl.twitterUsername = username;
                vl.twitterToken = tokenKey;
                vl.twitterSecret = tokenSecret;
                vl.twitterUserId = userId;
                vl.loginType = 2;
                b.putParcelable(MATEvent.LOGIN, vl);
                String usersMe = request.api.getBaseUrl() + "/users/me";
                VineParserReader vp2 = VineParserReader.createParserReader(2);
                NetworkOperation op2 = request.networkFactory.createBasicAuthGetRequest(request.context, new StringBuilder(usersMe), request.api, vp2, vl.key).execute();
                if (op2.isOK()) {
                    VineUser user = (VineUser) vp2.getParsedObject();
                    b.putParcelable(PropertyConfiguration.USER, user);
                }
                return new VineServiceActionResult(vp2, op2);
            }
            VineError error = (VineError) vp.getParsedObject();
            Bundle ret = new Bundle();
            if (error != null) {
                reasonPhrase = request.context.getString(R.string.error_server);
                int code = error.getErrorCode();
                if (VineKnownErrors.BAD_CREDENTIALS.code == code || VineKnownErrors.RECORD_DOES_NOT_EXIST.code == code || VineKnownErrors.ACCOUNT_DEACTIVATED.code == code) {
                    VineLogin login = new VineLogin(null, username, tokenKey, tokenSecret, userId, null);
                    b.putParcelable(MATEvent.LOGIN, login);
                    b.putBoolean("a_exists", false);
                    b.putInt("error_code", code);
                } else {
                    reasonPhrase = error.getMessage();
                }
            } else {
                reasonPhrase = request.context.getString(R.string.error_server);
                ConsoleLoggers.VINE_SERVICE.get().e("Got error from Twitter Login");
            }
            ret.putString("reasonPhrase", reasonPhrase);
            ret.putInt("statusCode", op.statusCode);
            ret.putInt("executionCode", 1);
            return new VineServiceActionResult(ret);
        }
        throw new IllegalArgumentException("Token missing.");
    }
}
