package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
class ScriptIntrinsicConvolve5x5Thunker extends ScriptIntrinsicConvolve5x5 {
    android.renderscript.ScriptIntrinsicConvolve5x5 mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.Script, android.support.v8.renderscript.BaseObj
    public android.renderscript.ScriptIntrinsicConvolve5x5 getNObj() {
        return this.mN;
    }

    ScriptIntrinsicConvolve5x5Thunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicConvolve5x5Thunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicConvolve5x5Thunker si = new ScriptIntrinsicConvolve5x5Thunker(0, rs);
        si.mN = android.renderscript.ScriptIntrinsicConvolve5x5.create(rst.mN, et.getNObj());
        return si;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicConvolve5x5
    public void setInput(Allocation ain) {
        AllocationThunker aint = (AllocationThunker) ain;
        this.mN.setInput(aint.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicConvolve5x5
    public void setCoefficients(float[] v) {
        this.mN.setCoefficients(v);
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicConvolve5x5
    public void forEach(Allocation aout) {
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEach(aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicConvolve5x5
    public Script.KernelID getKernelID() {
        Script.KernelID k = createKernelID(0, 2, null, null);
        k.mN = this.mN.getKernelID();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicConvolve5x5
    public Script.FieldID getFieldID_Input() {
        Script.FieldID f = createFieldID(1, null);
        f.mN = this.mN.getFieldID_Input();
        return f;
    }
}
