package co.vine.android;

import android.support.v7.widget.Toolbar;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.widget.FloatingLikesView;
import co.vine.android.widget.TypefacesTextView;
import com.google.android.exoplayer.AspectRatioFrameLayout;

/* loaded from: classes.dex */
public class LongformViewHolder {
    View controlsContainer;
    TypefacesTextView description;
    ImageView fullScreenImage;
    View fullscreenButtonContainer;
    FloatingLikesView heartsView;
    ImageView likeImage;
    View likesContainer;
    ImageView playPauseButton;
    View postInfoContainer;
    RelativeLayout resolveStateContainer;
    ImageView resolveUserImage;
    TextView resolveUsername;
    View rootView;
    View share;
    Toolbar toolbar;
    TypefacesTextView username;
    FrameLayout videoControllerContainer;
    AspectRatioFrameLayout videoFrame;
    TextureView videoView;
    View watchAgain;

    public LongformViewHolder(View contentView) {
        this.rootView = contentView.findViewById(R.id.root_view);
        this.videoView = (TextureView) contentView.findViewById(R.id.video_view);
        this.videoFrame = (AspectRatioFrameLayout) contentView.findViewById(R.id.video_frame);
        this.heartsView = (FloatingLikesView) contentView.findViewById(R.id.hearts);
        this.controlsContainer = contentView.findViewById(R.id.controls_container);
        this.videoControllerContainer = (FrameLayout) contentView.findViewById(R.id.video_controller_container);
        this.fullscreenButtonContainer = contentView.findViewById(R.id.fullscreen);
        this.likesContainer = contentView.findViewById(R.id.like);
        this.postInfoContainer = contentView.findViewById(R.id.post_info_container);
        this.username = (TypefacesTextView) contentView.findViewById(R.id.username);
        this.description = (TypefacesTextView) contentView.findViewById(R.id.description);
        this.likeImage = (ImageView) contentView.findViewById(R.id.like_image);
        this.fullScreenImage = (ImageView) contentView.findViewById(R.id.fullscreen_image);
        this.resolveStateContainer = (RelativeLayout) contentView.findViewById(R.id.resolve_container);
        this.resolveUserImage = (ImageView) contentView.findViewById(R.id.resolve_user_image);
        this.resolveUsername = (TextView) contentView.findViewById(R.id.resolve_username);
        this.watchAgain = contentView.findViewById(R.id.watch_again);
        this.share = contentView.findViewById(R.id.share_video);
        this.playPauseButton = (ImageView) contentView.findViewById(R.id.play_pause);
        this.toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
    }
}
