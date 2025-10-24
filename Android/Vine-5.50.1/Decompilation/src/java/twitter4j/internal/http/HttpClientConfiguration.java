package twitter4j.internal.http;

/* loaded from: classes.dex */
public interface HttpClientConfiguration {
    int getHttpConnectionTimeout();

    int getHttpDefaultMaxPerRoute();

    int getHttpMaxTotalConnections();

    String getHttpProxyHost();

    String getHttpProxyPassword();

    int getHttpProxyPort();

    String getHttpProxyUser();

    int getHttpReadTimeout();

    int getHttpRetryCount();

    int getHttpRetryIntervalSeconds();

    boolean isGZIPEnabled();

    boolean isPrettyDebugEnabled();
}
