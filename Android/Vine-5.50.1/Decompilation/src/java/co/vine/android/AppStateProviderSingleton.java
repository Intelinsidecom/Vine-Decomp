package co.vine.android;

import android.content.Context;
import co.vine.android.scribe.AppStateProvider;

/* loaded from: classes.dex */
public final class AppStateProviderSingleton {
    private static AppStateProvider sProvider;

    public static AppStateProvider getInstance(Context context) {
        if (sProvider == null) {
            sProvider = new AppStateProviderImpl(context);
        }
        return sProvider;
    }
}
