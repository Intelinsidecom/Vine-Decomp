package com.twitter.sdk.android.core;

import com.twitter.sdk.android.core.Session;
import java.util.Map;

/* loaded from: classes.dex */
public interface SessionManager<T extends Session> {
    void clearActiveSession();

    T getActiveSession();

    T getSession(long j);

    Map<Long, T> getSessionMap();

    void setActiveSession(T t);

    void setSession(long j, T t);
}
