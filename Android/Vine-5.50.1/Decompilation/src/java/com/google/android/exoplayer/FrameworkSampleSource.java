package com.google.android.exoplayer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaExtractor;
import android.net.Uri;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@TargetApi(16)
@Deprecated
/* loaded from: classes.dex */
public final class FrameworkSampleSource implements SampleSource, SampleSource.SampleSourceReader {
    private final Context context;
    private MediaExtractor extractor;
    private final FileDescriptor fileDescriptor;
    private final long fileDescriptorLength;
    private final long fileDescriptorOffset;
    private final Map<String, String> headers;
    private long lastSeekPositionUs;
    private boolean[] pendingDiscontinuities;
    private long pendingSeekPositionUs;
    private IOException preparationError;
    private boolean prepared;
    private int remainingReleaseCount;
    private MediaFormat[] trackFormats;
    private int[] trackStates;
    private final Uri uri;

    public FrameworkSampleSource(Context context, Uri uri, Map<String, String> headers) {
        Assertions.checkState(Util.SDK_INT >= 16);
        this.context = (Context) Assertions.checkNotNull(context);
        this.uri = (Uri) Assertions.checkNotNull(uri);
        this.headers = headers;
        this.fileDescriptor = null;
        this.fileDescriptorOffset = 0L;
        this.fileDescriptorLength = 0L;
    }

