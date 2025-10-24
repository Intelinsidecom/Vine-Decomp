package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Descriptor(tags = {4})
/* loaded from: classes.dex */
public class DecoderConfigDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(DecoderConfigDescriptor.class.getName());
    AudioSpecificConfig audioSpecificInfo;
    long avgBitRate;
    int bufferSizeDB;
    byte[] configDescriptorDeadBytes;
    DecoderSpecificInfo decoderSpecificInfo;
    long maxBitRate;
    int objectTypeIndication;
    List<ProfileLevelIndicationDescriptor> profileLevelIndicationDescriptors = new ArrayList();
    int streamType;
    int upStream;

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public void parseDetail(ByteBuffer bb) throws IllegalAccessException, InstantiationException, IOException {
        int size;
        this.objectTypeIndication = IsoTypeReader.readUInt8(bb);
        int data = IsoTypeReader.readUInt8(bb);
        this.streamType = data >>> 2;
        this.upStream = (data >> 1) & 1;
        this.bufferSizeDB = IsoTypeReader.readUInt24(bb);
        this.maxBitRate = IsoTypeReader.readUInt32(bb);
        this.avgBitRate = IsoTypeReader.readUInt32(bb);
        if (bb.remaining() > 2) {
            int begin = bb.position();
            BaseDescriptor descriptor = ObjectDescriptorFactory.createFrom(this.objectTypeIndication, bb);
            int read = bb.position() - begin;
            log.finer(descriptor + " - DecoderConfigDescr1 read: " + read + ", size: " + (descriptor != null ? Integer.valueOf(descriptor.getSize()) : null));
            if (descriptor != null && read < (size = descriptor.getSize())) {
                this.configDescriptorDeadBytes = new byte[size - read];
                bb.get(this.configDescriptorDeadBytes);
            }
            if (descriptor instanceof DecoderSpecificInfo) {
                this.decoderSpecificInfo = (DecoderSpecificInfo) descriptor;
            }
            if (descriptor instanceof AudioSpecificConfig) {
                this.audioSpecificInfo = (AudioSpecificConfig) descriptor;
            }
        }
        while (bb.remaining() > 2) {
            long begin2 = bb.position();
            BaseDescriptor descriptor2 = ObjectDescriptorFactory.createFrom(this.objectTypeIndication, bb);
            log.finer(descriptor2 + " - DecoderConfigDescr2 read: " + (bb.position() - begin2) + ", size: " + (descriptor2 != null ? Integer.valueOf(descriptor2.getSize()) : null));
            if (descriptor2 instanceof ProfileLevelIndicationDescriptor) {
                this.profileLevelIndicationDescriptors.add((ProfileLevelIndicationDescriptor) descriptor2);
            }
        }
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DecoderConfigDescriptor");
        sb.append("{objectTypeIndication=").append(this.objectTypeIndication);
        sb.append(", streamType=").append(this.streamType);
        sb.append(", upStream=").append(this.upStream);
        sb.append(", bufferSizeDB=").append(this.bufferSizeDB);
        sb.append(", maxBitRate=").append(this.maxBitRate);
        sb.append(", avgBitRate=").append(this.avgBitRate);
        sb.append(", decoderSpecificInfo=").append(this.decoderSpecificInfo);
        sb.append(", audioSpecificInfo=").append(this.audioSpecificInfo);
        sb.append(", configDescriptorDeadBytes=").append(Hex.encodeHex(this.configDescriptorDeadBytes != null ? this.configDescriptorDeadBytes : new byte[0]));
        sb.append(", profileLevelIndicationDescriptors=").append(this.profileLevelIndicationDescriptors == null ? "null" : Arrays.asList(this.profileLevelIndicationDescriptors).toString());
        sb.append('}');
        return sb.toString();
    }
}
