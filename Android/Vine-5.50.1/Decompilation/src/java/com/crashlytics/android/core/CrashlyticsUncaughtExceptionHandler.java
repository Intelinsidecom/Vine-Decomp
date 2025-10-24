package com.crashlytics.android.core;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import com.crashlytics.android.core.internal.models.SessionEventData;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.DeliveryMechanism;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.settings.SessionSettingsData;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.settings.SettingsData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.Thread;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
class CrashlyticsUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final CrashlyticsCore crashlyticsCore;
    private final Thread.UncaughtExceptionHandler defaultHandler;
    private final CrashlyticsExecutorServiceWrapper executorServiceWrapper;
    private final File filesDir;
    private final IdManager idManager;
    private final LogFileManager logFileManager;
    private boolean powerConnected;
    private final BroadcastReceiver powerConnectedReceiver;
    private final BroadcastReceiver powerDisconnectedReceiver;
    private final SessionDataWriter sessionDataWriter;
    static final FilenameFilter SESSION_FILE_FILTER = new FilenameFilter() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.1
        @Override // java.io.FilenameFilter
        public boolean accept(File dir, String filename) {
            return filename.length() == ".cls".length() + 35 && filename.endsWith(".cls");
        }
    };
    static final Comparator<File> LARGEST_FILE_NAME_FIRST = new Comparator<File>() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.2
        @Override // java.util.Comparator
        public int compare(File file1, File file2) {
            return file2.getName().compareTo(file1.getName());
        }
    };
    static final Comparator<File> SMALLEST_FILE_NAME_FIRST = new Comparator<File>() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.3
        @Override // java.util.Comparator
        public int compare(File file1, File file2) {
            return file1.getName().compareTo(file2.getName());
        }
    };
    static final FilenameFilter ANY_SESSION_FILENAME_FILTER = new FilenameFilter() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.4
        @Override // java.io.FilenameFilter
        public boolean accept(File file, String filename) {
            return CrashlyticsUncaughtExceptionHandler.SESSION_FILE_PATTERN.matcher(filename).matches();
        }
    };
    private static final Pattern SESSION_FILE_PATTERN = Pattern.compile("([\\d|A-Z|a-z]{12}\\-[\\d|A-Z|a-z]{4}\\-[\\d|A-Z|a-z]{4}\\-[\\d|A-Z|a-z]{12}).+");
    private static final Map<String, String> SEND_AT_CRASHTIME_HEADER = Collections.singletonMap("X-CRASHLYTICS-SEND-FLAGS", "1");
    private final AtomicInteger eventCounter = new AtomicInteger(0);
    private final AtomicBoolean receiversRegistered = new AtomicBoolean(false);
    private final AtomicBoolean isHandlingException = new AtomicBoolean(false);

    static class FileNameContainsFilter implements FilenameFilter {
        private final String string;

        public FileNameContainsFilter(String s) {
            this.string = s;
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File dir, String filename) {
            return filename.contains(this.string) && !filename.endsWith(".cls_temp");
        }
    }

    static class SessionPartFileFilter implements FilenameFilter {
        private final String sessionId;

        public SessionPartFileFilter(String sessionId) {
            this.sessionId = sessionId;
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String fileName) {
            return (fileName.equals(new StringBuilder().append(this.sessionId).append(".cls").toString()) || !fileName.contains(this.sessionId) || fileName.endsWith(".cls_temp")) ? false : true;
        }
    }

    private static class AnySessionPartFileFilter implements FilenameFilter {
        private AnySessionPartFileFilter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String fileName) {
            return !CrashlyticsUncaughtExceptionHandler.SESSION_FILE_FILTER.accept(file, fileName) && CrashlyticsUncaughtExceptionHandler.SESSION_FILE_PATTERN.matcher(fileName).matches();
        }
    }

    CrashlyticsUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler, CrashlyticsListener listener, CrashlyticsExecutorServiceWrapper executorServiceWrapper, IdManager idManager, SessionDataWriter sessionDataWriter, CrashlyticsCore crashlyticsCore) {
        this.defaultHandler = handler;
        this.executorServiceWrapper = executorServiceWrapper;
        this.idManager = idManager;
        this.crashlyticsCore = crashlyticsCore;
        this.sessionDataWriter = sessionDataWriter;
        this.filesDir = crashlyticsCore.getSdkDirectory();
        this.logFileManager = new LogFileManager(crashlyticsCore.getContext(), this.filesDir);
        notifyCrashlyticsListenerOfPreviousCrash(listener);
        this.powerConnectedReceiver = new BroadcastReceiver() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                CrashlyticsUncaughtExceptionHandler.this.powerConnected = true;
            }
        };
        IntentFilter powerConnectedFilter = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        this.powerDisconnectedReceiver = new BroadcastReceiver() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                CrashlyticsUncaughtExceptionHandler.this.powerConnected = false;
            }
        };
        IntentFilter powerDisconnectedFilter = new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");
        Context context = crashlyticsCore.getContext();
        context.registerReceiver(this.powerConnectedReceiver, powerConnectedFilter);
        context.registerReceiver(this.powerDisconnectedReceiver, powerDisconnectedFilter);
        this.receiversRegistered.set(true);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public synchronized void uncaughtException(final Thread thread, final Throwable ex) {
        this.isHandlingException.set(true);
        try {
            Fabric.getLogger().d("Fabric", "Crashlytics is handling uncaught exception \"" + ex + "\" from thread " + thread.getName());
            if (!this.receiversRegistered.getAndSet(true)) {
                Fabric.getLogger().d("Fabric", "Unregistering power receivers.");
                Context context = this.crashlyticsCore.getContext();
                context.unregisterReceiver(this.powerConnectedReceiver);
                context.unregisterReceiver(this.powerDisconnectedReceiver);
            }
            final Date now = new Date();
            this.executorServiceWrapper.executeSyncLoggingException(new Callable<Void>() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.7
                @Override // java.util.concurrent.Callable
                public Void call() throws Exception {
                    CrashlyticsUncaughtExceptionHandler.this.handleUncaughtException(now, thread, ex);
                    return null;
                }
            });
        } catch (Exception e) {
            Fabric.getLogger().e("Fabric", "An error occurred in the uncaught exception handler", e);
        } finally {
            Fabric.getLogger().d("Fabric", "Crashlytics completed exception processing. Invoking default exception handler.");
            this.defaultHandler.uncaughtException(thread, ex);
            this.isHandlingException.set(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUncaughtException(Date time, Thread thread, Throwable ex) throws Exception {
        writeFatal(time, thread, ex);
        doCloseSessions();
        doOpenSession();
        trimSessionFiles();
        if (!this.crashlyticsCore.shouldPromptUserBeforeSendingCrashReports()) {
            sendSessionReports();
        }
    }

    boolean isHandlingException() {
        return this.isHandlingException.get();
    }

    private void notifyCrashlyticsListenerOfPreviousCrash(CrashlyticsListener listener) {
        Fabric.getLogger().d("Fabric", "Checking for previous crash marker.");
        File markerFile = new File(this.crashlyticsCore.getSdkDirectory(), "crash_marker");
        if (markerFile.exists()) {
            markerFile.delete();
            if (listener != null) {
                try {
                    listener.crashlyticsDidDetectCrashDuringPreviousExecution();
                } catch (Exception e) {
                    Fabric.getLogger().e("Fabric", "Exception thrown by CrashlyticsListener while notifying of previous crash.", e);
                }
            }
        }
    }

    void writeToLog(final long timestamp, final String msg) {
        this.executorServiceWrapper.executeAsync(new Callable<Void>() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.8
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                if (!CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    CrashlyticsUncaughtExceptionHandler.this.logFileManager.writeToLog(timestamp, msg);
                    return null;
                }
                return null;
            }
        });
    }

    void writeNonFatalException(final Thread thread, final Throwable ex) {
        final Date now = new Date();
        this.executorServiceWrapper.executeAsync(new Runnable() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.9
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                if (!CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    CrashlyticsUncaughtExceptionHandler.this.doWriteNonFatal(now, thread, ex);
                }
            }
        });
    }

    private void writeFatal(Date time, Thread thread, Throwable ex) throws Throwable {
        ClsFileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            try {
                new File(this.filesDir, "crash_marker").createNewFile();
                String currentSessionId = getCurrentSessionId();
                if (currentSessionId != null) {
                    CrashlyticsCore.recordFatalExceptionEvent(currentSessionId);
                    ClsFileOutputStream fos2 = new ClsFileOutputStream(this.filesDir, currentSessionId + "SessionCrash");
                    try {
                        cos = CodedOutputStream.newInstance(fos2);
                        writeSessionEvent(cos, time, thread, ex, "crash", true);
                        fos = fos2;
                    } catch (Exception e) {
                        e = e;
                        fos = fos2;
                        Fabric.getLogger().e("Fabric", "An error occurred in the fatal exception logger", e);
                        ExceptionUtils.writeStackTraceIfNotNull(e, fos);
                        CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                        CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                        return;
                    } catch (Throwable th) {
                        th = th;
                        fos = fos2;
                        CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                        CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                        throw th;
                    }
                } else {
                    Fabric.getLogger().e("Fabric", "Tried to write a fatal exception while no session was open.", null);
                }
                CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeExternalCrashEvent(SessionEventData crashEventData) throws Throwable {
        ClsFileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            try {
                String currentSessionId = getCurrentSessionId();
                if (currentSessionId != null) {
                    CrashlyticsCore.recordFatalExceptionEvent(currentSessionId);
                    ClsFileOutputStream fos2 = new ClsFileOutputStream(this.filesDir, currentSessionId + "SessionCrash");
                    try {
                        cos = CodedOutputStream.newInstance(fos2);
                        NativeCrashWriter.writeNativeCrash(crashEventData, cos);
                        fos = fos2;
                    } catch (Exception e) {
                        e = e;
                        fos = fos2;
                        Fabric.getLogger().e("Fabric", "An error occurred in the native crash logger", e);
                        ExceptionUtils.writeStackTraceIfNotNull(e, fos);
                        CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                        CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                        return;
                    } catch (Throwable th) {
                        th = th;
                        fos = fos2;
                        CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                        CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                        throw th;
                    }
                } else {
                    Fabric.getLogger().e("Fabric", "Tried to write a native crash while no session was open.", null);
                }
                CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    void ensureOpenSessionExists() {
        this.executorServiceWrapper.executeAsync(new Callable<Void>() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.10
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                if (!CrashlyticsUncaughtExceptionHandler.this.hasOpenSession()) {
                    CrashlyticsUncaughtExceptionHandler.this.doOpenSession();
                    return null;
                }
                return null;
            }
        });
    }

    private String getCurrentSessionId() {
        File[] sessionBeginFiles = listFilesMatching(new FileNameContainsFilter("BeginSession"));
        Arrays.sort(sessionBeginFiles, LARGEST_FILE_NAME_FIRST);
        if (sessionBeginFiles.length > 0) {
            return getSessionIdFromSessionFile(sessionBeginFiles[0]);
        }
        return null;
    }

    private String getSessionIdFromSessionFile(File sessionFile) {
        return sessionFile.getName().substring(0, 35);
    }

    boolean hasOpenSession() {
        return listSessionBeginFiles().length > 0;
    }

    boolean finalizeSessions() {
        return ((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new Callable<Boolean>() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                if (!CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    SessionEventData crashEventData = CrashlyticsUncaughtExceptionHandler.this.crashlyticsCore.getExternalCrashEventData();
                    if (crashEventData != null) {
                        CrashlyticsUncaughtExceptionHandler.this.writeExternalCrashEvent(crashEventData);
                    }
                    CrashlyticsUncaughtExceptionHandler.this.doCloseSessions();
                    CrashlyticsUncaughtExceptionHandler.this.doOpenSession();
                    Fabric.getLogger().d("Fabric", "Open sessions were closed and a new session was opened.");
                    return true;
                }
                Fabric.getLogger().d("Fabric", "Skipping session finalization because a crash has already occurred.");
                return false;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doOpenSession() throws Exception {
        Date startedAt = new Date();
        String sessionIdentifier = new CLSUUID(this.idManager).toString();
        Fabric.getLogger().d("Fabric", "Opening an new session with ID " + sessionIdentifier);
        writeBeginSession(sessionIdentifier, startedAt);
        writeSessionApp(sessionIdentifier);
        writeSessionOS(sessionIdentifier);
        writeSessionDevice(sessionIdentifier);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCloseSessions() throws Exception {
        trimOpenSessions(8);
        String currentSessionId = getCurrentSessionId();
        if (currentSessionId != null) {
            writeSessionUser(currentSessionId);
            SessionSettingsData settingsData = this.crashlyticsCore.getSessionSettingsData();
            if (settingsData != null) {
                int maxLoggedExceptionsCount = settingsData.maxCustomExceptionEvents;
                Fabric.getLogger().d("Fabric", "Closing all open sessions.");
                File[] sessionBeginFiles = listSessionBeginFiles();
                if (sessionBeginFiles != null && sessionBeginFiles.length > 0) {
                    for (File sessionBeginFile : sessionBeginFiles) {
                        String sessionIdentifier = getSessionIdFromSessionFile(sessionBeginFile);
                        Fabric.getLogger().d("Fabric", "Closing session: " + sessionIdentifier);
                        writeSessionPartsToSessionFile(sessionBeginFile, sessionIdentifier, maxLoggedExceptionsCount);
                    }
                    return;
                }
                return;
            }
            Fabric.getLogger().d("Fabric", "Unable to close session. Settings are not loaded.");
            return;
        }
        Fabric.getLogger().d("Fabric", "No open sessions exist.");
    }

    private void closeWithoutRenamingOrLog(ClsFileOutputStream fos) {
        if (fos != null) {
            try {
                fos.closeInProgressStream();
            } catch (IOException ex) {
                Fabric.getLogger().e("Fabric", "Error closing session file stream in the presence of an exception", ex);
            }
        }
    }

    private void deleteSessionPartFilesFor(String sessionId) {
        File[] arr$ = listSessionPartFilesFor(sessionId);
        for (File file : arr$) {
            file.delete();
        }
    }

    private File[] listSessionPartFilesFor(String sessionId) {
        return listFilesMatching(new SessionPartFileFilter(sessionId));
    }

    private File[] listCompleteSessionFiles() {
        return listFilesMatching(SESSION_FILE_FILTER);
    }

    File[] listSessionBeginFiles() {
        return listFilesMatching(new FileNameContainsFilter("BeginSession"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File[] listFilesMatching(FilenameFilter filter) {
        return ensureFileArrayNotNull(this.filesDir.listFiles(filter));
    }

    private File[] ensureFileArrayNotNull(File[] files) {
        return files == null ? new File[0] : files;
    }

    private void trimSessionEventFiles(String sessionId, int limit) {
        Utils.capFileCount(this.filesDir, new FileNameContainsFilter(sessionId + "SessionEvent"), limit, SMALLEST_FILE_NAME_FIRST);
    }

    void trimSessionFiles() {
        Utils.capFileCount(this.filesDir, SESSION_FILE_FILTER, 4, SMALLEST_FILE_NAME_FIRST);
    }

    private void trimOpenSessions(int maxOpenSessionCount) {
        Set<String> sessionIdsToKeep = new HashSet<>();
        File[] beginSessionFiles = listSessionBeginFiles();
        Arrays.sort(beginSessionFiles, LARGEST_FILE_NAME_FIRST);
        int count = Math.min(maxOpenSessionCount, beginSessionFiles.length);
        for (int i = 0; i < count; i++) {
            String sessionId = getSessionIdFromSessionFile(beginSessionFiles[i]);
            sessionIdsToKeep.add(sessionId);
        }
        File[] allSessionPartFiles = listFilesMatching(new AnySessionPartFileFilter());
        for (File sessionPartFile : allSessionPartFiles) {
            String fileName = sessionPartFile.getName();
            Matcher matcher = SESSION_FILE_PATTERN.matcher(fileName);
            matcher.matches();
            String sessionId2 = matcher.group(1);
            if (!sessionIdsToKeep.contains(sessionId2)) {
                Fabric.getLogger().d("Fabric", "Trimming open session file: " + fileName);
                sessionPartFile.delete();
            }
        }
    }

    void cleanInvalidTempFiles() {
        this.executorServiceWrapper.executeAsync(new Runnable() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.12
            @Override // java.lang.Runnable
            public void run() {
                CrashlyticsUncaughtExceptionHandler.this.doCleanInvalidTempFiles(CrashlyticsUncaughtExceptionHandler.this.listFilesMatching(ClsFileOutputStream.TEMP_FILENAME_FILTER));
            }
        });
    }

    void doCleanInvalidTempFiles(File[] invalidFiles) {
        deleteLegacyInvalidCacheDir();
        for (File invalidFile : invalidFiles) {
            Fabric.getLogger().d("Fabric", "Found invalid session part file: " + invalidFile);
            final String sessionId = getSessionIdFromSessionFile(invalidFile);
            FilenameFilter sessionFilter = new FilenameFilter() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.13
                @Override // java.io.FilenameFilter
                public boolean accept(File f, String name) {
                    return name.startsWith(sessionId);
                }
            };
            Fabric.getLogger().d("Fabric", "Deleting all part files for invalid session: " + sessionId);
            File[] arr$ = listFilesMatching(sessionFilter);
            for (File sessionFile : arr$) {
                Fabric.getLogger().d("Fabric", "Deleting session file: " + sessionFile);
                sessionFile.delete();
            }
        }
    }

    private void deleteLegacyInvalidCacheDir() {
        File cacheDir = new File(this.crashlyticsCore.getSdkDirectory(), "invalidClsFiles");
        if (cacheDir.exists()) {
            if (cacheDir.isDirectory()) {
                File[] arr$ = cacheDir.listFiles();
                for (File cacheFile : arr$) {
                    cacheFile.delete();
                }
            }
            cacheDir.delete();
        }
    }

    private void writeBeginSession(String sessionId, Date startedAt) throws Exception {
        FileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            try {
                FileOutputStream fos2 = new ClsFileOutputStream(this.filesDir, sessionId + "BeginSession");
                try {
                    cos = CodedOutputStream.newInstance(fos2);
                    String generator = String.format(Locale.US, "Crashlytics Android SDK/%s", this.crashlyticsCore.getVersion());
                    long startedAtSeconds = startedAt.getTime() / 1000;
                    this.sessionDataWriter.writeBeginSession(cos, sessionId, generator, startedAtSeconds);
                    CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                    CommonUtils.closeOrLog(fos2, "Failed to close begin session file.");
                } catch (Exception e) {
                    e = e;
                    fos = fos2;
                    ExceptionUtils.writeStackTraceIfNotNull(e, fos);
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    fos = fos2;
                    CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                    CommonUtils.closeOrLog(fos, "Failed to close begin session file.");
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void writeSessionApp(String sessionId) throws Exception {
        FileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            try {
                FileOutputStream fos2 = new ClsFileOutputStream(this.filesDir, sessionId + "SessionApp");
                try {
                    cos = CodedOutputStream.newInstance(fos2);
                    String packageName = this.crashlyticsCore.getPackageName();
                    String versionCode = this.crashlyticsCore.getVersionCode();
                    String versionName = this.crashlyticsCore.getVersionName();
                    String installUuid = this.idManager.getAppInstallIdentifier();
                    int deliveryMechanism = DeliveryMechanism.determineFrom(this.crashlyticsCore.getInstallerPackageName()).getId();
                    this.sessionDataWriter.writeSessionApp(cos, packageName, versionCode, versionName, installUuid, deliveryMechanism);
                    CommonUtils.flushOrLog(cos, "Failed to flush to session app file.");
                    CommonUtils.closeOrLog(fos2, "Failed to close session app file.");
                } catch (Exception e) {
                    e = e;
                    fos = fos2;
                    ExceptionUtils.writeStackTraceIfNotNull(e, fos);
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    fos = fos2;
                    CommonUtils.flushOrLog(cos, "Failed to flush to session app file.");
                    CommonUtils.closeOrLog(fos, "Failed to close session app file.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private void writeSessionOS(String sessionId) throws Exception {
        FileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            try {
                FileOutputStream fos2 = new ClsFileOutputStream(this.filesDir, sessionId + "SessionOS");
                try {
                    cos = CodedOutputStream.newInstance(fos2);
                    boolean isRooted = CommonUtils.isRooted(this.crashlyticsCore.getContext());
                    this.sessionDataWriter.writeSessionOS(cos, isRooted);
                    CommonUtils.flushOrLog(cos, "Failed to flush to session OS file.");
                    CommonUtils.closeOrLog(fos2, "Failed to close session OS file.");
                } catch (Exception e) {
                    e = e;
                    fos = fos2;
                    ExceptionUtils.writeStackTraceIfNotNull(e, fos);
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    fos = fos2;
                    CommonUtils.flushOrLog(cos, "Failed to flush to session OS file.");
                    CommonUtils.closeOrLog(fos, "Failed to close session OS file.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private void writeSessionDevice(String sessionId) throws Exception {
        FileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            try {
                FileOutputStream fos2 = new ClsFileOutputStream(this.filesDir, sessionId + "SessionDevice");
                try {
                    cos = CodedOutputStream.newInstance(fos2);
                    Context context = this.crashlyticsCore.getContext();
                    StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
                    String clsDeviceId = this.idManager.getDeviceUUID();
                    int arch = CommonUtils.getCpuArchitectureInt();
                    int availableProcessors = Runtime.getRuntime().availableProcessors();
                    long totalRam = CommonUtils.getTotalRamInBytes();
                    long diskSpace = statFs.getBlockCount() * statFs.getBlockSize();
                    boolean isEmulator = CommonUtils.isEmulator(context);
                    Map<IdManager.DeviceIdentifierType, String> ids = this.idManager.getDeviceIdentifiers();
                    int state = CommonUtils.getDeviceState(context);
                    this.sessionDataWriter.writeSessionDevice(cos, clsDeviceId, arch, Build.MODEL, availableProcessors, totalRam, diskSpace, isEmulator, ids, state, Build.MANUFACTURER, Build.PRODUCT);
                    CommonUtils.flushOrLog(cos, "Failed to flush session device info.");
                    CommonUtils.closeOrLog(fos2, "Failed to close session device file.");
                } catch (Exception e) {
                    e = e;
                    fos = fos2;
                    ExceptionUtils.writeStackTraceIfNotNull(e, fos);
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    fos = fos2;
                    CommonUtils.flushOrLog(cos, "Failed to flush session device info.");
                    CommonUtils.closeOrLog(fos, "Failed to close session device file.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private void writeSessionUser(String sessionId) throws Exception {
        FileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            try {
                FileOutputStream fos2 = new ClsFileOutputStream(this.filesDir, sessionId + "SessionUser");
                try {
                    cos = CodedOutputStream.newInstance(fos2);
                    String id = this.crashlyticsCore.getUserIdentifier();
                    String name = this.crashlyticsCore.getUserName();
                    String email = this.crashlyticsCore.getUserEmail();
                    if (id == null && name == null && email == null) {
                        CommonUtils.flushOrLog(cos, "Failed to flush session user file.");
                        CommonUtils.closeOrLog(fos2, "Failed to close session user file.");
                    } else {
                        this.sessionDataWriter.writeSessionUser(cos, id, name, email);
                        CommonUtils.flushOrLog(cos, "Failed to flush session user file.");
                        CommonUtils.closeOrLog(fos2, "Failed to close session user file.");
                    }
                } catch (Exception e) {
                    e = e;
                    fos = fos2;
                    ExceptionUtils.writeStackTraceIfNotNull(e, fos);
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    fos = fos2;
                    CommonUtils.flushOrLog(cos, "Failed to flush session user file.");
                    CommonUtils.closeOrLog(fos, "Failed to close session user file.");
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void writeSessionEvent(CodedOutputStream cos, Date time, Thread thread, Throwable ex, String eventType, boolean includeAllThreads) throws Exception {
        Thread[] threads;
        Map<String, String> attributes;
        Context context = this.crashlyticsCore.getContext();
        long eventTime = time.getTime() / 1000;
        float batteryLevel = CommonUtils.getBatteryLevel(context);
        int batteryVelocity = CommonUtils.getBatteryVelocity(context, this.powerConnected);
        boolean proximityEnabled = CommonUtils.getProximitySensorEnabled(context);
        int orientation = context.getResources().getConfiguration().orientation;
        long usedRamBytes = CommonUtils.getTotalRamInBytes() - CommonUtils.calculateFreeRamInBytes(context);
        long diskUsedBytes = CommonUtils.calculateUsedDiskSpaceInBytes(Environment.getDataDirectory().getPath());
        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = CommonUtils.getAppProcessInfo(context.getPackageName(), context);
        List<StackTraceElement[]> stacks = new LinkedList<>();
        StackTraceElement[] exceptionStack = ex.getStackTrace();
        if (includeAllThreads) {
            Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
            threads = new Thread[allStackTraces.size()];
            int i = 0;
            for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
                threads[i] = entry.getKey();
                stacks.add(entry.getValue());
                i++;
            }
        } else {
            threads = new Thread[0];
        }
        if (!CommonUtils.getBooleanResourceValue(context, "com.crashlytics.CollectCustomKeys", true)) {
            attributes = new TreeMap<>();
        } else {
            attributes = this.crashlyticsCore.getAttributes();
            if (attributes != null && attributes.size() > 1) {
                attributes = new TreeMap<>(attributes);
            }
        }
        this.sessionDataWriter.writeSessionEvent(cos, eventTime, thread, ex, eventType, threads, batteryLevel, batteryVelocity, proximityEnabled, orientation, usedRamBytes, diskUsedBytes, runningAppProcessInfo, stacks, exceptionStack, this.logFileManager, attributes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doWriteNonFatal(Date time, Thread thread, Throwable ex) throws Throwable {
        String currentSessionId = getCurrentSessionId();
        if (currentSessionId == null) {
            Fabric.getLogger().e("Fabric", "Tried to write a non-fatal exception while no session was open.", null);
            return;
        }
        CrashlyticsCore.recordLoggedExceptionEvent(currentSessionId);
        ClsFileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            try {
                Fabric.getLogger().d("Fabric", "Crashlytics is logging non-fatal exception \"" + ex + "\" from thread " + thread.getName());
                String counterString = CommonUtils.padWithZerosToMaxIntWidth(this.eventCounter.getAndIncrement());
                String nonFatalFileName = currentSessionId + "SessionEvent" + counterString;
                ClsFileOutputStream fos2 = new ClsFileOutputStream(this.filesDir, nonFatalFileName);
                try {
                    cos = CodedOutputStream.newInstance(fos2);
                    writeSessionEvent(cos, time, thread, ex, "error", false);
                    CommonUtils.flushOrLog(cos, "Failed to flush to non-fatal file.");
                    CommonUtils.closeOrLog(fos2, "Failed to close non-fatal file output stream.");
                } catch (Exception e) {
                    e = e;
                    fos = fos2;
                    Fabric.getLogger().e("Fabric", "An error occurred in the non-fatal exception logger", e);
                    ExceptionUtils.writeStackTraceIfNotNull(e, fos);
                    CommonUtils.flushOrLog(cos, "Failed to flush to non-fatal file.");
                    CommonUtils.closeOrLog(fos, "Failed to close non-fatal file output stream.");
                    trimSessionEventFiles(currentSessionId, 64);
                } catch (Throwable th) {
                    th = th;
                    fos = fos2;
                    CommonUtils.flushOrLog(cos, "Failed to flush to non-fatal file.");
                    CommonUtils.closeOrLog(fos, "Failed to close non-fatal file output stream.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
        try {
            trimSessionEventFiles(currentSessionId, 64);
        } catch (Exception e3) {
            Fabric.getLogger().e("Fabric", "An error occurred when trimming non-fatal files.", e3);
        }
    }

    private void writeSessionPartsToSessionFile(File sessionBeginFile, String sessionId, int maxLoggedExceptionsCount) throws Throwable {
        Fabric.getLogger().d("Fabric", "Collecting session parts for ID " + sessionId);
        File[] fatalFiles = listFilesMatching(new FileNameContainsFilter(sessionId + "SessionCrash"));
        boolean hasFatal = fatalFiles != null && fatalFiles.length > 0;
        Fabric.getLogger().d("Fabric", String.format(Locale.US, "Session %s has fatal exception: %s", sessionId, Boolean.valueOf(hasFatal)));
        File[] nonFatalFiles = listFilesMatching(new FileNameContainsFilter(sessionId + "SessionEvent"));
        boolean hasNonFatal = nonFatalFiles != null && nonFatalFiles.length > 0;
        Fabric.getLogger().d("Fabric", String.format(Locale.US, "Session %s has non-fatal exceptions: %s", sessionId, Boolean.valueOf(hasNonFatal)));
        if (hasFatal || hasNonFatal) {
            ClsFileOutputStream fos = null;
            CodedOutputStream cos = null;
            try {
                try {
                    ClsFileOutputStream fos2 = new ClsFileOutputStream(this.filesDir, sessionId);
                    try {
                        cos = CodedOutputStream.newInstance(fos2);
                        Fabric.getLogger().d("Fabric", "Collecting SessionStart data for session ID " + sessionId);
                        writeToCosFromFile(cos, sessionBeginFile);
                        cos.writeUInt64(4, new Date().getTime() / 1000);
                        cos.writeBool(5, hasFatal);
                        writeInitialPartsTo(cos, sessionId);
                        if (hasNonFatal) {
                            if (nonFatalFiles.length > maxLoggedExceptionsCount) {
                                Fabric.getLogger().d("Fabric", String.format(Locale.US, "Trimming down to %d logged exceptions.", Integer.valueOf(maxLoggedExceptionsCount)));
                                trimSessionEventFiles(sessionId, maxLoggedExceptionsCount);
                                nonFatalFiles = listFilesMatching(new FileNameContainsFilter(sessionId + "SessionEvent"));
                            }
                            writeNonFatalEventsTo(cos, nonFatalFiles, sessionId);
                        }
                        if (hasFatal) {
                            writeToCosFromFile(cos, fatalFiles[0]);
                        }
                        cos.writeUInt32(11, 1);
                        cos.writeEnum(12, 3);
                        CommonUtils.flushOrLog(cos, "Error flushing session file stream");
                        if (0 != 0) {
                            closeWithoutRenamingOrLog(fos2);
                        } else {
                            CommonUtils.closeOrLog(fos2, "Failed to close CLS file");
                        }
                    } catch (Exception e) {
                        e = e;
                        fos = fos2;
                        Fabric.getLogger().e("Fabric", "Failed to write session file for session ID: " + sessionId, e);
                        ExceptionUtils.writeStackTraceIfNotNull(e, fos);
                        CommonUtils.flushOrLog(cos, "Error flushing session file stream");
                        if (1 != 0) {
                            closeWithoutRenamingOrLog(fos);
                        } else {
                            CommonUtils.closeOrLog(fos, "Failed to close CLS file");
                        }
                        Fabric.getLogger().d("Fabric", "Removing session part files for ID " + sessionId);
                        deleteSessionPartFilesFor(sessionId);
                    } catch (Throwable th) {
                        th = th;
                        fos = fos2;
                        CommonUtils.flushOrLog(cos, "Error flushing session file stream");
                        if (0 != 0) {
                            closeWithoutRenamingOrLog(fos);
                        } else {
                            CommonUtils.closeOrLog(fos, "Failed to close CLS file");
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } else {
            Fabric.getLogger().d("Fabric", "No events present for session ID " + sessionId);
        }
        Fabric.getLogger().d("Fabric", "Removing session part files for ID " + sessionId);
        deleteSessionPartFilesFor(sessionId);
    }

    private void writeNonFatalEventsTo(CodedOutputStream cos, File[] nonFatalFiles, String sessionId) throws Throwable {
        Arrays.sort(nonFatalFiles, CommonUtils.FILE_MODIFIED_COMPARATOR);
        for (File nonFatalFile : nonFatalFiles) {
            try {
                Fabric.getLogger().d("Fabric", String.format(Locale.US, "Found Non Fatal for session ID %s in %s ", sessionId, nonFatalFile.getName()));
                writeToCosFromFile(cos, nonFatalFile);
            } catch (Exception e) {
                Fabric.getLogger().e("Fabric", "Error writting non-fatal to session.", e);
            }
        }
    }

    private void writeInitialPartsTo(CodedOutputStream cos, String sessionId) throws Throwable {
        String[] tags = {"SessionUser", "SessionApp", "SessionOS", "SessionDevice"};
        for (String tag : tags) {
            File[] sessionPartFiles = listFilesMatching(new FileNameContainsFilter(sessionId + tag));
            if (sessionPartFiles.length == 0) {
                Fabric.getLogger().e("Fabric", "Can't find " + tag + " data for session ID " + sessionId, null);
            } else {
                Fabric.getLogger().d("Fabric", "Collecting " + tag + " data for session ID " + sessionId);
                writeToCosFromFile(cos, sessionPartFiles[0]);
            }
        }
    }

    private void writeToCosFromFile(CodedOutputStream cos, File file) throws Throwable {
        int numRead;
        if (file.exists()) {
            long length = file.length();
            byte[] bytes = new byte[(int) length];
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(file);
                int offset = 0;
                while (offset < bytes.length && (numRead = fis2.read(bytes, offset, bytes.length - offset)) >= 0) {
                    try {
                        offset += numRead;
                    } catch (Throwable th) {
                        th = th;
                        fis = fis2;
                        CommonUtils.closeOrLog(fis, "Failed to close file input stream.");
                        throw th;
                    }
                }
                CommonUtils.closeOrLog(fis2, "Failed to close file input stream.");
                cos.writeRawBytes(bytes);
            } catch (Throwable th2) {
                th = th2;
            }
        } else {
            Fabric.getLogger().e("Fabric", "Tried to include a file that doesn't exist: " + file.getName(), null);
        }
    }

    private void sendSessionReports() {
        File[] arr$ = listCompleteSessionFiles();
        for (final File finishedSessionFile : arr$) {
            this.executorServiceWrapper.executeAsync(new Runnable() { // from class: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.14
                @Override // java.lang.Runnable
                public void run() {
                    if (CommonUtils.canTryConnection(CrashlyticsUncaughtExceptionHandler.this.crashlyticsCore.getContext())) {
                        Fabric.getLogger().d("Fabric", "Attempting to send crash report at time of crash...");
                        SettingsData settingsData = Settings.getInstance().awaitSettingsData();
                        CreateReportSpiCall call = CrashlyticsUncaughtExceptionHandler.this.crashlyticsCore.getCreateReportSpiCall(settingsData);
                        if (call != null) {
                            new ReportUploader(call).forceUpload(new SessionReport(finishedSessionFile, CrashlyticsUncaughtExceptionHandler.SEND_AT_CRASHTIME_HEADER));
                        }
                    }
                }
            });
        }
    }
}
