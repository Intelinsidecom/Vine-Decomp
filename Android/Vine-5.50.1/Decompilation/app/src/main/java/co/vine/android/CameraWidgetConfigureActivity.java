package co.vine.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import co.vine.android.util.analytics.FlurryUtils;

/* loaded from: classes.dex */
public class CameraWidgetConfigureActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupShortcut(this);
        Toast.makeText(this, R.string.widget_added_toast, 0).show();
        FlurryUtils.trackCameraWidgetAdded();
        setResult(0);
        finish();
    }

    public static void setupShortcut(Context context) {
        Intent shortcutIntent = new Intent(context.getApplicationContext(), (Class<?>) StartActivity.class);
        shortcutIntent.setAction("co.vine.android.RECORD");
        shortcutIntent.setFlags(131072);
        Intent addIntent = new Intent();
        addIntent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
        addIntent.putExtra("android.intent.extra.shortcut.NAME", context.getText(R.string.camera_widget_name));
        addIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), R.drawable.ic_launcher_capture_widget));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.getApplicationContext().sendBroadcast(addIntent);
    }
}
