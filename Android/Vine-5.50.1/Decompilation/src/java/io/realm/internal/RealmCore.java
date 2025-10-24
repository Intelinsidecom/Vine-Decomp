package io.realm.internal;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Locale;

/* loaded from: classes.dex */
public class RealmCore {
    private static final String FILE_SEP = File.separator;
    private static final String PATH_SEP = File.pathSeparator;
    private static final String BINARIES_PATH = "lib" + PATH_SEP + ".." + FILE_SEP + "lib";
    private static volatile boolean libraryIsLoaded = false;

    public static boolean osIsWindows() {
        String os = System.getProperty("os.name").toLowerCase(Locale.getDefault());
        return os.contains("win");
    }

    public static synchronized void loadLibrary() {
        String jnilib;
        if (!libraryIsLoaded) {
            if (osIsWindows()) {
                loadLibraryWindows();
            } else {
                String debug = System.getenv("REALM_JAVA_DEBUG");
                if (debug == null || debug.isEmpty()) {
                    jnilib = "realm-jni";
                } else {
                    jnilib = "realm-jni-dbg";
                }
                System.loadLibrary(jnilib);
            }
            libraryIsLoaded = true;
            Version.coreLibVersionCompatible(true);
        }
    }

    private static String loadLibraryWindows() {
        try {
            addNativeLibraryPath(BINARIES_PATH);
            resetLibraryPath();
        } catch (Throwable th) {
        }
        String jnilib = loadCorrectLibrary("realm_jni32d", "realm_jni64d");
        if (jnilib != null) {
            System.out.println("!!! Realm debug version loaded. !!!\n");
        } else {
            jnilib = loadCorrectLibrary("realm_jni32", "realm_jni64");
            if (jnilib == null) {
                System.err.println("Searched java.library.path=" + System.getProperty("java.library.path"));
                throw new RuntimeException("Couldn't load the Realm JNI library 'realm_jni32.dll or realm_jni64.dll'. Please include the directory to the library in java.library.path.");
            }
        }
        return jnilib;
    }

    private static String loadCorrectLibrary(String... libraryCandidateNames) {
        for (String libraryCandidateName : libraryCandidateNames) {
            try {
                System.loadLibrary(libraryCandidateName);
                return libraryCandidateName;
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public static void addNativeLibraryPath(String path) {
        try {
            String libraryPath = System.getProperty("java.library.path") + PATH_SEP + path + PATH_SEP;
            System.setProperty("java.library.path", libraryPath);
        } catch (Exception e) {
            throw new RuntimeException("Cannot set the library path!", e);
        }
    }

    private static void resetLibraryPath() throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException("Cannot reset the library path!", e);
        }
    }
}
