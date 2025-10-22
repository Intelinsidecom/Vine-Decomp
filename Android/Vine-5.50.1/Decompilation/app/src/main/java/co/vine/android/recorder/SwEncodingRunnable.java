package co.vine.android.recorder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Process;
import co.vine.android.recorder.AbstractEncodingRunnable;
import co.vine.android.recorder.ByteBufferQueue;
import co.vine.android.recorder.RecordProcessor;
import co.vine.android.recorder.SwVineFrameRecorder;
import co.vine.android.recorder.buffered.BufferedPreviewManagerCallback;
import co.vine.android.recorder.camera.CameraSetting;
import co.vine.android.recorder.video.VideoData;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.ExceptionUtil;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.opencv_core;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public class SwEncodingRunnable extends AbstractEncodingRunnable {
    private static int sRunningInstances;
    private BaseFinishProcessTask mAsyncTask;
    private final ByteBufferQueue mByteBufferQueue;
    private final ConcurrentLinkedQueue<VideoData> mDataQueue;
    private SwVineFrameRecorder mEncoder;
    private final opencv_core.IplImage mEncodingImage;
    private final String mEncodingTempPath;
    private final RecordProcessor.ProcessingErrorHandler mErrorhandler;
    private RecordSegment mLastSegment;
    private Bitmap mMatrixBitmap;
    private Canvas mMatrixCanvas;
    private int mMaxEncodedSize;
    private final String mOutput;
    private final PictureConverter mPictureConverter;
    private final BufferedPreviewManagerCallback mPreviewCallback;
    private final int mRecorderFrameRate;
    private final RecordState mState;
    private volatile boolean mTerminateImmediately = false;
    private final Bitmap mThumbnailBitmap;
    private final Canvas mThumbnailCanvas;
    private final Matrix mThumbnailMatrix;
    private int mTotalProcessed;
    private final boolean mUseMp4;
    private final byte[] mVideoDataBufferMax;
    private int mVideoDataBufferPosition;
    private long timeToMatrix;
    private long timeToRGB;
    private long timeToRecord;

    public SwEncodingRunnable(ConcurrentLinkedQueue<VideoData> queue, byte[] videoDataBufferMax, RecordState state, String output, int frameRate, opencv_core.IplImage image, Bitmap bitmap, Bitmap thumbnailBitmap, PictureConverter pc, int videoBufferPosition, BufferedPreviewManagerCallback cb, RecordProcessor.ProcessingErrorHandler errorhandler, ByteBufferQueue byteBufferQueue, Matrix thumbnailMatrix, Canvas thumbnailCanvas, Canvas matrixCanvas, boolean useMp4) {
        this.mEncodingImage = image;
        this.mUseMp4 = useMp4;
        this.mDataQueue = queue;
        this.mState = state;
        this.mPreviewCallback = cb;
        this.mMatrixBitmap = bitmap;
        this.mThumbnailBitmap = thumbnailBitmap;
        this.mThumbnailCanvas = thumbnailCanvas;
        this.mMatrixCanvas = matrixCanvas;
        this.mVideoDataBufferMax = videoDataBufferMax;
        this.mVideoDataBufferPosition = videoBufferPosition;
        this.mPictureConverter = pc;
        this.mRecorderFrameRate = frameRate;
        this.mOutput = output;
        this.mErrorhandler = errorhandler;
        this.mThumbnailMatrix = thumbnailMatrix;
        this.mEncodingTempPath = this.mOutput + ".encode";
        this.mByteBufferQueue = byteBufferQueue;
        if (this.mByteBufferQueue == null || !this.mByteBufferQueue.isFresh()) {
            throw new RuntimeException("Invalid buffer queue.");
        }
    }

    static class EncodingProcessRunnable implements Runnable {
        private LinkedList<VideoData> mBufferedVideoData;
        private SwEncodingRunnable mParent;
        public boolean mRun;
        private final int[] LOCK = new int[0];
        private int totalSizeToWait = -1;

        public EncodingProcessRunnable(SwEncodingRunnable parent) {
            this.mParent = parent;
            this.mParent.makeNewEncoder();
            this.mBufferedVideoData = new LinkedList<>();
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException, SecurityException, IllegalArgumentException {
            Process.setThreadPriority(19);
            SLog.i("Started encoding process runnable, additional drawers: {}.", Integer.valueOf(this.mParent.mAdditionalDrawers.size()));
            while (this.mRun) {
                ByteBufferQueue.QueueItem data = this.mParent.mByteBufferQueue.get();
                if (!this.mParent.mState.isStarted() && this.mParent.mAsyncTask != null) {
                    int size = Math.max(this.mParent.mByteBufferQueue.size(), 1);
                    if (this.totalSizeToWait == -1) {
                        this.totalSizeToWait = size;
                        Process.setThreadPriority(-8);
                        SLog.i("Size to wait: {}.", Integer.valueOf(this.totalSizeToWait));
                    }
                    this.mParent.mAsyncTask.publish(((this.totalSizeToWait - size) * 90) / this.totalSizeToWait);
                }
                if (data == null) {
                    if (this.mParent.mByteBufferQueue.isEndOfPut()) {
                        this.mRun = false;
                    } else {
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException e) {
                            SLog.e("Interrupted.");
                        }
                    }
                } else {
                    VideoData next = (VideoData) data.tag;
                    if (next == null) {
                        CrashUtil.log("Ignored invalid data tag, segment may have been gone.");
                    } else {
                        next.data = data.bytes;
                        try {
                            this.mParent.processVideoData(this.mBufferedVideoData, next);
                        } catch (Exception e2) {
                            if (e2 instanceof NotEnoughSpaceException) {
                                this.mParent.onNotEnoughSpaceLeft(e2);
                                return;
                            }
                            throw new RuntimeException(e2);
                        }
                    }
                }
            }
            this.mParent.stopEncoder(this.mBufferedVideoData);
            this.mParent = null;
        }

        public void add(VideoData next) {
            synchronized (this.LOCK) {
                SwEncodingRunnable parent = this.mParent;
                byte[] bytes = next.data;
                if (bytes != null && parent != null && !parent.mByteBufferQueue.put(new ByteBufferQueue.QueueItem(bytes, next))) {
                    CrashUtil.log("Not enough memory left. (BufferOverflow)");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNotEnoughSpaceLeft(Exception e) {
        if (this.mErrorhandler != null) {
            this.mErrorhandler.onNotEnoughSpaceLeft(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void makeNewEncoder() {
        this.mEncoder = RecordConfigUtils.newVideoRecorder(this.mEncodingTempPath, this.mRecorderFrameRate, 480, this.mUseMp4);
        try {
            this.mEncoder.start();
        } catch (SwVineFrameRecorder.Exception e) {
            CrashUtil.logException(e, "Failed to start encoder", new Object[0]);
        }
    }

    public void processVideoData(LinkedList<VideoData> bufferedVideoData, VideoData next) throws SwVineFrameRecorder.Exception, IOException {
        boolean newSegment = this.mLastSegment != next.segment;
        this.mLastSegment = next.segment;
        if (this.mLastSegment.removed) {
            SLog.i("Skipped processing data from deleted segments.");
            return;
        }
        synchronized (this.mPictureConverter.LOCK) {
            long startTime = System.currentTimeMillis();
            CameraSetting cameraSetting = next.segment.getCameraSetting();
            boolean hasChanged = this.mPictureConverter.updateSettingsIfNeeded(cameraSetting);
            this.mPictureConverter.giveMatrixNewValuesWithScaleIfDegreeHasChangedWithKnownConfigs(cameraSetting.degrees, false);
            this.mPictureConverter.convert(cameraSetting, next, hasChanged);
            this.timeToRGB += System.currentTimeMillis() - startTime;
            long startTime2 = System.currentTimeMillis();
            this.mPictureConverter.draw(this.mMatrixCanvas);
            Iterator<AbstractEncodingRunnable.Drawer> it = this.mAdditionalDrawers.iterator();
            while (it.hasNext()) {
                AbstractEncodingRunnable.Drawer drawer = it.next();
                drawer.draw(this.mMatrixCanvas, cameraSetting.frontFacing);
            }
            this.timeToMatrix += System.currentTimeMillis() - startTime2;
        }
        this.mMatrixBitmap.copyPixelsToBuffer(this.mEncodingImage.getByteBuffer());
        if (newSegment && !this.mTerminateImmediately) {
            this.mPictureConverter.drawGeneric(this.mThumbnailCanvas, this.mMatrixBitmap, this.mThumbnailMatrix);
            String path = this.mOutput + "." + System.currentTimeMillis() + ".jpg";
            next.segment.setThumbnailPath(path);
            File f = new File(path);
            File parentFile = f.getParentFile();
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                this.mThumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] compressed = bos.toByteArray();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(compressed);
                fos.close();
            } catch (Throwable e) {
                boolean wasSessionJustDeleted = RecordSessionManager.wasSessionJustDeleted(parentFile);
                if (wasSessionJustDeleted) {
                    CrashUtil.log("Session was just deleted, terminate encoder.");
                    this.mTerminateImmediately = true;
                    CrashUtil.logException(e);
                } else {
                    if ((e instanceof Exception) && ExceptionUtil.isNoSpaceLeftException((Exception) e)) {
                        throw new NotEnoughSpaceException(e);
                    }
                    throw e;
                }
            }
        }
        if (!this.mTerminateImmediately) {
            long startTime3 = System.currentTimeMillis();
            bufferedVideoData.add(next);
            doVideoFrame(bufferedVideoData, this.mEncodingImage, newSegment);
            this.timeToRecord += System.currentTimeMillis() - startTime3;
        }
        next.data = null;
    }

    private boolean doVideoFrame(LinkedList<VideoData> bufferedVideoData, opencv_core.IplImage image, boolean newSegment) throws SwVineFrameRecorder.Exception {
        if (this.mEncoder == null) {
            makeNewEncoder();
        } else if (newSegment) {
            stopEncoder(bufferedVideoData);
            makeNewEncoder();
        }
        VideoData data = bufferedVideoData.getFirst();
        data.size = this.mEncoder.encode(image, this.mVideoDataBufferMax, this.mVideoDataBufferPosition);
        SLog.d("Encoded video frame size {}. Total processed {}.", Integer.valueOf(data.size), Integer.valueOf(this.mTotalProcessed));
        this.mTotalProcessed++;
        if (data.size != -1) {
            data.start = this.mVideoDataBufferPosition;
            data.keyFrame = this.mEncoder.wasLastEncodedFrameKeyFrame();
            data.encoded = true;
            this.mMaxEncodedSize = Math.max(this.mMaxEncodedSize, data.size);
            this.mVideoDataBufferPosition += data.size;
            bufferedVideoData.pop();
            return true;
        }
        SLog.d("******Image was buffered to the next frame.********");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopEncoder(LinkedList<VideoData> bufferedVideoData) {
        while (bufferedVideoData.size() > 0 && doVideoFrame(bufferedVideoData, null, false)) {
            try {
            } catch (SwVineFrameRecorder.Exception e) {
                SLog.e("Error flushing...", (Throwable) e);
                return;
            } finally {
                this.mEncoder.release();
            }
        }
        this.mEncoder.stopEncoding();
    }

    @Override // co.vine.android.recorder.AbstractEncodingRunnable
    public void setAsyncTask(BaseFinishProcessTask task) {
        this.mAsyncTask = task;
    }

    @Override // co.vine.android.recorder.AbstractEncodingRunnable
    public void terminate() {
        CrashUtil.log("Terminate without saving turned on.");
        this.mTerminateImmediately = true;
    }

    @Override // java.lang.Runnable
    public void run() throws InterruptedException, SecurityException, IllegalArgumentException {
        sRunningInstances++;
        CrashUtil.set("Encoder Count", sRunningInstances);
        Process.setThreadPriority(19);
        CrashUtil.log("[ML] Processing started: {}, running: {}", this.mOutput, Integer.valueOf(sRunningInstances));
        EncodingProcessRunnable encodingRunnable = new EncodingProcessRunnable(this);
        encodingRunnable.mRun = true;
        Thread encodingThread = new Thread(encodingRunnable);
        encodingThread.start();
        while (this.mState.isStarted() && !this.mTerminateImmediately) {
            process(encodingRunnable, true);
        }
        CrashUtil.log("[ML] Flushing... {}, {}", Boolean.valueOf(this.mState.isStarted()), Boolean.valueOf(this.mTerminateImmediately));
        if (!this.mTerminateImmediately) {
            process(encodingRunnable, false);
        } else {
            SLog.d("Terminate without saving....");
        }
        this.mByteBufferQueue.endOfPut();
        SLog.d("[ML] Waiting for encoding prcess thread to end...");
        try {
            encodingThread.join();
        } catch (InterruptedException e) {
            SLog.e("Interrupted.");
        }
        SLog.d("[ML] Encoding process thread ended");
        FileUtils.deleteQuietly(new File(this.mEncodingTempPath));
        if (this.mAsyncTask != null) {
            this.mAsyncTask.publish(90);
        }
        SLog.d("Time used: rgb: {}, matrix: {}, encode: {}, n: {}, max: {}.", new Object[]{Long.valueOf(this.timeToRGB), Long.valueOf(this.timeToMatrix), Long.valueOf(this.timeToRecord), Integer.valueOf(this.mTotalProcessed), Integer.valueOf(this.mMaxEncodedSize)});
        this.mLastSegment = null;
        CrashUtil.log("[ML] Processing Ended: {}, {}.", this.mOutput, Boolean.valueOf(this.mTerminateImmediately));
        sRunningInstances--;
    }

    private void process(EncodingProcessRunnable runnable, boolean waitForMore) throws InterruptedException {
        while (!this.mTerminateImmediately) {
            VideoData next = this.mDataQueue.poll();
            if (next != null || this.mState.isStarted()) {
                if (next == null) {
                    if (waitForMore) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            SLog.d("Break signal reached.");
                            return;
                        }
                    } else {
                        continue;
                    }
                } else {
                    if (!next.segment.removed) {
                        runnable.add(next);
                    } else {
                        SLog.i("Skipped processing data from deleted segments.");
                    }
                    this.mPreviewCallback.addBuffer(next.data, !waitForMore);
                }
            } else {
                return;
            }
        }
    }
}
