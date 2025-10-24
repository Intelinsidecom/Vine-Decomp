package co.vine.android.embed.player;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

/* loaded from: classes.dex */
public class VineSampleSource {
    private AudioPreparingThread mAudioPrepareThread;
    private AudioSampleTrack mAudioTrack;
    private Exception mError;
    private boolean mHasAudioTrack;
    private final VineMediaExtractor mMediaExtractor;
    private byte[] mVideoData;
    private MediaCodec.BufferInfo[] mVideoFrames;

    public VineSampleSource(Uri uri) {
        this.mMediaExtractor = new VineMediaExtractor(uri);
    }

    public void prepare(Context context) throws IOException {
        System.currentTimeMillis();
        this.mMediaExtractor.open(context);
        this.mHasAudioTrack = this.mMediaExtractor.hasAudioTrack();
        if (this.mHasAudioTrack) {
            prepareAudio(this.mMediaExtractor);
        }
        if (this.mMediaExtractor.hasVideoTrack()) {
            prepareVideo(this.mMediaExtractor);
        }
    }

    public void release() {
        AudioPreparingThread thread = this.mAudioPrepareThread;
        if (thread != null) {
            thread.cancel();
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
            this.mError = thread.getError();
            this.mAudioPrepareThread = null;
        }
        this.mMediaExtractor.release();
        this.mAudioTrack = null;
        this.mVideoData = null;
        this.mVideoFrames = null;
    }

    public boolean hasAudioTrack() {
        return this.mHasAudioTrack;
    }

    private void prepareAudio(VineMediaExtractor extractor) {
        this.mAudioTrack = extractor.getAudioSamples().track;
        this.mAudioPrepareThread = new AudioPreparingThread(extractor);
        this.mAudioPrepareThread.start();
    }

    public AudioSampleTrack getAudioTrack() {
        return this.mAudioTrack;
    }

    public ByteBuffer getPreparedAudioSamples() {
        if (this.mAudioPrepareThread != null) {
            return this.mAudioPrepareThread.getPreparedSamples();
        }
        return null;
    }

    public ByteBuffer getUnpreparedAudioSamples() {
        if (this.mAudioPrepareThread != null) {
            return this.mAudioPrepareThread.getUnpreparedSamples();
        }
        return null;
    }

    private void prepareVideo(VineMediaExtractor extractor) {
        if (extractor.mVideoSamples == null) {
            return;
        }
        this.mVideoData = extractor.mVideoSamples.data;
        this.mVideoFrames = extractor.mVideoSamples.frames;
    }

    public MediaFormat getVideoTrackFormat() {
        return this.mMediaExtractor.getVideoTrackFormat();
    }

    public byte[] getVideoFrames() {
        return this.mVideoData;
    }

    public MediaCodec.BufferInfo[] getVideoFrameInfo() {
        return this.mVideoFrames;
    }

    public Exception getError() {
        return this.mError;
    }

    public static class EncodedAudioSamples {
        public final byte[] data;
        public final long estimatedDuration;
        public final AudioFrame[] frames;
        public final AudioSampleTrack track;

        public EncodedAudioSamples(byte[] data, AudioSampleTrack track, AudioFrame[] frames, long estimatedDuration) {
            this.data = data;
            this.track = track;
            this.frames = frames;
            this.estimatedDuration = estimatedDuration;
        }
    }

    public static class EncodedVideoSamples {
        public final byte[] data;
        public final MediaCodec.BufferInfo[] frames;

        public EncodedVideoSamples(byte[] array, MediaCodec.BufferInfo[] videoFrames) {
            this.data = array;
            this.frames = videoFrames;
        }
    }

    public static class VineMediaExtractor {
        private EncodedAudioSamples mAudioSamples;
        private AudioSampleTrack mAudioTrack;
        private final int mEstimatedFileSize;
        private final Uri mUri;
        private MediaFormat mVideoFormat;
        private EncodedVideoSamples mVideoSamples;
        private int mVideoTrackIndex;

        public VineMediaExtractor(Uri uri) {
            this.mUri = uri;
            String scheme = uri.getScheme();
            if ("file".equals(scheme)) {
                this.mEstimatedFileSize = (int) (new File(uri.getPath()).length() * 1.5d);
            } else {
                this.mEstimatedFileSize = 5242880;
            }
        }

        public synchronized void release() {
            this.mAudioSamples = null;
        }

