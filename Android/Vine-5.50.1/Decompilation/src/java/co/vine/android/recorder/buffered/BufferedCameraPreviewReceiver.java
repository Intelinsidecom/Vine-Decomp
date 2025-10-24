package co.vine.android.recorder.buffered;

import android.hardware.Camera;
import co.vine.android.plugin.BaseRecorderPluginManager;
import co.vine.android.recorder.RecordClock;
import co.vine.android.recorder.RecordSegment;
import co.vine.android.recorder.RecordState;
import co.vine.android.recorder.video.VideoData;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/* loaded from: classes.dex */
public class BufferedCameraPreviewReceiver implements Camera.PreviewCallback {
    private final RecordClock mClock;
    private boolean mCurrentSegmentEverReceivedFrames;
    private final ConcurrentLinkedQueue<VideoData> mDataQueue;
    private VideoData mLastFrame;
    private VideoData mLastLastFrame;
    private final BaseRecorderPluginManager mPluginManager;
    private final BufferedPreviewManagerCallback mPreviewManagerCallback;
    private final RecordState mState;
    private PreviewStatusValidator mValidator;
    private final int[] mVideoRecordLock = new int[0];

    public interface PreviewStatusValidator {
        boolean validateOnFrameReceived(boolean z);
    }

    public void setValidator(PreviewStatusValidator previewStatusValidator) {
        this.mValidator = previewStatusValidator;
    }

    public BufferedCameraPreviewReceiver(RecordState state, RecordClock clock, ConcurrentLinkedQueue<VideoData> dataQueue, BaseRecorderPluginManager pluginManager, BufferedPreviewManagerCallback previewManagerCallback) {
        this.mState = state;
        this.mClock = clock;
        this.mDataQueue = dataQueue;
        this.mPluginManager = pluginManager;
        this.mPreviewManagerCallback = previewManagerCallback;
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] data, Camera camera) {
        this.mCurrentSegmentEverReceivedFrames = true;
        if (this.mValidator != null) {
            if (!this.mValidator.validateOnFrameReceived(data == null)) {
                return;
            }
        }
        long timestamp = this.mClock.onNewVideoFrame();
        this.mPreviewManagerCallback.removeBufferFromAvailableQueue(data);
        synchronized (this.mVideoRecordLock) {
            RecordSegment segment = this.mState.getCurrentSegment();
            if (segment != null) {
                SLog.d("new video timestamp: {}", Long.valueOf(timestamp));
                if (this.mLastFrame == null) {
                    this.mLastFrame = new VideoData(timestamp, data);
                    offerLastFrame(segment, null);
                } else {
                    offerLastFrame(segment, new VideoData(timestamp, data));
                }
            } else {
                if (this.mLastLastFrame != null) {
                    this.mPreviewManagerCallback.addCallbackBuffer(this.mLastLastFrame.data);
                }
                this.mLastLastFrame = this.mLastFrame;
                this.mLastFrame = new VideoData(timestamp, data);
            }
        }
    }

    public void offerLastFrame(RecordSegment segment, VideoData next) {
        synchronized (this.mVideoRecordLock) {
            if (this.mLastFrame != null) {
                this.mClock.onFrameIncrement();
                if (this.mLastFrame.timestamp > this.mClock.getVideoTimestampUs()) {
                    this.mClock.setVideoTimestampUs(this.mLastFrame.timestamp);
                }
                this.mLastFrame.setSegment(segment);
                segment.getVideoData().add(this.mLastFrame);
                this.mDataQueue.offer(this.mLastFrame);
                ByteBuffer bb = this.mPreviewManagerCallback.getSharedByteBuffer();
                bb.rewind();
                bb.put(this.mLastFrame.data);
                this.mPluginManager.onOfferLastFrame(bb.array(), segment.getCameraSetting());
                this.mState.setLastVideoSegment(segment);
                SLog.d("Video timestamp {}, actual {}, segment: {}", Long.valueOf(this.mClock.getVideoTimestampUs()), Long.valueOf(this.mLastFrame.timestamp), segment);
                this.mLastFrame = next;
                if (next == null) {
                    CrashUtil.log("Flushed last frame to " + segment);
                }
            } else {
                CrashUtil.log("Offer last frame failed, did we ever got a frame? " + this.mCurrentSegmentEverReceivedFrames + ", " + this + " - Callback state: " + BufferedPreviewManagerCallback.getCallbackStateString());
            }
        }
    }

    public void clearLastFrames() {
        synchronized (this.mVideoRecordLock) {
            if (this.mLastFrame != null && !this.mPreviewManagerCallback.isFrameAdded(this.mLastFrame.data)) {
                SLog.d("Cleared Last frame.");
                this.mPreviewManagerCallback.addBuffer(this.mLastFrame.data, false);
                this.mLastFrame = null;
            }
            if (this.mLastLastFrame != null && !this.mPreviewManagerCallback.isFrameAdded(this.mLastLastFrame.data)) {
                SLog.d("Cleared Last Last frame.");
                this.mPreviewManagerCallback.addBuffer(this.mLastLastFrame.data, false);
                this.mLastLastFrame = null;
            }
        }
    }

    public void updateVideoTimestampUs(long timestamp) {
        synchronized (this.mVideoRecordLock) {
            clearLastFrames();
            this.mClock.setVideoTimestampUs(timestamp);
        }
    }

    public void onSessionSwapped() {
        this.mLastLastFrame = null;
        this.mLastFrame = null;
    }

    public void updateLastFrameTimestampUs() {
        if (this.mLastFrame != null) {
            this.mClock.onFrameIncrement();
            this.mLastFrame.timestamp = this.mClock.getVideoTimestampUs();
        }
    }

    public void onNewSegmentStart() {
        this.mCurrentSegmentEverReceivedFrames = false;
    }
}
