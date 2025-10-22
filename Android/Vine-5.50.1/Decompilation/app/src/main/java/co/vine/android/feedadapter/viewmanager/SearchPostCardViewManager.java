package co.vine.android.feedadapter.viewmanager;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import co.vine.android.ActionableFeedComponent;
import co.vine.android.Friendships;
import co.vine.android.R;
import co.vine.android.Settings;
import co.vine.android.TimelineItemScribeActionsListener;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VinePost;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.ViewGroupHelper;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedAdapterTouchListenerFactory;
import co.vine.android.feedadapter.v2.FeedNotifier;
import co.vine.android.feedadapter.v2.FeedVideoController;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.v2.LoopIncrementer;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.CardViewHolder;
import co.vine.android.feedadapter.viewholder.SearchPostCardViewHolder;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.widget.SensitiveAcknowledgments;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;
import com.google.common.android.base.android.Optional;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/* loaded from: classes.dex */
public final class SearchPostCardViewManager extends CardViewManager implements ActionableFeedComponent {
    private final Activity mContext;
    private final FeedNotifier mFeedNotifier;
    private final HomePostFooterViewManager mFooterManager;
    private final HomePostInfoViewManager mHeaderManager;
    private final FeedAdapterItems mItems;
    private int mProfileColor;
    private final SearchTitleViewManager mTitleManager;
    private final PostVideoViewManager mVideoManager;
    private final FeedViewHolderCollection mViewHolders;

