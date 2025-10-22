package co.vine.android.service.components.postactions;

import android.os.Bundle;
import co.vine.android.api.VineEntity;
import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;
import co.vine.android.util.analytics.FlurryUtils;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class PostActionsComponent extends NotifiableComponent<Actions, PostActionsListener> {

    protected enum Actions {
        LIKE,
        UNLIKE,
        REVINE,
        UNREVINE,
        EDIT_CAPTION
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.REVINE, new RevineAction(), new RevineActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.UNREVINE, new UnrevineAction(), new UnrevineActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.LIKE, new LikeAction(), new LikeActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.UNLIKE, new UnlikeAction(), new UnlikeActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.EDIT_CAPTION, new EditCaptionAction(), new EditCaptionActionNotifier(this.mListeners));
    }

    public String revine(AppController appController, String source, long postToRevine, long repostId, String meUsername) {
        Bundle b = appController.createServiceBundle();
        b.putLong("post_id", postToRevine);
        b.putLong("repost_id", repostId);
        b.putString("username", meUsername);
        FlurryUtils.trackRevine(postToRevine, source);
        return executeServiceAction(appController, Actions.REVINE, b);
    }

    public String unRevine(AppController appController, String source, long postToUnrevine, long myRepostId, long repostId, boolean following, boolean notify) {
        Bundle b = appController.createServiceBundle();
        b.putLong("post_id", postToUnrevine);
        b.putLong("my_repost_id", myRepostId);
        b.putLong("repost_id", repostId);
        b.putBoolean("notify", notify);
        b.putBoolean("following", following);
        FlurryUtils.trackUnRevine(source);
        return executeServiceAction(appController, Actions.UNREVINE, b);
    }

    public String likePost(AppController appController, String source, long postToLike, long repostId, boolean notify) {
        Bundle b = appController.createServiceBundle();
        b.putLong("post_id", postToLike);
        b.putLong("repost_id", repostId);
        b.putLong("user_id", appController.getActiveId());
        b.putString("username", appController.getActiveSessionReadOnly().getScreenName());
        b.putBoolean("notify", notify);
        FlurryUtils.trackLikePost(postToLike, source);
        return executeServiceAction(appController, Actions.LIKE, b);
    }

    public String unlikePost(AppController appController, String source, long postToUnlike, long repostId, boolean notify) {
        Bundle b = appController.createServiceBundle();
        b.putLong("post_id", postToUnlike);
        b.putLong("repost_id", repostId);
        b.putBoolean("notify", notify);
        FlurryUtils.trackUnLikePost(source);
        return executeServiceAction(appController, Actions.UNLIKE, b);
    }

    public String editCaption(AppController appController, long postId, String description, ArrayList<VineEntity> entities) {
        Bundle b = appController.createServiceBundle();
        b.putLong("post_id", postId);
        b.putString("desc", description);
        b.putParcelableArrayList("entities", entities);
        return executeServiceAction(appController, Actions.EDIT_CAPTION, b);
    }
}
