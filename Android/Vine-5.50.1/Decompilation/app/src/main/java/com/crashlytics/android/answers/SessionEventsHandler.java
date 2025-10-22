package com.crashlytics.android.answers;

import android.content.Context;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.events.EventsHandler;
import io.fabric.sdk.android.services.events.EventsStrategy;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import java.util.concurrent.ScheduledExecutorService;

/* loaded from: classes.dex */
class SessionEventsHandler extends EventsHandler<SessionEvent> {
    SessionEventsHandler(Context context, EventsStrategy<SessionEvent> strategy, SessionAnalyticsFilesManager filesManager, ScheduledExecutorService executor) {
        super(context, strategy, filesManager, executor);
    }

    @Override // io.fabric.sdk.android.services.events.EventsHandler
    protected EventsStrategy<SessionEvent> getDisabledEventsStrategy() {
        return new DisabledSessionAnalyticsManagerStrategy();
    }

    protected void setAnalyticsSettingsData(final AnalyticsSettingsData analyticsSettingsData, final String protocolAndHostOverride) {
        super.executeAsync(new Runnable() { // from class: com.crashlytics.android.answers.SessionEventsHandler.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ((SessionAnalyticsManagerStrategy) SessionEventsHandler.this.strategy).setAnalyticsSettingsData(analyticsSettingsData, protocolAndHostOverride);
                } catch (Exception e) {
                    CommonUtils.logControlledError(Answers.getInstance().getContext(), "Crashlytics failed to set analytics settings data.", e);
                }
            }
        });
    }
}
