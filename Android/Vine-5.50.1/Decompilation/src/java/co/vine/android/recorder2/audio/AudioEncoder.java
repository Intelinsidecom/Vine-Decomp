package co.vine.android.recorder2.audio;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import co.vine.android.recorder2.MuxerManager;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/* loaded from: classes.dex */
public class AudioEncoder {
    private MediaCodec.BufferInfo mAudioBufferInfo;
    private MediaCodec mAudioEncoder;
    private MediaFormat mAudioFormat;
    AudioSoftwarePoller mAudioSoftwarePoller;
    private MuxerManager mMuxerManager;
    boolean mEosReceived = false;
    boolean mEosSentToEncoder = false;
    boolean mStopReceived = false;
    int mFrameCount = 0;
    int mEncodingServiceQueueLength = 0;
    private ExecutorService mEncodingService = Executors.newSingleThreadExecutor();

    enum EncoderTaskType {
        ENCODE_FRAME,
        FINALIZE_ENCODER
    }

    public AudioEncoder(MuxerManager muxerManager) {
        this.mMuxerManager = muxerManager;
        prepare();
    }

    public void setAudioSoftwarePoller(AudioSoftwarePoller audioSoftwarePoller) {
        this.mAudioSoftwarePoller = audioSoftwarePoller;
    }

    private void prepare() {
        this.mFrameCount = 0;
        this.mEosReceived = false;
        this.mEosSentToEncoder = false;
        this.mStopReceived = false;
        this.mAudioBufferInfo = new MediaCodec.BufferInfo();
        this.mAudioFormat = buildFormat();
        this.mAudioEncoder = buildEncoder(this.mAudioFormat);
        this.mAudioEncoder.start();
    }

