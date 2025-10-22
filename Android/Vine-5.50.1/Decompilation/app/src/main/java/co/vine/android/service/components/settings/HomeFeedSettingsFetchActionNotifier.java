package co.vine.android.service.components.settings;

import co.vine.android.api.VineHomeFeedSetting;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public final class HomeFeedSettingsFetchActionNotifier implements ListenerNotifier {
    private ArrayList<SettingsListener> mListeners;

    public HomeFeedSettingsFetchActionNotifier(ArrayList<SettingsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        if (this.mListeners != null) {
            ArrayList<VineHomeFeedSetting> homeFeedSettings = (ArrayList) Parcels.unwrap(notification.b.getParcelable("notificationSettings"));
            Iterator<SettingsListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                SettingsListener listener = it.next();
                listener.onGetHomeFeedControlSettingsComplete(notification.reqId, homeFeedSettings);
            }
        }
    }
}
