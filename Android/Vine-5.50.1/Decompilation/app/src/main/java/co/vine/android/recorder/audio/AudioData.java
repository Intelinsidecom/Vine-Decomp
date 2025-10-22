package co.vine.android.recorder.audio;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/* loaded from: classes.dex */
public class AudioData implements Externalizable {
    static final long serialVersionUID = -210670612808274402L;
    public int size;
    public int start;

    public AudioData() {
    }

    public AudioData(int start, int size) {
        this.start = start;
        this.size = size;
    }

    public AudioData(AudioData source) {
        this.start = source.start;
        this.size = source.size;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        this.start = input.readInt();
        this.size = input.readInt();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeInt(this.start);
        output.writeInt(this.size);
    }
}
