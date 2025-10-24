package com.googlecode.javacv.cpp;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.FunctionPointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.LongPointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.PointerPointer;
import com.googlecode.javacpp.annotation.ByPtrPtr;
import com.googlecode.javacpp.annotation.ByVal;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.MemberGetter;
import com.googlecode.javacpp.annotation.NoOffset;
import com.googlecode.javacpp.annotation.Opaque;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;
import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avutil;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

@Properties(inherit = {avcodec.class}, value = {@Platform(cinclude = {"<libavformat/avio.h>", "<libavformat/avformat.h>"}, link = {"avformat@.55"}), @Platform(preload = {"avformat-55"}, value = {"windows"})})
/* loaded from: classes.dex */
public class avformat {
    public static final int AVFMTCTX_NOHEADER = 1;
    public static final int AVFMT_ALLOW_FLUSH = 65536;
    public static final int AVFMT_DURATION_FROM_BITRATE = 2;
    public static final int AVFMT_DURATION_FROM_PTS = 0;
    public static final int AVFMT_DURATION_FROM_STREAM = 1;
    public static final int AVFMT_GENERIC_INDEX = 256;
    public static final int AVFMT_GLOBALHEADER = 64;
    public static final int AVFMT_NEEDNUMBER = 2;
    public static final int AVFMT_NOBINSEARCH = 8192;
    public static final int AVFMT_NODIMENSIONS = 2048;
    public static final int AVFMT_NOFILE = 1;
    public static final int AVFMT_NOGENSEARCH = 16384;
    public static final int AVFMT_NOSTREAMS = 4096;
    public static final int AVFMT_NOTIMESTAMPS = 128;
    public static final int AVFMT_NO_BYTE_SEEK = 32768;
    public static final int AVFMT_RAWPICTURE = 32;
    public static final int AVFMT_SEEK_TO_PTS = 67108864;
    public static final int AVFMT_SHOW_IDS = 8;
    public static final int AVFMT_TS_DISCONT = 512;
    public static final int AVFMT_TS_NEGATIVE = 262144;
    public static final int AVFMT_TS_NONSTRICT = 131072;
    public static final int AVFMT_VARIABLE_FPS = 1024;
    public static final int AVIO_FLAG_DIRECT = 32768;
    public static final int AVIO_FLAG_NONBLOCK = 8;
    public static final int AVIO_FLAG_READ = 1;
    public static final int AVIO_FLAG_READ_WRITE = 3;
    public static final int AVIO_FLAG_WRITE = 2;
    public static final int AVIO_SEEKABLE_NORMAL = 1;
    public static final int AVPROBE_PADDING_SIZE = 32;
    public static final int AVPROBE_SCORE_EXTENSION = 50;
    public static final int AVPROBE_SCORE_MAX = 100;
    public static final int AVPROBE_SCORE_RETRY;
    public static final int AVSEEK_FLAG_ANY = 4;
    public static final int AVSEEK_FLAG_BACKWARD = 1;
    public static final int AVSEEK_FLAG_BYTE = 2;
    public static final int AVSEEK_FLAG_FRAME = 8;
    public static final int AVSEEK_FORCE = 131072;
    public static final int AVSEEK_SIZE = 65536;
    public static final int AVSTREAM_PARSE_FULL = 1;
    public static final int AVSTREAM_PARSE_FULL_ONCE = 4;
    public static final int AVSTREAM_PARSE_FULL_RAW;
    public static final int AVSTREAM_PARSE_HEADERS = 2;
    public static final int AVSTREAM_PARSE_NONE = 0;
    public static final int AVSTREAM_PARSE_TIMESTAMPS = 3;
    public static final int AV_DISPOSITION_ATTACHED_PIC = 1024;
    public static final int AV_DISPOSITION_CAPTIONS = 65536;
    public static final int AV_DISPOSITION_CLEAN_EFFECTS = 512;
    public static final int AV_DISPOSITION_COMMENT = 8;
    public static final int AV_DISPOSITION_DEFAULT = 1;
    public static final int AV_DISPOSITION_DESCRIPTIONS = 131072;
    public static final int AV_DISPOSITION_DUB = 2;
    public static final int AV_DISPOSITION_FORCED = 64;
    public static final int AV_DISPOSITION_HEARING_IMPAIRED = 128;
    public static final int AV_DISPOSITION_KARAOKE = 32;
    public static final int AV_DISPOSITION_LYRICS = 16;
    public static final int AV_DISPOSITION_METADATA = 262144;
    public static final int AV_DISPOSITION_ORIGINAL = 4;
    public static final int AV_DISPOSITION_VISUAL_IMPAIRED = 256;
    public static final int AV_PROGRAM_RUNNING = 1;
    public static final int AV_PTS_WRAP_ADD_OFFSET = 1;
    public static final int AV_PTS_WRAP_IGNORE = 0;
    public static final int AV_PTS_WRAP_SUB_OFFSET = -1;

    @MemberGetter
    public static native int AVPROBE_SCORE_RETRY();

    @MemberGetter
    public static native int AVSTREAM_PARSE_FULL_RAW();

    public static native int av_add_index_entry(AVStream aVStream, long j, long j2, int i, int i2, int i3);

    public static native int av_append_packet(AVIOContext aVIOContext, avcodec.AVPacket aVPacket, int i);

    @Deprecated
    public static native void av_close_input_file(AVFormatContext aVFormatContext);

    @Cast({"AVCodecID"})
    public static native int av_codec_get_id(@Cast({"const AVCodecTag*const*"}) PointerPointer pointerPointer, @Cast({"unsigned int"}) int i);

    @Cast({"AVCodecID"})
    public static native int av_codec_get_id(@Const @ByPtrPtr AVCodecTag aVCodecTag, @Cast({"unsigned int"}) int i);

    @Cast({"unsigned int"})
    public static native int av_codec_get_tag(@Cast({"const AVCodecTag*const*"}) PointerPointer pointerPointer, @Cast({"AVCodecID"}) int i);

    @Cast({"unsigned int"})
    public static native int av_codec_get_tag(@Const @ByPtrPtr AVCodecTag aVCodecTag, @Cast({"AVCodecID"}) int i);

