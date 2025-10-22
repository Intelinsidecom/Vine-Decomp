package com.flurry.sdk;

import android.app.Activity;

/* loaded from: classes.dex */
public interface dr {

    public enum a {
        kCreated,
        kDestroyed,
        kPaused,
        kResumed,
        kStarted,
        kStopped,
        kSaveState
    }

    void a(Activity activity, a aVar);
}
