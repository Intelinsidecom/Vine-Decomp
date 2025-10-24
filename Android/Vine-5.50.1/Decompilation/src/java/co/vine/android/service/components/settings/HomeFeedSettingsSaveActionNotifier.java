package co.vine.android.service.components.settings;

import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class HomeFeedSettingsSaveActionNotifier implements ListenerNotifier {
    private ArrayList<SettingsListener> mListeners;

    public HomeFeedSettingsSaveActionNotifier(ArrayList<SettingsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        if (this.mListeners != null) {
            Iterator<SettingsListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                SettingsListener listener = it.next();
                listener.onSaveHomeFeedControlSettingsComplete(notification.reqId, notification.statusCode, notification.reasonPhrase);
            }
        }
    }
}
