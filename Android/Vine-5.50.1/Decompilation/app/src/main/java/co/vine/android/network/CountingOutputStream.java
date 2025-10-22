package co.vine.android.network;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class CountingOutputStream extends FilterOutputStream {
    private final long mChunk;
    private final long mLength;
    private final ProgressListener mListener;
    private long mNext;
    private long mTransferred;

    public CountingOutputStream(OutputStream out, long length, ProgressListener listener) {
        super(out);
        this.mListener = listener;
        this.mLength = 2 * length;
        this.mTransferred = 0L;
        this.mChunk = this.mLength / 5;
        this.mNext = this.mChunk;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        this.mTransferred += len;
        if (this.mTransferred >= this.mNext) {
            super.flush();
            if (this.mListener != null) {
                this.mListener.onProgress(this.mTransferred, this.mLength);
            }
            this.mNext += this.mChunk;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int b) throws IOException {
        super.write(b);
        this.mTransferred++;
        if (this.mTransferred >= this.mNext) {
            super.flush();
            if (this.mListener != null) {
                this.mListener.onProgress(this.mTransferred, this.mLength);
            }
            this.mNext += this.mChunk;
        }
    }
}
