package com.digits.sdk.android;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.digits.sdk.android.DigitsScribeConstants;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
abstract class DigitsActivityDelegateImpl implements DigitsActivityDelegate {
    DigitsActivityDelegateImpl() {
    }

    @Override // com.digits.sdk.android.ActivityLifecycle
    public void onDestroy() {
    }

    public void setUpSendButton(final Activity activity, final DigitsController controller, StateButton stateButton) {
        stateButton.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.DigitsActivityDelegateImpl.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                controller.clearError();
                controller.executeRequest(activity);
            }
        });
    }

    void setupResendButton(final Activity activity, final DigitsController controller, final DigitsScribeService scribeService, final InvertedStateButton resendButton) {
        resendButton.setEnabled(false);
        resendButton.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.DigitsActivityDelegateImpl.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                scribeService.click(DigitsScribeConstants.Element.RESEND);
                controller.clearError();
                controller.resendCode(activity, resendButton, Verification.sms);
            }
        });
    }

    void setupCallMeButton(final Activity activity, final DigitsController controller, final DigitsScribeService scribeService, final InvertedStateButton callMeButton, AuthConfig config) {
        callMeButton.setVisibility(config.isVoiceEnabled ? 0 : 8);
        callMeButton.setEnabled(false);
        callMeButton.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.DigitsActivityDelegateImpl.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                scribeService.click(DigitsScribeConstants.Element.CALL);
                controller.clearError();
                controller.resendCode(activity, callMeButton, Verification.voicecall);
            }
        });
    }

    void setupCountDownTimer(DigitsController controller, TextView timerText, AuthConfig config) {
        setTimerAlignment(timerText, config.isVoiceEnabled);
        controller.startTimer();
    }

    protected void setUpEditPhoneNumberLink(final Activity activity, LinkTextView editPhoneLink, String phoneNumber) {
        editPhoneLink.setText(phoneNumber);
        editPhoneLink.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.DigitsActivityDelegateImpl.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                activity.setResult(HttpResponseCode.BAD_REQUEST);
                activity.finish();
            }
        });
    }

    public void setUpEditText(final Activity activity, final DigitsController controller, EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.digits.sdk.android.DigitsActivityDelegateImpl.5
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 5) {
                    return false;
                }
                controller.clearError();
                controller.executeRequest(activity);
                return true;
            }
        });
        editText.addTextChangedListener(controller.getTextWatcher());
    }

    public void setUpTermsText(final Activity activity, final DigitsController controller, TextView termsText) {
        termsText.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.DigitsActivityDelegateImpl.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                controller.clearError();
                controller.showTOS(activity);
            }
        });
    }

    protected Spanned getFormattedTerms(Activity activity, int termsResId) {
        return Html.fromHtml(activity.getString(termsResId, new Object[]{"\"", "<u>", "</u>"}));
    }

    @Override // com.digits.sdk.android.ActivityLifecycle
    public void onActivityResult(int requestCode, int resultCode, Activity activity) {
    }

    private void setTimerAlignment(TextView timerText, boolean isVoiceEnabled) {
        int resendElementToAlign = isVoiceEnabled ? R.id.dgts__callMeButton : R.id.dgts__resendConfirmationButton;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) timerText.getLayoutParams();
        params.addRule(7, resendElementToAlign);
        params.addRule(8, resendElementToAlign);
        timerText.setLayoutParams(params);
    }
}
