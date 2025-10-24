package com.google.android.exoplayer.hls;

import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.exoplayer.BehindLiveWindowException;
import com.google.android.exoplayer.chunk.Chunk;
import com.google.android.exoplayer.chunk.ChunkOperationHolder;
import com.google.android.exoplayer.chunk.DataChunk;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import com.google.android.exoplayer.extractor.ts.TsExtractor;
import com.google.android.exoplayer.hls.HlsMediaPlaylist;
import com.google.android.exoplayer.hls.HlsTrackSelector;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.UriUtil;
import com.google.android.exoplayer.util.Util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class HlsChunkSource implements HlsTrackSelector.Output {
    private final int adaptiveMode;
    private final BandwidthMeter bandwidthMeter;
    private final String baseUri;
    private final DataSource dataSource;
    private long durationUs;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private IOException fatalError;
    private final boolean isMaster;
    private boolean live;
    private final HlsMasterPlaylist masterPlaylist;
    private final long maxBufferDurationToSwitchDownUs;
    private final long minBufferDurationToSwitchUpUs;
    private final HlsPlaylistParser playlistParser;
    private boolean prepareCalled;
    private byte[] scratchSpace;
    private int selectedTrackIndex;
    private int selectedVariantIndex;
    private final PtsTimestampAdjusterProvider timestampAdjusterProvider;
    private final HlsTrackSelector trackSelector;
    private final ArrayList<ExposedTrack> tracks;
    private long[] variantBlacklistTimes;
    private long[] variantLastPlaylistLoadTimesMs;
    private HlsMediaPlaylist[] variantPlaylists;
    private Variant[] variants;

    public HlsChunkSource(boolean isMaster, DataSource dataSource, String playlistUrl, HlsPlaylist playlist, HlsTrackSelector trackSelector, BandwidthMeter bandwidthMeter, PtsTimestampAdjusterProvider timestampAdjusterProvider, int adaptiveMode) {
        this(isMaster, dataSource, playlistUrl, playlist, trackSelector, bandwidthMeter, timestampAdjusterProvider, adaptiveMode, 5000L, 20000L);
    }

    public HlsChunkSource(boolean isMaster, DataSource dataSource, String playlistUrl, HlsPlaylist playlist, HlsTrackSelector trackSelector, BandwidthMeter bandwidthMeter, PtsTimestampAdjusterProvider timestampAdjusterProvider, int adaptiveMode, long minBufferDurationToSwitchUpMs, long maxBufferDurationToSwitchDownMs) {
        this.isMaster = isMaster;
        this.dataSource = dataSource;
        this.trackSelector = trackSelector;
        this.bandwidthMeter = bandwidthMeter;
        this.timestampAdjusterProvider = timestampAdjusterProvider;
        this.adaptiveMode = adaptiveMode;
        this.minBufferDurationToSwitchUpUs = 1000 * minBufferDurationToSwitchUpMs;
        this.maxBufferDurationToSwitchDownUs = 1000 * maxBufferDurationToSwitchDownMs;
        this.baseUri = playlist.baseUri;
        this.playlistParser = new HlsPlaylistParser();
        this.tracks = new ArrayList<>();
        if (playlist.type == 0) {
            this.masterPlaylist = (HlsMasterPlaylist) playlist;
            return;
        }
        Format format = new Format("0", "application/x-mpegURL", -1, -1, -1.0f, -1, -1, -1, null, null);
        List<Variant> variants = new ArrayList<>();
        variants.add(new Variant(playlistUrl, format));
        this.masterPlaylist = new HlsMasterPlaylist(playlistUrl, variants, Collections.emptyList());
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        }
    }

    public boolean prepare() {
        if (!this.prepareCalled) {
            this.prepareCalled = true;
            try {
                this.trackSelector.selectTracks(this.masterPlaylist, this);
                selectTrack(0);
            } catch (IOException e) {
                this.fatalError = e;
            }
        }
        return this.fatalError == null;
    }

    public boolean isLive() {
        return this.live;
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public int getTrackCount() {
        return this.tracks.size();
    }

    public Variant getFixedTrackVariant(int index) {
        Variant[] variants = this.tracks.get(index).variants;
        if (variants.length == 1) {
            return variants[0];
        }
        return null;
    }

    public int getSelectedTrackIndex() {
        return this.selectedTrackIndex;
    }

    public void selectTrack(int index) {
        this.selectedTrackIndex = index;
        ExposedTrack selectedTrack = this.tracks.get(this.selectedTrackIndex);
        this.selectedVariantIndex = selectedTrack.defaultVariantIndex;
        this.variants = selectedTrack.variants;
        this.variantPlaylists = new HlsMediaPlaylist[this.variants.length];
        this.variantLastPlaylistLoadTimesMs = new long[this.variants.length];
        this.variantBlacklistTimes = new long[this.variants.length];
    }

    public void seek() {
        if (this.isMaster) {
            this.timestampAdjusterProvider.reset();
        }
    }

    public void reset() {
        this.fatalError = null;
    }

    public void getChunkOperation(TsChunk previousTsChunk, long playbackPositionUs, ChunkOperationHolder out) {
        int nextVariantIndex;
        boolean switchingVariantSpliced;
        int chunkMediaSequence;
        long startTimeUs;
        HlsExtractorWrapper extractorWrapper;
        if (this.adaptiveMode == 0) {
            nextVariantIndex = this.selectedVariantIndex;
            switchingVariantSpliced = false;
        } else {
            nextVariantIndex = getNextVariantIndex(previousTsChunk, playbackPositionUs);
            switchingVariantSpliced = (previousTsChunk == null || this.variants[nextVariantIndex].format.equals(previousTsChunk.format) || this.adaptiveMode != 1) ? false : true;
        }
        HlsMediaPlaylist mediaPlaylist = this.variantPlaylists[nextVariantIndex];
        if (mediaPlaylist == null) {
            out.chunk = newMediaPlaylistChunk(nextVariantIndex);
            return;
        }
        this.selectedVariantIndex = nextVariantIndex;
        if (this.live) {
            if (previousTsChunk == null) {
                chunkMediaSequence = getLiveStartChunkMediaSequence(nextVariantIndex);
            } else {
                chunkMediaSequence = switchingVariantSpliced ? previousTsChunk.chunkIndex : previousTsChunk.chunkIndex + 1;
                if (chunkMediaSequence < mediaPlaylist.mediaSequence) {
                    this.fatalError = new BehindLiveWindowException();
                    return;
                }
            }
        } else if (previousTsChunk == null) {
            chunkMediaSequence = Util.binarySearchFloor((List<? extends Comparable<? super Long>>) mediaPlaylist.segments, Long.valueOf(playbackPositionUs), true, true) + mediaPlaylist.mediaSequence;
        } else {
            chunkMediaSequence = switchingVariantSpliced ? previousTsChunk.chunkIndex : previousTsChunk.chunkIndex + 1;
        }
        int chunkIndex = chunkMediaSequence - mediaPlaylist.mediaSequence;
        if (chunkIndex >= mediaPlaylist.segments.size()) {
            if (!mediaPlaylist.live) {
                out.endOfStream = true;
                return;
            } else {
                if (shouldRerequestLiveMediaPlaylist(nextVariantIndex)) {
                    out.chunk = newMediaPlaylistChunk(nextVariantIndex);
                    return;
                }
                return;
            }
        }
        HlsMediaPlaylist.Segment segment = mediaPlaylist.segments.get(chunkIndex);
        Uri chunkUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.url);
        if (segment.isEncrypted) {
            Uri keyUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.encryptionKeyUri);
            if (!keyUri.equals(this.encryptionKeyUri)) {
                out.chunk = newEncryptionKeyChunk(keyUri, segment.encryptionIV, this.selectedVariantIndex);
                return;
            } else if (!Util.areEqual(segment.encryptionIV, this.encryptionIvString)) {
                setEncryptionData(keyUri, segment.encryptionIV, this.encryptionKey);
            }
        } else {
            clearEncryptionData();
        }
        DataSpec dataSpec = new DataSpec(chunkUri, segment.byterangeOffset, segment.byterangeLength, null);
        if (this.live) {
            if (previousTsChunk == null) {
                startTimeUs = 0;
            } else if (switchingVariantSpliced) {
                startTimeUs = previousTsChunk.startTimeUs;
            } else {
                startTimeUs = previousTsChunk.endTimeUs;
            }
        } else {
            startTimeUs = segment.startTimeUs;
        }
        long endTimeUs = startTimeUs + ((long) (segment.durationSecs * 1000000.0d));
        Format format = this.variants[this.selectedVariantIndex].format;
        String lastPathSegment = chunkUri.getLastPathSegment();
        if (lastPathSegment.endsWith(".aac")) {
            Extractor extractor = new AdtsExtractor(startTimeUs);
            extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, extractor, switchingVariantSpliced, -1, -1);
        } else if (lastPathSegment.endsWith(".mp3")) {
            Extractor extractor2 = new Mp3Extractor(startTimeUs);
            extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, extractor2, switchingVariantSpliced, -1, -1);
        } else if (lastPathSegment.endsWith(".webvtt") || lastPathSegment.endsWith(".vtt")) {
            PtsTimestampAdjuster timestampAdjuster = this.timestampAdjusterProvider.getAdjuster(this.isMaster, segment.discontinuitySequenceNumber, startTimeUs);
            if (timestampAdjuster != null) {
                Extractor extractor3 = new WebvttExtractor(timestampAdjuster);
                extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, extractor3, switchingVariantSpliced, -1, -1);
            } else {
                return;
            }
        } else if (previousTsChunk == null || previousTsChunk.discontinuitySequenceNumber != segment.discontinuitySequenceNumber || !format.equals(previousTsChunk.format)) {
            PtsTimestampAdjuster timestampAdjuster2 = this.timestampAdjusterProvider.getAdjuster(this.isMaster, segment.discontinuitySequenceNumber, startTimeUs);
            if (timestampAdjuster2 != null) {
                ExposedTrack selectedTrack = this.tracks.get(this.selectedTrackIndex);
                Extractor extractor4 = new TsExtractor(timestampAdjuster2);
                extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, extractor4, switchingVariantSpliced, selectedTrack.adaptiveMaxWidth, selectedTrack.adaptiveMaxHeight);
            } else {
                return;
            }
        } else {
            extractorWrapper = previousTsChunk.extractorWrapper;
        }
        out.chunk = new TsChunk(this.dataSource, dataSpec, 0, format, startTimeUs, endTimeUs, chunkMediaSequence, segment.discontinuitySequenceNumber, extractorWrapper, this.encryptionKey, this.encryptionIv);
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof MediaPlaylistChunk) {
            MediaPlaylistChunk mediaPlaylistChunk = (MediaPlaylistChunk) chunk;
            this.scratchSpace = mediaPlaylistChunk.getDataHolder();
            setMediaPlaylist(mediaPlaylistChunk.variantIndex, mediaPlaylistChunk.getResult());
        } else if (chunk instanceof EncryptionKeyChunk) {
            EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk) chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.iv, encryptionKeyChunk.getResult());
        }
    }

    public boolean onChunkLoadError(Chunk chunk, IOException e) {
        int variantIndex;
        if (chunk.bytesLoaded() == 0 && (((chunk instanceof TsChunk) || (chunk instanceof MediaPlaylistChunk) || (chunk instanceof EncryptionKeyChunk)) && (e instanceof HttpDataSource.InvalidResponseCodeException))) {
            HttpDataSource.InvalidResponseCodeException responseCodeException = (HttpDataSource.InvalidResponseCodeException) e;
            int responseCode = responseCodeException.responseCode;
            if (responseCode == 404 || responseCode == 410) {
                if (chunk instanceof TsChunk) {
                    TsChunk tsChunk = (TsChunk) chunk;
                    variantIndex = getVariantIndex(tsChunk.format);
                } else if (chunk instanceof MediaPlaylistChunk) {
                    MediaPlaylistChunk playlistChunk = (MediaPlaylistChunk) chunk;
                    variantIndex = playlistChunk.variantIndex;
                } else {
                    EncryptionKeyChunk encryptionChunk = (EncryptionKeyChunk) chunk;
                    variantIndex = encryptionChunk.variantIndex;
                }
                boolean alreadyBlacklisted = this.variantBlacklistTimes[variantIndex] != 0;
                this.variantBlacklistTimes[variantIndex] = SystemClock.elapsedRealtime();
                if (alreadyBlacklisted) {
                    Log.w("HlsChunkSource", "Already blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
                    return false;
                }
                if (!allVariantsBlacklisted()) {
                    Log.w("HlsChunkSource", "Blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
                    return true;
                }
                Log.w("HlsChunkSource", "Final variant not blacklisted (" + responseCode + "): " + chunk.dataSpec.uri);
                this.variantBlacklistTimes[variantIndex] = 0;
                return false;
            }
        }
        return false;
    }

    @Override // com.google.android.exoplayer.hls.HlsTrackSelector.Output
    public void adaptiveTrack(HlsMasterPlaylist playlist, Variant[] variants) {
        Arrays.sort(variants, new Comparator<Variant>() { // from class: com.google.android.exoplayer.hls.HlsChunkSource.1
            private final Comparator<Format> formatComparator = new Format.DecreasingBandwidthComparator();

            @Override // java.util.Comparator
            public int compare(Variant first, Variant second) {
                return this.formatComparator.compare(first.format, second.format);
            }
        });
        int defaultVariantIndex = 0;
        int maxWidth = -1;
        int maxHeight = -1;
        int minOriginalVariantIndex = Integer.MAX_VALUE;
        for (int i = 0; i < variants.length; i++) {
            int originalVariantIndex = playlist.variants.indexOf(variants[i]);
            if (originalVariantIndex < minOriginalVariantIndex) {
                minOriginalVariantIndex = originalVariantIndex;
                defaultVariantIndex = i;
            }
            Format variantFormat = variants[i].format;
            maxWidth = Math.max(variantFormat.width, maxWidth);
            maxHeight = Math.max(variantFormat.height, maxHeight);
        }
        if (maxWidth <= 0) {
            maxWidth = 1920;
        }
        if (maxHeight <= 0) {
            maxHeight = 1080;
        }
        this.tracks.add(new ExposedTrack(variants, defaultVariantIndex, maxWidth, maxHeight));
    }

    @Override // com.google.android.exoplayer.hls.HlsTrackSelector.Output
    public void fixedTrack(HlsMasterPlaylist playlist, Variant variant) {
        this.tracks.add(new ExposedTrack(variant));
    }

    private int getNextVariantIndex(TsChunk previousTsChunk, long playbackPositionUs) {
        clearStaleBlacklistedVariants();
        long bitrateEstimate = this.bandwidthMeter.getBitrateEstimate();
        if (this.variantBlacklistTimes[this.selectedVariantIndex] != 0) {
            return getVariantIndexForBandwidth(bitrateEstimate);
        }
        if (previousTsChunk == null) {
            return this.selectedVariantIndex;
        }
        if (bitrateEstimate == -1) {
            return this.selectedVariantIndex;
        }
        int idealIndex = getVariantIndexForBandwidth(bitrateEstimate);
        if (idealIndex == this.selectedVariantIndex) {
            return this.selectedVariantIndex;
        }
        long bufferedPositionUs = this.adaptiveMode == 1 ? previousTsChunk.startTimeUs : previousTsChunk.endTimeUs;
        long bufferedUs = bufferedPositionUs - playbackPositionUs;
        if (this.variantBlacklistTimes[this.selectedVariantIndex] != 0) {
            return idealIndex;
        }
        if (idealIndex <= this.selectedVariantIndex || bufferedUs >= this.maxBufferDurationToSwitchDownUs) {
            return (idealIndex >= this.selectedVariantIndex || bufferedUs <= this.minBufferDurationToSwitchUpUs) ? this.selectedVariantIndex : idealIndex;
        }
        return idealIndex;
    }

    private int getVariantIndexForBandwidth(long bitrateEstimate) {
        if (bitrateEstimate == -1) {
            bitrateEstimate = 0;
        }
        int effectiveBitrate = (int) (bitrateEstimate * 0.8f);
        int lowestQualityEnabledVariantIndex = -1;
        for (int i = 0; i < this.variants.length; i++) {
            if (this.variantBlacklistTimes[i] == 0) {
                if (this.variants[i].format.bitrate > effectiveBitrate) {
                    lowestQualityEnabledVariantIndex = i;
                } else {
                    return i;
                }
            }
        }
        Assertions.checkState(lowestQualityEnabledVariantIndex != -1);
        int i2 = lowestQualityEnabledVariantIndex;
        return i2;
    }

    private boolean shouldRerequestLiveMediaPlaylist(int nextVariantIndex) {
        HlsMediaPlaylist mediaPlaylist = this.variantPlaylists[nextVariantIndex];
        long timeSinceLastMediaPlaylistLoadMs = SystemClock.elapsedRealtime() - this.variantLastPlaylistLoadTimesMs[nextVariantIndex];
        return timeSinceLastMediaPlaylistLoadMs >= ((long) ((mediaPlaylist.targetDurationSecs * 1000) / 2));
    }

    private int getLiveStartChunkMediaSequence(int variantIndex) {
        HlsMediaPlaylist mediaPlaylist = this.variantPlaylists[variantIndex];
        int chunkIndex = mediaPlaylist.segments.size() > 3 ? mediaPlaylist.segments.size() - 3 : 0;
        return mediaPlaylist.mediaSequence + chunkIndex;
    }

    private MediaPlaylistChunk newMediaPlaylistChunk(int variantIndex) {
        Uri mediaPlaylistUri = UriUtil.resolveToUri(this.baseUri, this.variants[variantIndex].url);
        DataSpec dataSpec = new DataSpec(mediaPlaylistUri, 0L, -1L, null, 1);
        return new MediaPlaylistChunk(this.dataSource, dataSpec, this.scratchSpace, this.playlistParser, variantIndex, mediaPlaylistUri.toString());
    }

    private EncryptionKeyChunk newEncryptionKeyChunk(Uri keyUri, String iv, int variantIndex) {
        DataSpec dataSpec = new DataSpec(keyUri, 0L, -1L, null, 1);
        return new EncryptionKeyChunk(this.dataSource, dataSpec, this.scratchSpace, iv, variantIndex);
    }

    private void setEncryptionData(Uri keyUri, String iv, byte[] secretKey) {
        String trimmedIv;
        if (iv.toLowerCase(Locale.getDefault()).startsWith("0x")) {
            trimmedIv = iv.substring(2);
        } else {
            trimmedIv = iv;
        }
        byte[] ivData = new BigInteger(trimmedIv, 16).toByteArray();
        byte[] ivDataWithPadding = new byte[16];
        int offset = ivData.length > 16 ? ivData.length - 16 : 0;
        System.arraycopy(ivData, offset, ivDataWithPadding, (ivDataWithPadding.length - ivData.length) + offset, ivData.length - offset);
        this.encryptionKeyUri = keyUri;
        this.encryptionKey = secretKey;
        this.encryptionIvString = iv;
        this.encryptionIv = ivDataWithPadding;
    }

    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }

    private void setMediaPlaylist(int variantIndex, HlsMediaPlaylist mediaPlaylist) {
        this.variantLastPlaylistLoadTimesMs[variantIndex] = SystemClock.elapsedRealtime();
        this.variantPlaylists[variantIndex] = mediaPlaylist;
        this.live |= mediaPlaylist.live;
        this.durationUs = this.live ? -1L : mediaPlaylist.durationUs;
    }

    private boolean allVariantsBlacklisted() {
        for (int i = 0; i < this.variantBlacklistTimes.length; i++) {
            if (this.variantBlacklistTimes[i] == 0) {
                return false;
            }
        }
        return true;
    }

    private void clearStaleBlacklistedVariants() {
        long currentTime = SystemClock.elapsedRealtime();
        for (int i = 0; i < this.variantBlacklistTimes.length; i++) {
            if (this.variantBlacklistTimes[i] != 0 && currentTime - this.variantBlacklistTimes[i] > 60000) {
                this.variantBlacklistTimes[i] = 0;
            }
        }
    }

    private int getVariantIndex(Format format) {
        for (int i = 0; i < this.variants.length; i++) {
            if (this.variants[i].format.equals(format)) {
                return i;
            }
        }
        throw new IllegalStateException("Invalid format: " + format);
    }

    private static final class ExposedTrack {
        private final int adaptiveMaxHeight;
        private final int adaptiveMaxWidth;
        private final int defaultVariantIndex;
        private final Variant[] variants;

        public ExposedTrack(Variant fixedVariant) {
            this.variants = new Variant[]{fixedVariant};
            this.defaultVariantIndex = 0;
            this.adaptiveMaxWidth = -1;
            this.adaptiveMaxHeight = -1;
        }

        public ExposedTrack(Variant[] adaptiveVariants, int defaultVariantIndex, int maxWidth, int maxHeight) {
            this.variants = adaptiveVariants;
            this.defaultVariantIndex = defaultVariantIndex;
            this.adaptiveMaxWidth = maxWidth;
            this.adaptiveMaxHeight = maxHeight;
        }
    }

    private static final class MediaPlaylistChunk extends DataChunk {
        private final HlsPlaylistParser playlistParser;
        private final String playlistUrl;
        private HlsMediaPlaylist result;
        public final int variantIndex;

        public MediaPlaylistChunk(DataSource dataSource, DataSpec dataSpec, byte[] scratchSpace, HlsPlaylistParser playlistParser, int variantIndex, String playlistUrl) {
            super(dataSource, dataSpec, 4, 0, null, -1, scratchSpace);
            this.variantIndex = variantIndex;
            this.playlistParser = playlistParser;
            this.playlistUrl = playlistUrl;
        }

        @Override // com.google.android.exoplayer.chunk.DataChunk
        protected void consume(byte[] data, int limit) throws IOException {
            this.result = (HlsMediaPlaylist) this.playlistParser.parse(this.playlistUrl, (InputStream) new ByteArrayInputStream(data, 0, limit));
        }

        public HlsMediaPlaylist getResult() {
            return this.result;
        }
    }

    private static final class EncryptionKeyChunk extends DataChunk {
        public final String iv;
        private byte[] result;
        public final int variantIndex;

        public EncryptionKeyChunk(DataSource dataSource, DataSpec dataSpec, byte[] scratchSpace, String iv, int variantIndex) {
            super(dataSource, dataSpec, 3, 0, null, -1, scratchSpace);
            this.iv = iv;
            this.variantIndex = variantIndex;
        }

        @Override // com.google.android.exoplayer.chunk.DataChunk
        protected void consume(byte[] data, int limit) throws IOException {
            this.result = Arrays.copyOf(data, limit);
        }

        public byte[] getResult() {
            return this.result;
        }
    }
}
