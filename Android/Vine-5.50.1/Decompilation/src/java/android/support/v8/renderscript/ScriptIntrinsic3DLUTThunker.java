package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
class ScriptIntrinsic3DLUTThunker extends ScriptIntrinsic3DLUT {
    android.renderscript.ScriptIntrinsic3DLUT mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.Script, android.support.v8.renderscript.BaseObj
    public android.renderscript.ScriptIntrinsic3DLUT getNObj() {
        return this.mN;
    }

    private ScriptIntrinsic3DLUTThunker(int id, RenderScript rs, Element e) {
        super(id, rs, e);
    }

    public static ScriptIntrinsic3DLUTThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsic3DLUTThunker lut = new ScriptIntrinsic3DLUTThunker(0, rs, e);
        lut.mN = android.renderscript.ScriptIntrinsic3DLUT.create(rst.mN, et.getNObj());
        return lut;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsic3DLUT
    public void setLUT(Allocation lut) {
        AllocationThunker lutt = (AllocationThunker) lut;
        this.mN.setLUT(lutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsic3DLUT
    public void forEach(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEach(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsic3DLUT
    public Script.KernelID getKernelID() {
        Script.KernelID k = createKernelID(0, 3, null, null);
        k.mN = this.mN.getKernelID();
        return k;
    }
}
