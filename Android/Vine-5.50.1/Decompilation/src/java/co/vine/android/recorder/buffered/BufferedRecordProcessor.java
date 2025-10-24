package co.vine.android.recorder.buffered;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Looper;
import android.support.v8.renderscript.RenderScript;
import co.vine.android.VineLoggingException;
import co.vine.android.VineNotSupportedException;
import co.vine.android.plugin.BaseRecorderPluginManager;
import co.vine.android.recorder.AbstractCombiningRunnable;
import co.vine.android.recorder.AbstractEncodingRunnable;
import co.vine.android.recorder.AudioDataReceiver;
import co.vine.android.recorder.BaseFinishProcessTask;
import co.vine.android.recorder.BaseSurfaceListener;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.ByteBufferQueue;
import co.vine.android.recorder.DeviceIssue;
import co.vine.android.recorder.ParentHolder;
import co.vine.android.recorder.PictureConverter;
import co.vine.android.recorder.RecordClock;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordController;
import co.vine.android.recorder.RecordProcessor;
import co.vine.android.recorder.RecordSegment;
import co.vine.android.recorder.RecordSession;
import co.vine.android.recorder.RecordState;
import co.vine.android.recorder.RecordingFile;
import co.vine.android.recorder.SurfaceController;
import co.vine.android.recorder.SwCombiningRunnable;
import co.vine.android.recorder.SwEncodingRunnable;
import co.vine.android.recorder.SwVineFrameRecorder;
import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.recorder.audio.AudioArrays;
import co.vine.android.recorder.audio.AudioReceiver;
import co.vine.android.recorder.buffered.BufferedCameraPreviewReceiver;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.recorder.camera.CameraSetting;
import co.vine.android.recorder.camera.PreviewManagerCallback;
import co.vine.android.recorder.video.VideoData;
import co.vine.android.service.ResourceService;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.MediaUtil;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.opencv_core;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/* loaded from: classes.dex */
public class BufferedRecordProcessor implements RecordProcessor {
    private static WeakReference<ByteBufferQueue> sByteBufferQueue;
    private static ByteBufferQueue sByteBufferQueueReal;
    private AudioArray<?, ?> mAudioDataBufferMax;
    private AudioDataReceiver mAudioReceiver;
    private final RecordClock mClock;
    private final Context mContext;
    private AbstractEncodingRunnable mEncodingRunnable;
    private boolean mEncodingThreadWasRunning;
    private BaseFinishProcessTask mFinishProcessTask;
    private final BufferedCameraPreviewReceiver mImageReceiver;
    private String mOutput;
    private final ParentHolder mParentHolder;
    private final BaseRecorderPluginManager mPluginManager;
    private Thread mProcessThread;
    private final RecordState mState;
    private final boolean mUseMp4;
    private byte[] mVideoDataBufferMax;
    private int mVideoDataStartCount;
    private final ConcurrentLinkedQueue<VideoData> mDataQueue = new ConcurrentLinkedQueue<>();
    private final BufferedPreviewManagerCallback mPreviewManagerCallback = BufferedPreviewManagerCallback.getInstance();

