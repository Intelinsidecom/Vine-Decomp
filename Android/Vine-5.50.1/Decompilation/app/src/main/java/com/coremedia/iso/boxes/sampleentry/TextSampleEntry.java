package com.coremedia.iso.boxes.sampleentry;

import android.support.v4.media.session.PlaybackStateCompat;
import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.javacv.cpp.avutil;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class TextSampleEntry extends AbstractSampleEntry {
    public static final String TYPE1 = "tx3g";
    public static final String TYPE_ENCRYPTED = "enct";
    private int[] backgroundColorRgba;
    private BoxRecord boxRecord;
    private long displayFlags;
    private int horizontalJustification;
    private StyleRecord styleRecord;
    private int verticalJustification;

    public TextSampleEntry() {
        super(TYPE1);
        this.backgroundColorRgba = new int[4];
        this.boxRecord = new BoxRecord();
        this.styleRecord = new StyleRecord();
    }

    public TextSampleEntry(String type) {
        super(type);
        this.backgroundColorRgba = new int[4];
        this.boxRecord = new BoxRecord();
        this.styleRecord = new StyleRecord();
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override // com.coremedia.iso.boxes.sampleentry.AbstractSampleEntry, com.googlecode.mp4parser.AbstractContainerBox, com.coremedia.iso.boxes.Box
    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        ByteBuffer content = ByteBuffer.allocate(38);
        dataSource.read(content);
        content.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(content);
        this.displayFlags = IsoTypeReader.readUInt32(content);
        this.horizontalJustification = IsoTypeReader.readUInt8(content);
        this.verticalJustification = IsoTypeReader.readUInt8(content);
        this.backgroundColorRgba = new int[4];
        this.backgroundColorRgba[0] = IsoTypeReader.readUInt8(content);
        this.backgroundColorRgba[1] = IsoTypeReader.readUInt8(content);
        this.backgroundColorRgba[2] = IsoTypeReader.readUInt8(content);
        this.backgroundColorRgba[3] = IsoTypeReader.readUInt8(content);
        this.boxRecord = new BoxRecord();
        this.boxRecord.parse(content);
        this.styleRecord = new StyleRecord();
        this.styleRecord.parse(content);
        initContainer(dataSource, contentSize - 38, boxParser);
    }

    @Override // com.coremedia.iso.boxes.sampleentry.AbstractSampleEntry, com.googlecode.mp4parser.AbstractContainerBox, com.coremedia.iso.boxes.Box
    public void getBox(WritableByteChannel writableByteChannel) throws IOException {
        writableByteChannel.write(getHeader());
        ByteBuffer byteBuffer = ByteBuffer.allocate(38);
        byteBuffer.position(6);
        IsoTypeWriter.writeUInt16(byteBuffer, this.dataReferenceIndex);
        IsoTypeWriter.writeUInt32(byteBuffer, this.displayFlags);
        IsoTypeWriter.writeUInt8(byteBuffer, this.horizontalJustification);
        IsoTypeWriter.writeUInt8(byteBuffer, this.verticalJustification);
        IsoTypeWriter.writeUInt8(byteBuffer, this.backgroundColorRgba[0]);
        IsoTypeWriter.writeUInt8(byteBuffer, this.backgroundColorRgba[1]);
        IsoTypeWriter.writeUInt8(byteBuffer, this.backgroundColorRgba[2]);
        IsoTypeWriter.writeUInt8(byteBuffer, this.backgroundColorRgba[3]);
        this.boxRecord.getContent(byteBuffer);
        this.styleRecord.getContent(byteBuffer);
        writableByteChannel.write((ByteBuffer) byteBuffer.rewind());
        writeContainer(writableByteChannel);
    }

    @Override // com.googlecode.mp4parser.BasicContainer
    public String toString() {
        return "TextSampleEntry";
    }

    public BoxRecord getBoxRecord() {
        return this.boxRecord;
    }

    public void setBoxRecord(BoxRecord boxRecord) {
        this.boxRecord = boxRecord;
    }

    public StyleRecord getStyleRecord() {
        return this.styleRecord;
    }

    public void setStyleRecord(StyleRecord styleRecord) {
        this.styleRecord = styleRecord;
    }

    public boolean isScrollIn() {
        return (this.displayFlags & 32) == 32;
    }

    public void setScrollIn(boolean scrollIn) {
        if (scrollIn) {
            this.displayFlags |= 32;
        } else {
            this.displayFlags &= -33;
        }
    }

    public boolean isScrollOut() {
        return (this.displayFlags & 64) == 64;
    }

    public void setScrollOut(boolean scrollOutIn) {
        if (scrollOutIn) {
            this.displayFlags |= 64;
        } else {
            this.displayFlags &= -65;
        }
    }

    public boolean isScrollDirection() {
        return (this.displayFlags & 384) == 384;
    }

    public void setScrollDirection(boolean scrollOutIn) {
        if (scrollOutIn) {
            this.displayFlags |= 384;
        } else {
            this.displayFlags &= -385;
        }
    }

    public boolean isContinuousKaraoke() {
        return (this.displayFlags & PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
    }

    public void setContinuousKaraoke(boolean continuousKaraoke) {
        if (continuousKaraoke) {
            this.displayFlags |= PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
        } else {
            this.displayFlags &= -2049;
        }
    }

    public boolean isWriteTextVertically() {
        return (this.displayFlags & PlaybackStateCompat.ACTION_PREPARE_FROM_URI) == PlaybackStateCompat.ACTION_PREPARE_FROM_URI;
    }

    public void setWriteTextVertically(boolean writeTextVertically) {
        if (writeTextVertically) {
            this.displayFlags |= PlaybackStateCompat.ACTION_PREPARE_FROM_URI;
        } else {
            this.displayFlags &= -131073;
        }
    }

    public boolean isFillTextRegion() {
        return (this.displayFlags & 262144) == 262144;
    }

    public void setFillTextRegion(boolean fillTextRegion) {
        if (fillTextRegion) {
            this.displayFlags |= 262144;
        } else {
            this.displayFlags &= -262145;
        }
    }

    public int getHorizontalJustification() {
        return this.horizontalJustification;
    }

    public void setHorizontalJustification(int horizontalJustification) {
        this.horizontalJustification = horizontalJustification;
    }

    public int getVerticalJustification() {
        return this.verticalJustification;
    }

    public void setVerticalJustification(int verticalJustification) {
        this.verticalJustification = verticalJustification;
    }

    public int[] getBackgroundColorRgba() {
        return this.backgroundColorRgba;
    }

    public void setBackgroundColorRgba(int[] backgroundColorRgba) {
        this.backgroundColorRgba = backgroundColorRgba;
    }

    public static class BoxRecord {
        int bottom;
        int left;
        int right;
        int top;

        public void parse(ByteBuffer in) {
            this.top = IsoTypeReader.readUInt16(in);
            this.left = IsoTypeReader.readUInt16(in);
            this.bottom = IsoTypeReader.readUInt16(in);
            this.right = IsoTypeReader.readUInt16(in);
        }

        public void getContent(ByteBuffer bb) {
            IsoTypeWriter.writeUInt16(bb, this.top);
            IsoTypeWriter.writeUInt16(bb, this.left);
            IsoTypeWriter.writeUInt16(bb, this.bottom);
            IsoTypeWriter.writeUInt16(bb, this.right);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BoxRecord boxRecord = (BoxRecord) o;
            return this.bottom == boxRecord.bottom && this.left == boxRecord.left && this.right == boxRecord.right && this.top == boxRecord.top;
        }

        public int hashCode() {
            int result = this.top;
            return (((((result * 31) + this.left) * 31) + this.bottom) * 31) + this.right;
        }
    }

    public static class StyleRecord {
        int endChar;
        int faceStyleFlags;
        int fontId;
        int fontSize;
        int startChar;
        int[] textColor = {255, 255, 255, 255};

        public void parse(ByteBuffer in) {
            this.startChar = IsoTypeReader.readUInt16(in);
            this.endChar = IsoTypeReader.readUInt16(in);
            this.fontId = IsoTypeReader.readUInt16(in);
            this.faceStyleFlags = IsoTypeReader.readUInt8(in);
            this.fontSize = IsoTypeReader.readUInt8(in);
            this.textColor = new int[4];
            this.textColor[0] = IsoTypeReader.readUInt8(in);
            this.textColor[1] = IsoTypeReader.readUInt8(in);
            this.textColor[2] = IsoTypeReader.readUInt8(in);
            this.textColor[3] = IsoTypeReader.readUInt8(in);
        }

        public void getContent(ByteBuffer bb) {
            IsoTypeWriter.writeUInt16(bb, this.startChar);
            IsoTypeWriter.writeUInt16(bb, this.endChar);
            IsoTypeWriter.writeUInt16(bb, this.fontId);
            IsoTypeWriter.writeUInt8(bb, this.faceStyleFlags);
            IsoTypeWriter.writeUInt8(bb, this.fontSize);
            IsoTypeWriter.writeUInt8(bb, this.textColor[0]);
            IsoTypeWriter.writeUInt8(bb, this.textColor[1]);
            IsoTypeWriter.writeUInt8(bb, this.textColor[2]);
            IsoTypeWriter.writeUInt8(bb, this.textColor[3]);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StyleRecord that = (StyleRecord) o;
            return this.endChar == that.endChar && this.faceStyleFlags == that.faceStyleFlags && this.fontId == that.fontId && this.fontSize == that.fontSize && this.startChar == that.startChar && Arrays.equals(this.textColor, that.textColor);
        }

        public int hashCode() {
            int result = this.startChar;
            return (((((((((result * 31) + this.endChar) * 31) + this.fontId) * 31) + this.faceStyleFlags) * 31) + this.fontSize) * 31) + (this.textColor != null ? Arrays.hashCode(this.textColor) : 0);
        }
    }

    @Override // com.googlecode.mp4parser.AbstractContainerBox, com.coremedia.iso.boxes.Box
    public long getSize() {
        long s = getContainerSize();
        return ((this.largeBox || s + 38 >= avutil.AV_CH_WIDE_RIGHT) ? 16 : 8) + s + 38;
    }
}
