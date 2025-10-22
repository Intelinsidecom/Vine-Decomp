package co.vine.android.gles;

import co.vine.android.gles.CameraRenderDrawable2D;

/* loaded from: classes.dex */
public class FullFrameRect {
    private Texture2dProgram mProgram;
    private final CameraRenderDrawable2D mRectDrawable = new CameraRenderDrawable2D(CameraRenderDrawable2D.Prefab.FULL_RECTANGLE);

    public FullFrameRect(Texture2dProgram program) {
        this.mProgram = program;
    }

    public void release(boolean doEglCleanup) {
        if (this.mProgram != null) {
            if (doEglCleanup) {
                this.mProgram.release();
            }
            this.mProgram = null;
        }
    }

    public Texture2dProgram getProgram() {
        return this.mProgram;
    }

    public void drawFrame(int textureId, float[] texMatrix) {
        this.mProgram.draw(GlUtil.IDENTITY_MATRIX, this.mRectDrawable.getVertexArray(), 0, this.mRectDrawable.getVertexCount(), this.mRectDrawable.getCoordsPerVertex(), this.mRectDrawable.getVertexStride(), texMatrix, this.mRectDrawable.getTexCoordArray(), textureId, this.mRectDrawable.getTexCoordStride());
    }
}
