package co.vine.android.widget.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class TabInfo {
    public final Bundle bundle;
    public final Class<?> clss;
    private WeakReference<Fragment> mRef;
    protected String mTag;

    public TabInfo(Class<?> clss, Bundle bundle, String tag) {
        this.clss = clss;
        this.bundle = bundle;
        this.mTag = tag;
    }

    public void setFragment(Fragment f) {
        if (f != null) {
            this.mRef = new WeakReference<>(f);
        } else {
            this.mRef = null;
        }
    }

    public Fragment fragment() {
        if (this.mRef != null) {
            return this.mRef.get();
        }
        return null;
    }
}
