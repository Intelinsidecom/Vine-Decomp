package co.vine.android;

import android.view.Menu;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.LoopManager;

/* loaded from: classes.dex */
public abstract class BaseTimelineActivity extends BaseControllerActionBarActivity {
    protected boolean mShowCaptureIcon = true;

    protected abstract BaseTimelineFragment getCurrentTimeLineFragment();

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        BaseTimelineFragment fragment;
        if (!BuildUtil.isExplore() || (fragment = getCurrentTimeLineFragment()) == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mShowCaptureIcon && isAutoCaptureIconEnabled() && !BuildUtil.isExplore()) {
            getMenuInflater().inflate(R.menu.capture, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        LoopManager.get(this).save();
    }

    protected boolean isAutoCaptureIconEnabled() {
        return true;
    }
}
