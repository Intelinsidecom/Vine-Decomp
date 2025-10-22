package co.vine.android.recorder.camera;

import android.hardware.Camera;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.recorder.camera.PreviewManager;

/* loaded from: classes.dex */
public interface PreviewManagerCallback {
    void cancelPendingPreviews();

    int getPreviewHeight();

    int getPreviewWidth();

    void onReleaseCameraAndPreview(String str, boolean z);

    void preReleaseCameraAndPreview();

    void preparePreviewParameters(Camera.Parameters parameters, RecordConfigUtils.RecordConfig recordConfig) throws PreviewManager.InvalidPreviewSizeException;

    void releaseResources();

    void setPreviewSize(int i, int i2);

    void startPreview(CameraManager.CameraManagerController cameraManagerController, int i, int i2, RecordConfigUtils.RecordConfig recordConfig);

    void waitForPreviewToStart();
}
