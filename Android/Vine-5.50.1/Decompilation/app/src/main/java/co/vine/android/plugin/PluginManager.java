package co.vine.android.plugin;

import android.os.Handler;
import co.vine.android.plugin.Plugin;

/* loaded from: classes.dex */
public interface PluginManager<T extends Plugin> {
    int getDialogIdForPlugin(T t, int i);

    Handler getHandler();
}
