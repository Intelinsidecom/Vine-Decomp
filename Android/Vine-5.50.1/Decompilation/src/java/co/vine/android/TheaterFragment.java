package co.vine.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemUtil;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineRepost;
import co.vine.android.client.AppController;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.feedadapter.RequestKeyGetter;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.viewholder.CardViewHolder;
import co.vine.android.feedadapter.viewholder.LongformPostOverlayViewHolder;
import co.vine.android.feedadapter.viewholder.LongformPostOverlayViewManager;
import co.vine.android.feedadapter.viewmanager.ImmersiveViewsPostInfoViewManager;
import co.vine.android.model.ModelEvents;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineItemModel;
import co.vine.android.model.TimelineModel;
import co.vine.android.model.impl.Timeline;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.player.SdkVideoView;
import co.vine.android.scribe.AppNavigationProvider;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.AppStateProvider;
import co.vine.android.scribe.ScribeLogger;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.TimelineItemScribeActionsLogger;
import co.vine.android.scribe.UIEventScribeLogger;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.postactions.PostActionsListener;
import co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener;
import co.vine.android.share.activities.PostShareActivity;
import co.vine.android.util.LinkSuppressor;
import co.vine.android.util.LoopManager;
import co.vine.android.util.MuteUtil;
import co.vine.android.util.ResourceLoader;
import co.vine.android.util.SmartOnGestureListener;
import co.vine.android.util.SparseArray;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import co.vine.android.widget.FloatingLikesView;
import java.util.ArrayList;
import java.util.Iterator;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class TheaterFragment extends BaseFragment {
    private View mActions;
    private TheaterActivity mActivity;
    private String mApiUrl;
    private AppController mAppController;
    private AppNavigationProvider mAppNavProvider;
    private AppStateProvider mAppStateProvider;
    private TextView mCaption;
    private ImageView mCommentIcon;
    private View mCurrentTouchView;
    private SdkVideoView mCurrentVideoView;
    private GestureDetectorCompat mDetector;
    private String mFeedTitle;
    private FloatingLikesView mFloatingLikes;
    private int mIndex;
    private boolean mIsUIVisible;
    private boolean mIsUserInitiated;
    private RequestKeyGetter mKeyGetter;
    private float mLastX;
    private float mLastY;
    private ImageView mLikeIcon;
    private boolean mLongPressing;
    private LoopManager mLoopManager;
    private ModelEvents.ModelListener mModelListener;
    private ImageView mMoreOptionIcon;
    private boolean mMuted;
    private ViewPager mPager;
    private TheaterPagerAdapter mPagerAdapter;
    private PendingRequestHelper mPendingRequestHelper;
    private View mPlaybackView;
    private TimelineItemScribeActionsListener mPostActionLogger;
    private PostActionsListener mPostActionsResponseHandler;
    private ArrayList<VinePost> mPosts;
    private ResourceLoader mResourceLoader;
    private ScribeLogger mScribeLogger;
    private ImageView mShareIcon;
    private ProgressBar mSpinner;
    private long mStartId;
    private int mStartIndex;
    private TimelineDetails mTimelineDetails;
    private TimelineFetchActionsListener mTimelineFetchActionsListener;
    private TextView mTitle;
    private TextView mUsername;
    private ViewGroup mVideoContainer;
    private final Animation mUIFadeIn = new AlphaAnimation(0.0f, 1.0f);
    private final Animation mUIFadeOut = new AlphaAnimation(1.0f, 0.0f);
    private final Animation mUISharpen = new AlphaAnimation(0.5f, 1.0f);
    private final Animation mUIDim = new AlphaAnimation(1.0f, 0.5f);
    private final CountDownTimer mCountDownTimer = new CountDownTimer(3000, 2000) { // from class: co.vine.android.TheaterFragment.1
        @Override // android.os.CountDownTimer
        public void onTick(long millisUntilFinished) {
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            TheaterFragment.this.fadeOutUI();
        }
    };
    private final VideoViewInterface.OnCompletionListener mDefaultCompletionListener = new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.TheaterFragment.2
        @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
        public void onCompletion(VideoViewInterface view) throws Resources.NotFoundException {
            TheaterFragment.this.mIsUserInitiated = false;
            TheaterFragment.this.playNext();
        }
    };
    private final VideoViewInterface.OnCompletionListener mLoopingCompletionListener = new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.TheaterFragment.3
        @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
        public void onCompletion(VideoViewInterface view) throws IllegalStateException {
            TheaterFragment.this.mCurrentVideoView.seekTo(0);
            TheaterFragment.this.mCurrentVideoView.start();
            TheaterFragment.this.mLoopManager.increment(TheaterFragment.this.getCurrentId());
        }
    };
    private LinkSuppressor mUserLinkSuppressor = new LinkSuppressor() { // from class: co.vine.android.TheaterFragment.7
        @Override // co.vine.android.util.LinkSuppressor
        public boolean shouldSuppressUserLink(long id) {
            return TheaterFragment.this.mTimelineDetails.type == 2 && id == TheaterFragment.this.mTimelineDetails.channelId;
        }
    };
    private TimelineClickListenerFactory.Callback mOverlayClickListener = new TimelineClickListenerFactory.Callback() { // from class: co.vine.android.TheaterFragment.12
        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onPlaylistButtonClicked(VinePost post) {
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onLikePost(VinePost post, int position) {
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onUnlikePost(VinePost post) {
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onMoreButtonClicked(TimelineItem timelineItem) {
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onShareClicked(VinePost post) {
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onLongformOverlayClicked(VinePost post, int position) {
            Intent i = LongformActivity.getIntent(TheaterFragment.this.getActivity(), post);
            TheaterFragment.this.startActivityForResult(i, HttpResponseCode.OK);
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onClosePromptClicked(TimelineItem item, CardViewHolder holder) {
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onPromptClicked(TimelineItem item, CardViewHolder holder) {
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onFeedOverlayClicked(VineFeed item) {
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onShareFeedButtonClicked(VineFeed item) {
        }
    };

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        this.mStartId = data.getLong("id");
        this.mFeedTitle = data.getString("title");
        this.mApiUrl = data.getString("url");
        this.mTimelineDetails = (TimelineDetails) data.getParcelable("timeline_details");
        if (this.mTimelineDetails == null) {
            this.mActivity.finish(SmartOnGestureListener.Direction.NONE, getCurrentId());
            return;
        }
        this.mIsUIVisible = true;
        this.mIsUserInitiated = true;
        this.mLongPressing = false;
        this.mPostActionsResponseHandler = new TheaterPostActionsListener();
        this.mModelListener = new LifetimeSafeModelListener(this, new ModelListener());
        this.mTimelineFetchActionsListener = new TimelineFetchActionsListener() { // from class: co.vine.android.TheaterFragment.4
            @Override // co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener
            public void onTimelineFetched(String reqId, int statusCode, String reasonPhrase, int type, int count, boolean memory, boolean userInitiated, int size, String title, UrlCachePolicy cachePolicy, boolean network, Bundle bundle) {
                TheaterFragment.this.mPendingRequestHelper.removeRequest(reqId);
            }

            @Override // co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener
            public void onChannelsFetched(String reqId, int statusCode, String reasonPhrase, Bundle bundle) {
            }
        };
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_theater, container, false);
        this.mPlaybackView = v.findViewById(R.id.playback_linear);
        this.mTitle = (TextView) v.findViewById(R.id.top_username);
        this.mTitle.setText(this.mFeedTitle);
        this.mVideoContainer = (ViewGroup) v.findViewById(R.id.video_container);
        this.mPager = (ViewPager) v.findViewById(R.id.pager);
        this.mFloatingLikes = (FloatingLikesView) v.findViewById(R.id.floating_likes);
        this.mUsername = (TextView) v.findViewById(R.id.username);
        this.mSpinner = (ProgressBar) v.findViewById(R.id.spinner);
        this.mCaption = (TextView) v.findViewById(R.id.caption);
        this.mActions = v.findViewById(R.id.post_viewer_actions);
        this.mLikeIcon = (ImageView) v.findViewById(R.id.like);
        this.mCommentIcon = (ImageView) v.findViewById(R.id.comment);
        this.mShareIcon = (ImageView) v.findViewById(R.id.share_post);
        this.mMoreOptionIcon = (ImageView) v.findViewById(R.id.more_options);
        return v;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivity = (TheaterActivity) getActivity();
        this.mAppController = AppController.getInstance(this.mActivity);
        this.mPendingRequestHelper = new PendingRequestHelper();
        this.mPendingRequestHelper.onCreate(this.mAppController, savedInstanceState);
        this.mKeyGetter = new RequestKeyGetter(this.mActivity, null);
        this.mResourceLoader = new ResourceLoader(this.mActivity, this.mAppController);
        this.mLoopManager = LoopManager.get(this.mActivity);
        this.mMuted = MuteUtil.isMuted(this.mActivity);
        Context appContext = this.mActivity.getApplicationContext();
        this.mAppStateProvider = AppStateProviderSingleton.getInstance(this.mActivity.getApplicationContext());
        this.mAppNavProvider = AppNavigationProviderSingleton.getInstance();
        this.mScribeLogger = ScribeLoggerSingleton.getInstance(appContext);
        this.mPostActionLogger = new TimelineItemScribeActionsLogger(ScribeLoggerSingleton.getInstance(appContext), AppStateProviderSingleton.getInstance(getActivity()), this.mAppNavProvider);
        int screenSize = SystemUtil.getDisplaySize(this.mActivity).x;
        ViewGroup.LayoutParams params = this.mVideoContainer.getLayoutParams();
        params.height = screenSize;
        this.mVideoContainer.setLayoutParams(params);
        ViewGroup.LayoutParams params2 = this.mPager.getLayoutParams();
        params2.height = screenSize;
        this.mPager.setLayoutParams(params2);
        ViewGroup.LayoutParams params3 = this.mFloatingLikes.getLayoutParams();
        params3.height = screenSize;
        this.mFloatingLikes.setLayoutParams(params3);
        setupAnimations();
        this.mDetector = new GestureDetectorCompat(this.mActivity, new TheaterGestureListener());
        View.OnTouchListener listener = new View.OnTouchListener() { // from class: co.vine.android.TheaterFragment.5
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != 1 || !TheaterFragment.this.mLongPressing) {
                    if (TheaterFragment.this.mLongPressing) {
                        return true;
                    }
                } else {
                    TheaterFragment.this.mCountDownTimer.start();
                    TheaterFragment.this.restoreVideo();
                    TheaterFragment.this.mSpinner.setVisibility(4);
                    TheaterFragment.this.mLongPressing = false;
                    TheaterFragment.this.mCurrentVideoView.setOnCompletionListener(TheaterFragment.this.mDefaultCompletionListener);
                }
                TheaterFragment.this.mCurrentTouchView = v;
                return TheaterFragment.this.mDetector.onTouchEvent(event);
            }
        };
        this.mPlaybackView.setOnTouchListener(listener);
        this.mPager.setOnTouchListener(listener);
        this.mLikeIcon.setOnTouchListener(listener);
        this.mCommentIcon.setOnTouchListener(listener);
        this.mShareIcon.setOnTouchListener(listener);
        this.mMoreOptionIcon.setOnTouchListener(listener);
        this.mCaption.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setupAdapter() throws Resources.NotFoundException {
        this.mPagerAdapter = new TheaterPagerAdapter();
        this.mPager.setAdapter(this.mPagerAdapter);
        this.mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: co.vine.android.TheaterFragment.6
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int position) throws IllegalStateException, Resources.NotFoundException {
                if (TheaterFragment.this.mIndex > TheaterFragment.this.mPosts.size() - 5) {
                    TheaterFragment.this.fetchContent(1, true, UrlCachePolicy.FORCE_REFRESH, false);
                }
                if (TheaterFragment.this.mIsUserInitiated && TheaterFragment.this.mIndex != TheaterFragment.this.mStartIndex) {
                    UIEventScribeLogger.onTheaterSwipe(TheaterFragment.this.mScribeLogger, TheaterFragment.this.mAppStateProvider, TheaterFragment.this.mAppNavProvider, position > TheaterFragment.this.mIndex ? UIEventScribeLogger.SwipeDirection.LEFT : UIEventScribeLogger.SwipeDirection.RIGHT);
                    if (!TheaterFragment.this.mIsUIVisible) {
                        TheaterFragment.this.fadeInUI();
                    }
                }
                if (TheaterFragment.this.mIsUIVisible) {
                    TheaterFragment.this.mCountDownTimer.start();
                }
                TheaterFragment.this.mIndex = position;
                TheaterFragment.this.bindData();
                if (TheaterFragment.this.mCurrentVideoView != null) {
                    TheaterFragment.this.mCurrentVideoView.setAutoPlayOnPrepared(false);
                    TheaterFragment.this.mCurrentVideoView.pause();
                    TheaterFragment.this.mCurrentVideoView.seekToDelayed(0, HttpResponseCode.INTERNAL_SERVER_ERROR);
                    TheaterFragment.this.mCurrentVideoView = (SdkVideoView) TheaterFragment.this.mPagerAdapter.views.get(position).findViewById(R.id.sdkVideoView);
                    TheaterFragment.this.play();
                }
                TheaterFragment.this.mIsUserInitiated = true;
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int state) {
            }
        });
        this.mPager.setCurrentItem(this.mIndex, false);
        this.mCountDownTimer.start();
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() throws IllegalStateException {
        super.onPause();
        this.mCountDownTimer.cancel();
        if (this.mCurrentVideoView != null) {
            this.mCurrentVideoView.pause();
            if (this.mLongPressing) {
                this.mLongPressing = false;
                restoreVideo();
                this.mCurrentVideoView.setOnCompletionListener(this.mDefaultCompletionListener);
                this.mSpinner.setVisibility(4);
            }
        }
        Components.postActionsComponent().removeListener(this.mPostActionsResponseHandler);
        VineModelFactory.getModelInstance().getModelEvents().removeListener(this.mModelListener);
        Components.timelineFetchComponent().removeListener(this.mTimelineFetchActionsListener);
        this.mLoopManager.save();
    }

    @Override // co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() throws IllegalStateException, Resources.NotFoundException {
        super.onResume();
        Components.postActionsComponent().addListener(this.mPostActionsResponseHandler);
        VineModelFactory.getModelInstance().getModelEvents().addListener(this.mModelListener);
        Components.timelineFetchComponent().addListener(this.mTimelineFetchActionsListener);
        if (this.mPosts == null) {
            getPosts();
            if (this.mPosts != null) {
                this.mStartIndex = findIndexById(this.mStartId);
                this.mIndex = this.mStartIndex;
                setupAdapter();
                bindData();
            } else {
                return;
            }
        } else {
            getPosts();
            this.mPagerAdapter.notifyDataSetChanged();
        }
        if (!this.mIsUIVisible) {
            tempFadeInUI();
        } else {
            this.mCountDownTimer.start();
        }
        if (this.mCurrentVideoView != null) {
            play();
        }
    }

    private int findIndexById(long id) {
        if (this.mPosts == null) {
            return -1;
        }
        for (int i = 0; i < this.mPosts.size(); i++) {
            if (this.mPosts.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getPosts() {
        TimelineModel timelineModel = VineModelFactory.getModelInstance().getTimelineModel();
        Timeline timeline = timelineModel.getUserTimeline(this.mTimelineDetails);
        if (timeline == null || timeline.itemIds == null || timeline.itemIds.isEmpty()) {
            this.mActivity.finish(SmartOnGestureListener.Direction.NONE, getCurrentId());
            return;
        }
        ArrayList<TimelineItem> items = new ArrayList<>();
        TimelineItemModel timelineItemModel = VineModelFactory.getModelInstance().getTimelineItemModel();
        Iterator<Long> it = timeline.itemIds.iterator();
        while (it.hasNext()) {
            long id = it.next().longValue();
            items.add(timelineItemModel.getTimelineItem(id));
        }
        this.mPosts = TimelineItemUtil.getVinePostsFromItems(items);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (data != null) {
                    long postId = data.getLongExtra("post_id", 0L);
                    long repostId = data.getLongExtra("repost_id", 0L);
                    boolean following = data.getBooleanExtra("following", false);
                    String username = data.getStringExtra("username");
                    long myRepostId = data.getLongExtra("my_repost_id", 0L);
                    if (data.getBooleanExtra("revine", false)) {
                        this.mPendingRequestHelper.addRequest(Components.postActionsComponent().revine(this.mAppController, null, postId, repostId, username));
                        this.mShareIcon.setSelected(true);
                    } else {
                        this.mPendingRequestHelper.addRequest(Components.postActionsComponent().unRevine(this.mAppController, null, postId, myRepostId, repostId, following, true));
                        this.mShareIcon.setSelected(false);
                    }
                }
                if (!this.mIsUIVisible) {
                    tempFadeInUI();
                    break;
                }
                break;
            case HttpResponseCode.OK /* 200 */:
                this.mPagerAdapter.notifyDataSetChanged();
                this.mPager.invalidate();
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindData() throws Resources.NotFoundException {
        int count = this.mPosts.size();
        if (this.mIndex < 0 || this.mIndex >= count) {
            this.mActivity.finish(SmartOnGestureListener.Direction.NONE, -1L);
            return;
        }
        VinePost post = this.mPosts.get(this.mIndex);
        if (post.isRTL == null) {
            post.isRTL = Boolean.valueOf(Util.isRtlLanguage(Util.addDirectionalMarkers(post.description)));
        }
        new ImmersiveViewsPostInfoViewManager(getView().findViewById(R.id.post_info_container)).bindPostData(getActivity(), post, this.mUserLinkSuppressor, new ImmersiveViewsPostInfoViewManager.UsernameClickListener() { // from class: co.vine.android.TheaterFragment.8
            @Override // co.vine.android.feedadapter.viewmanager.ImmersiveViewsPostInfoViewManager.UsernameClickListener
            public void onUsernameClicked(long userId) {
                if (!TheaterFragment.this.mUserLinkSuppressor.shouldSuppressUserLink(userId)) {
                    ChannelActivity.startProfile(TheaterFragment.this.getActivity(), userId, "Theater");
                }
            }
        });
        this.mLikeIcon.setSelected(post.isLiked());
        this.mShareIcon.setSelected(post.isRevined());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindVideo(View v, final int position) throws IllegalStateException {
        final SdkVideoView videoView = (SdkVideoView) v.findViewById(R.id.sdkVideoView);
        final ImageView thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        final VinePost post = this.mPosts.get(position);
        videoView.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.TheaterFragment.9
            @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
            public void onPrepared(VideoViewInterface view) {
                TheaterFragment.this.mPostActionLogger.onPostPlayed(post, position);
                TheaterFragment.this.mLoopManager.increment(post.postId);
            }
        });
        videoView.setOnErrorListener(new VideoViewInterface.OnErrorListener() { // from class: co.vine.android.TheaterFragment.10
            @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
            public boolean onError(VideoViewInterface videoViewInterface, int what, int extra) {
                videoView.setVisibility(8);
                return true;
            }
        });
        videoView.setOnCompletionListener(this.mDefaultCompletionListener);
        videoView.setSurfaceUpdatedListener(new VideoViewInterface.SurfaceUpdatedListener() { // from class: co.vine.android.TheaterFragment.11
            @Override // co.vine.android.embed.player.VideoViewInterface.SurfaceUpdatedListener
            public void onSurfaceUpdated() {
                thumbnail.setVisibility(4);
            }
        });
        videoView.setMute(this.mMuted);
        videoView.setAutoPlayOnPrepared(false);
        this.mResourceLoader.loadVideo(videoView, post.videoUrl, false);
        this.mResourceLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(thumbnail), post.thumbnailUrl, false);
        LongformPostOverlayViewManager.setupOverlay(this.mActivity, new LongformPostOverlayViewHolder(v), post, position, this.mOverlayClickListener);
        if (this.mIndex == this.mStartIndex) {
            this.mCurrentVideoView = videoView;
            play();
            this.mStartIndex = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playNext() throws Resources.NotFoundException {
        this.mIndex++;
        if (this.mIndex < 0 || this.mIndex >= this.mPosts.size()) {
            this.mActivity.finish(SmartOnGestureListener.Direction.NONE, -1L);
        } else {
            this.mPager.setCurrentItem(this.mIndex, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void play() throws IllegalStateException {
        if (this.mCurrentVideoView.isInPlaybackState()) {
            this.mCurrentVideoView.start();
        } else {
            this.mCurrentVideoView.setAutoPlayOnPrepared(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLikeClicked() {
        VinePost post = this.mPosts.get(this.mIndex);
        if (!post.isLiked()) {
            onLike();
            return;
        }
        this.mPendingRequestHelper.addRequest(Components.postActionsComponent().unlikePost(this.mAppController, null, post.postId, post.getVineRepostRepostId(), true));
        post.removeMeLike(this.mAppController.getActiveSession().getUserId());
        this.mLikeIcon.setSelected(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLike() {
        VinePost post = this.mPosts.get(this.mIndex);
        this.mPendingRequestHelper.addRequest(Components.postActionsComponent().likePost(this.mAppController, null, post.postId, post.getVineRepostRepostId(), true));
        post.addMeLike(this.mAppController.getActiveSession());
        this.mPostActionLogger.onPostLiked(post, this.mIndex);
        this.mLikeIcon.setSelected(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCommentClicked() {
        VinePost post = this.mPosts.get(this.mIndex);
        Intent intent = VideoEditTextActivity.getIntent(this.mActivity, post.videoUrl, post, 1);
        this.mActivity.startActivity(intent);
        this.mActivity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onShareClicked() {
        VinePost post = this.mPosts.get(this.mIndex);
        boolean isPostMine = post.userId == this.mAppController.getActiveId();
        Intent shareIntent = PostShareActivity.getPostShareIntent(this.mActivity, post, isPostMine);
        this.mActivity.startActivityForResult(shareIntent, 100);
    }

    public void onMoreOptionClicked() {
        VinePost post = this.mPosts.get(this.mIndex);
        String videoPath = post.videoUrl;
        Intent intent = PostOptionsDialogActivity.getMoreIntentForPost(post, videoPath, getActivity(), this.mAppController.getActiveId(), post.following, false);
        this.mActivity.startActivityForResult(intent, 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVideoTap() throws IllegalStateException {
        if (this.mCurrentVideoView.isPlaying()) {
            this.mCurrentVideoView.pause();
            if (!this.mIsUIVisible) {
                fadeInUI();
            }
            this.mCountDownTimer.cancel();
            return;
        }
        this.mCurrentVideoView.resume();
        if (this.mIsUIVisible) {
            this.mCountDownTimer.start();
        }
    }

    private void setupAnimations() {
        setupAnimation(this.mUIFadeIn, new AccelerateInterpolator(), 300, true);
        setupAnimation(this.mUIFadeOut, new DecelerateInterpolator(), HttpResponseCode.BAD_REQUEST, true);
        setupAnimation(this.mUISharpen, new AccelerateInterpolator(), 300, true);
        setupAnimation(this.mUIDim, new DecelerateInterpolator(), HttpResponseCode.BAD_REQUEST, true);
    }

    private void setupAnimation(Animation animation, Interpolator interpolator, int duration, boolean fillAfter) {
        animation.setInterpolator(interpolator);
        animation.setDuration(duration);
        animation.setFillAfter(fillAfter);
        animation.setAnimationListener(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleUI() {
        if (this.mIsUIVisible) {
            fadeOutUI();
        } else if (this.mCurrentVideoView.isPlaying()) {
            tempFadeInUI();
        } else {
            fadeInUI();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeInUI() {
        this.mCountDownTimer.cancel();
        this.mTitle.startAnimation(this.mUIFadeIn);
        if (this.mUsername.getVisibility() == 0) {
            this.mCaption.startAnimation(this.mUISharpen);
            this.mUsername.startAnimation(this.mUISharpen);
        }
        this.mActions.startAnimation(this.mUIFadeIn);
        this.mMoreOptionIcon.startAnimation(this.mUIFadeIn);
        this.mIsUIVisible = true;
    }

    private void tempFadeInUI() {
        fadeInUI();
        this.mCountDownTimer.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeOutUI() {
        this.mCountDownTimer.cancel();
        this.mTitle.startAnimation(this.mUIFadeOut);
        if (this.mUsername.getVisibility() == 0) {
            this.mCaption.startAnimation(this.mUIDim);
            this.mUsername.startAnimation(this.mUIDim);
        }
        this.mActions.startAnimation(this.mUIFadeOut);
        this.mMoreOptionIcon.startAnimation(this.mUIFadeOut);
        this.mIsUIVisible = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shrinkVideo() {
        this.mCurrentVideoView.animate().scaleX(0.95f).scaleY(0.95f).setDuration(200L).setInterpolator(new LinearInterpolator());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreVideo() {
        this.mCurrentVideoView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200L).setInterpolator(new LinearInterpolator());
    }

    public long getCurrentId() {
        if (this.mPosts == null || this.mIndex < 0 || this.mIndex >= this.mPosts.size()) {
            return -1L;
        }
        return this.mPosts.get(this.mIndex).getId();
    }

    @Override // co.vine.android.BaseFragment
    public AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.PLAYLIST;
    }

    @Override // co.vine.android.BaseFragment
    protected String getTimelineApiUrl() {
        return this.mApiUrl;
    }

    protected void fetchContent(int fetchType, boolean userInitiated, UrlCachePolicy cachePolicy, boolean forceReplacePosts) {
        if (!this.mPendingRequestHelper.hasPendingRequest(fetchType)) {
            Long mChannelId = Long.valueOf(this.mTimelineDetails.channelId);
            this.mPendingRequestHelper.addRequest(Components.timelineFetchComponent().fetchPosts(this.mAppController, this.mAppController.getActiveSession(), 20, mChannelId.longValue(), this.mTimelineDetails.type, userInitiated, String.valueOf(mChannelId), this.mTimelineDetails.sort, null, cachePolicy, forceReplacePosts, -1L, fetchType), fetchType);
        }
    }

    public static TheaterFragment newInstance(TimelineDetails timelineDetails, long id, String title, String url) {
        TheaterFragment fragment = new TheaterFragment();
        Bundle b = new Bundle();
        b.putParcelable("timeline_details", timelineDetails);
        b.putLong("id", id);
        b.putString("title", title);
        b.putString("url", url);
        fragment.setArguments(b);
        return fragment;
    }

    private class TheaterGestureListener extends SmartOnGestureListener {
        private TheaterGestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent e) {
            TheaterFragment.this.mLastX = e.getX();
            TheaterFragment.this.mLastY = e.getY();
            return true;
        }

        @Override // co.vine.android.util.SmartOnGestureListener
        public boolean onSwipe(SmartOnGestureListener.Direction direction) {
            if (direction == SmartOnGestureListener.Direction.UP || direction == SmartOnGestureListener.Direction.DOWN) {
                UIEventScribeLogger.onTheaterExit(TheaterFragment.this.mScribeLogger, TheaterFragment.this.mAppStateProvider, TheaterFragment.this.mAppNavProvider, true);
                TheaterFragment.this.mActivity.finish(direction, TheaterFragment.this.mIndex == -1 ? -1L : TheaterFragment.this.getCurrentId());
                return true;
            }
            return super.onSwipe(direction);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent e) throws IllegalStateException {
            if (TheaterFragment.this.mCurrentTouchView == TheaterFragment.this.mPager) {
                TheaterFragment.this.onVideoTap();
                return true;
            }
            if (TheaterFragment.this.mCurrentTouchView != TheaterFragment.this.mLikeIcon || !TheaterFragment.this.mIsUIVisible) {
                if (TheaterFragment.this.mCurrentTouchView != TheaterFragment.this.mCommentIcon || !TheaterFragment.this.mIsUIVisible) {
                    if (TheaterFragment.this.mCurrentTouchView != TheaterFragment.this.mShareIcon || !TheaterFragment.this.mIsUIVisible) {
                        if (TheaterFragment.this.mCurrentTouchView != TheaterFragment.this.mMoreOptionIcon || !TheaterFragment.this.mIsUIVisible) {
                            TheaterFragment.this.toggleUI();
                            return true;
                        }
                        TheaterFragment.this.onMoreOptionClicked();
                        return true;
                    }
                    TheaterFragment.this.onShareClicked();
                    return true;
                }
                TheaterFragment.this.onCommentClicked();
                return true;
            }
            TheaterFragment.this.onLikeClicked();
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent e) {
            if (TheaterFragment.this.mCurrentTouchView == TheaterFragment.this.mPager) {
                TheaterFragment.this.mFloatingLikes.addLikeAt((int) TheaterFragment.this.mLastX, (int) TheaterFragment.this.mLastY, 0);
                TheaterFragment.this.onLike();
                return true;
            }
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent e) {
            if (TheaterFragment.this.mCurrentVideoView.isPlaying()) {
                if (TheaterFragment.this.mIsUIVisible) {
                    TheaterFragment.this.mCountDownTimer.cancel();
                } else {
                    TheaterFragment.this.fadeInUI();
                }
                TheaterFragment.this.shrinkVideo();
                TheaterFragment.this.mSpinner.setVisibility(0);
                TheaterFragment.this.mLongPressing = true;
                TheaterFragment.this.mCurrentVideoView.setOnCompletionListener(TheaterFragment.this.mLoopingCompletionListener);
                UIEventScribeLogger.onTheaterHoldToReloop(TheaterFragment.this.mScribeLogger, TheaterFragment.this.mAppStateProvider, TheaterFragment.this.mAppNavProvider, (VinePost) TheaterFragment.this.mPosts.get(TheaterFragment.this.mIndex));
            }
        }
    }

    private class TheaterPostActionsListener implements PostActionsListener {
        private TheaterPostActionsListener() {
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onLikePost(String reqId, int statusCode, String reasonPhrase, long postId) {
            PendingRequest req = TheaterFragment.this.mPendingRequestHelper.removeRequest(reqId);
            if (req != null && statusCode != 200) {
                Iterator it = TheaterFragment.this.mPosts.iterator();
                while (it.hasNext()) {
                    VinePost post = (VinePost) it.next();
                    if (post.postId == postId) {
                        post.removeMeLike(TheaterFragment.this.mAppController.getActiveId());
                        return;
                    }
                }
            }
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onUnlikePost(String reqId, int statusCode, String reasonPhrase, long postId) {
            PendingRequest req = TheaterFragment.this.mPendingRequestHelper.removeRequest(reqId);
            if (req != null && statusCode != 200) {
                Iterator it = TheaterFragment.this.mPosts.iterator();
                while (it.hasNext()) {
                    VinePost post = (VinePost) it.next();
                    if (post.postId == postId) {
                        if (statusCode == 404) {
                            post.removeMeLike(TheaterFragment.this.mAppController.getActiveId());
                            return;
                        } else {
                            post.addMeLike(TheaterFragment.this.mAppController.getActiveSession());
                            return;
                        }
                    }
                }
            }
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onRevine(String reqId, int statusCode, String reasonPhrase, long postId, VineRepost repost) {
            PendingRequest req = TheaterFragment.this.mPendingRequestHelper.removeRequest(reqId);
            if (req != null) {
                Iterator it = TheaterFragment.this.mPosts.iterator();
                while (it.hasNext()) {
                    VinePost post = (VinePost) it.next();
                    if (post.postId == postId) {
                        if (statusCode == 200) {
                            post.updateRevined(repost, true);
                            return;
                        } else {
                            post.setFlagRevined(false);
                            return;
                        }
                    }
                }
            }
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onUnrevine(String reqId, int statusCode, String reasonPhrase, long postId) {
            PendingRequest req = TheaterFragment.this.mPendingRequestHelper.removeRequest(reqId);
            if (req != null) {
                Iterator it = TheaterFragment.this.mPosts.iterator();
                while (it.hasNext()) {
                    VinePost post = (VinePost) it.next();
                    if (post.postId == postId) {
                        if (statusCode == 200) {
                            post.updateRevined(null, false);
                        } else {
                            post.setFlagRevined(true);
                        }
                    }
                }
            }
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onPostEditCaption(String reqId, int statusCode, String reasonPhrase) {
        }
    }

    private final class ModelListener implements ModelEvents.ModelListener {
        private ModelListener() {
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTagsAdded(TagModel tagModel, String query) {
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
            if (timelineDetails.equals(TheaterFragment.this.mTimelineDetails)) {
                TheaterFragment.this.getPosts();
                TheaterFragment.this.mPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    private class TheaterPagerAdapter extends PagerAdapter {
        public SparseArray<View> views;

        private TheaterPagerAdapter() {
            this.views = new SparseArray<>();
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup container, int position) throws IllegalStateException {
            View v = LayoutInflater.from(TheaterFragment.this.mActivity).inflate(R.layout.theater_mode_video_view, (ViewGroup) null);
            this.views.put(position, v);
            container.addView(v);
            TheaterFragment.this.bindVideo(v, position);
            return v;
        }

        @Override // android.support.v4.view.PagerAdapter
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = this.views.get(position);
            this.views.remove(position);
            container.removeView(v);
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getItemPosition(Object object) {
            return -1;
        }

        @Override // android.support.v4.view.PagerAdapter
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            if (TheaterFragment.this.mPosts != null) {
                return TheaterFragment.this.mPosts.size();
            }
            return 0;
        }
    }
}
