package android.support.v8.renderscript;

import android.support.v4.view.PointerIconCompat;
import android.support.v8.renderscript.ElementThunker;

/* loaded from: classes.dex */
public class Element extends BaseObj {
    int[] mArraySizes;
    String[] mElementNames;
    Element[] mElements;
    DataKind mKind;
    boolean mNormalized;
    int[] mOffsetInBytes;
    int mSize;
    DataType mType;
    int mVectorSize;
    int[] mVisibleElementMap;

    private void updateVisibleSubElements() {
        int ctNoPadding;
        if (this.mElements != null) {
            int noPaddingFieldCount = 0;
            int fieldCount = this.mElementNames.length;
            for (int ct = 0; ct < fieldCount; ct++) {
                if (this.mElementNames[ct].charAt(0) != '#') {
                    noPaddingFieldCount++;
                }
            }
            this.mVisibleElementMap = new int[noPaddingFieldCount];
            int ct2 = 0;
            int ctNoPadding2 = 0;
            while (ct2 < fieldCount) {
                if (this.mElementNames[ct2].charAt(0) != '#') {
                    ctNoPadding = ctNoPadding2 + 1;
                    this.mVisibleElementMap[ctNoPadding2] = ct2;
                } else {
                    ctNoPadding = ctNoPadding2;
                }
                ct2++;
                ctNoPadding2 = ctNoPadding;
            }
        }
    }

    public int getBytesSize() {
        return this.mSize;
    }

    public int getVectorSize() {
        return this.mVectorSize;
    }

    /* loaded from: classes2.dex */
    public enum DataType {
        NONE(0, 0),
        FLOAT_32(2, 4),
        FLOAT_64(3, 8),
        SIGNED_8(4, 1),
        SIGNED_16(5, 2),
        SIGNED_32(6, 4),
        SIGNED_64(7, 8),
        UNSIGNED_8(8, 1),
        UNSIGNED_16(9, 2),
        UNSIGNED_32(10, 4),
        UNSIGNED_64(11, 8),
        BOOLEAN(12, 1),
        UNSIGNED_5_6_5(13, 2),
        UNSIGNED_5_5_5_1(14, 2),
        UNSIGNED_4_4_4_4(15, 2),
        MATRIX_4X4(16, 64),
        MATRIX_3X3(17, 36),
        MATRIX_2X2(18, 16),
        RS_ELEMENT(1000, 4),
        RS_TYPE(PointerIconCompat.TYPE_CONTEXT_MENU, 4),
        RS_ALLOCATION(PointerIconCompat.TYPE_HAND, 4),
        RS_SAMPLER(PointerIconCompat.TYPE_HELP, 4),
        RS_SCRIPT(PointerIconCompat.TYPE_WAIT, 4);

        int mID;
        int mSize;

        DataType(int id, int size) {
            this.mID = id;
            this.mSize = size;
        }
    }

    /* loaded from: classes2.dex */
    public enum DataKind {
        USER(0),
        PIXEL_L(7),
        PIXEL_A(8),
        PIXEL_LA(9),
        PIXEL_RGB(10),
        PIXEL_RGBA(11),
        PIXEL_DEPTH(12),
        PIXEL_YUV(13);

        int mID;

        DataKind(int id) {
            this.mID = id;
        }
    }

    public boolean isComplex() {
        if (this.mElements == null) {
            return false;
        }
        for (int ct = 0; ct < this.mElements.length; ct++) {
            if (this.mElements[ct].mElements != null) {
                return true;
            }
        }
        return false;
    }

    public int getSubElementCount() {
        if (this.mVisibleElementMap == null) {
            return 0;
        }
        return this.mVisibleElementMap.length;
    }

    public Element getSubElement(int index) {
        if (this.mVisibleElementMap == null) {
            throw new RSIllegalArgumentException("Element contains no sub-elements");
        }
        if (index < 0 || index >= this.mVisibleElementMap.length) {
            throw new RSIllegalArgumentException("Illegal sub-element index");
        }
        return this.mElements[this.mVisibleElementMap[index]];
    }

