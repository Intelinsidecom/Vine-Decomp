package co.vine.android.service.components.longform;

import android.os.Bundle;
import co.vine.android.api.VineEndlessLikesPostRecord;
import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public final class LongformComponent extends NotifiableComponent<Actions, LongformActionsListener> {

    protected enum Actions {
        FETCH_ENDLESS_LIKES,
        POST_ENDLESS_LIKES
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.FETCH_ENDLESS_LIKES, new FetchEndlessLikesAction(), new FetchEndlessLikesActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.POST_ENDLESS_LIKES, new PostEndlessLikesAction(), null);
    }

    public String fetchEndlessLikes(AppController appController, String longformId) {
        Bundle b = appController.createServiceBundle();
        b.putString("longform_id", longformId);
        return executeServiceAction(appController, Actions.FETCH_ENDLESS_LIKES, b);
    }

    public String postEndlessLikes(AppController appController, String longformId, VineEndlessLikesPostRecord record) {
        Bundle b = appController.createServiceBundle();
        b.putString("longform_id", longformId);
        b.putParcelable("endless_likes", Parcels.wrap(record));
        return executeServiceAction(appController, Actions.POST_ENDLESS_LIKES, b);
    }
}
