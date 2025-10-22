package org.scribe.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.exceptions.OAuthException;

/* loaded from: classes.dex */
public class Request {
    private static RequestTuner NOOP = new RequestTuner() { // from class: org.scribe.model.Request.1
        @Override // org.scribe.model.RequestTuner
        public void tune(Request request) {
        }
    };
    private String charset;
    private HttpURLConnection connection;
    private String url;
    private Verb verb;
    private String payload = null;
    private byte[] bytePayload = null;
    private boolean connectionKeepAlive = false;
    private boolean followRedirects = true;
    private Long connectTimeout = null;
    private Long readTimeout = null;
    private ParameterList querystringParams = new ParameterList();
    private ParameterList bodyParams = new ParameterList();
    private Map<String, String> headers = new HashMap();

    public Request(Verb verb, String url) {
        this.verb = verb;
        this.url = url;
    }

    public Response send(RequestTuner tuner) {
        try {
            createConnection();
            return doSend(tuner);
        } catch (Exception e) {
            throw new OAuthConnectionException(e);
        }
    }

    public Response send() {
        return send(NOOP);
    }

    private void createConnection() throws IOException {
        String completeUrl = getCompleteUrl();
        if (this.connection == null) {
            System.setProperty("http.keepAlive", this.connectionKeepAlive ? "true" : "false");
            this.connection = (HttpURLConnection) new URL(completeUrl).openConnection();
            this.connection.setInstanceFollowRedirects(this.followRedirects);
        }
    }

    public String getCompleteUrl() {
        return this.querystringParams.appendTo(this.url);
    }

    Response doSend(RequestTuner tuner) throws IOException {
        this.connection.setRequestMethod(this.verb.name());
        if (this.connectTimeout != null) {
            this.connection.setConnectTimeout(this.connectTimeout.intValue());
        }
        if (this.readTimeout != null) {
            this.connection.setReadTimeout(this.readTimeout.intValue());
        }
        addHeaders(this.connection);
        if (this.verb.equals(Verb.PUT) || this.verb.equals(Verb.POST)) {
            addBody(this.connection, getByteBodyContents());
        }
        tuner.tune(this);
        return new Response(this.connection);
    }

    void addHeaders(HttpURLConnection conn) {
        for (String key : this.headers.keySet()) {
            conn.setRequestProperty(key, this.headers.get(key));
        }
    }

    void addBody(HttpURLConnection conn, byte[] content) throws IOException {
        conn.setRequestProperty("Content-Length", String.valueOf(content.length));
        if (conn.getRequestProperty("Content-Type") == null) {
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }
        conn.setDoOutput(true);
        conn.getOutputStream().write(content);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void addQuerystringParameter(String key, String value) {
        this.querystringParams.add(key, value);
    }

    public ParameterList getQueryStringParams() {
        try {
            ParameterList result = new ParameterList();
            String queryString = new URL(this.url).getQuery();
            result.addQuerystring(queryString);
            result.addAll(this.querystringParams);
            return result;
        } catch (MalformedURLException mue) {
            throw new OAuthException("Malformed URL", mue);
        }
    }

    public ParameterList getBodyParams() {
        return this.bodyParams;
    }

    public String getUrl() {
        return this.url;
    }

    public String getSanitizedUrl() {
        return this.url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");
    }

    byte[] getByteBodyContents() {
        if (this.bytePayload != null) {
            return this.bytePayload;
        }
        String body = this.payload != null ? this.payload : this.bodyParams.asFormUrlEncodedString();
        try {
            return body.getBytes(getCharset());
        } catch (UnsupportedEncodingException uee) {
            throw new OAuthException("Unsupported Charset: " + getCharset(), uee);
        }
    }

    public Verb getVerb() {
        return this.verb;
    }

    public String getCharset() {
        return this.charset == null ? Charset.defaultCharset().name() : this.charset;
    }

    public String toString() {
        return String.format("@Request(%s %s)", getVerb(), getUrl());
    }
}
