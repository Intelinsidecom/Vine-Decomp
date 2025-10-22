package android.support.v8.renderscript;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.support.v8.renderscript.Allocation;

/* loaded from: classes2.dex */
class AllocationThunker extends Allocation {
    static BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
    android.renderscript.Allocation mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.BaseObj
    public android.renderscript.Allocation getNObj() {
        return this.mN;
    }

    static Allocation.MipmapControl convertMipmapControl(Allocation.MipmapControl mc) {
        switch (mc) {
            case MIPMAP_NONE:
                return Allocation.MipmapControl.MIPMAP_NONE;
            case MIPMAP_FULL:
                return Allocation.MipmapControl.MIPMAP_FULL;
            case MIPMAP_ON_SYNC_TO_TEXTURE:
                return Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE;
            default:
                return null;
        }
    }

    @Override // android.support.v8.renderscript.Allocation
    public Type getType() {
        return TypeThunker.find(this.mN.getType());
    }

    @Override // android.support.v8.renderscript.Allocation
    public Element getElement() {
        return getType().getElement();
    }

    @Override // android.support.v8.renderscript.Allocation
    public int getUsage() {
        return this.mN.getUsage();
    }

    @Override // android.support.v8.renderscript.Allocation
    public int getBytesSize() {
        return this.mN.getBytesSize();
    }

    AllocationThunker(RenderScript rs, Type t, int usage, android.renderscript.Allocation na) {
        super(0, rs, t, usage);
        this.mType = t;
        this.mUsage = usage;
        this.mN = na;
    }

