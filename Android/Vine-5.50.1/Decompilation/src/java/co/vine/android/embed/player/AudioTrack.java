package co.vine.android.embed.player;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.AudioTimestamp;
import android.media.MediaFormat;
import android.os.Build;
import android.os.ConditionVariable;
import android.support.v4.view.PointerIconCompat;
import android.util.Log;
import com.googlecode.javacv.cpp.opencv_core;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

@TargetApi(16)
/* loaded from: classes.dex */
public final class AudioTrack {
    private int ac3Bitrate;
    private boolean audioTimestampSet;
    private android.media.AudioTrack audioTrack;
    private AudioTrackUtil audioTrackUtil;
    private int bufferSize;
    private int channelConfig;
    private int encoding;
    private int frameSize;
    private Method getLatencyMethod;
    private boolean isAc3;
    private long lastPlayheadSampleTimeUs;
    private long lastTimestampSampleTimeUs;
    private long latencyUs;
    private int mLastSize;
    private int minBufferSize;
    private int nextPlayheadOffsetIndex;
    private int playheadOffsetCount;
    private final long[] playheadOffsets;
    private long resumeSystemTimeUs;
    private int sampleRate;
    private long smoothedPlayheadOffsetUs;
    private int startMediaTimeState;
    private long startMediaTimeUs;
    private long submittedBytes;
    private byte[] temporaryBuffer;
    private int temporaryBufferOffset;
    private int temporaryBufferSize;
    private float volume;
    private final boolean mUseLegacyWritingMethods = true;
    private final ConditionVariable releasingConditionVariable = new ConditionVariable(true);

    public AudioTrack() {
        if (Util.SDK_INT >= 18) {
            try {
                this.getLatencyMethod = android.media.AudioTrack.class.getMethod("getLatency", (Class[]) null);
            } catch (NoSuchMethodException e) {
            }
        }
        this.playheadOffsets = new long[10];
        this.volume = 1.0f;
        this.startMediaTimeState = -1;
    }

    @TargetApi(21)
    private static int writeNonBlockingV21(android.media.AudioTrack audioTrack, ByteBuffer buffer, int size) {
        return audioTrack.write(buffer, size, 1);
    }

    @TargetApi(21)
    private static void setVolumeV21(android.media.AudioTrack audioTrack, float volume) {
        audioTrack.setVolume(volume);
    }

    private static void setVolumeV3(android.media.AudioTrack audioTrack, float volume) {
        audioTrack.setStereoVolume(volume, volume);
    }

    public int getLastSize() {
        return this.mLastSize;
    }

    public boolean isInitialized() {
        return this.audioTrack != null;
    }

    public int initialize() throws InitializationException {
        return initialize(0);
    }

    public long getLatencyUs() {
        return this.latencyUs;
    }

    public int initialize(int sessionId) throws InitializationException {
        this.releasingConditionVariable.block();
        if (sessionId == 0) {
            this.audioTrack = new android.media.AudioTrack(3, this.sampleRate, this.channelConfig, this.encoding, this.bufferSize, 1);
        } else {
            this.audioTrack = new android.media.AudioTrack(3, this.sampleRate, this.channelConfig, this.encoding, this.bufferSize, 1, sessionId);
        }
        checkAudioTrackInitialized();
        if (Util.SDK_INT >= 19) {
            this.audioTrackUtil = new AudioTrackUtilV19(this.audioTrack);
        } else {
            this.audioTrackUtil = new AudioTrackUtil(this.audioTrack);
        }
        setVolume(this.volume);
        return this.audioTrack.getAudioSessionId();
    }

    public void reconfigure(MediaFormat format) {
        reconfigure(format, 2, 0);
    }

