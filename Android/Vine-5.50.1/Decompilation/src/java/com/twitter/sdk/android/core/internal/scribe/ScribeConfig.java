package com.twitter.sdk.android.core.internal.scribe;

/* loaded from: classes.dex */
public class ScribeConfig {
    public final String baseUrl;
    public final boolean isEnabled;
    public final int maxFilesToKeep;
    public final String pathType;
    public final String pathVersion;
    public final int sendIntervalSeconds;
    public final String sequence;
    public final String userAgent;

    public ScribeConfig(boolean isEnabled, String baseUrl, String pathVersion, String pathType, String sequence, String userAgent, int maxFilesToKeep, int sendIntervalSeconds) {
        this.isEnabled = isEnabled;
        this.baseUrl = baseUrl;
        this.pathVersion = pathVersion;
        this.pathType = pathType;
        this.sequence = sequence;
        this.userAgent = userAgent;
        this.maxFilesToKeep = maxFilesToKeep;
        this.sendIntervalSeconds = sendIntervalSeconds;
    }
}
