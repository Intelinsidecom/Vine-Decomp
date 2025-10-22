package co.vine.android.recordingui;

import android.content.res.Resources;
import co.vine.android.player.StaticSizeExoPlayerTextureView;
import co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout;

/* loaded from: classes.dex */
public class MultiImportTrimmerManager implements ThumbnailRangeFinderLayout.OnVideoTrimmedListener {
    private MultiImportViewHolder mCurrentHolder;
    private MultiImportTrimmerHolder mTrimmerHolder;

    public MultiImportTrimmerManager(MultiImportTrimmerHolder trimmerHolder) {
        this.mTrimmerHolder = trimmerHolder;
    }

    public void bind(MultiImportTrimmerHolder trimmerHolder) throws Resources.NotFoundException {
        trimmerHolder.thumbnailRangeFinder.setListener(this);
        trimmerHolder.addToRoot();
    }

    public void updateUri(MultiImportViewHolder h, long duration) {
        this.mCurrentHolder = h;
        this.mTrimmerHolder.thumbnailRangeFinder.clear();
        this.mTrimmerHolder.thumbnailRangeFinder.setVideoPath(this.mCurrentHolder.getUri(), duration, 6000L);
    }

    @Override // co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout.OnVideoTrimmedListener
    public void onVideoTrimmedByScrubber() {
        updatePosition(((int) this.mTrimmerHolder.thumbnailRangeFinder.trimEndTimeUsec) / 1000);
    }

    @Override // co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout.OnVideoTrimmedListener
    public void onVideoTrimmedByCarousel() {
        updatePosition(((int) this.mTrimmerHolder.thumbnailRangeFinder.trimStartTimeUsec) / 1000);
    }

    @Override // co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout.OnVideoTrimmedListener
    public void onStartEndTimeChanged(long trimStartTimeUsec, long trimEndTimeUsec) {
    }

    private void updatePosition(int position) {
        this.mCurrentHolder.textureView.setPlayWhenReady(this.mCurrentHolder.textureView.getPlayWhenReady());
        this.mCurrentHolder.textureView.seekTo(position);
    }

    public void seekTo(StaticSizeExoPlayerTextureView textureView) {
        textureView.seekTo(((int) this.mTrimmerHolder.thumbnailRangeFinder.trimStartTimeUsec) / 1000);
    }
}