    @SuppressLint({"InlinedApi"})
    private void reconfigure(MediaFormat format, int encoding, int specifiedBufferSize) {
        int channelConfig;
        int channelCount = format.getInteger("channel-count");
        switch (channelCount) {
            case 1:
                channelConfig = 4;
                break;
            case 2:
                channelConfig = 12;
                break;
            case 3:
            case 4:
            case 5:
            case 7:
            default:
                throw new IllegalArgumentException("Unsupported channel count: " + channelCount);
            case 6:
                channelConfig = 252;
                break;
            case 8:
                channelConfig = PointerIconCompat.TYPE_GRAB;
                break;
        }
        int sampleRate = format.getInteger("sample-rate");
        boolean isAc3 = encoding == 5 || encoding == 6;
        if (!isInitialized() || this.sampleRate != sampleRate || this.channelConfig != channelConfig || this.isAc3 || isAc3) {
            reset();
            this.encoding = encoding;
            this.sampleRate = sampleRate;
            this.channelConfig = channelConfig;
            this.isAc3 = isAc3;
            this.ac3Bitrate = 0;
            this.frameSize = channelCount * 2;
            this.minBufferSize = android.media.AudioTrack.getMinBufferSize(sampleRate, channelConfig, encoding);
            Log.i("AudioTrack", "Buffer size: " + this.minBufferSize);
            if (specifiedBufferSize != 0) {
                this.bufferSize = specifiedBufferSize;
                return;
            }
            int multipliedBufferSize = this.minBufferSize * 1;
            int minAppBufferSize = ((int) durationUsToFrames(250000L)) * this.frameSize;
            int maxAppBufferSize = (int) Math.max(this.minBufferSize, durationUsToFrames(750000L) * this.frameSize);
            if (multipliedBufferSize >= minAppBufferSize) {
                minAppBufferSize = multipliedBufferSize > maxAppBufferSize ? maxAppBufferSize : multipliedBufferSize;
            }
            this.bufferSize = minAppBufferSize;
        }
    }

    public void play() throws IllegalStateException {
        if (isInitialized()) {
            this.resumeSystemTimeUs = System.nanoTime() / 1000;
            this.audioTrack.play();
        }
    }

