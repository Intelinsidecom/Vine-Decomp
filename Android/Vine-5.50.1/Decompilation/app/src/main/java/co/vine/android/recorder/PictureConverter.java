package co.vine.android.recorder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.renderscript.RSInvalidStateException;
import android.support.v8.renderscript.RenderScript;
import co.vine.android.VineNotSupportedException;
import co.vine.android.recorder.camera.CameraSetting;
import co.vine.android.recorder.video.VideoData;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class PictureConverter {
    private int lastH;
    private int lastW;
    private RsYuv mFilterYuv;
    private boolean mLastFront;
    private RenderScript mRS;
    private ScaleStep mScaleStep;
    private final int mTargetSize;
    private Bitmap srcBmp;
    private int mLastDegree = -99999;
    public final int[] LOCK = new int[0];
    private boolean mLastMirrored = false;
    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint = new Paint();

    public class ScaleStep {
        private final Bitmap mPreScaleBitmap;
        private final Canvas mPreScaleCanvas;
        private final Matrix mPreScaleMatrix;
        private final int scaledH;
        private final int scaledW;

        private ScaleStep(float w2h) {
            this.mPreScaleMatrix = new Matrix();
            this.scaledW = (int) (PictureConverter.this.lastH * w2h);
            this.scaledH = PictureConverter.this.lastH;
            this.mPreScaleMatrix.setScale(this.scaledW / PictureConverter.this.lastW, 1.0f);
            this.mPreScaleBitmap = Bitmap.createBitmap(this.scaledW, this.scaledH, RecordConfigUtils.DEFAULT_BITMAP_CONFIG);
            this.mPreScaleCanvas = new Canvas(this.mPreScaleBitmap);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void scale() {
            this.mPreScaleCanvas.drawBitmap(PictureConverter.this.srcBmp, this.mPreScaleMatrix, PictureConverter.this.mPaint);
        }
    }

    public PictureConverter(RenderScript rs, int targetSize, CameraSetting cameraSetting) throws VineNotSupportedException {
        this.mRS = rs;
        this.mTargetSize = targetSize;
        try {
            setCameraSetting(cameraSetting);
        } catch (Exception e) {
            throw new VineNotSupportedException();
        }
    }

    public void draw(Canvas canvas) {
        if (this.mScaleStep != null) {
            this.mScaleStep.scale();
            canvas.drawBitmap(this.mScaleStep.mPreScaleBitmap, this.mMatrix, this.mPaint);
        } else {
            canvas.drawBitmap(this.srcBmp, this.mMatrix, this.mPaint);
        }
    }

    public void drawGeneric(Canvas canvas, Bitmap bitmap, Matrix matrix) {
        canvas.drawBitmap(bitmap, matrix, this.mPaint);
    }

    public boolean updateSettingsIfNeeded(CameraSetting cameraSetting) {
        boolean hasChanged = (this.lastW == cameraSetting.originalW && this.lastH == cameraSetting.originalH && cameraSetting.frontFacing == this.mLastFront) ? false : true;
        if (this.srcBmp == null || hasChanged) {
            try {
                setCameraSetting(cameraSetting);
            } catch (VineNotSupportedException e) {
                CrashUtil.log("Failed to make RsYuv, it should never reach here.");
            }
        }
        return hasChanged;
    }

    private void setCameraSetting(CameraSetting cameraSetting) throws VineNotSupportedException {
        if (this.srcBmp != null) {
            this.srcBmp.recycle();
            this.srcBmp = null;
        }
        this.srcBmp = Bitmap.createBitmap(cameraSetting.originalW, cameraSetting.originalH, RecordConfigUtils.DEFAULT_BITMAP_CONFIG);
        int currentFrameSize = cameraSetting.originalW * cameraSetting.originalH;
        this.lastH = cameraSetting.originalH;
        this.lastW = cameraSetting.originalW;
        this.mLastFront = cameraSetting.frontFacing;
        if (cameraSetting.frontFacing && cameraSetting.frontFacingAspectRatio > 0.0f) {
            this.mScaleStep = new ScaleStep(cameraSetting.frontFacingAspectRatio);
        } else if (cameraSetting.backFacingAspectRatio > 0.0f) {
            this.mScaleStep = new ScaleStep(cameraSetting.backFacingAspectRatio);
        } else {
            this.mScaleStep = null;
        }
        giveMatrixNewValuesWithScaleIfDegreeHasChanged(cameraSetting.degrees, false);
        allocate(currentFrameSize, cameraSetting.originalW, cameraSetting.originalH);
    }

    public void giveMatrixNewValuesWithScaleIfDegreeHasChangedWithKnownConfigs(int degrees, boolean frontFacing) {
        giveMatrixNewValuesWithScaleIfDegreeHasChanged(degrees, frontFacing);
    }

    public boolean convert(CameraSetting cameraSetting, VideoData next, boolean hasChanged) {
        try {
            convertUsingRenderScript(cameraSetting, next, hasChanged);
            return true;
        } catch (RSInvalidStateException e) {
            return false;
        } catch (android.support.v8.renderscript.RSInvalidStateException e2) {
            return false;
        }
    }

    private void convertUsingRenderScript(CameraSetting cameraSetting, VideoData next, boolean hasChanged) {
        if (next.data == null) {
            CrashUtil.log("Data is null.");
        }
        if (this.mRS != null && (this.mFilterYuv == null || hasChanged)) {
            try {
                this.mFilterYuv = new RsYuv(this.mRS, cameraSetting.originalW, cameraSetting.originalH);
            } catch (VineNotSupportedException e) {
                CrashUtil.log("Failed to RsYuv, it should never reach here.");
            }
        }
        this.mFilterYuv.execute(next.data, this.srcBmp);
    }

    private void giveMatrixNewValuesWithScaleIfDegreeHasChanged(int degrees, boolean mirror) {
        if (this.mLastDegree != degrees || this.mLastMirrored != mirror) {
            int width = this.mScaleStep == null ? this.lastW : this.mScaleStep.scaledW;
            int height = this.mScaleStep == null ? this.lastH : this.mScaleStep.scaledH;
            this.mMatrix.reset();
            float scaleFactor = Math.max(this.mTargetSize / width, this.mTargetSize / height);
            float actualWidth = width * scaleFactor;
            float actualHeight = height * scaleFactor;
            float offsetX = (-(actualWidth - this.mTargetSize)) / 2.0f;
            float offsetY = (-(actualHeight - this.mTargetSize)) / 2.0f;
            this.mMatrix.postRotate(degrees, width / 2, height / 2);
            SLog.d("Creating with mirrored {}.", Boolean.valueOf(mirror));
            if (mirror) {
                this.mMatrix.postScale(-scaleFactor, scaleFactor);
                this.mMatrix.postTranslate((-offsetX) + this.mTargetSize, offsetY);
            } else {
                this.mMatrix.postScale(scaleFactor, scaleFactor);
                this.mMatrix.postTranslate(offsetX, offsetY);
            }
            this.mLastMirrored = mirror;
            this.mLastDegree = degrees;
        }
    }

    private void allocate(int frameSize, int originalW, int originalH) throws VineNotSupportedException {
        this.mFilterYuv = new RsYuv(this.mRS, originalW, originalH);
    }
}
