package co.vine.android.share.screens;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import co.vine.android.R;
import co.vine.android.animation.SimpleAnimatorListener;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/* loaded from: classes.dex */
public class ScreenManager extends FrameLayout {
    private AppController mAppController;
    private final List<AppSessionListener> mAppSessionListeners;
    private final Stack<String> mBackStack;
    private final FakeActionBar mFakeActionBar;
    private Bundle mResult;
    private final Map<String, Screen> mScreenMap;
    private boolean mTouchEnabled;

    public ScreenManager(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mFakeActionBar = (FakeActionBar) inflater.inflate(R.layout.screen_fake_action_bar_standalone, (ViewGroup) null);
        this.mScreenMap = new HashMap();
        this.mBackStack = new Stack<>();
        this.mAppSessionListeners = new ArrayList();
        this.mResult = new Bundle();
        initialize();
    }

    public ScreenManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mFakeActionBar = (FakeActionBar) inflater.inflate(R.layout.screen_fake_action_bar_standalone, (ViewGroup) null);
        this.mScreenMap = new HashMap();
        this.mBackStack = new Stack<>();
        this.mAppSessionListeners = new ArrayList();
        this.mResult = new Bundle();
        initialize();
    }

    public ScreenManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mFakeActionBar = (FakeActionBar) inflater.inflate(R.layout.screen_fake_action_bar_standalone, (ViewGroup) null);
        this.mScreenMap = new HashMap();
        this.mBackStack = new Stack<>();
        this.mAppSessionListeners = new ArrayList();
        this.mResult = new Bundle();
        initialize();
    }

    @TargetApi(21)
    public ScreenManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mFakeActionBar = (FakeActionBar) inflater.inflate(R.layout.screen_fake_action_bar_standalone, (ViewGroup) null);
        this.mScreenMap = new HashMap();
        this.mBackStack = new Stack<>();
        this.mAppSessionListeners = new ArrayList();
        this.mResult = new Bundle();
        initialize();
    }

    public void onResume() {
        for (AppSessionListener listener : this.mAppSessionListeners) {
            this.mAppController.addListener(listener);
        }
        Activity activity = (Activity) getContext();
        activity.getWindowManager().addView(this.mFakeActionBar, this.mFakeActionBar.getLayoutParams());
        Screen screen = getActiveScreen();
        if (screen != null) {
            screen.onResume();
        }
    }

    public void onPause() {
        for (AppSessionListener listener : this.mAppSessionListeners) {
            this.mAppController.removeListener(listener);
        }
        Activity activity = (Activity) getContext();
        activity.getWindowManager().removeView(this.mFakeActionBar);
    }

    public void initialize(AppController appController) {
        this.mAppController = appController;
    }

    public void registerAppSessionListener(AppSessionListener listener) {
        AppController appController = this.mAppController;
        if (appController != null) {
            this.mAppSessionListeners.add(listener);
            appController.addListener(listener);
        } else {
            SLog.w("Attempting to add an AppSessionListener before the ScreenManager has been initialized. Ignoring request.");
        }
    }

    public void addScreen(String tag, Screen screen, Bundle initialData) {
        if (!TextUtils.isEmpty(tag) && screen != null) {
            if (initialData == null) {
                initialData = new Bundle();
            }
            removeScreen(tag);
            this.mScreenMap.put(tag, screen);
            ViewUtil.disableAndHide(screen);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.gravity = 80;
            addView(screen, layoutParams);
            screen.onInitialize(this, this.mAppController, initialData);
        }
    }

    public void removeScreen(String tag) {
        if (!TextUtils.isEmpty(tag) && this.mScreenMap.containsKey(tag)) {
            View view = this.mScreenMap.remove(tag);
            removeView(view);
        }
    }

    public void showScreen(String tag) {
        if (!TextUtils.isEmpty(tag) && this.mScreenMap.containsKey(tag)) {
            this.mTouchEnabled = false;
            Screen currentScreen = getCurrentScreen();
            AnimatorSet currentScreenHideAnimatorSet = null;
            if (currentScreen != null) {
                currentScreenHideAnimatorSet = currentScreen.getHideAnimatorSet();
                currentScreen.onHide();
            }
            Screen screen = this.mScreenMap.get(tag);
            AnimatorSet nextScreenShowAnimatorSet = screen.getShowAnimatorSet();
            nextScreenShowAnimatorSet.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.ScreenManager.1
                @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    ScreenManager.this.mTouchEnabled = true;
                }
            });
            this.mBackStack.push(tag);
            showScreen(screen);
            if (currentScreenHideAnimatorSet != null) {
                currentScreenHideAnimatorSet.start();
            }
            nextScreenShowAnimatorSet.start();
        }
    }

    public void popScreen() {
        this.mTouchEnabled = false;
        AnimatorSet currentScreenHideAnimatorSet = null;
        if (!this.mBackStack.isEmpty()) {
            String tag = this.mBackStack.peek();
            Screen currentScreen = this.mScreenMap.get(tag);
            currentScreenHideAnimatorSet = currentScreen.getHideAnimatorSet();
            currentScreen.onHide();
            this.mBackStack.pop();
        }
        AnimatorSet previousScreenShowAnimatorSet = null;
        if (!this.mBackStack.isEmpty()) {
            String previousTag = this.mBackStack.peek();
            Screen previousScreen = this.mScreenMap.get(previousTag);
            previousScreenShowAnimatorSet = previousScreen.getShowAnimatorSet();
            showScreen(previousScreen);
        }
        if (currentScreenHideAnimatorSet != null) {
            currentScreenHideAnimatorSet.start();
        }
        if (previousScreenShowAnimatorSet != null) {
            previousScreenShowAnimatorSet.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.ScreenManager.2
                @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    ScreenManager.this.mTouchEnabled = true;
                }
            });
            previousScreenShowAnimatorSet.start();
        }
    }

    public Screen getActiveScreen() {
        if (this.mBackStack.isEmpty()) {
            return null;
        }
        return this.mScreenMap.get(this.mBackStack.peek());
    }

    public FakeActionBar getFakeActionBar() {
        return this.mFakeActionBar;
    }

    public void setScreenResult(Bundle bundle) {
        this.mResult.clear();
        if (bundle != null) {
            this.mResult.putAll(bundle);
        }
    }

    public boolean onBackPressed() {
        Screen screen = getActiveScreen();
        if (screen == null) {
            return false;
        }
        boolean handled = screen.onBack();
        if (handled) {
            return true;
        }
        if (this.mBackStack.size() == 1) {
            return false;
        }
        Util.setSoftKeyboardVisibility(getContext(), this, false);
        popScreen();
        return true;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !this.mTouchEnabled;
    }

    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Screen activeShareScreen = getActiveScreen();
        if (activeShareScreen != null) {
            return activeShareScreen.onActivityResult(activity, requestCode, resultCode, data);
        }
        return false;
    }

    private void initialize() {
        this.mFakeActionBar.setTopOfWindowLayoutParams();
        setBackgroundColor(0);
        this.mFakeActionBar.setOnActionListener(new FakeActionBar.OnActionListener() { // from class: co.vine.android.share.screens.ScreenManager.3
            @Override // co.vine.android.share.widgets.FakeActionBar.OnActionListener
            public void onBackPressed() {
                boolean handled = false;
                Screen screen = ScreenManager.this.getActiveScreen();
                if (screen != null) {
                    handled = screen.onBack();
                }
                if (!handled) {
                    Activity activity = (Activity) ScreenManager.this.getContext();
                    activity.onBackPressed();
                }
            }
        });
    }

    public void onSaveInstanceState(Bundle bundle) {
        for (Screen screen : this.mScreenMap.values()) {
            screen.onSaveInstanceState(bundle);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        for (Screen screen : this.mScreenMap.values()) {
            screen.onRestoreInstanceState(savedInstanceState);
        }
    }

    private void fireScreenFakeActionBarOnBind(final Screen screen) {
        this.mFakeActionBar.setLabelText(null);
        Animator hideAnimator = this.mFakeActionBar.getHideAnimator();
        hideAnimator.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.ScreenManager.4
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                screen.onBindFakeActionBar(ScreenManager.this.mFakeActionBar);
            }
        });
        Animator showAnimator = this.mFakeActionBar.getShowAnimator();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(hideAnimator, showAnimator);
        animatorSet.start();
    }

    private void showScreen(Screen screen) {
        ViewUtil.enableAndShow(screen);
        fireScreenFakeActionBarOnBind(screen);
        Bundle previousScreenResult = new Bundle(this.mResult);
        setScreenResult(null);
        screen.onShow(previousScreenResult);
    }

    private Screen getCurrentScreen() {
        if (this.mBackStack.isEmpty()) {
            return null;
        }
        String tag = this.mBackStack.peek();
        Screen currentScreen = this.mScreenMap.get(tag);
        return currentScreen;
    }
}
