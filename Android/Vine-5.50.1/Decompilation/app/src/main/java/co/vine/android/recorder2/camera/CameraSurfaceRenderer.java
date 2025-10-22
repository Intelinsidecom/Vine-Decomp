package co.vine.android.recorder2.camera;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import co.vine.android.recorder2.MuxerManager;
import co.vine.android.recorder2.SetCameraSurfaceTextureHandler;
import co.vine.android.recorder2.gles.FullFrameRect;
import co.vine.android.recorder2.gles.Texture2dProgram;
import co.vine.android.recorder2.video.TextureVideoEncoder;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avformat;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@TargetApi(17)
/* loaded from: classes.dex */
public class CameraSurfaceRenderer implements GLSurfaceView.Renderer {
    private SetCameraSurfaceTextureHandler mCameraHandler;
    private FrameCapturedListener mFrameListener;
    private FullFrameRect mFullScreen;
    private boolean mHasWrittenFrame;
    private String mNextGhostPath;
    private MuxerManager mNextMuxer;
    private long mStartRecordingTimeNS;
    private SurfaceTexture mSurfaceTexture;
    private long FRAME_DURATION_MILLIS = 33;
    private boolean mEndRecordingAfterDrawFrame = false;
    private final float[] mSTMatrix = new float[16];
    private Texture2dProgram.ProgramType mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT;
    private int mTextureId = -1;
    private int mRecordingStatus = -1;
    private boolean mRecordingEnabled = false;
    private boolean mIncomingSizeUpdated = false;
    private int mIncomingHeight = -1;
    private int mIncomingWidth = -1;
    private int mCurrentFilter = -1;
    private int mNewFilter = 0;
    private TextureVideoEncoder mVideoEncoder = new TextureVideoEncoder(720, 720, avformat.AVFormatContext.RAW_PACKET_BUFFER_SIZE);

    public interface FrameCapturedListener {
        void onFrame(long j);
    }

