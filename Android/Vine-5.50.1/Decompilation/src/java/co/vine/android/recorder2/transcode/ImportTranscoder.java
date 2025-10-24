package co.vine.android.recorder2.transcode;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;
import android.view.Surface;
import co.vine.android.recorder2.ImportProgressListener;
import co.vine.android.recorder2.audio.AudioEncoder;
import co.vine.android.recorder2.util.MediaExtractorUtil;
import co.vine.android.recorder2.video.VideoEncoderCore;
import com.coremedia.iso.boxes.Container;
import com.edisonwang.android.slog.SLog;
import com.flurry.android.Constants;
import com.googlecode.javacv.cpp.avutil;
import com.googlecode.javacv.cpp.swresample;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

@TargetApi(18)
/* loaded from: classes.dex */
public class ImportTranscoder {
    private Boolean mAudioDataWritten = false;
    private int mAudioTrackIndex = -1;
    private boolean mCancel;
    private KeyframeWrittenListener mKeyframeWrittenListener;

    public interface KeyframeWrittenListener {
        void onKeyframeWritten(long j);
    }

    public ImportTranscoder(KeyframeWrittenListener keyframeWrittenListener) {
        this.mKeyframeWrittenListener = keyframeWrittenListener;
    }

    public void transcode(final Context context, final Uri inUri, String outPath, String thumbnailPath, String ghostPath, final long startMicros, final long endMicros, Point cropOrigin, ImportProgressListener listener) throws Throwable {
        try {
            String videoOnlyPath = outPath + ".video.mp4";
            final String audioOnlyPath = outPath + ".audio.m4a";
            Thread audioThread = new Thread() { // from class: co.vine.android.recorder2.transcode.ImportTranscoder.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() throws Throwable {
                    try {
                        ImportTranscoder.this.transcodeAudio(context, inUri, startMicros, endMicros, audioOnlyPath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            audioThread.start();
            transcodeVideo(context, inUri, videoOnlyPath, thumbnailPath, ghostPath, startMicros, endMicros, cropOrigin, listener);
            audioThread.join();
            combineMovies(videoOnlyPath, audioOnlyPath, outPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void transcodeVideo(android.content.Context r39, android.net.Uri r40, java.lang.String r41, java.lang.String r42, java.lang.String r43, long r44, long r46, android.graphics.Point r48, co.vine.android.recorder2.ImportProgressListener r49) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 510
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder2.transcode.ImportTranscoder.transcodeVideo(android.content.Context, android.net.Uri, java.lang.String, java.lang.String, java.lang.String, long, long, android.graphics.Point, co.vine.android.recorder2.ImportProgressListener):void");
    }

    @TargetApi(18)
    private void doTranscode(String thumbPath, String lastFramePath, long startMicros, long endMicros, int orientation, float aspectRatio, float cropOriginRatio, MediaExtractor extractor, MediaCodec decoder, OutputSurface outputSurface, InputSurface inputSurface, MediaCodec encoder, MediaMuxer muxer, ImportProgressListener listener) throws MediaCodec.CryptoException {
        int inputBufIndex;
        ByteBuffer[] decoderInputBuffers = decoder.getInputBuffers();
        ByteBuffer[] encoderOutputBuffers = encoder.getOutputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int inputChunk = 0;
        boolean outputDone = false;
        boolean inputDone = false;
        boolean hasRenderedFirstFrame = false;
        int countSinceLastIFrame = 0;
        long lastTimeStampUs = 0;
        extractor.seekTo(startMicros, 0);
        int trackIndex = -1;
        while (!outputDone && !this.mCancel) {
            if (!inputDone && (inputBufIndex = decoder.dequeueInputBuffer(10000L)) >= 0) {
                ByteBuffer inputBuf = decoderInputBuffers[inputBufIndex];
                int chunkSize = extractor.readSampleData(inputBuf, 0);
                if (chunkSize < 0 || extractor.getSampleTime() > endMicros) {
                    decoder.queueInputBuffer(inputBufIndex, 0, 0, 0L, 4);
                    inputDone = true;
                } else {
                    long presentationTimeUs = extractor.getSampleTime();
                    decoder.queueInputBuffer(inputBufIndex, 0, chunkSize, presentationTimeUs, 0);
                    inputChunk++;
                    extractor.advance();
                }
            }
            boolean decoderOutputAvailable = 0 == 0;
            boolean encoderOutputAvailable = true;
            while (true) {
                if (decoderOutputAvailable || encoderOutputAvailable) {
                    int encoderStatus = encoder.dequeueOutputBuffer(info, 10000L);
                    if (encoderStatus == -1) {
                        encoderOutputAvailable = false;
                    } else if (encoderStatus == -3) {
                        encoderOutputBuffers = encoder.getOutputBuffers();
                    } else if (encoderStatus == -2) {
                        MediaFormat newFormat = encoder.getOutputFormat();
                        trackIndex = muxer.addTrack(newFormat);
                        SLog.d("ryango track index {}", Integer.valueOf(trackIndex));
                        muxer.start();
                    } else if (encoderStatus >= 0) {
                        ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                        VideoEncoderCore.requestIFrame(encoder, countSinceLastIFrame);
                        if (info.size != 0) {
                            encodedData.position(info.offset);
                            encodedData.limit(info.offset + info.size);
                            info.presentationTimeUs -= startMicros;
                            if (info.presentationTimeUs < 0) {
                                info.presentationTimeUs = 0L;
                            }
                            muxer.writeSampleData(trackIndex, encodedData, info);
                            countSinceLastIFrame++;
                            if ((info.flags & 1) > 0) {
                                countSinceLastIFrame = 1;
                                this.mKeyframeWrittenListener.onKeyframeWritten(info.presentationTimeUs);
                            }
                            listener.onImportProgressChanged((100 * (info.presentationTimeUs - lastTimeStampUs)) / (endMicros - startMicros));
                            lastTimeStampUs = info.presentationTimeUs;
                        }
                        outputDone = (info.flags & 4) != 0;
                        encoder.releaseOutputBuffer(encoderStatus, false);
                    }
                    if (encoderStatus == -1 && 0 == 0) {
                        int decoderStatus = decoder.dequeueOutputBuffer(info, 10000L);
                        if (decoderStatus == -1) {
                            decoderOutputAvailable = false;
                        } else if (decoderStatus != -3) {
                            if (decoderStatus == -2) {
                                decoder.getOutputFormat();
                            } else if (decoderStatus >= 0) {
                                boolean doRender = info.size != 0 && info.presentationTimeUs >= startMicros;
                                boolean eos = (info.flags & 4) != 0;
                                decoder.releaseOutputBuffer(decoderStatus, doRender);
                                if (doRender) {
                                    outputSurface.awaitNewImage();
                                    outputSurface.drawImage(orientation, aspectRatio, cropOriginRatio);
                                    if (!hasRenderedFirstFrame) {
                                        outputSurface.saveImage(720, 720, thumbPath);
                                    }
                                    hasRenderedFirstFrame = true;
                                    inputSurface.setPresentationTime(info.presentationTimeUs * 1000);
                                    inputSurface.swapBuffers();
                                }
                                if (eos) {
                                    outputSurface.saveImage(720, 720, lastFramePath);
                                    encoder.signalEndOfInputStream();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (countSinceLastIFrame > 0) {
            muxer.stop();
            muxer.release();
        }
    }

    @TargetApi(16)
    public void transcodeAudio(Context context, Uri uri, long startMicros, long endMicros, String m4a) throws Throwable {
        MediaExtractor extractor = null;
        MediaCodec decoder = null;
        try {
            MediaExtractor extractor2 = new MediaExtractor();
            try {
                extractor2.setDataSource(context, uri, (Map<String, String>) null);
                int trackIndex = MediaExtractorUtil.selectAudioTrack(extractor2);
                if (trackIndex < 0) {
                    generateNoise(endMicros - startMicros, m4a);
                    if (extractor2 != null) {
                        extractor2.release();
                    }
                    if (0 != 0) {
                        decoder.stop();
                        decoder.release();
                        return;
                    }
                    return;
                }
                extractor2.selectTrack(trackIndex);
                long endMicros2 = endMicros + 5000;
                MediaFormat format = extractor2.getTrackFormat(trackIndex);
                int channelCount = format.getInteger("channel-count");
                if (format.containsKey("bitrate")) {
                    format.getInteger("bitrate");
                }
                long durationMicros = format.containsKey("durationUs") ? format.getLong("durationUs") : 0L;
                int sampleRate = format.containsKey("sample-rate") ? format.getInteger("sample-rate") : -1;
                if (sampleRate < 0) {
                    throw new RuntimeException("no sample rate");
                }
                long sampleDurationUs = (long) ((1.0f / sampleRate) * 1000000.0f);
                String mime = format.getString("mime");
                MediaCodec decoder2 = MediaCodec.createDecoderByType(mime);
                decoder2.configure(format, (Surface) null, (MediaCrypto) null, 0);
                decoder2.start();
                ByteBuffer[] input = decoder2.getInputBuffers();
                ByteBuffer[] output = decoder2.getOutputBuffers();
                MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                boolean sawInputEOS = false;
                boolean sawOutputEOS = false;
                int noOutputCounter = 0;
                Boolean doStop = false;
                long largestSampleRatePerSecond = Math.max(44100, sampleRate);
                byte[] audioArray = new byte[(int) (((durationMicros * largestSampleRatePerSecond) / 1000000.0f) * 2.0f * 2.0f)];
                int bufferSize = 0;
                extractor2.seekTo(startMicros, 0);
                System.currentTimeMillis();
                byte[] chunk = null;
                SLog.d("ryango extractor start {} desired start {}", Long.valueOf(extractor2.getSampleTime()), Long.valueOf(startMicros));
                while (!this.mCancel && !sawOutputEOS && noOutputCounter < 50 && !doStop.booleanValue()) {
                    noOutputCounter++;
                    if (!sawInputEOS) {
                        long presentationTimeUs = 0;
                        int inputBufIndex = decoder2.dequeueInputBuffer(10000L);
                        if (inputBufIndex >= 0) {
                            ByteBuffer dstBuf = input[inputBufIndex];
                            int sampleSize = extractor2.readSampleData(dstBuf, 0);
                            if (sampleSize < 0) {
                                sawInputEOS = true;
                                sampleSize = 0;
                            } else {
                                presentationTimeUs = extractor2.getSampleTime();
                            }
                            decoder2.queueInputBuffer(inputBufIndex, 0, sampleSize, presentationTimeUs, sawInputEOS ? 4 : 0);
                            if (!sawInputEOS) {
                                extractor2.advance();
                            }
                        }
                        int res = decoder2.dequeueOutputBuffer(info, 10000L);
                        if (res >= 0) {
                            if (info.size > 0) {
                                noOutputCounter = 0;
                            }
                            ByteBuffer buf = output[res];
                            if (chunk == null || chunk.length < info.size) {
                                chunk = new byte[info.size];
                            }
                            if (chunk.length > buf.remaining()) {
                                chunk = Arrays.copyOf(chunk, buf.remaining());
                            }
                            buf.get(chunk);
                            buf.clear();
                            int i = 0;
                            int ratio = channelCount * 2;
                            long sampleTimestampUs = info.presentationTimeUs;
                            long expectedSampleTime = (bufferSize / 2) + (((info.size / ratio) * 1000000) / sampleRate);
                            if (info.presentationTimeUs != 0 && expectedSampleTime / info.presentationTimeUs == 2) {
                                channelCount *= 2;
                                ratio = channelCount * 2;
                                SLog.i("Updated channel count: {}, one time upgrade.", Integer.valueOf(channelCount));
                            }
                            int bufferSize2 = bufferSize;
                            while (true) {
                                if (i >= info.size / ratio) {
                                    bufferSize = bufferSize2;
                                    break;
                                }
                                int value = (chunk[i * ratio] & Constants.UNKNOWN) | (chunk[(i * ratio) + 1] << 8);
                                if (channelCount == 2) {
                                    int value2 = chunk[(i * ratio) + 2] & Constants.UNKNOWN;
                                    value = (value + (value2 | (chunk[(i * ratio) + 3] << 8))) / 2;
                                }
                                int bufferSize3 = bufferSize2 + 1;
                                audioArray[bufferSize2] = (byte) (value & 255);
                                bufferSize2 = bufferSize3 + 1;
                                audioArray[bufferSize3] = (byte) ((value >> 8) & 255);
                                sampleTimestampUs += sampleDurationUs;
                                if (sampleTimestampUs >= endMicros2) {
                                    doStop = true;
                                    bufferSize = bufferSize2;
                                    break;
                                }
                                i++;
                            }
                            decoder2.releaseOutputBuffer(res, false);
                            if ((info.flags & 4) != 0) {
                                sawOutputEOS = true;
                            }
                        } else if (res == -3) {
                            output = decoder2.getOutputBuffers();
                        } else if (res == -2) {
                            decoder2.getOutputFormat();
                        }
                    }
                }
                if (!this.mCancel) {
                    byte[] out = audioArray;
                    if (sampleRate != 44100) {
                        ByteBuffer buffer = ByteBuffer.allocate(audioArray.length);
                        buffer.order(ByteOrder.LITTLE_ENDIAN);
                        buffer.put(audioArray);
                        out = resample(sampleRate, bufferSize / 2, buffer.array(), 44100);
                        bufferSize = out.length;
                    }
                    encodeAudio(out, bufferSize, m4a);
                }
                if (extractor2 != null) {
                    extractor2.release();
                }
                if (decoder2 != null) {
                    decoder2.stop();
                    decoder2.release();
                }
            } catch (Throwable th) {
                th = th;
                extractor = extractor2;
                if (extractor != null) {
                    extractor.release();
                }
                if (0 != 0) {
                    decoder.stop();
                    decoder.release();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void encodeAudio(byte[] input, int byteCount, String m4a) throws IOException {
        MediaCodec encoder = AudioEncoder.buildEncoder(AudioEncoder.buildFormat());
        encoder.start();
        MediaMuxer muxer = new MediaMuxer(m4a, 0);
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        boolean outputDone = false;
        this.mAudioDataWritten = false;
        int offset = 0;
        while (!outputDone) {
            try {
                try {
                    outputDone = drainEncoder(encoder, bufferInfo, false, muxer);
                    ByteBuffer[] inputBuffers = encoder.getInputBuffers();
                    int inputBufferIndex = encoder.dequeueInputBuffer(-1L);
                    if (inputBufferIndex >= 0) {
                        ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                        inputBuffer.clear();
                        int chunkSize = Math.min(2048, byteCount - offset);
                        inputBuffer.put(input, offset, chunkSize);
                        long presentationTimeUs = (long) (((offset / 2) / 44100.0f) * 1000000.0f);
                        if (offset + chunkSize >= byteCount) {
                            encoder.queueInputBuffer(inputBufferIndex, 0, chunkSize, presentationTimeUs, 4);
                            drainEncoder(encoder, bufferInfo, true, muxer);
                            outputDone = true;
                        } else {
                            encoder.queueInputBuffer(inputBufferIndex, 0, chunkSize, presentationTimeUs, 0);
                        }
                        offset += chunkSize;
                    }
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            } finally {
                if (encoder != null) {
                    encoder.stop();
                    encoder.release();
                }
                if (muxer != null && this.mAudioDataWritten.booleanValue()) {
                    muxer.stop();
                    muxer.release();
                }
            }
        }
    }

    private boolean drainEncoder(MediaCodec encoder, MediaCodec.BufferInfo bufferInfo, boolean endOfStream, MediaMuxer muxer) {
        ByteBuffer[] encoderOutputBuffers = encoder.getOutputBuffers();
        while (true) {
            int encoderStatus = encoder.dequeueOutputBuffer(bufferInfo, 100L);
            if (encoderStatus == -1) {
                if (!endOfStream) {
                    return false;
                }
            } else if (encoderStatus == -3) {
                encoderOutputBuffers = encoder.getOutputBuffers();
            } else if (encoderStatus == -2) {
                MediaFormat newFormat = encoder.getOutputFormat();
                this.mAudioTrackIndex = muxer.addTrack(newFormat);
                muxer.start();
            } else if (encoderStatus >= 0) {
                ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                if ((bufferInfo.flags & 2) != 0) {
                    bufferInfo.size = 0;
                }
                if (bufferInfo.size != 0) {
                    encodedData.position(bufferInfo.offset);
                    encodedData.limit(bufferInfo.offset + bufferInfo.size);
                    muxer.writeSampleData(this.mAudioTrackIndex, encodedData, bufferInfo);
                    this.mAudioDataWritten = true;
                }
                encoder.releaseOutputBuffer(encoderStatus, false);
                if ((bufferInfo.flags & 4) != 0) {
                    return true;
                }
            } else {
                continue;
            }
        }
    }

    private void generateNoise(long durationUs, String m4a) throws IOException {
        Random r = new Random();
        int byteCount = ((int) ((44100 * durationUs) / 1000000)) * 2;
        byte[] buffer = new byte[byteCount];
        for (int i = 0; i < byteCount; i++) {
            buffer[i] = (byte) (r.nextInt(127) * 0.01f);
        }
        encodeAudio(buffer, byteCount, m4a);
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

    public static int calculateOutputSampleCount(int inRate, int inCount, int outRate) {
        float inputSeconds = inCount / (1.0f * inRate);
        return (int) (outRate * inputSeconds);
    }

    public static void combineMovies(String videoPath, String audioPath, String outPath) throws IOException {
        new MovieCreator();
        Track videoTrack = MovieCreator.build(videoPath).getTracks().get(0);
        new MovieCreator();
        Track audioTrack = MovieCreator.build(audioPath).getTracks().get(0);
        Movie outMovie = new Movie();
        outMovie.addTrack(videoTrack);
        outMovie.addTrack(audioTrack);
        Container out = new DefaultMp4Builder().build(outMovie);
        FileOutputStream fos = new FileOutputStream(outPath);
        out.writeContainer(fos.getChannel());
    }
}
