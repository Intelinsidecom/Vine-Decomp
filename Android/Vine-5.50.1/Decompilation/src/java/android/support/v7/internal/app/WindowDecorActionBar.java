package android.support.v7.internal.app;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v7.app.ActionBar;
import android.support.v7.internal.view.ActionBarPolicy;
import android.support.v7.internal.view.SupportMenuInflater;
import android.support.v7.internal.view.ViewPropertyAnimatorCompatSet;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.widget.ActionBarContainer;
import android.support.v7.internal.widget.ActionBarContextView;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.support.v7.internal.widget.DecorToolbar;
import android.support.v7.internal.widget.ScrollingTabContainerView;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class WindowDecorActionBar extends ActionBar implements ActionBarOverlayLayout.ActionBarVisibilityCallback {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final boolean ALLOW_SHOW_HIDE_ANIMATIONS;
    ActionModeImpl mActionMode;
    private Activity mActivity;
    private ActionBarContainer mContainerView;
    private View mContentView;
    private Context mContext;
    private int mContextDisplayMode;
    private ActionBarContextView mContextView;
    private ViewPropertyAnimatorCompatSet mCurrentShowAnim;
    private DecorToolbar mDecorToolbar;
    ActionMode mDeferredDestroyActionMode;
    ActionMode.Callback mDeferredModeDestroyCallback;
    private Dialog mDialog;
    private boolean mDisplayHomeAsUpSet;
    private boolean mHasEmbeddedTabs;
    private boolean mHiddenByApp;
    private boolean mHiddenBySystem;
    boolean mHideOnContentScroll;
    private boolean mLastMenuVisibility;
    private ActionBarOverlayLayout mOverlayLayout;
    private boolean mShowHideAnimationEnabled;
    private boolean mShowingForMode;
    private ActionBarContainer mSplitView;
    private ScrollingTabContainerView mTabScrollView;
    private Context mThemedContext;
    private ArrayList<Object> mTabs = new ArrayList<>();
    private int mSavedTabPosition = -1;
    private ArrayList<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new ArrayList<>();
    private int mCurWindowVisibility = 0;
    private boolean mContentAnimations = true;
    private boolean mNowShowing = true;
    final ViewPropertyAnimatorListener mHideListener = new ViewPropertyAnimatorListenerAdapter() { // from class: android.support.v7.internal.app.WindowDecorActionBar.1
        @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
        public void onAnimationEnd(View view) {
            if (WindowDecorActionBar.this.mContentAnimations && WindowDecorActionBar.this.mContentView != null) {
                ViewCompat.setTranslationY(WindowDecorActionBar.this.mContentView, 0.0f);
                ViewCompat.setTranslationY(WindowDecorActionBar.this.mContainerView, 0.0f);
            }
            if (WindowDecorActionBar.this.mSplitView != null && WindowDecorActionBar.this.mContextDisplayMode == 1) {
                WindowDecorActionBar.this.mSplitView.setVisibility(8);
            }
            WindowDecorActionBar.this.mContainerView.setVisibility(8);
            WindowDecorActionBar.this.mContainerView.setTransitioning(false);
            WindowDecorActionBar.this.mCurrentShowAnim = null;
            WindowDecorActionBar.this.completeDeferredDestroyActionMode();
            if (WindowDecorActionBar.this.mOverlayLayout != null) {
                ViewCompat.requestApplyInsets(WindowDecorActionBar.this.mOverlayLayout);
            }
        }
    };
    final ViewPropertyAnimatorListener mShowListener = new ViewPropertyAnimatorListenerAdapter() { // from class: android.support.v7.internal.app.WindowDecorActionBar.2
        @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
        public void onAnimationEnd(View view) {
            WindowDecorActionBar.this.mCurrentShowAnim = null;
            WindowDecorActionBar.this.mContainerView.requestLayout();
        }
    };
    final ViewPropertyAnimatorUpdateListener mUpdateListener = new ViewPropertyAnimatorUpdateListener() { // from class: android.support.v7.internal.app.WindowDecorActionBar.3
        @Override // android.support.v4.view.ViewPropertyAnimatorUpdateListener
        public void onAnimationUpdate(View view) {
            ((View) WindowDecorActionBar.this.mContainerView.getParent()).invalidate();
        }
    };

    static {
        $assertionsDisabled = !WindowDecorActionBar.class.desiredAssertionStatus();
        ALLOW_SHOW_HIDE_ANIMATIONS = Build.VERSION.SDK_INT >= 14;
    }

    public WindowDecorActionBar(Activity activity, boolean overlayMode) {
        this.mActivity = activity;
        Window window = activity.getWindow();
        View decor = window.getDecorView();
        init(decor);
        if (!overlayMode) {
            this.mContentView = decor.findViewById(R.id.content);
        }
    }

    public WindowDecorActionBar(Dialog dialog) {
        this.mDialog = dialog;
        init(dialog.getWindow().getDecorView());
    }

    private void init(View decor) {
        this.mOverlayLayout = (ActionBarOverlayLayout) decor.findViewById(android.support.v7.appcompat.R.id.decor_content_parent);
        if (this.mOverlayLayout != null) {
            this.mOverlayLayout.setActionBarVisibilityCallback(this);
        }
        this.mDecorToolbar = getDecorToolbar(decor.findViewById(android.support.v7.appcompat.R.id.action_bar));
        this.mContextView = (ActionBarContextView) decor.findViewById(android.support.v7.appcompat.R.id.action_context_bar);
        this.mContainerView = (ActionBarContainer) decor.findViewById(android.support.v7.appcompat.R.id.action_bar_container);
        this.mSplitView = (ActionBarContainer) decor.findViewById(android.support.v7.appcompat.R.id.split_action_bar);
        if (this.mDecorToolbar == null || this.mContextView == null || this.mContainerView == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with a compatible window decor layout");
        }
        this.mContext = this.mDecorToolbar.getContext();
        this.mContextDisplayMode = this.mDecorToolbar.isSplit() ? 1 : 0;
        int current = this.mDecorToolbar.getDisplayOptions();
        boolean homeAsUp = (current & 4) != 0;
        if (homeAsUp) {
            this.mDisplayHomeAsUpSet = true;
        }
        ActionBarPolicy abp = ActionBarPolicy.get(this.mContext);
        setHomeButtonEnabled(abp.enableHomeButtonByDefault() || homeAsUp);
        setHasEmbeddedTabs(abp.hasEmbeddedTabs());
        TypedArray a = this.mContext.obtainStyledAttributes(null, android.support.v7.appcompat.R.styleable.ActionBar, android.support.v7.appcompat.R.attr.actionBarStyle, 0);
        if (a.getBoolean(android.support.v7.appcompat.R.styleable.ActionBar_hideOnContentScroll, false)) {
            setHideOnContentScrollEnabled(true);
        }
        int elevation = a.getDimensionPixelSize(android.support.v7.appcompat.R.styleable.ActionBar_elevation, 0);
        if (elevation != 0) {
            setElevation(elevation);
        }
        a.recycle();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private DecorToolbar getDecorToolbar(View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        throw new IllegalStateException("Can't make a decor toolbar out of " + view.getClass().getSimpleName());
    }

    @Override // android.support.v7.app.ActionBar
    public void setElevation(float elevation) {
        ViewCompat.setElevation(this.mContainerView, elevation);
        if (this.mSplitView != null) {
            ViewCompat.setElevation(this.mSplitView, elevation);
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void onConfigurationChanged(Configuration newConfig) {
        setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
    }

    private void setHasEmbeddedTabs(boolean hasEmbeddedTabs) {
        this.mHasEmbeddedTabs = hasEmbeddedTabs;
        if (!this.mHasEmbeddedTabs) {
            this.mDecorToolbar.setEmbeddedTabView(null);
            this.mContainerView.setTabContainer(this.mTabScrollView);
        } else {
            this.mContainerView.setTabContainer(null);
            this.mDecorToolbar.setEmbeddedTabView(this.mTabScrollView);
        }
        boolean isInTabMode = getNavigationMode() == 2;
        if (this.mTabScrollView != null) {
            if (isInTabMode) {
                this.mTabScrollView.setVisibility(0);
                if (this.mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets(this.mOverlayLayout);
                }
            } else {
                this.mTabScrollView.setVisibility(8);
            }
        }
        this.mDecorToolbar.setCollapsible(!this.mHasEmbeddedTabs && isInTabMode);
        this.mOverlayLayout.setHasNonEmbeddedTabs(!this.mHasEmbeddedTabs && isInTabMode);
    }

    void completeDeferredDestroyActionMode() {
        if (this.mDeferredModeDestroyCallback != null) {
            this.mDeferredModeDestroyCallback.onDestroyActionMode(this.mDeferredDestroyActionMode);
            this.mDeferredDestroyActionMode = null;
            this.mDeferredModeDestroyCallback = null;
        }
    }

    @Override // android.support.v7.internal.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void onWindowVisibilityChanged(int visibility) {
        this.mCurWindowVisibility = visibility;
    }

    @Override // android.support.v7.app.ActionBar
    public void setShowHideAnimationEnabled(boolean enabled) {
        this.mShowHideAnimationEnabled = enabled;
        if (!enabled && this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void dispatchMenuVisibilityChanged(boolean isVisible) {
        if (isVisible != this.mLastMenuVisibility) {
            this.mLastMenuVisibility = isVisible;
            int count = this.mMenuVisibilityListeners.size();
            for (int i = 0; i < count; i++) {
                this.mMenuVisibilityListeners.get(i).onMenuVisibilityChanged(isVisible);
            }
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void setCustomView(int resId) {
        setCustomView(LayoutInflater.from(getThemedContext()).inflate(resId, this.mDecorToolbar.getViewGroup(), false));
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        setDisplayOptions(showHomeAsUp ? 4 : 0, 4);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayShowTitleEnabled(boolean showTitle) {
        setDisplayOptions(showTitle ? 8 : 0, 8);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayShowCustomEnabled(boolean showCustom) {
        setDisplayOptions(showCustom ? 16 : 0, 16);
    }

    @Override // android.support.v7.app.ActionBar
    public void setHomeButtonEnabled(boolean enable) {
        this.mDecorToolbar.setHomeButtonEnabled(enable);
    }

    @Override // android.support.v7.app.ActionBar
    public void setTitle(int resId) {
        setTitle(this.mContext.getString(resId));
    }

    @Override // android.support.v7.app.ActionBar
    public void setTitle(CharSequence title) {
        this.mDecorToolbar.setTitle(title);
    }

    @Override // android.support.v7.app.ActionBar
    public void setWindowTitle(CharSequence title) {
        this.mDecorToolbar.setWindowTitle(title);
    }

    @Override // android.support.v7.app.ActionBar
    public void setSubtitle(CharSequence subtitle) {
        this.mDecorToolbar.setSubtitle(subtitle);
    }

    public void setDisplayOptions(int options, int mask) {
        int current = this.mDecorToolbar.getDisplayOptions();
        if ((mask & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mDecorToolbar.setDisplayOptions((options & mask) | ((mask ^ (-1)) & current));
    }

    @Override // android.support.v7.app.ActionBar
    public void setBackgroundDrawable(Drawable d) {
        this.mContainerView.setPrimaryBackground(d);
    }

    @Override // android.support.v7.app.ActionBar
    public View getCustomView() {
        return this.mDecorToolbar.getCustomView();
    }

    public int getNavigationMode() {
        return this.mDecorToolbar.getNavigationMode();
    }

    @Override // android.support.v7.app.ActionBar
    public int getDisplayOptions() {
        return this.mDecorToolbar.getDisplayOptions();
    }

    @Override // android.support.v7.app.ActionBar
    public ActionMode startActionMode(ActionMode.Callback callback) {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        this.mOverlayLayout.setHideOnContentScrollEnabled(false);
        this.mContextView.killMode();
        ActionModeImpl mode = new ActionModeImpl(this.mContextView.getContext(), callback);
        if (!mode.dispatchOnCreate()) {
            return null;
        }
        mode.invalidate();
        this.mContextView.initForMode(mode);
        animateToMode(true);
        if (this.mSplitView != null && this.mContextDisplayMode == 1 && this.mSplitView.getVisibility() != 0) {
            this.mSplitView.setVisibility(0);
            if (this.mOverlayLayout != null) {
                ViewCompat.requestApplyInsets(this.mOverlayLayout);
            }
        }
        this.mContextView.sendAccessibilityEvent(32);
        this.mActionMode = mode;
        return mode;
    }

    @Override // android.support.v7.app.ActionBar
    public int getHeight() {
        return this.mContainerView.getHeight();
    }

    @Override // android.support.v7.internal.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void enableContentAnimations(boolean enabled) {
        this.mContentAnimations = enabled;
    }

    private void showForActionMode() {
        if (!this.mShowingForMode) {
            this.mShowingForMode = true;
            if (this.mOverlayLayout != null) {
                this.mOverlayLayout.setShowingForActionMode(true);
            }
            updateVisibility(false);
        }
    }

    @Override // android.support.v7.internal.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void showForSystem() {
        if (this.mHiddenBySystem) {
            this.mHiddenBySystem = false;
            updateVisibility(true);
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void hide() {
        if (!this.mHiddenByApp) {
            this.mHiddenByApp = true;
            updateVisibility(false);
        }
    }

    private void hideForActionMode() {
        if (this.mShowingForMode) {
            this.mShowingForMode = false;
            if (this.mOverlayLayout != null) {
                this.mOverlayLayout.setShowingForActionMode(false);
            }
            updateVisibility(false);
        }
    }

    @Override // android.support.v7.internal.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void hideForSystem() {
        if (!this.mHiddenBySystem) {
            this.mHiddenBySystem = true;
            updateVisibility(true);
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void setHideOnContentScrollEnabled(boolean hideOnContentScroll) {
        if (hideOnContentScroll && !this.mOverlayLayout.isInOverlayMode()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
        }
        this.mHideOnContentScroll = hideOnContentScroll;
        this.mOverlayLayout.setHideOnContentScrollEnabled(hideOnContentScroll);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean checkShowingFlags(boolean hiddenByApp, boolean hiddenBySystem, boolean showingForMode) {
        if (showingForMode) {
            return true;
        }
        return (hiddenByApp || hiddenBySystem) ? false : true;
    }

    private void updateVisibility(boolean fromSystem) {
        boolean shown = checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode);
        if (shown) {
            if (!this.mNowShowing) {
                this.mNowShowing = true;
                doShow(fromSystem);
                return;
            }
            return;
        }
        if (this.mNowShowing) {
            this.mNowShowing = false;
            doHide(fromSystem);
        }
    }

    public void doShow(boolean fromSystem) {
        if (this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
        }
        this.mContainerView.setVisibility(0);
        if (this.mCurWindowVisibility == 0 && ALLOW_SHOW_HIDE_ANIMATIONS && (this.mShowHideAnimationEnabled || fromSystem)) {
            ViewCompat.setTranslationY(this.mContainerView, 0.0f);
            float startingY = -this.mContainerView.getHeight();
            if (fromSystem) {
                int[] topLeft = {0, 0};
                this.mContainerView.getLocationInWindow(topLeft);
                startingY -= topLeft[1];
            }
            ViewCompat.setTranslationY(this.mContainerView, startingY);
            ViewPropertyAnimatorCompatSet anim = new ViewPropertyAnimatorCompatSet();
            ViewPropertyAnimatorCompat a = ViewCompat.animate(this.mContainerView).translationY(0.0f);
            a.setUpdateListener(this.mUpdateListener);
            anim.play(a);
            if (this.mContentAnimations && this.mContentView != null) {
                ViewCompat.setTranslationY(this.mContentView, startingY);
                anim.play(ViewCompat.animate(this.mContentView).translationY(0.0f));
            }
            if (this.mSplitView != null && this.mContextDisplayMode == 1) {
                ViewCompat.setTranslationY(this.mSplitView, this.mSplitView.getHeight());
                this.mSplitView.setVisibility(0);
                anim.play(ViewCompat.animate(this.mSplitView).translationY(0.0f));
            }
            anim.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, R.anim.decelerate_interpolator));
            anim.setDuration(250L);
            anim.setListener(this.mShowListener);
            this.mCurrentShowAnim = anim;
            anim.start();
        } else {
            ViewCompat.setAlpha(this.mContainerView, 1.0f);
            ViewCompat.setTranslationY(this.mContainerView, 0.0f);
            if (this.mContentAnimations && this.mContentView != null) {
                ViewCompat.setTranslationY(this.mContentView, 0.0f);
            }
            if (this.mSplitView != null && this.mContextDisplayMode == 1) {
                ViewCompat.setAlpha(this.mSplitView, 1.0f);
                ViewCompat.setTranslationY(this.mSplitView, 0.0f);
                this.mSplitView.setVisibility(0);
            }
            this.mShowListener.onAnimationEnd(null);
        }
        if (this.mOverlayLayout != null) {
            ViewCompat.requestApplyInsets(this.mOverlayLayout);
        }
    }

    public void doHide(boolean fromSystem) {
        if (this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
        }
        if (this.mCurWindowVisibility == 0 && ALLOW_SHOW_HIDE_ANIMATIONS && (this.mShowHideAnimationEnabled || fromSystem)) {
            ViewCompat.setAlpha(this.mContainerView, 1.0f);
            this.mContainerView.setTransitioning(true);
            ViewPropertyAnimatorCompatSet anim = new ViewPropertyAnimatorCompatSet();
            float endingY = -this.mContainerView.getHeight();
            if (fromSystem) {
                int[] topLeft = {0, 0};
                this.mContainerView.getLocationInWindow(topLeft);
                endingY -= topLeft[1];
            }
            ViewPropertyAnimatorCompat a = ViewCompat.animate(this.mContainerView).translationY(endingY);
            a.setUpdateListener(this.mUpdateListener);
            anim.play(a);
            if (this.mContentAnimations && this.mContentView != null) {
                anim.play(ViewCompat.animate(this.mContentView).translationY(endingY));
            }
            if (this.mSplitView != null && this.mSplitView.getVisibility() == 0) {
                ViewCompat.setAlpha(this.mSplitView, 1.0f);
                anim.play(ViewCompat.animate(this.mSplitView).translationY(this.mSplitView.getHeight()));
            }
            anim.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, R.anim.accelerate_interpolator));
            anim.setDuration(250L);
            anim.setListener(this.mHideListener);
            this.mCurrentShowAnim = anim;
            anim.start();
            return;
        }
        this.mHideListener.onAnimationEnd(null);
    }

    public void animateToMode(boolean toActionMode) {
        if (toActionMode) {
            showForActionMode();
        } else {
            hideForActionMode();
        }
        this.mDecorToolbar.animateToVisibility(toActionMode ? 8 : 0);
        this.mContextView.animateToVisibility(toActionMode ? 0 : 8);
    }

    @Override // android.support.v7.app.ActionBar
    public Context getThemedContext() {
        if (this.mThemedContext == null) {
            TypedValue outValue = new TypedValue();
            Resources.Theme currentTheme = this.mContext.getTheme();
            currentTheme.resolveAttribute(android.support.v7.appcompat.R.attr.actionBarWidgetTheme, outValue, true);
            int targetThemeRes = outValue.resourceId;
            if (targetThemeRes != 0) {
                this.mThemedContext = new ContextThemeWrapper(this.mContext, targetThemeRes);
            } else {
                this.mThemedContext = this.mContext;
            }
        }
        return this.mThemedContext;
    }

    @Override // android.support.v7.internal.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void onContentScrollStarted() {
        if (this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
            this.mCurrentShowAnim = null;
        }
    }

    @Override // android.support.v7.internal.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void onContentScrollStopped() {
    }

    @Override // android.support.v7.app.ActionBar
    public boolean collapseActionView() {
        if (this.mDecorToolbar == null || !this.mDecorToolbar.hasExpandedActionView()) {
            return false;
        }
        this.mDecorToolbar.collapseActionView();
        return true;
    }

    public class ActionModeImpl extends ActionMode implements MenuBuilder.Callback {
        private final Context mActionModeContext;
        private ActionMode.Callback mCallback;
        private WeakReference<View> mCustomView;
        private final MenuBuilder mMenu;

        public ActionModeImpl(Context context, ActionMode.Callback callback) {
            this.mActionModeContext = context;
            this.mCallback = callback;
            this.mMenu = new MenuBuilder(context).setDefaultShowAsAction(1);
            this.mMenu.setCallback(this);
        }

        @Override // android.support.v7.view.ActionMode
        public MenuInflater getMenuInflater() {
            return new SupportMenuInflater(this.mActionModeContext);
        }

        @Override // android.support.v7.view.ActionMode
        public Menu getMenu() {
            return this.mMenu;
        }

        @Override // android.support.v7.view.ActionMode
        public void finish() {
            if (WindowDecorActionBar.this.mActionMode == this) {
                if (!WindowDecorActionBar.checkShowingFlags(WindowDecorActionBar.this.mHiddenByApp, WindowDecorActionBar.this.mHiddenBySystem, false)) {
                    WindowDecorActionBar.this.mDeferredDestroyActionMode = this;
                    WindowDecorActionBar.this.mDeferredModeDestroyCallback = this.mCallback;
                } else {
                    this.mCallback.onDestroyActionMode(this);
                }
                this.mCallback = null;
                WindowDecorActionBar.this.animateToMode(false);
                WindowDecorActionBar.this.mContextView.closeMode();
                WindowDecorActionBar.this.mDecorToolbar.getViewGroup().sendAccessibilityEvent(32);
                WindowDecorActionBar.this.mOverlayLayout.setHideOnContentScrollEnabled(WindowDecorActionBar.this.mHideOnContentScroll);
                WindowDecorActionBar.this.mActionMode = null;
            }
        }

        @Override // android.support.v7.view.ActionMode
        public void invalidate() {
            if (WindowDecorActionBar.this.mActionMode == this) {
                this.mMenu.stopDispatchingItemsChanged();
                try {
                    this.mCallback.onPrepareActionMode(this, this.mMenu);
                } finally {
                    this.mMenu.startDispatchingItemsChanged();
                }
            }
        }

        public boolean dispatchOnCreate() {
            this.mMenu.stopDispatchingItemsChanged();
            try {
                return this.mCallback.onCreateActionMode(this, this.mMenu);
            } finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }

        @Override // android.support.v7.view.ActionMode
        public void setCustomView(View view) {
            WindowDecorActionBar.this.mContextView.setCustomView(view);
            this.mCustomView = new WeakReference<>(view);
        }

        @Override // android.support.v7.view.ActionMode
        public void setSubtitle(CharSequence subtitle) {
            WindowDecorActionBar.this.mContextView.setSubtitle(subtitle);
        }

        @Override // android.support.v7.view.ActionMode
        public void setTitle(CharSequence title) {
            WindowDecorActionBar.this.mContextView.setTitle(title);
        }

        @Override // android.support.v7.view.ActionMode
        public void setTitle(int resId) {
            setTitle(WindowDecorActionBar.this.mContext.getResources().getString(resId));
        }

        @Override // android.support.v7.view.ActionMode
        public void setSubtitle(int resId) {
            setSubtitle(WindowDecorActionBar.this.mContext.getResources().getString(resId));
        }

        @Override // android.support.v7.view.ActionMode
        public CharSequence getTitle() {
            return WindowDecorActionBar.this.mContextView.getTitle();
        }

        @Override // android.support.v7.view.ActionMode
        public CharSequence getSubtitle() {
            return WindowDecorActionBar.this.mContextView.getSubtitle();
        }

        @Override // android.support.v7.view.ActionMode
        public void setTitleOptionalHint(boolean titleOptional) {
            super.setTitleOptionalHint(titleOptional);
            WindowDecorActionBar.this.mContextView.setTitleOptional(titleOptional);
        }

        @Override // android.support.v7.view.ActionMode
        public boolean isTitleOptional() {
            return WindowDecorActionBar.this.mContextView.isTitleOptional();
        }

        @Override // android.support.v7.view.ActionMode
        public View getCustomView() {
            if (this.mCustomView != null) {
                return this.mCustomView.get();
            }
            return null;
        }

        @Override // android.support.v7.internal.view.menu.MenuBuilder.Callback
        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
            if (this.mCallback != null) {
                return this.mCallback.onActionItemClicked(this, item);
            }
            return false;
        }

        @Override // android.support.v7.internal.view.menu.MenuBuilder.Callback
        public void onMenuModeChange(MenuBuilder menu) {
            if (this.mCallback != null) {
                invalidate();
                WindowDecorActionBar.this.mContextView.showOverflowMenu();
            }
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void setCustomView(View view) {
        this.mDecorToolbar.setCustomView(view);
    }

    @Override // android.support.v7.app.ActionBar
    public void setIcon(int resId) {
        this.mDecorToolbar.setIcon(resId);
    }

    @Override // android.support.v7.app.ActionBar
    public void setIcon(Drawable icon) {
        this.mDecorToolbar.setIcon(icon);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDefaultDisplayHomeAsUpEnabled(boolean enable) {
        if (!this.mDisplayHomeAsUpSet) {
            setDisplayHomeAsUpEnabled(enable);
        }
    }
}
