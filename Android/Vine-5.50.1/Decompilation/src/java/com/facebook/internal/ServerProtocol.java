package com.facebook.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public final class ServerProtocol {
    private static final String TAG = ServerProtocol.class.getName();
    public static final Collection<String> errorsProxyAuthDisabled = Utility.unmodifiableCollection("service_disabled", "AndroidAuthKillSwitchException");
    public static final Collection<String> errorsUserCanceled = Utility.unmodifiableCollection("access_denied", "OAuthAccessDeniedException");

    public static final String getDialogAuthority() {
        return String.format("m.%s", FacebookSdk.getFacebookDomain());
    }

    public static final String getGraphUrlBase() {
        return String.format("https://graph.%s", FacebookSdk.getFacebookDomain());
    }

    public static final String getGraphVideoUrlBase() {
        return String.format("https://graph-video.%s", FacebookSdk.getFacebookDomain());
    }

    public static final String getAPIVersion() {
        return "v2.4";
    }

    public static Bundle getQueryParamsForPlatformActivityIntentWebFallback(String callId, int version, Bundle methodArgs) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        Context context = FacebookSdk.getApplicationContext();
        String keyHash = FacebookSdk.getApplicationSignature(context);
        if (Utility.isNullOrEmpty(keyHash)) {
            return null;
        }
        Bundle webParams = new Bundle();
        webParams.putString("android_key_hash", keyHash);
        webParams.putString("app_id", FacebookSdk.getApplicationId());
        webParams.putInt("version", version);
        webParams.putString("display", "touch");
        Bundle bridgeArguments = new Bundle();
        bridgeArguments.putString("action_id", callId);
        if (methodArgs == null) {
            methodArgs = new Bundle();
        }
        try {
            JSONObject bridgeArgsJSON = BundleJSONConverter.convertToJSON(bridgeArguments);
            JSONObject methodArgsJSON = BundleJSONConverter.convertToJSON(methodArgs);
            if (bridgeArgsJSON == null || methodArgsJSON == null) {
                return null;
            }
            webParams.putString("bridge_args", bridgeArgsJSON.toString());
            webParams.putString("method_args", methodArgsJSON.toString());
            return webParams;
        } catch (JSONException je) {
            Logger.log(LoggingBehavior.DEVELOPER_ERRORS, 6, TAG, "Error creating Url -- " + je);
            return null;
        }
    }
}
