package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import co.vine.android.R;
import co.vine.android.TimelineItemScribeActionsListener;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedAdapterTouchListenerFactory;
import co.vine.android.feedadapter.v2.FeedVideoController;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.v2.LoopIncrementer;
import co.vine.android.feedadapter.viewholder.FeedVideoViewHolder;
import co.vine.android.feedadapter.viewholder.TimelineItemVideoViewHolder;
import co.vine.android.player.SdkVideoView;
import co.vine.android.widget.OnTopViewBoundListener;
import co.vine.android.widget.SensitiveAcknowledgments;
import com.edisonwang.android.slog.SLogger;

/* loaded from: classes.dex */
public class FeedVideoViewManager extends TimelineItemVideoViewManager implements ViewManager {
    public FeedVideoViewManager(Context context, AppController appController, TimelineClickListenerFactory.Callback callback, FeedAdapterItems items, FeedViewHolderCollection viewHolders, FeedVideoController videoController, SensitiveAcknowledgments sensitiveAcknowledgments, LoopIncrementer incrementer, FeedAdapterTouchListenerFactory listenerFactory, OnTopViewBoundListener onTopViewBoundListener, TimelineItemScribeActionsListener postPlayedListener, SLogger logger) {
        super(context, appController, callback, items, viewHolders, videoController, sensitiveAcknowledgments, incrementer, listenerFactory, onTopViewBoundListener, postPlayedListener, logger);
    }

    public void bind(TimelineItemVideoViewHolder h, VineFeed data, int position) {
        if (data != null && data.coverPost != null) {
            super.bind(h, data.coverPost, position);
            setupOverlay((FeedVideoViewHolder) h, data);
        }
    }

    private void setupOverlay(FeedVideoViewHolder h, VineFeed data) {
        if (h.overlay != null) {
            h.overlayText.setText(this.mContext.getString(R.string.go_to_channel, data.title.toUpperCase()));
            h.overlayIcon.setVisibility(8);
            h.overlay.setOnClickListener(TimelineClickListenerFactory.newFeedOverlayClickedListener(this.mCallback, data));
        }
    }

    @Override // co.vine.android.feedadapter.viewmanager.TimelineItemVideoViewManager
    protected void initVideoView(TimelineItemVideoViewHolder h, VinePost post, int position) {
        super.initVideoView(h, post, position);
        VideoViewInterface view = h.video;
        view.setOnCompletionListener(new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.feedadapter.viewmanager.FeedVideoViewManager.1
            @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
            public void onCompletion(VideoViewInterface videoView) throws IllegalStateException {
                if (videoView instanceof SdkVideoView) {
                    ((SdkVideoView) videoView).seekTo(0);
                }
                FeedVideoViewManager.this.countedMediaPlayerStart(videoView, FeedVideoViewManager.this.mItems);
            }
        });
    }
}
