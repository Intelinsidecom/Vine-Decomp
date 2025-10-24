package com.googlecode.javacv.cpp;

import com.flurry.android.Constants;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.DoublePointer;
import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.FunctionPointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.LongPointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.PointerPointer;
import com.googlecode.javacpp.ShortPointer;
import com.googlecode.javacpp.SizeTPointer;
import com.googlecode.javacpp.annotation.Adapter;
import com.googlecode.javacpp.annotation.ByPtrPtr;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.ByVal;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Convention;
import com.googlecode.javacpp.annotation.Index;
import com.googlecode.javacpp.annotation.MemberGetter;
import com.googlecode.javacpp.annotation.MemberSetter;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Namespace;
import com.googlecode.javacpp.annotation.NoOffset;
import com.googlecode.javacpp.annotation.Opaque;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;
import com.googlecode.javacpp.annotation.StdVector;
import com.googlecode.javacpp.annotation.ValueGetter;
import com.googlecode.javacpp.annotation.ValueSetter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

@Properties({@Platform(include = {"<opencv2/core/core.hpp>", "opencv_adapters.h"}, link = {"opencv_core@.2.4"}, preload = {"tbb"}), @Platform(define = {"_WIN32_WINNT 0x0502"}, includepath = {"C:/opencv/build/include/"}, link = {"opencv_core246"}, preload = {"msvcr100", "msvcp100"}, value = {"windows"}), @Platform(linkpath = {"C:/opencv/build/x86/vc10/lib/"}, preloadpath = {"C:/opencv/build/x86/vc10/bin/"}, value = {"windows-x86"}), @Platform(linkpath = {"C:/opencv/build/x64/vc10/lib/"}, preloadpath = {"C:/opencv/build/x64/vc10/bin/"}, value = {"windows-x86_64"})})
/* loaded from: classes.dex */
public class opencv_core {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final int CV_16S = 3;
    public static final int CV_16SC1;
    public static final int CV_16SC2;
    public static final int CV_16SC3;
    public static final int CV_16SC4;
    public static final int CV_16U = 2;
    public static final int CV_16UC1;
    public static final int CV_16UC2;
    public static final int CV_16UC3;
    public static final int CV_16UC4;
    public static final int CV_32F = 5;
    public static final int CV_32FC1;
    public static final int CV_32FC2;
    public static final int CV_32FC3;
    public static final int CV_32FC4;
    public static final int CV_32S = 4;
    public static final int CV_32SC1;
    public static final int CV_32SC2;
    public static final int CV_32SC3;
    public static final int CV_32SC4;
    public static final int CV_64F = 6;
    public static final int CV_64FC1;
    public static final int CV_64FC2;
    public static final int CV_64FC3;
    public static final int CV_64FC4;
    public static final int CV_8S = 1;
    public static final int CV_8SC1;
    public static final int CV_8SC2;
    public static final int CV_8SC3;
    public static final int CV_8SC4;
    public static final int CV_8U = 0;
    public static final int CV_8UC1;
    public static final int CV_8UC2;
    public static final int CV_8UC3;
    public static final int CV_8UC4;
    public static final int CV_AA = 16;
    public static final CvAttrList CV_ATTR_LIST_EMPTY;
    public static final int CV_AUTOSTEP = Integer.MAX_VALUE;
    public static final int CV_AUTO_STEP = Integer.MAX_VALUE;
    public static final int CV_BACK = 0;
    public static final int CV_BadAlign = -21;
    public static final int CV_BadAlphaChannel = -18;
    public static final int CV_BadCOI = -24;
    public static final int CV_BadCallBack = -22;
    public static final int CV_BadDataPtr = -12;
    public static final int CV_BadDepth = -17;
    public static final int CV_BadImageSize = -10;
    public static final int CV_BadModelOrChSeq = -14;
    public static final int CV_BadNumChannel1U = -16;
    public static final int CV_BadNumChannels = -15;
    public static final int CV_BadOffset = -11;
    public static final int CV_BadOrder = -19;
    public static final int CV_BadOrigin = -20;
    public static final int CV_BadROISize = -25;
    public static final int CV_BadStep = -13;
    public static final int CV_BadTileSize = -23;
    public static final int CV_C = 1;
    public static final int CV_CHECK_QUIET = 2;
    public static final int CV_CHECK_RANGE = 1;
    public static final int CV_CHOLESKY = 3;
    public static final int CV_CMP_EQ = 0;
    public static final int CV_CMP_GE = 2;
    public static final int CV_CMP_GT = 1;
    public static final int CV_CMP_LE = 4;
    public static final int CV_CMP_LT = 3;
    public static final int CV_CMP_NE = 5;
    public static final int CV_CN_MAX = 512;
    public static final int CV_CN_SHIFT = 3;
    public static final int CV_COVAR_COLS = 16;
    public static final int CV_COVAR_NORMAL = 1;
    public static final int CV_COVAR_ROWS = 8;
    public static final int CV_COVAR_SCALE = 4;
    public static final int CV_COVAR_SCRAMBLED = 0;
    public static final int CV_COVAR_USE_AVG = 2;
    public static final int CV_CPU_AVX = 10;
    public static final int CV_CPU_MMX = 1;
    public static final int CV_CPU_NONE = 0;
    public static final int CV_CPU_POPCNT = 8;
    public static final int CV_CPU_SSE = 2;
    public static final int CV_CPU_SSE2 = 3;
    public static final int CV_CPU_SSE3 = 4;
    public static final int CV_CPU_SSE4_1 = 6;
    public static final int CV_CPU_SSE4_2 = 7;
    public static final int CV_CPU_SSSE3 = 5;
    public static final int CV_DEPTH_MAX = 8;
    public static final int CV_DIFF = 16;
    public static final int CV_DIFF_C = 17;
    public static final int CV_DIFF_L1 = 18;
    public static final int CV_DIFF_L2 = 20;
    public static final int CV_DXT_FORWARD = 0;
    public static final int CV_DXT_INVERSE = 1;
    public static final int CV_DXT_INVERSE_SCALE = 3;
    public static final int CV_DXT_INV_SCALE = 3;
    public static final int CV_DXT_MUL_CONJ = 8;
    public static final int CV_DXT_ROWS = 4;
    public static final int CV_DXT_SCALE = 2;
    public static final int CV_ErrModeLeaf = 0;
    public static final int CV_ErrModeParent = 1;
    public static final int CV_ErrModeSilent = 2;
    public static final int CV_FILLED = -1;
    public static final int CV_FONT_HERSHEY_COMPLEX = 3;
    public static final int CV_FONT_HERSHEY_COMPLEX_SMALL = 5;
    public static final int CV_FONT_HERSHEY_DUPLEX = 2;
    public static final int CV_FONT_HERSHEY_PLAIN = 1;
    public static final int CV_FONT_HERSHEY_SCRIPT_COMPLEX = 7;
    public static final int CV_FONT_HERSHEY_SCRIPT_SIMPLEX = 6;
    public static final int CV_FONT_HERSHEY_SIMPLEX = 0;
    public static final int CV_FONT_HERSHEY_TRIPLEX = 4;
    public static final int CV_FONT_ITALIC = 16;
    public static final int CV_FONT_VECTOR0 = 0;
    public static final int CV_FRONT = 1;
    public static final int CV_GEMM_A_T = 1;
    public static final int CV_GEMM_B_T = 2;
    public static final int CV_GEMM_C_T = 4;
    public static final int CV_GRAPH = 4096;
    public static final int CV_GRAPH_ALL_ITEMS = -1;
    public static final int CV_GRAPH_ANY_EDGE = 30;
    public static final int CV_GRAPH_BACKTRACKING = 64;
    public static final int CV_GRAPH_BACK_EDGE = 4;
    public static final int CV_GRAPH_CROSS_EDGE = 16;
    public static final int CV_GRAPH_FLAG_ORIENTED = 16384;
    public static final int CV_GRAPH_FORWARD_EDGE = 8;
    public static final int CV_GRAPH_FORWARD_EDGE_FLAG = 268435456;
    public static final int CV_GRAPH_ITEM_VISITED_FLAG = 1073741824;
    public static final int CV_GRAPH_NEW_TREE = 32;
    public static final int CV_GRAPH_OVER = -1;
    public static final int CV_GRAPH_SEARCH_TREE_NODE_FLAG = 536870912;
    public static final int CV_GRAPH_TREE_EDGE = 2;
    public static final int CV_GRAPH_VERTEX = 1;
    public static final int CV_GpuApiCallError = -217;
    public static final int CV_GpuNotSupported = -216;
    public static final int CV_HARDWARE_MAX_FEATURE = 255;
    public static final int CV_HeaderIsNull = -9;
    public static final int CV_KMEANS_USE_INITIAL_LABELS = 1;
    public static final int CV_L1 = 2;
    public static final int CV_L2 = 4;
    public static final int CV_LSQ = 8;
    public static final int CV_LU = 0;
    public static final int CV_MAGIC_MASK = -65536;
    public static final int CV_MAJOR_VERSION = 2;
    public static final int CV_MATND_MAGIC_VAL = 1111687168;
    public static final int CV_MAT_CN_MASK = 4088;
    public static final int CV_MAT_CONT_FLAG = 16384;
    public static final int CV_MAT_CONT_FLAG_SHIFT = 14;
    public static final int CV_MAT_DEPTH_MASK = 7;
    public static final int CV_MAT_MAGIC_VAL = 1111621632;
    public static final int CV_MAT_TEMP_FLAG = 32768;
    public static final int CV_MAT_TEMP_FLAG_SHIFT = 15;
    public static final int CV_MAT_TYPE_MASK = 4095;
    public static final int CV_MAX_ARR = 10;
    public static final int CV_MAX_DIM = 32;
    public static final int CV_MAX_DIM_HEAP = 1024;
    public static final int CV_MINMAX = 32;
    public static final int CV_MINOR_VERSION = 4;
    public static final int CV_MaskIsTiled = -26;
    public static final int CV_NODE_EMPTY = 32;
    public static final int CV_NODE_FLOAT = 2;
    public static final int CV_NODE_FLOW = 8;
    public static final int CV_NODE_INT = 1;
    public static final int CV_NODE_INTEGER = 1;
    public static final int CV_NODE_MAP = 6;
    public static final int CV_NODE_NAMED = 64;
    public static final int CV_NODE_NONE = 0;
    public static final int CV_NODE_REAL = 2;
    public static final int CV_NODE_REF = 4;
    public static final int CV_NODE_SEQ = 5;
    public static final int CV_NODE_SEQ_SIMPLE = 256;
    public static final int CV_NODE_STR = 3;
    public static final int CV_NODE_STRING = 3;
    public static final int CV_NODE_TYPE_MASK = 7;
    public static final int CV_NODE_USER = 16;
    public static final int CV_NORMAL = 16;
    public static final int CV_NORM_MASK = 7;
    public static final int CV_NO_CN_CHECK = 2;
    public static final int CV_NO_DEPTH_CHECK = 1;
    public static final int CV_NO_SIZE_CHECK = 4;
    public static final int CV_ORIENTED_GRAPH = 20480;
    public static final int CV_OpenGlApiCallError = -219;
    public static final int CV_OpenGlNotSupported = -218;
    public static final int CV_PCA_DATA_AS_COL = 1;
    public static final int CV_PCA_DATA_AS_ROW = 0;
    public static final int CV_PCA_USE_AVG = 2;
    public static final int CV_QR = 4;
    public static final int CV_RAND_NORMAL = 1;
    public static final int CV_RAND_UNI = 0;
    public static final int CV_REDUCE_AVG = 1;
    public static final int CV_REDUCE_MAX = 2;
    public static final int CV_REDUCE_MIN = 3;
    public static final int CV_REDUCE_SUM = 0;
    public static final int CV_RELATIVE = 8;
    public static final int CV_RELATIVE_C = 9;
    public static final int CV_RELATIVE_L1 = 10;
    public static final int CV_RELATIVE_L2 = 12;
    public static final long CV_RNG_COEFF = 4164903690L;
    public static final int CV_SEQ_CHAIN;
    public static final int CV_SEQ_CHAIN_CONTOUR;
    public static final int CV_SEQ_CONNECTED_COMP = 0;
    public static final int CV_SEQ_CONTOUR;
    public static final int CV_SEQ_ELTYPE_BITS = 12;
    public static final int CV_SEQ_ELTYPE_CODE;
    public static final int CV_SEQ_ELTYPE_CONNECTED_COMP = 0;
    public static final int CV_SEQ_ELTYPE_GENERIC = 0;
    public static final int CV_SEQ_ELTYPE_GRAPH_EDGE = 0;
    public static final int CV_SEQ_ELTYPE_GRAPH_VERTEX = 0;
    public static final int CV_SEQ_ELTYPE_INDEX;
    public static final int CV_SEQ_ELTYPE_MASK = 4095;
    public static final int CV_SEQ_ELTYPE_POINT;
    public static final int CV_SEQ_ELTYPE_POINT3D;
    public static final int CV_SEQ_ELTYPE_PPOINT = 7;
    public static final int CV_SEQ_ELTYPE_PTR = 7;
    public static final int CV_SEQ_ELTYPE_TRIAN_ATR = 0;
    public static final int CV_SEQ_FLAG_CLOSED = 16384;
    public static final int CV_SEQ_FLAG_CONVEX = 0;
    public static final int CV_SEQ_FLAG_HOLE = 32768;
    public static final int CV_SEQ_FLAG_SHIFT = 14;
    public static final int CV_SEQ_FLAG_SIMPLE = 0;
    public static final int CV_SEQ_INDEX;
    public static final int CV_SEQ_KIND_BIN_TREE = 8192;
    public static final int CV_SEQ_KIND_BITS = 2;
    public static final int CV_SEQ_KIND_CURVE = 4096;
    public static final int CV_SEQ_KIND_GENERIC = 0;
    public static final int CV_SEQ_KIND_GRAPH = 4096;
    public static final int CV_SEQ_KIND_MASK = 12288;
    public static final int CV_SEQ_KIND_SUBDIV2D = 8192;
    public static final int CV_SEQ_MAGIC_VAL = 1117323264;
    public static final int CV_SEQ_POINT3D_SET;
    public static final int CV_SEQ_POINT_SET;
    public static final int CV_SEQ_POLYGON;
    public static final int CV_SEQ_POLYGON_TREE = 8192;
    public static final int CV_SEQ_POLYLINE;
    public static final int CV_SEQ_SIMPLE_POLYGON;
    public static final int CV_SET_ELEM_FREE_FLAG = 128;
    public static final int CV_SET_ELEM_IDX_MASK = 67108863;
    public static final int CV_SET_MAGIC_VAL = 1117257728;
    public static final int CV_SORT_ASCENDING = 0;
    public static final int CV_SORT_DESCENDING = 16;
    public static final int CV_SORT_EVERY_COLUMN = 1;
    public static final int CV_SORT_EVERY_ROW = 0;
    public static final int CV_SPARSE_MAT_MAGIC_VAL = 1111752704;
    public static final int CV_STORAGE_APPEND = 2;
    public static final int CV_STORAGE_FORMAT_AUTO = 0;
    public static final int CV_STORAGE_FORMAT_MASK = 56;
    public static final int CV_STORAGE_FORMAT_XML = 8;
    public static final int CV_STORAGE_FORMAT_YAML = 16;
    public static final int CV_STORAGE_MAGIC_VAL = 1116274688;
    public static final int CV_STORAGE_MEMORY = 4;
    public static final int CV_STORAGE_READ = 0;
    public static final int CV_STORAGE_WRITE = 1;
    public static final int CV_STORAGE_WRITE_BINARY = 1;
    public static final int CV_STORAGE_WRITE_TEXT = 1;
    public static final int CV_SUBMINOR_VERSION = 6;
    public static final int CV_SVD = 1;
    public static final int CV_SVD_MODIFY_A = 1;
    public static final int CV_SVD_SYM = 2;
    public static final int CV_SVD_U_T = 2;
    public static final int CV_SVD_V_T = 4;
    public static final int CV_StsAssert = -215;
    public static final int CV_StsAutoTrace = -8;
    public static final int CV_StsBackTrace = -1;
    public static final int CV_StsBadArg = -5;
    public static final int CV_StsBadFlag = -206;
    public static final int CV_StsBadFunc = -6;
    public static final int CV_StsBadMask = -208;
    public static final int CV_StsBadMemBlock = -214;
    public static final int CV_StsBadPoint = -207;
    public static final int CV_StsBadSize = -201;
    public static final int CV_StsDivByZero = -202;
    public static final int CV_StsError = -2;
    public static final int CV_StsFilterOffsetErr = -31;
    public static final int CV_StsFilterStructContentErr = -29;
    public static final int CV_StsInplaceNotSupported = -203;
    public static final int CV_StsInternal = -3;
    public static final int CV_StsKernelStructContentErr = -30;
    public static final int CV_StsNoConv = -7;
    public static final int CV_StsNoMem = -4;
    public static final int CV_StsNotImplemented = -213;
    public static final int CV_StsNullPtr = -27;
    public static final int CV_StsObjectNotFound = -204;
    public static final int CV_StsOk = 0;
    public static final int CV_StsOutOfRange = -211;
    public static final int CV_StsParseError = -212;
    public static final int CV_StsUnmatchedFormats = -205;
    public static final int CV_StsUnmatchedSizes = -209;
    public static final int CV_StsUnsupportedFormat = -210;
    public static final int CV_StsVecLengthErr = -28;
    public static final int CV_TERMCRIT_EPS = 2;
    public static final int CV_TERMCRIT_ITER = 1;
    public static final int CV_TERMCRIT_NUMBER = 1;
    public static final String CV_TYPE_NAME_GRAPH = "opencv-graph";
    public static final String CV_TYPE_NAME_IMAGE = "opencv-image";
    public static final String CV_TYPE_NAME_MAT = "opencv-matrix";
    public static final String CV_TYPE_NAME_MATND = "opencv-nd-matrix";
    public static final String CV_TYPE_NAME_SEQ = "opencv-sequence";
    public static final String CV_TYPE_NAME_SEQ_TREE = "opencv-sequence-tree";
    public static final String CV_TYPE_NAME_SPARSE_MAT = "opencv-sparse-matrix";
    public static final int CV_USRTYPE1 = 7;
    public static final String CV_VERSION = "2.4.6.1";
    public static final int CV_VERSION_EPOCH = 2;
    public static final int CV_VERSION_MAJOR = 4;
    public static final int CV_VERSION_MINOR = 6;
    public static final int CV_VERSION_REVISION = 1;
    public static final CvSlice CV_WHOLE_ARR;
    public static final CvSlice CV_WHOLE_SEQ;
    public static final int CV_WHOLE_SEQ_END_INDEX = 1073741823;
    public static final int IPL_ALIGN_16BYTES = 16;
    public static final int IPL_ALIGN_32BYTES = 32;
    public static final int IPL_ALIGN_4BYTES = 4;
    public static final int IPL_ALIGN_8BYTES = 8;
    public static final int IPL_ALIGN_DWORD = 4;
    public static final int IPL_ALIGN_QWORD = 8;
    public static final int IPL_BORDER_CONSTANT = 0;
    public static final int IPL_BORDER_REFLECT = 2;
    public static final int IPL_BORDER_REFLECT_101 = 4;
    public static final int IPL_BORDER_REPLICATE = 1;
    public static final int IPL_BORDER_TRANSPARENT = 5;
    public static final int IPL_BORDER_WRAP = 3;
    public static final int IPL_DATA_ORDER_PIXEL = 0;
    public static final int IPL_DATA_ORDER_PLANE = 1;
    public static final int IPL_DEPTH_16S = -2147483632;
    public static final int IPL_DEPTH_16U = 16;
    public static final int IPL_DEPTH_1U = 1;
    public static final int IPL_DEPTH_32F = 32;
    public static final int IPL_DEPTH_32S = -2147483616;
    public static final int IPL_DEPTH_64F = 64;
    public static final int IPL_DEPTH_8S = -2147483640;
    public static final int IPL_DEPTH_8U = 8;
    public static final int IPL_DEPTH_SIGN = Integer.MIN_VALUE;
    public static final int IPL_IMAGE_DATA = 2;
    public static final int IPL_IMAGE_HEADER = 1;
    public static final int IPL_IMAGE_MAGIC_VAL;
    public static final int IPL_IMAGE_ROI = 4;
    public static final int IPL_ORIGIN_BL = 1;
    public static final int IPL_ORIGIN_TL = 0;
    public static final int NORM_HAMMING = 6;
    public static final int NORM_HAMMING2 = 7;
    public static final int NORM_INF = 1;
    public static final int NORM_L1 = 2;
    public static final int NORM_L2 = 4;
    public static final int NORM_L2SQR = 5;
    public static final int NORM_MINMAX = 32;
    public static final int NORM_RELATIVE = 8;
    public static final int NORM_TYPE_MASK = 7;

    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @Const
    @Retention(RetentionPolicy.RUNTIME)
    @Adapter("ArrayAdapter")
    public @interface InputArray {
    }

    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @Const
    @Retention(RetentionPolicy.RUNTIME)
    @Adapter("MatAdapter")
    public @interface InputMat {
    }

    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Adapter("ArrayAdapter")
    public @interface OutputArray {
    }

    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Adapter("MatAdapter")
    public @interface OutputMat {
    }

    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Adapter("PtrAdapter")
    @Cast({"cv::Ptr", "&"})
    public @interface Ptr {
        String value() default "";
    }

    public static native void CV_NEXT_LINE_POINT(@ByVal CvLineIterator cvLineIterator);

    public static native void CV_NEXT_SEQ_ELEM(int i, @ByVal CvSeqReader cvSeqReader);

    public static native void CV_PREV_SEQ_ELEM(int i, @ByVal CvSeqReader cvSeqReader);

    public static native void CV_READ_SEQ_ELEM(@ByVal CvPoint cvPoint, @ByVal CvSeqReader cvSeqReader);

    public static native void CV_REV_READ_SEQ_ELEM(@ByVal CvPoint cvPoint, @ByVal CvSeqReader cvSeqReader);

    public static native void CV_WRITE_SEQ_ELEM(@ByVal CvPoint cvPoint, @ByVal CvSeqWriter cvSeqWriter);

    public static native void CV_WRITE_SEQ_ELEM_VAR(Pointer pointer, @ByVal CvSeqWriter cvSeqWriter);

    public static native void SetLibraryPath(String str);

    @Namespace("cv")
    public static native void batchDistance(@InputArray CvArr cvArr, @InputArray CvArr cvArr2, @OutputArray CvMat cvMat, int i, @OutputArray CvMat cvMat2, int i2, int i3, @InputArray CvArr cvArr3, int i4, @Cast({"bool"}) boolean z);

    public static native void cvAbsDiff(CvArr cvArr, CvArr cvArr2, CvArr cvArr3);

    public static native void cvAbsDiffS(CvArr cvArr, CvArr cvArr2, @ByVal CvScalar cvScalar);

    public static native void cvAdd(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4);

    public static native void cvAddS(CvArr cvArr, @ByVal CvScalar cvScalar, CvArr cvArr2, CvArr cvArr3);

    public static native void cvAddWeighted(CvArr cvArr, double d, CvArr cvArr2, double d2, double d3, CvArr cvArr3);

    public static native Pointer cvAlloc(@Cast({"size_t"}) long j);

    public static native void cvAnd(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4);

    public static native void cvAndS(CvArr cvArr, @ByVal CvScalar cvScalar, CvArr cvArr2, CvArr cvArr3);

    public static native String cvAttrValue(CvAttrList cvAttrList, String str);

    @ByVal
    public static native CvScalar cvAvg(CvArr cvArr, CvArr cvArr2);

    public static native void cvAvgSdv(CvArr cvArr, CvScalar cvScalar, CvScalar cvScalar2, CvArr cvArr2);

    public static native void cvBackProjectPCA(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4);

    public static native void cvCalcCovarMatrix(@Const CvArrArray cvArrArray, int i, CvArr cvArr, CvArr cvArr2, int i2);

    public static native void cvCalcPCA(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4, int i);

    public static native void cvCartToPolar(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4, int i);

    public static native float cvCbrt(float f);

    public static native void cvChangeSeqBlock(CvSeqReader cvSeqReader, int i);

    public static native int cvCheckArr(CvArr cvArr, int i, double d, double d2);

