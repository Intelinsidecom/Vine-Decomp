package co.vine.android.recorder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import co.vine.android.recorder.SwVineFrameRecorder;
import co.vine.android.recorder.video.VideoData;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.opencv_core;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: classes.dex */
public class TranscodingCodecOutputSurface extends CodecOutputSurface implements SurfaceTexture.OnFrameAvailableListener {
    protected final Canvas mBitmapCanvas;
    private Point mCropOrigin;
    private LinkedBlockingDeque<VideoFrame> mEncodingIplQueue;
    private final EncodingThread mEncodingThread;
    private boolean mHasGeneratedThumbnail;
    private Matrix mMatrix;
    protected Bitmap mOutputBitmap;
    private ByteBuffer mPixelBuf;
    protected Bitmap mScaledCroppedBitmap;
    private LinkedBlockingDeque<opencv_core.IplImage> mSourceIplQueue;
    private final String mThumbnailPath;

    public static class VideoFrame {
        final opencv_core.IplImage image;
        final long timestamp;

        public VideoFrame(opencv_core.IplImage image, long timestamp) {
            this.image = image;
            this.timestamp = timestamp;
        }
    }

    public TranscodingCodecOutputSurface(int inWidth, int inHeight, Point cropOrigin, int orientation, int outSize, int frames, final ExtractorTranscoder extractorTranscoder, int videoBufferMaxByteCount) {
        super(inWidth, inHeight, outSize, cropOrigin);
        this.mSourceIplQueue = new LinkedBlockingDeque<>();
        this.mEncodingIplQueue = new LinkedBlockingDeque<>();
        this.mHasGeneratedThumbnail = false;
        final SwVineFrameRecorder recorder = extractorTranscoder.getRecorder();
        this.mThumbnailPath = recorder.getFilename() + System.currentTimeMillis() + ".jpg";
        this.mMatrix = new Matrix();
        this.mMatrix.postRotate(orientation, outSize / 2.0f, outSize / 2.0f);
        if (orientation == 180) {
            this.mCropOrigin.x = -((this.mInWidth - this.mCropOrigin.x) - outSize);
        } else if (orientation == 270) {
            this.mCropOrigin.y = -((this.mInWidth - this.mCropOrigin.y) - outSize);
        }
        System.nanoTime();
        frames = frames < 10 ? 10 : frames;
        for (int i = 0; i < frames / 2; i++) {
            this.mSourceIplQueue.add(opencv_core.IplImage.create(outSize, outSize, 8, 4));
        }
        this.mEncodingThread = new EncodingThread(recorder, this.mEncodingIplQueue, this.mSourceIplQueue, new EncodingThread.EncodingDoneListener() { // from class: co.vine.android.recorder.TranscodingCodecOutputSurface.1
            @Override // co.vine.android.recorder.TranscodingCodecOutputSurface.EncodingThread.EncodingDoneListener
            public void encodingProgressMade(long justProcessedPresentationTimestampUs) {
                if (justProcessedPresentationTimestampUs > 0) {
                    extractorTranscoder.onEncodingProgressMade(justProcessedPresentationTimestampUs);
                    return;
                }
                try {
                    recorder.stop();
                    extractorTranscoder.onFinishedVideoEncoding(TranscodingCodecOutputSurface.this.mEncodingThread.mVideoBufferMax, TranscodingCodecOutputSurface.this.mEncodingThread.mVideoData, TranscodingCodecOutputSurface.this.mThumbnailPath, TranscodingCodecOutputSurface.this.mScaledCroppedBitmap, extractorTranscoder.getFrameRate());
                } catch (SwVineFrameRecorder.Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, videoBufferMaxByteCount);
        this.mOutputBitmap = Bitmap.createBitmap(this.mInWidth, this.mInHeight, Bitmap.Config.ARGB_8888);
        this.mScaledCroppedBitmap = Bitmap.createBitmap(outSize, outSize, Bitmap.Config.ARGB_8888);
        this.mPixelBuf = ByteBuffer.allocateDirect(this.mInWidth * this.mInHeight * 4);
        this.mPixelBuf.order(ByteOrder.LITTLE_ENDIAN);
        this.mPaint.setAntiAlias(false);
        this.mPaint.setDither(false);
        this.mPaint.setFilterBitmap(false);
        this.mBitmapCanvas = new Canvas(this.mScaledCroppedBitmap);
        this.mEncodingThread.setPriority(10);
        this.mEncodingThread.start();
    }

    @Override // co.vine.android.recorder.CodecOutputSurface
    protected void establishSize(int inWidth, int inHeight, int outSize, Point cropOrigin) {
        int smallestDimension = Math.min(inWidth, inHeight);
        float scale = outSize / (1.0f * smallestDimension);
        this.mInWidth = Math.round(inWidth * scale);
        this.mInHeight = Math.round(inHeight * scale);
        int scaledOriginX = Math.round(cropOrigin.x * scale);
        int scaledOriginY = Math.round(cropOrigin.y * scale);
        this.mCropOrigin = new Point(scaledOriginX, scaledOriginY);
    }

    public void done() throws FileNotFoundException {
        this.mEncodingThread.done();
        SLog.d("Done decoding, waiting for encode to be done.");
        try {
            this.mEncodingThread.join();
        } catch (InterruptedException e) {
            if (SLog.sLogsOn) {
                e.printStackTrace();
            }
        }
    }

    public void saveFrame(long presentationTimestampUS) throws InterruptedException, IOException {
        this.mPixelBuf.rewind();
        System.nanoTime();
        GLES20.glReadPixels(0, 0, this.mInWidth, this.mInHeight, 6408, 5121, this.mPixelBuf);
        this.mPixelBuf.rewind();
        System.nanoTime();
        this.mOutputBitmap.copyPixelsFromBuffer(this.mPixelBuf);
        System.nanoTime();
        this.mBitmapCanvas.save();
        this.mBitmapCanvas.translate(-this.mCropOrigin.x, -this.mCropOrigin.y);
        this.mBitmapCanvas.drawBitmap(this.mOutputBitmap, this.mMatrix, this.mPaint);
        this.mBitmapCanvas.restore();
        try {
            opencv_core.IplImage image = this.mSourceIplQueue.take();
            this.mScaledCroppedBitmap.copyPixelsToBuffer(image.getByteBuffer());
            if (!this.mHasGeneratedThumbnail) {
                this.mScaledCroppedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(this.mThumbnailPath));
                this.mHasGeneratedThumbnail = true;
            }
            this.mEncodingIplQueue.add(new VideoFrame(image, presentationTimestampUS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class EncodingThread extends Thread {
        private LinkedBlockingDeque<VideoFrame> mInQueue;
        private EncodingDoneListener mListener;
        private LinkedBlockingDeque<opencv_core.IplImage> mOutQueue;
        private SwVineFrameRecorder mRecorder;
        private byte[] mVideoBufferMax;
        private ArrayList<VideoData> mVideoData = new ArrayList<>();
        private boolean mDone = false;
        private int mVideoBufferPosition = 0;

        public interface EncodingDoneListener {
            void encodingProgressMade(long j);
        }

        public EncodingThread(SwVineFrameRecorder recorder, LinkedBlockingDeque<VideoFrame> inQueue, LinkedBlockingDeque<opencv_core.IplImage> recycleQueue, EncodingDoneListener listener, int videoBufferMaxBytes) {
            this.mVideoBufferMax = new byte[videoBufferMaxBytes];
            this.mInQueue = inQueue;
            this.mOutQueue = recycleQueue;
            this.mRecorder = recorder;
            this.mListener = listener;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws InterruptedException {
            while (true) {
                if (this.mDone && this.mInQueue.isEmpty()) {
                    break;
                }
                try {
                    VideoFrame frame = this.mInQueue.take();
                    opencv_core.IplImage image = frame.image;
                    long presentationTimestampUS = frame.timestamp;
                    System.nanoTime();
                    VideoData data = new VideoData();
                    data.size = this.mRecorder.encode(image, this.mVideoBufferMax, this.mVideoBufferPosition);
                    data.encoded = true;
                    data.start = this.mVideoBufferPosition;
                    data.timestamp = presentationTimestampUS;
                    data.keyFrame = this.mRecorder.wasLastEncodedFrameKeyFrame();
                    this.mVideoData.add(data);
                    this.mVideoBufferPosition += data.size;
                    this.mOutQueue.add(image);
                    if (presentationTimestampUS > 0) {
                        this.mListener.encodingProgressMade(presentationTimestampUS);
                    }
                } catch (SwVineFrameRecorder.Exception e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e2) {
                }
            }
            if (this.mListener != null) {
                this.mListener.encodingProgressMade(-1L);
            }
        }

        public void done() {
            this.mDone = true;
            interrupt();
        }
    }
}