    public String getSubElementName(int index) {
        if (this.mVisibleElementMap == null) {
            throw new RSIllegalArgumentException("Element contains no sub-elements");
        }
        if (index < 0 || index >= this.mVisibleElementMap.length) {
            throw new RSIllegalArgumentException("Illegal sub-element index");
        }
        return this.mElementNames[this.mVisibleElementMap[index]];
    }

    public int getSubElementArraySize(int index) {
        if (this.mVisibleElementMap == null) {
            throw new RSIllegalArgumentException("Element contains no sub-elements");
        }
        if (index < 0 || index >= this.mVisibleElementMap.length) {
            throw new RSIllegalArgumentException("Illegal sub-element index");
        }
        return this.mArraySizes[this.mVisibleElementMap[index]];
    }

    public int getSubElementOffsetBytes(int index) {
        if (this.mVisibleElementMap == null) {
            throw new RSIllegalArgumentException("Element contains no sub-elements");
        }
        if (index < 0 || index >= this.mVisibleElementMap.length) {
            throw new RSIllegalArgumentException("Illegal sub-element index");
        }
        return this.mOffsetInBytes[this.mVisibleElementMap[index]];
    }

    public DataType getDataType() {
        return this.mType;
    }

    public DataKind getDataKind() {
        return this.mKind;
    }

    public static Element BOOLEAN(RenderScript rs) {
        if (rs.mElement_BOOLEAN == null) {
            rs.mElement_BOOLEAN = createUser(rs, DataType.BOOLEAN);
        }
        return rs.mElement_BOOLEAN;
    }

    public static Element U8(RenderScript rs) {
        if (rs.mElement_U8 == null) {
            rs.mElement_U8 = createUser(rs, DataType.UNSIGNED_8);
        }
        return rs.mElement_U8;
    }

    public static Element I8(RenderScript rs) {
        if (rs.mElement_I8 == null) {
            rs.mElement_I8 = createUser(rs, DataType.SIGNED_8);
        }
        return rs.mElement_I8;
    }

    public static Element U16(RenderScript rs) {
        if (rs.mElement_U16 == null) {
            rs.mElement_U16 = createUser(rs, DataType.UNSIGNED_16);
        }
        return rs.mElement_U16;
    }

    public static Element I16(RenderScript rs) {
        if (rs.mElement_I16 == null) {
            rs.mElement_I16 = createUser(rs, DataType.SIGNED_16);
        }
        return rs.mElement_I16;
    }

    public static Element U32(RenderScript rs) {
        if (rs.mElement_U32 == null) {
            rs.mElement_U32 = createUser(rs, DataType.UNSIGNED_32);
        }
        return rs.mElement_U32;
    }

    public static Element I32(RenderScript rs) {
        if (rs.mElement_I32 == null) {
            rs.mElement_I32 = createUser(rs, DataType.SIGNED_32);
        }
        return rs.mElement_I32;
    }

    public static Element U64(RenderScript rs) {
        if (rs.mElement_U64 == null) {
            rs.mElement_U64 = createUser(rs, DataType.UNSIGNED_64);
        }
        return rs.mElement_U64;
    }

    public static Element I64(RenderScript rs) {
        if (rs.mElement_I64 == null) {
            rs.mElement_I64 = createUser(rs, DataType.SIGNED_64);
        }
        return rs.mElement_I64;
    }

    public static Element F32(RenderScript rs) {
        if (rs.mElement_F32 == null) {
            rs.mElement_F32 = createUser(rs, DataType.FLOAT_32);
        }
        return rs.mElement_F32;
    }

    public static Element F64(RenderScript rs) {
        if (rs.mElement_F64 == null) {
            rs.mElement_F64 = createUser(rs, DataType.FLOAT_64);
        }
        return rs.mElement_F64;
    }