    private static String getEncoderNameForSamsung() {
        MediaCodecInfo[] mediaCodecInfos;
        String name = null;
        if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            if (Build.VERSION.SDK_INT > 21) {
                MediaCodecList codecList = new MediaCodecList(0);
                mediaCodecInfos = codecList.getCodecInfos();
            } else {
                int numCodecs = MediaCodecList.getCodecCount();
                mediaCodecInfos = new MediaCodecInfo[numCodecs];
                for (int i = 0; i < numCodecs; i++) {
                    MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
                    mediaCodecInfos[i] = codecInfo;
                }
            }
            for (MediaCodecInfo info : mediaCodecInfos) {
                if (info.isEncoder()) {
                    try {
                        MediaCodecInfo.CodecCapabilities caps = info.getCapabilitiesForType("audio/mp4a-latm");
                        if (caps != null && info.getName().startsWith("OMX.SEC")) {
                            name = info.getName();
                            break;
                        }
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        }
        return name;
    }

    public static MediaCodec buildEncoder(MediaFormat format) throws IOException {
        try {
            String samsungEncoder = getEncoderNameForSamsung();
            MediaCodec audioEncoder = null;
            boolean useDefaultEncoder = false;
            if (samsungEncoder != null) {
                try {
                    audioEncoder = MediaCodec.createByCodecName(samsungEncoder);
                    audioEncoder.configure(format, (Surface) null, (MediaCrypto) null, 1);
                } catch (Exception e) {
                    useDefaultEncoder = true;
                }
            }
            if (audioEncoder == null || useDefaultEncoder) {
                MediaCodec audioEncoder2 = MediaCodec.createEncoderByType("audio/mp4a-latm");
                audioEncoder2.configure(format, (Surface) null, (MediaCrypto) null, 1);
                return audioEncoder2;
            }
            return audioEncoder;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public static MediaFormat buildFormat() {
        MediaFormat audioFormat = new MediaFormat();
        audioFormat.setString("mime", "audio/mp4a-latm");
        audioFormat.setInteger("aac-profile", 2);
        audioFormat.setInteger("sample-rate", 44100);
        audioFormat.setInteger("channel-count", 1);
        audioFormat.setInteger("bitrate", 64000);
        audioFormat.setInteger("max-input-size", 16384);
        return audioFormat;
    }

    public void stop() {
        if (!this.mEncodingService.isShutdown()) {
            this.mEncodingService.submit(new EncoderTask(this, EncoderTaskType.FINALIZE_ENCODER));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopInternal() {
        this.mStopReceived = true;
        this.mEosReceived = true;
    }

    private void closeEncoderAndMuxer(MediaCodec encoder, MediaCodec.BufferInfo bufferInfo) throws InterruptedException {
        drainEncoder(encoder, bufferInfo, true);
        try {
            encoder.stop();
            encoder.release();
            closeMuxer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeMuxer() {
        this.mMuxerManager.stopAudio();
        this.mMuxerManager = null;
    }

    public void offerAudioEncoder(byte[] input, long presentationTimeStampNs) {
        if (!this.mEncodingService.isShutdown()) {
            try {
                this.mEncodingService.submit(new EncoderTask(this, input, presentationTimeStampNs));
                this.mEncodingServiceQueueLength++;
            } catch (RejectedExecutionException e) {
                SLog.e("ryango rejected caught excep", (Throwable) e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void offerAudioEncoderInternal(byte[] input, long presentationTimeNs) throws InterruptedException {
        if ((this.mEosSentToEncoder && this.mStopReceived && this.mFrameCount > 0) || input == null) {
            if (this.mEosReceived) {
                Log.i("AudioEncoder", "EOS received in offerAudioEncoder");
                closeEncoderAndMuxer(this.mAudioEncoder, this.mAudioBufferInfo);
                this.mEosSentToEncoder = true;
                if (!this.mStopReceived) {
                    prepare();
                    return;
                } else {
                    this.mAudioSoftwarePoller.setAudioEncoder(null);
                    this.mEncodingService.shutdown();
                    return;
                }
            }
            return;
        }
        drainEncoder(this.mAudioEncoder, this.mAudioBufferInfo, false);
        try {
            ByteBuffer[] inputBuffers = this.mAudioEncoder.getInputBuffers();
            int inputBufferIndex = this.mAudioEncoder.dequeueInputBuffer(-1L);
            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(input);
                if (this.mAudioSoftwarePoller != null) {
                    this.mAudioSoftwarePoller.recycleInputBuffer(input);
                }
                long presentationTimeUs = presentationTimeNs / 1000;
                if (input.length > 0) {
                    this.mFrameCount++;
                }
                if (this.mEosReceived) {
                    Log.i("AudioEncoder", "EOS received in offerEncoder");
                    this.mAudioEncoder.queueInputBuffer(inputBufferIndex, 0, input.length, presentationTimeUs, 4);
                    closeEncoderAndMuxer(this.mAudioEncoder, this.mAudioBufferInfo);
                    this.mEosSentToEncoder = true;
                    if (this.mStopReceived) {
                        Log.i("AudioEncoder", "Stopping Encoding Service");
                        this.mEncodingService.shutdown();
                        return;
                    }
                    return;
                }
                this.mAudioEncoder.queueInputBuffer(inputBufferIndex, 0, input.length, presentationTimeUs, 0);
            }
        } catch (Throwable t) {
            Log.e("AudioEncoder", "offerAudioEncoderInternal exception");
            t.printStackTrace();
        }
    }

    private void drainEncoder(MediaCodec encoder, MediaCodec.BufferInfo bufferInfo, boolean endOfStream) throws InterruptedException {
        ByteBuffer[] encoderOutputBuffers = encoder.getOutputBuffers();
        while (true) {
            int encoderStatus = encoder.dequeueOutputBuffer(bufferInfo, 100L);
            if (encoderStatus == -1) {
                if (!endOfStream) {
                    return;
                }
            } else if (encoderStatus == -3) {
                encoderOutputBuffers = encoder.getOutputBuffers();
            } else if (encoderStatus == -2) {
                MediaFormat newFormat = encoder.getOutputFormat();
                this.mMuxerManager.addAudioTrack(newFormat);
                Log.d("AudioEncoder", "mEncoder output format changed: " + newFormat + ". Added track index: ");
            } else if (encoderStatus < 0) {
                Log.w("AudioEncoder", "unexpected result from mEncoder.dequeueOutputBuffer: " + encoderStatus);
            } else {
                ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                if (encodedData == null) {
                    throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
                }
                if ((bufferInfo.flags & 2) != 0) {
                    bufferInfo.size = 0;
                }
                if (bufferInfo.size != 0) {
                    while (!this.mMuxerManager.hasStarted()) {
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e) {
                        }
                    }
                    encodedData.position(bufferInfo.offset);
                    encodedData.limit(bufferInfo.offset + bufferInfo.size);
                    this.mMuxerManager.writeAudioData(encodedData, bufferInfo);
                }
                encoder.releaseOutputBuffer(encoderStatus, false);
                if ((bufferInfo.flags & 4) != 0) {
                    if (!endOfStream) {
                        Log.w("AudioEncoder", "reached end of stream unexpectedly");
                        return;
                    }
                    return;
                }
            }
        }
    }

    private class EncoderTask implements Runnable {
        private byte[] mAudioData;
        private AudioEncoder mEncoder;
        boolean mInitialized = false;
        long mPresentationTimestampNS;
        private EncoderTaskType mType;

        public EncoderTask(AudioEncoder encoder, EncoderTaskType type) {
            setEncoder(encoder);
            this.mType = type;
            switch (type) {
                case FINALIZE_ENCODER:
                    setFinalizeEncoderParams();
                    break;
            }
        }

        public EncoderTask(AudioEncoder encoder, byte[] audio_data, long pts) {
            setEncoder(encoder);
            setEncodeFrameParams(audio_data, pts);
        }

        private void setEncoder(AudioEncoder encoder) {
            this.mEncoder = encoder;
        }

        private void setFinalizeEncoderParams() {
            this.mInitialized = true;
        }

        private void setEncodeFrameParams(byte[] audio_data, long pts) {
            this.mAudioData = audio_data;
            this.mPresentationTimestampNS = pts;
            this.mInitialized = true;
            this.mType = EncoderTaskType.ENCODE_FRAME;
        }

        private void encodeFrame() throws InterruptedException {
            if (this.mEncoder != null && this.mAudioData != null) {
                this.mEncoder.offerAudioEncoderInternal(this.mAudioData, this.mPresentationTimestampNS);
                this.mAudioData = null;
            }
        }

        private void finalizeEncoder() {
            this.mEncoder.stopInternal();
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            if (this.mInitialized) {
                switch (this.mType) {
                    case FINALIZE_ENCODER:
                        finalizeEncoder();
                        break;
                    case ENCODE_FRAME:
                        encodeFrame();
                        break;
                }
                this.mInitialized = false;
                AudioEncoder audioEncoder = AudioEncoder.this;
                audioEncoder.mEncodingServiceQueueLength--;
                return;
            }
            Log.e("encoderTask", "run() called but EncoderTask not initialized");
        }
    }
}