    private class PrepareEncoderThread extends Thread {
        private PrepareEncoderThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            CameraSurfaceRenderer.this.mVideoEncoder = new TextureVideoEncoder(720, 720, avformat.AVFormatContext.RAW_PACKET_BUFFER_SIZE);
            CameraSurfaceRenderer.this.mVideoEncoder.prepareEncoder();
            CameraSurfaceRenderer.this.mRecordingStatus = 0;
        }
    }

    public CameraSurfaceRenderer(SetCameraSurfaceTextureHandler cameraHandler, FrameCapturedListener listener) {
        this.mCameraHandler = cameraHandler;
        this.mFrameListener = listener;
        this.mVideoEncoder.prepareEncoder();
    }

    public void notifyPausing() {
        if (this.mSurfaceTexture != null) {
            SLog.d("renderer pausing -- releasing SurfaceTexture");
            this.mSurfaceTexture.release();
            this.mSurfaceTexture = null;
        }
        if (this.mFullScreen != null) {
            this.mFullScreen.release(false);
            this.mFullScreen = null;
        }
        this.mIncomingHeight = -1;
        this.mIncomingWidth = -1;
    }

    public void startRecording(String ghostFile, MuxerManager manager) {
        this.mRecordingEnabled = true;
        this.mNextMuxer = manager;
        this.mNextGhostPath = ghostFile;
    }

    public void endRecording() {
        if (!this.mHasWrittenFrame) {
            this.mEndRecordingAfterDrawFrame = true;
        } else {
            this.mRecordingEnabled = false;
        }
    }

    public void changeFilterMode(int filter) {
        this.mNewFilter = filter;
    }

    public void updateFilter() {
        float[] kernel = null;
        float colorAdj = 0.0f;
        SLog.d("Updating filter to " + this.mNewFilter);
        switch (this.mNewFilter) {
            case 0:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT;
                break;
            case 1:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_FINGER_INPUT_MONO_DIRECTION;
                break;
            case 2:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_FILT;
                kernel = new float[]{0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f, 0.0625f, 0.125f, 0.0625f};
                break;
            case 3:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_FILT;
                kernel = new float[]{0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f};
                break;
            case 4:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_FILT;
                kernel = new float[]{-1.0f, -1.0f, -1.0f, -1.0f, 8.0f, -1.0f, -1.0f, -1.0f, -1.0f};
                break;
            case 5:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_FILT;
                kernel = new float[]{2.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f};
                colorAdj = 0.5f;
                break;
            case 6:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_RADIAL_COLOR_SPLIT_SQUARE;
                break;
            case 7:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_LENS_DISTORT;
                break;
            case 8:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_MIRROR;
                break;
            case 9:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_TIME_MODULATED;
                break;
            case 10:
                this.mProgramType = Texture2dProgram.ProgramType.TEXTURE_EXT_LENS_TWIST;
                break;
            default:
                throw new RuntimeException("Unknown filter mode " + this.mNewFilter);
        }
        if (this.mProgramType != this.mFullScreen.getProgram().getProgramType()) {
            this.mFullScreen.changeProgram(new Texture2dProgram(this.mProgramType));
            this.mIncomingSizeUpdated = true;
        }
        if (kernel != null) {
            this.mFullScreen.getProgram().setKernel(kernel, colorAdj);
        }
        this.mCurrentFilter = this.mNewFilter;
    }

    public void setCameraPreviewSize(int width, int height) {
        SLog.d("setCameraPreviewSize");
        this.mIncomingWidth = width;
        this.mIncomingHeight = height;
        this.mIncomingSizeUpdated = true;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        SLog.d("onSurfaceCreated");
        this.mRecordingEnabled = this.mVideoEncoder.isRecording();
        if (this.mRecordingEnabled) {
            this.mRecordingStatus = 2;
        } else {
            this.mRecordingStatus = 0;
        }
        this.mFullScreen = new FullFrameRect(new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT));
        this.mTextureId = this.mFullScreen.createTextureObject();
        this.mSurfaceTexture = new SurfaceTexture(this.mTextureId);
        this.mCameraHandler.sendMessage(this.mCameraHandler.obtainMessage(0, this.mSurfaceTexture));
        Thread.currentThread().setPriority(10);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        SLog.d("onSurfaceChanged " + width + "x" + height);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 unused) {
        this.mSurfaceTexture.updateTexImage();
        if (this.mRecordingEnabled) {
            switch (this.mRecordingStatus) {
                case 0:
                    this.mStartRecordingTimeNS = this.mSurfaceTexture.getTimestamp();
                    updateFilter();
                    this.mVideoEncoder.startRecording(EGL14.eglGetCurrentContext(), this.mFullScreen, this.mNextMuxer, this.mNextGhostPath, this.mProgramType);
                    this.mRecordingStatus = 1;
                    break;
                case 1:
                case 3:
                    break;
                case 2:
                    this.mVideoEncoder.updateSharedContext(EGL14.eglGetCurrentContext());
                    this.mRecordingStatus = 1;
                    break;
                default:
                    throw new RuntimeException("unknown status " + this.mRecordingStatus);
            }
        } else if (this.mHasWrittenFrame) {
            switch (this.mRecordingStatus) {
                case 0:
                case 3:
                    break;
                case 1:
                case 2:
                    SLog.d("STOP recording");
                    long milis = System.currentTimeMillis();
                    this.mRecordingStatus = 3;
                    this.mVideoEncoder.frameAvailable(this.mSurfaceTexture, true);
                    this.mVideoEncoder.stopRecording();
                    new PrepareEncoderThread().start();
                    this.mHasWrittenFrame = false;
                    SLog.d("ryango stop millis {}", Long.valueOf(System.currentTimeMillis() - milis));
                    break;
                default:
                    throw new RuntimeException("unknown status " + this.mRecordingStatus);
            }
        }
        this.mVideoEncoder.setTextureId(this.mTextureId);
        if (this.mRecordingStatus == 1) {
            this.mHasWrittenFrame = true;
            this.mFrameListener.onFrame(((this.mSurfaceTexture.getTimestamp() - this.mStartRecordingTimeNS) / 1000000) + this.FRAME_DURATION_MILLIS);
        }
        this.mVideoEncoder.frameAvailable(this.mSurfaceTexture, false);
        if (this.mIncomingWidth <= 0 || this.mIncomingHeight <= 0) {
            SLog.d("Drawing before incoming texture size set; skipping");
            return;
        }
        if (this.mCurrentFilter != this.mNewFilter) {
            updateFilter();
        }
        if (this.mIncomingSizeUpdated) {
            this.mFullScreen.getProgram().setTexSize(this.mIncomingWidth, this.mIncomingHeight);
            this.mIncomingSizeUpdated = false;
        }
        this.mSurfaceTexture.getTransformMatrix(this.mSTMatrix);
        this.mFullScreen.drawFrame(this.mTextureId, this.mSTMatrix);
        if (this.mEndRecordingAfterDrawFrame) {
            this.mRecordingEnabled = false;
            this.mEndRecordingAfterDrawFrame = false;
        }
    }
}
