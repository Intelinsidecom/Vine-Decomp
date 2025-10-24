package co.vine.android.recorder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.BuildConfig;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import co.vine.android.VineLoggingException;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.plugin.BaseRecorderPluginManager;
import co.vine.android.recorder.ByteBufferQueue;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordController;
import co.vine.android.recorder.buffered.BufferedPreviewManagerCallback;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.recorder.camera.CameraSetting;
import co.vine.android.recorder.camera.PreviewManager;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.ExceptionUtil;
import co.vine.android.util.MediaUtil;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.opencv_imgproc;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public abstract class BasicVineRecorder implements VideoViewInterface.OnErrorListener {
    private static int STRING_WAITING_ON_CAMERA = -1;
    public static long sTimeTaken;
    public volatile RecordingFile finalFile;
    public MediaUtil.GenerateThumbnailsRunnable grabThumbnailsRunnable;
    protected Activity mActivity;
    private boolean mAlreadyStoppingForUnspportedReasons;
    protected View mCameraView;
    protected boolean mCanKeepRecording;
    protected volatile long mCurrentDurationMs;
    protected int mCurrentFrameRate;
    protected RecordingFile mCurrentRecordingFile;
    protected RecordSegment mCurrentSegment;
    protected boolean mDelayDialog;
    public volatile boolean mDiscardChanges;
    protected boolean mDoNotDeleteSession;
    protected final boolean mFastEncoding;
    protected final FinishProcessRunnable mFinishProcessRunnable;
    protected boolean mFinished;
    protected boolean mFrontFacing;
    protected volatile boolean mIsSwitchingCamera;
    protected boolean mNeverResumedRecorder;
    public volatile Runnable mOnCompleteConsumer;
    protected final BaseRecorderPluginManager mPluginManager;
    protected ProgressTimer mProgressTimer;
    protected int mRecordingFileDuration;
    protected OnResumeTask mResumeTask;
    protected RecordSession mSession;
    protected ResumeCameraAsyncTask mSwitchCameraTask;
    protected final int mThresholdMs;
    private WeakReference<Toast> mToast;
    protected final boolean mUseMp4;
    protected RecordController mVideoController;
    protected final ArrayList<RecordSegment> mEditedSegments = new ArrayList<>();
    protected final Handler mHandler = new Handler();
    protected final ChangeProgressRunnable mChangeProgressRunnable = new ChangeProgressRunnable();
    protected final ArrayList<RecordSegment> mAddedSegments = new ArrayList<>();
    protected boolean mAutoFocusing = true;
    protected final HashSet<RecordSegment> mToRemove = new HashSet<>();
    protected boolean mEnabled = true;

    protected abstract void adjustBoundaries(CameraSetting cameraSetting);

    public abstract void changeProgress(long j);

    protected abstract Runnable getOnDeviceIssueAction(DeviceIssue deviceIssue);

    public abstract OnResumeTask getOnResumeTask(View view, String str);

    protected abstract boolean onStop();

    protected abstract void showCameraFailedToast();

    public static void initErrorStrings(int waitingOnCamera) {
        STRING_WAITING_ON_CAMERA = waitingOnCamera;
    }

    public BasicVineRecorder(int thresholdMs, BaseRecorderPluginManager manager, Activity activity, boolean startWithFrontFacingCamera, boolean hardwareEncoding, RecordSessionVersion version) {
        if (STRING_WAITING_ON_CAMERA == -1) {
            throw new IllegalStateException("Error strings not initialized");
        }
        this.mPluginManager = manager;
        this.mPluginManager.setRecorder(this);
        this.mThresholdMs = thresholdMs;
        this.mNeverResumedRecorder = true;
        this.mFastEncoding = RecordConfigUtils.isFastEncoding(hardwareEncoding);
        this.mUseMp4 = version != RecordSessionVersion.SW_WEBM;
        this.mVideoController = new RecordController(this, activity, this.mFastEncoding, this.mUseMp4, manager);
        this.mActivity = activity;
        this.mFrontFacing = startWithFrontFacingCamera || RecordConfigUtils.isDefaultFrontFacing();
        this.mFinishProcessRunnable = getFinishProcessRunnable();
    }

    protected FinishProcessRunnable getFinishProcessRunnable() {
        return new FinishProcessRunnable();
    }

    public void start(final String tag, boolean stopPrevious, boolean adjustBoundaries) {
        long start = System.currentTimeMillis();
        if (!this.mCanKeepRecording) {
            CrashUtil.logException(new IllegalStateException("You cannot start recording again if it is finished."));
            CrashUtil.log("Start failed.");
        } else if (this.mVideoController != null) {
            if (adjustBoundaries) {
                adjustBoundaries(this.mVideoController.getCameraSetting());
            }
            if (this.mVideoController.isRecordingStarted() && stopPrevious) {
                stop("stopPrevious", new Runnable() { // from class: co.vine.android.recorder.BasicVineRecorder.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BasicVineRecorder.this.startRecording(tag);
                    }
                }, true, false);
            } else {
                startRecording(tag);
            }
        }
        CrashUtil.log("Start called from {} took {}ms.", tag, Long.valueOf(System.currentTimeMillis() - start));
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean canKeepRecording() {
        return this.mCanKeepRecording && !this.mDiscardChanges;
    }

    public void onFinishPressed() {
        setHasPreviewedAlreadyIfNeeded();
        finish("onFinishPressed");
    }

    public RecordConfigUtils.RecordConfig getConfig() {
        RecordSession session = this.mSession;
        if (session != null) {
            return session.getConfig();
        }
        return null;
    }

    public boolean isRecordingSegment() {
        return this.mCurrentSegment != null;
    }

    protected synchronized void startRecording(String tag) {
        LinkageError e;
        Activity activity = this.mActivity;
        CrashUtil.log("Start recording called from {}.", tag);
        if (activity != null && this.mVideoController != null) {
            boolean isCameraReady = this.mVideoController.isCameraReady();
            if (!isCameraReady) {
                CrashUtil.log("Start recording.");
                isCameraReady = this.mVideoController.openDefaultCamera(this.mFrontFacing, false);
            }
            if (isCameraReady) {
                if (!this.mVideoController.isPreviewing()) {
                    this.mVideoController.startPreview();
                }
                RecordSession session = this.mCurrentRecordingFile.getSession();
                if (!this.mVideoController.isRecordingStarted()) {
                    session.setAudioDataCount(session.calculateAudioCount());
                    session.setVideoDataCount(session.calculateVideoCount());
                    try {
                        opencv_imgproc.tryLoad();
                        long start = System.currentTimeMillis();
                        this.mVideoController.start(activity, this.mCurrentRecordingFile.getVideoPath(), session);
                        CrashUtil.log("Start recording: {} took {}ms.", this.mCurrentRecordingFile.getVideoPath(), Long.valueOf(System.currentTimeMillis() - start));
                    } catch (RecordController.RecordControllerReadyStartedException e2) {
                        CrashUtil.logException(e2);
                    } catch (ExceptionInInitializerError e3) {
                        e = e3;
                        onDeviceIssue(e, DeviceIssue.LIBS_NOT_COMPATIBLE);
                    } catch (NoClassDefFoundError e4) {
                        e = e4;
                        onDeviceIssue(e, DeviceIssue.LIBS_NOT_COMPATIBLE);
                    } catch (UnsatisfiedLinkError e5) {
                        e = e5;
                        onDeviceIssue(e, DeviceIssue.LIBS_NOT_COMPATIBLE);
                    }
                }
            } else {
                showCameraFailedToast();
            }
            this.mVideoController.waitForPreviewToStart();
            if (!this.mVideoController.isPreviewing()) {
                this.mVideoController.startPreview();
            }
            this.mVideoController.onRecordingStarted();
            SLog.dWithTag("TestLog", "Recorder fully initialized.");
        } else {
            SLog.e("Activity is not in the right state. ");
        }
    }

    protected void finishRecording() {
        if (!this.mCanKeepRecording) {
            setFinalFile(this.mCurrentRecordingFile);
        }
        Runnable onCompleteRunner = getOnCompleteConsumer();
        if (onCompleteRunner != null && !this.mCanKeepRecording) {
            this.mHandler.post(onCompleteRunner);
        }
    }

    public RecordingFile swapSession(String tag, RecordingFile file) {
        if (file != null) {
            CrashUtil.log("Swap new Recorder for {} from {}.", tag, file.folder.getAbsolutePath());
            this.mSession = file.getSession();
            if (SLog.sLogsOn) {
                SLog.i("[session] Swapping file {} for {}.", Integer.valueOf(this.mCurrentRecordingFile == null ? 0 : this.mCurrentRecordingFile.hashCode()), Integer.valueOf(file.hashCode()));
            }
            this.mCurrentRecordingFile = file;
            this.mFinished = false;
            this.mEditedSegments.clear();
            this.mEditedSegments.addAll(this.mSession.getSegments());
            this.mToRemove.clear();
            this.mAddedSegments.clear();
            this.mCurrentRecordingFile.editedSegments = this.mEditedSegments;
            swapTimestampsFromSegments(this.mSession, this.mEditedSegments);
            this.mCanKeepRecording = true;
            setHasPreviewedAlreadyIfNeeded();
            changeProgress(this.mCurrentDurationMs);
            this.mDiscardChanges = false;
            this.finalFile = null;
            this.mVideoController.swapSession();
        }
        return file;
    }

    public boolean hasSessionFile() {
        return this.mCurrentRecordingFile != null;
    }

    public RecordingFile getCurrentRecordingFile() {
        return this.mCurrentRecordingFile;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
    public boolean onError(VideoViewInterface view, int what, int extra) {
        return false;
    }

    public boolean isCurrentlyRecording() {
        return this.mCurrentSegment != null;
    }

    public ByteBufferQueue.MemoryResponder getMemoryResponder() {
        if (this.mActivity instanceof ByteBufferQueue.MemoryResponder) {
            return (ByteBufferQueue.MemoryResponder) this.mActivity;
        }
        return null;
    }

    public boolean onBackPressed(boolean isEditing) {
        return this.mPluginManager.onBackPressed(isEditing);
    }

    public RecordSession getSesion() {
        if (this.mSession != null) {
            return this.mSession;
        }
        return null;
    }

    public ArrayList<RecordSegment> getEditedSegments() {
        return this.mEditedSegments;
    }

    public boolean canChangeFocus() {
        return this.mCurrentSegment == null && this.mVideoController.canChangeFocus();
    }

    public boolean isAutoFocusing() {
        return this.mAutoFocusing;
    }

    public boolean setAutoFocusing(boolean autoFocusing) {
        RecordController controller = this.mVideoController;
        if (controller == null) {
            return false;
        }
        this.mAutoFocusing = autoFocusing;
        this.mVideoController.setAutoFocus(this.mAutoFocusing);
        return this.mAutoFocusing;
    }

    public void switchCamera() {
        if (!this.mIsSwitchingCamera && this.mCanKeepRecording) {
            if (canSwitchCamera()) {
                this.mSwitchCameraTask = getSwitchCameraTask();
                this.mSwitchCameraTask.execute(new Void[0]);
            } else {
                CrashUtil.logException(new VineLoggingException("You can not switch camera after recording has started."), "Person trying to record and switch? not cool.", new Object[0]);
            }
        }
    }

    public ResumeCameraAsyncTask getSwitchCameraTask() {
        throw new UnsupportedOperationException();
    }

    public boolean hasEditedSegments() {
        return this.mEditedSegments.size() > 0;
    }

    public Activity getActivity() {
        return this.mActivity;
    }

    public RecordSessionVersion getVersion() {
        if (this.mCurrentRecordingFile != null) {
            return this.mCurrentRecordingFile.version;
        }
        return null;
    }

    public boolean canSwitchFlash() {
        return this.mVideoController != null && this.mVideoController.canSwitchFlash();
    }

    public CameraSetting getCurrentCameraSetting() {
        return this.mVideoController.getCameraSetting();
    }

    public void setDelayDialog(boolean delayDialog) {
        this.mDelayDialog = delayDialog;
    }

    public void postOnHandlerLoop(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public void onPreviewError(RuntimeException callbackException) {
        throw callbackException;
    }

    protected class ResumeCameraAsyncTask extends AsyncTask<Void, Void, Void> {
        private final boolean mSwitchCamera;

        public ResumeCameraAsyncTask(boolean switchCamera) {
            this.mSwitchCamera = switchCamera;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            if (BasicVineRecorder.this.canSwitchCamera() || (!BasicVineRecorder.this.mVideoController.isRecording() && !this.mSwitchCamera)) {
                try {
                    System.gc();
                    if (this.mSwitchCamera) {
                        BasicVineRecorder.this.mFrontFacing = BasicVineRecorder.this.mFrontFacing ? false : true;
                    }
                    if (BasicVineRecorder.this.mVideoController.openDefaultCamera(BasicVineRecorder.this.mFrontFacing, true)) {
                        CameraSetting setting = BasicVineRecorder.this.mVideoController.getCameraSetting();
                        if (setting != null) {
                            BasicVineRecorder.this.mCurrentFrameRate = setting.fps;
                        }
                        BasicVineRecorder.this.start("Switch camera", false, true);
                        return null;
                    }
                    BasicVineRecorder.this.showCameraFailedToast();
                    return null;
                } catch (Exception e) {
                    SLog.e("Error on cancel camera switching.", (Throwable) e);
                    return null;
                }
            }
            return null;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            BasicVineRecorder.this.mIsSwitchingCamera = true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            BasicVineRecorder.this.mIsSwitchingCamera = false;
            BasicVineRecorder.this.mPluginManager.onResumeCameraAsyncTaskPostExecute();
        }
    }

    protected abstract class OnResumeTask extends AsyncTask<Void, CharSequence, RecordingFile> {
        public boolean isRunning;
        protected final View mClicker;
        protected long mStartTime;
        protected final String mTag;
        public boolean showDialogDelayed;

        protected abstract void publishFinishLastSegmentProgress();

        protected abstract void publishStartProgres();

        public OnResumeTask(View view, String tag) {
            this.mClicker = view;
            this.mTag = tag;
        }

        protected boolean onMakingSureCameraReady(RecordController controller) {
            return controller.openDefaultCamera(BasicVineRecorder.this.mFrontFacing, false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public RecordingFile doInBackground(Void... params) throws InterruptedException {
            CrashUtil.log("OnResume task started from {}.", this.mTag);
            RecordController controller = BasicVineRecorder.this.mVideoController;
            if (controller != null) {
                if (controller.isProcessing()) {
                    publishFinishLastSegmentProgress();
                    try {
                        controller.finishLastIfNeeded();
                    } catch (Exception e) {
                        CrashUtil.logException(e, "Failed to finish last one.", new Object[0]);
                    }
                    publishStartProgres();
                }
                BasicVineRecorder.this.stopProgressTimer();
                if (BasicVineRecorder.this.canKeepRecording()) {
                    CrashUtil.log("Async open camera");
                    if (onMakingSureCameraReady(controller)) {
                        CameraSetting cs = controller.getCameraSetting();
                        if (cs != null) {
                            BasicVineRecorder.this.mCurrentFrameRate = cs.fps;
                        }
                        BasicVineRecorder.this.startProgressTimer();
                        CrashUtil.log("Open camera successful: {} fps.", Integer.valueOf(BasicVineRecorder.this.mCurrentFrameRate));
                        if (BasicVineRecorder.this.mSession != null) {
                            RecordConfigUtils.RecordConfig config = BasicVineRecorder.this.mSession.getConfig();
                            while (controller != null && config != null && BasicVineRecorder.this.mCurrentDurationMs < config.maxDuration && !controller.isAudioReady()) {
                                SLog.d("Wait for audio to be ready. " + this);
                                if (isCancelled()) {
                                    break;
                                }
                                try {
                                    Thread.sleep(100L);
                                } catch (InterruptedException e2) {
                                }
                                config = BasicVineRecorder.this.mSession != null ? BasicVineRecorder.this.mSession.getConfig() : null;
                                controller = BasicVineRecorder.this.mVideoController;
                            }
                        }
                    } else if (!isCancelled()) {
                        BasicVineRecorder.this.showCameraFailedToast();
                    }
                }
            }
            return null;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            this.mStartTime = System.currentTimeMillis();
            if (this.mClicker != null) {
                this.mClicker.setEnabled(false);
            }
            if (BasicVineRecorder.this.canKeepRecording() && BasicVineRecorder.this.mVideoController != null && !BasicVineRecorder.this.mVideoController.isRecordingStarted() && !isCancelled()) {
                try {
                    if (this.showDialogDelayed) {
                        this.isRunning = true;
                    }
                } catch (WindowManager.BadTokenException e) {
                }
            }
            SLog.d("OnResume PreExecute took {}ms.", Long.valueOf(System.currentTimeMillis() - this.mStartTime));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(RecordingFile needHashTask) {
            this.isRunning = false;
            if (this.mClicker != null) {
                this.mClicker.setEnabled(true);
            }
            long timeTaken = System.currentTimeMillis() - this.mStartTime;
            if (timeTaken > BasicVineRecorder.sTimeTaken) {
                BasicVineRecorder.sTimeTaken = timeTaken;
            }
            BasicVineRecorder.this.mPluginManager.onResumeAsyncTaskPostExecute();
            SLog.d("OnResumeTask took {}ms.", Long.valueOf(timeTaken));
        }
    }

    public boolean canSwitchCamera() {
        return CameraManager.hasFrontFacingCamera() && CameraManager.hasBackFacingCamera() && this.mVideoController != null && !this.mVideoController.isRecording();
    }

    protected void stopProgressTimer() {
        if (this.mProgressTimer != null) {
            this.mProgressTimer.release();
            this.mProgressTimer = null;
        }
    }

    protected void startProgressTimer() {
        this.mProgressTimer = new ProgressTimer(this, this.mHandler, this.mThresholdMs);
        this.mProgressTimer.start();
    }

    public Runnable getOnCompleteConsumer() {
        return this.mOnCompleteConsumer;
    }

    public void setFinalFile(RecordingFile recordingFile) {
        this.finalFile = recordingFile;
    }

    public void onProgressMaxReached() {
    }

    public long getCurrentDuration() {
        return this.mCurrentDurationMs;
    }

    public RecordingFile getFile() {
        return this.mCurrentRecordingFile;
    }

    public void onAutoFocusComplete(boolean success) {
        SLog.d("Auto focus {}.", Boolean.valueOf(success));
    }

    public void postProgressUpdate(int progress) {
        this.mChangeProgressRunnable.progress = progress;
        this.mHandler.post(this.mChangeProgressRunnable);
    }

    private class ChangeProgressRunnable implements Runnable {
        public int progress;

        private ChangeProgressRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            BasicVineRecorder.this.changeProgress(this.progress);
        }
    }

    public void setCameraView(View cameraView) {
        this.mCameraView = cameraView;
    }

    public void initPreviewSurface() {
        try {
            this.mVideoController.setPreviewSurface(this.mCameraView);
        } catch (Exception e) {
            CrashUtil.logException(e, "Cannot resume.", new Object[0]);
        }
    }

    public View getCameraView() {
        return this.mCameraView;
    }

    public boolean isResuming() {
        OnResumeTask task = this.mResumeTask;
        return task != null && task.isRunning;
    }

    public String getThumbnailPath() {
        return this.mCurrentRecordingFile.getThumbnailPath();
    }

    public void setDiscardChanges(boolean discardChanges) {
        this.mDiscardChanges = discardChanges;
    }

    public void onCameraReady(RecordController controller) {
        if (this.mActivity != null) {
            if (controller.isSurfaceReady() && !controller.isRecordingStarted()) {
                start("Camera ready", false, true);
                return;
            }
            SLog.d("Do not start recording: {} {}", Boolean.valueOf(controller.isSurfaceReady()), Boolean.valueOf(controller.isRecordingStarted()));
            if (controller.isRecordingStarted() && !controller.isPreviewing()) {
                controller.startPreview();
            }
        }
    }

    public void onSurfaceReady(RecordController controller) {
        if (controller.isCameraReady()) {
            SLog.d("Start recording on surface ready.");
            if (!controller.isRecordingStarted()) {
                start("Surface Ready", false, true);
                return;
            } else {
                adjustBoundaries(controller.getCameraSetting());
                controller.startPreview();
                return;
            }
        }
        SLog.d("Do not start recording: {} {}.", false, Boolean.valueOf(controller.isRecordingStarted()));
    }

    public boolean startRelativeTime() {
        if (this.mVideoController == null || this.mSession == null || this.mSession.getConfig() == null || !this.mVideoController.isRecordingStarted() || !this.mCanKeepRecording || !this.mVideoController.isCameraReady() || this.mCurrentDurationMs >= this.mSession.getConfig().maxDuration) {
            return false;
        }
        if (!this.mVideoController.hasCameraReceivingFrames()) {
            this.mHandler.post(new Runnable() { // from class: co.vine.android.recorder.BasicVineRecorder.2
                @Override // java.lang.Runnable
                public void run() throws Resources.NotFoundException {
                    if (BasicVineRecorder.this.mActivity != null) {
                        Toast toast = null;
                        if (BasicVineRecorder.this.mToast != null) {
                            toast = (Toast) BasicVineRecorder.this.mToast.get();
                        }
                        if (toast == null) {
                            toast = Toast.makeText(BasicVineRecorder.this.mActivity, BasicVineRecorder.STRING_WAITING_ON_CAMERA, 0);
                        } else {
                            toast.setText(BasicVineRecorder.STRING_WAITING_ON_CAMERA);
                        }
                        BasicVineRecorder.this.mToast = new WeakReference(toast);
                        toast.show();
                    }
                }
            });
            return false;
        }
        if (this.mCurrentSegment != null && !endRelativeTime()) {
            return false;
        }
        this.mCurrentDurationMs = this.mRecordingFileDuration - System.currentTimeMillis();
        this.mPluginManager.onStartRelativeTime(this);
        this.mCurrentSegment = new RecordSegment(this.mCurrentDurationMs, this.mVideoController.getCameraSetting().fps);
        CrashUtil.log("START RELATIVE TIME: " + this.mVideoController + " bc: " + MasterPreviewCallback.getInstance().getCallbackStateString() + ", added: " + (BufferedPreviewManagerCallback.isUsed() ? Integer.valueOf(BufferedPreviewManagerCallback.getInstance().getAddedBufferArrayCount()) : "n/a"));
        this.mVideoController.setRecordingAudio(true);
        this.mVideoController.setRecording(this.mCurrentSegment);
        return true;
    }

    public synchronized boolean endRelativeTime() {
        if (this.mCurrentSegment != null) {
            CrashUtil.log("END RELATIVE TIME.");
            this.mVideoController.onEndRelativeTime(this.mCurrentSegment);
            this.mRecordingFileDuration = (int) this.mVideoController.getTimestampMs();
            this.mCurrentRecordingFile.getSession().add(this.mCurrentSegment);
            this.mEditedSegments.add(this.mCurrentSegment);
            this.mAddedSegments.add(this.mCurrentSegment);
            this.mCurrentRecordingFile.isDirty = true;
            this.mCurrentSegment = null;
            this.mPluginManager.onEndRelativeTime(this);
            this.mPluginManager.onSegmentDataChanged(this.mEditedSegments);
            this.mCurrentDurationMs = this.mRecordingFileDuration;
            this.mVideoController.setRecordingAudio(false);
            this.mVideoController.setRecording(this.mCurrentSegment);
        }
        return true;
    }

    public int getEditedDurationMs() {
        int duration = 0;
        Iterator<RecordSegment> it = this.mEditedSegments.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            if (!segment.removed) {
                duration += segment.getDurationMs();
            }
        }
        return duration;
    }

    public void changeFocusTo(float x, float y) {
        this.mVideoController.autoFocus((int) x, (int) y);
    }

    public void onUiPaused() {
        this.mActivity = null;
        this.mCameraView = null;
        this.mOnCompleteConsumer = null;
        onPause();
        this.mPluginManager.onPause();
    }

    public void onProgressThresholdReached() {
    }

    public void onPause() {
        this.mNeverResumedRecorder = false;
        if (this.mSwitchCameraTask != null) {
            this.mSwitchCameraTask.cancel(true);
        }
        if (this.mResumeTask != null) {
            SLog.d("Cancel resume task: " + this.mResumeTask);
            this.mResumeTask.cancel(true);
        }
        PreviewManager.getInstance().cancelPendingPreviews();
        this.mFinishProcessRunnable.run("onPause", true, false);
        if (this.mVideoController != null) {
            this.mVideoController.onPause();
        }
        if (this.mProgressTimer != null) {
            this.mProgressTimer.release();
            this.mProgressTimer = null;
        }
    }

    protected void finish(String tag) {
        this.mFinished = true;
        if (this.mVideoController != null && this.mVideoController.isRecordingStarted()) {
            stop(tag, null, true, false);
            this.mCanKeepRecording = false;
        } else if (this.mOnCompleteConsumer != null) {
            this.mOnCompleteConsumer.run();
        }
    }

    public boolean isFinished() {
        return this.mFinished;
    }

    protected void stop(String tag, Runnable onComplete, boolean releasePreview, boolean saveSession) {
        if (onStop()) {
            BaseFinishProcessTask finishProcessTask = getFinishProcessTask(tag, onComplete, releasePreview, saveSession);
            this.mVideoController.setFinishProcessTask(finishProcessTask);
            finishProcessTask.execute(new Void[0]);
        }
    }

    public BaseFinishProcessTask getFinishProcessTask(String tag, Runnable onComplete, boolean releasePreview, boolean saveSession) {
        return new FinishProcessTask(tag, onComplete, releasePreview, saveSession || this.mCurrentRecordingFile.isSavedSession);
    }

    public class FinishProcessTask extends BaseFinishProcessTask {
        public FinishProcessTask(String tag, Runnable onComplete, boolean releasePreview, boolean saveSession) {
            super(tag, onComplete, releasePreview, saveSession);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            BasicVineRecorder.this.mFinishProcessRunnable.run(this.tag, this.releasePreview, this.saveSession);
            return null;
        }
    }

    public boolean isSavedSession() {
        return this.mCurrentRecordingFile.isSavedSession;
    }

    public void modifyWhiteBalance(boolean up) {
        if (this.mVideoController != null) {
            this.mVideoController.modifyWhiteBalance(up);
        }
    }

    public void modifyExposure(boolean up) {
        if (this.mVideoController != null) {
            this.mVideoController.modifyExposure(up);
        }
    }

    public boolean isSessionDirty() {
        return this.mCurrentRecordingFile != null && this.mCurrentRecordingFile.isDirty;
    }

    public void modifySceneMode(boolean up) {
        if (this.mVideoController != null) {
            this.mVideoController.modifySceneMode(up);
        }
    }

    public void modifyColorEffects(boolean up) {
        if (this.mVideoController != null) {
            this.mVideoController.modifyColorEffects(up);
        }
    }

    public void modifyAntiBanding(boolean up) {
        if (this.mVideoController != null) {
            this.mVideoController.modifyAntiBanding(up);
        }
    }

    public void switchImageStabilization() {
        if (this.mVideoController != null) {
            this.mVideoController.switchImageStabilization();
        }
    }

    public class FinishProcessRunnable {
        private boolean isResumed;

        public FinishProcessRunnable() {
        }

        public void reset() {
            this.isResumed = false;
        }

        public void onIncompleteSessionFinish() {
            if (BasicVineRecorder.this.mFinished && !BasicVineRecorder.this.canKeepRecording() && !this.isResumed) {
                this.isResumed = true;
            }
        }

        public boolean doNotDeleteSession(boolean isFinishing) {
            return isFinishing;
        }

        public boolean onMakeRecordingStop(String tag, boolean releasePreview, boolean saveSession) {
            if (BasicVineRecorder.this.mVideoController == null) {
                return false;
            }
            CrashUtil.log("Stop recording in VineRecorder from {}: ", tag);
            boolean wasRecordingStarted = BasicVineRecorder.this.mVideoController.isRecordingStarted();
            if (wasRecordingStarted) {
                BasicVineRecorder.this.endRelativeTime();
                BasicVineRecorder.this.grabThumbnailsRunnable = BasicVineRecorder.this.mVideoController.stop(BasicVineRecorder.this.mDiscardChanges, releasePreview);
            }
            CrashUtil.log("Status: wasRecordingStarted {} releasePreview {} saveSession {} mDiscardChanges {} mDoNotDeleteSession {} mCurrentRecordingFile {}", Boolean.valueOf(wasRecordingStarted), Boolean.valueOf(releasePreview), Boolean.valueOf(saveSession), Boolean.valueOf(BasicVineRecorder.this.mDiscardChanges), Boolean.valueOf(BasicVineRecorder.this.mDoNotDeleteSession), BasicVineRecorder.this.mCurrentRecordingFile);
            return wasRecordingStarted;
        }

        public void onCompleteSessionFinish(boolean saveSession) {
            RecordingFile recordingFile = BasicVineRecorder.this.mCurrentRecordingFile;
            if (recordingFile == null) {
                CrashUtil.logException(new VineLoggingException("Failed to stop recording."));
                return;
            }
            RecordSession session = recordingFile.getSession();
            session.setAudioDataCount(session.calculateAudioCount());
            session.setVideoDataCount(session.calculateVideoCount());
            if (!BasicVineRecorder.this.mDiscardChanges) {
                boolean isFinishing = BasicVineRecorder.this.mFinished && recordingFile.hasData() && !BasicVineRecorder.this.canKeepRecording();
                BasicVineRecorder.this.mDoNotDeleteSession = doNotDeleteSession(isFinishing);
                boolean isValidSession = BasicVineRecorder.this.mEditedSegments.size() > 0 && session.getVideoDataCount() > 0;
                if (BasicVineRecorder.this.mEditedSegments.size() > 0 && !isValidSession) {
                    CrashUtil.log("Invalid session found.");
                }
                if (isValidSession) {
                    try {
                        RecordSessionManager.writeRecordingFile(recordingFile, saveSession);
                    } catch (IOException e) {
                        if (ExceptionUtil.isNoSpaceLeftException(e)) {
                            BasicVineRecorder.this.onDeviceIssue(e, DeviceIssue.NOT_ENOUGH_SPACE, false);
                            return;
                        } else {
                            if (saveSession) {
                                BasicVineRecorder.this.onDeviceIssue(e, DeviceIssue.STORAGE_NOT_READY, false);
                                return;
                            }
                            CrashUtil.log("Failed to save the files due to IOException: " + e.getMessage());
                        }
                    }
                } else if (!BasicVineRecorder.this.mCanKeepRecording) {
                    try {
                        RecordSessionManager.deleteSession(recordingFile.folder, "Invalid session");
                    } catch (IOException e2) {
                        CrashUtil.log("Failed to delete session: " + e2.getMessage());
                    }
                }
                if (saveSession) {
                    if (!recordingFile.isLastSession) {
                        recordingFile.isSavedSession = true;
                        recordingFile.isDirty = false;
                    }
                    if (isValidSession) {
                        BasicVineRecorder.this.cleanThumbnails(false);
                    }
                }
                if (isFinishing) {
                    BasicVineRecorder.this.finishRecording();
                    return;
                }
                return;
            }
            if (!BasicVineRecorder.this.mDoNotDeleteSession && !BasicVineRecorder.this.mCurrentRecordingFile.isSavedSession) {
                try {
                    CrashUtil.log("Session {} deleted.", BasicVineRecorder.this.mCurrentRecordingFile.folder);
                    RecordSessionManager.deleteSession(BasicVineRecorder.this.mCurrentRecordingFile.folder, "Discard Changes.");
                } catch (IOException e3) {
                    CrashUtil.logException(e3, "Failed to delete session.", new Object[0]);
                }
            }
            BasicVineRecorder.this.mToRemove.clear();
        }

        public boolean isCompleteSession(boolean wasRecordingStarted) {
            return wasRecordingStarted;
        }

        public synchronized void run(String tag, boolean releasePreview, boolean saveSession) {
            boolean wasRecordingStarted = onMakeRecordingStop(tag, releasePreview, saveSession);
            onPreCompleteSession(wasRecordingStarted);
            if (isCompleteSession(wasRecordingStarted)) {
                onCompleteSessionFinish(saveSession);
            } else {
                onIncompleteSessionFinish();
            }
        }

        protected void onPreCompleteSession(boolean wasRecordingStarted) {
        }
    }

    public void onDeviceHasNotEnoughSpaceLeft(Throwable e) {
        onDeviceIssue(e, DeviceIssue.NOT_ENOUGH_SPACE);
    }

    public void onDeviceIssue(Throwable e, DeviceIssue issue) {
        onDeviceIssue(e, issue, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDeviceIssue(Throwable e, DeviceIssue issue, boolean stopIfNeeded) {
        if (!this.mAlreadyStoppingForUnspportedReasons) {
            this.mAlreadyStoppingForUnspportedReasons = true;
            if (e != null) {
                CrashUtil.logException(e);
                CrashUtil.log("Device not supported happened.");
            }
            if (stopIfNeeded) {
                stopAndDiscardChanges("OnDeviceNotSupported", getOnDeviceIssueAction(issue), true);
            } else {
                getOnDeviceIssueAction(issue).run();
            }
        }
    }

    public void stopAndDiscardChanges(String tag, Runnable onComplete, boolean releasePreview) {
        setDiscardChanges(true);
        this.mFinishProcessRunnable.run(tag, releasePreview, false);
        if (onComplete != null) {
            onComplete.run();
        }
    }

    @SuppressLint({"ShowToast"})
    public void onUiResumed(Activity activity, Runnable onCompleteAsyncTask, boolean doNotResumeRecorder) {
        this.mActivity = activity;
        this.mDelayDialog = false;
        this.mPluginManager.onResume(activity);
        this.mVideoController.updateRotation(activity);
        this.mOnCompleteConsumer = onCompleteAsyncTask;
        this.mFinishProcessRunnable.reset();
    }

    public boolean deleteCurrentDraftFolder(String tag) {
        try {
            RecordSessionManager.deleteSession(this.mCurrentRecordingFile.folder, tag);
            return true;
        } catch (IOException e) {
            CrashUtil.logException(e);
            return false;
        }
    }

    public boolean release() {
        boolean abandoned = false;
        CrashUtil.log("Recorder Released.");
        this.mVideoController.releaseParent();
        this.mVideoController.releaseCameraResources();
        this.mVideoController.releaseResources();
        this.mVideoController.releaseCallbacks();
        this.mVideoController.logPreviewSizeExceptionIfNeeded();
        boolean sessionDeleted = false;
        if (this.mCurrentRecordingFile != null) {
            File folder = this.mCurrentRecordingFile.folder;
            if (!this.mDoNotDeleteSession && !RecordSessionManager.isSessionSaved(folder)) {
                sessionDeleted = deleteCurrentDraftFolder(BuildConfig.BUILD_TYPE);
            } else {
                File temporaryDataFile = RecordSessionManager.getDataFile(folder, false);
                if (temporaryDataFile.exists()) {
                    FileUtils.deleteQuietly(temporaryDataFile);
                }
            }
            SLog.i("Session deleted: {}", Boolean.valueOf(sessionDeleted));
        }
        this.mSession = null;
        this.mCurrentRecordingFile = null;
        this.mCurrentSegment = null;
        this.mVideoController = null;
        this.mAddedSegments.clear();
        if (this.mEditedSegments.size() > 0 && sessionDeleted) {
            abandoned = true;
        }
        this.mEditedSegments.clear();
        return abandoned;
    }

    protected void invalidateFinishButton() {
    }

    protected void setHasPreviewedAlreadyIfNeeded() {
    }

    protected void swapTimestampsFromSegments(RecordSession session, ArrayList<RecordSegment> segments) {
        int timestamp = 0;
        Iterator<RecordSegment> it = segments.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            if (!segment.removed) {
                timestamp += segment.getDurationMs();
            }
        }
        this.mCurrentDurationMs = timestamp;
        int timestamp2 = timestamp * 1000;
        this.mVideoController.setAudioTimestampUs(timestamp2);
        this.mVideoController.setVideoTimeStampUs(timestamp2);
        this.mRecordingFileDuration = (int) this.mCurrentDurationMs;
        invalidateFinishButton();
        changeProgress(this.mCurrentDurationMs);
        this.mVideoController.onTimestampSwap(session);
        this.mPluginManager.onSegmentDataChanged(this.mEditedSegments);
    }

    public void cleanThumbnails(boolean discardNewOnes) {
        HashSet<RecordSegment> toRemove = new HashSet<>(this.mToRemove);
        this.mToRemove.clear();
        if (discardNewOnes) {
            toRemove.addAll(this.mAddedSegments);
        }
        Iterator<RecordSegment> it = toRemove.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            String thumbnailPath = segment.getThumbnailPath();
            if (!TextUtils.isEmpty(thumbnailPath)) {
                FileUtils.deleteQuietly(new File(thumbnailPath));
            }
        }
    }

    public void onResume(String tag) {
        boolean canKeepRecording = canKeepRecording();
        Object[] objArr = new Object[4];
        objArr[0] = Boolean.valueOf(this.mDelayDialog);
        objArr[1] = Boolean.valueOf(canKeepRecording);
        objArr[2] = false;
        objArr[3] = Boolean.valueOf(this.finalFile != null);
        CrashUtil.log("Resume VineRecorder: delayDialog: {} canKeepRecording: {} isEditing: {} NullFinalFile: {}.", objArr);
        if (canKeepRecording) {
            this.mResumeTask = getOnResumeTask(null, "OnResume_" + tag);
            this.mResumeTask.showDialogDelayed = this.mDelayDialog;
            this.mResumeTask.execute(new Void[0]);
        } else if (this.finalFile != null) {
            this.mOnCompleteConsumer.run();
        }
    }

    public void receivedFirstFrameAfterStartingPreview() {
    }
}
