package android.support.v4.app;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;

/* loaded from: classes2.dex */
class ActivityCompatEclair {
    ActivityCompatEclair() {
    }

    public static void startIntentSenderForResult(Activity activity, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        activity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
    }
}
