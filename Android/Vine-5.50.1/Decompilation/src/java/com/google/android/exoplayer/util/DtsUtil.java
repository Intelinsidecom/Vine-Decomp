package com.google.android.exoplayer.util;

import com.flurry.android.Constants;
import com.google.android.exoplayer.MediaFormat;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public final class DtsUtil {
    private static final int[] CHANNELS_BY_AMODE = {1, 2, 2, 2, 2, 3, 3, 4, 4, 5, 6, 6, 6, 7, 8, 8};
    private static final int[] SAMPLE_RATE_BY_SFREQ = {-1, 8000, 16000, 32000, -1, -1, 11025, 22050, 44100, -1, -1, 12000, 24000, 48000, -1, -1};
    private static final int[] TWICE_BITRATE_KBPS_BY_RATE = {64, 112, 128, 192, 224, 256, 384, 448, 512, 640, 768, 896, 1024, 1152, 1280, 1536, 1920, 2048, 2304, 2560, 2688, 2816, 2823, 2944, 3072, 3840, 4096, 6144, 7680};
    private static final ParsableBitArray SCRATCH_BITS = new ParsableBitArray();

    public static MediaFormat parseDtsFormat(byte[] frame, String trackId, long durationUs, String language) {
        ParsableBitArray frameBits = SCRATCH_BITS;
        frameBits.reset(frame);
        frameBits.skipBits(60);
        int amode = frameBits.readBits(6);
        int channelCount = CHANNELS_BY_AMODE[amode];
        int sfreq = frameBits.readBits(4);
        int sampleRate = SAMPLE_RATE_BY_SFREQ[sfreq];
        int rate = frameBits.readBits(5);
        int bitrate = rate >= TWICE_BITRATE_KBPS_BY_RATE.length ? -1 : (TWICE_BITRATE_KBPS_BY_RATE[rate] * 1000) / 2;
        frameBits.skipBits(10);
        return MediaFormat.createAudioFormat(trackId, "audio/vnd.dts", bitrate, -1, durationUs, channelCount + (frameBits.readBits(2) > 0 ? 1 : 0), sampleRate, null, language);
    }

    public static int parseDtsAudioSampleCount(byte[] data) {
        int nblks = ((data[4] & 1) << 6) | ((data[5] & 252) >> 2);
        return (nblks + 1) * 32;
    }

    public static int parseDtsAudioSampleCount(ByteBuffer data) {
        int position = data.position();
        int nblks = ((data.get(position + 4) & 1) << 6) | ((data.get(position + 5) & 252) >> 2);
        return (nblks + 1) * 32;
    }

    public static int getDtsFrameSize(byte[] data) {
        return (((data[5] & 2) << 12) | ((data[6] & Constants.UNKNOWN) << 4) | ((data[7] & 240) >> 4)) + 1;
    }
}
