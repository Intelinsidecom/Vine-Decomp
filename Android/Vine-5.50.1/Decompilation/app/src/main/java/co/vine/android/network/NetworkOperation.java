package co.vine.android.network;

import co.vine.android.cache.CacheKey;
import co.vine.android.cache.text.TextCache;
import co.vine.android.cache.text.TextKey;
import co.vine.android.cache.text.UrlText;
import co.vine.android.util.ConsoleLoggers;
import co.vine.android.util.FileLogger;
import co.vine.android.util.FileLoggers;
import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public abstract class NetworkOperation<T, K, L> {
    public static long sNetworkDataUsed;
    public static long sSavedDataSize;
    public Exception exception;
    private TextCache<?> mCache;
    private TextKey mCacheKey;
    protected final K mHttpClient;
    protected final T mHttpRequestBuilder;
    private NetworkOperationResult mOperationResult;
    protected final NetworkOperationReader mReader;
    protected HttpResult[] mResults;
    private UrlCachePolicy mUrlCachePolicy;
    public int statusCode;
    public String statusPhrase;
    public String uploadKey;
    protected static final SLogger sLogger = ConsoleLoggers.NETWORK.get();
    protected static final FileLogger sFileLogger = FileLoggers.NETWORK.get();
    public static final AtomicLong sTimeOffset = new AtomicLong(0);
    public static double RATE_LIMIT_BYTES_PER_MS = -1.0d;
    public static final String USER_AGENT_STRING = System.getProperty("http.agent");
    protected static final HttpResult[] EMPTY_RESULTS = new HttpResult[0];
    protected static final NetworkOperationReader DEFAULT_READER = new DefaultHttpOperationReader();

    public interface CancelableRequest {
        void cancel();

        boolean isCancelled();
    }

    public enum NetworkOperationResult {
        CACHED,
        NETWORK,
        FAILURE
    }

    public abstract void addHeader(String str, String str2);

    protected abstract L buildRequest();

    protected abstract ExecuteResult executeRequest(L l, int i, int i2, UrlCachePolicy urlCachePolicy) throws Exception;

    public abstract CancelableRequest getCancelableRequest();

    public NetworkOperation<T, K, L> execute() {
        return execute(0);
    }

    public NetworkOperationReader getReader() {
        return this.mReader;
    }

    protected NetworkOperation(NetworkOperationReader reader, T httpRequestBuilder, K httpClient, HeaderInjecter injecter) {
        this.mHttpRequestBuilder = httpRequestBuilder;
        this.mHttpClient = httpClient;
        addHeader("Accept-Encoding", "gzip");
        addHeader("User-Agent", USER_AGENT_STRING);
        if (injecter != null) {
            injecter.addClientHeaders(this);
        }
        if (reader == null) {
            this.mReader = DEFAULT_READER;
        } else {
            this.mReader = reader;
        }
    }

    protected void prepareRequest(long timeOffset) {
    }

    public NetworkOperationResult getLastExecuteResult() {
        return this.mOperationResult;
    }

    public boolean isOK() {
        return this.statusCode == 200;
    }

    public NetworkOperation<T, K, L> execute(int retries) {
        NetworkOperationResult networkOperationResult;
        UrlCachePolicy cachePolicy = getCachePolicy();
        boolean canUseCache = this.mCache != null && cachePolicy.mCachedResponseAllowed;
        boolean hasFetched = false;
        if (canUseCache && cachePolicy.mCacheTakesPriority) {
            hasFetched = fetchTextCacheResponse(cachePolicy, true);
        }
        boolean wasCachedDataUsed = hasFetched;
        if (!hasFetched && cachePolicy.mNetworkDataAllowed) {
            hasFetched = fetchHttpResponse(retries, cachePolicy);
        }
        if (!hasFetched && canUseCache) {
            hasFetched = fetchTextCacheResponse(cachePolicy, cachePolicy.mUseExpiredDataAllowedIfNetworkIsDown ? false : true);
            wasCachedDataUsed = hasFetched;
        }
        if (hasFetched) {
            networkOperationResult = wasCachedDataUsed ? NetworkOperationResult.CACHED : NetworkOperationResult.NETWORK;
        } else {
            networkOperationResult = NetworkOperationResult.FAILURE;
        }
        this.mOperationResult = networkOperationResult;
        return this;
    }

    private boolean fetchHttpResponse(int retries, UrlCachePolicy cachePolicy) {
        ArrayList<HttpResult> results = new ArrayList<>(Math.max(retries, 0) + 1);
        int authRetry = 1;
        do {
            long currentTimeOffset = sTimeOffset.get();
            prepareRequest(currentTimeOffset);
            try {
                ExecuteResult result = executeRequest(buildRequest(), authRetry, retries, cachePolicy);
                authRetry = result.mAuthRetries;
                int retries2 = result.mRetries;
                results.add(result.mResult);
                if (result.mResult.statusCode != 200) {
                    this.mReader.onHandleError(result.mResult);
                }
                if (sFileLogger != null && this.mCacheKey != null) {
                    sFileLogger.write(null, "Remote {} {} ", this.mCacheKey.url, Integer.valueOf(this.statusCode));
                }
                this.statusCode = result.mResult.statusCode;
                this.statusPhrase = result.mResult.reasonPhrase;
                this.exception = result.mResult.exception;
                this.uploadKey = result.mResult.uploadKey;
                retries = retries2 - 1;
            } catch (Exception e) {
                String url = this.mCacheKey != null ? this.mCacheKey.url : null;
                String error = MessageFormatter.toStringMessage("Request failed: {} ({})", url, e.getMessage());
                sFileLogger.write(null, error, new Object[0]);
            }
        } while (retries >= 0);
        this.mResults = new HttpResult[results.size()];
        results.toArray(this.mResults);
        return this.statusCode == 200;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected boolean fetchTextCacheResponse(UrlCachePolicy cachePolicy, boolean checkExpired) {
        boolean expired;
        sLogger.d("Peek from cached {}", this.mCacheKey.url);
        sLogger.timingStart();
        UrlText data = this.mCache.get(this.mCacheKey);
        sLogger.timingStop();
        boolean valid = (data == null || data.value == 0) ? false : true;
        if (valid) {
            expired = cachePolicy.mIfReachableFetchMaxStaleTimeMs > 0 && System.currentTimeMillis() - data.cacheTime > cachePolicy.mIfReachableFetchMaxStaleTimeMs;
            if (expired) {
                sLogger.d("Cache has expired, it was loaded {}min ago.", Double.valueOf((System.currentTimeMillis() - data.cacheTime) / 60000.0d));
                this.mCacheKey.setCacheState(CacheKey.CacheState.EXPIRED);
            } else {
                this.mCacheKey.setCacheState(CacheKey.CacheState.DISK);
                sLogger.d("Valid cache found.");
            }
        } else {
            expired = false;
        }
        if (sFileLogger != null) {
            sFileLogger.write(null, "{}\n{}\n", this.mCacheKey.url, this.mCacheKey.getCacheState());
        }
        if (valid && (!checkExpired || !expired)) {
            this.statusCode = HttpResponseCode.OK;
            this.mResults = new HttpResult[]{new HttpResult(HttpResponseCode.OK, null)};
            try {
                sSavedDataSize = ((byte[]) data.value).length + sSavedDataSize;
                this.mReader.readInput(this.statusCode, ((byte[]) data.value).length, new ByteArrayInputStream((byte[]) data.value));
                return true;
            } catch (Exception e) {
                SLog.e("Failed to read from cache.", (Throwable) e);
            }
        } else {
            this.mCacheKey.setCacheState(CacheKey.CacheState.NOT_CACHED);
        }
        return false;
    }

    public void setCachePolicy(UrlCachePolicy urlCachePolicy) {
        this.mUrlCachePolicy = urlCachePolicy;
    }

    protected void setCacheStorage(TextCache cacheStorage) {
        this.mCache = cacheStorage;
    }

    protected void setCacheKey(TextKey textKey) {
        this.mCacheKey = textKey;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onRemoteInputStreamReady(InputStream in, UrlCachePolicy cachePolicy, int statusCode, long contentLength) throws IOException {
        sNetworkDataUsed += contentLength;
        if (statusCode == 200 && cachePolicy.mShouldCacheResponse) {
            sLogger.timingStart();
            UrlText data = this.mCache.save(this.mCacheKey, in);
            in = new ByteArrayInputStream((byte[]) data.value);
            sLogger.timingStop();
            sFileLogger.write(sLogger, "{} cached.", this.mCacheKey.url);
        }
        this.mReader.readInput(statusCode, contentLength, in);
    }

    public UrlCachePolicy getCachePolicy() {
        return this.mUrlCachePolicy == null ? UrlCachePolicy.NOT_CACHABLE : this.mUrlCachePolicy;
    }

    protected static class ExecuteResult {
        public final int mAuthRetries;
        public final HttpResult mResult;
        public final int mRetries;

        public ExecuteResult(HttpResult result, int retries, int authRetries) {
            this.mResult = result;
            this.mRetries = retries;
            this.mAuthRetries = authRetries;
        }
    }
}
