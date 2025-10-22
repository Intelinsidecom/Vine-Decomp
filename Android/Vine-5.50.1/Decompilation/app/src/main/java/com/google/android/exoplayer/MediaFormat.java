package com.google.android.exoplayer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class MediaFormat {
    public final boolean adaptive;
    public final int bitrate;
    public final int channelCount;
    public final long durationUs;
    private android.media.MediaFormat frameworkMediaFormat;
    private int hashCode;
    public final int height;
    public final List<byte[]> initializationData;
    public final String language;
    public final int maxHeight;
    public final int maxInputSize;
    public final int maxWidth;
    public final String mimeType;
    public final float pixelWidthHeightRatio;
    public final int rotationDegrees;
    public final int sampleRate;
    public final long subsampleOffsetUs;
    public final String trackId;
    public final int width;

    public static MediaFormat createVideoFormat(String trackId, String mimeType, int bitrate, int maxInputSize, long durationUs, int width, int height, List<byte[]> initializationData) {
        return createVideoFormat(trackId, mimeType, bitrate, maxInputSize, durationUs, width, height, initializationData, -1, -1.0f);
    }

    public static MediaFormat createVideoFormat(String trackId, String mimeType, int bitrate, int maxInputSize, long durationUs, int width, int height, List<byte[]> initializationData, int rotationDegrees, float pixelWidthHeightRatio) {
        return new MediaFormat(trackId, mimeType, bitrate, maxInputSize, durationUs, width, height, rotationDegrees, pixelWidthHeightRatio, -1, -1, null, Long.MAX_VALUE, initializationData, false, -1, -1);
    }

    public static MediaFormat createAudioFormat(String trackId, String mimeType, int bitrate, int maxInputSize, long durationUs, int channelCount, int sampleRate, List<byte[]> initializationData, String language) {
        return new MediaFormat(trackId, mimeType, bitrate, maxInputSize, durationUs, -1, -1, -1, -1.0f, channelCount, sampleRate, language, Long.MAX_VALUE, initializationData, false, -1, -1);
    }

    public static MediaFormat createTextFormat(String trackId, String mimeType, int bitrate, long durationUs, String language) {
        return createTextFormat(trackId, mimeType, bitrate, durationUs, language, Long.MAX_VALUE);
    }

    public static MediaFormat createTextFormat(String trackId, String mimeType, int bitrate, long durationUs, String language, long subsampleOffsetUs) {
        return new MediaFormat(trackId, mimeType, bitrate, -1, durationUs, -1, -1, -1, -1.0f, -1, -1, language, subsampleOffsetUs, null, false, -1, -1);
    }

    public static MediaFormat createFormatForMimeType(String trackId, String mimeType, int bitrate, long durationUs) {
        return new MediaFormat(trackId, mimeType, bitrate, -1, durationUs, -1, -1, -1, -1.0f, -1, -1, null, Long.MAX_VALUE, null, false, -1, -1);
    }

    public static MediaFormat createId3Format() {
        return createFormatForMimeType(null, "application/id3", -1, -1L);
    }

    MediaFormat(String trackId, String mimeType, int bitrate, int maxInputSize, long durationUs, int width, int height, int rotationDegrees, float pixelWidthHeightRatio, int channelCount, int sampleRate, String language, long subsampleOffsetUs, List<byte[]> initializationData, boolean adaptive, int maxWidth, int maxHeight) {
        this.trackId = trackId;
        this.mimeType = Assertions.checkNotEmpty(mimeType);
        this.bitrate = bitrate;
        this.maxInputSize = maxInputSize;
        this.durationUs = durationUs;
        this.width = width;
        this.height = height;
        this.rotationDegrees = rotationDegrees;
        this.pixelWidthHeightRatio = pixelWidthHeightRatio;
        this.channelCount = channelCount;
        this.sampleRate = sampleRate;
        this.language = language;
        this.subsampleOffsetUs = subsampleOffsetUs;
        this.initializationData = initializationData == null ? Collections.emptyList() : initializationData;
        this.adaptive = adaptive;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public MediaFormat copyWithMaxInputSize(int maxInputSize) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight);
    }

    public MediaFormat copyWithMaxVideoDimensions(int maxWidth, int maxHeight) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, maxWidth, maxHeight);
    }

    public MediaFormat copyWithDurationUs(long durationUs) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight);
    }

    public MediaFormat copyWithFixedTrackInfo(String trackId, int bitrate, int width, int height, String language) {
        return new MediaFormat(trackId, this.mimeType, bitrate, this.maxInputSize, this.durationUs, width, height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, language, this.subsampleOffsetUs, this.initializationData, this.adaptive, -1, -1);
    }

    public MediaFormat copyAsAdaptive(String trackId) {
        return new MediaFormat(trackId, this.mimeType, -1, -1, this.durationUs, -1, -1, -1, -1.0f, -1, -1, null, Long.MAX_VALUE, null, true, this.maxWidth, this.maxHeight);
    }

    @SuppressLint({"InlinedApi"})
    @TargetApi(16)
    public final android.media.MediaFormat getFrameworkMediaFormatV16() {
        if (this.frameworkMediaFormat == null) {
            android.media.MediaFormat format = new android.media.MediaFormat();
            format.setString("mime", this.mimeType);
            maybeSetStringV16(format, "language", this.language);
            maybeSetIntegerV16(format, "max-input-size", this.maxInputSize);
            maybeSetIntegerV16(format, "width", this.width);
            maybeSetIntegerV16(format, "height", this.height);
            maybeSetIntegerV16(format, "rotation-degrees", this.rotationDegrees);
            maybeSetIntegerV16(format, "max-width", this.maxWidth);
            maybeSetIntegerV16(format, "max-height", this.maxHeight);
            maybeSetIntegerV16(format, "channel-count", this.channelCount);
            maybeSetIntegerV16(format, "sample-rate", this.sampleRate);
            for (int i = 0; i < this.initializationData.size(); i++) {
                format.setByteBuffer("csd-" + i, ByteBuffer.wrap(this.initializationData.get(i)));
            }
            if (this.durationUs != -1) {
                format.setLong("durationUs", this.durationUs);
            }
            this.frameworkMediaFormat = format;
        }
        return this.frameworkMediaFormat;
    }

    @TargetApi(16)
    @Deprecated
    final void setFrameworkFormatV16(android.media.MediaFormat format) {
        this.frameworkMediaFormat = format;
    }

    public String toString() {
        return "MediaFormat(" + this.trackId + ", " + this.mimeType + ", " + this.bitrate + ", " + this.maxInputSize + ", " + this.width + ", " + this.height + ", " + this.rotationDegrees + ", " + this.pixelWidthHeightRatio + ", " + this.channelCount + ", " + this.sampleRate + ", " + this.language + ", " + this.durationUs + ", " + this.adaptive + ", " + this.maxWidth + ", " + this.maxHeight + ")";
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (this.trackId == null ? 0 : this.trackId.hashCode()) + 527;
            int result2 = (((((((((((((((((((((((((((result * 31) + (this.mimeType == null ? 0 : this.mimeType.hashCode())) * 31) + this.bitrate) * 31) + this.maxInputSize) * 31) + this.width) * 31) + this.height) * 31) + this.rotationDegrees) * 31) + Float.floatToRawIntBits(this.pixelWidthHeightRatio)) * 31) + ((int) this.durationUs)) * 31) + (this.adaptive ? 1231 : 1237)) * 31) + this.maxWidth) * 31) + this.maxHeight) * 31) + this.channelCount) * 31) + this.sampleRate) * 31) + (this.language != null ? this.language.hashCode() : 0);
            for (int i = 0; i < this.initializationData.size(); i++) {
                result2 = (result2 * 31) + Arrays.hashCode(this.initializationData.get(i));
            }
            this.hashCode = result2;
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MediaFormat other = (MediaFormat) obj;
        if (this.adaptive != other.adaptive || this.bitrate != other.bitrate || this.maxInputSize != other.maxInputSize || this.width != other.width || this.height != other.height || this.rotationDegrees != other.rotationDegrees || this.pixelWidthHeightRatio != other.pixelWidthHeightRatio || this.maxWidth != other.maxWidth || this.maxHeight != other.maxHeight || this.channelCount != other.channelCount || this.sampleRate != other.sampleRate || !Util.areEqual(this.trackId, other.trackId) || !Util.areEqual(this.language, other.language) || !Util.areEqual(this.mimeType, other.mimeType) || this.initializationData.size() != other.initializationData.size()) {
            return false;
        }
        for (int i = 0; i < this.initializationData.size(); i++) {
            if (!Arrays.equals(this.initializationData.get(i), other.initializationData.get(i))) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(16)
    private static final void maybeSetStringV16(android.media.MediaFormat format, String key, String value) {
        if (value != null) {
            format.setString(key, value);
        }
    }

    @TargetApi(16)
    private static final void maybeSetIntegerV16(android.media.MediaFormat format, String key, int value) {
        if (value != -1) {
            format.setInteger(key, value);
        }
    }
}
