package co.vine.android.recorder;

import android.graphics.Bitmap;
import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.MediaUtil;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class SwCombiningRunnable extends AbstractCombiningRunnable {
    private boolean hasLoggedException;
    private final BaseFinishProcessTask mAsyncTask;
    private final short[] mAudioArray;
    private final File mDataFolder;
    private final int mFps;
    private final boolean mIgnoreTrim;
    private final String mLastFrameOnlyModePath;
    private final String mLastFramePath;
    private final ArrayList<RecordSegment> mSegments;
    private final boolean mSingleSegment;
    private final String mThumbnailPath;
    private final byte[] mVideoArray;
    private final String mVideoPath;
    private final SwVineFrameRecorder mVideoRecorder;
    public int mVideoSize;

    public static SwCombiningRunnable newInstance(RecordingFile file, boolean isPreview, AudioArray audioArray, byte[] videoArray, ArrayList<RecordSegment> segments, SwVineFrameRecorder videoRecorder, BaseFinishProcessTask task) {
        ArrayList<RecordSegment> copy = new ArrayList<>();
        copy.addAll(segments);
        return new SwCombiningRunnable(file.folder, audioArray, videoArray, videoRecorder, task, isPreview ? file.getPreviewVideoPath() : file.getVideoPath(), isPreview ? file.getPreviewThumbnailPath() : file.getThumbnailPath(), false, Boolean.valueOf(isPreview), null, copy, false);
    }

    public static SwCombiningRunnable newSinglePreview(RecordingFile file, RecordSegment segment, SwVineFrameRecorder videoRecorder, BaseFinishProcessTask task, boolean onlyGrabLastSegmentMode, boolean ignoreTrim) {
        ArrayList<RecordSegment> segments = new ArrayList<>();
        segments.add(segment);
        segment.videoPath = file.createSegmentVideoPath();
        return new SwCombiningRunnable(file.folder, file.getSession().getAudioData(), file.getSession().getVideoData(), videoRecorder, task, segment.videoPath, file.getSegmentThumbnailPath(), true, true, onlyGrabLastSegmentMode ? file.getPreviewThumbnailPath() : null, segments, ignoreTrim);
    }

    private SwCombiningRunnable(File folder, AudioArray audioArray, byte[] videoArray, SwVineFrameRecorder videoRecorder, BaseFinishProcessTask task, String videoPath, String thumbnailPath, boolean singleSegment, Boolean isPreview, String lastFrameOnlyModePath, ArrayList<RecordSegment> segments, boolean ignoreTrim) {
        this.mFps = RecordSegment.getFrameRate(segments);
        this.mSegments = segments;
        this.mIgnoreTrim = ignoreTrim;
        this.mDataFolder = folder;
        this.mVideoPath = videoPath;
        this.mThumbnailPath = thumbnailPath;
        this.mSingleSegment = singleSegment;
        this.mAudioArray = audioArray != null ? (short[]) audioArray.getData() : null;
        this.mVideoArray = videoArray;
        this.mVideoRecorder = videoRecorder;
        this.mAsyncTask = task;
        this.mLastFrameOnlyModePath = lastFrameOnlyModePath;
        this.mLastFramePath = isPreview != null ? initLastFramePath(isPreview.booleanValue()) : null;
    }

    private String initLastFramePath(boolean isPreview) {
        if (!this.mSingleSegment || this.mLastFrameOnlyModePath != null) {
            String lastFramePath = RecordingFile.getLastFramePathThumbnailFromThumbnailPath(this.mLastFrameOnlyModePath == null ? this.mThumbnailPath : this.mLastFrameOnlyModePath, isPreview);
            return lastFramePath;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x040a  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x018e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0186 A[EDGE_INSN: B:144:0x0186->B:44:0x0186 BREAK  A[LOOP:1: B:39:0x0173->B:77:0x02cc], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0132  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0179  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x02ac  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x02b2  */
    @Override // co.vine.android.recorder.AbstractCombiningRunnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public co.vine.android.util.MediaUtil.GenerateThumbnailsRunnable combineVideos() {
        /*
            Method dump skipped, instructions count: 1273
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.SwCombiningRunnable.combineVideos():co.vine.android.util.MediaUtil$GenerateThumbnailsRunnable");
    }

    private void saveThumbnail(int nextFrameNumber) throws IOException {
        if (this.mSingleSegment && this.mSegments.get(0).removed) {
            SLog.i("Skip making thumbnail for removed single segment.");
            return;
        }
        try {
            if (this.mVideoRecorder.hasData()) {
                String lastFramePath = this.mLastFramePath;
                long start = System.currentTimeMillis();
                long duration = (long) (((nextFrameNumber * 1.0d) / this.mFps) * 1000.0d * 1000.0d);
                SLog.d("Use duration: {}.", Long.valueOf(duration));
                Bitmap[] bitmaps = MediaUtil.getVideoFrames(this.mVideoPath, lastFramePath != null ? new long[]{0, duration} : new long[]{0});
                SLog.d("Grabbing using MediaUtil took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
                if (bitmaps != null && bitmaps.length > 0 && bitmaps[0] != null) {
                    bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(this.mThumbnailPath));
                    if (lastFramePath != null && bitmaps.length > 1 && bitmaps[1] != null) {
                        bitmaps[1].compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(lastFramePath));
                    }
                }
                try {
                    if (!new File(this.mThumbnailPath).exists()) {
                        CrashUtil.log("Retry generating thumbnail.");
                        Bitmap[] bitmaps2 = MediaUtil.getVideoFrames(this.mVideoPath, new long[]{0});
                        if (bitmaps2 != null && bitmaps2.length == 1) {
                            bitmaps2[0].compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(this.mThumbnailPath));
                            SLog.i("First Thumbnail generated.");
                            return;
                        }
                        throw new IllegalStateException();
                    }
                } catch (Exception e) {
                    CrashUtil.log("Failed to generate thumbnail.");
                }
            }
        } catch (FileNotFoundException e2) {
            CrashUtil.log("Error saving thumbnail...", e2);
        }
    }
}
