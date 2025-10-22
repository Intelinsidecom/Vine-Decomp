package io.fabric.sdk.android.services.common;

/* loaded from: classes.dex */
public enum DeliveryMechanism {
    DEVELOPER(1),
    USER_SIDELOAD(2),
    TEST_DISTRIBUTION(3),
    APP_STORE(4);

    private final int id;

    DeliveryMechanism(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override // java.lang.Enum
    public String toString() {
        return Integer.toString(this.id);
    }

    public static DeliveryMechanism determineFrom(String installerPackageName) {
        if ("io.crash.air".equals(installerPackageName)) {
            return TEST_DISTRIBUTION;
        }
        if (installerPackageName != null) {
            return APP_STORE;
        }
        return DEVELOPER;
    }
}
