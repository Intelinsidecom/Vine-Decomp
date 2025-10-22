package co.vine.android.service.components.settings;

import co.vine.android.api.VineHomeFeedSetting;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class SettingsListener {
    public void onSaveHomeFeedControlSettingsComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onGetHomeFeedControlSettingsComplete(String reqId, ArrayList<VineHomeFeedSetting> notifSettings) {
    }
}