    public static Element ELEMENT(RenderScript rs) {
        if (rs.mElement_ELEMENT == null) {
            rs.mElement_ELEMENT = createUser(rs, DataType.RS_ELEMENT);
        }
        return rs.mElement_ELEMENT;
    }

    public static Element TYPE(RenderScript rs) {
        if (rs.mElement_TYPE == null) {
            rs.mElement_TYPE = createUser(rs, DataType.RS_TYPE);
        }
        return rs.mElement_TYPE;
    }

    public static Element ALLOCATION(RenderScript rs) {
        if (rs.mElement_ALLOCATION == null) {
            rs.mElement_ALLOCATION = createUser(rs, DataType.RS_ALLOCATION);
        }
        return rs.mElement_ALLOCATION;
    }

    public static Element SAMPLER(RenderScript rs) {
        if (rs.mElement_SAMPLER == null) {
            rs.mElement_SAMPLER = createUser(rs, DataType.RS_SAMPLER);
        }
        return rs.mElement_SAMPLER;
    }

    public static Element SCRIPT(RenderScript rs) {
        if (rs.mElement_SCRIPT == null) {
            rs.mElement_SCRIPT = createUser(rs, DataType.RS_SCRIPT);
        }
        return rs.mElement_SCRIPT;
    }

    public static Element A_8(RenderScript rs) {
        if (rs.mElement_A_8 == null) {
            rs.mElement_A_8 = createPixel(rs, DataType.UNSIGNED_8, DataKind.PIXEL_A);
        }
        return rs.mElement_A_8;
    }

    public static Element RGB_565(RenderScript rs) {
        if (rs.mElement_RGB_565 == null) {
            rs.mElement_RGB_565 = createPixel(rs, DataType.UNSIGNED_5_6_5, DataKind.PIXEL_RGB);
        }
        return rs.mElement_RGB_565;
    }

    public static Element RGB_888(RenderScript rs) {
        if (rs.mElement_RGB_888 == null) {
            rs.mElement_RGB_888 = createPixel(rs, DataType.UNSIGNED_8, DataKind.PIXEL_RGB);
        }
        return rs.mElement_RGB_888;
    }

    public static Element RGBA_5551(RenderScript rs) {
        if (rs.mElement_RGBA_5551 == null) {
            rs.mElement_RGBA_5551 = createPixel(rs, DataType.UNSIGNED_5_5_5_1, DataKind.PIXEL_RGBA);
        }
        return rs.mElement_RGBA_5551;
    }

    public static Element RGBA_4444(RenderScript rs) {
        if (rs.mElement_RGBA_4444 == null) {
            rs.mElement_RGBA_4444 = createPixel(rs, DataType.UNSIGNED_4_4_4_4, DataKind.PIXEL_RGBA);
        }
        return rs.mElement_RGBA_4444;
    }

    public static Element RGBA_8888(RenderScript rs) {
        if (rs.mElement_RGBA_8888 == null) {
            rs.mElement_RGBA_8888 = createPixel(rs, DataType.UNSIGNED_8, DataKind.PIXEL_RGBA);
        }
        return rs.mElement_RGBA_8888;
    }

    public static Element F32_2(RenderScript rs) {
        if (rs.mElement_FLOAT_2 == null) {
            rs.mElement_FLOAT_2 = createVector(rs, DataType.FLOAT_32, 2);
        }
        return rs.mElement_FLOAT_2;
    }

    public static Element F32_3(RenderScript rs) {
        if (rs.mElement_FLOAT_3 == null) {
            rs.mElement_FLOAT_3 = createVector(rs, DataType.FLOAT_32, 3);
        }
        return rs.mElement_FLOAT_3;
    }

    public static Element F32_4(RenderScript rs) {
        if (rs.mElement_FLOAT_4 == null) {
            rs.mElement_FLOAT_4 = createVector(rs, DataType.FLOAT_32, 4);
        }
        return rs.mElement_FLOAT_4;
    }

