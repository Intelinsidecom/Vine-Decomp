package io.realm.internal;

/* loaded from: classes.dex */
public class Version {
    static native int nativeGetAPIVersion();

    static native String nativeGetVersion();

    static native boolean nativeHasFeature(int i);

    static native boolean nativeIsAtLeast(int i, int i2, int i3);

    public static String getCoreVersion() {
        return nativeGetVersion();
    }

    public static boolean coreLibVersionCompatible(boolean throwIfNot) {
        if (!nativeIsAtLeast(0, 1, 6)) {
            String errTxt = "Version mismatch between realm.jar (0.1.6) and native core library (" + getCoreVersion() + ")";
            if (throwIfNot) {
                throw new RuntimeException(errTxt);
            }
            System.err.println(errTxt);
            return false;
        }
        boolean compatible = nativeGetAPIVersion() == 23;
        if (!compatible) {
            String errTxt2 = "Native lib API is version " + nativeGetAPIVersion() + " != 23 which is expected by the jar.";
            if (throwIfNot) {
                throw new RuntimeException(errTxt2);
            }
            System.err.println(errTxt2);
        }
        return compatible;
    }
}
