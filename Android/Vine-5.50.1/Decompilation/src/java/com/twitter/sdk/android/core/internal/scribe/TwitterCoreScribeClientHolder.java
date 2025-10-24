package com.twitter.sdk.android.core.internal.scribe;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.services.common.IdManager;
import java.util.List;

/* loaded from: classes.dex */
public class TwitterCoreScribeClientHolder {
    private static DefaultScribeClient instance;

    public static DefaultScribeClient getScribeClient() {
        return instance;
    }

    public static void initialize(TwitterCore kit, List<SessionManager<? extends Session>> sessionManagers, IdManager idManager) {
        instance = new DefaultScribeClient(kit, "TwitterCore", sessionManagers, idManager);
    }
}