        public synchronized void open(Context context) throws IOException {
            System.currentTimeMillis();
            MediaExtractor extractor = new MediaExtractor();
            try {
                try {
                    extractor.setDataSource(context, this.mUri, (Map<String, String>) null);
                    int trackCount = extractor.getTrackCount();
                    this.mAudioTrack = null;
                    int videoTrackIndex = -1;
                    for (int i = 0; i < trackCount; i++) {
                        extractor.selectTrack(i);
                        MediaFormat format = extractor.getTrackFormat(i);
                        String mime = format.getString("mime");
                        if (this.mAudioTrack == null && isAudio(mime)) {
                            int sampleRate = -1;
                            if (format.containsKey("sample-rate")) {
                                sampleRate = format.getInteger("sample-rate");
                            }
                            this.mAudioTrack = new AudioSampleTrack(mime, i, sampleRate, format);
                        } else if (isVideo(mime)) {
                            videoTrackIndex = i;
                            this.mVideoFormat = extractor.getTrackFormat(videoTrackIndex);
                        }
                        if (videoTrackIndex != -1 && this.mAudioTrack != null) {
                            break;
                        }
                    }
                    this.mVideoTrackIndex = videoTrackIndex;
                    byte[] audioData = new byte[5242880];
                    ArrayList<AudioFrame> audioFrames = new ArrayList<>();
                    ByteBuffer audioBuffer = ByteBuffer.wrap(audioData);
                    ByteBuffer videoBuffer = ByteBuffer.allocate((int) (this.mEstimatedFileSize * 1.5d));
                    long lastTimestamp = 0;
                    ArrayList<MediaCodec.BufferInfo> frames = new ArrayList<>();
                    int offsetAudio = 0;
                    int offsetVideo = 0;
                    for (int trackIndex = extractor.getSampleTrackIndex(); trackIndex >= 0; trackIndex = extractor.getSampleTrackIndex()) {
                        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                        if (trackIndex == this.mVideoTrackIndex) {
                            bufferInfo.offset = offsetVideo;
                            bufferInfo.size = extractor.readSampleData(videoBuffer, offsetVideo);
                            if (bufferInfo.size < 0) {
                                bufferInfo.size = 0;
                            } else {
                                offsetVideo += bufferInfo.size;
                                if (videoBuffer.capacity() - offsetVideo < 2073600) {
                                    ByteBuffer newBuffer = ByteBuffer.allocate(videoBuffer.capacity() * 2);
                                    videoBuffer.position(0);
                                    newBuffer.put(videoBuffer);
                                    videoBuffer = newBuffer;
                                }
                                bufferInfo.presentationTimeUs = extractor.getSampleTime();
                                bufferInfo.flags = extractor.getSampleFlags();
                                frames.add(bufferInfo);
                                if (bufferInfo.presentationTimeUs > lastTimestamp) {
                                    lastTimestamp = bufferInfo.presentationTimeUs;
                                }
                            }
                        } else if (this.mAudioTrack != null && trackIndex == this.mAudioTrack.index) {
                            bufferInfo.offset = offsetAudio;
                            bufferInfo.size = extractor.readSampleData(audioBuffer, offsetAudio);
                            if (bufferInfo.size < 0) {
                                bufferInfo.size = 0;
                                Log.i("VineSampleSource", "EOS reached? ");
                            } else {
                                offsetAudio += bufferInfo.size;
                                if (audioBuffer.capacity() - offsetAudio < 2073600) {
                                    Log.w("VineSampleSource", "Processing large file?");
                                    audioData = new byte[audioBuffer.capacity() * 2];
                                    ByteBuffer newBuffer2 = ByteBuffer.wrap(audioData);
                                    audioBuffer.position(0);
                                    newBuffer2.put(audioBuffer);
                                    audioBuffer = newBuffer2;
                                }
                                bufferInfo.presentationTimeUs = extractor.getSampleTime();
                                bufferInfo.flags = extractor.getSampleFlags();
                                audioFrames.add(new AudioFrame(this.mAudioTrack, bufferInfo));
                                if (bufferInfo.presentationTimeUs > lastTimestamp) {
                                    lastTimestamp = bufferInfo.presentationTimeUs;
                                }
                            }
                        }
                        extractor.advance();
                    }
                    MediaCodec.BufferInfo[] videoFrames = new MediaCodec.BufferInfo[frames.size()];
                    frames.toArray(videoFrames);
                    this.mVideoSamples = new EncodedVideoSamples(videoBuffer.array(), videoFrames);
                    this.mAudioSamples = new EncodedAudioSamples(audioData, this.mAudioTrack, (AudioFrame[]) audioFrames.toArray(new AudioFrame[audioFrames.size()]), 1000 + lastTimestamp);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            } finally {
                extractor.release();
            }
        }

        public EncodedAudioSamples getAudioSamples() {
            return this.mAudioSamples;
        }

        private boolean isAudio(String mimeType) {
            int indexOfSlash = mimeType.indexOf(47);
            if (indexOfSlash == -1) {
                throw new IllegalArgumentException("Invalid mime type: " + mimeType);
            }
            return "audio".equals(mimeType.substring(0, indexOfSlash));
        }

        private boolean isVideo(String mimeType) {
            int indexOfSlash = mimeType.indexOf(47);
            if (indexOfSlash == -1) {
                throw new IllegalArgumentException("Invalid mime type: " + mimeType);
            }
            return "video".equals(mimeType.substring(0, indexOfSlash));
        }

        public boolean hasAudioTrack() {
            return this.mAudioTrack != null;
        }

        public boolean hasVideoTrack() {
            return this.mVideoTrackIndex >= 0;
        }

        public MediaFormat getVideoTrackFormat() {
            return this.mVideoFormat;
        }
    }
}
