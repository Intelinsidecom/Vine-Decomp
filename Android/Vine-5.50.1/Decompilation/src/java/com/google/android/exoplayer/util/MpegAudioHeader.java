package com.google.android.exoplayer.util;

import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avutil;

/* loaded from: classes.dex */
public final class MpegAudioHeader {
    public int bitrate;
    public int channels;
    public int frameSize;
    public String mimeType;
    public int sampleRate;
    public int samplesPerFrame;
    public int version;
    private static final String[] MIME_TYPE_BY_LAYER = {"audio/mpeg-L1", "audio/mpeg-L2", "audio/mpeg"};
    private static final int[] SAMPLING_RATE_V1 = {44100, 48000, 32000};
    private static final int[] BITRATE_V1_L1 = {32, 64, 96, 128, avcodec.AV_CODEC_ID_CDXL, 192, 224, 256, 288, avutil.AV_PIX_FMT_YUVJ411P, 352, 384, 416, 448};
    private static final int[] BITRATE_V2_L1 = {32, 48, 56, 64, 80, 96, 112, 128, 144, avcodec.AV_CODEC_ID_CDXL, 176, 192, 224, 256};
    private static final int[] BITRATE_V1_L2 = {32, 48, 56, 64, 80, 96, 112, 128, avcodec.AV_CODEC_ID_CDXL, 192, 224, 256, avutil.AV_PIX_FMT_YUVJ411P, 384};
    private static final int[] BITRATE_V1_L3 = {32, 40, 48, 56, 64, 80, 96, 112, 128, avcodec.AV_CODEC_ID_CDXL, 192, 224, 256, avutil.AV_PIX_FMT_YUVJ411P};
    private static final int[] BITRATE_V2 = {8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, avcodec.AV_CODEC_ID_CDXL};

    public static int getFrameSize(int header) {
        int version;
        int layer;
        int bitrateIndex;
        int samplingRateIndex;
        int bitrate;
        if ((header & (-2097152)) != -2097152 || (version = (header >>> 19) & 3) == 1 || (layer = (header >>> 17) & 3) == 0 || (bitrateIndex = (header >>> 12) & 15) == 0 || bitrateIndex == 15 || (samplingRateIndex = (header >>> 10) & 3) == 3) {
            return -1;
        }
        int samplingRate = SAMPLING_RATE_V1[samplingRateIndex];
        if (version == 2) {
            samplingRate /= 2;
        } else if (version == 0) {
            samplingRate /= 4;
        }
        int padding = (header >>> 9) & 1;
        if (layer == 3) {
            int bitrate2 = version == 3 ? BITRATE_V1_L1[bitrateIndex - 1] : BITRATE_V2_L1[bitrateIndex - 1];
            return (((bitrate2 * 12000) / samplingRate) + padding) * 4;
        }
        if (version == 3) {
            bitrate = layer == 2 ? BITRATE_V1_L2[bitrateIndex - 1] : BITRATE_V1_L3[bitrateIndex - 1];
        } else {
            bitrate = BITRATE_V2[bitrateIndex - 1];
        }
        if (version == 3) {
            return ((144000 * bitrate) / samplingRate) + padding;
        }
        return (((layer == 1 ? 72000 : 144000) * bitrate) / samplingRate) + padding;
    }

    public static boolean populateHeader(int headerData, MpegAudioHeader header) {
        int version;
        int layer;
        int bitrateIndex;
        int samplingRateIndex;
        int bitrate;
        int samplesPerFrame;
        int frameSize;
        if (((-2097152) & headerData) != -2097152 || (version = (headerData >>> 19) & 3) == 1 || (layer = (headerData >>> 17) & 3) == 0 || (bitrateIndex = (headerData >>> 12) & 15) == 0 || bitrateIndex == 15 || (samplingRateIndex = (headerData >>> 10) & 3) == 3) {
            return false;
        }
        int sampleRate = SAMPLING_RATE_V1[samplingRateIndex];
        if (version == 2) {
            sampleRate /= 2;
        } else if (version == 0) {
            sampleRate /= 4;
        }
        int padding = (headerData >>> 9) & 1;
        if (layer == 3) {
            bitrate = version == 3 ? BITRATE_V1_L1[bitrateIndex - 1] : BITRATE_V2_L1[bitrateIndex - 1];
            frameSize = (((bitrate * 12000) / sampleRate) + padding) * 4;
            samplesPerFrame = 384;
        } else if (version == 3) {
            bitrate = layer == 2 ? BITRATE_V1_L2[bitrateIndex - 1] : BITRATE_V1_L3[bitrateIndex - 1];
            samplesPerFrame = 1152;
            frameSize = ((144000 * bitrate) / sampleRate) + padding;
        } else {
            bitrate = BITRATE_V2[bitrateIndex - 1];
            samplesPerFrame = layer == 1 ? 576 : 1152;
            frameSize = (((layer == 1 ? 72000 : 144000) * bitrate) / sampleRate) + padding;
        }
        String mimeType = MIME_TYPE_BY_LAYER[3 - layer];
        int channels = ((headerData >> 6) & 3) == 3 ? 1 : 2;
        header.setValues(version, mimeType, frameSize, sampleRate, channels, bitrate * 1000, samplesPerFrame);
        return true;
    }

    private void setValues(int version, String mimeType, int frameSize, int sampleRate, int channels, int bitrate, int samplesPerFrame) {
        this.version = version;
        this.mimeType = mimeType;
        this.frameSize = frameSize;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.bitrate = bitrate;
        this.samplesPerFrame = samplesPerFrame;
    }
}
