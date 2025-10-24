package co.vine.android.util.analytics;

import android.app.Activity;
import co.vine.android.plugin.BasePlugin;

/* loaded from: classes.dex */
public class AnalyticsPlugin extends BasePlugin {
    private Boolean mIsStarted;

    public AnalyticsPlugin() {
        super("analytics");
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onStart(Activity activity) {
        AnalyticsManager.onStart(activity);
        this.mIsStarted = true;
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onStop(Activity activity) {
        AnalyticsManager.onStop(activity);
        this.mIsStarted = false;
    }
}
