package com.google.android.exoplayer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Surface;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.TraceUtil;
import com.google.android.exoplayer.util.Util;
import java.nio.ByteBuffer;

@TargetApi(16)
/* loaded from: classes.dex */
public class MediaCodecVideoTrackRenderer extends MediaCodecTrackRenderer {
    private final long allowedJoiningTimeUs;
    private int consecutiveDroppedFrameCount;
    private int currentHeight;
    private float currentPixelWidthHeightRatio;
    private int currentUnappliedRotationDegrees;
    private int currentWidth;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrameCount;
    private final EventListener eventListener;
    private final VideoFrameReleaseTimeHelper frameReleaseTimeHelper;
    private long joiningDeadlineUs;
    private int lastReportedHeight;
    private float lastReportedPixelWidthHeightRatio;
    private int lastReportedUnappliedRotationDegrees;
    private int lastReportedWidth;
    private final int maxDroppedFrameCountToNotify;
    private float pendingPixelWidthHeightRatio;
    private int pendingRotationDegrees;
    private boolean renderedFirstFrame;
    private boolean reportedDrawnToSurface;
    private Surface surface;
    private final int videoScalingMode;

    public interface EventListener extends MediaCodecTrackRenderer.EventListener {
        void onDrawnToSurface(Surface surface);

        void onDroppedFrames(int i, long j);

        void onVideoSizeChanged(int i, int i2, int i3, float f);
    }

    public MediaCodecVideoTrackRenderer(Context context, SampleSource source, MediaCodecSelector mediaCodecSelector, int videoScalingMode, long allowedJoiningTimeMs, Handler eventHandler, EventListener eventListener, int maxDroppedFrameCountToNotify) {
        this(context, source, mediaCodecSelector, videoScalingMode, allowedJoiningTimeMs, null, false, eventHandler, eventListener, maxDroppedFrameCountToNotify);
    }

