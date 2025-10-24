package co.vine.android.recorder2;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import java.io.IOException;
import java.nio.ByteBuffer;

@TargetApi(18)
/* loaded from: classes.dex */
public class MuxerManager {
    private long mFirstVideoTimestamp;
    private KeyframeWrittenListener mKeyframeListener;
    private MediaMuxer mMuxer;
    private Runnable mOnReleaseRunnable;
    private static MediaFormat sVideoFormat = null;
    private static MediaFormat sAudioFormat = null;
    private boolean mVideoTrackAdded = false;
    private boolean mAudioTrackAdded = false;
    private boolean mStarted = false;
    private boolean mVideoStopped = false;
    private boolean mAudioStopped = false;
    private boolean mAudioFrameWritten = false;
    private boolean mVideoFrameWritten = false;
    private int mVideoTrackIndex = -1;
    private int mAudioTrackIndex = -1;

    public interface KeyframeWrittenListener {
        void onKeyframeWritten(long j);
    }

    public MuxerManager(String filePath, KeyframeWrittenListener listener, Runnable onReleaseRunnable) {
        this.mOnReleaseRunnable = onReleaseRunnable;
        try {
            this.mMuxer = new MediaMuxer(filePath, 0);
            if (sVideoFormat != null) {
                addVideoTrack(sVideoFormat);
            }
            if (sAudioFormat != null) {
                addAudioTrack(sAudioFormat);
            }
            this.mKeyframeListener = listener;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAudioTrack(MediaFormat format) {
        if (this.mMuxer != null && !this.mAudioTrackAdded) {
            sAudioFormat = format;
            this.mAudioTrackIndex = this.mMuxer.addTrack(format);
            this.mAudioTrackAdded = true;
            startMuxerIfReady();
        }
    }

    public void addVideoTrack(MediaFormat format) {
        if (this.mMuxer != null && !this.mVideoTrackAdded) {
            sVideoFormat = format;
            this.mVideoTrackIndex = this.mMuxer.addTrack(format);
            this.mVideoTrackAdded = true;
            startMuxerIfReady();
        }
    }

    private void startMuxerIfReady() {
        if (this.mMuxer != null && this.mVideoTrackAdded && this.mAudioTrackAdded) {
            this.mMuxer.start();
            this.mStarted = true;
        }
    }

    public void stopVideo() {
        this.mVideoStopped = true;
        maybeStopAndReleaseMuxer();
    }

    public void stopAudio() {
        this.mAudioStopped = true;
        maybeStopAndReleaseMuxer();
    }

    private void maybeStopAndReleaseMuxer() {
        if (this.mMuxer != null && this.mStarted && this.mVideoStopped && this.mAudioStopped) {
            this.mVideoTrackAdded = false;
            this.mAudioTrackAdded = false;
            this.mVideoStopped = false;
            this.mAudioStopped = false;
            this.mStarted = false;
            if (this.mVideoFrameWritten || this.mAudioFrameWritten) {
                this.mMuxer.stop();
                this.mMuxer.release();
            }
            this.mMuxer = null;
            this.mVideoFrameWritten = false;
            this.mAudioFrameWritten = false;
            if (this.mOnReleaseRunnable != null) {
                this.mOnReleaseRunnable.run();
            }
        }
    }

    public void writeAudioData(ByteBuffer buffer, MediaCodec.BufferInfo info) {
        writeSampleData(this.mAudioTrackIndex, buffer, info);
    }

    public void writeVideoData(ByteBuffer buffer, MediaCodec.BufferInfo info) {
        writeSampleData(this.mVideoTrackIndex, buffer, info);
    }

    private void writeSampleData(int trackIndex, ByteBuffer buffer, MediaCodec.BufferInfo info) {
        if (this.mStarted) {
            this.mMuxer.writeSampleData(trackIndex, buffer, info);
            if (trackIndex == this.mAudioTrackIndex) {
                this.mAudioFrameWritten = true;
                return;
            }
            if (trackIndex == this.mVideoTrackIndex) {
                if (!this.mVideoFrameWritten) {
                    this.mVideoFrameWritten = true;
                    this.mFirstVideoTimestamp = info.presentationTimeUs;
                }
                if ((info.flags & 1) > 0 && this.mKeyframeListener != null) {
                    this.mKeyframeListener.onKeyframeWritten(info.presentationTimeUs - this.mFirstVideoTimestamp);
                }
            }
        }
    }

    public boolean hasStarted() {
        return this.mStarted;
    }
}
