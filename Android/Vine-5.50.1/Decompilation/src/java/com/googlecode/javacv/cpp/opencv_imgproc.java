package com.googlecode.javacv.cpp;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.FunctionPointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.PointerPointer;
import com.googlecode.javacpp.SizeTPointer;
import com.googlecode.javacpp.annotation.Adapter;
import com.googlecode.javacpp.annotation.ByPtrPtr;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.ByVal;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Namespace;
import com.googlecode.javacpp.annotation.NoOffset;
import com.googlecode.javacpp.annotation.Opaque;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;
import com.googlecode.javacpp.annotation.StdVector;
import com.googlecode.javacv.cpp.opencv_core;

@Properties(inherit = {opencv_core.class}, value = {@Platform(include = {"<opencv2/imgproc/imgproc_c.h>", "<opencv2/imgproc/imgproc.hpp>"}, link = {"opencv_imgproc@.2.4"}), @Platform(link = {"opencv_imgproc246"}, value = {"windows"})})
/* loaded from: classes.dex */
public class opencv_imgproc {
    public static final int BORDER_CONSTANT = 0;
    public static final int BORDER_DEFAULT = 4;
    public static final int BORDER_ISOLATED = 16;
    public static final int BORDER_REFLECT = 2;
    public static final int BORDER_REFLECT101 = 4;
    public static final int BORDER_REFLECT_101 = 4;
    public static final int BORDER_REPLICATE = 1;
    public static final int BORDER_TRANSPARENT = 5;
    public static final int BORDER_WRAP = 3;
    public static final int CV_ADAPTIVE_THRESH_GAUSSIAN_C = 1;
    public static final int CV_ADAPTIVE_THRESH_MEAN_C = 0;
    public static final int CV_BGR2BGR555 = 22;
    public static final int CV_BGR2BGR565 = 12;
    public static final int CV_BGR2BGRA = 0;
    public static final int CV_BGR2GRAY = 6;
    public static final int CV_BGR2HLS = 52;
    public static final int CV_BGR2HLS_FULL = 68;
    public static final int CV_BGR2HSV = 40;
    public static final int CV_BGR2HSV_FULL = 66;
    public static final int CV_BGR2Lab = 44;
    public static final int CV_BGR2Luv = 50;
    public static final int CV_BGR2RGB = 4;
    public static final int CV_BGR2RGBA = 2;
    public static final int CV_BGR2XYZ = 32;
    public static final int CV_BGR2YCrCb = 36;
    public static final int CV_BGR2YUV = 82;
    public static final int CV_BGR2YUV_I420 = 128;
    public static final int CV_BGR2YUV_IYUV = 128;
    public static final int CV_BGR2YUV_YV12 = 132;
    public static final int CV_BGR5552BGR = 24;
    public static final int CV_BGR5552BGRA = 28;
    public static final int CV_BGR5552GRAY = 31;
    public static final int CV_BGR5552RGB = 25;
    public static final int CV_BGR5552RGBA = 29;
    public static final int CV_BGR5652BGR = 14;
    public static final int CV_BGR5652BGRA = 18;
    public static final int CV_BGR5652GRAY = 21;
    public static final int CV_BGR5652RGB = 15;
    public static final int CV_BGR5652RGBA = 19;
    public static final int CV_BGRA2BGR = 1;
    public static final int CV_BGRA2BGR555 = 26;
    public static final int CV_BGRA2BGR565 = 16;
    public static final int CV_BGRA2GRAY = 10;
    public static final int CV_BGRA2RGB = 3;
    public static final int CV_BGRA2RGBA = 5;
    public static final int CV_BGRA2YUV_I420 = 130;
    public static final int CV_BGRA2YUV_IYUV = 130;
    public static final int CV_BGRA2YUV_YV12 = 134;
    public static final int CV_BILATERAL = 4;
    public static final int CV_BLUR = 1;
    public static final int CV_BLUR_NO_SCALE = 0;
    public static final int CV_BayerBG2BGR = 46;
    public static final int CV_BayerBG2BGR_VNG = 62;
    public static final int CV_BayerBG2GRAY = 86;
    public static final int CV_BayerBG2RGB = 48;
    public static final int CV_BayerBG2RGB_VNG = 64;
    public static final int CV_BayerGB2BGR = 47;
    public static final int CV_BayerGB2BGR_VNG = 63;
    public static final int CV_BayerGB2GRAY = 87;
    public static final int CV_BayerGB2RGB = 49;
    public static final int CV_BayerGB2RGB_VNG = 65;
    public static final int CV_BayerGR2BGR = 49;
    public static final int CV_BayerGR2BGR_VNG = 65;
    public static final int CV_BayerGR2GRAY = 89;
    public static final int CV_BayerGR2RGB = 47;
    public static final int CV_BayerGR2RGB_VNG = 63;
    public static final int CV_BayerRG2BGR = 48;
    public static final int CV_BayerRG2BGR_VNG = 64;
    public static final int CV_BayerRG2GRAY = 88;
    public static final int CV_BayerRG2RGB = 46;
    public static final int CV_BayerRG2RGB_VNG = 62;
    public static final int CV_CANNY_L2_GRADIENT = Integer.MIN_VALUE;
    public static final int CV_CHAIN_APPROX_NONE = 1;
    public static final int CV_CHAIN_APPROX_SIMPLE = 2;
    public static final int CV_CHAIN_APPROX_TC89_KCOS = 4;
    public static final int CV_CHAIN_APPROX_TC89_L1 = 3;
    public static final int CV_CHAIN_CODE = 0;
    public static final int CV_CLOCKWISE = 1;
    public static final int CV_COLORCVT_MAX = 135;
    public static final int CV_COMP_BHATTACHARYYA = 3;
    public static final int CV_COMP_CHISQR = 1;
    public static final int CV_COMP_CORREL = 0;
    public static final int CV_COMP_HELLINGER = 3;
    public static final int CV_COMP_INTERSECT = 2;
    public static final int CV_CONTOURS_MATCH_I1 = 1;
    public static final int CV_CONTOURS_MATCH_I2 = 2;
    public static final int CV_CONTOURS_MATCH_I3 = 3;
    public static final int CV_COUNTER_CLOCKWISE = 2;
    public static final int CV_DIST_C = 3;
    public static final int CV_DIST_FAIR = 5;
    public static final int CV_DIST_HUBER = 7;
    public static final int CV_DIST_L1 = 1;
    public static final int CV_DIST_L12 = 4;
    public static final int CV_DIST_L2 = 2;
    public static final int CV_DIST_LABEL_CCOMP = 0;
    public static final int CV_DIST_LABEL_PIXEL = 1;
    public static final int CV_DIST_MASK_3 = 3;
    public static final int CV_DIST_MASK_5 = 5;
    public static final int CV_DIST_MASK_PRECISE = 0;
    public static final int CV_DIST_USER = -1;
    public static final int CV_DIST_WELSCH = 6;
    public static final int CV_FLOODFILL_FIXED_RANGE = 65536;
    public static final int CV_FLOODFILL_MASK_ONLY = 131072;
    public static final int CV_GAUSSIAN = 2;
    public static final int CV_GAUSSIAN_5x5 = 7;
    public static final int CV_GRAY2BGR = 8;
    public static final int CV_GRAY2BGR555 = 30;
    public static final int CV_GRAY2BGR565 = 20;
    public static final int CV_GRAY2BGRA = 9;
    public static final int CV_GRAY2RGB = 8;
    public static final int CV_GRAY2RGBA = 9;
    public static final int CV_HIST_ARRAY = 0;
    public static final int CV_HIST_MAGIC_VAL = 1111818240;
    public static final int CV_HIST_RANGES_FLAG = 2048;
    public static final int CV_HIST_SPARSE = 1;
    public static final int CV_HIST_TREE = 1;
    public static final int CV_HIST_UNIFORM = 1;
    public static final int CV_HIST_UNIFORM_FLAG = 1024;
    public static final int CV_HLS2BGR = 60;
    public static final int CV_HLS2BGR_FULL = 72;
    public static final int CV_HLS2RGB = 61;
    public static final int CV_HLS2RGB_FULL = 73;
    public static final int CV_HOUGH_GRADIENT = 3;
    public static final int CV_HOUGH_MULTI_SCALE = 2;
    public static final int CV_HOUGH_PROBABILISTIC = 1;
    public static final int CV_HOUGH_STANDARD = 0;
    public static final int CV_HSV2BGR = 54;
    public static final int CV_HSV2BGR_FULL = 70;
    public static final int CV_HSV2RGB = 55;
    public static final int CV_HSV2RGB_FULL = 71;
    public static final int CV_INTER_AREA = 3;
    public static final int CV_INTER_CUBIC = 2;
    public static final int CV_INTER_LANCZOS4 = 4;
    public static final int CV_INTER_LINEAR = 1;
    public static final int CV_INTER_NN = 0;
    public static final int CV_LBGR2Lab = 74;
    public static final int CV_LBGR2Luv = 76;
    public static final int CV_LINK_RUNS = 5;
    public static final int CV_LRGB2Lab = 75;
    public static final int CV_LRGB2Luv = 77;
    public static final int CV_Lab2BGR = 56;
    public static final int CV_Lab2LBGR = 78;
    public static final int CV_Lab2LRGB = 79;
    public static final int CV_Lab2RGB = 57;
    public static final int CV_Luv2BGR = 58;
    public static final int CV_Luv2LBGR = 80;
    public static final int CV_Luv2LRGB = 81;
    public static final int CV_Luv2RGB = 59;
    public static final int CV_MAX_SOBEL_KSIZE = 7;
    public static final int CV_MEDIAN = 3;
    public static final int CV_MOP_BLACKHAT = 6;
    public static final int CV_MOP_CLOSE = 3;
    public static final int CV_MOP_DILATE = 1;
    public static final int CV_MOP_ERODE = 0;
    public static final int CV_MOP_GRADIENT = 4;
    public static final int CV_MOP_OPEN = 2;
    public static final int CV_MOP_TOPHAT = 5;
    public static final int CV_NEXT_AROUND_DST = 34;
    public static final int CV_NEXT_AROUND_LEFT = 19;
    public static final int CV_NEXT_AROUND_ORG = 0;
    public static final int CV_NEXT_AROUND_RIGHT = 49;
    public static final int CV_POLY_APPROX_DP = 0;
    public static final int CV_PREV_AROUND_DST = 51;
    public static final int CV_PREV_AROUND_LEFT = 32;
    public static final int CV_PREV_AROUND_ORG = 17;
    public static final int CV_PREV_AROUND_RIGHT = 2;
    public static final int CV_PTLOC_ERROR = -2;
    public static final int CV_PTLOC_INSIDE = 0;
    public static final int CV_PTLOC_ON_EDGE = 2;
    public static final int CV_PTLOC_OUTSIDE_RECT = -1;
    public static final int CV_PTLOC_VERTEX = 1;
    public static final int CV_RETR_CCOMP = 2;
    public static final int CV_RETR_EXTERNAL = 0;
    public static final int CV_RETR_FLOODFILL = 4;
    public static final int CV_RETR_LIST = 1;
    public static final int CV_RETR_TREE = 3;
    public static final int CV_RGB2BGR = 4;
    public static final int CV_RGB2BGR555 = 23;
    public static final int CV_RGB2BGR565 = 13;
    public static final int CV_RGB2BGRA = 2;
    public static final int CV_RGB2GRAY = 7;
    public static final int CV_RGB2HLS = 53;
    public static final int CV_RGB2HLS_FULL = 69;
    public static final int CV_RGB2HSV = 41;
    public static final int CV_RGB2HSV_FULL = 67;
    public static final int CV_RGB2Lab = 45;
    public static final int CV_RGB2Luv = 51;
    public static final int CV_RGB2RGBA = 0;
    public static final int CV_RGB2XYZ = 33;
    public static final int CV_RGB2YCrCb = 37;
    public static final int CV_RGB2YUV = 83;
    public static final int CV_RGB2YUV_I420 = 127;
    public static final int CV_RGB2YUV_IYUV = 127;
    public static final int CV_RGB2YUV_YV12 = 131;
    public static final int CV_RGBA2BGR = 3;
    public static final int CV_RGBA2BGR555 = 27;
    public static final int CV_RGBA2BGR565 = 17;
    public static final int CV_RGBA2BGRA = 5;
    public static final int CV_RGBA2GRAY = 11;
    public static final int CV_RGBA2RGB = 1;
    public static final int CV_RGBA2YUV_I420 = 129;
    public static final int CV_RGBA2YUV_IYUV = 129;
    public static final int CV_RGBA2YUV_YV12 = 133;
    public static final int CV_RGBA2mRGBA = 125;
    public static final int CV_SCHARR = -1;
    public static final int CV_SHAPE_CROSS = 1;
    public static final int CV_SHAPE_CUSTOM = 100;
    public static final int CV_SHAPE_ELLIPSE = 2;
    public static final int CV_SHAPE_RECT = 0;
    public static final int CV_SUBDIV2D_VIRTUAL_POINT_FLAG = 1073741824;
    public static final int CV_THRESH_BINARY = 0;
    public static final int CV_THRESH_BINARY_INV = 1;
    public static final int CV_THRESH_MASK = 7;
    public static final int CV_THRESH_OTSU = 8;
    public static final int CV_THRESH_TOZERO = 3;
    public static final int CV_THRESH_TOZERO_INV = 4;
    public static final int CV_THRESH_TRUNC = 2;
    public static final int CV_TM_CCOEFF = 4;
    public static final int CV_TM_CCOEFF_NORMED = 5;
    public static final int CV_TM_CCORR = 2;
    public static final int CV_TM_CCORR_NORMED = 3;
    public static final int CV_TM_SQDIFF = 0;
    public static final int CV_TM_SQDIFF_NORMED = 1;
    public static final int CV_WARP_FILL_OUTLIERS = 8;
    public static final int CV_WARP_INVERSE_MAP = 16;
    public static final int CV_XYZ2BGR = 34;
    public static final int CV_XYZ2RGB = 35;
    public static final int CV_YCrCb2BGR = 38;
    public static final int CV_YCrCb2RGB = 39;
    public static final int CV_YUV2BGR = 84;
    public static final int CV_YUV2BGRA_I420 = 105;
    public static final int CV_YUV2BGRA_IYUV = 105;
    public static final int CV_YUV2BGRA_NV12 = 95;
    public static final int CV_YUV2BGRA_NV21 = 97;
    public static final int CV_YUV2BGRA_UYNV = 112;
    public static final int CV_YUV2BGRA_UYVY = 112;
    public static final int CV_YUV2BGRA_Y422 = 112;
    public static final int CV_YUV2BGRA_YUNV = 120;
    public static final int CV_YUV2BGRA_YUY2 = 120;
    public static final int CV_YUV2BGRA_YUYV = 120;
    public static final int CV_YUV2BGRA_YV12 = 103;
    public static final int CV_YUV2BGRA_YVYU = 122;
    public static final int CV_YUV2BGR_I420 = 101;
    public static final int CV_YUV2BGR_IYUV = 101;
    public static final int CV_YUV2BGR_NV12 = 91;
    public static final int CV_YUV2BGR_NV21 = 93;
    public static final int CV_YUV2BGR_UYNV = 108;
    public static final int CV_YUV2BGR_UYVY = 108;
    public static final int CV_YUV2BGR_Y422 = 108;
    public static final int CV_YUV2BGR_YUNV = 116;
    public static final int CV_YUV2BGR_YUY2 = 116;
    public static final int CV_YUV2BGR_YUYV = 116;
    public static final int CV_YUV2BGR_YV12 = 99;
    public static final int CV_YUV2BGR_YVYU = 118;
    public static final int CV_YUV2GRAY_420 = 106;
    public static final int CV_YUV2GRAY_I420 = 106;
    public static final int CV_YUV2GRAY_IYUV = 106;
    public static final int CV_YUV2GRAY_NV12 = 106;
    public static final int CV_YUV2GRAY_NV21 = 106;
    public static final int CV_YUV2GRAY_UYNV = 123;
    public static final int CV_YUV2GRAY_UYVY = 123;
    public static final int CV_YUV2GRAY_Y422 = 123;
    public static final int CV_YUV2GRAY_YUNV = 124;
    public static final int CV_YUV2GRAY_YUY2 = 124;
    public static final int CV_YUV2GRAY_YUYV = 124;
    public static final int CV_YUV2GRAY_YV12 = 106;
    public static final int CV_YUV2GRAY_YVYU = 124;
    public static final int CV_YUV2RGB = 85;
    public static final int CV_YUV2RGBA_I420 = 104;
    public static final int CV_YUV2RGBA_IYUV = 104;
    public static final int CV_YUV2RGBA_NV12 = 94;
    public static final int CV_YUV2RGBA_NV21 = 96;
    public static final int CV_YUV2RGBA_UYNV = 111;
    public static final int CV_YUV2RGBA_UYVY = 111;
    public static final int CV_YUV2RGBA_Y422 = 111;
    public static final int CV_YUV2RGBA_YUNV = 119;
    public static final int CV_YUV2RGBA_YUY2 = 119;
    public static final int CV_YUV2RGBA_YUYV = 119;
    public static final int CV_YUV2RGBA_YV12 = 102;
    public static final int CV_YUV2RGBA_YVYU = 121;
    public static final int CV_YUV2RGB_I420 = 100;
    public static final int CV_YUV2RGB_IYUV = 100;
    public static final int CV_YUV2RGB_NV12 = 90;
    public static final int CV_YUV2RGB_NV21 = 92;
    public static final int CV_YUV2RGB_UYNV = 107;
    public static final int CV_YUV2RGB_UYVY = 107;
    public static final int CV_YUV2RGB_Y422 = 107;
    public static final int CV_YUV2RGB_YUNV = 115;
    public static final int CV_YUV2RGB_YUY2 = 115;
    public static final int CV_YUV2RGB_YUYV = 115;
    public static final int CV_YUV2RGB_YV12 = 98;
    public static final int CV_YUV2RGB_YVYU = 117;
    public static final int CV_YUV420p2BGR = 99;
    public static final int CV_YUV420p2BGRA = 103;
    public static final int CV_YUV420p2GRAY = 106;
    public static final int CV_YUV420p2RGB = 98;
    public static final int CV_YUV420p2RGBA = 102;
    public static final int CV_YUV420sp2BGR = 93;
    public static final int CV_YUV420sp2BGRA = 97;
    public static final int CV_YUV420sp2GRAY = 106;
    public static final int CV_YUV420sp2RGB = 92;
    public static final int CV_YUV420sp2RGBA = 96;
    public static final int CV_mRGBA2RGBA = 126;
    public static final int GC_BGD = 0;
    public static final int GC_EVAL = 2;
    public static final int GC_FGD = 1;
    public static final int GC_INIT_WITH_MASK = 1;
    public static final int GC_INIT_WITH_RECT = 0;
    public static final int GC_PR_BGD = 2;
    public static final int GC_PR_FGD = 3;
    public static final int GHT_POSITION = 0;
    public static final int GHT_ROTATION = 2;
    public static final int GHT_SCALE = 1;
    public static final int KERNEL_ASYMMETRICAL = 2;
    public static final int KERNEL_GENERAL = 0;
    public static final int KERNEL_INTEGER = 8;
    public static final int KERNEL_SMOOTH = 4;
    public static final int KERNEL_SYMMETRICAL = 1;
    public static final int MORPH_BLACKHAT = 6;
    public static final int MORPH_CLOSE = 3;
    public static final int MORPH_DILATE = 1;
    public static final int MORPH_ERODE = 0;
    public static final int MORPH_GRADIENT = 4;
    public static final int MORPH_OPEN = 2;
    public static final int MORPH_TOPHAT = 5;
    public static final int PROJ_SPHERICAL_EQRECT = 1;
    public static final int PROJ_SPHERICAL_ORTHO = 0;

