package co.vine.android.recorder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.View;
import co.vine.android.plugin.BaseRecorderPluginManager;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordProcessor;
import co.vine.android.recorder.SwVineFrameRecorder;
import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.recorder.audio.AudioReceiver;
import co.vine.android.recorder.audio.AudioRecordSource;
import co.vine.android.recorder.audio.AudioSource;
import co.vine.android.recorder.buffered.BufferedRecordProcessor;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.recorder.camera.CameraSetting;
import co.vine.android.recorder.camera.PreviewManager;
import co.vine.android.service.ResourceService;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.MediaUtil;
import co.vine.android.util.SystemUtil;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avutil;
import java.io.IOException;
import java.util.ArrayList;

@TargetApi(14)
/* loaded from: classes.dex */
public class RecordController implements ParentHolder {
    public static long sMaxKnownStopTime;
    private AudioSource mAudioSource;
    private final Camera.AutoFocusCallback mAutoFocusCallback;
    private final CameraManager.CameraManagerController mCameraManagerController;
    private final boolean mFastEncoding;
    private PreviewManager.InvalidPreviewSizeException mInvalidPreviewSizeException;
    private BasicVineRecorder mParent;
    private final BaseRecorderPluginManager mPluginManager;
    private final RecordProcessor mRecordProcessor;
    private int mRotation;
    private final SurfaceController mSurfaceController;
    private boolean mFlash = false;
    private boolean mAutoFocus = true;
    private boolean mIsFocusing = false;
    private final RecordState mState = new RecordState();
    private final RecordClock mClock = new RecordClock();
    private final CameraManager mCameraManager = CameraManager.getInstance();
    private final PreviewManager mPreviewManager = PreviewManager.getInstance();

    public static class RecordControllerReadyStartedException extends Exception {
    }

    public RecordController(BasicVineRecorder parent, Activity activity, boolean fastEncoding, boolean useMp4, BaseRecorderPluginManager manager) {
        this.mParent = parent;
        this.mFastEncoding = fastEncoding;
        this.mCameraManagerController = new ControllerCameraManagerController();
        this.mAutoFocusCallback = new ControllerAutoFocusCallback();
        this.mPluginManager = manager;
        this.mRecordProcessor = new BufferedRecordProcessor(useMp4, this.mState, this.mClock, activity.getApplicationContext(), this.mPluginManager, this);
        this.mPreviewManager.setManagerCallback(this.mRecordProcessor.getPreviewManagerCallback());
        this.mSurfaceController = new SurfaceController(this.mRecordProcessor.getSurfaceListener(this));
    }

    public void swapSession() {
        this.mRecordProcessor.swapSession(this.mParent.getFile().getSession());
        this.mState.setCurrentSegment(null);
        this.mState.setLastVideoSegment(null);
        this.mState.setLastAudioRecordingSegment(null);
        this.mState.setEnded(false);
        if (this.mAudioSource != null) {
            this.mAudioSource.stop();
            this.mAudioSource = null;
        }
    }

    public void updateRotation(Activity activity) {
        this.mRotation = CameraManager.getCameraRotation(activity);
    }

    public void switchFlash() {
        this.mFlash = !this.mFlash;
        this.mCameraManager.changeFlashState(this.mFlash);
    }

    public boolean isFlashOn() {
        return this.mFlash;
    }

    public void setAutoFocus(boolean isOn) {
        this.mAutoFocus = isOn;
        try {
            if (this.mAutoFocus) {
                if (this.mPreviewManager.isPreviewing()) {
                    this.mCameraManager.startContinuousAutoFocus(this.mCameraManager.getParameters());
                }
            } else if (this.mPreviewManager.isPreviewing()) {
                this.mCameraManager.stopContinuousFocus(this.mCameraManager.getParameters());
                if (this.mAutoFocus && !this.mIsFocusing && this.mCameraManager.autoFocus(this.mAutoFocusCallback)) {
                    this.mIsFocusing = true;
                }
            }
        } catch (Exception e) {
            CrashUtil.logException(e, "It's ok if we failed to auto focus here.", new Object[0]);
        }
    }

