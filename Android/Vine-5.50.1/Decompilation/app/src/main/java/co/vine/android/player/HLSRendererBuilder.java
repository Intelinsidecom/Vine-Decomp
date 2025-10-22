package co.vine.android.player;

import android.content.Context;
import android.os.Handler;
import co.vine.android.player.ExoPlayerWrapper;
import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.hls.DefaultHlsTrackSelector;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.hls.PtsTimestampAdjusterProvider;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import java.io.IOException;

/* loaded from: classes.dex */
public class HLSRendererBuilder implements ExoPlayerWrapper.RendererBuilder {
    private AsyncRendererBuilder currentAsyncBuilder;
    private final Context mContext;
    private final String mUrl;
    private final String mUserAgent;

    public HLSRendererBuilder(Context context, String userAgent, String url) {
        this.mContext = context;
        this.mUserAgent = userAgent;
        this.mUrl = url;
    }

    @Override // co.vine.android.player.ExoPlayerWrapper.RendererBuilder
    public void buildRenderers(ExoPlayerWrapper player) {
        this.currentAsyncBuilder = new AsyncRendererBuilder(this.mContext, this.mUserAgent, this.mUrl, player);
        this.currentAsyncBuilder.init();
    }

    @Override // co.vine.android.player.ExoPlayerWrapper.RendererBuilder
    public void cancel() {
        if (this.currentAsyncBuilder != null) {
            this.currentAsyncBuilder.cancel();
            this.currentAsyncBuilder = null;
        }
    }

    private static final class AsyncRendererBuilder implements ManifestFetcher.ManifestCallback<HlsPlaylist> {
        private boolean canceled;
        private final Context mContext;
        private final ExoPlayerWrapper mPlayer;
        private final ManifestFetcher<HlsPlaylist> mPlaylistFetcher;
        private final String mUrl;
        private final String mUserAgent;

        public AsyncRendererBuilder(Context context, String userAgent, String url, ExoPlayerWrapper player) {
            this.mContext = context;
            this.mUserAgent = userAgent;
            this.mUrl = url;
            this.mPlayer = player;
            HlsPlaylistParser parser = new HlsPlaylistParser();
            this.mPlaylistFetcher = new ManifestFetcher<>(url, new DefaultUriDataSource(context, userAgent), parser);
        }

        public void init() {
            this.mPlaylistFetcher.singleLoad(this.mPlayer.getmMainHandler().getLooper(), this);
        }

        public void cancel() {
            this.canceled = true;
        }

        @Override // com.google.android.exoplayer.util.ManifestFetcher.ManifestCallback
        public void onSingleManifestError(IOException e) {
            if (!this.canceled) {
                this.mPlayer.onRenderersError(e);
            }
        }

        @Override // com.google.android.exoplayer.util.ManifestFetcher.ManifestCallback
        public void onSingleManifest(HlsPlaylist manifest) {
            if (!this.canceled) {
                Handler mainHandler = this.mPlayer.getmMainHandler();
                LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(65536));
                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                PtsTimestampAdjusterProvider timestampAdjusterProvider = new PtsTimestampAdjusterProvider();
                DataSource dataSource = new DefaultUriDataSource(this.mContext, bandwidthMeter, this.mUserAgent);
                HlsChunkSource chunkSource = new HlsChunkSource(true, dataSource, this.mUrl, manifest, DefaultHlsTrackSelector.newDefaultInstance(this.mContext), bandwidthMeter, timestampAdjusterProvider, 1);
                HlsSampleSource sampleSource = new HlsSampleSource(chunkSource, loadControl, 16777216, mainHandler, this.mPlayer, 0);
                MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(this.mContext, sampleSource, MediaCodecSelector.DEFAULT, 1, 5000L, mainHandler, this.mPlayer, 50);
                MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT, null, true, this.mPlayer.getmMainHandler(), this.mPlayer, AudioCapabilities.getCapabilities(this.mContext), 3);
                TrackRenderer[] renderers = {videoRenderer, audioRenderer};
                this.mPlayer.onRenderers(renderers);
            }
        }
    }
}
