package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class UserInteractionsComponent extends NotifiableComponent<Actions, UserInteractionsListener> {

    protected enum Actions {
        FETCH_FOLLOW_RECOMMENDATIONS_FOR_USER,
        FOLLOW,
        BULK_FOLLOW,
        BULK_FOLLOW_TWITTER,
        UNFOLLOW,
        CLOSE_PROMPT,
        BULK_FOLLOW_CHANNELS,
        BULK_UNFOLLOW_CHANNELS,
        UNSUBSCRIBE_ACTION,
        RESUBSCRIBE_ACTION,
        FOLLOW_USER_ON_TWITTER,
        UNFOLLOW_USER_ON_TWITTER
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.FETCH_FOLLOW_RECOMMENDATIONS_FOR_USER, new FetchUserFollowRecommendationsAction(), new FetchUserFollowRecommendationsActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.FOLLOW, new FollowAction(), new FollowActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.BULK_FOLLOW, new BulkFollowAction(), new BulkFollowActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.BULK_FOLLOW_TWITTER, new BulkFollowTwitterAction(), new BulkFollowTwitterActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.UNFOLLOW, new UnfollowAction(), new UnfollowActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.CLOSE_PROMPT, new ClosePromptAction());
        registerAsActionCode(builder, Actions.BULK_FOLLOW_CHANNELS, new BulkFollowChannelsAction(), new BulkFollowChannelsActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.BULK_UNFOLLOW_CHANNELS, new BulkUnfollowChannelsAction());
        registerAsActionCode(builder, Actions.UNSUBSCRIBE_ACTION, new UnsubscribeAction(), new UnsubscribeActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.RESUBSCRIBE_ACTION, new ResubscribeAction(), new ResubscribeActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.FOLLOW_USER_ON_TWITTER, new FollowOnTwitterAction());
        registerAsActionCode(builder, Actions.UNFOLLOW_USER_ON_TWITTER, new UnfollowOnTwitterAction());
    }

    public String followUser(AppController appController, long userIdToFollow, boolean notify, FollowScribeActionsLogger logger) {
        return followUser(appController, userIdToFollow, notify, logger, null);
    }

    public String followUser(AppController appController, long userIdToFollow, boolean notify, FollowScribeActionsLogger logger, VinePost post) {
        if (logger != null) {
            logger.onFollow(userIdToFollow, post);
        }
        Bundle b = appController.createServiceBundle();
        b.putLong("follow_id", userIdToFollow);
        b.putBoolean("notify", notify);
        return executeServiceAction(appController, Actions.FOLLOW, b);
    }

    public String followUserOnTwitter(AppController appController, long userIdToFollow, boolean notify, FollowScribeActionsLogger logger) {
        if (logger != null) {
            logger.onTwitterFollow(userIdToFollow);
        }
        Bundle b = appController.createServiceBundle();
        b.putLong("follow_id", userIdToFollow);
        b.putBoolean("notify", notify);
        return executeServiceAction(appController, Actions.FOLLOW_USER_ON_TWITTER, b);
    }

    public String unfollowUserOnTwitter(AppController appController, long userIdToUnfollow, boolean notify, FollowScribeActionsLogger logger) {
        if (logger != null) {
            logger.onTwitterUnfollow(Long.valueOf(userIdToUnfollow));
        }
        Bundle b = appController.createServiceBundle();
        b.putLong("follow_id", userIdToUnfollow);
        b.putBoolean("notify", notify);
        return executeServiceAction(appController, Actions.UNFOLLOW_USER_ON_TWITTER, b);
    }

    public String bulkFollowUsers(AppController appController, ArrayList<String> userIdsToFollow) {
        Bundle b = AppController.injectServiceBundle(new Bundle(), appController.getActiveSession());
        b.putStringArrayList("follow_ids", userIdsToFollow);
        return executeServiceAction(appController, Actions.BULK_FOLLOW, b);
    }

    public String bulkFollowTwitter(AppController appController, long[] userIdsToFollow, boolean followAll) {
        Bundle b = AppController.injectServiceBundle(new Bundle(), appController.getActiveSession());
        b.putLongArray("follow_ids", userIdsToFollow);
        b.putBoolean("followAll", followAll);
        return executeServiceAction(appController, Actions.BULK_FOLLOW_TWITTER, b);
    }

    public String unfollowUser(AppController appController, long userIdToUnfollow, boolean notify, FollowScribeActionsLogger logger) {
        return unfollowUser(appController, userIdToUnfollow, notify, -1L, logger, null);
    }

    public String unfollowUser(AppController appController, long userIdToUnfollow, boolean notify, FollowScribeActionsLogger logger, VinePost post) {
        return unfollowUser(appController, userIdToUnfollow, notify, -1L, logger, post);
    }

    public String unfollowUser(AppController appController, long userIdToUnfollow, boolean notify, long currentProfileId, FollowScribeActionsLogger logger, VinePost post) {
        if (logger != null) {
            logger.onUnfollow(userIdToUnfollow, post);
        }
        Bundle b = appController.createServiceBundle();
        b.putLong("follow_id", userIdToUnfollow);
        b.putLong("profile_id", currentProfileId);
        b.putLong("user_id", appController.getActiveId());
        b.putBoolean("notify", notify);
        return executeServiceAction(appController, Actions.UNFOLLOW, b);
    }

    public String bulkFollowChannels(AppController appController, ArrayList<String> channelIdsToFollow, boolean notify) {
        return bulkChannelAction(appController, channelIdsToFollow, notify, Actions.BULK_FOLLOW_CHANNELS);
    }

    public String bulkUnfollowChannels(AppController appController, ArrayList<String> channelIdsToUnfollow, boolean notify) {
        return bulkChannelAction(appController, channelIdsToUnfollow, notify, Actions.BULK_UNFOLLOW_CHANNELS);
    }

    private String bulkChannelAction(AppController appController, ArrayList<String> channelIds, boolean notify, Actions action) {
        Bundle b = appController.createServiceBundle();
        b.putStringArrayList("channels", channelIds);
        b.putBoolean("notify", notify);
        return executeServiceAction(appController, action, b);
    }

    public String closePrompt(AppController appController, String reference) {
        Bundle b = appController.createServiceBundle();
        b.putString("reference", reference);
        return executeServiceAction(appController, Actions.CLOSE_PROMPT, b);
    }
}
