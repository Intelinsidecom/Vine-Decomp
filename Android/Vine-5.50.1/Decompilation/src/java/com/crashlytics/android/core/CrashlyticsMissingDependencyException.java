package com.crashlytics.android.core;

/* loaded from: classes.dex */
public class CrashlyticsMissingDependencyException extends RuntimeException {
    private static final long serialVersionUID = -1151536370019872859L;

    CrashlyticsMissingDependencyException(String message) {
        super(buildExceptionMessage(message));
    }

    private static String buildExceptionMessage(String message) {
        return "\n" + message + "\n";
    }
}
