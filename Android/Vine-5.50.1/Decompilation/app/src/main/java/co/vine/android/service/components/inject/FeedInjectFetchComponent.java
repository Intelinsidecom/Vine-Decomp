package co.vine.android.service.components.inject;

import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;

/* loaded from: classes.dex */
public final class FeedInjectFetchComponent extends NotifiableComponent<Actions, InjectionFetchListener> {

    protected enum Actions {
        FETCH_SUGGESTED_FEED_MOSAIC
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.FETCH_SUGGESTED_FEED_MOSAIC, new FetchSuggestedFeedMosaicAction(), new FetchSuggestedMosaicActionNotifier(this.mListeners));
    }

    public String fetchSuggestedFeedMosaic(AppController appController) {
        return executeServiceAction(appController, Actions.FETCH_SUGGESTED_FEED_MOSAIC, null);
    }
}
