package co.vine.android.util;

import android.app.Dialog;

/* loaded from: classes.dex */
public final class DialogUtils {
    public static void showDialogUnsafe(Dialog dialog) {
        try {
            dialog.show();
        } catch (Exception e) {
        }
    }
}