    private SearchPostCardViewManager(Builder builder) {
        this.mItems = builder.items;
        this.mViewHolders = builder.viewHolders;
        this.mContext = builder.context;
        this.mFeedNotifier = builder.feedNotifier;
        AppController appController = AppController.getInstance(this.mContext);
        SensitiveAcknowledgments sensitiveAcknowledgments = new SensitiveAcknowledgments(100);
        FeedVideoController videoController = new FeedVideoController(this.mContext, builder.logger, this.mItems, new ViewGroupHelper(builder.listView), sensitiveAcknowledgments, this.mViewHolders);
        this.mTitleManager = new SearchTitleViewManager(this.mContext, builder.searchListener);
        this.mHeaderManager = new HomePostInfoViewManager(this.mContext, appController, this.mFeedNotifier, builder.friendships, builder.followActionsLogger, builder.followEventSource);
        this.mVideoManager = new PostVideoViewManager(this.mContext, appController, builder.callback, this.mItems, this.mViewHolders, videoController, sensitiveAcknowledgments, new LoopIncrementer(this.mContext, videoController, this.mItems), new FeedAdapterTouchListenerFactory(videoController, this.mContext, builder.callback), null, builder.postPlayedListener, builder.logger);
        this.mFooterManager = new HomePostFooterViewManager(this.mContext, appController, builder.callback);
        this.mProfileColor = this.mContext.getResources().getColor(R.color.vine_green);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.POST;
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public View newView(int position, View view, ViewGroup viewGroup) {
        if (view == null || !(view.getTag() instanceof SearchPostCardViewHolder)) {
            view = this.mContext.getLayoutInflater().inflate(R.layout.post_row_home_view, viewGroup, false);
            SearchPostCardViewHolder tag = new SearchPostCardViewHolder(view);
            view.setTag(tag);
            this.mViewHolders.add(new WeakReference<>(tag));
        }
        SearchPostCardViewHolder h = (SearchPostCardViewHolder) view.getTag();
        TimelineItem item = this.mItems.getItem(position);
        if (item != null && item.getType() == TimelineItemType.POST) {
            VinePost post = (VinePost) item;
            this.mVideoManager.init(h.getVideoHolder(), post);
            post.deregisterActionableComponent();
            h.post = Optional.of((VinePost) item);
            post.registerActionableComponent(this);
            h.position = position;
            bind(h, h.post.get(), position);
        } else {
            SLog.e("Item at position: " + position + " is not a post!");
        }
        return view;
    }

    public void bind(SearchPostCardViewHolder h, VinePost data, int position) {
        if (data != null) {
            this.mTitleManager.bind(h.getTitleHolder(), position == 0);
            this.mHeaderManager.bind(h.getHeaderHolder(), data, position, this.mProfileColor, null);
            this.mVideoManager.bind(h.getVideoHolder(), data, position);
            this.mFooterManager.bind(h.getFooterHolder(), data, h.position, this.mProfileColor, this.mProfileColor, null);
        }
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void resetStates(boolean hasDataSetChanged) {
        this.mVideoManager.resetStates(hasDataSetChanged);
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onPause(boolean focused) {
        this.mVideoManager.onPause();
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onFocusChanged(boolean focused) {
        this.mVideoManager.onFocusChanged(focused);
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onResume(boolean focused) {
        this.mVideoManager.onResume(focused);
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onDestroy() {
        this.mVideoManager.onDestroy();
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onDestroyView() {
        this.mVideoManager.onDestroyView();
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onScrollIdle() {
        this.mVideoManager.onScrollIdle();
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onVideoImageObtained() {
        this.mVideoManager.onVideoImageObtained();
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
        this.mVideoManager.onVideoPathObtained(videos);
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public boolean isPlaying() {
        return this.mVideoManager.isPlaying();
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public CardViewHolder getValidViewHolder(int position) {
        return this.mVideoManager.getValidViewHolder(position);
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void pausePlayer() {
        this.mVideoManager.pausePlayer();
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void setProfileColor(int color) throws Resources.NotFoundException {
        if (color == Settings.DEFAULT_PROFILE_COLOR || color <= 0) {
            color = this.mContext.getResources().getColor(R.color.vine_green);
        }
        if (this.mProfileColor != color) {
            this.mProfileColor = (-16777216) | color;
        }
    }

    @Override // co.vine.android.feedadapter.viewmanager.CardViewManager
    public void toggleMute(boolean mute) {
        this.mVideoManager.toggleMute(mute);
    }

    @Override // co.vine.android.ActionableFeedComponent
    public void adjustLoopCount(VinePost data, int newCount) {
        CardViewHolder postHolder = this.mViewHolders.getViewHolderForPostId(data.postId);
        if (postHolder != null && postHolder.getType() == ViewType.POST) {
            this.mHeaderManager.adjustLoopCount(((SearchPostCardViewHolder) postHolder).getHeaderHolder(), newCount);
        }
    }

    @Override // co.vine.android.ActionableFeedComponent
    public void updateLikedIcon(VinePost data, boolean liked) {
        CardViewHolder postHolder = this.mViewHolders.getViewHolderForPostId(data.postId);
        if (postHolder != null && postHolder.getType() == ViewType.POST) {
            this.mFooterManager.updateLikedIcon(((SearchPostCardViewHolder) postHolder).getFooterHolder(), data, liked);
        }
    }

    @Override // co.vine.android.ActionableFeedComponent
    public void updateRevinedIcon(VinePost data, boolean revined) {
        CardViewHolder postHolder = this.mViewHolders.getViewHolderForPostId(data.postId);
        if (postHolder != null && postHolder.getType() == ViewType.POST) {
            this.mFooterManager.updateRevinedIcon(((SearchPostCardViewHolder) postHolder).getFooterHolder(), data, revined);
        }
    }

    public static class Builder {
        private TimelineClickListenerFactory.Callback callback;
        private Activity context;
        private FeedNotifier feedNotifier;
        private FollowScribeActionsLogger followActionsLogger;
        private String followEventSource;
        private Friendships friendships;
        private FeedAdapterItems items;
        private ListView listView;
        private SLogger logger;
        private TimelineItemScribeActionsListener postPlayedListener;
        private View.OnClickListener searchListener;
        private FeedViewHolderCollection viewHolders;

        public SearchPostCardViewManager build() {
            if (this.feedNotifier == null || this.items == null || this.viewHolders == null || this.listView == null || this.context == null || this.callback == null || this.friendships == null || this.logger == null) {
                throw new IllegalArgumentException("One or more required variables are null");
            }
            return new SearchPostCardViewManager(this);
        }

        public Builder notifier(FeedNotifier notifier) {
            this.feedNotifier = notifier;
            return this;
        }

        public Builder items(FeedAdapterItems items) {
            this.items = items;
            return this;
        }

        public Builder viewHolders(FeedViewHolderCollection viewHolders) {
            this.viewHolders = viewHolders;
            return this;
        }

        public Builder listView(ListView listView) {
            this.listView = listView;
            return this;
        }

        public Builder context(Activity context) {
            this.context = context;
            return this;
        }

        public Builder callback(TimelineClickListenerFactory.Callback callback) {
            this.callback = callback;
            return this;
        }

        public Builder friendships(Friendships friendships) {
            this.friendships = friendships;
            return this;
        }

        public Builder searchListener(View.OnClickListener listener) {
            this.searchListener = listener;
            return this;
        }

        public Builder followActionsLogger(FollowScribeActionsLogger followActionsLogger) {
            this.followActionsLogger = followActionsLogger;
            return this;
        }

        public Builder postPlayedListener(TimelineItemScribeActionsListener listener) {
            this.postPlayedListener = listener;
            return this;
        }

        public Builder logger(SLogger logger) {
            this.logger = logger;
            return this;
        }

        public Builder followEventSource(String followEventSource) {
            this.followEventSource = followEventSource;
            return this;
        }
    }
}
