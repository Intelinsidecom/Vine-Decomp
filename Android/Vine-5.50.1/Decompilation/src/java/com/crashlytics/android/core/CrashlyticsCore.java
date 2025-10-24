package com.crashlytics.android.core;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.ScrollView;
import android.widget.TextView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.internal.CrashEventDataProvider;
import com.crashlytics.android.core.internal.models.SessionEventData;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.Crash;
import io.fabric.sdk.android.services.common.ExecutorUtils;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import io.fabric.sdk.android.services.concurrency.Priority;
import io.fabric.sdk.android.services.concurrency.PriorityCallable;
import io.fabric.sdk.android.services.concurrency.Task;
import io.fabric.sdk.android.services.concurrency.UnmetDependencyException;
import io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.persistence.FileStoreImpl;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import io.fabric.sdk.android.services.settings.PromptSettingsData;
import io.fabric.sdk.android.services.settings.SessionSettingsData;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.settings.SettingsData;
import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@DependsOn({CrashEventDataProvider.class})
/* loaded from: classes.dex */
public class CrashlyticsCore extends Kit<Void> {
    private final ConcurrentHashMap<String, String> attributes;
    private String buildId;
    private float delay;
    private boolean disabled;
    private CrashlyticsExecutorServiceWrapper executorServiceWrapper;
    private CrashEventDataProvider externalCrashEventDataProvider;
    private CrashlyticsUncaughtExceptionHandler handler;
    private HttpRequestFactory httpRequestFactory;
    private File initializationMarkerFile;
    private String installerPackageName;
    private CrashlyticsListener listener;
    private String packageName;
    private final PinningInfoProvider pinningInfo;
    private final long startTime;
    private String userEmail;
    private String userId;
    private String userName;
    private String versionCode;
    private String versionName;

    public CrashlyticsCore() {
        this(1.0f, null, null, false);
    }

    CrashlyticsCore(float delay, CrashlyticsListener listener, PinningInfoProvider pinningInfo, boolean disabled) {
        this(delay, listener, pinningInfo, disabled, ExecutorUtils.buildSingleThreadExecutorService("Crashlytics Exception Handler"));
    }

    CrashlyticsCore(float delay, CrashlyticsListener listener, PinningInfoProvider pinningInfo, boolean disabled, ExecutorService crashHandlerExecutor) {
        this.userId = null;
        this.userEmail = null;
        this.userName = null;
        this.attributes = new ConcurrentHashMap<>();
        this.startTime = System.currentTimeMillis();
        this.delay = delay;
        this.listener = listener;
        this.pinningInfo = pinningInfo;
        this.disabled = disabled;
        this.executorServiceWrapper = new CrashlyticsExecutorServiceWrapper(crashHandlerExecutor);
    }

    @Override // io.fabric.sdk.android.Kit
    protected boolean onPreExecute() {
        Context context = super.getContext();
        return onPreExecute(context);
    }

