package co.vine.android.recorder2;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder2.MuxerManager;
import co.vine.android.recorder2.audio.AudioEncoder;
import co.vine.android.recorder2.audio.AudioSoftwarePoller;
import co.vine.android.recorder2.camera.CameraController;
import co.vine.android.recorder2.camera.CameraSurfaceRenderer;
import co.vine.android.recorder2.model.Draft;
import co.vine.android.recorder2.model.DraftsManager;
import co.vine.android.recorder2.model.ImportVideoInfo;
import co.vine.android.recorder2.model.Segment;
import co.vine.android.recorder2.transcode.ImportTranscoder;
import co.vine.android.recorder2.util.AutoTrimCropSelector;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class RecordControllerImpl implements RecordController, CameraSurfaceRenderer.FrameCapturedListener {
    private AudioEncoder mAudioEncoder;
    private AudioSoftwarePoller mAudioPoller;
    private long mCachedDuration;
    private final CameraController mCameraController;
    private final SetCameraSurfaceTextureHandler mCameraHandler;
    private CombineSegmentsTask mCombiningTask;
    private final Context mContext;
    private Segment mCurrentSegment;
    private Draft mDraft;
    private Draft mDraftBeforeEditing;
    private final GLSurfaceView mGLView;
    private InvalidateGhostListener mGhostInvalidatedListener;
    private int mImportTaskCount;
    private Draft mLoadedDraft;
    private final long mMaxDuration;
    private MuxerManager mMuxerManager;
    private boolean mRecordingEnabled;
    private RecordingEventListener mRecordingEventListener;
    private final CameraSurfaceRenderer mRenderer;
    private ArrayList<Segment> mSegmentsInFlight = new ArrayList<>();
    private final long mMinDuration = 1000;

    public interface RecordingEventListener {
        void onMaxDurationReached();

        void onRecordingProgressChanged(float f, boolean z);
    }

    static /* synthetic */ int access$1108(RecordControllerImpl x0) {
        int i = x0.mImportTaskCount;
        x0.mImportTaskCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$1110(RecordControllerImpl x0) {
        int i = x0.mImportTaskCount;
        x0.mImportTaskCount = i - 1;
        return i;
    }

    @Override // co.vine.android.recorder2.RecordController
    public void changeFilter(int filterType) {
        this.mRenderer.changeFilterMode(filterType);
    }

    public RecordControllerImpl(Context context, GLSurfaceView glPreview) {
        this.mContext = context;
        this.mMaxDuration = RecordConfigUtils.getGenericConfig(context).maxDuration;
        this.mCameraController = new CameraController(glPreview, context);
        this.mCameraHandler = new SetCameraSurfaceTextureHandler(this.mCameraController);
        this.mRenderer = new CameraSurfaceRenderer(this.mCameraHandler, this);
        this.mGLView = glPreview;
        this.mGLView.setEGLContextClientVersion(2);
        this.mGLView.setRenderer(this.mRenderer);
        this.mGLView.setRenderMode(0);
        this.mRecordingEnabled = false;
        this.mDraft = DraftsManager.newDraft(context);
    }

    @Override // co.vine.android.recorder2.RecordController
    public void setRecordingEventListener(RecordingEventListener eventListener) {
        this.mRecordingEventListener = eventListener;
        invalidateProgress();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void setGhostInvalidatedListener(InvalidateGhostListener listener) {
        this.mGhostInvalidatedListener = listener;
    }

    private class FinishSegmentRunnable implements Runnable {
        Segment mSegment;

        public FinishSegmentRunnable(Segment segment) {
            this.mSegment = segment;
        }

        @Override // java.lang.Runnable
        public void run() throws Throwable {
            this.mSegment.retrieveMetadata();
            if (!RecordControllerImpl.this.mDraft.getSegments().contains(this.mSegment)) {
                RecordControllerImpl.this.mDraft.addSegment(this.mSegment);
            }
            RecordControllerImpl.this.mGhostInvalidatedListener.invalidateGhost();
            RecordControllerImpl.this.mSegmentsInFlight.remove(this.mSegment);
            if (RecordControllerImpl.this.mDraft.getDuration() >= RecordControllerImpl.this.mMaxDuration) {
                RecordControllerImpl.this.mRecordingEventListener.onMaxDurationReached();
            }
        }
    }

    @Override // co.vine.android.recorder2.RecordController
    public Draft getDraft() {
        return this.mDraft;
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean isRecording() {
        return this.mRecordingEnabled;
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean isCurrentlyIdle() {
        return this.mSegmentsInFlight.size() == 0 && !isRecording();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void startRecording() {
        if (!this.mRecordingEnabled && this.mDraft.getDuration() < this.mMaxDuration && isCurrentlyIdle()) {
            this.mRecordingEnabled = true;
            this.mGLView.queueEvent(new Runnable() { // from class: co.vine.android.recorder2.RecordControllerImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    RecordControllerImpl.this.mCurrentSegment = new Segment();
                    RecordControllerImpl.this.mSegmentsInFlight.add(RecordControllerImpl.this.mCurrentSegment);
                    RecordControllerImpl.this.mCurrentSegment.generateVideoPath(RecordControllerImpl.this.mDraft.getPath());
                    RecordControllerImpl.this.mCurrentSegment.generateGhostPath();
                    final Segment callbackSegment = RecordControllerImpl.this.mCurrentSegment;
                    RecordControllerImpl.this.mMuxerManager = new MuxerManager(RecordControllerImpl.this.mCurrentSegment.getVideoPath(), new MuxerManager.KeyframeWrittenListener() { // from class: co.vine.android.recorder2.RecordControllerImpl.1.1
                        @Override // co.vine.android.recorder2.MuxerManager.KeyframeWrittenListener
                        public void onKeyframeWritten(long timestampUS) {
                            callbackSegment.onKeyframeWritten(timestampUS);
                        }
                    }, RecordControllerImpl.this.new FinishSegmentRunnable(RecordControllerImpl.this.mCurrentSegment));
                    RecordControllerImpl.this.mAudioEncoder = new AudioEncoder(RecordControllerImpl.this.mMuxerManager);
                    RecordControllerImpl.this.mAudioPoller.setAudioEncoder(RecordControllerImpl.this.mAudioEncoder);
                    RecordControllerImpl.this.mAudioEncoder.setAudioSoftwarePoller(RecordControllerImpl.this.mAudioPoller);
                    RecordControllerImpl.this.mCameraController.lockFocus();
                    RecordControllerImpl.this.mRenderer.startRecording(RecordControllerImpl.this.mCurrentSegment.getGhostPath(), RecordControllerImpl.this.mMuxerManager);
                }
            });
        }
    }

    @Override // co.vine.android.recorder2.camera.CameraSurfaceRenderer.FrameCapturedListener
    public void onFrame(long currentRecordingDurationMillis) {
        long duration = this.mDraft.getDuration() + currentRecordingDurationMillis;
        this.mCachedDuration = Math.max(duration, this.mCachedDuration);
        this.mCurrentSegment.setDuration(currentRecordingDurationMillis);
        this.mRecordingEventListener.onRecordingProgressChanged(this.mCachedDuration / this.mMaxDuration, this.mCachedDuration >= this.mMinDuration);
        if (this.mCachedDuration >= this.mMaxDuration) {
            endRecording();
        }
    }

    @Override // co.vine.android.recorder2.RecordController
    public void endRecording() {
        if (this.mRecordingEnabled) {
            this.mRecordingEnabled = false;
            this.mGLView.queueEvent(new Runnable() { // from class: co.vine.android.recorder2.RecordControllerImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    RecordControllerImpl.this.mAudioEncoder.stop();
                    RecordControllerImpl.this.mCameraController.unlockFocus();
                    RecordControllerImpl.this.mRenderer.endRecording();
                }
            });
        }
    }

    @Override // co.vine.android.recorder2.RecordController
    public void onResume() {
        this.mCameraController.openCamera();
        this.mAudioPoller = new AudioSoftwarePoller();
        this.mAudioPoller.startPolling();
        if (this.mImportTaskCount == 0) {
            resumeGL();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resumeGL() {
        this.mGLView.onResume();
        this.mGLView.queueEvent(new Runnable() { // from class: co.vine.android.recorder2.RecordControllerImpl.3
            @Override // java.lang.Runnable
            public void run() {
                RecordControllerImpl.this.mRenderer.setCameraPreviewSize(720, 720);
            }
        });
    }

    @Override // co.vine.android.recorder2.RecordController
    public void onPause() {
        this.mCameraController.releaseCamera();
        this.mAudioPoller.setAudioEncoder(null);
        this.mAudioPoller.stopPolling();
        this.mAudioPoller = null;
        pauseGL();
    }

    private void pauseGL() {
        this.mGLView.onPause();
        this.mGLView.queueEvent(new Runnable() { // from class: co.vine.android.recorder2.RecordControllerImpl.4
            @Override // java.lang.Runnable
            public void run() {
                RecordControllerImpl.this.mRenderer.notifyPausing();
            }
        });
    }

    @Override // co.vine.android.recorder2.RecordController
    public void onDestroy() {
        this.mCameraHandler.invalidateHandler();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void generateFullVideo(Runnable onComplete) {
        this.mCombiningTask = new CombineSegmentsTask(this.mContext, this.mDraft, onComplete);
        this.mCombiningTask.execute(new Void[0]);
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean switchCamera() {
        if (this.mCameraController.canSwitchCamera()) {
            pauseGL();
            this.mCameraController.releaseCamera();
            boolean ffcOpen = this.mCameraController.toggleCameraToOpen();
            this.mCameraController.openCamera();
            resumeGL();
            return ffcOpen;
        }
        boolean ffcOpen2 = this.mCameraController.isUsingFrontFacing();
        return ffcOpen2;
    }

    @Override // co.vine.android.recorder2.RecordController
    public String getVideoPathForSegmentAtPosition(int position) {
        if (this.mDraft.getSegmentCount() > position) {
            return this.mDraft.getSegment(position).getVideoPath();
        }
        return null;
    }

    @Override // co.vine.android.recorder2.RecordController
    public String getVideoPathForLastSegment() {
        if (this.mDraft.getSegmentCount() > 0) {
            return this.mDraft.getLastSegment().getVideoPath();
        }
        return null;
    }

    @Override // co.vine.android.recorder2.RecordController
    public void deleteLastSegment() {
        if (this.mDraft.getSegmentCount() > 0) {
            this.mDraft.remove(this.mDraft.getSegmentCount() - 1);
            invalidateProgress();
            this.mGhostInvalidatedListener.invalidateGhost();
        }
    }

    @Override // co.vine.android.recorder2.RecordController
    public float[] getSegmentRatioBounds(int index) {
        if (this.mDraft.getSegmentCount() <= 0) {
            return new float[]{0.0f, 0.0f};
        }
        long[] segmentBounds = this.mDraft.getSegmentTimestampBounds(index);
        return new float[]{segmentBounds[0] / this.mMaxDuration, segmentBounds[1] / this.mMaxDuration};
    }

    @Override // co.vine.android.recorder2.RecordController
    public long[] getSegmentTrimBounds(int position) {
        return this.mDraft.getSegment(position).getTrimPointsMs();
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean draftHasSegment() {
        return this.mDraft.getSegmentCount() + this.mSegmentsInFlight.size() > 0;
    }

    @Override // co.vine.android.recorder2.RecordController
    public Drawable createGhostDrawable() {
        if (draftHasSegment()) {
            Segment ghostSegment = this.mDraft.getLastSegment();
            Drawable ghost = ghostSegment.getGhostDrawable();
            if (ghost == null) {
                Drawable ghost2 = BitmapDrawable.createFromPath(ghostSegment.getGhostPath());
                ghostSegment.setGhostDrawable(ghost2);
                return ghost2;
            }
            return ghost;
        }
        return null;
    }

    @Override // co.vine.android.recorder2.RecordController
    public void startEditing() {
        this.mDraftBeforeEditing = new Draft(this.mDraft);
    }

    @Override // co.vine.android.recorder2.RecordController
    public void endEditing(boolean discardChanges) {
        if (discardChanges) {
            this.mDraft = this.mDraftBeforeEditing;
        } else {
            this.mDraftBeforeEditing = null;
        }
        invalidateProgress();
    }

    public void invalidateProgress() {
        this.mCachedDuration = this.mDraft.getDuration();
        this.mRecordingEventListener.onRecordingProgressChanged(getProgressRatioFromDuration(this.mCachedDuration), this.mCachedDuration >= this.mMinDuration);
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean editorDraftHasChanged() {
        return !this.mDraft.equals(this.mDraftBeforeEditing);
    }

    public boolean loadedDraftHasChanged() {
        if (this.mLoadedDraft == null) {
            return !isCurrentlyIdle();
        }
        return (this.mLoadedDraft.equals(this.mDraft) && isCurrentlyIdle()) ? false : true;
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean toggleFlash() {
        return this.mCameraController.toggleFlash();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void turnOffFlash() {
        this.mCameraController.turnOffFlash();
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean cameraHasFlash() {
        return this.mCameraController.canSwitchFlash();
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean setFocusPoint(float x, float y) {
        return this.mCameraController.setFocusPointWithPreviewCoordinates(x, y);
    }

    @Override // co.vine.android.recorder2.RecordController
    public void toggleSilenceSegment(int selection) {
        this.mDraft.getSegment(selection).toggleSilence();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void duplicateSegment(int selection, boolean animate) {
        this.mDraft.duplicateSegment(selection, animate);
        invalidateProgress();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void removeSegmentAt(int which) {
        this.mDraft.remove(which);
        if (which == this.mDraft.getSegmentCount()) {
            this.mGhostInvalidatedListener.invalidateGhost();
        }
        invalidateProgress();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void setDraft(Draft draft) {
        this.mLoadedDraft = draft;
        this.mDraft = new Draft(draft);
        this.mGhostInvalidatedListener.invalidateGhost();
        invalidateProgress();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void revertToLoadedDraft() {
        if (this.mLoadedDraft != null) {
            this.mDraft = this.mLoadedDraft;
        }
    }

    @Override // co.vine.android.recorder2.RecordController
    public void createFreshDraft() {
        this.mDraft = DraftsManager.newDraft(this.mContext);
        this.mGhostInvalidatedListener.invalidateGhost();
        invalidateProgress();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void saveDraft() throws IOException {
        this.mDraft.save();
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean draftHasBeenSaved() {
        return this.mDraft.hasBeenSaved();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void deleteDraft() throws IOException {
        this.mDraft.delete();
    }

    @Override // co.vine.android.recorder2.RecordController
    public String getDraftFullVideoPath() {
        return this.mDraft.getVideoPath();
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean isCameraFrontFacing() {
        return this.mCameraController.isUsingFrontFacing();
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean isFlashOn() {
        return this.mCameraController.isFlashOn();
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean hasRecordedMinimumDuration() {
        return this.mDraft.getDuration() >= this.mMinDuration;
    }

    @Override // co.vine.android.recorder2.RecordController
    public float getProgressRatioFromDuration(long duration) {
        return duration / this.mMaxDuration;
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean isSegmentSilenced(int position) {
        return this.mDraft.getSegment(position).isSilenced();
    }

    @Override // co.vine.android.recorder2.RecordController
    public long getCurrentDuration() {
        return this.mDraft.getDuration();
    }

    @Override // co.vine.android.recorder2.RecordController
    public long getMaxDuration() {
        return this.mMaxDuration;
    }

    @Override // co.vine.android.recorder2.RecordController
    public void importVideos(ArrayList<ImportVideoInfo> videos, ImportProgressListener listener) {
        long maxAllowedUs = 1000 * RecordConfigUtils.getGenericConfig(this.mContext).maxDuration;
        long remainingUs = maxAllowedUs - (1000 * getCurrentDuration());
        AutoTrimCropSelector selector = new AutoTrimCropSelector(videos, remainingUs);
        if (selector == null) {
            throw new RuntimeException("Failed at reading video data during import.");
        }
        for (int i = 0; i < videos.size(); i++) {
            if (selector.getUri(i) != null && selector.getCropOrigin(i) != null) {
                Uri inUri = selector.getUri(i);
                long startMicros = selector.getTrimStart(i);
                long endMicros = selector.getTrimEnd(i);
                Point cropOrigin = selector.getCropOrigin(i);
                Segment segment = generateNewSegment(inUri);
                segment.setCropOrigin(cropOrigin);
                segment.setSourcePostId(videos.get(i).getSourcePostId());
                ImportAsyncTask task = new ImportAsyncTask(this.mContext, inUri, startMicros, endMicros, cropOrigin, listener, segment);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            }
        }
    }

    private Segment generateNewSegment(Uri uri) {
        Segment segment = new Segment();
        segment.generateVideoPath(this.mDraft.getPath());
        segment.generateThumbnailPath();
        segment.generateGhostPath();
        segment.setImportVideoPath(uri.getPath());
        this.mSegmentsInFlight.add(segment);
        this.mDraft.addSegment(segment);
        return segment;
    }

    @Override // co.vine.android.recorder2.RecordController
    public void importVideo(Uri inUri, Point cropOrigin, long startMicros, long endMicros, ImportProgressListener progressListener, String sourcePostId) {
        Segment segment = generateNewSegment(inUri);
        segment.setSourcePostId(sourcePostId);
        ImportAsyncTask task = new ImportAsyncTask(this.mContext, inUri, startMicros, endMicros, cropOrigin, progressListener, segment);
        task.execute(new Void[0]);
    }

    private class ImportAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        private Point mCropOrigin;
        private long mEndMicros;
        private ImportProgressListener mProgressListener;
        private Segment mSegment;
        private long mStartMicros;
        private Uri mUri;

        public ImportAsyncTask(Context context, Uri inUri, long startMicros, long endMicros, Point cropOrigin, ImportProgressListener listener, Segment segment) {
            this.mSegment = segment;
            this.mContext = context;
            this.mUri = inUri;
            this.mStartMicros = startMicros;
            this.mEndMicros = endMicros;
            this.mCropOrigin = cropOrigin;
            this.mProgressListener = listener;
            RecordControllerImpl.access$1108(RecordControllerImpl.this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) throws Throwable {
            final Segment callbackSegment = this.mSegment;
            ImportTranscoder transcoder = new ImportTranscoder(new ImportTranscoder.KeyframeWrittenListener() { // from class: co.vine.android.recorder2.RecordControllerImpl.ImportAsyncTask.1
                @Override // co.vine.android.recorder2.transcode.ImportTranscoder.KeyframeWrittenListener
                public void onKeyframeWritten(long timestampUS) {
                    callbackSegment.onKeyframeWritten(timestampUS);
                }
            });
            transcoder.transcode(this.mContext, this.mUri, this.mSegment.getVideoPath(), this.mSegment.getThumbnailPath(), this.mSegment.getGhostPath(), this.mStartMicros, this.mEndMicros, this.mCropOrigin, this.mProgressListener);
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void aVoid) throws Throwable {
            this.mSegment.setDuration((this.mEndMicros - this.mStartMicros) / 1000);
            RecordControllerImpl.this.new FinishSegmentRunnable(this.mSegment).run();
            RecordControllerImpl.this.invalidateProgress();
            this.mProgressListener.onImportProgressFinished();
            RecordControllerImpl.access$1110(RecordControllerImpl.this);
            if (RecordControllerImpl.this.mImportTaskCount == 0) {
                RecordControllerImpl.this.resumeGL();
            }
        }
    }

    @Override // co.vine.android.recorder2.RecordController
    public int getSegmentCount() {
        return this.mDraft.getSegmentCount();
    }

    @Override // co.vine.android.recorder2.RecordController
    public void setSegmentTrimPoints(int selection, int minMS, int maxMS) {
        Segment segment = this.mDraft.getSegment(selection);
        segment.setTrimStart(minMS);
        segment.setTrimEnd(maxMS);
        invalidateProgress();
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean canDuplicateSegment(int position) {
        Segment segment = this.mDraft.getSegment(position);
        return getCurrentDuration() + segment.getTrimmedDurationMS() <= this.mMaxDuration;
    }

    @Override // co.vine.android.recorder2.RecordController
    public ArrayList<Long> getSegmentKeyframeTimestampsMS(int position) throws IOException {
        return this.mDraft.getSegment(position).getKeyframeTimestampsMS();
    }

    @Override // co.vine.android.recorder2.RecordController
    public long getSegmentUntrimmedDurationMS(int position) {
        return this.mDraft.getSegment(position).getUntrimmedDurationMS();
    }

    @Override // co.vine.android.recorder2.RecordController
    public boolean draftCanBeSaved() {
        return (draftHasBeenSaved() && loadedDraftHasChanged()) || (!draftHasBeenSaved() && draftHasSegment());
    }
}
