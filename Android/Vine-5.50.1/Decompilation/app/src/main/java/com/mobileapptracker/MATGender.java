package com.mobileapptracker;

import java.util.Locale;

/* loaded from: classes.dex */
public enum MATGender {
    MALE,
    FEMALE,
    UNKNOWN;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static MATGender[] valuesCustom() {
        MATGender[] mATGenderArrValuesCustom = values();
        int length = mATGenderArrValuesCustom.length;
        MATGender[] mATGenderArr = new MATGender[length];
        System.arraycopy(mATGenderArrValuesCustom, 0, mATGenderArr, 0, length);
        return mATGenderArr;
    }

    public static MATGender forValue(String value) {
        String enumName = value.toUpperCase(Locale.ENGLISH);
        return valueOf(enumName);
    }

    public String value() {
        return name();
    }
}
