package com.google.android.exoplayer.util;

import com.flurry.android.Constants;
import com.google.android.exoplayer.MediaFormat;
import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avutil;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public final class Ac3Util {
    private static final int[] BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = {1, 2, 3, 6};
    private static final int[] SAMPLE_RATE_BY_FSCOD = {48000, 44100, 32000};
    private static final int[] SAMPLE_RATE_BY_FSCOD2 = {24000, 22050, 16000};
    private static final int[] CHANNEL_COUNT_BY_ACMOD = {2, 1, 2, 3, 3, 4, 4, 5};
    private static final int[] BITRATE_BY_HALF_FRMSIZECOD = {32, 40, 48, 56, 64, 80, 96, 112, 128, avcodec.AV_CODEC_ID_CDXL, 192, 224, 256, avutil.AV_PIX_FMT_YUVJ411P, 384, 448, 512, 576, 640};
    private static final int[] SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = {69, 87, 104, 121, avcodec.AV_CODEC_ID_KGV1, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393};

    public static MediaFormat parseAc3AnnexFFormat(ParsableByteArray data, String trackId, long durationUs, String language) {
        int fscod = (data.readUnsignedByte() & 192) >> 6;
        int sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        int nextByte = data.readUnsignedByte();
        int channelCount = CHANNEL_COUNT_BY_ACMOD[(nextByte & 56) >> 3];
        if ((nextByte & 4) != 0) {
            channelCount++;
        }
        return MediaFormat.createAudioFormat(trackId, "audio/ac3", -1, -1, durationUs, channelCount, sampleRate, null, language);
    }

    public static MediaFormat parseEAc3AnnexFFormat(ParsableByteArray data, String trackId, long durationUs, String language) {
        data.skipBytes(2);
        int fscod = (data.readUnsignedByte() & 192) >> 6;
        int sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        int nextByte = data.readUnsignedByte();
        int channelCount = CHANNEL_COUNT_BY_ACMOD[(nextByte & 14) >> 1];
        if ((nextByte & 1) != 0) {
            channelCount++;
        }
        return MediaFormat.createAudioFormat(trackId, "audio/eac3", -1, -1, durationUs, channelCount, sampleRate, null, language);
    }

    public static MediaFormat parseAc3SyncframeFormat(ParsableBitArray data, String trackId, long durationUs, String language) {
        data.skipBits(32);
        int fscod = data.readBits(2);
        data.skipBits(14);
        int acmod = data.readBits(3);
        if ((acmod & 1) != 0 && acmod != 1) {
            data.skipBits(2);
        }
        if ((acmod & 4) != 0) {
            data.skipBits(2);
        }
        if (acmod == 2) {
            data.skipBits(2);
        }
        boolean lfeon = data.readBit();
        return MediaFormat.createAudioFormat(trackId, "audio/ac3", -1, -1, durationUs, CHANNEL_COUNT_BY_ACMOD[acmod] + (lfeon ? 1 : 0), SAMPLE_RATE_BY_FSCOD[fscod], null, language);
    }

    public static MediaFormat parseEac3SyncframeFormat(ParsableBitArray data, String trackId, long durationUs, String language) {
        int sampleRate;
        data.skipBits(32);
        int fscod = data.readBits(2);
        if (fscod == 3) {
            sampleRate = SAMPLE_RATE_BY_FSCOD2[data.readBits(2)];
        } else {
            data.skipBits(2);
            sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        }
        int acmod = data.readBits(3);
        boolean lfeon = data.readBit();
        return MediaFormat.createAudioFormat(trackId, "audio/eac3", -1, -1, durationUs, CHANNEL_COUNT_BY_ACMOD[acmod] + (lfeon ? 1 : 0), sampleRate, null, language);
    }

    public static int parseAc3SyncframeSize(byte[] data) {
        int fscod = (data[4] & 192) >> 6;
        int frmsizecod = data[4] & 63;
        return getAc3SyncframeSize(fscod, frmsizecod);
    }

    public static int parseEAc3SyncframeSize(byte[] data) {
        return (((data[2] & 7) << 8) + (data[3] & Constants.UNKNOWN) + 1) * 2;
    }

    public static int getAc3SyncframeAudioSampleCount() {
        return 1536;
    }

    public static int parseEAc3SyncframeAudioSampleCount(byte[] data) {
        return (((data[4] & 192) >> 6) == 3 ? 6 : BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[(data[4] & 48) >> 4]) * 256;
    }

    public static int parseEAc3SyncframeAudioSampleCount(ByteBuffer buffer) {
        int fscod = (buffer.get(buffer.position() + 4) & 192) >> 6;
        return (fscod == 3 ? 6 : BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[(buffer.get(buffer.position() + 4) & 48) >> 4]) * 256;
    }

    private static int getAc3SyncframeSize(int fscod, int frmsizecod) {
        int sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        if (sampleRate == 44100) {
            return (SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1[frmsizecod / 2] + (frmsizecod % 2)) * 2;
        }
        int bitrate = BITRATE_BY_HALF_FRMSIZECOD[frmsizecod / 2];
        if (sampleRate == 32000) {
            return bitrate * 6;
        }
        return bitrate * 4;
    }
}
