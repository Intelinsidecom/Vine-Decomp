package com.tune.crosspromo;

import java.util.Locale;

/* loaded from: classes.dex */
public enum TuneAdOrientation {
    ALL,
    PORTRAIT_ONLY,
    LANDSCAPE_ONLY;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static TuneAdOrientation[] valuesCustom() {
        TuneAdOrientation[] tuneAdOrientationArrValuesCustom = values();
        int length = tuneAdOrientationArrValuesCustom.length;
        TuneAdOrientation[] tuneAdOrientationArr = new TuneAdOrientation[length];
        System.arraycopy(tuneAdOrientationArrValuesCustom, 0, tuneAdOrientationArr, 0, length);
        return tuneAdOrientationArr;
    }

    public static TuneAdOrientation forValue(String value) {
        String enumName = value.toUpperCase(Locale.ENGLISH);
        return valueOf(enumName);
    }
}
