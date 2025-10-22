package twitter4j.internal.http;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import twitter4j.auth.Authorization;

/* loaded from: classes.dex */
public final class HttpRequest implements Serializable {
    private static final HttpParameter[] NULL_PARAMETERS = new HttpParameter[0];
    private static final long serialVersionUID = -3463594029098858381L;
    private final Authorization authorization;
    private final RequestMethod method;
    private final HttpParameter[] parameters;
    private Map<String, String> requestHeaders;
    private final String url;

    public HttpRequest(RequestMethod method, String url, HttpParameter[] parameters, Authorization authorization, Map<String, String> requestHeaders) {
        this.method = method;
        if (method != RequestMethod.POST && parameters != null && parameters.length != 0) {
            this.url = url + "?" + HttpParameter.encodeParameters(parameters);
            this.parameters = NULL_PARAMETERS;
        } else {
            this.url = url;
            this.parameters = parameters;
        }
        this.authorization = authorization;
        this.requestHeaders = requestHeaders;
    }

    public RequestMethod getMethod() {
        return this.method;
    }

    public HttpParameter[] getParameters() {
        return this.parameters;
    }

    public String getURL() {
        return this.url;
    }

    public Authorization getAuthorization() {
        return this.authorization;
    }

    public Map<String, String> getRequestHeaders() {
        return this.requestHeaders;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpRequest that = (HttpRequest) o;
        if (this.authorization == null ? that.authorization != null : !this.authorization.equals(that.authorization)) {
            return false;
        }
        if (!Arrays.equals(this.parameters, that.parameters)) {
            return false;
        }
        if (this.requestHeaders == null ? that.requestHeaders != null : !this.requestHeaders.equals(that.requestHeaders)) {
            return false;
        }
        if (this.method == null ? that.method != null : !this.method.equals(that.method)) {
            return false;
        }
        if (this.url != null) {
            if (this.url.equals(that.url)) {
                return true;
            }
        } else if (that.url == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.method != null ? this.method.hashCode() : 0;
        return (((((((result * 31) + (this.url != null ? this.url.hashCode() : 0)) * 31) + (this.parameters != null ? Arrays.hashCode(this.parameters) : 0)) * 31) + (this.authorization != null ? this.authorization.hashCode() : 0)) * 31) + (this.requestHeaders != null ? this.requestHeaders.hashCode() : 0);
    }

    public String toString() {
        return "HttpRequest{requestMethod=" + this.method + ", url='" + this.url + "', postParams=" + (this.parameters == null ? null : Arrays.asList(this.parameters)) + ", authentication=" + this.authorization + ", requestHeaders=" + this.requestHeaders + '}';
    }
}
