package co.vine.android.feedadapter.viewmanager;

import android.view.View;
import co.vine.android.Friendships;
import co.vine.android.api.FeedMetadata;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.AvatarFollowViewHolder;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.service.components.Components;

/* loaded from: classes.dex */
public class AvatarFollowViewManager implements ViewManager {
    private final AppController mAppController;
    private final FollowScribeActionsLogger mFollowActionsLogger;
    private final Friendships mFriendships;

    public AvatarFollowViewManager(AppController appController, Friendships friendships, FollowScribeActionsLogger followActionsLogger) {
        this.mAppController = appController;
        this.mFollowActionsLogger = followActionsLogger;
        this.mFriendships = friendships;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.AVATAR;
    }

    public void bind(AvatarFollowViewHolder h, long userId, boolean shouldShow, boolean checked, int color) {
        if (h.toggle != null && userId > 0) {
            if (shouldShow) {
                h.button.setVisibility(0);
                h.toggle.setChecked(checked);
                h.toggle.setTag(Long.valueOf(userId));
                h.toggle.setCheckedColorStyle(Integer.valueOf(color));
                h.toggle.setUncheckedColorStyle(Integer.valueOf(color));
                return;
            }
            h.button.setVisibility(8);
        }
    }

    public void bindClickListener(final AvatarFollowViewHolder h, View toggle, final VinePost post) {
        toggle.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.AvatarFollowViewManager.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                long userId = post.userId;
                if (h.toggle.isChecked()) {
                    Components.userInteractionsComponent().followUser(AvatarFollowViewManager.this.mAppController, userId, true, AvatarFollowViewManager.this.mFollowActionsLogger, post);
                    AvatarFollowViewManager.this.mFriendships.addFollowing(userId);
                } else {
                    Components.userInteractionsComponent().unfollowUser(AvatarFollowViewManager.this.mAppController, userId, true, AvatarFollowViewManager.this.mFollowActionsLogger, post);
                    AvatarFollowViewManager.this.mFriendships.removeFollowing(userId);
                }
            }
        });
    }

    public void bindClickListener(final AvatarFollowViewHolder h, final VineFeed feedItem) {
        h.toggle.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.AvatarFollowViewManager.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (feedItem.feedMetadata.feedType.equals(FeedMetadata.FeedType.PROFILE)) {
                    Long sharedUserId = Long.valueOf(feedItem.feedMetadata.userProfile.userId);
                    if (h.toggle.isChecked()) {
                        Components.userInteractionsComponent().followUser(AvatarFollowViewManager.this.mAppController, sharedUserId.longValue(), true, AvatarFollowViewManager.this.mFollowActionsLogger, null);
                        AvatarFollowViewManager.this.mFriendships.addFollowing(sharedUserId.longValue());
                        return;
                    } else {
                        Components.userInteractionsComponent().unfollowUser(AvatarFollowViewManager.this.mAppController, sharedUserId.longValue(), true, AvatarFollowViewManager.this.mFollowActionsLogger, null);
                        AvatarFollowViewManager.this.mFriendships.removeFollowing(sharedUserId.longValue());
                        return;
                    }
                }
                Long channelId = Long.valueOf(feedItem.feedMetadata.channel.channelId);
                boolean follow = h.toggle.isChecked();
                AvatarFollowViewManager.this.mAppController.followChannel(channelId.longValue(), follow);
                if (follow) {
                    AvatarFollowViewManager.this.mFriendships.addChannelFollowing(channelId.longValue());
                } else {
                    AvatarFollowViewManager.this.mFriendships.removeChannelFollowing(channelId.longValue());
                }
            }
        });
    }
}
