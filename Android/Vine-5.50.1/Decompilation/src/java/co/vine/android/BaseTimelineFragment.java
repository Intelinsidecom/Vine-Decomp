package co.vine.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.PostOptionsDialogActivity;
import co.vine.android.api.FeedMetadata;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.TimelineItemUtil;
import co.vine.android.api.VineComment;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VineMosaic;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineUrlAction;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppSessionListener;
import co.vine.android.feedadapter.RequestKeyGetter;
import co.vine.android.feedadapter.TimelineActionResponseHandler;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.TimelineScrollListener;
import co.vine.android.feedadapter.v2.FeedAdapter;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedNotifier;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.viewholder.CardViewHolder;
import co.vine.android.feedadapter.viewmanager.CardViewManager;
import co.vine.android.feedadapter.viewmanager.FeedCardViewManager;
import co.vine.android.feedadapter.viewmanager.HomePostCardViewManager;
import co.vine.android.feedadapter.viewmanager.SolicitorCardViewManager;
import co.vine.android.feedadapter.viewmanager.SuggestedFeedCardViewManager;
import co.vine.android.feedadapter.viewmanager.SuggestedFeedVideoGridCardViewManager;
import co.vine.android.feedadapter.viewmanager.SuggestedUsersCardViewManager;
import co.vine.android.feedadapter.viewmanager.UrlActionCardViewManager;
import co.vine.android.network.HttpResult;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.TimelineItemScribeActionsLogger;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.PendingAction;
import co.vine.android.service.VineService;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.remoterequestcontrol.RemoteRequestControlActionListener;
import co.vine.android.share.activities.FeedShareActivity;
import co.vine.android.share.activities.PostShareActivity;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.LinkBuilderUtil;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.LinkSuppressor;
import co.vine.android.util.MuteUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.OnTopViewBoundListener;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class BaseTimelineFragment extends BaseArrayListFragment {
    protected String mApiUrl;
    private AppCompatActivity mCallback;
    private boolean mDismissPostMosaic;
    protected FeedAdapter mFeedAdapter;
    protected FeedNotifier mFeedNotifier;
    protected boolean mFetched;
    protected String mFlurryEventSource;
    protected FollowScribeActionsLogger mFollowScribeActionsLogger;
    protected Friendships mFriendships;
    private RequestKeyGetter mKeyGetter;
    protected int mLastFetchType;
    protected final SLogger mLogger;
    protected final BroadcastReceiver mMergePostReceiver;
    private boolean mMergePostReceiverRegistered;
    private final BroadcastReceiver mMuteChangeReceiver;
    private final RemoteRequestControlActionListener mRemoteRequestActionListener;
    private long mStartLoadingTime;
    private TimelineActionResponseHandler mTimelineActionResponseHandler;
    protected TimelineClickListenerFactory.Callback mTimelineClickEventCallback;
    protected TimelineItemScribeActionsListener mTimelineItemScribeActionsLogger;

    protected abstract String fetchPosts(int i, String str, String str2, boolean z, UrlCachePolicy urlCachePolicy);

    public BaseTimelineFragment() {
        String logTag = getClass().getSimpleName();
        this.mLogger = SLogger.getCachedEnabledLogger(logTag.replace("Fragment", ""));
        this.mMergePostReceiverRegistered = false;
        this.mRemoteRequestActionListener = new RemoteRequestControlActionListener() { // from class: co.vine.android.BaseTimelineFragment.1
            @Override // co.vine.android.service.components.remoterequestcontrol.RemoteRequestControlActionListener
            public void onAbortAllRequestsComplete() {
                BaseTimelineFragment.this.hideProgress(2);
                BaseTimelineFragment.this.fetchContent(2, false, UrlCachePolicy.CACHE_THEN_NETWORK, false);
            }
        };
        this.mMergePostReceiver = new BroadcastReceiver() { // from class: co.vine.android.BaseTimelineFragment.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                }
            }
        };
        this.mMuteChangeReceiver = new BroadcastReceiver() { // from class: co.vine.android.BaseTimelineFragment.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    CrashUtil.log("Something wrong has happened");
                } else {
                    boolean mute = intent.getAction().equals(MuteUtil.ACTION_CHANGED_TO_MUTE);
                    BaseTimelineFragment.this.mFeedAdapter.toggleMute(mute);
                }
            }
        };
        this.mTimelineClickEventCallback = new TimelineClickListenerFactory.Callback() { // from class: co.vine.android.BaseTimelineFragment.6
            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onPlaylistButtonClicked(VinePost post) {
            }

            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onLikePost(VinePost post, int position) {
                Activity activity = BaseTimelineFragment.this.getActivity();
                if (activity != null) {
                    if (!BaseTimelineFragment.this.mAppController.isLoggedIn()) {
                        StartActivity.toStart(activity);
                        return;
                    }
                    BaseTimelineFragment.this.addRequest(Components.postActionsComponent().likePost(BaseTimelineFragment.this.mAppController, null, post.postId, post.getVineRepostRepostId(), false));
                    post.addMeLike(BaseTimelineFragment.this.mAppController.getActiveSession());
                    if (BaseTimelineFragment.this.mTimelineItemScribeActionsLogger != null) {
                        BaseTimelineFragment.this.mTimelineItemScribeActionsLogger.onPostLiked(post, position);
                    }
                }
            }

            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onUnlikePost(VinePost post) {
                BaseTimelineFragment.this.addRequest(Components.postActionsComponent().unlikePost(BaseTimelineFragment.this.mAppController, null, post.postId, post.getVineRepostRepostId(), false));
                post.removeMeLike(BaseTimelineFragment.this.mAppController.getActiveId());
            }

            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onMoreButtonClicked(TimelineItem timelineItem) {
                Activity activity = BaseTimelineFragment.this.getActivity();
                if (timelineItem instanceof VinePost) {
                    VinePost post = (VinePost) timelineItem;
                    String videoPath = BaseTimelineFragment.this.mFeedAdapter.getVideoPathForPostId(post.postId);
                    Intent intent = PostOptionsDialogActivity.getMoreIntentForPost(post, videoPath, activity, BaseTimelineFragment.this.mAppController.getActiveId(), post.following, false);
                    activity.startActivityForResult(intent, 10);
                    return;
                }
                if (timelineItem instanceof VineFeed) {
                    VineFeed feed = (VineFeed) timelineItem;
                    Intent intent2 = PostOptionsDialogActivity.getMoreIntentForFeed(feed, activity, BaseTimelineFragment.this.mAppController.getActiveId());
                    activity.startActivityForResult(intent2, 10);
                }
            }

            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onShareClicked(VinePost post) {
                Activity activity = BaseTimelineFragment.this.getActivity();
                if (activity != null) {
                    boolean isPostMine = post.userId == BaseTimelineFragment.this.mAppController.getActiveId();
                    Intent shareIntent = PostShareActivity.getPostShareIntent(activity, post, isPostMine);
                    activity.startActivityForResult(shareIntent, 20);
                }
            }

            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onClosePromptClicked(TimelineItem item, CardViewHolder holder) {
                if (item.getType().equals(TimelineItemType.POST_MOSAIC)) {
                    BaseTimelineFragment.this.mDismissPostMosaic = true;
                    if (BaseTimelineFragment.this.getActivity() instanceof HomeTabActivity) {
                        ((HomeTabActivity) BaseTimelineFragment.this.getActivity()).setPostMosaicDismissed(true);
                    }
                }
                BaseTimelineFragment.this.mFeedAdapter.removeItem(item.getId());
                if (BaseTimelineFragment.this.mTimelineItemScribeActionsLogger != null) {
                    BaseTimelineFragment.this.mTimelineItemScribeActionsLogger.onTimelineItemDismissed(item, holder.position);
                }
            }

            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onPromptClicked(TimelineItem item, CardViewHolder holder) {
                if (BaseTimelineFragment.this.mTimelineItemScribeActionsLogger != null) {
                    BaseTimelineFragment.this.mTimelineItemScribeActionsLogger.onTimelineItemClicked(item, holder.position);
                }
                String url = "";
                switch (AnonymousClass8.$SwitchMap$co$vine$android$api$TimelineItemType[item.getType().ordinal()]) {
                    case 1:
                    case 2:
                        url = ((VineMosaic) item).link;
                        break;
                    case 3:
                        url = ((VineUrlAction) item).actionLink;
                        break;
                    case 4:
                        SolicitorActivity.start(BaseTimelineFragment.this.getActivity());
                        break;
                }
                if (url != null && !"".equals(url)) {
                    LinkDispatcher.dispatch(url, BaseTimelineFragment.this.getActivity());
                }
            }

            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onLongformOverlayClicked(VinePost post, int position) {
                Intent i = LongformActivity.getIntent(BaseTimelineFragment.this.getActivity(), post);
                BaseTimelineFragment.this.getActivity().startActivityForResult(i, 50);
            }

            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onFeedOverlayClicked(VineFeed item) {
                if (item != null && item.feedMetadata != null) {
                    if (item.feedMetadata.feedType.equals(FeedMetadata.FeedType.PROFILE)) {
                        ChannelActivity.startProfile(BaseTimelineFragment.this.getActivity(), item.feedMetadata.userProfile.userId, "FeedTimelineItem");
                    } else {
                        ChannelActivity.startExploreChannel(BaseTimelineFragment.this.getActivity(), item.feedMetadata.channel, false);
                    }
                }
            }

            @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
            public void onShareFeedButtonClicked(VineFeed item) {
                if (item != null && item.coverPost != null) {
                    VinePost post = item.coverPost;
                    RequestKeyGetter keyGetter = new RequestKeyGetter(BaseTimelineFragment.this.getActivity(), BaseTimelineFragment.this.mLogger);
                    VideoKey videoKey = keyGetter.getRequestKey(post, false);
                    VideoKey lowVideoKey = keyGetter.getRequestKey(post, true);
                    String remoteVideoUrl = post.videoUrl;
                    long channelId = item.feedMetadata.feedType == FeedMetadata.FeedType.CHANNEL ? item.feedMetadata.channel.channelId : item.feedMetadata.userProfile.userId;
                    Intent shareIntent = FeedShareActivity.getIntent(BaseTimelineFragment.this.getActivity(), post.postId, post.shareUrl, post.username, post.thumbnailUrl, remoteVideoUrl, videoKey, lowVideoKey, item.feedMetadata.feedType, channelId);
                    BaseTimelineFragment.this.getActivity().startActivityForResult(shareIntent, 21);
                }
            }
        };
    }

    public void registerMergePostReceiver() {
        this.mMergePostReceiverRegistered = true;
        getActivity().registerReceiver(this.mMergePostReceiver, VineService.SHOW_POST_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(this.mMuteChangeReceiver, MuteUtil.MUTE_INTENT_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
        Components.postActionsComponent().addListener(this.mTimelineActionResponseHandler);
        Components.feedActionsComponent().addListener(this.mTimelineActionResponseHandler);
        Components.remoteRequestControlComponent().addListener(this.mRemoteRequestActionListener);
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.mMuteChangeReceiver);
        Components.postActionsComponent().removeListener(this.mTimelineActionResponseHandler);
        Components.feedActionsComponent().removeListener(this.mTimelineActionResponseHandler);
        Components.remoteRequestControlComponent().removeListener(this.mRemoteRequestActionListener);
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("friendships")) {
                this.mFriendships = (Friendships) savedInstanceState.getParcelable("friendships");
            }
        } else {
            this.mFriendships = new Friendships();
        }
        setAppSessionListener(new TimeLineSessionListener());
        this.mApiUrl = LinkBuilderUtil.buildUrl(1, null);
        if (getActivity() instanceof HomeTabActivity) {
            this.mDismissPostMosaic = ((HomeTabActivity) getActivity()).getPostMosaicDismissed();
        }
    }

    protected void setFlurryEventSource(String source) {
        this.mFlurryEventSource = source;
    }

    public boolean onBackPressed() {
        return this.mFeedAdapter.onBackPressed();
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AppCompatActivity) {
            this.mCallback = (AppCompatActivity) activity;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity instanceof HomeTabActivity) {
            setUpUnderNavHeader();
        }
        this.mKeyGetter = new RequestKeyGetter(activity, this.mLogger);
        this.mTimelineItemScribeActionsLogger = new TimelineItemScribeActionsLogger(ScribeLoggerSingleton.getInstance(getActivity().getApplicationContext()), AppStateProviderSingleton.getInstance(getActivity()), AppNavigationProviderSingleton.getInstance());
        this.mFollowScribeActionsLogger = FollowScribeActionsLoggerSingleton.getInstance(ScribeLoggerSingleton.getInstance(getActivity().getApplicationContext()), AppStateProviderSingleton.getInstance(getActivity()), AppNavigationProviderSingleton.getInstance());
        final FeedAdapterItems adapterItems = new FeedAdapterItems(this.mLogger);
        FeedViewHolderCollection viewHolderCollection = new FeedViewHolderCollection();
        this.mFeedNotifier = new FeedNotifier() { // from class: co.vine.android.BaseTimelineFragment.4
        };
        ArrayList<CardViewManager> viewManagers = getViewManagers(activity, this.mFeedNotifier, adapterItems, viewHolderCollection);
        this.mFeedAdapter = new FeedAdapter(activity, this.mListView, viewManagers, this.mLogger, adapterItems, viewHolderCollection);
        this.mTimelineActionResponseHandler = new TimelineActionResponseHandler(adapterItems, this.mAppController, this.mPendingRequestHelper, viewHolderCollection, this.mFeedAdapter);
        this.mAdapter = this.mFeedAdapter;
        this.mListView.setOnScrollListener(new BaseTimelineScrollListener(this.mFeedAdapter));
    }

    protected LinkSuppressor getLinkSuppressor() {
        return new LinkSuppressor() { // from class: co.vine.android.BaseTimelineFragment.5
        };
    }

    protected OnTopViewBoundListener getOnTopViewBoundListener() {
        return null;
    }

    public ArrayList<CardViewManager> getViewManagers(Activity activity, FeedNotifier feedNotifier, FeedAdapterItems adapterItems, FeedViewHolderCollection viewHolderCollection) {
        ArrayList<CardViewManager> viewManagers = new ArrayList<>();
        HomePostCardViewManager.Builder postViewManagerBuilder = new HomePostCardViewManager.Builder();
        postViewManagerBuilder.items(adapterItems).notifier(feedNotifier).viewHolders(viewHolderCollection).listView(this.mListView).context(activity).callback(this.mTimelineClickEventCallback).friendships(this.mFriendships).linkSuppressor(getLinkSuppressor()).onTopViewBoundListener(getOnTopViewBoundListener()).followActionsLogger(this.mFollowScribeActionsLogger).postPlayedListener(this.mTimelineItemScribeActionsLogger).logger(this.mLogger).followEventSource(this.mFlurryEventSource);
        viewManagers.add(postViewManagerBuilder.build());
        if (ClientFlagsHelper.suggestedFeedVideoGrid(getActivity())) {
            SuggestedFeedVideoGridCardViewManager.Builder suggestedViewManagerBuilder = new SuggestedFeedVideoGridCardViewManager.Builder();
            suggestedViewManagerBuilder.items(adapterItems).viewHolders(viewHolderCollection).context(activity).callback(this.mTimelineClickEventCallback).mosaicLogger(this.mTimelineItemScribeActionsLogger).onTopViewBoundListener(getOnTopViewBoundListener());
            viewManagers.add(suggestedViewManagerBuilder.build());
        } else {
            SuggestedFeedCardViewManager.Builder suggestedViewManagerBuilder2 = new SuggestedFeedCardViewManager.Builder();
            suggestedViewManagerBuilder2.items(adapterItems).viewHolders(viewHolderCollection).context(activity).callback(this.mTimelineClickEventCallback).mosaicLogger(this.mTimelineItemScribeActionsLogger).onTopViewBoundListener(getOnTopViewBoundListener());
            viewManagers.add(suggestedViewManagerBuilder2.build());
        }
        SuggestedUsersCardViewManager.Builder suggestedUserViewManagerBuilder = new SuggestedUsersCardViewManager.Builder();
        suggestedUserViewManagerBuilder.items(adapterItems).viewHolders(viewHolderCollection).context(activity).callback(this.mTimelineClickEventCallback).mosaicLogger(this.mTimelineItemScribeActionsLogger).onTopViewBoundListener(getOnTopViewBoundListener());
        viewManagers.add(suggestedUserViewManagerBuilder.build());
        UrlActionCardViewManager.Builder urlActionViewManagerBuilder = new UrlActionCardViewManager.Builder();
        urlActionViewManagerBuilder.items(adapterItems).viewHolders(viewHolderCollection).context(activity).callback(this.mTimelineClickEventCallback).onTopViewBoundListener(getOnTopViewBoundListener());
        viewManagers.add(urlActionViewManagerBuilder.build());
        SolicitorCardViewManager.Builder solicitorViewManagerBuilder = new SolicitorCardViewManager.Builder();
        solicitorViewManagerBuilder.items(adapterItems).viewHolders(viewHolderCollection).context(activity).callback(this.mTimelineClickEventCallback).onTopViewBoundListener(getOnTopViewBoundListener());
        viewManagers.add(solicitorViewManagerBuilder.build());
        FeedCardViewManager.Builder feedCardViewManagerBuilder = new FeedCardViewManager.Builder();
        feedCardViewManagerBuilder.items(adapterItems).notifier(feedNotifier).viewHolders(viewHolderCollection).listView(this.mListView).context(activity).callback(this.mTimelineClickEventCallback).friendships(this.mFriendships).linkSuppressor(getLinkSuppressor()).onTopViewBoundListener(getOnTopViewBoundListener()).followActionsLogger(this.mFollowScribeActionsLogger).postPlayedListener(this.mTimelineItemScribeActionsLogger).logger(this.mLogger).followEventSource(this.mFlurryEventSource);
        viewManagers.add(feedCardViewManagerBuilder.build());
        return viewManagers;
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("friendships", this.mFriendships);
    }

    protected void startLoadingTimeProfiling() {
        this.mStartLoadingTime = System.currentTimeMillis();
    }

    /* renamed from: co.vine.android.BaseTimelineFragment$8, reason: invalid class name */
    static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$co$vine$android$api$TimelineItemType = new int[TimelineItemType.values().length];

        static {
            try {
                $SwitchMap$co$vine$android$api$TimelineItemType[TimelineItemType.POST_MOSAIC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$co$vine$android$api$TimelineItemType[TimelineItemType.USER_MOSAIC.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$co$vine$android$api$TimelineItemType[TimelineItemType.URL_ACTION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$co$vine$android$api$TimelineItemType[TimelineItemType.SOLICITOR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public View setUpUnderNavHeader() {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.under_nav_header, (ViewGroup) null);
        View header = v.findViewById(R.id.ptr_body);
        ((RefreshableListView) this.mListView).setRefreshHeader(header, getResources().getDimensionPixelSize(R.dimen.tabbar_height));
        this.mListView.addHeaderView(v, null, false);
        return v;
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onDestroyView() {
        this.mFeedAdapter.onDestroyView();
        if (this.mMergePostReceiverRegistered) {
            getActivity().unregisterReceiver(this.mMergePostReceiver);
        }
        super.onDestroyView();
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveTo(int oldPosition) {
        super.onMoveTo(oldPosition);
        if (this.mAdapter.isEmpty()) {
            fetchContent(3, true, UrlCachePolicy.CACHE_THEN_NETWORK, false);
        }
        this.mFeedAdapter.onResume(isFocused());
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveAway(int newPosition) {
        super.onMoveAway(newPosition);
        this.mFeedAdapter.onFocusChanged(isFocused());
    }

    protected void scrollTo(final int position) {
        this.mListView.post(new Runnable() { // from class: co.vine.android.BaseTimelineFragment.7
            @Override // java.lang.Runnable
            public void run() {
                BaseTimelineFragment.this.mListView.setSelection(position);
            }
        });
    }

    protected void fetchContent(int fetchType, boolean userInitiated, UrlCachePolicy cachePolicy, boolean forceReplacePosts) {
        this.mLastFetchType = fetchType;
        if (!hasPendingRequest(fetchType)) {
            this.mFetched = true;
            switch (fetchType) {
                case 2:
                case 3:
                case 4:
                    this.mNextPage = 1;
                    this.mAnchor = null;
                    this.mBackAnchor = null;
                    break;
            }
            if (userInitiated) {
                showProgress(fetchType);
            }
            addRequest(fetchPosts(this.mNextPage, this.mAnchor, this.mBackAnchor, userInitiated, cachePolicy), fetchType);
        }
    }

    @Override // co.vine.android.BaseArrayListFragment
    protected void refresh() {
        hideProgress(2);
        fetchContent(2, true, UrlCachePolicy.FORCE_REFRESH, false);
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        VinePost post;
        switch (requestCode) {
            case 10:
                PostOptionsDialogActivity.Result result = PostOptionsDialogActivity.processActivityResult(this.mAppController, getActivity(), resultCode, data, this.mFollowScribeActionsLogger);
                if (result.intent != null && result.intent.getBooleanExtra("show_less_user", false)) {
                    refresh();
                    break;
                } else {
                    processPostOptionsResult(result);
                    break;
                }
                break;
            case 20:
                if (resultCode == -1 && data != null) {
                    addRequest(ShareRequestHandler.handlePostShareResult(data, this.mAppController));
                    break;
                }
                break;
            case 21:
                if (data != null) {
                    String comment = data.getStringExtra("comment");
                    long coverPostId = data.getLongExtra("coverPostId", -1L);
                    boolean postToVine = data.getBooleanExtra("postToVine", true);
                    boolean postToTwitter = data.getBooleanExtra("postToTwitter", false);
                    boolean postToFacebook = data.getBooleanExtra("postToFacebook", false);
                    boolean postToTumblr = data.getBooleanExtra("postToTumblr", false);
                    long channelId = data.getLongExtra("channelId", -1L);
                    FeedMetadata.FeedType feedType = (FeedMetadata.FeedType) data.getSerializableExtra("feedType");
                    String tumblrOAuthToken = null;
                    String tumblrOAuthSecret = null;
                    if (postToTumblr) {
                        tumblrOAuthToken = data.getStringExtra("tumblrOauthToken");
                        tumblrOAuthSecret = data.getStringExtra("tumblrOauthSecret");
                    }
                    addRequest(Components.feedActionsComponent().shareFeed(this.mAppController, null, channelId, comment, feedType, coverPostId, postToVine, postToTwitter, postToFacebook, postToTumblr, tumblrOAuthToken, tumblrOAuthSecret));
                    break;
                }
                break;
            case 30:
                if (data != null && data.getBooleanExtra("reported", false)) {
                    long userId = data.getLongExtra("userId", 0L);
                    this.mFeedAdapter.removeAllFromUser(userId);
                    break;
                }
                break;
            case 40:
                if (data != null && (post = (VinePost) data.getParcelableExtra("post")) != null) {
                    this.mFeedAdapter.updateCurrentItem(post);
                    break;
                }
                break;
            case 50:
                if (data != null && data.getBooleanExtra("reported", false)) {
                    long userId2 = data.getLongExtra("userId", 0L);
                    this.mFeedAdapter.removeAllFromUser(userId2);
                }
                this.mFeedAdapter.notifyDataSetChanged();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        try {
            super.onDestroy();
            this.mFeedAdapter.onDestroy();
        } catch (Exception e) {
            CrashUtil.logException(e, "Failed to remove timeline fragment", new Object[0]);
        }
    }

    protected void fetchInitialRequest(UrlCachePolicy cachePolicy) {
        fetchContent(3, cachePolicy.mCacheTakesPriority, cachePolicy, false);
    }

    class TimeLineSessionListener extends AppSessionListener {
        TimeLineSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
            BaseTimelineFragment.this.mFeedAdapter.onVideoPathObtained(videos);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onVideoPathError(VideoKey key, HttpResult result) {
            SLog.e("Download of video failed: " + result.reasonPhrase + " key: " + key.url);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageError(ImageKey key, HttpResult result) {
            SLog.e("Download of image failed: " + result.reasonPhrase + " key: " + key.url);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            BaseTimelineFragment.this.mFeedAdapter.setImages(images);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetTimeLineComplete(String reqId, int statusCode, String reasonPhrase, int type, int count, boolean memory, ArrayList<TimelineItem> items, String tag, int pageType, int next, int previous, String anchor, String backAnchor, boolean userInitiated, int size, String title, UrlCachePolicy cachePolicy, boolean network, Bundle bundle) {
            PendingRequest req = BaseTimelineFragment.this.removeRequest(reqId);
            boolean forceReplacePosts = bundle.getBoolean("replace_posts", false);
            TimelineItemUtil.getVinePostsFromItems(items);
            if (req != null) {
                if (memory && statusCode == 200) {
                    if (SLog.sLogsOn && BaseTimelineFragment.this.mStartLoadingTime > 0) {
                        SLog.i("Time since started loading to end fetch: {}ms", Long.valueOf(System.currentTimeMillis() - BaseTimelineFragment.this.mStartLoadingTime));
                    }
                    if (items != null) {
                        BaseTimelineFragment.this.mNextPage = next;
                        BaseTimelineFragment.this.mAnchor = anchor;
                        BaseTimelineFragment.this.mBackAnchor = backAnchor;
                        if (req.fetchType == 1) {
                            BaseTimelineFragment.this.mFeedAdapter.addOlderItems(items);
                        } else if ((cachePolicy.mNetworkDataAllowed && !cachePolicy.mUseExpiredDataAllowedIfNetworkIsDown) || forceReplacePosts) {
                            BaseTimelineFragment.this.mFeedAdapter.replaceItems(items);
                        } else if (!cachePolicy.mNetworkDataAllowed) {
                            BaseTimelineFragment.this.mFeedAdapter.mergeItems(items);
                        } else {
                            BaseTimelineFragment.this.onTimelineUpdatesCameIn(items);
                        }
                        if (BaseTimelineFragment.this.mDismissPostMosaic) {
                            BaseTimelineFragment.this.mFeedAdapter.removePostMosaic();
                        }
                    }
                }
                if (!cachePolicy.mNetworkDataAllowed) {
                    BaseTimelineFragment.this.fetchInitialRequest(UrlCachePolicy.NETWORK_THEN_CACHE);
                    if (statusCode != 200) {
                        return;
                    }
                }
                switch (req.fetchType) {
                    case 3:
                        if (BaseTimelineFragment.this.mAdapter != null && BaseTimelineFragment.this.mAdapter.isEmpty() && count == 0) {
                            BaseTimelineFragment.this.hideProgress(req.fetchType);
                            BaseTimelineFragment.this.showSadface(true);
                            break;
                        } else {
                            BaseTimelineFragment.this.hideSadface();
                            break;
                        }
                        break;
                    default:
                        BaseTimelineFragment.this.hideProgress(req.fetchType);
                        break;
                }
            }
            if (!TextUtils.isEmpty(title) && BaseTimelineFragment.this.mCallback.getSupportActionBar() != null) {
                BaseTimelineFragment.this.mCallback.getSupportActionBar().setTitle(title);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetCommentsComplete(String reqId, int statusCode, String reasonPhrase, int nextPage, String anchor, ArrayList<VineComment> comments) {
            super.onGetCommentsComplete(reqId, statusCode, reasonPhrase, nextPage, anchor, comments);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onTrimMemory(int level) {
            super.onTrimMemory(level);
            if (BaseTimelineFragment.this.mFeedAdapter != null) {
                BaseTimelineFragment.this.mFeedAdapter.onTrimMemory(level);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onLowMemory() {
            super.onLowMemory();
            if (BaseTimelineFragment.this.mFeedAdapter != null) {
                BaseTimelineFragment.this.mFeedAdapter.onLowMemory();
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onReportPostComplete(String reqId, int statusCode, String reasonPhrase, long postId) {
            PendingRequest req = BaseTimelineFragment.this.removeRequest(reqId);
            if (req != null) {
                if (statusCode == 200) {
                    Util.showCenteredToast(BaseTimelineFragment.this.getActivity(), R.string.post_reported);
                } else {
                    Util.showCenteredToast(BaseTimelineFragment.this.getActivity(), R.string.post_reported_error);
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onDeletePostComplete(String reqId, long postId, int statusCode, String reasonPhrase) {
            PendingRequest req = BaseTimelineFragment.this.removeRequest(reqId);
            if (req != null) {
                if (statusCode == 200) {
                    Util.showCenteredToast(BaseTimelineFragment.this.getActivity(), R.string.post_deleted);
                } else {
                    Util.showCenteredToast(BaseTimelineFragment.this.getActivity(), R.string.post_deleted_error);
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onHidePostComplete(String reqId, long postId, int statusCode, String reasonPhrase) {
            BaseTimelineFragment.this.removeRequest(reqId);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onCaptchaRequired(String reqId, String captchaUrl, PendingAction action) {
            PendingRequest req = BaseTimelineFragment.this.removeRequest(reqId);
            if (req != null) {
                BaseTimelineFragment.this.mPendingRequestHelper.onCaptchaRequired(BaseTimelineFragment.this.getActivity(), reqId, action.actionCode, action.bundle, captchaUrl);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhoneVerificationRequired(String reqId, String verifyMsg, PendingAction action) {
            PendingRequest req = BaseTimelineFragment.this.removeRequest(reqId);
            if (req != null) {
                BaseTimelineFragment.this.mPendingRequestHelper.onPhoneVerificationRequired(BaseTimelineFragment.this.getActivity(), reqId, action.actionCode, action.bundle, verifyMsg);
            }
        }
    }

    protected void onTimelineUpdatesCameIn(ArrayList<TimelineItem> items) {
        this.mFeedAdapter.mergeItems(items);
    }

    protected class BaseTimelineScrollListener extends TimelineScrollListener {
        public BaseTimelineScrollListener(FeedAdapter feedAdapter) {
            super(feedAdapter);
        }

        @Override // co.vine.android.feedadapter.ArrayListScrollListener
        protected void onScrollLastItem(int totalItemCount) {
            super.onScrollLastItem(totalItemCount);
            if (BaseTimelineFragment.this.mRefreshable && BaseTimelineFragment.this.mNextPage > 0 && totalItemCount <= 400) {
                this.mFeedAdapter.onFocusChanged(BaseTimelineFragment.this.isFocused());
                BaseTimelineFragment.this.fetchContent(1, true, UrlCachePolicy.NETWORK_THEN_CACHE, false);
                BaseTimelineFragment.this.showProgress(1);
                FlurryUtils.trackTimeLinePageScroll(getClass().getName(), BaseTimelineFragment.this.mNextPage);
            }
        }
    }

    @Override // co.vine.android.BaseFragment
    public AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.TIMELINE;
    }

    @Override // co.vine.android.BaseFragment
    protected String getTimelineApiUrl() {
        return this.mApiUrl;
    }
}
