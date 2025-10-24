package twitter4j.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/* loaded from: classes.dex */
final class StreamingGZIPInputStream extends GZIPInputStream {
    private final InputStream wrapped;

    public StreamingGZIPInputStream(InputStream is) throws IOException {
        super(is);
        this.wrapped = is;
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        return this.wrapped.available();
    }
}
