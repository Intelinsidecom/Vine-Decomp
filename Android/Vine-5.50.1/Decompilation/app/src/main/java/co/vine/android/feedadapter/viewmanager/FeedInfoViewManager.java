package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.view.View;
import co.vine.android.ChannelActivity;
import co.vine.android.Friendships;
import co.vine.android.api.FeedMetadata;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.FeedNotifier;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.FeedInfoViewHolder;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.util.LinkSuppressor;

/* loaded from: classes.dex */
public class FeedInfoViewManager implements ViewManager {
    private final AppController mAppController;
    private final AvatarFollowViewManager mAvatarFollowManager;
    private final AvatarViewManager mAvatarManager;
    private final BylineViewManager mBylineManager;
    protected final Context mContext;
    private final FeedNotifier mFeedNotifier;
    private final String mFollowEventSource;
    private final Friendships mFriendships;
    private final PostUsernameViewManager mUsernameManager;

    public FeedInfoViewManager(Context context, AppController appController, FeedNotifier feedNotifier, Friendships friendships, FollowScribeActionsLogger followActionsLogger, String followEventSource) {
        this.mFeedNotifier = feedNotifier;
        this.mContext = context;
        this.mAppController = appController;
        this.mFriendships = friendships;
        this.mFollowEventSource = followEventSource;
        this.mBylineManager = new BylineViewManager(this.mContext, this.mAppController, this.mFeedNotifier);
        this.mAvatarManager = new AvatarViewManager(this.mContext, this.mAppController);
        this.mAvatarFollowManager = new AvatarFollowViewManager(this.mAppController, this.mFriendships, followActionsLogger);
        this.mUsernameManager = new PostUsernameViewManager(this.mContext);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.FEED_HEADER;
    }

    public void bind(FeedInfoViewHolder h, final VineFeed data, int position, int color, final LinkSuppressor suppressor) {
        if (data != null && data.feedMetadata != null) {
            this.mBylineManager.bind(h.getBylineHolder(), data);
            this.mBylineManager.bindClickListener(h.getBylineHolder(), data.userId);
            if (data.feedMetadata.feedType.equals(FeedMetadata.FeedType.PROFILE)) {
                VineUser sharedUser = data.feedMetadata.userProfile;
                if (sharedUser != null) {
                    this.mAvatarManager.bind(h.getAvatarHolder(), sharedUser.avatarUrl, false, color);
                    boolean showFollowButton = sharedUser.userId != AppController.getInstance(this.mContext).getActiveId();
                    boolean followChecked = sharedUser.following == 1 || this.mFriendships.isFollowing(sharedUser.userId);
                    this.mAvatarFollowManager.bind(h.getAvatarFollowHolder(), sharedUser.userId, showFollowButton, followChecked, color);
                    if (!showFollowButton) {
                        h.getAvatarHolder().image.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.FeedInfoViewManager.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View v) {
                                if (suppressor == null || !suppressor.shouldSuppressUserLink(data.userId)) {
                                    ChannelActivity.startProfile(FeedInfoViewManager.this.mContext, data.userId, FeedInfoViewManager.this.mFollowEventSource);
                                }
                            }
                        });
                    } else {
                        this.mAvatarFollowManager.bindClickListener(h.getAvatarFollowHolder(), data);
                    }
                    this.mUsernameManager.bind(h.getUsernameHolder(), sharedUser.username, sharedUser.userId, null, suppressor, this.mFollowEventSource);
                    return;
                }
                return;
            }
            VineChannel vineChannel = data.feedMetadata.channel;
            if (vineChannel != null) {
                this.mAvatarManager.bind(h.getAvatarHolder(), vineChannel.iconFullUrl, false, color);
                boolean followChecked2 = vineChannel.following || this.mFriendships.isChannelFollowing(vineChannel.channelId);
                this.mAvatarFollowManager.bind(h.getAvatarFollowHolder(), vineChannel.channelId, true, followChecked2, color);
                this.mAvatarFollowManager.bindClickListener(h.getAvatarFollowHolder(), data);
                this.mUsernameManager.bind(h.getUsernameHolder(), vineChannel.channel, vineChannel, null);
            }
        }
    }
}