    public static native int cvCheckHardwareSupport(int i);

    @ByVal
    public static native CvTermCriteria cvCheckTermCriteria(@ByVal CvTermCriteria cvTermCriteria, double d, int i);

    public static native void cvCircle(CvArr cvArr, @ByVal CvPoint cvPoint, int i, @ByVal CvScalar cvScalar, int i2, int i3, int i4);

    public static native void cvClearGraph(CvGraph cvGraph);

    public static native void cvClearMemStorage(CvMemStorage cvMemStorage);

    public static native void cvClearND(CvArr cvArr, int[] iArr);

    public static native void cvClearSeq(CvSeq cvSeq);

    public static native void cvClearSet(CvSet cvSet);

    public static native int cvClipLine(@ByVal CvSize cvSize, CvPoint cvPoint, CvPoint cvPoint2);

    public static native Pointer cvClone(Pointer pointer);

    public static native CvGraph cvCloneGraph(CvGraph cvGraph, CvMemStorage cvMemStorage);

    public static native IplImage cvCloneImage(IplImage iplImage);

    public static native CvMat cvCloneMat(CvMat cvMat);

    public static native CvMatND cvCloneMatND(CvMatND cvMatND);

    public static native CvSparseMat cvCloneSparseMat(CvSparseMat cvSparseMat);

    public static native void cvCmp(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, int i);

    public static native void cvCmpS(CvArr cvArr, double d, CvArr cvArr2, int i);

    @ByVal
    public static native CvScalar cvColorToScalar(double d, int i);

    public static native void cvCompleteSymm(CvMat cvMat, int i);

    public static native void cvConvertScale(CvArr cvArr, CvArr cvArr2, double d, double d2);

    public static native void cvConvertScaleAbs(CvArr cvArr, CvArr cvArr2, double d, double d2);

    public static native void cvCopy(CvArr cvArr, CvArr cvArr2, CvArr cvArr3);

    public static native int cvCountNonZero(CvArr cvArr);

    public static native CvMemStorage cvCreateChildMemStorage(CvMemStorage cvMemStorage);

    public static native void cvCreateData(CvArr cvArr);

    public static native CvGraph cvCreateGraph(int i, int i2, int i3, int i4, CvMemStorage cvMemStorage);

    public static native CvGraphScanner cvCreateGraphScanner(CvGraph cvGraph, CvGraphVtx cvGraphVtx, int i);

    public static native IplImage cvCreateImage(@ByVal CvSize cvSize, int i, int i2);

    public static native IplImage cvCreateImageHeader(@ByVal CvSize cvSize, int i, int i2);

    public static native CvMat cvCreateMat(int i, int i2, int i3);

    public static native CvMat cvCreateMatHeader(int i, int i2, int i3);

    public static native CvMatND cvCreateMatND(int i, int[] iArr, int i2);

    public static native CvMatND cvCreateMatNDHeader(int i, int[] iArr, int i2);

    public static native CvMemStorage cvCreateMemStorage(int i);

    public static native CvSeq cvCreateSeq(int i, @Cast({"size_t"}) long j, int i2, CvMemStorage cvMemStorage);

    public static native void cvCreateSeqBlock(CvSeqWriter cvSeqWriter);

    public static native CvSet cvCreateSet(int i, int i2, int i3, CvMemStorage cvMemStorage);

    public static native CvSparseMat cvCreateSparseMat(int i, int[] iArr, int i2);

    public static native void cvCrossProduct(CvArr cvArr, CvArr cvArr2, CvArr cvArr3);

    public static native Pointer cvCvtSeqToArray(CvSeq cvSeq, Pointer pointer, @ByVal CvSlice cvSlice);

    public static native Pointer cvCvtSeqToArray(CvSeq cvSeq, Buffer buffer, @ByVal CvSlice cvSlice);

    public static native void cvDCT(CvArr cvArr, CvArr cvArr2, int i);

    public static native void cvDFT(CvArr cvArr, CvArr cvArr2, int i, int i2);

    public static native double cvDet(CvArr cvArr);

    public static native void cvDiv(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, double d);

    public static native double cvDotProduct(CvArr cvArr, CvArr cvArr2);

    public static native void cvDrawContours(CvArr cvArr, CvSeq cvSeq, @ByVal CvScalar cvScalar, @ByVal CvScalar cvScalar2, int i, int i2, int i3, @ByVal CvPoint cvPoint);

    public static native void cvEigenVV(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, double d, int i, int i2);

    public static native void cvEllipse(CvArr cvArr, @ByVal CvPoint cvPoint, @ByVal CvSize cvSize, double d, double d2, double d3, @ByVal CvScalar cvScalar, int i, int i2, int i3);

    public static native int cvEllipse2Poly(@ByVal CvPoint cvPoint, @ByVal CvSize cvSize, int i, int i2, int i3, CvPoint cvPoint2, int i4);

    public static native CvSeq cvEndWriteSeq(CvSeqWriter cvSeqWriter);

    public static native void cvEndWriteStruct(CvFileStorage cvFileStorage);

    public static native void cvError(int i, String str, String str2, String str3, int i2);

    public static native int cvErrorFromIppStatus(int i);

    public static native String cvErrorStr(int i);

    public static native void cvExp(CvArr cvArr, CvArr cvArr2);

    public static native float cvFastArctan(float f, float f2);

    public static native void cvFillConvexPoly(CvArr cvArr, CvPoint cvPoint, int i, @ByVal CvScalar cvScalar, int i2, int i3);

    public static native void cvFillConvexPoly(CvArr cvArr, @Cast({"CvPoint*"}) int[] iArr, int i, @ByVal CvScalar cvScalar, int i2, int i3);

    public static native void cvFillPoly(CvArr cvArr, @Cast({"CvPoint**"}) PointerPointer pointerPointer, int[] iArr, int i, @ByVal CvScalar cvScalar, int i2, int i3);

    public static native void cvFillPoly(CvArr cvArr, @ByPtrPtr CvPoint cvPoint, int[] iArr, int i, @ByVal CvScalar cvScalar, int i2, int i3);

    public static native CvGraphEdge cvFindGraphEdge(CvGraph cvGraph, int i, int i2);

