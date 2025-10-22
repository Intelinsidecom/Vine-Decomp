package co.vine.android;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.SdkVideoView;
import co.vine.android.recorder.ProgressView;
import java.io.File;

/* loaded from: classes.dex */
public class DraftFragment extends BaseFragment implements VideoViewInterface.OnErrorListener, VideoViewInterface.OnPreparedListener, VideoViewInterface.OnTextureReadinessChangedListener {
    private Bitmap mBitmap;
    private int mDimen;
    private boolean mExpired;
    private File mFolder;
    private DraftPageViewHolder mHolder;
    private View.OnClickListener mListener;
    private int mProgress;
    private boolean mSelected;
    private String mThumbUrl;
    private String mVideoUrl;
    private SdkVideoView mVideoView;
    public boolean mCanUnhide = true;
    private final Runnable mStartPlayingRunnable = new Runnable() { // from class: co.vine.android.DraftFragment.1
        @Override // java.lang.Runnable
        public void run() {
            if (DraftFragment.this.mVideoView.getVisibility() != 0 && DraftFragment.this.mCanUnhide) {
                DraftFragment.this.mVideoView.setVisibility(0);
            }
        }
    };

    @Override // co.vine.android.embed.player.VideoViewInterface.OnTextureReadinessChangedListener
    public void onTextureReadinessChanged(VideoViewInterface videoView, SurfaceTexture surface, boolean isReady) throws IllegalStateException {
        if (isReady) {
            videoView.setVisibility(4);
            this.mVideoView.startOpenVideo();
        }
    }

    private static class DraftPageViewHolder {
        public ViewGroup containerView;
        public RelativeLayout draftContainer;
        public ProgressView progressView;
        public ImageView thumb;
        public String videoUrl;
        public SdkVideoView videoView;

        public DraftPageViewHolder(View v, String videoUrl) {
            this.videoView = (SdkVideoView) v.findViewById(R.id.videoView);
            this.videoUrl = videoUrl;
            this.containerView = (ViewGroup) v.findViewById(R.id.videoViewContainer);
            this.progressView = (ProgressView) v.findViewById(R.id.progress);
            this.thumb = (ImageView) v.findViewById(R.id.thumb);
            this.draftContainer = (RelativeLayout) v.findViewById(R.id.draft_container);
        }
    }

    public void setArguments(boolean selected, String videoUrl, String thumbUrl, File folder, int progress, int dimension) {
        this.mSelected = selected;
        this.mVideoUrl = videoUrl;
        this.mFolder = folder;
        this.mProgress = progress;
        this.mThumbUrl = thumbUrl;
        this.mDimen = dimension;
        this.mExpired = false;
    }

