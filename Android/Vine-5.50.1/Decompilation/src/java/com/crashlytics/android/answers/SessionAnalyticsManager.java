package com.crashlytics.android.answers;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import com.crashlytics.android.answers.SessionEvent;
import io.fabric.sdk.android.services.common.ExecutorUtils;
import io.fabric.sdk.android.services.events.EventsStrategy;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import java.util.concurrent.ScheduledExecutorService;

/* loaded from: classes.dex */
class SessionAnalyticsManager {
    boolean customEventsEnabled = true;
    final SessionEventsHandler eventsHandler;
    final SessionEventMetadata metadata;

    public static SessionAnalyticsManager build(Context context, SessionEventMetadata metadata, SessionAnalyticsFilesManager filesManager, HttpRequestFactory httpRequestFactory) {
        ScheduledExecutorService executor = ExecutorUtils.buildSingleThreadScheduledExecutorService("Crashlytics SAM");
        EventsStrategy<SessionEvent> strategy = new EnabledSessionAnalyticsManagerStrategy(context, executor, filesManager, httpRequestFactory);
        SessionEventsHandler eventsHandler = new SessionEventsHandler(context, strategy, filesManager, executor);
        return new SessionAnalyticsManager(metadata, eventsHandler);
    }

    SessionAnalyticsManager(SessionEventMetadata metadata, SessionEventsHandler eventsHandler) {
        this.metadata = metadata;
        this.eventsHandler = eventsHandler;
    }

    public void onCrash(String sessionId) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("onCrash called from main thread!!!");
        }
        this.eventsHandler.recordEventSync(SessionEvent.buildCrashEvent(this.metadata, sessionId));
    }

    public void onError(String sessionId) {
        this.eventsHandler.recordEventAsync(SessionEvent.buildErrorEvent(this.metadata, sessionId), false);
    }

    public void onInstall() {
        this.eventsHandler.recordEventAsync(SessionEvent.buildInstallEvent(this.metadata), true);
    }

    public void onCreate(Activity activity) {
        this.eventsHandler.recordEventAsync(SessionEvent.buildActivityLifecycleEvent(this.metadata, SessionEvent.Type.CREATE, activity), false);
    }

    public void onDestroy(Activity activity) {
        this.eventsHandler.recordEventAsync(SessionEvent.buildActivityLifecycleEvent(this.metadata, SessionEvent.Type.DESTROY, activity), false);
    }

    public void onPause(Activity activity) {
        this.eventsHandler.recordEventAsync(SessionEvent.buildActivityLifecycleEvent(this.metadata, SessionEvent.Type.PAUSE, activity), false);
    }

    public void onResume(Activity activity) {
        this.eventsHandler.recordEventAsync(SessionEvent.buildActivityLifecycleEvent(this.metadata, SessionEvent.Type.RESUME, activity), false);
    }

    public void onSaveInstanceState(Activity activity) {
        this.eventsHandler.recordEventAsync(SessionEvent.buildActivityLifecycleEvent(this.metadata, SessionEvent.Type.SAVE_INSTANCE_STATE, activity), false);
    }

    public void onStart(Activity activity) {
        this.eventsHandler.recordEventAsync(SessionEvent.buildActivityLifecycleEvent(this.metadata, SessionEvent.Type.START, activity), false);
    }

    public void onStop(Activity activity) {
        this.eventsHandler.recordEventAsync(SessionEvent.buildActivityLifecycleEvent(this.metadata, SessionEvent.Type.STOP, activity), false);
    }

    public void setAnalyticsSettingsData(AnalyticsSettingsData analyticsSettingsData, String protocolAndHostOverride) {
        this.customEventsEnabled = analyticsSettingsData.trackCustomEvents;
        this.eventsHandler.setAnalyticsSettingsData(analyticsSettingsData, protocolAndHostOverride);
    }

    public void disable() {
        this.eventsHandler.disable();
    }
}
