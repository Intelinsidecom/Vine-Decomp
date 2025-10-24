package com.digits.sdk.android;

import android.content.Context;
import android.text.TextWatcher;

/* loaded from: classes.dex */
interface DigitsController {
    void cancelTimer();

    void clearError();

    void executeRequest(Context context);

    ErrorCodes getErrors();

    TextWatcher getTextWatcher();

    void handleError(Context context, DigitsException digitsException);

    void onResume();

    void resendCode(Context context, InvertedStateButton invertedStateButton, Verification verification);

    void showTOS(Context context);

    void startTimer();
}
