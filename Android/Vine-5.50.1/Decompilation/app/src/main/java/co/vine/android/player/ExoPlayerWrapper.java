package co.vine.android.player;

import android.media.MediaCodec;
import android.os.Handler;
import android.view.Surface;
import com.google.android.exoplayer.DummyTrackRenderer;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.util.PlayerControl;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class ExoPlayerWrapper implements ExoPlayer.Listener, MediaCodecAudioTrackRenderer.EventListener, MediaCodecVideoTrackRenderer.EventListener, HlsSampleSource.EventListener, BandwidthMeter.EventListener {
    private InfoListener mInfoListener;
    private InternalErrorListener mInternalErrorListener;
    private boolean mLastReportedPlayWhenReady;
    private int mLastReportedPlaybackState;
    private final CopyOnWriteArrayList<Listener> mListeners;
    private final Handler mMainHandler;
    private final ExoPlayer mPlayer = ExoPlayer.Factory.newInstance(2, 1000, 5000);
    private final PlayerControl mPlayerControl;
    private final RendererBuilder mRendererBuilder;
    private int mRendererBuildingState;
    private Surface mSurface;
    private TrackRenderer mVideoRenderer;

    public interface InfoListener {
        void onAudioFormatEnabled(Format format, int i, long j);

        void onBandwidthSample(int i, long j, long j2);

        void onDecoderInitialized(String str, long j, long j2);

        void onDroppedFrames(int i, long j);

        void onLoadCompleted(int i, long j, int i2, int i3, Format format, long j2, long j3, long j4, long j5);

        void onLoadStarted(int i, long j, int i2, int i3, Format format, long j2, long j3);

        void onVideoFormatEnabled(Format format, int i, long j);
    }

    public interface InternalErrorListener {
        void onAudioTrackInitializationError(AudioTrack.InitializationException initializationException);

        void onAudioTrackUnderrun(int i, long j, long j2);

        void onAudioTrackWriteError(AudioTrack.WriteException writeException);

        void onCryptoError(MediaCodec.CryptoException cryptoException);

        void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException decoderInitializationException);

        void onLoadError(int i, IOException iOException);

        void onRendererInitializationError(Exception exc);
    }

    public interface Listener {
        void onError(Exception exc);

        void onStateChanged(boolean z, int i);

        void onVideoSizeChanged(int i, int i2, int i3, float f);
    }

    public interface RendererBuilder {
        void buildRenderers(ExoPlayerWrapper exoPlayerWrapper);

        void cancel();
    }

    public ExoPlayerWrapper(RendererBuilder rendererBuilder) {
        this.mRendererBuilder = rendererBuilder;
        this.mPlayer.addListener(this);
        this.mPlayerControl = new PlayerControl(this.mPlayer);
        this.mMainHandler = new Handler();
        this.mListeners = new CopyOnWriteArrayList<>();
        this.mLastReportedPlaybackState = 1;
        this.mRendererBuildingState = 1;
    }

    public PlayerControl getPlayerControl() {
        return this.mPlayerControl;
    }

    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }

    public void setSurface(Surface surface) {
        this.mSurface = surface;
        pushSurface(false);
    }

    public void prepare() {
        if (this.mRendererBuildingState == 3) {
            this.mPlayer.stop();
        }
        this.mRendererBuilder.cancel();
        this.mVideoRenderer = null;
        this.mRendererBuildingState = 2;
        maybeReportPlayerState();
        this.mRendererBuilder.buildRenderers(this);
    }

    void onRenderers(TrackRenderer[] renderers) {
        for (int i = 0; i < 2; i++) {
            if (renderers[i] == null) {
                renderers[i] = new DummyTrackRenderer();
            }
        }
        this.mVideoRenderer = renderers[0];
        pushSurface(false);
        this.mPlayer.prepare(renderers);
        this.mRendererBuildingState = 3;
    }

    void onRenderersError(Exception e) {
        if (this.mInternalErrorListener != null) {
            this.mInternalErrorListener.onRendererInitializationError(e);
        }
        Iterator<Listener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            Listener listener = it.next();
            listener.onError(e);
        }
        this.mRendererBuildingState = 1;
        maybeReportPlayerState();
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.mPlayer.setPlayWhenReady(playWhenReady);
    }

    public void seekTo(long positionMs) {
        this.mPlayer.seekTo(positionMs);
    }

    public void release() {
        this.mRendererBuilder.cancel();
        this.mRendererBuildingState = 1;
        this.mSurface = null;
        this.mPlayer.release();
    }

    public int getPlaybackState() {
        if (this.mRendererBuildingState == 2) {
            return 2;
        }
        int playerState = this.mPlayer.getPlaybackState();
        if (this.mRendererBuildingState == 3 && playerState == 1) {
            return 2;
        }
        return playerState;
    }

    public long getCurrentPosition() {
        return this.mPlayer.getCurrentPosition();
    }

    public boolean getPlayWhenReady() {
        return this.mPlayer.getPlayWhenReady();
    }

    Handler getmMainHandler() {
        return this.mMainHandler;
    }

    @Override // com.google.android.exoplayer.ExoPlayer.Listener
    public void onPlayerStateChanged(boolean playWhenReady, int state) {
        maybeReportPlayerState();
    }

    @Override // com.google.android.exoplayer.ExoPlayer.Listener
    public void onPlayerError(ExoPlaybackException exception) {
        this.mRendererBuildingState = 1;
        Iterator<Listener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            Listener listener = it.next();
            listener.onError(exception);
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecVideoTrackRenderer.EventListener
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Iterator<Listener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            Listener listener = it.next();
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecVideoTrackRenderer.EventListener
    public void onDroppedFrames(int count, long elapsed) {
        if (this.mInfoListener != null) {
            this.mInfoListener.onDroppedFrames(count, elapsed);
        }
    }

    @Override // com.google.android.exoplayer.upstream.BandwidthMeter.EventListener
    public void onBandwidthSample(int elapsedMs, long bytes, long bitrateEstimate) {
        if (this.mInfoListener != null) {
            this.mInfoListener.onBandwidthSample(elapsedMs, bytes, bitrateEstimate);
        }
    }

    @Override // com.google.android.exoplayer.chunk.BaseChunkSampleSourceEventListener
    public void onDownstreamFormatChanged(int sourceId, Format format, int trigger, long mediaTimeMs) {
        if (this.mInfoListener != null) {
            if (sourceId == 0) {
                this.mInfoListener.onVideoFormatEnabled(format, trigger, mediaTimeMs);
            } else if (sourceId == 1) {
                this.mInfoListener.onAudioFormatEnabled(format, trigger, mediaTimeMs);
            }
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer.EventListener
    public void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e) {
        if (this.mInternalErrorListener != null) {
            this.mInternalErrorListener.onDecoderInitializationError(e);
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecAudioTrackRenderer.EventListener
    public void onAudioTrackInitializationError(AudioTrack.InitializationException e) {
        if (this.mInternalErrorListener != null) {
            this.mInternalErrorListener.onAudioTrackInitializationError(e);
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecAudioTrackRenderer.EventListener
    public void onAudioTrackWriteError(AudioTrack.WriteException e) {
        if (this.mInternalErrorListener != null) {
            this.mInternalErrorListener.onAudioTrackWriteError(e);
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecAudioTrackRenderer.EventListener
    public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        if (this.mInternalErrorListener != null) {
            this.mInternalErrorListener.onAudioTrackUnderrun(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer.EventListener
    public void onCryptoError(MediaCodec.CryptoException e) {
        if (this.mInternalErrorListener != null) {
            this.mInternalErrorListener.onCryptoError(e);
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer.EventListener
    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
        if (this.mInfoListener != null) {
            this.mInfoListener.onDecoderInitialized(decoderName, elapsedRealtimeMs, initializationDurationMs);
        }
    }

    @Override // com.google.android.exoplayer.chunk.BaseChunkSampleSourceEventListener
    public void onLoadError(int sourceId, IOException e) {
        if (this.mInternalErrorListener != null) {
            this.mInternalErrorListener.onLoadError(sourceId, e);
        }
    }

    @Override // com.google.android.exoplayer.ExoPlayer.Listener
    public void onPlayWhenReadyCommitted() {
    }

    @Override // com.google.android.exoplayer.MediaCodecVideoTrackRenderer.EventListener
    public void onDrawnToSurface(Surface surface) {
    }

    @Override // com.google.android.exoplayer.chunk.BaseChunkSampleSourceEventListener
    public void onLoadStarted(int sourceId, long length, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
        if (this.mInfoListener != null) {
            this.mInfoListener.onLoadStarted(sourceId, length, type, trigger, format, mediaStartTimeMs, mediaEndTimeMs);
        }
    }

    @Override // com.google.android.exoplayer.chunk.BaseChunkSampleSourceEventListener
    public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
        if (this.mInfoListener != null) {
            this.mInfoListener.onLoadCompleted(sourceId, bytesLoaded, type, trigger, format, mediaStartTimeMs, mediaEndTimeMs, elapsedRealtimeMs, loadDurationMs);
        }
    }

    @Override // com.google.android.exoplayer.chunk.BaseChunkSampleSourceEventListener
    public void onLoadCanceled(int sourceId, long bytesLoaded) {
    }

    private void maybeReportPlayerState() {
        boolean playWhenReady = this.mPlayer.getPlayWhenReady();
        int playbackState = getPlaybackState();
        if (this.mLastReportedPlayWhenReady != playWhenReady || this.mLastReportedPlaybackState != playbackState) {
            Iterator<Listener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                Listener listener = it.next();
                listener.onStateChanged(playWhenReady, playbackState);
            }
            this.mLastReportedPlayWhenReady = playWhenReady;
            this.mLastReportedPlaybackState = playbackState;
        }
    }

    private void pushSurface(boolean blockForSurfacePush) {
        if (this.mVideoRenderer != null) {
            if (blockForSurfacePush) {
                this.mPlayer.blockingSendMessage(this.mVideoRenderer, 1, this.mSurface);
            } else {
                this.mPlayer.sendMessage(this.mVideoRenderer, 1, this.mSurface);
            }
        }
    }
}