    @Override // android.support.v8.renderscript.Allocation
    public void syncAll(int srcLocation) {
        this.mN.syncAll(srcLocation);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void ioSend() {
        this.mN.ioSend();
    }

    @Override // android.support.v8.renderscript.Allocation
    public void ioReceive() {
        this.mN.ioReceive();
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFrom(BaseObj[] d) {
        if (d != null) {
            android.renderscript.BaseObj[] dN = new android.renderscript.BaseObj[d.length];
            for (int i = 0; i < d.length; i++) {
                dN[i] = d[i].getNObj();
            }
            this.mN.copyFrom(dN);
        }
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFromUnchecked(int[] d) {
        this.mN.copyFromUnchecked(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFromUnchecked(short[] d) {
        this.mN.copyFromUnchecked(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFromUnchecked(byte[] d) {
        this.mN.copyFromUnchecked(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFromUnchecked(float[] d) {
        this.mN.copyFromUnchecked(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFrom(int[] d) {
        this.mN.copyFrom(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFrom(short[] d) {
        this.mN.copyFrom(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFrom(byte[] d) {
        this.mN.copyFrom(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFrom(float[] d) {
        this.mN.copyFrom(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFrom(Bitmap b) {
        this.mN.copyFrom(b);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyFrom(Allocation a) {
        AllocationThunker at = (AllocationThunker) a;
        this.mN.copyFrom(at.mN);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void setFromFieldPacker(int xoff, FieldPacker fp) {
        android.renderscript.FieldPacker nfp = new android.renderscript.FieldPacker(fp.getData());
        this.mN.setFromFieldPacker(xoff, nfp);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void setFromFieldPacker(int xoff, int component_number, FieldPacker fp) {
        android.renderscript.FieldPacker nfp = new android.renderscript.FieldPacker(fp.getData());
        this.mN.setFromFieldPacker(xoff, component_number, nfp);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void generateMipmaps() {
        this.mN.generateMipmaps();
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy1DRangeFromUnchecked(int off, int count, int[] d) {
        this.mN.copy1DRangeFromUnchecked(off, count, d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy1DRangeFromUnchecked(int off, int count, short[] d) {
        this.mN.copy1DRangeFromUnchecked(off, count, d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy1DRangeFromUnchecked(int off, int count, byte[] d) {
        this.mN.copy1DRangeFromUnchecked(off, count, d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy1DRangeFromUnchecked(int off, int count, float[] d) {
        this.mN.copy1DRangeFromUnchecked(off, count, d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy1DRangeFrom(int off, int count, int[] d) {
        this.mN.copy1DRangeFrom(off, count, d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy1DRangeFrom(int off, int count, short[] d) {
        this.mN.copy1DRangeFrom(off, count, d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy1DRangeFrom(int off, int count, byte[] d) {
        this.mN.copy1DRangeFrom(off, count, d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy1DRangeFrom(int off, int count, float[] d) {
        this.mN.copy1DRangeFrom(off, count, d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy1DRangeFrom(int off, int count, Allocation data, int dataOff) {
        AllocationThunker at = (AllocationThunker) data;
        this.mN.copy1DRangeFrom(off, count, at.mN, dataOff);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, byte[] data) {
        this.mN.copy2DRangeFrom(xoff, yoff, w, h, data);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, short[] data) {
        this.mN.copy2DRangeFrom(xoff, yoff, w, h, data);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, int[] data) {
        this.mN.copy2DRangeFrom(xoff, yoff, w, h, data);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, float[] data) {
        this.mN.copy2DRangeFrom(xoff, yoff, w, h, data);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy2DRangeFrom(int xoff, int yoff, int w, int h, Allocation data, int dataXoff, int dataYoff) {
        AllocationThunker at = (AllocationThunker) data;
        this.mN.copy2DRangeFrom(xoff, yoff, w, h, at.mN, dataXoff, dataYoff);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copy2DRangeFrom(int xoff, int yoff, Bitmap data) {
        this.mN.copy2DRangeFrom(xoff, yoff, data);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyTo(Bitmap b) {
        this.mN.copyTo(b);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyTo(byte[] d) {
        this.mN.copyTo(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyTo(short[] d) {
        this.mN.copyTo(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyTo(int[] d) {
        this.mN.copyTo(d);
    }

    @Override // android.support.v8.renderscript.Allocation
    public void copyTo(float[] d) {
        this.mN.copyTo(d);
    }

    static {
        mBitmapOptions.inScaled = false;
    }

    public static Allocation createTyped(RenderScript rs, Type type, Allocation.MipmapControl mips, int usage) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        TypeThunker tt = (TypeThunker) type;
        android.renderscript.Allocation a = android.renderscript.Allocation.createTyped(rst.mN, tt.mN, convertMipmapControl(mips), usage);
        return new AllocationThunker(rs, type, usage, a);
    }

    public static Allocation createFromBitmap(RenderScript rs, Bitmap b, Allocation.MipmapControl mips, int usage) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        android.renderscript.Allocation a = android.renderscript.Allocation.createFromBitmap(rst.mN, b, convertMipmapControl(mips), usage);
        TypeThunker tt = new TypeThunker(rs, a.getType());
        return new AllocationThunker(rs, tt, usage, a);
    }

    public static Allocation createCubemapFromBitmap(RenderScript rs, Bitmap b, Allocation.MipmapControl mips, int usage) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        android.renderscript.Allocation a = android.renderscript.Allocation.createCubemapFromBitmap(rst.mN, b, convertMipmapControl(mips), usage);
        TypeThunker tt = new TypeThunker(rs, a.getType());
        return new AllocationThunker(rs, tt, usage, a);
    }

    public static Allocation createCubemapFromCubeFaces(RenderScript rs, Bitmap xpos, Bitmap xneg, Bitmap ypos, Bitmap yneg, Bitmap zpos, Bitmap zneg, Allocation.MipmapControl mips, int usage) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        android.renderscript.Allocation a = android.renderscript.Allocation.createCubemapFromCubeFaces(rst.mN, xpos, xneg, ypos, yneg, zpos, zneg, convertMipmapControl(mips), usage);
        TypeThunker tt = new TypeThunker(rs, a.getType());
        return new AllocationThunker(rs, tt, usage, a);
    }

    public static Allocation createFromBitmapResource(RenderScript rs, Resources res, int id, Allocation.MipmapControl mips, int usage) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        android.renderscript.Allocation a = android.renderscript.Allocation.createFromBitmapResource(rst.mN, res, id, convertMipmapControl(mips), usage);
        TypeThunker tt = new TypeThunker(rs, a.getType());
        return new AllocationThunker(rs, tt, usage, a);
    }

    public static Allocation createFromString(RenderScript rs, String str, int usage) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        android.renderscript.Allocation a = android.renderscript.Allocation.createFromString(rst.mN, str, usage);
        TypeThunker tt = new TypeThunker(rs, a.getType());
        return new AllocationThunker(rs, tt, usage, a);
    }

    public static Allocation createSized(RenderScript rs, Element e, int count, int usage) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        android.renderscript.Allocation a = android.renderscript.Allocation.createSized(rst.mN, (android.renderscript.Element) e.getNObj(), count, usage);
        TypeThunker tt = new TypeThunker(rs, a.getType());
        return new AllocationThunker(rs, tt, usage, a);
    }
}
