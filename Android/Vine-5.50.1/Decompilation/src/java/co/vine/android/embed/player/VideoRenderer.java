package co.vine.android.embed.player;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import co.vine.android.util.MediaCodecUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

/* loaded from: classes.dex */
final class VideoRenderer {
    private static long sLastRenderMs;
    private MediaCodec mCodec;
    private int mCurrentFrameIndex;
    private int mCurrentHeight;
    private ByteBuffer mCurrentInputBuffer;
    private int mCurrentWidth;
    private MediaFormat mFormat;
    private byte[] mFrameData;
    private MediaCodec.BufferInfo[] mFrameInfo;
    private ByteBuffer[] mInputBuffers;
    private int mInputIndex;
    private boolean mJustMadeActive;
    private long mLastBufferTimeUs;
    private long mLastPositionUs;
    private int mLastRenderedFrameNumber;
    private int mOutputIndex;
    private long mRenderedFrames;
    private Surface mSurface;
    private final String mTag;
    private boolean mWaitingForFirstSyncFrame;
    private final HashMap<Long, Integer> mPresentationUsToIndexMap = new HashMap<>();
    private int mFirstRendererdFrameIndex = -1;
    private final MediaCodec.BufferInfo mOutputBufferInfo = new MediaCodec.BufferInfo();

    public VideoRenderer(int id) {
        this.mTag = "VideoRenderer " + id;
    }

    public void release() {
        releaseCodec();
        this.mCurrentInputBuffer = null;
        this.mInputBuffers = null;
        this.mFrameInfo = null;
        this.mFrameData = null;
    }

    public void doSomeWork(MediaClock clock, boolean hasAudioTrack, boolean isActive) throws MediaCodec.CryptoException, IOException {
        boolean feedMore;
        long positionUs = clock.getLatencyAdjustedPositionUs();
        if (this.mCurrentFrameIndex >= this.mFrameInfo.length || (this.mLastPositionUs > positionUs && !clock.hasNegativePosition())) {
            if (isActive && !this.mJustMadeActive) {
                this.mCurrentFrameIndex = 0;
                if (!hasAudioTrack) {
                    clock.setPosition(0L);
                }
                flushCodec();
            }
            this.mJustMadeActive = false;
        }
        while (true) {
            if (this.mOutputIndex < 0) {
                this.mOutputIndex = this.mCodec.dequeueOutputBuffer(this.mOutputBufferInfo, 0L);
            }
            if (this.mOutputIndex == -2) {
                onOutputFormatChanged(this.mCodec.getOutputFormat());
            } else if (this.mOutputIndex != -3) {
                if (this.mOutputIndex < 0 || !processOutputBuffer(clock, this.mCodec, this.mOutputBufferInfo, this.mOutputIndex, isActive)) {
                    break;
                }
                if (isActive && !hasAudioTrack) {
                    clock.setPosition(this.mOutputBufferInfo.presentationTimeUs);
                }
                this.mOutputIndex = -1;
            } else {
                continue;
            }
        }
        do {
            feedMore = feedInputBuffer(clock, isActive && !hasAudioTrack);
        } while (feedMore);
        this.mLastPositionUs = positionUs;
    }

    public void prepare(VineSampleSource source, Surface surface) throws IOException {
        System.currentTimeMillis();
        this.mFrameInfo = source.getVideoFrameInfo();
        this.mFrameData = source.getVideoFrames();
        this.mFormat = source.getVideoTrackFormat();
        if (this.mSurface != surface) {
            this.mInputIndex = -1;
            this.mOutputIndex = -1;
            this.mSurface = surface;
            releaseCodec();
            initCodec();
        }
    }

    private void initCodec() throws IOException {
        String selectedDecoderName = MediaCodecUtil.getDecoderInfo(this.mFormat.getString("mime"));
        this.mCodec = MediaCodec.createByCodecName(selectedDecoderName);
        this.mCodec.configure(this.mFormat, this.mSurface, (MediaCrypto) null, 0);
        this.mCodec.setVideoScalingMode(1);
        this.mCodec.start();
        this.mInputBuffers = this.mCodec.getInputBuffers();
        this.mInputIndex = -1;
        this.mOutputIndex = -1;
        this.mWaitingForFirstSyncFrame = true;
    }

