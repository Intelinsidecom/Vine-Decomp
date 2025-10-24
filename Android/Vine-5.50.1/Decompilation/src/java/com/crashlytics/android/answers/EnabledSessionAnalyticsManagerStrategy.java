package com.crashlytics.android.answers;

import android.content.Context;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.events.EnabledEventsStrategy;
import io.fabric.sdk.android.services.events.FilesSender;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import java.util.concurrent.ScheduledExecutorService;

/* loaded from: classes.dex */
class EnabledSessionAnalyticsManagerStrategy extends EnabledEventsStrategy<SessionEvent> implements SessionAnalyticsManagerStrategy<SessionEvent> {
    EventFilter eventFilter;
    FilesSender filesSender;
    private final HttpRequestFactory httpRequestFactory;

    public EnabledSessionAnalyticsManagerStrategy(Context context, ScheduledExecutorService executorService, SessionAnalyticsFilesManager filesManager, HttpRequestFactory httpRequestFactory) {
        super(context, executorService, filesManager);
        this.eventFilter = new KeepAllEventFilter();
        this.httpRequestFactory = httpRequestFactory;
    }

    @Override // io.fabric.sdk.android.services.events.EventsStrategy
    public FilesSender getFilesSender() {
        return this.filesSender;
    }

    @Override // com.crashlytics.android.answers.SessionAnalyticsManagerStrategy
    public void setAnalyticsSettingsData(AnalyticsSettingsData analyticsSettingsData, String protocolAndHostOverride) {
        this.filesSender = AnswersRetryFilesSender.build(new SessionAnalyticsFilesSender(Answers.getInstance(), protocolAndHostOverride, analyticsSettingsData.analyticsURL, this.httpRequestFactory, new ApiKey().getValue(this.context)));
        ((SessionAnalyticsFilesManager) this.filesManager).setAnalyticsSettingsData(analyticsSettingsData);
        configureRollover(analyticsSettingsData.flushIntervalSeconds);
        if (analyticsSettingsData.samplingRate > 1) {
            this.eventFilter = new SamplingEventFilter(analyticsSettingsData.samplingRate);
        }
    }

    @Override // io.fabric.sdk.android.services.events.EnabledEventsStrategy, io.fabric.sdk.android.services.events.EventsManager
    public void recordEvent(SessionEvent event) {
        if (this.eventFilter.skipEvent(event)) {
            CommonUtils.logControlled(Answers.getInstance().getContext(), "skipping filtered event " + event);
        } else {
            super.recordEvent((EnabledSessionAnalyticsManagerStrategy) event);
        }
    }
}
