package co.vine.android.service.components.remoterequestcontrol;

import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;

/* loaded from: classes.dex */
public final class RemoteRequestControlComponent extends NotifiableComponent<Actions, RemoteRequestControlActionListener> {

    protected enum Actions {
        ABORT_ALL_REQUESTS
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.ABORT_ALL_REQUESTS, new AbortAllRequestsAction(), new AbortAllRequestsNotifier(this.mListeners));
    }

    public void abortAllRequests(AppController appController) {
        appController.cancelAllPendingRequests();
        executeServiceAction(appController, Actions.ABORT_ALL_REQUESTS, appController.createServiceBundle());
    }
}
