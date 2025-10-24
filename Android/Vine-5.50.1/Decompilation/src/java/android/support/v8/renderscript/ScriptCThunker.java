package android.support.v8.renderscript;

import android.content.res.Resources;
import android.renderscript.Script;
import android.support.v8.renderscript.Script;

/* loaded from: classes2.dex */
class ScriptCThunker extends android.renderscript.ScriptC {
    private static final String TAG = "ScriptC";

    protected ScriptCThunker(RenderScriptThunker rs, Resources resources, int resourceID) {
        super(rs.mN, resources, resourceID);
    }

    Script.KernelID thunkCreateKernelID(int slot, int sig, Element ein, Element eout) {
        android.renderscript.Element nein = null;
        android.renderscript.Element neout = null;
        if (ein != null) {
            nein = ((ElementThunker) ein).mN;
        }
        if (eout != null) {
            neout = ((ElementThunker) eout).mN;
        }
        return createKernelID(slot, sig, nein, neout);
    }

    void thunkInvoke(int slot) {
        invoke(slot);
    }

    void thunkBindAllocation(Allocation va, int slot) {
        android.renderscript.Allocation nva = null;
        if (va != null) {
            nva = ((AllocationThunker) va).mN;
        }
        bindAllocation(nva, slot);
    }

    void thunkSetTimeZone(String timeZone) {
        setTimeZone(timeZone);
    }

    void thunkInvoke(int slot, FieldPacker v) {
        android.renderscript.FieldPacker nfp = new android.renderscript.FieldPacker(v.getData());
        invoke(slot, nfp);
    }

    void thunkForEach(int slot, Allocation ain, Allocation aout, FieldPacker v) {
        android.renderscript.Allocation nin = null;
        android.renderscript.Allocation nout = null;
        android.renderscript.FieldPacker nfp = null;
        if (ain != null) {
            nin = ((AllocationThunker) ain).mN;
        }
        if (aout != null) {
            nout = ((AllocationThunker) aout).mN;
        }
        if (v != null) {
            nfp = new android.renderscript.FieldPacker(v.getData());
        }
        forEach(slot, nin, nout, nfp);
    }

    void thunkForEach(int slot, Allocation ain, Allocation aout, FieldPacker v, Script.LaunchOptions sc) {
        Script.LaunchOptions lo = null;
        if (sc != null) {
            lo = new Script.LaunchOptions();
            if (sc.getXEnd() > 0) {
                lo.setX(sc.getXStart(), sc.getXEnd());
            }
            if (sc.getYEnd() > 0) {
                lo.setY(sc.getYStart(), sc.getYEnd());
            }
            if (sc.getZEnd() > 0) {
                lo.setZ(sc.getZStart(), sc.getZEnd());
            }
        }
        android.renderscript.Allocation nin = null;
        android.renderscript.Allocation nout = null;
        android.renderscript.FieldPacker nfp = null;
        if (ain != null) {
            nin = ((AllocationThunker) ain).mN;
        }
        if (aout != null) {
            nout = ((AllocationThunker) aout).mN;
        }
        if (v != null) {
            nfp = new android.renderscript.FieldPacker(v.getData());
        }
        forEach(slot, nin, nout, nfp, lo);
    }

    void thunkSetVar(int index, float v) {
        setVar(index, v);
    }

    void thunkSetVar(int index, double v) {
        setVar(index, v);
    }

    void thunkSetVar(int index, int v) {
        setVar(index, v);
    }

    void thunkSetVar(int index, long v) {
        setVar(index, v);
    }

    void thunkSetVar(int index, boolean v) {
        setVar(index, v);
    }

    void thunkSetVar(int index, BaseObj o) {
        if (o == null) {
            setVar(index, 0);
        } else {
            setVar(index, o.getNObj());
        }
    }

    void thunkSetVar(int index, FieldPacker v) {
        android.renderscript.FieldPacker nfp = new android.renderscript.FieldPacker(v.getData());
        setVar(index, nfp);
    }

    void thunkSetVar(int index, FieldPacker v, Element e, int[] dims) {
        android.renderscript.FieldPacker nfp = new android.renderscript.FieldPacker(v.getData());
        ElementThunker et = (ElementThunker) e;
        setVar(index, nfp, et.mN, dims);
    }

    Script.FieldID thunkCreateFieldID(int slot, Element e) {
        ElementThunker et = (ElementThunker) e;
        return createFieldID(slot, et.getNObj());
    }
}
