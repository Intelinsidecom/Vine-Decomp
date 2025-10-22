package com.digits.sdk.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import com.googlecode.javacv.cpp.avcodec;
import com.twitter.sdk.android.core.SessionManager;
import io.fabric.sdk.android.services.common.CommonUtils;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
abstract class DigitsControllerImpl implements TextWatcher, DigitsController {
    final ActivityClassManager activityClassManager;
    CountDownTimer countDownTimer;
    final DigitsClient digitsClient;
    final EditText editText;
    int errorCount = 0;
    final ErrorCodes errors;
    final ResultReceiver resultReceiver;
    final DigitsScribeService scribeService;
    final StateButton sendButton;
    final SessionManager<DigitsSession> sessionManager;

    abstract Uri getTosUri();

    DigitsControllerImpl(ResultReceiver resultReceiver, StateButton stateButton, EditText editText, DigitsClient client, ErrorCodes errors, ActivityClassManager activityClassManager, SessionManager<DigitsSession> sessionManager, DigitsScribeService scribeService) {
        this.resultReceiver = resultReceiver;
        this.digitsClient = client;
        this.activityClassManager = activityClassManager;
        this.sendButton = stateButton;
        this.editText = editText;
        this.errors = errors;
        this.sessionManager = sessionManager;
        this.scribeService = scribeService;
    }

    @Override // com.digits.sdk.android.DigitsController
    public void showTOS(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(getTosUri());
        context.startActivity(intent);
    }

    public void handleError(Context context, DigitsException exception) {
        this.errorCount++;
        this.scribeService.error(exception);
        if (isUnrecoverable(exception)) {
            this.scribeService.failure();
            startFallback(context, this.resultReceiver, exception);
        } else {
            this.editText.setError(exception.getLocalizedMessage());
            this.sendButton.showError();
        }
    }

    private boolean isUnrecoverable(DigitsException exception) {
        return this.errorCount == 5 || (exception instanceof UnrecoverableException);
    }

    void startActivityForResult(Activity activity, Intent intent) {
        activity.startActivityForResult(intent, avcodec.AV_CODEC_ID_YOP);
    }

    public void startFallback(Context context, ResultReceiver receiver, DigitsException reason) {
        Intent intent = new Intent(context, this.activityClassManager.getFailureActivity());
        intent.putExtra("receiver", receiver);
        intent.putExtra("fallback_reason", reason);
        context.startActivity(intent);
        CommonUtils.finishAffinity(context, HttpResponseCode.OK);
    }

    public boolean validateInput(CharSequence text) {
        return !TextUtils.isEmpty(text);
    }

    @Override // com.digits.sdk.android.DigitsController
    public void clearError() {
        this.editText.setError(null);
    }

    @Override // com.digits.sdk.android.DigitsController
    public ErrorCodes getErrors() {
        return this.errors;
    }

    @Override // com.digits.sdk.android.DigitsController
    public void onResume() {
        this.sendButton.showStart();
    }

    @Override // com.digits.sdk.android.DigitsController
    public TextWatcher getTextWatcher() {
        return this;
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        clearError();
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
    }

    public void resendCode(Context context, InvertedStateButton resendButton, Verification verificationType) {
    }

    Bundle getBundle(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("phone_number", phoneNumber);
        return bundle;
    }

    void loginSuccess(final Context context, DigitsSession session, final String phoneNumber) {
        this.sessionManager.setActiveSession(session);
        this.sendButton.showFinish();
        this.editText.postDelayed(new Runnable() { // from class: com.digits.sdk.android.DigitsControllerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                DigitsControllerImpl.this.resultReceiver.send(HttpResponseCode.OK, DigitsControllerImpl.this.getBundle(phoneNumber));
                CommonUtils.finishAffinity((Activity) context, HttpResponseCode.OK);
            }
        }, 1500L);
    }

    void startEmailRequest(Context context, String phoneNumber) {
        this.sendButton.showFinish();
        Intent intent = new Intent(context, this.activityClassManager.getEmailRequestActivity());
        Bundle bundle = getBundle(phoneNumber);
        bundle.putParcelable("receiver", this.resultReceiver);
        intent.putExtras(bundle);
        startActivityForResult((Activity) context, intent);
    }

    @Override // com.digits.sdk.android.DigitsController
    public void startTimer() {
        if (this.countDownTimer != null) {
            this.countDownTimer.start();
        }
    }

    @Override // com.digits.sdk.android.DigitsController
    public void cancelTimer() {
        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
        }
    }

    CountDownTimer createCountDownTimer(int disableDurationMillis, final TextView timerText, final InvertedStateButton resentButton, final InvertedStateButton callMeButton) {
        timerText.setText(String.valueOf(15));
        return new CountDownTimer(disableDurationMillis, 500L) { // from class: com.digits.sdk.android.DigitsControllerImpl.2
            @Override // android.os.CountDownTimer
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.valueOf(timeRoundedToSeconds(millisUntilFinished)));
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                timerText.setText("");
                timerText.setEnabled(true);
                resentButton.setEnabled(true);
                callMeButton.setEnabled(true);
            }

            private int timeRoundedToSeconds(double millis) {
                return (int) Math.ceil(millis / 1000.0d);
            }
        };
    }
}
