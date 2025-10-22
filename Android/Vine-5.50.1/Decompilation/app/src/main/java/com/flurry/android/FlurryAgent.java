package com.flurry.android;

import android.content.Context;
import android.location.Criteria;
import android.os.Build;
import com.flurry.sdk.cu;
import com.flurry.sdk.di;
import com.flurry.sdk.dk;
import com.flurry.sdk.dm;
import com.flurry.sdk.dw;
import com.flurry.sdk.el;
import com.flurry.sdk.fb;
import java.util.Date;
import java.util.Map;

/* loaded from: classes.dex */
public final class FlurryAgent {
    private static final String a = FlurryAgent.class.getSimpleName();

    private FlurryAgent() {
    }

    public static void setVersionName(String str) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            el.b(a, "String versionName passed to setVersionName was null.");
        } else {
            dm.a().a("VersionName", (Object) str);
        }
    }

    public static int getAgentVersion() {
        return dk.a().b();
    }

    public static String getReleaseVersion() {
        return dk.a().g();
    }

    public static void setReportLocation(boolean z) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            dm.a().a("ReportLocation", (Object) Boolean.valueOf(z));
        }
    }

    public static void setLocation(float f, float f2) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            dw.a().a(f, f2);
        }
    }

    public static void clearLocation() {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            dw.a().e();
        }
    }

    public static void setLogEnabled(boolean z) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else if (z) {
            el.b();
        } else {
            el.a();
        }
    }

    public static void setLogLevel(int i) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            el.a(i);
        }
    }

    public static void setContinueSessionMillis(long j) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else if (j < 5000) {
            el.b(a, "Invalid time set for session resumption: " + j);
        } else {
            dm.a().a("ContinueSessionMillis", (Object) Long.valueOf(j));
        }
    }

    public static void setLogEvents(boolean z) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            dm.a().a("LogEvents", (Object) Boolean.valueOf(z));
        }
    }

    public static void setUseHttps(boolean z) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            dm.a().a("UseHttps", (Object) Boolean.valueOf(z));
        }
    }

    public static boolean getUseHttps() {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return false;
        }
        return ((Boolean) dm.a().a("UseHttps")).booleanValue();
    }

    public static void setCaptureUncaughtExceptions(boolean z) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            dm.a().a("CaptureUncaughtExceptions", (Object) Boolean.valueOf(z));
        }
    }

    public static void addOrigin(String str, String str2) {
        addOrigin(str, str2, null);
    }

    public static void addOrigin(String str, String str2, Map<String, String> map) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("originName not specified");
        }
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("originVersion not specified");
        }
        try {
            cu.a().a(str, str2, map);
        } catch (Throwable th) {
            el.a(a, "", th);
        }
    }

    public static void onStartSession(Context context, String str) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (context == null) {
            throw new NullPointerException("Null context");
        }
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Api key not specified");
        }
        try {
            di.a().a(context, str);
        } catch (Throwable th) {
            el.a(a, "", th);
        }
    }

    public static void onEndSession(Context context) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            if (context == null) {
                throw new NullPointerException("Null context");
            }
            try {
                di.a().a(context);
            } catch (Throwable th) {
                el.a(a, "", th);
            }
        }
    }

    public static void logEvent(String str) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String eventId passed to logEvent was null.");
            return;
        }
        try {
            cu.a().a(str);
        } catch (Throwable th) {
            el.a(a, "Failed to log event: " + str, th);
        }
    }

    public static void logEvent(String str, Map<String, String> map) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String eventId passed to logEvent was null.");
            return;
        }
        if (map == null) {
            el.b(a, "String parameters passed to logEvent was null.");
            return;
        }
        try {
            cu.a().a(str, map);
        } catch (Throwable th) {
            el.a(a, "Failed to log event: " + str, th);
        }
    }

    public static void logEvent(String str, boolean z) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String eventId passed to logEvent was null.");
            return;
        }
        try {
            cu.a().a(str, z);
        } catch (Throwable th) {
            el.a(a, "Failed to log event: " + str, th);
        }
    }

    public static void logEvent(String str, Map<String, String> map, boolean z) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String eventId passed to logEvent was null.");
            return;
        }
        if (map == null) {
            el.b(a, "String parameters passed to logEvent was null.");
            return;
        }
        try {
            cu.a().a(str, map, z);
        } catch (Throwable th) {
            el.a(a, "Failed to log event: " + str, th);
        }
    }

    public static void endTimedEvent(String str) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String eventId passed to endTimedEvent was null.");
            return;
        }
        try {
            cu.a().b(str);
        } catch (Throwable th) {
            el.a(a, "Failed to signify the end of event: " + str, th);
        }
    }

    public static void endTimedEvent(String str, Map<String, String> map) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String eventId passed to endTimedEvent was null.");
            return;
        }
        if (map == null) {
            el.b(a, "String eventId passed to endTimedEvent was null.");
            return;
        }
        try {
            cu.a().b(str, map);
        } catch (Throwable th) {
            el.a(a, "Failed to signify the end of event: " + str, th);
        }
    }

    @Deprecated
    public static void onError(String str, String str2, String str3) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String errorId passed to onError was null.");
            return;
        }
        if (str2 == null) {
            el.b(a, "String message passed to onError was null.");
            return;
        }
        if (str3 == null) {
            el.b(a, "String errorClass passed to onError was null.");
            return;
        }
        try {
            cu.a().a(str, str2, str3);
        } catch (Throwable th) {
            el.a(a, "", th);
        }
    }

    public static void onError(String str, String str2, Throwable th) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String errorId passed to onError was null.");
            return;
        }
        if (str2 == null) {
            el.b(a, "String message passed to onError was null.");
            return;
        }
        if (th == null) {
            el.b(a, "Throwable passed to onError was null.");
            return;
        }
        try {
            cu.a().a(str, str2, th);
        } catch (Throwable th2) {
            el.a(a, "", th2);
        }
    }

    @Deprecated
    public static void onEvent(String str) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String eventId passed to onEvent was null.");
            return;
        }
        try {
            cu.a().c(str);
        } catch (Throwable th) {
            el.a(a, "", th);
        }
    }

    @Deprecated
    public static void onEvent(String str, Map<String, String> map) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        if (str == null) {
            el.b(a, "String eventId passed to onEvent was null.");
            return;
        }
        if (map == null) {
            el.b(a, "Parameters Map passed to onEvent was null.");
            return;
        }
        try {
            cu.a().c(str, map);
        } catch (Throwable th) {
            el.a(a, "", th);
        }
    }

    public static void onPageView() {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
            return;
        }
        try {
            cu.a().c();
        } catch (Throwable th) {
            el.a(a, "", th);
        }
    }

    public static void setReportUrl(String str) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            dm.a().a("ReportUrl", (Object) str);
        }
    }

    public static void setLocationCriteria(Criteria criteria) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else {
            dm.a().a("LocationCriteria", (Object) criteria);
        }
    }

    public static void setAge(int i) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else if (i > 0 && i < 110) {
            dm.a().a("Age", (Object) Long.valueOf(new Date(new Date(System.currentTimeMillis() - (i * 31449600000L)).getYear(), 1, 1).getTime()));
        }
    }

    public static void setGender(byte b) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        }
        switch (b) {
            case 0:
            case 1:
                dm.a().a("Gender", (Object) Byte.valueOf(b));
                break;
            default:
                dm.a().a("Gender", (Object) (byte) -1);
                break;
        }
    }

    public static void setUserId(String str) {
        if (Build.VERSION.SDK_INT < 10) {
            el.b(a, "Device SDK Version older than 10");
        } else if (str == null) {
            el.b(a, "String userId passed to setUserId was null.");
        } else {
            dm.a().a("UserId", (Object) fb.a(str));
        }
    }
}
