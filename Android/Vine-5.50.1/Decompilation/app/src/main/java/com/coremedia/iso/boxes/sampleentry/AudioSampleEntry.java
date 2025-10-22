package com.coremedia.iso.boxes.sampleentry;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.javacv.cpp.avutil;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/* loaded from: classes2.dex */
public final class AudioSampleEntry extends AbstractSampleEntry {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final String TYPE1 = "samr";
    public static final String TYPE10 = "mlpa";
    public static final String TYPE11 = "dtsl";
    public static final String TYPE12 = "dtsh";
    public static final String TYPE13 = "dtse";
    public static final String TYPE2 = "sawb";
    public static final String TYPE3 = "mp4a";
    public static final String TYPE4 = "drms";
    public static final String TYPE5 = "alac";
    public static final String TYPE7 = "owma";
    public static final String TYPE8 = "ac-3";
    public static final String TYPE9 = "ec-3";
    public static final String TYPE_ENCRYPTED = "enca";
    private long bytesPerFrame;
    private long bytesPerPacket;
    private long bytesPerSample;
    private int channelCount;
    private int compressionId;
    private int packetSize;
    private int reserved1;
    private long reserved2;
    private long sampleRate;
    private int sampleSize;
    private long samplesPerPacket;
    private int soundVersion;
    private byte[] soundVersion2Data;

    static {
        $assertionsDisabled = !AudioSampleEntry.class.desiredAssertionStatus();
    }

