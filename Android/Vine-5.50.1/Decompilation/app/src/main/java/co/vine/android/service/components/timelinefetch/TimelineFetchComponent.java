package co.vine.android.service.components.timelinefetch;

import android.net.Uri;
import android.os.Bundle;
import co.vine.android.client.AppController;
import co.vine.android.client.Session;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;

/* loaded from: classes.dex */
public final class TimelineFetchComponent extends NotifiableComponent<Actions, TimelineFetchActionsListener> {

    protected enum Actions {
        FETCH_POSTS,
        FETCH_CHANNELS
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.FETCH_POSTS, new TimelineFetchAction(), new TimelineFetchActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.FETCH_CHANNELS, new ChannelsFetchAction(), new ChannelsFetchActionNotifier(this.mListeners));
    }

    public String fetchPosts(AppController appController, Session session, int size, long channelId, int type, boolean userInitiated, String tag, String sort, Uri data, UrlCachePolicy cachePolicy, boolean forceReplacePosts, long postId, int fetchType) {
        Bundle b = appController.createServiceBundle(session);
        b.putInt("size", size);
        b.putInt("type", type);
        b.putLong("profile_id", channelId);
        b.putParcelable("cache_policy", cachePolicy);
        b.putBoolean("user_init", userInitiated);
        b.putBoolean("replace_posts", forceReplacePosts);
        b.putLong("post_id", postId);
        b.putInt("fetch_type", fetchType);
        if (tag != null) {
            b.putString("tag_name", tag);
        }
        if (sort != null) {
            b.putString("sort", sort);
        }
        b.putParcelable("data", data);
        return executeServiceAction(appController, Actions.FETCH_POSTS, b);
    }

    public String fetchChannels(AppController appController, Session session, UrlCachePolicy cachePolicy) {
        Bundle b = appController.createServiceBundle(session);
        b.putParcelable("cache_policy", cachePolicy);
        return executeServiceAction(appController, Actions.FETCH_CHANNELS, b);
    }
}