    public static Element F64_2(RenderScript rs) {
        if (rs.mElement_DOUBLE_2 == null) {
            rs.mElement_DOUBLE_2 = createVector(rs, DataType.FLOAT_64, 2);
        }
        return rs.mElement_DOUBLE_2;
    }

    public static Element F64_3(RenderScript rs) {
        if (rs.mElement_DOUBLE_3 == null) {
            rs.mElement_DOUBLE_3 = createVector(rs, DataType.FLOAT_64, 3);
        }
        return rs.mElement_DOUBLE_3;
    }

    public static Element F64_4(RenderScript rs) {
        if (rs.mElement_DOUBLE_4 == null) {
            rs.mElement_DOUBLE_4 = createVector(rs, DataType.FLOAT_64, 4);
        }
        return rs.mElement_DOUBLE_4;
    }

    public static Element U8_2(RenderScript rs) {
        if (rs.mElement_UCHAR_2 == null) {
            rs.mElement_UCHAR_2 = createVector(rs, DataType.UNSIGNED_8, 2);
        }
        return rs.mElement_UCHAR_2;
    }

    public static Element U8_3(RenderScript rs) {
        if (rs.mElement_UCHAR_3 == null) {
            rs.mElement_UCHAR_3 = createVector(rs, DataType.UNSIGNED_8, 3);
        }
        return rs.mElement_UCHAR_3;
    }

    public static Element U8_4(RenderScript rs) {
        if (rs.mElement_UCHAR_4 == null) {
            rs.mElement_UCHAR_4 = createVector(rs, DataType.UNSIGNED_8, 4);
        }
        return rs.mElement_UCHAR_4;
    }

    public static Element I8_2(RenderScript rs) {
        if (rs.mElement_CHAR_2 == null) {
            rs.mElement_CHAR_2 = createVector(rs, DataType.SIGNED_8, 2);
        }
        return rs.mElement_CHAR_2;
    }

    public static Element I8_3(RenderScript rs) {
        if (rs.mElement_CHAR_3 == null) {
            rs.mElement_CHAR_3 = createVector(rs, DataType.SIGNED_8, 3);
        }
        return rs.mElement_CHAR_3;
    }

    public static Element I8_4(RenderScript rs) {
        if (rs.mElement_CHAR_4 == null) {
            rs.mElement_CHAR_4 = createVector(rs, DataType.SIGNED_8, 4);
        }
        return rs.mElement_CHAR_4;
    }

    public static Element U16_2(RenderScript rs) {
        if (rs.mElement_USHORT_2 == null) {
            rs.mElement_USHORT_2 = createVector(rs, DataType.UNSIGNED_16, 2);
        }
        return rs.mElement_USHORT_2;
    }

    public static Element U16_3(RenderScript rs) {
        if (rs.mElement_USHORT_3 == null) {
            rs.mElement_USHORT_3 = createVector(rs, DataType.UNSIGNED_16, 3);
        }
        return rs.mElement_USHORT_3;
    }

    public static Element U16_4(RenderScript rs) {
        if (rs.mElement_USHORT_4 == null) {
            rs.mElement_USHORT_4 = createVector(rs, DataType.UNSIGNED_16, 4);
        }
        return rs.mElement_USHORT_4;
    }

    public static Element I16_2(RenderScript rs) {
        if (rs.mElement_SHORT_2 == null) {
            rs.mElement_SHORT_2 = createVector(rs, DataType.SIGNED_16, 2);
        }
        return rs.mElement_SHORT_2;
    }

    public static Element I16_3(RenderScript rs) {
        if (rs.mElement_SHORT_3 == null) {
            rs.mElement_SHORT_3 = createVector(rs, DataType.SIGNED_16, 3);
        }
        return rs.mElement_SHORT_3;
    }

    public static Element I16_4(RenderScript rs) {
        if (rs.mElement_SHORT_4 == null) {
            rs.mElement_SHORT_4 = createVector(rs, DataType.SIGNED_16, 4);
        }
        return rs.mElement_SHORT_4;
    }