    public void setListener(View.OnClickListener listener) {
        this.mListener = listener;
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() throws IllegalStateException {
        super.onPause();
        if (this.mVideoView != null) {
            this.mVideoView.pause();
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroyView() throws IllegalStateException {
        super.onDestroyView();
        if (this.mBitmap != null) {
            this.mBitmap.recycle();
        }
        if (this.mVideoView != null) {
            this.mVideoView.suspend();
        }
    }

    public void showImage() {
        this.mHolder.thumb.setVisibility(0);
        this.mHolder.videoView.setVisibility(8);
    }

    public View getProgressView() {
        return this.mHolder.progressView;
    }

    public View getThumbnailView() {
        return this.mHolder.thumb;
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) throws Resources.NotFoundException {
        Resources res = getResources();
        View v = inflater.inflate(R.layout.draft_page, root, false);
        DraftPageViewHolder holder = new DraftPageViewHolder(v, this.mVideoUrl);
        int progressHeight = res.getDimensionPixelOffset(R.dimen.progress_view_height);
        int draftProgressHeight = res.getDimensionPixelOffset(R.dimen.draft_progress_view_height);
        RelativeLayout.LayoutParams containerParams = (RelativeLayout.LayoutParams) holder.draftContainer.getLayoutParams();
        containerParams.topMargin = progressHeight + draftProgressHeight;
        holder.draftContainer.setLayoutParams(containerParams);
        RelativeLayout videoContainer = (RelativeLayout) v.findViewById(R.id.videoViewContainer);
        videoContainer.setDrawingCacheEnabled(true);
        ViewGroup.LayoutParams params = videoContainer.getLayoutParams();
        params.height = this.mDimen;
        params.width = this.mDimen;
        videoContainer.setLayoutParams(params);
        videoContainer.setTag(this.mFolder);
        videoContainer.setOnClickListener(this.mListener);
        ViewGroup.LayoutParams progressLayoutParams = holder.progressView.getLayoutParams();
        progressLayoutParams.width = this.mDimen;
        holder.progressView.setLayoutParams(progressLayoutParams);
        holder.progressView.setProgressRatio(this.mProgress / 6000.0f);
        View mask = v.findViewById(R.id.draftPageMask);
        ViewGroup.LayoutParams params2 = mask.getLayoutParams();
        params2.width = this.mDimen;
        params2.height = ((AbstractRecordingActivity) getActivity()).getScreenSize().y;
        mask.setLayoutParams(params2);
        this.mBitmap = BitmapFactory.decodeFile(this.mThumbUrl);
        ViewGroup.LayoutParams thumbLayoutParams = holder.thumb.getLayoutParams();
        thumbLayoutParams.height = this.mDimen;
        thumbLayoutParams.width = this.mDimen;
        holder.thumb.setLayoutParams(thumbLayoutParams);
        holder.thumb.setImageDrawable(new RecyclableBitmapDrawable(getActivity().getResources(), this.mBitmap));
        holder.thumb.setVisibility(0);
        this.mHolder = holder;
        this.mVideoView = holder.videoView;
        ((AbstractRecordingActivity) getActivity()).addPlayerToPool(this.mVideoView);
        this.mVideoView.setOnPreparedListener(this);
        this.mVideoView.setKeepScreenOn(true);
        this.mVideoView.setLooping(true);
        this.mVideoView.setOnErrorListener(this);
        this.mVideoView.setSurfaceReadyListener(this);
        v.setDrawingCacheEnabled(true);
        v.setDrawingCacheQuality(1048576);
        return v;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
        if (selected) {
            onSelected();
        } else {
            onUnselected();
        }
    }

    public void release() {
        this.mVideoView.suspend();
    }

    private void onUnselected() throws IllegalStateException {
        this.mVideoView.pause();
    }

    private void onSelected() {
        resumePlayer();
    }

    public void resumePlayer() {
        if (this.mVideoView.isInPlaybackState() && this.mVideoView.isPathPlaying(this.mVideoUrl)) {
            this.mVideoView.start();
        } else if (this.mVideoView.isAvailable()) {
            this.mVideoView.setVisibility(4);
            this.mVideoView.setVideoPath(this.mVideoUrl);
        } else {
            this.mVideoView.setVideoPath(this.mVideoUrl);
            this.mVideoView.setVisibility(0);
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
    public void onPrepared(VideoViewInterface view) {
        if (this.mSelected && !view.isPlaying()) {
            view.start();
            this.mVideoView.postDelayed(this.mStartPlayingRunnable, 100L);
        }
    }

    public void setExpired(boolean expired) {
        this.mExpired = expired;
    }

    public boolean isExpired() {
        return this.mExpired;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
    public boolean onError(VideoViewInterface view, int what, int extra) throws IllegalStateException {
        AbstractRecordingActivity activity = (AbstractRecordingActivity) getActivity();
        if (activity != null) {
            activity.releaseOtherPlayers(this.mVideoView);
            this.mVideoView.retryOpenVideo(false);
            return true;
        }
        return true;
    }

    public void pausePlayer() {
        if (this.mSelected && this.mVideoView.canPause()) {
            this.mVideoView.pause();
        }
    }
}
