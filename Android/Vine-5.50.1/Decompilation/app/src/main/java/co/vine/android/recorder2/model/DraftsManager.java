package co.vine.android.recorder2.model;

import android.content.Context;
import android.os.AsyncTask;
import com.edisonwang.android.slog.SLog;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DraftsManager {
    private static ArrayList<Draft> sDrafts;

    public interface OnDraftsLoadedListener {
        void onDraftsLoaded(int i);
    }

    public static String getDraftsRootAndCreateIfNecessary(Context context) {
        String directoryName = context.getFilesDir().getPath() + "/drafts/";
        File f = new File(directoryName);
        f.mkdir();
        return f.getPath();
    }

    public static Draft newDraft(Context context) {
        if (sDrafts.size() >= 9) {
            return sDrafts.get(sDrafts.size() - 1);
        }
        String directoryName = String.valueOf(System.currentTimeMillis());
        File f = new File(getDraftsRootAndCreateIfNecessary(context) + "/" + directoryName);
        f.mkdir();
        String directoryPath = f.getPath();
        return new Draft(directoryPath);
    }

    public static int getDraftsCount(Context context) throws IOException, ClassNotFoundException {
        if (sDrafts == null) {
            loadDrafts(context);
        }
        return getDraftsCount();
    }

    public static int getDraftsCount() {
        return sDrafts.size();
    }

    public static void loadDrafts(Context context) throws IOException, ClassNotFoundException {
        sDrafts = new ArrayList<>();
        File draftsRoot = new File(getDraftsRootAndCreateIfNecessary(context));
        if (draftsRoot.exists() && draftsRoot.isDirectory()) {
            File[] draftDirectories = draftsRoot.listFiles();
            for (File draftDirectory : draftDirectories) {
                File dataBin = new File(Draft.getSerializedFilePath(draftDirectory.getPath()));
                if (dataBin.exists()) {
                    FileInputStream inputFileStream = new FileInputStream(dataBin);
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputFileStream);
                    try {
                        Draft draft = (Draft) objectInputStream.readObject();
                        if (draft != null) {
                            sDrafts.add(draft);
                        }
                    } catch (EOFException e) {
                        SLog.w("Draft corrupted: " + e);
                    } catch (InvalidClassException e2) {
                        SLog.w("Serialized model class version mismatch: " + e2);
                    } finally {
                        objectInputStream.close();
                    }
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [co.vine.android.recorder2.model.DraftsManager$1] */
    public static void loadDraftsAsync(final Context context, final OnDraftsLoadedListener onDraftsLoadedListener) {
        new AsyncTask<Void, Void, Void>() { // from class: co.vine.android.recorder2.model.DraftsManager.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                try {
                    DraftsManager.loadDrafts(context);
                    return null;
                } catch (IOException e) {
                    return null;
                } catch (ClassNotFoundException e2) {
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Void aVoid) {
                onDraftsLoadedListener.onDraftsLoaded(DraftsManager.getDraftsCount());
            }
        }.execute(new Void[0]);
    }

    public static Draft getDraftAtPosition(int i) {
        if (i < 0 || i >= sDrafts.size()) {
            return null;
        }
        return sDrafts.get(i);
    }
}
