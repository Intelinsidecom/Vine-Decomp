package co.vine.android.recorder;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.SwVineFrameRecorder;
import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.recorder.audio.AudioArrays;
import co.vine.android.recorder.audio.AudioData;
import co.vine.android.recorder.video.VideoData;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import com.flurry.android.Constants;
import com.googlecode.javacv.cpp.avutil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/* loaded from: classes.dex */
public class ExtractorTranscoder {
    private AudioArray mAudioArray;
    private RecordConfigUtils.RecordConfig mConfig;
    private Context mContext;
    private File mDraftFolder;
    private long mDurationMicros;
    private ExtractorTranscoderListener mExtractorTranscoderListener;
    private int mFrameRate;
    private int mHeight;
    private int mMaxVideoByteCount;
    private String mRecorderOutPath;
    private int mRecorderTargetSize;
    private Bitmap mScaledCroppedBitmap;
    private RecordSegment mSegment;
    private long mStartTime;
    private byte[] mVideoBytes;
    private String mVideoMime;
    private int mWidth;
    private boolean mVideoEncodingFinished = false;
    private boolean mAudioEncodingFinished = false;
    private boolean mCancel = false;

    public interface ExtractorTranscoderListener {
        void onError(Exception exc);

        void onFinishedTranscoding(RecordSegment recordSegment, Bitmap bitmap);

        void onProgressUpdate(int i);
    }

    public int getFrameRate() {
        return this.mFrameRate;
    }

    public void cancel() {
        this.mCancel = true;
    }

    public void onEncodingProgressMade(long justProcessedPresentationTimestampUs) {
        if (this.mExtractorTranscoderListener != null && this.mDurationMicros > 0) {
            this.mExtractorTranscoderListener.onProgressUpdate((int) ((100 * justProcessedPresentationTimestampUs) / this.mDurationMicros));
        }
    }

    public void transcode(Context context, File draftFolder, RecordSession session, RecordSegment segment, Uri inUri, String outPath, long startMicros, long endMicros, Point cropOrigin, ExtractorTranscoderListener listener) throws Throwable {
        long adjustedEndMicros;
        this.mStartTime = System.currentTimeMillis();
        this.mContext = context;
        SLog.d("ryango startMicros " + startMicros + " endmicros " + endMicros + " cropOrigin " + cropOrigin);
        int maxDurationUs = session.getConfig().maxDuration * 1000;
        if (endMicros - startMicros > maxDurationUs) {
            SLog.d("ryango too long, originally {} sec now trimmed", Float.valueOf((endMicros - startMicros) / 1000000.0f));
            adjustedEndMicros = startMicros + maxDurationUs;
        } else {
            adjustedEndMicros = endMicros;
        }
        this.mExtractorTranscoderListener = listener;
        this.mConfig = session.getConfig();
        this.mSegment = segment;
        this.mDraftFolder = draftFolder;
        this.mMaxVideoByteCount = RecordConfigUtils.getVideoBufferMax(this.mConfig);
        this.mRecorderOutPath = outPath;
        this.mRecorderTargetSize = this.mConfig.targetSize;
        try {
            TranscodeAudioThread audioThread = new TranscodeAudioThread(inUri, startMicros, adjustedEndMicros);
            audioThread.start();
            transcodeVideo(inUri, startMicros, adjustedEndMicros, cropOrigin);
            SLog.d("Video Transcode done.");
            audioThread.join();
        } catch (InterruptedException e) {
            if (SLog.sLogsOn) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            if (listener != null) {
                listener.onError(e2);
            }
            CrashUtil.logException(e2, "Error while importing", new Object[0]);
        }
        SLog.d("Verified Audio Transcode done.");
    }

    private class TranscodeAudioThread extends Thread {
        private long mEndMicros;
        private long mStartMicros;
        private Uri mUri;

