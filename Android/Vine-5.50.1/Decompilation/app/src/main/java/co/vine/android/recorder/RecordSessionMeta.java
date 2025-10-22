package co.vine.android.recorder;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/* loaded from: classes.dex */
public class RecordSessionMeta implements Externalizable {
    private static final long serialVersionUID = 3412330863787653276L;
    private long mLastModified;
    private int mProgress;

    public RecordSessionMeta() {
    }

    public RecordSessionMeta(int progress) {
        this.mLastModified = System.currentTimeMillis();
        this.mProgress = progress;
    }

    public int getProgress() {
        return this.mProgress;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        this.mProgress = input.readInt();
        this.mLastModified = input.readLong();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeInt(this.mProgress);
        output.writeLong(this.mLastModified);
    }
}
