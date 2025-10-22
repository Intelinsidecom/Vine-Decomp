package com.digits.sdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.EditText;
import android.widget.TextView;
import io.fabric.sdk.android.services.common.CommonUtils;

/* loaded from: classes.dex */
class PinCodeActivityDelegate extends DigitsActivityDelegateImpl {
    DigitsController controller;
    EditText editText;
    private final DigitsScribeService scribeService;
    StateButton stateButton;
    TextView termsText;

    PinCodeActivityDelegate(DigitsScribeService scribeService) {
        this.scribeService = scribeService;
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public int getLayoutId() {
        return R.layout.dgts__activity_pin_code;
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public void init(Activity activity, Bundle bundle) {
        this.editText = (EditText) activity.findViewById(R.id.dgts__confirmationEditText);
        this.stateButton = (StateButton) activity.findViewById(R.id.dgts__createAccount);
        this.termsText = (TextView) activity.findViewById(R.id.dgts__termsTextCreateAccount);
        this.controller = initController(bundle);
        setUpEditText(activity, this.controller, this.editText);
        setUpSendButton(activity, this.controller, this.stateButton);
        setUpTermsText(activity, this.controller, this.termsText);
        CommonUtils.openKeyboard(activity, this.editText);
    }

    DigitsController initController(Bundle bundle) {
        return new PinCodeController((ResultReceiver) bundle.getParcelable("receiver"), this.stateButton, this.editText, bundle.getString("request_id"), bundle.getLong("user_id"), bundle.getString("phone_number"), this.scribeService, Boolean.valueOf(bundle.getBoolean("email_enabled")));
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public boolean isValid(Bundle bundle) {
        return BundleManager.assertContains(bundle, "receiver", "phone_number", "request_id", "user_id");
    }

    @Override // com.digits.sdk.android.ActivityLifecycle
    public void onResume() {
        this.scribeService.impression();
        this.controller.onResume();
    }
}
