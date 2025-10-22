package co.vine.android.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class BasePlugin implements Plugin {
    private WeakReference<Activity> mActivity;
    private int mId = -1;
    protected PluginManager mManager;
    public final String name;

    public BasePlugin(String name) {
        this.name = name;
    }

    @Override // co.vine.android.plugin.Plugin
    public int getId() {
        return this.mId;
    }

    @Override // co.vine.android.plugin.Plugin
    public String getName() {
        return this.name;
    }

    @Override // co.vine.android.plugin.Plugin
    public int onAdded(PluginManager manager, int id) {
        this.mManager = manager;
        this.mId = id;
        return 1;
    }

    public Handler getHandler() {
        if (this.mManager != null) {
            return this.mManager.getHandler();
        }
        return null;
    }

    @Override // co.vine.android.plugin.Plugin
    public void onActivityCreated(Activity activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    public Activity getActivity() {
        if (this.mActivity != null) {
            return this.mActivity.get();
        }
        return null;
    }

    @Override // co.vine.android.plugin.Plugin
    public void onResume(Activity activity) {
    }

    @Override // co.vine.android.plugin.Plugin
    public void onPause() {
    }

    @Override // co.vine.android.plugin.Plugin
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override // co.vine.android.plugin.Plugin
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override // co.vine.android.plugin.Plugin
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return false;
    }

    @Override // co.vine.android.plugin.Plugin
    public boolean onBackPressed() {
        return false;
    }

    @Override // co.vine.android.plugin.Plugin
    public void onDestroy() {
    }

    @Override // co.vine.android.plugin.Plugin
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override // co.vine.android.plugin.Plugin
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override // co.vine.android.plugin.Plugin
    public void onStart(Activity activity) {
    }

    @Override // co.vine.android.plugin.Plugin
    public void onStop(Activity activity) {
    }
}
