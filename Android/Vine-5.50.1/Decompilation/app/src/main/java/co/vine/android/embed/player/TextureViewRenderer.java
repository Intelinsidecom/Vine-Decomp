package co.vine.android.embed.player;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLSurface;
import android.util.Log;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.embed.player.VinePlayerInternal;
import co.vine.android.gles.CameraRenderDrawable2D;
import co.vine.android.gles.EglCore;
import co.vine.android.gles.FullFrameRect;
import co.vine.android.gles.Texture2dProgram;

/* loaded from: classes.dex */
public class TextureViewRenderer extends Thread implements SurfaceTexture.OnFrameAvailableListener, VideoViewInterface.OnTextureReadinessChangedListener {
    private volatile boolean mCreated;
    private volatile boolean mDone;
    private boolean mDrawFrame;
    private EGLSurface mEGLSurface;
    private EglCore mEglCore;
    private FullFrameRect mFullScreen;
    private volatile int mIncomingHeight;
    private volatile boolean mIncomingSizeUpdated;
    private volatile int mIncomingWidth;
    private final OnTexturesChangedListener mListener;
    private volatile boolean mNextTexture;
    private VinePlayerInternal.EventListener mOnGLErrorListener;
    private SurfaceTexture mSurfaceTexture;
    private final String mTag;
    private volatile int mTextureIndex;
    private final TextureObject[] mTextures;
    private final float[] mSTMatrix = new float[16];
    private final Object mLock = new Object();

    public interface OnTexturesChangedListener {
        void onTexturesChanged(TextureObject textureObject, TextureObject textureObject2);
    }

    public TextureViewRenderer(String tag, OnTexturesChangedListener listener, VinePlayerInternal.EventListener errorListener) {
        CameraRenderDrawable2D.setAspectRatio(1.0f);
        this.mTextures = new TextureObject[2];
        this.mTextureIndex = -1;
        this.mListener = listener;
        this.mTag = tag + " (" + hashCode() + ")";
        this.mOnGLErrorListener = errorListener;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (this.mEglCore == null) {
            initialSetup();
        }
        while (!this.mDone) {
            if (this.mSurfaceTexture == null) {
                this.mDone = true;
            } else if (this.mNextTexture) {
                this.mNextTexture = false;
                if (this.mTextureIndex == this.mTextures.length - 1) {
                    this.mTextureIndex = 0;
                } else {
                    this.mTextureIndex++;
                }
                onTextureChanged();
            } else {
                waitForFrame();
                if (this.mDrawFrame) {
                    this.mDrawFrame = false;
                    try {
                        onDrawFrame();
                    } catch (RuntimeException e) {
                        Log.e(this.mTag, "GL ERROR with " + this.mSurfaceTexture);
                        this.mOnGLErrorListener.onGLError(e);
                    }
                } else {
                    continue;
                }
            }
        }
        this.mCreated = false;
        this.mTextureIndex = -1;
        onTextureChanged();
        for (int i = 0; i < this.mTextures.length; i++) {
            TextureObject texture = this.mTextures[i];
            if (texture != null && texture.isValid()) {
                texture.release();
            }
            this.mTextures[i] = null;
        }
        if (this.mFullScreen != null) {
            this.mFullScreen.release(false);
            this.mFullScreen = null;
        }
        this.mIncomingHeight = -1;
        this.mIncomingWidth = -1;
        if (this.mEGLSurface != null) {
            this.mEglCore.releaseSurface(this.mEGLSurface);
            this.mEGLSurface = null;
        }
        if (this.mEglCore != null) {
            this.mEglCore.release();
            this.mEglCore = null;
        }
        Log.d(this.mTag, "Renderer thread exiting");
    }

    private void initialSetup() {
        SurfaceTexture surfaceTexture = waitForTexture();
        if (surfaceTexture != null) {
            createSurfaces(surfaceTexture);
        }
    }