    public static native void CV_INIT_3X3_DELTAS(int[] iArr, int i, int i2);

    @ByVal
    public static native CvSubdiv2DEdge CV_SUBDIV2D_NEXT_EDGE(@ByVal CvSubdiv2DEdge cvSubdiv2DEdge);

    @Namespace("cv")
    public static native void Canny(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, double d, double d2, int i, boolean z);

    @Namespace("cv")
    public static native float EMD(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i, @opencv_core.InputArray opencv_core.CvArr cvArr3, float[] fArr, @opencv_core.InputArray opencv_core.CvArr cvArr4);

    @Namespace("cv")
    public static native void GaussianBlur(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, @ByVal opencv_core.CvSize cvSize, double d, double d2, int i);

    @Namespace("cv")
    public static native void Laplacian(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i, int i2, double d, double d2, int i3);

    @Namespace("cv")
    public static native double PSNR(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2);

    @Namespace("cv")
    public static native void Scharr(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i, int i2, int i3, double d, double d2, int i4);

    @Namespace("cv")
    public static native void Sobel(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i, int i2, int i3, int i4, double d, double d2, int i5);

    @Namespace("cv")
    public static native void bilateralFilter(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i, double d, double d2, int i2);

    @Namespace("cv")
    public static native void blur(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, @ByVal opencv_core.CvSize cvSize, @ByVal opencv_core.CvPoint cvPoint, int i);

