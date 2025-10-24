package co.vine.android.recorder2;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import co.vine.android.recorder2.model.Segment;
import co.vine.android.recorder2.util.MediaExtractorUtil;
import com.edisonwang.android.slog.SLog;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SegmentCombiner {

    private static class TrackIndices {
        int muxerAudioTrackIndex;
        int muxerVideoTrackIndex;

        public TrackIndices(int muxerVideoTrackIndex, int muxerAudioTrackIndex) {
            this.muxerVideoTrackIndex = muxerVideoTrackIndex;
            this.muxerAudioTrackIndex = muxerAudioTrackIndex;
        }
    }

    public static void combineSegments(Context context, ArrayList<Segment> segments, String outPath) throws Throwable {
        int muxerTrackIndex;
        if (segments.size() > 0) {
            MediaExtractor extractor = null;
            try {
                MediaMuxer muxer = new MediaMuxer(outPath, 0);
                try {
                    TrackIndices muxerTrackIndices = setupMuxerAndReturnTrackIndices(muxer, segments.get(0).getVideoPath());
                    muxer.start();
                    ByteBuffer buffer = ByteBuffer.allocate(777600);
                    MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                    long largestTimestamp = 0;
                    Iterator<Segment> it = segments.iterator();
                    while (true) {
                        try {
                            MediaExtractor mediaExtractor = extractor;
                            if (it.hasNext()) {
                                Segment segment = it.next();
                                String path = segment.getVideoPath();
                                MediaExtractor extractor2 = new MediaExtractor();
                                extractor2.setDataSource(path);
                                long timestampOffset = largestTimestamp;
                                int extractorVideoTrackIndex = MediaExtractorUtil.selectVideoTrack(extractor2);
                                int extractorAudioTrackIndex = MediaExtractorUtil.selectAudioTrack(extractor2);
                                extractor2.selectTrack(extractorVideoTrackIndex);
                                extractor2.selectTrack(extractorAudioTrackIndex);
                                long[] trimPoints = segment.getTrimPointsMs();
                                extractor2.seekTo(trimPoints[0] * 1000, 2);
                                MediaExtractor silenceMediaExtractor = null;
                                if (segment.isSilenced()) {
                                    silenceMediaExtractor = new MediaExtractor();
                                    AssetFileDescriptor fd = context.getAssets().openFd("whiteNoise.m4a");
                                    silenceMediaExtractor.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                                    silenceMediaExtractor.selectTrack(0);
                                }
                                boolean done = false;
                                while (!done) {
                                    int size = extractor2.readSampleData(buffer, 0);
                                    if (size < 0) {
                                        done = true;
                                    } else {
                                        int extractorSampleTrackIndex = extractor2.getSampleTrackIndex();
                                        if (extractorSampleTrackIndex == extractorVideoTrackIndex) {
                                            muxerTrackIndex = muxerTrackIndices.muxerVideoTrackIndex;
                                        } else if (extractorSampleTrackIndex == extractorAudioTrackIndex) {
                                            muxerTrackIndex = muxerTrackIndices.muxerAudioTrackIndex;
                                        } else {
                                            done = !extractor2.advance();
                                        }
                                        long sampleTime = timestampOffset + extractor2.getSampleTime();
                                        if (sampleTime > largestTimestamp) {
                                            largestTimestamp = sampleTime;
                                        }
                                        int flags = extractor2.getSampleFlags();
                                        if (segment.isSilenced() && muxerTrackIndex == muxerTrackIndices.muxerAudioTrackIndex) {
                                            size = silenceMediaExtractor.readSampleData(buffer, 0);
                                            flags = silenceMediaExtractor.getSampleFlags();
                                            silenceMediaExtractor.advance();
                                            if (size >= 0) {
                                            }
                                        }
                                        info.set(0, size, sampleTime, flags);
                                        muxer.writeSampleData(muxerTrackIndex, buffer, info);
                                        done = !extractor2.advance() || extractor2.getSampleTime() > trimPoints[1] * 1000 || sampleTime > 6450000;
                                    }
                                }
                                largestTimestamp += 33333;
                                extractor2.release();
                                extractor = null;
                                if (silenceMediaExtractor != null) {
                                    silenceMediaExtractor.release();
                                }
                            } else {
                                muxer.stop();
                                muxer.release();
                                return;
                            }
                        } catch (Exception e) {
                            e = e;
                            SLog.e("extract excep {}", (Throwable) e);
                            throw new RuntimeException(e);
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Exception e3) {
                e = e3;
            }
        }
    }

    private static TrackIndices setupMuxerAndReturnTrackIndices(MediaMuxer muxer, String firstPath) throws Throwable {
        MediaExtractor extractor;
        MediaExtractor extractor2 = null;
        try {
            extractor = new MediaExtractor();
        } catch (Throwable th) {
            th = th;
        }
        try {
            extractor.setDataSource(firstPath);
            int extractorVideoTrackIndex = MediaExtractorUtil.selectVideoTrack(extractor);
            MediaFormat videoFormat = extractor.getTrackFormat(extractorVideoTrackIndex);
            int extractorAudioTrackIndex = MediaExtractorUtil.selectAudioTrack(extractor);
            MediaFormat audioFormat = extractor.getTrackFormat(extractorAudioTrackIndex);
            TrackIndices trackIndices = new TrackIndices(muxer.addTrack(videoFormat), muxer.addTrack(audioFormat));
            if (extractor != null) {
                extractor.release();
            }
            return trackIndices;
        } catch (Throwable th2) {
            th = th2;
            extractor2 = extractor;
            if (extractor2 != null) {
                extractor2.release();
            }
            throw th;
        }
    }
}
