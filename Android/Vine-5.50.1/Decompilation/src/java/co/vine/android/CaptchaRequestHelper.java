package co.vine.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/* loaded from: classes.dex */
public abstract class CaptchaRequestHelper {
    protected PendingCaptchaRequest mPendingCaptchaRequest;

    protected abstract void handleFailedCaptchaRequest(PendingCaptchaRequest pendingCaptchaRequest);

    protected abstract void handleRetryCaptchaRequest(PendingCaptchaRequest pendingCaptchaRequest);

    public void setPendingCaptchaRequest(PendingCaptchaRequest pendingCaptchaRequest) {
        this.mPendingCaptchaRequest = pendingCaptchaRequest;
    }

    public void restorePendingCaptchaRequest(Bundle savedInstanceState) {
        this.mPendingCaptchaRequest = (PendingCaptchaRequest) savedInstanceState.getParcelable("state_pending_captcha_request");
    }

    public void onCaptchaRequired(Activity activity, String reqId, int actionCode, Bundle bundle, String captchaUrl) {
        setPendingCaptchaRequest(new PendingCaptchaRequest(reqId, actionCode, bundle));
        if (activity != null) {
            Intent i = CaptchaActivity.getIntent(activity, captchaUrl, reqId);
            activity.startActivityForResult(i, 11);
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 11:
                if (this.mPendingCaptchaRequest != null && data != null) {
                    String reqId = data.getStringExtra("rid");
                    if (resultCode == -1 && reqId.equals(this.mPendingCaptchaRequest.reqId)) {
                        this.mPendingCaptchaRequest.state = 2;
                    } else if (resultCode == 2 && reqId.equals(this.mPendingCaptchaRequest.reqId)) {
                        this.mPendingCaptchaRequest.state = 3;
                    }
                }
                return true;
            default:
                return false;
        }
    }

    public void handlePendingCaptchaRequest() {
        if (this.mPendingCaptchaRequest != null) {
            if (this.mPendingCaptchaRequest.state == 3) {
                handleFailedCaptchaRequest(this.mPendingCaptchaRequest);
            } else if (this.mPendingCaptchaRequest.state == 2) {
                handleRetryCaptchaRequest(this.mPendingCaptchaRequest);
            }
            this.mPendingCaptchaRequest = null;
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("state_pending_captcha_request", this.mPendingCaptchaRequest);
    }
}
