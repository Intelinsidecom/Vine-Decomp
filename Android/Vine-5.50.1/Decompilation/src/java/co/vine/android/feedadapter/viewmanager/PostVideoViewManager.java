package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import co.vine.android.TimelineItemScribeActionsListener;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedAdapterTouchListenerFactory;
import co.vine.android.feedadapter.v2.FeedVideoController;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.v2.LoopIncrementer;
import co.vine.android.feedadapter.viewholder.LongformPostOverlayViewManager;
import co.vine.android.feedadapter.viewholder.PostVideoViewHolder;
import co.vine.android.feedadapter.viewholder.TimelineItemVideoViewHolder;
import co.vine.android.player.SdkVideoView;
import co.vine.android.util.LoopManager;
import co.vine.android.widget.OnTopViewBoundListener;
import co.vine.android.widget.SensitiveAcknowledgments;
import com.edisonwang.android.slog.SLogger;

/* loaded from: classes.dex */
public class PostVideoViewManager extends TimelineItemVideoViewManager implements ViewManager {
    public PostVideoViewManager(Context context, AppController appController, TimelineClickListenerFactory.Callback callback, FeedAdapterItems items, FeedViewHolderCollection viewHolders, FeedVideoController videoController, SensitiveAcknowledgments sensitiveAcknowledgments, LoopIncrementer incrementer, FeedAdapterTouchListenerFactory listenerFactory, OnTopViewBoundListener onTopViewBoundListener, TimelineItemScribeActionsListener postPlayedListener, SLogger logger) {
        super(context, appController, callback, items, viewHolders, videoController, sensitiveAcknowledgments, incrementer, listenerFactory, onTopViewBoundListener, postPlayedListener, logger);
    }

    @Override // co.vine.android.feedadapter.viewmanager.TimelineItemVideoViewManager
    public void bind(TimelineItemVideoViewHolder h, VinePost data, int position) {
        if (data != null && (h instanceof PostVideoViewHolder)) {
            PostVideoViewHolder holder = (PostVideoViewHolder) h;
            super.bind(h, data, position);
            if (data.hidden && data.repost == null && holder.hiddenOverlay != null) {
                holder.hiddenOverlay.setVisibility(0);
            } else if (holder.hiddenOverlay != null) {
                holder.hiddenOverlay.setVisibility(8);
            }
            LongformPostOverlayViewManager.setupOverlay(this.mContext, holder.longformOverlayHolder, data, position, this.mCallback);
        }
    }

    @Override // co.vine.android.feedadapter.viewmanager.TimelineItemVideoViewManager
    protected void initVideoView(TimelineItemVideoViewHolder holder, final VinePost post, int position) {
        super.initVideoView(holder, post, position);
        PostVideoViewHolder h = (PostVideoViewHolder) holder;
        VideoViewInterface view = h.video;
        view.setOnCompletionListener(new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.feedadapter.viewmanager.PostVideoViewManager.1
            @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
            public void onCompletion(VideoViewInterface videoView) throws IllegalStateException {
                if (videoView instanceof SdkVideoView) {
                    ((SdkVideoView) videoView).seekTo(0);
                }
                TimelineItem currentItem = PostVideoViewManager.this.mItems.getItem(PostVideoViewManager.this.mVideoController.getCurrentPosition());
                if (currentItem != null && currentItem.getType() == TimelineItemType.POST) {
                    int loops = LoopManager.getLocalLoopCount(post.postId);
                    ((VinePost) currentItem).adjustLoopCount(loops);
                }
                PostVideoViewManager.this.countedMediaPlayerStart(videoView, PostVideoViewManager.this.mItems);
            }
        });
        if (view instanceof SdkVideoView) {
            view.setSurfaceReadyListener(this.mVideoController);
        }
    }
}
