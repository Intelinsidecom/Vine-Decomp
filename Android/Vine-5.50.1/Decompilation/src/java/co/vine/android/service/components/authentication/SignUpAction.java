package co.vine.android.service.components.authentication;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import co.vine.android.R;
import co.vine.android.api.TwitterUser;
import co.vine.android.api.VineError;
import co.vine.android.api.VineLogin;
import co.vine.android.api.VineParserReader;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.util.CrossAnalytics;
import co.vine.android.util.analytics.FlurryUtils;
import com.mobileapptracker.MATEvent;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.parceler.Parcels;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public final class SignUpAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        String email = b.getString("email");
        TwitterUser twitterUser = (TwitterUser) Parcels.unwrap(b.getParcelable("t_user"));
        String token = b.getString("key");
        String secret = b.getString("secret");
        String preinstallKey = CrossAnalytics.getInstance(request.context).get("pre_install_sign up");
        ArrayList<BasicNameValuePair> params = new ArrayList<>();
        if (twitterUser != null) {
            addTwitterParams(params, twitterUser, token, secret);
        } else {
            addEmailParams(params, b, email);
        }
        addCommonParams(params, b, preinstallKey);
        StringBuilder url = new StringBuilder(request.api.getBaseUrl()).append("/users");
        VineParserReader vp = VineParserReader.createParserReader(5);
        NetworkOperation op = request.networkFactory.createPostRequest(request.context, url, params, vp, request.api).execute();
        if (op.isOK()) {
            return onSuccess(twitterUser, b, preinstallKey, request, vp, op, token, secret, email);
        }
        return onFailure(twitterUser != null, vp, op, request.context);
    }

    private void addCommonParams(ArrayList<BasicNameValuePair> params, Bundle b, String preinstallKey) {
        String phone = b.getString("phone");
        if (!TextUtils.isEmpty(phone)) {
            params.add(new BasicNameValuePair("phoneNumber", phone));
        }
        params.add(new BasicNameValuePair("authenticate", "true"));
        if (preinstallKey != null) {
            params.add(new BasicNameValuePair("preinstallTarget", preinstallKey));
        }
    }

    private void addEmailParams(ArrayList<BasicNameValuePair> params, Bundle b, String email) {
        String password = b.getString("pass");
        String name = b.getString("a_name");
        params.add(new BasicNameValuePair("username", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair(PropertyConfiguration.PASSWORD, password));
    }

    private void addTwitterParams(ArrayList<BasicNameValuePair> params, TwitterUser twitterUser, String token, String secret) {
        if (!TextUtils.isEmpty(twitterUser.name)) {
            params.add(new BasicNameValuePair("username", twitterUser.name));
        }
        params.add(new BasicNameValuePair("location", twitterUser.location));
        params.add(new BasicNameValuePair("description", twitterUser.description));
        params.add(new BasicNameValuePair("twitterId", String.valueOf(twitterUser.userId)));
        params.add(new BasicNameValuePair("twitterOauthToken", token));
        params.add(new BasicNameValuePair("twitterOauthSecret", secret));
    }

    private VineServiceActionResult onFailure(boolean twitterSignup, VineParserReader vp, NetworkOperation op, Context context) {
        Bundle ret = new Bundle();
        ret.putInt("statusCode", op.statusCode);
        VineError error = (VineError) vp.getParsedObject();
        if (error != null) {
            FlurryUtils.onSignupFailure(twitterSignup, error.getErrorCode(), error.getMessage(), op.statusCode);
            ret.putString("reasonPhrase", error.getMessage());
        } else {
            FlurryUtils.onSignupFailure(twitterSignup, 0, "Connection failed. No error object was received.", op.statusCode);
            ret.putString("reasonPhrase", context.getString(R.string.error_unknown));
        }
        ret.putInt("executionCode", 1);
        return new VineServiceActionResult(ret);
    }

    private VineServiceActionResult onSuccess(TwitterUser twitterUser, Bundle b, String preinstallKey, VineServiceAction.Request request, VineParserReader vp, NetworkOperation op, String token, String secret, String email) {
        FlurryUtils.onSignupSuccess(twitterUser != null);
        if (preinstallKey != null) {
            FlurryUtils.onSignupWithPreinstallSuccess(preinstallKey);
            CrossAnalytics.getInstance(request.context).clear("pre_install_sign up");
        }
        VineLogin vl = (VineLogin) vp.getParsedObject();
        if (twitterUser != null) {
            vl.twitterToken = token;
            vl.twitterSecret = secret;
            vl.twitterUserId = twitterUser.userId;
            vl.loginType = 2;
        } else {
            vl.username = email;
        }
        b.putParcelable(MATEvent.LOGIN, vl);
        b.putString("s_key", vl.key);
        b.putLong("s_owner_id", vl.userId);
        return new VineServiceActionResult(vp, op);
    }
}
