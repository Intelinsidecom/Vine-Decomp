package co.vine.android.recorder2.transcode;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import co.vine.android.recorder2.gles.GLFrameSaver;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/* loaded from: classes.dex */
class OutputSurface implements SurfaceTexture.OnFrameAvailableListener {
    private EGL10 mEGL;
    private EGLContext mEGLContext;
    private EGLDisplay mEGLDisplay;
    private EGLSurface mEGLSurface;
    private boolean mFrameAvailable;
    private Object mFrameSyncObject = new Object();
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private TextureRender mTextureRender;

    public OutputSurface() {
        setup();
    }

    private void setup() {
        this.mTextureRender = new TextureRender();
        this.mTextureRender.surfaceCreated();
        this.mSurfaceTexture = new SurfaceTexture(this.mTextureRender.getTextureId());
        this.mSurfaceTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mSurfaceTexture);
    }

    public void release() {
        if (this.mEGL != null) {
            if (this.mEGL.eglGetCurrentContext().equals(this.mEGLContext)) {
                this.mEGL.eglMakeCurrent(this.mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            }
            this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
            this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
        }
        this.mSurface.release();
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mEGL = null;
        this.mTextureRender = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void changeFragmentShader(String fragmentShader) {
        this.mTextureRender.changeFragmentShader(fragmentShader);
    }

    public void awaitNewImage() {
        synchronized (this.mFrameSyncObject) {
            while (!this.mFrameAvailable) {
                try {
                    this.mFrameSyncObject.wait(500L);
                    if (!this.mFrameAvailable) {
                        this.mFrameSyncObject.wait(500L);
                        if (!this.mFrameAvailable) {
                            throw new RuntimeException("Surface frame wait timed out");
                        }
                    }
                } catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }
            this.mFrameAvailable = false;
        }
        this.mTextureRender.checkGlError("before updateTexImage");
        this.mSurfaceTexture.updateTexImage();
    }

    public void drawImage(int rotation, float aspectRatio, float cropOriginRatio) {
        this.mTextureRender.drawFrame(this.mSurfaceTexture, rotation, aspectRatio, cropOriginRatio);
    }

    public void saveImage(int width, int height, String path) {
        ByteBuffer lastFrameBuffer = ByteBuffer.allocate(width * height * 4);
        GLFrameSaver.cacheFrame(lastFrameBuffer, width, height);
        try {
            GLFrameSaver.saveFrame(lastFrameBuffer, width, height, path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture st) {
        synchronized (this.mFrameSyncObject) {
            if (this.mFrameAvailable) {
                throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
            }
            this.mFrameAvailable = true;
            this.mFrameSyncObject.notifyAll();
        }
    }
}
