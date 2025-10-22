package co.vine.android.widget.tabs;

import co.vine.android.BaseControllerActionBarActivity;

/* loaded from: classes.dex */
public class TabScrollListener {
    private final BaseControllerActionBarActivity mActivity;
    private final int mTabIndex;

    public TabScrollListener(BaseControllerActionBarActivity activity, int position) {
        this.mTabIndex = position;
        this.mActivity = activity;
    }

    public void onScrollFirstItem() {
    }
}
