package co.vine.android.scribe;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.LaunchDetails;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AppLifecycleScribeLogger implements Application.ActivityLifecycleCallbacks {
    private final AppStateProvider mAppStateProvider;
    private int mForegroundActivityCount = 0;
    private final ScribeLogger mLogger;

    AppLifecycleScribeLogger(ScribeLogger logger, AppStateProvider provider) {
        this.mLogger = logger;
        this.mAppStateProvider = provider;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        this.mForegroundActivityCount++;
        if (this.mForegroundActivityCount == 1) {
            ClientEvent event = this.mLogger.getDefaultClientEvent();
            event.appState = this.mAppStateProvider.getAppState();
            event.eventType = "session_started";
            event.eventDetails.launch = getLaunchDataFromActivity(activity);
            this.mLogger.logClientEvent(event);
        }
    }

    private LaunchDetails getLaunchDataFromActivity(Activity activity) {
        Uri uri;
        Intent intent = activity.getIntent();
        if (intent == null || (uri = intent.getData()) == null) {
            return null;
        }
        String srcParam = uri.getQueryParameter("src");
        if (TextUtils.isEmpty(srcParam)) {
            return null;
        }
        LaunchDetails details = new LaunchDetails();
        details.webSrc = srcParam;
        return details;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        this.mForegroundActivityCount--;
        if (this.mForegroundActivityCount == 0) {
            ClientEvent event = this.mLogger.getDefaultClientEvent();
            event.appState = this.mAppStateProvider.getAppState();
            event.eventType = "session_ended";
            this.mLogger.logClientEvent(event);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }
}
