package co.vine.android.client;

import android.content.Context;

/* loaded from: classes.dex */
public final class AppController extends AbstractAppController {
    private static AppController sInstance;

    public static synchronized AppController getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppController(context.getApplicationContext());
        }
        return sInstance;
    }

    private AppController(Context context) {
        super(context);
    }
}
