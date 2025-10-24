package co.vine.android.scribe;

import co.vine.android.scribe.AppNavigationProvider;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.scribe.model.ClientEvent;

/* loaded from: classes.dex */
public final class ViewImpressionScribeLogger implements AppNavigationProvider.ViewChangedListener {
    AppNavigationProvider mAppNavProvider;
    AppStateProvider mAppStateProvider;
    ScribeLogger mScribeLogger;

    public ViewImpressionScribeLogger(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        this.mScribeLogger = logger;
        this.mAppStateProvider = appStateProvider;
        this.mAppNavProvider = appNavProvider;
    }

    @Override // co.vine.android.scribe.AppNavigationProvider.ViewChangedListener
    public void onViewChanged() {
        AppNavigation appNav = this.mAppNavProvider.getAppNavigation();
        if (!AppNavigation.Views.EMPTY.toString().equals(appNav.view)) {
            ClientEvent event = this.mScribeLogger.getDefaultClientEvent();
            event.appState = this.mAppStateProvider.getAppState();
            event.navigation = appNav;
            event.eventType = "view_impression";
            this.mScribeLogger.logClientEvent(event);
        }
    }
}
