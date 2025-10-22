package co.vine.android;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/* loaded from: classes.dex */
public class NotificationAlertsSettingsFragment extends PreferenceFragment {
    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.notification_preferences);
    }
}