    public static Element U32_2(RenderScript rs) {
        if (rs.mElement_UINT_2 == null) {
            rs.mElement_UINT_2 = createVector(rs, DataType.UNSIGNED_32, 2);
        }
        return rs.mElement_UINT_2;
    }

    public static Element U32_3(RenderScript rs) {
        if (rs.mElement_UINT_3 == null) {
            rs.mElement_UINT_3 = createVector(rs, DataType.UNSIGNED_32, 3);
        }
        return rs.mElement_UINT_3;
    }

    public static Element U32_4(RenderScript rs) {
        if (rs.mElement_UINT_4 == null) {
            rs.mElement_UINT_4 = createVector(rs, DataType.UNSIGNED_32, 4);
        }
        return rs.mElement_UINT_4;
    }

    public static Element I32_2(RenderScript rs) {
        if (rs.mElement_INT_2 == null) {
            rs.mElement_INT_2 = createVector(rs, DataType.SIGNED_32, 2);
        }
        return rs.mElement_INT_2;
    }

    public static Element I32_3(RenderScript rs) {
        if (rs.mElement_INT_3 == null) {
            rs.mElement_INT_3 = createVector(rs, DataType.SIGNED_32, 3);
        }
        return rs.mElement_INT_3;
    }

    public static Element I32_4(RenderScript rs) {
        if (rs.mElement_INT_4 == null) {
            rs.mElement_INT_4 = createVector(rs, DataType.SIGNED_32, 4);
        }
        return rs.mElement_INT_4;
    }

    public static Element U64_2(RenderScript rs) {
        if (rs.mElement_ULONG_2 == null) {
            rs.mElement_ULONG_2 = createVector(rs, DataType.UNSIGNED_64, 2);
        }
        return rs.mElement_ULONG_2;
    }

    public static Element U64_3(RenderScript rs) {
        if (rs.mElement_ULONG_3 == null) {
            rs.mElement_ULONG_3 = createVector(rs, DataType.UNSIGNED_64, 3);
        }
        return rs.mElement_ULONG_3;
    }

    public static Element U64_4(RenderScript rs) {
        if (rs.mElement_ULONG_4 == null) {
            rs.mElement_ULONG_4 = createVector(rs, DataType.UNSIGNED_64, 4);
        }
        return rs.mElement_ULONG_4;
    }

    public static Element I64_2(RenderScript rs) {
        if (rs.mElement_LONG_2 == null) {
            rs.mElement_LONG_2 = createVector(rs, DataType.SIGNED_64, 2);
        }
        return rs.mElement_LONG_2;
    }

    public static Element I64_3(RenderScript rs) {
        if (rs.mElement_LONG_3 == null) {
            rs.mElement_LONG_3 = createVector(rs, DataType.SIGNED_64, 3);
        }
        return rs.mElement_LONG_3;
    }

    public static Element I64_4(RenderScript rs) {
        if (rs.mElement_LONG_4 == null) {
            rs.mElement_LONG_4 = createVector(rs, DataType.SIGNED_64, 4);
        }
        return rs.mElement_LONG_4;
    }

    public static Element MATRIX_4X4(RenderScript rs) {
        if (rs.mElement_MATRIX_4X4 == null) {
            rs.mElement_MATRIX_4X4 = createUser(rs, DataType.MATRIX_4X4);
        }
        return rs.mElement_MATRIX_4X4;
    }

    public static Element MATRIX_3X3(RenderScript rs) {
        if (rs.mElement_MATRIX_3X3 == null) {
            rs.mElement_MATRIX_3X3 = createUser(rs, DataType.MATRIX_3X3);
        }
        return rs.mElement_MATRIX_3X3;
    }

    public static Element MATRIX_2X2(RenderScript rs) {
        if (rs.mElement_MATRIX_2X2 == null) {
            rs.mElement_MATRIX_2X2 = createUser(rs, DataType.MATRIX_2X2);
        }
        return rs.mElement_MATRIX_2X2;
    }