    @Namespace("cv")
    public static native int borderInterpolate(int i, int i2, int i3);

    @Namespace("cv")
    public static native void boxFilter(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i, @ByVal opencv_core.CvSize cvSize, @ByVal opencv_core.CvPoint cvPoint, @Cast({"bool"}) boolean z, int i2);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native FilterEngine createBoxFilter(int i, int i2, @ByVal opencv_core.CvSize cvSize, @ByVal opencv_core.CvPoint cvPoint, @Cast({"bool"}) boolean z, int i3);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native CLAHE createCLAHE(double d, @ByVal opencv_core.CvSize cvSize);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native FilterEngine createDerivFilter(int i, int i2, int i3, int i4, int i5, int i6);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native FilterEngine createGaussianFilter(int i, @ByVal opencv_core.CvSize cvSize, double d, double d2, int i2);

    @Namespace("cv")
    public static native void createHanningWindow(@opencv_core.OutputArray opencv_core.CvMat cvMat, @ByVal opencv_core.CvSize cvSize, int i);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native FilterEngine createLinearFilter(int i, int i2, @opencv_core.InputArray opencv_core.CvMat cvMat, @ByVal opencv_core.CvPoint cvPoint, double d, int i3, int i4, @ByVal opencv_core.CvScalar cvScalar);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native FilterEngine createMorphologyFilter(int i, int i2, @opencv_core.InputArray opencv_core.CvMat cvMat, @ByVal opencv_core.CvPoint cvPoint, int i3, int i4, @ByVal opencv_core.CvScalar cvScalar);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native FilterEngine createSeparableLinearFilter(int i, int i2, @opencv_core.InputArray opencv_core.CvMat cvMat, @opencv_core.InputArray opencv_core.CvMat cvMat2, @ByVal opencv_core.CvPoint cvPoint, double d, int i3, int i4, @ByVal opencv_core.CvScalar cvScalar);

    public static native opencv_core.CvMat cv2DRotationMatrix(@ByVal opencv_core.CvPoint2D32f cvPoint2D32f, double d, double d2, opencv_core.CvMat cvMat);

    public static native void cvAcc(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvArr cvArr3);

    public static native void cvAdaptiveThreshold(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, double d, int i, int i2, int i3, double d2);

    public static native opencv_core.CvSeq cvApproxChains(opencv_core.CvSeq cvSeq, opencv_core.CvMemStorage cvMemStorage, int i, double d, int i2, int i3);

    public static native opencv_core.CvSeq cvApproxPoly(Pointer pointer, int i, opencv_core.CvMemStorage cvMemStorage, int i2, double d, int i3);

    public static native double cvArcLength(Pointer pointer, @ByVal opencv_core.CvSlice cvSlice, int i);

    @ByVal
    public static native opencv_core.CvRect cvBoundingRect(opencv_core.CvArr cvArr, int i);

    public static native void cvBoxPoints(@ByVal opencv_core.CvBox2D cvBox2D, opencv_core.CvPoint2D32f cvPoint2D32f);

    public static native void cvBoxPoints(@ByVal opencv_core.CvBox2D cvBox2D, @Cast({"CvPoint2D32f*"}) float[] fArr);

    public static native void cvCalcArrBackProject(opencv_core.CvArrArray cvArrArray, opencv_core.CvArr cvArr, CvHistogram cvHistogram);

    public static native void cvCalcArrBackProjectPatch(opencv_core.CvArrArray cvArrArray, opencv_core.CvArr cvArr, @ByVal opencv_core.CvSize cvSize, CvHistogram cvHistogram, int i, double d);

    public static native void cvCalcArrHist(opencv_core.CvArrArray cvArrArray, CvHistogram cvHistogram, int i, opencv_core.CvArr cvArr);

    public static native void cvCalcBayesianProb(@ByPtrPtr CvHistogram cvHistogram, int i, @ByPtrPtr CvHistogram cvHistogram2);

    public static native float cvCalcEMD2(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i, CvDistanceFunction cvDistanceFunction, opencv_core.CvArr cvArr3, opencv_core.CvArr cvArr4, float[] fArr, Pointer pointer);

    public static native void cvCalcProbDensity(CvHistogram cvHistogram, CvHistogram cvHistogram2, CvHistogram cvHistogram3, double d);

    public static native void cvCanny(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, double d, double d2, int i);

    public static native int cvCheckContourConvexity(opencv_core.CvArr cvArr);

    public static native void cvClearHist(CvHistogram cvHistogram);

    public static native double cvCompareHist(CvHistogram cvHistogram, CvHistogram cvHistogram2, int i);

    public static native double cvContourArea(opencv_core.CvArr cvArr, @ByVal opencv_core.CvSlice cvSlice, int i);

    public static native void cvConvertMaps(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvArr cvArr3, opencv_core.CvArr cvArr4);

    public static native opencv_core.CvSeq cvConvexHull2(opencv_core.CvArr cvArr, Pointer pointer, int i, int i2);

    public static native opencv_core.CvSeq cvConvexityDefects(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvMemStorage cvMemStorage);

    public static native void cvCopyHist(CvHistogram cvHistogram, @ByPtrPtr CvHistogram cvHistogram2);

    public static native void cvCopyMakeBorder(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, @ByVal opencv_core.CvPoint cvPoint, int i, @ByVal opencv_core.CvScalar cvScalar);

