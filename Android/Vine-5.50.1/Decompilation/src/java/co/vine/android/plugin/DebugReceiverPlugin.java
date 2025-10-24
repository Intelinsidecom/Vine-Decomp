package co.vine.android.plugin;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class DebugReceiverPlugin extends BasePlugin {
    private static int[] mDebugMenuSecrets = {24, 25, 24, 25};
    private int mDebugMenu;

    public DebugReceiverPlugin() {
        super("debugReceiver");
        this.mDebugMenu = 0;
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Activity activity;
        if (SLog.sLogsOn && this.mDebugMenu < mDebugMenuSecrets.length && keyCode == mDebugMenuSecrets[this.mDebugMenu]) {
            this.mDebugMenu++;
            if (this.mDebugMenu == mDebugMenuSecrets.length && (activity = getActivity()) != null) {
                activity.sendBroadcast(new Intent("co.vine.android.debug_receiver"));
                return false;
            }
            return false;
        }
        return false;
    }
}
