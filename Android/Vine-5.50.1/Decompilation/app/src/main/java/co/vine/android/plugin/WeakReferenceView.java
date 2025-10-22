package co.vine.android.plugin;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class WeakReferenceView extends WeakReference<View> {
    public WeakReferenceView(View r) {
        super(r);
    }

    public void setVisibility(int visibility) {
        View grid = (View) get();
        if (grid != null) {
            grid.setVisibility(visibility);
        }
    }

    public void startAnimation(Animation animation) {
        View grid = (View) get();
        if (grid != null) {
            grid.startAnimation(animation);
        }
    }

    public int getVisibility() {
        View grid = (View) get();
        if (grid != null) {
            return grid.getVisibility();
        }
        return -1;
    }

    public ViewGroup.LayoutParams getLayoutParams() {
        View grid = (View) get();
        if (grid != null) {
            return grid.getLayoutParams();
        }
        return null;
    }
}
