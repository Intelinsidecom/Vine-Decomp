package co.vine.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.util.CrashUtil;

/* loaded from: classes.dex */
public class BaseControllerFragment extends BaseFragment {
    protected AppController mAppController;
    private AppSessionListener mAppSessionListener;
    private boolean mIsResumed;
    protected ProgressDialog mProgressDialog;

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAppController = AppController.getInstance(getActivity());
    }

    @Override // co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mIsResumed = true;
        if (this.mAppSessionListener != null) {
            this.mAppController.addListener(this.mAppSessionListener);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mIsResumed = false;
        if (this.mAppSessionListener != null) {
            this.mAppController.removeListener(this.mAppSessionListener);
        }
    }

    protected void setAppSessionListener(AppSessionListener listener) {
        if (listener != this.mAppSessionListener) {
            if (this.mAppSessionListener != null) {
                this.mAppController.removeListener(this.mAppSessionListener);
            }
            this.mAppSessionListener = listener;
            if (this.mAppSessionListener != null && this.mIsResumed) {
                this.mAppController.addListener(listener);
            }
        }
    }

    public AppController getAppController() {
        return this.mAppController;
    }

    protected void showProgressDialog(int messageResId) {
        FragmentActivity activity;
        if (this.mProgressDialog == null && (activity = getActivity()) != null) {
            ProgressDialog d = new ProgressDialog(activity, 2);
            d.setMessage(getString(messageResId));
            d.setProgress(0);
            try {
                d.show();
                this.mProgressDialog = d;
            } catch (IllegalStateException e) {
                CrashUtil.logOrThrowInDebug(e);
            }
        }
    }

    protected void dismissProgressDialog() {
        if (this.mProgressDialog != null) {
            try {
                this.mProgressDialog.dismiss();
                this.mProgressDialog = null;
            } catch (IllegalStateException e) {
                CrashUtil.logOrThrowInDebug(e);
            }
        }
    }
}
