package co.vine.android.recorder2.gles;

import android.opengl.GLES20;
import android.util.Log;
import java.nio.Buffer;
import java.nio.FloatBuffer;

/* loaded from: classes.dex */
public class Texture2dProgram {
    private float mColorAdjust;
    private float[] mKernel = new float[9];
    private int mProgramHandle;
    private ProgramType mProgramType;
    private float[] mTexOffset;
    private int mTextureTarget;
    private int maPositionLoc;
    private int maTextureCoordLoc;
    private int muColorAdjustLoc;
    private int muHorizOffsetLoc;
    private int muKernelLoc;
    private int muMVPMatrixLoc;
    private int muTexMatrixLoc;
    private int muTexOffsetLoc;
    private int muTimeOffsetLoc;
    private int muVertOffsetLoc;
    public static float sVertOffset = 0.0f;
    public static float sHorizOffset = 0.0f;
    public static float sAspectRatio = 1.0f;

    public enum ProgramType {
        TEXTURE_2D,
        TEXTURE_EXT,
        TEXTURE_EXT_FINGER_INPUT_MONO_DIRECTION,
        TEXTURE_EXT_FILT,
        TEXTURE_EXT_RADIAL_COLOR_SPLIT_SQUARE,
        TEXTURE_EXT_MIRROR,
        TEXTURE_EXT_TIME_MODULATED,
        TEXTURE_EXT_LENS_TWIST,
        TEXTURE_EXT_LENS_DISTORT
    }

    private static final String getCircleShader() {
        return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform float uVertOffset;\nuniform float uHorizOffset;\nuniform float uTime;\nuniform samplerExternalOES sTexture;\nvoid main() {\n    vec2 red = vec2(vTextureCoord.x + uVertOffset / 2.0 * cos(uTime), vTextureCoord.y + uVertOffset / 2.0 * " + sAspectRatio + " * sin(uTime));\n    vec2 green = vec2(vTextureCoord.x + uVertOffset / 2.0 * cos(uTime + 3.141592653589793), vTextureCoord.y + uVertOffset / 2.0 * " + sAspectRatio + " * sin(uTime + 3.141592653589793));\n    vec4 otherred = texture2D(sTexture, red);\n    vec4 othergreen = texture2D(sTexture, green);\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(otherred.r, othergreen.g, tc.b, 1.0);\n}\n";
    }

    public static String getRadialSpreadShaderSquare() {
        return "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float uVertOffset;\nuniform float uHorizOffset;\nuniform samplerExternalOES sTexture;\nvoid getDistort(in vec2 point, in float divisor, out vec2 outpoint) {\n\n    vec2 delta = vec2(point.x - .5, point.y - .5);\n    vec2 deltaadj = vec2(vTextureCoord.x * " + (sAspectRatio - ((sAspectRatio - 1.0f) / 2.0f)) + " - .5 * " + (sAspectRatio - ((sAspectRatio - 1.0f) / 2.0f)) + " , vTextureCoord.y - .5);\n    float angle = atan(delta.y, delta.x);\n    float dist = sqrt(dot(delta, delta));\n    float distadj = sqrt(dot(deltaadj, deltaadj));\n    float distSq = distadj * distadj;\n    float cosang = cos(angle);\n    float sinang = sin(angle);\n    float xNew = (dist + (uVertOffset) / divisor * distSq) * cosang;\n    float yNew = (dist + (uVertOffset) / divisor * distSq) * sinang;\n    outpoint = vec2(xNew + .5, yNew + .5);\n}\nvoid main() {\n    float deltaY = vTextureCoord.y - 0.5;\n    float deltaYSq = deltaY * deltaY;\n// * deltaY * deltaY;\n    float signDeltaY = sign(deltaY);\n    float deltaX = vTextureCoord.x - 0.5;\n    float deltaXSq = deltaX * deltaX;\n// * deltaX * deltaX;\n    float signDeltaX = sign(deltaX);\n    vec2 red = vec2(1.0);\n    vec2 green = vec2(1.0);\n    getDistort(vTextureCoord, 1.0, red);\n    getDistort(vTextureCoord, 2.0, green);\n    vec4 redcol = texture2D(sTexture, red);\n    vec4 greencol = texture2D(sTexture, green);\n    vec4 tec = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(redcol.r, greencol.g, tec.b, 1.0);\n}\n";
    }

