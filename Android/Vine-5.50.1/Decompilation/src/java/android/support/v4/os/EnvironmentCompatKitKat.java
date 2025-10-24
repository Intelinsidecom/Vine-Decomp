package android.support.v4.os;

import android.os.Environment;
import java.io.File;

/* loaded from: classes2.dex */
class EnvironmentCompatKitKat {
    EnvironmentCompatKitKat() {
    }

    public static String getStorageState(File path) {
        return Environment.getStorageState(path);
    }
}
