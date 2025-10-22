package co.vine.android.scribe;

import co.vine.android.scribe.model.AppNavigation;

/* loaded from: classes.dex */
public interface AppNavigationProvider {

    public interface ViewChangedListener {
        void onViewChanged();
    }

    AppNavigation getAppNavigation();

    void setSection(AppNavigation.Sections sections);

    void setTimelineApiUrl(String str);

    void setViewAndSubview(AppNavigation.Views views, AppNavigation.Subviews subviews);

    void setViewChangedListener(ViewChangedListener viewChangedListener);
}
