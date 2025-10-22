package com.crashlytics.android.core;

import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
class SessionReport implements Report {
    private final Map<String, String> customHeaders;
    private final File file;

    public SessionReport(File file) {
        this(file, Collections.emptyMap());
    }

    public SessionReport(File file, Map<String, String> customHeaders) {
        this.file = file;
        this.customHeaders = new HashMap(customHeaders);
        if (this.file.length() == 0) {
            this.customHeaders.putAll(ReportUploader.HEADER_INVALID_CLS_FILE);
        }
    }

    @Override // com.crashlytics.android.core.Report
    public File getFile() {
        return this.file;
    }

    @Override // com.crashlytics.android.core.Report
    public String getFileName() {
        return getFile().getName();
    }

    @Override // com.crashlytics.android.core.Report
    public String getIdentifier() {
        String fileName = getFileName();
        return fileName.substring(0, fileName.lastIndexOf(46));
    }

    @Override // com.crashlytics.android.core.Report
    public Map<String, String> getCustomHeaders() {
        return Collections.unmodifiableMap(this.customHeaders);
    }

    @Override // com.crashlytics.android.core.Report
    public boolean remove() {
        Fabric.getLogger().d("Fabric", "Removing report at " + this.file.getPath());
        return this.file.delete();
    }
}
