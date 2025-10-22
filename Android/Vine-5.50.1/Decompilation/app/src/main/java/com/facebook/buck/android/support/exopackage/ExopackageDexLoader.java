package com.facebook.buck.android.support.exopackage;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class ExopackageDexLoader {
    private static final String TAG = "ExopackageDexLoader";

    private ExopackageDexLoader() {
    }

    public static void loadExopackageJars(Context context) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        File containingDirectory = new File("/data/local/tmp/exopackage/" + context.getPackageName() + "/secondary-dex");
        List<File> dexJars = new ArrayList<>();
        Set<String> expectedOdexSet = new HashSet<>();
        File[] files = containingDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals("metadata.txt")) {
                    if (!file.getName().endsWith(".dex.jar")) {
                        Log.w(TAG, "Skipping unexpected file in exopackage directory: " + file.getName());
                    } else {
                        dexJars.add(file);
                        expectedOdexSet.add(file.getName().replaceFirst("\\.jar$", ".dex"));
                    }
                }
            }
        }
        File dexOptDir = context.getDir("exopackage_dex_opt", 0);
        SystemClassLoaderAdder.installDexJars(context.getClassLoader(), dexOptDir, dexJars);
        File[] odexes = dexOptDir.listFiles();
        if (odexes != null) {
            for (File odex : odexes) {
                if (!expectedOdexSet.contains(odex.getName()) && !odex.delete()) {
                    Log.w(TAG, "Failed to delete stale odex: " + odex.getAbsolutePath());
                }
            }
        }
    }
}
