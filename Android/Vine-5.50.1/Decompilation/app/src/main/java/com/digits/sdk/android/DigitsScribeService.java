package com.digits.sdk.android;

import com.digits.sdk.android.DigitsScribeConstants;

/* loaded from: classes.dex */
interface DigitsScribeService {
    void click(DigitsScribeConstants.Element element);

    void error(DigitsException digitsException);

    void failure();

    void impression();

    void success();
}
