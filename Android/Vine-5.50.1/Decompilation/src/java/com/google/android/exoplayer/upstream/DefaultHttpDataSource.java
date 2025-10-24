package com.google.android.exoplayer.upstream;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Predicate;
import com.google.android.exoplayer.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class DefaultHttpDataSource implements HttpDataSource {
    private static final Pattern CONTENT_RANGE_HEADER = Pattern.compile("^bytes (\\d+)-(\\d+)/(\\d+)$");
    private static final AtomicReference<byte[]> skipBufferReference = new AtomicReference<>();
    private final boolean allowCrossProtocolRedirects;
    private long bytesRead;
    private long bytesSkipped;
    private long bytesToRead;
    private long bytesToSkip;
    private final int connectTimeoutMillis;
    private HttpURLConnection connection;
    private final Predicate<String> contentTypePredicate;
    private DataSpec dataSpec;
    private InputStream inputStream;
    private final TransferListener listener;
    private boolean opened;
    private final int readTimeoutMillis;
    private final HashMap<String, String> requestProperties = new HashMap<>();
    private final String userAgent;

    public DefaultHttpDataSource(String userAgent, Predicate<String> contentTypePredicate, TransferListener listener, int connectTimeoutMillis, int readTimeoutMillis, boolean allowCrossProtocolRedirects) {
        this.userAgent = Assertions.checkNotEmpty(userAgent);
        this.contentTypePredicate = contentTypePredicate;
        this.listener = listener;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.allowCrossProtocolRedirects = allowCrossProtocolRedirects;
    }

    @Override // com.google.android.exoplayer.upstream.UriDataSource
    public String getUri() {
        if (this.connection == null) {
            return null;
        }
        return this.connection.getURL().toString();
    }

    @Override // com.google.android.exoplayer.upstream.DataSource
    public long open(DataSpec dataSpec) throws IOException, NumberFormatException {
        long j = 0;
        this.dataSpec = dataSpec;
        this.bytesRead = 0L;
        this.bytesSkipped = 0L;
        try {
            this.connection = makeConnection(dataSpec);
            try {
                int responseCode = this.connection.getResponseCode();
                if (responseCode < 200 || responseCode > 299) {
                    Map<String, List<String>> headers = this.connection.getHeaderFields();
                    closeConnectionQuietly();
                    throw new HttpDataSource.InvalidResponseCodeException(responseCode, headers, dataSpec);
                }
                String contentType = this.connection.getContentType();
                if (this.contentTypePredicate != null && !this.contentTypePredicate.evaluate(contentType)) {
                    closeConnectionQuietly();
                    throw new HttpDataSource.InvalidContentTypeException(contentType, dataSpec);
                }
                if (responseCode == 200 && dataSpec.position != 0) {
                    j = dataSpec.position;
                }
                this.bytesToSkip = j;
                if ((dataSpec.flags & 1) == 0) {
                    long contentLength = getContentLength(this.connection);
                    this.bytesToRead = dataSpec.length != -1 ? dataSpec.length : contentLength != -1 ? contentLength - this.bytesToSkip : -1L;
                } else {
                    this.bytesToRead = dataSpec.length;
                }
                try {
                    this.inputStream = this.connection.getInputStream();
                    this.opened = true;
                    if (this.listener != null) {
                        this.listener.onTransferStart();
                    }
                    return this.bytesToRead;
                } catch (IOException e) {
                    closeConnectionQuietly();
                    throw new HttpDataSource.HttpDataSourceException(e, dataSpec);
                }
            } catch (IOException e2) {
                closeConnectionQuietly();
                throw new HttpDataSource.HttpDataSourceException("Unable to connect to " + dataSpec.uri.toString(), e2, dataSpec);
            }
        } catch (IOException e3) {
            throw new HttpDataSource.HttpDataSourceException("Unable to connect to " + dataSpec.uri.toString(), e3, dataSpec);
        }
    }

    @Override // com.google.android.exoplayer.upstream.DataSource
    public int read(byte[] buffer, int offset, int readLength) throws HttpDataSource.HttpDataSourceException {
        try {
            skipInternal();
            return readInternal(buffer, offset, readLength);
        } catch (IOException e) {
            throw new HttpDataSource.HttpDataSourceException(e, this.dataSpec);
        }
    }

    @Override // com.google.android.exoplayer.upstream.DataSource
    public void close() throws HttpDataSource.HttpDataSourceException {
        try {
            if (this.inputStream != null) {
                Util.maybeTerminateInputStream(this.connection, bytesRemaining());
                try {
                    this.inputStream.close();
                } catch (IOException e) {
                    throw new HttpDataSource.HttpDataSourceException(e, this.dataSpec);
                }
            }
        } finally {
            this.inputStream = null;
            closeConnectionQuietly();
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd();
                }
            }
        }
    }

    protected final long bytesRemaining() {
        return this.bytesToRead == -1 ? this.bytesToRead : this.bytesToRead - this.bytesRead;
    }

    private HttpURLConnection makeConnection(DataSpec dataSpec) throws IOException {
        URL url = new URL(dataSpec.uri.toString());
        byte[] postBody = dataSpec.postBody;
        long position = dataSpec.position;
        long length = dataSpec.length;
        boolean allowGzip = (dataSpec.flags & 1) != 0;
        if (!this.allowCrossProtocolRedirects) {
            return makeConnection(url, postBody, position, length, allowGzip, true);
        }
        int redirectCount = 0;
        while (true) {
            int redirectCount2 = redirectCount;
            redirectCount = redirectCount2 + 1;
            if (redirectCount2 <= 20) {
                HttpURLConnection connection = makeConnection(url, postBody, position, length, allowGzip, false);
                int responseCode = connection.getResponseCode();
                if (responseCode != 300 && responseCode != 301 && responseCode != 302 && responseCode != 303) {
                    if (postBody != null) {
                        return connection;
                    }
                    if (responseCode != 307 && responseCode != 308) {
                        return connection;
                    }
                }
                postBody = null;
                String location = connection.getHeaderField("Location");
                connection.disconnect();
                url = handleRedirect(url, location);
            } else {
                throw new NoRouteToHostException("Too many redirects: " + redirectCount);
            }
        }
    }

    private HttpURLConnection makeConnection(URL url, byte[] postBody, long position, long length, boolean allowGzip, boolean followRedirects) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(this.connectTimeoutMillis);
        connection.setReadTimeout(this.readTimeoutMillis);
        synchronized (this.requestProperties) {
            for (Map.Entry<String, String> property : this.requestProperties.entrySet()) {
                connection.setRequestProperty(property.getKey(), property.getValue());
            }
        }
        if (position != 0 || length != -1) {
            String rangeRequest = "bytes=" + position + "-";
            if (length != -1) {
                rangeRequest = rangeRequest + ((position + length) - 1);
            }
            connection.setRequestProperty("Range", rangeRequest);
        }
        connection.setRequestProperty("User-Agent", this.userAgent);
        if (!allowGzip) {
            connection.setRequestProperty("Accept-Encoding", "identity");
        }
        connection.setInstanceFollowRedirects(followRedirects);
        connection.setDoOutput(postBody != null);
        if (postBody != null) {
            connection.setFixedLengthStreamingMode(postBody.length);
            connection.connect();
            OutputStream os = connection.getOutputStream();
            os.write(postBody);
            os.close();
        } else {
            connection.connect();
        }
        return connection;
    }

    private static URL handleRedirect(URL originalUrl, String location) throws IOException {
        if (location == null) {
            throw new ProtocolException("Null location redirect");
        }
        URL url = new URL(originalUrl, location);
        String protocol = url.getProtocol();
        if (!"https".equals(protocol) && !"http".equals(protocol)) {
            throw new ProtocolException("Unsupported protocol redirect: " + protocol);
        }
        return url;
    }

    private static long getContentLength(HttpURLConnection connection) throws NumberFormatException {
        long contentLength = -1;
        String contentLengthHeader = connection.getHeaderField("Content-Length");
        if (!TextUtils.isEmpty(contentLengthHeader)) {
            try {
                contentLength = Long.parseLong(contentLengthHeader);
            } catch (NumberFormatException e) {
                Log.e("DefaultHttpDataSource", "Unexpected Content-Length [" + contentLengthHeader + "]");
            }
        }
        String contentRangeHeader = connection.getHeaderField("Content-Range");
        if (!TextUtils.isEmpty(contentRangeHeader)) {
            Matcher matcher = CONTENT_RANGE_HEADER.matcher(contentRangeHeader);
            if (matcher.find()) {
                try {
                    long contentLengthFromRange = (Long.parseLong(matcher.group(2)) - Long.parseLong(matcher.group(1))) + 1;
                    if (contentLength < 0) {
                        return contentLengthFromRange;
                    }
                    if (contentLength != contentLengthFromRange) {
                        Log.w("DefaultHttpDataSource", "Inconsistent headers [" + contentLengthHeader + "] [" + contentRangeHeader + "]");
                        return Math.max(contentLength, contentLengthFromRange);
                    }
                    return contentLength;
                } catch (NumberFormatException e2) {
                    Log.e("DefaultHttpDataSource", "Unexpected Content-Range [" + contentRangeHeader + "]");
                    return contentLength;
                }
            }
            return contentLength;
        }
        return contentLength;
    }

    private void skipInternal() throws IOException {
        if (this.bytesSkipped != this.bytesToSkip) {
            byte[] skipBuffer = skipBufferReference.getAndSet(null);
            if (skipBuffer == null) {
                skipBuffer = new byte[4096];
            }
            while (this.bytesSkipped != this.bytesToSkip) {
                int readLength = (int) Math.min(this.bytesToSkip - this.bytesSkipped, skipBuffer.length);
                int read = this.inputStream.read(skipBuffer, 0, readLength);
                if (Thread.interrupted()) {
                    throw new InterruptedIOException();
                }
                if (read == -1) {
                    throw new EOFException();
                }
                this.bytesSkipped += read;
                if (this.listener != null) {
                    this.listener.onBytesTransferred(read);
                }
            }
            skipBufferReference.set(skipBuffer);
        }
    }

    private int readInternal(byte[] buffer, int offset, int readLength) throws IOException {
        if (this.bytesToRead != -1) {
            readLength = (int) Math.min(readLength, this.bytesToRead - this.bytesRead);
        }
        if (readLength == 0) {
            return -1;
        }
        int read = this.inputStream.read(buffer, offset, readLength);
        if (read == -1) {
            if (this.bytesToRead == -1 || this.bytesToRead == this.bytesRead) {
                return -1;
            }
            throw new EOFException();
        }
        this.bytesRead += read;
        if (this.listener != null) {
            this.listener.onBytesTransferred(read);
            return read;
        }
        return read;
    }

    private void closeConnectionQuietly() {
        if (this.connection != null) {
            try {
                this.connection.disconnect();
            } catch (Exception e) {
                Log.e("DefaultHttpDataSource", "Unexpected error while disconnecting", e);
            }
            this.connection = null;
        }
    }
}
