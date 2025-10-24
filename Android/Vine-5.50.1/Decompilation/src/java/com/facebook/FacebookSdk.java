package com.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Base64;
import com.facebook.internal.BoltsMeasurementEventListener;
import com.facebook.internal.LockOnGetVariable;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes2.dex */
public final class FacebookSdk {
    private static volatile String appClientToken;
    private static Context applicationContext;
    private static volatile String applicationId;
    private static volatile String applicationName;
    private static LockOnGetVariable<File> cacheDir;
    private static volatile Executor executor;
    private static volatile int webDialogTheme;
    private static final String TAG = FacebookSdk.class.getCanonicalName();
    private static final HashSet<LoggingBehavior> loggingBehaviors = new HashSet<>(Arrays.asList(LoggingBehavior.DEVELOPER_ERRORS));
    private static volatile String facebookDomain = "facebook.com";
    private static AtomicLong onProgressThreshold = new AtomicLong(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH);
    private static volatile boolean isDebugEnabled = false;
    private static boolean isLegacyTokenUpgradeSupported = false;
    private static int callbackRequestCodeOffset = 64206;
    private static final Object LOCK = new Object();
    private static final BlockingQueue<Runnable> DEFAULT_WORK_QUEUE = new LinkedBlockingQueue(10);
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() { // from class: com.facebook.FacebookSdk.1
        private final AtomicInteger counter = new AtomicInteger(0);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "FacebookSdk #" + this.counter.incrementAndGet());
        }
    };
    private static Boolean sdkInitialized = false;

    public interface InitializeCallback {
        void onInitialized();
    }

    public static synchronized void sdkInitialize(Context applicationContext2) {
        sdkInitialize(applicationContext2, null);
    }

    public static synchronized void sdkInitialize(Context applicationContext2, final InitializeCallback callback) {
        if (sdkInitialized.booleanValue()) {
            if (callback != null) {
                callback.onInitialized();
            }
        } else {
            Validate.notNull(applicationContext2, "applicationContext");
            Validate.hasFacebookActivity(applicationContext2, false);
            Validate.hasInternetPermissions(applicationContext2, false);
            applicationContext = applicationContext2.getApplicationContext();
            loadDefaultsFromMetadata(applicationContext);
            Utility.loadAppSettingsAsync(applicationContext, applicationId);
            NativeProtocol.updateAllAvailableProtocolVersionsAsync();
            BoltsMeasurementEventListener.getInstance(applicationContext);
            cacheDir = new LockOnGetVariable<>(new Callable<File>() { // from class: com.facebook.FacebookSdk.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public File call() throws Exception {
                    return FacebookSdk.applicationContext.getCacheDir();
                }
            });
            FutureTask<Void> accessTokenLoadFutureTask = new FutureTask<>(new Callable<Void>() { // from class: com.facebook.FacebookSdk.3
                @Override // java.util.concurrent.Callable
                public Void call() throws Exception {
                    AccessTokenManager.getInstance().loadCurrentAccessToken();
                    ProfileManager.getInstance().loadCurrentProfile();
                    if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() == null) {
                        Profile.fetchProfileForCurrentAccessToken();
                    }
                    if (callback != null) {
                        callback.onInitialized();
                        return null;
                    }
                    return null;
                }
            });
            getExecutor().execute(accessTokenLoadFutureTask);
            sdkInitialized = true;
        }
    }

    public static synchronized boolean isInitialized() {
        return sdkInitialized.booleanValue();
    }

    public static boolean isLoggingBehaviorEnabled(LoggingBehavior behavior) {
        boolean z;
        synchronized (loggingBehaviors) {
            z = isDebugEnabled() && loggingBehaviors.contains(behavior);
        }
        return z;
    }

    public static boolean isDebugEnabled() {
        return isDebugEnabled;
    }

    public static boolean isLegacyTokenUpgradeSupported() {
        return isLegacyTokenUpgradeSupported;
    }

    public static Executor getExecutor() {
        synchronized (LOCK) {
            if (executor == null) {
                executor = AsyncTask.THREAD_POOL_EXECUTOR;
            }
        }
        return executor;
    }

    public static String getFacebookDomain() {
        return facebookDomain;
    }

    public static Context getApplicationContext() {
        Validate.sdkInitialized();
        return applicationContext;
    }

    public static String getSdkVersion() {
        return "4.6.0";
    }

    public static boolean getLimitEventAndDataUsage(Context context) {
        Validate.sdkInitialized();
        SharedPreferences preferences = context.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0);
        return preferences.getBoolean("limitEventUsage", false);
    }

    public static long getOnProgressThreshold() {
        Validate.sdkInitialized();
        return onProgressThreshold.get();
    }

    static void loadDefaultsFromMetadata(Context context) throws PackageManager.NameNotFoundException {
        if (context != null) {
            try {
                ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                if (ai != null && ai.metaData != null) {
                    if (applicationId == null) {
                        Object appId = ai.metaData.get("com.facebook.sdk.ApplicationId");
                        if (appId instanceof String) {
                            String appIdString = (String) appId;
                            if (appIdString.toLowerCase(Locale.ROOT).startsWith("fb")) {
                                applicationId = appIdString.substring(2);
                            } else {
                                applicationId = appIdString;
                            }
                        } else if (appId instanceof Integer) {
                            throw new FacebookException("App Ids cannot be directly placed in the manifest.They must be prefixed by 'fb' or be placed in the string resource file.");
                        }
                    }
                    if (applicationName == null) {
                        applicationName = ai.metaData.getString("com.facebook.sdk.ApplicationName");
                    }
                    if (appClientToken == null) {
                        appClientToken = ai.metaData.getString("com.facebook.sdk.ClientToken");
                    }
                    if (webDialogTheme == 0) {
                        setWebDialogTheme(ai.metaData.getInt("com.facebook.sdk.WebDialogTheme"));
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
    }

    public static String getApplicationSignature(Context context) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        PackageManager packageManager;
        Validate.sdkInitialized();
        if (context == null || (packageManager = context.getPackageManager()) == null) {
            return null;
        }
        String packageName = context.getPackageName();
        try {
            PackageInfo pInfo = packageManager.getPackageInfo(packageName, 64);
            Signature[] signatures = pInfo.signatures;
            if (signatures == null || signatures.length == 0) {
                return null;
            }
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(pInfo.signatures[0].toByteArray());
                return Base64.encodeToString(md.digest(), 9);
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        } catch (PackageManager.NameNotFoundException e2) {
            return null;
        }
    }

    public static String getApplicationId() {
        Validate.sdkInitialized();
        return applicationId;
    }

    public static void setApplicationId(String applicationId2) {
        applicationId = applicationId2;
    }

    public static String getApplicationName() {
        Validate.sdkInitialized();
        return applicationName;
    }

    public static String getClientToken() {
        Validate.sdkInitialized();
        return appClientToken;
    }

    public static int getWebDialogTheme() {
        Validate.sdkInitialized();
        return webDialogTheme;
    }

    public static void setWebDialogTheme(int theme) {
        webDialogTheme = theme;
    }

    public static File getCacheDir() {
        Validate.sdkInitialized();
        return cacheDir.getValue();
    }

    public static int getCallbackRequestCodeOffset() {
        Validate.sdkInitialized();
        return callbackRequestCodeOffset;
    }

    public static boolean isFacebookRequestCode(int requestCode) {
        return requestCode >= callbackRequestCodeOffset && requestCode < callbackRequestCodeOffset + 100;
    }
}
