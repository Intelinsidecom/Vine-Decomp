package io.realm.internal.log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public final class RealmLog {
    private static final List<Logger> LOGGERS = new CopyOnWriteArrayList();

    public static void add(Logger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("A non-null logger has to be provided");
        }
        LOGGERS.add(logger);
    }

    public static void d(String message) {
        for (int i = 0; i < LOGGERS.size(); i++) {
            LOGGERS.get(i).d(message);
        }
    }

    public static void w(String message) {
        for (int i = 0; i < LOGGERS.size(); i++) {
            LOGGERS.get(i).w(message);
        }
    }

    public static void e(String message, Throwable t) {
        for (int i = 0; i < LOGGERS.size(); i++) {
            LOGGERS.get(i).v(message, t);
        }
    }
}
