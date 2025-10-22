package co.vine.android.embed.player;

import android.media.MediaFormat;

/* loaded from: classes.dex */
public class AudioSampleTrack {
    public final int channelCount;
    public final MediaFormat format;
    public final int index;
    public final String mime;
    public final int sampleRate;

    public AudioSampleTrack(String mime, int index, int sampleRate, MediaFormat format) {
        this.mime = mime;
        this.index = index;
        this.sampleRate = sampleRate;
        this.format = format;
        this.channelCount = format.getInteger("channel-count");
    }

    public long decodedIndexToTimeUs(int index, int frameSize) {
        return (index * 1000000) / ((this.sampleRate * this.channelCount) * frameSize);
    }

    public double decodedTimeUsToIndex(long timeUs, int frameSize) {
        return (((timeUs * this.sampleRate) * this.channelCount) * frameSize) / 1000000.0d;
    }
}