    public static native CvGraphEdge cvFindGraphEdgeByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx, CvGraphVtx cvGraphVtx2);

    public static native CvTypeInfo cvFindType(String str);

    public static native CvTypeInfo cvFirstType();

    public static native void cvFlip(CvArr cvArr, CvArr cvArr2, int i);

    public static native void cvFlushSeqWriter(CvSeqWriter cvSeqWriter);

    public static native void cvFree_(Pointer pointer);

    public static native void cvGEMM(CvArr cvArr, CvArr cvArr2, double d, CvArr cvArr3, double d2, CvArr cvArr4, int i);

    @ByVal
    public static native CvScalar cvGet1D(CvArr cvArr, int i);

    @ByVal
    public static native CvScalar cvGet2D(CvArr cvArr, int i, int i2);

    @ByVal
    public static native CvScalar cvGet3D(CvArr cvArr, int i, int i2, int i3);

    public static native CvMat cvGetCols(CvArr cvArr, CvMat cvMat, int i, int i2);

    public static native CvMat cvGetDiag(CvArr cvArr, CvMat cvMat, int i);

    public static native int cvGetDimSize(CvArr cvArr, int i);

    public static native int cvGetDims(CvArr cvArr, int[] iArr);

    public static native int cvGetElemType(CvArr cvArr);

    public static native int cvGetErrInfo(@ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer2, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer3, int[] iArr);

    public static native int cvGetErrMode();

    public static native int cvGetErrStatus();

    public static native CvFileNode cvGetFileNode(CvFileStorage cvFileStorage, CvFileNode cvFileNode, CvStringHashNode cvStringHashNode, int i);

    public static native CvFileNode cvGetFileNodeByName(CvFileStorage cvFileStorage, CvFileNode cvFileNode, String str);

    public static native String cvGetFileNodeName(CvFileNode cvFileNode);

    public static native CvStringHashNode cvGetHashedKey(CvFileStorage cvFileStorage, String str, int i, int i2);

    public static native IplImage cvGetImage(CvArr cvArr, IplImage iplImage);

    public static native int cvGetImageCOI(IplImage iplImage);

    @ByVal
    public static native CvRect cvGetImageROI(IplImage iplImage);

    public static native CvMat cvGetMat(CvArr cvArr, CvMat cvMat, int[] iArr, int i);

    public static native void cvGetModuleInfo(String str, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer2);

    @ByVal
    public static native CvScalar cvGetND(CvArr cvArr, int[] iArr);

    public static native CvSparseNode cvGetNextSparseNode(CvSparseMatIterator cvSparseMatIterator);

    public static native int cvGetNumThreads();

    public static native int cvGetOptimalDFTSize(int i);

    public static native void cvGetRawData(CvArr cvArr, @ByPtrPtr @Cast({"uchar**"}) BytePointer bytePointer, int[] iArr, CvSize cvSize);

    public static native double cvGetReal1D(CvArr cvArr, int i);

    public static native double cvGetReal2D(CvArr cvArr, int i, int i2);

    public static native double cvGetReal3D(CvArr cvArr, int i, int i2, int i3);

    public static native double cvGetRealND(CvArr cvArr, int[] iArr);

    public static native CvFileNode cvGetRootFileNode(CvFileStorage cvFileStorage, int i);

    public static native CvMat cvGetRows(CvArr cvArr, CvMat cvMat, int i, int i2, int i3);

    public static native Pointer cvGetSeqElem(CvSeq cvSeq, int i);

    public static native int cvGetSeqReaderPos(CvSeqReader cvSeqReader);

    @ByVal
    public static native CvSize cvGetSize(CvArr cvArr);

    public static native CvMat cvGetSubRect(CvArr cvArr, CvMat cvMat, @ByVal CvRect cvRect);

    public static native void cvGetTextSize(String str, CvFont cvFont, CvSize cvSize, int[] iArr);

    public static native int cvGetThreadNum();

    public static native long cvGetTickCount();

    public static native double cvGetTickFrequency();

    public static native int cvGraphAddEdge(CvGraph cvGraph, int i, int i2, CvGraphEdge cvGraphEdge, @ByPtrPtr CvGraphEdge cvGraphEdge2);

    public static native int cvGraphAddEdgeByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx, CvGraphVtx cvGraphVtx2, CvGraphEdge cvGraphEdge, @ByPtrPtr CvGraphEdge cvGraphEdge2);

    public static native int cvGraphAddVtx(CvGraph cvGraph, CvGraphVtx cvGraphVtx, @ByPtrPtr CvGraphVtx cvGraphVtx2);

    public static native void cvGraphRemoveEdge(CvGraph cvGraph, int i, int i2);

    public static native void cvGraphRemoveEdgeByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx, CvGraphVtx cvGraphVtx2);

    public static native int cvGraphRemoveVtx(CvGraph cvGraph, int i);

    public static native int cvGraphRemoveVtxByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx);

    public static native int cvGraphVtxDegree(CvGraph cvGraph, int i);

    public static native int cvGraphVtxDegreeByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx);

    public static native int cvGuiBoxReport(int i, String str, String str2, String str3, int i2, Pointer pointer);

    public static native void cvInRange(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4);

    public static native void cvInRangeS(CvArr cvArr, @ByVal CvScalar cvScalar, @ByVal CvScalar cvScalar2, CvArr cvArr2);

    public static native void cvInitFont(CvFont cvFont, int i, double d, double d2, double d3, int i2, int i3);

    public static native IplImage cvInitImageHeader(IplImage iplImage, @ByVal CvSize cvSize, int i, int i2, int i3, int i4);

    public static native int cvInitLineIterator(CvArr cvArr, @ByVal CvPoint cvPoint, @ByVal CvPoint cvPoint2, CvLineIterator cvLineIterator, int i, int i2);

    public static native CvMat cvInitMatHeader(CvMat cvMat, int i, int i2, int i3, Pointer pointer, int i4);

    public static native CvMatND cvInitMatNDHeader(CvMatND cvMatND, int i, int[] iArr, int i2, Pointer pointer);

    public static native int cvInitNArrayIterator(int i, CvArrArray cvArrArray, CvArr cvArr, CvMatND cvMatND, CvNArrayIterator cvNArrayIterator, int i2);

    public static native CvSparseNode cvInitSparseMatIterator(CvSparseMat cvSparseMat, CvSparseMatIterator cvSparseMatIterator);

    public static native void cvInitTreeNodeIterator(CvTreeNodeIterator cvTreeNodeIterator, Pointer pointer, int i);

    public static native void cvInsertNodeIntoTree(Pointer pointer, Pointer pointer2, Pointer pointer3);

    public static native double cvInvert(CvArr cvArr, CvArr cvArr2, int i);

    public static native int cvKMeans2(CvArr cvArr, int i, CvArr cvArr2, @ByVal CvTermCriteria cvTermCriteria, int i2, CvRNG cvRNG, int i3, CvArr cvArr3, double[] dArr);

    public static native void cvLUT(CvArr cvArr, CvArr cvArr2, CvArr cvArr3);

    public static native void cvLine(CvArr cvArr, @ByVal CvPoint cvPoint, @ByVal CvPoint cvPoint2, @ByVal CvScalar cvScalar, int i, int i2, int i3);

    public static native Pointer cvLoad(String str, CvMemStorage cvMemStorage, String str2, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer);

    public static native void cvLog(CvArr cvArr, CvArr cvArr2);

    public static native double cvMahalanobis(CvArr cvArr, CvArr cvArr2, CvArr cvArr3);

    public static native CvSeq cvMakeSeqHeaderForArray(int i, int i2, int i3, Pointer pointer, int i4, CvSeq cvSeq, CvSeqBlock cvSeqBlock);

    public static native void cvMax(CvArr cvArr, CvArr cvArr2, CvArr cvArr3);

    public static native void cvMaxS(CvArr cvArr, double d, CvArr cvArr2);

    public static native Pointer cvMemStorageAlloc(CvMemStorage cvMemStorage, @Cast({"size_t"}) long j);

    @ByVal
    public static native CvString cvMemStorageAllocString(CvMemStorage cvMemStorage, @Cast({"const char*"}) BytePointer bytePointer, int i);

    @ByVal
    public static native CvString cvMemStorageAllocString(CvMemStorage cvMemStorage, String str, int i);

    public static native void cvMerge(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4, CvArr cvArr5);

    public static native void cvMin(CvArr cvArr, CvArr cvArr2, CvArr cvArr3);

    public static native void cvMinMaxLoc(CvArr cvArr, double[] dArr, double[] dArr2, CvPoint cvPoint, CvPoint cvPoint2, CvArr cvArr2);

    public static native void cvMinMaxLoc(CvArr cvArr, double[] dArr, double[] dArr2, @Cast({"CvPoint*"}) int[] iArr, @Cast({"CvPoint*"}) int[] iArr2, CvArr cvArr2);

    public static native void cvMinS(CvArr cvArr, double d, CvArr cvArr2);

    public static native void cvMixChannels(@Const CvArrArray cvArrArray, int i, CvArrArray cvArrArray2, int i2, int[] iArr, int i3);

    public static native void cvMul(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, double d);

    public static native void cvMulSpectrums(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, int i);

    public static native void cvMulTransposed(CvArr cvArr, CvArr cvArr2, int i, CvArr cvArr3, double d);

    public static native int cvNextGraphItem(CvGraphScanner cvGraphScanner);

    public static native int cvNextNArraySlice(CvNArrayIterator cvNArrayIterator);

    public static native Pointer cvNextTreeNode(CvTreeNodeIterator cvTreeNodeIterator);

    public static native double cvNorm(CvArr cvArr, CvArr cvArr2, int i, CvArr cvArr3);

    public static native void cvNormalize(CvArr cvArr, CvArr cvArr2, double d, double d2, int i, CvArr cvArr3);

    public static native void cvNot(CvArr cvArr, CvArr cvArr2);

    public static native int cvNulDevReport(int i, String str, String str2, String str3, int i2, Pointer pointer);

    public static native CvFileStorage cvOpenFileStorage(String str, CvMemStorage cvMemStorage, int i, String str2);

    public static native void cvOr(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4);

    public static native void cvOrS(CvArr cvArr, @ByVal CvScalar cvScalar, CvArr cvArr2, CvArr cvArr3);

    public static native void cvPerspectiveTransform(CvArr cvArr, CvArr cvArr2, CvMat cvMat);

    public static native void cvPolarToCart(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4, int i);

    public static native void cvPolyLine(CvArr cvArr, @Cast({"CvPoint**"}) PointerPointer pointerPointer, int[] iArr, int i, int i2, @ByVal CvScalar cvScalar, int i3, int i4, int i5);

    public static native void cvPolyLine(CvArr cvArr, @ByPtrPtr CvPoint cvPoint, int[] iArr, int i, int i2, @ByVal CvScalar cvScalar, int i3, int i4, int i5);

    public static native void cvPow(CvArr cvArr, CvArr cvArr2, double d);

    public static native Pointer cvPrevTreeNode(CvTreeNodeIterator cvTreeNodeIterator);

    public static native void cvProjectPCA(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4);

    public static native Pointer cvPtr1D(CvArr cvArr, int i, int[] iArr);

    public static native Pointer cvPtr2D(CvArr cvArr, int i, int i2, int[] iArr);

    public static native Pointer cvPtr3D(CvArr cvArr, int i, int i2, int i3, int[] iArr);

    public static native Pointer cvPtrND(CvArr cvArr, int[] iArr, int[] iArr2, int i, @Cast({"unsigned*"}) int[] iArr3);

    public static native void cvPutText(CvArr cvArr, String str, @ByVal CvPoint cvPoint, CvFont cvFont, @ByVal CvScalar cvScalar);

    public static native void cvRandArr(CvRNG cvRNG, CvArr cvArr, int i, @ByVal CvScalar cvScalar, @ByVal CvScalar cvScalar2);

    public static native void cvRandShuffle(CvArr cvArr, CvRNG cvRNG, double d);

    public static native CvArr cvRange(CvArr cvArr, double d, double d2);

    public static native void cvRawDataToScalar(Pointer pointer, int i, CvScalar cvScalar);

    public static native Pointer cvRead(CvFileStorage cvFileStorage, CvFileNode cvFileNode, CvAttrList cvAttrList);

    public static native void cvReadRawData(CvFileStorage cvFileStorage, CvFileNode cvFileNode, Pointer pointer, String str);

    public static native void cvReadRawDataSlice(CvFileStorage cvFileStorage, CvSeqReader cvSeqReader, int i, Pointer pointer, String str);

    public static native void cvRectangle(CvArr cvArr, @ByVal CvPoint cvPoint, @ByVal CvPoint cvPoint2, @ByVal CvScalar cvScalar, int i, int i2, int i3);

    public static native void cvRectangleR(CvArr cvArr, @ByVal CvRect cvRect, @ByVal CvScalar cvScalar, int i, int i2, int i3);

    public static native CvErrorCallback cvRedirectError(CvErrorCallback cvErrorCallback, Pointer pointer, @ByPtrPtr @Cast({"void**"}) Pointer pointer2);

    public static native void cvReduce(CvArr cvArr, CvArr cvArr2, int i, int i2);

    public static native int cvRegisterModule(CvModuleInfo cvModuleInfo);

    public static native void cvRegisterType(CvTypeInfo cvTypeInfo);

    public static native void cvRelease(PointerPointer pointerPointer);

    public static native void cvReleaseData(CvArr cvArr);

    public static native void cvReleaseFileStorage(@ByPtrPtr CvFileStorage cvFileStorage);

    public static native void cvReleaseGraphScanner(@ByPtrPtr CvGraphScanner cvGraphScanner);

    public static native void cvReleaseImage(@ByPtrPtr IplImage iplImage);

    public static native void cvReleaseImageHeader(@ByPtrPtr IplImage iplImage);

    public static native void cvReleaseMat(@ByPtrPtr CvMat cvMat);

    public static native void cvReleaseMatND(@ByPtrPtr CvMatND cvMatND);

    public static native void cvReleaseMemStorage(@ByPtrPtr CvMemStorage cvMemStorage);

    public static native void cvReleaseSparseMat(@ByPtrPtr CvSparseMat cvSparseMat);

    public static native void cvRemoveNodeFromTree(Pointer pointer, Pointer pointer2);

    public static native void cvRepeat(CvArr cvArr, CvArr cvArr2);

    public static native void cvResetImageROI(IplImage iplImage);

    public static native CvMat cvReshape(CvArr cvArr, CvMat cvMat, int i, int i2);

    public static native CvArr cvReshapeMatND(CvArr cvArr, int i, CvArr cvArr2, int i2, int i3, int[] iArr);

    public static native void cvRestoreMemStoragePos(CvMemStorage cvMemStorage, CvMemStoragePos cvMemStoragePos);

    public static native void cvSVBkSb(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4, CvArr cvArr5, int i);

    public static native void cvSVD(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4, int i);

    public static native void cvSave(String str, Pointer pointer, String str2, String str3, @ByVal CvAttrList cvAttrList);

    public static native void cvSaveMemStoragePos(CvMemStorage cvMemStorage, CvMemStoragePos cvMemStoragePos);

    public static native void cvScalarToRawData(CvScalar cvScalar, Pointer pointer, int i, int i2);

    public static native void cvScaleAdd(CvArr cvArr, @ByVal CvScalar cvScalar, CvArr cvArr2, CvArr cvArr3);

    public static native int cvSeqElemIdx(CvSeq cvSeq, Pointer pointer, @ByPtrPtr CvSeqBlock cvSeqBlock);

    public static native Pointer cvSeqInsert(CvSeq cvSeq, int i, Pointer pointer);

    public static native void cvSeqInsertSlice(CvSeq cvSeq, int i, CvArr cvArr);

    public static native void cvSeqInvert(CvSeq cvSeq);

    public static native int cvSeqPartition(CvSeq cvSeq, CvMemStorage cvMemStorage, @ByPtrPtr CvSeq cvSeq2, CvCmpFunc cvCmpFunc, Pointer pointer);

    public static native void cvSeqPop(CvSeq cvSeq, Pointer pointer);

    public static native void cvSeqPopFront(CvSeq cvSeq, Pointer pointer);

    public static native void cvSeqPopMulti(CvSeq cvSeq, Pointer pointer, int i, int i2);

    @Cast({"schar*"})
    public static native BytePointer cvSeqPush(CvSeq cvSeq, Pointer pointer);

    @Cast({"schar*"})
    public static native BytePointer cvSeqPushFront(CvSeq cvSeq, Pointer pointer);

    public static native void cvSeqPushMulti(CvSeq cvSeq, Pointer pointer, int i, int i2);

    public static native void cvSeqRemove(CvSeq cvSeq, int i);

    public static native void cvSeqRemoveSlice(CvSeq cvSeq, @ByVal CvSlice cvSlice);

    public static native Pointer cvSeqSearch(CvSeq cvSeq, Pointer pointer, CvCmpFunc cvCmpFunc, int i, int[] iArr, Pointer pointer2);

    public static native CvSeq cvSeqSlice(CvSeq cvSeq, @ByVal CvSlice cvSlice, CvMemStorage cvMemStorage, int i);

    public static native void cvSeqSort(CvSeq cvSeq, CvCmpFunc cvCmpFunc, Pointer pointer);

    public static native void cvSet(CvArr cvArr, @ByVal CvScalar cvScalar, CvArr cvArr2);

    public static native void cvSet1D(CvArr cvArr, int i, @ByVal CvScalar cvScalar);

    public static native void cvSet2D(CvArr cvArr, int i, int i2, @ByVal CvScalar cvScalar);

    public static native void cvSet3D(CvArr cvArr, int i, int i2, int i3, @ByVal CvScalar cvScalar);

    public static native int cvSetAdd(CvSet cvSet, CvSetElem cvSetElem, @ByPtrPtr CvSetElem cvSetElem2);

    public static native void cvSetData(CvArr cvArr, Pointer pointer, int i);

    public static native int cvSetErrMode(int i);

    public static native void cvSetErrStatus(int i);

    public static native void cvSetIPLAllocators(Cv_iplCreateImageHeader cv_iplCreateImageHeader, Cv_iplAllocateImageData cv_iplAllocateImageData, Cv_iplDeallocate cv_iplDeallocate, Cv_iplCreateROI cv_iplCreateROI, Cv_iplCloneImage cv_iplCloneImage);

    public static native void cvSetIdentity(CvArr cvArr, @ByVal CvScalar cvScalar);

    public static native void cvSetImageCOI(IplImage iplImage, int i);

    public static native void cvSetImageROI(IplImage iplImage, @ByVal CvRect cvRect);

    public static native void cvSetMemoryManager(CvAllocFunc cvAllocFunc, CvFreeFunc cvFreeFunc, Pointer pointer);

    public static native void cvSetND(CvArr cvArr, int[] iArr, @ByVal CvScalar cvScalar);

    public static native void cvSetNumThreads(int i);

    public static native void cvSetReal1D(CvArr cvArr, int i, double d);

    public static native void cvSetReal2D(CvArr cvArr, int i, int i2, double d);

    public static native void cvSetReal3D(CvArr cvArr, int i, int i2, int i3, double d);

    public static native void cvSetRealND(CvArr cvArr, int[] iArr, double d);

    public static native void cvSetRemove(CvSet cvSet, int i);

    public static native void cvSetSeqBlockSize(CvSeq cvSeq, int i);

    public static native void cvSetSeqReaderPos(CvSeqReader cvSeqReader, int i, int i2);

    public static native void cvSetZero(CvArr cvArr);

    public static native int cvSliceLength(@ByVal CvSlice cvSlice, CvSeq cvSeq);

    public static native int cvSolve(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, int i);

    public static native int cvSolveCubic(CvMat cvMat, CvMat cvMat2);

    public static native void cvSolvePoly(CvMat cvMat, CvMat cvMat2, int i, int i2);

    public static native void cvSort(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, int i);

    public static native void cvSplit(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4, CvArr cvArr5);

    public static native void cvStartAppendToSeq(CvSeq cvSeq, CvSeqWriter cvSeqWriter);

    public static native void cvStartNextStream(CvFileStorage cvFileStorage);

    public static native void cvStartReadRawData(CvFileStorage cvFileStorage, CvFileNode cvFileNode, CvSeqReader cvSeqReader);

    public static native void cvStartReadSeq(CvSeq cvSeq, CvSeqReader cvSeqReader, int i);

    public static native void cvStartWriteSeq(int i, int i2, int i3, CvMemStorage cvMemStorage, CvSeqWriter cvSeqWriter);

    public static native void cvStartWriteStruct(CvFileStorage cvFileStorage, String str, int i, String str2, @ByVal CvAttrList cvAttrList);

    public static native int cvStdErrReport(int i, String str, String str2, String str3, int i2, Pointer pointer);

    public static native void cvSub(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4);

    public static native void cvSubRS(CvArr cvArr, @ByVal CvScalar cvScalar, CvArr cvArr2, CvArr cvArr3);

    @ByVal
    public static native CvScalar cvSum(CvArr cvArr);

    @ByVal
    public static native CvScalar cvTrace(CvArr cvArr);

    public static native void cvTransform(CvArr cvArr, CvArr cvArr2, CvMat cvMat, CvMat cvMat2);

    public static native void cvTranspose(CvArr cvArr, CvArr cvArr2);

    public static native CvSeq cvTreeToNodeSeq(Pointer pointer, int i, CvMemStorage cvMemStorage);

    public static native CvTypeInfo cvTypeOf(Pointer pointer);

    public static native void cvUnregisterType(String str);

    public static native int cvUseOptimized(int i);

    public static native void cvWrite(CvFileStorage cvFileStorage, String str, Pointer pointer, @ByVal CvAttrList cvAttrList);

    public static native void cvWriteComment(CvFileStorage cvFileStorage, String str, int i);

    public static native void cvWriteFileNode(CvFileStorage cvFileStorage, String str, CvFileNode cvFileNode, int i);

    public static native void cvWriteInt(CvFileStorage cvFileStorage, String str, int i);

    public static native void cvWriteRawData(CvFileStorage cvFileStorage, Pointer pointer, int i, String str);

    public static native void cvWriteReal(CvFileStorage cvFileStorage, String str, double d);

    public static native void cvWriteString(CvFileStorage cvFileStorage, String str, String str2, int i);

    public static native void cvXor(CvArr cvArr, CvArr cvArr2, CvArr cvArr3, CvArr cvArr4);

    public static native void cvXorS(CvArr cvArr, @ByVal CvScalar cvScalar, CvArr cvArr2, CvArr cvArr3);

    @ByRef
    @Namespace("cv")
    public static native String getBuildInformation();

    @Name({"cv::partition<void*>"})
    public static native int partition(@StdVector PointerPointer pointerPointer, @StdVector IntPointer intPointer, @ByRef Predicate predicate);

    static {
        $assertionsDisabled = !opencv_core.class.desiredAssertionStatus();
        if (Loader.load() != null) {
            String platformName = Loader.getPlatformName();
            if (platformName.equals("windows-x86")) {
                SetLibraryPath("C:/opencv/build/x86/vc10/bin/");
            } else if (platformName.equals("windows-x86_64")) {
                SetLibraryPath("C:/opencv/build/x64/vc10/bin/");
            }
        }
        IPL_IMAGE_MAGIC_VAL = Loader.load() == null ? 0 : Loader.sizeof(IplImage.class);
        CV_8UC1 = CV_MAKETYPE(0, 1);
        CV_8UC2 = CV_MAKETYPE(0, 2);
        CV_8UC3 = CV_MAKETYPE(0, 3);
        CV_8UC4 = CV_MAKETYPE(0, 4);
        CV_8SC1 = CV_MAKETYPE(1, 1);
        CV_8SC2 = CV_MAKETYPE(1, 2);
        CV_8SC3 = CV_MAKETYPE(1, 3);
        CV_8SC4 = CV_MAKETYPE(1, 4);
        CV_16UC1 = CV_MAKETYPE(2, 1);
        CV_16UC2 = CV_MAKETYPE(2, 2);
        CV_16UC3 = CV_MAKETYPE(2, 3);
        CV_16UC4 = CV_MAKETYPE(2, 4);
        CV_16SC1 = CV_MAKETYPE(3, 1);
        CV_16SC2 = CV_MAKETYPE(3, 2);
        CV_16SC3 = CV_MAKETYPE(3, 3);
        CV_16SC4 = CV_MAKETYPE(3, 4);
        CV_32SC1 = CV_MAKETYPE(4, 1);
        CV_32SC2 = CV_MAKETYPE(4, 2);
        CV_32SC3 = CV_MAKETYPE(4, 3);
        CV_32SC4 = CV_MAKETYPE(4, 4);
        CV_32FC1 = CV_MAKETYPE(5, 1);
        CV_32FC2 = CV_MAKETYPE(5, 2);
        CV_32FC3 = CV_MAKETYPE(5, 3);
        CV_32FC4 = CV_MAKETYPE(5, 4);
        CV_64FC1 = CV_MAKETYPE(6, 1);
        CV_64FC2 = CV_MAKETYPE(6, 2);
        CV_64FC3 = CV_MAKETYPE(6, 3);
        CV_64FC4 = CV_MAKETYPE(6, 4);
        CV_WHOLE_ARR = Loader.load() == null ? null : cvSlice(0, CV_WHOLE_SEQ_END_INDEX);
        CV_WHOLE_SEQ = Loader.load() == null ? null : cvSlice(0, CV_WHOLE_SEQ_END_INDEX);
        CV_SEQ_ELTYPE_POINT = CV_32SC2;
        CV_SEQ_ELTYPE_CODE = CV_8UC1;
        CV_SEQ_ELTYPE_INDEX = CV_32SC1;
        CV_SEQ_ELTYPE_POINT3D = CV_32FC3;
        CV_SEQ_POINT_SET = CV_SEQ_ELTYPE_POINT | 0;
        CV_SEQ_POINT3D_SET = CV_SEQ_ELTYPE_POINT3D | 0;
        CV_SEQ_POLYLINE = CV_SEQ_ELTYPE_POINT | 4096;
        CV_SEQ_POLYGON = CV_SEQ_POLYLINE | 16384;
        CV_SEQ_CONTOUR = CV_SEQ_POLYGON;
        CV_SEQ_SIMPLE_POLYGON = CV_SEQ_POLYGON | 0;
        CV_SEQ_CHAIN = CV_SEQ_ELTYPE_CODE | 4096;
        CV_SEQ_CHAIN_CONTOUR = CV_SEQ_CHAIN | 16384;
        CV_SEQ_INDEX = CV_SEQ_ELTYPE_INDEX | 0;
        CV_ATTR_LIST_EMPTY = new CvAttrList();
    }

    @Opaque
    public static class CvArr extends Pointer implements Cloneable {
        static {
            Loader.load();
        }

        protected CvArr() {
        }

        protected CvArr(Pointer p) {
            super(p);
        }
    }

    @Name({"CvArr*"})
    public static class CvArrArray extends Pointer {
        private native void allocateArray(int i);

        public native CvArr get();

        public native CvArrArray put(CvArr cvArr);

        static {
            Loader.load();
        }

        public CvArrArray(CvArr... array) {
            this(array.length);
            put(array);
            position(0);
        }

        public CvArrArray(int size) {
            allocateArray(size);
        }

        public CvArrArray(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvArrArray position(int position) {
            return (CvArrArray) super.position(position);
        }

        public CvArrArray put(CvArr... array) {
            for (int i = 0; i < array.length; i++) {
                position(i).put(array[i]);
            }
            return this;
        }
    }

    @Name({"CvMat*"})
    public static class CvMatArray extends CvArrArray {
        private native void allocateArray(int i);

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray
        @ValueGetter
        public native CvMat get();

        public CvMatArray(CvMat... array) {
            this(array.length);
            put((CvArr[]) array);
            position(0);
        }

        public CvMatArray(int size) {
            super(new CvArr[0]);
            allocateArray(size);
        }

        public CvMatArray(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray, com.googlecode.javacpp.Pointer
        public CvMatArray position(int position) {
            return (CvMatArray) super.position(position);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray
        public CvMatArray put(CvArr... array) {
            return (CvMatArray) super.put(array);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray
        public CvMatArray put(CvArr p) {
            if (p instanceof CvMat) {
                return (CvMatArray) super.put(p);
            }
            throw new ArrayStoreException(p.getClass().getName());
        }
    }

    @Name({"CvMatND*"})
    public static class CvMatNDArray extends CvArrArray {
        private native void allocateArray(int i);

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray
        @ValueGetter
        public native CvMatND get();

        public CvMatNDArray(CvMatND... array) {
            this(array.length);
            put((CvArr[]) array);
            position(0);
        }

        public CvMatNDArray(int size) {
            super(new CvArr[0]);
            allocateArray(size);
        }

        public CvMatNDArray(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray, com.googlecode.javacpp.Pointer
        public CvMatNDArray position(int position) {
            return (CvMatNDArray) super.position(position);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray
        public CvMatNDArray put(CvArr... array) {
            return (CvMatNDArray) super.put(array);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray
        public CvMatNDArray put(CvArr p) {
            if (p instanceof CvMatND) {
                return (CvMatNDArray) super.put(p);
            }
            throw new ArrayStoreException(p.getClass().getName());
        }
    }

    @Name({"IplImage*"})
    public static class IplImageArray extends CvArrArray {
        private native void allocateArray(int i);

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray
        @ValueGetter
        public native IplImage get();

        public IplImageArray(IplImage... array) {
            this(array.length);
            put((CvArr[]) array);
            position(0);
        }

        public IplImageArray(int size) {
            super(new CvArr[0]);
            allocateArray(size);
        }

        public IplImageArray(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray, com.googlecode.javacpp.Pointer
        public IplImageArray position(int position) {
            return (IplImageArray) super.position(position);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray
        public IplImageArray put(CvArr... array) {
            return (IplImageArray) super.put(array);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvArrArray
        public IplImageArray put(CvArr p) {
            if (p instanceof IplImage) {
                return (IplImageArray) super.put(p);
            }
            throw new ArrayStoreException(p.getClass().getName());
        }
    }

    public static class Cv32suf extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native float f();

        public native Cv32suf f(float f);

        public native int i();

        public native Cv32suf i(int i);

        public native int u();

        public native Cv32suf u(int i);

        static {
            Loader.load();
        }

        public Cv32suf() {
            allocate();
        }

        public Cv32suf(int size) {
            allocateArray(size);
        }

        public Cv32suf(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public Cv32suf position(int position) {
            return (Cv32suf) super.position(position);
        }
    }

    public static class Cv64suf extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native double f();

        public native Cv64suf f(double d);

        public native long i();

        public native Cv64suf i(long j);

        public native long u();

        public native Cv64suf u(long j);

        static {
            Loader.load();
        }

        public Cv64suf() {
            allocate();
        }

        public Cv64suf(int size) {
            allocateArray(size);
        }

        public Cv64suf(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public Cv64suf position(int position) {
            return (Cv64suf) super.position(position);
        }
    }

    public static class CvRNG extends LongPointer {
        private native void allocate();

        static {
            Loader.load();
        }

        public CvRNG() {
            this(null);
            allocate();
        }

        public CvRNG(Pointer p) {
            super(p);
        }
    }

    public static CvRNG cvRNG() {
        return cvRNG(-1L);
    }

    public static CvRNG cvRNG(long seed) {
        CvRNG cvRNG = new CvRNG();
        if (seed == 0) {
            seed = -1;
        }
        return (CvRNG) cvRNG.put(seed);
    }

    public static int cvRandInt(CvRNG rng) {
        long temp = rng.get();
        long temp2 = ((temp & 4294967295L) * CV_RNG_COEFF) + ((temp >> 32) & 4294967295L);
        rng.put(temp2);
        return (int) temp2;
    }

    public static double cvRandReal(CvRNG rng) {
        return (cvRandInt(rng) & 4294967295L) * 2.3283064365386963E-10d;
    }

    public static class IplImage extends CvArr {
        static final /* synthetic */ boolean $assertionsDisabled;
        public static final byte[] gamma22;
        public static final byte[] gamma22inv;

        private native void allocate();

        private native void allocateArray(int i);

        public native int BorderConst(int i);

        public native IplImage BorderConst(int i, int i2);

        public native int BorderMode(int i);

        public native IplImage BorderMode(int i, int i2);

        public native int ID();

        public native IplImage ID(int i);

        public native int align();

        public native IplImage align(int i);

        public native int alphaChannel();

        public native IplImage alphaChannel(int i);

        public native int channelSeq(int i);

        public native IplImage channelSeq(int i, int i2);

        public native int colorModel(int i);

        public native IplImage colorModel(int i, int i2);

        public native int dataOrder();

        public native IplImage dataOrder(int i);

        public native int depth();

        public native IplImage depth(int i);

        public native int height();

        public native IplImage height(int i);

        @Cast({"char*"})
        public native BytePointer imageData();

        public native IplImage imageData(BytePointer bytePointer);

        @MemberSetter
        public native IplImage imageData(@Cast({"char*"}) ByteBuffer byteBuffer);

        @Cast({"char*"})
        public native BytePointer imageDataOrigin();

        public native IplImage imageDataOrigin(BytePointer bytePointer);

        public native Pointer imageId();

        public native IplImage imageId(Pointer pointer);

        public native int imageSize();

        public native IplImage imageSize(int i);

        public native IplImage maskROI();

        public native IplImage maskROI(IplImage iplImage);

        public native int nChannels();

        public native IplImage nChannels(int i);

        public native int nSize();

        public native IplImage nSize(int i);

        public native int origin();

        public native IplImage origin(int i);

        public native IplImage roi(IplROI iplROI);

        public native IplROI roi();

        public native IplImage tileInfo(IplTileInfo iplTileInfo);

        public native IplTileInfo tileInfo();

        public native int width();

        public native IplImage width(int i);

        public native int widthStep();

        public native IplImage widthStep(int i);

        static {
            $assertionsDisabled = !opencv_core.class.desiredAssertionStatus();
            gamma22 = new byte[256];
            gamma22inv = new byte[256];
            for (int i = 0; i < 256; i++) {
                gamma22[i] = (byte) Math.round(Math.pow(i / 255.0d, 2.2d) * 255.0d);
                gamma22inv[i] = (byte) Math.round(Math.pow(i / 255.0d, 0.45454545454545453d) * 255.0d);
            }
        }

        public IplImage() {
            allocate();
            zero();
        }

        public IplImage(int size) {
            allocateArray(size);
            zero();
        }

        public IplImage(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public IplImage position(int position) {
            return (IplImage) super.position(position);
        }

        public static IplImage create(CvSize size, int depth, int channels) {
            IplImage i = opencv_core.cvCreateImage(size, depth, channels);
            if (i != null) {
                i.deallocator(new ReleaseDeallocator(i));
            }
            return i;
        }

        public static IplImage create(int width, int height, int depth, int channels) {
            return create(opencv_core.cvSize(width, height), depth, channels);
        }

        public static IplImage create(CvSize size, int depth, int channels, int origin) {
            IplImage i = create(size, depth, channels);
            if (i != null) {
                i.origin(origin);
            }
            return i;
        }

        public static IplImage create(int width, int height, int depth, int channels, int origin) {
            IplImage i = create(width, height, depth, channels);
            if (i != null) {
                i.origin(origin);
            }
            return i;
        }

        public static IplImage createHeader(CvSize size, int depth, int channels) {
            IplImage i = opencv_core.cvCreateImageHeader(size, depth, channels);
            if (i != null) {
                i.deallocator(new HeaderReleaseDeallocator(i));
            }
            return i;
        }

        public static IplImage createHeader(int width, int height, int depth, int channels) {
            return createHeader(opencv_core.cvSize(width, height), depth, channels);
        }

        public static IplImage createHeader(CvSize size, int depth, int channels, int origin) {
            IplImage i = createHeader(size, depth, channels);
            if (i != null) {
                i.origin(origin);
            }
            return i;
        }

        public static IplImage createHeader(int width, int height, int depth, int channels, int origin) {
            IplImage i = createHeader(width, height, depth, channels);
            if (i != null) {
                i.origin(origin);
            }
            return i;
        }

        public static IplImage createCompatible(IplImage template) {
            return createIfNotCompatible(null, template);
        }

        public static IplImage createIfNotCompatible(IplImage image, IplImage template) {
            if (image == null || image.width() != template.width() || image.height() != template.height() || image.depth() != template.depth() || image.nChannels() != template.nChannels()) {
                image = create(template.width(), template.height(), template.depth(), template.nChannels(), template.origin());
            }
            image.origin(template.origin());
            return image;
        }

        @Override // 
        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public IplImage mo13clone() {
            IplImage i = opencv_core.cvCloneImage(this);
            if (i != null) {
                i.deallocator(new ReleaseDeallocator(i));
            }
            return i;
        }

        public void release() {
            deallocate();
        }

        protected static class ReleaseDeallocator extends IplImage implements Pointer.Deallocator {
            @Override // com.googlecode.javacv.cpp.opencv_core.IplImage
            /* renamed from: clone */
            public /* bridge */ /* synthetic */ Object mo13clone() throws CloneNotSupportedException {
                return super.mo13clone();
            }

            @Override // com.googlecode.javacv.cpp.opencv_core.IplImage, com.googlecode.javacpp.Pointer
            public /* bridge */ /* synthetic */ Pointer position(int i) {
                return super.position(i);
            }

            ReleaseDeallocator(IplImage p) {
                super(p);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_core.cvReleaseImage(this);
            }
        }

        protected static class HeaderReleaseDeallocator extends IplImage implements Pointer.Deallocator {
            @Override // com.googlecode.javacv.cpp.opencv_core.IplImage
            /* renamed from: clone */
            public /* bridge */ /* synthetic */ Object mo13clone() throws CloneNotSupportedException {
                return super.mo13clone();
            }

            @Override // com.googlecode.javacv.cpp.opencv_core.IplImage, com.googlecode.javacpp.Pointer
            public /* bridge */ /* synthetic */ Pointer position(int i) {
                return super.position(i);
            }

            HeaderReleaseDeallocator(IplImage p) {
                super(p);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_core.cvReleaseImageHeader(this);
            }
        }

        public double highValue() {
            switch (depth()) {
                case opencv_core.IPL_DEPTH_8S /* -2147483640 */:
                    return 127.0d;
                case opencv_core.IPL_DEPTH_16S /* -2147483632 */:
                    return 32767.0d;
                case opencv_core.IPL_DEPTH_32S /* -2147483616 */:
                    return 2.147483647E9d;
                case 1:
                case 32:
                case 64:
                    return 1.0d;
                case 8:
                    return 255.0d;
                case 16:
                    return 65535.0d;
                default:
                    if ($assertionsDisabled) {
                        return 0.0d;
                    }
                    throw new AssertionError();
            }
        }

        public CvSize cvSize() {
            return opencv_core.cvSize(width(), height());
        }

        public ByteBuffer getByteBuffer(int index) {
            return imageData().position(index).capacity(imageSize()).asByteBuffer();
        }

        public ShortBuffer getShortBuffer(int index) {
            return getByteBuffer(index * 2).asShortBuffer();
        }

        public IntBuffer getIntBuffer(int index) {
            return getByteBuffer(index * 4).asIntBuffer();
        }

        public FloatBuffer getFloatBuffer(int index) {
            return getByteBuffer(index * 4).asFloatBuffer();
        }

        public DoubleBuffer getDoubleBuffer(int index) {
            return getByteBuffer(index * 8).asDoubleBuffer();
        }

        public ByteBuffer getByteBuffer() {
            return getByteBuffer(0);
        }

        public ShortBuffer getShortBuffer() {
            return getShortBuffer(0);
        }

        public IntBuffer getIntBuffer() {
            return getIntBuffer(0);
        }

        public FloatBuffer getFloatBuffer() {
            return getFloatBuffer(0);
        }

        public DoubleBuffer getDoubleBuffer() {
            return getDoubleBuffer(0);
        }

        public static int decodeGamma22(int value) {
            return gamma22[value & 255] & Constants.UNKNOWN;
        }

        public static int encodeGamma22(int value) {
            return gamma22inv[value & 255] & Constants.UNKNOWN;
        }

        public static void flipCopyWithGamma(ByteBuffer srcBuf, int srcStep, ByteBuffer dstBuf, int dstStep, boolean signed, double gamma, boolean flip, int channels) {
            byte out;
            byte out2;
            byte out3;
            byte out4;
            if (!$assertionsDisabled && srcBuf == dstBuf) {
                throw new AssertionError();
            }
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            byte[] buffer = new byte[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                if (signed) {
                    if (channels > 1) {
                        int x = 0;
                        while (x < w) {
                            for (int z = 0; z < channels; z++) {
                                int in = srcBuf.get();
                                if (gamma == 1.0d) {
                                    out4 = (byte) in;
                                } else {
                                    out4 = (byte) Math.round(Math.pow(in / 127.0d, gamma) * 127.0d);
                                }
                                buffer[z] = out4;
                            }
                            for (int z2 = channels - 1; z2 >= 0; z2--) {
                                dstBuf.put(buffer[z2]);
                            }
                            x += channels;
                        }
                    } else {
                        for (int x2 = 0; x2 < w; x2++) {
                            int in2 = srcBuf.get();
                            if (gamma == 1.0d) {
                                out3 = (byte) in2;
                            } else {
                                out3 = (byte) Math.round(Math.pow(in2 / 127.0d, gamma) * 127.0d);
                            }
                            dstBuf.put(out3);
                        }
                    }
                } else if (channels > 1) {
                    int x3 = 0;
                    while (x3 < w) {
                        for (int z3 = 0; z3 < channels; z3++) {
                            int in3 = srcBuf.get() & Constants.UNKNOWN;
                            if (gamma == 1.0d) {
                                out2 = (byte) in3;
                            } else if (gamma == 2.2d) {
                                out2 = gamma22[in3];
                            } else if (gamma == 0.45454545454545453d) {
                                out2 = gamma22inv[in3];
                            } else {
                                out2 = (byte) Math.round(Math.pow(in3 / 255.0d, gamma) * 255.0d);
                            }
                            buffer[z3] = out2;
                        }
                        for (int z4 = channels - 1; z4 >= 0; z4--) {
                            dstBuf.put(buffer[z4]);
                        }
                        x3 += channels;
                    }
                } else {
                    for (int x4 = 0; x4 < w; x4++) {
                        int in4 = srcBuf.get() & Constants.UNKNOWN;
                        if (gamma == 1.0d) {
                            out = (byte) in4;
                        } else if (gamma == 2.2d) {
                            out = gamma22[in4];
                        } else if (gamma == 0.45454545454545453d) {
                            out = gamma22inv[in4];
                        } else {
                            out = (byte) Math.round(Math.pow(in4 / 255.0d, gamma) * 255.0d);
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
        }

        public static void flipCopyWithGamma(ShortBuffer srcBuf, int srcStep, ShortBuffer dstBuf, int dstStep, boolean signed, double gamma, boolean flip, int channels) {
            short out;
            short out2;
            short out3;
            short out4;
            if (!$assertionsDisabled && srcBuf == dstBuf) {
                throw new AssertionError();
            }
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            short[] buffer = new short[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                if (signed) {
                    if (channels > 1) {
                        int x = 0;
                        while (x < w) {
                            for (int z = 0; z < channels; z++) {
                                int in = srcBuf.get();
                                if (gamma == 1.0d) {
                                    out4 = (short) in;
                                } else {
                                    out4 = (short) Math.round(Math.pow(in / 32767.0d, gamma) * 32767.0d);
                                }
                                buffer[z] = out4;
                            }
                            for (int z2 = channels - 1; z2 >= 0; z2--) {
                                dstBuf.put(buffer[z2]);
                            }
                            x += channels;
                        }
                    } else {
                        for (int x2 = 0; x2 < w; x2++) {
                            int in2 = srcBuf.get();
                            if (gamma == 1.0d) {
                                out3 = (short) in2;
                            } else {
                                out3 = (short) Math.round(Math.pow(in2 / 32767.0d, gamma) * 32767.0d);
                            }
                            dstBuf.put(out3);
                        }
                    }
                } else if (channels > 1) {
                    int x3 = 0;
                    while (x3 < w) {
                        for (int z3 = 0; z3 < channels; z3++) {
                            int in3 = srcBuf.get();
                            if (gamma == 1.0d) {
                                out2 = (short) in3;
                            } else {
                                out2 = (short) Math.round(Math.pow(in3 / 65535.0d, gamma) * 65535.0d);
                            }
                            buffer[z3] = out2;
                        }
                        for (int z4 = channels - 1; z4 >= 0; z4--) {
                            dstBuf.put(buffer[z4]);
                        }
                        x3 += channels;
                    }
                } else {
                    for (int x4 = 0; x4 < w; x4++) {
                        int in4 = srcBuf.get() & 65535;
                        if (gamma == 1.0d) {
                            out = (short) in4;
                        } else {
                            out = (short) Math.round(Math.pow(in4 / 65535.0d, gamma) * 65535.0d);
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
        }

        public static void flipCopyWithGamma(IntBuffer srcBuf, int srcStep, IntBuffer dstBuf, int dstStep, double gamma, boolean flip, int channels) {
            int out;
            int out2;
            if (!$assertionsDisabled && srcBuf == dstBuf) {
                throw new AssertionError();
            }
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            int[] buffer = new int[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                if (channels > 1) {
                    int x = 0;
                    while (x < w) {
                        for (int z = 0; z < channels; z++) {
                            int in = srcBuf.get();
                            if (gamma == 1.0d) {
                                out2 = in;
                            } else {
                                out2 = (int) Math.round(Math.pow(in / 2.147483647E9d, gamma) * 2.147483647E9d);
                            }
                            buffer[z] = out2;
                        }
                        for (int z2 = channels - 1; z2 >= 0; z2--) {
                            dstBuf.put(buffer[z2]);
                        }
                        x += channels;
                    }
                } else {
                    for (int x2 = 0; x2 < w; x2++) {
                        int in2 = srcBuf.get();
                        if (gamma == 1.0d) {
                            out = in2;
                        } else {
                            out = (int) Math.round(Math.pow(in2 / 2.147483647E9d, gamma) * 2.147483647E9d);
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
        }

        public static void flipCopyWithGamma(FloatBuffer srcBuf, int srcStep, FloatBuffer dstBuf, int dstStep, double gamma, boolean flip, int channels) {
            float out;
            float out2;
            if (!$assertionsDisabled && srcBuf == dstBuf) {
                throw new AssertionError();
            }
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            float[] buffer = new float[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                if (channels > 1) {
                    int x = 0;
                    while (x < w) {
                        for (int z = 0; z < channels; z++) {
                            float in = srcBuf.get();
                            if (gamma == 1.0d) {
                                out2 = in;
                            } else {
                                out2 = (float) Math.pow(in, gamma);
                            }
                            buffer[z] = out2;
                        }
                        for (int z2 = channels - 1; z2 >= 0; z2--) {
                            dstBuf.put(buffer[z2]);
                        }
                        x += channels;
                    }
                } else {
                    for (int x2 = 0; x2 < w; x2++) {
                        float in2 = srcBuf.get();
                        if (gamma == 1.0d) {
                            out = in2;
                        } else {
                            out = (float) Math.pow(in2, gamma);
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
        }

        public static void flipCopyWithGamma(DoubleBuffer srcBuf, int srcStep, DoubleBuffer dstBuf, int dstStep, double gamma, boolean flip, int channels) {
            double out;
            double out2;
            if (!$assertionsDisabled && srcBuf == dstBuf) {
                throw new AssertionError();
            }
            int w = Math.min(srcStep, dstStep);
            int srcLine = srcBuf.position();
            int dstLine = dstBuf.position();
            double[] buffer = new double[channels];
            while (srcLine < srcBuf.capacity() && dstLine < dstBuf.capacity()) {
                if (flip) {
                    srcBuf.position((srcBuf.capacity() - srcLine) - srcStep);
                } else {
                    srcBuf.position(srcLine);
                }
                dstBuf.position(dstLine);
                w = Math.min(Math.min(w, srcBuf.remaining()), dstBuf.remaining());
                if (channels > 1) {
                    int x = 0;
                    while (x < w) {
                        for (int z = 0; z < channels; z++) {
                            double in = srcBuf.get();
                            if (gamma == 1.0d) {
                                out2 = in;
                            } else {
                                out2 = Math.pow(in, gamma);
                            }
                            buffer[z] = out2;
                        }
                        for (int z2 = channels - 1; z2 >= 0; z2--) {
                            dstBuf.put(buffer[z2]);
                        }
                        x += channels;
                    }
                } else {
                    for (int x2 = 0; x2 < w; x2++) {
                        double in2 = srcBuf.get();
                        if (gamma == 1.0d) {
                            out = in2;
                        } else {
                            out = Math.pow(in2, gamma);
                        }
                        dstBuf.put(out);
                    }
                }
                srcLine += srcStep;
                dstLine += dstStep;
            }
        }

        public void applyGamma(double gamma) {
            if (gamma != 1.0d) {
                switch (depth()) {
                    case opencv_core.IPL_DEPTH_8S /* -2147483640 */:
                        flipCopyWithGamma(getByteBuffer(), widthStep(), getByteBuffer(), widthStep(), true, gamma, false, 0);
                        return;
                    case opencv_core.IPL_DEPTH_16S /* -2147483632 */:
                        flipCopyWithGamma(getShortBuffer(), widthStep() / 2, getShortBuffer(), widthStep() / 2, true, gamma, false, 0);
                        return;
                    case opencv_core.IPL_DEPTH_32S /* -2147483616 */:
                        flipCopyWithGamma(getFloatBuffer(), widthStep() / 4, getFloatBuffer(), widthStep() / 4, gamma, false, 0);
                        return;
                    case 8:
                        flipCopyWithGamma(getByteBuffer(), widthStep(), getByteBuffer(), widthStep(), false, gamma, false, 0);
                        return;
                    case 16:
                        flipCopyWithGamma(getShortBuffer(), widthStep() / 2, getShortBuffer(), widthStep() / 2, false, gamma, false, 0);
                        return;
                    case 32:
                        flipCopyWithGamma(getFloatBuffer(), widthStep() / 4, getFloatBuffer(), widthStep() / 4, gamma, false, 0);
                        return;
                    case 64:
                        flipCopyWithGamma(getDoubleBuffer(), widthStep() / 8, getDoubleBuffer(), widthStep() / 8, gamma, false, 0);
                        return;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        return;
                }
            }
        }

        public CvMat asCvMat() {
            CvMat mat = new CvMat();
            opencv_core.cvGetMat(this, mat, null, 0);
            return mat;
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            return "IplImage[width=" + width() + ",height=" + height() + ",depth=" + depth() + ",nChannels=" + nChannels() + "]";
        }
    }

    @Opaque
    public static class IplTileInfo extends Pointer {
        static {
            Loader.load();
        }

        public IplTileInfo() {
        }

        public IplTileInfo(Pointer p) {
            super(p);
        }
    }

    public static class IplROI extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int coi();

        public native IplROI coi(int i);

        public native int height();

        public native IplROI height(int i);

        public native int width();

        public native IplROI width(int i);

        public native int xOffset();

        public native IplROI xOffset(int i);

        public native int yOffset();

        public native IplROI yOffset(int i);

        static {
            Loader.load();
        }

        public IplROI() {
            allocate();
        }

        public IplROI(int size) {
            allocateArray(size);
        }

        public IplROI(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public IplROI position(int position) {
            return (IplROI) super.position(position);
        }
    }

    public static boolean CV_IS_IMAGE_HDR(CvArr img) {
        return img != null && new IplImage(img).nSize() == Loader.sizeof(IplImage.class);
    }

    public static boolean CV_IS_IMAGE(CvArr img) {
        return CV_IS_IMAGE_HDR(img) && new IplImage(img).imageData() != null;
    }

    public static int CV_MAT_DEPTH(int flags) {
        return flags & 7;
    }

    public static int CV_MAKETYPE(int depth, int cn) {
        return CV_MAT_DEPTH(depth) + ((cn - 1) << 3);
    }

    public static int CV_MAKE_TYPE(int depth, int cn) {
        return CV_MAKETYPE(depth, cn);
    }

    public static int CV_8UC(int n) {
        return CV_MAKETYPE(0, n);
    }

    public static int CV_8SC(int n) {
        return CV_MAKETYPE(1, n);
    }

    public static int CV_16UC(int n) {
        return CV_MAKETYPE(0, n);
    }

    public static int CV_16SC(int n) {
        return CV_MAKETYPE(3, n);
    }

    public static int CV_32SC(int n) {
        return CV_MAKETYPE(4, n);
    }

    public static int CV_32FC(int n) {
        return CV_MAKETYPE(5, n);
    }

    public static int CV_64FC(int n) {
        return CV_MAKETYPE(6, n);
    }

    public static int CV_MAT_CN(int flags) {
        return ((flags & CV_MAT_CN_MASK) >> 3) + 1;
    }

    public static int CV_MAT_TYPE(int flags) {
        return flags & 4095;
    }

    public static boolean CV_IS_MAT_CONT(int flags) {
        return (flags & 16384) != 0;
    }

    public static boolean CV_IS_CONT_MAT(int flags) {
        return CV_IS_MAT_CONT(flags);
    }

    public static boolean CV_IS_TEMP_MAT(int flags) {
        return (32768 & flags) != 0;
    }

    public static class CvMat extends CvArr {
        static final /* synthetic */ boolean $assertionsDisabled;
        private ByteBuffer byteBuffer;
        private DoubleBuffer doubleBuffer;
        private FloatBuffer floatBuffer;
        private int fullSize;
        private IntBuffer intBuffer;
        private ShortBuffer shortBuffer;

        private native void allocate();

        private native void allocateArray(int i);

        public native int cols();

        public native CvMat cols(int i);

        @Name({"data.db"})
        public native DoublePointer data_db();

        public native CvMat data_db(DoublePointer doublePointer);

        @Name({"data.fl"})
        public native FloatPointer data_fl();

        public native CvMat data_fl(FloatPointer floatPointer);

        @Name({"data.i"})
        public native IntPointer data_i();

        public native CvMat data_i(IntPointer intPointer);

        @Name({"data.ptr"})
        @Cast({"uchar*"})
        public native BytePointer data_ptr();

        public native CvMat data_ptr(BytePointer bytePointer);

        @Name({"data.s"})
        public native ShortPointer data_s();

        public native CvMat data_s(ShortPointer shortPointer);

        public native int hdr_refcount();

        public native CvMat hdr_refcount(int i);

        @Name({"type"})
        public native int raw_type();

        public native CvMat raw_type(int i);

        public native IntPointer refcount();

        public native CvMat refcount(IntPointer intPointer);

        public native int rows();

        public native CvMat rows(int i);

        public native int step();

        public native CvMat step(int i);

        static {
            $assertionsDisabled = !opencv_core.class.desiredAssertionStatus();
        }

        public CvMat() {
            this.fullSize = 0;
            this.byteBuffer = null;
            this.shortBuffer = null;
            this.intBuffer = null;
            this.floatBuffer = null;
            this.doubleBuffer = null;
            allocate();
            zero();
        }

        public CvMat(int size) {
            this.fullSize = 0;
            this.byteBuffer = null;
            this.shortBuffer = null;
            this.intBuffer = null;
            this.floatBuffer = null;
            this.doubleBuffer = null;
            allocateArray(size);
            zero();
        }

        public CvMat(Pointer p) {
            super(p);
            this.fullSize = 0;
            this.byteBuffer = null;
            this.shortBuffer = null;
            this.intBuffer = null;
            this.floatBuffer = null;
            this.doubleBuffer = null;
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvMat position(int position) {
            return (CvMat) super.position(position);
        }

        public static CvMat create(int rows, int cols, int type) {
            CvMat m = opencv_core.cvCreateMat(rows, cols, type);
            if (m != null) {
                m.fullSize = m.size();
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public static CvMat create(int rows, int cols, int depth, int channels) {
            return create(rows, cols, opencv_core.CV_MAKETYPE(depth, channels));
        }

        public static CvMat create(int rows, int cols) {
            return create(rows, cols, 6, 1);
        }

        public static CvMat createHeader(int rows, int cols, int type) {
            CvMat m = opencv_core.cvCreateMatHeader(rows, cols, type);
            if (m != null) {
                m.fullSize = m.size();
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public static CvMat createHeader(int rows, int cols, int depth, int channels) {
            return createHeader(rows, cols, opencv_core.CV_MAKETYPE(depth, channels));
        }

        public static CvMat createHeader(int rows, int cols) {
            return createHeader(rows, cols, 6, 1);
        }

        public static ThreadLocal<CvMat> createThreadLocal(final int rows, final int cols, final int type) {
            return new ThreadLocal<CvMat>() { // from class: com.googlecode.javacv.cpp.opencv_core.CvMat.1
                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.lang.ThreadLocal
                public CvMat initialValue() {
                    return CvMat.create(rows, cols, type);
                }
            };
        }

        public static ThreadLocal<CvMat> createThreadLocal(int rows, int cols, int depth, int channels) {
            return createThreadLocal(rows, cols, opencv_core.CV_MAKETYPE(depth, channels));
        }

        public static ThreadLocal<CvMat> createThreadLocal(int rows, int cols) {
            return createThreadLocal(rows, cols, 6, 1);
        }

        public static ThreadLocal<CvMat> createHeaderThreadLocal(final int rows, final int cols, final int type) {
            return new ThreadLocal<CvMat>() { // from class: com.googlecode.javacv.cpp.opencv_core.CvMat.2
                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.lang.ThreadLocal
                public CvMat initialValue() {
                    return CvMat.createHeader(rows, cols, type);
                }
            };
        }

        public static ThreadLocal<CvMat> createHeaderThreadLocal(int rows, int cols, int depth, int channels) {
            return createHeaderThreadLocal(rows, cols, opencv_core.CV_MAKETYPE(depth, channels));
        }

        public static ThreadLocal<CvMat> createHeaderThreadLocal(int rows, int cols) {
            return createHeaderThreadLocal(rows, cols, 6, 1);
        }

        @Override // 
        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public CvMat mo9clone() {
            CvMat m = opencv_core.cvCloneMat(this);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public void release() {
            deallocate();
        }

        protected static class ReleaseDeallocator extends CvMat implements Pointer.Deallocator {
            @Override // com.googlecode.javacv.cpp.opencv_core.CvMat
            /* renamed from: clone */
            public /* bridge */ /* synthetic */ Object mo9clone() throws CloneNotSupportedException {
                return super.mo9clone();
            }

            @Override // com.googlecode.javacv.cpp.opencv_core.CvMat, com.googlecode.javacpp.Pointer
            public /* bridge */ /* synthetic */ Pointer position(int i) {
                return super.position(i);
            }

            ReleaseDeallocator(CvMat m) {
                super(m);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_core.cvReleaseMat(this);
            }
        }

        public int type() {
            return opencv_core.CV_MAT_TYPE(raw_type());
        }

        public void type(int depth, int cn) {
            raw_type(opencv_core.CV_MAKETYPE(depth, cn) | opencv_core.CV_MAT_MAGIC_VAL);
        }

        public int depth() {
            return opencv_core.CV_MAT_DEPTH(type());
        }

        public int channels() {
            return opencv_core.CV_MAT_CN(type());
        }

        public int nChannels() {
            return opencv_core.CV_MAT_CN(type());
        }

        public boolean isContinuous() {
            return opencv_core.CV_IS_MAT_CONT(type());
        }

        public int elemSize() {
            switch (depth()) {
                case 0:
                case 1:
                    return 1;
                case 2:
                case 3:
                    return 2;
                case 4:
                case 5:
                    return 4;
                case 6:
                    return 8;
                default:
                    if ($assertionsDisabled) {
                        return 0;
                    }
                    throw new AssertionError();
            }
        }

        public int length() {
            return rows() * cols();
        }

        public int total() {
            return rows() * cols();
        }

        public boolean empty() {
            return length() == 0;
        }

        public int size() {
            int rows = rows();
            return (rows > 1 ? step() * (rows - 1) : 0) + (channels() * cols() * elemSize());
        }

        public CvSize cvSize() {
            return opencv_core.cvSize(cols(), rows());
        }

        public void reset() {
            this.fullSize = 0;
            this.byteBuffer = null;
            this.shortBuffer = null;
            this.intBuffer = null;
            this.floatBuffer = null;
            this.doubleBuffer = null;
        }

        private int fullSize() {
            if (this.fullSize > 0) {
                return this.fullSize;
            }
            int size = size();
            this.fullSize = size;
            return size;
        }

        public ByteBuffer getByteBuffer() {
            if (this.byteBuffer == null) {
                this.byteBuffer = data_ptr().capacity(fullSize()).asBuffer();
            }
            this.byteBuffer.position(0);
            return this.byteBuffer;
        }

        public ShortBuffer getShortBuffer() {
            if (this.shortBuffer == null) {
                this.shortBuffer = data_s().capacity(fullSize() / 2).asBuffer();
            }
            this.shortBuffer.position(0);
            return this.shortBuffer;
        }

        public IntBuffer getIntBuffer() {
            if (this.intBuffer == null) {
                this.intBuffer = data_i().capacity(fullSize() / 4).asBuffer();
            }
            this.intBuffer.position(0);
            return this.intBuffer;
        }

        public FloatBuffer getFloatBuffer() {
            if (this.floatBuffer == null) {
                this.floatBuffer = data_fl().capacity(fullSize() / 4).asBuffer();
            }
            this.floatBuffer.position(0);
            return this.floatBuffer;
        }

        public DoubleBuffer getDoubleBuffer() {
            if (this.doubleBuffer == null) {
                this.doubleBuffer = data_db().capacity(fullSize() / 8).asBuffer();
            }
            this.doubleBuffer.position(0);
            return this.doubleBuffer;
        }

        public double get(int i) {
            switch (depth()) {
                case 0:
                    return getByteBuffer().get(i) & Constants.UNKNOWN;
                case 1:
                    return getByteBuffer().get(i);
                case 2:
                    return getShortBuffer().get(i) & 65535;
                case 3:
                    return getShortBuffer().get(i);
                case 4:
                    return getIntBuffer().get(i);
                case 5:
                    return getFloatBuffer().get(i);
                case 6:
                    return getDoubleBuffer().get(i);
                default:
                    if ($assertionsDisabled) {
                        return Double.NaN;
                    }
                    throw new AssertionError();
            }
        }

        public double get(int i, int j) {
            return get(((step() * i) / elemSize()) + (channels() * j));
        }

        public double get(int i, int j, int k) {
            return get(((step() * i) / elemSize()) + (channels() * j) + k);
        }

        public synchronized CvMat get(int index, double[] vv, int offset, int length) {
            int d = depth();
            switch (d) {
                case 0:
                case 1:
                    ByteBuffer bb = getByteBuffer();
                    bb.position(index);
                    for (int i = 0; i < length; i++) {
                        if (d == 0) {
                            vv[i + offset] = bb.get(i) & Constants.UNKNOWN;
                        } else {
                            vv[i + offset] = bb.get(i);
                        }
                    }
                    break;
                case 2:
                case 3:
                    ShortBuffer sb = getShortBuffer();
                    sb.position(index);
                    for (int i2 = 0; i2 < length; i2++) {
                        if (d == 2) {
                            vv[i2 + offset] = sb.get() & 65535;
                        } else {
                            vv[i2 + offset] = sb.get();
                        }
                    }
                    break;
                case 4:
                    IntBuffer ib = getIntBuffer();
                    ib.position(index);
                    for (int i3 = 0; i3 < length; i3++) {
                        vv[i3 + offset] = ib.get();
                    }
                    break;
                case 5:
                    FloatBuffer fb = getFloatBuffer();
                    fb.position(index);
                    for (int i4 = 0; i4 < length; i4++) {
                        vv[i4 + offset] = fb.get();
                    }
                    break;
                case 6:
                    getDoubleBuffer().position(index);
                    getDoubleBuffer().get(vv, offset, length);
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            return this;
        }

        public CvMat get(int index, double[] vv) {
            return get(index, vv, 0, vv.length);
        }

        public CvMat get(double[] vv) {
            return get(0, vv);
        }

        public double[] get() {
            double[] vv = new double[fullSize() / elemSize()];
            get(vv);
            return vv;
        }

        public CvMat put(int i, double v) {
            switch (depth()) {
                case 0:
                case 1:
                    getByteBuffer().put(i, (byte) v);
                    return this;
                case 2:
                case 3:
                    getShortBuffer().put(i, (short) v);
                    return this;
                case 4:
                    getIntBuffer().put(i, (int) v);
                    return this;
                case 5:
                    getFloatBuffer().put(i, (float) v);
                    return this;
                case 6:
                    getDoubleBuffer().put(i, v);
                    return this;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    return this;
            }
        }

        public CvMat put(int i, int j, double v) {
            return put(((step() * i) / elemSize()) + (channels() * j), v);
        }

        public CvMat put(int i, int j, int k, double v) {
            return put(((step() * i) / elemSize()) + (channels() * j) + k, v);
        }

        public synchronized CvMat put(int index, double[] vv, int offset, int length) {
            switch (depth()) {
                case 0:
                case 1:
                    ByteBuffer bb = getByteBuffer();
                    bb.position(index);
                    for (int i = 0; i < length; i++) {
                        bb.put((byte) vv[i + offset]);
                    }
                    break;
                case 2:
                case 3:
                    ShortBuffer sb = getShortBuffer();
                    sb.position(index);
                    for (int i2 = 0; i2 < length; i2++) {
                        sb.put((short) vv[i2 + offset]);
                    }
                    break;
                case 4:
                    IntBuffer ib = getIntBuffer();
                    ib.position(index);
                    for (int i3 = 0; i3 < length; i3++) {
                        ib.put((int) vv[i3 + offset]);
                    }
                    break;
                case 5:
                    FloatBuffer fb = getFloatBuffer();
                    fb.position(index);
                    for (int i4 = 0; i4 < length; i4++) {
                        fb.put((float) vv[i4 + offset]);
                    }
                    break;
                case 6:
                    DoubleBuffer db = getDoubleBuffer();
                    db.position(index);
                    db.put(vv, offset, length);
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            return this;
        }

        public CvMat put(int index, double... vv) {
            return put(index, vv, 0, vv.length);
        }

        public CvMat put(double... vv) {
            return put(0, vv);
        }

        public CvMat put(CvMat mat) {
            return put(0, 0, 0, mat, 0, 0, 0);
        }

        public synchronized CvMat put(int dsti, int dstj, int dstk, CvMat mat, int srci, int srcj, int srck) {
            if (rows() == mat.rows() && cols() == mat.cols() && step() == mat.step() && type() == mat.type() && dsti == 0 && dstj == 0 && dstk == 0 && srci == 0 && srcj == 0 && srck == 0) {
                getByteBuffer().clear();
                mat.getByteBuffer().clear();
                getByteBuffer().put(mat.getByteBuffer());
            } else {
                int w = Math.min(rows() - dsti, mat.rows() - srci);
                int h = Math.min(cols() - dstj, mat.cols() - srcj);
                int d = Math.min(channels() - dstk, mat.channels() - srck);
                for (int i = 0; i < w; i++) {
                    for (int j = 0; j < h; j++) {
                        for (int k = 0; k < d; k++) {
                            put(i + dsti, j + dstj, k + dstk, mat.get(i + srci, j + srcj, k + srck));
                        }
                    }
                }
            }
            return this;
        }

        public IplImage asIplImage() {
            IplImage image = new IplImage();
            opencv_core.cvGetImage(this, image);
            return image;
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            return toString(0);
        }

        public String toString(int indent) {
            StringBuilder s = new StringBuilder("[ ");
            int channels = channels();
            for (int i = 0; i < rows(); i++) {
                for (int j = 0; j < cols(); j++) {
                    CvScalar v = opencv_core.cvGet2D(this, i, j);
                    if (channels > 1) {
                        s.append("(");
                    }
                    for (int k = 0; k < channels; k++) {
                        s.append((float) v.val(k));
                        if (k < channels - 1) {
                            s.append(", ");
                        }
                    }
                    if (channels > 1) {
                        s.append(")");
                    }
                    if (j < cols() - 1) {
                        s.append(", ");
                    }
                }
                if (i < rows() - 1) {
                    s.append("\n  ");
                    for (int j2 = 0; j2 < indent; j2++) {
                        s.append(' ');
                    }
                }
            }
            s.append(" ]");
            return s.toString();
        }
    }

    public static boolean CV_IS_MAT_HDR(CvArr mat) {
        CvMat m = new CvMat(mat);
        return mat != null && (m.raw_type() & (-65536)) == 1111621632 && m.cols() > 0 && m.rows() > 0;
    }

    public static boolean CV_IS_MAT_HDR_Z(CvArr mat) {
        CvMat m = new CvMat(mat);
        return mat != null && (m.raw_type() & (-65536)) == 1111621632 && m.cols() >= 0 && m.rows() >= 0;
    }

    public static boolean CV_IS_MAT(CvArr mat) {
        return CV_IS_MAT_HDR(mat) && new CvMat(mat).data_ptr() != null;
    }

    public static boolean CV_IS_MASK_ARR(CvMat mat) {
        return (mat.raw_type() & ((CV_8SC1 ^ (-1)) & 4095)) == 0;
    }

    public static boolean CV_ARE_TYPES_EQ(CvMat mat1, CvMat mat2) {
        return ((mat1.raw_type() ^ mat2.raw_type()) & 4095) == 0;
    }

    public static boolean CV_ARE_CNS_EQ(CvMat mat1, CvMat mat2) {
        return ((mat1.raw_type() ^ mat2.raw_type()) & CV_MAT_CN_MASK) == 0;
    }

    public static boolean CV_ARE_DEPTHS_EQ(CvMat mat1, CvMat mat2) {
        return ((mat1.raw_type() ^ mat2.raw_type()) & 7) == 0;
    }

    public static boolean CV_ARE_SIZES_EQ(CvMat mat1, CvMat mat2) {
        return mat1.rows() == mat2.rows() && mat1.cols() == mat2.cols();
    }

    public static boolean CV_IS_MAT_CONST(CvMat mat) {
        return (mat.rows() | mat.cols()) == 1;
    }

    public static int CV_ELEM_SIZE1(int type) {
        return (((Loader.sizeof(SizeTPointer.class) << 28) | 138682897) >> (CV_MAT_DEPTH(type) * 4)) & 15;
    }

    public static int CV_ELEM_SIZE(int type) {
        return CV_MAT_CN(type) << ((((((Loader.sizeof(SizeTPointer.class) / 4) + 1) * 16384) | 14928) >> (CV_MAT_DEPTH(type) * 2)) & 3);
    }

    public static int IPL2CV_DEPTH(int depth) {
        return (1125516576 >> (((Integer.MIN_VALUE & depth) != 0 ? 20 : 0) + ((depth & 240) >> 2))) & 15;
    }

    public static CvMat cvMat(int rows, int cols, int type, Pointer data) {
        CvMat m = new CvMat();
        if (!$assertionsDisabled && (CV_MAT_DEPTH(type) < 0 || CV_MAT_DEPTH(type) > 6)) {
            throw new AssertionError();
        }
        int type2 = CV_MAT_TYPE(type);
        m.raw_type(1111638016 | type2);
        m.cols(cols);
        m.rows(rows);
        m.step(CV_ELEM_SIZE(type2) * cols);
        m.data_ptr(new BytePointer(data));
        m.refcount(null);
        m.hdr_refcount(0);
        return m;
    }

    public static int cvIplDepth(int type) {
        int depth = CV_MAT_DEPTH(type);
        return ((depth == 1 || depth == 3 || depth == 4) ? Integer.MIN_VALUE : 0) | (CV_ELEM_SIZE1(depth) * 8);
    }

    public static class CvMatND extends CvArr {
        private native void allocate();

        private native void allocateArray(int i);

        @Name({"data.db"})
        public native DoublePointer data_db();

        public native CvMatND data_db(DoublePointer doublePointer);

        @Name({"data.fl"})
        public native FloatPointer data_fl();

        public native CvMatND data_fl(FloatPointer floatPointer);

        @Name({"data.i"})
        public native IntPointer data_i();

        public native CvMatND data_i(IntPointer intPointer);

        @Name({"data.ptr"})
        @Cast({"uchar*"})
        public native BytePointer data_ptr();

        public native CvMatND data_ptr(BytePointer bytePointer);

        @Name({"data.s"})
        public native ShortPointer data_s();

        public native CvMatND data_s(ShortPointer shortPointer);

        @Name({"dim", ".size"})
        public native int dim_size(int i);

        public native CvMatND dim_size(int i, int i2);

        @Name({"dim", ".step"})
        public native int dim_step(int i);

        public native CvMatND dim_step(int i, int i2);

        public native int dims();

        public native CvMatND dims(int i);

        public native int hdr_refcount();

        public native CvMatND hdr_refcount(int i);

        public native IntPointer refcount();

        public native CvMatND refcount(IntPointer intPointer);

        public native int type();

        public native CvMatND type(int i);

        public CvMatND() {
            allocate();
            zero();
        }

        public CvMatND(int size) {
            allocateArray(size);
            zero();
        }

        public CvMatND(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvMatND position(int position) {
            return (CvMatND) super.position(position);
        }

        public static CvMatND create(int dims, int[] sizes, int type) {
            CvMatND m = opencv_core.cvCreateMatND(dims, sizes, type);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        @Override // 
        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public CvMatND mo10clone() {
            CvMatND m = opencv_core.cvCloneMatND(this);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public void release() {
            deallocate();
        }

        protected static class ReleaseDeallocator extends CvMatND implements Pointer.Deallocator {
            @Override // com.googlecode.javacv.cpp.opencv_core.CvMatND
            /* renamed from: clone */
            public /* bridge */ /* synthetic */ Object mo10clone() throws CloneNotSupportedException {
                return super.mo10clone();
            }

            @Override // com.googlecode.javacv.cpp.opencv_core.CvMatND, com.googlecode.javacpp.Pointer
            public /* bridge */ /* synthetic */ Pointer position(int i) {
                return super.position(i);
            }

            ReleaseDeallocator(CvMatND p) {
                super(p);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_core.cvReleaseMatND(this);
            }
        }
    }

    public static boolean CV_IS_MATND_HDR(CvArr mat) {
        return mat != null && (new CvMatND(mat).type() & (-65536)) == 1111687168;
    }

    public static boolean CV_IS_MATND(CvArr mat) {
        return CV_IS_MATND_HDR(mat) && new CvMatND(mat).data_ptr() != null;
    }

    public static class CvSparseMat extends CvArr {
        private native void allocate();

        private native void allocateArray(int i);

        public native int dims();

        public native CvSparseMat dims(int i);

        public native int hashsize();

        public native CvSparseMat hashsize(int i);

        public native PointerPointer hashtable();

        public native CvSparseMat hashtable(PointerPointer pointerPointer);

        public native int hdr_refcount();

        public native CvSparseMat hdr_refcount(int i);

        public native CvSet heap();

        public native CvSparseMat heap(CvSet cvSet);

        public native int idxoffset();

        public native CvSparseMat idxoffset(int i);

        public native IntPointer refcount();

        public native CvSparseMat refcount(IntPointer intPointer);

        public native int size(int i);

        public native CvSparseMat size(int i, int i2);

        public native int type();

        public native CvSparseMat type(int i);

        public native int valoffset();

        public native CvSparseMat valoffset(int i);

        public CvSparseMat() {
            allocate();
            zero();
        }

        public CvSparseMat(int size) {
            allocateArray(size);
            zero();
        }

        public CvSparseMat(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSparseMat position(int position) {
            return (CvSparseMat) super.position(position);
        }

        public static CvSparseMat create(int dims, int[] sizes, int type) {
            CvSparseMat m = opencv_core.cvCreateSparseMat(dims, sizes, type);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        @Override // 
        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public CvSparseMat mo11clone() {
            CvSparseMat m = opencv_core.cvCloneSparseMat(this);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public void release() {
            deallocate();
        }

        protected static class ReleaseDeallocator extends CvSparseMat implements Pointer.Deallocator {
            @Override // com.googlecode.javacv.cpp.opencv_core.CvSparseMat
            /* renamed from: clone */
            public /* bridge */ /* synthetic */ Object mo11clone() throws CloneNotSupportedException {
                return super.mo11clone();
            }

            @Override // com.googlecode.javacv.cpp.opencv_core.CvSparseMat, com.googlecode.javacpp.Pointer
            public /* bridge */ /* synthetic */ Pointer position(int i) {
                return super.position(i);
            }

            ReleaseDeallocator(CvSparseMat p) {
                super(p);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_core.cvReleaseSparseMat(this);
            }
        }
    }

    public static boolean CV_IS_SPARSE_MAT_HDR(CvArr mat) {
        return mat != null && (new CvSparseMat(mat).type() & (-65536)) == 1111752704;
    }

    public static boolean CV_IS_SPARSE_MAT(CvArr mat) {
        return CV_IS_SPARSE_MAT_HDR(mat);
    }

    public static class CvSparseNode extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int hashval();

        public native CvSparseNode hashval(int i);

        public native CvSparseNode next();

        public native CvSparseNode next(CvSparseNode cvSparseNode);

        static {
            Loader.load();
        }

        public CvSparseNode() {
            allocate();
        }

        public CvSparseNode(int size) {
            allocateArray(size);
        }

        public CvSparseNode(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSparseNode position(int position) {
            return (CvSparseNode) super.position(position);
        }
    }

    public static class CvSparseMatIterator extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int curidx();

        public native CvSparseMatIterator curidx(int i);

        public native CvSparseMat mat();

        public native CvSparseMatIterator mat(CvSparseMat cvSparseMat);

        public native CvSparseMatIterator node(CvSparseNode cvSparseNode);

        public native CvSparseNode node();

        static {
            Loader.load();
        }

        public CvSparseMatIterator() {
            allocate();
        }

        public CvSparseMatIterator(int size) {
            allocateArray(size);
        }

        public CvSparseMatIterator(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSparseMatIterator position(int position) {
            return (CvSparseMatIterator) super.position(position);
        }
    }

    public static Pointer CV_NODE_VAL(CvSparseMat mat, CvSparseNode node) {
        return new BytePointer(node).position(mat.valoffset());
    }

    public static IntPointer CV_NODE_IDX(CvSparseMat mat, CvSparseNode node) {
        return new IntPointer(new BytePointer(node).position(mat.idxoffset()));
    }

    public static class CvRect extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int height();

        public native CvRect height(int i);

        public native int width();

        public native CvRect width(int i);

        public native int x();

        public native CvRect x(int i);

        public native int y();

        public native CvRect y(int i);

        static {
            Loader.load();
        }

        public CvRect() {
            allocate();
        }

        public CvRect(int size) {
            allocateArray(size);
        }

        public CvRect(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvRect position(int position) {
            return (CvRect) super.position(position);
        }

        public CvRect(int x, int y, int width, int height) {
            allocate();
            x(x).y(y).width(width).height(height);
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + x() + ", " + y() + "; " + width() + ", " + height() + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + x() + ", " + y() + "; " + width() + ", " + height() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvRect cvRect(int x, int y, int width, int height) {
        return new CvRect().x(x).y(y).width(width).height(height);
    }

    public static IplROI cvRectToROI(CvRect rect, int coi) {
        IplROI roi = new IplROI();
        roi.xOffset(rect.x());
        roi.yOffset(rect.y());
        roi.width(rect.width());
        roi.height(rect.height());
        roi.coi(coi);
        return roi;
    }

    public static CvRect cvROIToRect(IplROI roi) {
        return cvRect(roi.xOffset(), roi.yOffset(), roi.width(), roi.height());
    }

    public static class CvTermCriteria extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native double epsilon();

        public native CvTermCriteria epsilon(double d);

        public native int max_iter();

        public native CvTermCriteria max_iter(int i);

        public native int type();

        public native CvTermCriteria type(int i);

        static {
            Loader.load();
        }

        public CvTermCriteria() {
            allocate();
        }

        public CvTermCriteria(int size) {
            allocateArray(size);
        }

        public CvTermCriteria(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvTermCriteria position(int position) {
            return (CvTermCriteria) super.position(position);
        }

        public CvTermCriteria(int type, int max_iter, double epsilon) {
            allocate();
            type(type).max_iter(max_iter).epsilon(epsilon);
        }
    }

    public static CvTermCriteria cvTermCriteria(int type, int max_iter, double epsilon) {
        return new CvTermCriteria().type(type).max_iter(max_iter).epsilon(epsilon);
    }

    public static class CvPoint extends Pointer {
        public static final CvPoint ZERO;

        private native void allocate();

        private native void allocateArray(int i);

        public native int x();

        public native CvPoint x(int i);

        public native int y();

        public native CvPoint y(int i);

        static {
            Loader.load();
            ZERO = new CvPoint().x(0).y(0);
        }

        public CvPoint() {
            allocate();
        }

        public CvPoint(int size) {
            allocateArray(size);
        }

        public CvPoint(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvPoint position(int position) {
            return (CvPoint) super.position(position);
        }

        public CvPoint(int[] pts, int offset, int length) {
            this(length / 2);
            put(pts, offset, length);
        }

        public CvPoint(int... pts) {
            this(pts, 0, pts.length);
        }

        public CvPoint(byte shift, double[] pts, int offset, int length) {
            this(length / 2);
            put(shift, pts, offset, length);
        }

        public CvPoint(byte shift, double... pts) {
            this(shift, pts, 0, pts.length);
        }

        public int[] get() {
            int[] pts = new int[this.capacity == 0 ? 2 : this.capacity * 2];
            get(pts);
            return pts;
        }

        public CvPoint get(int[] pts) {
            return get(pts, 0, pts.length);
        }

        public CvPoint get(int[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position(i);
                pts[(i * 2) + offset] = x();
                pts[(i * 2) + offset + 1] = y();
            }
            return position(0);
        }

        public final CvPoint put(int[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position(i).put(pts[(i * 2) + offset], pts[(i * 2) + offset + 1]);
            }
            return position(0);
        }

        public final CvPoint put(int... pts) {
            return put(pts, 0, pts.length);
        }

        public final CvPoint put(byte shift, double[] pts, int offset, int length) {
            int[] a = new int[length];
            for (int i = 0; i < length; i++) {
                a[i] = (int) Math.round(pts[offset + i] * (1 << shift));
            }
            return put(a, 0, length);
        }

        public final CvPoint put(byte shift, double... pts) {
            return put(shift, pts, 0, pts.length);
        }

        public CvPoint put(int x, int y) {
            return x(x).y(y);
        }

        public CvPoint put(CvPoint o) {
            return x(o.x()).y(o.y());
        }

        public CvPoint put(byte shift, CvPoint2D32f o) {
            x(Math.round(o.x() * (1 << shift)));
            y(Math.round(o.y() * (1 << shift)));
            return this;
        }

        public CvPoint put(byte shift, CvPoint2D64f o) {
            x((int) Math.round(o.x() * (1 << shift)));
            y((int) Math.round(o.y() * (1 << shift)));
            return this;
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + x() + ", " + y() + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + x() + ", " + y() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvPoint cvPoint(int x, int y) {
        return new CvPoint().x(x).y(y);
    }

    public static class CvPoint2D32f extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native float x();

        public native CvPoint2D32f x(float f);

        public native float y();

        public native CvPoint2D32f y(float f);

        static {
            Loader.load();
        }

        public CvPoint2D32f() {
            allocate();
        }

        public CvPoint2D32f(int size) {
            allocateArray(size);
        }

        public CvPoint2D32f(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvPoint2D32f position(int position) {
            return (CvPoint2D32f) super.position(position);
        }

        public CvPoint2D32f(double[] pts, int offset, int length) {
            this(length / 2);
            put(pts, offset, length);
        }

        public CvPoint2D32f(double... pts) {
            this(pts, 0, pts.length);
        }

        public double[] get() {
            double[] pts = new double[this.capacity == 0 ? 2 : this.capacity * 2];
            get(pts);
            return pts;
        }

        public CvPoint2D32f get(double[] pts) {
            return get(pts, 0, pts.length);
        }

        public CvPoint2D32f get(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position(i);
                pts[(i * 2) + offset] = x();
                pts[(i * 2) + offset + 1] = y();
            }
            return position(0);
        }

        public final CvPoint2D32f put(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position(i).put(pts[(i * 2) + offset], pts[(i * 2) + offset + 1]);
            }
            return position(0);
        }

        public final CvPoint2D32f put(double... pts) {
            return put(pts, 0, pts.length);
        }

        public CvPoint2D32f put(double x, double y) {
            return x((float) x).y((float) y);
        }

        public CvPoint2D32f put(CvPoint o) {
            return x(o.x()).y(o.y());
        }

        public CvPoint2D32f put(CvPoint2D32f o) {
            return x(o.x()).y(o.y());
        }

        public CvPoint2D32f put(CvPoint2D64f o) {
            return x((float) o.x()).y((float) o.y());
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + x() + ", " + y() + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + x() + ", " + y() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvPoint2D32f cvPoint2D32f(double x, double y) {
        return new CvPoint2D32f().x((float) x).y((float) y);
    }

    public static CvPoint2D32f cvPointTo32f(CvPoint point) {
        return cvPoint2D32f(point.x(), point.y());
    }

    public static CvPoint cvPointFrom32f(CvPoint2D32f point) {
        CvPoint ipt = new CvPoint();
        ipt.x(Math.round(point.x()));
        ipt.y(Math.round(point.y()));
        return ipt;
    }

    public static class CvPoint3D32f extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native float x();

        public native CvPoint3D32f x(float f);

        public native float y();

        public native CvPoint3D32f y(float f);

        public native float z();

        public native CvPoint3D32f z(float f);

        static {
            Loader.load();
        }

        public CvPoint3D32f() {
            allocate();
        }

        public CvPoint3D32f(int size) {
            allocateArray(size);
        }

        public CvPoint3D32f(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvPoint3D32f position(int position) {
            return (CvPoint3D32f) super.position(position);
        }

        public CvPoint3D32f(double[] pts, int offset, int length) {
            this(length / 3);
            put(pts, offset, length);
        }

        public CvPoint3D32f(double... pts) {
            this(pts, 0, pts.length);
        }

        public double[] get() {
            double[] pts = new double[this.capacity == 0 ? 3 : this.capacity * 3];
            get(pts);
            return pts;
        }

        public CvPoint3D32f get(double[] pts) {
            return get(pts, 0, pts.length);
        }

        public CvPoint3D32f get(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 3; i++) {
                position(i);
                pts[(i * 3) + offset] = x();
                pts[(i * 3) + offset + 1] = y();
                pts[(i * 3) + offset + 2] = z();
            }
            return position(0);
        }

        public final CvPoint3D32f put(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 3; i++) {
                position(i).put(pts[(i * 3) + offset], pts[(i * 3) + offset + 1], pts[(i * 3) + offset + 2]);
            }
            return position(0);
        }

        public final CvPoint3D32f put(double... pts) {
            return put(pts, 0, pts.length);
        }

        public CvPoint3D32f put(double x, double y, double z) {
            return x((float) x).y((float) y).z((float) z);
        }

        public CvPoint3D32f put(CvPoint o) {
            return x(o.x()).y(o.y()).z(0.0f);
        }

        public CvPoint3D32f put(CvPoint2D32f o) {
            return x(o.x()).y(o.y()).z(0.0f);
        }

        public CvPoint3D32f put(CvPoint2D64f o) {
            return x((float) o.x()).y((float) o.y()).z(0.0f);
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + x() + ", " + y() + ", " + z() + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + x() + ", " + y() + ", " + z() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvPoint3D32f cvPoint3D32f(double x, double y, double z) {
        return new CvPoint3D32f().x((float) x).y((float) y).z((float) z);
    }

    public static class CvPoint2D64f extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native double x();

        public native CvPoint2D64f x(double d);

        public native double y();

        public native CvPoint2D64f y(double d);

        static {
            Loader.load();
        }

        public CvPoint2D64f() {
            allocate();
        }

        public CvPoint2D64f(int size) {
            allocateArray(size);
        }

        public CvPoint2D64f(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvPoint2D64f position(int position) {
            return (CvPoint2D64f) super.position(position);
        }

        public CvPoint2D64f(double[] pts, int offset, int length) {
            this(length / 2);
            put(pts, offset, length);
        }

        public CvPoint2D64f(double... pts) {
            this(pts, 0, pts.length);
        }

        public double[] get() {
            double[] pts = new double[this.capacity == 0 ? 2 : this.capacity * 2];
            get(pts);
            return pts;
        }

        public CvPoint2D64f get(double[] pts) {
            return get(pts, 0, pts.length);
        }

        public CvPoint2D64f get(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position(i);
                pts[(i * 2) + offset] = x();
                pts[(i * 2) + offset + 1] = y();
            }
            return position(0);
        }

        public final CvPoint2D64f put(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 2; i++) {
                position(i).put(pts[(i * 2) + offset], pts[(i * 2) + offset + 1]);
            }
            return position(0);
        }

        public final CvPoint2D64f put(double... pts) {
            return put(pts, 0, pts.length);
        }

        public CvPoint2D64f put(double x, double y) {
            return x(x).y(y);
        }

        public CvPoint2D64f put(CvPoint o) {
            return x(o.x()).y(o.y());
        }

        public CvPoint2D64f put(CvPoint2D32f o) {
            return x(o.x()).y(o.y());
        }

        public CvPoint2D64f put(CvPoint2D64f o) {
            return x(o.x()).y(o.y());
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + ((float) x()) + ", " + ((float) y()) + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + ((float) x()) + ", " + ((float) y()) + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvPoint2D64f cvPoint2D64f(double x, double y) {
        return new CvPoint2D64f().x(x).y(y);
    }

    public static class CvPoint3D64f extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native double x();

        public native CvPoint3D64f x(double d);

        public native double y();

        public native CvPoint3D64f y(double d);

        public native double z();

        public native CvPoint3D64f z(double d);

        static {
            Loader.load();
        }

        public CvPoint3D64f() {
            allocate();
        }

        public CvPoint3D64f(int size) {
            allocateArray(size);
        }

        public CvPoint3D64f(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvPoint3D64f position(int position) {
            return (CvPoint3D64f) super.position(position);
        }

        public CvPoint3D64f(double[] pts, int offset, int length) {
            this(length / 3);
            put(pts, offset, length);
        }

        public CvPoint3D64f(double... pts) {
            this(pts, 0, pts.length);
        }

        public double[] get() {
            double[] pts = new double[this.capacity == 0 ? 3 : this.capacity * 3];
            get(pts);
            return pts;
        }

        public CvPoint3D64f get(double[] pts) {
            return get(pts, 0, pts.length);
        }

        public CvPoint3D64f get(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 3; i++) {
                position(i);
                pts[(i * 3) + offset] = x();
                pts[(i * 3) + offset + 1] = y();
                pts[(i * 3) + offset + 2] = z();
            }
            return position(0);
        }

        public final CvPoint3D64f put(double[] pts, int offset, int length) {
            for (int i = 0; i < length / 3; i++) {
                position(i).put(pts[(i * 3) + offset], pts[(i * 3) + offset + 1], pts[(i * 3) + offset + 2]);
            }
            return position(0);
        }

        public final CvPoint3D64f put(double... pts) {
            return put(pts, 0, pts.length);
        }

        public CvPoint3D64f put(double x, double y, double z) {
            return x(x()).y(y()).z(z());
        }

        public CvPoint3D64f put(CvPoint o) {
            return x(o.x()).y(o.y()).z(0.0d);
        }

        public CvPoint3D64f put(CvPoint2D32f o) {
            return x(o.x()).y(o.y()).z(0.0d);
        }

        public CvPoint3D64f put(CvPoint2D64f o) {
            return x(o.x()).y(o.y()).z(0.0d);
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + ((float) x()) + ", " + ((float) y()) + ", " + ((float) z()) + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + ((float) x()) + ", " + ((float) y()) + ", " + ((float) z()) + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvPoint3D64f cvPoint3D64f(double x, double y, double z) {
        return new CvPoint3D64f().x(x).y(y).z(z);
    }

    public static class CvSize extends Pointer {
        public static final CvSize ZERO;

        private native void allocate();

        private native void allocateArray(int i);

        public native int height();

        public native CvSize height(int i);

        public native int width();

        public native CvSize width(int i);

        static {
            Loader.load();
            ZERO = new CvSize().width(0).height(0);
        }

        public CvSize() {
            allocate();
        }

        public CvSize(int size) {
            allocateArray(size);
        }

        public CvSize(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSize position(int position) {
            return (CvSize) super.position(position);
        }

        public CvSize(int width, int height) {
            allocate();
            width(width).height(height);
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + width() + ", " + height() + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + width() + ", " + height() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvSize cvSize(int width, int height) {
        return new CvSize().width(width).height(height);
    }

    public static class CvSize2D32f extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native float height();

        public native CvSize2D32f height(float f);

        public native float width();

        public native CvSize2D32f width(float f);

        static {
            Loader.load();
        }

        public CvSize2D32f() {
            allocate();
        }

        public CvSize2D32f(int size) {
            allocateArray(size);
        }

        public CvSize2D32f(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSize2D32f position(int position) {
            return (CvSize2D32f) super.position(position);
        }

        public CvSize2D32f(float width, float height) {
            allocate();
            width(width).height(height);
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + width() + ", " + height() + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + width() + ", " + height() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvSize2D32f cvSize2D32f(double width, double height) {
        return new CvSize2D32f().width((float) width).height((float) height);
    }

    public static class CvBox2D extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native float angle();

        public native CvBox2D angle(float f);

        public native CvBox2D center(CvPoint2D32f cvPoint2D32f);

        @ByRef
        public native CvPoint2D32f center();

        public native CvBox2D size(CvSize2D32f cvSize2D32f);

        @ByRef
        public native CvSize2D32f size();

        static {
            Loader.load();
        }

        public CvBox2D() {
            allocate();
        }

        public CvBox2D(int size) {
            allocateArray(size);
        }

        public CvBox2D(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvBox2D position(int position) {
            return (CvBox2D) super.position(position);
        }

        public CvBox2D(CvPoint2D32f center, CvSize2D32f size, float angle) {
            allocate();
            center(center).size(size).angle(angle);
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + center() + ", " + size() + ", " + angle() + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + center() + ", " + size() + ", " + angle() + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static class CvLineIterator extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int err();

        public native CvLineIterator err(int i);

        public native int minus_delta();

        public native CvLineIterator minus_delta(int i);

        public native int minus_step();

        public native CvLineIterator minus_step(int i);

        public native int plus_delta();

        public native CvLineIterator plus_delta(int i);

        public native int plus_step();

        public native CvLineIterator plus_step(int i);

        @Cast({"uchar*"})
        public native BytePointer ptr();

        public native CvLineIterator ptr(BytePointer bytePointer);

        static {
            Loader.load();
        }

        public CvLineIterator() {
            allocate();
        }

        public CvLineIterator(int size) {
            allocateArray(size);
        }

        public CvLineIterator(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvLineIterator position(int position) {
            return (CvLineIterator) super.position(position);
        }
    }

    public static class CvSlice extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int end_index();

        public native CvSlice end_index(int i);

        public native int start_index();

        public native CvSlice start_index(int i);

        static {
            Loader.load();
        }

        public CvSlice() {
            allocate();
        }

        public CvSlice(int size) {
            allocateArray(size);
        }

        public CvSlice(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSlice position(int position) {
            return (CvSlice) super.position(position);
        }
    }

    public static CvSlice cvSlice(int start, int end) {
        return new CvSlice().start_index(start).end_index(end);
    }

    public static class CvScalar extends Pointer {
        public static final CvScalar ALPHA1;
        public static final CvScalar ALPHA255;
        public static final CvScalar BLACK;
        public static final CvScalar BLUE;
        public static final CvScalar CYAN;
        public static final CvScalar GRAY;
        public static final CvScalar GREEN;
        public static final CvScalar MAGENTA;
        public static final CvScalar ONE;
        public static final CvScalar ONEHALF;
        public static final CvScalar RED;
        public static final CvScalar WHITE;
        public static final CvScalar YELLOW;
        public static final CvScalar ZERO;

        private native void allocate();

        private native void allocateArray(int i);

        @MemberGetter
        @Name({"val"})
        public native DoublePointer getDoublePointerVal();

        @MemberGetter
        @Name({"val"})
        @Cast({"double*"})
        public native LongPointer getLongPointerVal();

        public native double val(int i);

        public native CvScalar val(int i, double d);

        static {
            Loader.load();
            ZERO = new CvScalar().val(0, 0.0d).val(1, 0.0d).val(2, 0.0d).val(3, 0.0d);
            ONE = new CvScalar().val(0, 1.0d).val(1, 1.0d).val(2, 1.0d).val(3, 1.0d);
            ONEHALF = new CvScalar().val(0, 0.5d).val(1, 0.5d).val(2, 0.5d).val(3, 0.5d);
            ALPHA1 = new CvScalar().val(0, 0.0d).val(1, 0.0d).val(2, 0.0d).val(3, 1.0d);
            ALPHA255 = new CvScalar().val(0, 0.0d).val(1, 0.0d).val(2, 0.0d).val(3, 255.0d);
            WHITE = opencv_core.CV_RGB(255.0d, 255.0d, 255.0d);
            GRAY = opencv_core.CV_RGB(128.0d, 128.0d, 128.0d);
            BLACK = opencv_core.CV_RGB(0.0d, 0.0d, 0.0d);
            RED = opencv_core.CV_RGB(255.0d, 0.0d, 0.0d);
            GREEN = opencv_core.CV_RGB(0.0d, 255.0d, 0.0d);
            BLUE = opencv_core.CV_RGB(0.0d, 0.0d, 255.0d);
            CYAN = opencv_core.CV_RGB(0.0d, 255.0d, 255.0d);
            MAGENTA = opencv_core.CV_RGB(255.0d, 0.0d, 255.0d);
            YELLOW = opencv_core.CV_RGB(255.0d, 255.0d, 0.0d);
        }

        public CvScalar() {
            allocate();
        }

        public CvScalar(int size) {
            allocateArray(size);
        }

        public CvScalar(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvScalar position(int position) {
            return (CvScalar) super.position(position);
        }

        public CvScalar(double val0, double val1, double val2, double val3) {
            allocate();
            val(0, val0).val(1, val1).val(2, val2).val(3, val3);
        }

        public double getVal(int i) {
            return val(i);
        }

        public CvScalar setVal(int i, double val) {
            return val(i, val);
        }

        public void scale(double s) {
            for (int i = 0; i < 4; i++) {
                val(i, val(i) * s);
            }
        }

        public double red() {
            return val(2);
        }

        public double green() {
            return val(1);
        }

        public double blue() {
            return val(0);
        }

        public CvScalar red(double r) {
            val(2, r);
            return this;
        }

        public CvScalar green(double g) {
            val(1, g);
            return this;
        }

        public CvScalar blue(double b) {
            val(0, b);
            return this;
        }

        public double magnitude() {
            return Math.sqrt((val(0) * val(0)) + (val(1) * val(1)) + (val(2) * val(2)) + (val(3) * val(3)));
        }

        @Override // com.googlecode.javacpp.Pointer
        public String toString() {
            if (isNull()) {
                return super.toString();
            }
            if (capacity() == 0) {
                return "(" + ((float) val(0)) + ", " + ((float) val(1)) + ", " + ((float) val(2)) + ", " + ((float) val(3)) + ")";
            }
            String s = "";
            int p = position();
            int i = 0;
            while (i < capacity()) {
                position(i);
                s = s + (i == 0 ? "(" : " (") + ((float) val(0)) + ", " + ((float) val(1)) + ", " + ((float) val(2)) + ", " + ((float) val(3)) + ")";
                i++;
            }
            position(p);
            return s;
        }
    }

    public static CvScalar cvScalar(double val0, double val1, double val2, double val3) {
        return new CvScalar().val(0, val0).val(1, val1).val(2, val2).val(3, val3);
    }

    public static CvScalar cvRealScalar(double val0) {
        return new CvScalar().val(0, val0).val(1, 0.0d).val(2, 0.0d).val(3, 0.0d);
    }

    public static CvScalar cvScalarAll(double val0123) {
        return new CvScalar().val(0, val0123).val(1, val0123).val(2, val0123).val(3, val0123);
    }

    public static class CvMemBlock extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native CvMemBlock next();

        public native CvMemBlock next(CvMemBlock cvMemBlock);

        public native CvMemBlock prev();

        public native CvMemBlock prev(CvMemBlock cvMemBlock);

        static {
            Loader.load();
        }

        public CvMemBlock() {
            allocate();
        }

        public CvMemBlock(int size) {
            allocateArray(size);
        }

        public CvMemBlock(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvMemBlock position(int position) {
            return (CvMemBlock) super.position(position);
        }
    }

    public static class CvMemStorage extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int block_size();

        public native CvMemStorage block_size(int i);

        public native CvMemBlock bottom();

        public native CvMemStorage bottom(CvMemBlock cvMemBlock);

        public native int free_space();

        public native CvMemStorage free_space(int i);

        public native CvMemStorage parent();

        public native CvMemStorage parent(CvMemStorage cvMemStorage);

        public native int signature();

        public native CvMemStorage signature(int i);

        public native CvMemBlock top();

        public native CvMemStorage top(CvMemBlock cvMemBlock);

        static {
            Loader.load();
        }

        public CvMemStorage() {
            allocate();
            zero();
        }

        public CvMemStorage(int size) {
            allocateArray(size);
            zero();
        }

        public CvMemStorage(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvMemStorage position(int position) {
            return (CvMemStorage) super.position(position);
        }

        public static CvMemStorage create(int block_size) {
            CvMemStorage m = opencv_core.cvCreateMemStorage(block_size);
            if (m != null) {
                m.deallocator(new ReleaseDeallocator(m));
            }
            return m;
        }

        public static CvMemStorage create() {
            return create(0);
        }

        public void release() {
            deallocate();
        }

        protected static class ReleaseDeallocator extends CvMemStorage implements Pointer.Deallocator {
            @Override // com.googlecode.javacv.cpp.opencv_core.CvMemStorage, com.googlecode.javacpp.Pointer
            public /* bridge */ /* synthetic */ Pointer position(int i) {
                return super.position(i);
            }

            ReleaseDeallocator(CvMemStorage p) {
                super(p);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_core.cvReleaseMemStorage(this);
            }
        }
    }

    public static boolean CV_IS_STORAGE(CvArr storage) {
        return storage != null && (new CvMemStorage(storage).signature() & (-65536)) == 1116274688;
    }

    public static class CvMemStoragePos extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int free_space();

        public native CvMemStoragePos free_space(int i);

        public native CvMemBlock top();

        public native CvMemStoragePos top(CvMemBlock cvMemBlock);

        static {
            Loader.load();
        }

        public CvMemStoragePos() {
            allocate();
        }

        public CvMemStoragePos(int size) {
            allocateArray(size);
        }

        public CvMemStoragePos(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvMemStoragePos position(int position) {
            return (CvMemStoragePos) super.position(position);
        }
    }

    public static class CvSeqBlock extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int count();

        public native CvSeqBlock count(int i);

        @Cast({"schar*"})
        public native BytePointer data();

        public native CvSeqBlock data(BytePointer bytePointer);

        public native CvSeqBlock next();

        public native CvSeqBlock next(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock prev();

        public native CvSeqBlock prev(CvSeqBlock cvSeqBlock);

        public native int start_index();

        public native CvSeqBlock start_index(int i);

        static {
            Loader.load();
        }

        public CvSeqBlock() {
            allocate();
        }

        public CvSeqBlock(int size) {
            allocateArray(size);
        }

        public CvSeqBlock(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSeqBlock position(int position) {
            return (CvSeqBlock) super.position(position);
        }
    }

    public static class CvSeq extends CvArr {
        private native void allocate();

        private native void allocateArray(int i);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvSeq block_max(BytePointer bytePointer);

        public native int delta_elems();

        public native CvSeq delta_elems(int i);

        public native int elem_size();

        public native CvSeq elem_size(int i);

        public native CvSeq first(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock first();

        public native int flags();

        public native CvSeq flags(int i);

        public native CvSeq free_blocks(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock free_blocks();

        public native CvSeq h_next();

        public native CvSeq h_next(CvSeq cvSeq);

        public native CvSeq h_prev();

        public native CvSeq h_prev(CvSeq cvSeq);

        public native int header_size();

        public native CvSeq header_size(int i);

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvSeq ptr(BytePointer bytePointer);

        public native CvMemStorage storage();

        public native CvSeq storage(CvMemStorage cvMemStorage);

        public native int total();

        public native CvSeq total(int i);

        public native CvSeq v_next();

        public native CvSeq v_next(CvSeq cvSeq);

        public native CvSeq v_prev();

        public native CvSeq v_prev(CvSeq cvSeq);

        public CvSeq() {
            allocate();
            zero();
        }

        public CvSeq(int size) {
            allocateArray(size);
            zero();
        }

        public CvSeq(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSeq position(int position) {
            return (CvSeq) super.position(position);
        }

        public static CvSeq create(int seq_flags, int header_size, int elem_size, CvMemStorage storage) {
            return opencv_core.cvCreateSeq(seq_flags, header_size, elem_size, storage);
        }
    }

    public static class CvSetElem extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int flags();

        public native CvSetElem flags(int i);

        public native CvSetElem next_free();

        public native CvSetElem next_free(CvSetElem cvSetElem);

        static {
            Loader.load();
        }

        public CvSetElem() {
            allocate();
        }

        public CvSetElem(int size) {
            allocateArray(size);
        }

        public CvSetElem(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSetElem position(int position) {
            return (CvSetElem) super.position(position);
        }
    }

    public static class CvSet extends CvSeq {
        private native void allocate();

        private native void allocateArray(int i);

        public native int active_count();

        public native CvSet active_count(int i);

        public native CvSet free_elems(CvSetElem cvSetElem);

        public native CvSetElem free_elems();

        public CvSet() {
            allocate();
            zero();
        }

        public CvSet(int size) {
            allocateArray(size);
            zero();
        }

        public CvSet(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvSeq, com.googlecode.javacpp.Pointer
        public CvSet position(int position) {
            return (CvSet) super.position(position);
        }

        public static CvSet create(int set_flags, int header_size, int elem_size, CvMemStorage storage) {
            return opencv_core.cvCreateSet(set_flags, header_size, elem_size, storage);
        }
    }

    public static boolean CV_IS_SET_ELEM(Pointer ptr) {
        return new CvSetElem(ptr).flags() >= 0;
    }

    public static class CvGraphEdge extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int flags();

        public native CvGraphEdge flags(int i);

        public native CvGraphEdge next(int i);

        public native CvGraphEdge next(int i, CvGraphEdge cvGraphEdge);

        public native CvGraphEdge vtx(int i, CvGraphVtx cvGraphVtx);

        public native CvGraphVtx vtx(int i);

        public native float weight();

        public native CvGraphEdge weight(float f);

        static {
            Loader.load();
        }

        public CvGraphEdge() {
            allocate();
        }

        public CvGraphEdge(int size) {
            allocateArray(size);
        }

        public CvGraphEdge(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvGraphEdge position(int position) {
            return (CvGraphEdge) super.position(position);
        }
    }

    public static class CvGraphVtx extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native CvGraphEdge first();

        public native CvGraphVtx first(CvGraphEdge cvGraphEdge);

        public native int flags();

        public native CvGraphVtx flags(int i);

        static {
            Loader.load();
        }

        public CvGraphVtx() {
            allocate();
        }

        public CvGraphVtx(int size) {
            allocateArray(size);
        }

        public CvGraphVtx(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvGraphVtx position(int position) {
            return (CvGraphVtx) super.position(position);
        }
    }

    public static class CvGraphVtx2D extends CvGraphVtx {
        private native void allocate();

        private native void allocateArray(int i);

        public native CvGraphVtx2D ptr(CvPoint2D32f cvPoint2D32f);

        public native CvPoint2D32f ptr();

        public CvGraphVtx2D() {
            allocate();
        }

        public CvGraphVtx2D(int size) {
            allocateArray(size);
        }

        public CvGraphVtx2D(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvGraphVtx, com.googlecode.javacpp.Pointer
        public CvGraphVtx2D position(int position) {
            return (CvGraphVtx2D) super.position(position);
        }
    }

    public static class CvGraph extends CvSet {
        private native void allocate();

        private native void allocateArray(int i);

        public native CvGraph edges(CvSet cvSet);

        public native CvSet edges();

        public CvGraph() {
            allocate();
            zero();
        }

        public CvGraph(int size) {
            allocateArray(size);
            zero();
        }

        public CvGraph(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvSet, com.googlecode.javacv.cpp.opencv_core.CvSeq, com.googlecode.javacpp.Pointer
        public CvGraph position(int position) {
            return (CvGraph) super.position(position);
        }

        public static CvGraph create(int graph_flags, int header_size, int vtx_size, int edge_size, CvMemStorage storage) {
            return opencv_core.cvCreateGraph(graph_flags, header_size, vtx_size, edge_size, storage);
        }
    }

    public static class CvChain extends CvSeq {
        private native void allocate();

        private native void allocateArray(int i);

        public native CvChain origin(CvPoint cvPoint);

        @ByRef
        public native CvPoint origin();

        public CvChain() {
            allocate();
        }

        public CvChain(int size) {
            allocateArray(size);
        }

        public CvChain(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvSeq, com.googlecode.javacpp.Pointer
        public CvChain position(int position) {
            return (CvChain) super.position(position);
        }
    }

    public static class CvContour extends CvSeq {
        private native void allocate();

        private native void allocateArray(int i);

        public native int color();

        public native CvContour color(int i);

        public native CvContour rect(CvRect cvRect);

        @ByRef
        public native CvRect rect();

        public native int reserved(int i);

        public native CvContour reserved(int i, int i2);

        public CvContour() {
            allocate();
        }

        public CvContour(int size) {
            allocateArray(size);
        }

        public CvContour(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvSeq, com.googlecode.javacpp.Pointer
        public CvContour position(int position) {
            return (CvContour) super.position(position);
        }
    }

    public static boolean CV_IS_SEQ(CvArr seq) {
        return seq != null && (new CvSeq(seq).flags() & (-65536)) == 1117323264;
    }

    public static boolean CV_IS_SET(CvArr set) {
        return set != null && (new CvSet(set).flags() & (-65536)) == 1117257728;
    }

    public static int CV_SEQ_ELTYPE(CvSeq seq) {
        return seq.flags() & 4095;
    }

    public static int CV_SEQ_KIND(CvSeq seq) {
        return seq.flags() & 12288;
    }

    public static boolean CV_IS_SEQ_INDEX(CvSeq seq) {
        return CV_SEQ_ELTYPE(seq) == CV_SEQ_ELTYPE_INDEX && CV_SEQ_KIND(seq) == 0;
    }

    public static boolean CV_IS_SEQ_CURVE(CvSeq seq) {
        return CV_SEQ_KIND(seq) == 4096;
    }

    public static boolean CV_IS_SEQ_CLOSED(CvSeq seq) {
        return (seq.flags() & 16384) != 0;
    }

    public static boolean CV_IS_SEQ_CONVEX(CvSeq seq) {
        return false;
    }

    public static boolean CV_IS_SEQ_HOLE(CvSeq seq) {
        return (seq.flags() & 32768) != 0;
    }

    public static boolean CV_IS_SEQ_SIMPLE(CvSeq seq) {
        return true;
    }

    public static boolean CV_IS_SEQ_POINT_SET(CvSeq seq) {
        return CV_SEQ_ELTYPE(seq) == CV_32SC2 || CV_SEQ_ELTYPE(seq) == CV_32FC2;
    }

    public static boolean CV_IS_SEQ_POINT_SUBSET(CvSeq seq) {
        return CV_IS_SEQ_INDEX(seq) || CV_SEQ_ELTYPE(seq) == 7;
    }

    public static boolean CV_IS_SEQ_POLYLINE(CvSeq seq) {
        return CV_SEQ_KIND(seq) == 4096 && CV_IS_SEQ_POINT_SET(seq);
    }

    public static boolean CV_IS_SEQ_POLYGON(CvSeq seq) {
        return CV_IS_SEQ_POLYLINE(seq) && CV_IS_SEQ_CLOSED(seq);
    }

    public static boolean CV_IS_SEQ_CHAIN(CvSeq seq) {
        return CV_SEQ_KIND(seq) == 4096 && seq.elem_size() == 1;
    }

    public static boolean CV_IS_SEQ_CONTOUR(CvSeq seq) {
        return CV_IS_SEQ_CLOSED(seq) && (CV_IS_SEQ_POLYLINE(seq) || CV_IS_SEQ_CHAIN(seq));
    }

    public static boolean CV_IS_SEQ_CHAIN_CONTOUR(CvSeq seq) {
        return CV_IS_SEQ_CHAIN(seq) && CV_IS_SEQ_CLOSED(seq);
    }

    public static boolean CV_IS_SEQ_POLYGON_TREE(CvSeq seq) {
        return CV_SEQ_ELTYPE(seq) == 0 && CV_SEQ_KIND(seq) == 8192;
    }

    public static boolean CV_IS_GRAPH(CvSeq seq) {
        return CV_IS_SET(seq) && CV_SEQ_KIND(seq) == 4096;
    }

    public static boolean CV_IS_GRAPH_ORIENTED(CvSeq seq) {
        return (seq.flags() & 16384) != 0;
    }

    public static boolean CV_IS_SUBDIV2D(CvSeq seq) {
        return CV_IS_SET(seq) && CV_SEQ_KIND(seq) == 8192;
    }

    public static class CvSeqWriter extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native CvSeqBlock block();

        public native CvSeqWriter block(CvSeqBlock cvSeqBlock);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvSeqWriter block_max(BytePointer bytePointer);

        @Cast({"schar*"})
        public native BytePointer block_min();

        public native CvSeqWriter block_min(BytePointer bytePointer);

        public native int header_size();

        public native CvSeqWriter header_size(int i);

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvSeqWriter ptr(BytePointer bytePointer);

        public native CvSeq seq();

        public native CvSeqWriter seq(CvSeq cvSeq);

        static {
            Loader.load();
        }

        public CvSeqWriter() {
            allocate();
        }

        public CvSeqWriter(int size) {
            allocateArray(size);
        }

        public CvSeqWriter(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSeqWriter position(int position) {
            return (CvSeqWriter) super.position(position);
        }
    }

    public static class CvSeqReader extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native CvSeqBlock block();

        public native CvSeqReader block(CvSeqBlock cvSeqBlock);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvSeqReader block_max(BytePointer bytePointer);

        @Cast({"schar*"})
        public native BytePointer block_min();

        public native CvSeqReader block_min(BytePointer bytePointer);

        public native int delta_index();

        public native CvSeqReader delta_index(int i);

        public native int header_size();

        public native CvSeqReader header_size(int i);

        @Cast({"schar*"})
        public native BytePointer prev_elem();

        public native CvSeqReader prev_elem(BytePointer bytePointer);

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvSeqReader ptr(BytePointer bytePointer);

        public native CvSeq seq();

        public native CvSeqReader seq(CvSeq cvSeq);

        static {
            Loader.load();
        }

        public CvSeqReader() {
            allocate();
        }

        public CvSeqReader(int size) {
            allocateArray(size);
        }

        public CvSeqReader(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSeqReader position(int position) {
            return (CvSeqReader) super.position(position);
        }
    }

    public static CvPoint CV_CURRENT_POINT(CvSeqReader reader) {
        return new CvPoint(reader.ptr());
    }

    public static CvPoint CV_PREV_POINT(CvSeqReader reader) {
        return new CvPoint(reader.prev_elem());
    }

    public static void CV_READ_EDGE(CvPoint pt1, CvPoint pt2, CvSeqReader reader) {
        if (!$assertionsDisabled && reader.seq().elem_size() != Loader.sizeof(CvPoint.class)) {
            throw new AssertionError();
        }
        CV_PREV_POINT(reader);
        CV_CURRENT_POINT(reader);
        reader.prev_elem(reader.ptr());
        CV_NEXT_SEQ_ELEM(Loader.sizeof(CvPoint.class), reader);
    }

    public static CvGraphEdge CV_NEXT_GRAPH_EDGE(CvGraphEdge edge, CvGraphVtx vertex) {
        if ($assertionsDisabled || edge.vtx(0).equals(vertex) || edge.vtx(1).equals(vertex)) {
            return edge.next(edge.vtx(1).equals(vertex) ? 1 : 0);
        }
        throw new AssertionError();
    }

    @Opaque
    public static class CvFileStorage extends Pointer {
        static {
            Loader.load();
        }

        public CvFileStorage() {
        }

        public CvFileStorage(Pointer p) {
            super(p);
        }

        public static CvFileStorage open(String filename, CvMemStorage memstorage, int flags) {
            return open(filename, memstorage, flags, null);
        }

        public static CvFileStorage open(String filename, CvMemStorage memstorage, int flags, String encoding) {
            CvFileStorage f = opencv_core.cvOpenFileStorage(filename, memstorage, flags, encoding);
            if (f != null) {
                f.deallocator(new ReleaseDeallocator(f));
            }
            return f;
        }

        public void release() {
            deallocate();
        }

        protected static class ReleaseDeallocator extends CvFileStorage implements Pointer.Deallocator {
            ReleaseDeallocator(CvFileStorage p) {
                super(p);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_core.cvReleaseFileStorage(this);
            }
        }
    }

    public static class CvAttrList extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        @Cast({"const char**"})
        public native PointerPointer attr();

        public native CvAttrList attr(PointerPointer pointerPointer);

        public native CvAttrList next();

        public native CvAttrList next(CvAttrList cvAttrList);

        static {
            Loader.load();
        }

        public CvAttrList() {
            allocate();
        }

        public CvAttrList(int size) {
            allocateArray(size);
        }

        public CvAttrList(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvAttrList position(int position) {
            return (CvAttrList) super.position(position);
        }
    }

    public static CvAttrList cvAttrList(PointerPointer attr, CvAttrList next) {
        return new CvAttrList().attr(attr).next(next);
    }

    public static CvAttrList cvAttrList() {
        return new CvAttrList();
    }

    public static int CV_NODE_TYPE(int flags) {
        return flags & 7;
    }

    public static boolean CV_NODE_IS_INT(int flags) {
        return CV_NODE_TYPE(flags) == 1;
    }

    public static boolean CV_NODE_IS_REAL(int flags) {
        return CV_NODE_TYPE(flags) == 2;
    }

    public static boolean CV_NODE_IS_STRING(int flags) {
        return CV_NODE_TYPE(flags) == 3;
    }

    public static boolean CV_NODE_IS_SEQ(int flags) {
        return CV_NODE_TYPE(flags) == 5;
    }

    public static boolean CV_NODE_IS_MAP(int flags) {
        return CV_NODE_TYPE(flags) == 6;
    }

    public static boolean CV_NODE_IS_COLLECTION(int flags) {
        return CV_NODE_TYPE(flags) >= 5;
    }

    public static boolean CV_NODE_IS_FLOW(int flags) {
        return (flags & 8) != 0;
    }

    public static boolean CV_NODE_IS_EMPTY(int flags) {
        return (flags & 32) != 0;
    }

    public static boolean CV_NODE_IS_USER(int flags) {
        return (flags & 16) != 0;
    }

    public static boolean CV_NODE_HAS_NAME(int flags) {
        return (flags & 64) != 0;
    }

    public static boolean CV_NODE_SEQ_IS_SIMPLE(CvSeq seq) {
        return (seq.flags() & 256) != 0;
    }

    public static class CvString extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int len();

        public native CvString len(int i);

        @Cast({"char*"})
        public native BytePointer ptr();

        public native CvString ptr(BytePointer bytePointer);

        static {
            Loader.load();
        }

        public CvString() {
            allocate();
        }

        public CvString(int size) {
            allocateArray(size);
        }

        public CvString(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvString position(int position) {
            return (CvString) super.position(position);
        }
    }

    public static class CvStringHashNode extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int hashval();

        public native CvStringHashNode hashval(int i);

        public native CvStringHashNode next();

        public native CvStringHashNode next(CvStringHashNode cvStringHashNode);

        @ByRef
        public native CvString str();

        public native CvStringHashNode str(CvString cvString);

        static {
            Loader.load();
        }

        public CvStringHashNode() {
            allocate();
        }

        public CvStringHashNode(int size) {
            allocateArray(size);
        }

        public CvStringHashNode(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvStringHashNode position(int position) {
            return (CvStringHashNode) super.position(position);
        }
    }

    @Opaque
    public static class CvFileNodeHash extends Pointer {
        static {
            Loader.load();
        }

        public CvFileNodeHash() {
        }

        public CvFileNodeHash(Pointer p) {
            super(p);
        }
    }

    public static class CvFileNode extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        @Name({"data.f"})
        public native double data_f();

        public native CvFileNode data_f(double d);

        @Name({"data.i"})
        public native int data_i();

        public native CvFileNode data_i(int i);

        public native CvFileNode data_map(CvFileNodeHash cvFileNodeHash);

        @Name({"data.map"})
        public native CvFileNodeHash data_map();

        public native CvFileNode data_seq(CvSeq cvSeq);

        @Name({"data.seq"})
        public native CvSeq data_seq();

        public native CvFileNode data_str(CvString cvString);

        @Name({"data.str"})
        @ByRef
        public native CvString data_str();

        public native CvFileNode info(CvTypeInfo cvTypeInfo);

        public native CvTypeInfo info();

        public native int tag();

        public native CvFileNode tag(int i);

        static {
            Loader.load();
        }

        public CvFileNode() {
            allocate();
        }

        public CvFileNode(int size) {
            allocateArray(size);
        }

        public CvFileNode(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvFileNode position(int position) {
            return (CvFileNode) super.position(position);
        }
    }

    public static class CvIsInstanceFunc extends FunctionPointer {
        private native void allocate();

        public native int call(@Const Pointer pointer);

        static {
            Loader.load();
        }

        public CvIsInstanceFunc(Pointer p) {
            super(p);
        }

        protected CvIsInstanceFunc() {
            allocate();
        }
    }

    public static class CvReleaseFunc extends FunctionPointer {
        private native void allocate();

        public native void call(PointerPointer pointerPointer);

        static {
            Loader.load();
        }

        public CvReleaseFunc(Pointer p) {
            super(p);
        }

        protected CvReleaseFunc() {
            allocate();
        }
    }

    public static class CvReadFunc extends FunctionPointer {
        private native void allocate();

        public native Pointer call(CvFileStorage cvFileStorage, CvFileNode cvFileNode);

        static {
            Loader.load();
        }

        public CvReadFunc(Pointer p) {
            super(p);
        }

        protected CvReadFunc() {
            allocate();
        }
    }

    public static class CvWriteFunc extends FunctionPointer {
        private native void allocate();

        public native void call(CvFileStorage cvFileStorage, String str, @Const Pointer pointer, @ByVal CvAttrList cvAttrList);

        static {
            Loader.load();
        }

        public CvWriteFunc(Pointer p) {
            super(p);
        }

        protected CvWriteFunc() {
            allocate();
        }
    }

    public static class CvCloneFunc extends FunctionPointer {
        private native void allocate();

        public native Pointer call(@Const Pointer pointer);

        static {
            Loader.load();
        }

        public CvCloneFunc(Pointer p) {
            super(p);
        }

        protected CvCloneFunc() {
            allocate();
        }
    }

    public static class CvTypeInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public native CvCloneFunc m12clone();

        public native CvTypeInfo clone(CvCloneFunc cvCloneFunc);

        public native int flags();

        public native CvTypeInfo flags(int i);

        public native int header_size();

        public native CvTypeInfo header_size(int i);

        public native CvIsInstanceFunc is_instance();

        public native CvTypeInfo is_instance(CvIsInstanceFunc cvIsInstanceFunc);

        public native CvTypeInfo next();

        public native CvTypeInfo next(CvTypeInfo cvTypeInfo);

        public native CvTypeInfo prev();

        public native CvTypeInfo prev(CvTypeInfo cvTypeInfo);

        public native CvReadFunc read();

        public native CvTypeInfo read(CvReadFunc cvReadFunc);

        public native CvReleaseFunc release();

        public native CvTypeInfo release(CvReleaseFunc cvReleaseFunc);

        @Cast({"const char*"})
        public native BytePointer type_name();

        public native CvTypeInfo type_name(BytePointer bytePointer);

        public native CvTypeInfo write(CvWriteFunc cvWriteFunc);

        public native CvWriteFunc write();

        static {
            Loader.load();
        }

        public CvTypeInfo() {
            allocate();
        }

        public CvTypeInfo(int size) {
            allocateArray(size);
        }

        public CvTypeInfo(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvTypeInfo position(int position) {
            return (CvTypeInfo) super.position(position);
        }
    }

    public static class CvPluginFuncInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native Pointer default_func_addr();

        public native CvPluginFuncInfo default_func_addr(Pointer pointer);

        public native PointerPointer func_addr();

        public native CvPluginFuncInfo func_addr(PointerPointer pointerPointer);

        @Cast({"const char*"})
        public native BytePointer func_names();

        public native CvPluginFuncInfo func_names(BytePointer bytePointer);

        public native int loaded_from();

        public native CvPluginFuncInfo loaded_from(int i);

        public native int search_modules();

        public native CvPluginFuncInfo search_modules(int i);

        static {
            Loader.load();
        }

        public CvPluginFuncInfo() {
            allocate();
        }

        public CvPluginFuncInfo(int size) {
            allocateArray(size);
        }

        public CvPluginFuncInfo(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvPluginFuncInfo position(int position) {
            return (CvPluginFuncInfo) super.position(position);
        }
    }

    public static class CvModuleInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native CvModuleInfo func_tab(CvPluginFuncInfo cvPluginFuncInfo);

        public native CvPluginFuncInfo func_tab();

        @Cast({"const char*"})
        public native BytePointer name();

        public native CvModuleInfo name(BytePointer bytePointer);

        public native CvModuleInfo next();

        public native CvModuleInfo next(CvModuleInfo cvModuleInfo);

        @Cast({"const char*"})
        public native BytePointer version();

        public native CvModuleInfo version(BytePointer bytePointer);

        static {
            Loader.load();
        }

        public CvModuleInfo() {
            allocate();
        }

        public CvModuleInfo(int size) {
            allocateArray(size);
        }

        public CvModuleInfo(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvModuleInfo position(int position) {
            return (CvModuleInfo) super.position(position);
        }
    }

    public static void cvFree(Pointer ptr) {
        cvFree_(ptr);
        ptr.setNull();
    }

    public static CvMat cvGetSubArr(CvArr arr, CvMat submat, @ByVal CvRect rect) {
        return cvGetSubRect(arr, submat, rect);
    }

    public static CvMat cvGetRow(CvArr arr, CvMat submat, int row) {
        return cvGetRows(arr, submat, row, row + 1, 1);
    }

    public static CvMat cvGetCol(CvArr arr, CvMat submat, int col) {
        return cvGetCols(arr, submat, col, col + 1);
    }

    public static class CvNArrayIterator extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int count();

        public native CvNArrayIterator count(int i);

        public native int dims();

        public native CvNArrayIterator dims(int i);

        public native CvMatND hdr(int i);

        public native CvNArrayIterator hdr(int i, CvMatND cvMatND);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i);

        public native CvNArrayIterator ptr(int i, BytePointer bytePointer);

        public native CvNArrayIterator size(CvSize cvSize);

        @ByRef
        public native CvSize size();

        public native int stack(int i);

        public native CvNArrayIterator stack(int i, int i2);

        static {
            Loader.load();
        }

        public CvNArrayIterator() {
            allocate();
        }

        public CvNArrayIterator(int size) {
            allocateArray(size);
        }

        public CvNArrayIterator(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvNArrayIterator position(int position) {
            return (CvNArrayIterator) super.position(position);
        }
    }

    public static int cvInitNArrayIterator(int count, CvArr[] arrs, CvArr mask, CvMatND stubs, CvNArrayIterator array_iterator, int flags) {
        return cvInitNArrayIterator(count, new CvArrArray(arrs), mask, stubs, array_iterator, flags);
    }

    public static CvArr cvReshapeND(CvArr arr, CvArr header, int new_cn, int new_dims, int[] new_sizes) {
        return cvReshapeMatND(arr, Loader.sizeof(header.getClass()), header, new_cn, new_dims, new_sizes);
    }

    public static void cvCopy(CvArr src, CvArr dst) {
        cvCopy(src, dst, null);
    }

    public static void cvSet(CvArr arr, CvScalar value) {
        cvSet(arr, value, null);
    }

    public static void cvZero(CvArr arr) {
        cvSetZero(arr);
    }

    public static void cvMixChannels(CvArr[] src, int src_count, CvArr[] dst, int dst_count, int[] from_to, int pair_count) {
        cvMixChannels(new CvArrArray(src), src_count, new CvArrArray(dst), dst_count, from_to, pair_count);
    }

    public static void cvCvtScale(CvArr src, CvArr dst, double scale, double shift) {
        cvConvertScale(src, dst, scale, shift);
    }

    public static void cvScale(CvArr src, CvArr dst, double scale, double shift) {
        cvConvertScale(src, dst, scale, shift);
    }

    public static void cvConvert(CvArr src, CvArr dst) {
        cvConvertScale(src, dst, 1.0d, 0.0d);
    }

    public static void cvCvtScaleAbs(CvArr src, CvArr dst, double scale, double shift) {
        cvConvertScaleAbs(src, dst, scale, shift);
    }

    public static void cvSubS(CvArr src, CvScalar value, CvArr dst, CvArr mask) {
        cvAddS(src, cvScalar(-value.val(0), -value.val(1), -value.val(2), -value.val(3)), dst, mask);
    }

    public static void cvAXPY(CvArr A, double real_scalar, CvArr B, CvArr C) {
        cvScaleAdd(A, cvRealScalar(real_scalar), B, C);
    }

    public static void cvAbs(CvArr src, CvArr dst) {
        cvAbsDiffS(src, dst, cvScalarAll(0.0d));
    }

    public static int cvCheckArray(CvArr arr, int flags, double min_val, double max_val) {
        return cvCheckArr(arr, flags, min_val, max_val);
    }

    public static void cvMatMulAdd(CvArr src1, CvArr src2, CvArr src3, CvArr dst) {
        cvGEMM(src1, src2, 1.0d, src3, 1.0d, dst, 0);
    }

    public static void cvMatMul(CvArr src1, CvArr src2, CvArr dst) {
        cvMatMulAdd(src1, src2, null, dst);
    }

    public static void cvMatMulAddEx(CvArr srcA, CvArr srcB, double alpha, CvArr srcC, double beta, CvArr dst, int tABC) {
        cvGEMM(srcA, srcB, alpha, srcC, beta, dst, tABC);
    }

    public static void cvMatMulAddS(CvArr src, CvArr dst, CvMat transmat, CvMat shiftvec) {
        cvTransform(src, dst, transmat, shiftvec);
    }

    public static void cvT(CvArr src, CvArr dst) {
        cvTranspose(src, dst);
    }

    public static void cvMirror(CvArr src, CvArr dst, int flip_mode) {
        cvFlip(src, dst, flip_mode);
    }

    public static double cvInvert(CvArr src, CvArr dst) {
        return cvInvert(src, dst, 0);
    }

    public static double cvInv(CvArr src, CvArr dst, int method) {
        return cvInvert(src, dst, method);
    }

    public static double cvInv(CvArr src, CvArr dst) {
        return cvInvert(src, dst, 0);
    }

    public static int cvSolve(CvArr A, CvArr B, CvArr X) {
        return cvSolve(A, B, X, 0);
    }

    public static void cvSetIdentity(CvArr mat, double value) {
        cvSetIdentity(mat, cvRealScalar(value));
    }

    public static void cvSetIdentity(CvArr mat) {
        cvSetIdentity(mat, 1.0d);
    }

    public static void cvCalcCovarMatrix(CvArr[] vects, int count, CvArr cov_mat, CvArr avg, int flags) {
        cvCalcCovarMatrix(new CvArrArray(vects), count, cov_mat, avg, flags);
    }

    public static double cvMahalonobis(CvArr vec1, CvArr vec2, CvArr mat) {
        return cvMahalanobis(vec1, vec2, mat);
    }

    public static void cvMinMaxLoc(CvArr arr, double[] min_val, double[] max_val) {
        cvMinMaxLoc(arr, min_val, max_val, (CvPoint) null, (CvPoint) null, (CvArr) null);
    }

    public static double cvNorm(CvArr arr1, CvArr arr2, int norm_type) {
        return cvNorm(arr1, arr2, norm_type, null);
    }

    public static double cvNorm(CvArr arr1, CvArr arr2) {
        return cvNorm(arr1, arr2, 4, null);
    }

    public static double cvNorm(CvArr arr1) {
        return cvNorm(arr1, null, 4, null);
    }

    public static void cvNormalize(CvArr src, CvArr dst) {
        cvNormalize(src, dst, 1.0d, 0.0d, 4, null);
    }

    public static void cvFFT(CvArr src, CvArr dst, int flags, int nonzero_rows) {
        cvDFT(src, dst, flags, nonzero_rows);
    }

    public static Pointer cvCvtSeqToArray(CvSeq seq, Pointer elements) {
        return cvCvtSeqToArray(seq, elements, CV_WHOLE_SEQ);
    }

    public static Pointer cvCvtSeqToArray(CvSeq seq, Buffer elements) {
        return cvCvtSeqToArray(seq, elements, CV_WHOLE_SEQ);
    }

    public static CvSeq cvCloneSeq(CvSeq seq, CvMemStorage storage) {
        return cvSeqSlice(seq, CV_WHOLE_SEQ, storage, 1);
    }

    public static class CvCmpFunc extends FunctionPointer {
        private native void allocate();

        public native int call(@Const Pointer pointer, @Const Pointer pointer2, Pointer pointer3);

        static {
            Loader.load();
        }

        public CvCmpFunc(Pointer p) {
            super(p);
        }

        protected CvCmpFunc() {
            allocate();
        }
    }

    public CvSetElem cvSetNew(CvSet set_header) {
        CvSetElem elem = set_header.free_elems();
        if (elem != null) {
            set_header.free_elems(elem.next_free());
            elem.flags(elem.flags() & CV_SET_ELEM_IDX_MASK);
            set_header.active_count(set_header.active_count() + 1);
        } else {
            cvSetAdd(set_header, null, elem);
        }
        return elem;
    }

    public void cvSetRemoveByPtr(CvSet set_header, CvSetElem elem) {
        if (!$assertionsDisabled && elem.flags() < 0) {
            throw new AssertionError();
        }
        elem.next_free(set_header.free_elems());
        elem.flags((elem.flags() & CV_SET_ELEM_IDX_MASK) | 128);
        set_header.free_elems(elem);
        set_header.active_count(set_header.active_count() - 1);
    }

    public static CvSetElem cvGetSetElem(CvSet set_header, int index) {
        CvSetElem elem = new CvSetElem(cvGetSeqElem(set_header, index));
        if (elem == null || !CV_IS_SET_ELEM(elem)) {
            return null;
        }
        return elem;
    }

    public static CvGraphEdge cvGraphFindEdge(CvGraph graph, int start_idx, int end_idx) {
        return cvFindGraphEdge(graph, start_idx, end_idx);
    }

    public static CvGraphEdge cvGraphFindEdgeByPtr(CvGraph graph, CvGraphVtx start_vtx, CvGraphVtx end_vtx) {
        return cvFindGraphEdgeByPtr(graph, start_vtx, end_vtx);
    }

    public static CvGraphVtx cvGetGraphVtx(CvGraph graph, int idx) {
        return new CvGraphVtx(cvGetSetElem(graph, idx));
    }

    public static int cvGraphVtxIdx(CvGraph graph, CvGraphVtx vtx) {
        return vtx.flags() & CV_SET_ELEM_IDX_MASK;
    }

    public static int cvGraphEdgeIdx(CvGraph graph, CvGraphEdge edge) {
        return edge.flags() & CV_SET_ELEM_IDX_MASK;
    }

    public static int cvGraphGetVtxCount(CvGraph graph) {
        return graph.active_count();
    }

    public static int cvGraphGetEdgeCount(CvGraph graph) {
        return graph.edges().active_count();
    }

    public static boolean CV_IS_GRAPH_EDGE_VISITED(CvGraphVtx vtx) {
        return (vtx.flags() & 1073741824) != 0;
    }

    public static boolean CV_IS_GRAPH_VERTEX_VISITED(CvGraphEdge edge) {
        return (edge.flags() & 1073741824) != 0;
    }

    public static class CvGraphScanner extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native CvGraphScanner dst(CvGraphVtx cvGraphVtx);

        public native CvGraphVtx dst();

        public native CvGraphEdge edge();

        public native CvGraphScanner edge(CvGraphEdge cvGraphEdge);

        public native CvGraph graph();

        public native CvGraphScanner graph(CvGraph cvGraph);

        public native int index();

        public native CvGraphScanner index(int i);

        public native int mask();

        public native CvGraphScanner mask(int i);

        public native CvGraphScanner stack(CvSeq cvSeq);

        public native CvSeq stack();

        public native CvGraphScanner vtx(CvGraphVtx cvGraphVtx);

        public native CvGraphVtx vtx();

        static {
            Loader.load();
        }

        public CvGraphScanner() {
            allocate();
            zero();
        }

        public CvGraphScanner(int size) {
            allocateArray(size);
            zero();
        }

        public CvGraphScanner(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvGraphScanner position(int position) {
            return (CvGraphScanner) super.position(position);
        }

        public static CvGraphScanner create(CvGraph graph, CvGraphVtx vtx, int mask) {
            CvGraphScanner g = opencv_core.cvCreateGraphScanner(graph, vtx, mask);
            if (g != null) {
                g.deallocator(new ReleaseDeallocator(g));
            }
            return g;
        }

        public void release() {
            deallocate();
        }

        protected static class ReleaseDeallocator extends CvGraphScanner implements Pointer.Deallocator {
            @Override // com.googlecode.javacv.cpp.opencv_core.CvGraphScanner, com.googlecode.javacpp.Pointer
            public /* bridge */ /* synthetic */ Pointer position(int i) {
                return super.position(i);
            }

            ReleaseDeallocator(CvGraphScanner p) {
                super(p);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_core.cvReleaseGraphScanner(this);
            }
        }
    }

    public static CvScalar CV_RGB(double r, double g, double b) {
        return cvScalar(b, g, r, 0.0d);
    }

    public static void cvEllipseBox(CvArr img, @ByVal CvBox2D box, @ByVal CvScalar color, int thickness, int line_type, int shift) {
        CvSize2D32f size = box.size();
        CvSize axes = cvSize((int) Math.round(size.width() * 0.5d), (int) Math.round(size.height() * 0.5d));
        cvEllipse(img, cvPointFrom32f(box.center()), axes, box.angle(), 0.0d, 360.0d, color, thickness, line_type, shift);
    }

    public static void cvFillPoly(CvArr img, CvPoint[] pts, int[] npts, int contours, @ByVal CvScalar color, int line_type, int shift) {
        cvFillPoly(img, new PointerPointer(pts), npts, contours, color, line_type, shift);
    }

    public static void cvPolyLine(CvArr img, CvPoint[] pts, int[] npts, int contours, int is_closed, @ByVal CvScalar color, int thickness, int line_type, int shift) {
        cvPolyLine(img, new PointerPointer(pts), npts, contours, is_closed, color, thickness, line_type, shift);
    }

    public static void cvDrawRect(CvArr img, CvPoint pt1, CvPoint pt2, CvScalar color, int thickness, int line_type, int shift) {
        cvRectangle(img, pt1, pt2, color, thickness, line_type, shift);
    }

    public static void cvDrawLine(CvArr img, CvPoint pt1, CvPoint pt2, CvScalar color, int thickness, int line_type, int shift) {
        cvLine(img, pt1, pt2, color, thickness, line_type, shift);
    }

    public static void cvDrawCircle(CvArr img, CvPoint center, int radius, CvScalar color, int thickness, int line_type, int shift) {
        cvCircle(img, center, radius, color, thickness, line_type, shift);
    }

    public static void cvDrawEllipse(CvArr img, CvPoint center, CvSize axes, double angle, double start_angle, double end_angle, CvScalar color, int thickness, int line_type, int shift) {
        cvEllipse(img, center, axes, angle, start_angle, end_angle, color, thickness, line_type, shift);
    }

    public static void cvDrawPolyLine(CvArr img, CvPoint[] pts, int[] npts, int contours, int is_closed, CvScalar color, int thickness, int line_type, int shift) {
        cvPolyLine(img, pts, npts, contours, is_closed, color, thickness, line_type, shift);
    }

    public static void cvDrawPolyLine(CvArr img, @Cast({"CvPoint**"}) PointerPointer pts, int[] npts, int contours, int is_closed, CvScalar color, int thickness, int line_type, int shift) {
        cvPolyLine(img, pts, npts, contours, is_closed, color, thickness, line_type, shift);
    }

    public static void cvDrawPolyLine(CvArr img, @ByPtrPtr CvPoint pts, int[] npts, int contours, int is_closed, CvScalar color, int thickness, int line_type, int shift) {
        cvPolyLine(img, pts, npts, contours, is_closed, color, thickness, line_type, shift);
    }

    public static class CvFont extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        @Const
        public native IntPointer ascii();

        public native CvFont ascii(IntPointer intPointer);

        public native CvFont color(CvScalar cvScalar);

        @ByRef
        public native CvScalar color();

        @Const
        public native IntPointer cyrillic();

        public native CvFont cyrillic(IntPointer intPointer);

        public native float dx();

        public native CvFont dx(float f);

        public native int font_face();

        public native CvFont font_face(int i);

        @Const
        public native IntPointer greek();

        public native CvFont greek(IntPointer intPointer);

        public native float hscale();

        public native CvFont hscale(float f);

        public native int line_type();

        public native CvFont line_type(int i);

        @Cast({"const char*"})
        public native BytePointer nameFont();

        public native CvFont nameFont(BytePointer bytePointer);

        public native float shear();

        public native CvFont shear(float f);

        public native int thickness();

        public native CvFont thickness(int i);

        public native float vscale();

        public native CvFont vscale(float f);

        static {
            Loader.load();
        }

        public CvFont() {
            allocate();
        }

        public CvFont(int size) {
            allocateArray(size);
        }

        public CvFont(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvFont position(int position) {
            return (CvFont) super.position(position);
        }

        public CvFont(int font_face, double hscale, double vscale, double shear, int thickness, int line_type) {
            allocate();
            opencv_core.cvInitFont(this, font_face, hscale, vscale, shear, thickness, line_type);
        }

        public CvFont(int font_face, double scale, int thickness) {
            allocate();
            opencv_core.cvInitFont(this, font_face, scale, scale, 0.0d, thickness, 16);
        }
    }

    public static CvFont cvFont(double scale, int thickness) {
        CvFont font = new CvFont();
        cvInitFont(font, 1, scale, scale, 0.0d, thickness, 16);
        return font;
    }

    public static void cvDrawContours(CvArr img, CvSeq contour, @ByVal CvScalar external_color, @ByVal CvScalar hole_color, int max_level, int thickness, int line_type) {
        cvDrawContours(img, contour, external_color, hole_color, max_level, thickness, line_type, CvPoint.ZERO);
    }

    public static class CvTreeNodeIterator extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int level();

        public native CvTreeNodeIterator level(int i);

        public native int max_level();

        public native CvTreeNodeIterator max_level(int i);

        @Const
        public native Pointer node();

        public native CvTreeNodeIterator node(Pointer pointer);

        static {
            Loader.load();
        }

        public CvTreeNodeIterator() {
            allocate();
        }

        public CvTreeNodeIterator(int size) {
            allocateArray(size);
        }

        public CvTreeNodeIterator(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvTreeNodeIterator position(int position) {
            return (CvTreeNodeIterator) super.position(position);
        }
    }

    public static class CvAllocFunc extends FunctionPointer {
        private native void allocate();

        public native Pointer call(@Cast({"size_t"}) long j, Pointer pointer);

        static {
            Loader.load();
        }

        public CvAllocFunc(Pointer p) {
            super(p);
        }

        protected CvAllocFunc() {
            allocate();
        }
    }

    public static class CvFreeFunc extends FunctionPointer {
        private native void allocate();

        public native int call(Pointer pointer, Pointer pointer2);

        static {
            Loader.load();
        }

        public CvFreeFunc(Pointer p) {
            super(p);
        }

        protected CvFreeFunc() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplCreateImageHeader extends FunctionPointer {
        private native void allocate();

        public native IplImage call(int i, int i2, int i3, @Cast({"char*"}) BytePointer bytePointer, @Cast({"char*"}) BytePointer bytePointer2, int i4, int i5, int i6, int i7, int i8, IplROI iplROI, IplImage iplImage, Pointer pointer, IplTileInfo iplTileInfo);

        static {
            Loader.load();
        }

        public Cv_iplCreateImageHeader(Pointer p) {
            super(p);
        }

        protected Cv_iplCreateImageHeader() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplAllocateImageData extends FunctionPointer {
        private native void allocate();

        public native void call(IplImage iplImage, int i, int i2);

        static {
            Loader.load();
        }

        public Cv_iplAllocateImageData(Pointer p) {
            super(p);
        }

        protected Cv_iplAllocateImageData() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplDeallocate extends FunctionPointer {
        private native void allocate();

        public native void call(IplImage iplImage, int i);

        static {
            Loader.load();
        }

        public Cv_iplDeallocate(Pointer p) {
            super(p);
        }

        protected Cv_iplDeallocate() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplCreateROI extends FunctionPointer {
        private native void allocate();

        public native IplROI call(int i, int i2, int i3, int i4, int i5);

        static {
            Loader.load();
        }

        public Cv_iplCreateROI(Pointer p) {
            super(p);
        }

        protected Cv_iplCreateROI() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplCloneImage extends FunctionPointer {
        private native void allocate();

        public native IplImage call(@Const IplImage iplImage);

        static {
            Loader.load();
        }

        public Cv_iplCloneImage(Pointer p) {
            super(p);
        }

        protected Cv_iplCloneImage() {
            allocate();
        }
    }

    public static void cvStartWriteStruct(CvFileStorage fs, String name, int struct_flags, String type_name) {
        cvStartWriteStruct(fs, name, struct_flags, type_name, CV_ATTR_LIST_EMPTY);
    }

    public static void cvWrite(CvFileStorage fs, String name, Pointer ptr) {
        cvWrite(fs, name, ptr, CV_ATTR_LIST_EMPTY);
    }

    public static int cvReadInt(CvFileNode node) {
        return cvReadInt(node, 0);
    }

    public static int cvReadInt(CvFileNode node, int default_value) {
        if (node == null) {
            return default_value;
        }
        if (CV_NODE_IS_INT(node.tag())) {
            int default_value2 = node.data_i();
            return default_value2;
        }
        if (!CV_NODE_IS_REAL(node.tag())) {
            return Integer.MAX_VALUE;
        }
        int default_value3 = (int) Math.round(node.data_f());
        return default_value3;
    }

    public static int cvReadIntByName(CvFileStorage fs, CvFileNode map, String name) {
        return cvReadIntByName(fs, map, name, 0);
    }

    public static int cvReadIntByName(CvFileStorage fs, CvFileNode map, String name, int default_value) {
        return cvReadInt(cvGetFileNodeByName(fs, map, name), default_value);
    }

    public static double cvReadReal(CvFileNode node) {
        return cvReadReal(node, 0.0d);
    }

    public static double cvReadReal(CvFileNode node, double default_value) {
        if (node == null) {
            return default_value;
        }
        if (CV_NODE_IS_INT(node.tag())) {
            double default_value2 = node.data_i();
            return default_value2;
        }
        if (!CV_NODE_IS_REAL(node.tag())) {
            return 1.0E300d;
        }
        double default_value3 = node.data_f();
        return default_value3;
    }

    public static double cvReadRealByName(CvFileStorage fs, CvFileNode map, String name) {
        return cvReadRealByName(fs, map, name, 0.0d);
    }

    public static double cvReadRealByName(CvFileStorage fs, CvFileNode map, String name, double default_value) {
        return cvReadReal(cvGetFileNodeByName(fs, map, name), default_value);
    }

    public static String cvReadString(CvFileNode node) {
        return cvReadString(node, null);
    }

    public static String cvReadString(CvFileNode node, String default_value) {
        if (node != null) {
            if (CV_NODE_IS_STRING(node.tag())) {
                CvString str = node.data_str();
                BytePointer pointer = str.ptr();
                byte[] bytes = new byte[str.len()];
                pointer.get(bytes);
                String default_value2 = new String(bytes);
                return default_value2;
            }
            return null;
        }
        return default_value;
    }

    public static String cvReadStringByName(CvFileStorage fs, CvFileNode map, String name) {
        return cvReadStringByName(fs, map, name, null);
    }

    public static String cvReadStringByName(CvFileStorage fs, CvFileNode map, String name, String default_value) {
        return cvReadString(cvGetFileNodeByName(fs, map, name), default_value);
    }

    public static Pointer cvRead(CvFileStorage fs, CvFileNode node) {
        return cvRead(fs, node, CV_ATTR_LIST_EMPTY);
    }

    public static Pointer cvReadByName(CvFileStorage fs, CvFileNode map, String name) {
        return cvReadByName(fs, map, name, CV_ATTR_LIST_EMPTY);
    }

    public static Pointer cvReadByName(CvFileStorage fs, CvFileNode map, String name, CvAttrList attributes) {
        CvFileNode n = cvGetFileNodeByName(fs, map, name);
        return cvRead(fs, n, attributes);
    }

    public static void cvSave(String filename, Pointer struct_ptr) {
        cvSave(filename, struct_ptr, null, null, CV_ATTR_LIST_EMPTY);
    }

    public static Pointer cvLoad(String filename) {
        return cvLoad(filename, null, null, null);
    }

    public static class CvErrorCallback extends FunctionPointer {
        private native void allocate();

        public native int call(int i, String str, String str2, String str3, int i2, Pointer pointer);

        static {
            Loader.load();
        }

        public CvErrorCallback(Pointer p) {
            super(p);
        }

        protected CvErrorCallback() {
            allocate();
        }
    }

    @Name({"std::vector<std::string>"})
    public static class StringVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native String get(@Cast({"size_t"}) long j);

        public native StringVector put(@Cast({"size_t"}) long j, String str);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public StringVector(String... array) {
            this(array.length);
            put(array);
        }

        public StringVector() {
            allocate();
        }

        public StringVector(long n) {
            allocate(n);
        }

        public StringVector(Pointer p) {
            super(p);
        }

        public StringVector put(String... array) {
            if (size() < array.length) {
                resize(array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put(i, array[i]);
            }
            return this;
        }
    }

    @Name({"std::vector<cv::Mat>"})
    public static class MatVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @OutputMat
        @ValueGetter
        @Index
        public native CvMat getCvMat(@Cast({"size_t"}) long j);

        @OutputMat
        @ValueGetter
        @Index
        public native CvMatND getCvMatND(@Cast({"size_t"}) long j);

        @OutputMat
        @ValueGetter
        @Index
        public native IplImage getIplImage(@Cast({"size_t"}) long j);

        @ValueSetter
        @Index
        public native MatVector put(@Cast({"size_t"}) long j, @InputMat CvArr cvArr);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public MatVector(CvArr... array) {
            this(array.length);
            put(array);
        }

        public MatVector() {
            allocate();
        }

        public MatVector(long n) {
            allocate(n);
        }

        public MatVector(Pointer p) {
            super(p);
        }

        public MatVector put(CvArr... array) {
            if (size() < array.length) {
                resize(array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put(i, array[i]);
            }
            return this;
        }
    }

    @Name({"std::vector<std::vector<char> >"})
    public static class ByteVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @Index
        public native byte get(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native ByteVectorVector put(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, byte b);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native void resize(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native long size();

        @Index
        public native long size(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public ByteVectorVector() {
            allocate();
        }

        public ByteVectorVector(long n) {
            allocate(n);
        }

        public ByteVectorVector(Pointer p) {
            super(p);
        }
    }

    @Name({"std::vector<std::vector<int> >"})
    public static class IntVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @Index
        public native int get(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native IntVectorVector put(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, int i);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native void resize(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native long size();

        @Index
        public native long size(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public IntVectorVector() {
            allocate();
        }

        public IntVectorVector(long n) {
            allocate(n);
        }

        public IntVectorVector(Pointer p) {
            super(p);
        }
    }

    @Name({"std::vector<std::vector<cv::Point> >"})
    public static class PointVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByVal
        @Index
        public native CvPoint get(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native PointVectorVector put(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, CvPoint cvPoint);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native void resize(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native long size();

        @Index
        public native long size(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public PointVectorVector() {
            allocate();
        }

        public PointVectorVector(long n) {
            allocate(n);
        }

        public PointVectorVector(Pointer p) {
            super(p);
        }
    }

    @Name({"std::vector<std::vector<cv::Point2f> >"})
    public static class Point2fVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByVal
        @Index
        public native CvPoint2D32f get(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native Point2fVectorVector put(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, CvPoint2D32f cvPoint2D32f);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native void resize(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native long size();

        @Index
        public native long size(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public Point2fVectorVector() {
            allocate();
        }

        public Point2fVectorVector(long n) {
            allocate(n);
        }

        public Point2fVectorVector(Pointer p) {
            super(p);
        }
    }

    @Name({"std::vector<std::vector<cv::Point2d> >"})
    public static class Point2dVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByVal
        @Index
        public native CvPoint2D32f get(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native Point2dVectorVector put(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, CvPoint2D32f cvPoint2D32f);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native void resize(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native long size();

        @Index
        public native long size(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public Point2dVectorVector() {
            allocate();
        }

        public Point2dVectorVector(long n) {
            allocate(n);
        }

        public Point2dVectorVector(Pointer p) {
            super(p);
        }
    }

    @Name({"std::vector<std::vector<cv::Rect> >"})
    public static class RectVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByVal
        @Index
        public native CvRect get(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native RectVectorVector put(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, CvRect cvRect);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native void resize(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native long size();

        @Index
        public native long size(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public RectVectorVector() {
            allocate();
        }

        public RectVectorVector(long n) {
            allocate(n);
        }

        public RectVectorVector(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class KDTree extends Pointer {
        private native void allocate();

        private native void allocate(@InputArray CvMat cvMat, @InputArray CvMat cvMat2, @Cast({"bool"}) boolean z);

        private native void allocate(@InputArray CvMat cvMat, @Cast({"bool"}) boolean z);

        public native void build(@InputArray CvMat cvMat, @InputArray CvMat cvMat2, @Cast({"bool"}) boolean z);

        public native void build(@InputArray CvMat cvMat, @Cast({"bool"}) boolean z);

        public native int dims();

        public native int findNearest(@InputArray FloatPointer floatPointer, int i, int i2, @OutputArray IntPointer intPointer, @OutputArray CvMat cvMat, @OutputArray FloatPointer floatPointer2, @OutputArray IntPointer intPointer2);

        public native int findNearest(@InputArray CvMat cvMat, int i, int i2, @OutputArray CvMat cvMat2, @OutputArray CvMat cvMat3, @OutputArray CvMat cvMat4, @OutputArray CvMat cvMat5);

        public native void findOrthoRange(@InputArray FloatPointer floatPointer, @InputArray FloatPointer floatPointer2, @OutputArray IntPointer intPointer, @OutputArray CvMat cvMat, @OutputArray IntPointer intPointer2);

        public native void findOrthoRange(@InputArray CvMat cvMat, @InputArray CvMat cvMat2, @OutputArray CvMat cvMat3, @OutputArray CvMat cvMat4, @OutputArray CvMat cvMat5);

        @Const
        public native FloatPointer getPoint(int i, int[] iArr);

        public native void getPoints(@InputArray FloatPointer floatPointer, @OutputArray CvMat cvMat, @OutputArray IntPointer intPointer);

        public native void getPoints(@InputArray CvMat cvMat, @OutputArray CvMat cvMat2, @OutputArray CvMat cvMat3);

        @StdVector
        @Const
        public native IntPointer labels();

        public native KDTree labels(IntPointer intPointer);

        public native int maxDepth();

        public native KDTree maxDepth(int i);

        @StdVector
        @Const
        public native Node nodes();

        public native KDTree nodes(Node node);

        public native int normType();

        public native KDTree normType(int i);

        @InputMat
        public native CvMat points();

        public native KDTree points(CvMat cvMat);

        static {
            Loader.load();
        }

        @NoOffset
        public static class Node extends Pointer {
            private native void allocate();

            private native void allocate(int i, int i2, int i3, float f);

            private native void allocateArray(int i);

            public native float boundary();

            public native Node boundary(float f);

            public native int idx();

            public native Node idx(int i);

            public native int left();

            public native Node left(int i);

            public native int right();

            public native Node right(int i);

            static {
                Loader.load();
            }

            public Node() {
                allocate();
            }

            public Node(int size) {
                allocateArray(size);
            }

            public Node(Pointer p) {
                super(p);
            }

            public Node(int _idx, int _left, int _right, float _boundary) {
                allocate(_idx, _left, _right, _boundary);
            }

            @Override // com.googlecode.javacpp.Pointer
            public Node position(int position) {
                return (Node) super.position(position);
            }
        }

        public KDTree() {
            allocate();
        }

        public KDTree(Pointer p) {
            super(p);
        }

        public KDTree(CvMat _points, boolean copyAndReorderPoints) {
            allocate(_points, copyAndReorderPoints);
        }

        public KDTree(CvMat _points, CvMat _labels, boolean copyAndReorderPoints) {
            allocate(_points, _labels, copyAndReorderPoints);
        }
    }

    @Namespace("cv")
    public static class Algorithm extends Pointer {
        @Ptr
        public static native Algorithm _create(String str);

        private native void allocate();

        public static native void getList(@ByRef StringVector stringVector);

        @Ptr
        @Const
        public native Algorithm getAlgorithm(String str);

        @Cast({"bool"})
        public native boolean getBool(String str);

        public native double getDouble(String str);

        public native int getInt(String str);

        @OutputMat
        public native CvMat getMat(String str);

        @ByVal
        public native MatVector getMatVector(String str);

        public native void getParams(@ByRef StringVector stringVector);

        @ByRef
        public native String getString(String str);

        public native AlgorithmInfo info();

        @ByRef
        public native String name();

        @ByRef
        public native String paramHelp(String str);

        public native int paramType(String str);

        public native void read(@Const @Adapter(argc = 2, value = "FileNodeAdapter") CvFileStorage cvFileStorage, CvFileNode cvFileNode);

        public native void set(String str, double d);

        public native void set(String str, int i);

        public native void set(String str, @Ptr Algorithm algorithm);

        public native void set(String str, CvMat cvMat);

        public native void set(String str, @ByRef MatVector matVector);

        public native void set(String str, String str2);

        public native void set(String str, @Cast({"bool"}) boolean z);

        public native void write(@Const @Adapter("FileStorageAdapter") CvFileStorage cvFileStorage);

        static {
            Loader.load();
        }

        public Algorithm() {
            allocate();
        }

        public Algorithm(Pointer p) {
            super(p);
        }

        public static class Constructor extends FunctionPointer {
            private native void allocate();

            public native Algorithm call();

            static {
                Loader.load();
            }

            public Constructor(Pointer p) {
                super(p);
            }

            protected Constructor() {
                allocate();
            }
        }

        @Const
        @Namespace("cv::Algorithm")
        public static class Getter extends FunctionPointer {
            public native int call(Algorithm algorithm);

            static {
                Loader.load();
            }

            public Getter(Pointer p) {
                super(p);
            }
        }

        @Namespace("cv::Algorithm")
        public static class Setter extends FunctionPointer {
            public native void call(Algorithm algorithm, int i);

            static {
                Loader.load();
            }

            public Setter(Pointer p) {
                super(p);
            }
        }
    }

    @Namespace("cv")
    public static class AlgorithmInfo extends Pointer {
        private native void allocate(String str, Algorithm.Constructor constructor);

        public native void addParam_(@ByRef Algorithm algorithm, String str, int i, Pointer pointer, @Cast({"bool"}) boolean z, Algorithm.Getter getter, Algorithm.Setter setter, String str2);

        public native void get(Algorithm algorithm, String str, int i, Pointer pointer);

        public native void getParams(@ByRef StringVector stringVector);

        @ByRef
        public native String name();

        @ByRef
        public native String paramHelp(String str);

        public native int paramType(String str);

        public native void read(Algorithm algorithm, @Const @Adapter(argc = 2, value = "FileNodeAdapter") CvFileStorage cvFileStorage, CvFileNode cvFileNode);

        public native void write(Algorithm algorithm, @Const @Adapter("FileStorageAdapter") CvFileStorage cvFileStorage);

        static {
            Loader.load();
        }

        public AlgorithmInfo(String name, Algorithm.Constructor create) {
            allocate(name, create);
        }

        public AlgorithmInfo(Pointer p) {
            super(p);
        }
    }

    @NoOffset
    @Namespace("cv")
    public static class Param extends Pointer {
        public static final int ALGORITHM = 6;
        public static final int BOOLEAN = 1;
        public static final int FLOAT = 7;
        public static final int INT = 0;
        public static final int MAT = 4;
        public static final int MAT_VECTOR = 5;
        public static final int REAL = 2;
        public static final int SHORT = 10;
        public static final int STRING = 3;
        public static final int UCHAR = 11;
        public static final int UINT64 = 9;
        public static final int UNSIGNED_INT = 8;

        private native void allocate();

        private native void allocate(int i, @Cast({"bool"}) boolean z, int i2, Algorithm.Getter getter, Algorithm.Setter setter, String str);

        public native Algorithm.Getter getter();

        public native Param getter(Algorithm.Getter getter);

        public native Param help(String str);

        @ByRef
        public native String help();

        public native int offset();

        public native Param offset(int i);

        public native Param readonly(boolean z);

        @Cast({"bool"})
        public native boolean readonly();

        public native Algorithm.Setter setter();

        public native Param setter(Algorithm.Setter setter);

        public native int type();

        public native Param type(int i);

        static {
            Loader.load();
        }

        public Param() {
            allocate();
        }

        public Param(int _type, @Cast({"bool"}) boolean _readonly, int _offset, Algorithm.Getter _getter, Algorithm.Setter _setter, String _help) {
            allocate(_type, _readonly, _offset, _getter, _setter, _help);
        }

        public Param(Pointer p) {
            super(p);
        }
    }

    public static class Predicate extends FunctionPointer {
        private native void allocate();

        public native boolean call(Pointer pointer, Pointer pointer2);

        static {
            Loader.load();
        }

        public Predicate(Pointer p) {
            super(p);
        }

        protected Predicate() {
            allocate();
        }
    }
}
