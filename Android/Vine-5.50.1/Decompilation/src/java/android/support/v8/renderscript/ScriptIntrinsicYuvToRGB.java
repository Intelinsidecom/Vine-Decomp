package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
public class ScriptIntrinsicYuvToRGB extends ScriptIntrinsic {
    private Allocation mInput;

    ScriptIntrinsicYuvToRGB(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicYuvToRGB create(RenderScript rs, Element e) {
        if (RenderScript.isNative) {
            return ScriptIntrinsicYuvToRGBThunker.create(rs, e);
        }
        int id = rs.nScriptIntrinsicCreate(6, e.getID(rs));
        return new ScriptIntrinsicYuvToRGB(id, rs);
    }

    public void setInput(Allocation ain) {
        this.mInput = ain;
        setVar(0, ain);
    }

    public void forEach(Allocation aout) {
        forEach(0, null, aout, null);
    }

    public Script.KernelID getKernelID() {
        return createKernelID(0, 2, null, null);
    }

    public Script.FieldID getFieldID_Input() {
        return createFieldID(0, null);
    }
}
