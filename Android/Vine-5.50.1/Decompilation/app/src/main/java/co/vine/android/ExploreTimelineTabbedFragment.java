package co.vine.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/* loaded from: classes.dex */
public class ExploreTimelineTabbedFragment extends ExploreTimelineFragment {
    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabbedActivity tabbedActivity = (TabbedActivity) getActivity();
        if (tabbedActivity.hasScrollAwayTabs()) {
            setUpUnderNavHeader();
        }
    }

    @Override // co.vine.android.ExploreTimelineFragment, co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        TabbedActivity activity = (TabbedActivity) getActivity();
        if (activity.isTabVisible(this.mCategory)) {
            setFocused(true);
            if (!this.mFeedAdapter.isPlaying()) {
                this.mFeedAdapter.onResume(true);
                return;
            }
            return;
        }
        setFocused(false);
        this.mFeedAdapter.onResume(false);
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveTo(int oldPosition) {
        super.onMoveTo(oldPosition);
        Activity activity = getActivity();
        ((TabbedFeedActivity) activity).resetNav();
    }
}
