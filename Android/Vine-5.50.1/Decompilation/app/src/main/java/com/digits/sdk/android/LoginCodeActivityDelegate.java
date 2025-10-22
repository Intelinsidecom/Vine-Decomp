package com.digits.sdk.android;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.EditText;
import android.widget.TextView;
import io.fabric.sdk.android.services.common.CommonUtils;

/* loaded from: classes.dex */
class LoginCodeActivityDelegate extends DigitsActivityDelegateImpl {
    Activity activity;
    InvertedStateButton callMeButton;
    AuthConfig config;
    DigitsController controller;
    LinkTextView editPhoneNumberLinkTextView;
    EditText editText;
    SmsBroadcastReceiver receiver;
    InvertedStateButton resendButton;
    private final DigitsScribeService scribeService;
    StateButton stateButton;
    TextView termsText;
    TextView timerText;

    LoginCodeActivityDelegate(DigitsScribeService scribeService) {
        this.scribeService = scribeService;
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public void init(Activity activity, Bundle bundle) {
        this.activity = activity;
        this.editText = (EditText) activity.findViewById(R.id.dgts__confirmationEditText);
        this.stateButton = (StateButton) activity.findViewById(R.id.dgts__createAccount);
        this.resendButton = (InvertedStateButton) activity.findViewById(R.id.dgts__resendConfirmationButton);
        this.callMeButton = (InvertedStateButton) activity.findViewById(R.id.dgts__callMeButton);
        this.editPhoneNumberLinkTextView = (LinkTextView) activity.findViewById(R.id.dgts__editPhoneNumber);
        this.termsText = (TextView) activity.findViewById(R.id.dgts__termsTextCreateAccount);
        this.timerText = (TextView) activity.findViewById(R.id.dgts__countdownTimer);
        this.config = (AuthConfig) bundle.getParcelable("auth_config");
        this.controller = initController(bundle);
        setUpEditText(activity, this.controller, this.editText);
        setUpSendButton(activity, this.controller, this.stateButton);
        setupResendButton(activity, this.controller, this.scribeService, this.resendButton);
        setupCallMeButton(activity, this.controller, this.scribeService, this.callMeButton, this.config);
        setupCountDownTimer(this.controller, this.timerText, this.config);
        setUpEditPhoneNumberLink(activity, this.editPhoneNumberLinkTextView, bundle.getString("phone_number"));
        setUpTermsText(activity, this.controller, this.termsText);
        setUpSmsIntercept(activity, this.editText);
        CommonUtils.openKeyboard(activity, this.editText);
    }

    DigitsController initController(Bundle bundle) {
        return new LoginCodeController((ResultReceiver) bundle.getParcelable("receiver"), this.stateButton, this.resendButton, this.callMeButton, this.editText, bundle.getString("request_id"), bundle.getLong("user_id"), bundle.getString("phone_number"), this.scribeService, Boolean.valueOf(bundle.getBoolean("email_enabled")), this.timerText);
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl
    public void setUpTermsText(Activity activity, DigitsController controller, TextView termsText) {
        if (this.config != null && this.config.tosUpdate) {
            termsText.setText(getFormattedTerms(activity, R.string.dgts__terms_text_sign_in));
            super.setUpTermsText(activity, controller, termsText);
        } else {
            termsText.setVisibility(8);
        }
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public int getLayoutId() {
        return R.layout.dgts__activity_confirmation;
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

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl, com.digits.sdk.android.ActivityLifecycle
    public void onDestroy() {
        if (this.receiver != null) {
            this.activity.unregisterReceiver(this.receiver);
        }
        this.controller.cancelTimer();
    }

    protected void setUpSmsIntercept(Activity activity, EditText editText) {
        if (CommonUtils.checkPermission(activity, "android.permission.RECEIVE_SMS")) {
            IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            this.receiver = new SmsBroadcastReceiver(editText);
            activity.registerReceiver(this.receiver, filter);
        }
    }
}
