package com.crashlytics.android.answers;

import io.fabric.sdk.android.services.events.EventsStrategy;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;

/* loaded from: classes.dex */
interface SessionAnalyticsManagerStrategy<T> extends EventsStrategy<T> {
    void setAnalyticsSettingsData(AnalyticsSettingsData analyticsSettingsData, String str);
}
