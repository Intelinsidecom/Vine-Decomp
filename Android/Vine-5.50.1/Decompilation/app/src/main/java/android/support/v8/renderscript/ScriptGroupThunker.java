package android.support.v8.renderscript;

import android.renderscript.ScriptGroup;
import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
class ScriptGroupThunker extends ScriptGroup {
    android.renderscript.ScriptGroup mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.BaseObj
    public android.renderscript.ScriptGroup getNObj() {
        return this.mN;
    }

    ScriptGroupThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    @Override // android.support.v8.renderscript.ScriptGroup
    public void setInput(Script.KernelID s, Allocation a) {
        AllocationThunker at = (AllocationThunker) a;
        this.mN.setInput(s.mN, at.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptGroup
    public void setOutput(Script.KernelID s, Allocation a) {
        AllocationThunker at = (AllocationThunker) a;
        this.mN.setOutput(s.mN, at.getNObj());
    }

    @Override // android.support.v8.renderscript.ScriptGroup
    public void execute() {
        this.mN.execute();
    }

    public static final class Builder {
        ScriptGroup.Builder bN;
        RenderScript mRS;

        Builder(RenderScript rs) {
            RenderScriptThunker rst = (RenderScriptThunker) rs;
            this.mRS = rs;
            this.bN = new ScriptGroup.Builder(rst.mN);
        }

        public Builder addKernel(Script.KernelID k) {
            this.bN.addKernel(k.mN);
            return this;
        }

        public Builder addConnection(Type t, Script.KernelID from, Script.FieldID to) {
            TypeThunker tt = (TypeThunker) t;
            this.bN.addConnection(tt.getNObj(), from.mN, to.mN);
            return this;
        }

        public Builder addConnection(Type t, Script.KernelID from, Script.KernelID to) {
            TypeThunker tt = (TypeThunker) t;
            this.bN.addConnection(tt.getNObj(), from.mN, to.mN);
            return this;
        }

        public ScriptGroupThunker create() {
            ScriptGroupThunker sg = new ScriptGroupThunker(0, this.mRS);
            sg.mN = this.bN.create();
            return sg;
        }
    }
}
