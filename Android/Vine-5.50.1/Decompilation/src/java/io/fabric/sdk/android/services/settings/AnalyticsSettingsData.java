package io.fabric.sdk.android.services.settings;

/* loaded from: classes.dex */
public class AnalyticsSettingsData {
    public final String analyticsURL;
    public final int flushIntervalSeconds;
    public final boolean flushOnBackground;
    public final int maxByteSizePerFile;
    public final int maxFileCountPerSend;
    public final int maxPendingSendFileCount;
    public final int samplingRate;
    public final boolean trackCustomEvents;
    public final boolean trackPredefinedEvents;

    public AnalyticsSettingsData(String analyticsURL, int flushIntervalSeconds, int maxByteSizePerFile, int maxFileCountPerSend, int maxPendingSendFileCount, boolean trackCustomEvents, boolean trackPredefinedEvents, int samplingRate, boolean flushOnBackground) {
        this.analyticsURL = analyticsURL;
        this.flushIntervalSeconds = flushIntervalSeconds;
        this.maxByteSizePerFile = maxByteSizePerFile;
        this.maxFileCountPerSend = maxFileCountPerSend;
        this.maxPendingSendFileCount = maxPendingSendFileCount;
        this.trackCustomEvents = trackCustomEvents;
        this.trackPredefinedEvents = trackPredefinedEvents;
        this.samplingRate = samplingRate;
        this.flushOnBackground = flushOnBackground;
    }
}
