package co.vine.android.recorder2.gles;

import com.edisonwang.android.slog.SLog;
import java.nio.FloatBuffer;

/* loaded from: classes.dex */
public class FullFrameRect {
    private static FloatBuffer ASPECT_RATIO_ADJUSTED_TEX_COORDS_BUF;
    private Texture2dProgram mProgram;
    private static final float[] FULL_DISPLAY_COORDS = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    private static final FloatBuffer FULL_DISPLAY_BUF = GlUtil.createFloatBuffer(FULL_DISPLAY_COORDS);
    private static final int VERTEX_COUNT = FULL_DISPLAY_COORDS.length / 2;

    public static void setAspectRatioAndGenerateGlobalTextureCoordinates(float aspectRatio) {
        Texture2dProgram.sAspectRatio = aspectRatio;
        float bottom = (1.0f - (1.0f / aspectRatio)) / 2.0f;
        float[] texCoords = {0.0f, bottom, 1.0f, bottom, 0.0f, 1.0f - bottom, 1.0f, 1.0f - bottom};
        ASPECT_RATIO_ADJUSTED_TEX_COORDS_BUF = GlUtil.createFloatBuffer(texCoords);
        SLog.d("ryango aspect {}", Float.valueOf(aspectRatio));
    }

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

    public void changeProgram(Texture2dProgram program) {
        this.mProgram.release();
        this.mProgram = program;
    }

    public int createTextureObject() {
        return this.mProgram.createTextureObject();
    }

    public void drawFrame(int textureId, float[] texMatrix) {
        if (this.mProgram != null) {
            this.mProgram.draw(GlUtil.IDENTITY_MATRIX, FULL_DISPLAY_BUF, 0, VERTEX_COUNT, 2, 8, texMatrix, ASPECT_RATIO_ADJUSTED_TEX_COORDS_BUF, textureId, 8);
        }
    }
}
