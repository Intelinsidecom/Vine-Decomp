package org.apache.commons.io;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public class FileUtils {
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
    public static final BigInteger ONE_ZB = BigInteger.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID).multiply(BigInteger.valueOf(1152921504606846976L));
    public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
    public static final File[] EMPTY_FILE_ARRAY = new File[0];
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static void copyFile(File srcFile, File destFile) throws Throwable {
        copyFile(srcFile, destFile, true);
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws Throwable {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null && !parentFile.mkdirs() && !parentFile.isDirectory()) {
            throw new IOException("Destination '" + parentFile + "' directory cannot be created");
        }
        if (destFile.exists() && !destFile.canWrite()) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws Throwable {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            FileInputStream fis2 = new FileInputStream(srcFile);
            try {
                FileOutputStream fos2 = new FileOutputStream(destFile);
                try {
                    input = fis2.getChannel();
                    output = fos2.getChannel();
                    long size = input.size();
                    long pos = 0;
                    while (pos < size) {
                        long count = size - pos > 31457280 ? 31457280L : size - pos;
                        pos += output.transferFrom(input, pos, count);
                    }
                    IOUtils.closeQuietly(output);
                    IOUtils.closeQuietly((OutputStream) fos2);
                    IOUtils.closeQuietly(input);
                    IOUtils.closeQuietly((InputStream) fis2);
                    if (srcFile.length() != destFile.length()) {
                        throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
                    }
                    if (preserveFileDate) {
                        destFile.setLastModified(srcFile.lastModified());
                    }
                } catch (Throwable th) {
                    th = th;
                    fos = fos2;
                    fis = fis2;
                    IOUtils.closeQuietly(output);
                    IOUtils.closeQuietly((OutputStream) fos);
                    IOUtils.closeQuietly(input);
                    IOUtils.closeQuietly((InputStream) fis);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fis = fis2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (directory.exists()) {
            if (!isSymlink(directory)) {
                cleanDirectory(directory);
            }
            if (!directory.delete()) {
                String message = "Unable to delete directory " + directory + ".";
                throw new IOException(message);
            }
        }
    }

    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (Exception e) {
        }
        try {
            return file.delete();
        } catch (Exception e2) {
            return false;
        }
    }

    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }
        if (!directory.isDirectory()) {
            String message2 = directory + " is not a directory";
            throw new IllegalArgumentException(message2);
        }
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of " + directory);
        }
        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
            return;
        }
        boolean filePresent = file.exists();
        if (!file.delete()) {
            if (!filePresent) {
                throw new FileNotFoundException("File does not exist: " + file);
            }
            String message = "Unable to delete file: " + file;
            throw new IOException(message);
        }
    }

    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                String message = "File " + directory + " exists and is not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else if (!directory.mkdirs() && !directory.isDirectory()) {
            String message2 = "Unable to create directory " + directory;
            throw new IOException(message2);
        }
    }

    public static long sizeOf(File file) {
        if (file.exists()) {
            return file.isDirectory() ? sizeOfDirectory(file) : file.length();
        }
        String message = file + " does not exist";
        throw new IllegalArgumentException(message);
    }

    public static long sizeOfDirectory(File directory) {
        checkDirectory(directory);
        File[] files = directory.listFiles();
        if (files == null) {
            return 0L;
        }
        long size = 0;
        for (File file : files) {
            try {
                if (isSymlink(file)) {
                    continue;
                } else {
                    size += sizeOf(file);
                    if (size < 0) {
                        return size;
                    }
                }
            } catch (IOException e) {
            }
        }
        return size;
    }

    private static void checkDirectory(File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " does not exist");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
    }

    public static void moveFile(File srcFile, File destFile) throws Throwable {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.exists()) {
            throw new FileExistsException("Destination '" + destFile + "' already exists");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile, destFile);
            if (!srcFile.delete()) {
                deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
            }
        }
    }

    public static boolean isSymlink(File file) throws IOException {
        File fileInCanonicalDir;
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        if (FilenameUtils.isSystemWindows()) {
            return false;
        }
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }
        return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
    }
}
