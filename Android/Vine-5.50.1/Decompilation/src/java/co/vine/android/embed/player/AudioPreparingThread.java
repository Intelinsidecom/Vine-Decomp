package co.vine.android.embed.player;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import co.vine.android.embed.player.VineSampleSource;
import co.vine.android.util.AudioUtils;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
final class AudioPreparingThread extends Thread {
    private boolean mCancelled;
    private Exception mError;
    private final VineSampleSource.VineMediaExtractor mExtractor;
    private ByteBuffer mPreparedSamples;
    private ByteBuffer mUnpreparedSamples;

    public AudioPreparingThread(VineSampleSource.VineMediaExtractor extractor) {
        this.mExtractor = extractor;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        VineSampleSource.EncodedAudioSamples audioSamples = this.mExtractor.getAudioSamples();
        byte[] data = audioSamples.data;
        AudioSampleTrack track = audioSamples.track;
        AudioFrame[] frames = audioSamples.frames;
        long endMicros = audioSamples.estimatedDuration;
        MediaFormat format = track.format;
        int sampleRate = -1;
        int channelCount = format.getInteger("channel-count");
        if (format.containsKey("sample-rate")) {
            sampleRate = format.getInteger("sample-rate");
        }
        if (sampleRate <= 0) {
            throw new RuntimeException("no sample rate");
        }
        if (frames.length != 0) {
            long startMicros = frames[0].startUs;
            MediaCodec decoder = null;
            boolean isStarted = false;
            try {
                try {
                    decoder = MediaCodec.createDecoderByType(format.getString("mime"));
                    decoder.configure(format, (Surface) null, (MediaCrypto) null, 0);
                    decoder.start();
                    isStarted = true;
                    ByteBuffer[] input = decoder.getInputBuffers();
                    ByteBuffer[] output = decoder.getOutputBuffers();
                    MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                    int noOutputCounter = 0;
                    Boolean doStop = false;
                    int bufferSize = 0;
                    int frameIndex = 0;
                    byte[] unpreparedArray = new byte[((int) (((sampleRate * 1.1d) / 1000.0d) * ((endMicros - startMicros) / 1000) * 1.1d * 4.0d)) * channelCount];
                    this.mUnpreparedSamples = ByteBuffer.wrap(unpreparedArray);
                    this.mUnpreparedSamples.limit(0);
                    byte[] chunk = null;
                    long sampleDurationUs = (long) ((((1.0f / sampleRate) / 2.0f) * 1000000.0f) / channelCount);
                    while (noOutputCounter < 50 && !this.mCancelled && !doStop.booleanValue()) {
                        if (frameIndex >= frames.length) {
                            break;
                        }
                        AudioFrame frame = frames[frameIndex];
                        noOutputCounter++;
                        int inputBufIndex = decoder.dequeueInputBuffer(10000L);
                        if (inputBufIndex >= 0) {
                            ByteBuffer dstBuf = input[inputBufIndex];
                            dstBuf.position(0);
                            dstBuf.put(data, frame.offset, frame.size);
                            decoder.queueInputBuffer(inputBufIndex, 0, frame.size, frame.presentationTimeUs, 0);
                            frameIndex++;
                        }
                        int res = decoder.dequeueOutputBuffer(info, 10000L);
                        if (res >= 0) {
                            if (info.size > 0) {
                                noOutputCounter = 0;
                            }
                            ByteBuffer buf = output[res];
                            if (chunk == null || chunk.length < info.size) {
                                chunk = new byte[info.size];
                            }
                            if (info.size > 0) {
                                buf.get(chunk, 0, info.size);
                            }
                            buf.clear();
                            long sampleTimestampUs = AudioUtils.presentationTimeUsToStartTimeUs(info.presentationTimeUs, info.size, sampleRate, channelCount);
                            long durationNeeded = endMicros - sampleTimestampUs;
                            int maxSampleNeeded = (int) (durationNeeded / sampleDurationUs);
                            int sampleNeeded = Math.min(maxSampleNeeded, info.size);
                            if (sampleNeeded < info.size) {
                                doStop = true;
                            }
                            if (sampleNeeded > 0) {
                                System.arraycopy(chunk, 0, unpreparedArray, bufferSize, sampleNeeded);
                                bufferSize += sampleNeeded;
                                this.mUnpreparedSamples.limit(bufferSize);
                            }
                            decoder.releaseOutputBuffer(res, false);
                            if ((info.flags & 4) != 0) {
                                doStop = true;
                            }
                        } else if (res == -3) {
                            output = decoder.getOutputBuffers();
                        }
                    }
                    int rampCount = Math.min(bufferSize / 2, (int) (((110000.0d * track.decodedTimeUsToIndex(1L, 2)) / 2.0d) * 2.0d));
                    this.mPreparedSamples = ByteBuffer.wrap(AudioUtils.cubicCrossFade(0, rampCount, bufferSize - 1, unpreparedArray));
                    this.mUnpreparedSamples = null;
                    if (decoder != null) {
                        if (1 != 0) {
                            try {
                                decoder.stop();
                            } catch (IllegalStateException e) {
                                this.mError = e;
                            }
                        }
                        decoder.release();
                    }
                } catch (IOException e2) {
                    this.mError = e2;
                    if (decoder != null) {
                        if (isStarted) {
                            try {
                                decoder.stop();
                            } catch (IllegalStateException e3) {
                                this.mError = e3;
                            }
                        }
                        decoder.release();
                    }
                }
            } catch (Throwable th) {
                if (decoder != null) {
                    if (isStarted) {
                        try {
                            decoder.stop();
                        } catch (IllegalStateException e4) {
                            this.mError = e4;
                        }
                    }
                    decoder.release();
                }
                throw th;
            }
        }
    }

    public void cancel() {
        this.mCancelled = true;
    }

    public ByteBuffer getPreparedSamples() {
        return this.mPreparedSamples;
    }

    public ByteBuffer getUnpreparedSamples() {
        return this.mUnpreparedSamples;
    }

    public Exception getError() {
        return this.mError;
    }
}