    public MediaCodecVideoTrackRenderer(Context context, SampleSource source, MediaCodecSelector mediaCodecSelector, int videoScalingMode, long allowedJoiningTimeMs, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener, int maxDroppedFrameCountToNotify) {
        super(source, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener);
        this.frameReleaseTimeHelper = new VideoFrameReleaseTimeHelper(context);
        this.videoScalingMode = videoScalingMode;
        this.allowedJoiningTimeUs = 1000 * allowedJoiningTimeMs;
        this.eventListener = eventListener;
        this.maxDroppedFrameCountToNotify = maxDroppedFrameCountToNotify;
        this.joiningDeadlineUs = -1L;
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.lastReportedWidth = -1;
        this.lastReportedHeight = -1;
        this.lastReportedPixelWidthHeightRatio = -1.0f;
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer
    protected boolean handlesTrack(MediaCodecSelector mediaCodecSelector, MediaFormat mediaFormat) throws MediaCodecUtil.DecoderQueryException {
        String mimeType = mediaFormat.mimeType;
        if (MimeTypes.isVideo(mimeType)) {
            return "video/x-unknown".equals(mimeType) || mediaCodecSelector.getDecoderInfo(mediaFormat, false) != null;
        }
        return false;
    }

    @Override // com.google.android.exoplayer.SampleSourceTrackRenderer, com.google.android.exoplayer.TrackRenderer
    protected void onEnabled(int track, long positionUs, boolean joining) throws ExoPlaybackException {
        super.onEnabled(track, positionUs, joining);
        if (joining && this.allowedJoiningTimeUs > 0) {
            this.joiningDeadlineUs = (SystemClock.elapsedRealtime() * 1000) + this.allowedJoiningTimeUs;
        }
        this.frameReleaseTimeHelper.enable();
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer, com.google.android.exoplayer.SampleSourceTrackRenderer
    protected void onDiscontinuity(long positionUs) throws ExoPlaybackException {
        super.onDiscontinuity(positionUs);
        this.renderedFirstFrame = false;
        this.consecutiveDroppedFrameCount = 0;
        this.joiningDeadlineUs = -1L;
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer, com.google.android.exoplayer.TrackRenderer
    protected boolean isReady() {
        if (super.isReady() && (this.renderedFirstFrame || !codecInitialized() || getSourceState() == 2)) {
            this.joiningDeadlineUs = -1L;
            return true;
        }
        if (this.joiningDeadlineUs == -1) {
            return false;
        }
        if (SystemClock.elapsedRealtime() * 1000 < this.joiningDeadlineUs) {
            return true;
        }
        this.joiningDeadlineUs = -1L;
        return false;
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer, com.google.android.exoplayer.TrackRenderer
    protected void onStarted() {
        super.onStarted();
        this.droppedFrameCount = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer, com.google.android.exoplayer.TrackRenderer
    protected void onStopped() {
        this.joiningDeadlineUs = -1L;
        maybeNotifyDroppedFrameCount();
        super.onStopped();
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer, com.google.android.exoplayer.SampleSourceTrackRenderer, com.google.android.exoplayer.TrackRenderer
    protected void onDisabled() throws ExoPlaybackException {
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.lastReportedWidth = -1;
        this.lastReportedHeight = -1;
        this.lastReportedPixelWidthHeightRatio = -1.0f;
        this.frameReleaseTimeHelper.disable();
        super.onDisabled();
    }

    @Override // com.google.android.exoplayer.TrackRenderer, com.google.android.exoplayer.ExoPlayer.ExoPlayerComponent
    public void handleMessage(int messageType, Object message) throws ExoPlaybackException {
        if (messageType == 1) {
            setSurface((Surface) message);
        } else {
            super.handleMessage(messageType, message);
        }
    }

    private void setSurface(Surface surface) throws ExoPlaybackException {
        if (this.surface != surface) {
            this.surface = surface;
            this.reportedDrawnToSurface = false;
            int state = getState();
            if (state == 2 || state == 3) {
                releaseCodec();
                maybeInitCodec();
            }
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer
    protected boolean shouldInitCodec() {
        return super.shouldInitCodec() && this.surface != null && this.surface.isValid();
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer
    protected void configureCodec(MediaCodec codec, boolean codecIsAdaptive, android.media.MediaFormat format, MediaCrypto crypto) {
        maybeSetMaxInputSize(format, codecIsAdaptive);
        codec.configure(format, this.surface, crypto, 0);
        codec.setVideoScalingMode(this.videoScalingMode);
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer
    protected void onInputFormatChanged(MediaFormatHolder holder) throws ExoPlaybackException {
        super.onInputFormatChanged(holder);
        this.pendingPixelWidthHeightRatio = holder.format.pixelWidthHeightRatio == -1.0f ? 1.0f : holder.format.pixelWidthHeightRatio;
        this.pendingRotationDegrees = holder.format.rotationDegrees == -1 ? 0 : holder.format.rotationDegrees;
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer
    protected void onOutputFormatChanged(android.media.MediaFormat outputFormat) {
        boolean hasCrop = outputFormat.containsKey("crop-right") && outputFormat.containsKey("crop-left") && outputFormat.containsKey("crop-bottom") && outputFormat.containsKey("crop-top");
        this.currentWidth = hasCrop ? (outputFormat.getInteger("crop-right") - outputFormat.getInteger("crop-left")) + 1 : outputFormat.getInteger("width");
        this.currentHeight = hasCrop ? (outputFormat.getInteger("crop-bottom") - outputFormat.getInteger("crop-top")) + 1 : outputFormat.getInteger("height");
        this.currentPixelWidthHeightRatio = this.pendingPixelWidthHeightRatio;
        if (Util.SDK_INT >= 21) {
            if (this.pendingRotationDegrees == 90 || this.pendingRotationDegrees == 270) {
                int rotatedHeight = this.currentWidth;
                this.currentWidth = this.currentHeight;
                this.currentHeight = rotatedHeight;
                this.currentPixelWidthHeightRatio = 1.0f / this.currentPixelWidthHeightRatio;
                return;
            }
            return;
        }
        this.currentUnappliedRotationDegrees = this.pendingRotationDegrees;
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer
    protected boolean canReconfigureCodec(MediaCodec codec, boolean codecIsAdaptive, MediaFormat oldFormat, MediaFormat newFormat) {
        return newFormat.mimeType.equals(oldFormat.mimeType) && (codecIsAdaptive || (oldFormat.width == newFormat.width && oldFormat.height == newFormat.height));
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer
    protected boolean processOutputBuffer(long positionUs, long elapsedRealtimeUs, MediaCodec codec, ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo, int bufferIndex, boolean shouldSkip) throws InterruptedException {
        if (shouldSkip) {
            skipOutputBuffer(codec, bufferIndex);
            this.consecutiveDroppedFrameCount = 0;
            return true;
        }
        if (!this.renderedFirstFrame) {
            if (Util.SDK_INT >= 21) {
                renderOutputBufferV21(codec, bufferIndex, System.nanoTime());
            } else {
                renderOutputBuffer(codec, bufferIndex);
            }
            this.consecutiveDroppedFrameCount = 0;
            return true;
        }
        if (getState() != 3) {
            return false;
        }
        long elapsedSinceStartOfLoopUs = (SystemClock.elapsedRealtime() * 1000) - elapsedRealtimeUs;
        long earlyUs = (bufferInfo.presentationTimeUs - positionUs) - elapsedSinceStartOfLoopUs;
        long systemTimeNs = System.nanoTime();
        long unadjustedFrameReleaseTimeNs = systemTimeNs + (1000 * earlyUs);
        long adjustedReleaseTimeNs = this.frameReleaseTimeHelper.adjustReleaseTime(bufferInfo.presentationTimeUs, unadjustedFrameReleaseTimeNs);
        long earlyUs2 = (adjustedReleaseTimeNs - systemTimeNs) / 1000;
        if (earlyUs2 < -30000) {
            dropOutputBuffer(codec, bufferIndex);
            return true;
        }
        if (Util.SDK_INT >= 21) {
            if (earlyUs2 < 50000) {
                renderOutputBufferV21(codec, bufferIndex, adjustedReleaseTimeNs);
                this.consecutiveDroppedFrameCount = 0;
                return true;
            }
        } else if (earlyUs2 < 30000) {
            if (earlyUs2 > 11000) {
                try {
                    Thread.sleep((earlyUs2 - 10000) / 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            renderOutputBuffer(codec, bufferIndex);
            this.consecutiveDroppedFrameCount = 0;
            return true;
        }
        return false;
    }

    protected void skipOutputBuffer(MediaCodec codec, int bufferIndex) {
        TraceUtil.beginSection("skipVideoBuffer");
        codec.releaseOutputBuffer(bufferIndex, false);
        TraceUtil.endSection();
        this.codecCounters.skippedOutputBufferCount++;
    }

    protected void dropOutputBuffer(MediaCodec codec, int bufferIndex) {
        TraceUtil.beginSection("dropVideoBuffer");
        codec.releaseOutputBuffer(bufferIndex, false);
        TraceUtil.endSection();
        this.codecCounters.droppedOutputBufferCount++;
        this.droppedFrameCount++;
        this.consecutiveDroppedFrameCount++;
        this.codecCounters.maxConsecutiveDroppedOutputBufferCount = Math.max(this.consecutiveDroppedFrameCount, this.codecCounters.maxConsecutiveDroppedOutputBufferCount);
        if (this.droppedFrameCount == this.maxDroppedFrameCountToNotify) {
            maybeNotifyDroppedFrameCount();
        }
    }

    protected void renderOutputBuffer(MediaCodec codec, int bufferIndex) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        codec.releaseOutputBuffer(bufferIndex, true);
        TraceUtil.endSection();
        this.codecCounters.renderedOutputBufferCount++;
        this.renderedFirstFrame = true;
        maybeNotifyDrawnToSurface();
    }

    @TargetApi(21)
    protected void renderOutputBufferV21(MediaCodec codec, int bufferIndex, long releaseTimeNs) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        codec.releaseOutputBuffer(bufferIndex, releaseTimeNs);
        TraceUtil.endSection();
        this.codecCounters.renderedOutputBufferCount++;
        this.renderedFirstFrame = true;
        maybeNotifyDrawnToSurface();
    }

    @SuppressLint({"InlinedApi"})
    private void maybeSetMaxInputSize(android.media.MediaFormat format, boolean codecIsAdaptive) {
        int maxHeight;
        int maxWidth;
        int maxPixels;
        int minCompressionRatio;
        if (!format.containsKey("max-input-size")) {
            maxHeight = format.getInteger("height");
            if (codecIsAdaptive && format.containsKey("max-height")) {
                maxHeight = Math.max(maxHeight, format.getInteger("max-height"));
            }
            maxWidth = format.getInteger("width");
            if (codecIsAdaptive && format.containsKey("max-width")) {
                maxWidth = Math.max(maxHeight, format.getInteger("max-width"));
            }
            switch (format.getString("mime")) {
                case "video/avc":
                    if (!"BRAVIA 4K 2015".equals(Util.MODEL)) {
                        maxPixels = ((maxWidth + 15) / 16) * ((maxHeight + 15) / 16) * 16 * 16;
                        minCompressionRatio = 2;
                        break;
                    } else {
                        return;
                    }
                case "video/x-vnd.on2.vp8":
                    maxPixels = maxWidth * maxHeight;
                    minCompressionRatio = 2;
                    break;
                case "video/x-vnd.on2.vp9":
                    maxPixels = maxWidth * maxHeight;
                    minCompressionRatio = 4;
                    break;
                default:
                    return;
            }
            int maxInputSize = (maxPixels * 3) / (minCompressionRatio * 2);
            format.setInteger("max-input-size", maxInputSize);
        }
    }

    private void maybeNotifyVideoSizeChanged() {
        if (this.eventHandler == null || this.eventListener == null) {
            return;
        }
        if (this.lastReportedWidth != this.currentWidth || this.lastReportedHeight != this.currentHeight || this.lastReportedUnappliedRotationDegrees != this.currentUnappliedRotationDegrees || this.lastReportedPixelWidthHeightRatio != this.currentPixelWidthHeightRatio) {
            final int currentWidth = this.currentWidth;
            final int currentHeight = this.currentHeight;
            final int currentUnappliedRotationDegrees = this.currentUnappliedRotationDegrees;
            final float currentPixelWidthHeightRatio = this.currentPixelWidthHeightRatio;
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer.MediaCodecVideoTrackRenderer.1
                @Override // java.lang.Runnable
                public void run() {
                    MediaCodecVideoTrackRenderer.this.eventListener.onVideoSizeChanged(currentWidth, currentHeight, currentUnappliedRotationDegrees, currentPixelWidthHeightRatio);
                }
            });
            this.lastReportedWidth = currentWidth;
            this.lastReportedHeight = currentHeight;
            this.lastReportedUnappliedRotationDegrees = currentUnappliedRotationDegrees;
            this.lastReportedPixelWidthHeightRatio = currentPixelWidthHeightRatio;
        }
    }

    private void maybeNotifyDrawnToSurface() {
        if (this.eventHandler != null && this.eventListener != null && !this.reportedDrawnToSurface) {
            final Surface surface = this.surface;
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer.MediaCodecVideoTrackRenderer.2
                @Override // java.lang.Runnable
                public void run() {
                    MediaCodecVideoTrackRenderer.this.eventListener.onDrawnToSurface(surface);
                }
            });
            this.reportedDrawnToSurface = true;
        }
    }

    private void maybeNotifyDroppedFrameCount() {
        if (this.eventHandler != null && this.eventListener != null && this.droppedFrameCount != 0) {
            long now = SystemClock.elapsedRealtime();
            final int countToNotify = this.droppedFrameCount;
            final long elapsedToNotify = now - this.droppedFrameAccumulationStartTimeMs;
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer.MediaCodecVideoTrackRenderer.3
                @Override // java.lang.Runnable
                public void run() {
                    MediaCodecVideoTrackRenderer.this.eventListener.onDroppedFrames(countToNotify, elapsedToNotify);
                }
            });
            this.droppedFrameCount = 0;
            this.droppedFrameAccumulationStartTimeMs = now;
        }
    }
}
