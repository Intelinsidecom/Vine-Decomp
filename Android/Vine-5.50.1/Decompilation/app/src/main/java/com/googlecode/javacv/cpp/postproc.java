package com.googlecode.javacv.cpp;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.PointerPointer;
import com.googlecode.javacpp.annotation.ByPtrPtr;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.MemberGetter;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Properties(inherit = {avutil.class}, value = {@Platform(cinclude = {"<libpostproc/postprocess.h>"}, link = {"postproc@.52"}), @Platform(preload = {"postproc-52"}, value = {"windows"})})
/* loaded from: classes.dex */
public class postproc {
    public static final int PP_CPU_CAPS_3DNOW = 1073741824;
    public static final int PP_CPU_CAPS_ALTIVEC = 268435456;
    public static final int PP_CPU_CAPS_AUTO = 524288;
    public static final int PP_CPU_CAPS_MMX = Integer.MIN_VALUE;
    public static final int PP_CPU_CAPS_MMX2 = 536870912;
    public static final int PP_FORMAT = 8;
    public static final int PP_FORMAT_411 = 10;
    public static final int PP_FORMAT_420 = 25;
    public static final int PP_FORMAT_422 = 9;
    public static final int PP_FORMAT_444 = 8;
    public static final int PP_PICT_TYPE_QP2 = 16;
    public static final int PP_QUALITY_MAX = 6;

    @Cast({"const char*"})
    public static native BytePointer postproc_configuration();

    @Cast({"const char*"})
    public static native BytePointer postproc_license();

    @Cast({"unsigned"})
    public static native int postproc_version();

    public static native void pp_free_context(@Cast({"pp_context*"}) Pointer pointer);

    public static native void pp_free_mode(@Cast({"pp_mode*"}) Pointer pointer);

    @Cast({"pp_context*"})
    public static native Pointer pp_get_context(int i, int i2, int i3);

    @Cast({"pp_mode*"})
    public static native Pointer pp_get_mode_by_name_and_quality(@Cast({"const char*"}) BytePointer bytePointer, int i);

    @Cast({"pp_mode*"})
    public static native Pointer pp_get_mode_by_name_and_quality(String str, int i);

    @MemberGetter
    public static native byte pp_help(int i);

    @MemberGetter
    @Cast({"const char*"})
    public static native BytePointer pp_help();

    public static native void pp_postprocess(@ByPtrPtr @Cast({"const uint8_t**"}) BytePointer bytePointer, @Const IntPointer intPointer, @ByPtrPtr @Cast({"uint8_t**"}) BytePointer bytePointer2, @Const IntPointer intPointer2, int i, int i2, @Const BytePointer bytePointer3, int i3, @Cast({"pp_mode*"}) Pointer pointer, @Cast({"pp_context*"}) Pointer pointer2, int i4);

    public static native void pp_postprocess(@Cast({"const uint8_t**"}) PointerPointer pointerPointer, @Const IntPointer intPointer, @Cast({"uint8_t**"}) PointerPointer pointerPointer2, @Const IntPointer intPointer2, int i, int i2, @Const BytePointer bytePointer, int i3, @Cast({"pp_mode*"}) Pointer pointer, @Cast({"pp_context*"}) Pointer pointer2, int i4);

    public static native void pp_postprocess(@ByPtrPtr @Cast({"const uint8_t**"}) ByteBuffer byteBuffer, @Const IntBuffer intBuffer, @ByPtrPtr @Cast({"uint8_t**"}) ByteBuffer byteBuffer2, @Const IntBuffer intBuffer2, int i, int i2, @Const BytePointer bytePointer, int i3, @Cast({"pp_mode*"}) Pointer pointer, @Cast({"pp_context*"}) Pointer pointer2, int i4);

    public static native void pp_postprocess(@ByPtrPtr @Cast({"const uint8_t**"}) byte[] bArr, @Const int[] iArr, @ByPtrPtr @Cast({"uint8_t**"}) byte[] bArr2, @Const int[] iArr2, int i, int i2, @Const BytePointer bytePointer, int i3, @Cast({"pp_mode*"}) Pointer pointer, @Cast({"pp_context*"}) Pointer pointer2, int i4);

    static {
        Loader.load();
    }
}
