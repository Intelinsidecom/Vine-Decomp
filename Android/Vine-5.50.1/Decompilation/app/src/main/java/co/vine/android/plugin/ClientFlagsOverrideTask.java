package co.vine.android.plugin;

import android.app.Activity;
import android.content.Intent;
import co.vine.android.ClientFlagsOverrideActivity;
import co.vine.android.plugin.DebugTasksPlugin;

/* loaded from: classes.dex */
public class ClientFlagsOverrideTask implements DebugTasksPlugin.DebugTask {
    @Override // co.vine.android.plugin.DebugTasksPlugin.DebugTask
    public void performTask(Activity activity) {
        activity.startActivity(new Intent(activity, (Class<?>) ClientFlagsOverrideActivity.class));
    }
}