    public static native void cvCornerEigenValsAndVecs(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i, int i2);

    public static native void cvCornerHarris(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i, int i2, double d);

    public static native void cvCornerMinEigenVal(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i, int i2);

    public static native CvHistogram cvCreateHist(int i, int[] iArr, int i2, @Cast({"float**"}) PointerPointer pointerPointer, int i3);

    public static native opencv_core.CvMatArray cvCreatePyramid(opencv_core.CvArr cvArr, int i, double d, opencv_core.CvSize cvSize, opencv_core.CvArr cvArr2, int i2, int i3);

    public static native IplConvKernel cvCreateStructuringElementEx(int i, int i2, int i3, int i4, int i5, int[] iArr);

    public static native void cvCvtColor(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i);

    public static native void cvDilate(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, IplConvKernel iplConvKernel, int i);

    public static native void cvDistTransform(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i, int i2, FloatPointer floatPointer, opencv_core.CvArr cvArr3, int i3);

    public static native opencv_core.CvSeq cvEndFindContours(CvContourScanner cvContourScanner);

    public static native void cvEqualizeHist(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2);

    public static native void cvErode(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, IplConvKernel iplConvKernel, int i);

    public static native void cvFilter2D(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvMat cvMat, @ByVal opencv_core.CvPoint cvPoint);

    public static native int cvFindContours(opencv_core.CvArr cvArr, opencv_core.CvMemStorage cvMemStorage, @ByPtrPtr opencv_core.CvSeq cvSeq, int i, int i2, int i3, @ByVal opencv_core.CvPoint cvPoint);

    public static native void cvFindCornerSubPix(opencv_core.CvArr cvArr, opencv_core.CvPoint2D32f cvPoint2D32f, int i, @ByVal opencv_core.CvSize cvSize, @ByVal opencv_core.CvSize cvSize2, @ByVal opencv_core.CvTermCriteria cvTermCriteria);

    public static native opencv_core.CvSeq cvFindNextContour(@ByVal CvContourScanner cvContourScanner);

    @ByVal
    public static native opencv_core.CvBox2D cvFitEllipse2(opencv_core.CvArr cvArr);

    public static native void cvFitLine(opencv_core.CvArr cvArr, int i, double d, double d2, double d3, FloatPointer floatPointer);

    public static native void cvFitLine(opencv_core.CvArr cvArr, int i, double d, double d2, double d3, float[] fArr);

    public static native void cvFloodFill(opencv_core.CvArr cvArr, @ByVal opencv_core.CvPoint cvPoint, @ByVal opencv_core.CvScalar cvScalar, @ByVal opencv_core.CvScalar cvScalar2, @ByVal opencv_core.CvScalar cvScalar3, CvConnectedComp cvConnectedComp, int i, opencv_core.CvArr cvArr2);

    public static native opencv_core.CvMat cvGetAffineTransform(opencv_core.CvPoint2D32f cvPoint2D32f, opencv_core.CvPoint2D32f cvPoint2D32f2, opencv_core.CvMat cvMat);

    public static native opencv_core.CvMat cvGetAffineTransform(@Cast({"CvPoint2D32f*"}) float[] fArr, @Cast({"CvPoint2D32f*"}) float[] fArr2, opencv_core.CvMat cvMat);

    public static native double cvGetCentralMoment(CvMoments cvMoments, int i, int i2);

    public static native void cvGetHuMoments(CvMoments cvMoments, CvHuMoments cvHuMoments);

    public static native void cvGetMinMaxHistValue(CvHistogram cvHistogram, float[] fArr, float[] fArr2, int[] iArr, int[] iArr2);

    public static native double cvGetNormalizedCentralMoment(CvMoments cvMoments, int i, int i2);

    public static native opencv_core.CvMat cvGetPerspectiveTransform(opencv_core.CvPoint2D32f cvPoint2D32f, opencv_core.CvPoint2D32f cvPoint2D32f2, opencv_core.CvMat cvMat);

    public static native opencv_core.CvMat cvGetPerspectiveTransform(@Cast({"CvPoint2D32f*"}) float[] fArr, @Cast({"CvPoint2D32f*"}) float[] fArr2, opencv_core.CvMat cvMat);

    public static native void cvGetQuadrangleSubPix(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvMat cvMat);

    public static native void cvGetRectSubPix(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, @ByVal opencv_core.CvPoint2D32f cvPoint2D32f);

    public static native double cvGetSpatialMoment(CvMoments cvMoments, int i, int i2);

    public static native void cvGoodFeaturesToTrack(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvArr cvArr3, opencv_core.CvPoint2D32f cvPoint2D32f, int[] iArr, double d, double d2, opencv_core.CvArr cvArr4, int i, int i2, double d3);

    public static native opencv_core.CvSeq cvHoughCircles(opencv_core.CvArr cvArr, Pointer pointer, int i, double d, double d2, double d3, double d4, int i2, int i3);

    public static native opencv_core.CvSeq cvHoughLines2(opencv_core.CvArr cvArr, Pointer pointer, int i, double d, double d2, int i2, double d3, double d4);

    public static native void cvInitUndistortMap(opencv_core.CvMat cvMat, opencv_core.CvMat cvMat2, opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2);

    public static native void cvInitUndistortRectifyMap(opencv_core.CvMat cvMat, opencv_core.CvMat cvMat2, opencv_core.CvMat cvMat3, opencv_core.CvMat cvMat4, opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2);

    public static native void cvIntegral(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvArr cvArr3, opencv_core.CvArr cvArr4);

    public static native void cvLaplace(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i);

    public static native void cvLinearPolar(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, @ByVal opencv_core.CvPoint2D32f cvPoint2D32f, double d, int i);

    public static native void cvLogPolar(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, @ByVal opencv_core.CvPoint2D32f cvPoint2D32f, double d, int i);

    public static native CvHistogram cvMakeHistHeaderForArray(int i, int[] iArr, CvHistogram cvHistogram, FloatPointer floatPointer, @Cast({"float**"}) PointerPointer pointerPointer, int i2);

    public static native CvHistogram cvMakeHistHeaderForArray(int i, int[] iArr, CvHistogram cvHistogram, float[] fArr, @Cast({"float**"}) PointerPointer pointerPointer, int i2);

    public static native double cvMatchShapes(Pointer pointer, Pointer pointer2, int i, double d);

    public static native void cvMatchTemplate(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvArr cvArr3, int i);

    @ByVal
    public static native opencv_core.CvRect cvMaxRect(opencv_core.CvRect cvRect, opencv_core.CvRect cvRect2);

    @ByVal
    public static native opencv_core.CvBox2D cvMinAreaRect2(opencv_core.CvArr cvArr, opencv_core.CvMemStorage cvMemStorage);

    public static native int cvMinEnclosingCircle(opencv_core.CvArr cvArr, opencv_core.CvPoint2D32f cvPoint2D32f, float[] fArr);

    public static native int cvMinEnclosingCircle(opencv_core.CvArr cvArr, @Cast({"CvPoint2D32f*"}) float[] fArr, float[] fArr2);

    public static native void cvMoments(opencv_core.CvArr cvArr, CvMoments cvMoments, int i);

    public static native void cvMorphologyEx(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvArr cvArr3, IplConvKernel iplConvKernel, int i, int i2);

    public static native void cvMultiplyAcc(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvArr cvArr3, opencv_core.CvArr cvArr4);

    public static native void cvNormalizeHist(CvHistogram cvHistogram, double d);

    public static native double cvPointPolygonTest(opencv_core.CvArr cvArr, @ByVal opencv_core.CvPoint2D32f cvPoint2D32f, int i);

    public static native opencv_core.CvSeq cvPointSeqFromMat(int i, opencv_core.CvArr cvArr, opencv_core.CvContour cvContour, opencv_core.CvSeqBlock cvSeqBlock);

    public static native void cvPreCornerDetect(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i);

    public static native void cvPyrDown(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i);

    public static native void cvPyrMeanShiftFiltering(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, double d, double d2, int i, @ByVal opencv_core.CvTermCriteria cvTermCriteria);

    public static native void cvPyrUp(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i);

    @ByVal
    public static native opencv_core.CvPoint cvReadChainPoint(CvChainPtReader cvChainPtReader);

    public static native void cvReleaseHist(@ByPtrPtr CvHistogram cvHistogram);

    public static native void cvReleasePyramid(@ByPtrPtr opencv_core.CvMatArray cvMatArray, int i);

    public static native void cvReleaseStructuringElement(@ByPtrPtr IplConvKernel iplConvKernel);

    public static native void cvRemap(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvArr cvArr3, opencv_core.CvArr cvArr4, int i, @ByVal opencv_core.CvScalar cvScalar);

    public static native void cvResize(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i);

    public static native void cvRunningAvg(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, double d, opencv_core.CvArr cvArr3);