    @Override // com.google.android.exoplayer.SampleSource
    public SampleSource.SampleSourceReader register() {
        this.remainingReleaseCount++;
        return this;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public boolean prepare(long positionUs) throws IOException {
        if (!this.prepared) {
            if (this.preparationError != null) {
                return false;
            }
            this.extractor = new MediaExtractor();
            try {
                if (this.context != null) {
                    this.extractor.setDataSource(this.context, this.uri, this.headers);
                } else {
                    this.extractor.setDataSource(this.fileDescriptor, this.fileDescriptorOffset, this.fileDescriptorLength);
                }
                this.trackStates = new int[this.extractor.getTrackCount()];
                this.pendingDiscontinuities = new boolean[this.trackStates.length];
                this.trackFormats = new MediaFormat[this.trackStates.length];
                for (int i = 0; i < this.trackStates.length; i++) {
                    this.trackFormats[i] = createMediaFormat(this.extractor.getTrackFormat(i));
                }
                this.prepared = true;
            } catch (IOException e) {
                this.preparationError = e;
                return false;
            }
        }
        return true;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public int getTrackCount() {
        Assertions.checkState(this.prepared);
        return this.trackStates.length;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public MediaFormat getFormat(int track) {
        Assertions.checkState(this.prepared);
        return this.trackFormats[track];
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void enable(int track, long positionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackStates[track] == 0);
        this.trackStates[track] = 1;
        this.extractor.selectTrack(track);
        seekToUsInternal(positionUs, positionUs != 0);
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public boolean continueBuffering(int track, long positionUs) {
        return true;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public long readDiscontinuity(int track) {
        if (!this.pendingDiscontinuities[track]) {
            return Long.MIN_VALUE;
        }
        this.pendingDiscontinuities[track] = false;
        return this.lastSeekPositionUs;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public int readData(int track, long positionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackStates[track] != 0);
        if (this.pendingDiscontinuities[track]) {
            return -2;
        }
        if (this.trackStates[track] != 2) {
            formatHolder.format = this.trackFormats[track];
            formatHolder.drmInitData = Util.SDK_INT >= 18 ? getDrmInitDataV18() : null;
            this.trackStates[track] = 2;
            return -4;
        }
        int extractorTrackIndex = this.extractor.getSampleTrackIndex();
        if (extractorTrackIndex == track) {
            if (sampleHolder.data != null) {
                int offset = sampleHolder.data.position();
                sampleHolder.size = this.extractor.readSampleData(sampleHolder.data, offset);
                sampleHolder.data.position(sampleHolder.size + offset);
            } else {
                sampleHolder.size = 0;
            }
            sampleHolder.timeUs = this.extractor.getSampleTime();
            sampleHolder.flags = this.extractor.getSampleFlags() & 3;
            if (sampleHolder.isEncrypted()) {
                sampleHolder.cryptoInfo.setFromExtractorV16(this.extractor);
            }
            this.pendingSeekPositionUs = -1L;
            this.extractor.advance();
            return -3;
        }
        return extractorTrackIndex < 0 ? -1 : -2;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void disable(int track) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackStates[track] != 0);
        this.extractor.unselectTrack(track);
        this.pendingDiscontinuities[track] = false;
        this.trackStates[track] = 0;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void maybeThrowError() throws IOException {
        if (this.preparationError != null) {
            throw this.preparationError;
        }
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void seekToUs(long positionUs) {
        Assertions.checkState(this.prepared);
        seekToUsInternal(positionUs, false);
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public long getBufferedPositionUs() {
        Assertions.checkState(this.prepared);
        long bufferedDurationUs = this.extractor.getCachedDuration();
        if (bufferedDurationUs == -1) {
            return -1L;
        }
        long sampleTime = this.extractor.getSampleTime();
        if (sampleTime == -1) {
            return -3L;
        }
        return sampleTime + bufferedDurationUs;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void release() {
        Assertions.checkState(this.remainingReleaseCount > 0);
        int i = this.remainingReleaseCount - 1;
        this.remainingReleaseCount = i;
        if (i == 0 && this.extractor != null) {
            this.extractor.release();
            this.extractor = null;
        }
    }

    @TargetApi(18)
    private DrmInitData getDrmInitDataV18() {
        Map<UUID, byte[]> psshInfo = this.extractor.getPsshInfo();
        if (psshInfo == null || psshInfo.isEmpty()) {
            return null;
        }
        DrmInitData.Mapped drmInitData = new DrmInitData.Mapped();
        for (UUID uuid : psshInfo.keySet()) {
            byte[] psshAtom = PsshAtomUtil.buildPsshAtom(uuid, psshInfo.get(uuid));
            drmInitData.put(uuid, new DrmInitData.SchemeInitData("video/mp4", psshAtom));
        }
        return drmInitData;
    }

    private void seekToUsInternal(long positionUs, boolean force) {
        if (force || this.pendingSeekPositionUs != positionUs) {
            this.lastSeekPositionUs = positionUs;
            this.pendingSeekPositionUs = positionUs;
            this.extractor.seekTo(positionUs, 0);
            for (int i = 0; i < this.trackStates.length; i++) {
                if (this.trackStates[i] != 0) {
                    this.pendingDiscontinuities[i] = true;
                }
            }
        }
    }

    @SuppressLint({"InlinedApi"})
    private static MediaFormat createMediaFormat(android.media.MediaFormat format) {
        String mimeType = format.getString("mime");
        String language = getOptionalStringV16(format, "language");
        int maxInputSize = getOptionalIntegerV16(format, "max-input-size");
        int width = getOptionalIntegerV16(format, "width");
        int height = getOptionalIntegerV16(format, "height");
        int rotationDegrees = getOptionalIntegerV16(format, "rotation-degrees");
        int channelCount = getOptionalIntegerV16(format, "channel-count");
        int sampleRate = getOptionalIntegerV16(format, "sample-rate");
        ArrayList<byte[]> initializationData = new ArrayList<>();
        for (int i = 0; format.containsKey("csd-" + i); i++) {
            ByteBuffer buffer = format.getByteBuffer("csd-" + i);
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            initializationData.add(data);
            buffer.flip();
        }
        long durationUs = format.containsKey("durationUs") ? format.getLong("durationUs") : -1L;
        MediaFormat mediaFormat = new MediaFormat(null, mimeType, -1, maxInputSize, durationUs, width, height, rotationDegrees, -1.0f, channelCount, sampleRate, language, Long.MAX_VALUE, initializationData, false, -1, -1);
        mediaFormat.setFrameworkFormatV16(format);
        return mediaFormat;
    }

    @TargetApi(16)
    private static final String getOptionalStringV16(android.media.MediaFormat format, String key) {
        if (format.containsKey(key)) {
            return format.getString(key);
        }
        return null;
    }

    @TargetApi(16)
    private static final int getOptionalIntegerV16(android.media.MediaFormat format, String key) {
        if (format.containsKey(key)) {
            return format.getInteger(key);
        }
        return -1;
    }
}