    public static String getLensDistortShader() {
        return "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float uVertOffset;\nuniform float uHorizOffset;\nuniform samplerExternalOES sTexture;\nvoid main() {\n    vec2 delta = vec2(vTextureCoord.x - .5, vTextureCoord.y - .5);\n    vec2 deltaadj = vec2(vTextureCoord.x * " + (sAspectRatio - ((sAspectRatio - 1.0f) / 2.0f)) + " - .5 * " + (sAspectRatio - ((sAspectRatio - 1.0f) / 2.0f)) + " , vTextureCoord.y - .5);\n    float angle = atan(delta.y, delta.x);\n    float dist = sqrt(dot(delta, delta));\n    float distadj = sqrt(dot(deltaadj, deltaadj));\n    float distSq = distadj * distadj;\n    float cosang = cos(angle);\n    float sinang = sin(angle);\n    float xNew = (dist + (uVertOffset) * distSq) * cosang;\n    float yNew = (dist + (uVertOffset) * distSq) * sinang;\n    vec2 point = vec2(xNew + .5, yNew + .5);\n    gl_FragColor = texture2D(sTexture, point);\n}\n";
    }

    public static String getTwistShader() {
        return "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float uVertOffset;\nuniform float uHorizOffset;\nuniform samplerExternalOES sTexture;\nvoid main() {\n    vec2 delta = vec2(vTextureCoord.x - .5, vTextureCoord.y - .5);\n    vec2 deltaadj = vec2(vTextureCoord.x * " + (sAspectRatio - ((sAspectRatio - 1.0f) / 2.0f)) + " - .5 * " + (sAspectRatio - ((sAspectRatio - 1.0f) / 2.0f)) + " , vTextureCoord.y - .5);\n    float angle = atan(delta.y, delta.x);\n    float dist = sqrt(dot(delta, delta));\n    float distadj = sqrt(dot(deltaadj, deltaadj));\n    float cosang = cos(angle + uVertOffset * distadj);\n    float sinang = sin(angle + uVertOffset * distadj);\n    float xNew = (dist) * cosang;\n    float yNew = (dist) * sinang;\n    vec2 point = vec2(xNew + .5, yNew + .5);\n    gl_FragColor = texture2D(sTexture, point);\n}\n";
    }