    public int handleBuffer(ByteBuffer buffer, int offset, int size, long presentationTimeUs) throws WriteException {
        int bytesToWrite;
        if (size <= 0) {
            return 2;
        }
        int result = 0;
        if (this.temporaryBufferSize == 0) {
            if (this.isAc3 && this.ac3Bitrate == 0) {
                int unscaledAc3Bitrate = size * 8 * this.sampleRate;
                this.ac3Bitrate = (768000 + unscaledAc3Bitrate) / 1536000;
            }
            long bufferStartTime = presentationTimeUs - framesToDurationUs(bytesToFrames(size));
            if (this.startMediaTimeUs == -1) {
                this.startMediaTimeUs = Math.max(0L, bufferStartTime);
                this.startMediaTimeState = 1;
            } else {
                long expectedBufferStartTime = this.startMediaTimeUs + framesToDurationUs(bytesToFrames(this.submittedBytes));
                if (this.startMediaTimeState == 1 && Math.abs(expectedBufferStartTime - bufferStartTime) > 200000) {
                    Log.e("AudioTrack", "Discontinuity detected [expected " + expectedBufferStartTime + ", got " + bufferStartTime + "]");
                    this.startMediaTimeState = 2;
                }
                if (this.startMediaTimeState == 2) {
                    this.startMediaTimeUs += bufferStartTime - expectedBufferStartTime;
                    this.startMediaTimeState = 1;
                    result = 0 | 1;
                }
            }
        }
        if (this.temporaryBufferSize == 0) {
            if (offset >= buffer.limit()) {
                return 2;
            }
            buffer.position(offset);
            this.mLastSize = Math.min(size, buffer.remaining());
            int size2 = this.mLastSize;
            this.temporaryBufferSize = size2;
            if (this.mUseLegacyWritingMethods) {
                if (this.temporaryBuffer == null || this.temporaryBuffer.length < size2) {
                    this.temporaryBuffer = new byte[size2];
                }
                try {
                    buffer.get(this.temporaryBuffer, 0, size2);
                    this.temporaryBufferOffset = 0;
                } catch (BufferUnderflowException e) {
                    Log.e("AudioTrack", "Want " + size2 + " has " + buffer.remaining());
                    throw e;
                }
            }
        }
        int bytesWritten = 0;
        if (this.mUseLegacyWritingMethods) {
            int bytesPending = (int) (this.submittedBytes - (this.audioTrackUtil.getPlaybackHeadPosition() * this.frameSize));
            int bytesToWrite2 = this.bufferSize - bytesPending;
            if (bytesToWrite2 > 0 && (bytesToWrite = Math.min(this.temporaryBufferSize, bytesToWrite2)) > 0) {
                bytesWritten = this.audioTrack.write(this.temporaryBuffer, this.temporaryBufferOffset, bytesToWrite);
                if (bytesWritten > 0) {
                    this.temporaryBufferOffset += bytesWritten;
                } else {
                    switch (bytesWritten) {
                        case opencv_core.CV_StsInternal /* -3 */:
                            Log.e("AudioTrack", "Invalid operation.");
                            break;
                        case -2:
                            if (this.temporaryBuffer == null) {
                                Log.e("AudioTrack", "Buffer was null");
                            }
                            if (this.temporaryBufferOffset < 0) {
                                Log.e("AudioTrack", "TemporaryBufferOffset was negative");
                            }
                            if (bytesToWrite < 0) {
                                Log.e("AudioTrack", "Tried to write negative bytes.");
                            }
                            if (this.temporaryBufferOffset + bytesToWrite < 0) {
                                Log.e("AudioTrack", "Integer overflow.");
                            }
                            if (this.temporaryBufferOffset + bytesToWrite > this.temporaryBuffer.length) {
                                Log.e("AudioTrack", "Bytes to write is too big.");
                                break;
                            }
                            break;
                        case -1:
                            Log.e("AudioTrack", "Unknown Error.");
                            break;
                    }
                    if (this.temporaryBufferOffset >= 4) {
                        Log.i("AudioTrack", "Failed to write " + bytesToWrite);
                    }
                    if (this.temporaryBufferSize < 100) {
                        this.temporaryBufferOffset = 0;
                        this.temporaryBufferSize = 0;
                        return 4;
                    }
                }
            }
        } else {
            bytesWritten = writeNonBlockingV21(this.audioTrack, buffer, this.temporaryBufferSize);
        }
        if (bytesWritten < 0) {
            throw new WriteException(bytesWritten);
        }
        this.temporaryBufferSize -= bytesWritten;
        this.submittedBytes += bytesWritten;
        if (this.temporaryBufferSize == 0) {
            result |= 2;
        }
        if (bytesWritten == 0 && this.temporaryBufferSize != 0 && hasNoPendingData()) {
            this.temporaryBufferOffset = 0;
            this.temporaryBufferSize = 0;
            return 4;
        }
        return result;
    }

    public long getTemporaryBufferSize() {
        return this.temporaryBufferSize;
    }

