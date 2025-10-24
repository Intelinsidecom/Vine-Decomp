package retrofit.mime;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public interface TypedOutput {
    String fileName();

    long length();

    String mimeType();

    void writeTo(OutputStream outputStream) throws IOException;
}
