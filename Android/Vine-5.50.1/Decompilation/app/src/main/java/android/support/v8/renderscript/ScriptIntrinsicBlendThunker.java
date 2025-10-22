package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
class ScriptIntrinsicBlendThunker extends ScriptIntrinsicBlend {
    android.renderscript.ScriptIntrinsicBlend mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.Script, android.support.v8.renderscript.BaseObj
    public android.renderscript.ScriptIntrinsicBlend getNObj() {
        return this.mN;
    }

    ScriptIntrinsicBlendThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    public static ScriptIntrinsicBlendThunker create(RenderScript rs, Element e) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        ElementThunker et = (ElementThunker) e;
        ScriptIntrinsicBlendThunker blend = new ScriptIntrinsicBlendThunker(0, rs);
        blend.mN = android.renderscript.ScriptIntrinsicBlend.create(rst.mN, et.getNObj());
        return blend;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachClear(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachClear(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDClear() {
        Script.KernelID k = createKernelID(0, 3, null, null);
        k.mN = this.mN.getKernelIDClear();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachSrc(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachSrc(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDSrc() {
        Script.KernelID k = createKernelID(1, 3, null, null);
        k.mN = this.mN.getKernelIDSrc();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachDst(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachDst(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDDst() {
        Script.KernelID k = createKernelID(2, 3, null, null);
        k.mN = this.mN.getKernelIDDst();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachSrcOver(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachSrcOver(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDSrcOver() {
        Script.KernelID k = createKernelID(3, 3, null, null);
        k.mN = this.mN.getKernelIDSrcOver();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachDstOver(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachDstOver(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDDstOver() {
        Script.KernelID k = createKernelID(4, 3, null, null);
        k.mN = this.mN.getKernelIDDstOver();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachSrcIn(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachSrcIn(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDSrcIn() {
        Script.KernelID k = createKernelID(5, 3, null, null);
        k.mN = this.mN.getKernelIDSrcIn();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachDstIn(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachDstIn(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDDstIn() {
        Script.KernelID k = createKernelID(6, 3, null, null);
        k.mN = this.mN.getKernelIDDstIn();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachSrcOut(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachSrcOut(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDSrcOut() {
        Script.KernelID k = createKernelID(7, 3, null, null);
        k.mN = this.mN.getKernelIDSrcOut();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachDstOut(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachDstOut(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDDstOut() {
        Script.KernelID k = createKernelID(8, 3, null, null);
        k.mN = this.mN.getKernelIDDstOut();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachSrcAtop(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachSrcAtop(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDSrcAtop() {
        Script.KernelID k = createKernelID(9, 3, null, null);
        k.mN = this.mN.getKernelIDSrcAtop();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachDstAtop(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachDstAtop(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDDstAtop() {
        Script.KernelID k = createKernelID(10, 3, null, null);
        k.mN = this.mN.getKernelIDDstAtop();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachXor(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachXor(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDXor() {
        Script.KernelID k = createKernelID(11, 3, null, null);
        k.mN = this.mN.getKernelIDXor();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachMultiply(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachMultiply(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDMultiply() {
        Script.KernelID k = createKernelID(14, 3, null, null);
        k.mN = this.mN.getKernelIDMultiply();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachAdd(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachAdd(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDAdd() {
        Script.KernelID k = createKernelID(34, 3, null, null);
        k.mN = this.mN.getKernelIDAdd();
        return k;
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public void forEachSubtract(Allocation ain, Allocation aout) {
        AllocationThunker aint = (AllocationThunker) ain;
        AllocationThunker aoutt = (AllocationThunker) aout;
        this.mN.forEachSubtract(aint.getNObj(), aoutt.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptIntrinsicBlend
    public Script.KernelID getKernelIDSubtract() {
        Script.KernelID k = createKernelID(35, 3, null, null);
        k.mN = this.mN.getKernelIDSubtract();
        return k;
    }
}
