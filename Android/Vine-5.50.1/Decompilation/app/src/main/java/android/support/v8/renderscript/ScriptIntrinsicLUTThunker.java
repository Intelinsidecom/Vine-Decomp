package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
class ScriptIntrinsicLUTThunker extends ScriptIntrinsicLUT {
    android.renderscript.ScriptIntrinsicLUT mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.Script, android.support.v8.renderscript.BaseObj
    public android.renderscript.ScriptIntrinsicLUT getNObj() {
        return this.mN;
    }

    private ScriptIntrinsicLUTThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicLUTThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicLUTThunker si = new ScriptIntrinsicLUTThunker(0, rs);
        si.mN = android.renderscript.ScriptIntrinsicLUT.create(rst.mN, et.getNObj());
        return si;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicLUT
    public void setRed(int index, int value) {
        this.mN.setRed(index, value);
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicLUT
    public void setGreen(int index, int value) {
        this.mN.setGreen(index, value);
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicLUT
    public void setBlue(int index, int value) {
        this.mN.setBlue(index, value);
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicLUT
    public void setAlpha(int index, int value) {
        this.mN.setAlpha(index, value);
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicLUT
    public void forEach(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEach(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicLUT
    public Script.KernelID getKernelID() {
        Script.KernelID k = createKernelID(0, 3, null, null);
        k.mN = this.mN.getKernelID();
        return k;
    }
}
