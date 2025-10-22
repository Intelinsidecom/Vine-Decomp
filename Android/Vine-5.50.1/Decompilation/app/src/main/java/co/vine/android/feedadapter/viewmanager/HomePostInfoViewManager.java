package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import co.vine.android.ChannelActivity;
import co.vine.android.Friendships;
import co.vine.android.R;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.FeedNotifier;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.HomePostInfoViewHolder;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.util.LinkSuppressor;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public final class HomePostInfoViewManager implements ViewManager {
    private final AppController mAppController;
    private final AvatarFollowViewManager mAvatarFollowManager;
    private final AvatarViewManager mAvatarManager;
    private final BylineViewManager mBylineManager;
    protected final Context mContext;
    private final Animation mFadeInAnim;
    private final FeedNotifier mFeedNotifier;
    private final String mFollowEventSource;
    private final Friendships mFriendships;
    private final LoopCounterViewManager mLoopCounterManager;
    private final TimestampViewManager mTimestampManager;
    private final PostUsernameViewManager mUsernameManager;

    public HomePostInfoViewManager(Context context, AppController appController, FeedNotifier feedNotifier, Friendships friendships, FollowScribeActionsLogger followActionsLogger, String followEventSource) {
        this.mFeedNotifier = feedNotifier;
        this.mContext = context;
        this.mAppController = appController;
        this.mFriendships = friendships;
        this.mFollowEventSource = followEventSource;
        this.mBylineManager = new BylineViewManager(this.mContext, this.mAppController, this.mFeedNotifier);
        this.mAvatarManager = new AvatarViewManager(this.mContext, this.mAppController);
        this.mAvatarFollowManager = new AvatarFollowViewManager(this.mAppController, this.mFriendships, followActionsLogger);
        this.mUsernameManager = new PostUsernameViewManager(this.mContext);
        this.mTimestampManager = new TimestampViewManager(this.mContext);
        this.mLoopCounterManager = new LoopCounterViewManager(this.mContext);
        this.mFadeInAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.fade_in);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.POST_HEADER;
    }

    public void bind(HomePostInfoViewHolder h, final VinePost data, int position, int color, final LinkSuppressor suppressor) {
        if (data != null) {
            if (data.byline != null) {
                this.mBylineManager.bind(h.getBylineHolder(), data.byline);
                if (data.byline.entities != null && data.byline.entities.size() > 0) {
                    this.mBylineManager.bindClickListener(h.getBylineHolder(), data.byline.entities.get(0).link);
                }
            } else if (data.repost != null) {
                boolean isYou = data.repost.userId == this.mAppController.getActiveId();
                this.mBylineManager.bind(h.getBylineHolder(), data.repost, isYou);
                if (!isYou) {
                    this.mBylineManager.bindClickListener(h.getBylineHolder(), data.repost.userId);
                }
            } else {
                h.getBylineHolder().container.setVisibility(8);
            }
            this.mAvatarManager.bind(h.getAvatarHolder(), data.avatarUrl, data.isRevined(), color);
            boolean showFollowButton = (data.originalFollowingState || data.userId == AppController.getInstance(this.mContext).getActiveId()) ? false : true;
            boolean followChecked = data.following || this.mFriendships.isFollowing(data.userId);
            this.mAvatarFollowManager.bind(h.getAvatarFollowHolder(), data.userId, showFollowButton, followChecked, color);
            this.mAvatarFollowManager.bindClickListener(h.getAvatarFollowHolder(), h.getAvatarFollowHolder().toggle, data);
            if (!showFollowButton) {
                h.getAvatarHolder().image.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.HomePostInfoViewManager.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (suppressor == null || !suppressor.shouldSuppressUserLink(data.userId)) {
                            ChannelActivity.startProfile(HomePostInfoViewManager.this.mContext, data.userId, HomePostInfoViewManager.this.mFollowEventSource);
                        }
                    }
                });
            } else {
                this.mAvatarFollowManager.bindClickListener(h.getAvatarFollowHolder(), h.getAvatarHolder().image, data);
            }
            this.mUsernameManager.bind(h.getUsernameHolder(), data.username, data.userId, data.styledUserName, suppressor, this.mFollowEventSource);
            boolean hasVenue = (data.venueData == null || TextUtils.isEmpty(data.venueData.venueName)) ? false : true;
            if (data.dateString == null) {
                String dateString = Util.getRelativeTimeString(this.mContext, data.created, false);
                if (hasVenue) {
                    data.dateString = this.mContext.getString(R.string.date_and_location_formatted, dateString, data.venueData.venueName.trim());
                } else {
                    data.dateString = dateString;
                }
            }
            this.mTimestampManager.bind(h.getTimestampHolder(), data.dateString, hasVenue);
            this.mTimestampManager.bindClickListener(h.getTimestampHolder(), suppressor, data.foursquareVenueId, data.venueData);
            this.mLoopCounterManager.bind(h.getLoopCounterHolder(), data.postId, data.loops, data.velocity, data.lastRefresh, data.onFire, data.created);
        }
    }

    protected void adjustLoopCount(HomePostInfoViewHolder h, int newCount) {
        this.mLoopCounterManager.adjustLoopCount(h.getLoopCounterHolder(), newCount);
    }
}
