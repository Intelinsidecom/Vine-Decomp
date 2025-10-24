package co.vine.android.scribe;

import co.vine.android.scribe.AppNavigationProvider;
import co.vine.android.scribe.model.AppNavigation;

/* loaded from: classes.dex */
public final class AppNavigationProviderImpl implements AppNavigationProvider {
    private AppNavigationProvider.ViewChangedListener mListener;
    private String mTimelineApiUrl;
    private AppNavigation.Sections mSection = AppNavigation.Sections.EMPTY;
    private AppNavigation.Views mView = AppNavigation.Views.EMPTY;
    private AppNavigation.Subviews mSubview = AppNavigation.Subviews.EMPTY;

    AppNavigationProviderImpl() {
    }

    @Override // co.vine.android.scribe.AppNavigationProvider
    public AppNavigation getAppNavigation() {
        AppNavigation appNavigation = new AppNavigation();
        appNavigation.section = this.mSection.toString();
        appNavigation.view = this.mView.toString();
        appNavigation.subview = this.mSubview.toString();
        appNavigation.timelineApiUrl = this.mTimelineApiUrl;
        return appNavigation;
    }

    @Override // co.vine.android.scribe.AppNavigationProvider
    public void setViewChangedListener(AppNavigationProvider.ViewChangedListener listener) {
        this.mListener = listener;
    }

    @Override // co.vine.android.scribe.AppNavigationProvider
    public void setSection(AppNavigation.Sections section) {
        this.mSection = section;
    }

    @Override // co.vine.android.scribe.AppNavigationProvider
    public void setViewAndSubview(AppNavigation.Views view, AppNavigation.Subviews subview) {
        if (view == null) {
            view = AppNavigation.Views.EMPTY;
        }
        this.mView = view;
        if (subview == null) {
            subview = AppNavigation.Subviews.EMPTY;
        }
        this.mSubview = subview;
        if (this.mListener != null) {
            this.mListener.onViewChanged();
        }
    }

    @Override // co.vine.android.scribe.AppNavigationProvider
    public void setTimelineApiUrl(String url) {
        this.mTimelineApiUrl = url;
    }
}
