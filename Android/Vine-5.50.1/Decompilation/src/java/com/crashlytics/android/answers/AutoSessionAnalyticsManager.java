package com.crashlytics.android.answers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.ExecutorUtils;
import io.fabric.sdk.android.services.events.EventsStrategy;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import java.util.concurrent.ScheduledExecutorService;

@TargetApi(14)
/* loaded from: classes.dex */
class AutoSessionAnalyticsManager extends SessionAnalyticsManager {
    private final Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private final Application application;

    public static AutoSessionAnalyticsManager build(Application application, SessionEventMetadata metadata, SessionAnalyticsFilesManager filesManager, HttpRequestFactory httpRequestFactory) {
        ScheduledExecutorService executor = ExecutorUtils.buildSingleThreadScheduledExecutorService("Crashlytics Trace Manager");
        EventsStrategy<SessionEvent> strategy = new EnabledSessionAnalyticsManagerStrategy(application, executor, filesManager, httpRequestFactory);
        SessionEventsHandler eventsHandler = new SessionEventsHandler(application, strategy, filesManager, executor);
        return new AutoSessionAnalyticsManager(metadata, eventsHandler, application);
    }

    AutoSessionAnalyticsManager(SessionEventMetadata metadata, SessionEventsHandler eventsHandler, Application application) {
        super(metadata, eventsHandler);
        this.activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() { // from class: com.crashlytics.android.answers.AutoSessionAnalyticsManager.1
            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityCreated(Activity activity, Bundle bundle) {
                AutoSessionAnalyticsManager.this.onCreate(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityDestroyed(Activity activity) {
                AutoSessionAnalyticsManager.this.onDestroy(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPaused(Activity activity) {
                AutoSessionAnalyticsManager.this.onPause(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityResumed(Activity activity) {
                AutoSessionAnalyticsManager.this.onResume(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                AutoSessionAnalyticsManager.this.onSaveInstanceState(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStarted(Activity activity) {
                AutoSessionAnalyticsManager.this.onStart(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStopped(Activity activity) {
                AutoSessionAnalyticsManager.this.onStop(activity);
            }
        };
        this.application = application;
        CommonUtils.logControlled(Answers.getInstance().getContext(), "Registering activity lifecycle callbacks for session analytics.");
        application.registerActivityLifecycleCallbacks(this.activityLifecycleCallbacks);
    }

    @Override // com.crashlytics.android.answers.SessionAnalyticsManager
    public void disable() {
        CommonUtils.logControlled(Answers.getInstance().getContext(), "Unregistering activity lifecycle callbacks for session analytics");
        this.application.unregisterActivityLifecycleCallbacks(this.activityLifecycleCallbacks);
        super.disable();
    }
}
