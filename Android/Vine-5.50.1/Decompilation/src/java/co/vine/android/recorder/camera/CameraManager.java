package co.vine.android.recorder.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.os.Build;
import android.util.SparseArray;
import co.vine.android.VineNotSupportedException;
import co.vine.android.recorder.MasterPreviewCallback;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.MathUtil;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@TargetApi(14)
/* loaded from: classes.dex */
public class CameraManager implements Camera.OnZoomChangeListener {
    public static final int[] CAMERA_LOCK = new int[0];
    private static CameraManager INSTANCE;
    private static final Integer[] sBackFacing;
    private static final SparseArray<Camera.CameraInfo> sCameraInfoTable;
    private static final SparseArray<CameraSetting> sCameraSettingTable;
    private static final Integer[] sFrontFacing;
    private boolean mAutoStopZoomCalled;
    private Camera mCamera;
    private CameraSetting mCameraSetting;
    private int mCurrentZoomTarget;
    private ArrayList<Camera.Area> mFocusArea;
    private boolean mIsSmoothZoomSupported;
    private boolean mIsSmoothZooming;
    private boolean mIsSmoothZoomingStopCalled;
    private int mMaxZoom;
    private int mRequestedZoom;
    private boolean mIsCurrentFront = false;
    private int mCurrentCamera = -1;
    private final Matrix mMatrix = new Matrix();

    public interface CameraManagerController {
        Camera.PreviewCallback getCameraPreviewCallback();

        void onPreviewError(RuntimeException runtimeException);

        void onZoomUpdated(int i, boolean z);
    }

