package co.vine.android.recorder2.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.opengl.GLSurfaceView;
import android.support.v4.app.NotificationManagerCompat;
import co.vine.android.recorder2.gles.FullFrameRect;
import co.vine.android.util.MathUtil;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CameraController implements SurfaceTexture.OnFrameAvailableListener {
    private float mAspectRatio;
    private Camera mCamera;
    private final Context mContext;
    private GLSurfaceView mPreviewGLSurface;
    private String mContinuousFocusMode = "continuous-video";
    private boolean mUsingFrontFacing = false;
    private boolean mCanFocusAgain = true;
    private boolean mTextureHasBeenSet = false;

    public CameraController(GLSurfaceView previewSurface, Context context) {
        this.mPreviewGLSurface = previewSurface;
        this.mContext = context;
    }

    public void openCamera() {
        if (this.mCamera != null) {
            throw new RuntimeException("camera already initialized");
        }
        this.mTextureHasBeenSet = false;
        Camera.CameraInfo info = new Camera.CameraInfo();
        int numCameras = Camera.getNumberOfCameras();
        int cameraId = -1;
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if ((this.mUsingFrontFacing && info.facing == 1) || (!this.mUsingFrontFacing && info.facing == 0)) {
                this.mCamera = Camera.open(i);
                cameraId = i;
                break;
            }
        }
        if (this.mCamera == null) {
            SLog.d("No front-facing camera found; opening default");
            this.mCamera = Camera.open();
        }
        if (this.mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }
        Camera.Parameters params = this.mCamera.getParameters();
        int[] selectedSize = selectPreviewSize(params, cameraId);
        params.setPreviewSize(selectedSize[0], selectedSize[1]);
        SLog.d("ryango selected size {} {}", Integer.valueOf(selectedSize[0]), Integer.valueOf(selectedSize[1]));
        params.setRecordingHint(true);
        if (params.getSupportedFocusModes().contains("continuous-video")) {
            this.mContinuousFocusMode = "continuous-video";
            params.setFocusMode(this.mContinuousFocusMode);
        } else if (params.getSupportedFocusModes().contains("continuous-picture")) {
            this.mContinuousFocusMode = "continuous-picture";
            params.setFocusMode(this.mContinuousFocusMode);
        }
        int[] selectedRange = choosePreviewFPS(params);
        params.setPreviewFpsRange(selectedRange[0], selectedRange[1]);
        this.mCamera.setParameters(params);
        this.mCamera.setDisplayOrientation(getCameraDisplayOrientation(info));
        this.mAspectRatio = selectedSize[0] / selectedSize[1];
        FullFrameRect.setAspectRatioAndGenerateGlobalTextureCoordinates(this.mAspectRatio);
    }

    private int getCameraDisplayOrientation(Camera.CameraInfo info) {
        int rotation = ((Activity) this.mContext).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
                break;
        }
        if (info.facing == 1) {
            int result = (info.orientation + degrees) % 360;
            return (360 - result) % 360;
        }
        int result2 = ((info.orientation - degrees) + 360) % 360;
        return result2;
    }

    private int[] selectPreviewSize(Camera.Parameters params, int cameraId) {
        List<Camera.Size> supportedSizes;
        Camera.Size size = params.getPreferredPreviewSizeForVideo();
        if (size != null) {
            return new int[]{size.width, size.height};
        }
        try {
            if (CamcorderProfile.hasProfile(cameraId, 5)) {
                CamcorderProfile profile = CamcorderProfile.get(cameraId, 5);
                return new int[]{profile.videoFrameWidth, profile.videoFrameHeight};
            }
        } catch (Exception e) {
        }
        CamcorderProfile profile2 = CamcorderProfile.get(cameraId, 1);
        if (profile2 != null) {
            return new int[]{profile2.videoFrameWidth, profile2.videoFrameHeight};
        }
        List<Camera.Size> supportedSizes2 = params.getSupportedVideoSizes();
        if (supportedSizes2 != null && supportedSizes2.size() > 0) {
            size = supportedSizes2.get(0);
            for (Camera.Size supported : supportedSizes2) {
                if (supported.width > size.width && supported.height > size.height) {
                    size = supported;
                }
            }
        }
        if (size == null && (supportedSizes = params.getSupportedPreviewSizes()) != null && supportedSizes.size() > 0) {
            size = supportedSizes.get(0);
            for (Camera.Size supported2 : supportedSizes) {
                if (supported2.width > size.width && supported2.height > size.height) {
                    size = supported2;
                }
            }
        }
        if (size == null) {
            throw new RuntimeException("No appropriate preview size. Nothing we can really do at this point");
        }
        return new int[]{size.width, size.height};
    }

    private static int[] choosePreviewFPS(Camera.Parameters params) {
        List<int[]> ranges = params.getSupportedPreviewFpsRange();
        int[] selectedRange = null;
        int i = 0;
        while (true) {
            if (i >= ranges.size()) {
                break;
            }
            int[] range = ranges.get(i);
            if (range[0] == 30000 && range[1] == 30000) {
                selectedRange = range;
                break;
            }
            if (range[1] == 30000) {
                selectedRange = range;
            }
            i++;
        }
        if (selectedRange == null) {
            for (int i2 = 0; i2 < ranges.size(); i2++) {
                if (ranges.get(i2)[1] <= 30000) {
                    int[] selectedRange2 = ranges.get(i2);
                    selectedRange = selectedRange2;
                }
            }
        }
        if (selectedRange == null) {
            int[] selectedRange3 = ranges.get(0);
            selectedRange = selectedRange3;
        }
        SLog.d("ryango setting fps {} {}", Integer.valueOf(selectedRange[0]), Integer.valueOf(selectedRange[1]));
        return selectedRange;
    }

    public void releaseCamera() {
        if (this.mCamera != null) {
            this.mTextureHasBeenSet = false;
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
            SLog.d("releaseCamera -- done");
        }
    }

    public void handleSetSurfaceTexture(SurfaceTexture st) throws IOException {
        if (this.mCamera != null) {
            st.setOnFrameAvailableListener(this);
            try {
                this.mCamera.setPreviewTexture(st);
                this.mTextureHasBeenSet = true;
                this.mCamera.startPreview();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture st) {
        this.mPreviewGLSurface.requestRender();
    }

    public boolean toggleCameraToOpen() {
        this.mUsingFrontFacing = !this.mUsingFrontFacing;
        return this.mUsingFrontFacing;
    }

    public boolean canSwitchFlash() {
        if (this.mCamera == null) {
            return false;
        }
        try {
            List<String> modes = this.mCamera.getParameters().getSupportedFlashModes();
            if (modes == null) {
                return false;
            }
            for (String mode : modes) {
                if ("torch".equals(mode)) {
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean toggleFlash() {
        if (!canSwitchFlash()) {
            return false;
        }
        Camera.Parameters parameters = this.mCamera.getParameters();
        boolean flashCurrentlyOn = "torch".equals(parameters.getFlashMode());
        if (flashCurrentlyOn) {
            parameters.setFlashMode("off");
        } else {
            parameters.setFlashMode("torch");
        }
        this.mCamera.setParameters(parameters);
        return !flashCurrentlyOn;
    }

    public void turnOffFlash() {
        if (this.mCamera != null) {
            Camera.Parameters parameters = this.mCamera.getParameters();
            boolean flashCurrentlyOn = "torch".equals(parameters.getFlashMode());
            if (flashCurrentlyOn) {
                parameters.setFlashMode("off");
            }
            this.mCamera.setParameters(parameters);
        }
    }

    private Rect getFocusPointRectFromPreviewCoords(float x, float y) {
        float sensorYRatio;
        float aspectRatioCorrectedPreviewSurfaceHeight = this.mPreviewGLSurface.getHeight() * this.mAspectRatio;
        float sensorXRatio = y / aspectRatioCorrectedPreviewSurfaceHeight;
        if (this.mUsingFrontFacing) {
            sensorYRatio = x / this.mPreviewGLSurface.getWidth();
        } else {
            sensorYRatio = (this.mPreviewGLSurface.getWidth() - x) / this.mPreviewGLSurface.getWidth();
        }
        float sensorX = (sensorXRatio * 2000.0f) - 1000.0f;
        float sensorY = (sensorYRatio * 2000.0f) - 1000.0f;
        int left = MathUtil.clamp(((int) sensorX) - 10, NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, 980);
        int top = MathUtil.clamp(((int) sensorY) - 10, NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, 980);
        return new Rect(left, top, left + 20, top + 20);
    }

    public boolean setFocusPointWithPreviewCoordinates(float x, float y) {
        if (!this.mCanFocusAgain) {
            return false;
        }
        try {
            Rect area = getFocusPointRectFromPreviewCoords(x, y);
            ArrayList<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(area, 1000));
            Camera.Parameters params = this.mCamera.getParameters();
            params.setFocusAreas(focusAreas);
            if (params.getSupportedFocusModes().contains("auto")) {
                params.setFocusMode("auto");
            }
            this.mCamera.setParameters(params);
            this.mCanFocusAgain = false;
            this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: co.vine.android.recorder2.camera.CameraController.1
                @Override // android.hardware.Camera.AutoFocusCallback
                public void onAutoFocus(boolean success, Camera camera) {
                    CameraController.this.mCanFocusAgain = true;
                }
            });
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public void lockFocus() {
        if (this.mContinuousFocusMode.equals(this.mCamera.getParameters().getFocusMode())) {
            SLog.d("ryango autofocusing locked");
            this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: co.vine.android.recorder2.camera.CameraController.2
                @Override // android.hardware.Camera.AutoFocusCallback
                public void onAutoFocus(boolean success, Camera camera) {
                }
            });
        }
    }

    public void unlockFocus() {
        if (this.mContinuousFocusMode.equals(this.mCamera.getParameters().getFocusMode())) {
            SLog.d("ryango autofocusing unlocked");
            this.mCamera.cancelAutoFocus();
        }
    }

    public boolean isUsingFrontFacing() {
        return this.mUsingFrontFacing;
    }

    public boolean isFlashOn() {
        if (!canSwitchFlash()) {
            return false;
        }
        Camera.Parameters parameters = this.mCamera.getParameters();
        return "torch".equals(parameters.getFlashMode());
    }

    public boolean canSwitchCamera() {
        return this.mTextureHasBeenSet;
    }
}
