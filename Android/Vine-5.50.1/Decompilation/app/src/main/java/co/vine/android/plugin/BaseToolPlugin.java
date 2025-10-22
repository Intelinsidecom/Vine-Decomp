package co.vine.android.plugin;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import co.vine.android.R;
import co.vine.android.recorder.VineRecorder;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public abstract class BaseToolPlugin<T extends View> extends BaseRecorderPlugin<LinearLayout, VineRecorder> implements View.OnClickListener {
    private WeakReference<T> mInflatedChild;
    protected int mPrimaryColor;
    protected int mSecondaryColor;
    protected boolean mShouldOnboard;

    protected abstract T onLayoutInflated(LinearLayout linearLayout, LayoutInflater layoutInflater, Fragment fragment);

    BaseToolPlugin(String name) {
        super(name);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // co.vine.android.plugin.BaseRecorderPlugin
    public LinearLayout onLayout(ViewGroup parent, LayoutInflater inflater, Fragment fragment) {
        if (fragment instanceof RecorderPluginSupportedFragment) {
            this.mPrimaryColor = ((RecorderPluginSupportedFragment) fragment).getColor(true);
            this.mSecondaryColor = ((RecorderPluginSupportedFragment) fragment).getColor(false);
        }
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.plugin_base_tool, parent, false);
        view.setOnClickListener(this);
        View viewOnLayoutInflated = onLayoutInflated(view, inflater, fragment);
        if (viewOnLayoutInflated != null) {
            viewOnLayoutInflated.setOnClickListener(this);
            view.addView(viewOnLayoutInflated);
            this.mInflatedChild = new WeakReference<>(viewOnLayoutInflated);
        }
        return view;
    }

    public T getInflatedChild() {
        if (this.mInflatedChild == null) {
            return null;
        }
        T child = this.mInflatedChild.get();
        return child;
    }

    public static View.OnTouchListener createOnToolTouchedListener() {
        return new View.OnTouchListener() { // from class: co.vine.android.plugin.BaseToolPlugin.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        view.setAlpha(1.0f);
                        break;
                    case 1:
                        view.setAlpha(0.35f);
                        break;
                }
                return false;
            }
        };
    }
}
