package co.vine.android.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import co.vine.android.plugin.Plugin;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: classes.dex */
public class BasePluginManager<T extends Plugin> implements PluginManager<T> {
    protected final Handler mHandler;
    protected int mNextPluginId;
    protected final ArrayList<T> mPlugins;

    public BasePluginManager() {
        this.mPlugins = new ArrayList<>();
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override // co.vine.android.plugin.PluginManager
    public int getDialogIdForPlugin(T plugin, int dialogId) {
        return (plugin.getId() << 10) + dialogId;
    }

    public BasePluginManager(Collection<T> plugins) {
        this();
        addPlugins(this.mNextPluginId, plugins);
    }

    public void addPlugins(int startId, Collection<T> plugins) {
        int id = startId;
        for (T plugin : plugins) {
            id += plugin.onAdded(this, id);
            SLog.i("Plugin enabled: {}, id {}.", plugin.getName(), Integer.valueOf(plugin.getId()));
        }
        this.mNextPluginId = id;
        this.mPlugins.addAll(plugins);
    }

    public void addPlugins(Collection<T> plugins) {
        addPlugins(this.mNextPluginId, plugins);
    }

    public void addPlugin(T plugin) {
        int id = this.mNextPluginId;
        int id2 = id + plugin.onAdded(this, id);
        SLog.i("Plugin enabled: {}, id {}.", plugin.getName(), Integer.valueOf(plugin.getId()));
        this.mNextPluginId = id2;
        this.mPlugins.add(plugin);
    }

    public void onResume(Activity activity) {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            plugin.onResume(activity);
        }
    }

    public void onPause() {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            plugin.onPause();
        }
    }

    public boolean onBackPressed() {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            if (plugin.onBackPressed()) {
                return true;
            }
        }
        return false;
    }

    public void onDestroy() {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            plugin.onDestroy();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean shouldReturnTrue = false;
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            if (plugin.onCreateOptionsMenu(menu)) {
                shouldReturnTrue = true;
            }
        }
        return shouldReturnTrue;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            if (plugin.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public void onStart(Activity activity) {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            plugin.onStart(activity);
        }
    }

    public void onStop(Activity activity) {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            plugin.onStop(activity);
        }
    }

    @Override // co.vine.android.plugin.PluginManager
    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            if (plugin.onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            if (plugin.onKeyUp(keyCode, event)) {
                return true;
            }
        }
        return false;
    }

    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            if (plugin.onActivityResult(activity, requestCode, resultCode, data)) {
                return true;
            }
        }
        return false;
    }

    public void onActivityCreated(Activity activity) {
        Iterator<T> it = this.mPlugins.iterator();
        while (it.hasNext()) {
            T plugin = it.next();
            plugin.onActivityCreated(activity);
        }
    }

    public ArrayList<T> getEnabledPlugins() {
        return this.mPlugins;
    }
}
