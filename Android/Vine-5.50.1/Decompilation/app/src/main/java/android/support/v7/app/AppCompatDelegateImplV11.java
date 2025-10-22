package android.support.v7.app;

import android.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.v7.internal.view.SupportActionModeWrapper;
import android.support.v7.internal.widget.NativeActionModeAwareLayout;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

@TargetApi(11)
/* loaded from: classes.dex */
class AppCompatDelegateImplV11 extends AppCompatDelegateImplV7 implements NativeActionModeAwareLayout.OnActionModeForChildListener {
    private NativeActionModeAwareLayout mNativeActionModeAwareLayout;

    AppCompatDelegateImplV11(Context context, Window window, AppCompatCallback callback) {
        super(context, window, callback);
    }

    @Override // android.support.v7.app.AppCompatDelegateImplV7
    void onSubDecorInstalled(ViewGroup subDecor) {
        this.mNativeActionModeAwareLayout = (NativeActionModeAwareLayout) subDecor.findViewById(R.id.content);
        if (this.mNativeActionModeAwareLayout != null) {
            this.mNativeActionModeAwareLayout.setActionModeForChildListener(this);
        }
    }

    @Override // android.support.v7.internal.widget.NativeActionModeAwareLayout.OnActionModeForChildListener
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
        Context context = originalView.getContext();
        android.support.v7.view.ActionMode supportActionMode = startSupportActionMode(new SupportActionModeWrapper.CallbackWrapper(context, callback));
        if (supportActionMode != null) {
            return new SupportActionModeWrapper(this.mContext, supportActionMode);
        }
        return null;
    }

    @Override // android.support.v7.app.AppCompatDelegateImplV7
    View callActivityOnCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = super.callActivityOnCreateView(parent, name, context, attrs);
        if (view == null) {
            if (this.mOriginalWindowCallback instanceof LayoutInflater.Factory2) {
                return ((LayoutInflater.Factory2) this.mOriginalWindowCallback).onCreateView(parent, name, context, attrs);
            }
            return null;
        }
        return view;
    }
}