    public Texture2dProgram(ProgramType programType) {
        this.mProgramType = programType;
        switch (programType) {
            case TEXTURE_2D:
                this.mTextureTarget = 3553;
                this.mProgramHandle = GlUtil.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
                break;
            case TEXTURE_EXT:
                this.mTextureTarget = 36197;
                this.mProgramHandle = GlUtil.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float uVertOffset;\nuniform float uHorizOffset;\nuniform samplerExternalOES sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
                break;
            case TEXTURE_EXT_FINGER_INPUT_MONO_DIRECTION:
                this.mTextureTarget = 36197;
                this.mProgramHandle = GlUtil.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform float uVertOffset;\nuniform float uHorizOffset;\nuniform samplerExternalOES sTexture;\nvoid main() {\n    vec2 red = vec2(vTextureCoord.x + uVertOffset, vTextureCoord.y + uHorizOffset);\n    vec2 green = vec2(vTextureCoord.x + uVertOffset / 2.0, vTextureCoord.y + uHorizOffset / 2.0);\n    vec4 otherred = texture2D(sTexture, red);\n    vec4 othergreen = texture2D(sTexture, green);\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    gl_FragColor = vec4(otherred.r, othergreen.g, tc.b, 1.0);\n}\n");
                break;
            case TEXTURE_EXT_FILT:
                this.mTextureTarget = 36197;
                this.mProgramHandle = GlUtil.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#extension GL_OES_EGL_image_external : require\n#define KERNEL_SIZE 9\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform float uKernel[KERNEL_SIZE];\nuniform vec2 uTexOffset[KERNEL_SIZE];\nuniform float uColorAdjust;\nvoid main() {\n    int i = 0;\n    vec4 sum = vec4(0.0);\n    for (i = 0; i < KERNEL_SIZE; i++) {\n        vec4 texc = texture2D(sTexture, vTextureCoord + uTexOffset[i]);\n        sum += texc * uKernel[i];\n    }\n    sum += uColorAdjust;\n    gl_FragColor = sum;\n}\n");
                break;
            case TEXTURE_EXT_RADIAL_COLOR_SPLIT_SQUARE:
                this.mTextureTarget = 36197;
                this.mProgramHandle = GlUtil.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", getRadialSpreadShaderSquare());
                break;
            case TEXTURE_EXT_LENS_DISTORT:
                this.mTextureTarget = 36197;
                this.mProgramHandle = GlUtil.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", getLensDistortShader());
                break;
            case TEXTURE_EXT_LENS_TWIST:
                this.mTextureTarget = 36197;
                this.mProgramHandle = GlUtil.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", getTwistShader());
                break;
            case TEXTURE_EXT_MIRROR:
                this.mTextureTarget = 36197;
                this.mProgramHandle = GlUtil.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform float uVertOffset;\nuniform float uHorizOffset;\nuniform samplerExternalOES sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vec2(vTextureCoord.x, abs(vTextureCoord.y - .5) + .5));\n}\n");
                break;
            case TEXTURE_EXT_TIME_MODULATED:
                this.mTextureTarget = 36197;
                this.mProgramHandle = GlUtil.createProgram("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", getCircleShader());
                break;
            default:
                throw new RuntimeException("Unhandled type " + programType);
        }
        if (this.mProgramHandle == 0) {
            throw new RuntimeException("Unable to create program");
        }
        Log.d("Grafika", "Created program " + this.mProgramHandle + " (" + programType + ")");
        this.maPositionLoc = GLES20.glGetAttribLocation(this.mProgramHandle, "aPosition");
        GlUtil.checkLocation(this.maPositionLoc, "aPosition");
        this.maTextureCoordLoc = GLES20.glGetAttribLocation(this.mProgramHandle, "aTextureCoord");
        GlUtil.checkLocation(this.maTextureCoordLoc, "aTextureCoord");
        this.muMVPMatrixLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uMVPMatrix");
        GlUtil.checkLocation(this.muMVPMatrixLoc, "uMVPMatrix");
        this.muTexMatrixLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uTexMatrix");
        GlUtil.checkLocation(this.muTexMatrixLoc, "uTexMatrix");
        this.muKernelLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uKernel");
        this.muVertOffsetLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uVertOffset");
        this.muHorizOffsetLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uHorizOffset");
        this.muTimeOffsetLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uTime");
        if (this.muKernelLoc < 0) {
            this.muKernelLoc = -1;
            this.muTexOffsetLoc = -1;
            this.muColorAdjustLoc = -1;
        } else {
            this.muTexOffsetLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uTexOffset");
            GlUtil.checkLocation(this.muTexOffsetLoc, "uTexOffset");
            this.muColorAdjustLoc = GLES20.glGetUniformLocation(this.mProgramHandle, "uColorAdjust");
            GlUtil.checkLocation(this.muColorAdjustLoc, "uColorAdjust");
            setKernel(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f}, 0.0f);
            setTexSize(256, 256);
        }
    }

    public void release() {
        Log.d("Grafika", "deleting program " + this.mProgramHandle);
        GLES20.glDeleteProgram(this.mProgramHandle);
        this.mProgramHandle = -1;
    }

    public ProgramType getProgramType() {
        return this.mProgramType;
    }

    public int createTextureObject() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GlUtil.checkGlError("glGenTextures");
        int texId = textures[0];
        GLES20.glBindTexture(this.mTextureTarget, texId);
        GlUtil.checkGlError("glBindTexture " + texId);
        GLES20.glTexParameterf(36197, 10241, 9729.0f);
        GLES20.glTexParameterf(36197, 10240, 9729.0f);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        GlUtil.checkGlError("glTexParameter");
        return texId;
    }

    public void setKernel(float[] values, float colorAdj) {
        if (values.length != 9) {
            throw new IllegalArgumentException("Kernel size is " + values.length + " vs. 9");
        }
        System.arraycopy(values, 0, this.mKernel, 0, 9);
        this.mColorAdjust = colorAdj;
    }

    public void setTexSize(int width, int height) {
        float rw = 1.0f / width;
        float rh = 1.0f / height;
        this.mTexOffset = new float[]{-rw, -rh, 0.0f, -rh, rw, -rh, -rw, 0.0f, 0.0f, 0.0f, rw, 0.0f, -rw, rh, 0.0f, rh, rw, rh};
    }

    public void draw(float[] mvpMatrix, FloatBuffer vertexBuffer, int firstVertex, int vertexCount, int coordsPerVertex, int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int textureId, int texStride) {
        GlUtil.checkGlError("draw start");
        GLES20.glUseProgram(this.mProgramHandle);
        GlUtil.checkGlError("glUseProgram");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(this.mTextureTarget, textureId);
        GLES20.glUniformMatrix4fv(this.muMVPMatrixLoc, 1, false, mvpMatrix, 0);
        GlUtil.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(this.muTexMatrixLoc, 1, false, texMatrix, 0);
        GlUtil.checkGlError("glUniformMatrix4fv");
        GLES20.glEnableVertexAttribArray(this.maPositionLoc);
        GlUtil.checkGlError("glEnableVertexAttribArray");
        GLES20.glVertexAttribPointer(this.maPositionLoc, coordsPerVertex, 5126, false, vertexStride, (Buffer) vertexBuffer);
        GlUtil.checkGlError("glVertexAttribPointer");
        GLES20.glEnableVertexAttribArray(this.maTextureCoordLoc);
        GlUtil.checkGlError("glEnableVertexAttribArray");
        GLES20.glVertexAttribPointer(this.maTextureCoordLoc, 2, 5126, false, texStride, (Buffer) texBuffer);
        GlUtil.checkGlError("glVertexAttribPointer");
        if (this.muKernelLoc >= 0) {
            GLES20.glUniform1fv(this.muKernelLoc, 9, this.mKernel, 0);
            GLES20.glUniform2fv(this.muTexOffsetLoc, 9, this.mTexOffset, 0);
            GLES20.glUniform1f(this.muColorAdjustLoc, this.mColorAdjust);
        }
        long time = System.currentTimeMillis();
        GLES20.glUniform1f(this.muVertOffsetLoc, sVertOffset);
        GLES20.glUniform1f(this.muHorizOffsetLoc, sHorizOffset);
        float f = (float) ((6.283185307179586d * (time % 1000)) / 1000.0d);
        GLES20.glUniform1f(this.muTimeOffsetLoc, f);
        GLES20.glDrawArrays(5, firstVertex, vertexCount);
        GlUtil.checkGlError("glDrawArrays");
        GLES20.glDisableVertexAttribArray(this.maPositionLoc);
        GLES20.glDisableVertexAttribArray(this.maTextureCoordLoc);
        GLES20.glBindTexture(this.mTextureTarget, 0);
        GLES20.glUseProgram(0);
    }
}
