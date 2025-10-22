package co.vine.android.embed.player;

import android.media.MediaCodec;
import co.vine.android.util.AudioUtils;

/* loaded from: classes.dex */
public final class AudioFrame implements Comparable {
    public final int offset;
    public final long presentationTimeUs;
    public final int size;
    public final long startUs;
    public final boolean syncFrame;

    public AudioFrame(AudioSampleTrack track, MediaCodec.BufferInfo info) {
        this.syncFrame = (info.flags & 1) > 0;
        this.presentationTimeUs = info.presentationTimeUs;
        this.size = info.size;
        this.offset = info.offset;
        this.startUs = AudioUtils.presentationTimeUsToStartTimeUs(this.presentationTimeUs, this.size, track.sampleRate, track.channelCount);
    }

    @Override // java.lang.Comparable
    public int compareTo(Object another) {
        return (int) (this.presentationTimeUs - ((AudioFrame) another).presentationTimeUs);
    }
}