    public static native int cvSampleLine(opencv_core.CvArr cvArr, @ByVal opencv_core.CvPoint cvPoint, @ByVal opencv_core.CvPoint cvPoint2, Pointer pointer, int i);

    public static native void cvSetHistBinRanges(CvHistogram cvHistogram, @Cast({"float**"}) PointerPointer pointerPointer, int i);

    public static native void cvSmooth(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i, int i2, int i3, double d, double d2);

    public static native void cvSobel(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, int i, int i2, int i3);

    public static native void cvSquareAcc(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvArr cvArr3);

    @ByVal
    public static native CvContourScanner cvStartFindContours(opencv_core.CvArr cvArr, opencv_core.CvMemStorage cvMemStorage, int i, int i2, int i3, @ByVal opencv_core.CvPoint cvPoint);

    public static native void cvStartReadChainPoints(opencv_core.CvChain cvChain, CvChainPtReader cvChainPtReader);

    public static native void cvSubstituteContour(@ByVal CvContourScanner cvContourScanner, opencv_core.CvSeq cvSeq);

    public static native void cvThreshHist(CvHistogram cvHistogram, double d);

    public static native double cvThreshold(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, double d, double d2, int i);

    public static native void cvUndistort2(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvMat cvMat, opencv_core.CvMat cvMat2, opencv_core.CvMat cvMat3);

    public static native void cvUndistortPoints(opencv_core.CvMat cvMat, opencv_core.CvMat cvMat2, opencv_core.CvMat cvMat3, opencv_core.CvMat cvMat4, opencv_core.CvMat cvMat5, opencv_core.CvMat cvMat6);

    public static native void cvWarpAffine(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvMat cvMat, int i, @ByVal opencv_core.CvScalar cvScalar);

    public static native void cvWarpPerspective(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2, opencv_core.CvMat cvMat, int i, @ByVal opencv_core.CvScalar cvScalar);

    public static native void cvWatershed(opencv_core.CvArr cvArr, opencv_core.CvArr cvArr2);

    @Namespace("cv")
    public static native void eigen2x2(FloatPointer floatPointer, FloatPointer floatPointer2, int i);

    @Namespace("cv")
    public static native void eigen2x2(float[] fArr, float[] fArr2, int i);

    @Namespace("cv")
    public static native void filter2D(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i, @opencv_core.InputArray opencv_core.CvMat cvMat, @ByVal opencv_core.CvPoint cvPoint, double d, int i2);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native BaseColumnFilter getColumnSumFilter(int i, int i2, int i3, int i4, double d);

    @Namespace("cv")
    public static native void getDerivKernels(@opencv_core.OutputArray opencv_core.CvMat cvMat, @opencv_core.OutputArray opencv_core.CvMat cvMat2, int i, int i2, int i3, @Cast({"bool"}) boolean z, int i4);

    @opencv_core.OutputMat
    @Namespace("cv")
    public static native opencv_core.CvMat getGaborKernel(@ByVal opencv_core.CvSize cvSize, double d, double d2, double d3, double d4, double d5, int i);

    @opencv_core.OutputMat
    @Namespace("cv")
    public static native opencv_core.CvMat getGaussianKernel(int i, double d, int i2);

    @Namespace("cv")
    public static native int getKernelType(@opencv_core.InputArray opencv_core.CvMat cvMat, @ByVal opencv_core.CvPoint cvPoint);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native BaseColumnFilter getLinearColumnFilter(int i, int i2, @opencv_core.InputArray opencv_core.CvMat cvMat, int i3, int i4, double d, int i5);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native BaseFilter getLinearFilter(int i, int i2, @opencv_core.InputArray opencv_core.CvMat cvMat, @ByVal opencv_core.CvPoint cvPoint, double d, int i3);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native BaseRowFilter getLinearRowFilter(int i, int i2, @opencv_core.InputArray opencv_core.CvMat cvMat, int i3, int i4);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native BaseColumnFilter getMorphologyColumnFilter(int i, int i2, int i3, int i4);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native BaseFilter getMorphologyFilter(int i, int i2, @opencv_core.InputArray opencv_core.CvMat cvMat, @ByVal opencv_core.CvPoint cvPoint);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native BaseRowFilter getMorphologyRowFilter(int i, int i2, int i3, int i4);

    @opencv_core.Ptr
    @Namespace("cv")
    public static native BaseRowFilter getRowSumFilter(int i, int i2, int i3, int i4);

    @Namespace("cv")
    public static native void grabCut(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, @ByVal opencv_core.CvRect cvRect, @opencv_core.OutputArray opencv_core.CvMat cvMat, @opencv_core.OutputArray opencv_core.CvMat cvMat2, int i, int i2);

    @Namespace("cv")
    public static native float initWideAngleProjMap(@opencv_core.InputArray opencv_core.CvMat cvMat, @opencv_core.InputArray opencv_core.CvMat cvMat2, @ByVal opencv_core.CvSize cvSize, int i, int i2, @opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i3, double d);

    @Namespace("cv")
    public static native float intersectConvexConvex(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, @opencv_core.OutputArray opencv_core.CvMat cvMat, @Cast({"bool"}) boolean z);

    @Cast({"bool"})
    @Namespace("cv")
    public static native boolean isContourConvex(@opencv_core.InputArray opencv_core.CvArr cvArr);

    @Namespace("cv")
    public static native void medianBlur(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i);

    @ByVal
    @Namespace("cv")
    public static native opencv_core.CvScalar morphologyDefaultBorderValue();

    @Adapter("Point2dAdapter")
    @Namespace("cv")
    public static native opencv_core.CvPoint2D64f phaseCorrelate(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, @opencv_core.InputArray opencv_core.CvArr cvArr3);

    @Adapter("Point2dAdapter")
    @Namespace("cv")
    public static native opencv_core.CvPoint2D64f phaseCorrelateRes(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, @opencv_core.InputArray opencv_core.CvArr cvArr3, double[] dArr);

    @Namespace("cv")
    public static native void sepFilter2D(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, int i, @opencv_core.InputArray opencv_core.CvMat cvMat, @opencv_core.InputArray opencv_core.CvMat cvMat2, @ByVal opencv_core.CvPoint cvPoint, double d, int i2);

    static {
        Loader.load();
    }

    public static void tryLoad() {
    }

    public static class CvConnectedComp extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native double area();

        public native CvConnectedComp area(double d);

        public native opencv_core.CvSeq contour();

        public native CvConnectedComp contour(opencv_core.CvSeq cvSeq);

        @ByRef
        public native opencv_core.CvRect rect();

        public native CvConnectedComp rect(opencv_core.CvRect cvRect);

        @ByRef
        public native opencv_core.CvScalar value();

        public native CvConnectedComp value(opencv_core.CvScalar cvScalar);

        static {
            Loader.load();
        }

        public CvConnectedComp() {
            allocate();
        }

        public CvConnectedComp(int size) {
            allocateArray(size);
        }

        public CvConnectedComp(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvConnectedComp position(int position) {
            return (CvConnectedComp) super.position(position);
        }
    }

    public static class CvMoments extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native double inv_sqrt_m00();

        public native CvMoments inv_sqrt_m00(double d);

        public native double m00();

        public native CvMoments m00(double d);

        public native double m01();

        public native CvMoments m01(double d);

        public native double m02();

        public native CvMoments m02(double d);

        public native double m03();

        public native CvMoments m03(double d);

        public native double m10();

        public native CvMoments m10(double d);

        public native double m11();

        public native CvMoments m11(double d);

        public native double m12();

        public native CvMoments m12(double d);

        public native double m20();

        public native CvMoments m20(double d);

        public native double m21();

        public native CvMoments m21(double d);

        public native double m30();

        public native CvMoments m30(double d);

        public native double mu02();

        public native CvMoments mu02(double d);

        public native double mu03();

        public native CvMoments mu03(double d);

        public native double mu11();

        public native CvMoments mu11(double d);

        public native double mu12();

        public native CvMoments mu12(double d);

        public native double mu20();

        public native CvMoments mu20(double d);

        public native double mu21();

        public native CvMoments mu21(double d);

        public native double mu30();

        public native CvMoments mu30(double d);

        static {
            Loader.load();
        }

        public CvMoments() {
            allocate();
        }

        public CvMoments(int size) {
            allocateArray(size);
        }

        public CvMoments(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvMoments position(int position) {
            return (CvMoments) super.position(position);
        }

        public static ThreadLocal<CvMoments> createThreadLocal() {
            return new ThreadLocal<CvMoments>() { // from class: com.googlecode.javacv.cpp.opencv_imgproc.CvMoments.1
                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.lang.ThreadLocal
                public CvMoments initialValue() {
                    return new CvMoments();
                }
            };
        }
    }

    public static class CvHuMoments extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native double hu1();

        public native CvHuMoments hu1(double d);

        public native double hu2();

        public native CvHuMoments hu2(double d);

        public native double hu3();

        public native CvHuMoments hu3(double d);

        public native double hu4();

