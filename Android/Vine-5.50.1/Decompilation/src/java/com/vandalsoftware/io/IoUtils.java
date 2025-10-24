package com.vandalsoftware.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes.dex */
public final class IoUtils {
    public static void closeQuietly(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void deleteContents(File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("not a directory: " + dir);
        }
        LinkedList<File> dirs = new LinkedList<>();
        dirs.add(dir);
        ArrayList<File> emptyDirs = new ArrayList<>();
        while (!dirs.isEmpty()) {
            File d = dirs.remove();
            File[] fs = d.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    if (f.isDirectory()) {
                        dirs.add(f);
                    } else {
                        deleteFileOrThrow(f);
                    }
                }
                emptyDirs.add(d);
            }
        }
        for (int i = emptyDirs.size() - 1; i >= 0; i--) {
            deleteFileOrThrow(emptyDirs.get(i));
        }
    }

    public static void deleteFileOrThrow(File f) throws IOException {
        if (f.exists() && !f.delete()) {
            throw new IOException("failed to delete file: " + f);
        }
    }

    public static void renameFileOrThrow(File src, File dst) throws IOException {
        if (!src.renameTo(dst)) {
            throw new IOException("file not renamed " + src + " " + dst);
        }
    }
}
