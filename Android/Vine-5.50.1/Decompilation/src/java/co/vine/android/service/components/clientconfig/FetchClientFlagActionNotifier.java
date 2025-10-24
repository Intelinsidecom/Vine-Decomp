package co.vine.android.service.components.clientconfig;

import co.vine.android.api.response.VineClientFlags;
import co.vine.android.client.AppController;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.client.VineAPI;
import co.vine.android.service.components.Components;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CrashUtil;
import java.util.List;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public final class FetchClientFlagActionNotifier implements ListenerNotifier {
    private final List<ClientConfigUpdateListener> mListeners;

    public FetchClientFlagActionNotifier(List<ClientConfigUpdateListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        try {
            ClientFlagsHelper.setLastCheckMillis(notification.context);
            VineClientFlags clientFlags = (VineClientFlags) Parcels.unwrap(notification.b.getParcelable("client_flags"));
            if (notification.statusCode == 200 && clientFlags != null) {
                ClientFlagsHelper.updateClientFlags(notification.context, clientFlags);
                VineAPI.getInstance(notification.context).refreshHostUrls(clientFlags, notification.context.getResources());
                boolean abortRequests = notification.b.getBoolean("abort_requests", true);
                boolean hostsDidChange = ClientFlagsHelper.hostsDidChange(notification.context, clientFlags);
                if (hostsDidChange && abortRequests) {
                    Components.remoteRequestControlComponent().abortAllRequests(AppController.getInstance(notification.context));
                }
                for (ClientConfigUpdateListener listener : this.mListeners) {
                    listener.onFetchClientFlagsComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, clientFlags);
                }
            }
        } catch (Exception e) {
            CrashUtil.logException(e);
        }
    }
}
