package co.vine.android.util;

import co.vine.android.network.FileNetworkEntity;
import co.vine.android.network.TransferProgressEvent;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.entity.FileEntity;

/* loaded from: classes.dex */
public class MeasureOutputStream extends FileEntity implements FileNetworkEntity {
    private final String mContentType;
    private final File mFile;
    private final FileNetworkEntity.ProgressListener mProgressListener;

    public MeasureOutputStream(File file, String contentType, FileNetworkEntity.ProgressListener listener) {
        super(file, contentType);
        this.mFile = file;
        this.mContentType = contentType;
        this.mProgressListener = listener;
    }

    @Override // org.apache.http.entity.FileEntity, org.apache.http.HttpEntity
    public void writeTo(OutputStream out) throws IOException {
        super.writeTo(new CountingOutputStream(out, this.mProgressListener));
    }

    private static class CountingOutputStream extends FilterOutputStream {
        private final TransferProgressEvent mProgressEvent;
        private final FileNetworkEntity.ProgressListener mProgressListener;

        public CountingOutputStream(OutputStream out, FileNetworkEntity.ProgressListener listener) {
            super(out);
            this.mProgressListener = listener;
            this.mProgressEvent = new TransferProgressEvent(1);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] b, int off, int len) throws IOException {
            this.out.write(b, off, len);
            this.mProgressEvent.setBytesTransfered(len);
            if (this.mProgressListener != null) {
                this.mProgressListener.progressChanged(this.mProgressEvent);
            }
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(int b) throws IOException {
            this.out.write(b);
            this.mProgressEvent.setBytesTransfered(1L);
            if (this.mProgressListener != null) {
                this.mProgressListener.progressChanged(this.mProgressEvent);
            }
        }
    }
}
