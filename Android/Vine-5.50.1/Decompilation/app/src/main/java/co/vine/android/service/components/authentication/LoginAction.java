package co.vine.android.service.components.authentication;

import android.os.Bundle;
import android.text.TextUtils;
import co.vine.android.R;
import co.vine.android.VineLoggingException;
import co.vine.android.api.VineError;
import co.vine.android.api.VineLogin;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.analytics.FlurryUtils;
import com.mobileapptracker.MATEvent;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public final class LoginAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        try {
            VineLogin login = (VineLogin) request.b.getParcelable(MATEvent.LOGIN);
            String password = request.b.getString("pass");
            VineParserReader vp = VineParserReader.createParserReader(4);
            ArrayList<BasicNameValuePair> params = new ArrayList<>();
            if (!TextUtils.isEmpty(login.username) && !TextUtils.isEmpty(password)) {
                params.add(new BasicNameValuePair("username", login.username));
                params.add(new BasicNameValuePair(PropertyConfiguration.PASSWORD, password));
            }
            boolean isTwitter = (TextUtils.isEmpty(login.twitterToken) || TextUtils.isEmpty(login.twitterSecret) || login.twitterUserId <= 0) ? false : true;
            if (isTwitter) {
                params.add(new BasicNameValuePair("twitterOauthToken", login.twitterToken));
                params.add(new BasicNameValuePair("twitterOauthSecret", login.twitterSecret));
                params.add(new BasicNameValuePair("twitterId", String.valueOf(login.twitterUserId)));
            }
            String lastPathSegment = request.b.getBoolean("reactivate", false) ? "reactivate" : "authenticate";
            StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", lastPathSegment);
            NetworkOperation op = request.networkFactory.createPostRequest(request.context, url, params, vp, request.api).execute();
            if (op.isOK()) {
                VineLogin vl = (VineLogin) vp.getParsedObject();
                vl.screenName = vl.username;
                vl.username = login.username;
                request.b.putString("a_name", vl.screenName);
                request.b.putParcelable(MATEvent.LOGIN, vl);
                FlurryUtils.trackLoginSuccess(isTwitter);
                return new VineServiceActionResult(vp, op);
            }
            Bundle ret = new Bundle();
            ret.putInt("statusCode", op.statusCode);
            FlurryUtils.trackLoginFailure(isTwitter, op.statusCode);
            VineError error = (VineError) vp.getParsedObject();
            if (error != null) {
                ret.putString("reasonPhrase", error.getMessage());
                request.b.putInt("error_code", error.getErrorCode());
            } else {
                ret.putString("reasonPhrase", request.context.getString(R.string.error_unknown));
            }
            ret.putInt("executionCode", 1);
            return new VineServiceActionResult(ret);
        } catch (Throwable e) {
            CrashUtil.logOrThrowInDebug(new VineLoggingException(e));
            throw e;
        }
    }
}
