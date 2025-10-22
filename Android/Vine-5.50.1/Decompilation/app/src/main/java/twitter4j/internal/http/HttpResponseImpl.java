package twitter4j.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class HttpResponseImpl extends HttpResponse {
    private HttpURLConnection con;

    HttpResponseImpl(HttpURLConnection con, HttpClientConfiguration conf) throws IOException {
        super(conf);
        this.con = con;
        this.statusCode = con.getResponseCode();
        InputStream errorStream = con.getErrorStream();
        this.is = errorStream;
        if (errorStream == null) {
            this.is = con.getInputStream();
        }
        if (this.is != null && "gzip".equals(con.getContentEncoding())) {
            this.is = new StreamingGZIPInputStream(this.is);
        }
    }

    HttpResponseImpl(String content) {
        this.responseAsString = content;
    }

    @Override // twitter4j.internal.http.HttpResponse
    public String getResponseHeader(String name) {
        return this.con.getHeaderField(name);
    }

    @Override // twitter4j.internal.http.HttpResponse
    public Map<String, List<String>> getResponseHeaderFields() {
        return this.con.getHeaderFields();
    }

    @Override // twitter4j.internal.http.HttpResponse
    public void disconnect() {
        this.con.disconnect();
    }
}
