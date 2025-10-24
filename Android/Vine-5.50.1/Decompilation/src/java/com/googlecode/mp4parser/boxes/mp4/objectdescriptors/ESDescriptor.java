package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Descriptor(tags = {3})
/* loaded from: classes.dex */
public class ESDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(ESDescriptor.class.getName());
    int URLFlag;
    String URLString;
    DecoderConfigDescriptor decoderConfigDescriptor;
    int dependsOnEsId;
    int esId;
    int oCREsId;
    int oCRstreamFlag;
    int remoteODFlag;
    SLConfigDescriptor slConfigDescriptor;
    int streamDependenceFlag;
    int streamPriority;
    int URLLength = 0;
    List<BaseDescriptor> otherDescriptors = new ArrayList();

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public void parseDetail(ByteBuffer bb) throws IllegalAccessException, InstantiationException, IOException {
        this.esId = IsoTypeReader.readUInt16(bb);
        int data = IsoTypeReader.readUInt8(bb);
        this.streamDependenceFlag = data >>> 7;
        this.URLFlag = (data >>> 6) & 1;
        this.oCRstreamFlag = (data >>> 5) & 1;
        this.streamPriority = data & 31;
        if (this.streamDependenceFlag == 1) {
            this.dependsOnEsId = IsoTypeReader.readUInt16(bb);
        }
        if (this.URLFlag == 1) {
            this.URLLength = IsoTypeReader.readUInt8(bb);
            this.URLString = IsoTypeReader.readString(bb, this.URLLength);
        }
        if (this.oCRstreamFlag == 1) {
            this.oCREsId = IsoTypeReader.readUInt16(bb);
        }
        int baseSize = getSizeBytes() + 1 + 2 + 1 + (this.streamDependenceFlag == 1 ? 2 : 0) + (this.URLFlag == 1 ? this.URLLength + 1 : 0) + (this.oCRstreamFlag == 1 ? 2 : 0);
        int begin = bb.position();
        if (getSize() > baseSize + 2) {
            BaseDescriptor descriptor = ObjectDescriptorFactory.createFrom(-1, bb);
            long read = bb.position() - begin;
            log.finer(descriptor + " - ESDescriptor1 read: " + read + ", size: " + (descriptor != null ? Integer.valueOf(descriptor.getSize()) : null));
            if (descriptor != null) {
                int size = descriptor.getSize();
                bb.position(begin + size);
                baseSize += size;
            } else {
                baseSize = (int) (baseSize + read);
            }
            if (descriptor instanceof DecoderConfigDescriptor) {
                this.decoderConfigDescriptor = (DecoderConfigDescriptor) descriptor;
            }
        }
        int begin2 = bb.position();
        if (getSize() > baseSize + 2) {
            BaseDescriptor descriptor2 = ObjectDescriptorFactory.createFrom(-1, bb);
            long read2 = bb.position() - begin2;
            log.finer(descriptor2 + " - ESDescriptor2 read: " + read2 + ", size: " + (descriptor2 != null ? Integer.valueOf(descriptor2.getSize()) : null));
            if (descriptor2 != null) {
                int size2 = descriptor2.getSize();
                bb.position(begin2 + size2);
                baseSize += size2;
            } else {
                baseSize = (int) (baseSize + read2);
            }
            if (descriptor2 instanceof SLConfigDescriptor) {
                this.slConfigDescriptor = (SLConfigDescriptor) descriptor2;
            }
        } else {
            log.warning("SLConfigDescriptor is missing!");
        }
        while (getSize() - baseSize > 2) {
            int begin3 = bb.position();
            BaseDescriptor descriptor3 = ObjectDescriptorFactory.createFrom(-1, bb);
            long read3 = bb.position() - begin3;
            log.finer(descriptor3 + " - ESDescriptor3 read: " + read3 + ", size: " + (descriptor3 != null ? Integer.valueOf(descriptor3.getSize()) : null));
            if (descriptor3 != null) {
                int size3 = descriptor3.getSize();
                bb.position(begin3 + size3);
                baseSize += size3;
            } else {
                baseSize = (int) (baseSize + read3);
            }
            this.otherDescriptors.add(descriptor3);
        }
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ESDescriptor");
        sb.append("{esId=").append(this.esId);
        sb.append(", streamDependenceFlag=").append(this.streamDependenceFlag);
        sb.append(", URLFlag=").append(this.URLFlag);
        sb.append(", oCRstreamFlag=").append(this.oCRstreamFlag);
        sb.append(", streamPriority=").append(this.streamPriority);
        sb.append(", URLLength=").append(this.URLLength);
        sb.append(", URLString='").append(this.URLString).append('\'');
        sb.append(", remoteODFlag=").append(this.remoteODFlag);
        sb.append(", dependsOnEsId=").append(this.dependsOnEsId);
        sb.append(", oCREsId=").append(this.oCREsId);
        sb.append(", decoderConfigDescriptor=").append(this.decoderConfigDescriptor);
        sb.append(", slConfigDescriptor=").append(this.slConfigDescriptor);
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ESDescriptor that = (ESDescriptor) o;
        if (this.URLFlag == that.URLFlag && this.URLLength == that.URLLength && this.dependsOnEsId == that.dependsOnEsId && this.esId == that.esId && this.oCREsId == that.oCREsId && this.oCRstreamFlag == that.oCRstreamFlag && this.remoteODFlag == that.remoteODFlag && this.streamDependenceFlag == that.streamDependenceFlag && this.streamPriority == that.streamPriority) {
            if (this.URLString == null ? that.URLString != null : !this.URLString.equals(that.URLString)) {
                return false;
            }
            if (this.decoderConfigDescriptor == null ? that.decoderConfigDescriptor != null : !this.decoderConfigDescriptor.equals(that.decoderConfigDescriptor)) {
                return false;
            }
            if (this.otherDescriptors == null ? that.otherDescriptors != null : !this.otherDescriptors.equals(that.otherDescriptors)) {
                return false;
            }
            if (this.slConfigDescriptor != null) {
                if (this.slConfigDescriptor.equals(that.slConfigDescriptor)) {
                    return true;
                }
            } else if (that.slConfigDescriptor == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.esId;
        return (((((((((((((((((((((((result * 31) + this.streamDependenceFlag) * 31) + this.URLFlag) * 31) + this.oCRstreamFlag) * 31) + this.streamPriority) * 31) + this.URLLength) * 31) + (this.URLString != null ? this.URLString.hashCode() : 0)) * 31) + this.remoteODFlag) * 31) + this.dependsOnEsId) * 31) + this.oCREsId) * 31) + (this.decoderConfigDescriptor != null ? this.decoderConfigDescriptor.hashCode() : 0)) * 31) + (this.slConfigDescriptor != null ? this.slConfigDescriptor.hashCode() : 0)) * 31) + (this.otherDescriptors != null ? this.otherDescriptors.hashCode() : 0);
    }
}
