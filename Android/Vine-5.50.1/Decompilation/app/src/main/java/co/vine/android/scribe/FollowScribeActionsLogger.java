package co.vine.android.scribe;

import co.vine.android.api.VinePost;
import co.vine.android.api.VineUser;
import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.Item;
import co.vine.android.scribe.util.ScribeUtils;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class FollowScribeActionsLogger {
    protected final AppNavigationProvider mAppNavProvider;
    protected final AppStateProvider mAppStateProvider;
    protected final ScribeLogger mLogger;

    public FollowScribeActionsLogger(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        this.mAppStateProvider = appStateProvider;
        this.mAppNavProvider = appNavProvider;
        this.mLogger = logger;
    }

    public void onFollow(long userId, VinePost post) {
        onFollowEvent(userId, "follow", post);
    }

    public void onUnfollow(long userId, VinePost post) {
        onFollowEvent(userId, "unfollow", post);
    }

    public void onFollowAll(ArrayList<VineUser> users) {
        onFollowAllEvent(users, "follow_all");
    }

    public void onUnfollowAll(ArrayList<VineUser> users) {
        onFollowAllEvent(users, "unfollow_all");
    }

    public void onTwitterFollow(long userId) {
        onFollowEvent(userId, "twitter_follow", null);
    }

    public void onTwitterUnfollow(Long userId) {
        onFollowEvent(userId.longValue(), "twitter_unfollow", null);
    }

    private void onFollowEvent(long userId, String eventType, VinePost post) {
        ClientEvent event = getEvent(eventType);
        event.eventDetails.items = new ArrayList();
        Item userItem = ScribeUtils.getItemFromUserId(userId);
        event.eventDetails.items.add(userItem);
        if (post != null) {
            Item postItem = ScribeUtils.getItemFromPost(post);
            event.eventDetails.items.add(postItem);
            event.navigation.timelineApiUrl = post.originUrl;
        }
        this.mLogger.logClientEvent(event);
    }

    private void onFollowAllEvent(ArrayList<VineUser> users, String eventType) {
        ClientEvent event = getEvent(eventType);
        event.eventDetails.items = new ArrayList();
        Iterator<VineUser> it = users.iterator();
        while (it.hasNext()) {
            VineUser user = it.next();
            Item userItem = ScribeUtils.getItemFromUserId(user.userId);
            event.eventDetails.items.add(userItem);
        }
        this.mLogger.logClientEvent(event);
    }

    protected ClientEvent getEvent(String eventType) {
        ClientEvent event = this.mLogger.getDefaultClientEvent();
        event.appState = this.mAppStateProvider.getAppState();
        event.navigation = this.mAppNavProvider.getAppNavigation();
        event.eventType = eventType;
        return event;
    }
}
