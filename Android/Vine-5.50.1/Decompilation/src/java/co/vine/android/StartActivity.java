package co.vine.android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TextView;
import co.vine.android.api.VineLogin;
import co.vine.android.client.AppController;
import co.vine.android.client.ServiceNotification;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.nux.LoginFragment;
import co.vine.android.nux.SignUpPagerActivity;
import co.vine.android.nux.SignupFragment;
import co.vine.android.player.SdkVideoView;
import co.vine.android.scribe.AppLifecycleScribeLoggerSingleton;
import co.vine.android.scribe.AppNavigationProvider;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.ViewImpressionScribeLogger;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.authentication.AuthenticationActionListener;
import co.vine.android.util.AppTrackingUtil;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesSpan;
import co.vine.android.widget.tabs.IconTabHost;
import co.vine.android.widget.tabs.TabIndicator;
import co.vine.android.widget.tabs.TabsAdapter;
import co.vine.android.widget.tabs.ViewPagerScrollBar;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avformat;
import com.mobileapptracker.MATEvent;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class StartActivity extends BaseControllerActionBarActivity {
    private Intent mAuthIntent;
    private boolean mLoginRequest;
    private ViewPagerScrollBar mScrollBar;
    private int mStopPosition;
    private IconTabHost mTabHost;
    private TabsAdapter mTabsAdapter;
    private SdkVideoView mVideoView;
    private ViewPager mViewPager;
    private Handler mHandler = new Handler();
    private final AuthenticationActionListener mAuthListener = new StartAuthListener();

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        FlurryUtils.trackLockOutSessionCount();
        super.onStop();
    }

    private static Uri getReferrer(Intent intent) {
        if (Build.VERSION.SDK_INT < 17) {
            return null;
        }
        Uri referrerUri = (Uri) intent.getParcelableExtra("android.intent.extra.REFERRER");
        if (referrerUri == null) {
            String referrer = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
            if (referrer != null) {
                return Uri.parse(referrer);
            }
            return null;
        }
        return referrerUri;
    }

    private void trackGoogleRequestsIfApplicable(Uri referrerUri, boolean loggedIn) {
        if (referrerUri != null) {
            String scheme = referrerUri.getScheme();
            String packageId = referrerUri.getHost();
            if (!TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(packageId)) {
                if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
                    if (packageId.contains("google.com")) {
                        FlurryUtils.trackGoogleView(loggedIn, false);
                    }
                } else if ("android-app".equals(scheme)) {
                    if ("com.google.android.googlequicksearchbox".equals(packageId)) {
                        FlurryUtils.trackGoogleView(loggedIn, true);
                    } else if ("com.google.appcrawler".equals(packageId)) {
                        FlurryUtils.trackGoogleBotCrawl();
                    }
                }
            }
        }
    }

    public static boolean actionAllowedWhenLoggedOut(Intent i) {
        String action = i.getAction();
        return action != null && action.equals("android.intent.action.VIEW");
    }

    private void processLoggedOutIntent(Intent i) {
        Uri referrerUri = getReferrer(i);
        if (i.getData() != null && actionAllowedWhenLoggedOut(i)) {
            trackGoogleRequestsIfApplicable(referrerUri, false);
            String action = i.getAction();
            Intent intent = new Intent(this, (Class<?>) HomeTabActivity.class);
            intent.setFlags(335544320);
            intent.setAction(action);
            if (i.getExtras() != null) {
                intent.putExtras(i.getExtras());
            }
            intent.setData(i.getData());
            if (Build.VERSION.SDK_INT >= 17) {
                intent.putExtra("android.intent.extra.REFERRER", referrerUri);
            }
            startActivity(intent);
        }
    }

    private boolean processIntent(Intent i) {
        setRequestedOrientation(1);
        AppController appController = AppController.getInstance(this);
        if (i != null && i.getAction() != null && i.getAction().startsWith("com.facebook.application")) {
            this.mLoginRequest = false;
        } else {
            this.mLoginRequest = i != null && i.getBooleanExtra("login_request_start_activity", false);
        }
        boolean forceLogInScreen = false;
        if (i != null) {
            forceLogInScreen = i.getBooleanExtra("logged_in_add_account", false);
        }
        if (appController.getActiveSession().isLoggedIn() && !forceLogInScreen) {
            if (this.mLoginRequest) {
                setResult(-1);
            } else if (i != null) {
                String action = i.getAction();
                AppInterface impl = AppImpl.getInstance();
                Intent intent = null;
                if ("co.vine.android.MESSAGE_NOTIFICATION_PRESSED".equals(action)) {
                    this.mAppController.clearPushNotifications(2);
                    String dataString = i.getDataString();
                    String username = i.getStringExtra("username");
                    long userId = i.getLongExtra("user_id", -1L);
                    long conversationObjectId = dataString != null ? Long.parseLong(dataString) : -1L;
                    if (conversationObjectId > 0) {
                        intent = ConversationActivity.getIntent(this, conversationObjectId, username, userId, false, false, false);
                        intent.setAction(action);
                    }
                }
                if (intent == null) {
                    if (!"co.vine.android.RECORD".equals(action)) {
                        intent = new Intent(this, (Class<?>) HomeTabActivity.class);
                        intent.setFlags(335544320);
                        intent.setAction(action);
                        if (i.getExtras() != null) {
                            intent.putExtras(i.getExtras());
                        }
                        if (i.hasExtra("nux_exit_url") && i.getStringExtra("nux_exit_url").contains("explore")) {
                            intent.putExtra("startTab", "explore");
                        }
                        if ("android.intent.action.VIEW".equals(action)) {
                            Uri referrerUri = getReferrer(i);
                            trackGoogleRequestsIfApplicable(referrerUri, true);
                            intent.setData(i.getData());
                        } else if ("co.vine.android.ACTIVITY_NOTIFICATION_PRESSED".equals(action)) {
                            intent.setAction(action);
                            if (i.getData() != null) {
                                intent.setAction("android.intent.action.VIEW");
                                intent.setData(i.getData());
                            }
                            this.mAppController.clearPushNotifications(1);
                            intent.setFlags(avformat.AVFMT_SEEK_TO_PTS);
                        } else if ("co.vine.android.MESSAGE_NOTIFICATION_PRESSED".equals(action)) {
                            intent.setAction(action);
                            intent.setFlags(0);
                        }
                    } else {
                        intent = impl.getRecordingIntent(this, 131072, "StartActivity: " + getCallingActivity());
                    }
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
            finish();
            return true;
        }
        Util.fetchClientFlags(this, false, false);
        processLoggedOutIntent(i);
        return false;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent i) {
        processIntent(i);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    public void onCreate(Bundle savedInstanceState) throws IllegalStateException, Resources.NotFoundException {
        ActionBar ab;
        super.onCreate(savedInstanceState, R.layout.start, false);
        hideActionBar();
        if ((getIntent().getFlags() & 4194304) != 0 && "android.intent.action.MAIN".equals(getIntent().getAction())) {
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT <= 14 && (ab = getSupportActionBar()) != null) {
            ab.hide();
        }
        Context appContext = getApplicationContext();
        getApplication().registerActivityLifecycleCallbacks(AppLifecycleScribeLoggerSingleton.getInstance(ScribeLoggerSingleton.getInstance(appContext), AppStateProviderSingleton.getInstance(appContext)));
        if (savedInstanceState != null) {
            try {
                if (savedInstanceState.containsKey("auth_intent")) {
                    this.mAuthIntent = (Intent) savedInstanceState.getParcelable("auth_intent");
                    loginCheckTwitter(this.mAuthIntent, false);
                }
            } catch (SQLiteException e) {
                SharedPreferences prefs = Util.getDefaultSharedPrefs(this);
                CrashUtil.logException(e, "pref_log_startup_fail: {}.", Integer.valueOf(prefs.getInt("pref_log_startup_fail", 0)));
                Util.showCenteredToast(this, R.string.startup_error);
                prefs.edit().putInt("pref_log_startup_fail", 1).apply();
                finish();
                return;
            }
        }
        try {
            boolean processIntent = processIntent(getIntent());
            if (!processIntent) {
                Util.fetchClientFlags(this, false, false);
                TypefacesSpan boldSpan = new TypefacesSpan(Typefaces.get(this).getContentTypeface(1, 4));
                new TypefacesSpan[1][0] = boldSpan;
                setupTabsForLogin();
                this.mVideoView = (SdkVideoView) findViewById(R.id.start_video);
                this.mVideoView.setKeepScreenOn(true);
                this.mVideoView.setLooping(true);
                if (SystemUtil.isNormalVideoPlayable(this) == SystemUtil.PrefBooleanState.UNKNOWN) {
                    SLog.i("Video test started.");
                    this.mVideoView.setVideoPathDirect(SystemUtil.getPathFromResourceRaw(this, R.raw.vine_splash));
                    this.mVideoView.setOnErrorListener(new VideoViewInterface.OnErrorListener() { // from class: co.vine.android.StartActivity.1
                        @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
                        public boolean onError(VideoViewInterface videoView, int what, int extra) {
                            SLog.d("Video test failed.");
                            SystemUtil.setNormalVideoPlayable(StartActivity.this.getApplicationContext(), false);
                            StartActivity.this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.StartActivity.1.1
                                @Override // java.lang.Runnable
                                public void run() throws IllegalStateException {
                                    StartActivity.this.openStartVideo();
                                }
                            }, 50L);
                            return true;
                        }
                    });
                    this.mVideoView.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.StartActivity.2
                        @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
                        public void onPrepared(VideoViewInterface view) throws IllegalStateException {
                            SystemUtil.setNormalVideoPlayable(StartActivity.this.getApplicationContext(), true);
                            StartActivity.this.mVideoView.start();
                            SLog.d("Video test completed, assuming success, may change later.");
                        }
                    });
                } else {
                    openStartVideo();
                }
                if (savedInstanceState != null && savedInstanceState.containsKey("state_p")) {
                    this.mStopPosition = savedInstanceState.getInt("state_p");
                }
                this.mAppController = AppController.getInstance(this);
                Util.getDefaultSharedPrefs(this).edit().putInt("pref_log_startup_fail", 0).apply();
            }
        } catch (RuntimeException e2) {
            CrashUtil.logException(e2, "Failed to process intent: " + getCallingPackage() + " - " + getIntent().getAction() + " - " + getCallingActivity(), new Object[0]);
            Util.showCenteredToast(this, R.string.startup_error);
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openStartVideo() throws IllegalStateException {
        this.mVideoView.setAutoPlayOnPrepared(true);
        this.mVideoView.setOnPreparedListener(null);
        this.mVideoView.setOnErrorListener(null);
        this.mVideoView.setVideoPathDirect(SystemUtil.getPathFromResourceRaw(this, R.raw.vine_splash));
        this.mVideoView.seekTo(this.mStopPosition);
        this.mVideoView.start();
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() throws IllegalStateException {
        super.onPause();
        if (this.mVideoView != null) {
            this.mStopPosition = this.mVideoView.getCurrentPosition();
            this.mVideoView.pause();
        }
        dismissDialog();
        Components.authComponent().removeListener(this.mAuthListener);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() throws IllegalStateException {
        super.onDestroy();
        if (this.mVideoView != null) {
            this.mVideoView.suspend();
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mAuthIntent != null) {
            outState.putParcelable("auth_intent", this.mAuthIntent);
        }
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() throws IllegalStateException {
        super.onResume();
        AppTrackingUtil.sendAppOpenedMessage(this);
        if (this.mVideoView != null) {
            this.mVideoView.seekTo(this.mStopPosition);
            if (this.mVideoView.isPlaying()) {
                this.mVideoView.resume();
            } else {
                this.mVideoView.start();
            }
        }
        if (this.mAppController.isLoggedIn() && this.mLoginRequest) {
            setResult(-1);
            finish();
        } else {
            Context context = getApplicationContext();
            AppNavigationProvider appNavProvider = AppNavigationProviderSingleton.getInstance();
            appNavProvider.setViewChangedListener(new ViewImpressionScribeLogger(ScribeLoggerSingleton.getInstance(context), AppStateProviderSingleton.getInstance(context), appNavProvider));
            appNavProvider.setSection(AppNavigation.Sections.LOGGED_OUT);
            appNavProvider.setViewAndSubview(AppNavigation.Views.FRONT, null);
        }
        Components.authComponent().addListener(this.mAuthListener);
    }

    public static void toStart(Context context) {
        toStart(context, false);
    }

    public static void toStart(Context context, boolean isFromNUX) {
        Intent i = getStartIntent(context);
        i.putExtra("is_new_user", isFromNUX);
        if (isFromNUX) {
            i.putExtra("nux_exit_url", ClientFlagsHelper.getWelcomeFeedExitUrl(context));
        }
        context.startActivity(i);
        if (BuildUtil.isExplore() && !(context instanceof StartActivity)) {
            broadcastFinish(context);
        }
    }

    private static void broadcastFinish(Context context) {
        Intent i = new Intent();
        i.setAction("co.vine.android.FINISH");
        context.sendBroadcast(i, CrossConstants.BROADCAST_PERMISSION);
    }

    public static Intent getStartIntent(Context context) {
        return getStartIntent(context, "android.intent.action.VIEW");
    }

    public static Intent getStartIntent(Context context, String action) {
        Intent intent = new Intent(context, (Class<?>) StartActivity.class);
        intent.setAction(action);
        intent.setFlags(268468224);
        return intent;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            loginCheckTwitter(data, false);
            this.mAuthIntent = data;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginCheckTwitter(Intent data, boolean reactivate) {
        if (data != null) {
            String username = data.getStringExtra("screen_name");
            String mToken = data.getStringExtra("tk");
            String mSecret = data.getStringExtra("ts");
            ProgressDialog dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
            dialog.setProgressStyle(0);
            dialog.setMessage(getString(R.string.sign_up_authorizing));
            dialog.show();
            this.mProgressDialog = dialog;
            Components.authComponent().loginCheckTwitter(this.mAppController, username, mToken, mSecret, data.getLongExtra("user_id", 0L), reactivate);
        }
    }

    private final class StartAuthListener extends AuthenticationActionListener {
        private StartAuthListener() {
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginSuccess(VineLogin login) {
            StartActivity.this.dismissDialog();
            if (StartActivity.this.mLoginRequest) {
                StartActivity.this.setResult(-1);
                StartActivity.this.finish();
            } else {
                StartActivity.toStart(StartActivity.this);
            }
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginUnknownError(ServiceNotification notification) {
            StartActivity.this.dismissDialog();
            super.onCheckTwitterLoginUnknownError(notification);
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginBadCredentials(VineLogin login) {
            StartActivity.this.dismissDialog();
            Bundle extras = new Bundle();
            extras.putParcelable(PropertyConfiguration.USER, login);
            StartActivity.this.startActivity(SignUpPagerActivity.getIntent(StartActivity.this, extras));
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginDeactivated(VineLogin login) {
            StartActivity.this.dismissDialog();
            PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(1);
            p.setMessage(R.string.settings_activate_account_dialog);
            p.setTitle(R.string.settings_activate_account_title);
            p.setNegativeButton(R.string.cancel);
            p.setPositiveButton(R.string.settings_activate_account_confirm);
            p.setListener(new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.StartActivity.StartAuthListener.1
                @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
                public void onDialogDone(DialogInterface dialog, int id, int which) {
                    switch (which) {
                        case -1:
                            if (StartActivity.this.mAuthIntent != null) {
                                StartActivity.this.loginCheckTwitter(StartActivity.this.mAuthIntent, true);
                                break;
                            }
                            break;
                    }
                }
            });
            p.show(StartActivity.this.getSupportFragmentManager());
        }

        @Override // co.vine.android.service.components.authentication.AuthenticationActionListener
        public void onCheckTwitterLoginFailedToCreateLocalAccount(Context context) {
            StartActivity.this.dismissDialog();
            super.onCheckTwitterLoginFailedToCreateLocalAccount(context);
            StartActivity.this.finish();
        }
    }

    private void setupTabsForLogin() throws Resources.NotFoundException {
        this.mTabHost = (IconTabHost) findViewById(android.R.id.tabhost);
        this.mTabHost.setup();
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        this.mScrollBar = (ViewPagerScrollBar) findViewById(R.id.scrollbar);
        this.mScrollBar.setRange(2);
        this.mScrollBar.setScrollBarWidth(100);
        this.mViewPager.setOffscreenPageLimit(1);
        this.mTabsAdapter = new TabsAdapter(this, this.mTabHost, this.mViewPager, this.mScrollBar);
        this.mTabsAdapter.setSetActionBarColorOnPageSelectedEnabled(false);
        this.mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() { // from class: co.vine.android.StartActivity.3
            @Override // android.widget.TabHost.OnTabChangeListener
            public void onTabChanged(String tag) throws Resources.NotFoundException {
                StartActivity.this.mViewPager.setCurrentItem(StartActivity.this.mTabHost.getCurrentTab());
            }
        });
        addSignUpTab();
        addLoginTab();
    }

    private void addSignUpTab() {
        TabHost.TabSpec tabSpecSignup = this.mTabHost.newTabSpec("signup");
        tabSpecSignup.setIndicator(getTabIndicator(R.string.login_sign_up, R.color.solid_white));
        Bundle bundle = new Bundle();
        bundle.putBoolean("login_request", this.mLoginRequest);
        this.mTabsAdapter.addTab(tabSpecSignup, SignupFragment.class, bundle);
    }

    private void addLoginTab() {
        TabHost.TabSpec tabSpecLogin = this.mTabHost.newTabSpec(MATEvent.LOGIN);
        tabSpecLogin.setIndicator(getTabIndicator(R.string.login_signin, R.color.solid_white));
        Bundle bundle = new Bundle();
        bundle.putBoolean("login_request", this.mLoginRequest);
        this.mTabsAdapter.addTab(tabSpecLogin, LoginFragment.class, bundle);
    }

    private TabIndicator getTabIndicator(int tabTextId, int colorResId) throws Resources.NotFoundException {
        TabIndicator indicator = TabIndicator.newTextIndicator(LayoutInflater.from(this), R.layout.splash_tab_indicator, this.mTabHost, tabTextId, false);
        ColorStateList indicatorStateList = TabIndicator.createTextColorList(getResources().getColor(colorResId), getResources().getColor(R.color.white_fifty_percent));
        TextView indicatorText = indicator.getIndicatorText();
        indicatorText.setTextColor(indicatorStateList);
        indicatorText.setTypeface(Typefaces.get(this).lightContentBold);
        return indicator;
    }
}
