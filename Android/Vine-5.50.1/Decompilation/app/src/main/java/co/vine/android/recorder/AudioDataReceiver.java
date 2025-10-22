package co.vine.android.recorder;

import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.recorder.audio.AudioArrays;
import co.vine.android.recorder.audio.AudioData;
import co.vine.android.recorder.audio.AudioReceiver;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class AudioDataReceiver implements AudioReceiver {
    private AudioArray<?, ?> mAudioDataBufferMax;
    private final RecordClock mClock;
    private int mCount;
    private int mInitialDurationUs;
    private final RecordState mState;
    private final AudioArrays.AudioArrayType mType;
    private AudioLengthValidator mValidator;
    private final double AUDIO_SAMPLE_PER_US_EXACT = 0.0441d;
    private boolean mAudioTrim = false;

    public interface AudioLengthValidator {
        boolean isAudioLengthResonable(long j);
    }

    public AudioDataReceiver(RecordClock clock, RecordState state, AudioArray<?, ?> audioDataMax, int initialCount, int durationMs, AudioArrays.AudioArrayType type) {
        this.mCount = initialCount;
        this.mState = state;
        this.mType = type;
        this.mAudioDataBufferMax = audioDataMax;
        this.mInitialDurationUs = (durationMs * 1000) - ((int) (this.mCount / 0.0441d));
        this.mClock = clock;
    }

    public void onSessionTimestampChanged(RecordSession session) {
        this.mCount = session.calculateMemoryBackedAudioCount();
        this.mInitialDurationUs = (session.getDurationMs() * 1000) - ((int) (this.mCount / 0.0441d));
    }

    public void onExternalClipAdded(int microseconds) {
        this.mInitialDurationUs += microseconds;
    }

    @Override // co.vine.android.recorder.audio.AudioReceiver
    public boolean needsMoreData() {
        return (this.mState.collectMoreAudio() || this.mClock.isVideoAhead()) && !this.mState.isEnded();
    }

    @Override // co.vine.android.recorder.audio.AudioReceiver
    public void beforeDataCollection() {
        this.mClock.updateAudioTimestamp(false, getCurrentTimestampUs());
    }

    @Override // co.vine.android.recorder.audio.AudioReceiver
    public boolean onAudioDataReceived(int bufferReadResult, AudioArray audioDataBuffer) {
        int shouldPut = !this.mAudioTrim ? bufferReadResult : (int) Math.min(((this.mClock.getVideoTimestampUs() - this.mClock.getAudioTimestampUs()) * 0.0441d) + 1.0d, bufferReadResult);
        if (shouldPut > 0) {
            RecordSegment segment = this.mState.getCurrentSegment();
            if (segment != null && this.mState.recordingAudio()) {
                record(segment, audioDataBuffer, shouldPut);
                this.mState.setLastAudioRecordingSegment(segment);
            } else {
                RecordSegment segment2 = this.mState.getLastAudioRecordingSegment();
                if (segment2 == null) {
                    segment2 = this.mState.getLastVideoSegment();
                }
                if (this.mClock.isVideoAhead()) {
                    if (segment2 == null) {
                        SLog.e("Recording have not started yet? {} vs {}.", Long.valueOf(this.mClock.getVideoTimestampUs()), Integer.valueOf(this.mClock.getAudioTimestampUs()));
                    } else {
                        record(segment2, audioDataBuffer, shouldPut);
                    }
                }
            }
        }
        return this.mValidator.isAudioLengthResonable(this.mClock.getAudioTimestampUs());
    }

    @Override // co.vine.android.recorder.audio.AudioReceiver
    public void onAudioSourceStopped() {
        CrashUtil.log("mAudioRecord released on conditions: {} {}", Boolean.valueOf(this.mState.collectMoreAudio()), Boolean.valueOf(this.mState.isEnded()));
    }

    public void setAudioTrim(boolean enabled) {
        this.mAudioTrim = enabled;
    }

    @Override // co.vine.android.recorder.audio.AudioReceiver
    public AudioArrays.AudioArrayType getType() {
        return this.mType;
    }

    public int getCurrentTimestampUs() {
        return this.mInitialDurationUs + ((int) (this.mCount / 0.0441d));
    }

    public void setValidator(AudioLengthValidator audioLengthValidator) {
        this.mValidator = audioLengthValidator;
    }

    protected void record(RecordSegment segment, AudioArray buffer, int limit) {
        buffer.putInto(this.mAudioDataBufferMax, this.mCount, limit);
        segment.addAudioData(new AudioData(this.mCount, limit));
        this.mCount += limit;
    }
}