    boolean onPreExecute(Context context) {
        String apiKey;
        if (!this.disabled && (apiKey = new ApiKey().getValue(context)) != null) {
            Fabric.getLogger().i("Fabric", "Initializing Crashlytics " + getVersion());
            this.initializationMarkerFile = new File(getSdkDirectory(), "initialization_marker");
            boolean initializeSynchronously = false;
            try {
                try {
                    setAndValidateKitProperties(context, apiKey);
                    try {
                        SessionDataWriter sessionDataWriter = new SessionDataWriter(getContext(), this.buildId, getPackageName());
                        Fabric.getLogger().d("Fabric", "Installing exception handler...");
                        this.handler = new CrashlyticsUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), this.listener, this.executorServiceWrapper, getIdManager(), sessionDataWriter, this);
                        initializeSynchronously = didPreviousInitializationComplete();
                        this.handler.ensureOpenSessionExists();
                        Thread.setDefaultUncaughtExceptionHandler(this.handler);
                        Fabric.getLogger().d("Fabric", "Successfully installed exception handler.");
                    } catch (Exception e) {
                        Fabric.getLogger().e("Fabric", "There was a problem installing the exception handler.", e);
                    }
                    if (initializeSynchronously && CommonUtils.canTryConnection(context)) {
                        finishInitSynchronously();
                        return false;
                    }
                    return true;
                } catch (Exception e2) {
                    Fabric.getLogger().e("Fabric", "Crashlytics was not started due to an exception during initialization", e2);
                    return false;
                }
            } catch (CrashlyticsMissingDependencyException e3) {
                throw new UnmetDependencyException(e3);
            }
        }
        return false;
    }

    private void setAndValidateKitProperties(Context context, String apiKey) throws PackageManager.NameNotFoundException {
        CrashlyticsPinningInfoProvider infoProvider = this.pinningInfo != null ? new CrashlyticsPinningInfoProvider(this.pinningInfo) : null;
        this.httpRequestFactory = new DefaultHttpRequestFactory(Fabric.getLogger());
        this.httpRequestFactory.setPinningInfoProvider(infoProvider);
        try {
            this.packageName = context.getPackageName();
            this.installerPackageName = getIdManager().getInstallerPackageName();
            Fabric.getLogger().d("Fabric", "Installer package name is: " + this.installerPackageName);
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(this.packageName, 0);
            this.versionCode = Integer.toString(packageInfo.versionCode);
            this.versionName = packageInfo.versionName == null ? "0.0" : packageInfo.versionName;
            this.buildId = CommonUtils.resolveBuildId(context);
        } catch (Exception e) {
            Fabric.getLogger().e("Fabric", "Error setting up app properties", e);
        }
        getIdManager().getBluetoothMacAddress();
        getBuildIdValidator(this.buildId, isRequiringBuildId(context)).validate(apiKey, this.packageName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.fabric.sdk.android.Kit
    public Void doInBackground() {
        SettingsData settingsData;
        markInitializationStarted();
        this.handler.cleanInvalidTempFiles();
        boolean reportingDisabled = true;
        try {
            try {
                settingsData = Settings.getInstance().awaitSettingsData();
            } catch (Exception e) {
                Fabric.getLogger().e("Fabric", "Error dealing with settings", e);
            }
        } catch (Exception e2) {
            Fabric.getLogger().e("Fabric", "Problem encountered during Crashlytics initialization.", e2);
        } finally {
            markInitializationComplete();
        }
        if (settingsData == null) {
            Fabric.getLogger().w("Fabric", "Received null settings, skipping initialization!");
            return null;
        }
        if (settingsData.featuresData.collectReports) {
            reportingDisabled = false;
            this.handler.finalizeSessions();
            CreateReportSpiCall call = getCreateReportSpiCall(settingsData);
            if (call != null) {
                new ReportUploader(call).uploadReports(this.delay);
            } else {
                Fabric.getLogger().w("Fabric", "Unable to create a call to upload reports.");
            }
        }
        if (reportingDisabled) {
            Fabric.getLogger().d("Fabric", "Crash reporting disabled.");
        }
        return null;
    }

    @Override // io.fabric.sdk.android.Kit
    public String getIdentifier() {
        return "com.crashlytics.sdk.android.crashlytics-core";
    }

    @Override // io.fabric.sdk.android.Kit
    public String getVersion() {
        return "2.3.2.56";
    }

    public static CrashlyticsCore getInstance() {
        return (CrashlyticsCore) Fabric.getKit(CrashlyticsCore.class);
    }

    public void logException(Throwable throwable) {
        if (!this.disabled && ensureFabricWithCalled("prior to logging exceptions.")) {
            if (throwable == null) {
                Fabric.getLogger().log(5, "Fabric", "Crashlytics is ignoring a request to log a null exception.");
            } else {
                this.handler.writeNonFatalException(Thread.currentThread(), throwable);
            }
        }
    }

    public void log(String msg) {
        doLog(3, "Fabric", msg);
    }

    private void doLog(int priority, String tag, String msg) {
        if (!this.disabled && ensureFabricWithCalled("prior to logging messages.")) {
            long timestamp = System.currentTimeMillis() - this.startTime;
            this.handler.writeToLog(timestamp, formatLogMessage(priority, tag, msg));
        }
    }

    public void setString(String key, String value) {
        String value2;
        if (!this.disabled) {
            if (key == null) {
                if (getContext() != null && CommonUtils.isAppDebuggable(getContext())) {
                    throw new IllegalArgumentException("Custom attribute key must not be null.");
                }
                Fabric.getLogger().e("Fabric", "Attempting to set custom attribute with null key, ignoring.", null);
                return;
            }
            String key2 = sanitizeAttribute(key);
            if (this.attributes.size() < 64 || this.attributes.containsKey(key2)) {
                if (value == null) {
                    value2 = "";
                } else {
                    value2 = sanitizeAttribute(value);
                }
                this.attributes.put(key2, value2);
                return;
            }
            Fabric.getLogger().d("Fabric", "Exceeded maximum number of custom attributes (64)");
        }
    }

    public void setInt(String key, int value) {
        setString(key, Integer.toString(value));
    }

    private void finishInitSynchronously() throws ExecutionException, InterruptedException, TimeoutException {
        PriorityCallable<Void> callable = new PriorityCallable<Void>() { // from class: com.crashlytics.android.core.CrashlyticsCore.1
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                return CrashlyticsCore.this.doInBackground();
            }

            @Override // io.fabric.sdk.android.services.concurrency.PriorityTask, io.fabric.sdk.android.services.concurrency.PriorityProvider
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        for (Task task : getDependencies()) {
            callable.addDependency(task);
        }
        Future<Void> future = getFabric().getExecutorService().submit(callable);
        Fabric.getLogger().d("Fabric", "Crashlytics detected incomplete initialization on previous app launch. Will initialize synchronously.");
        try {
            future.get(4L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Fabric.getLogger().e("Fabric", "Crashlytics was interrupted during initialization.", e);
        } catch (ExecutionException e2) {
            Fabric.getLogger().e("Fabric", "Problem encountered during Crashlytics initialization.", e2);
        } catch (TimeoutException e3) {
            Fabric.getLogger().e("Fabric", "Crashlytics timed out during initialization.", e3);
        }
    }

    static void recordLoggedExceptionEvent(String sessionId) {
        Answers answers = (Answers) Fabric.getKit(Answers.class);
        if (answers != null) {
            answers.onException(new Crash.LoggedException(sessionId));
        }
    }

    static void recordFatalExceptionEvent(String sessionId) {
        Answers answers = (Answers) Fabric.getKit(Answers.class);
        if (answers != null) {
            answers.onException(new Crash.FatalException(sessionId));
        }
    }

    Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    BuildIdValidator getBuildIdValidator(String buildId, boolean requireBuildId) {
        return new BuildIdValidator(buildId, requireBuildId);
    }

    String getPackageName() {
        return this.packageName;
    }

    String getInstallerPackageName() {
        return this.installerPackageName;
    }

    String getVersionName() {
        return this.versionName;
    }

    String getVersionCode() {
        return this.versionCode;
    }

    String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(getContext(), "com.crashlytics.ApiEndpoint");
    }

    CrashlyticsUncaughtExceptionHandler getHandler() {
        return this.handler;
    }

    String getUserIdentifier() {
        if (getIdManager().canCollectUserIds()) {
            return this.userId;
        }
        return null;
    }

    String getUserEmail() {
        if (getIdManager().canCollectUserIds()) {
            return this.userEmail;
        }
        return null;
    }

    String getUserName() {
        if (getIdManager().canCollectUserIds()) {
            return this.userName;
        }
        return null;
    }

    void markInitializationStarted() {
        this.executorServiceWrapper.executeSyncLoggingException(new Callable<Void>() { // from class: com.crashlytics.android.core.CrashlyticsCore.2
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                CrashlyticsCore.this.initializationMarkerFile.createNewFile();
                Fabric.getLogger().d("Fabric", "Initialization marker file created.");
                return null;
            }
        });
    }

    void markInitializationComplete() {
        this.executorServiceWrapper.executeAsync(new Callable<Boolean>() { // from class: com.crashlytics.android.core.CrashlyticsCore.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                try {
                    boolean removed = CrashlyticsCore.this.initializationMarkerFile.delete();
                    Fabric.getLogger().d("Fabric", "Initialization marker file removed: " + removed);
                    return Boolean.valueOf(removed);
                } catch (Exception e) {
                    Fabric.getLogger().e("Fabric", "Problem encountered deleting Crashlytics initialization marker.", e);
                    return false;
                }
            }
        });
    }

    boolean didPreviousInitializationComplete() {
        return ((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new Callable<Boolean>() { // from class: com.crashlytics.android.core.CrashlyticsCore.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                return Boolean.valueOf(CrashlyticsCore.this.initializationMarkerFile.exists());
            }
        })).booleanValue();
    }

    SessionEventData getExternalCrashEventData() {
        if (this.externalCrashEventDataProvider == null) {
            return null;
        }
        SessionEventData eventData = this.externalCrashEventDataProvider.getCrashEventData();
        return eventData;
    }

    File getSdkDirectory() {
        return new FileStoreImpl(this).getFilesDir();
    }

    boolean shouldPromptUserBeforeSendingCrashReports() {
        return ((Boolean) Settings.getInstance().withSettings(new Settings.SettingsAccess<Boolean>() { // from class: com.crashlytics.android.core.CrashlyticsCore.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.fabric.sdk.android.services.settings.Settings.SettingsAccess
            public Boolean usingSettings(SettingsData settingsData) {
                if (settingsData.featuresData.promptEnabled) {
                    return Boolean.valueOf(CrashlyticsCore.this.shouldSendReportsWithoutPrompting() ? false : true);
                }
                return false;
            }
        }, false)).booleanValue();
    }

    boolean shouldSendReportsWithoutPrompting() {
        PreferenceStore prefStore = new PreferenceStoreImpl(this);
        return prefStore.get().getBoolean("always_send_reports_opt_in", false);
    }

    @SuppressLint({"CommitPrefEdits"})
    void setShouldSendUserReportsWithoutPrompting(boolean send) {
        PreferenceStore prefStore = new PreferenceStoreImpl(this);
        prefStore.save(prefStore.edit().putBoolean("always_send_reports_opt_in", send));
    }

    boolean canSendWithUserApproval() {
        return ((Boolean) Settings.getInstance().withSettings(new Settings.SettingsAccess<Boolean>() { // from class: com.crashlytics.android.core.CrashlyticsCore.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.fabric.sdk.android.services.settings.Settings.SettingsAccess
            public Boolean usingSettings(SettingsData settingsData) throws InterruptedException {
                boolean send = true;
                Activity activity = CrashlyticsCore.this.getFabric().getCurrentActivity();
                if (activity != null && !activity.isFinishing() && CrashlyticsCore.this.shouldPromptUserBeforeSendingCrashReports()) {
                    send = CrashlyticsCore.this.getSendDecisionFromUser(activity, settingsData.promptData);
                }
                return Boolean.valueOf(send);
            }
        }, true)).booleanValue();
    }

    CreateReportSpiCall getCreateReportSpiCall(SettingsData settingsData) {
        if (settingsData != null) {
            return new DefaultCreateReportSpiCall(this, getOverridenSpiEndpoint(), settingsData.appData.reportsUrl, this.httpRequestFactory);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getSendDecisionFromUser(final Activity context, final PromptSettingsData promptData) throws InterruptedException {
        final DialogStringResolver stringResolver = new DialogStringResolver(context, promptData);
        final OptInLatch latch = new OptInLatch();
        context.runOnUiThread(new Runnable() { // from class: com.crashlytics.android.core.CrashlyticsCore.7
            @Override // java.lang.Runnable
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                DialogInterface.OnClickListener sendClickListener = new DialogInterface.OnClickListener() { // from class: com.crashlytics.android.core.CrashlyticsCore.7.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        latch.setOptIn(true);
                        dialog.dismiss();
                    }
                };
                float density = context.getResources().getDisplayMetrics().density;
                int textViewPadding = CrashlyticsCore.this.dipsToPixels(density, 5);
                TextView textView = new TextView(context);
                textView.setAutoLinkMask(15);
                textView.setText(stringResolver.getMessage());
                textView.setTextAppearance(context, R.style.TextAppearance.Medium);
                textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
                textView.setFocusable(false);
                ScrollView scrollView = new ScrollView(context);
                scrollView.setPadding(CrashlyticsCore.this.dipsToPixels(density, 14), CrashlyticsCore.this.dipsToPixels(density, 2), CrashlyticsCore.this.dipsToPixels(density, 10), CrashlyticsCore.this.dipsToPixels(density, 12));
                scrollView.addView(textView);
                builder.setView(scrollView).setTitle(stringResolver.getTitle()).setCancelable(false).setNeutralButton(stringResolver.getSendButtonTitle(), sendClickListener);
                if (promptData.showCancelButton) {
                    DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() { // from class: com.crashlytics.android.core.CrashlyticsCore.7.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int id) {
                            latch.setOptIn(false);
                            dialog.dismiss();
                        }
                    };
                    builder.setNegativeButton(stringResolver.getCancelButtonTitle(), cancelClickListener);
                }
                if (promptData.showAlwaysSendButton) {
                    DialogInterface.OnClickListener alwaysSendClickListener = new DialogInterface.OnClickListener() { // from class: com.crashlytics.android.core.CrashlyticsCore.7.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int id) {
                            CrashlyticsCore.this.setShouldSendUserReportsWithoutPrompting(true);
                            latch.setOptIn(true);
                            dialog.dismiss();
                        }
                    };
                    builder.setPositiveButton(stringResolver.getAlwaysSendButtonTitle(), alwaysSendClickListener);
                }
                builder.show();
            }
        });
        Fabric.getLogger().d("Fabric", "Waiting for user opt-in.");
        latch.await();
        return latch.getOptIn();
    }

    SessionSettingsData getSessionSettingsData() {
        SettingsData settingsData = Settings.getInstance().awaitSettingsData();
        if (settingsData == null) {
            return null;
        }
        return settingsData.sessionData;
    }

    private boolean isRequiringBuildId(Context context) {
        return CommonUtils.getBooleanResourceValue(context, "com.crashlytics.RequireBuildId", true);
    }

    private static String formatLogMessage(int priority, String tag, String msg) {
        return CommonUtils.logPriorityToString(priority) + "/" + tag + " " + msg;
    }

    private static boolean ensureFabricWithCalled(String msg) {
        CrashlyticsCore instance = getInstance();
        if (instance != null && instance.handler != null) {
            return true;
        }
        Fabric.getLogger().e("Fabric", "Crashlytics must be initialized by calling Fabric.with(Context) " + msg, null);
        return false;
    }

    private static String sanitizeAttribute(String input) {
        if (input != null) {
            String input2 = input.trim();
            if (input2.length() > 1024) {
                return input2.substring(0, 1024);
            }
            return input2;
        }
        return input;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int dipsToPixels(float density, int dips) {
        return (int) (dips * density);
    }

    private class OptInLatch {
        private final CountDownLatch latch;
        private boolean send;

        private OptInLatch() {
            this.send = false;
            this.latch = new CountDownLatch(1);
        }

        void setOptIn(boolean optIn) {
            this.send = optIn;
            this.latch.countDown();
        }

        boolean getOptIn() {
            return this.send;
        }

        void await() throws InterruptedException {
            try {
                this.latch.await();
            } catch (InterruptedException e) {
            }
        }
    }
}
