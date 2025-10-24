package co.vine.android.service.components.authentication;

import android.net.Uri;
import android.os.Bundle;
import co.vine.android.api.TwitterUser;
import co.vine.android.api.VineLogin;
import co.vine.android.client.AppController;
import co.vine.android.client.Session;
import co.vine.android.client.SessionManager;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Hex;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.mobileapptracker.MATEvent;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import java.util.HashMap;
import java.util.Map;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public final class AuthenticationComponent extends NotifiableComponent<Actions, AuthenticationActionListener> {
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();

    protected enum Actions {
        VERIFY_DIGITS,
        LOGIN,
        SIGN_UP,
        TWITTER_LOGIN,
        TWITTER_X_AUTH,
        LOGOUT
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        LoginAction loginAction = new LoginAction();
        registerAsActionString(builder, "co.vine.android.session.login", loginAction);
        registerAsActionCode(builder, Actions.LOGIN, loginAction, new LoginActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.VERIFY_DIGITS, new DigitsVerifyAction(), new DigitsVerifyNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.TWITTER_LOGIN, new CheckLoginTwitterAction(), new CheckLoginTwitterNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.LOGOUT, new LogoutAction(), new LogoutNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.SIGN_UP, new SignUpAction(), new SignUpActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.TWITTER_X_AUTH, new TwitterXAuthAction(), new TwitterXAuthNotifier(this.mListeners));
    }

    public String login(AppController appController, Session session, VineLogin login, String password, boolean reactivate) {
        if (session.isLoggedIn()) {
            session = new Session();
            this.mSessionManager.setCurrentSession(session);
        }
        session.setLoginStatus(Session.LoginStatus.LOGGING_IN);
        Bundle b = appController.createServiceBundle(session);
        b.putParcelable(MATEvent.LOGIN, login);
        b.putString("pass", password);
        b.putBoolean("reactivate", reactivate);
        return executeServiceAction(appController, Actions.LOGIN, b);
    }

    public String loginWithTwitterXAuth(AppController appController, String username, String password) {
        Bundle b = appController.createServiceBundle();
        b.putString("username", username);
        b.putString("pass", password);
        return executeServiceAction(appController, Actions.TWITTER_X_AUTH, b);
    }

    public String loginCheckTwitter(AppController appController, String username, String token, String secret, long twitterUserId, boolean reactivate) {
        Session session = this.mSessionManager.getCurrentSession();
        if (session.isLoggedIn()) {
            session = new Session();
            this.mSessionManager.setCurrentSession(session);
        }
        session.setLoginStatus(Session.LoginStatus.LOGGING_IN);
        Bundle b = appController.createServiceBundle();
        b.putString("username", username);
        b.putString("key", token);
        b.putString("secret", secret);
        b.putLong("t_id", twitterUserId);
        b.putBoolean("reactivate", reactivate);
        return executeServiceAction(appController, Actions.TWITTER_LOGIN, b);
    }

    public String loginCheckTwitter(AppController appController, VineLogin login, boolean reactivate) {
        return loginCheckTwitter(appController, login.twitterUsername, login.twitterToken, login.twitterSecret, login.twitterUserId, reactivate);
    }

    public boolean isLoggedOut(int actionCode, Bundle b) {
        return b.getBoolean("logged_out") && actionCode != getActionCode(Actions.TWITTER_LOGIN);
    }

    public void logout(AppController appController, Session session) {
        if (session.getLoginStatus() == Session.LoginStatus.LOGGED_IN || session.getLoginStatus() == Session.LoginStatus.LOGGING_IN) {
            session.setLoginStatus(Session.LoginStatus.LOGGING_OUT);
            executeServiceAction(appController, Actions.LOGOUT, appController.createServiceBundle(session));
        }
    }

    public void logout(AppController appController) {
        logout(appController, appController.getActiveSession());
    }

    public void signUp(AppController appController, String login, String password, String username, String phoneNumber, Uri profileUri, TwitterUser twitterUser, VineLogin vl) {
        Bundle b = appController.createServiceBundle();
        b.putString("email", login);
        b.putString("pass", password);
        b.putString("a_name", username);
        b.putString("phone", phoneNumber);
        b.putParcelable("uri", profileUri);
        b.putParcelable("t_user", Parcels.wrap(twitterUser));
        if (vl != null) {
            b.putString("key", vl.twitterToken);
            b.putString("secret", vl.twitterSecret);
        }
        executeServiceAction(appController, Actions.SIGN_UP, b);
    }

    public void verifyDigits(AppController appController, Session session, TwitterAuthToken token, TwitterAuthConfig config) {
        DigitsOAuthSigning signing = new DigitsOAuthSigning(config, token);
        try {
            Map<String, String> oAuthHeaders = signing.getOAuthEchoHeadersForVerifyCredentials(getOptParams(session));
            Bundle b = getBundle(appController, session, oAuthHeaders);
            executeServiceAction(appController, Actions.VERIFY_DIGITS, b);
        } catch (Exception e) {
            CrashUtil.logException(e);
        }
    }

    private Bundle getBundle(AppController appController, Session session, Map<String, String> oAuthHeaders) {
        Bundle b = appController.createServiceBundle(session);
        b.putString("digits_auth_service_provider", oAuthHeaders.get("X-Auth-Service-Provider"));
        b.putString("digits_verify_credentials_authorization", oAuthHeaders.get("X-Verify-Credentials-Authorization"));
        return b;
    }

    private Map<String, String> getOptParams(Session session) throws Exception {
        Map<String, String> optParams = new HashMap<>(1);
        String hashedSessionKey = Hex.encodeHex(VineAccountHelper.getKeyDigest(session.getSessionKey()));
        optParams.put("sessionId", hashedSessionKey);
        return optParams;
    }
}
