package android.support.v8.renderscript;

import android.renderscript.Type;
import java.util.HashMap;

/* loaded from: classes2.dex */
class TypeThunker extends Type {
    static HashMap<android.renderscript.Type, Type> mMap = new HashMap<>();
    android.renderscript.Type mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.BaseObj
    public android.renderscript.Type getNObj() {
        return this.mN;
    }

    void internalCalc() {
        this.mDimX = this.mN.getX();
        this.mDimY = this.mN.getY();
        this.mDimZ = this.mN.getZ();
        this.mDimFaces = this.mN.hasFaces();
        this.mDimMipmaps = this.mN.hasMipmaps();
        this.mDimYuv = this.mN.getYuv();
        calcElementCount();
    }

    TypeThunker(RenderScript rs, android.renderscript.Type t) {
        super(0, rs);
        this.mN = t;
        internalCalc();
        this.mElement = new ElementThunker(rs, t.getElement());
        synchronized (mMap) {
            mMap.put(this.mN, this);
        }
    }

    static Type find(android.renderscript.Type nt) {
        return mMap.get(nt);
    }

    static Type create(RenderScript rs, Element e, int dx, int dy, int dz, boolean dmip, boolean dfaces, int yuv) {
        ElementThunker et = (ElementThunker) e;
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        Type.Builder tb = new Type.Builder(rst.mN, et.mN);
        if (dx > 0) {
            tb.setX(dx);
        }
        if (dy > 0) {
            tb.setY(dy);
        }
        if (dz > 0) {
            tb.setZ(dz);
        }
        if (dmip) {
            tb.setMipmaps(dmip);
        }
        if (dfaces) {
            tb.setFaces(dfaces);
        }
        if (yuv > 0) {
            tb.setYuvFormat(yuv);
        }
        android.renderscript.Type nt = tb.create();
        TypeThunker tt = new TypeThunker(rs, nt);
        tt.internalCalc();
        return tt;
    }
}
