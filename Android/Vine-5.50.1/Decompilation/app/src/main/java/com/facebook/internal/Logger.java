package com.facebook.internal;

import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class Logger {
    private static final HashMap<String, String> stringsToReplace = new HashMap<>();
    private final LoggingBehavior behavior;
    private StringBuilder contents;
    private int priority = 3;
    private final String tag;

    public static synchronized void registerStringToReplace(String original, String replace) {
        stringsToReplace.put(original, replace);
    }

    public static synchronized void registerAccessToken(String accessToken) {
        if (!FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.INCLUDE_ACCESS_TOKENS)) {
            registerStringToReplace(accessToken, "ACCESS_TOKEN_REMOVED");
        }
    }

    public static void log(LoggingBehavior behavior, String tag, String string) {
        log(behavior, 3, tag, string);
    }

    public static void log(LoggingBehavior behavior, String tag, String format, Object... args) {
        if (FacebookSdk.isLoggingBehaviorEnabled(behavior)) {
            String string = String.format(format, args);
            log(behavior, 3, tag, string);
        }
    }

    public static void log(LoggingBehavior behavior, int priority, String tag, String format, Object... args) {
        if (FacebookSdk.isLoggingBehaviorEnabled(behavior)) {
            String string = String.format(format, args);
            log(behavior, priority, tag, string);
        }
    }

    public static void log(LoggingBehavior behavior, int priority, String tag, String string) {
        if (FacebookSdk.isLoggingBehaviorEnabled(behavior)) {
            String string2 = replaceStrings(string);
            if (!tag.startsWith("FacebookSDK.")) {
                tag = "FacebookSDK." + tag;
            }
            Log.println(priority, tag, string2);
            if (behavior == LoggingBehavior.DEVELOPER_ERRORS) {
                new Exception().printStackTrace();
            }
        }
    }

    private static synchronized String replaceStrings(String string) {
        for (Map.Entry<String, String> entry : stringsToReplace.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue());
        }
        return string;
    }

    public Logger(LoggingBehavior behavior, String tag) {
        Validate.notNullOrEmpty(tag, "tag");
        this.behavior = behavior;
        this.tag = "FacebookSDK." + tag;
        this.contents = new StringBuilder();
    }

    public void log() {
        logString(this.contents.toString());
        this.contents = new StringBuilder();
    }

    public void logString(String string) {
        log(this.behavior, this.priority, this.tag, string);
    }

    public void append(String string) {
        if (shouldLog()) {
            this.contents.append(string);
        }
    }

    public void append(String format, Object... args) {
        if (shouldLog()) {
            this.contents.append(String.format(format, args));
        }
    }

    public void appendKeyValue(String key, Object value) {
        append("  %s:\t%s\n", key, value);
    }

    private boolean shouldLog() {
        return FacebookSdk.isLoggingBehaviorEnabled(this.behavior);
    }
}
