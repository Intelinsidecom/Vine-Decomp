package co.vine.android.plugin;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

/* loaded from: classes.dex */
public interface Plugin {
    int getId();

    String getName();

    void onActivityCreated(Activity activity);

    boolean onActivityResult(Activity activity, int i, int i2, Intent intent);

    int onAdded(PluginManager pluginManager, int i);

    boolean onBackPressed();

    boolean onCreateOptionsMenu(Menu menu);

    void onDestroy();

    boolean onKeyDown(int i, KeyEvent keyEvent);

    boolean onKeyUp(int i, KeyEvent keyEvent);

    boolean onOptionsItemSelected(MenuItem menuItem);

    void onPause();

    void onResume(Activity activity);

    void onStart(Activity activity);

    void onStop(Activity activity);
}