    private void releaseCodec() {
        if (this.mCodec != null) {
            this.mInputIndex = -1;
            this.mOutputIndex = -1;
            this.mInputBuffers = null;
            try {
                try {
                    this.mCodec.stop();
                    try {
                        this.mCodec.release();
                    } finally {
                    }
                } catch (Throwable th) {
                    try {
                        this.mCodec.release();
                        throw th;
                    } finally {
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                try {
                    this.mCodec.release();
                } finally {
                }
            }
        }
    }

    private boolean processOutputBuffer(MediaClock clock, MediaCodec codec, MediaCodec.BufferInfo bufferInfo, int bufferIndex, boolean modifyClock) throws InterruptedException {
        if (modifyClock && clock.getPositionSetTimeMs() == 0) {
            clock.setPosition(0L);
        }
        Integer frameIndex = this.mPresentationUsToIndexMap.get(Long.valueOf(bufferInfo.presentationTimeUs));
        String str = "\n\n#" + frameIndex;
        if (!modifyClock && this.mRenderedFrames >= 1) {
            return false;
        }
        long bufferTime = bufferInfo.presentationTimeUs;
        long releaseMaxTime = 35000;
        long releaseMinTime = 11000;
        if (frameIndex.intValue() > this.mFrameInfo.length - 10) {
            bufferTime -= 17000;
            releaseMaxTime = 35000 - 17000;
            releaseMinTime = 0;
        }
        long timeSinceLastDrain = System.currentTimeMillis() - sLastRenderMs;
        long elapsedLatencyAdjustedTime = 1000 * (SystemClock.elapsedRealtime() - clock.getLatencyAdjustedPositionSetMs());
        long audioWithLatency = clock.getLatencyAdjustedPositionUs();
        long earlyUs = (bufferTime - audioWithLatency) - elapsedLatencyAdjustedTime;
        long renderingGapUs = (System.currentTimeMillis() - sLastRenderMs) * 1000;
        long j = renderingGapUs + earlyUs;
        if (earlyUs < -30000 && earlyUs > -70000) {
            renderOutputBufferImmediate(codec, bufferIndex, bufferInfo.presentationTimeUs, modifyClock);
            return true;
        }
        if (timeSinceLastDrain > 33 && this.mFirstRendererdFrameIndex == -1) {
            renderOutputBufferImmediate(codec, bufferIndex, bufferInfo.presentationTimeUs, modifyClock);
            return true;
        }
        if (earlyUs < -70000 && this.mFirstRendererdFrameIndex >= 0) {
            dropOutputBuffer(codec, bufferIndex);
            return true;
        }
        if (earlyUs >= releaseMaxTime) {
            if (timeSinceLastDrain > 33) {
                renderOutputBufferImmediate(codec, bufferIndex, bufferInfo.presentationTimeUs, modifyClock);
                return true;
            }
            return false;
        }
        if (earlyUs - releaseMinTime > 100) {
            try {
                Thread.sleep((earlyUs - (releaseMinTime - 100)) / 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        renderOutputBufferImmediate(codec, bufferIndex, bufferInfo.presentationTimeUs, modifyClock);
        return true;
    }

    private void dropOutputBuffer(MediaCodec codec, int bufferIndex) {
        codec.releaseOutputBuffer(bufferIndex, false);
    }

    private void renderOutputBufferImmediate(MediaCodec codec, int bufferIndex, long presentationTimeUs, boolean isActive) {
        if (isActive) {
            if (sLastRenderMs > 0) {
                long timeSinceLastDrain = System.currentTimeMillis() - sLastRenderMs;
                if (timeSinceLastDrain > 50) {
                    Log.e(this.mTag, "!Delayed! Time since last frame was rendered " + timeSinceLastDrain);
                }
            }
            sLastRenderMs = System.currentTimeMillis();
        }
        this.mLastBufferTimeUs = presentationTimeUs;
        int frameNumber = this.mPresentationUsToIndexMap.get(Long.valueOf(this.mLastBufferTimeUs)).intValue();
        if (Math.abs(frameNumber - this.mLastRenderedFrameNumber) < 5) {
            this.mLastRenderedFrameNumber = Math.max(frameNumber, this.mLastRenderedFrameNumber);
        } else {
            this.mLastRenderedFrameNumber = frameNumber;
        }
        this.mRenderedFrames++;
        if (isActive && this.mFirstRendererdFrameIndex == -1) {
            this.mFirstRendererdFrameIndex = this.mLastRenderedFrameNumber;
        }
        codec.releaseOutputBuffer(bufferIndex, true);
    }

    private void flushCodec() throws IOException {
        long start = System.currentTimeMillis();
        this.mInputIndex = -1;
        this.mOutputIndex = -1;
        this.mWaitingForFirstSyncFrame = true;
        if (Build.VERSION.SDK_INT >= 18) {
            this.mCodec.flush();
        } else {
            releaseCodec();
            initCodec();
        }
        Log.i(this.mTag, "Flush took " + (System.currentTimeMillis() - start));
    }

    public int getLastRenderedIndex() {
        return this.mLastRenderedFrameNumber;
    }

    private boolean feedInputBuffer(MediaClock clock, boolean modifyClock) throws MediaCodec.CryptoException {
        if (this.mInputIndex < 0) {
            this.mInputIndex = this.mCodec.dequeueInputBuffer(0L);
            if (this.mInputIndex < 0) {
                return false;
            }
            this.mCurrentInputBuffer = this.mInputBuffers[this.mInputIndex];
            this.mCurrentInputBuffer.clear();
        }
        MediaCodec.BufferInfo nextFrame = this.mFrameInfo[this.mCurrentFrameIndex];
        int offset = this.mCurrentInputBuffer.position();
        this.mPresentationUsToIndexMap.put(Long.valueOf(nextFrame.presentationTimeUs), Integer.valueOf(this.mCurrentFrameIndex));
        this.mCurrentInputBuffer.put(this.mFrameData, nextFrame.offset, nextFrame.size);
        this.mCurrentInputBuffer.position(nextFrame.size + offset);
        this.mCurrentFrameIndex++;
        if (this.mWaitingForFirstSyncFrame) {
            if ((nextFrame.flags & 1) == 0) {
                this.mCurrentInputBuffer.clear();
                return true;
            }
            this.mWaitingForFirstSyncFrame = false;
        }
        int bufferSize = this.mCurrentInputBuffer != null ? this.mCurrentInputBuffer.position() : 0;
        long presentationTimeUs = nextFrame.presentationTimeUs;
        this.mCodec.queueInputBuffer(this.mInputIndex, 0, bufferSize, presentationTimeUs, 0);
        this.mInputIndex = -1;
        if (this.mCurrentFrameIndex >= this.mFrameInfo.length) {
            this.mCurrentFrameIndex = 0;
            if (modifyClock) {
                clock.setPosition(0L);
            }
        }
        return true;
    }

    private void onOutputFormatChanged(MediaFormat format) {
        int integer;
        int integer2;
        boolean hasCrop = format.containsKey("crop-right") && format.containsKey("crop-left") && format.containsKey("crop-bottom") && format.containsKey("crop-top");
        if (hasCrop) {
            integer = (format.getInteger("crop-right") - format.getInteger("crop-left")) + 1;
        } else {
            integer = format.getInteger("width");
        }
        this.mCurrentWidth = integer;
        if (hasCrop) {
            integer2 = (format.getInteger("crop-bottom") - format.getInteger("crop-top")) + 1;
        } else {
            integer2 = format.getInteger("height");
        }
        this.mCurrentHeight = integer2;
    }

    public int getWidth() {
        return this.mCurrentWidth;
    }

    public int getHeight() {
        return this.mCurrentHeight;
    }

    public long getDurationMs() {
        if (this.mFrameInfo != null) {
            return this.mFrameInfo[this.mFrameInfo.length - 1].presentationTimeUs / 1000;
        }
        return -1L;
    }

    public long getFramesRendered() {
        return this.mRenderedFrames;
    }

    public void reset() throws IOException {
        this.mCurrentFrameIndex = 0;
        this.mRenderedFrames = 0L;
        this.mFirstRendererdFrameIndex = -1;
        this.mLastPositionUs = 0L;
        flushCodec();
    }

    public void onMakeActive() {
        this.mFirstRendererdFrameIndex = -1;
        this.mJustMadeActive = true;
    }
}
