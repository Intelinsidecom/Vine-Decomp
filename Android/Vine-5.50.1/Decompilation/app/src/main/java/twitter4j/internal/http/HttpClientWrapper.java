package twitter4j.internal.http;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import twitter4j.TwitterException;
import twitter4j.auth.Authorization;
import twitter4j.conf.ConfigurationContext;

/* loaded from: classes.dex */
public final class HttpClientWrapper implements Serializable {
    private static final long serialVersionUID = -6511977105603119379L;
    private HttpClient http;
    private HttpResponseListener httpResponseListener;
    private final Map<String, String> requestHeaders;
    private final HttpClientWrapperConfiguration wrapperConf;

    public HttpClientWrapper(HttpClientWrapperConfiguration wrapperConf) {
        this.wrapperConf = wrapperConf;
        this.requestHeaders = wrapperConf.getRequestHeaders();
        this.http = HttpClientFactory.getInstance(wrapperConf);
    }

    public HttpClientWrapper() {
        this.wrapperConf = ConfigurationContext.getInstance();
        this.requestHeaders = this.wrapperConf.getRequestHeaders();
        this.http = HttpClientFactory.getInstance(this.wrapperConf);
    }

    public void shutdown() {
        this.http.shutdown();
    }

    private HttpResponse request(HttpRequest req) throws TwitterException {
        try {
            HttpResponse res = this.http.request(req);
            if (this.httpResponseListener != null) {
                this.httpResponseListener.httpResponseReceived(new HttpResponseEvent(req, res, null));
            }
            return res;
        } catch (TwitterException te) {
            if (this.httpResponseListener != null) {
                this.httpResponseListener.httpResponseReceived(new HttpResponseEvent(req, null, te));
            }
            throw te;
        }
    }

    public void setHttpResponseListener(HttpResponseListener listener) {
        this.httpResponseListener = listener;
    }

    public HttpResponse get(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse get(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse get(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse get(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, null, null, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters, Map<String, String> requestHeaders) throws TwitterException {
        Map<String, String> headers = new HashMap<>(this.requestHeaders);
        if (requestHeaders != null) {
            headers.putAll(requestHeaders);
        }
        return request(new HttpRequest(RequestMethod.POST, url, parameters, null, headers));
    }

    public HttpResponse post(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse post(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, null, null, this.requestHeaders));
    }

    public HttpResponse delete(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse delete(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse delete(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse delete(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, null, null, this.requestHeaders));
    }

    public HttpResponse head(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse head(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse head(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse head(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, null, null, this.requestHeaders));
    }

    public HttpResponse put(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse put(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, parameters, null, this.requestHeaders));
    }

    public HttpResponse put(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, null, authorization, this.requestHeaders));
    }

    public HttpResponse put(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, null, null, this.requestHeaders));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpClientWrapper that = (HttpClientWrapper) o;
        return this.http.equals(that.http) && this.requestHeaders.equals(that.requestHeaders) && this.wrapperConf.equals(that.wrapperConf);
    }

    public int hashCode() {
        int result = this.wrapperConf.hashCode();
        return (((result * 31) + this.http.hashCode()) * 31) + this.requestHeaders.hashCode();
    }

    public String toString() {
        return "HttpClientWrapper{wrapperConf=" + this.wrapperConf + ", http=" + this.http + ", requestHeaders=" + this.requestHeaders + ", httpResponseListener=" + this.httpResponseListener + '}';
    }
}
