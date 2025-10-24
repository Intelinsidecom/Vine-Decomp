package co.vine.android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
public abstract class BaseAdapterFragment extends BaseControllerFragment {
    protected final Handler mHandler = new Handler(Looper.getMainLooper());
    protected final PendingRequestHelper mPendingRequestHelper = createPendingRequestHelper();
    protected boolean mRefreshing;

    protected abstract void hideProgress(int i);

    protected abstract void showProgress(int i);

    protected class BasePendingRequestHelper extends PendingRequestHelper {
        protected BasePendingRequestHelper() {
        }

        @Override // co.vine.android.PendingRequestHelper
        public void showProgress(int progressType) {
            BaseAdapterFragment.this.showProgress(progressType);
        }

        @Override // co.vine.android.PendingRequestHelper
        public void hideProgress(int progressType) {
            BaseAdapterFragment.this.hideProgress(progressType);
        }
    }

    protected PendingRequestHelper createPendingRequestHelper() {
        return new BasePendingRequestHelper();
    }

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPendingRequestHelper.onCreate(this.mAppController, savedInstanceState);
    }

    @Override // co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mPendingRequestHelper.onResume();
    }

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        dismissProgressDialog();
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mPendingRequestHelper.onSaveInstanceState(outState);
    }

    protected String addRequest(String reqId) {
        return this.mPendingRequestHelper.addRequest(reqId);
    }

    protected String addRequest(String reqId, int fetchType) {
        return this.mPendingRequestHelper.addRequest(reqId, fetchType);
    }

    protected boolean hasRequest(String reqId) {
        return this.mPendingRequestHelper.hasRequest(reqId);
    }

    protected PendingRequest removeRequest(String reqId) {
        return this.mPendingRequestHelper.removeRequest(reqId);
    }

    protected boolean hasPendingRequest() {
        return this.mPendingRequestHelper.hasPendingRequest();
    }

    protected boolean hasPendingRequest(int fetchType) {
        return this.mPendingRequestHelper.hasPendingRequest(fetchType);
    }
}