    Element(int id, RenderScript rs, Element[] e, String[] n, int[] as) {
        super(id, rs);
        this.mSize = 0;
        this.mVectorSize = 1;
        this.mElements = e;
        this.mElementNames = n;
        this.mArraySizes = as;
        this.mType = DataType.NONE;
        this.mKind = DataKind.USER;
        this.mOffsetInBytes = new int[this.mElements.length];
        for (int ct = 0; ct < this.mElements.length; ct++) {
            this.mOffsetInBytes[ct] = this.mSize;
            this.mSize += this.mElements[ct].mSize * this.mArraySizes[ct];
        }
        updateVisibleSubElements();
    }

    Element(int id, RenderScript rs, DataType dt, DataKind dk, boolean norm, int size) {
        super(id, rs);
        if (dt != DataType.UNSIGNED_5_6_5 && dt != DataType.UNSIGNED_4_4_4_4 && dt != DataType.UNSIGNED_5_5_5_1) {
            if (size == 3) {
                this.mSize = dt.mSize * 4;
            } else {
                this.mSize = dt.mSize * size;
            }
        } else {
            this.mSize = dt.mSize;
        }
        this.mType = dt;
        this.mKind = dk;
        this.mNormalized = norm;
        this.mVectorSize = size;
    }

    Element(int id, RenderScript rs) {
        super(id, rs);
    }

    static Element createUser(RenderScript rs, DataType dt) {
        if (RenderScript.isNative) {
            RenderScriptThunker rst = (RenderScriptThunker) rs;
            return ElementThunker.create(rst, dt);
        }
        DataKind dk = DataKind.USER;
        int id = rs.nElementCreate(dt.mID, dk.mID, false, 1);
        return new Element(id, rs, dt, dk, false, 1);
    }

    public static Element createVector(RenderScript rs, DataType dt, int size) {
        if (RenderScript.isNative) {
            RenderScriptThunker rst = (RenderScriptThunker) rs;
            return ElementThunker.createVector(rst, dt, size);
        }
        if (size < 2 || size > 4) {
            throw new RSIllegalArgumentException("Vector size out of range 2-4.");
        }
        switch (dt) {
            case FLOAT_32:
            case FLOAT_64:
            case SIGNED_8:
            case SIGNED_16:
            case SIGNED_32:
            case SIGNED_64:
            case UNSIGNED_8:
            case UNSIGNED_16:
            case UNSIGNED_32:
            case UNSIGNED_64:
            case BOOLEAN:
                DataKind dk = DataKind.USER;
                int id = rs.nElementCreate(dt.mID, dk.mID, false, size);
                return new Element(id, rs, dt, dk, false, size);
            default:
                throw new RSIllegalArgumentException("Cannot create vector of non-primitive type.");
        }
    }

    public static Element createPixel(RenderScript rs, DataType dt, DataKind dk) {
        if (RenderScript.isNative) {
            RenderScriptThunker rst = (RenderScriptThunker) rs;
            return ElementThunker.createPixel(rst, dt, dk);
        }
        if (dk != DataKind.PIXEL_L && dk != DataKind.PIXEL_A && dk != DataKind.PIXEL_LA && dk != DataKind.PIXEL_RGB && dk != DataKind.PIXEL_RGBA && dk != DataKind.PIXEL_DEPTH && dk != DataKind.PIXEL_YUV) {
            throw new RSIllegalArgumentException("Unsupported DataKind");
        }
        if (dt != DataType.UNSIGNED_8 && dt != DataType.UNSIGNED_16 && dt != DataType.UNSIGNED_5_6_5 && dt != DataType.UNSIGNED_4_4_4_4 && dt != DataType.UNSIGNED_5_5_5_1) {
            throw new RSIllegalArgumentException("Unsupported DataType");
        }
        if (dt == DataType.UNSIGNED_5_6_5 && dk != DataKind.PIXEL_RGB) {
            throw new RSIllegalArgumentException("Bad kind and type combo");
        }
        if (dt == DataType.UNSIGNED_5_5_5_1 && dk != DataKind.PIXEL_RGBA) {
            throw new RSIllegalArgumentException("Bad kind and type combo");
        }
        if (dt == DataType.UNSIGNED_4_4_4_4 && dk != DataKind.PIXEL_RGBA) {
            throw new RSIllegalArgumentException("Bad kind and type combo");
        }
        if (dt == DataType.UNSIGNED_16 && dk != DataKind.PIXEL_DEPTH) {
            throw new RSIllegalArgumentException("Bad kind and type combo");
        }
        int size = 1;
        switch (dk) {
            case PIXEL_LA:
                size = 2;
                break;
            case PIXEL_RGB:
                size = 3;
                break;
            case PIXEL_RGBA:
                size = 4;
                break;
        }
        int id = rs.nElementCreate(dt.mID, dk.mID, true, size);
        return new Element(id, rs, dt, dk, true, size);
    }

