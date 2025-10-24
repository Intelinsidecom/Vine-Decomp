package retrofit.mime;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public interface TypedInput {
    InputStream in() throws IOException;

    long length();

    String mimeType();
}
