package co.vine.android;

import android.support.multidex.MultiDex;
import android.util.Log;
import com.facebook.buck.android.support.exopackage.ExopackageApplication;

/* loaded from: classes.dex */
public class AppShell extends ExopackageApplication {
    private static final String APP_CLASS = "co.vine.android.VineApplication";
    private static final String TAG = "AppShell";

    public AppShell() {
        super(APP_CLASS, false);
        Log.d(TAG, "Exo: false with co.vine.android.VineApplication");
    }

    @Override // com.facebook.buck.android.support.exopackage.ExopackageApplication
    protected void onBaseContextAttached() {
        super.onBaseContextAttached();
        Log.d(TAG, "Android MultiDex : On");
        try {
            MultiDex.install(getBaseContext());
        } catch (RuntimeException multiDexException) {
            if (!isDoingRobolectricTest()) {
                throw multiDexException;
            }
        }
        Log.d(TAG, "Android MultiDex : Installed.");
    }

    public static boolean isDoingRobolectricTest() throws ClassNotFoundException {
        try {
            Class<?> robolectric = Class.forName("org.robolectric.Robolectric");
            return robolectric != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
