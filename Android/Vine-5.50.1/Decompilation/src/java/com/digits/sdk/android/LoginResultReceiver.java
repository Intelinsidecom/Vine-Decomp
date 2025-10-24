package com.digits.sdk.android;

import android.os.Bundle;
import android.os.ResultReceiver;
import com.twitter.sdk.android.core.SessionManager;

/* loaded from: classes.dex */
class LoginResultReceiver extends ResultReceiver {
    final WeakAuthCallback callback;
    final SessionManager<DigitsSession> sessionManager;

    LoginResultReceiver(AuthCallback callback, SessionManager<DigitsSession> sessionManager) {
        super(null);
        this.callback = new WeakAuthCallback(callback);
        this.sessionManager = sessionManager;
    }

    @Override // android.os.ResultReceiver
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (this.callback != null) {
            if (resultCode == 200) {
                this.callback.success((DigitsSession) this.sessionManager.getActiveSession(), resultData.getString("phone_number"));
            } else if (resultCode == 400) {
                this.callback.failure(new DigitsException(resultData.getString("login_error")));
            }
        }
    }
}
