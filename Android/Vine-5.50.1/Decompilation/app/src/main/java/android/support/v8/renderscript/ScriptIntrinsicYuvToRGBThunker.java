package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
public class ScriptIntrinsicYuvToRGBThunker extends ScriptIntrinsicYuvToRGB {
    android.renderscript.ScriptIntrinsicYuvToRGB mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.Script, android.support.v8.renderscript.BaseObj
    public android.renderscript.ScriptIntrinsicYuvToRGB getNObj() {
        return this.mN;
    }

    private ScriptIntrinsicYuvToRGBThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicYuvToRGBThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicYuvToRGBThunker si = new ScriptIntrinsicYuvToRGBThunker(0, rs);
        si.mN = android.renderscript.ScriptIntrinsicYuvToRGB.create(rst.mN, et.getNObj());
        return si;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicYuvToRGB
    public void setInput(Allocation ain) {
        AllocationThunker aint = (AllocationThunker) ain;
        this.mN.setInput(aint.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicYuvToRGB
    public void forEach(Allocation aout) {
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEach(aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicYuvToRGB
    public Script.KernelID getKernelID() {
        Script.KernelID k = createKernelID(0, 2, null, null);
        k.mN = this.mN.getKernelID();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicYuvToRGB
    public Script.FieldID getFieldID_Input() {
        Script.FieldID f = createFieldID(0, null);
        f.mN = this.mN.getFieldID_Input();
        return f;
    }
}