    static {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        ArrayList<Integer> frontFacing = new ArrayList<>();
        ArrayList<Integer> backFacing = new ArrayList<>();
        for (int i = 0; i < numberOfCameras; i++) {
            try {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == 1) {
                    frontFacing.add(Integer.valueOf(i));
                } else if (cameraInfo.facing == 0) {
                    backFacing.add(Integer.valueOf(i));
                }
            } catch (Exception e) {
                CrashUtil.logException(e, "Cannot get camera info for camera #{}.", Integer.valueOf(i));
            }
        }
        sFrontFacing = (Integer[]) frontFacing.toArray(new Integer[frontFacing.size()]);
        sBackFacing = (Integer[]) backFacing.toArray(new Integer[backFacing.size()]);
        sCameraInfoTable = new SparseArray<>();
        sCameraSettingTable = new SparseArray<>();
    }

    public boolean addRecordingHintIfNeeded(Camera.Parameters parameters, RecordConfigUtils.RecordConfig config) {
        boolean setRecordingHint = (this.mIsCurrentFront && config.frontFacingRecordingHint) || (!this.mIsCurrentFront && config.backFacingRecordingHint);
        if (setRecordingHint) {
            parameters.setRecordingHint(true);
        }
        return setRecordingHint;
    }

    public static boolean hasCamera() {
        return hasBackFacingCamera() || hasFrontFacingCamera();
    }

    public static boolean hasFrontFacingCamera() {
        return sFrontFacing.length > 0;
    }

    public static boolean hasBackFacingCamera() {
        return sBackFacing.length > 0;
    }

    private CameraManager() {
    }

    public static synchronized CameraManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CameraManager();
        }
        return INSTANCE;
    }

    public boolean isCameraReady() {
        return (this.mCamera == null || this.mCameraSetting == null) ? false : true;
    }

    public CameraSetting getCameraSetting() {
        return this.mCameraSetting;
    }

    public boolean canChangeFocus() {
        try {
            if (this.mCamera != null) {
                return this.mCamera.getParameters().getMaxNumFocusAreas() > 0;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setFocusAreas(int x, int y) {
        try {
            initializeFocusAreas(x, y);
            Camera.Parameters params = this.mCamera.getParameters();
            params.setFocusAreas(this.mFocusArea);
            this.mCamera.setParameters(params);
            return true;
        } catch (Exception e) {
            CrashUtil.logException(e, "Failed to set focus areas {}, {}.", this.mMatrix, this.mFocusArea.get(0).rect);
            return false;
        }
    }

    private void initializeFocusAreas(int x, int y) {
        if (this.mFocusArea == null) {
            this.mFocusArea = new ArrayList<>();
            this.mFocusArea.add(new Camera.Area(new Rect(), 1));
        }
        calculateTapArea(100, 100, 1.0f, x, y, this.mCameraSetting.originalW, this.mCameraSetting.originalH, this.mFocusArea.get(0).rect);
    }

    private void calculateTapArea(int focusWidth, int focusHeight, float areaMultiple, int x, int y, int previewWidth, int previewHeight, Rect rect) {
        int areaWidth = (int) (focusWidth * areaMultiple);
        int areaHeight = (int) (focusHeight * areaMultiple);
        int left = MathUtil.clamp(x - (areaWidth / 2), 0, previewWidth - areaWidth);
        int top = MathUtil.clamp(y - (areaHeight / 2), 0, previewHeight - areaHeight);
        RectF rectF = new RectF(left, top, left + areaWidth, top + areaHeight);
        this.mMatrix.mapRect(rectF);
        MathUtil.rectFToRect(rectF, rect);
    }

    public Camera.Parameters getParameters() {
        if (this.mCamera != null) {
            return this.mCamera.getParameters();
        }
        return null;
    }

    public boolean stopContinuousFocus(Camera.Parameters params) {
        String supportedMode = "";
        List<String> focusModes = params.getSupportedFocusModes();
        SLog.d("Force auto focus: {}", Boolean.valueOf(RecordConfigUtils.RecordConfig.FORCE_AUTO_FOCUS));
        if (focusModes != null) {
            if (focusModes.contains("auto") || RecordConfigUtils.RecordConfig.FORCE_AUTO_FOCUS) {
                supportedMode = "auto";
                SLog.d("Focus Mode: FOCUS_MODE_AUTO");
            } else if (focusModes.contains("continuous-picture")) {
                supportedMode = "continuous-picture";
                SLog.d("Focus Mode: FOCUS_MODE_CONTINUOUS_PICTURE");
            }
            if (!supportedMode.equals("")) {
                params.setFocusMode(supportedMode);
                this.mCamera.setParameters(params);
                return true;
            }
        }
        return false;
    }

    public boolean autoFocus(Camera.AutoFocusCallback cb) {
        try {
            this.mCamera.autoFocus(cb);
            return true;
        } catch (Exception e) {
            CrashUtil.logException(e, "Failed to autoFocus.", new Object[0]);
            return false;
        }
    }

    public boolean isZoomSupported() {
        return this.mMaxZoom > 1;
    }

    public void printSupportedPreviewSizes() {
        if (this.mCamera != null) {
            Camera.Parameters param = this.mCamera.getParameters();
            Camera.Size preferred = param.getPreferredPreviewSizeForVideo();
            if (preferred != null) {
                CrashUtil.log("Preferred size: {} * {}", Integer.valueOf(preferred.width), Integer.valueOf(preferred.height));
            }
            List<Camera.Size> sizes = this.mCamera.getParameters().getSupportedPreviewSizes();
            if (sizes != null) {
                for (Camera.Size size : sizes) {
                    CrashUtil.log("Supported Preview Size: {} * {}.", Integer.valueOf(size.width), Integer.valueOf(size.height));
                }
            }
        }
    }

    public boolean isSmoothZoomingSupported() {
        return this.mIsSmoothZoomSupported;
    }

    public boolean canSwitchFlash() {
        boolean z = false;
        synchronized (CAMERA_LOCK) {
            if (this.mCamera != null) {
                try {
                    List<String> modes = this.mCamera.getParameters().getSupportedFlashModes();
                    if (modes != null) {
                        for (String mode : modes) {
                            if ("torch".equals(mode)) {
                                z = true;
                                break;
                            }
                        }
                    }
                } catch (RuntimeException e) {
                }
            }
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x000e A[Catch: all -> 0x0097, TRY_LEAVE, TryCatch #1 {, blocks: (B:9:0x000a, B:13:0x001a, B:15:0x001e, B:34:0x009a, B:18:0x0054, B:20:0x0067, B:21:0x0070, B:23:0x0074, B:26:0x007b, B:28:0x0081, B:29:0x0094, B:38:0x00a4, B:41:0x00a9, B:17:0x0022, B:37:0x00a1, B:11:0x000e), top: B:46:0x000a, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0067 A[Catch: all -> 0x0097, Exception -> 0x00a0, TryCatch #0 {Exception -> 0x00a0, blocks: (B:13:0x001a, B:15:0x001e, B:34:0x009a, B:18:0x0054, B:20:0x0067, B:21:0x0070, B:23:0x0074, B:26:0x007b, B:28:0x0081, B:41:0x00a9, B:17:0x0022), top: B:44:0x001a, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0081 A[Catch: all -> 0x0097, Exception -> 0x00a0, TRY_LEAVE, TryCatch #0 {Exception -> 0x00a0, blocks: (B:13:0x001a, B:15:0x001e, B:34:0x009a, B:18:0x0054, B:20:0x0067, B:21:0x0070, B:23:0x0074, B:26:0x007b, B:28:0x0081, B:41:0x00a9, B:17:0x0022), top: B:44:0x001a, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private co.vine.android.recorder.camera.CameraSetting safeCameraOpen(co.vine.android.recorder.RecordConfigUtils.RecordConfig r9, boolean r10, int r11, int r12, boolean r13) {
        /*
            r8 = this;
            r4 = -1
            if (r11 != r4) goto L5
            r4 = 0
        L4:
            return r4
        L5:
            int[] r5 = co.vine.android.recorder.camera.CameraManager.CAMERA_LOCK
            monitor-enter(r5)
            if (r13 != 0) goto Le
            int r4 = r8.mCurrentCamera     // Catch: java.lang.Throwable -> L97
            if (r4 == r11) goto L18
        Le:
            co.vine.android.recorder.camera.PreviewManager r4 = co.vine.android.recorder.camera.PreviewManager.getInstance()     // Catch: java.lang.Throwable -> L97
            java.lang.String r6 = "safeCameraOpen"
            r7 = 0
            r4.releaseCameraAndPreview(r6, r7)     // Catch: java.lang.Throwable -> L97
        L18:
            if (r13 != 0) goto L22
            android.hardware.Camera r4 = r8.mCamera     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            if (r4 == 0) goto L22
            int r4 = r8.mCurrentCamera     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            if (r4 == r11) goto L9a
        L22:
            long r2 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            android.hardware.Camera r4 = android.hardware.Camera.open(r11)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r8.mCamera = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            co.vine.android.recorder.MasterPreviewCallback r4 = co.vine.android.recorder.MasterPreviewCallback.getInstance()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            android.hardware.Camera r6 = r8.mCamera     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r4.onCameraOpened(r6)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            android.hardware.Camera r4 = r8.mCamera     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r4.setZoomChangeListener(r8)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            java.lang.String r4 = "Open camera took {}ms."
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            long r6 = r6 - r2
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            com.edisonwang.android.slog.SLog.d(r4, r6)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r4 = 0
            r8.mIsSmoothZooming = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r4 = 0
            r8.mAutoStopZoomCalled = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r4 = 0
            r8.mIsSmoothZoomingStopCalled = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r4 = -1
            r8.mCurrentZoomTarget = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
        L54:
            r4 = 0
            r8.mCameraSetting = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            android.hardware.Camera r4 = r8.mCamera     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            android.hardware.Camera$Parameters r1 = r4.getParameters()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            int r4 = r1.getMaxZoom()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r8.mMaxZoom = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            boolean r4 = co.vine.android.recorder.RecordConfigUtils.RecordConfig.IS_ZERO_INDEX_BASED_ZOOM     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            if (r4 == 0) goto La8
            int r4 = r8.mMaxZoom     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            int r4 = r4 + (-1)
            r8.mMaxZoom = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r4 = 0
            r8.mRequestedZoom = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
        L70:
            boolean r4 = co.vine.android.recorder.RecordConfigUtils.RecordConfig.SHOULD_FORCE_SMOOTH_ZOOM     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            if (r4 != 0) goto L7a
            boolean r4 = r1.isSmoothZoomSupported()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            if (r4 == 0) goto Lac
        L7a:
            r4 = 1
        L7b:
            r8.mIsSmoothZoomSupported = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            android.hardware.Camera r4 = r8.mCamera     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            if (r4 == 0) goto La4
            java.lang.String r4 = "1. Open Camera."
            com.edisonwang.android.slog.SLog.d(r4)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r8.mCurrentCamera = r11     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r8.mIsCurrentFront = r10     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            int r4 = r8.mCurrentCamera     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            co.vine.android.recorder.camera.CameraSetting r4 = getCameraInfo(r9, r4, r1, r12, r10)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            r8.mCameraSetting = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            co.vine.android.recorder.camera.CameraSetting r4 = r8.mCameraSetting     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L97
            goto L4
        L97:
            r4 = move-exception
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L97
            throw r4
        L9a:
            java.lang.String r4 = "Camera was already opened."
            com.edisonwang.android.slog.SLog.d(r4)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            goto L54
        La0:
            r0 = move-exception
            co.vine.android.util.CrashUtil.logException(r0)     // Catch: java.lang.Throwable -> L97
        La4:
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L97
            r4 = 0
            goto L4
        La8:
            r4 = 1
            r8.mRequestedZoom = r4     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> La0
            goto L70
        Lac:
            r4 = 0
            goto L7b
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.camera.CameraManager.safeCameraOpen(co.vine.android.recorder.RecordConfigUtils$RecordConfig, boolean, int, int, boolean):co.vine.android.recorder.camera.CameraSetting");
    }

    public Camera getCamera() {
        return this.mCamera;
    }

    public static int getCameraRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case 0:
            default:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
        }
    }

    public static CameraSetting getCameraInfo(RecordConfigUtils.RecordConfig config, int cameraId, Camera.Parameters params, int rotation, boolean isFront) throws VineNotSupportedException {
        int videoFrameHeight;
        int videoFrameWidth;
        int videoFrameRate;
        CamcorderProfile cp;
        int degree = getCameraDisplayOrientation(rotation, cameraId);
        if (isFront) {
            degree = -degree;
        }
        CameraSetting cameraSetting = sCameraSettingTable.get(cameraId);
        if (cameraSetting != null) {
            CameraSetting cameraSetting2 = new CameraSetting(cameraSetting);
            cameraSetting2.degrees = degree;
            SLog.i("Using cached camera setting.");
            return cameraSetting2;
        }
        List<Integer> previewFormats = null;
        try {
            previewFormats = params.getSupportedPreviewFormats();
        } catch (NullPointerException e) {
            CrashUtil.log("This device sucks, it cannot return params properly.");
        }
        int selectedFormat = 17;
        if (previewFormats != null) {
            Iterator<Integer> it = previewFormats.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Integer format = it.next();
                SLog.d("Supported format: {}", format);
                if (format.intValue() == 4) {
                    selectedFormat = format.intValue();
                    break;
                }
            }
            if (selectedFormat == 17) {
                Iterator<Integer> it2 = previewFormats.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    Integer format2 = it2.next();
                    if (format2.intValue() == 256) {
                        selectedFormat = format2.intValue();
                        break;
                    }
                }
            }
        }
        SLog.d("Selected format: {}.", Integer.valueOf(selectedFormat));
        try {
            if (CamcorderProfile.hasProfile(cameraId, 4)) {
                cp = CamcorderProfile.get(cameraId, 4);
                CrashUtil.log("Use default profile");
            } else {
                cp = CamcorderProfile.get(cameraId, 1);
            }
            SLog.d("Preferred FrameRate: {}.", Integer.valueOf(cp.videoFrameRate));
            if (cp.videoFrameRate >= config.targetFrameRate) {
                cp.videoFrameRate = config.targetFrameRate;
            }
            CrashUtil.log("Original Width * Height: {}/{}", Integer.valueOf(cp.videoFrameWidth), Integer.valueOf(cp.videoFrameHeight));
            cp.videoFrameWidth = config.previewWidth;
            if (cp.videoFrameHeight > cp.videoFrameWidth) {
                cp.videoFrameHeight = config.highQuality ? 480 : 240;
            }
            SLog.d("Video: {}*{} codec: {} bitRate:{} frameRate:{}.", new Object[]{Integer.valueOf(cp.videoFrameWidth), Integer.valueOf(cp.videoFrameHeight), Integer.valueOf(cp.videoCodec), Integer.valueOf(cp.videoBitRate), Integer.valueOf(cp.videoFrameRate)});
            SLog.d("Audio: bitRate:{} channels: {} codec: {} sampleRate: {} ", new Object[]{Integer.valueOf(cp.audioBitRate), Integer.valueOf(cp.audioChannels), Integer.valueOf(cp.audioCodec), Integer.valueOf(cp.audioSampleRate)});
            videoFrameWidth = cp.videoFrameWidth;
            videoFrameHeight = cp.videoFrameHeight;
            videoFrameRate = cp.videoFrameRate;
        } catch (Exception e2) {
            List<Camera.Size> sizes = params.getSupportedPreviewSizes();
            Set<Integer> supportedWidth = new HashSet<>();
            if (sizes != null) {
                for (Camera.Size size : sizes) {
                    CrashUtil.log("Supported width and height: {}, {}.", Integer.valueOf(size.width), Integer.valueOf(size.height));
                    if (size.height == config.targetSize) {
                        supportedWidth.add(Integer.valueOf(size.width));
                    }
                }
                if (supportedWidth.isEmpty()) {
                    CrashUtil.log("Edison: Target size is not supported.");
                    videoFrameHeight = config.previewHeight;
                    videoFrameWidth = config.previewWidth;
                } else if (supportedWidth.contains(Integer.valueOf(config.previewWidth))) {
                    videoFrameHeight = config.previewHeight;
                    videoFrameWidth = config.previewWidth;
                } else {
                    CrashUtil.log("Edison: Target width is not supported.");
                    videoFrameHeight = config.previewHeight;
                    videoFrameWidth = config.previewWidth;
                }
            } else {
                videoFrameHeight = config.previewHeight;
                videoFrameWidth = config.previewWidth;
            }
            videoFrameRate = config.targetFrameRate;
        } catch (UnsatisfiedLinkError e3) {
            throw new VineNotSupportedException(e3);
        }
        SLog.d("FFC OR: {}.", Float.valueOf(config.frontFacingAspectRatioOverride));
        SLog.d("BFC OR: {}.", Float.valueOf(config.backFacingAspectRatioOverride));
        if (videoFrameWidth == 640 && videoFrameHeight != 480) {
            CrashUtil.log("Force 480 with 480p.");
            videoFrameHeight = 480;
        }
        CameraSetting cameraSetting3 = new CameraSetting(videoFrameWidth, videoFrameHeight, degree, videoFrameRate, selectedFormat, isFront, config.frontFacingAspectRatioOverride, config.backFacingAspectRatioOverride);
        sCameraSettingTable.put(cameraId, cameraSetting3);
        CameraSetting cameraSetting4 = new CameraSetting(cameraSetting3);
        if (SLog.sLogsOn) {
            Camera.Size size2 = params.getPreferredPreviewSizeForVideo();
            if (size2 != null) {
                SLog.d("Preferred Width * Height: {}/{}.", Integer.valueOf(size2.width), Integer.valueOf(size2.height));
            }
            List<Camera.Size> sizes2 = params.getSupportedVideoSizes();
            if (sizes2 != null) {
                for (Camera.Size s : sizes2) {
                    SLog.d("Supported Video Width * Height: {}/{}.", Integer.valueOf(s.width), Integer.valueOf(s.height));
                }
            }
            List<Camera.Size> sizes3 = params.getSupportedPreviewSizes();
            if (sizes3 != null) {
                for (Camera.Size s2 : sizes3) {
                    SLog.d("Supported Preview Width * Height: {}/{}.", Integer.valueOf(s2.width), Integer.valueOf(s2.height));
                }
            }
            SLog.d("Camera w/h/f/p/d: {}/{}/{}/{}/{}", new Object[]{Integer.valueOf(cameraSetting4.originalW), Integer.valueOf(cameraSetting4.originalH), Integer.valueOf(cameraSetting4.fps), Integer.valueOf(params.getPreviewFormat()), Integer.valueOf(cameraSetting4.degrees)});
        }
        return cameraSetting4;
    }

    public int getDefaultCameraId(boolean frontFacing) {
        if (frontFacing) {
            if (hasFrontFacingCamera()) {
                return sFrontFacing[0].intValue();
            }
            return -1;
        }
        if (hasBackFacingCamera()) {
            return sBackFacing[0].intValue();
        }
        return -1;
    }

    public CameraSetting openDefaultCamera(RecordConfigUtils.RecordConfig config, boolean frontFacing, int rotation, boolean reOpen) {
        return safeCameraOpen(config, frontFacing, getDefaultCameraId(frontFacing), rotation, reOpen);
    }

    public boolean startContinuousAutoFocus(Camera.Parameters params) {
        String supportedMode = "";
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes == null) {
            return false;
        }
        if (RecordConfigUtils.RecordConfig.FORCE_AUTO_FOCUS) {
            supportedMode = "auto";
            SLog.d("Focus Mode: AUTO");
        } else if (focusModes.contains("continuous-video")) {
            supportedMode = "continuous-video";
            SLog.d("Focus Mode: CONTINUOUS VIDEO");
        } else if (focusModes.contains("infinity")) {
            supportedMode = "infinity";
            SLog.d("Focus Mode: INFINITY");
        }
        String originalFocusMode = params.getFocusMode();
        try {
            if (supportedMode.equals("")) {
                return false;
            }
            params.setFocusMode(supportedMode);
            this.mCamera.setParameters(params);
            return true;
        } catch (Exception e) {
            CrashUtil.logException(e, "Continuous Auto Focus is not supported on this device.", new Object[0]);
            params.setFocusMode(originalFocusMode);
            return false;
        }
    }

    @TargetApi(15)
    public void disableVideoStab(Camera.Parameters parameters) {
        if (Build.VERSION.SDK_INT >= 15 && parameters.isVideoStabilizationSupported()) {
            parameters.setVideoStabilization(false);
        }
    }

    public int setFrameRate(int frameRate) {
        synchronized (CAMERA_LOCK) {
            if (this.mCamera != null) {
                try {
                    Camera.Parameters parameters = this.mCamera.getParameters();
                    parameters.setPreviewFpsRange(frameRate * 1000, frameRate * 1000);
                    SLog.d("SET frameRate using fixed-range frameRate");
                    this.mCamera.setParameters(parameters);
                    return frameRate;
                } catch (Exception e) {
                    try {
                        Camera.Parameters parameters2 = this.mCamera.getParameters();
                        parameters2.setPreviewFrameRate(frameRate);
                        this.mCamera.setParameters(parameters2);
                        SLog.d("SET frameRate using fixed frameRate");
                        return frameRate;
                    } catch (Exception e2) {
                        try {
                            Camera.Parameters parameters3 = this.mCamera.getParameters();
                            try {
                                List<int[]> ss = parameters3.getSupportedPreviewFpsRange();
                                if (ss == null) {
                                    CrashUtil.log("Cannot set frame-rate");
                                    return frameRate;
                                }
                                int frameRate2 = frameRate * 1000;
                                int[] selected = null;
                                int lowest = 0;
                                int highest = 0;
                                int reachedIndex = -1;
                                int otherIndex = ss.size() - 1;
                                int i = 0;
                                while (true) {
                                    if (i >= ss.size()) {
                                        break;
                                    }
                                    int[] s = ss.get(i);
                                    SLog.d("Supported FPS: {}", s);
                                    if (s[0] == frameRate2) {
                                        selected = s;
                                        break;
                                    }
                                    if (s[1] == frameRate2) {
                                        if (lowest < s[0]) {
                                            lowest = s[0];
                                            reachedIndex = i;
                                        }
                                        if (highest <= s[1]) {
                                            highest = s[1];
                                        }
                                    } else if (lowest <= s[0]) {
                                        lowest = s[0];
                                        if (highest <= s[1]) {
                                            highest = s[1];
                                            otherIndex = i;
                                        }
                                    }
                                    i++;
                                }
                                if (selected == null) {
                                    if (reachedIndex != -1) {
                                        int[] selected2 = ss.get(reachedIndex);
                                        selected = selected2;
                                    } else {
                                        int[] selected3 = ss.get(otherIndex);
                                        selected = selected3;
                                    }
                                }
                                if (selected[1] >= frameRate2) {
                                    try {
                                        SLog.d("Try Using variable FPS: {}, {}.", Integer.valueOf(lowest), Integer.valueOf(frameRate2));
                                        parameters3.setPreviewFpsRange(lowest, frameRate2);
                                        this.mCamera.setParameters(parameters3);
                                        return frameRate2 / 1000;
                                    } catch (Exception e3) {
                                        try {
                                            Camera.Parameters parameters4 = this.mCamera.getParameters();
                                            SLog.d("Try Using fixed framerate FPS: {}", Integer.valueOf(frameRate2));
                                            parameters4.setPreviewFrameRate(frameRate2 / 1000);
                                            this.mCamera.setParameters(parameters4);
                                            return frameRate2 / 1000;
                                        } catch (Exception e4) {
                                            Camera.Parameters parameters5 = this.mCamera.getParameters();
                                            SLog.d("Try Using selected framerate FPS: {}, {}, {}.", Integer.valueOf(selected[0]), Integer.valueOf(selected[1]), Integer.valueOf(frameRate2));
                                            parameters5.setPreviewFpsRange(selected[0], selected[1]);
                                            this.mCamera.setParameters(parameters5);
                                            return selected[1] / 1000;
                                        }
                                    }
                                }
                                SLog.d("Fall back, should never reach here.");
                                int frameRate3 = frameRate2 / 1000;
                                parameters3.setPreviewFrameRate(frameRate3);
                                this.mCamera.setParameters(parameters3);
                                return frameRate3;
                            } catch (Exception e5) {
                                return 30;
                            }
                        } catch (Exception e6) {
                            return 30;
                        }
                    }
                }
            }
            return 30;
        }
    }

    protected int fixOrientation(int rotation) {
        int fixedDegrees;
        synchronized (CAMERA_LOCK) {
            fixedDegrees = 0;
            if (this.mCamera != null) {
                fixedDegrees = getCameraDisplayOrientation(rotation, this.mCurrentCamera);
                this.mCamera.setDisplayOrientation(fixedDegrees);
                SLog.d("Fixed orientation: {}", Integer.valueOf(fixedDegrees));
            }
        }
        return fixedDegrees;
    }

    protected void prepareFocusMatrix(int width, int height, int fixedDegrees) {
        if (width != 0 && height != 0) {
            Matrix matrix = new Matrix();
            MathUtil.prepareMatrix(matrix, this.mIsCurrentFront, fixedDegrees, width, height);
            matrix.invert(this.mMatrix);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x000b A[Catch: all -> 0x0023, TryCatch #0 {, blocks: (B:5:0x0005, B:11:0x001e, B:7:0x000b, B:10:0x0016), top: B:17:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void changeFlashState(boolean r4) {
        /*
            r3 = this;
            int[] r2 = co.vine.android.recorder.camera.CameraManager.CAMERA_LOCK
            monitor-enter(r2)
            if (r4 == 0) goto Lb
            boolean r1 = r3.canSwitchFlash()     // Catch: java.lang.Throwable -> L23
            if (r1 == 0) goto L1e
        Lb:
            android.hardware.Camera r1 = r3.mCamera     // Catch: java.lang.Throwable -> L23
            android.hardware.Camera$Parameters r0 = r1.getParameters()     // Catch: java.lang.Throwable -> L23
            if (r4 == 0) goto L20
            java.lang.String r1 = "torch"
        L16:
            r0.setFlashMode(r1)     // Catch: java.lang.Throwable -> L23
            android.hardware.Camera r1 = r3.mCamera     // Catch: java.lang.Throwable -> L23
            r1.setParameters(r0)     // Catch: java.lang.Throwable -> L23
        L1e:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L23
            return
        L20:
            java.lang.String r1 = "off"
            goto L16
        L23:
            r1 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L23
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.camera.CameraManager.changeFlashState(boolean):void");
    }

    public static synchronized int getCameraDisplayOrientation(int degrees, int cameraId) {
        int result;
        Camera.CameraInfo info = sCameraInfoTable.get(cameraId);
        if (info == null) {
            info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            sCameraInfoTable.put(cameraId, info);
        } else {
            SLog.i("Using cached camera info.");
        }
        SLog.d("Set rotation to {} degrees", Integer.valueOf(degrees));
        if (info.facing == 1) {
            int result2 = (info.orientation + degrees) % 360;
            result = (360 - result2) % 360;
        } else {
            result = ((info.orientation - degrees) + 360) % 360;
        }
        return result;
    }

    public int modifyZoom(boolean zoomIn) {
        int newZoom;
        if (this.mIsSmoothZoomSupported) {
            newZoom = zoomIn ? this.mMaxZoom : 0;
        } else {
            newZoom = zoomIn ? this.mRequestedZoom + 1 : this.mRequestedZoom - 1;
        }
        if (RecordConfigUtils.RecordConfig.IS_ZERO_INDEX_BASED_ZOOM) {
            newZoom++;
        }
        return modifyZoom(newZoom);
    }

    public void startSmoothZoom(boolean zoomIn) {
        if (!this.mIsSmoothZoomSupported) {
            throw new IllegalStateException("Smooth zoom not supported.");
        }
        if (!this.mIsSmoothZooming) {
            SLog.d("Smooth zoom started on value {}", Integer.valueOf(this.mRequestedZoom));
            stopContinuousFocus(this.mCamera.getParameters());
            this.mIsSmoothZooming = true;
            this.mCurrentZoomTarget = this.mRequestedZoom;
            if (zoomIn) {
                this.mCamera.startSmoothZoom(this.mMaxZoom);
            } else {
                this.mCamera.startSmoothZoom(0);
            }
        }
    }

    public void startInstantZoom() {
        SLog.d("Instant zoom started on value {}", Integer.valueOf(this.mRequestedZoom));
        this.mCurrentZoomTarget = this.mRequestedZoom;
        Camera.Parameters params = this.mCamera.getParameters();
        params.setZoom(this.mRequestedZoom);
        this.mCamera.setParameters(params);
        onZoomChange(this.mRequestedZoom, true, this.mCamera);
    }

    public int getMaxZoom() {
        return this.mMaxZoom;
    }

    @Override // android.hardware.Camera.OnZoomChangeListener
    public void onZoomChange(int zoomValue, boolean stopped, Camera camera) {
        if (stopped) {
            SLog.d("Zoom stopped on value {} (target {}, queued value: {}).", Integer.valueOf(zoomValue), Integer.valueOf(this.mCurrentZoomTarget), Integer.valueOf(this.mRequestedZoom));
            this.mIsSmoothZooming = false;
            if (this.mCamera != null) {
                try {
                    startContinuousAutoFocus(this.mCamera.getParameters());
                } catch (RuntimeException e) {
                }
            }
            if (this.mIsSmoothZoomingStopCalled) {
                this.mIsSmoothZoomingStopCalled = false;
                if (this.mRequestedZoom == this.mCurrentZoomTarget) {
                    this.mRequestedZoom = zoomValue;
                }
            }
            if (zoomValue != this.mRequestedZoom && this.mIsSmoothZoomSupported && this.mCamera != null) {
                startSmoothZoom(this.mRequestedZoom >= zoomValue);
            }
        } else {
            SLog.d("Zoom updated on value {} (queued value: {}).", Integer.valueOf(zoomValue), Integer.valueOf(this.mRequestedZoom));
            if (this.mCurrentZoomTarget == zoomValue) {
                SLog.d("Stop zoom on reaching target.");
                this.mAutoStopZoomCalled = stopSmoothZoom();
            }
        }
        MasterPreviewCallback.getInstance().onZoomUpdated(RecordConfigUtils.RecordConfig.IS_ZERO_INDEX_BASED_ZOOM ? zoomValue + 1 : zoomValue, stopped && zoomValue == this.mRequestedZoom);
        if (stopped) {
            this.mAutoStopZoomCalled = false;
        }
    }

    public boolean stopSmoothZoom() {
        if (this.mIsSmoothZooming && !this.mIsSmoothZoomingStopCalled) {
            this.mIsSmoothZoomingStopCalled = true;
            SLog.d("Stop smooth zoom.");
            try {
                this.mCamera.stopSmoothZoom();
                return true;
            } catch (Exception e) {
                CrashUtil.log("Failed to stop smooth zoom.");
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0039 A[Catch: all -> 0x004b, DONT_GENERATE, TRY_ENTER, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0004, B:6:0x0008, B:8:0x000d, B:9:0x0010, B:11:0x0014, B:14:0x001a, B:17:0x001f, B:18:0x0021, B:20:0x0036, B:24:0x003d, B:27:0x0042, B:21:0x0039), top: B:31:0x0004, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int modifyZoom(int r8) {
        /*
            r7 = this;
            r2 = 0
            int[] r4 = co.vine.android.recorder.camera.CameraManager.CAMERA_LOCK
            monitor-enter(r4)
            android.hardware.Camera r3 = r7.mCamera     // Catch: java.lang.Throwable -> L4b
            if (r3 == 0) goto L39
            r1 = 0
            boolean r3 = co.vine.android.recorder.RecordConfigUtils.RecordConfig.IS_ZERO_INDEX_BASED_ZOOM     // Catch: java.lang.Throwable -> L4b
            if (r3 == 0) goto L10
            int r8 = r8 + (-1)
            r1 = -1
        L10:
            int r3 = r7.mRequestedZoom     // Catch: java.lang.Throwable -> L4b
            if (r8 == r3) goto L39
            int r3 = r7.mMaxZoom     // Catch: java.lang.Throwable -> L4b
            if (r8 > r3) goto L39
            if (r8 <= r1) goto L39
            int r3 = r7.mRequestedZoom     // Catch: java.lang.Throwable -> L4b
            if (r8 <= r3) goto L1f
            r2 = 1
        L1f:
            r7.mRequestedZoom = r8     // Catch: java.lang.Throwable -> L4b
            java.lang.String r3 = "Modifying waiting to zoom to {} using smooth? {}."
            int r5 = r7.mRequestedZoom     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4b
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4b
            boolean r6 = r7.mIsSmoothZoomSupported     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4b
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4b
            com.edisonwang.android.slog.SLog.d(r3, r5, r6)     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4b
            boolean r3 = r7.mIsSmoothZoomSupported     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4b
            if (r3 == 0) goto L3d
            r7.startSmoothZoom(r2)     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4b
        L39:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L4b
            int r3 = r7.mRequestedZoom
            return r3
        L3d:
            r7.startInstantZoom()     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4b
            goto L39
        L41:
            r0 = move-exception
            java.lang.String r3 = "It just didn't work."
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L4b
            co.vine.android.util.CrashUtil.logException(r0, r3, r5)     // Catch: java.lang.Throwable -> L4b
            goto L39
        L4b:
            r3 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L4b
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.camera.CameraManager.modifyZoom(int):int");
    }

    public void modifyExposure(boolean up) {
        synchronized (CAMERA_LOCK) {
            if (this.mCamera != null) {
                try {
                    Camera.Parameters params = this.mCamera.getParameters();
                    int current = params.getExposureCompensation();
                    int max = params.getMaxExposureCompensation();
                    int min = params.getMinExposureCompensation();
                    double step = (max - min) / 10.0d;
                    if (up) {
                        int val = (int) Math.floor(current + step);
                        if (val < max) {
                            max = val;
                        }
                        params.setExposureCompensation(max);
                    } else {
                        int val2 = (int) Math.floor(current - step);
                        if (val2 > min) {
                            min = val2;
                        }
                        params.setExposureCompensation(min);
                    }
                    this.mCamera.setParameters(params);
                } catch (Exception e) {
                    SLog.d("It just didn't work.");
                }
            }
        }
    }

    private String getNextParamValue(boolean up, List<String> supported, String current) {
        int i;
        if (supported != null) {
            int i2 = 0;
            while (i2 < supported.size() && !supported.get(i2).equals(current)) {
                i2++;
            }
            if (up) {
                i = i2 + 1;
                if (i >= supported.size()) {
                    i = supported.size() - 1;
                }
            } else {
                i = i2 - 1;
                if (i < 0) {
                    i = 0;
                }
            }
            SLog.d("Modify to: {}", supported.get(i));
            return supported.get(i);
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0023 A[Catch: all -> 0x002c, DONT_GENERATE, TRY_ENTER, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0007, B:8:0x001b, B:9:0x0023, B:12:0x0026), top: B:18:0x0003, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void modifyWhiteBalance(boolean r7) {
        /*
            r6 = this;
            int[] r4 = co.vine.android.recorder.camera.CameraManager.CAMERA_LOCK
            monitor-enter(r4)
            android.hardware.Camera r3 = r6.mCamera     // Catch: java.lang.Throwable -> L2c
            if (r3 == 0) goto L23
            android.hardware.Camera r3 = r6.mCamera     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            android.hardware.Camera$Parameters r2 = r3.getParameters()     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            java.util.List r3 = r2.getSupportedWhiteBalance()     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            java.lang.String r5 = r2.getWhiteBalance()     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            java.lang.String r1 = r6.getNextParamValue(r7, r3, r5)     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            if (r1 == 0) goto L23
            r2.setWhiteBalance(r1)     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            android.hardware.Camera r3 = r6.mCamera     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            r3.setParameters(r2)     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
        L23:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L2c
            return
        L25:
            r0 = move-exception
            java.lang.String r3 = "It just didn't work."
            com.edisonwang.android.slog.SLog.d(r3)     // Catch: java.lang.Throwable -> L2c
            goto L23
        L2c:
            r3 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L2c
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.camera.CameraManager.modifyWhiteBalance(boolean):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0023 A[Catch: all -> 0x002c, DONT_GENERATE, TRY_ENTER, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0007, B:8:0x001b, B:9:0x0023, B:12:0x0026), top: B:18:0x0003, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void modifySceneMode(boolean r7) {
        /*
            r6 = this;
            int[] r4 = co.vine.android.recorder.camera.CameraManager.CAMERA_LOCK
            monitor-enter(r4)
            android.hardware.Camera r3 = r6.mCamera     // Catch: java.lang.Throwable -> L2c
            if (r3 == 0) goto L23
            android.hardware.Camera r3 = r6.mCamera     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            android.hardware.Camera$Parameters r2 = r3.getParameters()     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            java.util.List r3 = r2.getSupportedSceneModes()     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            java.lang.String r5 = r2.getSceneMode()     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            java.lang.String r1 = r6.getNextParamValue(r7, r3, r5)     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            if (r1 == 0) goto L23
            r2.setSceneMode(r1)     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            android.hardware.Camera r3 = r6.mCamera     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
            r3.setParameters(r2)     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L2c
        L23:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L2c
            return
        L25:
            r0 = move-exception
            java.lang.String r3 = "It just didn't work."
            com.edisonwang.android.slog.SLog.d(r3)     // Catch: java.lang.Throwable -> L2c
            goto L23
        L2c:
            r3 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L2c
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.camera.CameraManager.modifySceneMode(boolean):void");
    }

    public void modifyColorEffects(boolean up) {
        synchronized (CAMERA_LOCK) {
            if (this.mCamera != null) {
                try {
                    Camera.Parameters params = this.mCamera.getParameters();
                    String next = getNextParamValue(up, params.getSupportedColorEffects(), params.getColorEffect());
                    if (next != null) {
                        params.setColorEffect(next);
                        this.mCamera.setParameters(params);
                    }
                    this.mCamera.setParameters(params);
                } catch (Exception e) {
                    SLog.d("It just didn't work.");
                }
            }
        }
    }

    public void modifyAntiBanding(boolean up) {
        synchronized (CAMERA_LOCK) {
            if (this.mCamera != null) {
                try {
                    Camera.Parameters params = this.mCamera.getParameters();
                    String next = getNextParamValue(up, params.getSupportedAntibanding(), params.getAntibanding());
                    if (next != null) {
                        params.setAntibanding(next);
                        this.mCamera.setParameters(params);
                    }
                    this.mCamera.setParameters(params);
                } catch (Exception e) {
                    SLog.d("It just didn't work.");
                }
            }
        }
    }

    @TargetApi(15)
    public void switchImageStabilization() {
        synchronized (CAMERA_LOCK) {
            if (Build.VERSION.SDK_INT >= 15 && this.mCamera != null) {
                try {
                    Camera.Parameters params = this.mCamera.getParameters();
                    if (params.isVideoStabilizationSupported()) {
                        params.setVideoStabilization(!params.getVideoStabilization());
                    }
                    this.mCamera.setParameters(params);
                } catch (Exception e) {
                    SLog.d("It just didn't work.");
                }
            }
        }
    }

    public void releaseCamera() {
        try {
            this.mCamera.release();
        } catch (RuntimeException e) {
            CrashUtil.log("Failed to release camear, probably already released: " + e.getMessage());
        }
        this.mCamera = null;
        this.mCameraSetting = null;
        this.mCurrentCamera = -1;
    }
}
