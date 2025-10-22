package com.flurry.sdk;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;

/* loaded from: classes.dex */
public final class fa {
    private static String a = fa.class.getSimpleName();

    public static File a(boolean z) {
        File externalFilesDir = null;
        Context contextB = dl.a().b();
        if (z && "mounted".equals(Environment.getExternalStorageState()) && (Build.VERSION.SDK_INT >= 19 || contextB.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
            externalFilesDir = contextB.getExternalFilesDir(null);
        }
        if (externalFilesDir == null) {
            return contextB.getFilesDir();
        }
        return externalFilesDir;
    }

    public static File b(boolean z) {
        Context contextB = dl.a().b();
        File externalCacheDir = null;
        if (z && "mounted".equals(Environment.getExternalStorageState()) && (Build.VERSION.SDK_INT >= 19 || contextB.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0)) {
            externalCacheDir = contextB.getExternalCacheDir();
        }
        if (externalCacheDir == null) {
            return contextB.getCacheDir();
        }
        return externalCacheDir;
    }

    public static boolean a(File file) {
        if (file == null || file.getAbsoluteFile() == null) {
            return false;
        }
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return true;
        }
        if (parentFile.mkdirs() || parentFile.isDirectory()) {
            return true;
        }
        el.a(6, a, "Unable to create persistent dir: " + parentFile);
        return false;
    }

    public static boolean b(File file) {
        if (file != null && file.isDirectory()) {
            for (String str : file.list()) {
                if (!b(new File(file, str))) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String c(java.io.File r7) throws java.lang.Throwable {
        /*
            r4 = 4
            r0 = 0
            if (r7 == 0) goto La
            boolean r1 = r7.exists()
            if (r1 != 0) goto L12
        La:
            java.lang.String r1 = com.flurry.sdk.fa.a
            java.lang.String r2 = "Persistent file doesn't exist."
            com.flurry.sdk.el.a(r4, r1, r2)
        L11:
            return r0
        L12:
            java.lang.String r1 = com.flurry.sdk.fa.a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Loading persistent data: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = r7.getAbsolutePath()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.flurry.sdk.el.a(r4, r1, r2)
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L6d
            r2.<init>(r7)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L6d
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L6b
            r1.<init>()     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L6b
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r3]     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L6b
        L3c:
            int r4 = r2.read(r3)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L6b
            if (r4 <= 0) goto L60
            java.lang.String r5 = new java.lang.String     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L6b
            r6 = 0
            r5.<init>(r3, r6, r4)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L6b
            r1.append(r5)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L6b
            goto L3c
        L4c:
            r1 = move-exception
        L4d:
            r3 = 6
            java.lang.String r4 = com.flurry.sdk.fa.a     // Catch: java.lang.Throwable -> L6b
            java.lang.String r5 = "Error when loading persistent file"
            com.flurry.sdk.el.a(r3, r4, r5, r1)     // Catch: java.lang.Throwable -> L6b
            com.flurry.sdk.fb.a(r2)
            r1 = r0
        L59:
            if (r1 == 0) goto L11
            java.lang.String r0 = r1.toString()
            goto L11
        L60:
            com.flurry.sdk.fb.a(r2)
            goto L59
        L64:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L67:
            com.flurry.sdk.fb.a(r2)
            throw r0
        L6b:
            r0 = move-exception
            goto L67
        L6d:
            r1 = move-exception
            r2 = r0
            goto L4d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.fa.c(java.io.File):java.lang.String");
    }

    @Deprecated
    public static void a(File file, String str) throws Throwable {
        FileOutputStream fileOutputStream;
        if (file == null) {
            el.a(4, a, "No persistent file specified.");
            return;
        }
        if (str == null) {
            el.a(4, a, "No data specified; deleting persistent file: " + file.getAbsolutePath());
            file.delete();
            return;
        }
        el.a(4, a, "Writing persistent data: " + file.getAbsolutePath());
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (Throwable th) {
            th = th;
            fileOutputStream = null;
        }
        try {
            try {
                fileOutputStream.write(str.getBytes());
                fb.a(fileOutputStream);
            } catch (Throwable th2) {
                th = th2;
                fb.a(fileOutputStream);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            el.a(6, a, "Error writing persistent file", th);
            fb.a(fileOutputStream);
        }
    }
}