        public native CvHuMoments hu4(double d);

        public native double hu5();

        public native CvHuMoments hu5(double d);

        public native double hu6();

        public native CvHuMoments hu6(double d);

        public native double hu7();

        public native CvHuMoments hu7(double d);

        static {
            Loader.load();
        }

        public CvHuMoments() {
            allocate();
        }

        public CvHuMoments(int size) {
            allocateArray(size);
        }

        public CvHuMoments(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvHuMoments position(int position) {
            return (CvHuMoments) super.position(position);
        }
    }

    public static class CvDistanceFunction extends FunctionPointer {
        private native void allocate();

        public native float call(@Const FloatPointer floatPointer, @Const FloatPointer floatPointer2, Pointer pointer);

        static {
            Loader.load();
        }

        public CvDistanceFunction(Pointer p) {
            super(p);
        }

        protected CvDistanceFunction() {
            allocate();
        }
    }

    @Opaque
    public static class CvContourScanner extends Pointer {
        static {
            Loader.load();
        }

        public CvContourScanner() {
        }

        public CvContourScanner(Pointer p) {
            super(p);
        }
    }

    public static class CvChainPtReader extends opencv_core.CvSeqReader {
        private native void allocate();

        private native void allocateArray(int i);

        public native byte code();

        public native CvChainPtReader code(byte b);

        public native byte deltas(int i, int i2);

        public native CvChainPtReader deltas(int i, int i2, byte b);

        @ByRef
        public native opencv_core.CvPoint pt();

        public native CvChainPtReader pt(opencv_core.CvPoint cvPoint);

        static {
            Loader.load();
        }

        public CvChainPtReader() {
            allocate();
        }

        public CvChainPtReader(int size) {
            allocateArray(size);
        }

        public CvChainPtReader(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvSeqReader, com.googlecode.javacpp.Pointer
        public CvChainPtReader position(int position) {
            return (CvChainPtReader) super.position(position);
        }
    }

    public static class CvSubdiv2DEdge extends SizeTPointer {
        static {
            Loader.load();
        }

        public CvSubdiv2DEdge() {
            super(1);
        }

        public CvSubdiv2DEdge(int size) {
            super(size);
        }

        public CvSubdiv2DEdge(Pointer p) {
            super(p);
        }
    }

    public static class CvQuadEdge2D extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int flags();

        public native CvQuadEdge2D flags(int i);

        @Cast({"CvSubdiv2DEdge"})
        public native long next(int i);

        public native CvQuadEdge2D next(int i, long j);

        public native CvQuadEdge2D pt(int i, CvSubdiv2DPoint cvSubdiv2DPoint);

        public native CvSubdiv2DPoint pt(int i);

        static {
            Loader.load();
        }

        public CvQuadEdge2D() {
            allocate();
        }

        public CvQuadEdge2D(int size) {
            allocateArray(size);
        }

        public CvQuadEdge2D(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvQuadEdge2D position(int position) {
            return (CvQuadEdge2D) super.position(position);
        }

        @Cast({"CvSubdiv2DEdge"})
        public long CV_SUBDIV2D_NEXT_EDGE(@Cast({"CvSubdiv2DEdge"}) long edge) {
            return next(((int) edge) & 3);
        }

        @Cast({"CvSubdiv2DEdge"})
        public long cvSubdiv2DNextEdge(@Cast({"CvSubdiv2DEdge"}) long edge) {
            return CV_SUBDIV2D_NEXT_EDGE(edge);
        }

        @Cast({"CvSubdiv2DEdge"})
        public long cvSubdiv2DGetEdge(@Cast({"CvSubdiv2DEdge"}) long edge, @Cast({"CvNextEdgeType"}) int type) {
            long edge2 = next((((int) edge) + type) & 3);
            return ((-4) & edge2) + (((type >> 4) + edge2) & 3);
        }

        @Cast({"CvSubdiv2DEdge"})
        public static long cvSubdiv2DRotateEdge(@Cast({"CvSubdiv2DEdge"}) long edge, int rotate) {
            return ((-4) & edge) + ((rotate + edge) & 3);
        }

        public CvSubdiv2DPoint cvSubdiv2DEdgeOrg(@Cast({"CvSubdiv2DEdge"}) long edge) {
            return pt(((int) edge) & 3);
        }

        public CvSubdiv2DPoint cvSubdiv2DEdgeDst(@Cast({"CvSubdiv2DEdge"}) long edge) {
            return pt((((int) edge) + 2) & 3);
        }

        @Cast({"CvSubdiv2DEdge"})
        public static long cvSubdiv2DSymEdge(@Cast({"CvSubdiv2DEdge"}) long edge) {
            return 2 ^ edge;
        }
    }

    public static class CvSubdiv2DPoint extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        @Cast({"CvSubdiv2DEdge"})
        public native long first();

        public native CvSubdiv2DPoint first(long j);

        public native int flags();

        public native CvSubdiv2DPoint flags(int i);

        public native int id();

        public native CvSubdiv2DPoint id(int i);

        @ByRef
        public native opencv_core.CvPoint2D32f pt();

        public native CvSubdiv2DPoint pt(opencv_core.CvPoint2D32f cvPoint2D32f);

        static {
            Loader.load();
        }

        public CvSubdiv2DPoint() {
            allocate();
        }

        public CvSubdiv2DPoint(int size) {
            allocateArray(size);
        }

        public CvSubdiv2DPoint(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvSubdiv2DPoint position(int position) {
            return (CvSubdiv2DPoint) super.position(position);
        }
    }

    public static class CvSubdiv2D extends opencv_core.CvGraph {
        private native void allocate();

        private native void allocateArray(int i);

        @ByRef
        public native opencv_core.CvPoint2D32f bottomright();

        public native CvSubdiv2D bottomright(opencv_core.CvPoint2D32f cvPoint2D32f);

        public native int is_geometry_valid();

        public native CvSubdiv2D is_geometry_valid(int i);

        public native int quad_edges();

        public native CvSubdiv2D quad_edges(int i);

        @Cast({"CvSubdiv2DEdge"})
        public native long recent_edge();

        public native CvSubdiv2D recent_edge(long j);

        @ByRef
        public native opencv_core.CvPoint2D32f topleft();

        public native CvSubdiv2D topleft(opencv_core.CvPoint2D32f cvPoint2D32f);

        static {
            Loader.load();
        }

        public CvSubdiv2D() {
            allocate();
        }

        public CvSubdiv2D(int size) {
            allocateArray(size);
        }

        public CvSubdiv2D(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacv.cpp.opencv_core.CvGraph, com.googlecode.javacv.cpp.opencv_core.CvSet, com.googlecode.javacv.cpp.opencv_core.CvSeq, com.googlecode.javacpp.Pointer
        public CvSubdiv2D position(int position) {
            return (CvSubdiv2D) super.position(position);
        }
    }

    public static class CvConvexityDefect extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native float depth();

        public native CvConvexityDefect depth(float f);

        public native opencv_core.CvPoint depth_point();

        public native CvConvexityDefect depth_point(opencv_core.CvPoint cvPoint);

        public native opencv_core.CvPoint end();

        public native CvConvexityDefect end(opencv_core.CvPoint cvPoint);

        public native opencv_core.CvPoint start();

        public native CvConvexityDefect start(opencv_core.CvPoint cvPoint);

        static {
            Loader.load();
        }

        public CvConvexityDefect() {
            allocate();
        }

        public CvConvexityDefect(int size) {
            allocateArray(size);
        }

        public CvConvexityDefect(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvConvexityDefect position(int position) {
            return (CvConvexityDefect) super.position(position);
        }
    }

    @Opaque
    public static class CvFeatureTree extends Pointer {
        static {
            Loader.load();
        }

        public CvFeatureTree() {
        }

        public CvFeatureTree(Pointer p) {
            super(p);
        }
    }

    public static void cvSmooth(opencv_core.CvArr src, opencv_core.CvArr dst, int smoothtype, int size1) {
        cvSmooth(src, dst, smoothtype, size1, 0, 0.0d, 0.0d);
    }

    public static void cvResize(opencv_core.CvArr src, opencv_core.CvArr dst) {
        cvResize(src, dst, 1);
    }

    public static void cvWarpAffine(opencv_core.CvArr src, opencv_core.CvArr dst, opencv_core.CvMat map_matrix) {
        cvWarpAffine(src, dst, map_matrix, 9, opencv_core.CvScalar.ZERO);
    }

    public static void cvWarpPerspective(opencv_core.CvArr src, opencv_core.CvArr dst, opencv_core.CvMat map_matrix) {
        cvWarpPerspective(src, dst, map_matrix, 9, opencv_core.CvScalar.ZERO);
    }

    public static void cvUndistort2(opencv_core.CvArr src, opencv_core.CvArr dst, opencv_core.CvMat intrinsic_matrix, opencv_core.CvMat distortion_coeffs) {
        cvUndistort2(src, dst, intrinsic_matrix, distortion_coeffs, null);
    }

