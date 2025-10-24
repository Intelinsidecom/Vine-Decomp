package co.vine.android.recorder2;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import co.vine.android.recorder2.RecordControllerImpl;
import co.vine.android.recorder2.model.Draft;
import co.vine.android.recorder2.model.ImportVideoInfo;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public interface RecordController {
    boolean cameraHasFlash();

    boolean canDuplicateSegment(int i);

    void changeFilter(int i);

    void createFreshDraft();

    Drawable createGhostDrawable();

    void deleteDraft() throws IOException;

    void deleteLastSegment();

    boolean draftCanBeSaved();

    boolean draftHasBeenSaved();

    boolean draftHasSegment();

    void duplicateSegment(int i, boolean z);

    boolean editorDraftHasChanged();

    void endEditing(boolean z);

    void endRecording();

    void generateFullVideo(Runnable runnable);

    long getCurrentDuration();

    Draft getDraft();

    String getDraftFullVideoPath();

    long getMaxDuration();

    float getProgressRatioFromDuration(long j);

    int getSegmentCount();

    ArrayList<Long> getSegmentKeyframeTimestampsMS(int i) throws IOException;

    float[] getSegmentRatioBounds(int i);

    long[] getSegmentTrimBounds(int i);

    long getSegmentUntrimmedDurationMS(int i);

    String getVideoPathForLastSegment();

    String getVideoPathForSegmentAtPosition(int i);

    boolean hasRecordedMinimumDuration();

    void importVideo(Uri uri, Point point, long j, long j2, ImportProgressListener importProgressListener, String str);

    void importVideos(ArrayList<ImportVideoInfo> arrayList, ImportProgressListener importProgressListener);

    boolean isCameraFrontFacing();

    boolean isCurrentlyIdle();

    boolean isFlashOn();

    boolean isRecording();

    boolean isSegmentSilenced(int i);

    void onDestroy();

    void onPause();

    void onResume();

    void removeSegmentAt(int i);

    void revertToLoadedDraft();

    void saveDraft() throws IOException;

    void setDraft(Draft draft);

    boolean setFocusPoint(float f, float f2);

    void setGhostInvalidatedListener(InvalidateGhostListener invalidateGhostListener);

    void setRecordingEventListener(RecordControllerImpl.RecordingEventListener recordingEventListener);

    void setSegmentTrimPoints(int i, int i2, int i3);

    void startEditing();

    void startRecording();

    boolean switchCamera();

    boolean toggleFlash();

    void toggleSilenceSegment(int i);

    void turnOffFlash();
}
