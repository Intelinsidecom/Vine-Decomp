package co.vine.android.recorder;

import android.content.Context;
import co.vine.android.util.SystemUtil;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.DoublePointer;
import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.PointerPointer;
import com.googlecode.javacpp.ShortPointer;
import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avformat;
import com.googlecode.javacv.cpp.avutil;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.swresample;
import com.googlecode.javacv.cpp.swscale;
import java.io.File;
import java.io.Serializable;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class SwVineFrameRecorder {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final int[] LOCK;
    public static boolean hasEverSuccessfullyLoaded;
    private static Exception loadingException;
    protected int audioBitrate;
    protected int audioChannels;
    protected int audioCodec;
    protected String audioCodecName;
    private long audioRecordTime;
    private avcodec.AVCodecContext audio_c;
    private long audio_clock;
    private avcodec.AVCodec audio_codec;
    private int audio_input_frame_size;
    private BytePointer audio_outbuf;
    private int audio_outbuf_size;
    public avcodec.AVPacket audio_pkt;
    private avformat.AVStream audio_st;
    private String filename;
    protected String format;
    protected int fps;
    private avutil.AVFrame frame;
    protected int gop;
    private int[] got_audio_packet;
    private int[] got_video_packet;
    protected int imageHeight;
    protected int imageWidth;
    private swscale.SwsContext img_convert_ctx;
    protected boolean interleaved;
    private boolean isVorbis;
    protected AudioOutBuffer mAudioOutBuffer;
    private boolean mWasKeyFrame;
    private avformat.AVFormatContext oc;
    private avformat.AVOutputFormat oformat;
    private avutil.AVFrame picture;
    private BytePointer picture_buf;
    protected int pixelFormat;
    protected int sampleFormat;
    protected int sampleRate;
    private swresample.SwrContext samples_convert_ctx;
    private Pointer[] samples_in;
    private PointerPointer samples_in_ptr;
    private BytePointer[] samples_out;
    private PointerPointer samples_out_ptr;
    private avutil.AVFrame tmp_picture;
    protected int videoBitrate;
    protected int videoCodec;
    protected String videoCodecName;
    private long videoEncodeTime;
    private long videoScaleTime;
    private long videoWriteTime;
    private avcodec.AVCodecContext video_c;
    private avcodec.AVCodec video_codec;
    private BytePointer video_outbuf;
    private int video_outbuf_size;
    public avcodec.AVPacket video_pkt;
    private avformat.AVStream video_st;
    protected double videoQuality = -1.0d;
    protected double audioQuality = -1.0d;
    protected HashMap<String, String> videoOptions = new HashMap<>();
    protected HashMap<String, String> audioOptions = new HashMap<>();
    protected int frameNumber = 0;
    protected long timestamp = 0;
    private boolean hasData = false;
    private volatile boolean isReleased = false;

    static {
        $assertionsDisabled = !SwVineFrameRecorder.class.desiredAssertionStatus();
        LOCK = new int[0];
        loadingException = null;
        hasEverSuccessfullyLoaded = false;
        synchronized (LOCK) {
            avcodec.avcodec_register_all();
            avformat.av_register_all();
        }
    }

    public static class AudioOutBuffer implements Serializable {
        public final byte[] data;
        public final ArrayList<Integer> sizes;

        public int getPosition() {
            int size = 0;
            Iterator<Integer> it = this.sizes.iterator();
            while (it.hasNext()) {
                Integer s = it.next();
                size += s.intValue();
            }
            return size;
        }
    }

    public static void tryLoad(Context context) throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(avutil.class);
            Loader.load(avcodec.class);
            Loader.load(avformat.class);
            Loader.load(swscale.class);
            Loader.load(swresample.class);
            hasEverSuccessfullyLoaded = true;
            RecordConfigUtils.setLoadWasEverSuccessful(context, true);
        } catch (Throwable t) {
            if (t instanceof Exception) {
                Exception exception = (Exception) t;
                loadingException = exception;
                throw exception;
            }
            Exception exception2 = new Exception("Failed to load " + SwVineFrameRecorder.class, t);
            loadingException = exception2;
            throw exception2;
        }
    }

    public SwVineFrameRecorder(String filename, int imageWidth, int imageHeight, int audioChannels, avcodec.AVPacket videoPacket, avcodec.AVPacket audioPacket) {
        SLog.d("Creating a recorder thats {} * {}", Integer.valueOf(imageWidth), Integer.valueOf(imageHeight));
        this.filename = filename;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.audioChannels = audioChannels;
        this.pixelFormat = -1;
        this.videoCodec = 0;
        this.videoBitrate = RecordConfigUtils.VIDEO_BIT_RATE;
        this.fps = 30;
        this.gop = RecordConfigUtils.getGopFromKps(3.0d, this.fps);
        this.sampleFormat = -1;
        this.audioCodec = 0;
        this.audioBitrate = RecordConfigUtils.AUDIO_BIT_RATE;
        this.sampleRate = 44100;
        this.interleaved = true;
        this.video_pkt = videoPacket == null ? new avcodec.AVPacket() : videoPacket;
        this.audio_pkt = audioPacket == null ? new avcodec.AVPacket() : audioPacket;
    }

    public void release() {
        if (SLog.sLogsOn && !this.isReleased) {
            if (this.videoScaleTime > 0) {
                SLog.b("Video Scale: {}.", Long.valueOf(this.videoScaleTime));
            }
            if (this.videoEncodeTime > 0) {
                SLog.b("Video Encode: {}.", Long.valueOf(this.videoEncodeTime));
            }
            if (this.videoWriteTime > 0) {
                SLog.b("Video Write: {}.", Long.valueOf(this.videoWriteTime));
            }
            if (this.audioRecordTime > 0) {
                SLog.b("Audio Record: {}.", Long.valueOf(this.audioRecordTime));
            }
        }
        this.isReleased = true;
        synchronized (LOCK) {
            if (this.video_c != null) {
                avcodec.avcodec_close(this.video_c);
                this.video_c = null;
            }
            if (this.audio_c != null) {
                avcodec.avcodec_close(this.audio_c);
                this.audio_c = null;
            }
            if (this.picture_buf != null) {
                avutil.av_free(this.picture_buf);
                this.picture_buf = null;
            }
            if (this.picture != null) {
                avcodec.avcodec_free_frame(this.picture);
                this.picture = null;
            }
            if (this.tmp_picture != null) {
                avcodec.avcodec_free_frame(this.tmp_picture);
                this.tmp_picture = null;
            }
            if (this.video_outbuf != null) {
                avutil.av_free(this.video_outbuf);
                this.video_outbuf = null;
            }
            if (this.frame != null) {
                avcodec.avcodec_free_frame(this.frame);
                this.frame = null;
            }
            if (this.samples_out != null) {
                for (int i = 0; i < this.samples_out.length; i++) {
                    avutil.av_free(this.samples_out[i].position(0));
                }
                this.samples_out = null;
            }
            if (this.audio_outbuf != null) {
                avutil.av_free(this.audio_outbuf);
                this.audio_outbuf = null;
            }
            this.video_st = null;
            this.audio_st = null;
            if (this.oc != null) {
                int nb_streams = this.oc.nb_streams();
                for (int i2 = 0; i2 < nb_streams; i2++) {
                    avutil.av_free(this.oc.streams(i2).codec());
                    avutil.av_free(this.oc.streams(i2));
                }
                avutil.av_free(this.oc);
                this.oc = null;
            }
            if (this.img_convert_ctx != null) {
                swscale.sws_freeContext(this.img_convert_ctx);
                this.img_convert_ctx = null;
            }
            if (this.samples_convert_ctx != null) {
                swresample.swr_free(this.samples_convert_ctx);
                this.samples_convert_ctx = null;
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public String getFilename() {
        return this.filename;
    }

    public int getFrameNumber() {
        return this.picture == null ? (int) this.audio_pkt.pts() : (int) this.picture.pts();
    }

    public void setFrameNumber(int frameNumber) {
        if (this.picture == null) {
            this.frameNumber = frameNumber;
        } else {
            this.picture.pts(frameNumber);
        }
    }

    public long getTimestamp() {
        return this.picture == null ? (this.audio_clock * 1000000) / this.sampleRate : Math.round((getFrameNumber() * 1000000) / getFps());
    }

    public int setTimestampAndGetFrameNumber(long timestamp) {
        int fn = (int) Math.round((timestamp * getFps()) / 1000000.0d);
        setFrameNumber(fn);
        return fn;
    }

    public void makeAudioAheadOfVideo() throws Exception {
        if (this.picture != null && this.audio_c != null) {
            long nextFrameNumber = this.picture.pts();
            int x = (int) (this.sampleRate * ((nextFrameNumber / getFps()) - (this.audio_clock / this.sampleRate)));
            if (x > 0) {
                SLog.w("Making sure audio is ahead BAD: {}.", Integer.valueOf(x));
                record(ShortBuffer.wrap(new short[x]));
            } else {
                SLog.i("Making sure audio is ahead GOOD: {}.", Integer.valueOf(x));
            }
        }
    }

    public void start() throws Exception {
        this.picture = null;
        this.tmp_picture = null;
        this.picture_buf = null;
        this.frame = null;
        this.video_outbuf = null;
        this.audio_outbuf = null;
        this.oc = null;
        this.video_c = null;
        this.audio_c = null;
        this.video_st = null;
        this.audio_st = null;
        this.got_video_packet = new int[1];
        this.got_audio_packet = new int[1];
        this.videoEncodeTime = 0L;
        this.videoWriteTime = 0L;
        this.videoScaleTime = 0L;
        this.audioRecordTime = 0L;
        String format_name = (this.format == null || this.format.length() == 0) ? null : this.format;
        avformat.AVOutputFormat aVOutputFormatAv_guess_format = avformat.av_guess_format(format_name, this.filename, (String) null);
        this.oformat = aVOutputFormatAv_guess_format;
        if (aVOutputFormatAv_guess_format == null) {
            int proto = this.filename.indexOf("://");
            if (proto > 0) {
                format_name = this.filename.substring(0, proto);
            }
            avformat.AVOutputFormat aVOutputFormatAv_guess_format2 = avformat.av_guess_format(format_name, this.filename, (String) null);
            this.oformat = aVOutputFormatAv_guess_format2;
            if (aVOutputFormatAv_guess_format2 == null) {
                throw new Exception("av_guess_format() error: Could not guess output format for \"" + this.filename + "\" and " + this.format + " format.");
            }
        }
        String format_name2 = this.oformat.name().getString();
        avformat.AVFormatContext aVFormatContextAvformat_alloc_context = avformat.avformat_alloc_context();
        this.oc = aVFormatContextAvformat_alloc_context;
        if (aVFormatContextAvformat_alloc_context == null) {
            throw new Exception("avformat_alloc_context() error: Could not allocate format context");
        }
        this.oc.oformat(this.oformat);
        this.oc.filename().putString(this.filename);
        if (this.imageWidth > 0 && this.imageHeight > 0) {
            if (this.videoCodec != 0) {
                this.oformat.video_codec(this.videoCodec);
            } else if ("flv".equals(format_name2)) {
                this.oformat.video_codec(22);
            } else if ("mp4".equals(format_name2)) {
                this.oformat.video_codec(13);
            } else if ("3gp".equals(format_name2)) {
                this.oformat.video_codec(5);
            } else if ("avi".equals(format_name2)) {
                this.oformat.video_codec(26);
            }
            avcodec.AVCodec namedCodec = avcodec.avcodec_find_encoder_by_name(this.videoCodecName);
            this.video_codec = namedCodec;
            if (namedCodec == null) {
                avcodec.AVCodec aVCodecAvcodec_find_encoder = avcodec.avcodec_find_encoder(this.oformat.video_codec());
                this.video_codec = aVCodecAvcodec_find_encoder;
                if (aVCodecAvcodec_find_encoder == null) {
                    release();
                    throw new Exception("avcodec_find_encoder() error: Video codec not found.");
                }
            }
            if (namedCodec != null) {
                this.oformat.video_codec(this.video_codec.id());
            }
            avutil.AVRational frame_rate = avutil.av_d2q(this.fps, 1001000);
            avutil.AVRational supported_framerates = this.video_codec.supported_framerates();
            if (supported_framerates != null) {
                int idx = avutil.av_find_nearest_q_idx(frame_rate, supported_framerates);
                frame_rate = supported_framerates.position(idx);
            }
            avformat.AVStream aVStreamAvformat_new_stream = avformat.avformat_new_stream(this.oc, this.video_codec);
            this.video_st = aVStreamAvformat_new_stream;
            if (aVStreamAvformat_new_stream == null) {
                release();
                throw new Exception("avformat_new_stream() error: Could not allocate video stream.");
            }
            this.video_c = this.video_st.codec();
            this.video_c.codec_id(this.oformat.video_codec());
            this.video_c.codec_type(0);
            this.video_c.bit_rate(this.videoBitrate);
            this.video_c.width(((this.imageWidth + 15) / 16) * 16);
            this.video_c.height(this.imageHeight);
            this.video_c.time_base(avutil.av_inv_q(frame_rate));
            this.video_c.gop_size(this.gop);
            if (this.videoQuality >= 0.0d) {
                this.video_c.flags(this.video_c.flags() | 2);
                this.video_c.global_quality((int) Math.round(118.0d * this.videoQuality));
            }
            if (this.pixelFormat != -1) {
                this.video_c.pix_fmt(this.pixelFormat);
            } else if (this.video_c.codec_id() == 14 || this.video_c.codec_id() == 62 || this.video_c.codec_id() == 26 || this.video_c.codec_id() == 34) {
                this.video_c.pix_fmt(avutil.AV_PIX_FMT_RGB32);
            } else {
                this.video_c.pix_fmt(0);
            }
            if (this.video_c.codec_id() == 2) {
                this.video_c.max_b_frames(2);
            } else if (this.video_c.codec_id() == 1) {
                this.video_c.mb_decision(2);
            } else if (this.video_c.codec_id() == 5) {
                if (this.imageWidth <= 128 && this.imageHeight <= 96) {
                    this.video_c.width(128).height(96);
                } else if (this.imageWidth <= 176 && this.imageHeight <= 144) {
                    this.video_c.width(176).height(144);
                } else if (this.imageWidth <= 352 && this.imageHeight <= 288) {
                    this.video_c.width(352).height(288);
                } else if (this.imageWidth <= 704 && this.imageHeight <= 576) {
                    this.video_c.width(704).height(576);
                } else {
                    this.video_c.width(1408).height(1152);
                }
            } else if (this.video_c.codec_id() == 28) {
                this.video_c.profile(avcodec.AVCodecContext.FF_PROFILE_H264_CONSTRAINED_BASELINE);
            }
            if ((this.oformat.flags() & 64) != 0) {
                this.video_c.flags(this.video_c.flags() | 4194304);
            }
            if ((this.video_codec.capabilities() & 512) != 0) {
                this.video_c.strict_std_compliance(-2);
            }
        }
        if (this.audioChannels > 0 && this.audioBitrate > 0 && this.sampleRate > 0) {
            if (this.audioCodec != 0) {
                this.oformat.audio_codec(this.audioCodec);
            } else if ("flv".equals(format_name2) || "mp4".equals(format_name2) || "3gp".equals(format_name2)) {
                this.oformat.audio_codec(avcodec.AV_CODEC_ID_AAC);
            } else if ("avi".equals(format_name2)) {
                this.oformat.audio_codec(65536);
            }
            avcodec.AVCodec aVCodecAvcodec_find_encoder_by_name = avcodec.avcodec_find_encoder_by_name(this.audioCodecName);
            this.audio_codec = aVCodecAvcodec_find_encoder_by_name;
            if (aVCodecAvcodec_find_encoder_by_name == null) {
                avcodec.AVCodec aVCodecAvcodec_find_encoder2 = avcodec.avcodec_find_encoder(this.oformat.audio_codec());
                this.audio_codec = aVCodecAvcodec_find_encoder2;
                if (aVCodecAvcodec_find_encoder2 == null) {
                    release();
                    throw new Exception("avcodec_find_encoder() error: Audio codec not found.");
                }
            }
            avformat.AVStream aVStreamAvformat_new_stream2 = avformat.avformat_new_stream(this.oc, this.audio_codec);
            this.audio_st = aVStreamAvformat_new_stream2;
            if (aVStreamAvformat_new_stream2 == null) {
                release();
                throw new Exception("avformat_new_stream() error: Could not allocate audio stream.");
            }
            this.isVorbis = "libvorbis".equals(this.audioCodecName);
            this.audio_c = this.audio_st.codec();
            this.audio_c.codec_id(this.oformat.audio_codec());
            this.audio_c.codec_type(1);
            this.audio_c.bit_rate(this.audioBitrate);
            this.audio_c.sample_rate(this.sampleRate);
            this.audio_c.channels(this.audioChannels);
            this.audio_c.channel_layout(avutil.av_get_default_channel_layout(this.audioChannels));
            if (this.sampleFormat != -1) {
                this.audio_c.sample_fmt(this.sampleFormat);
            } else if (this.isVorbis || (this.audio_c.codec_id() == 86018 && (this.audio_codec.capabilities() & 512) != 0)) {
                this.audio_c.sample_fmt(8);
            } else {
                this.audio_c.sample_fmt(1);
            }
            this.audio_c.time_base().num(1).den(this.sampleRate);
            switch (this.audio_c.sample_fmt()) {
                case 0:
                case 5:
                    this.audio_c.bits_per_raw_sample(8);
                    break;
                case 1:
                case 6:
                    this.audio_c.bits_per_raw_sample(16);
                    break;
                case 2:
                case 7:
                    this.audio_c.bits_per_raw_sample(32);
                    break;
                case 3:
                case 8:
                    this.audio_c.bits_per_raw_sample(32);
                    break;
                case 4:
                case 9:
                    this.audio_c.bits_per_raw_sample(64);
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            if (this.audioQuality >= 0.0d) {
                this.audio_c.flags(this.audio_c.flags() | 2);
                this.audio_c.global_quality((int) Math.round(118.0d * this.audioQuality));
            }
            if ((this.oformat.flags() & 64) != 0) {
                this.audio_c.flags(this.audio_c.flags() | 4194304);
            }
            if ((this.audio_codec.capabilities() & 512) != 0) {
                this.audio_c.strict_std_compliance(-2);
            }
        }
        avformat.av_dump_format(this.oc, 0, this.filename, 1);
        if (this.video_st != null) {
            avutil.AVDictionary options = new avutil.AVDictionary(null);
            if (this.videoQuality >= 0.0d) {
                avutil.av_dict_set(options, "crf", "" + this.videoQuality, 0);
            }
            for (Map.Entry<String, String> e : this.videoOptions.entrySet()) {
                avutil.av_dict_set(options, e.getKey(), e.getValue(), 0);
            }
            synchronized (LOCK) {
                int ret = avcodec.avcodec_open2(this.video_c, this.video_codec, options);
                if (ret < 0) {
                    release();
                    throw new Exception("avcodec_open2() error " + ret + ": Could not open video codec.");
                }
            }
            avutil.av_dict_free(options);
            this.video_outbuf = null;
            if ((this.oformat.flags() & 32) == 0) {
                this.video_outbuf_size = Math.max(262144, this.video_c.width() * 8 * this.video_c.height());
                this.video_outbuf = new BytePointer(avutil.av_malloc(this.video_outbuf_size));
            }
            avutil.AVFrame aVFrameAvcodec_alloc_frame = avcodec.avcodec_alloc_frame();
            this.picture = aVFrameAvcodec_alloc_frame;
            if (aVFrameAvcodec_alloc_frame == null) {
                release();
                throw new Exception("avcodec_alloc_frame() error: Could not allocate picture.");
            }
            this.picture.pts(0L);
            int size = avcodec.avpicture_get_size(this.video_c.pix_fmt(), this.video_c.width(), this.video_c.height());
            BytePointer bytePointer = new BytePointer(avutil.av_malloc(size));
            this.picture_buf = bytePointer;
            if (bytePointer.isNull()) {
                release();
                throw new Exception("av_malloc() error: Could not allocate picture buffer.");
            }
            avutil.AVFrame aVFrameAvcodec_alloc_frame2 = avcodec.avcodec_alloc_frame();
            this.tmp_picture = aVFrameAvcodec_alloc_frame2;
            if (aVFrameAvcodec_alloc_frame2 == null) {
                release();
                throw new Exception("avcodec_alloc_frame() error: Could not allocate temporary picture.");
            }
        }
        if (this.audio_st != null) {
            avutil.AVDictionary options2 = new avutil.AVDictionary(null);
            if (this.audioQuality >= 0.0d) {
                avutil.av_dict_set(options2, "crf", "" + this.audioQuality, 0);
            }
            for (Map.Entry<String, String> e2 : this.audioOptions.entrySet()) {
                avutil.av_dict_set(options2, e2.getKey(), e2.getValue(), 0);
            }
            synchronized (LOCK) {
                int ret2 = avcodec.avcodec_open2(this.audio_c, this.audio_codec, options2);
                if (ret2 < 0) {
                    release();
                    throw new Exception("avcodec_open2() error " + ret2 + ": Could not open audio codec.");
                }
            }
            avutil.av_dict_free(options2);
            this.audio_outbuf_size = 262144;
            this.audio_outbuf = new BytePointer(avutil.av_malloc(this.audio_outbuf_size));
            if (this.audio_c.frame_size() <= 1) {
                this.audio_outbuf_size = 16384;
                this.audio_input_frame_size = this.audio_outbuf_size / this.audio_c.channels();
                switch (this.audio_c.codec_id()) {
                    case 65536:
                    case avcodec.AV_CODEC_ID_PCM_S16BE /* 65537 */:
                    case avcodec.AV_CODEC_ID_PCM_U16LE /* 65538 */:
                    case avcodec.AV_CODEC_ID_PCM_U16BE /* 65539 */:
                        this.audio_input_frame_size >>= 1;
                        break;
                }
            } else {
                this.audio_input_frame_size = this.audio_c.frame_size();
            }
            int planes = avutil.av_sample_fmt_is_planar(this.audio_c.sample_fmt()) != 0 ? this.audio_c.channels() : 1;
            int data_size = avutil.av_samples_get_buffer_size((IntPointer) null, this.audio_c.channels(), this.audio_input_frame_size, this.audio_c.sample_fmt(), 1) / planes;
            this.samples_out = new BytePointer[planes];
            for (int i = 0; i < this.samples_out.length; i++) {
                this.samples_out[i] = new BytePointer(avutil.av_malloc(data_size)).capacity(data_size);
            }
            this.samples_in = new Pointer[8];
            this.samples_in_ptr = new PointerPointer(8);
            this.samples_out_ptr = new PointerPointer(8);
            avutil.AVFrame aVFrameAvcodec_alloc_frame3 = avcodec.avcodec_alloc_frame();
            this.frame = aVFrameAvcodec_alloc_frame3;
            if (aVFrameAvcodec_alloc_frame3 == null) {
                release();
                throw new Exception("avcodec_alloc_frame() error: Could not allocate audio frame.");
            }
        }
        SystemUtil.quietlyEnsureParentExists(new File(this.filename));
        if ((this.oformat.flags() & 1) == 0) {
            avformat.AVIOContext pb = new avformat.AVIOContext((Pointer) null);
            int ret3 = avformat.avio_open(pb, this.filename, 2);
            if (ret3 < 0) {
                release();
                throw new Exception("BAD avio_open error() error " + ret3 + ": Could not open '" + this.filename);
            }
            this.oc.pb(pb);
        }
        avformat.avformat_write_header(this.oc, (PointerPointer) null);
    }

    public void stopEncoding() throws Exception {
        if (this.oc != null && (this.oformat.flags() & 1) == 0) {
            avformat.avio_close(this.oc.pb());
        }
        release();
    }

    public void stop() throws Exception {
        if (this.oc != null) {
            while (this.video_st != null && record(null, -1, false)) {
            }
            while (this.audio_st != null && record((avutil.AVFrame) null)) {
            }
            if (this.interleaved && this.video_st != null && this.audio_st != null) {
                avformat.av_interleaved_write_frame(this.oc, null);
            } else {
                avformat.av_write_frame(this.oc, null);
            }
            this.oc.duration((this.audio_clock * 1000) / this.sampleRate);
            avformat.av_write_trailer(this.oc);
            if ((this.oformat.flags() & 1) == 0) {
                avformat.avio_close(this.oc.pb());
            }
        }
        release();
    }

    public int encode(opencv_core.IplImage image, byte[] output, int start) throws Exception {
        BytePointer data;
        if (!record(image, -1, true) || (data = this.video_pkt.data()) == null) {
            return -1;
        }
        int limit = this.video_pkt.size();
        data.get(output, start, limit);
        return limit;
    }

    public void writeEncodedImage(byte[] data, int start, int length, boolean keyFrame) throws Exception {
        this.hasData = true;
        avcodec.av_init_packet(this.video_pkt);
        this.video_pkt.data(this.video_outbuf);
        BytePointer pData = this.video_pkt.data();
        pData.position(0);
        pData.put(data, start, length);
        pData.limit(length);
        this.video_pkt.size(length);
        this.video_pkt.pts(this.picture.pts());
        this.video_pkt.dts(this.video_pkt.pts());
        this.picture.pts(this.picture.pts() + 1);
        if (this.video_pkt.pts() != avutil.AV_NOPTS_VALUE) {
            this.video_pkt.pts(avutil.av_rescale_q(this.video_pkt.pts(), this.video_c.time_base(), this.video_st.time_base()));
        }
        if (this.video_pkt.dts() != avutil.AV_NOPTS_VALUE) {
            this.video_pkt.dts(avutil.av_rescale_q(this.video_pkt.dts(), this.video_c.time_base(), this.video_st.time_base()));
        }
        this.video_pkt.stream_index(this.video_st.index());
        if (keyFrame) {
            this.video_pkt.flags(this.video_pkt.flags() | 1);
        }
        writeVideoFrame();
    }

    private int writeVideoFrame() throws Exception {
        int ret;
        long start = System.currentTimeMillis();
        synchronized (this.oc) {
            if (this.interleaved && this.audio_st != null) {
                ret = avformat.av_interleaved_write_frame(this.oc, this.video_pkt);
                if (ret < 0) {
                    throw new Exception("av_interleaved_write_frame() error " + ret + " while writing interleaved video frame.");
                }
            } else {
                ret = avformat.av_write_frame(this.oc, this.video_pkt);
                if (ret < 0) {
                    throw new Exception("av_write_frame() error " + ret + " while writing video frame.");
                }
            }
            this.videoWriteTime += System.currentTimeMillis() - start;
        }
        return ret;
    }

    public boolean record(opencv_core.IplImage image, int pixelFormat, boolean encodeOnly) throws Exception {
        if (this.video_st == null) {
            throw new Exception("No video output stream (Is imageWidth > 0 && imageHeight > 0 and has start() been called?)");
        }
        if (image != null) {
            this.hasData = true;
            int width = image.width();
            int height = image.height();
            int step = image.widthStep();
            BytePointer data = image.imageData();
            if (pixelFormat == -1) {
                int depth = image.depth();
                int channels = image.nChannels();
                if ((depth == 8 || depth == -2147483640) && channels == 3) {
                    pixelFormat = 3;
                } else if ((depth == 8 || depth == -2147483640) && channels == 1) {
                    pixelFormat = 8;
                } else if ((depth == 16 || depth == -2147483632) && channels == 1) {
                    pixelFormat = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN) ? 31 : 32;
                } else if ((depth == 8 || depth == -2147483640) && channels == 4) {
                    pixelFormat = 28;
                } else if ((depth == 8 || depth == -2147483640) && channels == 2) {
                    pixelFormat = 26;
                    step = width;
                } else {
                    throw new Exception("Could not guess pixel format of image: depth=" + depth + ", channels=" + channels);
                }
            }
            long start = System.currentTimeMillis();
            if (this.video_c.pix_fmt() != pixelFormat || this.video_c.width() != width || this.video_c.height() != height) {
                this.img_convert_ctx = swscale.sws_getCachedContext(this.img_convert_ctx, width, height, pixelFormat, this.video_c.width(), this.video_c.height(), this.video_c.pix_fmt(), 2, (swscale.SwsFilter) null, (swscale.SwsFilter) null, (DoublePointer) null);
                if (this.img_convert_ctx == null) {
                    throw new Exception("sws_getCachedContext() error: Cannot initialize the conversion context.");
                }
                avcodec.avpicture_fill(new avcodec.AVPicture(this.tmp_picture), data, pixelFormat, width, height);
                avcodec.avpicture_fill(new avcodec.AVPicture(this.picture), this.picture_buf, this.video_c.pix_fmt(), this.video_c.width(), this.video_c.height());
                this.tmp_picture.linesize(0, step);
                swscale.sws_scale(this.img_convert_ctx, new PointerPointer(this.tmp_picture), this.tmp_picture.linesize(), 0, height, new PointerPointer(this.picture), this.picture.linesize());
                this.videoScaleTime += System.currentTimeMillis() - start;
            } else {
                avcodec.avpicture_fill(new avcodec.AVPicture(this.picture), data, pixelFormat, width, height);
                this.picture.linesize(0, step);
            }
        }
        if ((this.oformat.flags() & 32) != 0) {
            if (image == null) {
                return false;
            }
            avcodec.av_init_packet(this.video_pkt);
            this.video_pkt.flags(this.video_pkt.flags() | 1);
            this.video_pkt.stream_index(this.video_st.index());
            this.video_pkt.data(new BytePointer(this.picture));
            this.video_pkt.size(Loader.sizeof(avcodec.AVPicture.class));
        } else {
            avcodec.av_init_packet(this.video_pkt);
            this.video_pkt.data(this.video_outbuf);
            this.video_pkt.size(this.video_outbuf_size);
            this.picture.quality(this.video_c.global_quality());
            long start2 = System.currentTimeMillis();
            int ret = avcodec.avcodec_encode_video2(this.video_c, this.video_pkt, image == null ? null : this.picture, this.got_video_packet);
            if (ret < 0) {
                throw new Exception("avcodec_encode_video2() error " + ret + ": Could not encode video packet.");
            }
            this.picture.pts(this.picture.pts() + 1);
            if (this.got_video_packet[0] != 0) {
                this.mWasKeyFrame = (this.video_pkt.flags() & 1) == 1;
                SLog.i("Key frame: {}.", Boolean.valueOf(this.mWasKeyFrame));
                if (!encodeOnly) {
                    if (this.video_pkt.pts() != avutil.AV_NOPTS_VALUE) {
                        this.video_pkt.pts(avutil.av_rescale_q(this.video_pkt.pts(), this.video_c.time_base(), this.video_st.time_base()));
                    }
                    if (this.video_pkt.dts() != avutil.AV_NOPTS_VALUE) {
                        this.video_pkt.dts(avutil.av_rescale_q(this.video_pkt.dts(), this.video_c.time_base(), this.video_st.time_base()));
                    }
                    this.video_pkt.stream_index(this.video_st.index());
                }
                this.videoEncodeTime += System.currentTimeMillis() - start2;
            } else {
                return false;
            }
        }
        if (!encodeOnly) {
            writeVideoFrame();
        }
        return true;
    }

    public boolean wasLastEncodedFrameKeyFrame() {
        return this.mWasKeyFrame;
    }

    public void setVideoCodecName(String videoCodecName) {
        this.videoCodecName = videoCodecName;
    }

    public void setAudioCodecName(String audioCodecName) {
        this.audioCodecName = audioCodecName;
    }

    public boolean record(Buffer... samples) throws Exception {
        int inputFormat;
        int inputDepth;
        if (this.audio_st == null) {
            throw new Exception("No audio output stream (Is audioChannels > 0 and has start() been called?)");
        }
        this.hasData = true;
        long start = System.currentTimeMillis();
        int inputSize = samples[0].limit() - samples[0].position();
        int inputChannels = samples.length > 1 ? 1 : this.audioChannels;
        int outputFormat = this.audio_c.sample_fmt();
        int outputChannels = this.samples_out.length > 1 ? 1 : this.audioChannels;
        int outputDepth = avutil.av_get_bytes_per_sample(outputFormat);
        if (samples[0] instanceof ByteBuffer) {
            inputFormat = samples.length > 1 ? 5 : 0;
            inputDepth = 1;
            for (int i = 0; i < samples.length; i++) {
                ByteBuffer b = (ByteBuffer) samples[i];
                if ((this.samples_in[i] instanceof BytePointer) && this.samples_in[i].capacity() >= inputSize && b.hasArray()) {
                    ((BytePointer) this.samples_in[i]).position(0).put(b.array(), b.position(), inputSize);
                } else {
                    this.samples_in[i] = new BytePointer(b);
                }
            }
        } else if (samples[0] instanceof ShortBuffer) {
            inputFormat = samples.length > 1 ? 6 : 1;
            inputDepth = 2;
            for (int i2 = 0; i2 < samples.length; i2++) {
                ShortBuffer b2 = (ShortBuffer) samples[i2];
                if ((this.samples_in[i2] instanceof ShortPointer) && this.samples_in[i2].capacity() >= inputSize && b2.hasArray()) {
                    ((ShortPointer) this.samples_in[i2]).position(0).put(b2.array(), samples[i2].position(), inputSize);
                } else {
                    this.samples_in[i2] = new ShortPointer(b2);
                }
            }
        } else if (samples[0] instanceof IntBuffer) {
            inputFormat = samples.length > 1 ? 7 : 2;
            inputDepth = 4;
            for (int i3 = 0; i3 < samples.length; i3++) {
                IntBuffer b3 = (IntBuffer) samples[i3];
                if ((this.samples_in[i3] instanceof IntPointer) && this.samples_in[i3].capacity() >= inputSize && b3.hasArray()) {
                    ((IntPointer) this.samples_in[i3]).position(0).put(b3.array(), samples[i3].position(), inputSize);
                } else {
                    this.samples_in[i3] = new IntPointer(b3);
                }
            }
        } else if (samples[0] instanceof FloatBuffer) {
            inputFormat = samples.length > 1 ? 8 : 3;
            inputDepth = 4;
            for (int i4 = 0; i4 < samples.length; i4++) {
                FloatBuffer b4 = (FloatBuffer) samples[i4];
                if ((this.samples_in[i4] instanceof FloatPointer) && this.samples_in[i4].capacity() >= inputSize && b4.hasArray()) {
                    ((FloatPointer) this.samples_in[i4]).position(0).put(b4.array(), b4.position(), inputSize);
                } else {
                    this.samples_in[i4] = new FloatPointer(b4);
                }
            }
        } else if (samples[0] instanceof DoubleBuffer) {
            inputFormat = samples.length > 1 ? 9 : 4;
            inputDepth = 8;
            for (int i5 = 0; i5 < samples.length; i5++) {
                DoubleBuffer b5 = (DoubleBuffer) samples[i5];
                if ((this.samples_in[i5] instanceof DoublePointer) && this.samples_in[i5].capacity() >= inputSize && b5.hasArray()) {
                    ((DoublePointer) this.samples_in[i5]).position(0).put(b5.array(), b5.position(), inputSize);
                } else {
                    this.samples_in[i5] = new DoublePointer(b5);
                }
            }
        } else {
            throw new Exception("Audio samples Buffer has unsupported type: " + samples);
        }
        if (this.samples_convert_ctx == null) {
            this.samples_convert_ctx = swresample.swr_alloc_set_opts(null, this.audio_c.channel_layout(), outputFormat, this.audio_c.sample_rate(), this.audio_c.channel_layout(), inputFormat, this.audio_c.sample_rate(), 0, null);
            if (this.samples_convert_ctx == null) {
                throw new Exception("swr_alloc_set_opts() error: Cannot allocate the conversion context.");
            }
            int ret = swresample.swr_init(this.samples_convert_ctx);
            if (ret < 0) {
                throw new Exception("swr_init() error " + ret + ": Cannot initialize the conversion context.");
            }
        }
        for (int i6 = 0; i6 < samples.length; i6++) {
            this.samples_in[i6].position(this.samples_in[i6].position() * inputDepth).limit((this.samples_in[i6].position() + inputSize) * inputDepth);
        }
        while (this.samples_in[0].position() < this.samples_in[0].limit()) {
            int inputCount = (this.samples_in[0].limit() - this.samples_in[0].position()) / (inputChannels * inputDepth);
            int outputCount = (this.samples_out[0].limit() - this.samples_out[0].position()) / (outputChannels * outputDepth);
            int count = Math.min(inputCount, outputCount);
            for (int i7 = 0; i7 < samples.length; i7++) {
                this.samples_in_ptr.put(i7, this.samples_in[i7]);
            }
            for (int i8 = 0; i8 < this.samples_out.length; i8++) {
                this.samples_out_ptr.put(i8, this.samples_out[i8]);
            }
            int ret2 = swresample.swr_convert(this.samples_convert_ctx, this.samples_out_ptr, count, this.samples_in_ptr, count);
            if (ret2 < 0) {
                throw new Exception("swr_convert() error " + ret2 + ": Cannot convert audio samples.");
            }
            for (int i9 = 0; i9 < samples.length; i9++) {
                this.samples_in[i9].position(this.samples_in[i9].position() + (ret2 * inputChannels * inputDepth));
            }
            for (int i10 = 0; i10 < this.samples_out.length; i10++) {
                this.samples_out[i10].position(this.samples_out[i10].position() + (ret2 * outputChannels * outputDepth));
            }
            if (this.samples_out[0].position() >= this.samples_out[0].limit()) {
                this.frame.nb_samples(this.audio_input_frame_size);
                if (this.isVorbis) {
                    this.frame.pts((this.audio_clock * 1000) / this.sampleRate);
                }
                if (this.frame != null) {
                    this.audio_clock += this.audio_input_frame_size;
                }
                avcodec.avcodec_fill_audio_frame(this.frame, this.audio_c.channels(), outputFormat, this.samples_out[0], this.samples_out[0].limit(), 0);
                for (int i11 = 0; i11 < this.samples_out.length; i11++) {
                    this.frame.data(i11, this.samples_out[i11].position(0));
                    this.frame.linesize(i11, this.samples_out[i11].limit());
                }
                this.frame.quality(this.audio_c.global_quality());
                record(this.frame);
            }
        }
        this.audioRecordTime += System.currentTimeMillis() - start;
        return this.frame.key_frame() != 0;
    }

    private void writeAudioFrame() throws Exception {
        synchronized (this.oc) {
            if (this.interleaved && this.video_st != null) {
                int ret = avformat.av_interleaved_write_frame(this.oc, this.audio_pkt);
                if (ret < 0) {
                    throw new Exception("av_interleaved_write_frame() error " + ret + " while writing interleaved audio frame.");
                }
            } else {
                int ret2 = avformat.av_write_frame(this.oc, this.audio_pkt);
                if (ret2 < 0) {
                    throw new Exception("av_write_frame() error " + ret2 + " while writing audio frame.");
                }
            }
        }
    }

    boolean record(avutil.AVFrame frame) throws Exception {
        avcodec.av_init_packet(this.audio_pkt);
        this.audio_pkt.data(this.audio_outbuf);
        this.audio_pkt.size(this.audio_outbuf_size);
        int oldDelay = this.audio_c.delay();
        int ret = avcodec.avcodec_encode_audio2(this.audio_c, this.audio_pkt, frame, this.got_audio_packet);
        if (ret < 0) {
            throw new Exception("avcodec_encode_audio2() error " + ret + ": Could not encode audio packet.");
        }
        int newDelay = this.audio_c.delay();
        boolean delayHasChanged = oldDelay != newDelay;
        if (this.got_audio_packet[0] == 0) {
            return false;
        }
        if (delayHasChanged) {
            SLog.w("Delay has changed on audio encoder.");
        }
        if (this.audio_pkt.pts() < 0) {
            this.audio_pkt.pts(0L);
        }
        if (this.audio_pkt.dts() < 0) {
            this.audio_pkt.dts(0L);
        }
        if (this.audio_pkt.pts() != avutil.AV_NOPTS_VALUE) {
            this.audio_pkt.pts(avutil.av_rescale_q(this.audio_pkt.pts(), this.audio_c.time_base(), this.audio_c.time_base()));
        }
        if (this.audio_pkt.dts() != avutil.AV_NOPTS_VALUE) {
            this.audio_pkt.dts(avutil.av_rescale_q(this.audio_pkt.dts(), this.audio_c.time_base(), this.audio_c.time_base()));
        }
        this.audio_pkt.flags(this.audio_pkt.flags() | 1);
        this.audio_pkt.stream_index(this.audio_st.index());
        if (this.mAudioOutBuffer != null) {
            BytePointer data = this.audio_pkt.data();
            if (data == null) {
                return true;
            }
            int limit = this.audio_pkt.size();
            int start = this.mAudioOutBuffer.getPosition();
            data.get(this.mAudioOutBuffer.data, start, limit);
            this.mAudioOutBuffer.sizes.add(Integer.valueOf(start));
            return true;
        }
        writeAudioFrame();
        return true;
    }

    public boolean hasData() {
        return this.hasData;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public double getFps() {
        return this.fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public static class Exception extends java.lang.Exception {
        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static int calculateOutputSampleCount(int inRate, int inCount, int outRate) {
        float inputSeconds = inCount / (1.0f * inRate);
        return (int) (outRate * inputSeconds);
    }

    public static byte[] resample(int inRate, int inCount, byte[] inBuffer, int outRate) {
        long channel_layout = avutil.av_get_default_channel_layout(1);
        swresample.SwrContext samples_convert_ctx = swresample.swr_alloc_set_opts(null, channel_layout, 1, outRate, channel_layout, 1, inRate, 0, null);
        swresample.swr_init(samples_convert_ctx);
        int outCount = calculateOutputSampleCount(inRate, inCount, outRate);
        byte[] outBuffer = new byte[outCount * 2];
        swresample.swr_convert(samples_convert_ctx, outBuffer, outCount, inBuffer, inCount);
        return outBuffer;
    }
}