    public boolean hasNoPendingData() {
        return !isInitialized() || bytesToFrames(this.submittedBytes) <= this.audioTrackUtil.getPlaybackHeadPosition();
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (!isInitialized()) {
            return;
        }
        if (Util.SDK_INT >= 21) {
            setVolumeV21(this.audioTrack, volume);
        } else {
            setVolumeV3(this.audioTrack, volume);
        }
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [co.vine.android.embed.player.AudioTrack$1] */
    public void reset() {
        if (isInitialized()) {
            this.submittedBytes = 0L;
            this.temporaryBufferSize = 0;
            this.startMediaTimeUs = -1L;
            resetSyncParams();
            final android.media.AudioTrack toRelease = this.audioTrack;
            this.audioTrack = null;
            this.audioTrackUtil = null;
            this.releasingConditionVariable.close();
            new Thread() { // from class: co.vine.android.embed.player.AudioTrack.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        toRelease.release();
                    } finally {
                        AudioTrack.this.releasingConditionVariable.open();
                    }
                }
            }.start();
        }
    }

    public void updateLatency() {
        if (this.getLatencyMethod != null && this.audioTrack != null) {
            try {
                this.latencyUs = (((Integer) this.getLatencyMethod.invoke(this.audioTrack, (Object[]) null)).intValue() * 1000) - framesToDurationUs(bytesToFrames(this.bufferSize));
                this.latencyUs = Math.max(this.latencyUs, 0L);
                if (this.latencyUs > 5000000) {
                    Log.w("AudioTrack", "Ignoring impossibly large audio latency: " + this.latencyUs);
                    this.latencyUs = 0L;
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.getLatencyMethod = null;
            }
        }
    }

    private void checkAudioTrackInitialized() throws InitializationException {
        int state = this.audioTrack.getState();
        if (state == 1) {
            return;
        }
        try {
            this.audioTrack.release();
        } catch (Exception e) {
        } finally {
            this.audioTrack = null;
        }
        throw new InitializationException(state, this.sampleRate, this.channelConfig, this.bufferSize);
    }

    private long bytesToFrames(long byteCount) {
        if (this.isAc3) {
            if (this.ac3Bitrate == 0) {
                return 0L;
            }
            return ((8 * byteCount) * this.sampleRate) / (this.ac3Bitrate * 1000);
        }
        return byteCount / this.frameSize;
    }

    private long framesToDurationUs(long frameCount) {
        return (1000000 * frameCount) / this.sampleRate;
    }

    private long durationUsToFrames(long durationUs) {
        return (this.sampleRate * durationUs) / 1000000;
    }

    private void resetSyncParams() {
        this.smoothedPlayheadOffsetUs = 0L;
        this.playheadOffsetCount = 0;
        this.nextPlayheadOffsetIndex = 0;
        this.lastPlayheadSampleTimeUs = 0L;
        this.audioTimestampSet = false;
        this.lastTimestampSampleTimeUs = 0L;
    }

    private static class Util {
        private static final int SDK_INT = Build.VERSION.SDK_INT;
    }

    public static class InitializationException extends Exception {
        public final int audioTrackState;

        public InitializationException(int audioTrackState, int sampleRate, int channelConfig, int bufferSize) {
            super("AudioTrack init failed: " + audioTrackState + ", Config(" + sampleRate + ", " + channelConfig + ", " + bufferSize + ")");
            this.audioTrackState = audioTrackState;
        }
    }

    public static class WriteException extends Exception {
        public final int errorCode;

        public WriteException(int errorCode) {
            super("AudioTrack write failed: " + errorCode);
            this.errorCode = errorCode;
        }
    }

    private static class AudioTrackUtil {
        final android.media.AudioTrack audioTrack;
        private long lastRawPlaybackHeadPosition;
        private long rawPlaybackHeadWrapCount;
        private final int sampleRate;

        public AudioTrackUtil(android.media.AudioTrack audioTrack) {
            this.audioTrack = audioTrack;
            this.sampleRate = audioTrack.getSampleRate();
        }

        public long getPlaybackHeadPosition() {
            long rawPlaybackHeadPosition = 4294967295L & this.audioTrack.getPlaybackHeadPosition();
            if (this.lastRawPlaybackHeadPosition > rawPlaybackHeadPosition) {
                this.rawPlaybackHeadWrapCount++;
            }
            this.lastRawPlaybackHeadPosition = rawPlaybackHeadPosition;
            return (this.rawPlaybackHeadWrapCount << 32) + rawPlaybackHeadPosition;
        }
    }

    @TargetApi(19)
    private static class AudioTrackUtilV19 extends AudioTrackUtil {
        private final AudioTimestamp audioTimestamp;

        public AudioTrackUtilV19(android.media.AudioTrack audioTrack) {
            super(audioTrack);
            this.audioTimestamp = new AudioTimestamp();
        }
    }
}