    public static native int av_codec_get_tag2(@Cast({"const AVCodecTag*const*"}) PointerPointer pointerPointer, @Cast({"AVCodecID"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    public static native int av_codec_get_tag2(@Const @ByPtrPtr AVCodecTag aVCodecTag, @Cast({"AVCodecID"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    public static native int av_codec_get_tag2(@Const @ByPtrPtr AVCodecTag aVCodecTag, @Cast({"AVCodecID"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    public static native int av_codec_get_tag2(@Const @ByPtrPtr AVCodecTag aVCodecTag, @Cast({"AVCodecID"}) int i, @Cast({"unsigned int*"}) int[] iArr);

    @Deprecated
    public static native int av_demuxer_open(AVFormatContext aVFormatContext);

    public static native void av_dump_format(AVFormatContext aVFormatContext, int i, @Cast({"const char*"}) BytePointer bytePointer, int i2);

    public static native void av_dump_format(AVFormatContext aVFormatContext, int i, String str, int i2);

    public static native int av_filename_number_test(@Cast({"const char*"}) BytePointer bytePointer);

    public static native int av_filename_number_test(String str);

    public static native int av_find_best_stream(AVFormatContext aVFormatContext, @Cast({"AVMediaType"}) int i, int i2, int i3, @Cast({"AVCodec**"}) PointerPointer pointerPointer, int i4);

    public static native int av_find_best_stream(AVFormatContext aVFormatContext, @Cast({"AVMediaType"}) int i, int i2, int i3, @ByPtrPtr avcodec.AVCodec aVCodec, int i4);

    public static native int av_find_default_stream_index(AVFormatContext aVFormatContext);

    public static native AVInputFormat av_find_input_format(@Cast({"const char*"}) BytePointer bytePointer);

    public static native AVInputFormat av_find_input_format(String str);

    public static native AVProgram av_find_program_from_stream(AVFormatContext aVFormatContext, AVProgram aVProgram, int i);

    @Deprecated
    public static native int av_find_stream_info(AVFormatContext aVFormatContext);

    @Cast({"AVDurationEstimationMethod"})
    public static native int av_fmt_ctx_get_duration_estimation_method(@Const AVFormatContext aVFormatContext);

    public static native int av_get_frame_filename(@Cast({"char*"}) BytePointer bytePointer, int i, @Cast({"const char*"}) BytePointer bytePointer2, int i2);

    public static native int av_get_frame_filename(@Cast({"char*"}) BytePointer bytePointer, int i, String str, int i2);

    public static native int av_get_frame_filename(@Cast({"char*"}) ByteBuffer byteBuffer, int i, @Cast({"const char*"}) BytePointer bytePointer, int i2);

    public static native int av_get_frame_filename(@Cast({"char*"}) ByteBuffer byteBuffer, int i, String str, int i2);

    public static native int av_get_frame_filename(@Cast({"char*"}) byte[] bArr, int i, @Cast({"const char*"}) BytePointer bytePointer, int i2);

    public static native int av_get_frame_filename(@Cast({"char*"}) byte[] bArr, int i, String str, int i2);

    public static native int av_get_output_timestamp(AVFormatContext aVFormatContext, int i, LongPointer longPointer, LongPointer longPointer2);

    public static native int av_get_output_timestamp(AVFormatContext aVFormatContext, int i, LongBuffer longBuffer, LongBuffer longBuffer2);

    public static native int av_get_output_timestamp(AVFormatContext aVFormatContext, int i, long[] jArr, long[] jArr2);

    public static native int av_get_packet(AVIOContext aVIOContext, avcodec.AVPacket aVPacket, int i);

    @Cast({"AVCodecID"})
    public static native int av_guess_codec(AVOutputFormat aVOutputFormat, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, @Cast({"AVMediaType"}) int i);

    @Cast({"AVCodecID"})
    public static native int av_guess_codec(AVOutputFormat aVOutputFormat, String str, String str2, String str3, @Cast({"AVMediaType"}) int i);

    public static native AVOutputFormat av_guess_format(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3);

    public static native AVOutputFormat av_guess_format(String str, String str2, String str3);

    @ByVal
    public static native avutil.AVRational av_guess_frame_rate(AVFormatContext aVFormatContext, AVStream aVStream, avutil.AVFrame aVFrame);

    @ByVal
    public static native avutil.AVRational av_guess_sample_aspect_ratio(AVFormatContext aVFormatContext, AVStream aVStream, avutil.AVFrame aVFrame);

    public static native void av_hex_dump(@Cast({"FILE*"}) Pointer pointer, @Cast({"const uint8_t*"}) BytePointer bytePointer, int i);

    public static native void av_hex_dump(@Cast({"FILE*"}) Pointer pointer, @Cast({"const uint8_t*"}) ByteBuffer byteBuffer, int i);

    public static native void av_hex_dump(@Cast({"FILE*"}) Pointer pointer, @Cast({"const uint8_t*"}) byte[] bArr, int i);

    public static native void av_hex_dump_log(Pointer pointer, int i, @Cast({"const uint8_t*"}) BytePointer bytePointer, int i2);

    public static native void av_hex_dump_log(Pointer pointer, int i, @Cast({"const uint8_t*"}) ByteBuffer byteBuffer, int i2);

    public static native void av_hex_dump_log(Pointer pointer, int i, @Cast({"const uint8_t*"}) byte[] bArr, int i2);

    public static native AVInputFormat av_iformat_next(AVInputFormat aVInputFormat);

    public static native int av_index_search_timestamp(AVStream aVStream, long j, int i);

    public static native int av_interleaved_write_frame(AVFormatContext aVFormatContext, avcodec.AVPacket aVPacket);

    public static native int av_match_ext(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    public static native int av_match_ext(String str, String str2);

    public static native AVProgram av_new_program(AVFormatContext aVFormatContext, int i);

    @Deprecated
    public static native AVStream av_new_stream(AVFormatContext aVFormatContext, int i);

    public static native AVOutputFormat av_oformat_next(AVOutputFormat aVOutputFormat);

    public static native void av_pkt_dump2(@Cast({"FILE*"}) Pointer pointer, avcodec.AVPacket aVPacket, int i, AVStream aVStream);

    public static native void av_pkt_dump_log2(Pointer pointer, int i, avcodec.AVPacket aVPacket, int i2, AVStream aVStream);

    public static native int av_probe_input_buffer(AVIOContext aVIOContext, @Cast({"AVInputFormat**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, Pointer pointer, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    public static native int av_probe_input_buffer(AVIOContext aVIOContext, @ByPtrPtr AVInputFormat aVInputFormat, @Cast({"const char*"}) BytePointer bytePointer, Pointer pointer, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    public static native int av_probe_input_buffer(AVIOContext aVIOContext, @ByPtrPtr AVInputFormat aVInputFormat, String str, Pointer pointer, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    public static native AVInputFormat av_probe_input_format(AVProbeData aVProbeData, int i);

    public static native AVInputFormat av_probe_input_format2(AVProbeData aVProbeData, int i, IntPointer intPointer);

    public static native AVInputFormat av_probe_input_format2(AVProbeData aVProbeData, int i, IntBuffer intBuffer);

    public static native AVInputFormat av_probe_input_format2(AVProbeData aVProbeData, int i, int[] iArr);

    public static native AVInputFormat av_probe_input_format3(AVProbeData aVProbeData, int i, IntPointer intPointer);

    public static native AVInputFormat av_probe_input_format3(AVProbeData aVProbeData, int i, IntBuffer intBuffer);

    public static native AVInputFormat av_probe_input_format3(AVProbeData aVProbeData, int i, int[] iArr);

    public static native int av_read_frame(AVFormatContext aVFormatContext, avcodec.AVPacket aVPacket);

    @Deprecated
    public static native int av_read_packet(AVFormatContext aVFormatContext, avcodec.AVPacket aVPacket);

    public static native int av_read_pause(AVFormatContext aVFormatContext);

    public static native int av_read_play(AVFormatContext aVFormatContext);

    public static native void av_register_all();

    public static native void av_register_input_format(AVInputFormat aVInputFormat);

    public static native void av_register_output_format(AVOutputFormat aVOutputFormat);

    public static native int av_sdp_create(@Cast({"AVFormatContext**"}) PointerPointer pointerPointer, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    public static native int av_sdp_create(@ByPtrPtr AVFormatContext aVFormatContext, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    public static native int av_sdp_create(@ByPtrPtr AVFormatContext aVFormatContext, int i, @Cast({"char*"}) ByteBuffer byteBuffer, int i2);

    public static native int av_sdp_create(@ByPtrPtr AVFormatContext aVFormatContext, int i, @Cast({"char*"}) byte[] bArr, int i2);

    public static native int av_seek_frame(AVFormatContext aVFormatContext, int i, long j, int i2);

    @Deprecated
    public static native void av_set_pts_info(AVStream aVStream, int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3);

    @ByVal
    public static native avutil.AVRational av_stream_get_r_frame_rate(@Const AVStream aVStream);

    public static native void av_stream_set_r_frame_rate(AVStream aVStream, @ByVal avutil.AVRational aVRational);

    public static native void av_url_split(@Cast({"char*"}) BytePointer bytePointer, int i, @Cast({"char*"}) BytePointer bytePointer2, int i2, @Cast({"char*"}) BytePointer bytePointer3, int i3, IntPointer intPointer, @Cast({"char*"}) BytePointer bytePointer4, int i4, @Cast({"const char*"}) BytePointer bytePointer5);

    public static native void av_url_split(@Cast({"char*"}) BytePointer bytePointer, int i, @Cast({"char*"}) BytePointer bytePointer2, int i2, @Cast({"char*"}) BytePointer bytePointer3, int i3, IntPointer intPointer, @Cast({"char*"}) BytePointer bytePointer4, int i4, String str);

    public static native void av_url_split(@Cast({"char*"}) ByteBuffer byteBuffer, int i, @Cast({"char*"}) ByteBuffer byteBuffer2, int i2, @Cast({"char*"}) ByteBuffer byteBuffer3, int i3, IntBuffer intBuffer, @Cast({"char*"}) ByteBuffer byteBuffer4, int i4, @Cast({"const char*"}) BytePointer bytePointer);

    public static native void av_url_split(@Cast({"char*"}) ByteBuffer byteBuffer, int i, @Cast({"char*"}) ByteBuffer byteBuffer2, int i2, @Cast({"char*"}) ByteBuffer byteBuffer3, int i3, IntBuffer intBuffer, @Cast({"char*"}) ByteBuffer byteBuffer4, int i4, String str);

    public static native void av_url_split(@Cast({"char*"}) byte[] bArr, int i, @Cast({"char*"}) byte[] bArr2, int i2, @Cast({"char*"}) byte[] bArr3, int i3, int[] iArr, @Cast({"char*"}) byte[] bArr4, int i4, @Cast({"const char*"}) BytePointer bytePointer);

    public static native void av_url_split(@Cast({"char*"}) byte[] bArr, int i, @Cast({"char*"}) byte[] bArr2, int i2, @Cast({"char*"}) byte[] bArr3, int i3, int[] iArr, @Cast({"char*"}) byte[] bArr4, int i4, String str);

    public static native int av_write_frame(AVFormatContext aVFormatContext, avcodec.AVPacket aVPacket);

    public static native int av_write_trailer(AVFormatContext aVFormatContext);

    public static native AVFormatContext avformat_alloc_context();

    @Deprecated
    public static native AVFormatContext avformat_alloc_output_context(@Cast({"const char*"}) BytePointer bytePointer, AVOutputFormat aVOutputFormat, @Cast({"const char*"}) BytePointer bytePointer2);

    @Deprecated
    public static native AVFormatContext avformat_alloc_output_context(String str, AVOutputFormat aVOutputFormat, String str2);

    public static native int avformat_alloc_output_context2(@Cast({"AVFormatContext**"}) PointerPointer pointerPointer, AVOutputFormat aVOutputFormat, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    public static native int avformat_alloc_output_context2(@ByPtrPtr AVFormatContext aVFormatContext, AVOutputFormat aVOutputFormat, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    public static native int avformat_alloc_output_context2(@ByPtrPtr AVFormatContext aVFormatContext, AVOutputFormat aVOutputFormat, String str, String str2);

    public static native void avformat_close_input(@Cast({"AVFormatContext**"}) PointerPointer pointerPointer);

    public static native void avformat_close_input(@ByPtrPtr AVFormatContext aVFormatContext);

    @Cast({"const char*"})
    public static native BytePointer avformat_configuration();

    public static native int avformat_find_stream_info(AVFormatContext aVFormatContext, @Cast({"AVDictionary**"}) PointerPointer pointerPointer);

    public static native int avformat_find_stream_info(AVFormatContext aVFormatContext, @ByPtrPtr avutil.AVDictionary aVDictionary);

    public static native void avformat_free_context(AVFormatContext aVFormatContext);

    @Const
    public static native avutil.AVClass avformat_get_class();

    @Const
    public static native AVCodecTag avformat_get_riff_audio_tags();

    @Const
    public static native AVCodecTag avformat_get_riff_video_tags();

    @Cast({"const char*"})
    public static native BytePointer avformat_license();

    public static native int avformat_match_stream_specifier(AVFormatContext aVFormatContext, AVStream aVStream, @Cast({"const char*"}) BytePointer bytePointer);

    public static native int avformat_match_stream_specifier(AVFormatContext aVFormatContext, AVStream aVStream, String str);

    public static native int avformat_network_deinit();

    public static native int avformat_network_init();

    public static native AVStream avformat_new_stream(AVFormatContext aVFormatContext, @Const avcodec.AVCodec aVCodec);

    public static native int avformat_open_input(@Cast({"AVFormatContext**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, AVInputFormat aVInputFormat, @Cast({"AVDictionary**"}) PointerPointer pointerPointer2);

    public static native int avformat_open_input(@ByPtrPtr AVFormatContext aVFormatContext, @Cast({"const char*"}) BytePointer bytePointer, AVInputFormat aVInputFormat, @ByPtrPtr avutil.AVDictionary aVDictionary);

    public static native int avformat_open_input(@ByPtrPtr AVFormatContext aVFormatContext, String str, AVInputFormat aVInputFormat, @ByPtrPtr avutil.AVDictionary aVDictionary);

    public static native int avformat_query_codec(AVOutputFormat aVOutputFormat, @Cast({"AVCodecID"}) int i, int i2);

    public static native int avformat_queue_attached_pictures(AVFormatContext aVFormatContext);

    public static native int avformat_seek_file(AVFormatContext aVFormatContext, int i, long j, long j2, long j3, int i2);

    @Cast({"unsigned"})
    public static native int avformat_version();

    public static native int avformat_write_header(AVFormatContext aVFormatContext, @Cast({"AVDictionary**"}) PointerPointer pointerPointer);

    public static native int avformat_write_header(AVFormatContext aVFormatContext, @ByPtrPtr avutil.AVDictionary aVDictionary);

    public static native AVIOContext avio_alloc_context(@Cast({"unsigned char*"}) BytePointer bytePointer, int i, int i2, Pointer pointer, Read_packet_Pointer_BytePointer_int read_packet_Pointer_BytePointer_int, Write_packet_Pointer_BytePointer_int write_packet_Pointer_BytePointer_int, Seek_Pointer_long_int seek_Pointer_long_int);

    public static native AVIOContext avio_alloc_context(@Cast({"unsigned char*"}) ByteBuffer byteBuffer, int i, int i2, Pointer pointer, Read_packet_Pointer_ByteBuffer_int read_packet_Pointer_ByteBuffer_int, Write_packet_Pointer_ByteBuffer_int write_packet_Pointer_ByteBuffer_int, Seek_Pointer_long_int seek_Pointer_long_int);

    public static native AVIOContext avio_alloc_context(@Cast({"unsigned char*"}) byte[] bArr, int i, int i2, Pointer pointer, Read_packet_Pointer_byte_int read_packet_Pointer_byte_int, Write_packet_Pointer_byte_int write_packet_Pointer_byte_int, Seek_Pointer_long_int seek_Pointer_long_int);

    public static native int avio_check(@Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native int avio_check(String str, int i);

    public static native int avio_close(AVIOContext aVIOContext);

    public static native int avio_close_dyn_buf(AVIOContext aVIOContext, @ByPtrPtr @Cast({"uint8_t**"}) BytePointer bytePointer);

    public static native int avio_close_dyn_buf(AVIOContext aVIOContext, @Cast({"uint8_t**"}) PointerPointer pointerPointer);

    public static native int avio_close_dyn_buf(AVIOContext aVIOContext, @ByPtrPtr @Cast({"uint8_t**"}) ByteBuffer byteBuffer);

    public static native int avio_close_dyn_buf(AVIOContext aVIOContext, @ByPtrPtr @Cast({"uint8_t**"}) byte[] bArr);

    public static native int avio_closep(@Cast({"AVIOContext**"}) PointerPointer pointerPointer);

    public static native int avio_closep(@ByPtrPtr AVIOContext aVIOContext);

    @Cast({"const char*"})
    public static native BytePointer avio_enum_protocols(@ByPtrPtr @Cast({"void**"}) Pointer pointer, int i);

    @Cast({"const char*"})
    public static native BytePointer avio_enum_protocols(@Cast({"void**"}) PointerPointer pointerPointer, int i);

    public static native void avio_flush(AVIOContext aVIOContext);

    public static native int avio_get_str(AVIOContext aVIOContext, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    public static native int avio_get_str(AVIOContext aVIOContext, int i, @Cast({"char*"}) ByteBuffer byteBuffer, int i2);

    public static native int avio_get_str(AVIOContext aVIOContext, int i, @Cast({"char*"}) byte[] bArr, int i2);

    public static native int avio_get_str16be(AVIOContext aVIOContext, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    public static native int avio_get_str16be(AVIOContext aVIOContext, int i, @Cast({"char*"}) ByteBuffer byteBuffer, int i2);

    public static native int avio_get_str16be(AVIOContext aVIOContext, int i, @Cast({"char*"}) byte[] bArr, int i2);

    public static native int avio_get_str16le(AVIOContext aVIOContext, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    public static native int avio_get_str16le(AVIOContext aVIOContext, int i, @Cast({"char*"}) ByteBuffer byteBuffer, int i2);

    public static native int avio_get_str16le(AVIOContext aVIOContext, int i, @Cast({"char*"}) byte[] bArr, int i2);

    public static native int avio_open(@Cast({"AVIOContext**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native int avio_open(@ByPtrPtr AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native int avio_open(@ByPtrPtr AVIOContext aVIOContext, String str, int i);

    public static native int avio_open2(@Cast({"AVIOContext**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, int i, @Const AVIOInterruptCB aVIOInterruptCB, @Cast({"AVDictionary**"}) PointerPointer pointerPointer2);

    public static native int avio_open2(@ByPtrPtr AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer, int i, @Const AVIOInterruptCB aVIOInterruptCB, @ByPtrPtr avutil.AVDictionary aVDictionary);

    public static native int avio_open2(@ByPtrPtr AVIOContext aVIOContext, String str, int i, @Const AVIOInterruptCB aVIOInterruptCB, @ByPtrPtr avutil.AVDictionary aVDictionary);

    public static native int avio_open_dyn_buf(@Cast({"AVIOContext**"}) PointerPointer pointerPointer);

    public static native int avio_open_dyn_buf(@ByPtrPtr AVIOContext aVIOContext);

    public static native int avio_pause(AVIOContext aVIOContext, int i);

    public static native int avio_printf(AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer);

    public static native int avio_printf(AVIOContext aVIOContext, String str);

    public static native int avio_put_str(AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer);

    public static native int avio_put_str(AVIOContext aVIOContext, String str);

    public static native int avio_put_str16le(AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer);

    public static native int avio_put_str16le(AVIOContext aVIOContext, String str);

    public static native int avio_r8(AVIOContext aVIOContext);

    @Cast({"unsigned int"})
    public static native int avio_rb16(AVIOContext aVIOContext);

    @Cast({"unsigned int"})
    public static native int avio_rb24(AVIOContext aVIOContext);

    @Cast({"unsigned int"})
    public static native int avio_rb32(AVIOContext aVIOContext);

    @Cast({"uint64_t"})
    public static native long avio_rb64(AVIOContext aVIOContext);

    public static native int avio_read(AVIOContext aVIOContext, @Cast({"unsigned char*"}) BytePointer bytePointer, int i);

    public static native int avio_read(AVIOContext aVIOContext, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, int i);

    public static native int avio_read(AVIOContext aVIOContext, @Cast({"unsigned char*"}) byte[] bArr, int i);

    @Cast({"unsigned int"})
    public static native int avio_rl16(AVIOContext aVIOContext);

    @Cast({"unsigned int"})
    public static native int avio_rl24(AVIOContext aVIOContext);

    @Cast({"unsigned int"})
    public static native int avio_rl32(AVIOContext aVIOContext);

    @Cast({"uint64_t"})
    public static native long avio_rl64(AVIOContext aVIOContext);

    public static native long avio_seek(AVIOContext aVIOContext, long j, int i);

    public static native long avio_seek_time(AVIOContext aVIOContext, int i, long j, int i2);

    public static native long avio_size(AVIOContext aVIOContext);

    public static native long avio_skip(AVIOContext aVIOContext, long j);

    public static native long avio_tell(AVIOContext aVIOContext);

    public static native void avio_w8(AVIOContext aVIOContext, int i);

    public static native void avio_wb16(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    public static native void avio_wb24(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    public static native void avio_wb32(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    public static native void avio_wb64(AVIOContext aVIOContext, @Cast({"uint64_t"}) long j);

    public static native void avio_wl16(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    public static native void avio_wl24(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    public static native void avio_wl32(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    public static native void avio_wl64(AVIOContext aVIOContext, @Cast({"uint64_t"}) long j);

    public static native void avio_write(AVIOContext aVIOContext, @Cast({"const unsigned char*"}) BytePointer bytePointer, int i);

    public static native void avio_write(AVIOContext aVIOContext, @Cast({"const unsigned char*"}) ByteBuffer byteBuffer, int i);

    public static native void avio_write(AVIOContext aVIOContext, @Cast({"const unsigned char*"}) byte[] bArr, int i);

    public static native int url_feof(AVIOContext aVIOContext);

    static {
        Loader.load();
        AVPROBE_SCORE_RETRY = AVPROBE_SCORE_RETRY();
        AVSTREAM_PARSE_FULL_RAW = AVSTREAM_PARSE_FULL_RAW();
    }

    public static class AVIOInterruptCB extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native Callback_Pointer callback();

        public native AVIOInterruptCB callback(Callback_Pointer callback_Pointer);

        public native Pointer opaque();

        public native AVIOInterruptCB opaque(Pointer pointer);

        static {
            Loader.load();
        }

        public AVIOInterruptCB() {
            allocate();
        }

        public AVIOInterruptCB(int size) {
            allocateArray(size);
        }

        public AVIOInterruptCB(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVIOInterruptCB position(int position) {
            return (AVIOInterruptCB) super.position(position);
        }

        public static class Callback_Pointer extends FunctionPointer {
            private native void allocate();

            public native int call(Pointer pointer);

            static {
                Loader.load();
            }

            public Callback_Pointer(Pointer p) {
                super(p);
            }

            protected Callback_Pointer() {
                allocate();
            }
        }
    }

    public static class AVIOContext extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        @MemberGetter
        @Const
        public native avutil.AVClass av_class();

        @Cast({"unsigned char*"})
        public native BytePointer buf_end();

        public native AVIOContext buf_end(BytePointer bytePointer);

        @Cast({"unsigned char*"})
        public native BytePointer buf_ptr();

        public native AVIOContext buf_ptr(BytePointer bytePointer);

        @Cast({"unsigned char*"})
        public native BytePointer buffer();

        public native AVIOContext buffer(BytePointer bytePointer);

        public native int buffer_size();

        public native AVIOContext buffer_size(int i);

        public native long bytes_read();

        public native AVIOContext bytes_read(long j);

        @Cast({"unsigned long"})
        public native long checksum();

        public native AVIOContext checksum(long j);

        @Cast({"unsigned char*"})
        public native BytePointer checksum_ptr();

        public native AVIOContext checksum_ptr(BytePointer bytePointer);

        public native int direct();

        public native AVIOContext direct(int i);

        public native int eof_reached();

        public native AVIOContext eof_reached(int i);

        public native int error();

        public native AVIOContext error(int i);

        public native int max_packet_size();

        public native AVIOContext max_packet_size(int i);

        public native long maxsize();

        public native AVIOContext maxsize(long j);

        public native int must_flush();

        public native AVIOContext must_flush(int i);

        public native Pointer opaque();

        public native AVIOContext opaque(Pointer pointer);

        public native long pos();

        public native AVIOContext pos(long j);

        public native Read_packet_Pointer_BytePointer_int read_packet();

        public native AVIOContext read_packet(Read_packet_Pointer_BytePointer_int read_packet_Pointer_BytePointer_int);

        public native Read_pause_Pointer_int read_pause();

        public native AVIOContext read_pause(Read_pause_Pointer_int read_pause_Pointer_int);

        public native Read_seek_Pointer_int_long_int read_seek();

        public native AVIOContext read_seek(Read_seek_Pointer_int_long_int read_seek_Pointer_int_long_int);

        public native Seek_Pointer_long_int seek();

        public native AVIOContext seek(Seek_Pointer_long_int seek_Pointer_long_int);

        public native int seek_count();

        public native AVIOContext seek_count(int i);

        public native int seekable();

        public native AVIOContext seekable(int i);

        public native Update_checksum_long_BytePointer_int update_checksum();

        public native AVIOContext update_checksum(Update_checksum_long_BytePointer_int update_checksum_long_BytePointer_int);

        public native int write_flag();

        public native AVIOContext write_flag(int i);

        public native Write_packet_Pointer_BytePointer_int write_packet();

        public native AVIOContext write_packet(Write_packet_Pointer_BytePointer_int write_packet_Pointer_BytePointer_int);

        public native int writeout_count();

        public native AVIOContext writeout_count(int i);

        static {
            Loader.load();
        }

        public AVIOContext() {
            allocate();
        }

        public AVIOContext(int size) {
            allocateArray(size);
        }

        public AVIOContext(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVIOContext position(int position) {
            return (AVIOContext) super.position(position);
        }

        public static class Read_packet_Pointer_BytePointer_int extends FunctionPointer {
            private native void allocate();

            public native int call(Pointer pointer, @Cast({"uint8_t*"}) BytePointer bytePointer, int i);

            static {
                Loader.load();
            }

            public Read_packet_Pointer_BytePointer_int(Pointer p) {
                super(p);
            }

            protected Read_packet_Pointer_BytePointer_int() {
                allocate();
            }
        }

        public static class Write_packet_Pointer_BytePointer_int extends FunctionPointer {
            private native void allocate();

            public native int call(Pointer pointer, @Cast({"uint8_t*"}) BytePointer bytePointer, int i);

            static {
                Loader.load();
            }

            public Write_packet_Pointer_BytePointer_int(Pointer p) {
                super(p);
            }

            protected Write_packet_Pointer_BytePointer_int() {
                allocate();
            }
        }

        public static class Seek_Pointer_long_int extends FunctionPointer {
            private native void allocate();

            public native long call(Pointer pointer, long j, int i);

            static {
                Loader.load();
            }

            public Seek_Pointer_long_int(Pointer p) {
                super(p);
            }

            protected Seek_Pointer_long_int() {
                allocate();
            }
        }

        public static class Update_checksum_long_BytePointer_int extends FunctionPointer {
            private native void allocate();

            @Cast({"unsigned long"})
            public native long call(@Cast({"unsigned long"}) long j, @Cast({"const uint8_t*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i);

            static {
                Loader.load();
            }

            public Update_checksum_long_BytePointer_int(Pointer p) {
                super(p);
            }

            protected Update_checksum_long_BytePointer_int() {
                allocate();
            }
        }

        public static class Read_pause_Pointer_int extends FunctionPointer {
            private native void allocate();

            public native int call(Pointer pointer, int i);

            static {
                Loader.load();
            }

            public Read_pause_Pointer_int(Pointer p) {
                super(p);
            }

            protected Read_pause_Pointer_int() {
                allocate();
            }
        }

        public static class Read_seek_Pointer_int_long_int extends FunctionPointer {
            private native void allocate();

            public native long call(Pointer pointer, int i, long j, int i2);

            static {
                Loader.load();
            }

            public Read_seek_Pointer_int_long_int(Pointer p) {
                super(p);
            }

            protected Read_seek_Pointer_int_long_int() {
                allocate();
            }
        }
    }

    public static class Read_packet_Pointer_BytePointer_int extends FunctionPointer {
        private native void allocate();

        public native int call(Pointer pointer, @Cast({"uint8_t*"}) BytePointer bytePointer, int i);

        static {
            Loader.load();
        }

        public Read_packet_Pointer_BytePointer_int(Pointer p) {
            super(p);
        }

        protected Read_packet_Pointer_BytePointer_int() {
            allocate();
        }
    }

    public static class Write_packet_Pointer_BytePointer_int extends FunctionPointer {
        private native void allocate();

        public native int call(Pointer pointer, @Cast({"uint8_t*"}) BytePointer bytePointer, int i);

        static {
            Loader.load();
        }

        public Write_packet_Pointer_BytePointer_int(Pointer p) {
            super(p);
        }

        protected Write_packet_Pointer_BytePointer_int() {
            allocate();
        }
    }

    public static class Seek_Pointer_long_int extends FunctionPointer {
        private native void allocate();

        public native long call(Pointer pointer, long j, int i);

        static {
            Loader.load();
        }

        public Seek_Pointer_long_int(Pointer p) {
            super(p);
        }

        protected Seek_Pointer_long_int() {
            allocate();
        }
    }

    public static class Read_packet_Pointer_ByteBuffer_int extends FunctionPointer {
        private native void allocate();

        public native int call(Pointer pointer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer, int i);

        static {
            Loader.load();
        }

        public Read_packet_Pointer_ByteBuffer_int(Pointer p) {
            super(p);
        }

        protected Read_packet_Pointer_ByteBuffer_int() {
            allocate();
        }
    }

    public static class Write_packet_Pointer_ByteBuffer_int extends FunctionPointer {
        private native void allocate();

        public native int call(Pointer pointer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer, int i);

        static {
            Loader.load();
        }

        public Write_packet_Pointer_ByteBuffer_int(Pointer p) {
            super(p);
        }

        protected Write_packet_Pointer_ByteBuffer_int() {
            allocate();
        }
    }

    public static class Read_packet_Pointer_byte_int extends FunctionPointer {
        private native void allocate();

        public native int call(Pointer pointer, @Cast({"uint8_t*"}) byte[] bArr, int i);

        static {
            Loader.load();
        }

        public Read_packet_Pointer_byte_int(Pointer p) {
            super(p);
        }

        protected Read_packet_Pointer_byte_int() {
            allocate();
        }
    }

    public static class Write_packet_Pointer_byte_int extends FunctionPointer {
        private native void allocate();

        public native int call(Pointer pointer, @Cast({"uint8_t*"}) byte[] bArr, int i);

        static {
            Loader.load();
        }

        public Write_packet_Pointer_byte_int(Pointer p) {
            super(p);
        }

        protected Write_packet_Pointer_byte_int() {
            allocate();
        }
    }

    public static class AVFrac extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native long den();

        public native AVFrac den(long j);

        public native long num();

        public native AVFrac num(long j);

        public native long val();

        public native AVFrac val(long j);

        static {
            Loader.load();
        }

        public AVFrac() {
            allocate();
        }

        public AVFrac(int size) {
            allocateArray(size);
        }

        public AVFrac(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVFrac position(int position) {
            return (AVFrac) super.position(position);
        }
    }

    @Opaque
    public static class AVCodecTag extends Pointer {
        public AVCodecTag() {
        }

        public AVCodecTag(Pointer p) {
            super(p);
        }
    }

    public static class AVProbeData extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        @Cast({"unsigned char*"})
        public native BytePointer buf();

        public native AVProbeData buf(BytePointer bytePointer);

        public native int buf_size();

        public native AVProbeData buf_size(int i);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer filename();

        static {
            Loader.load();
        }

        public AVProbeData() {
            allocate();
        }

        public AVProbeData(int size) {
            allocateArray(size);
        }

        public AVProbeData(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVProbeData position(int position) {
            return (AVProbeData) super.position(position);
        }
    }

    public static class AVOutputFormat extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        @Cast({"AVCodecID"})
        public native int audio_codec();

        public native AVOutputFormat audio_codec(int i);

        @MemberGetter
        @Cast({"const AVCodecTag*const*"})
        public native PointerPointer codec_tag();

        @MemberGetter
        @Const
        public native AVCodecTag codec_tag(int i);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer extensions();

        public native int flags();

        public native AVOutputFormat flags(int i);

        public native Get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer get_output_timestamp();

        public native AVOutputFormat get_output_timestamp(Get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer);

        public native Interleave_packet_AVFormatContext_AVPacket_AVPacket_int interleave_packet();

        public native AVOutputFormat interleave_packet(Interleave_packet_AVFormatContext_AVPacket_AVPacket_int interleave_packet_AVFormatContext_AVPacket_AVPacket_int);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer long_name();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer mime_type();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer name();

        public native AVOutputFormat next();

        public native AVOutputFormat next(AVOutputFormat aVOutputFormat);

        @MemberGetter
        @Const
        public native avutil.AVClass priv_class();

        public native int priv_data_size();

        public native AVOutputFormat priv_data_size(int i);

        public native Query_codec_int_int query_codec();

        public native AVOutputFormat query_codec(Query_codec_int_int query_codec_int_int);

        @Cast({"AVCodecID"})
        public native int subtitle_codec();

        public native AVOutputFormat subtitle_codec(int i);

        @Cast({"AVCodecID"})
        public native int video_codec();

        public native AVOutputFormat video_codec(int i);

        public native Write_header_AVFormatContext write_header();

        public native AVOutputFormat write_header(Write_header_AVFormatContext write_header_AVFormatContext);

        public native Write_packet_AVFormatContext_AVPacket write_packet();

        public native AVOutputFormat write_packet(Write_packet_AVFormatContext_AVPacket write_packet_AVFormatContext_AVPacket);

        public native Write_trailer_AVFormatContext write_trailer();

        public native AVOutputFormat write_trailer(Write_trailer_AVFormatContext write_trailer_AVFormatContext);

        static {
            Loader.load();
        }

        public AVOutputFormat() {
            allocate();
        }

        public AVOutputFormat(int size) {
            allocateArray(size);
        }

        public AVOutputFormat(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVOutputFormat position(int position) {
            return (AVOutputFormat) super.position(position);
        }

        public static class Write_header_AVFormatContext extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext);

            static {
                Loader.load();
            }

            public Write_header_AVFormatContext(Pointer p) {
                super(p);
            }

            protected Write_header_AVFormatContext() {
                allocate();
            }
        }

        public static class Write_packet_AVFormatContext_AVPacket extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext, avcodec.AVPacket aVPacket);

            static {
                Loader.load();
            }

            public Write_packet_AVFormatContext_AVPacket(Pointer p) {
                super(p);
            }

            protected Write_packet_AVFormatContext_AVPacket() {
                allocate();
            }
        }

        public static class Write_trailer_AVFormatContext extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext);

            static {
                Loader.load();
            }

            public Write_trailer_AVFormatContext(Pointer p) {
                super(p);
            }

            protected Write_trailer_AVFormatContext() {
                allocate();
            }
        }

        public static class Interleave_packet_AVFormatContext_AVPacket_AVPacket_int extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext, avcodec.AVPacket aVPacket, avcodec.AVPacket aVPacket2, int i);

            static {
                Loader.load();
            }

            public Interleave_packet_AVFormatContext_AVPacket_AVPacket_int(Pointer p) {
                super(p);
            }

            protected Interleave_packet_AVFormatContext_AVPacket_AVPacket_int() {
                allocate();
            }
        }

        public static class Query_codec_int_int extends FunctionPointer {
            private native void allocate();

            public native int call(@Cast({"AVCodecID"}) int i, int i2);

            static {
                Loader.load();
            }

            public Query_codec_int_int(Pointer p) {
                super(p);
            }

            protected Query_codec_int_int() {
                allocate();
            }
        }

        public static class Get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer extends FunctionPointer {
            private native void allocate();

            public native void call(AVFormatContext aVFormatContext, int i, LongPointer longPointer, LongPointer longPointer2);

            static {
                Loader.load();
            }

            public Get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer(Pointer p) {
                super(p);
            }

            protected Get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer() {
                allocate();
            }
        }
    }

    public static class AVInputFormat extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        @MemberGetter
        @Cast({"const AVCodecTag*const*"})
        public native PointerPointer codec_tag();

        @MemberGetter
        @Const
        public native AVCodecTag codec_tag(int i);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer extensions();

        public native int flags();

        public native AVInputFormat flags(int i);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer long_name();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer name();

        public native AVInputFormat next();

        public native AVInputFormat next(AVInputFormat aVInputFormat);

        @MemberGetter
        @Const
        public native avutil.AVClass priv_class();

        public native int priv_data_size();

        public native AVInputFormat priv_data_size(int i);

        public native int raw_codec_id();

        public native AVInputFormat raw_codec_id(int i);

        public native Read_close_AVFormatContext read_close();

        public native AVInputFormat read_close(Read_close_AVFormatContext read_close_AVFormatContext);

        public native Read_header_AVFormatContext read_header();

        public native AVInputFormat read_header(Read_header_AVFormatContext read_header_AVFormatContext);

        public native Read_packet_AVFormatContext_AVPacket read_packet();

        public native AVInputFormat read_packet(Read_packet_AVFormatContext_AVPacket read_packet_AVFormatContext_AVPacket);

        public native Read_pause_AVFormatContext read_pause();

        public native AVInputFormat read_pause(Read_pause_AVFormatContext read_pause_AVFormatContext);

        public native Read_play_AVFormatContext read_play();

        public native AVInputFormat read_play(Read_play_AVFormatContext read_play_AVFormatContext);

        public native Read_probe_AVProbeData read_probe();

        public native AVInputFormat read_probe(Read_probe_AVProbeData read_probe_AVProbeData);

        public native Read_seek_AVFormatContext_int_long_int read_seek();

        public native AVInputFormat read_seek(Read_seek_AVFormatContext_int_long_int read_seek_AVFormatContext_int_long_int);

        public native Read_seek2_AVFormatContext_int_long_long_long_int read_seek2();

        public native AVInputFormat read_seek2(Read_seek2_AVFormatContext_int_long_long_long_int read_seek2_AVFormatContext_int_long_long_long_int);

        public native Read_timestamp_AVFormatContext_int_LongPointer_long read_timestamp();

        public native AVInputFormat read_timestamp(Read_timestamp_AVFormatContext_int_LongPointer_long read_timestamp_AVFormatContext_int_LongPointer_long);

        static {
            Loader.load();
        }

        public AVInputFormat() {
            allocate();
        }

        public AVInputFormat(int size) {
            allocateArray(size);
        }

        public AVInputFormat(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVInputFormat position(int position) {
            return (AVInputFormat) super.position(position);
        }

        public static class Read_probe_AVProbeData extends FunctionPointer {
            private native void allocate();

            public native int call(AVProbeData aVProbeData);

            static {
                Loader.load();
            }

            public Read_probe_AVProbeData(Pointer p) {
                super(p);
            }

            protected Read_probe_AVProbeData() {
                allocate();
            }
        }

        public static class Read_header_AVFormatContext extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext);

            static {
                Loader.load();
            }

            public Read_header_AVFormatContext(Pointer p) {
                super(p);
            }

            protected Read_header_AVFormatContext() {
                allocate();
            }
        }

        public static class Read_packet_AVFormatContext_AVPacket extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext, avcodec.AVPacket aVPacket);

            static {
                Loader.load();
            }

            public Read_packet_AVFormatContext_AVPacket(Pointer p) {
                super(p);
            }

            protected Read_packet_AVFormatContext_AVPacket() {
                allocate();
            }
        }

        public static class Read_close_AVFormatContext extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext);

            static {
                Loader.load();
            }

            public Read_close_AVFormatContext(Pointer p) {
                super(p);
            }

            protected Read_close_AVFormatContext() {
                allocate();
            }
        }

        public static class Read_seek_AVFormatContext_int_long_int extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext, int i, long j, int i2);

            static {
                Loader.load();
            }

            public Read_seek_AVFormatContext_int_long_int(Pointer p) {
                super(p);
            }

            protected Read_seek_AVFormatContext_int_long_int() {
                allocate();
            }
        }

        public static class Read_timestamp_AVFormatContext_int_LongPointer_long extends FunctionPointer {
            private native void allocate();

            public native long call(AVFormatContext aVFormatContext, int i, LongPointer longPointer, long j);

            static {
                Loader.load();
            }

            public Read_timestamp_AVFormatContext_int_LongPointer_long(Pointer p) {
                super(p);
            }

            protected Read_timestamp_AVFormatContext_int_LongPointer_long() {
                allocate();
            }
        }

        public static class Read_play_AVFormatContext extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext);

            static {
                Loader.load();
            }

            public Read_play_AVFormatContext(Pointer p) {
                super(p);
            }

            protected Read_play_AVFormatContext() {
                allocate();
            }
        }

        public static class Read_pause_AVFormatContext extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext);

            static {
                Loader.load();
            }

            public Read_pause_AVFormatContext(Pointer p) {
                super(p);
            }

            protected Read_pause_AVFormatContext() {
                allocate();
            }
        }

        public static class Read_seek2_AVFormatContext_int_long_long_long_int extends FunctionPointer {
            private native void allocate();

            public native int call(AVFormatContext aVFormatContext, int i, long j, long j2, long j3, int i2);

            static {
                Loader.load();
            }

            public Read_seek2_AVFormatContext_int_long_long_long_int(Pointer p) {
                super(p);
            }

            protected Read_seek2_AVFormatContext_int_long_long_long_int() {
                allocate();
            }
        }
    }

    public static class AVIndexEntry extends Pointer {
        public static final int AVINDEX_KEYFRAME = 1;

        private native void allocate();

        private native void allocateArray(int i);

        @NoOffset
        public native int flags();

        public native AVIndexEntry flags(int i);

        public native int min_distance();

        public native AVIndexEntry min_distance(int i);

        public native long pos();

        public native AVIndexEntry pos(long j);

        @NoOffset
        public native int size();

        public native AVIndexEntry size(int i);

        public native long timestamp();

        public native AVIndexEntry timestamp(long j);

        static {
            Loader.load();
        }

        public AVIndexEntry() {
            allocate();
        }

        public AVIndexEntry(int size) {
            allocateArray(size);
        }

        public AVIndexEntry(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVIndexEntry position(int position) {
            return (AVIndexEntry) super.position(position);
        }
    }

    public static class AVStream extends Pointer {
        public static final int MAX_PROBE_PACKETS = 2500;
        public static final int MAX_REORDER_DELAY = 16;
        public static final int MAX_STD_TIMEBASES = 726;

        private native void allocate();

        private native void allocateArray(int i);

        @ByVal
        public native avcodec.AVPacket attached_pic();

        public native AVStream attached_pic(avcodec.AVPacket aVPacket);

        public native AVStream avg_frame_rate(avutil.AVRational aVRational);

        @ByVal
        public native avutil.AVRational avg_frame_rate();

        public native avcodec.AVCodecContext codec();

        public native AVStream codec(avcodec.AVCodecContext aVCodecContext);

        public native int codec_info_nb_frames();

        public native AVStream codec_info_nb_frames(int i);

        public native long cur_dts();

        public native AVStream cur_dts(long j);

        @Cast({"AVDiscard"})
        public native int discard();

        public native AVStream discard(int i);

        public native int disposition();

        public native AVStream disposition(int i);

        public native long duration();

        public native AVStream duration(long j);

        public native long first_dts();

        public native AVStream first_dts(long j);

        public native int id();

        public native AVStream id(int i);

        public native int index();

        public native AVStream index(int i);

        public native AVIndexEntry index_entries();

        public native AVStream index_entries(AVIndexEntry aVIndexEntry);

        @Cast({"unsigned int"})
        public native int index_entries_allocated_size();

        public native AVStream index_entries_allocated_size(int i);

        public native long interleaver_chunk_duration();

        public native AVStream interleaver_chunk_duration(long j);

        public native long interleaver_chunk_size();

        public native AVStream interleaver_chunk_size(long j);

        public native int last_IP_duration();

        public native AVStream last_IP_duration(int i);

        public native long last_IP_pts();

        public native AVStream last_IP_pts(long j);

        public native AVPacketList last_in_packet_buffer();

        public native AVStream last_in_packet_buffer(AVPacketList aVPacketList);

        public native AVStream metadata(avutil.AVDictionary aVDictionary);

        public native avutil.AVDictionary metadata();

        public native long mux_ts_offset();

        public native AVStream mux_ts_offset(long j);

        public native int nb_decoded_frames();

        public native AVStream nb_decoded_frames(int i);

        public native long nb_frames();

        public native AVStream nb_frames(long j);

        public native int nb_index_entries();

        public native AVStream nb_index_entries(int i);

        @Cast({"AVStreamParseType"})
        public native int need_parsing();

        public native AVStream need_parsing(int i);

        public native avcodec.AVCodecParserContext parser();

        public native AVStream parser(avcodec.AVCodecParserContext aVCodecParserContext);

        public native Pointer priv_data();

        public native AVStream priv_data(Pointer pointer);

        @ByVal
        public native AVProbeData probe_data();

        public native AVStream probe_data(AVProbeData aVProbeData);

        public native int probe_packets();

        public native AVStream probe_packets(int i);

        @ByVal
        public native AVFrac pts();

        public native AVStream pts(AVFrac aVFrac);

        public native long pts_buffer(int i);

        @MemberGetter
        public native LongPointer pts_buffer();

        public native AVStream pts_buffer(int i, long j);

        public native int pts_wrap_behavior();

        public native AVStream pts_wrap_behavior(int i);

        public native int pts_wrap_bits();

        public native AVStream pts_wrap_bits(int i);

        public native long pts_wrap_reference();

        public native AVStream pts_wrap_reference(long j);

        public native AVStream r_frame_rate(avutil.AVRational aVRational);

        @ByVal
        public native avutil.AVRational r_frame_rate();

        public native long reference_dts();

        public native AVStream reference_dts(long j);

        public native int request_probe();

        public native AVStream request_probe(int i);

        public native AVStream sample_aspect_ratio(avutil.AVRational aVRational);

        @ByVal
        public native avutil.AVRational sample_aspect_ratio();

        public native int skip_samples();

        public native AVStream skip_samples(int i);

        public native int skip_to_keyframe();

        public native AVStream skip_to_keyframe(int i);

        public native long start_time();

        public native AVStream start_time(long j);

        public native int stream_identifier();

        public native AVStream stream_identifier(int i);

        public native AVStream time_base(avutil.AVRational aVRational);

        @ByVal
        public native avutil.AVRational time_base();

        static {
            Loader.load();
        }

        public AVStream() {
            allocate();
        }

        public AVStream(int size) {
            allocateArray(size);
        }

        public AVStream(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVStream position(int position) {
            return (AVStream) super.position(position);
        }
    }

    public static class AVProgram extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        @Cast({"AVDiscard"})
        public native int discard();

        public native AVProgram discard(int i);

        public native long end_time();

        public native AVProgram end_time(long j);

        public native int flags();

        public native AVProgram flags(int i);

        public native int id();

        public native AVProgram id(int i);

        public native AVProgram metadata(avutil.AVDictionary aVDictionary);

        public native avutil.AVDictionary metadata();

        @Cast({"unsigned int"})
        public native int nb_stream_indexes();

        public native AVProgram nb_stream_indexes(int i);

        public native int pcr_pid();

        public native AVProgram pcr_pid(int i);

        public native int pmt_pid();

        public native AVProgram pmt_pid(int i);

        public native int program_num();

        public native AVProgram program_num(int i);

        public native int pts_wrap_behavior();

        public native AVProgram pts_wrap_behavior(int i);

        public native long pts_wrap_reference();

        public native AVProgram pts_wrap_reference(long j);

        public native long start_time();

        public native AVProgram start_time(long j);

        @Cast({"unsigned int*"})
        public native IntPointer stream_index();

        public native AVProgram stream_index(IntPointer intPointer);

        static {
            Loader.load();
        }

        public AVProgram() {
            allocate();
        }

        public AVProgram(int size) {
            allocateArray(size);
        }

        public AVProgram(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVProgram position(int position) {
            return (AVProgram) super.position(position);
        }
    }

    public static class AVChapter extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native long end();

        public native AVChapter end(long j);

        public native int id();

        public native AVChapter id(int i);

        public native AVChapter metadata(avutil.AVDictionary aVDictionary);

        public native avutil.AVDictionary metadata();

        public native long start();

        public native AVChapter start(long j);

        public native AVChapter time_base(avutil.AVRational aVRational);

        @ByVal
        public native avutil.AVRational time_base();

        static {
            Loader.load();
        }

        public AVChapter() {
            allocate();
        }

        public AVChapter(int size) {
            allocateArray(size);
        }

        public AVChapter(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVChapter position(int position) {
            return (AVChapter) super.position(position);
        }
    }

    public static class AVFormatContext extends Pointer {
        public static final int AVFMT_FLAG_CUSTOM_IO = 128;
        public static final int AVFMT_FLAG_DISCARD_CORRUPT = 256;
        public static final int AVFMT_FLAG_GENPTS = 1;
        public static final int AVFMT_FLAG_IGNDTS = 8;
        public static final int AVFMT_FLAG_IGNIDX = 2;
        public static final int AVFMT_FLAG_KEEP_SIDE_DATA = 262144;
        public static final int AVFMT_FLAG_MP4A_LATM = 32768;
        public static final int AVFMT_FLAG_NOBUFFER = 64;
        public static final int AVFMT_FLAG_NOFILLIN = 16;
        public static final int AVFMT_FLAG_NONBLOCK = 4;
        public static final int AVFMT_FLAG_NOPARSE = 32;
        public static final int AVFMT_FLAG_PRIV_OPT = 131072;
        public static final int AVFMT_FLAG_SORT_DTS = 65536;
        public static final int FF_FDEBUG_TS = 1;
        public static final int RAW_PACKET_BUFFER_SIZE = 2500000;

        private native void allocate();

        private native void allocateArray(int i);

        @Cast({"AVCodecID"})
        public native int audio_codec_id();

        public native AVFormatContext audio_codec_id(int i);

        public native int audio_preload();

        public native AVFormatContext audio_preload(int i);

        @MemberGetter
        @Const
        public native avutil.AVClass av_class();

        public native int avio_flags();

        public native AVFormatContext avio_flags(int i);

        public native int avoid_negative_ts();

        public native AVFormatContext avoid_negative_ts(int i);

        public native int bit_rate();

        public native AVFormatContext bit_rate(int i);

        @MemberGetter
        @Cast({"AVChapter**"})
        public native PointerPointer chapters();

        public native AVChapter chapters(int i);

        public native AVFormatContext chapters(int i, AVChapter aVChapter);

        @Cast({"unsigned int"})
        public native int correct_ts_overflow();

        public native AVFormatContext correct_ts_overflow(int i);

        public native int ctx_flags();

        public native AVFormatContext ctx_flags(int i);

        public native long data_offset();

        public native AVFormatContext data_offset(long j);

        public native int debug();

        public native AVFormatContext debug(int i);

        public native long duration();

        public native AVFormatContext duration(long j);

        @Cast({"AVDurationEstimationMethod"})
        public native int duration_estimation_method();

        public native AVFormatContext duration_estimation_method(int i);

        public native int error_recognition();

        public native AVFormatContext error_recognition(int i);

        @Cast({"char"})
        public native byte filename(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer filename();

        public native AVFormatContext filename(int i, byte b);

        public native int flags();

        public native AVFormatContext flags(int i);

        public native int flush_packets();

        public native AVFormatContext flush_packets(int i);

        public native int fps_probe_size();

        public native AVFormatContext fps_probe_size(int i);

        public native AVFormatContext iformat(AVInputFormat aVInputFormat);

        public native AVInputFormat iformat();

        public native AVFormatContext interrupt_callback(AVIOInterruptCB aVIOInterruptCB);

        @ByVal
        public native AVIOInterruptCB interrupt_callback();

        public native int io_repositioned();

        public native AVFormatContext io_repositioned(int i);

        @MemberGetter
        @Cast({"const uint8_t*"})
        public native BytePointer key();

        public native int keylen();

        public native AVFormatContext keylen(int i);

        public native int max_analyze_duration();

        public native AVFormatContext max_analyze_duration(int i);

        public native int max_chunk_duration();

        public native AVFormatContext max_chunk_duration(int i);

        public native int max_chunk_size();

        public native AVFormatContext max_chunk_size(int i);

        public native int max_delay();

        public native AVFormatContext max_delay(int i);

        @Cast({"unsigned int"})
        public native int max_index_size();

        public native AVFormatContext max_index_size(int i);

        @Cast({"unsigned int"})
        public native int max_picture_buffer();

        public native AVFormatContext max_picture_buffer(int i);

        public native AVFormatContext metadata(avutil.AVDictionary aVDictionary);

        public native avutil.AVDictionary metadata();

        @Cast({"unsigned int"})
        public native int nb_chapters();

        public native AVFormatContext nb_chapters(int i);

        @Cast({"unsigned int"})
        public native int nb_programs();

        public native AVFormatContext nb_programs(int i);

        @Cast({"unsigned int"})
        public native int nb_streams();

        public native AVFormatContext nb_streams(int i);

        public native long offset();

        public native AVFormatContext offset(long j);

        public native AVFormatContext offset_timebase(avutil.AVRational aVRational);

        @ByVal
        public native avutil.AVRational offset_timebase();

        public native AVFormatContext oformat(AVOutputFormat aVOutputFormat);

        public native AVOutputFormat oformat();

        public native AVFormatContext packet_buffer(AVPacketList aVPacketList);

        public native AVPacketList packet_buffer();

        public native AVFormatContext packet_buffer_end(AVPacketList aVPacketList);

        public native AVPacketList packet_buffer_end();

        @Cast({"unsigned int"})
        public native int packet_size();

        public native AVFormatContext packet_size(int i);

        public native AVFormatContext parse_queue(AVPacketList aVPacketList);

        public native AVPacketList parse_queue();

        public native AVFormatContext parse_queue_end(AVPacketList aVPacketList);

        public native AVPacketList parse_queue_end();

        public native AVFormatContext pb(AVIOContext aVIOContext);

        public native AVIOContext pb();

        public native Pointer priv_data();

        public native AVFormatContext priv_data(Pointer pointer);

        @Cast({"unsigned int"})
        public native int probesize();

        public native AVFormatContext probesize(int i);

        @MemberGetter
        @Cast({"AVProgram**"})
        public native PointerPointer programs();

        public native AVFormatContext programs(int i, AVProgram aVProgram);

        public native AVProgram programs(int i);

        public native AVFormatContext raw_packet_buffer(AVPacketList aVPacketList);

        public native AVPacketList raw_packet_buffer();

        public native AVFormatContext raw_packet_buffer_end(AVPacketList aVPacketList);

        public native AVPacketList raw_packet_buffer_end();

        public native int raw_packet_buffer_remaining_size();

        public native AVFormatContext raw_packet_buffer_remaining_size(int i);

        public native int seek2any();

        public native AVFormatContext seek2any(int i);

        @Cast({"unsigned int"})
        public native int skip_initial_bytes();

        public native AVFormatContext skip_initial_bytes(int i);

        public native long start_time();

        public native AVFormatContext start_time(long j);

        public native long start_time_realtime();

        public native AVFormatContext start_time_realtime(long j);

        @MemberGetter
        @Cast({"AVStream**"})
        public native PointerPointer streams();

        public native AVFormatContext streams(int i, AVStream aVStream);

        public native AVStream streams(int i);

        @Cast({"AVCodecID"})
        public native int subtitle_codec_id();

        public native AVFormatContext subtitle_codec_id(int i);

        public native int ts_id();

        public native AVFormatContext ts_id(int i);

        public native int use_wallclock_as_timestamps();

        public native AVFormatContext use_wallclock_as_timestamps(int i);

        @Cast({"AVCodecID"})
        public native int video_codec_id();

        public native AVFormatContext video_codec_id(int i);

        static {
            Loader.load();
        }

        public AVFormatContext() {
            allocate();
        }

        public AVFormatContext(int size) {
            allocateArray(size);
        }

        public AVFormatContext(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVFormatContext position(int position) {
            return (AVFormatContext) super.position(position);
        }
    }

    public static class AVPacketList extends Pointer {
        private native void allocate();

        private native void allocateArray(int i);

        public native AVPacketList next();

        public native AVPacketList next(AVPacketList aVPacketList);

        @ByVal
        public native avcodec.AVPacket pkt();

        public native AVPacketList pkt(avcodec.AVPacket aVPacket);

        static {
            Loader.load();
        }

        public AVPacketList() {
            allocate();
        }

        public AVPacketList(int size) {
            allocateArray(size);
        }

        public AVPacketList(Pointer p) {
            super(p);
        }

        @Override // com.googlecode.javacpp.Pointer
        public AVPacketList position(int position) {
            return (AVPacketList) super.position(position);
        }
    }
}
