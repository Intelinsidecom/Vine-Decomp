package co.vine.android.network.apache;

import co.vine.android.cache.text.TextKey;
import co.vine.android.client.VineAPI;
import co.vine.android.network.HttpResult;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.MeasureInputStream;
import co.vine.android.util.analytics.FlurryUtils;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class HttpOperation extends NetworkOperation<HttpRequestBase, HttpClient, HttpRequestBase> {
    private static int sRequestCount;
    private final HttpRequestBase mHttpRequest;

    public static int getRequestCount() {
        return sRequestCount;
    }

    public static void resetRequestCount() {
        sLogger.i("HttpRequest count reseted.");
        sRequestCount = 0;
    }

    public HttpOperation(HttpOperationClient client, HttpRequestBase httpReq, NetworkOperationReader reader, VineAPI api) {
        super(reader, httpReq, client.getHttpClient(), api);
        if (api != null) {
            setCacheStorage(api.getCacheStorage());
            setCacheKey(new TextKey(httpReq.getURI().toString()));
        }
        this.mHttpRequest = httpReq;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // co.vine.android.network.NetworkOperation
    public void addHeader(String name, String value) {
        ((HttpRequestBase) this.mHttpRequestBuilder).addHeader(name, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.network.NetworkOperation
    public HttpRequestBase buildRequest() {
        return this.mHttpRequest;
    }

    private static class HttpCancelableRequest implements NetworkOperation.CancelableRequest {
        private final HttpRequestBase request;

        public HttpCancelableRequest(HttpRequestBase request) {
            this.request = request;
        }

        @Override // co.vine.android.network.NetworkOperation.CancelableRequest
        public void cancel() {
            this.request.abort();
        }

        @Override // co.vine.android.network.NetworkOperation.CancelableRequest
        public boolean isCancelled() {
            return this.request.isAborted();
        }
    }

    @Override // co.vine.android.network.NetworkOperation
    public NetworkOperation.CancelableRequest getCancelableRequest() {
        return new HttpCancelableRequest(this.mHttpRequest);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.network.NetworkOperation
    public NetworkOperation.ExecuteResult executeRequest(HttpRequestBase httpReq, int authRetry, int retries, UrlCachePolicy cachePolicy) throws Exception {
        String contentEncoding;
        String contentType;
        String uploadKey = null;
        long readTime = 0;
        long closeTime = 0;
        if (SLog.sLogsOn) {
            sLogger.d(httpReq.getMethod() + " " + httpReq.getURI());
        }
        sRequestCount++;
        long before = System.currentTimeMillis();
        try {
            try {
                HttpResponse resp = ((HttpClient) this.mHttpClient).execute(httpReq);
                StatusLine statusLine = resp.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                long firstByte = System.currentTimeMillis() - before;
                String reasonPhrase = statusLine.getReasonPhrase();
                if (sLogger.isActive()) {
                    sLogger.d(statusCode + "/" + reasonPhrase + " [" + httpReq.getURI() + "]");
                }
                switch (statusCode) {
                    case HttpResponseCode.OK /* 200 */:
                        Header[] headers = resp.getHeaders("X-Upload-Key");
                        if (headers != null && headers.length > 0) {
                            uploadKey = headers[0].getValue();
                            break;
                        }
                        break;
                    case HttpResponseCode.UNAUTHORIZED /* 401 */:
                        Header dateHeader = resp.getLastHeader("Date");
                        if (dateHeader != null) {
                            Date d = CommonUtil.DATE_TIME_RFC1123.parse(dateHeader.getValue());
                            long ts = d.getTime();
                            long now = System.currentTimeMillis();
                            sTimeOffset.set(ts - now);
                        }
                        if (authRetry > 0) {
                            authRetry--;
                            retries++;
                            break;
                        }
                        break;
                    case HttpResponseCode.INTERNAL_SERVER_ERROR /* 500 */:
                    case HttpResponseCode.BAD_GATEWAY /* 502 */:
                    case HttpResponseCode.SERVICE_UNAVAILABLE /* 503 */:
                        break;
                    default:
                        retries = 0;
                        break;
                }
                HttpEntity entity = resp.getEntity();
                long openTime = System.currentTimeMillis() - before;
                if (entity != null && entity.isStreaming()) {
                    if (entity.getContentEncoding() != null) {
                        contentEncoding = entity.getContentEncoding().getValue();
                    } else {
                        contentEncoding = null;
                    }
                    Header entityContentType = entity.getContentType();
                    if (entityContentType != null) {
                        contentType = entityContentType.getValue();
                    } else {
                        contentType = null;
                    }
                    int contentLength = (int) entity.getContentLength();
                    if (sLogger.isActive()) {
                        sLogger.d("read: Encoding: " + contentEncoding + ", type: " + contentType + ", length: " + contentLength);
                    }
                    try {
                        MeasureInputStream measuredIn = new MeasureInputStream(entity.getContent());
                        InputStream in = measuredIn;
                        if (contentType != null && !contentType.startsWith("application/octet-stream") && !contentType.startsWith("video/avc") && !contentType.startsWith("video/webm") && !contentType.startsWith("video/mp4") && !contentType.startsWith("binary/octet-stream") && !contentType.startsWith("application/x-www-form-urlencoded")) {
                            if (contentType.startsWith("application/json")) {
                                if (contentEncoding != null && contentEncoding.equals("gzip")) {
                                    contentLength = -1;
                                    in = new GZIPInputStream(in);
                                }
                            } else if (contentType.startsWith("text/html") || contentType.startsWith("text/plain") || contentType.startsWith("text/xml") || contentType.startsWith("application/xml") || contentType.startsWith("image/")) {
                                if (contentEncoding != null && contentEncoding.equals("gzip")) {
                                    contentLength = -1;
                                    in = new GZIPInputStream(in);
                                }
                            } else {
                                throw new IOException("Unsupported content type: " + contentType);
                            }
                        }
                        onRemoteInputStreamReady(in, cachePolicy, statusCode, contentLength);
                        readTime = measuredIn.getReadTime();
                        if (SLog.sLogsOn && RATE_LIMIT_BYTES_PER_MS != -1.0d) {
                            long rateLimitLeft = (long) ((contentLength / RATE_LIMIT_BYTES_PER_MS) - readTime);
                            if (rateLimitLeft > 0) {
                                SLog.d("Rate limiting: {}ms.", Long.valueOf(rateLimitLeft));
                                Thread.sleep(rateLimitLeft);
                            }
                        }
                        long now2 = System.currentTimeMillis();
                        CommonUtil.closeSilently(in);
                        closeTime = System.currentTimeMillis() - now2;
                    } catch (Throwable th) {
                        long now3 = System.currentTimeMillis();
                        CommonUtil.closeSilently(null);
                        long closeTime2 = System.currentTimeMillis() - now3;
                        throw th;
                    }
                }
                long durationMs = openTime + readTime + closeTime;
                if (firstByte != -1 && (SLog.sLogsOn || Math.random() > 0.1d)) {
                    URI uri = httpReq.getURI();
                    SLog.d("{}, {} took {} ms to open and {} total.", new Object[]{uri.getHost(), uri.getPath(), Long.valueOf(firstByte), Long.valueOf(durationMs)});
                    FlurryUtils.trackRespondTime(uri.getHost(), uri.getPath(), firstByte, durationMs, uri.toString().contains("/r/videos/"));
                }
                HttpResult result = new HttpResult(statusCode, reasonPhrase);
                result.exception = null;
                result.durationMs = durationMs;
                result.uploadKey = uploadKey;
                return new NetworkOperation.ExecuteResult(result, retries, authRetry);
            } catch (Exception ex) {
                if (sLogger.isActive()) {
                    sLogger.d("[" + httpReq.getURI() + "]", (Throwable) ex);
                }
                httpReq.abort();
                throw ex;
            }
        } catch (Throwable th2) {
            long durationMs2 = 0 + 0 + 0;
            if (-1 != -1 && (SLog.sLogsOn || Math.random() > 0.1d)) {
                URI uri2 = httpReq.getURI();
                SLog.d("{}, {} took {} ms to open and {} total.", new Object[]{uri2.getHost(), uri2.getPath(), -1L, Long.valueOf(durationMs2)});
                FlurryUtils.trackRespondTime(uri2.getHost(), uri2.getPath(), -1L, durationMs2, uri2.toString().contains("/r/videos/"));
            }
            throw th2;
        }
    }
}
