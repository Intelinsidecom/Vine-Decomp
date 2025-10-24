package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.UserBox;
import com.googlecode.javacv.cpp.avutil;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import com.googlecode.mp4parser.util.Path;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/* loaded from: classes2.dex */
public abstract class AbstractBox implements Box {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static Logger LOG;
    private ByteBuffer content;
    long contentStartPosition;
    DataSource dataSource;
    long offset;
    private Container parent;
    protected String type;
    private byte[] userType;
    long memMapSize = -1;
    private ByteBuffer deadBytes = null;
    boolean isRead = true;
    boolean isParsed = true;

    protected abstract void _parseDetails(ByteBuffer byteBuffer);

    protected abstract void getContent(ByteBuffer byteBuffer);

    protected abstract long getContentSize();

    static {
        $assertionsDisabled = !AbstractBox.class.desiredAssertionStatus();
        LOG = Logger.getLogger(AbstractBox.class);
    }

    private synchronized void readContent() {
        if (!this.isRead) {
            try {
                LOG.logDebug("mem mapping " + getType());
                this.content = this.dataSource.map(this.contentStartPosition, this.memMapSize);
                this.isRead = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public long getOffset() {
        return this.offset;
    }

    protected AbstractBox(String type) {
        this.type = type;
    }

    protected AbstractBox(String type, byte[] userType) {
        this.type = type;
        this.userType = userType;
    }

    @Override // com.coremedia.iso.boxes.Box
    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        this.contentStartPosition = dataSource.position();
        this.offset = this.contentStartPosition - header.remaining();
        this.memMapSize = contentSize;
        this.dataSource = dataSource;
        dataSource.position(dataSource.position() + contentSize);
        this.isRead = false;
        this.isParsed = false;
    }

    @Override // com.coremedia.iso.boxes.Box
    public void getBox(WritableByteChannel os) throws IOException {
        if (this.isRead) {
            if (this.isParsed) {
                ByteBuffer bb = ByteBuffer.allocate(CastUtils.l2i(getSize()));
                getHeader(bb);
                getContent(bb);
                if (this.deadBytes != null) {
                    this.deadBytes.rewind();
                    while (this.deadBytes.remaining() > 0) {
                        bb.put(this.deadBytes);
                    }
                }
                os.write((ByteBuffer) bb.rewind());
                return;
            }
            ByteBuffer header = ByteBuffer.allocate((isSmallBox() ? 8 : 16) + (UserBox.TYPE.equals(getType()) ? 16 : 0));
            getHeader(header);
            os.write((ByteBuffer) header.rewind());
            os.write((ByteBuffer) this.content.position(0));
            return;
        }
        ByteBuffer header2 = ByteBuffer.allocate((isSmallBox() ? 8 : 16) + (UserBox.TYPE.equals(getType()) ? 16 : 0));
        getHeader(header2);
        os.write((ByteBuffer) header2.rewind());
        this.dataSource.transferTo(this.contentStartPosition, this.memMapSize, os);
    }

    public final synchronized void parseDetails() {
        readContent();
        LOG.logDebug("parsing details of " + getType());
        if (this.content != null) {
            ByteBuffer content = this.content;
            this.isParsed = true;
            content.rewind();
            _parseDetails(content);
            if (content.remaining() > 0) {
                this.deadBytes = content.slice();
            }
            this.content = null;
            if (!$assertionsDisabled && !verify(content)) {
                throw new AssertionError();
            }
        }
    }

    protected void setDeadBytes(ByteBuffer newDeadBytes) {
        this.deadBytes = newDeadBytes;
    }

    @Override // com.coremedia.iso.boxes.Box
    public long getSize() {
        long size;
        if (!this.isRead) {
            size = this.memMapSize;
        } else if (this.isParsed) {
            size = getContentSize();
        } else {
            size = this.content != null ? this.content.limit() : 0;
        }
        return size + (UserBox.TYPE.equals(getType()) ? 16 : 0) + (size >= 4294967288L ? 8 : 0) + 8 + (this.deadBytes != null ? this.deadBytes.limit() : 0);
    }

    @Override // com.coremedia.iso.boxes.Box
    public String getType() {
        return this.type;
    }

    public byte[] getUserType() {
        return this.userType;
    }

    @Override // com.coremedia.iso.boxes.Box
    public Container getParent() {
        return this.parent;
    }

    @Override // com.coremedia.iso.boxes.Box
    public void setParent(Container parent) {
        this.parent = parent;
    }

    public boolean isParsed() {
        return this.isParsed;
    }

    private boolean verify(ByteBuffer content) {
        ByteBuffer bb = ByteBuffer.allocate(CastUtils.l2i((this.deadBytes != null ? this.deadBytes.limit() : 0) + getContentSize()));
        getContent(bb);
        if (this.deadBytes != null) {
            this.deadBytes.rewind();
            while (this.deadBytes.remaining() > 0) {
                bb.put(this.deadBytes);
            }
        }
        content.rewind();
        bb.rewind();
        if (content.remaining() != bb.remaining()) {
            System.err.print(String.valueOf(getType()) + ": remaining differs " + content.remaining() + " vs. " + bb.remaining());
            LOG.logError(String.valueOf(getType()) + ": remaining differs " + content.remaining() + " vs. " + bb.remaining());
            return false;
        }
        int p = content.position();
        int i = content.limit() - 1;
        int j = bb.limit() - 1;
        while (i >= p) {
            byte v1 = content.get(i);
            byte v2 = bb.get(j);
            if (v1 == v2) {
                i--;
                j--;
            } else {
                LOG.logError(String.format("%s: buffers differ at %d: %2X/%2X", getType(), Integer.valueOf(i), Byte.valueOf(v1), Byte.valueOf(v2)));
                byte[] b1 = new byte[content.remaining()];
                byte[] b2 = new byte[bb.remaining()];
                content.get(b1);
                bb.get(b2);
                System.err.println("original      : " + Hex.encodeHex(b1, 4));
                System.err.println("reconstructed : " + Hex.encodeHex(b2, 4));
                return false;
            }
        }
        return true;
    }

    private boolean isSmallBox() {
        int baseSize = 8;
        if (UserBox.TYPE.equals(getType())) {
            baseSize = 8 + 16;
        }
        if (!this.isRead) {
            return this.memMapSize + ((long) baseSize) < avutil.AV_CH_WIDE_RIGHT;
        }
        if (this.isParsed) {
            return (getContentSize() + ((long) (this.deadBytes != null ? this.deadBytes.limit() : 0))) + ((long) baseSize) < avutil.AV_CH_WIDE_RIGHT;
        }
        return ((long) (this.content.limit() + baseSize)) < avutil.AV_CH_WIDE_RIGHT;
    }

    private void getHeader(ByteBuffer byteBuffer) {
        if (isSmallBox()) {
            IsoTypeWriter.writeUInt32(byteBuffer, getSize());
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, 1L);
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
            IsoTypeWriter.writeUInt64(byteBuffer, getSize());
        }
        if (UserBox.TYPE.equals(getType())) {
            byteBuffer.put(getUserType());
        }
    }

    public String getPath() {
        return Path.createPath(this);
    }
}
