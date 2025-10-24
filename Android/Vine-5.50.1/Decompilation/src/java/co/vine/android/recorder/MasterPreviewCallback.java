package co.vine.android.recorder;

import android.hardware.Camera;
import co.vine.android.recorder.buffered.BufferedPreviewManagerCallback;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class MasterPreviewCallback implements Camera.PreviewCallback, CameraManager.CameraManagerController {
    private static MasterPreviewCallback INSTANCE;
    private int mCameraBufferCount;
    private boolean mFirstFrameReceived;
    private long mFramesReceived;
    private boolean mHasGivenFirstFrameToCurrentCallback;
    private boolean mHasPendingPreviewFrameError;
    private long mLastNotificationOfPreviewFrameError;
    private long mLastPreviewFrameTimestamp;
    private boolean mMismatchFromOnPreviewFrame;
    private boolean mMistMatchFromBufferAdding;
    private long mPreviewStartTime;
    private WeakReference<CameraManager.CameraManagerController> mCallback = new WeakReference<>(null);
    private WeakReference<Camera> mCamera = new WeakReference<>(null);
    private long mMaxtimeElapsed = -1;

    public static synchronized MasterPreviewCallback getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MasterPreviewCallback();
        }
        return INSTANCE;
    }

    public void setCallbackWithBuffer(CameraManager.CameraManagerController callback) {
        notifyWastedFrameError();
        if (callback != this.mCallback.get()) {
            this.mCallback = new WeakReference<>(callback);
            this.mHasGivenFirstFrameToCurrentCallback = false;
            CrashUtil.log("CameraManager callback state has changed: " + String.valueOf(callback));
        }
    }

    public boolean hasGivenFirstFrameToCurrentCallback() {
        return this.mHasGivenFirstFrameToCurrentCallback;
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.PreviewCallback previewCallback;
        if (this.mCamera.get() != camera) {
            this.mMismatchFromOnPreviewFrame = true;
            CrashUtil.log("Frame is being given from a different camera? {} {}.", this.mCamera.get(), camera);
        }
        if (!this.mFirstFrameReceived) {
            CrashUtil.log("First frame received for camera from master controller: " + camera + "Current callback {}", this.mCallback.get());
            this.mFirstFrameReceived = true;
        }
        if (SLog.sLogsOn) {
            this.mFramesReceived++;
            if (this.mLastPreviewFrameTimestamp != -1) {
                long timeElapsed = System.currentTimeMillis() - this.mLastPreviewFrameTimestamp;
                this.mMaxtimeElapsed = Math.max(timeElapsed, this.mMaxtimeElapsed);
            }
            this.mLastPreviewFrameTimestamp = System.currentTimeMillis();
        }
        this.mCameraBufferCount--;
        CameraManager.CameraManagerController cb = this.mCallback.get();
        if (cb != null) {
            previewCallback = cb.getCameraPreviewCallback();
        } else {
            previewCallback = null;
        }
        if (previewCallback != null) {
            notifyWastedFrameError();
            if (!this.mHasGivenFirstFrameToCurrentCallback) {
                CrashUtil.log("Giving callback {} its first frame.", this.mCallback.get());
            }
            this.mHasGivenFirstFrameToCurrentCallback = true;
            previewCallback.onPreviewFrame(data, camera);
            return;
        }
        if (System.currentTimeMillis() - this.mLastNotificationOfPreviewFrameError > 1000) {
            notifyWastedFrameError();
        } else {
            this.mHasPendingPreviewFrameError = true;
        }
        if (BufferedPreviewManagerCallback.isUsed()) {
            BufferedPreviewManagerCallback pc = BufferedPreviewManagerCallback.getInstance();
            pc.removeBufferFromAvailableQueue(data);
            pc.addCallbackBuffer(data);
        }
    }

    private void notifyWastedFrameError() {
        if (this.mHasPendingPreviewFrameError) {
            CrashUtil.log("Got frame when callback was already released.");
            this.mHasPendingPreviewFrameError = false;
            this.mLastNotificationOfPreviewFrameError = System.currentTimeMillis();
        }
    }

    @Override // co.vine.android.recorder.camera.CameraManager.CameraManagerController
    public Camera.PreviewCallback getCameraPreviewCallback() {
        return this.mCallback.get().getCameraPreviewCallback();
    }

    @Override // co.vine.android.recorder.camera.CameraManager.CameraManagerController
    public void onZoomUpdated(int zoom, boolean stopped) {
        CameraManager.CameraManagerController previewCallback = this.mCallback.get();
        if (previewCallback != null) {
            previewCallback.onZoomUpdated(zoom, stopped);
        }
    }

    @Override // co.vine.android.recorder.camera.CameraManager.CameraManagerController
    public void onPreviewError(RuntimeException callbackException) {
        CameraManager.CameraManagerController previewCallback = this.mCallback.get();
        if (previewCallback != null) {
            previewCallback.onPreviewError(callbackException);
        }
    }

    public String getCallbackStateString() {
        return "\nCallback : " + this.mCallback.get() + "\nCamera frame count: " + this.mCameraBufferCount + "\nMismatch from Buffer Adding? : " + this.mMistMatchFromBufferAdding + "\nMismatch from Preview Frame? : " + this.mMismatchFromOnPreviewFrame + "\nEver got a frame? : " + this.mFirstFrameReceived;
    }

    public void onBufferAdded(Camera camera) {
        if (this.mCamera.get() != camera) {
            this.mMistMatchFromBufferAdding = true;
            CrashUtil.log("Buffer is being added to a different camera? {} {}.", this.mCamera.get(), camera);
        }
        this.mCameraBufferCount++;
    }

    public void onReleaseFromCamera(Camera camera, boolean releaseCallback) {
        CrashUtil.log("Releasing callback from {}, ever received a frame? {}.", camera, Boolean.valueOf(this.mFirstFrameReceived));
        if (releaseCallback) {
            setCallbackWithBuffer(null);
        }
        this.mFirstFrameReceived = false;
        this.mCameraBufferCount = 0;
        this.mCamera = new WeakReference<>(null);
        int avgFrameRate = this.mFramesReceived > 0 ? (int) ((this.mFramesReceived * 1000) / (System.currentTimeMillis() - this.mPreviewStartTime)) : -1;
        if (avgFrameRate > 0) {
            SLog.b("Lowest frame rate: {}.", Long.valueOf(this.mMaxtimeElapsed > 0 ? 1000 / this.mMaxtimeElapsed : this.mMaxtimeElapsed));
            SLog.b("Average frame rate: {}.", Integer.valueOf(avgFrameRate), avgFrameRate > 0 && avgFrameRate < 15);
        }
    }

    public void onCameraOpened(Camera camera) {
        this.mCamera = new WeakReference<>(camera);
        CrashUtil.log("Camera opened: " + camera);
        this.mFirstFrameReceived = false;
    }

    public void onPreviewStarted() {
        CrashUtil.log("Preview started.");
        this.mLastPreviewFrameTimestamp = -1L;
        this.mFramesReceived = 0L;
        this.mPreviewStartTime = System.currentTimeMillis();
    }

    public void onPreviewStopped() {
        CrashUtil.log("Preview stopped.");
        this.mLastPreviewFrameTimestamp = -1L;
    }
}
