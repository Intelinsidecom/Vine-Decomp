package co.vine.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class MuteUtil {
    public static final String ACTION_CHANGED_TO_MUTE = SLog.getAuthority() + ".muted";
    public static final String ACTION_CHANGED_TO_UNMUTE = SLog.getAuthority() + ".un_muted";
    public static final IntentFilter MUTE_INTENT_FILTER = new IntentFilter();
    private static SharedPreferences sp;

    static {
        MUTE_INTENT_FILTER.addAction(ACTION_CHANGED_TO_MUTE);
        MUTE_INTENT_FILTER.addAction(ACTION_CHANGED_TO_UNMUTE);
    }

    public static synchronized void setMuted(Context context, boolean mute) {
        if (sp == null) {
            sp = context.getSharedPreferences("mute", 0);
        }
        sp.edit().putBoolean("mute", mute).apply();
        SLog.d("Mute state change send.");
        context.sendBroadcast(new Intent(mute ? ACTION_CHANGED_TO_MUTE : ACTION_CHANGED_TO_UNMUTE), CrossConstants.BROADCAST_PERMISSION);
    }

    public static synchronized boolean isMuted(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("mute", 0);
        }
        return sp.getBoolean("mute", false);
    }
}
