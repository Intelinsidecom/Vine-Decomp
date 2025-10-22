package co.vine.android.recorder2.video;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Surface;
import co.vine.android.recorder2.MuxerManager;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class VideoEncoderCore {
    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();
    private int mCountSinceLastIFrame = 0;
    private MediaCodec mEncoder;
    private Surface mInputSurface;
    private MuxerManager mMuxerManager;

    public VideoEncoderCore(int width, int height, int bitRate) throws IOException {
        MediaFormat format = MediaFormat.createVideoFormat("video/avc", width, height);
        format.setInteger("color-format", 2130708361);
        format.setInteger("bitrate", bitRate);
        format.setInteger("frame-rate", 30);
        format.setInteger("i-frame-interval", 1);
        this.mEncoder = MediaCodec.createEncoderByType("video/avc");
        this.mEncoder.configure(format, (Surface) null, (MediaCrypto) null, 1);
        this.mInputSurface = this.mEncoder.createInputSurface();
        this.mEncoder.start();
    }

    public void setMuxerManager(MuxerManager manager) {
        this.mMuxerManager = manager;
    }

    public Surface getInputSurface() {
        return this.mInputSurface;
    }

    public void release() {
        if (this.mEncoder != null) {
            this.mEncoder.stop();
            this.mEncoder.release();
            this.mEncoder = null;
        }
        if (this.mMuxerManager != null && this.mCountSinceLastIFrame > 0) {
            this.mMuxerManager.stopVideo();
            this.mMuxerManager = null;
        }
    }

    public void drainEncoder(boolean endOfStream) throws InterruptedException {
        if (endOfStream) {
            this.mEncoder.signalEndOfInputStream();
        }
        ByteBuffer[] encoderOutputBuffers = this.mEncoder.getOutputBuffers();
        while (true) {
            int encoderStatus = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, 3000L);
            if (encoderStatus == -1) {
                if (!endOfStream) {
                    return;
                }
            } else if (encoderStatus == -3) {
                encoderOutputBuffers = this.mEncoder.getOutputBuffers();
            } else if (encoderStatus == -2) {
                MediaFormat newFormat = this.mEncoder.getOutputFormat();
                SLog.d("encoder output format changed: " + newFormat);
                this.mMuxerManager.addVideoTrack(newFormat);
            } else if (encoderStatus < 0) {
                SLog.d("unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
            } else {
                ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                if (encodedData == null) {
                    throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
                }
                if ((this.mBufferInfo.flags & 2) != 0) {
                    this.mBufferInfo.size = 0;
                }
                requestIFrame(this.mEncoder, this.mCountSinceLastIFrame);
                if (this.mBufferInfo.size != 0) {
                    while (!this.mMuxerManager.hasStarted()) {
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e) {
                        }
                    }
                    encodedData.position(this.mBufferInfo.offset);
                    encodedData.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                    this.mMuxerManager.writeVideoData(encodedData, this.mBufferInfo);
                    this.mCountSinceLastIFrame++;
                    if ((this.mBufferInfo.flags & 1) > 0) {
                        this.mCountSinceLastIFrame = 1;
                    }
                }
                this.mEncoder.releaseOutputBuffer(encoderStatus, false);
                if ((this.mBufferInfo.flags & 4) != 0) {
                    if (!endOfStream) {
                        SLog.w("reached end of stream unexpectedly");
                        return;
                    }
                    return;
                }
            }
        }
    }

    public static void requestIFrame(MediaCodec encoder, int countSinceLastIFrame) {
        if (Build.VERSION.SDK_INT >= 19 && countSinceLastIFrame == 10) {
            Bundle b = new Bundle();
            b.putInt("request-sync", 0);
            encoder.setParameters(b);
        }
    }
}
