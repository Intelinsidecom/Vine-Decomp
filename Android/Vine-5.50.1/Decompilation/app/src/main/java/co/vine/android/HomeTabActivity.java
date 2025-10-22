package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import co.vine.android.api.VineRTCConversation;
import co.vine.android.api.VineRTCParticipant;
import co.vine.android.api.VineSingleNotification;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.drawable.CenteredTextDrawable;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.prefetch.PrefetchManager;
import co.vine.android.scribe.AppNavigationProvider;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.ViewImpressionScribeLogger;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.search.SearchActivity;
import co.vine.android.service.GCMRegistrationService;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.remoterequestcontrol.RemoteRequestControlActionListener;
import co.vine.android.util.AppTrackingUtil;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.MuteUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.OnboardHelper;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.tabs.IconTabHost;
import co.vine.android.widget.tabs.ScrollAwayTabWidget;
import co.vine.android.widget.tabs.TabIndicator;
import co.vine.android.widget.tabs.TabInfo;
import co.vine.android.widget.tabs.TabsAdapter;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.edisonwang.android.slog.SLog;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.googlecode.javacv.cpp.avformat;
import com.twitter.android.widget.TopScrollable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class HomeTabActivity extends BaseTimelineActivity implements View.OnLongClickListener, TabHost.OnTabChangeListener, IconTabHost.OnTabClickedListener {
    public static final String ACTION_VERIFICATION_COMPLETE = BuildUtil.getAuthority() + ".EMAIL_VERIFIED";
    private static final IntentFilter ACTIVITY_COUNT_FILTER = new IntentFilter();
    private int mActivityCount;
    private int mDisabledTabColor;
    private int mEnabledTabColor;
    private boolean mExploreRefreshReciverIsRegistered;
    private MenuItem mInboxMenu;
    private boolean mIsNewUser;
    private boolean mIsResumed;
    private int mLastItem;
    private LongSparseArray<Long> mLastMessageMap;
    private Menu mMenu;
    private int mMessageCount;
    private boolean mPostMosaicDismissed;
    private String mRecentTab;
    private boolean mShouldShowMenu;
    private boolean mShowActivityFeed;
    private Thread mStartUploadServiceThread;
    private IconTabHost mTabHost;
    private TabsAdapter mTabsAdapter;
    private ViewPager mViewPager;
    private final int WEEK_IN_MILLIS = 604800000;
    private final RemoteRequestControlActionListener mRemoteRequestListener = new RemoteRequestControlActionListener() { // from class: co.vine.android.HomeTabActivity.1
        @Override // co.vine.android.service.components.remoterequestcontrol.RemoteRequestControlActionListener
        public void onAbortAllRequestsComplete() {
            HomeTabActivity.this.mAppController.fetchActivityCounts();
        }
    };
    private final BroadcastReceiver mExploreRefreshReciver = new BroadcastReceiver() { // from class: co.vine.android.HomeTabActivity.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            TabInfo tab = HomeTabActivity.this.mTabsAdapter.getTab(2);
            if (tab != null) {
                Fragment fragment = tab.fragment();
                if (fragment instanceof ExploreFragment) {
                    ((ExploreFragment) fragment).reloadWebView();
                }
            }
        }
    };
    private boolean mScrollAwayNavEnabled = true;
    private final Runnable mRefreshCountRunnable = new Runnable() { // from class: co.vine.android.HomeTabActivity.3
        @Override // java.lang.Runnable
        public void run() {
            HomeTabActivity.this.invalidateMessagesBadge();
            TabsAdapter adapter = HomeTabActivity.this.mTabsAdapter;
            if (adapter != null && (adapter.getCurrentFragment() instanceof ActivityFragment)) {
                HomeTabActivity.this.markActivitiesRead();
            }
            HomeTabActivity.this.invalidateActivityBadge();
        }
    };
    private final BroadcastReceiver mActivityCountReceiver = new BroadcastReceiver() { // from class: co.vine.android.HomeTabActivity.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            HomeTabActivity.this.mMessageCount = intent.getIntExtra("messages_count", 0);
            HomeTabActivity.this.mActivityCount = intent.getIntExtra("notifications_count", 0);
            HomeTabActivity.this.runOnUiThread(HomeTabActivity.this.mRefreshCountRunnable);
        }
    };
    private int mTabOnOpen = 0;

    static {
        ACTIVITY_COUNT_FILTER.addAction("co.vine.android.service.activityCounts");
    }

    public static void startInExplore(Context context) {
        context.startActivity(new Intent(context, (Class<?>) HomeTabActivity.class).addFlags(avformat.AVFMT_SEEK_TO_PTS).setAction("android.intent.action.VIEW").putExtra("startTab", "explore"));
    }

    @Override // co.vine.android.BaseTimelineActivity
    protected boolean isAutoCaptureIconEnabled() {
        return false;
    }

    @Override // co.vine.android.BaseControllerActionBarActivity
    public boolean isConversationSideMenuEnabled() {
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public void onMenuClose() {
        if (!MuteUtil.isMuted(this)) {
            sendBroadcast(new Intent(MuteUtil.ACTION_CHANGED_TO_UNMUTE), CrossConstants.BROADCAST_PERMISSION);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, com.jeremyfeinstein.slidingmenu.lib.SlidingActivityHelper.MenuStateHandler.MenuStateListener
    public void onMenuOpened() {
        super.onMenuOpened();
        invalidateMessagesBadge();
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    public void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onCreate(savedInstanceState, R.layout.main, true);
        if (BuildUtil.isExplore()) {
            setupTabs();
        }
        if (SLog.sLogsOn) {
            sendBroadcast(new Intent("co.vine.android.debug_receiver"));
        }
        SLog.i("HomeTabActivity pid: {}", Integer.valueOf(Process.myPid()));
        setRequestedOrientation(1);
        setupActionBar((Boolean) false, (Boolean) null, (String) null, (Boolean) null, (Boolean) true);
        this.mAppSessionListener = new HomeTabSessionListener();
        this.mLastMessageMap = new LongSparseArray<>();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            this.mIsNewUser = intent.getBooleanExtra("is_new_user", false);
            String startTab = intent.getStringExtra("startTab");
            if (startTab != null) {
                if (startTab.contains("explore")) {
                    this.mTabOnOpen = 2;
                } else if (startTab.contains("profile")) {
                    this.mTabOnOpen = 3;
                } else {
                    this.mTabOnOpen = 0;
                }
            }
        }
        if (this.mAppController == null) {
            this.mAppController = AppController.getInstance(this);
        }
        Context appContext = getApplicationContext();
        AppNavigationProvider appNavProvider = AppNavigationProviderSingleton.getInstance();
        appNavProvider.setViewChangedListener(new ViewImpressionScribeLogger(ScribeLoggerSingleton.getInstance(appContext), AppStateProviderSingleton.getInstance(appContext), appNavProvider));
        Intent i = getIntent();
        if (this.mAppController.isLoggedIn() || StartActivity.actionAllowedWhenLoggedOut(i)) {
            if (this.mAppController.isLoggedIn()) {
                AccountManager am = AccountManager.get(this);
                Account acc = VineAccountHelper.getActiveAccount(this);
                VineAccountHelper.migratePassword(am, acc, this);
            }
            IconTabHost tabHost = this.mTabHost;
            this.mViewPager = (ViewPager) findViewById(R.id.pager);
            this.mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.home_pager_margin));
            this.mViewPager.setPageMarginDrawable(R.color.bg_grouped_list);
            this.mViewPager.setOffscreenPageLimit(3);
            this.mTabsAdapter = new HomeTabTabsAdapter(tabHost);
            LayoutInflater inflater = getLayoutInflater();
            tabHost.setOnTabChangedListener(this);
            tabHost.setOnTabClickedListener(this);
            processStartIntent(intent);
            Resources res = getResources();
            this.mDisabledTabColor = res.getColor(R.color.solid_dark_gray);
            this.mEnabledTabColor = -1;
            Bundle timelineBundle = new Bundle();
            timelineBundle.putInt("empty_desc", R.string.failed_to_load_posts);
            this.mTabsAdapter.addTab(newTabSpec(inflater, R.layout.activity_tab_indicator, R.drawable.ic_home, "home"), HomeTimelineFragment.class, timelineBundle);
            restoreActivityState();
            Bundle activityBundle = new Bundle();
            activityBundle.putBoolean("take_focus", this.mShowActivityFeed);
            activityBundle.putInt("empty_desc", R.string.activity_empty);
            this.mTabsAdapter.addTab(newTabSpec(inflater, R.layout.activity_tab_indicator, R.drawable.ic_activity, "activity"), ActivityFragment.class, activityBundle);
            Bundle exploreBundle = new Bundle();
            if (TextUtils.equals(this.mRecentTab, "explore")) {
                exploreBundle.putBoolean("take_focus", true);
            }
            TabHost.TabSpec tabSpec = newTabSpec(inflater, R.layout.explore_icon_tab_indicator, R.drawable.ic_explore, "explore");
            if (ClientFlagsHelper.isExploreGridEnabled(getApplicationContext())) {
                this.mTabsAdapter.addTab(tabSpec, GridExploreFragment.class, exploreBundle);
            } else {
                this.mTabsAdapter.addTab(tabSpec, ExploreFragment.class, exploreBundle);
            }
            Bundle profileBundle = new Bundle();
            if (TextUtils.equals(this.mRecentTab, "profile")) {
                profileBundle.putBoolean("take_focus", true);
            }
            profileBundle.putLong("user_id", this.mAppController.getActiveId());
            profileBundle.putBoolean("refresh", true);
            this.mTabsAdapter.addTab(newTabSpec(inflater, R.layout.home_tab_indicator, R.drawable.ic_profile, "profile"), ProfileFragment.class, profileBundle);
            this.mTabHost.setCurrentTab(this.mTabOnOpen);
            Session session = this.mAppController.getActiveSession();
            this.mAppController.determineCleanup(session);
            this.mPostMosaicDismissed = false;
            this.mActivityCount = 0;
            DelayHandler handler = new DelayHandler(this);
            if (handler.hasMessages(1)) {
                handler.removeMessages(1);
            }
            if (handler.hasMessages(2)) {
                handler.removeMessages(2);
            }
            if (handler.hasMessages(4)) {
                handler.removeMessages(4);
            }
            if (!OnboardHelper.hasShownFavoriteUserOverlay(this)) {
                handler.sendMessageDelayed(handler.obtainMessage(4, null), 3000L);
            }
            handler.sendMessageDelayed(handler.obtainMessage(1, null), 60000L);
            handler.sendMessageDelayed(handler.obtainMessage(2, null), 60000L);
            handler.sendMessageDelayed(handler.obtainMessage(3, null), 60000L);
            AppImpl.getInstance().startCameraService(this);
            VineAccountHelper.syncNormalVideoPlayable(this);
            PrefetchManager.getInstance(this).delayNextPrefetch();
            try {
                int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if (errorCode == 0) {
                    startService(GCMRegistrationService.getRegisterIntent(this, this.mAppController.getActiveId()));
                }
            } catch (Throwable e) {
                CrashUtil.logException(e, "Failed to validate Google Player Service status.", new Object[0]);
            }
        }
    }

    private boolean shouldShowLinkTwitterPrompt() {
        return (!ClientFlagsHelper.isLinkTwitterPromptEnabled(this) || CommonUtil.getDefaultSharedPrefs(this).getBoolean("settings_twitter_connected", false) || CommonUtil.getDefaultSharedPrefs(this).getBoolean("linkTwitterShown", false)) ? false : true;
    }

    private boolean shouldShowFollowOnTwitterPrompt() {
        SharedPreferences prefs = CommonUtil.getDefaultSharedPrefs(this);
        boolean done = prefs.getBoolean("followerMigrationDone", false);
        if (done) {
            return false;
        }
        long lastPrompted = prefs.getLong("followerMigrationLastPrompted", 0L);
        return ClientFlagsHelper.isFollowerMigrationEnabled(this) && System.currentTimeMillis() - lastPrompted >= 604800000;
    }

    private TabHost.TabSpec newTabSpec(LayoutInflater inflater, int layout, int icon, String tag) {
        TabIndicator indicator = TabIndicator.newIconIndicator(inflater, layout, this.mTabHost, icon, 0);
        indicator.setColor(this.mDisabledTabColor);
        return this.mTabHost.newTabSpec(tag).setIndicator(indicator);
    }

    @Override // co.vine.android.BaseTimelineActivity, co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mIsResumed = false;
        if (!this.mExploreRefreshReciverIsRegistered) {
            this.mExploreRefreshReciverIsRegistered = true;
            registerReceiver(this.mExploreRefreshReciver, AppController.ACTION_UPDATED_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
        }
        try {
            unregisterReceiver(this.mActivityCountReceiver);
        } catch (IllegalArgumentException e) {
        }
        Components.remoteRequestControlComponent().removeListener(this.mRemoteRequestListener);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        Util.fetchClientFlags(this, true, true);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.ScrollListener
    public void onScroll(int delta) {
        if (this.mScrollAwayNavEnabled) {
            Fragment fragment = this.mTabsAdapter.getCurrentFragment();
            if (ClientFlagsHelper.showDiscoverStickyHeader(getApplicationContext()) && (fragment instanceof HomeTimelineFragment) && ((HomeTimelineFragment) fragment).mHasStickyHeader) {
                this.mScrollAwayNavEnabled = false;
            }
            ScrollAwayTabWidget tabWidget = (ScrollAwayTabWidget) this.mTabHost.getTabWidget();
            tabWidget.onScroll(delta);
            if (fragment instanceof BaseArrayListFragment) {
                ((BaseArrayListFragment) fragment).setNavBottom(tabWidget.getNavBottom());
            }
        }
    }

    public void setScrollAwayNavEnabled(boolean enabled) {
        this.mScrollAwayNavEnabled = enabled;
    }

    public boolean shouldChangeActionBarColor() {
        return this.mViewPager != null && this.mViewPager.getCurrentItem() == 3;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processStartIntent(intent);
    }

    public void showPage(int position, Fragment fragment) throws Resources.NotFoundException {
        if (this.mTabsAdapter != null && this.mViewPager != null) {
            this.mViewPager.setCurrentItem(position);
            if (fragment instanceof BaseArrayListFragment) {
                ((BaseArrayListFragment) fragment).onMoveTo(1);
            }
            TabInfo tab = this.mTabsAdapter.getTab(this.mViewPager.getCurrentItem());
            if (tab != null) {
                Fragment current = tab.fragment();
                if (current instanceof BaseArrayListFragment) {
                    ((BaseArrayListFragment) current).onMoveAway(-1);
                }
                resetNav();
                logTabChange(fragment.getTag());
            }
        }
    }

    public void resetNav() {
        ScrollAwayTabWidget widget;
        if (this.mTabHost != null && (widget = (ScrollAwayTabWidget) this.mTabHost.getTabWidget()) != null) {
            widget.resetScroll();
            widget.invalidate();
            int navBottom = widget.getNavBottom();
            Fragment fragment = this.mTabsAdapter.getCurrentFragment();
            if (fragment != null && (fragment instanceof BaseArrayListFragment)) {
                ((BaseArrayListFragment) fragment).setNavBottom(navBottom);
            }
            this.mScrollAwayNavEnabled = true;
        }
    }

    public void refreshTabs() {
        Intent intent = new Intent(this, getClass());
        intent.putExtra("startTab", this.mTabHost.getCurrentTabTag());
        finish();
        startActivity(intent);
    }

    public void markActivitiesRead() {
        this.mActivityCount = 0;
    }

    @Override // android.widget.TabHost.OnTabChangeListener
    public void onTabChanged(String tag) throws Resources.NotFoundException {
        if (SLog.sLogsOn) {
            SLog.d(Util.printCursorWindowStats());
        }
        changeTabColor(this.mLastItem);
        int tab = this.mTabHost.getCurrentTab();
        AppNavigationProviderSingleton.getInstance().setSection(getSectionFromTag(tag));
        this.mViewPager.setCurrentItem(tab);
        this.mLastItem = tab;
        this.mRecentTab = tag;
        if ("activity".equals(tag)) {
            markActivitiesRead();
            invalidateActivityBadge();
        }
        if ("home".equals(tag)) {
            setHomeNewCount(0);
        }
        getSlidingMenu().setTouchModeAbove(0);
        logTabChange(tag);
        ArrayList<TabInfo> tabs = this.mTabsAdapter.getTabs();
        BaseFragment fragment = (BaseFragment) tabs.get(this.mTabHost.getCurrentTab()).fragment();
        if (fragment == null) {
        }
    }

    private void logTabChange(String tag) {
        CrashUtil.set("Current Tab", tag + " - " + System.currentTimeMillis());
        if (tag != null && !tag.contains("switcher")) {
            FlurryUtils.trackTabChange(tag);
        }
    }

    public void showMenuIfNeeded() {
        if (this.mShouldShowMenu) {
            SLog.d("Menu requested.");
            this.mShouldShowMenu = false;
            getSlidingMenu().showMenu(false);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.mExploreRefreshReciverIsRegistered) {
            this.mExploreRefreshReciverIsRegistered = false;
            unregisterReceiver(this.mExploreRefreshReciver);
        }
    }

    private synchronized Thread getUploadServiceThread() {
        Thread thread;
        if (this.mStartUploadServiceThread == null) {
            this.mStartUploadServiceThread = new Thread(new Runnable() { // from class: co.vine.android.HomeTabActivity.5
                @Override // java.lang.Runnable
                public void run() {
                    Intent i = AppImpl.getInstance().getNotifyFailedIntent(HomeTabActivity.this);
                    if (i != null) {
                        HomeTabActivity.this.startService(i);
                    }
                    HomeTabActivity.this.mStartUploadServiceThread = null;
                }
            });
            thread = this.mStartUploadServiceThread;
        } else {
            thread = null;
        }
        return thread;
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        AppTrackingUtil.sendAppOpenedMessage(this);
        this.mIsResumed = true;
        if (this.mTabsAdapter != null && this.mTabHost != null) {
            this.mTabsAdapter.previousTab = this.mTabHost.getCurrentTab();
        }
        Thread startUploadServiceThread = getUploadServiceThread();
        if (startUploadServiceThread != null) {
            startUploadServiceThread.start();
        }
        TabWidget tabWidget = this.mTabHost.getTabWidget();
        int tabCount = tabWidget == null ? 0 : tabWidget.getTabCount();
        for (int j = 0; j < tabCount; j++) {
            View tabView = tabWidget.getChildAt(j);
            if (tabView != null) {
                tabView.setTag(Integer.valueOf(j + 1));
                tabView.setOnLongClickListener(this);
            }
        }
        registerReceiver(this.mActivityCountReceiver, ACTIVITY_COUNT_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
        this.mAppController.fetchActivityCounts();
        invalidateActivityBadge();
        invalidateMessagesBadge();
        AppNavigationProviderSingleton.getInstance().setSection(getSectionFromTab(this.mTabHost.getCurrentTab()));
        Components.remoteRequestControlComponent().addListener(this.mRemoteRequestListener);
        if (getSlidingMenu() != null) {
            getSlidingMenu().setTouchModeAbove(0);
        }
        if (shouldShowLinkTwitterPrompt()) {
            SharedPreferences.Editor editor = CommonUtil.getDefaultSharedPrefs(this).edit();
            editor.putBoolean("linkTwitterShown", true);
            editor.apply();
            PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(3);
            p.setListener(new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.HomeTabActivity.6
                @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
                public void onDialogDone(DialogInterface dialog, int id, int which) {
                    if (which == -1) {
                        Intent intent = new Intent(HomeTabActivity.this, (Class<?>) SettingsActivity.class);
                        HomeTabActivity.this.startActivity(intent);
                    }
                }
            });
            p.setMessage(R.string.twitter_alert_link_prompt);
            p.setTitle(R.string.twitter_alert_link_title);
            p.setNegativeButton(R.string.twitter_alert_link_cancel);
            p.setPositiveButton(R.string.twitter_alert_link_ok);
            p.setCancelable(false);
            p.show(getSupportFragmentManager());
            return;
        }
        if (shouldShowFollowOnTwitterPrompt()) {
            SharedPreferences.Editor editor2 = CommonUtil.getDefaultSharedPrefs(this).edit();
            editor2.putLong("followerMigrationLastPrompted", System.currentTimeMillis());
            editor2.apply();
            PromptDialogSupportFragment p2 = PromptDialogSupportFragment.newInstance(2);
            p2.setListener(new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.HomeTabActivity.7
                @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
                public void onDialogDone(DialogInterface dialog, int id, int which) {
                    if (which == -1) {
                        Intent i = new Intent(HomeTabActivity.this, (Class<?>) TwitterFollowerMigrationActivity.class);
                        i.putExtra("p_id", HomeTabActivity.this.mAppController.getActiveId());
                        i.putExtra("u_type", 14);
                        HomeTabActivity.this.startActivity(i);
                    }
                }
            });
            p2.setMessage(R.string.twitter_migration_prompt);
            p2.setTitle(R.string.follow_on_twitter_title);
            p2.setNegativeButton(R.string.twitter_migration_cancel);
            p2.setPositiveButton(R.string.twitter_migration_ok);
            p2.setCancelable(false);
            p2.show(getSupportFragmentManager());
        }
    }

    private AppNavigation.Sections getSectionFromTab(int currentTab) {
        switch (currentTab) {
            case 0:
                return AppNavigation.Sections.HOME;
            case 1:
                return AppNavigation.Sections.ACTIVITY;
            case 2:
                return AppNavigation.Sections.EXPLORE;
            case 3:
                return AppNavigation.Sections.MY_PROFILE;
            default:
                return AppNavigation.Sections.EMPTY;
        }
    }

    private AppNavigation.Sections getSectionFromTag(String tag) {
        if ("home".equals(tag)) {
            return AppNavigation.Sections.HOME;
        }
        if ("activity".equals(tag)) {
            return AppNavigation.Sections.ACTIVITY;
        }
        if ("explore".equals(tag)) {
            return AppNavigation.Sections.EXPLORE;
        }
        if ("profile".equals(tag)) {
            return AppNavigation.Sections.MY_PROFILE;
        }
        return AppNavigation.Sections.EMPTY;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) throws Resources.NotFoundException {
        if (this.mTabsAdapter != null) {
            ArrayList<TabInfo> tabs = this.mTabsAdapter.getTabs();
            if (requestCode == 0 && resultCode == 1) {
                Iterator<TabInfo> it = tabs.iterator();
                while (it.hasNext()) {
                    TabInfo tab = it.next();
                    Fragment fragment = tab.fragment();
                    if (fragment instanceof BaseTimelineFragment) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
                return;
            }
            if (requestCode == 1 && resultCode == -1) {
                if (BuildUtil.isExplore() && this.mMenu != null) {
                    this.mMenu.removeItem(R.id.capture);
                    return;
                }
                return;
            }
            if (requestCode == 2) {
                if (resultCode == -1) {
                    showPage(2, this.mTabsAdapter.getItem(2));
                }
            } else {
                Fragment frag = tabs.get(this.mTabHost.getCurrentTab()).fragment();
                if (frag != null) {
                    frag.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override // co.vine.android.BaseTimelineActivity
    protected BaseTimelineFragment getCurrentTimeLineFragment() {
        try {
            Fragment frag = this.mTabsAdapter.getCurrentFragment();
            if (frag instanceof BaseTimelineFragment) {
                return (BaseTimelineFragment) frag;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override // android.support.v7.app.AppCompatActivity
    public void onSupportContentChanged() {
        super.onSupportContentChanged();
        if (!BuildUtil.isExplore()) {
            setupTabs();
        }
    }

    private void setupTabs() {
        this.mTabHost = (IconTabHost) findViewById(android.R.id.tabhost);
        if (this.mTabHost == null) {
            throw new RuntimeException("Your content must have a TabHost whose id attribute is 'android.R.id.tabhost'");
        }
        this.mTabHost.setup();
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
    }

    @Override // co.vine.android.BaseTimelineActivity, co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        SharedPreferences prefs = CommonUtil.getDefaultSharedPrefs(this);
        if (BuildUtil.isExplore() && prefs.getBoolean("pref_gb_notif_dismissed", false)) {
            menu.removeItem(R.id.capture);
        }
        this.mInboxMenu = menu.findItem(R.id.inbox);
        invalidateMessagesBadge();
        this.mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateMessagesBadge() {
        Resources res = getResources();
        if (res != null && this.mInboxMenu != null) {
            Drawable currentDrawable = this.mInboxMenu.getIcon();
            CenteredTextDrawable inboxBadgeDrawable = null;
            if (currentDrawable instanceof CenteredTextDrawable) {
                inboxBadgeDrawable = (CenteredTextDrawable) currentDrawable;
            }
            if (inboxBadgeDrawable == null) {
                inboxBadgeDrawable = new CenteredTextDrawable(res, R.drawable.vm_bubble_outline, R.drawable.vm_bubble_fill);
                inboxBadgeDrawable.setColorFilter(1, new PorterDuffColorFilter(Util.getProfileColor(this) | ViewCompat.MEASURED_STATE_MASK, PorterDuff.Mode.SRC_IN));
                int padding = (int) (6.0f * res.getDisplayMetrics().density);
                inboxBadgeDrawable.setTextPadding(new Rect(padding, padding, padding, padding));
                inboxBadgeDrawable.setTextColor(-1);
                inboxBadgeDrawable.setTypeFace(Typefaces.get(this).boldContentBold);
                inboxBadgeDrawable.setTextSize(TypedValue.applyDimension(2, 12.0f, res.getDisplayMetrics()));
            }
            if (this.mMessageCount == 0) {
                this.mInboxMenu.setIcon(R.drawable.ic_action_inbox);
            } else {
                inboxBadgeDrawable.setText(this.mMessageCount > 99 ? "99+" : String.valueOf(this.mMessageCount));
                this.mInboxMenu.setIcon(inboxBadgeDrawable);
            }
        }
    }

    public void setHomeNewCount(int newCount) {
        if (this.mTabsAdapter != null) {
            SLog.i("Set home badge count: {}", Integer.valueOf(newCount));
            this.mTabsAdapter.setNew(0, newCount > 0);
            ImageView indicator = this.mTabsAdapter.getNewIndicatorForTab(0);
            Resources res = getResources();
            if (res != null && indicator != null) {
                Drawable drawable = indicator.getDrawable();
                CenteredTextDrawable textDrawable = null;
                if (drawable instanceof CenteredTextDrawable) {
                    textDrawable = (CenteredTextDrawable) drawable;
                }
                if (textDrawable == null) {
                    textDrawable = makeAcitivytNewIndicator(res);
                    indicator.setImageDrawable(textDrawable);
                }
                int padding = (int) (6.0f * res.getDisplayMetrics().density);
                textDrawable.setTextPadding(new Rect(padding, padding, padding, padding));
                textDrawable.setText(newCount < 10 ? String.valueOf(newCount) : "9+");
            }
        }
    }

    public void setPostMosaicDismissed(boolean isDismissed) {
        this.mPostMosaicDismissed = isDismissed;
    }

    public boolean getPostMosaicDismissed() {
        return this.mPostMosaicDismissed;
    }

    public void invalidateActivityBadge() {
        if (this.mTabsAdapter != null) {
            this.mTabsAdapter.setNew(1, this.mActivityCount > 0);
            ImageView indicator = this.mTabsAdapter.getNewIndicatorForTab(1);
            Resources res = getResources();
            if (res != null && indicator != null) {
                Drawable drawable = indicator.getDrawable();
                CenteredTextDrawable textDrawable = null;
                if (drawable instanceof CenteredTextDrawable) {
                    textDrawable = (CenteredTextDrawable) drawable;
                }
                if (textDrawable == null) {
                    textDrawable = makeAcitivytNewIndicator(res);
                    indicator.setImageDrawable(textDrawable);
                }
                int minus = 0;
                if (this.mActivityCount > 9) {
                    minus = 0 + 1;
                }
                if (this.mActivityCount > 99) {
                    minus++;
                }
                int paddingVertical = (int) (6.0f * res.getDisplayMetrics().density);
                int paddingSides = (int) ((6 - (minus * 2)) * res.getDisplayMetrics().density);
                textDrawable.setTextPadding(new Rect(paddingSides, paddingVertical, paddingSides, paddingVertical));
                textDrawable.setText(this.mActivityCount > 99 ? "99+" : String.valueOf(this.mActivityCount));
            }
        }
    }

    private CenteredTextDrawable makeAcitivytNewIndicator(Resources res) {
        float density = res.getDisplayMetrics().density;
        GradientDrawable drawable1 = new GradientDrawable();
        drawable1.setStroke((int) (2.0f * density), -14408668);
        drawable1.setShape(0);
        drawable1.setCornerRadius(4.0f * density);
        drawable1.setColor(res.getColor(R.color.vine_green));
        drawable1.setSize((int) (density * 18.0f), (int) (density * 18.0f));
        CenteredTextDrawable textDrawable = new CenteredTextDrawable(drawable1);
        textDrawable.setTextColor(-1);
        textDrawable.setTypeFace(Typefaces.get(this).boldContentBold);
        textDrawable.setTextSize(TypedValue.applyDimension(2, 11.0f, res.getDisplayMetrics()));
        return textDrawable;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.find_friends) {
            FlurryUtils.trackVisitFindFriends("Menu");
            startActivity(new Intent(this, (Class<?>) FindFriendsActivity.class));
            return true;
        }
        if (id == R.id.settings) {
            FlurryUtils.trackVisitSettings("Menu");
            startActivityForResult(new Intent(this, (Class<?>) SettingsActivity.class), 0);
            return true;
        }
        if (id == R.id.search) {
            FlurryUtils.trackVisitSearch("Menu");
            Intent intent = new Intent(this, (Class<?>) SearchActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.inbox) {
            FlurryUtils.trackVisitInbox("Menu");
            getSlidingMenu().toggle(true);
            return true;
        }
        if (id == R.id.capture && BuildUtil.isExplore()) {
            Intent intent2 = ViewOverlayActivity.getIntent(this, R.layout.main_gb_notif, R.id.gingerbread_notif_button, 0);
            startActivityForResult(intent2, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) throws Resources.NotFoundException {
        int toastResId = -1;
        switch (((Integer) view.getTag()).intValue()) {
            case 1:
                toastResId = R.string.tab_title_timeline;
                break;
            case 2:
                toastResId = R.string.tab_title_activity;
                break;
            case 3:
                toastResId = R.string.tab_title_explore;
                break;
            case 4:
                toastResId = R.string.tab_title_profile;
                break;
        }
        if (toastResId > 0) {
            int xOffset = view.getLeft();
            int yOffset = view.getBottom() + view.getMeasuredHeight();
            Toast toast = Toast.makeText(this, toastResId, 0);
            toast.setGravity(51, xOffset, yOffset);
            toast.show();
        }
        if (view.getClass().equals(TabIndicator.class)) {
            view.performHapticFeedback(0);
            return true;
        }
        return true;
    }

    @Override // co.vine.android.widget.tabs.IconTabHost.OnTabClickedListener
    public void onCurrentTabClicked() {
        scrollTop();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void scrollTop() {
        TopScrollable list;
        Fragment currentFragment = this.mTabsAdapter.getCurrentFragment();
        resetNav();
        if (currentFragment instanceof BaseArrayListFragment) {
            View view = currentFragment.getView();
            if (view != null && (list = (TopScrollable) view.findViewById(android.R.id.list)) != null && !list.scrollTop()) {
                ((BaseArrayListFragment) currentFragment).invokeScrollFirstItem();
                return;
            }
            return;
        }
        if (currentFragment instanceof TopScrollable) {
            ((TopScrollable) currentFragment).scrollTop();
        }
    }

    private void changeTabColor(int lastItem) {
        if (lastItem >= 0) {
            ((TabIndicator) this.mTabHost.getTabWidget().getChildTabViewAt(lastItem)).setColor(this.mDisabledTabColor);
        }
        int newItem = this.mTabHost.getCurrentTab();
        if (newItem != 3) {
            ((TabIndicator) this.mTabHost.getTabWidget().getChildTabViewAt(newItem)).setColor(this.mEnabledTabColor);
        }
    }

    public void setProfileColor(int color) {
        int current = this.mTabHost.getCurrentTab();
        if (current == 3) {
            ((TabIndicator) this.mTabHost.getTabWidget().getChildTabViewAt(current)).setColor(color);
        }
    }

    private void processStartIntent(Intent intent) {
        Uri data = intent.getData();
        String action = intent.getAction();
        VineSingleNotification notification = (VineSingleNotification) Parcels.unwrap(intent.getParcelableExtra("notification"));
        if ("android.intent.action.VIEW".equals(action) && data != null) {
            ArrayList<String> pathSegments = new ArrayList<>(data.getPathSegments());
            String scheme = data.getScheme();
            String host = data.getHost();
            String pathPrefix = null;
            if (!pathSegments.isEmpty()) {
                String pathPrefix2 = pathSegments.get(0);
                pathPrefix = pathPrefix2;
            }
            if (host != null) {
                if (isConfirmEmailRequest(scheme, host, pathPrefix)) {
                    this.mAppController.verifyEmail(this.mAppController.getActiveSession(), data.getLastPathSegment());
                    this.mProgressDialog = new ProgressDialog(this);
                    this.mProgressDialog.setMessage(getString(R.string.login_validating));
                    try {
                        this.mProgressDialog.show();
                    } catch (Exception e) {
                        SLog.e("Failed to show pd.", (Throwable) e);
                    }
                } else {
                    LinkDispatcher.dispatch(data, this);
                }
            }
        } else if ("co.vine.android.ACTIVITY_NOTIFICATION_PRESSED".equals(action) && notification != null) {
            if (notification.notificationTypeId == 33 && notification.postId > 0) {
                Intent singleIntent = SingleActivity.getIntent(this, notification.postId);
                singleIntent.putExtra("is_favorite", true);
                startActivity(singleIntent);
            } else {
                this.mShowActivityFeed = true;
            }
        } else if ("co.vine.android.ONBOARD_NOTIFICATION_PRESSED".equals(action)) {
            if (notification != null && "favs".equals(notification.onboard) && OnboardHelper.hasShownFavoriteUserOverlay(this)) {
                FavoritePeopleActivity.start(this, 0);
            }
        } else if ("co.vine.android.MESSAGE_NOTIFICATION_PRESSED".equals(action)) {
            this.mShouldShowMenu = true;
        } else if ("co.vine.android.UPLOAD_LIST".equals(action)) {
            AppImpl.getInstance().startUploadsListActivity(this);
        }
        if (notification != null) {
            FlurryUtils.trackNotificationClicked(notification.toString());
        }
    }

    private boolean isConfirmEmailRequest(String scheme, String host, String pathPrefix) {
        return ("http://".equalsIgnoreCase(scheme) || "https://".equalsIgnoreCase(scheme)) && host.contains("vine.co") && !TextUtils.isEmpty(pathPrefix) && "confirmEmail".equals(pathPrefix);
    }

    private static final class DelayHandler extends Handler {
        private final WeakReference<Context> mContextRef;

        public DelayHandler(Context context) {
            this.mContextRef = new WeakReference<>(context.getApplicationContext());
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Context context = this.mContextRef.get();
            if (context != null) {
                AppController appController = AppController.getInstance(context);
                switch (msg.what) {
                    case 1:
                        appController.fetchUsersMe(appController.getActiveId(), UrlCachePolicy.NETWORK_THEN_CACHE);
                        break;
                    case 2:
                        appController.fetchFollowing(appController.getActiveId(), 1, null);
                        break;
                    case 3:
                        appController.determineServerStatus();
                        break;
                    case 4:
                        appController.fetchFavoriteUsers(1, "0:0");
                        break;
                }
            }
        }
    }

    private class HomeTabTabsAdapter extends TabsAdapter {
        public HomeTabTabsAdapter(TabHost tabHost) {
            super(HomeTabActivity.this, tabHost, HomeTabActivity.this.mViewPager, null);
        }

        @Override // co.vine.android.widget.tabs.TabsAdapter, android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            HomeTabActivity.this.resetNav();
            if (position == 1) {
                HomeTabActivity.this.mActivityCount = 0;
                HomeTabActivity.this.invalidateActivityBadge();
            }
        }
    }

    class HomeTabSessionListener extends AppSessionListener {
        HomeTabSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onVerifyEmailComplete(String reqId, int statusCode, String reasonPhrase) {
            if (statusCode == 200) {
                Intent intent = new Intent(HomeTabActivity.this, (Class<?>) SettingsActivity.class);
                intent.setAction(HomeTabActivity.ACTION_VERIFICATION_COMPLETE);
                HomeTabActivity.this.startActivityForResult(intent, 0);
            } else {
                Context applicationContext = HomeTabActivity.this.getApplicationContext();
                if (reasonPhrase == null) {
                    reasonPhrase = HomeTabActivity.this.getString(R.string.generic_error);
                }
                Util.showCenteredToast(applicationContext, reasonPhrase);
            }
            HomeTabActivity.this.dismissDialog();
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetActivityCountComplete(String reqId, int statusCode, String reasonPhrase, int activityCount, int followRequestCount, int messagesCount, UrlCachePolicy policyUsed) {
            if (statusCode == 200) {
                SharedPreferences pref = CommonUtil.getDefaultSharedPrefs(HomeTabActivity.this);
                int mostRecent = pref.getInt("pref_mrumc", 0);
                boolean dismissed = pref.getBoolean("pref_dismissed", false);
                if (!dismissed) {
                    HomeTabActivity.this.mMessageCount = messagesCount;
                    HomeTabActivity.this.invalidateMessagesBadge();
                } else if (mostRecent != messagesCount) {
                    pref.edit().putBoolean("pref_dismissed", false).apply();
                    HomeTabActivity.this.mMessageCount = messagesCount;
                    HomeTabActivity.this.invalidateMessagesBadge();
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onReceiveRTCMessage(ArrayList<VineRTCConversation> data) {
            Iterator<VineRTCConversation> it = data.iterator();
            while (it.hasNext()) {
                VineRTCConversation conversation = it.next();
                Iterator<VineRTCParticipant> it2 = conversation.participants.iterator();
                while (it2.hasNext()) {
                    VineRTCParticipant participant = it2.next();
                    long lastMessageId = participant.lastMessageId;
                    if (lastMessageId > 0) {
                        Long participantLast = (Long) HomeTabActivity.this.mLastMessageMap.get(participant.userId);
                        if (participantLast == null || lastMessageId <= participantLast.longValue()) {
                            HomeTabActivity.this.mLastMessageMap.put(participant.userId, Long.valueOf(lastMessageId));
                        } else {
                            HomeTabActivity.this.mAppController.fetchConversation(1, false, conversation.conversationId, 0L, true);
                            if (!HomeTabActivity.this.getSlidingMenu().isMenuShowing()) {
                                HomeTabActivity.this.mAppController.fetchActivityCounts();
                            }
                        }
                    }
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onFetchOnboardingSuggestedFavoritesComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineUser> users) {
            if (users != null && !users.isEmpty()) {
                FavoritePeopleOnboardActivity.start(HomeTabActivity.this, users);
                OnboardHelper.setFavoriteUserOverlayShown(HomeTabActivity.this);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onFetchFavoritePeopleComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineUser> users, int nextPage, int prevPage, String anchor) {
            if ((users == null || users.isEmpty()) && !OnboardHelper.hasShownFavoriteUserOverlay(HomeTabActivity.this)) {
                HomeTabActivity.this.mAppController.fetchOnboardingSuggestedFavoriteUsers();
                return;
            }
            boolean showOnboarding = true;
            if (users != null) {
                Iterator<VineUser> it = users.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    VineUser u = it.next();
                    if (u.sectionId == 0) {
                        showOnboarding = false;
                        break;
                    }
                }
            }
            if (!HomeTabActivity.this.mIsNewUser && !OnboardHelper.hasShownFavoriteUserOverlay(HomeTabActivity.this) && showOnboarding) {
                HomeTabActivity.this.mAppController.fetchOnboardingSuggestedFavoriteUsers();
            }
        }
    }
}
