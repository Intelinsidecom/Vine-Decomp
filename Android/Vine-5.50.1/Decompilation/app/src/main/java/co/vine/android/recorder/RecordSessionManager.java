package co.vine.android.recorder;

import android.content.Context;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.SystemUtil;
import com.edisonwang.android.slog.SLog;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public class RecordSessionManager {
    public static RecordSessionVersion DEFAULT_VERSION = RecordSessionVersion.SW_WEBM;
    private static final HashSet<String> sDeletedSessions;
    private final File mDir;
    private final RecordSessionVersion mVersion;

    static {
        if (DEFAULT_VERSION == RecordSessionVersion.HW && !RecordConfigUtils.HW_ENABLED) {
            throw new IllegalStateException("Invalidate version type, HW Encoding is off.");
        }
        if (DEFAULT_VERSION != RecordSessionVersion.HW && !RecordConfigUtils.SW_ENABLED) {
            throw new IllegalStateException("Invalidate version type, HW Encoding is on.");
        }
        sDeletedSessions = new HashSet<>();
    }

    public static RecordSessionVersion getCurrentVersion(Context context) {
        return DEFAULT_VERSION;
    }

    public RecordSessionManager(Context context, RecordSessionVersion version) throws IOException {
        this.mVersion = version;
        File root = null;
        try {
            root = context.getExternalFilesDir(null);
        } catch (RuntimeException e) {
        }
        if (root == null) {
            SLog.e("Failed to create drafts in external folder.");
            root = context.getFilesDir();
        }
        this.mDir = new File(root, version.folder);
        if (this.mDir.exists() && !this.mDir.isDirectory()) {
            FileUtils.forceDelete(this.mDir);
        }
        FileUtils.forceMkdir(this.mDir);
        try {
            long freeBytes = this.mDir.getFreeSpace();
            if (freeBytes > 0 && freeBytes < 20971520) {
                throw new NotEnoughSpaceException();
            }
            SLog.d("free space left: {}.", Long.valueOf(freeBytes));
        } catch (SecurityException e2) {
        }
    }

    public File getFolderFromName(String name) {
        if (name != null) {
            return new File(this.mDir, name);
        }
        return null;
    }

    public File createFolderForSession() throws IOException {
        File folder = new File(this.mDir, String.valueOf(System.currentTimeMillis()));
        FileUtils.forceMkdir(folder);
        return folder;
    }

    public ArrayList<File> getFolders() throws IOException {
        File[] files;
        ArrayList<File> folders = new ArrayList<>();
        File[] list = this.mDir.listFiles();
        if (list == null) {
            throw new IOException("This should never happen.");
        }
        for (File folder : list) {
            if (folder.isDirectory() && (files = folder.listFiles()) != null) {
                boolean hasAdded = false;
                int length = files.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    File file = files[i];
                    String name = file.getName();
                    if (!name.endsWith(this.mVersion.videoOutputExtension)) {
                        i++;
                    } else {
                        folders.add(folder);
                        hasAdded = true;
                        break;
                    }
                }
                if (!hasAdded) {
                    folders.add(folder);
                }
            }
        }
        return folders;
    }

    public static boolean isSessionSaved(File folder) {
        return getMetaFile(folder).exists() && getDataFile(folder, true).exists();
    }

    public void cleanUnusedFolders() {
        File[] listFiles;
        int i = 0;
        File[] list = this.mDir.listFiles();
        if (list != null) {
            for (File folder : list) {
                if (folder.isDirectory() && (listFiles = folder.listFiles()) != null) {
                    if (listFiles.length == 0) {
                        FileUtils.deleteQuietly(folder);
                        i++;
                    } else {
                        int j = listFiles.length;
                        for (File f : listFiles) {
                            if (f.getName().endsWith(".mp4") || f.getName().endsWith(".mkv")) {
                                j--;
                            }
                        }
                        if (j == 0) {
                            FileUtils.deleteQuietly(folder);
                        }
                        i++;
                    }
                }
            }
        }
        SLog.i("{} empty folders deleted.", Integer.valueOf(i));
    }

    public static class RecordSessionInfo implements Comparable<RecordSessionInfo> {
        public final File folder;
        public final RecordSessionMeta meta;
        public final File thumb;
        public final File video;

        public RecordSessionInfo(File folder, File thumb, File video, RecordSessionMeta meta) {
            this.folder = folder;
            this.thumb = thumb;
            this.video = video;
            this.meta = meta;
        }

        @Override // java.lang.Comparable
        public int compareTo(RecordSessionInfo another) {
            return Long.valueOf(this.folder.getName()).compareTo(Long.valueOf(another.folder.getName()));
        }
    }

    public static ArrayList<RecordSessionInfo> getValidSessions(Context context, RecordSessionVersion version) throws IOException {
        return version.getManager(context).getValidSessions();
    }

    public static int getNumberOfValidSessions(BasicVineRecorder recorder) throws IOException {
        return getNumberOfValidSessions(recorder.getActivity(), recorder.getVersion());
    }

    public static int getNumberOfValidSessions(Context context, RecordSessionVersion version) throws IOException {
        ArrayList<RecordSessionInfo> info = getValidSessions(context, version);
        if (info != null) {
            return info.size();
        }
        return 0;
    }

    public ArrayList<RecordSessionInfo> getValidSessions() throws IOException {
        ArrayList<File> folders = getFolders();
        ArrayList<RecordSessionInfo> sessions = new ArrayList<>(folders.size());
        Iterator<File> it = folders.iterator();
        while (it.hasNext()) {
            File folder = it.next();
            try {
                if (getMetaFile(folder).exists()) {
                    File thumb = new File(getThumbnailPath(folder));
                    File video = new File(getVideoPath(this.mVersion, folder));
                    File data = getDataFile(folder, true);
                    File data2 = getDataFile(folder, false);
                    RecordSessionMeta meta = readMetaObject(folder);
                    if (thumb.exists() && video.exists() && (data.exists() || data2.exists())) {
                        sessions.add(new RecordSessionInfo(folder, thumb, video, meta));
                    } else {
                        SLog.e("Invalid session found: {}.", folder.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                CrashUtil.logException(e);
            }
        }
        Collections.sort(sessions);
        return sessions;
    }

    public HashMap<RecordSession, File> getCrashedSession() throws IOException {
        ArrayList<File> folders = getFolders();
        Iterator<File> it = folders.iterator();
        while (it.hasNext()) {
            File folder = it.next();
            File dataFile = getDataFile(folder, false);
            if (dataFile.exists()) {
                try {
                    HashMap<RecordSession, File> map = new HashMap<>();
                    map.put((RecordSession) readObject(dataFile), folder);
                    return map;
                } catch (IOException e) {
                    SLog.e("Failed to read session object.", (Throwable) e);
                }
            }
        }
        return null;
    }

    public static File getDataFile(File folder, boolean isFinal) {
        return new File(folder, isFinal ? "data.bin" : "data.temp");
    }

    public static File getMetaFile(File folder) {
        return new File(folder, "meta.bin");
    }

    public static String getVideoPath(RecordSessionVersion version, File folder) {
        return new File(folder, "video" + version.videoOutputExtension).getAbsolutePath();
    }

    public static String getThumbnailPath(File folder) {
        return new File(folder, "thumbnail.jpg").getAbsolutePath();
    }

    public static String getSegmentVideoPath(RecordSessionVersion version, File folder) {
        return new File(folder, "segment_" + System.currentTimeMillis() + version.videoOutputExtension).getAbsolutePath();
    }

    public static String getSegmentThumbnailPath(File folder) {
        return new File(folder, "segment.jpg").getAbsolutePath();
    }

    public static String getPreviewVideoPath(RecordSessionVersion version, File folder) {
        return new File(folder, "preview" + version.videoOutputExtension).getAbsolutePath();
    }

    public static String getPreviewThumbnailPath(File folder) {
        return new File(folder, "preview.jpg").getAbsolutePath();
    }

    public static void deleteSession(File folder, String reason) throws IOException {
        CrashUtil.log("Session deleted: {}, {}.", reason, folder);
        if (sDeletedSessions.size() > 10) {
            sDeletedSessions.clear();
        }
        sDeletedSessions.add(folder.getAbsolutePath());
        if (folder.exists()) {
            FileUtils.deleteDirectory(folder);
        }
    }

    public static boolean wasSessionJustDeleted(File folder) {
        return sDeletedSessions.contains(folder.getAbsolutePath());
    }

    private static void writeObject(File f, Object obj) throws IOException {
        CrashUtil.log("Writing: {}.", f);
        SystemUtil.quietlyEnsureParentExists(f);
        OutputStream file = new FileOutputStream(f);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        try {
            output.writeObject(obj);
            output.close();
            CrashUtil.log("Closed: {}.", f);
        } catch (Throwable th) {
            output.close();
            CrashUtil.log("Closed: {}.", f);
            throw th;
        }
    }

    public static void writeRecordingFile(File folder, RecordSession session, boolean isFinal) throws IOException {
        writeData(folder, session, isFinal);
        if (isFinal) {
            writeMeta(folder, new RecordSessionMeta(session.getDurationMs()));
            FileUtils.deleteQuietly(getDataFile(folder, false));
        }
        cleanUpSegmentData(folder, session);
    }

    public static void cleanUpSegmentData(File folder, RecordSession session) {
        File segmentDataFolder = RecordSegment.getSegmentDataFolder(folder);
        if (segmentDataFolder.exists()) {
            File[] dataFiles = segmentDataFolder.listFiles();
            ArrayList<RecordSegment> segments = session.getSegments();
            HashSet<String> validSet = new HashSet<>();
            Iterator<RecordSegment> it = segments.iterator();
            while (it.hasNext()) {
                RecordSegment segment = it.next();
                String fn = segment.getDataFileName();
                if (fn != null) {
                    validSet.add(fn);
                }
            }
            for (File dataFile : dataFiles) {
                String fn2 = dataFile.getName();
                if (!validSet.contains(dataFile.getName())) {
                    boolean delete = dataFile.delete();
                    SLog.d("Detected removed segment: {}, deleted: {}.", fn2, Boolean.valueOf(delete));
                }
            }
        }
    }

    public static void cleanUpSegmentData(File folder, HashSet<RecordSegment> deletes) {
        File segmentDataFolder = RecordSegment.getSegmentDataFolder(folder);
        if (segmentDataFolder.exists()) {
            File[] dataFiles = segmentDataFolder.listFiles();
            HashSet<String> invalidSet = new HashSet<>();
            Iterator<RecordSegment> it = deletes.iterator();
            while (it.hasNext()) {
                RecordSegment segment = it.next();
                String fn = segment.getDataFileName();
                if (fn != null) {
                    invalidSet.add(fn);
                }
            }
            for (File dataFile : dataFiles) {
                String fn2 = dataFile.getName();
                if (invalidSet.contains(dataFile.getName())) {
                    boolean delete = dataFile.delete();
                    SLog.d("Detected removed segment: {}, deleted: {}.", fn2, Boolean.valueOf(delete));
                }
            }
        }
    }

    public static void writeRecordingFile(RecordingFile file, boolean isFinal) throws IOException {
        writeRecordingFile(file.folder, file.getSession(), isFinal);
    }

    private static void writeData(File folder, RecordSession session, boolean isFinal) throws IOException {
        writeObject(getDataFile(folder, isFinal), session);
        if (isFinal) {
            FileUtils.deleteQuietly(getDataFile(folder, false));
        }
    }

    private static void writeMeta(File folder, RecordSessionMeta meta) throws IOException {
        writeObject(getMetaFile(folder), meta);
    }

    private static Object readObject(File f) throws IOException {
        try {
            if (!f.exists()) {
                throw new FileNotFoundException("File not found: " + f.getPath());
            }
            InputStream file = new FileInputStream(f);
            InputStream buffer = new BufferedInputStream(file);
            try {
                ObjectInput input = new ObjectInputStream(buffer);
                try {
                    return input.readObject();
                } finally {
                    input.close();
                }
            } catch (EOFException e) {
                SLog.e("Failed to read a corrupted file.");
                throw e;
            }
        } catch (ClassNotFoundException ex) {
            throw new IOException("Invalid file found.", ex);
        }
    }

    public static RecordSession readDataObject(File folder) throws IOException {
        return (RecordSession) readObject(getDataFile(folder, true));
    }

    public static RecordSessionMeta readMetaObject(File folder) throws IOException {
        return (RecordSessionMeta) readObject(getMetaFile(folder));
    }
}
