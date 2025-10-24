package co.vine.android.recordingui;

import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.SdkVideoView;

/* loaded from: classes.dex */
public abstract class TrimmedVideoPlaybackRecordingMode implements VideoViewInterface.OnCompletionListener {
    private TrimPointPositionProvider mTrimPointPositionProvider;
    private Runnable mVideoProgressCheckerRunnable = new Runnable() { // from class: co.vine.android.recordingui.TrimmedVideoPlaybackRecordingMode.1
        @Override // java.lang.Runnable
        public void run() throws IllegalStateException {
            long currentPosition = TrimmedVideoPlaybackRecordingMode.this.mVideoView.getCurrentPosition();
            int minValue = (int) TrimmedVideoPlaybackRecordingMode.this.mTrimPointPositionProvider.getStartPositionMS();
            int maxValue = (int) TrimmedVideoPlaybackRecordingMode.this.mTrimPointPositionProvider.getEndPositionMS();
            if (currentPosition >= maxValue || currentPosition < minValue) {
                TrimmedVideoPlaybackRecordingMode.this.mVideoView.seekTo(minValue + 100);
            }
            TrimmedVideoPlaybackRecordingMode.this.mVideoView.postDelayed(this, 100L);
        }
    };
    protected SdkVideoView mVideoView;

    public interface TrimPointPositionProvider {
        long getEndPositionMS();

        long getStartPositionMS();
    }

    public TrimmedVideoPlaybackRecordingMode(SdkVideoView videoView) {
        this.mVideoView = videoView;
    }

    public void startMonitoringVideoPlaybackForTrim(TrimPointPositionProvider trimPointProvider) {
        this.mTrimPointPositionProvider = trimPointProvider;
        this.mVideoView.postDelayed(this.mVideoProgressCheckerRunnable, 100L);
        this.mVideoView.setOnCompletionListener(this);
    }

    @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
    public void onCompletion(VideoViewInterface view) {
        view.seekTo(((int) this.mTrimPointPositionProvider.getStartPositionMS()) + 100);
        view.start();
    }

    public void stopMonitoringVideoPlaybackForTrim() {
        this.mVideoView.removeCallbacks(this.mVideoProgressCheckerRunnable);
        this.mVideoView.setOnCompletionListener(null);
    }
}
