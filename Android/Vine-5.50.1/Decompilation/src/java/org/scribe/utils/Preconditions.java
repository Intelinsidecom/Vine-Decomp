package org.scribe.utils;

import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class Preconditions {
    private static final Pattern URL_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*://\\S+");

    public static void checkNotNull(Object object, String errorMsg) {
        check(object != null, errorMsg);
    }

    public static void checkEmptyString(String string, String errorMsg) {
        check((string == null || string.trim().equals("")) ? false : true, errorMsg);
    }

    private static void check(boolean requirements, String error) {
        String message = (error == null || error.trim().length() <= 0) ? "Received an invalid parameter" : error;
        if (!requirements) {
            throw new IllegalArgumentException(message);
        }
    }
}
