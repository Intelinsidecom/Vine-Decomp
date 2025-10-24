package com.facebook.login;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginClient;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
class LoginLogger {
    private final AppEventsLogger appEventsLogger;
    private String applicationId;
    private String facebookVersion;

    LoginLogger(Context context, String applicationId) {
        PackageInfo facebookInfo;
        this.applicationId = applicationId;
        this.appEventsLogger = AppEventsLogger.newLogger(context, applicationId);
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null && (facebookInfo = packageManager.getPackageInfo("com.facebook.katana", 0)) != null) {
                this.facebookVersion = facebookInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    static Bundle newAuthorizationLoggingBundle(String authLoggerId) {
        Bundle bundle = new Bundle();
        bundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        bundle.putString("0_auth_logger_id", authLoggerId);
        bundle.putString("3_method", "");
        bundle.putString("2_result", "");
        bundle.putString("5_error_message", "");
        bundle.putString("4_error_code", "");
        bundle.putString("6_extras", "");
        return bundle;
    }

    public void logStartLogin(LoginClient.Request pendingLoginRequest) {
        Bundle bundle = newAuthorizationLoggingBundle(pendingLoginRequest.getAuthId());
        try {
            JSONObject extras = new JSONObject();
            extras.put("login_behavior", pendingLoginRequest.getLoginBehavior().toString());
            extras.put("request_code", LoginClient.getLoginRequestCode());
            extras.put("permissions", TextUtils.join(",", pendingLoginRequest.getPermissions()));
            extras.put("default_audience", pendingLoginRequest.getDefaultAudience().toString());
            extras.put("isReauthorize", pendingLoginRequest.isRerequest());
            if (this.facebookVersion != null) {
                extras.put("facebookVersion", this.facebookVersion);
            }
            bundle.putString("6_extras", extras.toString());
        } catch (JSONException e) {
        }
        this.appEventsLogger.logSdkEvent("fb_mobile_login_start", null, bundle);
    }

    public void logCompleteLogin(String loginRequestId, Map<String, String> loggingExtras, LoginClient.Result.Code result, Map<String, String> resultExtras, Exception exception) {
        Bundle bundle = newAuthorizationLoggingBundle(loginRequestId);
        if (result != null) {
            bundle.putString("2_result", result.getLoggingValue());
        }
        if (exception != null && exception.getMessage() != null) {
            bundle.putString("5_error_message", exception.getMessage());
        }
        JSONObject jsonObject = null;
        if (!loggingExtras.isEmpty()) {
            jsonObject = new JSONObject(loggingExtras);
        }
        if (resultExtras != null) {
            if (jsonObject == null) {
                jsonObject = new JSONObject();
            }
            try {
                for (Map.Entry<String, String> entry : resultExtras.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            } catch (JSONException e) {
            }
        }
        if (jsonObject != null) {
            bundle.putString("6_extras", jsonObject.toString());
        }
        this.appEventsLogger.logSdkEvent("fb_mobile_login_complete", null, bundle);
    }

    public void logAuthorizationMethodStart(String authId, String method) {
        Bundle bundle = newAuthorizationLoggingBundle(authId);
        bundle.putString("3_method", method);
        this.appEventsLogger.logSdkEvent("fb_mobile_login_method_start", null, bundle);
    }

    public void logAuthorizationMethodComplete(String authId, String method, String result, String errorMessage, String errorCode, Map<String, String> loggingExtras) {
        Bundle bundle = newAuthorizationLoggingBundle(authId);
        if (result != null) {
            bundle.putString("2_result", result);
        }
        if (errorMessage != null) {
            bundle.putString("5_error_message", errorMessage);
        }
        if (errorCode != null) {
            bundle.putString("4_error_code", errorCode);
        }
        if (loggingExtras != null && !loggingExtras.isEmpty()) {
            JSONObject jsonObject = new JSONObject(loggingExtras);
            bundle.putString("6_extras", jsonObject.toString());
        }
        bundle.putString("3_method", method);
        this.appEventsLogger.logSdkEvent("fb_mobile_login_method_complete", null, bundle);
    }

    public void logUnexpectedError(String eventName, String errorMessage) {
        logUnexpectedError(eventName, errorMessage, "");
    }

    public void logUnexpectedError(String eventName, String errorMessage, String method) {
        Bundle bundle = newAuthorizationLoggingBundle("");
        bundle.putString("2_result", LoginClient.Result.Code.ERROR.getLoggingValue());
        bundle.putString("5_error_message", errorMessage);
        bundle.putString("3_method", method);
        this.appEventsLogger.logSdkEvent(eventName, null, bundle);
    }
}
