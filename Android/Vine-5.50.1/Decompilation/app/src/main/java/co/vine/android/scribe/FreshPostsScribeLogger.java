package co.vine.android.scribe;

import co.vine.android.api.VinePost;
import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.Item;
import co.vine.android.scribe.util.ScribeUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FreshPostsScribeLogger {
    public static void logFreshPosts(ScribeLogger logger, AppStateProvider appStateProvider, List<VinePost> freshPosts, long lastLaunchFetchTimestampMS) {
        ClientEvent event = logger.getDefaultClientEvent();
        event.eventType = "fresh_posts";
        event.appState = appStateProvider.getAppState();
        ArrayList<Item> postItems = new ArrayList<>();
        for (VinePost post : freshPosts) {
            postItems.add(ScribeUtils.getItemFromPost(post));
        }
        event.eventDetails.items = postItems;
        event.appState = appStateProvider.getAppState();
        event.appState.lastLaunchTimestamp = Double.valueOf(lastLaunchFetchTimestampMS / 1000.0d);
        logger.logClientEvent(event);
    }
}
