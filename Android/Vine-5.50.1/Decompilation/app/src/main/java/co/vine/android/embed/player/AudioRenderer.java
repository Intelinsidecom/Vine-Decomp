package co.vine.android.embed.player;

import android.util.Log;
import co.vine.android.embed.player.AudioTrack;
import co.vine.android.util.AudioUtils;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
final class AudioRenderer {
    private int mAudioSessionId;
    private AudioTrack mAudioTrack;
    private int mBufferedSampleIndex;
    private int mCurrentBatchCount;
    private boolean mEndReached;
    private long mLastKnownIndex;
    private long mLastKnownUs;
    private boolean mNewBatch;
    private long mOneIndexToUs;
    private double mOneUsToIndex;
    private VineSampleSource mSource;
    private float mGain = 1.0f;
    private boolean mUpdateTimestamp = true;

    AudioRenderer() {
    }

    public boolean doSomeWork(MediaClock clock) throws IllegalStateException, AudioTrack.InitializationException, AudioTrack.WriteException {
        if (this.mSource == null) {
            return false;
        }
        ByteBuffer preparedSamples = this.mSource.getPreparedAudioSamples();
        ByteBuffer sampleBuffer = preparedSamples != null ? preparedSamples : this.mSource.getUnpreparedAudioSamples();
        if (sampleBuffer == null || sampleBuffer.limit() == 0) {
            return false;
        }
        int sampleSize = sampleBuffer.limit();
        if (this.mNewBatch) {
            if (clock.isFirstOperation()) {
                this.mCurrentBatchCount = (int) (100000.0d * this.mOneUsToIndex);
            } else {
                this.mCurrentBatchCount = (int) (Math.max(60000L, clock.timeSinceLastOperationStartUs() * 2) * this.mOneIndexToUs);
            }
            if (this.mCurrentBatchCount <= 0) {
                return false;
            }
            this.mNewBatch = false;
            if (preparedSamples == null && sampleSize < this.mCurrentBatchCount) {
                return false;
            }
        }
        boolean justReachedEnd = false;
        this.mAudioTrack.updateLatency();
        clock.setAudioLatency(this.mAudioTrack.getLatencyUs() + AudioUtils.getArtificialLatency());
        this.mLastKnownUs = this.mLastKnownIndex * this.mOneIndexToUs;
        clock.setPosition(this.mLastKnownUs);
        if (this.mUpdateTimestamp) {
            this.mUpdateTimestamp = false;
            if (this.mBufferedSampleIndex == 0) {
                Log.i("AudioRenderer", "Clock just reset to 0");
                clock.updateAudioResetTime();
            }
        }
        int ret = processOutputBuffer(sampleBuffer, this.mBufferedSampleIndex, this.mCurrentBatchCount, clock.getAudioPresentationTimeUs());
        if (ret != 0) {
            this.mCurrentBatchCount = this.mAudioTrack.getLastSize();
            clock.incrementAudioPresentationTimeUs((long) (this.mOneUsToIndex * this.mCurrentBatchCount));
            if (this.mEndReached) {
                this.mBufferedSampleIndex = 0;
                this.mNewBatch = true;
                this.mEndReached = false;
                justReachedEnd = true;
            } else {
                this.mBufferedSampleIndex += this.mCurrentBatchCount;
                if (this.mBufferedSampleIndex + this.mCurrentBatchCount > sampleSize) {
                    this.mEndReached = true;
                    this.mCurrentBatchCount = sampleSize - this.mBufferedSampleIndex;
                } else {
                    this.mEndReached = false;
                    this.mNewBatch = true;
                }
            }
            this.mUpdateTimestamp = true;
            return justReachedEnd;
        }
        this.mCurrentBatchCount = this.mAudioTrack.getLastSize();
        this.mLastKnownIndex = this.mBufferedSampleIndex + (this.mCurrentBatchCount - this.mAudioTrack.getTemporaryBufferSize());
        return false;
    }

    public void release() {
        this.mSource = null;
        if (this.mAudioTrack != null) {
            this.mAudioTrack.reset();
            this.mAudioTrack = null;
        }
        this.mAudioSessionId = 0;
    }

    private int processOutputBuffer(ByteBuffer buffer, int offset, int size, long presentationTimeUs) throws IllegalStateException, AudioTrack.InitializationException, AudioTrack.WriteException {
        if (!this.mAudioTrack.isInitialized()) {
            if (this.mAudioSessionId != 0) {
                this.mAudioTrack.initialize(this.mAudioSessionId);
            } else {
                this.mAudioSessionId = this.mAudioTrack.initialize();
            }
            this.mAudioTrack.play();
        }
        int handleBufferResult = this.mAudioTrack.handleBuffer(buffer, offset, size, presentationTimeUs);
        if ((handleBufferResult & 1) != 0) {
            Log.w("AudioRenderer", "We are out of sync.");
        }
        if ((handleBufferResult & 2) != 0) {
            return 2;
        }
        if ((handleBufferResult & 4) != 0) {
            return 4;
        }
        return 0;
    }

    public void prepare(VineSampleSource source) {
        AudioSampleTrack track = source.getAudioTrack();
        if (track != null) {
            this.mAudioTrack = new AudioTrack();
            this.mAudioTrack.reconfigure(track.format);
            this.mAudioTrack.setVolume(this.mGain);
            this.mOneUsToIndex = track.decodedTimeUsToIndex(1L, 2);
            this.mOneIndexToUs = track.decodedIndexToTimeUs(1, 2);
            this.mSource = source;
            this.mBufferedSampleIndex = 0;
            this.mAudioSessionId = 0;
            this.mNewBatch = true;
        }
    }

    public void setVolume(float gain) {
        this.mGain = gain;
        if (this.mAudioTrack != null) {
            this.mAudioTrack.setVolume(gain);
        }
    }

    public long getDurationMs() {
        if (this.mSource == null || this.mSource.getPreparedAudioSamples() == null) {
            return -1L;
        }
        return (this.mSource.getPreparedAudioSamples().capacity() * this.mOneIndexToUs) / 1000;
    }
}