    public BufferedRecordProcessor(boolean useMp4, RecordState state, RecordClock clock, Context applicationContext, BaseRecorderPluginManager pluginManager, ParentHolder parentHolder) {
        this.mUseMp4 = useMp4;
        this.mState = state;
        this.mClock = clock;
        this.mParentHolder = parentHolder;
        this.mContext = applicationContext;
        this.mPluginManager = pluginManager;
        this.mImageReceiver = new BufferedCameraPreviewReceiver(this.mState, this.mClock, this.mDataQueue, this.mPluginManager, this.mPreviewManagerCallback);
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public boolean start(BasicVineRecorder parent, String output, Activity activity, CameraSetting cs, RecordProcessor.ProcessingErrorHandler errorHandler) {
        this.mImageReceiver.clearLastFrames();
        long start = System.currentTimeMillis();
        this.mOutput = output + ".video" + RecordConfigUtils.VIDEO_CONTAINER_EXT;
        RecordConfigUtils.RecordConfig config = parent.getConfig();
        if (config == null) {
            return false;
        }
        int frameRate = cs.fps;
        Bitmap matrixBitmap = getPreviewBitmap();
        Bitmap thumbnailBitmap = ResourceService.getThumbnailBitmap(activity);
        RenderScript rs = ResourceService.getRenderScript(activity);
        Matrix thumbnailMatrix = ResourceService.getThumbnailMatrix(activity);
        Canvas thumbnailCanvas = ResourceService.getThumbnailCanvas(activity);
        Canvas previewCanvas = ResourceService.getPreviewCanvas();
        SLog.d("start 4 took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
        long start2 = System.currentTimeMillis();
        PictureConverter pictureConverter = ResourceService.getPictureConverter();
        if (pictureConverter != null) {
            pictureConverter.updateSettingsIfNeeded(cs);
        } else {
            try {
                pictureConverter = new PictureConverter(rs, 480, cs);
                ResourceService.setPictureConverter(pictureConverter);
            } catch (VineNotSupportedException e) {
                parent.onDeviceIssue(new VineLoggingException("Device does not support Rs"), DeviceIssue.RS_NOT_SUPPORTED);
                return false;
            }
        }
        SLog.d("start 5 took {}ms.", Long.valueOf(System.currentTimeMillis() - start2));
        long start3 = System.currentTimeMillis();
        if (SLog.sLogsOn) {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) parent.getActivity().getSystemService("activity");
            activityManager.getMemoryInfo(mi);
            SLog.d("[mem] Avaliable bytes: {}.", Long.valueOf(mi.availMem));
        }
        CameraManager cameraManager = CameraManager.getInstance();
        boolean useRealQueue = parent.getConfig().memRatio == 1.0d;
        int frameSize = this.mPreviewManagerCallback.getFrameSize();
        ByteBufferQueue queue = null;
        if (sByteBufferQueue == null && sByteBufferQueueReal == null) {
            queue = new ByteBufferQueue(parent.getConfig().bufferCount, frameSize, parent.getMemoryResponder());
            if (useRealQueue) {
                sByteBufferQueueReal = queue;
            } else {
                sByteBufferQueue = new WeakReference<>(queue);
            }
        } else {
            if (useRealQueue) {
                queue = sByteBufferQueueReal;
            } else if (sByteBufferQueue != null) {
                ByteBufferQueue queue2 = sByteBufferQueue.get();
                queue = queue2;
            }
            if (queue == null) {
                queue = new ByteBufferQueue(parent.getConfig().bufferCount, frameSize, parent.getMemoryResponder());
                sByteBufferQueue = new WeakReference<>(queue);
            } else {
                queue.reset(parent.getConfig().bufferCount, frameSize);
            }
        }
        SLog.d("start 6 took {}ms.", Long.valueOf(System.currentTimeMillis() - start3));
        long start4 = System.currentTimeMillis();
        if (!cameraManager.isCameraReady()) {
            CrashUtil.logException(new VineLoggingException("Camera was released already. "));
            return false;
        }
        this.mEncodingRunnable = new SwEncodingRunnable(this.mDataQueue, this.mVideoDataBufferMax, this.mState, output, frameRate, getFrameBuffer(), matrixBitmap, thumbnailBitmap, pictureConverter, this.mVideoDataStartCount, this.mPreviewManagerCallback, errorHandler, queue, thumbnailMatrix, thumbnailCanvas, previewCanvas, this.mUseMp4);
        Iterator it = this.mPluginManager.getEnabledPlugins().iterator();
        while (it.hasNext()) {
            Object plugin = it.next();
            if (plugin instanceof AbstractEncodingRunnable.Drawer) {
                this.mEncodingRunnable.addAdditionalDrawer((AbstractEncodingRunnable.Drawer) plugin);
            }
        }
        this.mProcessThread = new Thread(this.mEncodingRunnable, "EncodingRunnable");
        this.mProcessThread.start();
        SLog.d("start 7 took {}ms.", Long.valueOf(System.currentTimeMillis() - start4));
        return true;
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public MediaUtil.GenerateThumbnailsRunnable stop() throws InterruptedException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.mEncodingThreadWasRunning = true;
        } else {
            BasicVineRecorder parent = this.mParentHolder.getParent();
            if (!parent.canKeepRecording()) {
                this.mEncodingThreadWasRunning = true;
            }
            finishLastIfNeeded();
            if (!parent.canKeepRecording()) {
                System.gc();
                RecordingFile file = parent.getFile();
                RecordSession session = file.getSession();
                return writeToFile(file, session.getSegments(), session.getVideoData(), session.getAudioData(), false);
            }
        }
        return null;
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void onNewSegmentStart() {
        this.mImageReceiver.onNewSegmentStart();
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void onEndRelativeTime(RecordSegment segmentToEnd) {
        this.mImageReceiver.offerLastFrame(segmentToEnd, null);
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public SurfaceController.SurfaceListener getSurfaceListener(RecordController recordController) {
        return new BaseSurfaceListener(this.mParentHolder, recordController, this.mState);
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public AudioReceiver prepareAudioReceiver(RecordSession session) {
        int currentAudioSampleCount = session.calculateMemoryBackedAudioCount();
        AudioArrays.AudioArrayType type = AudioArrays.AudioArrayType.SHORT;
        SLog.i("Audio recorder initialized with count {}.", Integer.valueOf(currentAudioSampleCount));
        this.mAudioReceiver = new AudioDataReceiver(this.mClock, this.mState, this.mAudioDataBufferMax, currentAudioSampleCount, session.getDurationMs(), type);
        this.mAudioReceiver.setValidator(new AudioDataReceiver.AudioLengthValidator() { // from class: co.vine.android.recorder.buffered.BufferedRecordProcessor.1
            @Override // co.vine.android.recorder.AudioDataReceiver.AudioLengthValidator
            public boolean isAudioLengthResonable(long audioTimestampUs) {
                RecordConfigUtils.RecordConfig config;
                BasicVineRecorder parent = BufferedRecordProcessor.this.mParentHolder.getParent();
                return (parent == null || (config = parent.getConfig()) == null || audioTimestampUs >= ((long) (config.maxDuration * 1000))) ? false : true;
            }
        });
        this.mClock.printState();
        return this.mAudioReceiver;
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void prepareImageReceiver(RecordSession session) {
        this.mVideoDataStartCount = session.getVideoDataCount();
        this.mImageReceiver.setValidator(new BufferedCameraPreviewReceiver.PreviewStatusValidator() { // from class: co.vine.android.recorder.buffered.BufferedRecordProcessor.2
            @Override // co.vine.android.recorder.buffered.BufferedCameraPreviewReceiver.PreviewStatusValidator
            public boolean validateOnFrameReceived(boolean emptyDataReceived) {
                BasicVineRecorder parent = BufferedRecordProcessor.this.mParentHolder.getParent();
                if (!BufferedRecordProcessor.this.mState.recordingAudio() && parent != null) {
                    parent.receivedFirstFrameAfterStartingPreview();
                }
                if (!emptyDataReceived || parent == null) {
                    return true;
                }
                parent.onDeviceIssue(new VineLoggingException("Camera does not return proper frames."), DeviceIssue.CAMERA_NULL_FRAMES);
                return false;
            }
        });
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void setAudioTrim(boolean enabled) {
        if (this.mAudioReceiver != null) {
            this.mAudioReceiver.setAudioTrim(enabled);
        }
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public Camera.PreviewCallback getCameraPreviewCallback() {
        return this.mImageReceiver;
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void onSessionTimestampChanged(RecordSession session) {
        if (this.mAudioReceiver != null) {
            this.mAudioReceiver.onSessionTimestampChanged(session);
            if (this.mOutput != null) {
                this.mClock.updateAudioTimestamp(true, this.mAudioReceiver.getCurrentTimestampUs());
            }
        }
        this.mImageReceiver.updateLastFrameTimestampUs();
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public PreviewManagerCallback getPreviewManagerCallback() {
        return this.mPreviewManagerCallback;
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void swapSession(RecordSession session) {
        this.mVideoDataBufferMax = session.getVideoData();
        this.mAudioDataBufferMax = session.getAudioData();
        this.mOutput = null;
        this.mImageReceiver.onSessionSwapped();
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void makePreview(RecordingFile file, RecordSegment segment, boolean getLastSegmentOnlyMode, boolean ignoreTrim) {
        if (!getLastSegmentOnlyMode) {
            try {
                if (segment.videoPath != null && new File(segment.videoPath).exists()) {
                    return;
                }
            } catch (Exception e) {
                CrashUtil.logException(e, "Cannot make previews", new Object[0]);
                return;
            }
        }
        String output = file.getPreviewVideoPath() + ".temp_video" + RecordConfigUtils.VIDEO_CONTAINER_EXT;
        if (segment.removed) {
            SLog.i("Do not make preview as this is already removed.");
            return;
        }
        CameraSetting setting = segment.getCameraSetting();
        SwVineFrameRecorder videoRecorder = RecordConfigUtils.newVideoRecorder(output, setting == null ? 30 : setting.fps, 480, this.mUseMp4);
        videoRecorder.start();
        AbstractCombiningRunnable combiningRunnable = SwCombiningRunnable.newSinglePreview(file, segment, videoRecorder, this.mFinishProcessTask, getLastSegmentOnlyMode, ignoreTrim);
        if (segment.removed) {
            SLog.i("Do not make preview as this is already removed.");
            return;
        }
        MediaUtil.GenerateThumbnailsRunnable r = combiningRunnable.combineVideos();
        if (r != null) {
            r.run();
        }
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public MediaUtil.GenerateThumbnailsRunnable writeToFile(RecordingFile file, ArrayList<RecordSegment> editedSegments, byte[] videoData, AudioArray audioData, boolean preview) {
        SwVineFrameRecorder videoRecorder;
        try {
            int frameRate = RecordSegment.getFrameRate(editedSegments);
            if (preview) {
                videoRecorder = RecordConfigUtils.newVideoRecorder(file.getPreviewVideoPath() + ".temp_video" + RecordConfigUtils.VIDEO_CONTAINER_EXT, frameRate, 480, this.mUseMp4);
            } else {
                videoRecorder = RecordConfigUtils.newVideoRecorder(this.mOutput, frameRate, 480, this.mUseMp4);
            }
            videoRecorder.start();
            AbstractCombiningRunnable combiningRunnable = SwCombiningRunnable.newInstance(file, preview, audioData, videoData, editedSegments, videoRecorder, this.mFinishProcessTask);
            return combiningRunnable.combineVideos();
        } catch (Exception e) {
            CrashUtil.logException(e, "failed to write to file. ", new Object[0]);
            return null;
        }
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void onExternalClipAdded(int microseconds) {
        if (this.mAudioReceiver != null) {
            this.mAudioReceiver.onExternalClipAdded(microseconds);
        }
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void stopProcessing() {
        if (this.mEncodingRunnable != null) {
            this.mEncodingRunnable.terminate();
        }
        if (this.mProcessThread != null) {
            this.mProcessThread.interrupt();
        }
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void finishLastIfNeeded() throws InterruptedException {
        try {
            if (this.mProcessThread != null) {
                this.mProcessThread.join();
            }
        } catch (InterruptedException e) {
        }
        this.mEncodingThreadWasRunning = false;
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public boolean wasProcessing() {
        return this.mEncodingThreadWasRunning;
    }

    private Bitmap getPreviewBitmap() {
        return ResourceService.getPreviewBitmap();
    }

    private opencv_core.IplImage getFrameBuffer() {
        return ResourceService.getFrameImage();
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void releaseResources() {
        this.mDataQueue.clear();
        this.mEncodingRunnable = null;
        System.gc();
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public BaseFinishProcessTask getFinishProcessTask() {
        return this.mFinishProcessTask;
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void setFinishProcessTask(BaseFinishProcessTask finishProcessTask) {
        this.mFinishProcessTask = finishProcessTask;
        if (this.mEncodingRunnable != null) {
            this.mEncodingRunnable.setAsyncTask(this.mFinishProcessTask);
        }
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void onStopped(boolean stopPreview) {
        if (this.mFinishProcessTask != null) {
            this.mFinishProcessTask.publish(100);
        }
        if (!stopPreview) {
            this.mImageReceiver.clearLastFrames();
        }
    }

    @Override // co.vine.android.recorder.RecordProcessor
    public void setVideoTimeStampUs(long timestamp) {
        this.mImageReceiver.updateVideoTimestampUs(timestamp);
    }
}