    public void autoFocus(int x, int y) {
        if (!this.mIsFocusing && this.mCameraManager.setFocusAreas(x, y) && this.mCameraManager.autoFocus(this.mAutoFocusCallback)) {
            this.mIsFocusing = true;
            this.mPluginManager.onAutoFocus(x, y);
        }
    }

    public void onPause() {
        setRecording(null);
        releaseCameraAndPreview("onPause");
        PreviewManager.releaseCallback();
    }

    public boolean openDefaultCamera(boolean frontFacing, boolean reOpen) {
        long start = System.currentTimeMillis();
        CameraSetting cameraSetting = this.mCameraManager.openDefaultCamera(this.mParent.getConfig(), frontFacing, this.mRotation, reOpen);
        SLog.d("Camera Open 1 took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
        if (cameraSetting != null) {
            SLog.d("Received camera info.");
            long start2 = System.currentTimeMillis();
            this.mPreviewManager.setPreviewSize(cameraSetting.originalW, cameraSetting.originalH);
            SLog.d("Camera Open 2 took {}ms.", Long.valueOf(System.currentTimeMillis() - start2));
            long start3 = System.currentTimeMillis();
            this.mIsFocusing = false;
            this.mParent.onCameraReady(this);
            SLog.d("Camera Open 3 took {}ms.", Long.valueOf(System.currentTimeMillis() - start3));
            return true;
        }
        CrashUtil.log("Failed to open camera: {}.", Boolean.valueOf(frontFacing));
        return false;
    }

    public boolean isProcessing() {
        return this.mRecordProcessor.wasProcessing();
    }

    public void finishLastIfNeeded() {
        this.mRecordProcessor.finishLastIfNeeded();
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0075 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public co.vine.android.util.MediaUtil.GenerateThumbnailsRunnable stop(boolean r12, boolean r13) throws java.lang.InterruptedException {
        /*
            r11 = this;
            r10 = 0
            co.vine.android.recorder.RecordState r8 = r11.mState
            boolean r8 = r8.isStarted()
            if (r8 != 0) goto L15
            boolean r8 = com.edisonwang.android.slog.SLog.sLogsOn
            if (r8 == 0) goto L15
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r9 = "You can't stop before you have start it."
            r8.<init>(r9)
            throw r8
        L15:
            r1 = 0
            r2 = 0
            long r4 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> L84
            java.lang.String r8 = "Stop audio runnable and recording."
            co.vine.android.util.CrashUtil.log(r8)     // Catch: java.lang.Exception -> L84
            co.vine.android.recorder.RecordState r8 = r11.mState     // Catch: java.lang.Exception -> L84
            r9 = 0
            r8.setCollectMoreAudio(r9)     // Catch: java.lang.Exception -> L84
            co.vine.android.recorder.RecordState r8 = r11.mState     // Catch: java.lang.Exception -> L84
            boolean r8 = r8.isStarted()     // Catch: java.lang.Exception -> L84
            if (r8 == 0) goto L5c
            java.lang.String r8 = "Finishing recording, calling stop and release on recorder"
            com.edisonwang.android.slog.SLog.d(r8)     // Catch: java.lang.Exception -> L84
            if (r13 == 0) goto L8d
            boolean r8 = r11.mFastEncoding     // Catch: java.lang.Exception -> L84
            if (r8 == 0) goto L7e
            java.lang.Thread r3 = new java.lang.Thread     // Catch: java.lang.Exception -> L84
            co.vine.android.recorder.RecordController$1 r8 = new co.vine.android.recorder.RecordController$1     // Catch: java.lang.Exception -> L84
            r8.<init>()     // Catch: java.lang.Exception -> L84
            r3.<init>(r8)     // Catch: java.lang.Exception -> L84
            r3.start()     // Catch: java.lang.Exception -> Lc4
            r2 = r3
        L47:
            java.lang.String r8 = "Waiting..........."
            com.edisonwang.android.slog.SLog.d(r8)     // Catch: java.lang.Exception -> L84
            co.vine.android.recorder.RecordState r8 = r11.mState     // Catch: java.lang.Exception -> L84
            r9 = 0
            r8.setStarted(r9)     // Catch: java.lang.Exception -> L84
            if (r12 == 0) goto L9b
            co.vine.android.recorder.RecordProcessor r8 = r11.mRecordProcessor     // Catch: java.lang.Exception -> L84
            r8.stopProcessing()     // Catch: java.lang.Exception -> L84
            r11.finishLastIfNeeded()     // Catch: java.lang.Exception -> L91
        L5c:
            co.vine.android.recorder.RecordState r8 = r11.mState     // Catch: java.lang.Exception -> L84
            r9 = 1
            r8.setEnded(r9)     // Catch: java.lang.Exception -> L84
        L62:
            co.vine.android.recorder.RecordState r8 = r11.mState
            r8.setStarted(r10)
            if (r13 == 0) goto Lb9
            java.lang.String r8 = "Force stop"
            r11.releaseCameraAndPreview(r8)
        L6e:
            co.vine.android.recorder.RecordProcessor r8 = r11.mRecordProcessor
            r8.onStopped(r13)
            if (r2 == 0) goto L78
            r2.join()     // Catch: java.lang.InterruptedException -> Lbd
        L78:
            java.lang.String r8 = "Recorder is stopped."
            com.edisonwang.android.slog.SLog.d(r8)
            return r1
        L7e:
            java.lang.String r8 = "Stop"
            r11.releaseCameraAndPreview(r8)     // Catch: java.lang.Exception -> L84
            goto L47
        L84:
            r0 = move-exception
        L85:
            java.lang.String r8 = "Stop failed"
            java.lang.Object[] r9 = new java.lang.Object[r10]
            co.vine.android.util.CrashUtil.logException(r0, r8, r9)
            goto L62
        L8d:
            co.vine.android.recorder.camera.PreviewManager.releaseCallback()     // Catch: java.lang.Exception -> L84
            goto L47
        L91:
            r0 = move-exception
            java.lang.String r8 = "discard failed."
            r9 = 0
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch: java.lang.Exception -> L84
            co.vine.android.util.CrashUtil.logException(r0, r8, r9)     // Catch: java.lang.Exception -> L84
            goto L5c
        L9b:
            co.vine.android.recorder.RecordProcessor r8 = r11.mRecordProcessor     // Catch: java.lang.Exception -> L84
            co.vine.android.util.MediaUtil$GenerateThumbnailsRunnable r1 = r8.stop()     // Catch: java.lang.Exception -> L84
            long r8 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> L84
            long r6 = r8 - r4
            java.lang.String r8 = "Stop wait time: {}."
            java.lang.Long r9 = java.lang.Long.valueOf(r6)     // Catch: java.lang.Exception -> L84
            com.edisonwang.android.slog.SLog.i(r8, r9)     // Catch: java.lang.Exception -> L84
            long r8 = co.vine.android.recorder.RecordController.sMaxKnownStopTime     // Catch: java.lang.Exception -> L84
            int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r8 <= 0) goto L5c
            co.vine.android.recorder.RecordController.sMaxKnownStopTime = r6     // Catch: java.lang.Exception -> L84
            goto L5c
        Lb9:
            co.vine.android.recorder.camera.PreviewManager.releaseCallback()
            goto L6e
        Lbd:
            r0 = move-exception
            java.lang.String r8 = "Camera still not released?!"
            com.edisonwang.android.slog.SLog.e(r8)
            goto L78
        Lc4:
            r0 = move-exception
            r2 = r3
            goto L85
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.RecordController.stop(boolean, boolean):co.vine.android.util.MediaUtil$GenerateThumbnailsRunnable");
    }

    public void makePreview(RecordSegment segment, boolean getLastSegmentOnlyMode, boolean ignoreTrim) {
        this.mRecordProcessor.makePreview(this.mParent.getFile(), segment, getLastSegmentOnlyMode, ignoreTrim);
    }

    public void writeToFile(ArrayList<RecordSegment> editedSegments, byte[] videoData, AudioArray audioData) throws InterruptedException, IOException {
        MediaUtil.GenerateThumbnailsRunnable r = writeToFile(editedSegments, videoData, audioData, true);
        if (r != null) {
            r.run();
        }
    }

    private MediaUtil.GenerateThumbnailsRunnable writeToFile(ArrayList<RecordSegment> editedSegments, byte[] videoData, AudioArray audioData, boolean preview) throws InterruptedException {
        try {
            SLog.i("Write to file: {}", Boolean.valueOf(preview));
            while (this.mAudioSource != null && this.mAudioSource.isInitialized()) {
                Thread.sleep(20L);
            }
        } catch (InterruptedException e) {
            SLog.d("No...");
        }
        return this.mRecordProcessor.writeToFile(this.mParent.getFile(), editedSegments, videoData, audioData, preview);
    }

    public boolean isAudioReady() {
        return this.mAudioSource != null && this.mAudioSource.isInitialized();
    }

    public CameraSetting getCameraSetting() {
        return this.mCameraManager.getCameraSetting();
    }

    public void logPreviewSizeExceptionIfNeeded() {
        if (this.mInvalidPreviewSizeException != null) {
            CrashUtil.logException(this.mInvalidPreviewSizeException);
        }
    }

    public void setAudioTrim(boolean enabled) {
        this.mRecordProcessor.setAudioTrim(enabled);
    }

    public void waitForPreviewToStart() {
        this.mPreviewManager.waitForPreviewToStart();
    }

    public boolean canSwitchFlash() {
        return this.mCameraManager.canSwitchFlash();
    }

    public void onExternalClipAdded(int microseconds) {
        this.mRecordProcessor.onExternalClipAdded(microseconds);
    }

    public void onRecordingStarted() {
        PreviewManager.setPreviewCallback(this.mCameraManagerController);
    }

    public boolean hasCameraReceivingFrames() {
        return this.mPreviewManager.hasGivenBufferToCurrentCallback();
    }

    public void onEndRelativeTime(RecordSegment segmentToEnd) {
        this.mRecordProcessor.onEndRelativeTime(segmentToEnd);
    }

    public void releaseCallbacks() {
        this.mSurfaceController.releaseCallbacks();
    }

    public boolean isSurfaceReady() {
        return this.mSurfaceController.isSurfaceReady();
    }

    public void setPreviewSurface(View cameraView) throws IOException {
        this.mSurfaceController.setPreviewSurface(cameraView);
    }

    public void onTimestampSwap(RecordSession session) {
        this.mRecordProcessor.onSessionTimestampChanged(session);
    }

    @Override // co.vine.android.recorder.ParentHolder
    public BasicVineRecorder getParent() {
        return this.mParent;
    }

    public synchronized boolean start(Activity activity, String output, RecordSession session) throws RecordControllerReadyStartedException {
        boolean z;
        if (!this.mCameraManager.isCameraReady()) {
            throw new RuntimeException("You have to choose a camera via open() first.");
        }
        this.mSurfaceController.assertValidSurfaces();
        if (this.mState.isStarted()) {
            throw new RecordControllerReadyStartedException();
        }
        CameraSetting cs = this.mCameraManager.getCameraSetting();
        if (cs == null) {
            throw new RuntimeException("Camera is ready but camera setting is null, something is terribly wrong.");
        }
        SLog.d("START Recorder, with ResourceService ready? {}.", Boolean.valueOf(ResourceService.IS_READY));
        try {
            finishLastIfNeeded();
        } catch (Exception e) {
            SLog.d("Finish last if needed Failed.", (Throwable) e);
        }
        long start = System.currentTimeMillis();
        this.mState.setEnded(false);
        this.mState.setStarted(true);
        if (this.mAudioSource != null && this.mAudioSource.isInitialized()) {
            this.mAudioSource.stop();
        }
        AudioReceiver receiver = this.mRecordProcessor.prepareAudioReceiver(session);
        this.mRecordProcessor.prepareImageReceiver(session);
        this.mAudioSource = new AudioRecordSource(output, ResourceService.getAudioDataBuffer(receiver.getType()));
        this.mState.setCollectMoreAudio(true);
        this.mAudioSource.start(receiver);
        this.mClock.resetStartTime();
        if (!SwVineFrameRecorder.hasEverSuccessfullyLoaded) {
            try {
                SwVineFrameRecorder.tryLoad(activity);
            } catch (SwVineFrameRecorder.Exception e2) {
                CrashUtil.logException(e2, "Failed to load native libraries.", new Object[0]);
            }
        }
        SLog.d("start 1 took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
        boolean success = this.mRecordProcessor.start(this.mParent, output, activity, cs, new RecordProcessor.ProcessingErrorHandler() { // from class: co.vine.android.recorder.RecordController.2
            @Override // co.vine.android.recorder.RecordProcessor.ProcessingErrorHandler
            public void onNotEnoughSpaceLeft(Exception e3) {
                RecordController.this.mParent.onDeviceHasNotEnoughSpaceLeft(e3);
            }
        });
        if (!success) {
            this.mAudioSource.stop();
            z = false;
        } else {
            z = true;
        }
        return z;
    }

    public void startPreview(CameraSetting cs) {
        RecordConfigUtils.RecordConfig config = this.mParent.getConfig();
        try {
            this.mPreviewManager.startPreview(this.mCameraManagerController, config, this.mSurfaceController, this.mFlash, this.mAutoFocus, cs.fps, this.mRotation, this.mClock.getAudioTimestampUs() / 1000);
        } catch (PreviewManager.InvalidPreviewSizeException e) {
            cs.originalH = 240;
            cs.originalW = avutil.AV_PIX_FMT_YUVJ411P;
            CrashUtil.log("CAUGHT INVALID PREVIEW SIZE, trying to use a smaller size again: " + cs.originalW + " * " + cs.originalH + " ff: " + cs.frontFacing);
            if (this.mParent != null && this.mParent.mActivity != null) {
                Point displaySize = SystemUtil.getDisplaySize(this.mParent.mActivity);
                CrashUtil.log("Screen size: {} * {}", Integer.valueOf(displaySize.x), Integer.valueOf(displaySize.y));
            }
            this.mCameraManager.printSupportedPreviewSizes();
            this.mInvalidPreviewSizeException = e;
            this.mPreviewManager.setPreviewSize(cs.originalW, cs.originalH);
            try {
                this.mPreviewManager.startPreview(this.mCameraManagerController, config, this.mSurfaceController, this.mFlash, this.mAutoFocus, cs.fps, this.mRotation, this.mClock.getAudioTimestampUs() / 1000);
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    protected void releaseCameraAndPreview(String tag) {
        CrashUtil.log("Release camera and preview: " + tag);
        this.mPreviewManager.releaseCameraAndPreview("Controller " + tag);
    }

    public boolean isCameraReady() {
        return this.mCameraManager.isCameraReady();
    }

    public void startPreview() {
        CameraSetting cs;
        if (!this.mSurfaceController.isSurfaceReady()) {
            CrashUtil.log("Start Preview requested, but surface is not ready yet, let's wait.");
            return;
        }
        if (this.mCameraManager.isCameraReady() && !this.mPreviewManager.isPreviewing() && (cs = this.mCameraManager.getCameraSetting()) != null) {
            cs.fps = this.mCameraManager.setFrameRate(cs.originalFrameRate);
            if (cs.fps > 0) {
                this.mClock.setFrameTimeUs(1000000 / cs.fps);
                SLog.d("Determined frame rate: {}, frame time: {}.", Integer.valueOf(cs.fps), Long.valueOf(this.mClock.getFrameTimeUs()));
                this.mFlash = false;
                startPreview(cs);
            }
        }
    }

    public boolean isRecording() {
        return this.mState.getCurrentSegment() != null;
    }

    public boolean isRecordingStarted() {
        return this.mState.isStarted();
    }

    public void setRecording(RecordSegment segment) {
        if (isRecordingStarted()) {
            if (segment != null) {
                this.mRecordProcessor.onNewSegmentStart();
                this.mClock.startSegment();
                segment.setCameraSetting(this.mCameraManager.getCameraSetting());
            }
            this.mState.setCurrentSegment(segment);
        }
    }

    public void setFinishProcessTask(BaseFinishProcessTask finishProcessTask) {
        this.mRecordProcessor.setFinishProcessTask(finishProcessTask);
    }

    public void modifyWhiteBalance(boolean up) {
        this.mCameraManager.modifyWhiteBalance(up);
    }

    public void modifyExposure(boolean up) {
        this.mCameraManager.modifyExposure(up);
    }

    public void modifyZoom(boolean zoomIn) {
        this.mCameraManager.modifyZoom(zoomIn);
    }

    public void stopZoom() {
        this.mCameraManager.stopSmoothZoom();
    }

    public void modifySceneMode(boolean up) {
        this.mCameraManager.modifySceneMode(up);
    }

    public void modifyColorEffects(boolean up) {
        this.mCameraManager.modifyColorEffects(up);
    }

    public void modifyAntiBanding(boolean up) {
        this.mCameraManager.modifyAntiBanding(up);
    }

    public void switchImageStabilization() {
        this.mCameraManager.switchImageStabilization();
    }

    public void setRecordingAudio(boolean recordingAudio) {
        this.mState.setRecordingAudio(recordingAudio);
    }

    public void releaseCameraResources() {
        this.mPreviewManager.releaseResources();
    }

    public void releaseParent() {
        this.mParent = null;
    }

    public void releaseResources() {
        SLog.d("releaseResources");
        this.mState.setLastAudioRecordingSegment(null);
        this.mState.setLastVideoSegment(null);
        this.mRecordProcessor.releaseResources();
    }

    public boolean isPreviewing() {
        return this.mPreviewManager.isPreviewing();
    }

    public boolean canChangeFocus() {
        return this.mCameraManager.canChangeFocus();
    }

    public void setAudioTimestampUs(int timestamp) {
        this.mClock.setAudioTimestampUs(timestamp);
        if (timestamp == 0) {
            this.mClock.resetStartTime();
        }
    }

    public void setVideoTimeStampUs(long timestamp) {
        this.mRecordProcessor.setVideoTimeStampUs(timestamp);
    }

    public BaseFinishProcessTask getFinishProcessTask() {
        return this.mRecordProcessor.getFinishProcessTask();
    }

    public long getTimestampMs() {
        int timestamp = this.mClock.getAudioTimestampUs();
        SLog.i("Recorder Timestamp: {}.", Integer.valueOf(timestamp));
        return timestamp / 1000;
    }

    public boolean wasJustGrabbingDataForSegment() {
        return this.mClock.wasAudioJustRecorded();
    }

    private class ControllerAutoFocusCallback implements Camera.AutoFocusCallback {
        private ControllerAutoFocusCallback() {
        }

        @Override // android.hardware.Camera.AutoFocusCallback
        public void onAutoFocus(boolean success, Camera camera) {
            RecordController.this.mIsFocusing = false;
            RecordController.this.mParent.onAutoFocusComplete(success);
        }
    }

    private class ControllerCameraManagerController implements CameraManager.CameraManagerController {
        private ControllerCameraManagerController() {
        }

        @Override // co.vine.android.recorder.camera.CameraManager.CameraManagerController
        public Camera.PreviewCallback getCameraPreviewCallback() {
            return RecordController.this.mRecordProcessor.getCameraPreviewCallback();
        }

        @Override // co.vine.android.recorder.camera.CameraManager.CameraManagerController
        public void onZoomUpdated(int zoom, boolean stopped) {
            if (RecordController.this.mPluginManager != null) {
                RecordController.this.mPluginManager.onZoomUpdated(zoom, stopped);
            }
        }

        @Override // co.vine.android.recorder.camera.CameraManager.CameraManagerController
        public void onPreviewError(RuntimeException callbackException) {
            if (RecordController.this.mParent != null) {
                RecordController.this.mParent.onPreviewError(callbackException);
                return;
            }
            throw callbackException;
        }
    }
}
