package co.vine.android;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.CursorWindow;
import android.os.Handler;
import android.text.TextUtils;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VineParsers;
import co.vine.android.client.AppController;
import co.vine.android.client.SessionManager;
import co.vine.android.client.TwitterVineApp;
import co.vine.android.client.VineAPI;
import co.vine.android.embed.player.VideoViewHelper;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.SdkVideoView;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.Components;
import co.vine.android.storage.RealmManager;
import co.vine.android.util.AppTrackingUtil;
import co.vine.android.util.AudioUtils;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.edisonwang.android.slog.SLog;
import com.facebook.buck.android.support.exopackage.DefaultApplicationLike;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes.dex */
public class VineApplication extends DefaultApplicationLike implements Thread.UncaughtExceptionHandler {
    private static VineApplication sInstance;
    private final Application mAppContext;
    private final VineServiceActionMapProvider mServiceActionProvider;
    private Thread.UncaughtExceptionHandler sDefaultHandler;

    public VineApplication(Application appContext) {
        this.mAppContext = appContext;
        BuildUtil.bootstrap(appContext);
        Components[] components = Components.values();
        AppController.clearNotifiers();
        VineServiceActionMapProvider.Builder builder = new VineServiceActionMapProvider.Builder();
        for (Components component : components) {
            component.register(builder);
        }
        VineAPI.setSessionKeyGetter(new VineAPI.SessionKeyGetter() { // from class: co.vine.android.VineApplication.1
            @Override // co.vine.android.client.VineAPI.SessionKeyGetter
            public String getSessionKey(Context c) {
                return AppController.getInstance(c).getActiveSessionReadOnly().getSessionKey();
            }
        });
        VineParserReader.setParserCreator(VineParsers.createParserCreator());
        this.mServiceActionProvider = builder.build();
    }

