package com.jeremyfeinstein.slidingmenu.lib;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.party.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/* loaded from: classes.dex */
public class SlidingActivityHelper {
    private Activity mActivity;
    private SlidingMenu mSlidingMenu;
    private View mViewAbove;
    private View mViewBehind;
    private boolean mBroadcasting = false;
    private boolean mOnPostCreateCalled = false;
    private boolean mEnableSlide = true;

    public SlidingActivityHelper(Activity activity) {
        this.mActivity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        this.mSlidingMenu = (SlidingMenu) LayoutInflater.from(this.mActivity).inflate(R.layout.slidingmenumain, (ViewGroup) null);
    }

    public void onPostCreate(Bundle savedInstanceState) {
        final boolean open;
        final boolean secondary;
        if (this.mViewBehind == null || this.mViewAbove == null) {
            throw new IllegalStateException("Both setBehindContentView must be called in onCreate in addition to setContentView.");
        }
        this.mOnPostCreateCalled = true;
        this.mSlidingMenu.attachToActivity(this.mActivity, this.mEnableSlide ? 0 : 1);
        if (savedInstanceState != null) {
            open = savedInstanceState.getBoolean("SlidingActivityHelper.open");
            secondary = savedInstanceState.getBoolean("SlidingActivityHelper.secondary");
        } else {
            open = false;
            secondary = false;
        }
        new Handler().post(new Runnable() { // from class: com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.1
            @Override // java.lang.Runnable
            public void run() {
                if (!open) {
                    SlidingActivityHelper.this.mSlidingMenu.showContent(false);
                } else if (secondary) {
                    SlidingActivityHelper.this.mSlidingMenu.showSecondaryMenu(false);
                } else {
                    SlidingActivityHelper.this.mSlidingMenu.showMenu(false);
                }
            }
        });
    }

    public void setSlidingActionBarEnabled(boolean slidingActionBarEnabled) {
        if (this.mOnPostCreateCalled) {
            throw new IllegalStateException("enableSlidingActionBar must be called in onCreate.");
        }
        this.mEnableSlide = slidingActionBarEnabled;
    }

    public View findViewById(int id) {
        View v;
        if (this.mSlidingMenu == null || (v = this.mSlidingMenu.findViewById(id)) == null) {
            return null;
        }
        return v;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("SlidingActivityHelper.open", this.mSlidingMenu.isMenuShowing());
        outState.putBoolean("SlidingActivityHelper.secondary", this.mSlidingMenu.isSecondaryMenuShowing());
    }

    public void registerAboveContentView(View v, ViewGroup.LayoutParams params) {
        if (!this.mBroadcasting) {
            this.mViewAbove = v;
        }
    }

    public void setBehindContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mViewBehind = view;
        this.mSlidingMenu.setMenu(this.mViewBehind);
    }

    public SlidingMenu getSlidingMenu() {
        return this.mSlidingMenu;
    }

    public void toggle() {
        this.mSlidingMenu.toggle();
    }

    public void showContent() {
        this.mSlidingMenu.showContent();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.mSlidingMenu.isMenuShowing()) {
            return false;
        }
        showContent();
        return true;
    }

    public static class MenuStateHandler implements View.OnClickListener, View.OnDragListener, SlidingMenu.OnCloseListener, SlidingMenu.OnClosedListener, SlidingMenu.OnOpenListener, SlidingMenu.OnOpenedListener {
        private final MenuStateListener mListener;

        public interface MenuStateListener {
            void onMenuClick(View view);

            void onMenuClose();

            void onMenuClosed();

            boolean onMenuDrag(View view, DragEvent dragEvent);

            void onMenuOpen();

            void onMenuOpened();
        }

        public MenuStateHandler(MenuStateListener listener) {
            this.mListener = listener;
        }

        @Override // android.view.View.OnDragListener
        public boolean onDrag(View v, DragEvent event) {
            return this.mListener.onMenuDrag(v, event);
        }

        @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener
        public void onClosed() {
            this.mListener.onMenuClosed();
        }

        @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener
        public void onClose() {
            this.mListener.onMenuClose();
        }

        @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener
        public void onOpened() {
            this.mListener.onMenuOpened();
        }

        @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener
        public void onOpen() {
            this.mListener.onMenuOpen();
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            this.mListener.onMenuClick(v);
        }
    }

    public void setMenuStateListener(MenuStateHandler.MenuStateListener listener) {
        MenuStateHandler handler = new MenuStateHandler(listener);
        this.mSlidingMenu.setOnClosedListener(handler);
        this.mSlidingMenu.setOnCloseListener(handler);
        this.mSlidingMenu.setOnOpenedListener(handler);
        this.mSlidingMenu.setOnOpenListener(handler);
        this.mSlidingMenu.setOnClickListener(handler);
        this.mSlidingMenu.setOnDragListener(handler);
    }
}
