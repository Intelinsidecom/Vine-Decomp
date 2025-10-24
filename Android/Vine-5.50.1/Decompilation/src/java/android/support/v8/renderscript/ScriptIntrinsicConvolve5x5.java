package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
public class ScriptIntrinsicConvolve5x5 extends ScriptIntrinsic {
    private Allocation mInput;
    private final float[] mValues;

    ScriptIntrinsicConvolve5x5(int id, RenderScript rs) {
        super(id, rs);
        this.mValues = new float[25];
    }

    public static ScriptIntrinsicConvolve5x5 create(RenderScript rs, Element e) {
        if (RenderScript.isNative) {
            return ScriptIntrinsicConvolve5x5Thunker.create(rs, e);
        }
        int id = rs.nScriptIntrinsicCreate(4, e.getID(rs));
        return new ScriptIntrinsicConvolve5x5(id, rs);
    }

    public void setInput(Allocation ain) {
        this.mInput = ain;
        setVar(1, ain);
    }

    public void setCoefficients(float[] v) {
        FieldPacker fp = new FieldPacker(100);
        for (int ct = 0; ct < this.mValues.length; ct++) {
            this.mValues[ct] = v[ct];
            fp.addF32(this.mValues[ct]);
        }
        setVar(0, fp);
    }

    public void forEach(Allocation aout) {
        forEach(0, null, aout, null);
    }

    public Script.KernelID getKernelID() {
        return createKernelID(0, 2, null, null);
    }

    public Script.FieldID getFieldID_Input() {
        return createFieldID(1, null);
    }
}
