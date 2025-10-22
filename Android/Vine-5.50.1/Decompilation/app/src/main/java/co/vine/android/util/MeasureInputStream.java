package co.vine.android.util;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class MeasureInputStream extends InputStream {
    private final InputStream mInputStream;
    private long mReadTimeMs = 0;
    private long mReadTimeNs;

    public MeasureInputStream(InputStream inputStream) {
        this.mInputStream = inputStream;
    }

    public long getReadTime() {
        return this.mReadTimeMs + (this.mReadTimeNs / 1000000);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.mInputStream.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.mInputStream.close();
    }

    @Override // java.io.InputStream
    public void mark(int readlimit) {
        this.mInputStream.mark(readlimit);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.mInputStream.markSupported();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        long now = System.nanoTime();
        int result = this.mInputStream.read();
        this.mReadTimeNs += System.nanoTime() - now;
        return result;
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        long now = System.currentTimeMillis();
        int result = this.mInputStream.read(buffer);
        this.mReadTimeMs += System.currentTimeMillis() - now;
        return result;
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        long now = System.currentTimeMillis();
        int result = this.mInputStream.read(buffer, offset, length);
        this.mReadTimeMs += System.currentTimeMillis() - now;
        return result;
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        this.mInputStream.reset();
    }

    @Override // java.io.InputStream
    public long skip(long byteCount) throws IOException {
        long now = System.currentTimeMillis();
        long result = super.skip(byteCount);
        this.mReadTimeMs += System.currentTimeMillis() - now;
        return result;
    }
}
