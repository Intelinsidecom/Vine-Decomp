package co.vine.android.recorder.audio;

import android.media.AudioRecord;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.Buffer;

/* loaded from: classes.dex */
public interface AudioArray<T, K extends Buffer> {
    K asBuffer();

    T getData();

    void getFrom(K k);

    int getLength();

    void putInto(AudioArray<T, ?> audioArray, int i, int i2);

    void putInto(K k, int i, int i2);

    int readFrom(AudioRecord audioRecord, int i);

    void writeInto(DataOutputStream dataOutputStream, int i, int i2) throws IOException;
}
