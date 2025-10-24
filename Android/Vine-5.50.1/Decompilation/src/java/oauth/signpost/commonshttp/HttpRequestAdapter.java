package oauth.signpost.commonshttp;

import java.io.IOException;
import java.io.InputStream;
import oauth.signpost.http.HttpRequest;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpUriRequest;

/* loaded from: classes.dex */
public class HttpRequestAdapter implements HttpRequest {
    private HttpEntity entity;
    private HttpUriRequest request;

    public HttpRequestAdapter(HttpUriRequest request) {
        this.request = request;
        if (request instanceof HttpEntityEnclosingRequest) {
            this.entity = ((HttpEntityEnclosingRequest) request).getEntity();
        }
    }

    @Override // oauth.signpost.http.HttpRequest
    public String getMethod() {
        return this.request.getRequestLine().getMethod();
    }

    @Override // oauth.signpost.http.HttpRequest
    public String getRequestUrl() {
        return this.request.getURI().toString();
    }

    @Override // oauth.signpost.http.HttpRequest
    public String getHeader(String name) {
        Header header = this.request.getFirstHeader(name);
        if (header == null) {
            return null;
        }
        return header.getValue();
    }

    @Override // oauth.signpost.http.HttpRequest
    public void setHeader(String name, String value) {
        this.request.setHeader(name, value);
    }

    @Override // oauth.signpost.http.HttpRequest
    public String getContentType() {
        Header header;
        if (this.entity == null || (header = this.entity.getContentType()) == null) {
            return null;
        }
        return header.getValue();
    }

    @Override // oauth.signpost.http.HttpRequest
    public InputStream getMessagePayload() throws IOException {
        if (this.entity == null) {
            return null;
        }
        return this.entity.getContent();
    }
}
