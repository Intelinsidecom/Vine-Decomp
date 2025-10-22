package co.vine.android.recorder.camera;

import android.hardware.Camera;
import co.vine.android.recorder.MasterPreviewCallback;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.SurfaceController;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class PreviewManager {
    private static PreviewManager INSTANCE;
    private volatile boolean isPreviewing;
    private PreviewManagerCallback mManagerCallback;

    public void setManagerCallback(PreviewManagerCallback cb) {
        this.mManagerCallback = cb;
    }

    public void cancelPendingPreviews() {
        PreviewManagerCallback cb = this.mManagerCallback;
        if (cb != null) {
            cb.cancelPendingPreviews();
        }
    }

    private boolean hasCamera() {
        return getCamera() != null;
    }

    private Camera getCamera() {
        return CameraManager.getInstance().getCamera();
    }

    public static PreviewManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PreviewManager();
        }
        return INSTANCE;
    }

    public static class InvalidPreviewSizeException extends Exception {
        public InvalidPreviewSizeException(String message) {
            super(message);
        }
    }

    private int getPreviewWidth() {
        return this.mManagerCallback.getPreviewWidth();
    }

    private int getPreviewHeight() {
        return this.mManagerCallback.getPreviewHeight();
    }

    public void startPreview(CameraManager.CameraManagerController controller, RecordConfigUtils.RecordConfig config, SurfaceController surfaceController, boolean flash, boolean autoFocus, int frameRate, int rotation, int currentDurationMs) throws InvalidPreviewSizeException {
        synchronized (CameraManager.CAMERA_LOCK) {
            try {
            } catch (Exception e) {
                this.isPreviewing = false;
                CrashUtil.log("Failed to startPreview: {}.", e.getMessage());
                SLog.e("Preview failed here.", (Throwable) e);
                if (e instanceof InvalidPreviewSizeException) {
                    throw ((InvalidPreviewSizeException) e);
                }
            }
            if (this.isPreviewing) {
                SLog.d("Do not preview again.");
                return;
            }
            if (hasCamera()) {
                SLog.d("2,3: Set preview display and start preview.");
                if (surfaceController.giveCameraPreview(getCamera())) {
                    this.isPreviewing = true;
                    Camera.Parameters parameters = getCamera().getParameters();
                    CameraManager cm = CameraManager.getInstance();
                    int degree = cm.fixOrientation(rotation);
                    cm.prepareFocusMatrix(getPreviewWidth(), getPreviewHeight(), degree);
                    Object[] objArr = new Object[1];
                    objArr[0] = Boolean.valueOf(degree == 90 || degree == 270);
                    CrashUtil.log("Preview flip: {} ", objArr);
                    if (autoFocus) {
                        cm.startContinuousAutoFocus(parameters);
                    }
                    if (flash) {
                        cm.changeFlashState(true);
                    }
                    this.mManagerCallback.preparePreviewParameters(parameters, config);
                    this.mManagerCallback.startPreview(controller, frameRate, currentDurationMs, config);
                }
            }
        }
    }

    public void waitForPreviewToStart() {
        this.mManagerCallback.waitForPreviewToStart();
    }

    public void releaseCameraAndPreview(String tag) {
        releaseCameraAndPreview(tag, true);
    }

    public void releaseCameraAndPreview(String tag, boolean releaseCallbackFromMaster) {
        this.mManagerCallback.preReleaseCameraAndPreview();
        waitForPreviewToStart();
        synchronized (CameraManager.CAMERA_LOCK) {
            if (hasCamera()) {
                SLog.d("6,7 RELEASE Camera and STOP Preview.");
                if (this.isPreviewing) {
                    stopPreview();
                }
                this.mManagerCallback.onReleaseCameraAndPreview(tag, releaseCallbackFromMaster);
                CameraManager.getInstance().releaseCamera();
            }
        }
    }

    public boolean hasGivenBufferToCurrentCallback() {
        return MasterPreviewCallback.getInstance().hasGivenFirstFrameToCurrentCallback();
    }

    public static void releaseCallback() {
        setPreviewCallback(null);
    }

    public static void setPreviewCallback(CameraManager.CameraManagerController controller) {
        MasterPreviewCallback.getInstance().setCallbackWithBuffer(controller);
    }

    public void stopPreview() {
        synchronized (CameraManager.CAMERA_LOCK) {
            getCamera().stopPreview();
            MasterPreviewCallback.getInstance().onPreviewStopped();
            this.isPreviewing = false;
        }
    }

    public void setPreviewSize(int previewWidth, int previewHeight) {
        this.mManagerCallback.setPreviewSize(previewWidth, previewHeight);
    }

    public boolean isPreviewing() {
        return this.isPreviewing;
    }

    public void releaseResources() {
        this.mManagerCallback.releaseResources();
    }
}
