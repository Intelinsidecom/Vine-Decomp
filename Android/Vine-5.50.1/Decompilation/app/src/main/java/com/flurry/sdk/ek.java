package com.flurry.sdk;

import android.annotation.TargetApi;
import android.os.Build;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

/* loaded from: classes.dex */
public class ek extends fd {
    private static final String a = ek.class.getSimpleName();
    private static SSLContext b;
    private static HostnameVerifier c;
    private String d;
    private a e;
    private c l;
    private HttpURLConnection m;
    private HttpClient n;
    private boolean o;
    private boolean p;
    private Exception q;
    private int f = 10000;
    private int i = 15000;
    private boolean j = true;
    private final dp<String, String> k = new dp<>();
    private int r = -1;
    private final dp<String, String> s = new dp<>();
    private final Object t = new Object();

    public interface c {
        void a(ek ekVar);

        void a(ek ekVar, InputStream inputStream) throws Exception;

        void a(ek ekVar, OutputStream outputStream) throws Exception;
    }

    public static class b implements c {
        @Override // com.flurry.sdk.ek.c
        public void a(ek ekVar, OutputStream outputStream) throws Exception {
        }

        @Override // com.flurry.sdk.ek.c
        public void a(ek ekVar, InputStream inputStream) throws Exception {
        }

        @Override // com.flurry.sdk.ek.c
        public void a(ek ekVar) {
        }
    }

    private static synchronized SSLContext m() {
        SSLContext sSLContext;
        if (b != null) {
            sSLContext = b;
        } else {
            try {
                TrustManager[] trustManagerArr = {new eg(null)};
                b = SSLContext.getInstance("TLS");
                b.init(null, trustManagerArr, new SecureRandom());
            } catch (Exception e) {
                el.a(3, a, "Exception creating SSL context", e);
            }
            sSLContext = b;
        }
        return sSLContext;
    }

    private static synchronized HostnameVerifier n() {
        HostnameVerifier hostnameVerifier;
        if (c != null) {
            hostnameVerifier = c;
        } else {
            c = new ee();
            hostnameVerifier = c;
        }
        return hostnameVerifier;
    }

    public enum a {
        kUnknown,
        kGet,
        kPost,
        kPut,
        kDelete,
        kHead;

        @Override // java.lang.Enum
        public String toString() {
            switch (this) {
                case kPost:
                    return "POST";
                case kPut:
                    return "PUT";
                case kDelete:
                    return "DELETE";
                case kHead:
                    return "HEAD";
                case kGet:
                    return "GET";
                default:
                    return null;
            }
        }

        public HttpRequestBase a(String str) {
            switch (this) {
                case kPost:
                    return new HttpPost(str);
                case kPut:
                    return new HttpPut(str);
                case kDelete:
                    return new HttpDelete(str);
                case kHead:
                    return new HttpHead(str);
                case kGet:
                    return new HttpGet(str);
                default:
                    return null;
            }
        }
    }

    public void a(String str) {
        this.d = str;
    }

    public void a(a aVar) {
        this.e = aVar;
    }

    public void a(boolean z) {
        this.j = z;
    }

    public void a(String str, String str2) {
        this.k.a((dp<String, String>) str, str2);
    }

    public void a(c cVar) {
        this.l = cVar;
    }

    public boolean b() {
        boolean z;
        synchronized (this.t) {
            z = this.p;
        }
        return z;
    }

    public boolean c() {
        return !f() && d();
    }

    public boolean d() {
        return this.r >= 200 && this.r < 400;
    }

    public int e() {
        return this.r;
    }

    public boolean f() {
        return this.q != null;
    }

    public List<String> b(String str) {
        if (str == null) {
            return null;
        }
        return this.s.a((dp<String, String>) str);
    }

    public void g() {
        synchronized (this.t) {
            this.p = true;
        }
        s();
    }

    @Override // com.flurry.sdk.fc
    public void a() {
        try {
            if (this.d == null) {
                return;
            }
            if (!es.a().c()) {
                el.a(3, a, "Network not available, aborting http request: " + this.d);
                return;
            }
            if (this.e == null || a.kUnknown.equals(this.e)) {
                this.e = a.kGet;
            }
            if (Build.VERSION.SDK_INT >= 9) {
                o();
            } else {
                p();
            }
            el.a(4, a, "HTTP status: " + this.r + " for url: " + this.d);
        } catch (Exception e) {
            el.a(4, a, "HTTP status: " + this.r + " for url: " + this.d);
            el.a(3, a, "Exception during http request: " + this.d, e);
            this.q = e;
        } finally {
            q();
        }
    }

