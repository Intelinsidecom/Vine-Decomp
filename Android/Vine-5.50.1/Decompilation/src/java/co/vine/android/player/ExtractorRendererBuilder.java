package co.vine.android.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import co.vine.android.player.ExoPlayerWrapper;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

/* loaded from: classes.dex */
public class ExtractorRendererBuilder implements ExoPlayerWrapper.RendererBuilder {
    private final Context mContext;
    private final String mUrl;
    private final String mUserAgent;

    public ExtractorRendererBuilder(Context context, String userAgent, String url) {
        this.mContext = context;
        this.mUserAgent = userAgent;
        this.mUrl = url;
    }

    @Override // co.vine.android.player.ExoPlayerWrapper.RendererBuilder
    public void buildRenderers(ExoPlayerWrapper player) {
        Allocator allocator = new DefaultAllocator(65536);
        Handler handler = new Handler(Looper.getMainLooper());
        DataSource dataSource = new DefaultUriDataSource(this.mContext, this.mUserAgent);
        ExtractorSampleSource sampleSource = new ExtractorSampleSource(Uri.parse(this.mUrl), dataSource, allocator, 16777216, new Extractor[0]);
        MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(this.mContext, sampleSource, MediaCodecSelector.DEFAULT, 1, 5000L, handler, player, 50);
        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT, null, true, handler, player, AudioCapabilities.getCapabilities(this.mContext), 3);
        TrackRenderer[] renderers = {videoRenderer, audioRenderer};
        player.onRenderers(renderers);
    }

    @Override // co.vine.android.player.ExoPlayerWrapper.RendererBuilder
    public void cancel() {
    }
}
