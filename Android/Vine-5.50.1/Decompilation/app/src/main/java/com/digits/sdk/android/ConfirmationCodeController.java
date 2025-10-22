package com.digits.sdk.android;

import android.content.Context;
import android.net.Uri;
import android.os.ResultReceiver;
import android.widget.EditText;
import android.widget.TextView;
import com.digits.sdk.android.DigitsScribeConstants;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import io.fabric.sdk.android.services.common.CommonUtils;

/* loaded from: classes.dex */
class ConfirmationCodeController extends DigitsControllerImpl {
    private final InvertedStateButton callMeButton;
    private final Boolean isEmailCollection;
    private final String phoneNumber;
    private final InvertedStateButton resendButton;
    private final TextView timerText;

    ConfirmationCodeController(ResultReceiver resultReceiver, StateButton stateButton, InvertedStateButton resendButton, InvertedStateButton callMeButton, EditText phoneEditText, String phoneNumber, DigitsScribeService scribeService, boolean isEmailCollection, TextView timerText) {
        this(resultReceiver, stateButton, resendButton, callMeButton, phoneEditText, phoneNumber, Digits.getSessionManager(), Digits.getInstance().getDigitsClient(), new ConfirmationErrorCodes(stateButton.getContext().getResources()), Digits.getInstance().getActivityClassManager(), scribeService, isEmailCollection, timerText);
    }

    ConfirmationCodeController(ResultReceiver resultReceiver, StateButton stateButton, InvertedStateButton resendButton, InvertedStateButton callMeButton, EditText phoneEditText, String phoneNumber, SessionManager<DigitsSession> sessionManager, DigitsClient client, ErrorCodes errors, ActivityClassManager activityClassManager, DigitsScribeService scribeService, boolean isEmailCollection, TextView timerText) {
        super(resultReceiver, stateButton, phoneEditText, client, errors, activityClassManager, sessionManager, scribeService);
        this.phoneNumber = phoneNumber;
        this.isEmailCollection = Boolean.valueOf(isEmailCollection);
        this.resendButton = resendButton;
        this.callMeButton = callMeButton;
        this.countDownTimer = createCountDownTimer(15000, timerText, resendButton, callMeButton);
        this.timerText = timerText;
    }

    @Override // com.digits.sdk.android.DigitsController
    public void executeRequest(final Context context) {
        this.scribeService.click(DigitsScribeConstants.Element.SUBMIT);
        if (validateInput(this.editText.getText())) {
            this.sendButton.showProgress();
            CommonUtils.hideKeyboard(context, this.editText);
            String code = this.editText.getText().toString();
            this.digitsClient.createAccount(code, this.phoneNumber, new DigitsCallback<DigitsUser>(context, this) { // from class: com.digits.sdk.android.ConfirmationCodeController.1
                @Override // com.twitter.sdk.android.core.Callback
                public void success(Result<DigitsUser> result) {
                    ConfirmationCodeController.this.scribeService.success();
                    DigitsSession session = DigitsSession.create(result, ConfirmationCodeController.this.phoneNumber);
                    if (!ConfirmationCodeController.this.isEmailCollection.booleanValue()) {
                        ConfirmationCodeController.this.loginSuccess(context, session, ConfirmationCodeController.this.phoneNumber);
                    } else {
                        ConfirmationCodeController.this.sessionManager.setActiveSession(session);
                        ConfirmationCodeController.this.startEmailRequest(context, ConfirmationCodeController.this.phoneNumber);
                    }
                }
            });
        }
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public void resendCode(Context context, final InvertedStateButton activeButton, Verification verificationType) {
        activeButton.showProgress();
        this.digitsClient.registerDevice(this.phoneNumber, verificationType, new DigitsCallback<DeviceRegistrationResponse>(context, this) { // from class: com.digits.sdk.android.ConfirmationCodeController.2
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<DeviceRegistrationResponse> result) {
                activeButton.showFinish();
                activeButton.postDelayed(new Runnable() { // from class: com.digits.sdk.android.ConfirmationCodeController.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        activeButton.showStart();
                        ConfirmationCodeController.this.timerText.setText(String.valueOf(15), TextView.BufferType.NORMAL);
                        ConfirmationCodeController.this.resendButton.setEnabled(false);
                        ConfirmationCodeController.this.callMeButton.setEnabled(false);
                        ConfirmationCodeController.this.startTimer();
                    }
                }, 1500L);
            }
        });
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public void handleError(Context context, DigitsException digitsException) {
        this.callMeButton.showError();
        this.resendButton.showError();
        super.handleError(context, digitsException);
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl
    Uri getTosUri() {
        return DigitsConstants.TWITTER_TOS;
    }
}
