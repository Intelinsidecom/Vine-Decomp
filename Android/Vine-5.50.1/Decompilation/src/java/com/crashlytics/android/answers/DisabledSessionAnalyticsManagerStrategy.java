package com.crashlytics.android.answers;

import io.fabric.sdk.android.services.events.DisabledEventsStrategy;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;

/* loaded from: classes.dex */
class DisabledSessionAnalyticsManagerStrategy extends DisabledEventsStrategy<SessionEvent> implements SessionAnalyticsManagerStrategy<SessionEvent> {
    DisabledSessionAnalyticsManagerStrategy() {
    }

    @Override // com.crashlytics.android.answers.SessionAnalyticsManagerStrategy
    public void setAnalyticsSettingsData(AnalyticsSettingsData analyticsSettingsData, String protocolAndHostOverride) {
    }
}
