package android.support.v8.renderscript;

import android.renderscript.Element;
import android.support.v8.renderscript.Element;

/* loaded from: classes2.dex */
class ElementThunker extends Element {
    android.renderscript.Element mN;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.support.v8.renderscript.BaseObj
    public android.renderscript.Element getNObj() {
        return this.mN;
    }

    @Override // android.support.v8.renderscript.Element
    public int getBytesSize() {
        return this.mN.getBytesSize();
    }

    @Override // android.support.v8.renderscript.Element
    public int getVectorSize() {
        return this.mN.getVectorSize();
    }

    static Element.DataKind convertKind(Element.DataKind cdk) {
        switch (cdk) {
            case USER:
                return Element.DataKind.USER;
            case PIXEL_L:
                return Element.DataKind.PIXEL_L;
            case PIXEL_A:
                return Element.DataKind.PIXEL_A;
            case PIXEL_LA:
                return Element.DataKind.PIXEL_LA;
            case PIXEL_RGB:
                return Element.DataKind.PIXEL_RGB;
            case PIXEL_RGBA:
                return Element.DataKind.PIXEL_RGBA;
            default:
                return null;
        }
    }

    static Element.DataType convertType(Element.DataType cdt) {
        switch (cdt) {
            case NONE:
                return Element.DataType.NONE;
            case FLOAT_32:
                return Element.DataType.FLOAT_32;
            case FLOAT_64:
                return Element.DataType.FLOAT_64;
            case SIGNED_8:
                return Element.DataType.SIGNED_8;
            case SIGNED_16:
                return Element.DataType.SIGNED_16;
            case SIGNED_32:
                return Element.DataType.SIGNED_32;
            case SIGNED_64:
                return Element.DataType.SIGNED_64;
            case UNSIGNED_8:
                return Element.DataType.UNSIGNED_8;
            case UNSIGNED_16:
                return Element.DataType.UNSIGNED_16;
            case UNSIGNED_32:
                return Element.DataType.UNSIGNED_32;
            case UNSIGNED_64:
                return Element.DataType.UNSIGNED_64;
            case BOOLEAN:
                return Element.DataType.BOOLEAN;
            case MATRIX_4X4:
                return Element.DataType.MATRIX_4X4;
            case MATRIX_3X3:
                return Element.DataType.MATRIX_3X3;
            case MATRIX_2X2:
                return Element.DataType.MATRIX_2X2;
            case RS_ELEMENT:
                return Element.DataType.RS_ELEMENT;
            case RS_TYPE:
                return Element.DataType.RS_TYPE;
            case RS_ALLOCATION:
                return Element.DataType.RS_ALLOCATION;
            case RS_SAMPLER:
                return Element.DataType.RS_SAMPLER;
            case RS_SCRIPT:
                return Element.DataType.RS_SCRIPT;
            default:
                return null;
        }
    }

    @Override // android.support.v8.renderscript.Element
    public boolean isComplex() {
        return this.mN.isComplex();
    }

    @Override // android.support.v8.renderscript.Element
    public int getSubElementCount() {
        return this.mN.getSubElementCount();
    }

    @Override // android.support.v8.renderscript.Element
    public Element getSubElement(int index) {
        return new ElementThunker(this.mRS, this.mN.getSubElement(index));
    }

    @Override // android.support.v8.renderscript.Element
    public String getSubElementName(int index) {
        return this.mN.getSubElementName(index);
    }

    @Override // android.support.v8.renderscript.Element
    public int getSubElementArraySize(int index) {
        return this.mN.getSubElementArraySize(index);
    }

    @Override // android.support.v8.renderscript.Element
    public int getSubElementOffsetBytes(int index) {
        return this.mN.getSubElementOffsetBytes(index);
    }

    @Override // android.support.v8.renderscript.Element
    public Element.DataType getDataType() {
        return this.mType;
    }

    @Override // android.support.v8.renderscript.Element
    public Element.DataKind getDataKind() {
        return this.mKind;
    }

    ElementThunker(RenderScript rs, android.renderscript.Element e) {
        super(0, rs);
        this.mN = e;
    }

    static Element create(RenderScript rs, Element.DataType dt) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        android.renderscript.Element e = null;
        switch (dt) {
            case FLOAT_32:
                e = android.renderscript.Element.F32(rst.mN);
                break;
            case FLOAT_64:
                e = android.renderscript.Element.F64(rst.mN);
                break;
            case SIGNED_8:
                e = android.renderscript.Element.I8(rst.mN);
                break;
            case SIGNED_16:
                e = android.renderscript.Element.I16(rst.mN);
                break;
            case SIGNED_32:
                e = android.renderscript.Element.I32(rst.mN);
                break;
            case SIGNED_64:
                e = android.renderscript.Element.I64(rst.mN);
                break;
            case UNSIGNED_8:
                e = android.renderscript.Element.U8(rst.mN);
                break;
            case UNSIGNED_16:
                e = android.renderscript.Element.U16(rst.mN);
                break;
            case UNSIGNED_32:
                e = android.renderscript.Element.U32(rst.mN);
                break;
            case UNSIGNED_64:
                e = android.renderscript.Element.U64(rst.mN);
                break;
            case BOOLEAN:
                e = android.renderscript.Element.BOOLEAN(rst.mN);
                break;
            case MATRIX_4X4:
                e = android.renderscript.Element.MATRIX_4X4(rst.mN);
                break;
            case MATRIX_3X3:
                e = android.renderscript.Element.MATRIX_3X3(rst.mN);
                break;
            case MATRIX_2X2:
                e = android.renderscript.Element.MATRIX_2X2(rst.mN);
                break;
            case RS_ELEMENT:
                e = android.renderscript.Element.ELEMENT(rst.mN);
                break;
            case RS_TYPE:
                e = android.renderscript.Element.TYPE(rst.mN);
                break;
            case RS_ALLOCATION:
                e = android.renderscript.Element.ALLOCATION(rst.mN);
                break;
            case RS_SAMPLER:
                e = android.renderscript.Element.SAMPLER(rst.mN);
                break;
            case RS_SCRIPT:
                e = android.renderscript.Element.SCRIPT(rst.mN);
                break;
        }
        return new ElementThunker(rs, e);
    }

    public static Element createVector(RenderScript rs, Element.DataType dt, int size) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        android.renderscript.Element e = android.renderscript.Element.createVector(rst.mN, convertType(dt), size);
        return new ElementThunker(rs, e);
    }

    public static Element createPixel(RenderScript rs, Element.DataType dt, Element.DataKind dk) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        android.renderscript.Element e = android.renderscript.Element.createPixel(rst.mN, convertType(dt), convertKind(dk));
        return new ElementThunker(rs, e);
    }

    @Override // android.support.v8.renderscript.Element
    public boolean isCompatible(Element e) {
        ElementThunker et = (ElementThunker) e;
        return et.mN.isCompatible(this.mN);
    }

    static class BuilderThunker {
        Element.Builder mN;

        public BuilderThunker(RenderScript rs) {
            RenderScriptThunker rst = (RenderScriptThunker) rs;
            this.mN = new Element.Builder(rst.mN);
        }

        public void add(Element e, String name, int arraySize) {
            ElementThunker et = (ElementThunker) e;
            this.mN.add(et.mN, name, arraySize);
        }

        public Element create(RenderScript rs) {
            android.renderscript.Element e = this.mN.create();
            return new ElementThunker(rs, e);
        }
    }
}