    @Override // com.facebook.buck.android.support.exopackage.DefaultApplicationLike, com.facebook.buck.android.support.exopackage.ApplicationLike
    public void onCreate() throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        super.onCreate();
        AppTrackingUtil.initialize(this.mAppContext);
        RealmManager.initialize(this.mAppContext);
        SessionManager.getSharedInstance().initSessions(this.mAppContext);
        if (BuildUtil.isI18nOn()) {
            Resources resources = this.mAppContext.getResources();
            if (resources != null) {
                forceCustomLocale(this.mAppContext);
            } else {
                return;
            }
        }
        initFabric();
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultHandler != this) {
            this.sDefaultHandler = defaultHandler;
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
        try {
            ArrayList<String> commandLine = new ArrayList<>();
            commandLine.add("logcat");
            commandLine.add("-c");
            Runtime.getRuntime().exec((String[]) commandLine.toArray(new String[commandLine.size()])).destroy();
        } catch (Throwable th) {
        }
        try {
            CrashUtil.set("RAM Budget", SystemUtil.getMemoryBudgetForLargeMemoryClass(this.mAppContext));
        } catch (Exception e) {
            CrashUtil.log("Failed to get ram budeget.");
        }
        VideoViewHelper.setDefaultCrashHandler(new VideoViewInterface.SilentExceptionHandler() { // from class: co.vine.android.VineApplication.2
            @Override // co.vine.android.embed.player.VideoViewInterface.SilentExceptionHandler
            public void onUnexpectedException(String message, Exception exception) {
                CrashUtil.logException(exception, message, new Object[0]);
            }

            @Override // co.vine.android.embed.player.VideoViewInterface.SilentExceptionHandler
            public void onExpectedException(String message, Exception exception) {
                CrashUtil.logException(exception, message, new Object[0]);
            }

            @Override // co.vine.android.embed.player.VideoViewInterface.SilentExceptionHandler
            public void onProgrammingErrorException(Exception e2) {
                CrashUtil.logOrThrowInDebug(e2);
            }

            @Override // co.vine.android.embed.player.VideoViewInterface.SilentExceptionHandler
            public void onExceptionDuringOpening(String message, Exception ex) {
            }
        });
        try {
            VideoViewHelper.setOnUseVinePlayerFlagChangedListener(new VideoViewHelper.OnUseVinePlayerFlagChangedListener() { // from class: co.vine.android.VineApplication.3
                @Override // co.vine.android.embed.player.VideoViewHelper.OnUseVinePlayerFlagChangedListener
                public void onUseVinePlayerFlagChanged(boolean useVineVideoView) {
                    CrossConstants.VINE_PLAYER_ENABLED = useVineVideoView;
                    SLog.i("Use Vine PLayer {}", String.valueOf(VideoViewHelper.getGlobalUseVineVideoView()));
                    CrashUtil.set("Using VineVideoView", String.valueOf(VideoViewHelper.getGlobalUseVineVideoView()));
                }
            });
            VideoViewHelper.setGlobalUseVineVideoView(ClientFlagsHelper.getUseVinePlayer(this.mAppContext) && VideoViewHelper.isDeviceWhiteListed());
        } catch (Exception e2) {
            CrashUtil.log("Failed to set Vine Player mode.");
        }
        try {
            CrossConstants.SHOW_TWITTER_SCREENNAME = ClientFlagsHelper.isShowTwitterScreennameEnabled(this.mAppContext);
        } catch (Exception e3) {
            CrashUtil.log("Failed to get show twitter handle flag");
        }
        try {
            long artificialAudioLatency = ClientFlagsHelper.getAudioLatency(this.mAppContext, 160000);
            AudioUtils.setArtificialLatency(artificialAudioLatency);
            SLog.i("Audio Latency {}", Long.valueOf(artificialAudioLatency));
        } catch (Exception e4) {
            CrashUtil.log("Failed to set Artificial AudioLatency");
        }
        try {
            SdkVideoView.setLogsOn(SLog.sLogsOn);
            SdkVideoView.setSinglePlayerMode(SystemUtil.isSinglePlayerEnabled(this.mAppContext));
        } catch (Exception e5) {
            CrashUtil.log("Failed to set Single Player mode.");
        }
        if (!BuildUtil.isExplore()) {
            SLog.d("Start changing value.");
            try {
                Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
                field.setAccessible(true);
                Object original = field.get(null);
                SLog.d("Original value: {}.", original);
                int target = 10000000;
                int ratio = SystemUtil.getMemoryBudgetForLargeMemoryClass(this.mAppContext);
                if (ratio == 1) {
                    target = (int) (10000000 * 0.8d);
                }
                SLog.d("Changing using weighted value: {}", Integer.valueOf(target));
                if (((Integer) original).intValue() < target) {
                    field.set(null, Integer.valueOf(target));
                }
                SLog.d("Sucessfully updated sCursorWindowSize.");
            } catch (Exception e6) {
                CrashUtil.logException(e6, "You are too evil.", new Object[0]);
            }
            try {
                Field field2 = CursorWindow.class.getDeclaredField("sCursorWindowSize");
                field2.setAccessible(true);
                SLog.d("Verify value: {}.", field2.get(null));
            } catch (Exception e7) {
                CrashUtil.logException(e7, "You are too evil.", new Object[0]);
            }
            SLog.d("End changing value.");
        }
        sInstance = this;
    }

    private void initFabric() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TwitterVineApp.API_KEY, TwitterVineApp.API_SECRET);
        if (BuildUtil.isLogsOn()) {
            Fabric.with(this.mAppContext, new TwitterCore(authConfig), new Digits());
        } else {
            Fabric.with(this.mAppContext, new Crashlytics(), new TwitterCore(authConfig), new Digits());
        }
    }

    public static VineApplication getInstance() {
        return sInstance;
    }

    public VineServiceActionMapProvider getServiceActionProvider() {
        return this.mServiceActionProvider;
    }

    @Override // com.facebook.buck.android.support.exopackage.DefaultApplicationLike, com.facebook.buck.android.support.exopackage.ApplicationLike
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (BuildUtil.isI18nOn()) {
                final Locale locale = getCustomLocale(this.mAppContext);
                if (newConfig.locale != null) {
                    CrashUtil.set("locale", newConfig.locale.getDisplayName());
                }
                if (locale != null && !locale.equals(newConfig.locale)) {
                    new Handler().postDelayed(new Runnable() { // from class: co.vine.android.VineApplication.4
                        @Override // java.lang.Runnable
                        public void run() {
                            VineApplication.forceCustomLocale(VineApplication.this.mAppContext, locale, true);
                        }
                    }, 500L);
                }
            }
        } catch (Exception e) {
            CrashUtil.logException(e);
        }
    }

    @Override // com.facebook.buck.android.support.exopackage.DefaultApplicationLike, com.facebook.buck.android.support.exopackage.ApplicationLike
    @TargetApi(14)
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        CrashUtil.log("onTrimMemory happened: {}.", Integer.valueOf(level));
        AppController instance = AppController.getInstance(this.mAppContext);
        if (instance != null) {
            instance.onTrimMemory(level);
        }
    }

    @Override // com.facebook.buck.android.support.exopackage.DefaultApplicationLike, com.facebook.buck.android.support.exopackage.ApplicationLike
    public void onLowMemory() {
        super.onLowMemory();
        CrashUtil.log("onLowMemory happened.");
        AppController instance = AppController.getInstance(this.mAppContext);
        if (instance != null) {
            instance.onLowMemory();
        }
    }

    public Locale getLocale() {
        return this.mAppContext.getResources().getConfiguration().locale;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable ex) {
        String msg;
        if (ex != null && (msg = ex.getMessage()) != null && (msg.contains("detachFromGLContext") || msg.contains("startPreview") || msg.contains("setParameters"))) {
            CrashUtil.collectLogs(300, CrashUtil.getDefaultIgnoreCountForLogCollection(ex));
        }
        if (this.sDefaultHandler != null) {
            this.sDefaultHandler.uncaughtException(thread, ex);
        }
    }

    public Context getApplicationContext() {
        return this.mAppContext;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void forceCustomLocale(Context context, Locale locale, boolean updateResources) {
        if (locale != null) {
            Locale.setDefault(locale);
            if (updateResources) {
                Resources resources = context.getApplicationContext().getResources();
                Configuration config = resources.getConfiguration();
                config.locale = locale;
                resources.updateConfiguration(config, resources.getDisplayMetrics());
            }
        }
    }

    private static void forceCustomLocale(Context context) {
        forceCustomLocale(context, getCustomLocale(context), true);
    }

    private static Locale getCustomLocale(Context context) {
        Locale locale;
        SharedPreferences prefs = Util.getDefaultSharedPrefs(context);
        if (prefs.getBoolean("pref_custom_locale_enabled", false)) {
            String localeCode = prefs.getString("pref_custom_locale", null);
            String countryCode = prefs.getString("pref_custom_locale_country", null);
            if (!TextUtils.isEmpty(localeCode)) {
                if (!TextUtils.isEmpty(countryCode)) {
                    locale = new Locale(localeCode, countryCode);
                } else {
                    locale = new Locale(localeCode);
                }
                if (!TextUtils.isEmpty(locale.getISO3Language())) {
                    return locale;
                }
            }
        }
        return null;
    }

    private static boolean isUsEnglishLocale() {
        String code = Locale.getDefault().getLanguage();
        return Locale.ENGLISH.getLanguage().equals(code);
    }
}
