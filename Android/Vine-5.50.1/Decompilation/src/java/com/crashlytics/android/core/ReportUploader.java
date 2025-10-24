package com.crashlytics.android.core;

import android.content.Context;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.common.BackgroundPriorityRunnable;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
class ReportUploader {
    private final CreateReportSpiCall createReportCall;
    private final Object fileAccessLock = new Object();
    private Thread uploadThread;
    private static final FilenameFilter crashFileFilter = new FilenameFilter() { // from class: com.crashlytics.android.core.ReportUploader.1
        @Override // java.io.FilenameFilter
        public boolean accept(File dir, String filename) {
            return filename.endsWith(".cls") && !filename.contains("Session");
        }
    };
    static final Map<String, String> HEADER_INVALID_CLS_FILE = Collections.singletonMap("X-CRASHLYTICS-INVALID-SESSION", "1");
    private static final short[] RETRY_INTERVALS = {10, 20, 30, 60, 120, 300};

    public ReportUploader(CreateReportSpiCall createReportCall) {
        if (createReportCall == null) {
            throw new IllegalArgumentException("createReportCall must not be null.");
        }
        this.createReportCall = createReportCall;
    }

    public synchronized void uploadReports(float delay) {
        if (this.uploadThread == null) {
            this.uploadThread = new Thread(new Worker(delay), "Crashlytics Report Uploader");
            this.uploadThread.start();
        }
    }

    boolean forceUpload(Report report) {
        boolean sent;
        boolean removed = false;
        synchronized (this.fileAccessLock) {
            try {
                Context context = CrashlyticsCore.getInstance().getContext();
                CreateReportRequest requestData = new CreateReportRequest(new ApiKey().getValue(context), report);
                sent = this.createReportCall.invoke(requestData);
                Fabric.getLogger().i("Fabric", "Crashlytics report upload " + (sent ? "complete: " : "FAILED: ") + report.getFileName());
            } catch (Exception e) {
                Fabric.getLogger().e("Fabric", "Error occurred sending report " + report, e);
            }
            if (sent) {
                report.remove();
                removed = true;
            }
        }
        return removed;
    }

    List<Report> findReports() {
        File[] clsFiles;
        Fabric.getLogger().d("Fabric", "Checking for crash reports...");
        synchronized (this.fileAccessLock) {
            clsFiles = CrashlyticsCore.getInstance().getSdkDirectory().listFiles(crashFileFilter);
        }
        List<Report> reports = new LinkedList<>();
        for (File file : clsFiles) {
            Fabric.getLogger().d("Fabric", "Found crash report " + file.getPath());
            reports.add(new SessionReport(file));
        }
        if (reports.isEmpty()) {
            Fabric.getLogger().d("Fabric", "No reports found.");
        }
        return reports;
    }

    /* loaded from: classes2.dex */
    private class Worker extends BackgroundPriorityRunnable {
        private final float delay;

        Worker(float delay) {
            this.delay = delay;
        }

        @Override // io.fabric.sdk.android.services.common.BackgroundPriorityRunnable
        public void onRun() {
            try {
                attemptUploadWithRetry();
            } catch (Exception e) {
                Fabric.getLogger().e("Fabric", "An unexpected error occurred while attempting to upload crash reports.", e);
            }
            ReportUploader.this.uploadThread = null;
        }

        private void attemptUploadWithRetry() throws InterruptedException {
            Fabric.getLogger().d("Fabric", "Starting report processing in " + this.delay + " second(s)...");
            if (this.delay > 0.0f) {
                try {
                    Thread.sleep((long) (this.delay * 1000.0f));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            CrashlyticsCore crashlyticsCore = CrashlyticsCore.getInstance();
            CrashlyticsUncaughtExceptionHandler handler = crashlyticsCore.getHandler();
            List<Report> reports = ReportUploader.this.findReports();
            if (!handler.isHandlingException()) {
                if (!reports.isEmpty() && !crashlyticsCore.canSendWithUserApproval()) {
                    Fabric.getLogger().d("Fabric", "User declined to send. Removing " + reports.size() + " Report(s).");
                    for (Report report : reports) {
                        report.remove();
                    }
                    return;
                }
                int retryCount = 0;
                while (!reports.isEmpty() && !CrashlyticsCore.getInstance().getHandler().isHandlingException()) {
                    Fabric.getLogger().d("Fabric", "Attempting to send " + reports.size() + " report(s)");
                    for (Report report2 : reports) {
                        ReportUploader.this.forceUpload(report2);
                    }
                    reports = ReportUploader.this.findReports();
                    if (!reports.isEmpty()) {
                        int retryCount2 = retryCount + 1;
                        long interval = ReportUploader.RETRY_INTERVALS[Math.min(retryCount, ReportUploader.RETRY_INTERVALS.length - 1)];
                        Fabric.getLogger().d("Fabric", "Report submisson: scheduling delayed retry in " + interval + " seconds");
                        try {
                            Thread.sleep(1000 * interval);
                            retryCount = retryCount2;
                        } catch (InterruptedException e2) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
        }
    }
}
