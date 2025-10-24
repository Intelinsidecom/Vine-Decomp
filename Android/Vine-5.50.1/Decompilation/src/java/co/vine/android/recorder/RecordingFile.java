package co.vine.android.recorder;

import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public class RecordingFile {
    public ArrayList<RecordSegment> editedSegments;
    public final File folder;
    public boolean isDirty;
    public final boolean isLastSession;
    public boolean isSavedSession;
    private RecordSession mSession;
    public final RecordSessionVersion version;

    public RecordingFile(File saveFolder, RecordSession session, boolean isSavedSession, boolean isDirty, boolean isLastSession) {
        if (SLog.sLogsOn) {
            SLog.i("[session] New file created: {}.", Integer.valueOf(hashCode()));
        }
        this.mSession = session;
        this.version = this.mSession.getVersion();
        this.folder = saveFolder;
        this.isSavedSession = isSavedSession;
        this.isLastSession = isLastSession;
        this.isDirty = isDirty;
        try {
            FileUtils.forceMkdir(this.folder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasData() {
        return this.mSession.getSegments().size() > 0;
    }

    public RecordSession getSession() {
        return this.mSession;
    }

    public String createSegmentVideoPath() {
        return RecordSessionManager.getSegmentVideoPath(this.version, this.folder);
    }

    public String getSegmentThumbnailPath() {
        return RecordSessionManager.getSegmentThumbnailPath(this.folder);
    }

    public String getPreviewVideoPath() {
        return RecordSessionManager.getPreviewVideoPath(this.version, this.folder);
    }

    public String getPreviewThumbnailPath() {
        return RecordSessionManager.getPreviewThumbnailPath(this.folder);
    }

    public String getVideoPath() {
        return RecordSessionManager.getVideoPath(this.version, this.folder);
    }

    public String getThumbnailPath() {
        return RecordSessionManager.getThumbnailPath(this.folder);
    }

    public static String getLastFramePathThumbnailFromThumbnailPath(String thumbnailPath, boolean isPreview) {
        return thumbnailPath + ".last." + String.valueOf(isPreview) + ".jpg";
    }

    public String getLastFramePath() {
        String thumbnailPath = getThumbnailPath();
        String lastThumbnailPath = getLastFramePathThumbnailFromThumbnailPath(thumbnailPath, false);
        return !new File(lastThumbnailPath).exists() ? thumbnailPath : lastThumbnailPath;
    }

    public String toString() {
        return MessageFormatter.format("Folder: {}, isLastSession: {}, isDirty: {}, isSavedSession: {}, Session: {}", new Object[]{this.folder, Boolean.valueOf(this.isLastSession), Boolean.valueOf(this.isDirty), Boolean.valueOf(this.isSavedSession), this.mSession}).getMessage();
    }

    public void invalidateGhostThumbnail() {
        File file = new File(getLastFramePathThumbnailFromThumbnailPath(getPreviewThumbnailPath(), true));
        if (file.exists()) {
            file.renameTo(new File(getLastFramePathThumbnailFromThumbnailPath(getThumbnailPath(), false)));
        }
    }

    public boolean isValid() {
        return this.mSession.getSegments().size() > 0;
    }
}
