package twitter4j.internal.http;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationContext;

/* loaded from: classes.dex */
public class HttpClientImpl extends HttpClientBase implements Serializable, HttpResponseCode {
    private static final Map<HttpClientConfiguration, HttpClient> instanceMap;
    private static final long serialVersionUID = -8819171414069621503L;

    static {
        if (ConfigurationContext.getInstance().isDalvik()) {
            System.setProperty("http.keepAlive", "false");
        }
        instanceMap = new HashMap(1);
    }

    public HttpClientImpl() {
        super(ConfigurationContext.getInstance());
    }

    public HttpClientImpl(HttpClientConfiguration conf) {
        super(conf);
    }

    public static HttpClient getInstance(HttpClientConfiguration conf) {
        HttpClient client = instanceMap.get(conf);
        if (client == null) {
            HttpClient client2 = new HttpClientImpl(conf);
            instanceMap.put(conf, client2);
            return client2;
        }
        return client;
    }

    public HttpResponse get(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, null, null, null));
    }

    public HttpResponse post(String url, HttpParameter[] params) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, params, null, null));
    }

    @Override // twitter4j.internal.http.HttpClient
    public HttpResponse request(HttpRequest req) throws Throwable {
        int retry = this.CONF.getHttpRetryCount() + 1;
        HttpResponse res = null;
        int retriedCount = 0;
        while (true) {
            HttpResponse res2 = res;
            if (retriedCount >= retry) {
                return res2;
            }
            OutputStream os = null;
            try {
                HttpURLConnection con = getConnection(req.getURL());
                con.setDoInput(true);
                setHeaders(req, con);
                con.setRequestMethod(req.getMethod().name());
                if (req.getMethod() == RequestMethod.POST) {
                    if (HttpParameter.containsFile(req.getParameters())) {
                        String boundary = "----Twitter4J-upload" + System.currentTimeMillis();
                        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                        String boundary2 = "--" + boundary;
                        con.setDoOutput(true);
                        os = con.getOutputStream();
                        DataOutputStream out = new DataOutputStream(os);
                        for (HttpParameter param : req.getParameters()) {
                            if (param.isFile()) {
                                write(out, boundary2 + "\r\n");
                                write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"; filename=\"" + param.getFile().getName() + "\"\r\n");
                                write(out, "Content-Type: " + param.getContentType() + "\r\n\r\n");
                                BufferedInputStream in = new BufferedInputStream(param.hasFileBody() ? param.getFileBody() : new FileInputStream(param.getFile()));
                                byte[] buff = new byte[1024];
                                while (true) {
                                    int length = in.read(buff);
                                    if (length == -1) {
                                        break;
                                    }
                                    out.write(buff, 0, length);
                                }
                                write(out, "\r\n");
                                in.close();
                            } else {
                                write(out, boundary2 + "\r\n");
                                write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"\r\n");
                                write(out, "Content-Type: text/plain; charset=UTF-8\r\n\r\n");
                                out.write(param.getValue().getBytes("UTF-8"));
                                write(out, "\r\n");
                            }
                        }
                        write(out, boundary2 + "--\r\n");
                        write(out, "\r\n");
                    } else {
                        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        String postParam = HttpParameter.encodeParameters(req.getParameters());
                        byte[] bytes = postParam.getBytes("UTF-8");
                        con.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                        con.setDoOutput(true);
                        os = con.getOutputStream();
                        os.write(bytes);
                    }
                    os.flush();
                    os.close();
                }
                res = new HttpResponseImpl(con, this.CONF);
                try {
                    int responseCode = con.getResponseCode();
                    if (responseCode < 200 || (responseCode != 302 && 300 <= responseCode)) {
                        if (responseCode == 420 || responseCode == 400 || responseCode < 500 || retriedCount == this.CONF.getHttpRetryCount()) {
                            break;
                        }
                        try {
                            try {
                                os.close();
                            } catch (IOException ioe) {
                                if (retriedCount == this.CONF.getHttpRetryCount()) {
                                    throw new TwitterException(ioe.getMessage(), ioe, responseCode);
                                }
                            }
                        } catch (Exception e) {
                        }
                        try {
                            Thread.sleep(this.CONF.getHttpRetryIntervalSeconds() * 1000);
                        } catch (InterruptedException e2) {
                        }
                        retriedCount++;
                    } else {
                        try {
                            os.close();
                            return res;
                        } catch (Exception e3) {
                            return res;
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    try {
                        os.close();
                    } catch (Exception e4) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        throw new TwitterException(res.asString(), res);
    }

    private void setHeaders(HttpRequest req, HttpURLConnection connection) {
        String authorizationHeader;
        if (req.getAuthorization() != null && (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req)) != null) {
            connection.addRequestProperty("Authorization", authorizationHeader);
        }
        if (req.getRequestHeaders() != null) {
            for (String key : req.getRequestHeaders().keySet()) {
                connection.addRequestProperty(key, req.getRequestHeaders().get(key));
            }
        }
    }

    protected HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con;
        if (isProxyConfigured()) {
            if (this.CONF.getHttpProxyUser() != null && !this.CONF.getHttpProxyUser().equals("")) {
                Authenticator.setDefault(new Authenticator() { // from class: twitter4j.internal.http.HttpClientImpl.1
                    @Override // java.net.Authenticator
                    protected PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType().equals(Authenticator.RequestorType.PROXY)) {
                            return new PasswordAuthentication(HttpClientImpl.this.CONF.getHttpProxyUser(), HttpClientImpl.this.CONF.getHttpProxyPassword().toCharArray());
                        }
                        return null;
                    }
                });
            }
            Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(this.CONF.getHttpProxyHost(), this.CONF.getHttpProxyPort()));
            con = (HttpURLConnection) new URL(url).openConnection(proxy);
        } else {
            con = (HttpURLConnection) new URL(url).openConnection();
        }
        if (this.CONF.getHttpConnectionTimeout() > 0) {
            con.setConnectTimeout(this.CONF.getHttpConnectionTimeout());
        }
        if (this.CONF.getHttpReadTimeout() > 0) {
            con.setReadTimeout(this.CONF.getHttpReadTimeout());
        }
        con.setInstanceFollowRedirects(false);
        return con;
    }
}
