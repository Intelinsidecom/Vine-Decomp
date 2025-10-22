package co.vine.android.recorder;

/* loaded from: classes.dex */
public class RecordState {
    private volatile RecordSegment mCurrentSegment;
    private boolean mIsRecordingEnded;
    private RecordSegment mLastAudioRecordingSegment;
    private RecordSegment mLastVideoSegment;
    private volatile boolean mRecordingAudio;
    private volatile boolean mCollectMoreAudio = true;
    private volatile boolean isRecordingStarted = false;

    public boolean collectMoreAudio() {
        return this.mCollectMoreAudio;
    }

    public boolean recordingAudio() {
        return this.mRecordingAudio;
    }

    public boolean isEnded() {
        return this.mIsRecordingEnded;
    }

    public void setRecordingAudio(boolean recordingAudio) {
        this.mRecordingAudio = recordingAudio;
    }

    public RecordSegment getCurrentSegment() {
        return this.mCurrentSegment;
    }

    public void setCurrentSegment(RecordSegment currentSegment) {
        this.mCurrentSegment = currentSegment;
    }

    public RecordSegment getLastVideoSegment() {
        return this.mLastVideoSegment;
    }

    public void setLastVideoSegment(RecordSegment mLastVideoSegment) {
        this.mLastVideoSegment = mLastVideoSegment;
    }

    public void setEnded(boolean isRecordingEnded) {
        this.mIsRecordingEnded = isRecordingEnded;
    }

    public RecordSegment getLastAudioRecordingSegment() {
        return this.mLastAudioRecordingSegment;
    }

    public void setLastAudioRecordingSegment(RecordSegment lastAudioRecordingSegment) {
        this.mLastAudioRecordingSegment = lastAudioRecordingSegment;
    }

    public void setCollectMoreAudio(boolean collectMoreAudio) {
        this.mCollectMoreAudio = collectMoreAudio;
    }

    public boolean isStarted() {
        return this.isRecordingStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isRecordingStarted = isStarted;
    }
}
