package co.vine.android.network.apache;

import android.content.Context;
import android.content.SharedPreferences;
import co.vine.android.util.CommonUtil;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/* loaded from: classes.dex */
public abstract class HttpOperationClient {
    public abstract HttpClient getHttpClient();

    protected SchemeRegistry initializeSchemeRegistry(Context context) {
        try {
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", new PinningSSLSocketFactory(context), 443));
            schemeRegistry.register(new Scheme("https+blob", new PinningSSLSocketFactory(context), 443));
            return schemeRegistry;
        } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new AssertionError(e);
        }
    }

    protected HttpParams initializeHttpParams() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(httpParams, 90000);
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        return httpParams;
    }

    private HttpProxyInfo getProxyInfo(Context context) {
        SharedPreferences preferences = CommonUtil.getDefaultSharedPrefs(context);
        return new HttpProxyInfo(preferences.getBoolean("proxy_enabled", false), preferences.getString("proxy_host", ""), preferences.getString("proxy_port", ""));
    }

    protected HttpClient initializeHttpClient(Context context, ClientConnectionManager connectionManager, HttpParams httpParams) {
        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager, httpParams);
        httpClient.removeRequestInterceptorByClass(RequestAddCookies.class);
        httpClient.removeResponseInterceptorByClass(ResponseProcessCookies.class);
        httpClient.setKeepAliveStrategy(new ShortKeepAliveStrategy());
        HttpProxyInfo proxySettings = getProxyInfo(context);
        if (proxySettings.enabled) {
            httpClient.getParams().setParameter("http.route.default-proxy", proxySettings.httpHost);
        }
        return httpClient;
    }

    private static class HttpProxyInfo {
        public final boolean enabled;
        public final HttpHost httpHost;

        public HttpProxyInfo(boolean enabled, String host, String portString) throws NumberFormatException {
            int port;
            String host2 = host.trim();
            enabled = host2.length() == 0 ? false : enabled;
            try {
                port = Integer.parseInt(portString);
            } catch (NumberFormatException e) {
                port = -1;
                enabled = false;
            }
            this.httpHost = new HttpHost(host2, port, "http");
            this.enabled = enabled;
        }
    }
}
