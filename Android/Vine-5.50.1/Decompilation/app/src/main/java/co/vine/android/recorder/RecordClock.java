package co.vine.android.recorder;

import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class RecordClock {
    private volatile long mAudioTimeRecorded;
    private volatile int mAudioTimestampUs;
    private long mFrameTimeUs;
    private long mLastAudioTimestamp;
    private long mStartTime;
    private volatile long mVideoTimestampUs;

    public long getVideoTimestampUs() {
        return this.mVideoTimestampUs;
    }

    public int getAudioTimestampUs() {
        return this.mAudioTimestampUs;
    }

    public boolean isVideoAhead() {
        return getVideoTimestampUs() > ((long) getAudioTimestampUs());
    }

    public void setVideoTimestampUs(long timestampUs) {
        this.mVideoTimestampUs = timestampUs;
    }

    public void printState() {
        SLog.i("Current timestamps: {}, {}.", Long.valueOf(this.mVideoTimestampUs), Integer.valueOf(this.mAudioTimestampUs));
    }

    public void setAudioTimestampUs(int timestampUs) {
        this.mAudioTimestampUs = timestampUs;
    }

    public void resetStartTime() {
        this.mStartTime = 0L;
    }

    public void setFrameTimeUs(long frameTimeUs) {
        this.mFrameTimeUs = frameTimeUs;
    }

    public long getFrameTimeUs() {
        return this.mFrameTimeUs;
    }

    public void startSegment() {
        this.mStartTime = System.currentTimeMillis();
    }

    public void onFrameIncrement() {
        this.mVideoTimestampUs += this.mFrameTimeUs;
    }

    public boolean wasAudioJustRecorded() {
        return System.nanoTime() - this.mAudioTimeRecorded < ((long) RecordConfigUtils.AUDIO_WAIT_THRESHOLD_NS);
    }

    public void updateAudioTimestamp(boolean resetAudioRecordTime, int newStamp) {
        if (this.mAudioTimestampUs != newStamp) {
            this.mAudioTimestampUs = newStamp;
            if (resetAudioRecordTime) {
                this.mAudioTimeRecorded = -1L;
            } else {
                this.mAudioTimeRecorded = System.nanoTime();
            }
        }
    }

    public long onNewVideoFrame() {
        long timestamp;
        int audioTimestamp = getAudioTimestampUs();
        if (audioTimestamp == 0) {
            if (this.mStartTime <= 0) {
                return 0L;
            }
            long timestamp2 = (System.currentTimeMillis() - this.mStartTime) * 1000;
            return timestamp2;
        }
        if (this.mLastAudioTimestamp == audioTimestamp) {
            long timestamp3 = audioTimestamp + this.mFrameTimeUs;
            return timestamp3;
        }
        if (this.mAudioTimeRecorded > 0) {
            long offset = (System.nanoTime() - this.mAudioTimeRecorded) / 1000;
            timestamp = offset + audioTimestamp;
            SLog.d("Offset {}.", Long.valueOf(offset));
        } else {
            timestamp = audioTimestamp;
        }
        this.mLastAudioTimestamp = audioTimestamp;
        return timestamp;
    }
}