    public AudioSampleEntry(String type) {
        super(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    public long getSampleRate() {
        return this.sampleRate;
    }

    public int getSoundVersion() {
        return this.soundVersion;
    }

    public int getCompressionId() {
        return this.compressionId;
    }

    public int getPacketSize() {
        return this.packetSize;
    }

    public long getSamplesPerPacket() {
        return this.samplesPerPacket;
    }

    public long getBytesPerPacket() {
        return this.bytesPerPacket;
    }

    public long getBytesPerFrame() {
        return this.bytesPerFrame;
    }

    public long getBytesPerSample() {
        return this.bytesPerSample;
    }

    public byte[] getSoundVersion2Data() {
        return this.soundVersion2Data;
    }

    public int getReserved1() {
        return this.reserved1;
    }

    public long getReserved2() {
        return this.reserved2;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public void setSampleRate(long sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setSoundVersion(int soundVersion) {
        this.soundVersion = soundVersion;
    }

    public void setCompressionId(int compressionId) {
        this.compressionId = compressionId;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public void setSamplesPerPacket(long samplesPerPacket) {
        this.samplesPerPacket = samplesPerPacket;
    }

    public void setBytesPerPacket(long bytesPerPacket) {
        this.bytesPerPacket = bytesPerPacket;
    }

    public void setBytesPerFrame(long bytesPerFrame) {
        this.bytesPerFrame = bytesPerFrame;
    }

    public void setBytesPerSample(long bytesPerSample) {
        this.bytesPerSample = bytesPerSample;
    }

    public void setReserved1(int reserved1) {
        this.reserved1 = reserved1;
    }

    public void setReserved2(long reserved2) {
        this.reserved2 = reserved2;
    }

    public void setSoundVersion2Data(byte[] soundVersion2Data) {
        this.soundVersion2Data = soundVersion2Data;
    }

    @Override // com.coremedia.iso.boxes.sampleentry.AbstractSampleEntry, com.googlecode.mp4parser.AbstractContainerBox, com.coremedia.iso.boxes.Box
    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer content = ByteBuffer.allocate(28);
        dataSource.read(content);
        content.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(content);
        this.soundVersion = IsoTypeReader.readUInt16(content);
        this.reserved1 = IsoTypeReader.readUInt16(content);
        this.reserved2 = IsoTypeReader.readUInt32(content);
        this.channelCount = IsoTypeReader.readUInt16(content);
        this.sampleSize = IsoTypeReader.readUInt16(content);
        this.compressionId = IsoTypeReader.readUInt16(content);
        this.packetSize = IsoTypeReader.readUInt16(content);
        this.sampleRate = IsoTypeReader.readUInt32(content);
        if (!this.type.equals(TYPE10)) {
            this.sampleRate >>>= 16;
        }
        if (this.soundVersion == 1) {
            ByteBuffer appleStuff = ByteBuffer.allocate(16);
            dataSource.read(appleStuff);
            appleStuff.rewind();
            this.samplesPerPacket = IsoTypeReader.readUInt32(appleStuff);
            this.bytesPerPacket = IsoTypeReader.readUInt32(appleStuff);
            this.bytesPerFrame = IsoTypeReader.readUInt32(appleStuff);
            this.bytesPerSample = IsoTypeReader.readUInt32(appleStuff);
        }
        if (this.soundVersion == 2) {
            ByteBuffer appleStuff2 = ByteBuffer.allocate(36);
            dataSource.read(appleStuff2);
            appleStuff2.rewind();
            this.samplesPerPacket = IsoTypeReader.readUInt32(appleStuff2);
            this.bytesPerPacket = IsoTypeReader.readUInt32(appleStuff2);
            this.bytesPerFrame = IsoTypeReader.readUInt32(appleStuff2);
            this.bytesPerSample = IsoTypeReader.readUInt32(appleStuff2);
            this.soundVersion2Data = new byte[20];
            appleStuff2.get(this.soundVersion2Data);
        }
        if (TYPE7.equals(this.type)) {
            System.err.println(TYPE7);
            final long remaining = ((contentSize - 28) - (this.soundVersion == 1 ? 16 : 0)) - (this.soundVersion == 2 ? 36 : 0);
            final ByteBuffer owmaSpecifics = ByteBuffer.allocate(CastUtils.l2i(remaining));
            dataSource.read(owmaSpecifics);
            addBox(new Box() { // from class: com.coremedia.iso.boxes.sampleentry.AudioSampleEntry.1
                @Override // com.coremedia.iso.boxes.Box
                public Container getParent() {
                    return AudioSampleEntry.this;
                }

                @Override // com.coremedia.iso.boxes.Box
                public void setParent(Container parent) {
                    if (!AudioSampleEntry.$assertionsDisabled && parent != AudioSampleEntry.this) {
                        throw new AssertionError("you cannot diswown this special box");
                    }
                }

                @Override // com.coremedia.iso.boxes.Box
                public long getSize() {
                    return remaining;
                }

                public long getOffset() {
                    return 0L;
                }

                @Override // com.coremedia.iso.boxes.Box
                public String getType() {
                    return "----";
                }

                @Override // com.coremedia.iso.boxes.Box
                public void getBox(WritableByteChannel writableByteChannel) throws IOException {
                    owmaSpecifics.rewind();
                    writableByteChannel.write(owmaSpecifics);
                }

                @Override // com.coremedia.iso.boxes.Box
                public void parse(DataSource dataSource2, ByteBuffer header2, long contentSize2, BoxParser boxParser2) throws IOException {
                    throw new RuntimeException("NotImplemented");
                }
            });
            return;
        }
        initContainer(dataSource, ((contentSize - 28) - (this.soundVersion == 1 ? 16 : 0)) - (this.soundVersion == 2 ? 36 : 0), boxParser);
    }

    @Override // com.coremedia.iso.boxes.sampleentry.AbstractSampleEntry, com.googlecode.mp4parser.AbstractContainerBox, com.coremedia.iso.boxes.Box
    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        ByteBuffer byteBuffer = ByteBuffer.allocate((this.soundVersion == 1 ? 16 : 0) + 28 + (this.soundVersion == 2 ? 36 : 0));
        byteBuffer.position(6);
        IsoTypeWriter.writeUInt16(byteBuffer, this.dataReferenceIndex);
        IsoTypeWriter.writeUInt16(byteBuffer, this.soundVersion);
        IsoTypeWriter.writeUInt16(byteBuffer, this.reserved1);
        IsoTypeWriter.writeUInt32(byteBuffer, this.reserved2);
        IsoTypeWriter.writeUInt16(byteBuffer, this.channelCount);
        IsoTypeWriter.writeUInt16(byteBuffer, this.sampleSize);
        IsoTypeWriter.writeUInt16(byteBuffer, this.compressionId);
        IsoTypeWriter.writeUInt16(byteBuffer, this.packetSize);
        if (this.type.equals(TYPE10)) {
            IsoTypeWriter.writeUInt32(byteBuffer, getSampleRate());
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, getSampleRate() << 16);
        }
        if (this.soundVersion == 1) {
            IsoTypeWriter.writeUInt32(byteBuffer, this.samplesPerPacket);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerPacket);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerFrame);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerSample);
        }
        if (this.soundVersion == 2) {
            IsoTypeWriter.writeUInt32(byteBuffer, this.samplesPerPacket);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerPacket);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerFrame);
            IsoTypeWriter.writeUInt32(byteBuffer, this.bytesPerSample);
            byteBuffer.put(this.soundVersion2Data);
        }
        writableByteChannel.write((ByteBuffer) byteBuffer.rewind());
        writeContainer(writableByteChannel);
    }

    @Override // com.googlecode.mp4parser.AbstractContainerBox, com.coremedia.iso.boxes.Box
    public long getSize() {
        int i = 16;
        long s = (this.soundVersion == 1 ? 16 : 0) + 28 + (this.soundVersion == 2 ? 36 : 0) + getContainerSize();
        if (!this.largeBox && 8 + s < avutil.AV_CH_WIDE_RIGHT) {
            i = 8;
        }
        return s + i;
    }

    @Override // com.googlecode.mp4parser.BasicContainer
    public String toString() {
        return "AudioSampleEntry{bytesPerSample=" + this.bytesPerSample + ", bytesPerFrame=" + this.bytesPerFrame + ", bytesPerPacket=" + this.bytesPerPacket + ", samplesPerPacket=" + this.samplesPerPacket + ", packetSize=" + this.packetSize + ", compressionId=" + this.compressionId + ", soundVersion=" + this.soundVersion + ", sampleRate=" + this.sampleRate + ", sampleSize=" + this.sampleSize + ", channelCount=" + this.channelCount + ", boxes=" + getBoxes() + '}';
    }
}