        public TranscodeAudioThread(Uri uri, long startMicros, long endMicros) {
            this.mUri = uri;
            this.mStartMicros = startMicros;
            this.mEndMicros = endMicros;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws Throwable {
            long nanos = System.nanoTime();
            try {
                AudioTranscodeResult result = ExtractorTranscoder.this.transcodeAudio(this.mUri, this.mStartMicros, this.mEndMicros);
                if (result != null) {
                    ExtractorTranscoder.this.mAudioArray = AudioArrays.wrap(result.audioArray);
                    ExtractorTranscoder.this.mSegment.addAudioData(new AudioData(0, result.bufferSize));
                }
                ExtractorTranscoder.this.mAudioEncodingFinished = true;
                ExtractorTranscoder.this.tryFinishingRecording();
            } catch (Exception e) {
                if (ExtractorTranscoder.this.mExtractorTranscoderListener != null) {
                    ExtractorTranscoder.this.mExtractorTranscoderListener.onError(e);
                }
                CrashUtil.logException(e, "Error while transcoding audio", new Object[0]);
            }
            SLog.d("ryango total audio transcode time {} " + ((System.nanoTime() - nanos) / 1000000));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @TargetApi(16)
    public AudioTranscodeResult transcodeAudio(Uri uri, long startMicros, long endMicros) throws Throwable {
        MediaExtractor extractor;
        MediaExtractor extractor2 = null;
        MediaCodec decoder = null;
        try {
            extractor = new MediaExtractor();
        } catch (Throwable th) {
            th = th;
        }
        try {
            extractor.setDataSource(this.mContext, uri, (Map<String, String>) null);
            int trackIndex = selectAudioTrack(extractor);
            if (trackIndex < 0) {
                AudioTranscodeResult audioTranscodeResultGenerateNoise = generateNoise(endMicros - startMicros);
                if (extractor != null) {
                    extractor.release();
                }
                if (0 == 0) {
                    return audioTranscodeResultGenerateNoise;
                }
                decoder.stop();
                decoder.release();
                return audioTranscodeResultGenerateNoise;
            }
            extractor.selectTrack(trackIndex);
            long endMicros2 = endMicros + 15000;
            MediaFormat format = extractor.getTrackFormat(trackIndex);
            int channelCount = format.getInteger("channel-count");
            if (format.containsKey("bitrate")) {
                format.getInteger("bitrate");
            }
            if (format.containsKey("durationUs")) {
                format.getLong("durationUs");
            }
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
            float sampleRatio = sampleRate / 44100;
            short[] audioArray = new short[(int) (RecordConfigUtils.getMaxAudioBufferSize(this.mConfig) * sampleRatio * 2.0f)];
            int bufferSize = 0;
            extractor.seekTo(startMicros, 0);
            SLog.d("ryango audio start time {}: delta of {} from start", Long.valueOf(extractor.getSampleTime()), Long.valueOf(extractor.getSampleTime() - startMicros));
            byte[] chunk = null;
            while (!this.mCancel && !sawOutputEOS && noOutputCounter < 50 && !doStop.booleanValue()) {
                noOutputCounter++;
                if (!sawInputEOS) {
                    long presentationTimeUs = 0;
                    int inputBufIndex = decoder2.dequeueInputBuffer(10000L);
                    if (inputBufIndex >= 0) {
                        ByteBuffer dstBuf = input[inputBufIndex];
                        int sampleSize = extractor.readSampleData(dstBuf, 0);
                        if (sampleSize < 0) {
                            sawInputEOS = true;
                            sampleSize = 0;
                        } else {
                            presentationTimeUs = extractor.getSampleTime();
                        }
                        decoder2.queueInputBuffer(inputBufIndex, 0, sampleSize, presentationTimeUs, sawInputEOS ? 4 : 0);
                        if (!sawInputEOS) {
                            extractor.advance();
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
                        buf.get(chunk, 0, info.size);
                        buf.clear();
                        int i = 0;
                        int ratio = channelCount * 2;
                        long sampleTimestampUs = info.presentationTimeUs;
                        long expectedSampleTime = bufferSize + (((info.size / ratio) * 1000000) / sampleRate);
                        if (info.presentationTimeUs != 0 && expectedSampleTime / info.presentationTimeUs == 2) {
                            channelCount *= 2;
                            ratio = channelCount * 2;
                            SLog.i("Updated channel count: {}, one time upgrade.", Integer.valueOf(channelCount));
                        }
                        while (true) {
                            if (i >= info.size / ratio) {
                                break;
                            }
                            int value = (chunk[i * ratio] & Constants.UNKNOWN) | (chunk[(i * ratio) + 1] << 8);
                            if (channelCount == 2) {
                                int value2 = chunk[(i * ratio) + 2] & Constants.UNKNOWN;
                                value = (value + (value2 | (chunk[(i * ratio) + 3] << 8))) / 2;
                            }
                            audioArray[bufferSize] = (short) value;
                            sampleTimestampUs += sampleDurationUs;
                            if (sampleTimestampUs >= endMicros2) {
                                doStop = true;
                                SLog.d("ryango last audio timestamp {}", Long.valueOf(sampleTimestampUs));
                                break;
                            }
                            i++;
                            bufferSize++;
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
            if (this.mCancel) {
                if (extractor != null) {
                    extractor.release();
                }
                if (decoder2 != null) {
                    decoder2.stop();
                    decoder2.release();
                }
                return null;
            }
            if (sampleRate != 44100) {
                long startNanos = System.nanoTime();
                ByteBuffer buffer = ByteBuffer.allocate(audioArray.length * 2);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.asShortBuffer().put(audioArray);
                byte[] out = SwVineFrameRecorder.resample(sampleRate, bufferSize, buffer.array(), 44100);
                int shortCount = out.length / 2;
                if (audioArray.length < shortCount) {
                    SLog.d("ryango reallocating audioArray old: {} new: {}", Integer.valueOf(audioArray.length), Integer.valueOf(shortCount));
                    audioArray = new short[shortCount];
                }
                ByteBuffer.wrap(out).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(audioArray, 0, shortCount);
                bufferSize = shortCount;
                SLog.d("ryango resampling audio took {} ms", Long.valueOf((System.nanoTime() - startNanos) / 1000000));
            }
            AudioTranscodeResult audioTranscodeResult = new AudioTranscodeResult(audioArray, bufferSize);
            if (extractor != null) {
                extractor.release();
            }
            if (decoder2 == null) {
                return audioTranscodeResult;
            }
            decoder2.stop();
            decoder2.release();
            return audioTranscodeResult;
        } catch (Throwable th2) {
            th = th2;
            extractor2 = extractor;
            if (extractor2 != null) {
                extractor2.release();
            }
            if (0 != 0) {
                decoder.stop();
                decoder.release();
            }
            throw th;
        }
    }

    private AudioTranscodeResult generateNoise(long durationUs) {
        Random r = new Random();
        int samples = (int) ((44100 * durationUs) / 1000000.0f);
        short[] buffer = new short[samples];
        for (int i = 0; i < samples; i++) {
            buffer[i] = (short) (r.nextInt(avutil.FF_LAMBDA_MAX) * 0.01f);
        }
        return new AudioTranscodeResult(buffer, samples);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x004f  */
    @android.annotation.TargetApi(16)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void transcodeVideo(android.net.Uri r29, long r30, long r32, android.graphics.Point r34) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 479
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.ExtractorTranscoder.transcodeVideo(android.net.Uri, long, long, android.graphics.Point):void");
    }

    public SwVineFrameRecorder getRecorder() {
        SwVineFrameRecorder recorder;
        synchronized (SwVineFrameRecorder.LOCK) {
            recorder = RecordConfigUtils.newVideoRecorder(this.mRecorderOutPath, this.mFrameRate, this.mRecorderTargetSize, false);
            try {
                recorder.start();
            } catch (SwVineFrameRecorder.Exception e) {
                throw new RuntimeException(e);
            }
        }
        return recorder;
    }

    @TargetApi(16)
    static int selectVideoTrack(MediaExtractor extractor) {
        return selectTrack(extractor, "video/");
    }

    @TargetApi(16)
    static int selectAudioTrack(MediaExtractor extractor) {
        return selectTrack(extractor, "audio/");
    }

    @TargetApi(16)
    static int selectTrack(MediaExtractor extractor, String type) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString("mime");
            if (mime.startsWith(type)) {
                Log.d("ExtractorTranscoder", "Extractor selected track " + i + " (" + mime + "): " + format);
                return i;
            }
        }
        return -1;
    }

    @TargetApi(16)
    private void doExtract(MediaExtractor extractor, int trackIndex, MediaCodec decoder, TranscodingCodecOutputSurface outputSurface, long startMicros, long endMicros) throws MediaCodec.CryptoException, InterruptedException, IOException {
        ByteBuffer[] decoderinput = decoder.getInputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int inputChunk = 0;
        boolean frameRendered = false;
        boolean outputDone = false;
        boolean inputDone = false;
        long totalDecodeNanos = 0;
        long totalDrawNanos = 0;
        extractor.seekTo(startMicros, 0);
        while (!outputDone && !this.mCancel) {
            long startDecodeNanos = System.nanoTime();
            if (!inputDone) {
                int inputBufIndex = decoder.dequeueInputBuffer(10000L);
                if (inputBufIndex >= 0) {
                    ByteBuffer inputBuf = decoderinput[inputBufIndex];
                    int chunkSize = extractor.readSampleData(inputBuf, 0);
                    if (chunkSize < 0) {
                        decoder.queueInputBuffer(inputBufIndex, 0, 0, 0L, 4);
                        inputDone = true;
                        Log.d("ExtractorTranscoder", "sent input EOS");
                    } else {
                        if (extractor.getSampleTrackIndex() != trackIndex) {
                            Log.w("ExtractorTranscoder", "WEIRD: got sample from track " + extractor.getSampleTrackIndex() + ", expected " + trackIndex);
                        }
                        long presentationTimeUs = extractor.getSampleTime();
                        decoder.queueInputBuffer(inputBufIndex, 0, chunkSize, presentationTimeUs, 0);
                        Log.d("ExtractorTranscoder", "submitted frame " + inputChunk + " to dec, size=" + chunkSize);
                        inputChunk++;
                        extractor.advance();
                    }
                } else {
                    Log.d("ExtractorTranscoder", "input buffer not available");
                }
            }
            int decoderStatus = decoder.dequeueOutputBuffer(info, 10000L);
            if (decoderStatus == -1) {
                SLog.d("no output from decoder available");
            } else if (decoderStatus == -3) {
                SLog.d("decoder output buffers changed");
            } else if (decoderStatus == -2) {
                MediaFormat newFormat = decoder.getOutputFormat();
                SLog.d("decoder output format changed: " + newFormat);
            } else if (decoderStatus >= 0) {
                SLog.d("surface decoder given buffer " + decoderStatus + " (size=" + info.size + ")");
                if ((info.flags & 4) != 0) {
                    outputDone = true;
                }
                this.mExtractorTranscoderListener.onProgressUpdate((int) ((80 * (info.presentationTimeUs - startMicros)) / this.mDurationMicros));
                if (info.presentationTimeUs >= endMicros) {
                    this.mExtractorTranscoderListener.onProgressUpdate(85);
                    outputDone = true;
                }
                boolean doRender = info.presentationTimeUs >= startMicros;
                decoder.releaseOutputBuffer(decoderStatus, doRender);
                totalDecodeNanos += System.nanoTime() - startDecodeNanos;
                if (doRender) {
                    boolean draw = true;
                    try {
                        outputSurface.awaitNewImage();
                    } catch (Exception e) {
                        if (!frameRendered) {
                            CrashUtil.log("Failed to render any frames. They all timed out. model {} mime {} ", Build.MODEL, this.mVideoMime);
                            this.mExtractorTranscoderListener.onError(new RuntimeException("Failed to render any frames. They all timed out. model " + Build.MODEL + " mime " + this.mVideoMime));
                            cancel();
                        }
                        draw = false;
                        outputDone = true;
                    }
                    if (draw) {
                        outputSurface.drawImage(true);
                        long startDrawNanos = System.nanoTime();
                        outputSurface.saveFrame(info.presentationTimeUs);
                        totalDrawNanos += System.nanoTime() - startDrawNanos;
                        frameRendered = true;
                    }
                }
            }
        }
        SLog.d("ryango time spent decoding {}", Long.valueOf(totalDecodeNanos / 1000000));
        SLog.d("ryango time spent drawing {}", Long.valueOf(totalDrawNanos / 1000000));
        outputSurface.done();
    }

    public void onFinishedVideoEncoding(byte[] videoBytes, ArrayList<VideoData> videoData, String thumbnailPath, Bitmap scaledCroppedBitmap, int frameRate) {
        if (!this.mCancel) {
            this.mSegment.getVideoData().addAll(videoData);
            this.mSegment.setThumbnailPath(thumbnailPath);
            this.mSegment.setFrameRate(frameRate);
            this.mVideoBytes = videoBytes;
            this.mVideoEncodingFinished = true;
            if (scaledCroppedBitmap != null) {
                this.mScaledCroppedBitmap = scaledCroppedBitmap;
            }
            tryFinishingRecording();
        }
    }

    public void tryFinishingRecording() {
        if (!this.mCancel && this.mAudioEncodingFinished && this.mVideoEncodingFinished) {
            try {
                long startNanos = System.nanoTime();
                this.mSegment.saveDataToDiskIfNeeded(this.mDraftFolder, this.mVideoBytes, this.mAudioArray, null);
                if (this.mExtractorTranscoderListener != null) {
                    this.mExtractorTranscoderListener.onFinishedTranscoding(this.mSegment, this.mScaledCroppedBitmap);
                }
                SLog.d("ryango saving to disk took {} ms", Long.valueOf((System.nanoTime() - startNanos) / 1000000));
                SLog.d("ryango total transcode time {} " + (System.currentTimeMillis() - this.mStartTime));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class AudioTranscodeResult {
        short[] audioArray;
        int bufferSize;

        public AudioTranscodeResult(short[] inAudioArray, int inBufferSize) {
            this.audioArray = inAudioArray;
            this.bufferSize = inBufferSize;
        }
    }
}
