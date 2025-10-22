package co.vine.android.recorder;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/* loaded from: classes.dex */
public abstract class CodecOutputSurface implements SurfaceTexture.OnFrameAvailableListener {
    protected EGL10 mEgl;
    protected boolean mFrameAvailable;
    int mInHeight;
    int mInWidth;
    protected Surface mSurface;
    protected SurfaceTexture mSurfaceTexture;
    protected STextureRender mTextureRender;
    protected EGLDisplay mEGLDisplay = EGL10.EGL_NO_DISPLAY;
    protected EGLContext mEGLContext = EGL10.EGL_NO_CONTEXT;
    protected EGLSurface mEGLSurface = EGL10.EGL_NO_SURFACE;
    protected Object mFrameSyncObject = new Object();
    protected Paint mPaint = new Paint();

    protected abstract void establishSize(int i, int i2, int i3, Point point);

    public CodecOutputSurface(int inWidth, int inHeight, int outSize, Point cropOrigin) {
        if (inWidth <= 0 || inHeight <= 0) {
            throw new IllegalArgumentException();
        }
        this.mEgl = (EGL10) EGLContext.getEGL();
        establishSize(inWidth, inHeight, outSize, cropOrigin);
        eglSetup();
        makeCurrent();
        setup();
    }

    protected void setup() {
        this.mTextureRender = new STextureRender();
        this.mTextureRender.surfaceCreated(getMinificationFilteringMode());
        this.mSurfaceTexture = new SurfaceTexture(this.mTextureRender.getTextureId());
        this.mSurfaceTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mSurfaceTexture);
    }

    protected void eglSetup() {
        this.mEGLDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (this.mEGLDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display");
        }
        int[] version = new int[2];
        if (!this.mEgl.eglInitialize(this.mEGLDisplay, version)) {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }
        int[] attribList = {12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12339, 1, 12344};
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!this.mEgl.eglChooseConfig(this.mEGLDisplay, attribList, configs, configs.length, numConfigs)) {
            throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
        }
        int[] attrib_list = {12440, 2, 12344};
        this.mEGLContext = this.mEgl.eglCreateContext(this.mEGLDisplay, configs[0], EGL10.EGL_NO_CONTEXT, attrib_list);
        checkEglError("eglCreateContext");
        if (this.mEGLContext == null) {
            throw new RuntimeException("null context");
        }
        int[] surfaceAttribs = {12375, this.mInWidth, 12374, this.mInHeight, 12344};
        this.mEGLSurface = this.mEgl.eglCreatePbufferSurface(this.mEGLDisplay, configs[0], surfaceAttribs);
        checkEglError("eglCreatePbufferSurface");
        if (this.mEGLSurface == null) {
            throw new RuntimeException("surface was null");
        }
    }

    public void release() {
        if (this.mEGLDisplay != EGL10.EGL_NO_DISPLAY) {
            this.mEgl.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
            this.mEgl.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
            this.mEgl.eglMakeCurrent(this.mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.mEgl.eglTerminate(this.mEGLDisplay);
        }
        this.mEGLDisplay = EGL10.EGL_NO_DISPLAY;
        this.mEGLContext = EGL10.EGL_NO_CONTEXT;
        this.mEGLSurface = EGL10.EGL_NO_SURFACE;
        this.mSurface.release();
        this.mTextureRender = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }

    public void makeCurrent() {
        if (!this.mEgl.eglMakeCurrent(this.mEGLDisplay, this.mEGLSurface, this.mEGLSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void awaitNewImage() {
        synchronized (this.mFrameSyncObject) {
            while (!this.mFrameAvailable) {
                try {
                    this.mFrameSyncObject.wait(2500L);
                    if (!this.mFrameAvailable) {
                        throw new RuntimeException("frame wait timed out");
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

    public void drawImage(boolean invert) {
        this.mTextureRender.drawFrame(this.mSurfaceTexture, invert);
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

    protected void checkEglError(String msg) {
        int error = this.mEgl.eglGetError();
        if (error != 12288) {
            throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
        }
    }

    protected static class STextureRender {
        private int mProgram;
        private int maPositionHandle;
        private int maTextureHandle;
        private int muMVPMatrixHandle;
        private int muSTMatrixHandle;
        private final float[] mTriangleVerticesData = {-1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
        private float[] mMVPMatrix = new float[16];
        private float[] mSTMatrix = new float[16];
        private int mTextureID = -12345;
        private FloatBuffer mTriangleVertices = ByteBuffer.allocateDirect(this.mTriangleVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

        public STextureRender() {
            this.mTriangleVertices.put(this.mTriangleVerticesData).position(0);
            Matrix.setIdentityM(this.mSTMatrix, 0);
        }

        public int getTextureId() {
            return this.mTextureID;
        }

        public void drawFrame(SurfaceTexture st, boolean invert) {
            checkGlError("onDrawFrame start");
            st.getTransformMatrix(this.mSTMatrix);
            if (invert) {
                this.mSTMatrix[5] = -this.mSTMatrix[5];
                this.mSTMatrix[13] = 1.0f - this.mSTMatrix[13];
            }
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            GLES20.glUseProgram(this.mProgram);
            checkGlError("glUseProgram");
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(36197, this.mTextureID);
            this.mTriangleVertices.position(0);
            GLES20.glVertexAttribPointer(this.maPositionHandle, 3, 5126, false, 20, (Buffer) this.mTriangleVertices);
            checkGlError("glVertexAttribPointer maPosition");
            GLES20.glEnableVertexAttribArray(this.maPositionHandle);
            checkGlError("glEnableVertexAttribArray maPositionHandle");
            this.mTriangleVertices.position(3);
            GLES20.glVertexAttribPointer(this.maTextureHandle, 2, 5126, false, 20, (Buffer) this.mTriangleVertices);
            checkGlError("glVertexAttribPointer maTextureHandle");
            GLES20.glEnableVertexAttribArray(this.maTextureHandle);
            checkGlError("glEnableVertexAttribArray maTextureHandle");
            Matrix.setIdentityM(this.mMVPMatrix, 0);
            GLES20.glUniformMatrix4fv(this.muMVPMatrixHandle, 1, false, this.mMVPMatrix, 0);
            GLES20.glUniformMatrix4fv(this.muSTMatrixHandle, 1, false, this.mSTMatrix, 0);
            GLES20.glDrawArrays(5, 0, 4);
            checkGlError("glDrawArrays");
            GLES20.glBindTexture(36197, 0);
        }

        public void surfaceCreated(int minificationMode) {
            this.mProgram = createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n", "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
            if (this.mProgram == 0) {
                throw new RuntimeException("failed creating program");
            }
            this.maPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "aPosition");
            checkLocation(this.maPositionHandle, "aPosition");
            this.maTextureHandle = GLES20.glGetAttribLocation(this.mProgram, "aTextureCoord");
            checkLocation(this.maTextureHandle, "aTextureCoord");
            this.muMVPMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uMVPMatrix");
            checkLocation(this.muMVPMatrixHandle, "uMVPMatrix");
            this.muSTMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, "uSTMatrix");
            checkLocation(this.muSTMatrixHandle, "uSTMatrix");
            int[] textures = new int[1];
            GLES20.glGenTextures(1, textures, 0);
            this.mTextureID = textures[0];
            GLES20.glBindTexture(36197, this.mTextureID);
            checkGlError("glBindTexture mTextureID");
            GLES20.glTexParameterf(36197, 10241, minificationMode);
            GLES20.glTexParameterf(36197, 10240, 9729.0f);
            GLES20.glTexParameteri(36197, 10242, 33071);
            GLES20.glTexParameteri(36197, 10243, 33071);
            checkGlError("glTexParameter");
        }

        private int loadShader(int shaderType, String source) {
            int shader = GLES20.glCreateShader(shaderType);
            checkGlError("glCreateShader type=" + shaderType);
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, 35713, compiled, 0);
            if (compiled[0] == 0) {
                Log.e("ExtractMpegFramesTest", "Could not compile shader " + shaderType + ":");
                Log.e("ExtractMpegFramesTest", " " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                return 0;
            }
            return shader;
        }

        private int createProgram(String vertexSource, String fragmentSource) {
            int pixelShader;
            int vertexShader = loadShader(35633, vertexSource);
            if (vertexShader != 0 && (pixelShader = loadShader(35632, fragmentSource)) != 0) {
                int program = GLES20.glCreateProgram();
                if (program == 0) {
                    Log.e("ExtractMpegFramesTest", "Could not create program");
                }
                GLES20.glAttachShader(program, vertexShader);
                checkGlError("glAttachShader");
                GLES20.glAttachShader(program, pixelShader);
                checkGlError("glAttachShader");
                GLES20.glLinkProgram(program);
                int[] linkStatus = new int[1];
                GLES20.glGetProgramiv(program, 35714, linkStatus, 0);
                if (linkStatus[0] != 1) {
                    Log.e("ExtractMpegFramesTest", "Could not link program: ");
                    Log.e("ExtractMpegFramesTest", GLES20.glGetProgramInfoLog(program));
                    GLES20.glDeleteProgram(program);
                    return 0;
                }
                return program;
            }
            return 0;
        }

        public void checkGlError(String op) {
            int error = GLES20.glGetError();
            if (error != 0) {
                Log.e("ExtractMpegFramesTest", op + ": glError " + error);
                throw new RuntimeException(op + ": glError " + error);
            }
        }

        public static void checkLocation(int location, String label) {
            if (location < 0) {
                throw new RuntimeException("Unable to locate '" + label + "' in program");
            }
        }
    }

    public int getMinificationFilteringMode() {
        return 9729;
    }
}
