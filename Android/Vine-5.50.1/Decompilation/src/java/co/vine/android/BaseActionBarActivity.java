package co.vine.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.client.AppController;
import co.vine.android.client.SessionManager;
import co.vine.android.plugin.BasePluginManager;
import co.vine.android.plugin.DebugReceiverPlugin;
import co.vine.android.plugin.DebugSubPluginManager;
import co.vine.android.plugin.DebugTasksPlugin;
import co.vine.android.plugin.Plugin;
import co.vine.android.plugin.ReportBugPlugin;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.MuteUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.AnalyticsPlugin;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.FakeActionBar;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesSpan;
import com.android.debug.hv.ViewServer;
import com.edisonwang.android.slog.SLog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import java.util.ArrayList;
import java.util.Collection;

/* loaded from: classes.dex */
public abstract class BaseActionBarActivity extends AppCompatActivity implements SlidingActivityHelper.MenuStateHandler.MenuStateListener {
    private static final IntentFilter FINISH_INTENT_FILTER = new IntentFilter();
    private FakeActionBar mFakeActionBar;
    private final BroadcastReceiver mFinishReceiver;
    protected Menu mMenu;
    protected ProgressDialog mProgressDialog;
    private SlidingActivityHelper mSlidingMenuHelper;
    private int mVineGreen;
    protected final SessionManager mSessionManager = SessionManager.getSharedInstance();
    private final BasePluginManager<Plugin> mDefaultPluginManager = new BasePluginManager<>();