    @Override // com.flurry.sdk.fd
    public void h() {
        g();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void o() throws Exception {
        BufferedOutputStream bufferedOutputStream;
        InputStream inputStream;
        BufferedInputStream bufferedInputStream = null;
        if (this.p) {
            return;
        }
        try {
            this.m = (HttpURLConnection) new URL(this.d).openConnection();
            this.m.setConnectTimeout(this.f);
            this.m.setReadTimeout(this.i);
            this.m.setRequestMethod(this.e.toString());
            this.m.setInstanceFollowRedirects(this.j);
            this.m.setDoOutput(a.kPost.equals(this.e));
            this.m.setDoInput(true);
            if (el.d() && (this.m instanceof HttpsURLConnection)) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) this.m;
                httpsURLConnection.setHostnameVerifier(n());
                httpsURLConnection.setSSLSocketFactory(m().getSocketFactory());
            }
            for (Map.Entry<String, String> entry : this.k.b()) {
                this.m.addRequestProperty(entry.getKey(), entry.getValue());
            }
            if (!a.kGet.equals(this.e) && !a.kPost.equals(this.e)) {
                this.m.setRequestProperty("Accept-Encoding", "");
            }
            if (this.p) {
                return;
            }
            if (a.kPost.equals(this.e)) {
                try {
                    OutputStream outputStream = this.m.getOutputStream();
                    try {
                        bufferedOutputStream = new BufferedOutputStream(outputStream);
                        try {
                            a(bufferedOutputStream);
                            fb.a(bufferedOutputStream);
                            fb.a(outputStream);
                        } catch (Throwable th) {
                            th = th;
                            bufferedInputStream = outputStream;
                            fb.a(bufferedOutputStream);
                            fb.a(bufferedInputStream);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedOutputStream = null;
                        bufferedInputStream = outputStream;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bufferedOutputStream = null;
                }
            }
            this.r = this.m.getResponseCode();
            for (Map.Entry<String, List<String>> entry2 : this.m.getHeaderFields().entrySet()) {
                Iterator<String> it = entry2.getValue().iterator();
                while (it.hasNext()) {
                    this.s.a((dp<String, String>) entry2.getKey(), it.next());
                }
            }
            if (!a.kGet.equals(this.e) && !a.kPost.equals(this.e)) {
                return;
            }
            if (this.p) {
                return;
            }
            try {
                InputStream inputStream2 = this.m.getInputStream();
                try {
                    BufferedInputStream bufferedInputStream2 = new BufferedInputStream(inputStream2);
                    try {
                        a(bufferedInputStream2);
                        fb.a(bufferedInputStream2);
                        fb.a(inputStream2);
                    } catch (Throwable th4) {
                        th = th4;
                        bufferedInputStream = bufferedInputStream2;
                        inputStream = inputStream2;
                        fb.a(bufferedInputStream);
                        fb.a(inputStream);
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    inputStream = inputStream2;
                }
            } catch (Throwable th6) {
                th = th6;
                inputStream = null;
            }
        } finally {
            r();
        }
    }

    private void p() throws Exception {
        BufferedInputStream bufferedInputStream;
        InputStream inputStream = null;
        if (!this.p) {
            HttpRequestBase httpRequestBaseA = this.e.a(this.d);
            for (Map.Entry<String, String> entry : this.k.b()) {
                httpRequestBaseA.setHeader(entry.getKey(), entry.getValue());
            }
            if (!a.kGet.equals(this.e) && !a.kPost.equals(this.e)) {
                httpRequestBaseA.removeHeaders("Accept-Encoding");
            }
            if (a.kPost.equals(this.e)) {
                ((HttpPost) httpRequestBaseA).setEntity(new AbstractHttpEntity() { // from class: com.flurry.sdk.ek.1
                    @Override // org.apache.http.HttpEntity
                    public boolean isRepeatable() {
                        return false;
                    }

                    @Override // org.apache.http.HttpEntity
                    public long getContentLength() {
                        return -1L;
                    }

                    @Override // org.apache.http.HttpEntity
                    public boolean isStreaming() {
                        return false;
                    }

                    @Override // org.apache.http.HttpEntity
                    public InputStream getContent() throws IOException {
                        throw new UnsupportedOperationException();
                    }

                    @Override // org.apache.http.HttpEntity
                    @TargetApi(9)
                    public void writeTo(OutputStream outputStream) throws Throwable {
                        BufferedOutputStream bufferedOutputStream;
                        try {
                            try {
                                bufferedOutputStream = new BufferedOutputStream(outputStream);
                            } catch (IOException e) {
                                throw e;
                            } catch (Exception e2) {
                                e = e2;
                            } catch (Throwable th) {
                                th = th;
                                fb.a((Closeable) null);
                                throw th;
                            }
                            try {
                                ek.this.a(bufferedOutputStream);
                                fb.a(bufferedOutputStream);
                            } catch (IOException e3) {
                            } catch (Exception e4) {
                                e = e4;
                                if (Build.VERSION.SDK_INT >= 9) {
                                    throw new IOException(e);
                                }
                                throw new IOException(e.toString());
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            fb.a((Closeable) null);
                            throw th;
                        }
                    }
                });
            }
            try {
                BasicHttpParams basicHttpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(basicHttpParams, this.f);
                HttpConnectionParams.setSoTimeout(basicHttpParams, this.i);
                basicHttpParams.setParameter("http.protocol.handle-redirects", Boolean.valueOf(this.j));
                this.n = eh.a(basicHttpParams);
                HttpResponse httpResponseExecute = this.n.execute(httpRequestBaseA);
                if (this.p) {
                    throw new Exception("Request cancelled");
                }
                if (httpResponseExecute != null) {
                    this.r = httpResponseExecute.getStatusLine().getStatusCode();
                    Header[] allHeaders = httpResponseExecute.getAllHeaders();
                    if (allHeaders != null) {
                        for (Header header : allHeaders) {
                            HeaderElement[] elements = header.getElements();
                            for (HeaderElement headerElement : elements) {
                                this.s.a((dp<String, String>) headerElement.getName(), headerElement.getValue());
                            }
                        }
                    }
                    if (a.kGet.equals(this.e) || a.kPost.equals(this.e)) {
                        if (this.p) {
                            throw new Exception("Request cancelled");
                        }
                        HttpEntity entity = httpResponseExecute.getEntity();
                        if (entity != null) {
                            try {
                                InputStream content = entity.getContent();
                                try {
                                    bufferedInputStream = new BufferedInputStream(content);
                                    try {
                                        a(bufferedInputStream);
                                        fb.a(bufferedInputStream);
                                        fb.a(content);
                                    } catch (Throwable th) {
                                        th = th;
                                        inputStream = content;
                                        fb.a(bufferedInputStream);
                                        fb.a(inputStream);
                                        throw th;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    bufferedInputStream = null;
                                    inputStream = content;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                bufferedInputStream = null;
                            }
                        }
                    }
                }
            } finally {
                r();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(OutputStream outputStream) throws Exception {
        if (this.l != null && !b() && outputStream != null) {
            this.l.a(this, outputStream);
        }
    }

    private void a(InputStream inputStream) throws Exception {
        if (this.l != null && !b() && inputStream != null) {
            this.l.a(this, inputStream);
        }
    }

    private void q() {
        if (this.l != null && !b()) {
            this.l.a(this);
        }
    }

    private void r() {
        if (!this.o) {
            this.o = true;
            if (this.m != null) {
                this.m.disconnect();
            }
            if (this.n != null) {
                this.n.getConnectionManager().shutdown();
            }
        }
    }

    private void s() {
        if (!this.o) {
            this.o = true;
            if (this.m != null || this.n != null) {
                new Thread() { // from class: com.flurry.sdk.ek.2
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        if (ek.this.m != null) {
                            ek.this.m.disconnect();
                        }
                        if (ek.this.n != null) {
                            ek.this.n.getConnectionManager().shutdown();
                        }
                    }
                }.start();
            }
        }
    }
}
