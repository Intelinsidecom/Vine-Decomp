package android.support.v8.renderscript;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.SystemProperties;
import android.renderscript.RenderScript;
import android.util.Log;
import java.io.File;

/* loaded from: classes.dex */
public class RenderScript {
    private static final String CACHE_PATH = "com.android.renderscript.cache";
    static final boolean DEBUG = false;
    static final boolean LOG_ENABLED = false;
    static final String LOG_TAG = "RenderScript_jni";
    static String mCachePath;
    static boolean sInitialized;
    private Context mApplicationContext;
    int mContext;
    int mDev;
    Element mElement_ALLOCATION;
    Element mElement_A_8;
    Element mElement_BOOLEAN;
    Element mElement_CHAR_2;
    Element mElement_CHAR_3;
    Element mElement_CHAR_4;
    Element mElement_DOUBLE_2;
    Element mElement_DOUBLE_3;
    Element mElement_DOUBLE_4;
    Element mElement_ELEMENT;
    Element mElement_F32;
    Element mElement_F64;
    Element mElement_FLOAT_2;
    Element mElement_FLOAT_3;
    Element mElement_FLOAT_4;
    Element mElement_I16;
    Element mElement_I32;
    Element mElement_I64;
    Element mElement_I8;
    Element mElement_INT_2;
    Element mElement_INT_3;
    Element mElement_INT_4;
    Element mElement_LONG_2;
    Element mElement_LONG_3;
    Element mElement_LONG_4;
    Element mElement_MATRIX_2X2;
    Element mElement_MATRIX_3X3;
    Element mElement_MATRIX_4X4;
    Element mElement_RGBA_4444;
    Element mElement_RGBA_5551;
    Element mElement_RGBA_8888;
    Element mElement_RGB_565;
    Element mElement_RGB_888;
    Element mElement_SAMPLER;
    Element mElement_SCRIPT;
    Element mElement_SHORT_2;
    Element mElement_SHORT_3;
    Element mElement_SHORT_4;
    Element mElement_TYPE;
    Element mElement_U16;
    Element mElement_U32;
    Element mElement_U64;
    Element mElement_U8;
    Element mElement_UCHAR_2;
    Element mElement_UCHAR_3;
    Element mElement_UCHAR_4;
    Element mElement_UINT_2;
    Element mElement_UINT_3;
    Element mElement_UINT_4;
    Element mElement_ULONG_2;
    Element mElement_ULONG_3;
    Element mElement_ULONG_4;
    Element mElement_USHORT_2;
    Element mElement_USHORT_3;
    Element mElement_USHORT_4;
    MessageThread mMessageThread;
    Sampler mSampler_CLAMP_LINEAR;
    Sampler mSampler_CLAMP_LINEAR_MIP_LINEAR;
    Sampler mSampler_CLAMP_NEAREST;
    Sampler mSampler_MIRRORED_REPEAT_LINEAR;
    Sampler mSampler_MIRRORED_REPEAT_LINEAR_MIP_LINEAR;
    Sampler mSampler_MIRRORED_REPEAT_NEAREST;
    Sampler mSampler_WRAP_LINEAR;
    Sampler mSampler_WRAP_LINEAR_MIP_LINEAR;
    Sampler mSampler_WRAP_NEAREST;
    static Object lock = new Object();
    static boolean isNative = false;
    private static int thunk = 0;
    RSMessageHandler mMessageCallback = null;
    RSErrorHandler mErrorCallback = null;

    native void nContextDeinitToClient(int i);

    native String nContextGetErrorMessage(int i);

    native int nContextGetUserMessage(int i, int[] iArr);

    native void nContextInitToClient(int i);

    native int nContextPeekMessage(int i, int[] iArr);

    native int nDeviceCreate();

    native void nDeviceDestroy(int i);

    native void nDeviceSetConfig(int i, int i2, int i3);

    native void rsnAllocationCopyFromBitmap(int i, int i2, Bitmap bitmap);

    native void rsnAllocationCopyToBitmap(int i, int i2, Bitmap bitmap);

