package co.vine.android.recorder.buffered;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import co.vine.android.recorder.MasterPreviewCallback;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.recorder.camera.PreviewManager;
import co.vine.android.recorder.camera.PreviewManagerCallback;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/* loaded from: classes.dex */
public class BufferedPreviewManagerCallback implements PreviewManagerCallback {
    private static BufferedPreviewManagerCallback INSTANCE;
    private static boolean hasPreallocateBuffers;
    private static final MasterPreviewCallback sCallback = MasterPreviewCallback.getInstance();
    private int mFrameSize;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private volatile boolean mRunAddBufferThread;
    private ByteBuffer mSharedByteBuffer;
    private StartPreviewThread mStartPreviewThread;
    private final Set<byte[]> mAddedBufferArray = Collections.newSetFromMap(new ConcurrentHashMap());
    private final ConcurrentLinkedQueue<byte[]> mBufferArray = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<byte[]> mAddArray = new ConcurrentLinkedQueue<>();

    public static synchronized BufferedPreviewManagerCallback getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BufferedPreviewManagerCallback();
        }
        return INSTANCE;
    }

    public static boolean isUsed() {
        return INSTANCE != null;
    }

    private BufferedPreviewManagerCallback() {
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public void preparePreviewParameters(Camera.Parameters parameters, RecordConfigUtils.RecordConfig config) throws PreviewManager.InvalidPreviewSizeException {
        CameraManager cm = CameraManager.getInstance();
        parameters.setPreviewSize(this.mPreviewWidth, this.mPreviewHeight);
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        if (sizes != null) {
            Iterator<Camera.Size> it = sizes.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Camera.Size size = it.next();
                if (size.width == this.mPreviewWidth && size.height == this.mPreviewHeight) {
                    parameters.setPictureSize(this.mPreviewWidth, this.mPreviewHeight);
                    break;
                }
            }
        }
        String v = Integer.toString(this.mPreviewWidth) + "x" + Integer.toString(this.mPreviewHeight);
        parameters.set("video-size", v);
        if (RecordConfigUtils.RecordConfig.DISABLE_FACE_RECOGNITION) {
            parameters.set("face-detection", "off");
        }
        boolean setRecordingHint = cm.addRecordingHintIfNeeded(parameters, config);
        CrashUtil.log("Set preview size: {} * {}, {}.", Integer.valueOf(this.mPreviewWidth), Integer.valueOf(this.mPreviewHeight), Boolean.valueOf(setRecordingHint));
        parameters.setPreviewFormat(17);
        cm.disableVideoStab(parameters);
        try {
            getCamera().setParameters(parameters);
            if (SLog.sLogsOn) {
                SLog.d("Starting params: {}.", getCamera().getParameters().flatten());
            }
        } catch (Exception e) {
            throw new PreviewManager.InvalidPreviewSizeException(e.getMessage());
        }
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public void cancelPendingPreviews() {
        StartPreviewThread previewThread = this.mStartPreviewThread;
        if (previewThread != null) {
            previewThread.cancel();
        }
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public void waitForPreviewToStart() {
        try {
            if (this.mStartPreviewThread != null) {
                long start = System.currentTimeMillis();
                this.mStartPreviewThread.join();
                SLog.i("Waiting for preview thread took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
            }
        } catch (InterruptedException e) {
            SLog.d("Couldn't wait for preview to join.");
        }
        this.mStartPreviewThread = null;
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public void preReleaseCameraAndPreview() {
        StartPreviewThread startPreviewThread = this.mStartPreviewThread;
        if (startPreviewThread != null) {
            startPreviewThread.notifyReleasedAlready();
        }
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public void onReleaseCameraAndPreview(String tag, boolean releaseCallbackFromMaster) {
        CrashUtil.log("Camera was released from {}.", tag);
        sCallback.onReleaseFromCamera(getCamera(), releaseCallbackFromMaster);
        try {
            getCamera().setPreviewCallback(null);
        } catch (RuntimeException e) {
            CrashUtil.log("Failed to release callback, probably released: " + e.getMessage());
        }
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public void setPreviewSize(int previewWidth, int previewHeight) {
        this.mPreviewWidth = previewWidth;
        this.mPreviewHeight = previewHeight;
        this.mFrameSize = ((this.mPreviewWidth * this.mPreviewHeight) * ImageFormat.getBitsPerPixel(17)) / 8;
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public int getPreviewWidth() {
        return this.mPreviewWidth;
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public int getPreviewHeight() {
        return this.mPreviewHeight;
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public void startPreview(CameraManager.CameraManagerController controller, int frameRate, int currentDurationMs, RecordConfigUtils.RecordConfig config) {
        this.mStartPreviewThread = new StartPreviewThread(controller, config, frameRate, currentDurationMs);
        this.mStartPreviewThread.start();
    }

    public class StartPreviewThread extends Thread {
        private final RecordConfigUtils.RecordConfig config;
        private final CameraManager.CameraManagerController controller;
        private final long currentDurationMs;
        private final int frameRate;
        private boolean mCanceled;
        private boolean releasedAlready;

        public StartPreviewThread(CameraManager.CameraManagerController controller, RecordConfigUtils.RecordConfig config, int frameRate, long currentDurationMs) {
            super("StartPreviewThread");
            this.frameRate = frameRate;
            this.currentDurationMs = currentDurationMs;
            this.config = config;
            this.controller = controller;
        }

        public void notifyReleasedAlready() {
            this.releasedAlready = true;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[][] buffers;
            byte[] tempBuffer;
            AddBufferThread addThread = BufferedPreviewManagerCallback.this.new AddBufferThread();
            int maxDuration = this.config.maxDuration;
            int bufferCount = Math.max((int) ((this.config.bufferCount * (this.frameRate / this.config.targetFrameRate)) * ((maxDuration - this.currentDurationMs) / maxDuration)), 30) / 10;
            CrashUtil.log("Buffer count: {}.", Integer.valueOf(bufferCount));
            this.releasedAlready = false;
            synchronized (CameraManager.CAMERA_LOCK) {
                if (BufferedPreviewManagerCallback.this.hasCamera() && !this.releasedAlready) {
                    for (byte[] b : BufferedPreviewManagerCallback.this.mAddedBufferArray) {
                        BufferedPreviewManagerCallback.this.getCamera().addCallbackBuffer(b);
                        bufferCount--;
                    }
                } else {
                    this.releasedAlready = true;
                    CrashUtil.log("Camera was released already.");
                }
            }
            if (!this.releasedAlready) {
                Iterator it = BufferedPreviewManagerCallback.this.mBufferArray.iterator();
                while (it.hasNext()) {
                    byte[] b2 = (byte[]) it.next();
                    BufferedPreviewManagerCallback.this.addCallbackBuffer(b2);
                    bufferCount--;
                }
            }
            CrashUtil.log("After reused buffer count: {}.", Integer.valueOf(bufferCount));
            long start = System.currentTimeMillis();
            synchronized (CameraManager.CAMERA_LOCK) {
                if (BufferedPreviewManagerCallback.this.hasCamera() && !this.releasedAlready) {
                    BufferedPreviewManagerCallback.this.getCamera().setPreviewCallbackWithBuffer(BufferedPreviewManagerCallback.sCallback);
                } else {
                    this.releasedAlready = true;
                }
            }
            System.gc();
            if (!this.releasedAlready) {
                boolean oomPreAlloc = false;
                if (this.config.preAllocateBuffer && !BufferedPreviewManagerCallback.hasPreallocateBuffers) {
                    int n = (int) (BufferedPreviewManagerCallback.this.mFrameSize * (bufferCount - BufferedPreviewManagerCallback.this.mBufferArray.size()) * this.config.preAllocRatio);
                    CrashUtil.log("Pre-allocate buffer {}: {}.", Double.valueOf(this.config.preAllocRatio), Integer.valueOf(n));
                    boolean unused = BufferedPreviewManagerCallback.hasPreallocateBuffers = true;
                    try {
                        tempBuffer = new byte[n];
                    } catch (OutOfMemoryError e) {
                        tempBuffer = null;
                        oomPreAlloc = true;
                        CrashUtil.log("Pre-allocation failed with ratio {}, ignore pre-allocation step.", Double.valueOf(this.config.preAllocRatio));
                    }
                    BufferedPreviewManagerCallback.this.mSharedByteBuffer = ByteBuffer.wrap(new byte[BufferedPreviewManagerCallback.this.mFrameSize]);
                    if (tempBuffer != null) {
                        tempBuffer[0] = 1;
                    }
                    byte[] tempBuffer2 = new byte[0];
                    System.gc();
                }
                if (oomPreAlloc && this.config.preAllocRatio <= 1.0d) {
                    bufferCount = (int) (bufferCount * 0.5d);
                }
                int bufferCount2 = Math.max(bufferCount, 0);
                if (BufferedPreviewManagerCallback.this.mSharedByteBuffer == null) {
                    buffers = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, bufferCount2 + 1, BufferedPreviewManagerCallback.this.mFrameSize);
                    BufferedPreviewManagerCallback.this.mSharedByteBuffer = ByteBuffer.wrap(buffers[bufferCount2]);
                } else {
                    buffers = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, bufferCount2, BufferedPreviewManagerCallback.this.mFrameSize);
                }
                BufferedPreviewManagerCallback.this.mRunAddBufferThread = !this.releasedAlready;
                addThread.start();
                for (int i = 0; i < bufferCount2; i++) {
                    if (i % 15 == 1) {
                        CrashUtil.log("Add buffer {}.", Integer.valueOf(i));
                    }
                    BufferedPreviewManagerCallback.this.addBuffer(buffers[i], false);
                }
            }
            SLog.d("Waiting for adding to be done.");
            BufferedPreviewManagerCallback.this.mRunAddBufferThread = false;
            CrashUtil.log("Making buffer took {} ms.", Long.valueOf(System.currentTimeMillis() - start));
            RuntimeException callbackException = null;
            if (this.mCanceled) {
                CrashUtil.log("Preview was cancelled.");
                return;
            }
            synchronized (CameraManager.CAMERA_LOCK) {
                if (BufferedPreviewManagerCallback.this.hasCamera() && !this.releasedAlready) {
                    BufferedPreviewManagerCallback.sCallback.setCallbackWithBuffer(this.controller);
                    try {
                        if (!this.mCanceled) {
                            BufferedPreviewManagerCallback.this.getCamera().startPreview();
                        } else {
                            CrashUtil.log("Preview was cancelled.");
                            return;
                        }
                    } catch (RuntimeException e2) {
                        callbackException = e2;
                    }
                }
                while (addThread.isAlive() && !this.releasedAlready) {
                    try {
                        addThread.join(200L);
                    } catch (InterruptedException e3) {
                        SLog.e("Couldn't wait for add thread to join.");
                    }
                }
                if (this.mCanceled) {
                    CrashUtil.log("Preview was cancelled.");
                } else if (callbackException == null) {
                    BufferedPreviewManagerCallback.sCallback.onPreviewStarted();
                } else {
                    BufferedPreviewManagerCallback.sCallback.onPreviewError(callbackException);
                }
            }
        }

        public void cancel() {
            this.mCanceled = true;
        }
    }

    public void removeBufferFromAvailableQueue(byte[] b) {
        if (b != null) {
            this.mAddedBufferArray.remove(b);
        }
    }

    public int getAddedBufferArrayCount() {
        return this.mAddedBufferArray.size();
    }

    public void addBuffer(byte[] bytes, boolean isFlushing) {
        if (bytes != null) {
            if (isFlushing) {
                this.mBufferArray.offer(bytes);
                return;
            }
            if (hasCamera()) {
                if (!this.mRunAddBufferThread) {
                    addCallbackBuffer(bytes);
                    return;
                } else {
                    this.mAddArray.offer(bytes);
                    return;
                }
            }
            this.mBufferArray.offer(bytes);
        }
    }

    public void addCallbackBuffer(byte[] bytes) {
        synchronized (CameraManager.CAMERA_LOCK) {
            if (hasCamera()) {
                getCamera().addCallbackBuffer(bytes);
                sCallback.onBufferAdded(getCamera());
                this.mAddedBufferArray.add(bytes);
            } else {
                SLog.e("Buffer couldn't be added.");
            }
        }
    }

    private class AddBufferThread extends Thread {
        public AddBufferThread() {
            super("AddBufferThread");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws InterruptedException {
            while (BufferedPreviewManagerCallback.this.mRunAddBufferThread) {
                if (BufferedPreviewManagerCallback.this.hasCamera()) {
                    byte[] bytes = (byte[]) BufferedPreviewManagerCallback.this.mAddArray.poll();
                    if (bytes == null) {
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        BufferedPreviewManagerCallback.this.addCallbackBuffer(bytes);
                    }
                }
            }
            ArrayList<byte[]> unused = new ArrayList<>();
            while (true) {
                byte[] bytes2 = (byte[]) BufferedPreviewManagerCallback.this.mAddArray.poll();
                if (bytes2 != null) {
                    if (BufferedPreviewManagerCallback.this.hasCamera()) {
                        BufferedPreviewManagerCallback.this.addCallbackBuffer(bytes2);
                    } else {
                        unused.add(bytes2);
                    }
                } else {
                    BufferedPreviewManagerCallback.this.mBufferArray.addAll(unused);
                    return;
                }
            }
        }
    }

    public ByteBuffer getSharedByteBuffer() {
        return this.mSharedByteBuffer;
    }

    public boolean isFrameAdded(byte[] data) {
        return this.mAddedBufferArray.contains(data);
    }

    public int getFrameSize() {
        return this.mFrameSize;
    }

    @Override // co.vine.android.recorder.camera.PreviewManagerCallback
    public void releaseResources() {
        this.mBufferArray.clear();
        this.mAddedBufferArray.clear();
        System.gc();
    }

    public static String getCallbackStateString() {
        return MasterPreviewCallback.getInstance().getCallbackStateString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasCamera() {
        return getCamera() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Camera getCamera() {
        return CameraManager.getInstance().getCamera();
    }
}
