package co.vine.android.recorder.video;

import co.vine.android.recorder.RecordSegment;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/* loaded from: classes.dex */
public class VideoData implements Externalizable {
    private static final long serialVersionUID = 8590112321194730309L;
    public byte[] data;
    public boolean encoded;
    public boolean keyFrame;
    public RecordSegment segment;
    public int size;
    public int start;
    public long timestamp;

    public VideoData() {
    }

    public VideoData(long timestamp, byte[] data) {
        this.timestamp = timestamp;
        this.data = data;
        this.start = -1;
        this.size = -1;
        this.encoded = false;
    }

    public VideoData(VideoData source) {
        this.timestamp = source.timestamp;
        this.data = source.data;
        this.size = source.size;
        this.start = source.start;
        this.keyFrame = source.keyFrame;
        this.encoded = source.encoded;
    }

    public void setSegment(RecordSegment segment) {
        this.segment = segment;
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(-3L);
        out.writeBoolean(this.keyFrame);
        out.writeLong(this.timestamp);
        out.writeInt(this.start);
        out.writeInt(this.size);
        out.writeBoolean(this.encoded);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput stream) throws IOException, ClassNotFoundException {
        long possibleVersion = stream.readLong();
        if (possibleVersion >= 0) {
            this.timestamp = -3L;
        } else {
            this.keyFrame = stream.readBoolean();
            this.timestamp = stream.readLong();
        }
        this.start = stream.readInt();
        this.size = stream.readInt();
        if (possibleVersion < -2) {
            this.encoded = stream.readBoolean();
        } else {
            this.encoded = true;
        }
    }
}
