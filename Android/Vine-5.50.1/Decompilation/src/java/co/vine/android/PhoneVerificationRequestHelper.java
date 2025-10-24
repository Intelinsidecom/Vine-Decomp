package co.vine.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/* loaded from: classes.dex */
public class PhoneVerificationRequestHelper {
    protected PendingPhoneVerificationRequest mPendingVerificationRequest;

    public void setPendingVerifyNumberRequest(PendingPhoneVerificationRequest pendingVerifyRequest) {
        this.mPendingVerificationRequest = pendingVerifyRequest;
    }

    public void onPhoneVerificationRequired(Activity activity, String reqId, int actionCode, Bundle bundle, String verifyMsg) {
        setPendingVerifyNumberRequest(new PendingPhoneVerificationRequest(reqId, actionCode, bundle));
        if (activity != null) {
            VerifyNumberDialog dialog = new VerifyNumberDialog(activity, verifyMsg);
            dialog.show();
        }
    }

    protected class VerifyNumberDialog extends AlertDialog {
        public VerifyNumberDialog(final Context context, CharSequence msg) {
            super(context);
            setCancelable(true);
            setMessage(msg);
            setButton(-1, getContext().getResources().getString(R.string.go_to_settings), new DialogInterface.OnClickListener() { // from class: co.vine.android.PhoneVerificationRequestHelper.VerifyNumberDialog.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent settingIntent = new Intent(context, (Class<?>) SettingsActivity.class);
                    context.startActivity(settingIntent);
                }
            });
        }
    }
}
