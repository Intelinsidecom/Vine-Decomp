package co.vine.android.util;

import java.io.IOException;

/* loaded from: classes.dex */
public class ExceptionUtil {
    public static boolean isNoSpaceLeftException(Exception e) {
        String msg;
        if (e == null || (msg = e.getMessage()) == null) {
            return false;
        }
        return e instanceof IOException ? msg.contains("ENOSPC") || msg.contains("EDQUOT") : msg.contains("No space left on device") || msg.contains("Quota exceeded");
    }
}
