package co.vine.android.widget.tabs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TabHost;

/* loaded from: classes.dex */
public class IconTabHost extends TabHost {
    private OnTabClickedListener mOnTabClickedListener;

    public interface OnTabClickedListener {
        void onCurrentTabClicked();
    }

    public IconTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnTabClickedListener(OnTabClickedListener listener) {
        this.mOnTabClickedListener = listener;
    }

    @Override // android.widget.TabHost, android.view.ViewGroup, android.view.View
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        if (getCurrentView() != null) {
            super.dispatchWindowFocusChanged(hasFocus);
        }
    }

    @Override // android.widget.TabHost
    public void setCurrentTab(int index) {
        if (index == getCurrentTab()) {
            if (this.mOnTabClickedListener != null) {
                this.mOnTabClickedListener.onCurrentTabClicked();
                return;
            }
            return;
        }
        super.setCurrentTab(index);
    }

    public void setTabExplicit(int index) {
        super.setCurrentTab(index);
    }
}
