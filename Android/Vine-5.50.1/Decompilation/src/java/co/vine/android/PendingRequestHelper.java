package co.vine.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import co.vine.android.client.AppController;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class PendingRequestHelper {
    private AppController mAppController;
    private ArrayList<PendingRequest> mPendingRequests;
    private final CaptchaRequestHelper mCaptchaRequestHelper = new CaptchaRequestHelper() { // from class: co.vine.android.PendingRequestHelper.1
        @Override // co.vine.android.CaptchaRequestHelper
        protected void handleRetryCaptchaRequest(PendingCaptchaRequest pendingCaptchaRequest) {
            PendingRequestHelper.this.handleRetryCaptchaRequest(pendingCaptchaRequest);
        }

        @Override // co.vine.android.CaptchaRequestHelper
        protected void handleFailedCaptchaRequest(PendingCaptchaRequest pendingCaptchaRequest) {
            PendingRequestHelper.this.handleFailedCaptchaRequest(pendingCaptchaRequest);
        }
    };
    private final PhoneVerificationRequestHelper mPhoneVerificationRequestHelper = new PhoneVerificationRequestHelper();

    public void restorePendingCaptchaRequest(Bundle savedInstanceState) {
        this.mCaptchaRequestHelper.restorePendingCaptchaRequest(savedInstanceState);
    }

    public void handlePendingCaptchaRequest() {
        this.mCaptchaRequestHelper.handlePendingCaptchaRequest();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return this.mCaptchaRequestHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void onCaptchaRequired(FragmentActivity activity, String reqId, int actionCode, Bundle bundle, String captchaUrl) {
        this.mCaptchaRequestHelper.onCaptchaRequired(activity, reqId, actionCode, bundle, captchaUrl);
    }

    public void onPhoneVerificationRequired(FragmentActivity activity, String reqId, int actionCode, Bundle bundle, String verifyMsg) {
        this.mPhoneVerificationRequestHelper.onPhoneVerificationRequired(activity, reqId, actionCode, bundle, verifyMsg);
    }

    public void onCreate(AppController appController, Bundle savedInstanceState) {
        this.mAppController = appController;
        if (savedInstanceState != null) {
            this.mPendingRequests = savedInstanceState.getParcelableArrayList("pending_reqs");
        }
        if (this.mPendingRequests == null) {
            this.mPendingRequests = new ArrayList<>(5);
        }
    }

    public void onResume() {
        for (int i = this.mPendingRequests.size() - 1; i >= 0; i--) {
            PendingRequest req = this.mPendingRequests.get(i);
            if (this.mAppController.isPendingRequest(req.id)) {
                showProgress(req.fetchType);
            } else {
                hideProgress(req.fetchType);
                this.mPendingRequests.remove(req);
            }
        }
    }

    public void showProgress(int progressType) {
    }

    public void hideProgress(int progressType) {
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("pending_reqs", this.mPendingRequests);
        this.mCaptchaRequestHelper.onSaveInstanceState(outState);
    }

    public String addRequest(String reqId) {
        this.mPendingRequests.add(new PendingRequest(reqId));
        return reqId;
    }

    public String addRequest(String reqId, int fetchType) {
        this.mPendingRequests.add(new PendingRequest(reqId, fetchType));
        return reqId;
    }

    public boolean hasRequest(String requestId) {
        Iterator<PendingRequest> it = this.mPendingRequests.iterator();
        while (it.hasNext()) {
            PendingRequest req = it.next();
            if (req.id.equals(requestId)) {
                return true;
            }
        }
        return false;
    }

    public PendingRequest removeRequest(String reqId) {
        ArrayList<PendingRequest> reqs = this.mPendingRequests;
        int size = reqs.size();
        for (int i = 0; i < size; i++) {
            PendingRequest req = reqs.get(i);
            if (req.id.equals(reqId)) {
                return reqs.remove(i);
            }
        }
        return null;
    }

    public boolean hasPendingRequest() {
        return !this.mPendingRequests.isEmpty();
    }

    public boolean hasPendingRequest(int fetchType) {
        if (fetchType != 0) {
            Iterator<PendingRequest> it = this.mPendingRequests.iterator();
            while (it.hasNext()) {
                PendingRequest req = it.next();
                if (req.fetchType == fetchType) {
                    return true;
                }
            }
        }
        return false;
    }

    public void handleRetryCaptchaRequest(PendingCaptchaRequest pendingCaptchaRequest) {
        if (this.mAppController != null) {
            addRequest(this.mAppController.retryRequest(pendingCaptchaRequest.actionCode, pendingCaptchaRequest.bundle));
        }
    }

    public void handleFailedCaptchaRequest(PendingCaptchaRequest pendingCaptchaRequest) {
        if (this.mAppController != null) {
            String newRequestToFail = this.mAppController.generateReqIdForCanceledCaptcha();
            addRequest(newRequestToFail);
            this.mAppController.failRequest(newRequestToFail, pendingCaptchaRequest.actionCode, pendingCaptchaRequest.bundle);
        }
    }
}
