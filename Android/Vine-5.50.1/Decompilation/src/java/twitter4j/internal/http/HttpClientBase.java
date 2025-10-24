package twitter4j.internal.http;

import android.util.Log;
import com.edisonwang.android.slog.SLog;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/* loaded from: classes.dex */
public abstract class HttpClientBase implements Serializable, HttpClient {
    private static final String TAG = "t4jHttpClientBase";
    private static final long serialVersionUID = 6944924907755685265L;
    protected final HttpClientConfiguration CONF;

    public HttpClientBase(HttpClientConfiguration conf) {
        this.CONF = conf;
    }

    @Override // twitter4j.internal.http.HttpClient
    public void shutdown() {
    }

    protected boolean isProxyConfigured() {
        return (this.CONF.getHttpProxyHost() == null || this.CONF.getHttpProxyHost().equals("")) ? false : true;
    }

    public void write(DataOutputStream out, String outStr) throws IOException {
        out.writeBytes(outStr);
        if (SLog.sLogsOn) {
            Log.v(TAG, outStr);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpClientBase)) {
            return false;
        }
        HttpClientBase that = (HttpClientBase) o;
        return this.CONF.equals(that.CONF);
    }

    public int hashCode() {
        return this.CONF.hashCode();
    }

    public String toString() {
        return "HttpClientBase{CONF=" + this.CONF + '}';
    }
}