    private void createSurfaces(SurfaceTexture surfaceTexture) {
        this.mEglCore = new EglCore(null, 2);
        if (this.mEGLSurface == null) {
            this.mEGLSurface = this.mEglCore.createWindowSurface(surfaceTexture);
            this.mEglCore.makeCurrent(this.mEGLSurface);
            Log.d(this.mTag, "Surfaces made. " + this.mEGLSurface);
            checkEglError("Failed to make surface.");
        }
        for (int i = 0; i < this.mTextures.length; i++) {
            this.mTextures[i] = TextureObject.create(36197);
            Log.d(this.mTag, "Texture made: " + this.mTextures[i].texture);
            this.mTextures[i].texture.setOnFrameAvailableListener(this);
            checkEglError("Failed to make textures.");
        }
        this.mFullScreen = new FullFrameRect(new Texture2dProgram());
        checkEglError("Failed to make screen.");
        this.mTextureIndex = 0;
        onTextureChanged();
        this.mCreated = true;
        Thread.currentThread().setPriority(10);
    }

    @TargetApi(17)
    private void checkEglError(String msg) {
        int error = EGL14.eglGetError();
        if (error != 12288) {
            throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
        }
    }

    private void onTextureChanged() {
        this.mListener.onTexturesChanged(getCurrentTextureObject(), getNextTextureObject());
    }

    private SurfaceTexture waitForTexture() {
        SurfaceTexture surfaceTexture = null;
        synchronized (this.mLock) {
            while (!this.mDone && (surfaceTexture = this.mSurfaceTexture) == null) {
                try {
                    this.mLock.wait();
                } catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }
        }
        Log.d(this.mTag, "Got surfaceTexture: " + surfaceTexture);
        return surfaceTexture;
    }

    private void waitForFrame() {
        synchronized (this.mLock) {
            while (!this.mDone && !this.mDrawFrame && !this.mNextTexture) {
                try {
                    this.mLock.wait(10L);
                } catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }
        }
    }

    public void halt() {
        synchronized (this.mLock) {
            this.mDone = true;
            this.mLock.notify();
        }
    }

    protected void onDrawFrame() {
        if (!this.mDone && this.mIncomingWidth > 0 && this.mIncomingHeight > 0) {
            if (this.mIncomingSizeUpdated) {
                this.mFullScreen.getProgram().setTexSize(this.mIncomingWidth, this.mIncomingHeight);
                this.mIncomingSizeUpdated = false;
            }
            TextureObject tx = getCurrentTextureObject();
            if (tx != null) {
                tx.texture.updateTexImage();
                tx.texture.getTransformMatrix(this.mSTMatrix);
                try {
                    this.mFullScreen.drawFrame(tx.textureId, this.mSTMatrix);
                    checkEglError("Draw frame");
                    this.mEglCore.swapBuffers(this.mEGLSurface);
                } catch (RuntimeException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    public boolean isSurfaceCreated() {
        return this.mCreated;
    }

    public TextureObject getCurrentTextureObject() {
        if (this.mTextureIndex == -1) {
            return null;
        }
        return this.mTextures[this.mTextureIndex];
    }

    public TextureObject getNextTextureObject() {
        if (this.mTextureIndex == -1) {
            return null;
        }
        if (this.mTextureIndex == this.mTextures.length - 1) {
            return this.mTextures[0];
        }
        return this.mTextures[this.mTextureIndex + 1];
    }

    public void queueNextTextureEvent() {
        this.mNextTexture = true;
    }

    public void queueSetSizeEvent(int width, int height) {
        this.mIncomingWidth = width;
        this.mIncomingHeight = height;
        this.mIncomingSizeUpdated = true;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.mDrawFrame = true;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface.OnTextureReadinessChangedListener
    public void onTextureReadinessChanged(VideoViewInterface videoView, SurfaceTexture surface, boolean isReady) {
        synchronized (this.mLock) {
            if (isReady) {
                this.mSurfaceTexture = surface;
            } else {
                Log.i(this.mTag, videoView + " Destroyed.");
                this.mSurfaceTexture = null;
            }
            this.mLock.notify();
        }
    }
}
