package com.crashlytics.android.beta;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/* loaded from: classes.dex */
class BuildProperties {
    public final String buildId;
    public final String packageName;
    public final String versionCode;
    public final String versionName;

    BuildProperties(String versionCode, String versionName, String buildId, String packageName) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.buildId = buildId;
        this.packageName = packageName;
    }

    public static BuildProperties fromProperties(Properties props) {
        String versionCode = props.getProperty("version_code");
        String versionName = props.getProperty("version_name");
        String buildId = props.getProperty("build_id");
        String packageName = props.getProperty("package_name");
        return new BuildProperties(versionCode, versionName, buildId, packageName);
    }

    public static BuildProperties fromPropertiesStream(InputStream is) throws IOException {
        Properties props = new Properties();
        props.load(is);
        return fromProperties(props);
    }
}
