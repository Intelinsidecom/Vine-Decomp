package co.vine.android;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;

/* loaded from: classes.dex */
public class NotificationAlertsSettingsActivity extends BaseControllerActionBarActivity {
    private boolean mNotificationWasEnabled;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.mNotificationWasEnabled && !Settings.isNotificationEnabled(this)) {
            FlurryUtils.trackNotificationDisabled();
        }
    }

    @Override // android.support.v4.app.FragmentActivity
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        try {
            super.startActivityFromFragment(fragment, intent, requestCode);
        } catch (ActivityNotFoundException e) {
            CrashUtil.logException(e);
            Util.showCenteredToast(this, R.string.unsupported_feature);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_layout, true);
        setupActionBar((Boolean) true, (Boolean) true, R.string.settings_notifications_alerts, (Boolean) true, (Boolean) false);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getInt("color", 0) != 0) {
            setActionBarColor(extras.getInt("color"));
        }
        this.mNotificationWasEnabled = Settings.isNotificationEnabled(this);
        if (savedInstanceState == null) {
            NotificationAlertsSettingsFragment fragment = new NotificationAlertsSettingsFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