    native int rsnAllocationCreateBitmapBackedAllocation(int i, int i2, int i3, Bitmap bitmap, int i4);

    native int rsnAllocationCreateBitmapRef(int i, int i2, Bitmap bitmap);

    native int rsnAllocationCreateFromAssetStream(int i, int i2, int i3, int i4);

    native int rsnAllocationCreateFromBitmap(int i, int i2, int i3, Bitmap bitmap, int i4);

    native int rsnAllocationCreateTyped(int i, int i2, int i3, int i4, int i5);

    native int rsnAllocationCubeCreateFromBitmap(int i, int i2, int i3, Bitmap bitmap, int i4);

    native void rsnAllocationData1D(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6);

    native void rsnAllocationData1D(int i, int i2, int i3, int i4, int i5, float[] fArr, int i6);

    native void rsnAllocationData1D(int i, int i2, int i3, int i4, int i5, int[] iArr, int i6);

    native void rsnAllocationData1D(int i, int i2, int i3, int i4, int i5, short[] sArr, int i6);

    native void rsnAllocationData2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13);

    native void rsnAllocationData2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, byte[] bArr, int i9);

    native void rsnAllocationData2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, float[] fArr, int i9);

    native void rsnAllocationData2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int[] iArr, int i9);

    native void rsnAllocationData2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, short[] sArr, int i9);

    native void rsnAllocationData2D(int i, int i2, int i3, int i4, int i5, int i6, Bitmap bitmap);

    native void rsnAllocationData3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14);

    native void rsnAllocationData3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, byte[] bArr, int i10);

    native void rsnAllocationData3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, float[] fArr, int i10);

    native void rsnAllocationData3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int[] iArr, int i10);

    native void rsnAllocationData3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, short[] sArr, int i10);

    native void rsnAllocationElementData1D(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6);

    native void rsnAllocationGenerateMipmaps(int i, int i2);

    native int rsnAllocationGetType(int i, int i2);

    native void rsnAllocationIoReceive(int i, int i2);

    native void rsnAllocationIoSend(int i, int i2);

    native void rsnAllocationRead(int i, int i2, byte[] bArr);

    native void rsnAllocationRead(int i, int i2, float[] fArr);

    native void rsnAllocationRead(int i, int i2, int[] iArr);

    native void rsnAllocationRead(int i, int i2, short[] sArr);

    native void rsnAllocationResize1D(int i, int i2, int i3);

    native void rsnAllocationResize2D(int i, int i2, int i3, int i4);

    native void rsnAllocationSyncAll(int i, int i2, int i3);

    native int rsnContextCreate(int i, int i2, int i3, int i4);

    native void rsnContextDestroy(int i);

    native void rsnContextDump(int i, int i2);

    native void rsnContextFinish(int i);

    native void rsnContextSendMessage(int i, int i2, int[] iArr);

    native void rsnContextSetPriority(int i, int i2);

    native int rsnElementCreate(int i, int i2, int i3, boolean z, int i4);

    native int rsnElementCreate2(int i, int[] iArr, String[] strArr, int[] iArr2);

    native void rsnElementGetNativeData(int i, int i2, int[] iArr);

    native void rsnElementGetSubElements(int i, int i2, int[] iArr, String[] strArr, int[] iArr2);

    native void rsnObjDestroy(int i, int i2);

    native int rsnSamplerCreate(int i, int i2, int i3, int i4, int i5, int i6, float f);

    native void rsnScriptBindAllocation(int i, int i2, int i3, int i4);

    native int rsnScriptCCreate(int i, String str, String str2, byte[] bArr, int i2);

    native int rsnScriptFieldIDCreate(int i, int i2, int i3);

    native void rsnScriptForEach(int i, int i2, int i3, int i4, int i5);

    native void rsnScriptForEach(int i, int i2, int i3, int i4, int i5, byte[] bArr);

    native void rsnScriptForEachClipped(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11);

    native void rsnScriptForEachClipped(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7, int i8, int i9, int i10, int i11);

    native int rsnScriptGroupCreate(int i, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5);

    native void rsnScriptGroupExecute(int i, int i2);

    native void rsnScriptGroupSetInput(int i, int i2, int i3, int i4);

    native void rsnScriptGroupSetOutput(int i, int i2, int i3, int i4);

    native int rsnScriptIntrinsicCreate(int i, int i2, int i3);

    native void rsnScriptInvoke(int i, int i2, int i3);

    native void rsnScriptInvokeV(int i, int i2, int i3, byte[] bArr);

    native int rsnScriptKernelIDCreate(int i, int i2, int i3, int i4);

    native void rsnScriptSetTimeZone(int i, int i2, byte[] bArr);

    native void rsnScriptSetVarD(int i, int i2, int i3, double d);

    native void rsnScriptSetVarF(int i, int i2, int i3, float f);

    native void rsnScriptSetVarI(int i, int i2, int i3, int i4);

    native void rsnScriptSetVarJ(int i, int i2, int i3, long j);

    native void rsnScriptSetVarObj(int i, int i2, int i3, int i4);

    native void rsnScriptSetVarV(int i, int i2, int i3, byte[] bArr);

    native void rsnScriptSetVarVE(int i, int i2, int i3, byte[] bArr, int i4, int[] iArr);

    native int rsnTypeCreate(int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, int i6);

    native void rsnTypeGetNativeData(int i, int i2, int[] iArr);

    static boolean shouldThunk() {
        if (thunk == 0) {
            if (Build.VERSION.SDK_INT >= 18 && SystemProperties.getInt("debug.rs.forcecompat", 0) == 0) {
                thunk = 1;
            } else {
                thunk = -1;
            }
        }
        return thunk == 1;
    }

    public static void setupDiskCache(File cacheDir) {
        File f = new File(cacheDir, CACHE_PATH);
        mCachePath = f.getAbsolutePath();
        f.mkdirs();
    }

    /* loaded from: classes2.dex */
    public enum ContextType {
        NORMAL(0),
        DEBUG(1),
        PROFILE(2);

        int mID;

        ContextType(int id) {
            this.mID = id;
        }
    }

    synchronized int nContextCreate(int dev, int ver, int sdkVer, int contextType) {
        return rsnContextCreate(dev, ver, sdkVer, contextType);
    }

    synchronized void nContextDestroy() {
        validate();
        rsnContextDestroy(this.mContext);
    }

    synchronized void nContextSetPriority(int p) {
        validate();
        rsnContextSetPriority(this.mContext, p);
    }

    synchronized void nContextDump(int bits) {
        validate();
        rsnContextDump(this.mContext, bits);
    }

    synchronized void nContextFinish() {
        validate();
        rsnContextFinish(this.mContext);
    }

    synchronized void nContextSendMessage(int id, int[] data) {
        validate();
        rsnContextSendMessage(this.mContext, id, data);
    }

    synchronized void nObjDestroy(int id) {
        if (this.mContext != 0) {
            rsnObjDestroy(this.mContext, id);
        }
    }

    synchronized int nElementCreate(int type, int kind, boolean norm, int vecSize) {
        validate();
        return rsnElementCreate(this.mContext, type, kind, norm, vecSize);
    }

    synchronized int nElementCreate2(int[] elements, String[] names, int[] arraySizes) {
        validate();
        return rsnElementCreate2(this.mContext, elements, names, arraySizes);
    }

    synchronized void nElementGetNativeData(int id, int[] elementData) {
        validate();
        rsnElementGetNativeData(this.mContext, id, elementData);
    }

    synchronized void nElementGetSubElements(int id, int[] IDs, String[] names, int[] arraySizes) {
        validate();
        rsnElementGetSubElements(this.mContext, id, IDs, names, arraySizes);
    }

    synchronized int nTypeCreate(int eid, int x, int y, int z, boolean mips, boolean faces, int yuv) {
        validate();
        return rsnTypeCreate(this.mContext, eid, x, y, z, mips, faces, yuv);
    }

    synchronized void nTypeGetNativeData(int id, int[] typeData) {
        validate();
        rsnTypeGetNativeData(this.mContext, id, typeData);
    }

    synchronized int nAllocationCreateTyped(int type, int mip, int usage, int pointer) {
        validate();
        return rsnAllocationCreateTyped(this.mContext, type, mip, usage, pointer);
    }

    synchronized int nAllocationCreateFromBitmap(int type, int mip, Bitmap bmp, int usage) {
        validate();
        return rsnAllocationCreateFromBitmap(this.mContext, type, mip, bmp, usage);
    }

    synchronized int nAllocationCreateBitmapBackedAllocation(int type, int mip, Bitmap bmp, int usage) {
        validate();
        return rsnAllocationCreateBitmapBackedAllocation(this.mContext, type, mip, bmp, usage);
    }

    synchronized int nAllocationCubeCreateFromBitmap(int type, int mip, Bitmap bmp, int usage) {
        validate();
        return rsnAllocationCubeCreateFromBitmap(this.mContext, type, mip, bmp, usage);
    }

    synchronized int nAllocationCreateBitmapRef(int type, Bitmap bmp) {
        validate();
        return rsnAllocationCreateBitmapRef(this.mContext, type, bmp);
    }

    synchronized int nAllocationCreateFromAssetStream(int mips, int assetStream, int usage) {
        validate();
        return rsnAllocationCreateFromAssetStream(this.mContext, mips, assetStream, usage);
    }

    synchronized void nAllocationCopyToBitmap(int alloc, Bitmap bmp) {
        validate();
        rsnAllocationCopyToBitmap(this.mContext, alloc, bmp);
    }

    synchronized void nAllocationSyncAll(int alloc, int src) {
        validate();
        rsnAllocationSyncAll(this.mContext, alloc, src);
    }

    synchronized void nAllocationIoSend(int alloc) {
        validate();
        rsnAllocationIoSend(this.mContext, alloc);
    }

    synchronized void nAllocationIoReceive(int alloc) {
        validate();
        rsnAllocationIoReceive(this.mContext, alloc);
    }

    synchronized void nAllocationGenerateMipmaps(int alloc) {
        validate();
        rsnAllocationGenerateMipmaps(this.mContext, alloc);
    }

    synchronized void nAllocationCopyFromBitmap(int alloc, Bitmap bmp) {
        validate();
        rsnAllocationCopyFromBitmap(this.mContext, alloc, bmp);
    }

    synchronized void nAllocationData1D(int id, int off, int mip, int count, int[] d, int sizeBytes) {
        validate();
        rsnAllocationData1D(this.mContext, id, off, mip, count, d, sizeBytes);
    }

    synchronized void nAllocationData1D(int id, int off, int mip, int count, short[] d, int sizeBytes) {
        validate();
        rsnAllocationData1D(this.mContext, id, off, mip, count, d, sizeBytes);
    }

    synchronized void nAllocationData1D(int id, int off, int mip, int count, byte[] d, int sizeBytes) {
        validate();
        rsnAllocationData1D(this.mContext, id, off, mip, count, d, sizeBytes);
    }

    synchronized void nAllocationData1D(int id, int off, int mip, int count, float[] d, int sizeBytes) {
        validate();
        rsnAllocationData1D(this.mContext, id, off, mip, count, d, sizeBytes);
    }

    synchronized void nAllocationElementData1D(int id, int xoff, int mip, int compIdx, byte[] d, int sizeBytes) {
        validate();
        rsnAllocationElementData1D(this.mContext, id, xoff, mip, compIdx, d, sizeBytes);
    }

    synchronized void nAllocationData2D(int dstAlloc, int dstXoff, int dstYoff, int dstMip, int dstFace, int width, int height, int srcAlloc, int srcXoff, int srcYoff, int srcMip, int srcFace) {
        validate();
        rsnAllocationData2D(this.mContext, dstAlloc, dstXoff, dstYoff, dstMip, dstFace, width, height, srcAlloc, srcXoff, srcYoff, srcMip, srcFace);
    }

    synchronized void nAllocationData2D(int id, int xoff, int yoff, int mip, int face, int w, int h, byte[] d, int sizeBytes) {
        validate();
        rsnAllocationData2D(this.mContext, id, xoff, yoff, mip, face, w, h, d, sizeBytes);
    }

    synchronized void nAllocationData2D(int id, int xoff, int yoff, int mip, int face, int w, int h, short[] d, int sizeBytes) {
        validate();
        rsnAllocationData2D(this.mContext, id, xoff, yoff, mip, face, w, h, d, sizeBytes);
    }

    synchronized void nAllocationData2D(int id, int xoff, int yoff, int mip, int face, int w, int h, int[] d, int sizeBytes) {
        validate();
        rsnAllocationData2D(this.mContext, id, xoff, yoff, mip, face, w, h, d, sizeBytes);
    }

    synchronized void nAllocationData2D(int id, int xoff, int yoff, int mip, int face, int w, int h, float[] d, int sizeBytes) {
        validate();
        rsnAllocationData2D(this.mContext, id, xoff, yoff, mip, face, w, h, d, sizeBytes);
    }

    synchronized void nAllocationData2D(int id, int xoff, int yoff, int mip, int face, Bitmap b) {
        validate();
        rsnAllocationData2D(this.mContext, id, xoff, yoff, mip, face, b);
    }

    synchronized void nAllocationData3D(int dstAlloc, int dstXoff, int dstYoff, int dstZoff, int dstMip, int width, int height, int depth, int srcAlloc, int srcXoff, int srcYoff, int srcZoff, int srcMip) {
        validate();
        rsnAllocationData3D(this.mContext, dstAlloc, dstXoff, dstYoff, dstZoff, dstMip, width, height, depth, srcAlloc, srcXoff, srcYoff, srcZoff, srcMip);
    }

    synchronized void nAllocationData3D(int id, int xoff, int yoff, int zoff, int mip, int w, int h, int depth, byte[] d, int sizeBytes) {
        validate();
        rsnAllocationData3D(this.mContext, id, xoff, yoff, zoff, mip, w, h, depth, d, sizeBytes);
    }

    synchronized void nAllocationData3D(int id, int xoff, int yoff, int zoff, int mip, int w, int h, int depth, short[] d, int sizeBytes) {
        validate();
        rsnAllocationData3D(this.mContext, id, xoff, yoff, zoff, mip, w, h, depth, d, sizeBytes);
    }

    synchronized void nAllocationData3D(int id, int xoff, int yoff, int zoff, int mip, int w, int h, int depth, int[] d, int sizeBytes) {
        validate();
        rsnAllocationData3D(this.mContext, id, xoff, yoff, zoff, mip, w, h, depth, d, sizeBytes);
    }

    synchronized void nAllocationData3D(int id, int xoff, int yoff, int zoff, int mip, int w, int h, int depth, float[] d, int sizeBytes) {
        validate();
        rsnAllocationData3D(this.mContext, id, xoff, yoff, zoff, mip, w, h, depth, d, sizeBytes);
    }

    synchronized void nAllocationRead(int id, byte[] d) {
        validate();
        rsnAllocationRead(this.mContext, id, d);
    }

    synchronized void nAllocationRead(int id, short[] d) {
        validate();
        rsnAllocationRead(this.mContext, id, d);
    }

    synchronized void nAllocationRead(int id, int[] d) {
        validate();
        rsnAllocationRead(this.mContext, id, d);
    }

    synchronized void nAllocationRead(int id, float[] d) {
        validate();
        rsnAllocationRead(this.mContext, id, d);
    }

    synchronized int nAllocationGetType(int id) {
        validate();
        return rsnAllocationGetType(this.mContext, id);
    }

    synchronized void nAllocationResize1D(int id, int dimX) {
        validate();
        rsnAllocationResize1D(this.mContext, id, dimX);
    }

    synchronized void nAllocationResize2D(int id, int dimX, int dimY) {
        validate();
        rsnAllocationResize2D(this.mContext, id, dimX, dimY);
    }

    synchronized void nScriptBindAllocation(int script, int alloc, int slot) {
        validate();
        rsnScriptBindAllocation(this.mContext, script, alloc, slot);
    }

    synchronized void nScriptSetTimeZone(int script, byte[] timeZone) {
        validate();
        rsnScriptSetTimeZone(this.mContext, script, timeZone);
    }

    synchronized void nScriptInvoke(int id, int slot) {
        validate();
        rsnScriptInvoke(this.mContext, id, slot);
    }

    synchronized void nScriptForEach(int id, int slot, int ain, int aout, byte[] params) {
        validate();
        if (params == null) {
            rsnScriptForEach(this.mContext, id, slot, ain, aout);
        } else {
            rsnScriptForEach(this.mContext, id, slot, ain, aout, params);
        }
    }

    synchronized void nScriptForEachClipped(int id, int slot, int ain, int aout, byte[] params, int xstart, int xend, int ystart, int yend, int zstart, int zend) {
        validate();
        if (params == null) {
            rsnScriptForEachClipped(this.mContext, id, slot, ain, aout, xstart, xend, ystart, yend, zstart, zend);
        } else {
            rsnScriptForEachClipped(this.mContext, id, slot, ain, aout, params, xstart, xend, ystart, yend, zstart, zend);
        }
    }

    synchronized void nScriptInvokeV(int id, int slot, byte[] params) {
        validate();
        rsnScriptInvokeV(this.mContext, id, slot, params);
    }

    synchronized void nScriptSetVarI(int id, int slot, int val) {
        validate();
        rsnScriptSetVarI(this.mContext, id, slot, val);
    }

    synchronized void nScriptSetVarJ(int id, int slot, long val) {
        validate();
        rsnScriptSetVarJ(this.mContext, id, slot, val);
    }

    synchronized void nScriptSetVarF(int id, int slot, float val) {
        validate();
        rsnScriptSetVarF(this.mContext, id, slot, val);
    }

    synchronized void nScriptSetVarD(int id, int slot, double val) {
        validate();
        rsnScriptSetVarD(this.mContext, id, slot, val);
    }

    synchronized void nScriptSetVarV(int id, int slot, byte[] val) {
        validate();
        rsnScriptSetVarV(this.mContext, id, slot, val);
    }

    synchronized void nScriptSetVarVE(int id, int slot, byte[] val, int e, int[] dims) {
        validate();
        rsnScriptSetVarVE(this.mContext, id, slot, val, e, dims);
    }

    synchronized void nScriptSetVarObj(int id, int slot, int val) {
        validate();
        rsnScriptSetVarObj(this.mContext, id, slot, val);
    }

    synchronized int nScriptCCreate(String resName, String cacheDir, byte[] script, int length) {
        validate();
        return rsnScriptCCreate(this.mContext, resName, cacheDir, script, length);
    }

    synchronized int nScriptIntrinsicCreate(int id, int eid) {
        validate();
        return rsnScriptIntrinsicCreate(this.mContext, id, eid);
    }

    synchronized int nScriptKernelIDCreate(int sid, int slot, int sig) {
        validate();
        return rsnScriptKernelIDCreate(this.mContext, sid, slot, sig);
    }

    synchronized int nScriptFieldIDCreate(int sid, int slot) {
        validate();
        return rsnScriptFieldIDCreate(this.mContext, sid, slot);
    }

    synchronized int nScriptGroupCreate(int[] kernels, int[] src, int[] dstk, int[] dstf, int[] types) {
        validate();
        return rsnScriptGroupCreate(this.mContext, kernels, src, dstk, dstf, types);
    }

    synchronized void nScriptGroupSetInput(int group, int kernel, int alloc) {
        validate();
        rsnScriptGroupSetInput(this.mContext, group, kernel, alloc);
    }

    synchronized void nScriptGroupSetOutput(int group, int kernel, int alloc) {
        validate();
        rsnScriptGroupSetOutput(this.mContext, group, kernel, alloc);
    }

    synchronized void nScriptGroupExecute(int group) {
        validate();
        rsnScriptGroupExecute(this.mContext, group);
    }

    synchronized int nSamplerCreate(int magFilter, int minFilter, int wrapS, int wrapT, int wrapR, float aniso) {
        validate();
        return rsnSamplerCreate(this.mContext, magFilter, minFilter, wrapS, wrapT, wrapR, aniso);
    }

    /* loaded from: classes2.dex */
    public static class RSMessageHandler implements Runnable {
        protected int[] mData;
        protected int mID;
        protected int mLength;

        @Override // java.lang.Runnable
        public void run() {
        }
    }

    public void setMessageHandler(RSMessageHandler msg) {
        this.mMessageCallback = msg;
        if (isNative) {
            RenderScriptThunker rst = (RenderScriptThunker) this;
            RenderScript.RSMessageHandler newmsg = new RenderScript.RSMessageHandler() { // from class: android.support.v8.renderscript.RenderScript.1
                @Override // android.renderscript.RenderScript.RSMessageHandler, java.lang.Runnable
                public void run() {
                    RenderScript.this.mMessageCallback.mData = this.mData;
                    RenderScript.this.mMessageCallback.mID = this.mID;
                    RenderScript.this.mMessageCallback.mLength = this.mLength;
                    RenderScript.this.mMessageCallback.run();
                }
            };
            rst.mN.setMessageHandler(newmsg);
        }
    }

    public RSMessageHandler getMessageHandler() {
        return this.mMessageCallback;
    }

    public void sendMessage(int id, int[] data) {
        nContextSendMessage(id, data);
    }

    /* loaded from: classes2.dex */
    public static class RSErrorHandler implements Runnable {
        protected String mErrorMessage;
        protected int mErrorNum;

        @Override // java.lang.Runnable
        public void run() {
        }
    }

    public void setErrorHandler(RSErrorHandler msg) {
        this.mErrorCallback = msg;
        if (isNative) {
            RenderScriptThunker rst = (RenderScriptThunker) this;
            RenderScript.RSErrorHandler newmsg = new RenderScript.RSErrorHandler() { // from class: android.support.v8.renderscript.RenderScript.2
                @Override // android.renderscript.RenderScript.RSErrorHandler, java.lang.Runnable
                public void run() {
                    RenderScript.this.mErrorCallback.mErrorMessage = this.mErrorMessage;
                    RenderScript.this.mErrorCallback.mErrorNum = this.mErrorNum;
                    RenderScript.this.mErrorCallback.run();
                }
            };
            rst.mN.setErrorHandler(newmsg);
        }
    }

    public RSErrorHandler getErrorHandler() {
        return this.mErrorCallback;
    }

    /* loaded from: classes2.dex */
    public enum Priority {
        LOW(15),
        NORMAL(-4);

        int mID;

        Priority(int id) {
            this.mID = id;
        }
    }

    void validate() {
        if (this.mContext == 0) {
            throw new RSInvalidStateException("Calling RS with no Context active.");
        }
    }

    public void setPriority(Priority p) {
        validate();
        nContextSetPriority(p.mID);
    }

    /* loaded from: classes2.dex */
    static class MessageThread extends Thread {
        static final int RS_ERROR_FATAL_UNKNOWN = 4096;
        static final int RS_MESSAGE_TO_CLIENT_ERROR = 3;
        static final int RS_MESSAGE_TO_CLIENT_EXCEPTION = 1;
        static final int RS_MESSAGE_TO_CLIENT_NONE = 0;
        static final int RS_MESSAGE_TO_CLIENT_RESIZE = 2;
        static final int RS_MESSAGE_TO_CLIENT_USER = 4;
        int[] mAuxData;
        RenderScript mRS;
        boolean mRun;

        MessageThread(RenderScript rs) {
            super("RSMessageThread");
            this.mRun = true;
            this.mAuxData = new int[2];
            this.mRS = rs;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            int[] rbuf = new int[16];
            this.mRS.nContextInitToClient(this.mRS.mContext);
            while (this.mRun) {
                rbuf[0] = 0;
                int msg = this.mRS.nContextPeekMessage(this.mRS.mContext, this.mAuxData);
                int size = this.mAuxData[1];
                int subID = this.mAuxData[0];
                if (msg == 4) {
                    if ((size >> 2) >= rbuf.length) {
                        rbuf = new int[(size + 3) >> 2];
                    }
                    if (this.mRS.nContextGetUserMessage(this.mRS.mContext, rbuf) != 4) {
                        throw new RSDriverException("Error processing message from RenderScript.");
                    }
                    if (this.mRS.mMessageCallback != null) {
                        this.mRS.mMessageCallback.mData = rbuf;
                        this.mRS.mMessageCallback.mID = subID;
                        this.mRS.mMessageCallback.mLength = size;
                        this.mRS.mMessageCallback.run();
                    } else {
                        throw new RSInvalidStateException("Received a message from the script with no message handler installed.");
                    }
                } else if (msg == 3) {
                    String e = this.mRS.nContextGetErrorMessage(this.mRS.mContext);
                    if (subID >= 4096) {
                        throw new RSRuntimeException("Fatal error " + subID + ", details: " + e);
                    }
                    if (this.mRS.mErrorCallback != null) {
                        this.mRS.mErrorCallback.mErrorMessage = e;
                        this.mRS.mErrorCallback.mErrorNum = subID;
                        this.mRS.mErrorCallback.run();
                    } else {
                        Log.e(RenderScript.LOG_TAG, "non fatal RS error, " + e);
                    }
                } else {
                    try {
                        sleep(1L, 0);
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }
    }

    RenderScript(Context ctx) {
        if (ctx != null) {
            this.mApplicationContext = ctx.getApplicationContext();
        }
    }

    public final Context getApplicationContext() {
        return this.mApplicationContext;
    }

    public static RenderScript create(Context ctx, int sdkVersion) {
        return create(ctx, sdkVersion, ContextType.NORMAL);
    }

    public static RenderScript create(Context ctx, int sdkVersion, ContextType ct) {
        RenderScript rs = new RenderScript(ctx);
        if (shouldThunk()) {
            Log.v(LOG_TAG, "RS native mode");
            return RenderScriptThunker.create(ctx, sdkVersion);
        }
        synchronized (lock) {
            if (!sInitialized) {
                try {
                    System.loadLibrary("RSSupport");
                    System.loadLibrary("rsjni");
                    sInitialized = true;
                } catch (UnsatisfiedLinkError e) {
                    Log.e(LOG_TAG, "Error loading RS jni library: " + e);
                    throw new RSRuntimeException("Error loading RS jni library: " + e);
                }
            }
        }
        Log.v(LOG_TAG, "RS compat mode");
        rs.mDev = rs.nDeviceCreate();
        rs.mContext = rs.nContextCreate(rs.mDev, 0, sdkVersion, ct.mID);
        if (rs.mContext == 0) {
            throw new RSDriverException("Failed to create RS context.");
        }
        rs.mMessageThread = new MessageThread(rs);
        rs.mMessageThread.start();
        return rs;
    }

    public static RenderScript create(Context ctx) {
        return create(ctx, ContextType.NORMAL);
    }

    public static RenderScript create(Context ctx, ContextType ct) {
        int v = ctx.getApplicationInfo().targetSdkVersion;
        return create(ctx, v, ct);
    }

    public void contextDump() {
        validate();
        nContextDump(0);
    }

    public void finish() {
        nContextFinish();
    }

    public void destroy() {
        validate();
        nContextDeinitToClient(this.mContext);
        this.mMessageThread.mRun = false;
        try {
            this.mMessageThread.join();
        } catch (InterruptedException e) {
        }
        nContextDestroy();
        this.mContext = 0;
        nDeviceDestroy(this.mDev);
        this.mDev = 0;
    }

    boolean isAlive() {
        return this.mContext != 0;
    }

    int safeID(BaseObj o) {
        if (o != null) {
            return o.getID(this);
        }
        return 0;
    }
}
