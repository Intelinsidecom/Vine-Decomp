package co.vine.android.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class DefaultHttpOperationReader implements NetworkOperationReader {
    private final byte[] mBuffer;
    private final OutputStream mOutputStream;
    private final ProgressListener mProgressListener;

    public DefaultHttpOperationReader() {
        this(null, null);
    }

    public DefaultHttpOperationReader(OutputStream outputStream, ProgressListener progressListener) {
        this.mBuffer = new byte[2048];
        this.mOutputStream = outputStream;
        this.mProgressListener = progressListener;
    }

    @Override // co.vine.android.network.NetworkOperationReader
    public final void readInput(int statusCode, long contentLength, InputStream in) throws IOException {
        OutputStream out;
        if (this.mOutputStream != null && this.mProgressListener != null) {
            out = new CountingOutputStream(this.mOutputStream, contentLength, this.mProgressListener);
        } else {
            out = this.mOutputStream;
        }
        byte[] buffer = this.mBuffer;
        try {
            if (contentLength < 0) {
                while (true) {
                    int bytesRead = in.read(buffer, 0, buffer.length);
                    if (bytesRead == -1) {
                        break;
                    } else if (out != null) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                long bytesRemaining = contentLength;
                while (bytesRemaining > 0) {
                    int bytesRead2 = in.read(buffer, 0, (int) Math.min(bytesRemaining, buffer.length));
                    if (bytesRead2 == -1) {
                        throw new IOException("Invalid content length: " + bytesRemaining);
                    }
                    if (bytesRead2 > 0) {
                        bytesRemaining -= bytesRead2;
                        if (out != null) {
                            out.write(buffer, 0, bytesRead2);
                        }
                    }
                }
            }
            while (in.read(buffer, 0, buffer.length) != -1) {
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            while (in.read(buffer, 0, buffer.length) != -1) {
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e2) {
                }
            }
            throw th;
        }
    }

    @Override // co.vine.android.network.NetworkOperationReader
    public void onHandleError(HttpResult result) {
    }
}
