package com.digits.sdk.android;

import android.content.res.Resources;
import com.googlecode.javacv.cpp.avutil;

/* loaded from: classes.dex */
class PhoneNumberErrorCodes extends DigitsErrorCodes {
    PhoneNumberErrorCodes(Resources resources) {
        super(resources);
        this.codeIdMap.put(44, R.string.dgts__try_again_phone_number);
        this.codeIdMap.put(300, R.string.dgts__try_again_phone_number);
        this.codeIdMap.put(avutil.AV_PIX_FMT_YUV420P14BE, R.string.dgts__try_again_phone_number);
        this.codeIdMap.put(285, R.string.dgts__confirmation_error_alternative);
        this.codeIdMap.put(286, R.string.dgts__unsupported_operator_error);
    }
}
