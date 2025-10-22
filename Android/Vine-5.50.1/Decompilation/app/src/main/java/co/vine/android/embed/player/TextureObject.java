package co.vine.android.embed.player;

import android.graphics.SurfaceTexture;
import co.vine.android.gles.Texture2dProgram;

/* loaded from: classes.dex */
final class TextureObject {
    private boolean mReleased;
    public final SurfaceTexture texture;
    public final int textureId;

    private TextureObject(int textureId) {
        this.textureId = textureId;
        this.texture = new SurfaceTexture(textureId);
    }

    public static TextureObject create(int target) {
        return new TextureObject(Texture2dProgram.createTextureObject(target));
    }

    public void release() {
        this.mReleased = true;
        this.texture.release();
    }

    public boolean isValid() {
        return !this.mReleased;
    }
}
