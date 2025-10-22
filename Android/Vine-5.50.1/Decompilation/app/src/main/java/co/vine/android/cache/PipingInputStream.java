package co.vine.android.cache;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class PipingInputStream extends FilterInputStream {
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    public PipingInputStream(InputStream inputStream, OutputStream outputStream) {
        super(inputStream);
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        this.mInputStream = inputStream;
        this.mOutputStream = outputStream;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer, int offset, int count) throws IOException {
        int readBytes = this.mInputStream.read(buffer, offset, count);
        if (readBytes != -1 && this.mOutputStream != null) {
            this.mOutputStream.write(buffer, offset, readBytes);
        }
        return readBytes;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int oneByte = this.mInputStream.read();
        if (oneByte != -1 && this.mOutputStream != null) {
            this.mOutputStream.write(oneByte);
        }
        return oneByte;
    }
}
