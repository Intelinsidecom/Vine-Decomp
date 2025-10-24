package co.vine.android.recorder.audio;

import android.media.AudioRecord;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class AudioByteArray implements AudioArray<byte[], ByteBuffer> {
    private final byte[] mData;
    private final int mLength;

    public AudioByteArray(byte[] data) {
        this.mData = data;
        this.mLength = data.length / 2;
    }

    public AudioByteArray(int length) {
        this(new byte[length * 2]);
    }

    @Override // co.vine.android.recorder.audio.AudioArray
    public byte[] getData() {
        return this.mData;
    }

    @Override // co.vine.android.recorder.audio.AudioArray
    public int getLength() {
        return this.mLength;
    }

    @Override // co.vine.android.recorder.audio.AudioArray
    public ByteBuffer asBuffer() {
        return ByteBuffer.wrap(this.mData);
    }

    @Override // co.vine.android.recorder.audio.AudioArray
    public void getFrom(ByteBuffer audioBuffer) {
        audioBuffer.get(this.mData);
    }

    @Override // co.vine.android.recorder.audio.AudioArray
    public void putInto(ByteBuffer audioBuffer, int start, int size) {
        audioBuffer.put(this.mData, start, size);
    }

    @Override // co.vine.android.recorder.audio.AudioArray
    public int readFrom(AudioRecord audioRecord, int length) {
        return audioRecord.read(this.mData, 0, length);
    }

    @Override // co.vine.android.recorder.audio.AudioArray
    public void putInto(AudioArray<byte[], ?> dest, int offset, int count) {
        ByteBuffer.wrap(this.mData, 0, count).get(dest.getData(), offset, count);
    }

    @Override // co.vine.android.recorder.audio.AudioArray
    public void writeInto(DataOutputStream stream, int start, int count) throws IOException {
        stream.write(this.mData, start, count);
    }
}