    public static class IplConvKernel extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int anchorX();

        public native IplConvKernel anchorX(int i);

        public native int anchorY();

        public native IplConvKernel anchorY(int i);

        public native int nCols();

        public native IplConvKernel nCols(int i);

        public native int nRows();

        public native IplConvKernel nRows(int i);

        public native int nShiftR();

        public native IplConvKernel nShiftR(int i);

        public native IntPointer values();

        public native IplConvKernel values(IntPointer intPointer);

        static {
            Loader.load();
        }

        public IplConvKernel() {
            allocate();
            zero();
        }

        public IplConvKernel(int size) {
            allocateArray(size);
            zero();
        }

        public IplConvKernel(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public IplConvKernel position(int position) {
            return (IplConvKernel) super.position(position);
        }

        public static IplConvKernel create(int cols, int rows, int anchor_x, int anchor_y, int shape, int[] values) {
            IplConvKernel p = opencv_imgproc.cvCreateStructuringElementEx(cols, rows, anchor_x, anchor_y, shape, values);
            if (p != null) {
                p.deallocator(new ReleaseDeallocator(p));
            }
            return p;
        }

        public void release() {
            deallocate();
        }

        static class ReleaseDeallocator extends IplConvKernel implements Pointer.Deallocator {
            @Override // com.googlecode.javacv.cpp.opencv_imgproc.IplConvKernel, com.googlecode.javacpp.Pointer
            public /* bridge */ /* synthetic */ Pointer position(int i) {
                return super.position(i);
            }

            ReleaseDeallocator(IplConvKernel p) {
                super(p);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_imgproc.cvReleaseStructuringElement(this);
            }
        }
    }

    public static class IplConvKernelFP extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native int anchorX();

        public native IplConvKernelFP anchorX(int i);

        public native int anchorY();

        public native IplConvKernelFP anchorY(int i);

        public native int nCols();

        public native IplConvKernelFP nCols(int i);

        public native int nRows();

        public native IplConvKernelFP nRows(int i);

        public native FloatPointer values();

        public native IplConvKernelFP values(FloatPointer floatPointer);

        static {
            Loader.load();
        }

        public IplConvKernelFP() {
            allocate();
            zero();
        }

        public IplConvKernelFP(int size) {
            allocateArray(size);
            zero();
        }

        public IplConvKernelFP(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public IplConvKernelFP position(int position) {
            return (IplConvKernelFP) super.position(position);
        }
    }

    public static int cvFindContours(opencv_core.CvArr image, opencv_core.CvMemStorage storage, @ByPtrPtr opencv_core.CvSeq first_contour, int header_size, int mode, int method) {
        return cvFindContours(image, storage, first_contour, header_size, mode, method, opencv_core.CvPoint.ZERO);
    }

    public static CvContourScanner cvStartFindContours(opencv_core.CvArr image, opencv_core.CvMemStorage storage, int header_size, int mode, int method) {
        return cvStartFindContours(image, storage, header_size, mode, method, opencv_core.CvPoint.ZERO);
    }

    public static double cvContourPerimeter(Pointer contour) {
        return cvArcLength(contour, opencv_core.CV_WHOLE_SEQ, 1);
    }

    public static class CvHistogram extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native opencv_core.CvArr bins();

        public native CvHistogram bins(opencv_core.CvArr cvArr);

        @ByRef
        public native opencv_core.CvMatND mat();

        public native CvHistogram mat(opencv_core.CvMatND cvMatND);

        public native float thresh(int i, int i2);

        public native CvHistogram thresh(int i, int i2, float f);

        @Cast({"float**"})
        public native PointerPointer thresh2();

        public native CvHistogram thresh2(PointerPointer pointerPointer);

        @Cast({"CvHistType"})
        public native int type();

        public native CvHistogram type(int i);

        static {
            Loader.load();
        }

        public CvHistogram() {
            allocate();
            zero();
        }

        public CvHistogram(int size) {
            allocateArray(size);
            zero();
        }

        public CvHistogram(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public CvHistogram position(int position) {
            return (CvHistogram) super.position(position);
        }

        public static CvHistogram create(int dims, int[] sizes, int type, float[][] ranges, int uniform) {
            CvHistogram h = opencv_imgproc.cvCreateHist(dims, sizes, type, ranges, uniform);
            if (h != null) {
                h.deallocator(new ReleaseDeallocator(h));
            }
            return h;
        }

        public void release() {
            deallocate();
        }

        static class ReleaseDeallocator extends CvHistogram implements Pointer.Deallocator {
            @Override // com.googlecode.javacv.cpp.opencv_imgproc.CvHistogram, com.googlecode.javacpp.Pointer
            public /* bridge */ /* synthetic */ Pointer position(int i) {
                return super.position(i);
            }

            ReleaseDeallocator(CvHistogram p) {
                super(p);
            }

            @Override // com.googlecode.javacpp.Pointer, com.googlecode.javacpp.Pointer.Deallocator
            public void deallocate() {
                opencv_imgproc.cvReleaseHist(this);
            }
        }
    }

    public static boolean CV_IS_HIST(opencv_core.CvArr hist) {
        CvHistogram h = new CvHistogram(hist);
        return (hist == null || (h.type() & (-65536)) != 1111818240 || h.bins() == null) ? false : true;
    }

    public static boolean CV_IS_UNIFORM_HIST(CvHistogram hist) {
        return (hist.type() & 1024) != 0;
    }

    public static boolean CV_IS_SPARSE_HIST(CvHistogram hist) {
        return opencv_core.CV_IS_SPARSE_MAT(hist.bins());
    }

    public static boolean CV_HIST_HAS_RANGES(CvHistogram hist) {
        return (hist.type() & 2048) != 0;
    }

    public static CvHistogram cvCreateHist(int dims, int[] sizes, int type, float[][] ranges, int uniform) {
        return cvCreateHist(dims, sizes, type, ranges == null ? null : new PointerPointer(ranges), uniform);
    }

    public static void cvSetHistBinRanges(CvHistogram hist, float[][] ranges, int uniform) {
        cvSetHistBinRanges(hist, ranges == null ? null : new PointerPointer(ranges), uniform);
    }

    public static CvHistogram cvMakeHistHeaderForArray(int dims, int[] sizes, CvHistogram hist, float[] data, float[][] ranges, int uniform) {
        return cvMakeHistHeaderForArray(dims, sizes, hist, data, ranges == null ? null : new PointerPointer(ranges), uniform);
    }

    public static CvHistogram cvMakeHistHeaderForArray(int dims, int[] sizes, CvHistogram hist, FloatPointer data, float[][] ranges, int uniform) {
        return cvMakeHistHeaderForArray(dims, sizes, hist, data, ranges == null ? null : new PointerPointer(ranges), uniform);
    }

    public static void cvCalcArrHist(opencv_core.CvArr[] arr, CvHistogram hist, int accumulate, opencv_core.CvArr mask) {
        cvCalcArrHist(new opencv_core.CvArrArray(arr), hist, accumulate, mask);
    }

    public static void cvCalcHist(opencv_core.IplImage[] arr, CvHistogram hist, int accumulate, opencv_core.CvArr mask) {
        cvCalcHist(new opencv_core.IplImageArray(arr), hist, accumulate, mask);
    }

    public static void cvCalcHist(opencv_core.IplImageArray arr, CvHistogram hist, int accumulate, opencv_core.CvArr mask) {
        cvCalcArrHist(arr, hist, accumulate, mask);
    }

    public static void cvCalcArrBackProject(opencv_core.CvArr[] image, opencv_core.CvArr dst, CvHistogram hist) {
        cvCalcArrBackProject(new opencv_core.CvArrArray(image), dst, hist);
    }

    public static void cvCalcBackProject(opencv_core.IplImage[] image, opencv_core.CvArr dst, CvHistogram hist) {
        cvCalcBackProject(new opencv_core.IplImageArray(image), dst, hist);
    }

    public static void cvCalcBackProject(opencv_core.IplImageArray image, opencv_core.CvArr dst, CvHistogram hist) {
        cvCalcArrBackProject(image, dst, hist);
    }

    public static void cvCalcArrBackProjectPatch(opencv_core.CvArr[] image, opencv_core.CvArr dst, @ByVal opencv_core.CvSize range, CvHistogram hist, int method, double factor) {
        cvCalcArrBackProjectPatch(new opencv_core.CvArrArray(image), dst, range, hist, method, factor);
    }

    public static void cvCalcBackProjectPatch(opencv_core.IplImage[] image, opencv_core.CvArr dst, @ByVal opencv_core.CvSize range, CvHistogram hist, int method, double factor) {
        cvCalcBackProjectPatch(new opencv_core.IplImageArray(image), dst, range, hist, method, factor);
    }

