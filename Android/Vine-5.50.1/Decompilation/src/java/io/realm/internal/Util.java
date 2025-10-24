package io.realm.internal;

import io.realm.RealmObject;

/* loaded from: classes.dex */
public class Util {
    static native long nativeGetMemUsage();

    static native String nativeGetTablePrefix();

    static native void nativeSetDebugLevel(int i);

    static native String nativeTestcase(int i, boolean z, long j);

    static {
        RealmCore.loadLibrary();
    }

    public static String getTablePrefix() {
        return nativeGetTablePrefix();
    }

    public static Class<? extends RealmObject> getOriginalModelClass(Class<? extends RealmObject> clazz) {
        Class superclass = clazz.getSuperclass();
        if (!superclass.equals(RealmObject.class)) {
            return superclass;
        }
        return clazz;
    }
}
