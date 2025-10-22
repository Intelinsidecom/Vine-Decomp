package co.vine.android.recorder.camera;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/* loaded from: classes.dex */
public class CameraSetting implements Externalizable {
    private static final long serialVersionUID = 8590112321194730309L;
    public float backFacingAspectRatio;
    public int degrees;
    public int fps;
    public boolean frontFacing;
    public float frontFacingAspectRatio;
    public int imageFormat;
    public int originalFrameRate;
    public int originalH;
    public int originalW;
    public int tpf;

    public CameraSetting() {
    }

    public CameraSetting(int originalW, int originalH, int degrees, int fps, int imageFormat, boolean frontFacing, float frontFacingAspectRatio, float backFacingAspectRatio) {
        this.originalW = originalW;
        this.originalH = originalH;
        this.degrees = degrees;
        this.fps = fps;
        this.originalFrameRate = fps;
        this.tpf = 1000 / fps;
        this.imageFormat = imageFormat;
        this.frontFacing = frontFacing;
        this.frontFacingAspectRatio = frontFacingAspectRatio;
        this.backFacingAspectRatio = backFacingAspectRatio;
    }

    public CameraSetting(CameraSetting setting) {
        this.originalW = setting.originalW;
        this.originalH = setting.originalH;
        this.degrees = setting.degrees;
        this.fps = setting.fps;
        this.originalFrameRate = setting.originalFrameRate;
        this.tpf = setting.tpf;
        this.imageFormat = setting.imageFormat;
        this.frontFacing = setting.frontFacing;
        this.frontFacingAspectRatio = setting.frontFacingAspectRatio;
        this.backFacingAspectRatio = setting.backFacingAspectRatio;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        this.originalW = input.readInt();
        this.originalH = input.readInt();
        this.degrees = input.readInt();
        this.fps = input.readInt();
        this.tpf = input.readInt();
        this.imageFormat = input.readInt();
        this.frontFacing = input.readBoolean();
        this.frontFacingAspectRatio = input.readFloat();
        this.backFacingAspectRatio = input.readFloat();
        this.originalFrameRate = input.readInt();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeInt(this.originalW);
        output.writeInt(this.originalH);
        output.writeInt(this.degrees);
        output.writeInt(this.fps);
        output.writeInt(this.tpf);
        output.writeInt(this.imageFormat);
        output.writeBoolean(this.frontFacing);
        output.writeFloat(this.frontFacingAspectRatio);
        output.writeFloat(this.backFacingAspectRatio);
        output.writeInt(this.originalFrameRate);
    }
}
