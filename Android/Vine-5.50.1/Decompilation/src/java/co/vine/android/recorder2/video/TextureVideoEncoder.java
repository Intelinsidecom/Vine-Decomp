package co.vine.android.recorder2.video;

import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import co.vine.android.recorder2.MuxerManager;
import co.vine.android.recorder2.gles.EglCore;
import co.vine.android.recorder2.gles.FullFrameRect;
import co.vine.android.recorder2.gles.GLFrameSaver;
import co.vine.android.recorder2.gles.Texture2dProgram;
import co.vine.android.recorder2.gles.WindowSurface;
import com.edisonwang.android.slog.SLog;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class TextureVideoEncoder implements Runnable {
    private int mBitRate;
    private EglCore mEglCore;
    private FullFrameRect mFullScreen;
    private ByteBuffer mGhostBuffer;
    private String mGhostFilePath;
    private volatile EncoderHandler mHandler;
    private int mHeight;
    private WindowSurface mInputWindowSurface;
    private boolean mReady;
    private boolean mRunning;
    private int mTextureId;
    private VideoEncoderCore mVideoEncoder;
    private int mWidth;
    private float[] mTransformMatrix = new float[16];
    private Object mReadyFence = new Object();
    private Texture2dProgram.ProgramType mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT;

    public TextureVideoEncoder(int width, int height, int bitrate) {
        this.mWidth = width;
        this.mHeight = height;
        this.mBitRate = bitrate;
        this.mGhostBuffer = ByteBuffer.allocate(width * height * 4);
    }

    public void startRecording(EGLContext context, FullFrameRect blitter, MuxerManager muxer, String ghostPath, Texture2dProgram.ProgramType programType) {
        SLog.d("Encoder: startRecording()");
        synchronized (this.mReadyFence) {
            if (this.mRunning) {
                SLog.w("Encoder thread already running");
                return;
            }
            this.mRunning = true;
            new Thread(this, "TextureMovieEncoder").start();
            while (!this.mReady) {
                try {
                    this.mReadyFence.wait();
                } catch (InterruptedException e) {
                }
            }
            this.mFullScreen = blitter;
            this.mVideoEncoder.setMuxerManager(muxer);
            this.mGhostFilePath = ghostPath;
            this.mProgramType = programType;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(0, context));
        }
    }

    public void stopRecording() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5));
    }

    public boolean isRecording() {
        boolean z;
        synchronized (this.mReadyFence) {
            z = this.mRunning;
        }
        return z;
    }

    public void updateSharedContext(EGLContext sharedContext) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, sharedContext));
    }

    public void frameAvailable(SurfaceTexture st, boolean onLastFrame) {
        synchronized (this.mReadyFence) {
            if (this.mReady) {
                st.getTransformMatrix(this.mTransformMatrix);
                long timestamp = st.getTimestamp();
                if (timestamp == 0) {
                    SLog.w("HEY: got SurfaceTexture with timestamp of zero");
                } else {
                    int msgKey = onLastFrame ? 6 : 2;
                    this.mHandler.sendMessage(this.mHandler.obtainMessage(msgKey, (int) (timestamp >> 32), (int) timestamp, this.mTransformMatrix));
                }
            }
        }
    }

    public void setTextureId(int id) {
        synchronized (this.mReadyFence) {
            if (this.mReady) {
                this.mHandler.sendMessage(this.mHandler.obtainMessage(3, id, 0, null));
            }
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        Looper.prepare();
        synchronized (this.mReadyFence) {
            this.mHandler = new EncoderHandler(this);
            this.mReady = true;
            this.mReadyFence.notify();
        }
        Looper.loop();
        SLog.d("Encoder thread exiting");
        synchronized (this.mReadyFence) {
            this.mRunning = false;
            this.mReady = false;
            this.mHandler = null;
        }
    }

    private static class EncoderHandler extends Handler {
        private WeakReference<TextureVideoEncoder> mWeakEncoder;

        public EncoderHandler(TextureVideoEncoder encoder) {
            this.mWeakEncoder = new WeakReference<>(encoder);
        }

        @Override // android.os.Handler
        public void handleMessage(Message inputMessage) throws InterruptedException {
            int what = inputMessage.what;
            Object obj = inputMessage.obj;
            TextureVideoEncoder encoder = this.mWeakEncoder.get();
            if (encoder != null) {
                switch (what) {
                    case 0:
                        encoder.handleStartRecording((EGLContext) obj);
                        return;
                    case 1:
                        encoder.handleStopRecording();
                        return;
                    case 2:
                        long timestamp = (inputMessage.arg1 << 32) | (inputMessage.arg2 & 4294967295L);
                        encoder.handleFrameAvailable((float[]) obj, timestamp, false);
                        return;
                    case 3:
                        encoder.handleSetTexture(inputMessage.arg1);
                        return;
                    case 4:
                        encoder.handleUpdateSharedContext((EGLContext) inputMessage.obj);
                        return;
                    case 5:
                        Looper.myLooper().quit();
                        return;
                    case 6:
                        long timestamp2 = (inputMessage.arg1 << 32) | (inputMessage.arg2 & 4294967295L);
                        encoder.handleFrameAvailable((float[]) obj, timestamp2, true);
                        return;
                    default:
                        throw new RuntimeException("Unhandled msg what=" + what);
                }
            }
            SLog.w("EncoderHandler.handleMessage: encoder is null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStartRecording(EGLContext eglContext) {
        SLog.d("handleStartRecording");
        prepareSurfaces(eglContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFrameAvailable(float[] transform, long timestampNanos, boolean onLastFrame) throws InterruptedException {
        this.mVideoEncoder.drainEncoder(false);
        this.mFullScreen.drawFrame(this.mTextureId, transform);
        if (onLastFrame) {
            GLFrameSaver.cacheFrame(this.mGhostBuffer, this.mWidth, this.mHeight);
        }
        this.mInputWindowSurface.setPresentationTime(timestampNanos);
        this.mInputWindowSurface.swapBuffers();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v2, types: [co.vine.android.recorder2.video.TextureVideoEncoder$1] */
    public void handleStopRecording() throws InterruptedException {
        SLog.d("handleStopRecording");
        this.mVideoEncoder.drainEncoder(true);
        new Thread() { // from class: co.vine.android.recorder2.video.TextureVideoEncoder.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    try {
                        GLFrameSaver.saveFrame(TextureVideoEncoder.this.mGhostBuffer, TextureVideoEncoder.this.mWidth, TextureVideoEncoder.this.mHeight, TextureVideoEncoder.this.mGhostFilePath);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    TextureVideoEncoder.this.releaseEncoder();
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetTexture(int id) {
        this.mTextureId = id;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateSharedContext(EGLContext newSharedContext) {
        SLog.d("handleUpdatedSharedContext " + newSharedContext);
        this.mInputWindowSurface.releaseEglSurface();
        this.mFullScreen.release(false);
        this.mEglCore.release();
        this.mEglCore = new EglCore(newSharedContext, 1);
        this.mInputWindowSurface.recreate(this.mEglCore);
        this.mInputWindowSurface.makeCurrent();
    }

    public void prepareEncoder() {
        try {
            this.mVideoEncoder = new VideoEncoderCore(this.mWidth, this.mHeight, this.mBitRate);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private void prepareSurfaces(EGLContext sharedContext) {
        this.mEglCore = new EglCore(sharedContext, 1);
        this.mInputWindowSurface = new WindowSurface(this.mEglCore, this.mVideoEncoder.getInputSurface(), true);
        this.mInputWindowSurface.makeCurrent();
        this.mFullScreen = new FullFrameRect(new Texture2dProgram(this.mProgramType));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseEncoder() {
        this.mVideoEncoder.release();
        if (this.mInputWindowSurface != null) {
            this.mInputWindowSurface.release();
            this.mInputWindowSurface = null;
        }
        if (this.mFullScreen != null) {
            this.mFullScreen.release(false);
            this.mFullScreen = null;
        }
        if (this.mEglCore != null) {
            this.mEglCore.release();
            this.mEglCore = null;
        }
    }
}
