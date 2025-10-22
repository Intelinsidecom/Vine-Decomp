package com.coremedia.iso.boxes.fragment;

import android.support.v4.media.session.PlaybackStateCompat;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class SampleFlags {
    private byte is_leading;
    private byte reserved;
    private int sampleDegradationPriority;
    private byte sampleDependsOn;
    private byte sampleHasRedundancy;
    private byte sampleIsDependedOn;
    private boolean sampleIsDifferenceSample;
    private byte samplePaddingValue;

    public SampleFlags() {
    }

    public SampleFlags(ByteBuffer bb) {
        long a = IsoTypeReader.readUInt32(bb);
        this.reserved = (byte) (((-268435456) & a) >> 28);
        this.is_leading = (byte) ((201326592 & a) >> 26);
        this.sampleDependsOn = (byte) ((50331648 & a) >> 24);
        this.sampleIsDependedOn = (byte) ((12582912 & a) >> 22);
        this.sampleHasRedundancy = (byte) ((3145728 & a) >> 20);
        this.samplePaddingValue = (byte) ((917504 & a) >> 17);
        this.sampleIsDifferenceSample = ((PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH & a) >> 16) > 0;
        this.sampleDegradationPriority = (int) (65535 & a);
    }

    public void getContent(ByteBuffer os) {
        long a = 0 | (this.reserved << 28);
        IsoTypeWriter.writeUInt32(os, a | (this.is_leading << 26) | (this.sampleDependsOn << 24) | (this.sampleIsDependedOn << 22) | (this.sampleHasRedundancy << 20) | (this.samplePaddingValue << 17) | ((this.sampleIsDifferenceSample ? 1 : 0) << 16) | this.sampleDegradationPriority);
    }

    public boolean isSampleIsDifferenceSample() {
        return this.sampleIsDifferenceSample;
    }

    public String toString() {
        return "SampleFlags{reserved=" + ((int) this.reserved) + ", is_leading=" + ((int) this.is_leading) + ", sampleDependsOn=" + ((int) this.sampleDependsOn) + ", sampleIsDependedOn=" + ((int) this.sampleIsDependedOn) + ", sampleHasRedundancy=" + ((int) this.sampleHasRedundancy) + ", samplePaddingValue=" + ((int) this.samplePaddingValue) + ", sampleIsDifferenceSample=" + this.sampleIsDifferenceSample + ", sampleDegradationPriority=" + this.sampleDegradationPriority + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SampleFlags that = (SampleFlags) o;
        return this.is_leading == that.is_leading && this.reserved == that.reserved && this.sampleDegradationPriority == that.sampleDegradationPriority && this.sampleDependsOn == that.sampleDependsOn && this.sampleHasRedundancy == that.sampleHasRedundancy && this.sampleIsDependedOn == that.sampleIsDependedOn && this.sampleIsDifferenceSample == that.sampleIsDifferenceSample && this.samplePaddingValue == that.samplePaddingValue;
    }

    public int hashCode() {
        int result = this.reserved;
        return (((((((((((((result * 31) + this.is_leading) * 31) + this.sampleDependsOn) * 31) + this.sampleIsDependedOn) * 31) + this.sampleHasRedundancy) * 31) + this.samplePaddingValue) * 31) + (this.sampleIsDifferenceSample ? 1 : 0)) * 31) + this.sampleDegradationPriority;
    }
}
