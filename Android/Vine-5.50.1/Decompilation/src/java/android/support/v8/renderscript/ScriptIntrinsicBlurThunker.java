package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
class ScriptIntrinsicBlurThunker extends ScriptIntrinsicBlur {
    android.renderscript.ScriptIntrinsicBlur mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.Script, android.support.v8.renderscript.BaseObj
    public android.renderscript.ScriptIntrinsicBlur getNObj() {
        return this.mN;
    }

    protected ScriptIntrinsicBlurThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicBlurThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicBlurThunker blur = new ScriptIntrinsicBlurThunker(0, rs);
        blur.mN = android.renderscript.ScriptIntrinsicBlur.create(rst.mN, et.getNObj());
        return blur;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlur
    public void setInput(Allocation ain) {
        AllocationThunker aint = (AllocationThunker) ain;
        this.mN.setInput(aint.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlur
    public void setRadius(float radius) {
        this.mN.setRadius(radius);
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlur
    public void forEach(Allocation aout) {
        AllocationThunker aoutt = (AllocationThunker) aout;
        if (aoutt != null) {
            this.mN.forEach(aoutt.getNObj());
        }
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlur
    public Script.KernelID getKernelID() {
        Script.KernelID k = createKernelID(0, 2, null, null);
        k.mN = this.mN.getKernelID();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlur
    public Script.FieldID getFieldID_Input() {
        Script.FieldID f = createFieldID(1, null);
        f.mN = this.mN.getFieldID_Input();
        return f;
    }
}
