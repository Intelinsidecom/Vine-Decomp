package com.tune.crosspromo;

/* loaded from: classes.dex */
public enum TuneBannerPosition {
    BOTTOM_CENTER,
    TOP_CENTER;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static TuneBannerPosition[] valuesCustom() {
        TuneBannerPosition[] tuneBannerPositionArrValuesCustom = values();
        int length = tuneBannerPositionArrValuesCustom.length;
        TuneBannerPosition[] tuneBannerPositionArr = new TuneBannerPosition[length];
        System.arraycopy(tuneBannerPositionArrValuesCustom, 0, tuneBannerPositionArr, 0, length);
        return tuneBannerPositionArr;
    }
}
