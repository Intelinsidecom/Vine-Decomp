package com.google.android.exoplayer.upstream;

import android.text.TextUtils;
import com.google.android.exoplayer.util.Predicate;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface HttpDataSource extends UriDataSource {
    public static final Predicate<String> REJECT_PAYWALL_TYPES = new Predicate<String>() { // from class: com.google.android.exoplayer.upstream.HttpDataSource.1
        @Override // com.google.android.exoplayer.util.Predicate
        public boolean evaluate(String contentType) {
            String contentType2 = Util.toLowerInvariant(contentType);
            return (TextUtils.isEmpty(contentType2) || (contentType2.contains("text") && !contentType2.contains("text/vtt")) || contentType2.contains("html") || contentType2.contains("xml")) ? false : true;
        }
    };

    public static class HttpDataSourceException extends IOException {
        public final DataSpec dataSpec;

        public HttpDataSourceException(String message, DataSpec dataSpec) {
            super(message);
            this.dataSpec = dataSpec;
        }

        public HttpDataSourceException(IOException cause, DataSpec dataSpec) {
            super(cause);
            this.dataSpec = dataSpec;
        }

        public HttpDataSourceException(String message, IOException cause, DataSpec dataSpec) {
            super(message, cause);
            this.dataSpec = dataSpec;
        }
    }

    public static final class InvalidContentTypeException extends HttpDataSourceException {
        public final String contentType;

        public InvalidContentTypeException(String contentType, DataSpec dataSpec) {
            super("Invalid content type: " + contentType, dataSpec);
            this.contentType = contentType;
        }
    }

    public static final class InvalidResponseCodeException extends HttpDataSourceException {
        public final Map<String, List<String>> headerFields;
        public final int responseCode;

        public InvalidResponseCodeException(int responseCode, Map<String, List<String>> headerFields, DataSpec dataSpec) {
            super("Response code: " + responseCode, dataSpec);
            this.responseCode = responseCode;
            this.headerFields = headerFields;
        }
    }
}
