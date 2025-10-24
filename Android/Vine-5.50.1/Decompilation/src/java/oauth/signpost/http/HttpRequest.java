package oauth.signpost.http;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public interface HttpRequest {
    String getContentType();

    String getHeader(String str);

    InputStream getMessagePayload() throws IOException;

    String getMethod();

    String getRequestUrl();

    void setHeader(String str, String str2);
}