    public boolean isCompatible(Element e) {
        if (equals(e)) {
            return true;
        }
        return this.mSize == e.mSize && this.mType != DataType.NONE && this.mType == e.mType && this.mVectorSize == e.mVectorSize;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        int[] mArraySizes;
        int mCount;
        String[] mElementNames;
        Element[] mElements;
        RenderScript mRS;
        int mSkipPadding;
        ElementThunker.BuilderThunker mT;

        public Builder(RenderScript rs) {
            if (RenderScript.isNative) {
                this.mT = new ElementThunker.BuilderThunker(rs);
            }
            this.mRS = rs;
            this.mCount = 0;
            this.mElements = new Element[8];
            this.mElementNames = new String[8];
            this.mArraySizes = new int[8];
        }

        public Builder add(Element element, String name, int arraySize) {
            if (this.mT != null) {
                this.mT.add(element, name, arraySize);
            } else {
                if (arraySize < 1) {
                    throw new RSIllegalArgumentException("Array size cannot be less than 1.");
                }
                if (this.mSkipPadding != 0 && name.startsWith("#padding_")) {
                    this.mSkipPadding = 0;
                } else {
                    if (element.mVectorSize == 3) {
                        this.mSkipPadding = 1;
                    } else {
                        this.mSkipPadding = 0;
                    }
                    if (this.mCount == this.mElements.length) {
                        Element[] e = new Element[this.mCount + 8];
                        String[] s = new String[this.mCount + 8];
                        int[] as = new int[this.mCount + 8];
                        System.arraycopy(this.mElements, 0, e, 0, this.mCount);
                        System.arraycopy(this.mElementNames, 0, s, 0, this.mCount);
                        System.arraycopy(this.mArraySizes, 0, as, 0, this.mCount);
                        this.mElements = e;
                        this.mElementNames = s;
                        this.mArraySizes = as;
                    }
                    this.mElements[this.mCount] = element;
                    this.mElementNames[this.mCount] = name;
                    this.mArraySizes[this.mCount] = arraySize;
                    this.mCount++;
                }
            }
            return this;
        }

        public Builder add(Element element, String name) {
            return add(element, name, 1);
        }

        public Element create() {
            if (this.mT != null) {
                return this.mT.create(this.mRS);
            }
            this.mRS.validate();
            Element[] ein = new Element[this.mCount];
            String[] sin = new String[this.mCount];
            int[] asin = new int[this.mCount];
            System.arraycopy(this.mElements, 0, ein, 0, this.mCount);
            System.arraycopy(this.mElementNames, 0, sin, 0, this.mCount);
            System.arraycopy(this.mArraySizes, 0, asin, 0, this.mCount);
            int[] ids = new int[ein.length];
            for (int ct = 0; ct < ein.length; ct++) {
                ids[ct] = ein[ct].getID(this.mRS);
            }
            int id = this.mRS.nElementCreate2(ids, sin, asin);
            return new Element(id, this.mRS, ein, sin, asin);
        }
    }
}
