package co.vine.android.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import co.vine.android.client.AppController;
import co.vine.android.client.FacebookVineApp;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import java.util.Arrays;

/* loaded from: classes.dex */
public class FacebookHelper {
    public static final boolean SINGLE_FACEBOOK_DIALOG;
    private static int sAttempts;
    private static CallbackManager sCallbackManager;
    private static LoginManager sLoginManager;

    static {
        SINGLE_FACEBOOK_DIALOG = Build.MODEL.length() == "SM-C115".length() && Build.MODEL.contains("SM-C11");
        sAttempts = 0;
    }

    public static void initialize(Context context) {
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.setApplicationId(FacebookVineApp.APP_ID);
            FacebookSdk.sdkInitialize(context);
        }
    }

    private static LoginManager getLoginManager() {
        if (sLoginManager == null) {
            sLoginManager = LoginManager.getInstance();
            sLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
            sLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
            sCallbackManager = CallbackManager.Factory.create();
            sLoginManager.registerCallback(sCallbackManager, new FacebookCallback<LoginResult>() { // from class: co.vine.android.social.FacebookHelper.1
                @Override // com.facebook.FacebookCallback
                public void onSuccess(LoginResult loginResult) {
                    AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                }

                @Override // com.facebook.FacebookCallback
                public void onCancel() {
                }

                @Override // com.facebook.FacebookCallback
                public void onError(FacebookException error) {
                }
            });
        }
        return sLoginManager;
    }

    public static boolean isFacebookConnected(Context context) {
        initialize(context);
        AccessToken token = AccessToken.getCurrentAccessToken();
        return (token == null || token.isExpired()) ? false : true;
    }

    public static void clearFacebookToken(Context context) {
        initialize(context);
        LoginManager.getInstance().logOut();
    }

    public static void connectToFacebookProfile(Activity activity) {
        initialize(activity);
        if (SINGLE_FACEBOOK_DIALOG) {
            connectToFacebookPublish(activity);
        } else {
            getLoginManager().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
        }
    }

    public static void connectToFacebookPublish(Activity activity) {
        initialize(activity);
        sAttempts = 1;
        getLoginManager().logInWithPublishPermissions(activity, Arrays.asList("publish_actions"));
    }

    public static boolean hasPermission(String permission) {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token != null && token.getPermissions().contains(permission);
    }

    public static boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        AccessToken token;
        if (sCallbackManager == null) {
            return false;
        }
        sCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1 || (token = AccessToken.getCurrentAccessToken()) == null) {
            return false;
        }
        if (token.getPermissions().contains("publish_actions")) {
            sAttempts = 0;
            AppController.getInstance(activity).sendFacebookToken(token.getToken());
        } else {
            sAttempts++;
            if (sAttempts > 1) {
                sAttempts = 0;
                AppController.getInstance(activity).sendFacebookToken(token.getToken());
                return false;
            }
            connectToFacebookPublish(activity);
        }
        return true;
    }
}
