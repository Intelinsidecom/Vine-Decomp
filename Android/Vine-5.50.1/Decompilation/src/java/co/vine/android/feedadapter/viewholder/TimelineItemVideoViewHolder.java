package co.vine.android.feedadapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import co.vine.android.R;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.embed.player.VideoViewHelper;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.feedadapter.v2.FeedAdapterTouchListenerFactory;
import co.vine.android.feedadapter.v2.VideoImageTopHideAnimation;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.player.VideoSensitiveImageClickListener;
import co.vine.android.widget.FloatingLikesView;

/* loaded from: classes.dex */
public class TimelineItemVideoViewHolder implements VideoViewInterface.SurfaceUpdatedListener, ViewHolder {
    public RecyclerView attributions;
    public ImageView bottomThumbnail;
    public ViewGroup container;
    public View dimOverlay;
    public FloatingLikesView doubleTap;
    public int frameCount;
    public boolean hasThumbnail;
    public VideoSensitiveImageClickListener imageListener;
    public FeedAdapterTouchListenerFactory.VideoContainerTouchListener listener;
    public ImageView musicNote;
    public View sensitiveTextContainer;
    public View spinner;
    public ImageKey thumbnailKey;
    public ImageView topThumbnail;
    public VideoImageTopHideAnimation topThumbnailAnimation;
    public View upperDim;
    public VideoViewInterface video;
    public ImageView videoCamera;

    public TimelineItemVideoViewHolder(View view) {
        this.container = (ViewGroup) view.findViewById(R.id.videoContainer);
        this.attributions = (RecyclerView) view.findViewById(R.id.attributions);
        this.dimOverlay = view.findViewById(R.id.dim_video_container);
        this.upperDim = view.findViewById(R.id.upper_dim);
        this.musicNote = (ImageView) view.findViewById(R.id.audio_remix);
        this.videoCamera = (ImageView) view.findViewById(R.id.vine_remix);
        this.sensitiveTextContainer = view.findViewById(R.id.sensitive_text);
        this.bottomThumbnail = (ImageView) view.findViewById(R.id.video_image);
        this.spinner = view.findViewById(R.id.video_load_image);
        this.video = VideoViewHelper.useDefaultVideoView(view, R.id.sdkVideoView, R.id.vineVideoView);
        this.topThumbnail = (ImageView) this.container.findViewById(R.id.video_image_top);
        this.doubleTap = (FloatingLikesView) view.findViewById(R.id.doubleTapView);
    }

    public ViewType getType() {
        return ViewType.TIMELINEITEM_VIDEO;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface.SurfaceUpdatedListener
    public void onSurfaceUpdated() {
        if (this.frameCount >= 5) {
            this.video.setVisibility(0);
            hideThumbnail(true);
        }
        this.frameCount++;
    }

    public void hideThumbnail(boolean animate) {
        if (this.topThumbnail.getVisibility() == 0 && this.topThumbnail.getAnimation() == null) {
            if (animate) {
                this.topThumbnail.startAnimation(this.topThumbnailAnimation);
            } else {
                this.topThumbnailAnimation.onAnimationEnd(this.topThumbnailAnimation);
            }
        }
    }
}
