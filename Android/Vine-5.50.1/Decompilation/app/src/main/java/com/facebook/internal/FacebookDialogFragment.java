package com.facebook.internal;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.internal.WebDialog;

/* loaded from: classes.dex */
public class FacebookDialogFragment extends DialogFragment {
    private Dialog dialog;

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override // android.support.v4.app.DialogFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        WebDialog webDialog;
        super.onCreate(savedInstanceState);
        if (this.dialog == null) {
            FragmentActivity activity = getActivity();
            Intent intent = activity.getIntent();
            Bundle params = NativeProtocol.getMethodArgumentsFromIntent(intent);
            boolean isWebFallback = params.getBoolean("is_fallback", false);
            if (!isWebFallback) {
                String actionName = params.getString("action");
                Bundle webParams = params.getBundle("params");
                if (Utility.isNullOrEmpty(actionName)) {
                    Utility.logd("FacebookDialogFragment", "Cannot start a WebDialog with an empty/missing 'actionName'");
                    activity.finish();
                    return;
                }
                webDialog = new WebDialog.Builder(activity, actionName, webParams).setOnCompleteListener(new WebDialog.OnCompleteListener() { // from class: com.facebook.internal.FacebookDialogFragment.1
                    @Override // com.facebook.internal.WebDialog.OnCompleteListener
                    public void onComplete(Bundle values, FacebookException error) {
                        FacebookDialogFragment.this.onCompleteWebDialog(values, error);
                    }
                }).build();
            } else {
                String url = params.getString("url");
                if (Utility.isNullOrEmpty(url)) {
                    Utility.logd("FacebookDialogFragment", "Cannot start a fallback WebDialog with an empty/missing 'url'");
                    activity.finish();
                    return;
                } else {
                    String redirectUrl = String.format("fb%s://bridge/", FacebookSdk.getApplicationId());
                    webDialog = new FacebookWebFallbackDialog(activity, url, redirectUrl);
                    webDialog.setOnCompleteListener(new WebDialog.OnCompleteListener() { // from class: com.facebook.internal.FacebookDialogFragment.2
                        @Override // com.facebook.internal.WebDialog.OnCompleteListener
                        public void onComplete(Bundle values, FacebookException error) {
                            FacebookDialogFragment.this.onCompleteWebFallbackDialog(values);
                        }
                    });
                }
            }
            this.dialog = webDialog;
        }
    }

    @Override // android.support.v4.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (this.dialog == null) {
            onCompleteWebDialog(null, null);
            setShowsDialog(false);
        }
        return this.dialog;
    }

    @Override // android.support.v4.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.dialog instanceof WebDialog) {
            ((WebDialog) this.dialog).resize();
        }
    }

    @Override // android.support.v4.app.DialogFragment, android.support.v4.app.Fragment
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCompleteWebDialog(Bundle values, FacebookException error) {
        FragmentActivity fragmentActivity = getActivity();
        Intent resultIntent = NativeProtocol.createProtocolResultIntent(fragmentActivity.getIntent(), values, error);
        int resultCode = error == null ? -1 : 0;
        fragmentActivity.setResult(resultCode, resultIntent);
        fragmentActivity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCompleteWebFallbackDialog(Bundle values) {
        FragmentActivity fragmentActivity = getActivity();
        Intent resultIntent = new Intent();
        if (values == null) {
            values = new Bundle();
        }
        resultIntent.putExtras(values);
        fragmentActivity.setResult(-1, resultIntent);
        fragmentActivity.finish();
    }
}
