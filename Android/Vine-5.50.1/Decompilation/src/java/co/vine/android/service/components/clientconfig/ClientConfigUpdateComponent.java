package co.vine.android.service.components.clientconfig;

import android.os.Bundle;
import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;

/* loaded from: classes.dex */
public final class ClientConfigUpdateComponent extends NotifiableComponent<Actions, ClientConfigUpdateListener> {

    protected enum Actions {
        GET_CLIENT_FLAGS
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.GET_CLIENT_FLAGS, new FetchClientFlagAction(), new FetchClientFlagActionNotifier(this.mListeners));
    }

    public void fetchClientFlags(AppController appController, boolean abortRequestsOnHostChange) {
        Bundle b = appController.createServiceBundle();
        b.putBoolean("abort_requests", abortRequestsOnHostChange);
        executeServiceAction(appController, Actions.GET_CLIENT_FLAGS, b);
    }
}
