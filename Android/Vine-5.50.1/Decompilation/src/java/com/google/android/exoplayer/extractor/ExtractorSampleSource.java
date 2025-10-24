package com.google.android.exoplayer.extractor;

import android.net.Uri;
import android.os.SystemClock;
import android.util.SparseArray;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.MediaFormatHolder;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class ExtractorSampleSource implements SampleSource, SampleSource.SampleSourceReader, ExtractorOutput, Loader.Callback {
    private static final List<Class<? extends Extractor>> DEFAULT_EXTRACTOR_CLASSES = new ArrayList();
    private final Allocator allocator;
    private IOException currentLoadableException;
    private int currentLoadableExceptionCount;
    private long currentLoadableExceptionTimestamp;
    private final DataSource dataSource;
    private long downstreamPositionUs;
    private volatile DrmInitData drmInitData;
    private int enabledTrackCount;
    private int extractedSampleCount;
    private int extractedSampleCountAtStartOfLoad;
    private final ExtractorHolder extractorHolder;
    private boolean havePendingNextSampleUs;
    private long lastSeekPositionUs;
    private ExtractingLoadable loadable;
    private Loader loader;
    private boolean loadingFinished;
    private long maxTrackDurationUs;
    private MediaFormat[] mediaFormats;
    private final int minLoadableRetryCount;
    private boolean[] pendingDiscontinuities;
    private boolean[] pendingMediaFormat;
    private long pendingNextSampleUs;
    private long pendingResetPositionUs;
    private boolean prepared;
    private int remainingReleaseCount;
    private final int requestedBufferSize;
    private final SparseArray<InternalTrackOutput> sampleQueues;
    private long sampleTimeOffsetUs;
    private volatile SeekMap seekMap;
    private boolean[] trackEnabledStates;
    private volatile boolean tracksBuilt;
    private final Uri uri;

    static /* synthetic */ int access$008(ExtractorSampleSource x0) {
        int i = x0.extractedSampleCount;
        x0.extractedSampleCount = i + 1;
        return i;
    }

    public static final class UnrecognizedInputFormatException extends ParserException {
        public UnrecognizedInputFormatException(Extractor[] extractors) {
            super("None of the available extractors (" + Util.getCommaDelimitedSimpleClassNames(extractors) + ") could read the stream.");
        }
    }

    static {
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.webm.WebmExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.mp4.FragmentedMp4Extractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e2) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.mp4.Mp4Extractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e3) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.mp3.Mp3Extractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e4) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.ts.AdtsExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e5) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.ts.TsExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e6) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.flv.FlvExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e7) {
        }
    }

    public ExtractorSampleSource(Uri uri, DataSource dataSource, Allocator allocator, int requestedBufferSize, Extractor... extractors) {
        this(uri, dataSource, allocator, requestedBufferSize, -1, extractors);
    }

    public ExtractorSampleSource(Uri uri, DataSource dataSource, Allocator allocator, int requestedBufferSize, int minLoadableRetryCount, Extractor... extractors) {
        this.uri = uri;
        this.dataSource = dataSource;
        this.allocator = allocator;
        this.requestedBufferSize = requestedBufferSize;
        this.minLoadableRetryCount = minLoadableRetryCount;
        if (extractors == null || extractors.length == 0) {
            extractors = new Extractor[DEFAULT_EXTRACTOR_CLASSES.size()];
            for (int i = 0; i < extractors.length; i++) {
                try {
                    extractors[i] = DEFAULT_EXTRACTOR_CLASSES.get(i).newInstance();
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Unexpected error creating default extractor", e);
                } catch (InstantiationException e2) {
                    throw new IllegalStateException("Unexpected error creating default extractor", e2);
                }
            }
        }
        this.extractorHolder = new ExtractorHolder(extractors, this);
        this.sampleQueues = new SparseArray<>();
        this.pendingResetPositionUs = Long.MIN_VALUE;
    }

    @Override // com.google.android.exoplayer.SampleSource
    public SampleSource.SampleSourceReader register() {
        this.remainingReleaseCount++;
        return this;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public boolean prepare(long positionUs) {
        if (this.prepared) {
            return true;
        }
        if (this.loader == null) {
            this.loader = new Loader("Loader:ExtractorSampleSource");
        }
        maybeStartLoading();
        if (this.seekMap != null && this.tracksBuilt && haveFormatsForAllTracks()) {
            int trackCount = this.sampleQueues.size();
            this.trackEnabledStates = new boolean[trackCount];
            this.pendingDiscontinuities = new boolean[trackCount];
            this.pendingMediaFormat = new boolean[trackCount];
            this.mediaFormats = new MediaFormat[trackCount];
            this.maxTrackDurationUs = -1L;
            for (int i = 0; i < trackCount; i++) {
                MediaFormat format = this.sampleQueues.valueAt(i).getFormat();
                this.mediaFormats[i] = format;
                if (format.durationUs != -1 && format.durationUs > this.maxTrackDurationUs) {
                    this.maxTrackDurationUs = format.durationUs;
                }
            }
            this.prepared = true;
            return true;
        }
        return false;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public int getTrackCount() {
        return this.sampleQueues.size();
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public MediaFormat getFormat(int track) {
        Assertions.checkState(this.prepared);
        return this.mediaFormats[track];
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void enable(int track, long positionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(!this.trackEnabledStates[track]);
        this.enabledTrackCount++;
        this.trackEnabledStates[track] = true;
        this.pendingMediaFormat[track] = true;
        this.pendingDiscontinuities[track] = false;
        if (this.enabledTrackCount == 1) {
            if (!this.seekMap.isSeekable()) {
                positionUs = 0;
            }
            this.downstreamPositionUs = positionUs;
            this.lastSeekPositionUs = positionUs;
            restartFrom(positionUs);
        }
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void disable(int track) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[track]);
        this.enabledTrackCount--;
        this.trackEnabledStates[track] = false;
        if (this.enabledTrackCount == 0) {
            this.downstreamPositionUs = Long.MIN_VALUE;
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
            } else {
                clearState();
                this.allocator.trim(0);
            }
        }
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public boolean continueBuffering(int track, long playbackPositionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[track]);
        this.downstreamPositionUs = playbackPositionUs;
        discardSamplesForDisabledTracks(this.downstreamPositionUs);
        if (this.loadingFinished) {
            return true;
        }
        maybeStartLoading();
        if (isPendingReset()) {
            return false;
        }
        return !this.sampleQueues.valueAt(track).isEmpty();
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
    public int readData(int track, long playbackPositionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder) {
        this.downstreamPositionUs = playbackPositionUs;
        if (this.pendingDiscontinuities[track] || isPendingReset()) {
            return -2;
        }
        InternalTrackOutput sampleQueue = this.sampleQueues.valueAt(track);
        if (this.pendingMediaFormat[track]) {
            formatHolder.format = sampleQueue.getFormat();
            formatHolder.drmInitData = this.drmInitData;
            this.pendingMediaFormat[track] = false;
            return -4;
        }
        if (!sampleQueue.getSample(sampleHolder)) {
            return this.loadingFinished ? -1 : -2;
        }
        boolean decodeOnly = sampleHolder.timeUs < this.lastSeekPositionUs;
        sampleHolder.flags = (decodeOnly ? 134217728 : 0) | sampleHolder.flags;
        if (this.havePendingNextSampleUs) {
            this.sampleTimeOffsetUs = this.pendingNextSampleUs - sampleHolder.timeUs;
            this.havePendingNextSampleUs = false;
        }
        sampleHolder.timeUs += this.sampleTimeOffsetUs;
        return -3;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void maybeThrowError() throws IOException {
        int minLoadableRetryCountForMedia;
        if (this.currentLoadableException != null) {
            if (isCurrentLoadableExceptionFatal()) {
                throw this.currentLoadableException;
            }
            if (this.minLoadableRetryCount != -1) {
                minLoadableRetryCountForMedia = this.minLoadableRetryCount;
            } else {
                minLoadableRetryCountForMedia = (this.seekMap == null || this.seekMap.isSeekable()) ? 3 : 6;
            }
            if (this.currentLoadableExceptionCount > minLoadableRetryCountForMedia) {
                throw this.currentLoadableException;
            }
        }
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void seekToUs(long positionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.enabledTrackCount > 0);
        if (!this.seekMap.isSeekable()) {
            positionUs = 0;
        }
        long currentPositionUs = isPendingReset() ? this.pendingResetPositionUs : this.downstreamPositionUs;
        this.downstreamPositionUs = positionUs;
        this.lastSeekPositionUs = positionUs;
        if (currentPositionUs != positionUs) {
            boolean seekInsideBuffer = !isPendingReset();
            for (int i = 0; seekInsideBuffer && i < this.sampleQueues.size(); i++) {
                seekInsideBuffer &= this.sampleQueues.valueAt(i).skipToKeyframeBefore(positionUs);
            }
            if (!seekInsideBuffer) {
                restartFrom(positionUs);
            }
            for (int i2 = 0; i2 < this.pendingDiscontinuities.length; i2++) {
                this.pendingDiscontinuities[i2] = true;
            }
        }
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public long getBufferedPositionUs() {
        if (this.loadingFinished) {
            return -3L;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long largestParsedTimestampUs = Long.MIN_VALUE;
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            largestParsedTimestampUs = Math.max(largestParsedTimestampUs, this.sampleQueues.valueAt(i).getLargestParsedTimestampUs());
        }
        if (largestParsedTimestampUs == Long.MIN_VALUE) {
            long largestParsedTimestampUs2 = this.downstreamPositionUs;
            return largestParsedTimestampUs2;
        }
        return largestParsedTimestampUs;
    }

    @Override // com.google.android.exoplayer.SampleSource.SampleSourceReader
    public void release() {
        Assertions.checkState(this.remainingReleaseCount > 0);
        int i = this.remainingReleaseCount - 1;
        this.remainingReleaseCount = i;
        if (i == 0 && this.loader != null) {
            this.loader.release();
            this.loader = null;
        }
    }

    @Override // com.google.android.exoplayer.upstream.Loader.Callback
    public void onLoadCompleted(Loader.Loadable loadable) {
        this.loadingFinished = true;
    }

    @Override // com.google.android.exoplayer.upstream.Loader.Callback
    public void onLoadCanceled(Loader.Loadable loadable) {
        if (this.enabledTrackCount > 0) {
            restartFrom(this.pendingResetPositionUs);
        } else {
            clearState();
            this.allocator.trim(0);
        }
    }

    @Override // com.google.android.exoplayer.upstream.Loader.Callback
    public void onLoadError(Loader.Loadable ignored, IOException e) {
        this.currentLoadableException = e;
        this.currentLoadableExceptionCount = this.extractedSampleCount > this.extractedSampleCountAtStartOfLoad ? 1 : this.currentLoadableExceptionCount + 1;
        this.currentLoadableExceptionTimestamp = SystemClock.elapsedRealtime();
        maybeStartLoading();
    }

    @Override // com.google.android.exoplayer.extractor.ExtractorOutput
    public TrackOutput track(int id) {
        InternalTrackOutput sampleQueue = this.sampleQueues.get(id);
        if (sampleQueue == null) {
            InternalTrackOutput sampleQueue2 = new InternalTrackOutput(this.allocator);
            this.sampleQueues.put(id, sampleQueue2);
            return sampleQueue2;
        }
        return sampleQueue;
    }

    @Override // com.google.android.exoplayer.extractor.ExtractorOutput
    public void endTracks() {
        this.tracksBuilt = true;
    }

    @Override // com.google.android.exoplayer.extractor.ExtractorOutput
    public void seekMap(SeekMap seekMap) {
        this.seekMap = seekMap;
    }

    @Override // com.google.android.exoplayer.extractor.ExtractorOutput
    public void drmInitData(DrmInitData drmInitData) {
        this.drmInitData = drmInitData;
    }

    private void restartFrom(long positionUs) {
        this.pendingResetPositionUs = positionUs;
        this.loadingFinished = false;
        if (this.loader.isLoading()) {
            this.loader.cancelLoading();
        } else {
            clearState();
            maybeStartLoading();
        }
    }

    private void maybeStartLoading() {
        if (!this.loadingFinished && !this.loader.isLoading()) {
            if (this.currentLoadableException != null) {
                if (!isCurrentLoadableExceptionFatal()) {
                    Assertions.checkState(this.loadable != null);
                    long elapsedMillis = SystemClock.elapsedRealtime() - this.currentLoadableExceptionTimestamp;
                    if (elapsedMillis >= getRetryDelayMillis(this.currentLoadableExceptionCount)) {
                        this.currentLoadableException = null;
                        if (!this.prepared) {
                            for (int i = 0; i < this.sampleQueues.size(); i++) {
                                this.sampleQueues.valueAt(i).clear();
                            }
                            this.loadable = createLoadableFromStart();
                        } else if (!this.seekMap.isSeekable() && this.maxTrackDurationUs == -1) {
                            for (int i2 = 0; i2 < this.sampleQueues.size(); i2++) {
                                this.sampleQueues.valueAt(i2).clear();
                            }
                            this.loadable = createLoadableFromStart();
                            this.pendingNextSampleUs = this.downstreamPositionUs;
                            this.havePendingNextSampleUs = true;
                        }
                        this.extractedSampleCountAtStartOfLoad = this.extractedSampleCount;
                        this.loader.startLoading(this.loadable, this);
                        return;
                    }
                    return;
                }
                return;
            }
            this.sampleTimeOffsetUs = 0L;
            this.havePendingNextSampleUs = false;
            if (!this.prepared) {
                this.loadable = createLoadableFromStart();
            } else {
                Assertions.checkState(isPendingReset());
                if (this.maxTrackDurationUs != -1 && this.pendingResetPositionUs >= this.maxTrackDurationUs) {
                    this.loadingFinished = true;
                    this.pendingResetPositionUs = Long.MIN_VALUE;
                    return;
                } else {
                    this.loadable = createLoadableFromPositionUs(this.pendingResetPositionUs);
                    this.pendingResetPositionUs = Long.MIN_VALUE;
                }
            }
            this.extractedSampleCountAtStartOfLoad = this.extractedSampleCount;
            this.loader.startLoading(this.loadable, this);
        }
    }

    private ExtractingLoadable createLoadableFromStart() {
        return new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this.allocator, this.requestedBufferSize, 0L);
    }

    private ExtractingLoadable createLoadableFromPositionUs(long positionUs) {
        return new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this.allocator, this.requestedBufferSize, this.seekMap.getPosition(positionUs));
    }

    private boolean haveFormatsForAllTracks() {
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            if (!this.sampleQueues.valueAt(i).hasFormat()) {
                return false;
            }
        }
        return true;
    }

    private void discardSamplesForDisabledTracks(long timeUs) {
        for (int i = 0; i < this.trackEnabledStates.length; i++) {
            if (!this.trackEnabledStates[i]) {
                this.sampleQueues.valueAt(i).discardUntil(timeUs);
            }
        }
    }

    private void clearState() {
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            this.sampleQueues.valueAt(i).clear();
        }
        this.loadable = null;
        this.currentLoadableException = null;
        this.currentLoadableExceptionCount = 0;
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != Long.MIN_VALUE;
    }

    private boolean isCurrentLoadableExceptionFatal() {
        return this.currentLoadableException instanceof UnrecognizedInputFormatException;
    }

    private long getRetryDelayMillis(long errorCount) {
        return Math.min((errorCount - 1) * 1000, 5000L);
    }

    private class InternalTrackOutput extends DefaultTrackOutput {
        public InternalTrackOutput(Allocator allocator) {
            super(allocator);
        }

        @Override // com.google.android.exoplayer.extractor.DefaultTrackOutput, com.google.android.exoplayer.extractor.TrackOutput
        public void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
            super.sampleMetadata(timeUs, flags, size, offset, encryptionKey);
            ExtractorSampleSource.access$008(ExtractorSampleSource.this);
        }
    }

    private static class ExtractingLoadable implements Loader.Loadable {
        private final Allocator allocator;
        private final DataSource dataSource;
        private final ExtractorHolder extractorHolder;
        private volatile boolean loadCanceled;
        private boolean pendingExtractorSeek;
        private final PositionHolder positionHolder = new PositionHolder();
        private final int requestedBufferSize;
        private final Uri uri;

        public ExtractingLoadable(Uri uri, DataSource dataSource, ExtractorHolder extractorHolder, Allocator allocator, int requestedBufferSize, long position) {
            this.uri = (Uri) Assertions.checkNotNull(uri);
            this.dataSource = (DataSource) Assertions.checkNotNull(dataSource);
            this.extractorHolder = (ExtractorHolder) Assertions.checkNotNull(extractorHolder);
            this.allocator = (Allocator) Assertions.checkNotNull(allocator);
            this.requestedBufferSize = requestedBufferSize;
            this.positionHolder.position = position;
            this.pendingExtractorSeek = true;
        }

        @Override // com.google.android.exoplayer.upstream.Loader.Loadable
        public void cancelLoad() {
            this.loadCanceled = true;
        }

        @Override // com.google.android.exoplayer.upstream.Loader.Loadable
        public boolean isLoadCanceled() {
            return this.loadCanceled;
        }

        @Override // com.google.android.exoplayer.upstream.Loader.Loadable
        public void load() throws Throwable {
            ExtractorInput input;
            int result = 0;
            while (result == 0 && !this.loadCanceled) {
                try {
                    long position = this.positionHolder.position;
                    long length = this.dataSource.open(new DataSpec(this.uri, position, -1L, null));
                    if (length != -1) {
                        length += position;
                    }
                    input = new DefaultExtractorInput(this.dataSource, position, length);
                    try {
                        Extractor extractor = this.extractorHolder.selectExtractor(input);
                        if (this.pendingExtractorSeek) {
                            extractor.seek();
                            this.pendingExtractorSeek = false;
                        }
                        while (result == 0 && !this.loadCanceled) {
                            this.allocator.blockWhileTotalBytesAllocatedExceeds(this.requestedBufferSize);
                            result = extractor.read(input, this.positionHolder);
                        }
                        if (result == 1) {
                            result = 0;
                        } else if (input != null) {
                            this.positionHolder.position = input.getPosition();
                        }
                        this.dataSource.close();
                    } catch (Throwable th) {
                        th = th;
                        if (result != 1 && input != null) {
                            this.positionHolder.position = input.getPosition();
                        }
                        this.dataSource.close();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    input = null;
                }
            }
        }
    }

    private static final class ExtractorHolder {
        private Extractor extractor;
        private final ExtractorOutput extractorOutput;
        private final Extractor[] extractors;

        public ExtractorHolder(Extractor[] extractors, ExtractorOutput extractorOutput) {
            this.extractors = extractors;
            this.extractorOutput = extractorOutput;
        }

        public Extractor selectExtractor(ExtractorInput input) throws InterruptedException, IOException {
            if (this.extractor != null) {
                return this.extractor;
            }
            Extractor[] arr$ = this.extractors;
            for (Extractor extractor : arr$) {
                if (extractor.sniff(input)) {
                    this.extractor = extractor;
                    break;
                }
                continue;
                input.resetPeekPosition();
            }
            if (this.extractor == null) {
                throw new UnrecognizedInputFormatException(this.extractors);
            }
            this.extractor.init(this.extractorOutput);
            return this.extractor;
        }
    }
}
