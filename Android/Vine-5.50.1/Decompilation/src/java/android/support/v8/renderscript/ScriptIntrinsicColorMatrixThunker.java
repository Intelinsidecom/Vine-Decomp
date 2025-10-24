package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
class ScriptIntrinsicColorMatrixThunker extends ScriptIntrinsicColorMatrix {
    android.renderscript.ScriptIntrinsicColorMatrix mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.Script, android.support.v8.renderscript.BaseObj
    public android.renderscript.ScriptIntrinsicColorMatrix getNObj() {
        return this.mN;
    }

    private ScriptIntrinsicColorMatrixThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicColorMatrixThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicColorMatrixThunker cm = new ScriptIntrinsicColorMatrixThunker(0, rs);
        cm.mN = android.renderscript.ScriptIntrinsicColorMatrix.create(rst.mN, et.getNObj());
        return cm;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicColorMatrix
    public void setColorMatrix(Matrix4f m) {
        this.mN.setColorMatrix(new android.renderscript.Matrix4f(m.getArray()));
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicColorMatrix
    public void setColorMatrix(Matrix3f m) {
        this.mN.setColorMatrix(new android.renderscript.Matrix3f(m.getArray()));
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicColorMatrix
    public void setGreyscale() {
        this.mN.setGreyscale();
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicColorMatrix
    public void setYUVtoRGB() {
        this.mN.setYUVtoRGB();
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicColorMatrix
    public void setRGBtoYUV() {
        this.mN.setRGBtoYUV();
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicColorMatrix
    public void forEach(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEach(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicColorMatrix
    public Script.KernelID getKernelID() {
        Script.KernelID k = createKernelID(0, 3, null, null);
        k.mN = this.mN.getKernelID();
        return k;
    }
}