    public BaseActionBarActivity() {
        this.mDefaultPluginManager.addPlugin(new AnalyticsPlugin());
        this.mFinishReceiver = new BroadcastReceiver() { // from class: co.vine.android.BaseActionBarActivity.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent != null && "co.vine.android.FINISH".equals(intent.getAction())) {
                    BaseActionBarActivity.this.finish();
                }
            }
        };
    }

    static {
        FINISH_INTENT_FILTER.addAction("co.vine.android.FINISH");
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        try {
            return super.onCreatePanelMenu(featureId, menu);
        } catch (Exception e) {
            CrashUtil.logException(e);
            return false;
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        FlurryUtils.start(this, Util.getPackageVersionName(this), BuildUtil.isProduction(), AppController.getInstance(this).getActiveId());
        this.mDefaultPluginManager.onStart(this);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        FlurryUtils.stop(this);
        this.mDefaultPluginManager.onStop(this);
    }

    public boolean isFakeActionBarEnabled() {
        return false;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        throw new IllegalArgumentException("If extending BaseActivity, please use multiple parameter version of this method.");
    }

    protected void restoreActivityState() {
        if (BuildUtil.isLogsOn()) {
            Log.d("BaseActionBarActivity", "Restoring activity " + getClass().getName());
        }
    }

    public void preSetContentView() {
    }

    @Override // android.support.v7.app.AppCompatActivity, android.app.Activity
    public void onPostCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onPostCreate(savedInstanceState);
        if (this.mFakeActionBar != null) {
            this.mFakeActionBar.onPostCreate();
            applyDefaultTitleViewStylingToFakeActionBar();
        }
        if (this.mSlidingMenuHelper != null) {
            this.mSlidingMenuHelper.onPostCreate(savedInstanceState);
        }
    }

    protected void applyDefaultTitleViewStylingToFakeActionBar() {
        Resources res = getResources();
        FakeActionBar fakeActionBar = getFakeActionBar();
        fakeActionBar.getTitleView().setTextColor(res.getColor(R.color.primary_text));
        fakeActionBar.getTitleView().setTypeface(Typefaces.get(this).mediumContent);
        fakeActionBar.getTitleView().setTextSize(0, res.getDimensionPixelSize(R.dimen.font_size_large));
    }

    @Override // android.app.Activity
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null) {
            if (this.mSlidingMenuHelper != null) {
                return this.mSlidingMenuHelper.findViewById(id);
            }
            return null;
        }
        return v;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.app.Activity
    public void setContentView(int layoutResID) {
        if (this.mSlidingMenuHelper != null || this.mFakeActionBar != null) {
            setContentView(getLayoutInflater().inflate(layoutResID, (ViewGroup) null));
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.app.Activity
    public void setContentView(View v) {
        if (this.mSlidingMenuHelper != null || this.mFakeActionBar != null) {
            setContentView(v, new ViewGroup.LayoutParams(-1, -1));
        } else {
            super.setContentView(v);
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.app.Activity
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        if (this.mFakeActionBar != null) {
            this.mFakeActionBar.setContentView(v, params);
            v = this.mFakeActionBar.getRoot();
        }
        super.setContentView(v, params);
        if (this.mSlidingMenuHelper != null) {
            this.mSlidingMenuHelper.registerAboveContentView(v, params);
        }
    }

    public SlidingMenu getSlidingMenu() {
        if (this.mSlidingMenuHelper != null) {
            return this.mSlidingMenuHelper.getSlidingMenu();
        }
        return null;
    }

    public SlidingActivityHelper getSlidingMenuHelper() {
        return this.mSlidingMenuHelper;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mSlidingMenuHelper != null) {
            boolean b = this.mSlidingMenuHelper.onKeyUp(keyCode, event);
            if (b) {
                return true;
            }
        }
        if (this.mDefaultPluginManager.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mDefaultPluginManager.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public View setBehindContentView(View v) {
        setBehindContentView(v, new ViewGroup.LayoutParams(-1, -1));
        return v;
    }

    public View setBehindContentView(View v, ViewGroup.LayoutParams params) {
        this.mSlidingMenuHelper.setBehindContentView(v, params);
        return v;
    }

    public View setBehindContentView(int id) {
        View backView = getLayoutInflater().inflate(id, (ViewGroup) null);
        setBehindContentView(backView);
        return backView;
    }

    public void onCreate(Bundle savedInstanceState, int layout, boolean loginRequired, boolean readOnly) {
        try {
            super.onCreate(savedInstanceState);
        } catch (AndroidRuntimeException e) {
            CrashUtil.logException(e);
        } catch (NoSuchFieldError e2) {
            CrashUtil.logException(e2);
        }
        this.mVineGreen = getResources().getColor(R.color.vine_green);
        if (isSlidingMenuEnabled()) {
            SLog.i("Sliding menu is enabled.");
            this.mSlidingMenuHelper = createSlidingMenuHelper();
            this.mSlidingMenuHelper.onCreate(savedInstanceState);
            this.mSlidingMenuHelper.setMenuStateListener(this);
        }
        if (isFakeActionBarEnabled()) {
            this.mFakeActionBar = new FakeActionBar(this);
            this.mFakeActionBar.onCreate();
        }
        preSetContentView();
        setContentView(layout);
        setUpTitleBar();
        Activity parent = getParent();
        boolean needsToLogin = loginRequired && !this.mSessionManager.isLoggedIn();
        if (needsToLogin && parent == null) {
            StartActivity.toStart(this);
        }
        if (SystemUtil.isViewServerEnabled(this)) {
            ViewServer.get(this).addWindow(this);
        }
        registerReceiver(this.mFinishReceiver, FINISH_INTENT_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
        this.mDefaultPluginManager.addPlugin(new DebugSubPluginManager(onCreateDebugPlugins()));
        this.mDefaultPluginManager.addPlugins(onCraeteDefaultPlugins());
        if (BuildUtil.isNonPublicBuild()) {
            this.mDefaultPluginManager.addPlugin(new ReportBugPlugin());
        }
        this.mDefaultPluginManager.onActivityCreated(this);
    }

    private void setUpTitleBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (isFakeActionBarEnabled()) {
                actionBar.hide();
            } else if (BuildUtil.isApi21Lollipop()) {
                actionBar.setElevation(0.0f);
            }
        }
    }

    protected Collection<Plugin> onCreateDebugPlugins() {
        ArrayList<Plugin> debugPlugins = new ArrayList<>();
        debugPlugins.add(new DebugReceiverPlugin());
        if (SLog.sLogsOn || CrossConstants.IS_BETA_BUILD) {
            debugPlugins.add(new DebugTasksPlugin());
        }
        return debugPlugins;
    }

    protected ArrayList<Plugin> onCraeteDefaultPlugins() {
        return new ArrayList<>();
    }

    protected SlidingActivityHelper createSlidingMenuHelper() {
        return new SlidingActivityHelper(this);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mSlidingMenuHelper != null) {
            this.mSlidingMenuHelper.onSaveInstanceState(outState);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        updateMuteActionState(menu.findItem(R.id.audio));
        this.mMenu = menu;
        if (this.mFakeActionBar != null) {
            this.mFakeActionBar.onCreateOptionsMenu(this.mMenu, true);
        }
        this.mDefaultPluginManager.onCreateOptionsMenu(menu);
        return true;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (getSlidingMenu() != null && getSlidingMenu().isMenuShowing()) {
            sendBroadcast(new Intent(MuteUtil.ACTION_CHANGED_TO_MUTE), CrossConstants.BROADCAST_PERMISSION);
        } else if (!MuteUtil.isMuted(this)) {
            sendBroadcast(new Intent(MuteUtil.ACTION_CHANGED_TO_UNMUTE), CrossConstants.BROADCAST_PERMISSION);
        }
        CrashUtil.set("Activity", getClass().getName() + " - " + System.currentTimeMillis());
        if (BuildUtil.isLogsOn() && SystemUtil.isViewServerEnabled(this)) {
            ViewServer.get(this).setFocusedWindow(this);
        }
        if (this.mMenu != null) {
            updateMuteActionState(this.mMenu.findItem(R.id.audio));
        }
        this.mDefaultPluginManager.onResume(this);
        registerReceiver(this.mFinishReceiver, FINISH_INTENT_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!this.mDefaultPluginManager.onActivityResult(this, requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        dismissDialog();
        this.mDefaultPluginManager.onPause();
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (SystemUtil.isViewServerEnabled(this)) {
            ViewServer.get(this).removeWindow(this);
        }
        this.mDefaultPluginManager.onDestroy();
        unregisterReceiver(this.mFinishReceiver);
    }

    public void updateMuteActionState(MenuItem menu) {
        if (menu != null) {
            boolean isMuted = MuteUtil.isMuted(this);
            SLog.d("Is muted? {}", Boolean.valueOf(isMuted));
            menu.setTitle(isMuted ? R.string.unmute : R.string.mute);
        }
    }

    @TargetApi(14)
    private void setupActionBarJBOnly(Boolean setHomeButtonEnabled, Boolean setDisplayShowTitleEnabled, Boolean setDisplayHomeAsUpEnabled) {
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            if (setHomeButtonEnabled != null) {
                actionBar.setHomeButtonEnabled(setHomeButtonEnabled.booleanValue());
            }
            if (setDisplayShowTitleEnabled != null) {
                actionBar.setDisplayShowTitleEnabled(setDisplayShowTitleEnabled.booleanValue());
            }
            if (setDisplayHomeAsUpEnabled != null) {
                actionBar.setDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnabled.booleanValue());
            }
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        try {
            if (!this.mDefaultPluginManager.onBackPressed()) {
                super.onBackPressed();
            }
        } catch (IllegalStateException e) {
            CrashUtil.logException(e);
        }
    }

    protected void setupActionBar(Boolean setHomeButtonEnabled, Boolean setDisplayShowTitleEnabled, int titleRes, Boolean setDisplayHomeAsUpEnabled, Boolean setDisplayLogoEnabled) {
        String title = titleRes > 0 ? getString(titleRes) : null;
        setupActionBar(setHomeButtonEnabled, setDisplayShowTitleEnabled, title, setDisplayHomeAsUpEnabled, setDisplayLogoEnabled);
    }

    protected void setupActionBar(Boolean setHomeButtonEnabled, Boolean setDisplayShowTitleEnabled, String title, Boolean setDisplayHomeAsUpEnabled, Boolean setDisplayLogoEnabled) {
        if (this.mFakeActionBar != null) {
            if (setHomeButtonEnabled != null) {
                this.mFakeActionBar.setHomeButtonEnabled(setHomeButtonEnabled);
            }
            if (setDisplayShowTitleEnabled != null) {
                this.mFakeActionBar.setDisplayShowTitleEnabled(setDisplayShowTitleEnabled);
            }
            if (setDisplayHomeAsUpEnabled != null) {
                this.mFakeActionBar.setDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnabled);
            }
            if (!TextUtils.isEmpty(title)) {
                this.mFakeActionBar.setTitle(title);
            }
            if (setDisplayLogoEnabled != null) {
                this.mFakeActionBar.setDisplayLogoEnabled(setDisplayLogoEnabled);
                return;
            }
            return;
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            if (setHomeButtonEnabled != null) {
                ab.setHomeButtonEnabled(setHomeButtonEnabled.booleanValue());
            }
            if (setDisplayShowTitleEnabled != null) {
                ab.setDisplayShowTitleEnabled(setDisplayShowTitleEnabled.booleanValue());
            }
            if (setDisplayHomeAsUpEnabled != null) {
                ab.setDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnabled.booleanValue());
            }
            if (!TextUtils.isEmpty(title) && Build.VERSION.SDK_INT >= 19) {
                SpannableString spannable = new SpannableString(title);
                TypefacesSpan medium = new TypefacesSpan(Typefaces.get(this).getContentTypeface(0, 3));
                spannable.setSpan(medium, 0, title.length(), 33);
                ab.setTitle(spannable);
            }
            if (!setDisplayLogoEnabled.booleanValue()) {
                ab.setIcon(android.R.color.transparent);
            }
        }
        if (Build.VERSION.SDK_INT >= 14) {
            setupActionBarJBOnly(setHomeButtonEnabled, setDisplayShowTitleEnabled, setDisplayHomeAsUpEnabled);
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mDefaultPluginManager.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == 16908332) {
            if (this.mSlidingMenuHelper != null) {
                this.mSlidingMenuHelper.toggle();
                return true;
            }
            try {
                super.onBackPressed();
                return true;
            } catch (IllegalStateException e) {
                return true;
            }
        }
        if (id == R.id.capture) {
            AppImpl.getInstance().startCapture(this);
            return true;
        }
        if (id == R.id.audio) {
            MuteUtil.setMuted(this, !MuteUtil.isMuted(this));
            updateMuteActionState(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public FakeActionBar getFakeActionBar() {
        return this.mFakeActionBar;
    }

    public void setActionBarColor(int channelBackground) {
        if (channelBackground != 0) {
            if (this.mFakeActionBar != null) {
                this.mFakeActionBar.setActionBarColor(channelBackground | ViewCompat.MEASURED_STATE_MASK);
                this.mFakeActionBar.getActionBar().invalidate();
            } else {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(channelBackground | ViewCompat.MEASURED_STATE_MASK));
            }
            if (this instanceof HomeTabActivity) {
                if (channelBackground == Settings.DEFAULT_PROFILE_COLOR || channelBackground <= 0 || channelBackground == (Settings.DEFAULT_PROFILE_COLOR | ViewCompat.MEASURED_STATE_MASK)) {
                    ((HomeTabActivity) this).setProfileColor(-1);
                } else {
                    ((HomeTabActivity) this).setProfileColor(channelBackground | ViewCompat.MEASURED_STATE_MASK);
                }
            }
        } else if (this.mVineGreen != 0) {
            setActionBarColor(this.mVineGreen);
        }
        invalidateOptionsMenu();
    }

    public void hideActionBar() {
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
    }

    public void setActionBarColor() {
        setActionBarColor(this.mVineGreen);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dismissDialog() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    protected boolean isSlidingMenuEnabled() {
        return false;
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public boolean onMenuDrag(View v, DragEvent event) {
        return false;
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public void onMenuClosed() {
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public void onMenuClose() {
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public void onMenuOpened() {
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public void onMenuOpen() {
    }

    @Override // com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public void onMenuClick(View v) {
    }
}
