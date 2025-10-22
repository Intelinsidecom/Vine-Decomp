package co.vine.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import co.vine.android.scribe.AppNavigationProvider;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.util.CrashUtil;
import com.flurry.android.FlurryAgent;

/* loaded from: classes.dex */
public class BaseFragment extends Fragment {
    public static Bundle prepareArguments(Intent intent) {
        if (intent != null) {
            Bundle args = intent.getExtras() != null ? intent.getExtras() : new Bundle();
            args.putString("action", intent.getAction());
            return args;
        }
        return null;
    }

    public void setActionBarColor() {
        Activity activity = getActivity();
        if (activity instanceof BaseActionBarActivity) {
            ((BaseActionBarActivity) activity).setActionBarColor();
        }
    }

    public void setActionBarColor(int color) {
        Activity activity = getActivity();
        if (activity instanceof BaseActionBarActivity) {
            ((BaseActionBarActivity) activity).setActionBarColor(color);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        CrashUtil.set("Fragment", getClass().getName() + " - " + System.currentTimeMillis());
        FlurryAgent.onPageView();
        updateAppNavigationProvider();
    }

    @Override // android.support.v4.app.Fragment
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        updateAppNavigationProvider();
    }

    protected void updateAppNavigationProvider() {
        Activity activity = getActivity();
        if (activity != null && getUserVisibleHint()) {
            AppNavigationProvider navProvider = AppNavigationProviderSingleton.getInstance();
            navProvider.setTimelineApiUrl(getTimelineApiUrl());
            navProvider.setViewAndSubview(getAppNavigationView(), getAppNavigationSubview());
        }
    }

    protected AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.EMPTY;
    }

    protected AppNavigation.Subviews getAppNavigationSubview() {
        return AppNavigation.Subviews.EMPTY;
    }

    protected String getTimelineApiUrl() {
        return "";
    }
}
