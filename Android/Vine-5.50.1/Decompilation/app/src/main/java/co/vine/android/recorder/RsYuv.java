package co.vine.android.recorder;

import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v8.renderscript.Type;
import co.vine.android.VineNotSupportedException;

/* loaded from: classes.dex */
public class RsYuv {
    private Allocation mAllocationIn;
    private Allocation mAllocationOut;
    private ScriptIntrinsicYuvToRGB mYuv;

    public RsYuv(RenderScript rs, int width, int height) throws VineNotSupportedException {
        try {
            this.mYuv = ScriptIntrinsicYuvToRGB.create(rs, Element.RGBA_8888(rs));
            Type.Builder tb = new Type.Builder(rs, Element.RGBA_8888(rs));
            tb.setX(width);
            tb.setY(height);
            this.mAllocationOut = Allocation.createTyped(rs, tb.create());
            this.mAllocationIn = Allocation.createSized(rs, Element.U8(rs), (height * width) + ((height / 2) * (width / 2) * 2));
            this.mYuv.setInput(this.mAllocationIn);
        } catch (Throwable th) {
            throw new VineNotSupportedException();
        }
    }

    public void execute(byte[] yuv, Bitmap b) {
        this.mAllocationIn.copyFrom(yuv);
        this.mYuv.forEach(this.mAllocationOut);
        this.mAllocationOut.copyTo(b);
    }
}
