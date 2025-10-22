package co.vine.android.network.apache;

import android.content.Context;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

/* loaded from: classes.dex */
public class ThreadedHttpOperationClient extends HttpOperationClient {
    private static ThreadedHttpOperationClient sInstance = null;
    private ClientConnectionManager mConnectionManager;
    private final Context mContext;
    private HttpClient mHttpClient;

    private ThreadedHttpOperationClient(Context context) {
        this.mContext = context.getApplicationContext();
        initialize();
    }

    public static synchronized ThreadedHttpOperationClient getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ThreadedHttpOperationClient(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override // co.vine.android.network.apache.HttpOperationClient
    public HttpClient getHttpClient() {
        return this.mHttpClient;
    }

    private void initialize() {
        HttpParams params = initializeHttpParams();
        SchemeRegistry schemeRegistry = initializeSchemeRegistry(this.mContext);
        this.mConnectionManager = initializeConnectionManager(params, schemeRegistry);
        this.mHttpClient = initializeHttpClient(this.mContext, this.mConnectionManager, params);
    }

    private ClientConnectionManager initializeConnectionManager(HttpParams params, SchemeRegistry schemeRegistry) {
        return new ThreadSafeClientConnManager(params, schemeRegistry);
    }
}
