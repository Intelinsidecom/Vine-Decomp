package co.vine.android.scribe;

import co.vine.android.TimelineItemScribeActionsListener;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.VineMosaic;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineSolicitor;
import co.vine.android.api.VineUrlAction;
import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.Item;
import co.vine.android.scribe.model.ItemPosition;
import co.vine.android.scribe.util.ScribeUtils;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class TimelineItemScribeActionsLogger implements TimelineItemScribeActionsListener {
    private final AppNavigationProvider mAppNavProvider;
    private final AppStateProvider mAppStateProvider;
    private final ScribeLogger mLogger;

    public TimelineItemScribeActionsLogger(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        this.mAppStateProvider = appStateProvider;
        this.mAppNavProvider = appNavProvider;
        this.mLogger = logger;
    }

    @Override // co.vine.android.TimelineItemScribeActionsListener
    public void onMosaicViewed(VineMosaic mosaic, int position) {
        onTimelineItemEvent(mosaic, position, "mosaic_viewed");
    }

    @Override // co.vine.android.TimelineItemScribeActionsListener
    public void onPostPlayed(VinePost post, int position) {
        onTimelineItemEvent(post, position, "post_played");
    }

    @Override // co.vine.android.TimelineItemScribeActionsListener
    public void onPostLiked(VinePost post, int position) {
        onTimelineItemEvent(post, position, "like");
    }

    @Override // co.vine.android.TimelineItemScribeActionsListener
    public void onTimelineItemClicked(TimelineItem item, int position) {
        onTimelineItemEvent(item, position, "item_selected");
    }

    @Override // co.vine.android.TimelineItemScribeActionsListener
    public void onTimelineItemDismissed(TimelineItem item, int position) {
        onTimelineItemEvent(item, position, "item_dismissed");
    }

    private void onTimelineItemEvent(TimelineItem item, int position, String eventType) {
        ClientEvent event = this.mLogger.getDefaultClientEvent();
        event.appState = this.mAppStateProvider.getAppState();
        event.navigation = this.mAppNavProvider.getAppNavigation();
        event.eventType = eventType;
        event.eventDetails.items = new ArrayList();
        Item scribeItem = null;
        switch (item.getType()) {
            case POST:
                VinePost post = (VinePost) item;
                scribeItem = ScribeUtils.getItemFromPost(post);
                event.navigation.timelineApiUrl = post.originUrl;
                break;
            case POST_MOSAIC:
                VineMosaic mosaic = (VineMosaic) item;
                scribeItem = ScribeUtils.getItemFromPostMosaic(mosaic);
                event.navigation.timelineApiUrl = mosaic.originUrl;
                break;
            case USER_MOSAIC:
                VineMosaic mosaic2 = (VineMosaic) item;
                scribeItem = ScribeUtils.getItemFromUserMosaic(mosaic2);
                event.navigation.timelineApiUrl = mosaic2.originUrl;
                break;
            case URL_ACTION:
                VineUrlAction urlAction = (VineUrlAction) item;
                scribeItem = ScribeUtils.getItemFromUrlAction(urlAction);
                event.navigation.timelineApiUrl = urlAction.originUrl;
                break;
            case SOLICITOR:
                VineSolicitor solicitor = (VineSolicitor) item;
                scribeItem = ScribeUtils.getItemFromSolicitor(solicitor);
                event.navigation.timelineApiUrl = solicitor.originUrl;
                break;
        }
        if (scribeItem != null) {
            scribeItem.position = new ItemPosition();
            scribeItem.position.offset = Integer.valueOf(position);
            event.eventDetails.items.add(scribeItem);
        }
        this.mLogger.logClientEvent(event);
    }
}
