package co.vine.android.share.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import co.vine.android.R;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.OnSingleVideoClickedListener;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.LoopSdkVideoView;

/* loaded from: classes.dex */
public final class ShareVideoView extends FrameLayout {
    private final View mDimOverlay;
    private final ImageView mVideoThumbnail;
    private final LoopSdkVideoView mVideoView;

    public ShareVideoView(Context context) {
        this(context, null, 0);
    }

    public ShareVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShareVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        inflater.inflate(R.layout.share_video, this);
        this.mVideoView = (LoopSdkVideoView) findViewById(R.id.video_view);
        this.mVideoThumbnail = (ImageView) findViewById(R.id.video_preview_thumbnail);
        this.mDimOverlay = findViewById(R.id.dim_overlay);
        setOnClickListener(new OnSingleVideoClickedListener(this.mVideoView));
        dim(0);
    }

    public void setHeight(int heightPx) {
        this.mVideoThumbnail.getLayoutParams().height = heightPx;
        this.mVideoView.getLayoutParams().height = heightPx;
        this.mDimOverlay.getLayoutParams().height = heightPx;
    }

    public LoopSdkVideoView getVideoView() {
        return this.mVideoView;
    }

    public ImageView getVideoThumbnail() {
        return this.mVideoThumbnail;
    }

    public void setVideoAndThumbnailImage(Uri videoUri, Bitmap bitmap) {
        setThumbnailImage(bitmap);
        setVideo(videoUri);
    }

    public void setThumbnailImage(Bitmap bitmap) {
        if (bitmap != null) {
            this.mVideoThumbnail.setImageBitmap(bitmap);
        }
    }

    public void setVideo(Uri videoUri) {
        if (videoUri != null) {
            this.mVideoView.setVideoURI(videoUri);
            this.mVideoView.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.share.widgets.ShareVideoView.1
                @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
                public void onPrepared(VideoViewInterface videoView) {
                    videoView.setMute(true);
                    videoView.start();
                    ViewUtil.disableAndHide(ShareVideoView.this.mVideoThumbnail);
                }
            });
        }
    }

    public void dim(int value) {
        if (value >= 0 && value <= 255) {
            this.mDimOverlay.getBackground().setAlpha(value);
        }
    }
}
