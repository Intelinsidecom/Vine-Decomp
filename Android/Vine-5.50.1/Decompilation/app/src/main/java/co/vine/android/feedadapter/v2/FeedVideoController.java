package co.vine.android.feedadapter.v2;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import co.vine.android.R;
import co.vine.android.RemixAttributions;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineSource;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.embed.player.VideoViewHelper;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.feedadapter.AttributionAdapter;
import co.vine.android.feedadapter.RequestKeyGetter;
import co.vine.android.feedadapter.ViewGroupHelper;
import co.vine.android.feedadapter.viewholder.CardViewHolder;
import co.vine.android.feedadapter.viewholder.PostViewHolder;
import co.vine.android.feedadapter.viewholder.TimelineItemVideoViewHolder;
import co.vine.android.feedadapter.viewholder.ViewHolder;
import co.vine.android.player.HasVideoPlayerAdapter;
import co.vine.android.player.SdkVideoView;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.SensitiveAcknowledgments;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class FeedVideoController implements VideoViewInterface.OnTextureReadinessChangedListener, HasVideoPlayerAdapter {
    private final AppController mAppController;
    private final Context mContext;
    private Animation mFadeIn;
    private Animation mFadeOut;
    private boolean mHasFocus;
    private final FeedAdapterItems mItems;
    private VideoViewInterface mLastPlayer;
    private final SLogger mLogger;
    private boolean mMuted;
    private VideoViewInterface mPlayingPlayer;
    private final RequestKeyGetter mRequestKeyGetter;
    private final SensitiveAcknowledgments mSensitiveAcknowledgments;
    private Animation mSlideIn;
    private final ViewGroupHelper<CardViewHolder> mViewGroupHelper;
    private final FeedViewHolderCollection mViewHolderCollection;
    private final Handler mHandler = new Handler();
    private final PlayCurrentPositionRunnable mPlayCurrentPositionRunnable = new PlayCurrentPositionRunnable();
    public int mCurrentPlayerCount = 0;
    private volatile int mCurrentPlaying = Integer.MIN_VALUE;
    private int mMaxPlayer = 10;
    private boolean mNeedReleaseOtherPlayers = false;
    private long mShouldBePlayingPostId = -2147483648L;

    public FeedVideoController(Context context, SLogger logger, FeedAdapterItems items, ViewGroupHelper viewGroupHelper, SensitiveAcknowledgments sensitiveAcknowledgments, FeedViewHolderCollection viewHolderCollection) {
        this.mContext = context;
        this.mLogger = logger;
        this.mItems = items;
        this.mViewGroupHelper = viewGroupHelper;
        this.mSensitiveAcknowledgments = sensitiveAcknowledgments;
        this.mViewHolderCollection = viewHolderCollection;
        this.mAppController = AppController.getInstance(context);
        this.mRequestKeyGetter = new RequestKeyGetter(context, logger);
        this.mFadeOut = AnimationUtils.loadAnimation(this.mContext, R.anim.fade_out);
        this.mFadeIn = AnimationUtils.loadAnimation(this.mContext, R.anim.fade_in);
        this.mSlideIn = AnimationUtils.loadAnimation(this.mContext, R.anim.slide_in_from_bottom);
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public int getCurrentPosition() {
        return this.mCurrentPlaying;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public synchronized void playFileAtPosition(int position, boolean isVideoLoaded) {
        VinePost post;
        boolean newView = true;
        synchronized (this) {
            if (position >= 0) {
                this.mLogger.i("Playing item: {}", Integer.valueOf(position));
                CardViewHolder viewHolderForPosition = this.mViewHolderCollection.getViewHolderForPosition(position);
                if (viewHolderForPosition != 0 && (viewHolderForPosition.getType() == ViewType.POST || viewHolderForPosition.getType() == ViewType.FEED)) {
                    TimelineItemVideoViewHolder h = ((PostViewHolder) viewHolderForPosition).getVideoHolder();
                    VideoViewInterface currentVideoView = h.video;
                    if (currentVideoView != this.mLastPlayer) {
                        this.mLogger.i("Pause previous player.");
                        pauseCurrentPlayer();
                    }
                    if (!viewHolderForPosition.post.isPresent() && (!viewHolderForPosition.feed.isPresent() || viewHolderForPosition.feed.get().coverPost == null)) {
                        this.mLogger.i("Post was cleared.");
                    } else {
                        if (viewHolderForPosition.post.isPresent()) {
                            post = viewHolderForPosition.post.get();
                        } else {
                            post = viewHolderForPosition.feed.get().coverPost;
                        }
                        String newPath = this.mItems.getPath(post.postId);
                        if (newPath == null) {
                            this.mLogger.i("Path was cleared.");
                        } else {
                            hideAttributionViews(h);
                            if (!currentVideoView.isPathPlaying(newPath)) {
                                if (SystemUtil.isSinglePlayerEnabled(this.mContext)) {
                                    if (SdkVideoView.getCurrentUri() != null) {
                                        newView = false;
                                    }
                                } else if (currentVideoView.getPath() != null) {
                                    newView = false;
                                }
                                if (!newView) {
                                    suspendPlayer(h.video);
                                    h.frameCount = 0;
                                    h.video.setVisibility(8);
                                }
                                Drawable bg = h.bottomThumbnail.getBackground();
                                ViewUtil.setBackground(h.topThumbnail, bg);
                                h.topThumbnail.setVisibility(0);
                                h.video.setSurfaceUpdatedListener(h);
                                currentVideoView = h.video;
                                currentVideoView.setMute(this.mMuted);
                                if (this.mNeedReleaseOtherPlayers && this.mCurrentPlayerCount >= this.mMaxPlayer) {
                                    releaseOtherPlayers(h.video);
                                }
                                currentVideoView.setAutoPlayOnPrepared(true);
                                this.mLogger.i("Start playing by set path.");
                                currentVideoView.setTag(Long.valueOf(post.postId));
                                CrashUtil.log("Setting video path in feed for post {}", Long.valueOf(post.postId));
                                currentVideoView.setVideoPath(newPath);
                                currentVideoView.setTag(CrossConstants.RES_CACHE_TAG_KEY, this.mItems.getKey(post.postId));
                                if (!currentVideoView.isAvailable() && currentVideoView.getVisibility() != 0) {
                                    currentVideoView.setVisibility(0);
                                }
                                this.mCurrentPlayerCount++;
                            } else {
                                if (!h.video.isAvailable()) {
                                    h.video.setVisibility(0);
                                }
                                currentVideoView.setMute(this.mMuted);
                                if (currentVideoView.isInPlaybackState()) {
                                    if (currentVideoView.isPaused() && currentVideoView == this.mLastPlayer) {
                                        this.mLogger.i("Current video was intentionally paused..");
                                    } else {
                                        this.mLogger.i("Start playing by calling start.");
                                        resumePlayer(currentVideoView);
                                    }
                                } else {
                                    this.mLogger.i("Start playing by set path.");
                                    currentVideoView.setTag(Long.valueOf(post.postId));
                                    CrashUtil.log("Setting video path in feed for post {}", Long.valueOf(post.postId));
                                    currentVideoView.setVideoPath(newPath);
                                }
                            }
                            currentVideoView.setPlayingPosition(position);
                            h.listener.setPosition(position);
                            if (h.imageListener != null) {
                                h.imageListener.setPosition(position);
                                h.imageListener.setPostId(post.postId);
                            }
                            SLog.i("Make current player {}", Integer.valueOf(position));
                            this.mCurrentPlaying = position;
                            this.mLastPlayer = currentVideoView;
                            showAttributionIcons(h, post);
                        }
                    }
                }
            }
        }
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void pausePlayer(VideoViewInterface player) {
        if (player != null) {
            try {
                player.pause();
            } catch (IllegalStateException e) {
            }
            onPlayerStoppedPlaying(player);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void showAttributions() {
        CardViewHolder currentViewHolder;
        VinePost post;
        if (ClientFlagsHelper.multiAttributionsView(this.mContext) && (currentViewHolder = getCurrentViewHolder()) != 0) {
            if (!currentViewHolder.post.isPresent() && (!currentViewHolder.feed.isPresent() || currentViewHolder.feed.get().coverPost == null)) {
                this.mLogger.i("Post was cleared.");
                return;
            }
            if (currentViewHolder.post.isPresent()) {
                post = currentViewHolder.post.get();
            } else {
                post = currentViewHolder.feed.get().coverPost;
            }
            TimelineItemVideoViewHolder h = ((PostViewHolder) currentViewHolder).getVideoHolder();
            ArrayList<RemixAttributions> attributions = new ArrayList<>();
            ArrayList<VineSource> sources = post.sources;
            if (sources != null && !sources.isEmpty()) {
                Collections.sort(sources, new Comparator<VineSource>() { // from class: co.vine.android.feedadapter.v2.FeedVideoController.1
                    @Override // java.util.Comparator
                    public int compare(VineSource first, VineSource second) {
                        return first.getContentType() - second.getContentType();
                    }
                });
                Iterator<VineSource> it = sources.iterator();
                while (it.hasNext()) {
                    VineSource source = it.next();
                    attributions.add(new RemixAttributions(source));
                }
            }
            if (post.audioMetadata != null && !post.audioMetadata.isEmpty()) {
                attributions.add(new RemixAttributions(post.audioMetadata.get(0)));
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext);
            layoutManager.setOrientation(1);
            layoutManager.setStackFromEnd(true);
            h.attributions.setLayoutManager(layoutManager);
            if (attributions.size() < 5) {
                ViewGroup.LayoutParams params = h.attributions.getLayoutParams();
                params.height = ((int) TypedValue.applyDimension(1, 62.0f, this.mContext.getResources().getDisplayMetrics())) * attributions.size();
                ViewGroup.LayoutParams dim_params = h.dimOverlay.getLayoutParams();
                dim_params.height = params.height;
                h.attributions.setLayoutParams(params);
                h.dimOverlay.setLayoutParams(dim_params);
            }
            if (!attributions.isEmpty()) {
                showAttributionViews(h);
                AttributionAdapter adapter = new AttributionAdapter(this.mContext, attributions);
                h.attributions.setAdapter(adapter);
                return;
            }
            hideAttributionViews(h);
        }
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void hideAttributions() {
        ViewGroupHelper.ViewChildViewHolder currentViewHolder;
        if (ClientFlagsHelper.multiAttributionsView(this.mContext) && (currentViewHolder = getCurrentViewHolder()) != null) {
            TimelineItemVideoViewHolder h = ((PostViewHolder) currentViewHolder).getVideoHolder();
            hideAttributionViews(h);
        }
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void resumePlayer(VideoViewInterface player) {
        player.resume();
        if (this.mPlayingPlayer != player) {
            if (this.mPlayingPlayer != null) {
                SLog.w("There's already someone playing, pause.");
                pausePlayer(this.mPlayingPlayer);
            }
            this.mPlayingPlayer = player;
        }
    }

    public boolean hasPlayerPlaying() {
        return (this.mLastPlayer == null || !this.mLastPlayer.isInPlaybackState() || this.mLastPlayer.isPaused()) ? false : true;
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public VideoViewInterface getLastPlayer() {
        return this.mLastPlayer;
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public VideoViewInterface getPlayer(int position, boolean b) {
        ViewHolder viewHolder = (CardViewHolder) this.mViewGroupHelper.getViewHolderFor(position);
        if (viewHolder == null || !(viewHolder.getType() == ViewType.POST || viewHolder.getType() == ViewType.FEED)) {
            return null;
        }
        return ((PostViewHolder) viewHolder).getVideoHolder().video;
    }

    public synchronized void postNewPlayCurrentPositionRunnable() {
        postNewPlayCurrentPositionRunnable(50L);
    }

    public synchronized void postNewPlayCurrentPositionRunnable(long delay) {
        this.mHandler.removeCallbacks(this.mPlayCurrentPositionRunnable);
        this.mHandler.postDelayed(this.mPlayCurrentPositionRunnable, delay);
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void pauseCurrentPlayer() {
        pausePlayer(this.mLastPlayer);
    }

    public void pauseVideoViews() {
        ArrayList<CardViewHolder> holders = this.mViewHolderCollection.getViewHolders();
        Iterator<CardViewHolder> it = holders.iterator();
        while (it.hasNext()) {
            ViewHolder viewHolder = (CardViewHolder) it.next();
            if (viewHolder.getType() == ViewType.POST || viewHolder.getType() == ViewType.FEED) {
                ((PostViewHolder) viewHolder).getVideoHolder().video.onPause();
            }
        }
    }

    public void resumeVideoViews() {
        ArrayList<CardViewHolder> holders = this.mViewHolderCollection.getViewHolders();
        Iterator<CardViewHolder> it = holders.iterator();
        while (it.hasNext()) {
            ViewHolder viewHolder = (CardViewHolder) it.next();
            if (viewHolder.getType() == ViewType.POST || viewHolder.getType() == ViewType.FEED) {
                ((PostViewHolder) viewHolder).getVideoHolder().video.onResume();
            }
        }
    }

    public void onFocusChanged(boolean focused) {
        resetShouldBePlaying();
        this.mHasFocus = focused;
        this.mHandler.removeCallbacks(this.mPlayCurrentPositionRunnable);
        releaseOtherPlayers(null);
        if (this.mPlayingPlayer != null) {
            pausePlayer(this.mPlayingPlayer);
        }
    }

    public ViewGroupHelper<CardViewHolder> getViewGroupHelper() {
        return this.mViewGroupHelper;
    }

    public CardViewHolder onResume(boolean focused) {
        resumeVideoViews();
        this.mHasFocus = focused;
        CardViewHolder largestChild = null;
        if (this.mHasFocus) {
            largestChild = (CardViewHolder) getViewGroupHelper().getLargestVisibleViewAdapterPosition();
        }
        postNewPlayCurrentPositionRunnable();
        return largestChild;
    }

    public void resetStates() {
        stopCurrentPlayer();
        this.mCurrentPlayerCount = 0;
    }

    public void onVideoError(VideoViewInterface view) {
        this.mMaxPlayer = this.mCurrentPlayerCount - 1;
        this.mNeedReleaseOtherPlayers = true;
        releaseOtherPlayers(view);
    }

    public void onSwitchToNextPlayer(int requestedPosition) {
        pauseCurrentPlayer();
        ViewHolder viewHolder = (CardViewHolder) this.mViewGroupHelper.getViewHolderFor(requestedPosition);
        if (viewHolder != null) {
            if (viewHolder.getType() == ViewType.POST || viewHolder.getType() == ViewType.FEED) {
                ((PostViewHolder) viewHolder).getVideoHolder().spinner.setVisibility(0);
            }
        }
    }

    private void onPlayerStoppedPlaying(VideoViewInterface videoView) {
        if (videoView == this.mPlayingPlayer) {
            this.mPlayingPlayer = null;
        }
    }

    public void suspendPlayer(VideoViewInterface player) {
        player.suspend();
        onPlayerStoppedPlaying(player);
    }

    public void stopCurrentPlayer() {
        if (this.mLastPlayer != null) {
            suspendPlayer(this.mLastPlayer);
            this.mLastPlayer = null;
            this.mCurrentPlaying = -1;
        }
    }

    public boolean shouldShowThumbnail(int position) {
        return (getCurrentPosition() == position && hasPlayerPlaying()) ? false : true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized void releaseOtherPlayers(VideoViewInterface view) {
        ArrayList<CardViewHolder> holders = this.mViewHolderCollection.getViewHolders();
        Iterator<CardViewHolder> it = holders.iterator();
        while (it.hasNext()) {
            CardViewHolder next = it.next();
            if (next.getType() == ViewType.POST || next.getType() == ViewType.FEED) {
                if (view != ((PostViewHolder) next).getVideoHolder().video) {
                    this.mLogger.d("Releasing player: {}", Integer.valueOf(next.position));
                    suspendPlayer(((PostViewHolder) next).getVideoHolder().video);
                    this.mCurrentPlayerCount--;
                }
            }
        }
    }

    public void toggleMute(boolean mute) {
        this.mMuted = mute;
        this.mLogger.d("Mute state changed to muted? {}.", Boolean.valueOf(mute));
        VideoViewInterface lastPlayer = this.mLastPlayer;
        if (lastPlayer != null) {
            lastPlayer.setMute(this.mMuted);
        }
    }

    public boolean hasFocus() {
        return this.mHasFocus;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface.OnTextureReadinessChangedListener
    public void onTextureReadinessChanged(VideoViewInterface videoView, SurfaceTexture surface, boolean isReady) {
        if (isReady && this.mHasFocus && videoView == this.mLastPlayer) {
            videoView.startOpenVideo();
        }
    }

    private class PlayCurrentPositionRunnable implements Runnable {
        private PlayCurrentPositionRunnable() {
        }

        @Override // java.lang.Runnable
        public synchronized void run() {
            CardViewHolder largestChild = (CardViewHolder) FeedVideoController.this.mViewGroupHelper.getLargestVisibleViewAdapterPosition();
            if (largestChild != null && (largestChild.getType() == ViewType.POST || largestChild.getType() == ViewType.FEED)) {
                if (largestChild.position == FeedVideoController.this.mCurrentPlaying || largestChild.position == -1) {
                    if (largestChild.position == FeedVideoController.this.mCurrentPlaying && !FeedVideoController.this.hasPlayerPlaying()) {
                        FeedVideoController.this.getVideoPathAndPlayForPosition(FeedVideoController.this.mCurrentPlaying, false);
                    }
                } else if (!FeedVideoController.this.mSensitiveAcknowledgments.isExplicit(largestChild.post.orNull())) {
                    FeedVideoController.this.getVideoPathAndPlayForPosition(largestChild.position, false);
                } else {
                    FeedVideoController.this.pauseCurrentPlayer();
                }
            } else {
                FeedVideoController.this.pauseCurrentPlayer();
            }
            if (FeedVideoController.this.mHasFocus && VideoViewHelper.getGlobalUseVineVideoView()) {
                FeedVideoController.this.postNewPlayCurrentPositionRunnable(200L);
            }
        }
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void getVideoPathAndPlayForPosition(int requestedPosition, boolean forceLowKey) {
        this.mLogger.d("play task executing with position {} ", Integer.valueOf(requestedPosition));
        if (!hasFocus()) {
            this.mLogger.d("not playing because not focused.");
            return;
        }
        TimelineItem item = this.mItems.getItem(requestedPosition);
        if (item != null) {
            if ((item.getType() != TimelineItemType.FEED || (item = ((VineFeed) item).coverPost) != null) && item.getType() == TimelineItemType.POST) {
                VinePost post = (VinePost) item;
                String postVideoPath = null;
                if (post != null) {
                    postVideoPath = this.mItems.getPath(post.postId);
                }
                if (postVideoPath != null) {
                    if (preFetch(requestedPosition + 1)) {
                        preFetch(requestedPosition + 2);
                    }
                    this.mLogger.d("{} is in cache or it is already pre-fetched.", Integer.valueOf(requestedPosition));
                    this.mLogger.d("playing file at position {}", Integer.valueOf(requestedPosition));
                    playFileAtPosition(requestedPosition, true);
                    return;
                }
                this.mLogger.d("Real fetch for {}.", Integer.valueOf(requestedPosition));
                if (post != null) {
                    VideoKey url = this.mRequestKeyGetter.getRequestKey(post, forceLowKey);
                    if (url == null) {
                        this.mRequestKeyGetter.onInvalidRequestKey(post);
                        return;
                    }
                    String path = this.mAppController.getVideoFilePath(url);
                    if (preFetch(requestedPosition + 1)) {
                        preFetch(requestedPosition + 2);
                    }
                    if (path != null) {
                        this.mItems.putPath(post.postId, path, url);
                        this.mLogger.d("playing file at position {}", Integer.valueOf(requestedPosition));
                        playFileAtPosition(requestedPosition, true);
                        return;
                    } else {
                        this.mShouldBePlayingPostId = post.postId;
                        this.mItems.putUrlReverse(url, this.mShouldBePlayingPostId);
                        this.mLogger.d("{} is not in app cache yet.", Integer.valueOf(requestedPosition));
                        onSwitchToNextPlayer(requestedPosition);
                        return;
                    }
                }
                this.mLogger.d("mItems is {} or the requested position has no post object in it yet, return -1.", this.mItems);
            }
        }
    }

    private boolean preFetch(int requestedPosition) {
        if (requestedPosition >= this.mItems.size()) {
            return false;
        }
        TimelineItem item = this.mItems.getItem(requestedPosition);
        if (item != null && item.getType() == TimelineItemType.FEED) {
            item = ((VineFeed) item).coverPost;
        }
        if (item == null || item.getType() != TimelineItemType.POST) {
            return false;
        }
        VinePost nextPost = (VinePost) item;
        if (nextPost != null) {
            if (this.mItems.getPath(nextPost.postId) == null) {
                TimelineItem i = this.mItems.getItem(requestedPosition);
                if (i != null && i.getType() == TimelineItemType.FEED) {
                    i = ((VineFeed) i).coverPost;
                }
                if (i == null || i.getType() != TimelineItemType.POST) {
                    return false;
                }
                VideoKey nextUrl = this.mRequestKeyGetter.getRequestKey((VinePost) i, false);
                this.mLogger.d("Pre-fetch {}: {}", Integer.valueOf(requestedPosition), nextUrl);
                if (nextUrl != null) {
                    if (nextPost.thumbnailUrl != null) {
                        ImageKey imageKey = new ImageKey(nextPost.thumbnailUrl);
                        this.mAppController.getPhotoBitmap(imageKey);
                    }
                    if (nextPost.avatarUrl != null) {
                        ImageKey imageKey2 = new ImageKey(nextPost.avatarUrl, true);
                        this.mAppController.getPhotoBitmap(imageKey2);
                    }
                    String nextPath = this.mAppController.getVideoFilePath(nextUrl);
                    if (nextPath != null) {
                        this.mItems.putPath(nextPost.postId, nextPath, null);
                    } else {
                        this.mItems.putUrlReverse(nextUrl, nextPost.postId);
                    }
                }
            } else {
                this.mLogger.d("{} is already fetched.", Integer.valueOf(requestedPosition));
            }
            return true;
        }
        this.mLogger.d("End of list, no pre-fetching: {} {}", Integer.valueOf(requestedPosition));
        return false;
    }

    public void resetShouldBePlaying() {
        this.mShouldBePlayingPostId = -2147483648L;
    }

    public boolean onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
        boolean shouldPlay = false;
        for (VideoKey key : videos.keySet()) {
            UrlVideo video = videos.get(key);
            Long postId = this.mItems.getUrlReverse(key);
            if (video != null && video.isValid() && postId != null) {
                this.mItems.putPath(postId.longValue(), video.getAbsolutePath(), key);
                if (postId.longValue() == this.mShouldBePlayingPostId) {
                    shouldPlay = true;
                }
            }
        }
        return shouldPlay;
    }

    public void hideAttributionViews(TimelineItemVideoViewHolder h) {
        if (ClientFlagsHelper.multiAttributionsView(this.mContext) && h.dimOverlay != null && h.attributions != null) {
            if (h.attributions.getVisibility() == 0) {
                h.attributions.startAnimation(this.mFadeOut);
                h.dimOverlay.startAnimation(this.mFadeOut);
                h.upperDim.startAnimation(this.mFadeOut);
            }
            h.dimOverlay.setVisibility(8);
            h.upperDim.setVisibility(8);
            h.attributions.setVisibility(8);
        }
    }

    public void showAttributionIcons(final TimelineItemVideoViewHolder h, VinePost post) {
        if (ClientFlagsHelper.multiAttributionsView(this.mContext) && h.musicNote != null && h.videoCamera != null) {
            h.musicNote.setVisibility(8);
            h.videoCamera.setVisibility(8);
            boolean audio = false;
            boolean video = false;
            if (post.audioMetadata != null && !post.audioMetadata.isEmpty()) {
                audio = true;
            }
            if (post.sources != null && !post.sources.isEmpty()) {
                Iterator<VineSource> it = post.sources.iterator();
                while (it.hasNext()) {
                    VineSource source = it.next();
                    if (source.getContentType() == 1 && !audio) {
                        audio = true;
                    }
                    if ((source.getContentType() == 2 || source.getContentType() == 3) && !video) {
                        video = true;
                    }
                    if (audio && video) {
                        break;
                    }
                }
            }
            if (audio) {
                h.musicNote.setVisibility(0);
                h.musicNote.postDelayed(new Runnable() { // from class: co.vine.android.feedadapter.v2.FeedVideoController.2
                    @Override // java.lang.Runnable
                    public void run() {
                        h.musicNote.setVisibility(8);
                    }
                }, 3000L);
            }
            if (video) {
                h.videoCamera.setVisibility(0);
                h.videoCamera.postDelayed(new Runnable() { // from class: co.vine.android.feedadapter.v2.FeedVideoController.3
                    @Override // java.lang.Runnable
                    public void run() {
                        h.videoCamera.setVisibility(8);
                    }
                }, 3000L);
            }
        }
    }

    public void showAttributionViews(TimelineItemVideoViewHolder h) {
        if (ClientFlagsHelper.multiAttributionsView(this.mContext) && h.musicNote != null && h.videoCamera != null && h.dimOverlay != null && h.attributions != null) {
            h.musicNote.setVisibility(8);
            h.videoCamera.setVisibility(8);
            h.dimOverlay.setVisibility(0);
            h.upperDim.setVisibility(0);
            h.attributions.setVisibility(0);
            h.attributions.startAnimation(this.mSlideIn);
            h.dimOverlay.startAnimation(this.mFadeIn);
            h.upperDim.startAnimation(this.mFadeIn);
        }
    }

    public CardViewHolder getCurrentViewHolder() {
        int current = getCurrentPosition();
        CardViewHolder holder = this.mViewHolderCollection.getViewHolderForPosition(current);
        if (holder == null || (holder.getType() != ViewType.POST && holder.getType() != ViewType.FEED)) {
            return null;
        }
        return holder;
    }
}