    public static void cvCalcBackProjectPatch(opencv_core.IplImageArray image, opencv_core.CvArr dst, opencv_core.CvSize range, CvHistogram hist, int method, double factor) {
        cvCalcArrBackProjectPatch(image, dst, range, hist, method, factor);
    }

    @NoOffset
    @Namespace("cv")
    public static class BaseRowFilter extends Pointer {
        public native int anchor();

        public native BaseRowFilter anchor(int i);

        @Name({"operator()"})
        public native void filter(@Cast({"uchar*"}) BytePointer bytePointer, @Cast({"uchar*"}) BytePointer bytePointer2, int i, int i2);

        public native int ksize();

        public native BaseRowFilter ksize(int i);

        static {
            Loader.load();
        }

        public BaseRowFilter() {
        }

        public BaseRowFilter(Pointer p) {
            super(p);
        }
    }

    @NoOffset
    @Namespace("cv")
    public static class BaseColumnFilter extends Pointer {
        public native int anchor();

        public native BaseColumnFilter anchor(int i);

        @Name({"operator()"})
        public native void filter(@Cast({"const uchar**"}) PointerPointer pointerPointer, @Cast({"uchar*"}) BytePointer bytePointer, int i, int i2, int i3);

        public native int ksize();

        public native BaseColumnFilter ksize(int i);

        public native void reset();

        static {
            Loader.load();
        }

        public BaseColumnFilter() {
        }

        public BaseColumnFilter(Pointer p) {
            super(p);
        }
    }

    @NoOffset
    @Namespace("cv")
    public static class BaseFilter extends Pointer {
        @ByVal
        public native opencv_core.CvPoint anchor();

        public native BaseFilter anchor(opencv_core.CvPoint cvPoint);

        @Name({"operator()"})
        public native void filter(@Cast({"const uchar**"}) PointerPointer pointerPointer, @Cast({"uchar*"}) BytePointer bytePointer, int i, int i2, int i3, int i4);

        @ByVal
        public native opencv_core.CvSize ksize();

        public native BaseFilter ksize(opencv_core.CvSize cvSize);

        public native void reset();

        static {
            Loader.load();
        }

        public BaseFilter() {
        }

        public BaseFilter(Pointer p) {
            super(p);
        }
    }

    @NoOffset
    @Namespace("cv")
    public static class FilterEngine extends Pointer {
        private native void allocate();

        private native void allocate(@opencv_core.Ptr BaseFilter baseFilter, @opencv_core.Ptr BaseRowFilter baseRowFilter, @opencv_core.Ptr BaseColumnFilter baseColumnFilter, int i, int i2, int i3, int i4, int i5, @ByVal opencv_core.CvScalar cvScalar);

        @ByVal
        public native opencv_core.CvPoint anchor();

        public native FilterEngine anchor(opencv_core.CvPoint cvPoint);

        public native void apply(@opencv_core.InputMat opencv_core.CvArr cvArr, @opencv_core.InputMat opencv_core.CvArr cvArr2, @ByVal opencv_core.CvRect cvRect, @ByVal opencv_core.CvPoint cvPoint, @Cast({"bool"}) boolean z);

        public native int borderElemSize();

        public native FilterEngine borderElemSize(int i);

        @StdVector
        @Const
        public native IntPointer borderTab();

        public native FilterEngine borderTab(IntPointer intPointer);

        public native int bufStep();

        public native FilterEngine bufStep(int i);

        public native int bufType();

        public native FilterEngine bufType(int i);

        public native int columnBorderType();

        public native FilterEngine columnBorderType(int i);

        @opencv_core.Ptr
        @Const
        public native BaseColumnFilter columnFilter();

        public native FilterEngine columnFilter(BaseColumnFilter baseColumnFilter);

        @StdVector
        @Const
        @Cast({"uchar*"})
        public native BytePointer constBorderRow();

        public native FilterEngine constBorderRow(BytePointer bytePointer);

        @StdVector
        @Const
        @Cast({"uchar*"})
        public native BytePointer constBorderValue();

        public native FilterEngine constBorderValue(BytePointer bytePointer);

        public native int dstType();

        public native FilterEngine dstType(int i);

        public native int dstY();

        public native FilterEngine dstY(int i);

        public native int dx1();

        public native FilterEngine dx1(int i);

        public native int dx2();

        public native FilterEngine dx2(int i);

        public native int endY();

        public native FilterEngine endY(int i);

        @opencv_core.Ptr
        @Const
        public native BaseFilter filter2D();

        public native FilterEngine filter2D(BaseFilter baseFilter);

        public native void init(@opencv_core.Ptr BaseFilter baseFilter, @opencv_core.Ptr BaseRowFilter baseRowFilter, @opencv_core.Ptr BaseColumnFilter baseColumnFilter, int i, int i2, int i3, int i4, int i5, @ByVal opencv_core.CvScalar cvScalar);

        public native boolean isSeparable();

        @ByVal
        public native opencv_core.CvSize ksize();

        public native FilterEngine ksize(opencv_core.CvSize cvSize);

        public native int maxWidth();

        public native FilterEngine maxWidth(int i);

        public native int proceed(@Cast({"uchar*"}) BytePointer bytePointer, int i, int i2, @Cast({"uchar*"}) BytePointer bytePointer2, int i3);

        public native int remainingInputRows();

        public native int remainingOutputRows();

        @StdVector
        @Const
        @Cast({"uchar*"})
        public native BytePointer ringBuf();

        public native FilterEngine ringBuf(BytePointer bytePointer);

        @ByVal
        public native opencv_core.CvRect roi();

        public native FilterEngine roi(opencv_core.CvRect cvRect);

        public native int rowBorderType();

        public native FilterEngine rowBorderType(int i);

        public native int rowCount();

        public native FilterEngine rowCount(int i);

        @opencv_core.Ptr
        @Const
        public native BaseRowFilter rowFilter();

        public native FilterEngine rowFilter(BaseRowFilter baseRowFilter);

        @StdVector
        @Const
        @Cast({"uchar**"})
        public native PointerPointer rows();

        public native FilterEngine rows(PointerPointer pointerPointer);

        @StdVector
        @Const
        @Cast({"uchar*"})
        public native BytePointer srcRow();

        public native FilterEngine srcRow(BytePointer bytePointer);

        public native int srcType();

        public native FilterEngine srcType(int i);

        public native int start(@opencv_core.InputMat opencv_core.CvArr cvArr, @ByVal opencv_core.CvRect cvRect, @Cast({"bool"}) boolean z, int i);

        public native int start(@ByVal opencv_core.CvSize cvSize, @ByVal opencv_core.CvRect cvRect, int i);

        public native int startY();

        public native FilterEngine startY(int i);

        public native int startY0();

        public native FilterEngine startY0(int i);

        @ByVal
        public native opencv_core.CvSize wholeSize();

        public native FilterEngine wholeSize(opencv_core.CvSize cvSize);

        static {
            Loader.load();
        }

        public FilterEngine() {
            allocate();
        }

        public FilterEngine(@opencv_core.Ptr BaseFilter _filter2D, @opencv_core.Ptr BaseRowFilter _rowFilter, @opencv_core.Ptr BaseColumnFilter _columnFilter, int srcType, int dstType, int bufType, int _rowBorderType, int _columnBorderType, @ByVal opencv_core.CvScalar _borderValue) {
            allocate(_filter2D, _rowFilter, _columnFilter, srcType, dstType, bufType, _rowBorderType, _columnBorderType, _borderValue);
        }

        public FilterEngine(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class GeneralizedHough extends opencv_core.Algorithm {
        @opencv_core.Ptr
        public static native GeneralizedHough create(int i);

        public native void detect(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, @opencv_core.InputArray opencv_core.CvArr cvArr3, @opencv_core.OutputArray opencv_core.CvMat cvMat, @opencv_core.OutputArray opencv_core.CvMat cvMat2);

        public native void detect(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.OutputArray opencv_core.CvMat cvMat, @opencv_core.OutputArray opencv_core.CvMat cvMat2, int i);

        public native void release();

        public native void setTemplate(@opencv_core.InputArray opencv_core.CvArr cvArr, int i, @ByVal opencv_core.CvPoint cvPoint);

        public native void setTemplate(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2, @opencv_core.InputArray opencv_core.CvArr cvArr3, @ByVal opencv_core.CvPoint cvPoint);

        static {
            Loader.load();
        }

        public GeneralizedHough() {
        }

        public GeneralizedHough(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class CLAHE extends opencv_core.Algorithm {
        public native void apply(@opencv_core.InputArray opencv_core.CvArr cvArr, @opencv_core.InputArray opencv_core.CvArr cvArr2);

        public native void collectGarbage();

        public native double getClipLimit();

        @ByVal
        public native opencv_core.CvSize getTilesGridSize();

        public native void setClipLimit(double d);

        public native void setTilesGridSize(@ByVal opencv_core.CvSize cvSize);

        static {
            Loader.load();
        }

        public CLAHE() {
        }

        public CLAHE(Pointer p) {
            super(p);
        }
    }
}
