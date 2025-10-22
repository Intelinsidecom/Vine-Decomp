package co.vine.android.service.components.feedactions;

import android.os.Bundle;
import co.vine.android.api.FeedMetadata;
import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;

/* loaded from: classes.dex */
public final class FeedActionsComponent extends NotifiableComponent<Actions, FeedActionsListener> {

    protected enum Actions {
        SHARE_FEED,
        DELETE_FEED
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.SHARE_FEED, new ShareFeedAction(), new ShareFeedActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.DELETE_FEED, new DeleteFeedAction(), new DeleteFeedActionNotifier(this.mListeners));
    }

    public String shareFeed(AppController appController, String source, long feedUserId, String comment, FeedMetadata.FeedType feedType, long coverPostId, boolean postToVine, boolean postToTwitter, boolean postToFacebook, boolean postToTumblr, String tumblrAuthToken, String tumblrAuthSecret) {
        Bundle b = appController.createServiceBundle();
        b.putLong("feed_user_id", feedUserId);
        b.putString("comment", comment);
        b.putSerializable("feed_type", feedType);
        b.putLong("coverPostId", coverPostId);
        b.putBoolean("postToVine", postToVine);
        b.putBoolean("postToTwitter", postToTwitter);
        b.putBoolean("postToFacebook", postToFacebook);
        b.putBoolean("postToTumblr", postToTumblr);
        b.putString("tumblrOauthToken", tumblrAuthToken);
        b.putString("tumblrOauthSecret", tumblrAuthSecret);
        return executeServiceAction(appController, Actions.SHARE_FEED, b);
    }

    public String deleteFeed(AppController appController, long feedId) {
        Bundle b = appController.createServiceBundle();
        b.putLong("feed_id", feedId);
        return executeServiceAction(appController, Actions.DELETE_FEED, b);
    }
}
