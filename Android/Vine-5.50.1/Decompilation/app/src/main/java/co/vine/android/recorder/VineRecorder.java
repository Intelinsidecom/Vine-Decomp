package co.vine.android.recorder;

import android.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.VineLoggingException;
import co.vine.android.dragsort.DragSortWidget;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.SdkVideoView;
import co.vine.android.player.StaticSizeVideoView;
import co.vine.android.plugin.BaseRecorderPluginManager;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.RegularVineRecorder;
import co.vine.android.recorder.SwVineFrameRecorder;
import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.recorder.audio.AudioArrays;
import co.vine.android.recorder.camera.CameraSetting;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.views.OnMeasureNotifyingRelativeLayout;
import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import twitter4j.internal.http.HttpResponseCode;

@TargetApi(14)
/* loaded from: classes.dex */
public class VineRecorder extends RegularVineRecorder implements View.OnKeyListener, View.OnTouchListener, DragSortWidget.SelectionChangedListener, VideoViewInterface.OnPreparedListener {
    public boolean detectedInvalidSession;
    private final Runnable mAnimateEditModeControlsInRunnable;
    private final Runnable mChangeProgressOverlayVisibilityRunnable;
    private DragSortWidget mDragSortWidget;
    private final int mDragSortWidgetId;
    private boolean mEditing;
    private SegmentEditorAdapter mEditorAdapter;
    private int mEditorButtonsHeight;
    private EditorDoneAsyncTask mEditorDoneAsyncTask;
    private View mFinishButton;
    private final int mFinishButtonId;
    private final View.OnClickListener mFinishClicker;
    private View mFinishLoadingOverlay;
    private final int mFinishLoadingOverlayId;
    private boolean mHasPreviewedAlready;
    private final boolean mInitiallyStartedWithEditMode;
    private final boolean mIsMessaging;
    private RecordSegment mLastPlayingSegment;
    private int mLastSelectedPosition;
    private MediaActionSound mMediaActionSound;
    private View mPlayButton;
    private View mPlayButtonContainer;
    private final int mPlayButtonContainerId;
    private PlayButtonOnClickListener mPlayButtonOnClickListener;
    private View mPlayRefreshButton;
    private boolean mPlaySound;
    private View mPreviewLoadingOverlay;
    private final int mPreviewLoadingOverlayId;
    private View mProgressOverlay;
    private final int mProgressOverlayId;
    private View mRecordingOptions;
    private final int mRecordingOptionsRowId;
    private RefreshPreviewTask mRefreshPreviewTask;
    private boolean mReturnToPreview;
    private SegmentChangeDetector mSegmentChangeDetector;
    private Thread mSegmentChangeThread;
    private final SegmentHighlightRunnable mSegmentHighlightRunnable;
    private int mShortAnimTime;
    private boolean mStartWithEditMode;
    private View mThumbnailList;
    private ImageView mThumbnailOverlay;
    private final int mThumbnailOverlayId;
    private int mThumbnailPadding;
    private RecordSegment mThumbnailSegment;
    private Runnable mVideoPreviewTrimPositionChecker;
    private SdkVideoView mVideoTexture;
    private long mVideoTextureSeekEndUs;
    private long mVideoTextureSeekStartUs;
    private final ViewGroup mVideoViewContainer;
    private RelativeLayout.LayoutParams mVideoViewParams;
    private final Runnable onCameraReadyRunnable;

    public VineRecorder(RecordSessionVersion version, BaseRecorderPluginManager manager, boolean startWithEditMode, Point screenSize, boolean returnToPreview, boolean hasPreviewedAlready, RecordingFile file, Activity activity, ViewGroup videoViewContainer, int recordingOptionsRowId, int dragSortWidgetId, int progressViewResourceId, int cameraViewResourceId, int finishButtonId, int topMaskId, int bottomMaskId, int lastSegmentString, int openCameraString, int playButtonContainerId, int thumbnailOverlayId, int rootLayoutId, int editorPadding, int editorButtonsHeight, int finishLoadingOverlayId, int previewLoadingOverlayId, int progressOverlay, Drawable progressDrawable, RegularVineRecorder.DeviceIssueStringGetter deviceNotSupportedString, boolean startWithFrontFacingCamera, boolean isMessaging, MediaActionSound mediaActionSound, CharSequence importingString, CharSequence... messages) {
        super(isMessaging ? 66 : 1000, manager, screenSize, activity, progressViewResourceId, cameraViewResourceId, topMaskId, bottomMaskId, openCameraString, rootLayoutId, progressDrawable, deviceNotSupportedString, startWithFrontFacingCamera, version, importingString, messages);
        this.mLastPlayingSegment = new RecordSegment(0L, 30);
        this.onCameraReadyRunnable = new Runnable() { // from class: co.vine.android.recorder.VineRecorder.1
            @Override // java.lang.Runnable
            public void run() {
                VineRecorder.this.mPluginManager.onCameraReady(VineRecorder.this.mFrontFacing, VineRecorder.this.setAutoFocusing(true));
            }
        };
        this.mPlaySound = false;
        this.mFinishClicker = new View.OnClickListener() { // from class: co.vine.android.recorder.VineRecorder.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                VineRecorder.this.setHasPreviewedAlreadyIfNeeded();
                VineRecorder.this.finish("finishClicker");
            }
        };
        this.mAnimateEditModeControlsInRunnable = new Runnable() { // from class: co.vine.android.recorder.VineRecorder.5
            @Override // java.lang.Runnable
            public void run() {
                VineRecorder.this.mPluginManager.onAnimateEditModeControlsInUI();
                VineRecorder.this.ensureLowProfileStatusBar();
                if (VineRecorder.this.mThumbnailList != null) {
                    int thumbnailListHeight = VineRecorder.this.mThumbnailList.getHeight();
                    VineRecorder.this.mThumbnailList.setTranslationY((-thumbnailListHeight) / 2);
                    VineRecorder.this.mThumbnailList.setScaleY(0.0f);
                    VineRecorder.this.mThumbnailList.setVisibility(0);
                    VineRecorder.this.mThumbnailList.animate().scaleY(1.0f).translationY(0.0f).setDuration(250L).start();
                }
            }
        };
        this.mSegmentHighlightRunnable = new SegmentHighlightRunnable();
        this.mVideoPreviewTrimPositionChecker = new Runnable() { // from class: co.vine.android.recorder.VineRecorder.12
            @Override // java.lang.Runnable
            public void run() throws IllegalStateException {
                int position;
                int trimEndTimeMs;
                Handler handler = VineRecorder.this.getHandler();
                if (handler != null) {
                    if (VineRecorder.this.mVideoTexture != null) {
                        if (VineRecorder.this.mVideoTextureSeekEndUs >= 0 && VineRecorder.this.mVideoTextureSeekStartUs >= 0) {
                            trimEndTimeMs = ((int) VineRecorder.this.mVideoTextureSeekEndUs) / 1000;
                        } else {
                            trimEndTimeMs = -1;
                        }
                        position = VineRecorder.this.mVideoTexture.getCurrentPosition();
                        if (position >= trimEndTimeMs && trimEndTimeMs > 0) {
                            VineRecorder.this.mVideoTexture.seekTo((int) (VineRecorder.this.mVideoTextureSeekStartUs / 1000));
                            VineRecorder.this.mVideoTexture.setAutoPlayOnPrepared(true);
                        }
                    } else {
                        position = -1;
                    }
                    handler.postDelayed(this, Math.max(66, 200 - (position % HttpResponseCode.OK)));
                }
            }
        };
        this.mChangeProgressOverlayVisibilityRunnable = new Runnable() { // from class: co.vine.android.recorder.VineRecorder.22
            @Override // java.lang.Runnable
            public void run() {
                if (VineRecorder.this.mProgressOverlay.getVisibility() != 8) {
                    VineRecorder.this.mProgressOverlay.setVisibility(8);
                }
            }
        };
        this.mReturnToPreview = returnToPreview;
        this.mHasPreviewedAlready = hasPreviewedAlready;
        this.mStartWithEditMode = startWithEditMode;
        this.mInitiallyStartedWithEditMode = startWithEditMode;
        this.mEditorButtonsHeight = editorButtonsHeight;
        this.mVideoViewContainer = videoViewContainer;
        this.mShortAnimTime = this.mActivity.getResources().getInteger(R.integer.config_shortAnimTime);
        this.mThumbnailPadding = editorPadding;
        this.mFinishButtonId = finishButtonId;
        this.mFinishLastSegmentString = activity.getText(lastSegmentString);
        this.mThumbnailOverlayId = thumbnailOverlayId;
        this.mFinishLoadingOverlayId = finishLoadingOverlayId;
        this.mPlayButtonContainerId = playButtonContainerId;
        this.mPreviewLoadingOverlayId = previewLoadingOverlayId;
        this.mRecordingOptionsRowId = recordingOptionsRowId;
        this.mDragSortWidgetId = dragSortWidgetId;
        this.mProgressOverlayId = progressOverlay;
        this.mMediaActionSound = mediaActionSound;
        Locale locale = Locale.getDefault();
        this.mPlaySound = (locale != null && "ja".equals(locale.getLanguage())) || this.mActivity.getResources().getConfiguration().mcc == 440;
        swapSession("Init", file);
        this.mIsMessaging = isMessaging;
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    protected BasicVineRecorder.FinishProcessRunnable getFinishProcessRunnable() {
        return new BasicVineRecorder.FinishProcessRunnable() { // from class: co.vine.android.recorder.VineRecorder.2
            @Override // co.vine.android.recorder.BasicVineRecorder.FinishProcessRunnable
            public boolean doNotDeleteSession(boolean isFinishing) {
                return isFinishing || VineRecorder.this.mInitiallyStartedWithEditMode;
            }

            @Override // co.vine.android.recorder.BasicVineRecorder.FinishProcessRunnable
            public boolean isCompleteSession(boolean wasRecordingStarted) {
                return wasRecordingStarted || VineRecorder.this.mInitiallyStartedWithEditMode;
            }

            @Override // co.vine.android.recorder.BasicVineRecorder.FinishProcessRunnable
            protected void onPreCompleteSession(boolean wasRecordingStarted) {
                if (wasRecordingStarted) {
                    VineRecorder.this.mLastPlayingSegment = new RecordSegment(VineRecorder.this.mCurrentDurationMs, 30);
                }
            }
        };
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public RecordingFile swapSession(String tag, RecordingFile file) {
        if (file == null) {
            return null;
        }
        RecordingFile ret = super.swapSession(tag, file);
        this.mPluginManager.onSessionSwapped();
        this.mPluginManager.onSegmentDataChanged(this.mEditedSegments);
        return ret;
    }

    public void switchFlash() {
        this.mVideoController.switchFlash();
    }

    public void modifyZoom(boolean zoomIn) {
        if (this.mVideoController.isRecordingStarted()) {
            this.mVideoController.modifyZoom(zoomIn);
        }
    }

    public void stopZoom(Runnable updateRunnable) {
        if (this.mVideoController.isRecordingStarted()) {
            this.mVideoController.stopZoom();
            this.mHandler.removeCallbacks(updateRunnable);
        }
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public boolean release() {
        this.mLastPlayingSegment = null;
        return super.release();
    }

    public ViewGroup getVideoContainer() {
        if (this.mVideoTexture != null) {
            return (ViewGroup) this.mVideoTexture.getParent();
        }
        return null;
    }

    private void animateCaptureControlsIn() {
        this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.4
            @Override // java.lang.Runnable
            public void run() {
                if (VineRecorder.this.mRecordingOptions != null) {
                    VineRecorder.this.mRecordingOptions.setAlpha(0.0f);
                    VineRecorder.this.mRecordingOptions.setVisibility(0);
                    VineRecorder.this.mRecordingOptions.animate().alpha(1.0f).setDuration(250L).start();
                }
                if (VineRecorder.this.mTopMaskView != null) {
                    VineRecorder.this.mTopMaskView.setAlpha(0.0f);
                    VineRecorder.this.mTopMaskView.setVisibility(0);
                    VineRecorder.this.mTopMaskView.animate().alpha(1.0f).setDuration(250L).start();
                }
                if (VineRecorder.this.mProgressView != null) {
                    VineRecorder.this.mProgressView.setTranslationY(VineRecorder.this.mEditorButtonsHeight);
                    VineRecorder.this.mProgressView.setScaleY(0.0f);
                    VineRecorder.this.mProgressView.setVisibility(0);
                    VineRecorder.this.mProgressView.animate().scaleY(1.0f).translationY(0.0f).setDuration(250L).start();
                }
            }
        }, 250L);
        if (this.mCameraView != null) {
            this.mCameraView.setVisibility(0);
        }
    }

    private void animateCaptureControlsOut() {
        ViewPropertyAnimator am;
        ViewPropertyAnimator am2;
        ViewPropertyAnimator am3;
        View v = this.mProgressView;
        if (v != null && (am3 = v.animate()) != null) {
            am3.scaleY(0.0f).translationY(this.mEditorButtonsHeight).setDuration(250L).setListener(new ViewGoneAnimationListener(v)).start();
        }
        View v2 = this.mTopMaskView;
        if (v2 != null && (am2 = v2.animate()) != null) {
            am2.alpha(0.0f).setDuration(250L).setListener(new ViewGoneAnimationListener(v2)).start();
        }
        View v3 = this.mRecordingOptions;
        if (v3 != null && (am = v3.animate()) != null) {
            am.alpha(0.0f).setDuration(250L).setListener(new ViewGoneAnimationListener(v3)).start();
        }
        if (this.mCameraView != null) {
            this.mCameraView.setVisibility(4);
        }
    }

    private void hideCaptureControls() {
        if (this.mProgressView != null) {
            this.mProgressView.setVisibility(4);
        }
        if (this.mCameraView != null) {
            this.mCameraView.setVisibility(4);
        }
        if (this.mTopMaskView != null) {
            this.mTopMaskView.setVisibility(4);
        }
        if (this.mRecordingOptions != null) {
            this.mRecordingOptions.setVisibility(4);
        }
    }

    public void higlightProgressView(float start, float end, int color) {
        if (this.mProgressView != null) {
            this.mProgressView.setSelectedColor(color);
            this.mProgressView.setSelectedSection(start, end);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureLowProfileStatusBar() {
        Activity activity = getActivity();
        if (activity != null) {
            ViewUtil.setLowProfileSystemUiMode(activity);
        }
    }

    private void animateEditModeControlsIn() {
        this.mHandler.postDelayed(this.mAnimateEditModeControlsInRunnable, 250L);
        if (this.mPreviewLoadingOverlay != null) {
            this.mPreviewLoadingOverlay.setVisibility(4);
            this.mPreviewLoadingOverlay.setAlpha(0.0f);
        }
        if (this.mPlayButtonContainer != null) {
            this.mPlayButtonContainer.setVisibility(4);
            this.mPlayButtonContainer.setAlpha(0.0f);
        }
        if (this.mThumbnailOverlay != null) {
            this.mThumbnailOverlay.setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateEditModeControlsOut(boolean hideVideo) {
        this.mHandler.removeCallbacks(this.mAnimateEditModeControlsInRunnable);
        if (this.mEditorAdapter != null && this.mThumbnailOverlay != null && this.mPlayButtonContainer != null && this.mPreviewLoadingOverlay != null && this.mThumbnailList != null && this.mPlayButton != null) {
            this.mPluginManager.onAnimateEditModeControlsOutUI();
            this.mThumbnailOverlay.animate().alpha(0.0f).setDuration(250L).setListener(new ViewGoneAnimationListener(this.mThumbnailOverlay)).start();
            this.mPlayButtonContainer.animate().alpha(0.0f).setDuration(250L).setListener(new ViewGoneAnimationListener(this.mPlayButtonContainer)).start();
            this.mPreviewLoadingOverlay.animate().alpha(0.0f).setDuration(250L).setListener(new ViewGoneAnimationListener(this.mPreviewLoadingOverlay)).start();
            this.mThumbnailList.animate().scaleY(0.0f).translationY((-this.mThumbnailList.getHeight()) / 2).setDuration(250L).setListener(new ViewGoneAnimationListener(this.mThumbnailList)).start();
            this.mPlayButtonContainer.animate().alpha(0.0f).setDuration(250L).start();
            this.mDragSortWidget.setSelection(-1);
        }
        if (hideVideo && this.mVideoTexture != null) {
            this.mVideoTexture.setVisibility(4);
        }
    }

    public void startEditMode(boolean wasRecordingAlready) throws IllegalStateException, InterruptedException {
        adjustEditBoundaries();
        if (!this.mReturnToPreview) {
            animateCaptureControlsOut();
        } else {
            hideCaptureControls();
        }
        animateEditModeControlsIn();
        this.mLastSelectedPosition = -1;
        this.mLastPlayingSegment = null;
        if (!wasRecordingAlready) {
            this.mEditorAdapter = new SegmentEditorAdapter(this.mEditedSegments, this.mActivity, this.mDragSortWidget, this.density);
        } else {
            this.mEditorAdapter = new SegmentEditorAdapter(this.mEditorAdapter);
        }
        if (this.mDragSortWidget != null) {
            this.mDragSortWidget.setAdapter(this.mEditorAdapter);
            this.mEditorAdapter.notifyDataSetChanged();
            this.mDragSortWidget.setSelection(-1);
            this.mDragSortWidget.setFocused(0);
        }
        this.mPluginManager.onStartEditMode();
        refreshVideoView(null, true, true, false, "startEditMode");
    }

    public class SegmentHighlightRunnable implements Runnable {
        public WeakReference<RecordSegment> target;

        public SegmentHighlightRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalStateException, InterruptedException {
            if (this.target != null) {
                RecordSegment t = this.target.get();
                if (t != null) {
                    VineRecorder.this.playPreview(t, true, false, "segmentHighlight");
                    return;
                }
                return;
            }
            if (!VineRecorder.this.mVideoController.isPreviewing()) {
                VineRecorder.this.mVideoController.openDefaultCamera(VineRecorder.this.mFrontFacing, true);
            }
        }
    }

    public boolean highlightSegment(int index, int color) {
        RecordSession session;
        if (!isCurrentlyRecording() && (session = getSesion()) != null) {
            int duration = 0;
            ArrayList<RecordSegment> segments = this.mEditedSegments;
            RecordSegment target = segments.get(index);
            long start = 0;
            long end = 0;
            Iterator<RecordSegment> it = segments.iterator();
            while (it.hasNext()) {
                RecordSegment segment = it.next();
                if (!segment.removed) {
                    if (target == segment) {
                        start = duration;
                        end = start + segment.getDurationMs();
                    }
                    duration += segment.getDurationMs();
                }
            }
            SLog.i("Segment {} highlighted: {} {} ", Integer.valueOf(index), Long.valueOf(start), Long.valueOf(end));
            float max = session.getConfig().maxDuration;
            if (this.mProgressView != null) {
                if (duration > 0 && Math.floor(end / duration) == 1.0d && end < this.mProgressView.getProgressRatio()) {
                    higlightProgressView(start / max, this.mProgressView.getProgressRatio(), color);
                } else {
                    higlightProgressView(start / max, end / max, color);
                }
            }
            this.mHandler.removeCallbacks(this.mSegmentHighlightRunnable);
            this.mSegmentHighlightRunnable.target = new WeakReference<>(target);
            this.mHandler.postDelayed(this.mSegmentHighlightRunnable, 500L);
            return true;
        }
        return false;
    }

    public void unhighlightSegments() throws IllegalStateException {
        if (this.mProgressView != null) {
            this.mProgressView.setSelectedSection(0.0f, 0.0f);
        }
        if (this.mCameraView != null) {
            this.mCameraView.setVisibility(0);
        }
        pausePreview(false);
        if (this.mVideoTexture != null) {
            this.mVideoTexture.setVisibility(4);
        }
        this.mHandler.removeCallbacks(this.mSegmentHighlightRunnable);
        this.mSegmentHighlightRunnable.target = null;
        this.mHandler.postDelayed(this.mSegmentHighlightRunnable, 250L);
    }

    public void deleteLastSegment() throws InterruptedException {
        if (this.mEditedSegments.size() > 0) {
            stopProgressTimer();
            int lastIndex = 0;
            for (int i = 0; i < this.mEditedSegments.size(); i++) {
                if (!this.mEditedSegments.get(i).removed) {
                    lastIndex = i;
                }
            }
            this.mEditedSegments.get(lastIndex).removed = true;
            RecordSession session = this.mCurrentRecordingFile.getSession();
            while (this.mVideoController.wasJustGrabbingDataForSegment()) {
                SLog.i("Waiting for audio recorder to settle down.");
                try {
                    Thread.sleep(RecordConfigUtils.AUDIO_WAIT_THRESHOLD_NS / 2);
                } catch (InterruptedException e) {
                }
            }
            try {
                this.mToRemove.addAll(RecordSegment.applyEditedChanges(this.mCurrentRecordingFile.folder, session, this.mEditedSegments));
                swapTimestampsFromSegments(session, this.mEditedSegments);
                this.mCurrentRecordingFile.isDirty = this.mCurrentRecordingFile.isSavedSession || this.mEditedSegments.size() > 0;
                startProgressTimer();
                SLog.i("Segment {} deleted.", Integer.valueOf(lastIndex));
            } catch (IOException e2) {
                throw new RecordSegmentIOException(e2);
            }
        }
    }

    public void setEditMode(View view, boolean on, boolean discard) throws IllegalStateException, InterruptedException {
        int size;
        Object[] objArr = new Object[2];
        objArr[0] = on ? "on" : "off";
        objArr[1] = discard ? "discard" : "save";
        CrashUtil.log("Set edit mode {} {}.", objArr);
        boolean hasData = this.mCurrentRecordingFile != null && this.mCurrentRecordingFile.hasData();
        if (!this.mPluginManager.onSetEditMode(on, hasData)) {
            final boolean wasEditingAlready = this.mEditing;
            this.mEditing = on;
            if (hasData) {
                if (this.mEditing) {
                    this.mStartWithEditMode = false;
                    this.mHandler.post(this.mVideoPreviewTrimPositionChecker);
                    if (canKeepRecording()) {
                        if (this.mVideoController != null && this.mVideoController.isRecordingStarted()) {
                            stop("editMode", new Runnable() { // from class: co.vine.android.recorder.VineRecorder.6
                                @Override // java.lang.Runnable
                                public void run() throws IllegalStateException, InterruptedException {
                                    VineRecorder.this.startEditMode(wasEditingAlready);
                                }
                            }, true, false);
                            return;
                        } else {
                            startEditMode(wasEditingAlready);
                            return;
                        }
                    }
                    this.mEditing = false;
                    return;
                }
                this.mHandler.removeCallbacks(this.mVideoPreviewTrimPositionChecker);
                releaseSegmentChangeDetector();
                if (this.mRefreshPreviewTask != null) {
                    this.mRefreshPreviewTask.cancel(true);
                }
                if (this.mVideoTexture != null) {
                    this.mVideoTexture.stopPlayback();
                    this.mVideoTexture.setAutoPlayOnPrepared(false);
                }
                if (this.mDragSortWidget != null) {
                    this.mDragSortWidget.cleanUp();
                }
                if (!discard) {
                    if (commitChanges() && (size = this.mEditedSegments.size()) > 0 && this.mVideoController != null) {
                        this.mVideoController.makePreview(this.mEditedSegments.get(size - 1), true, false);
                    }
                    if (this.mCurrentRecordingFile != null) {
                        this.mCurrentRecordingFile.invalidateGhostThumbnail();
                    }
                }
                animateEditModeControlsOut(true);
                animateCaptureControlsIn();
                invalidateFinishButton();
                setHasPreviewedAlreadyIfNeeded();
                this.mReturnToPreview = false;
                this.mResumeTask = new RegularVineRecorder.OnResumeTask(view, "Set EditMode.");
                this.mResumeTask.execute(new Void[0]);
                this.mEditorAdapter = null;
                return;
            }
            this.mEditing = false;
        }
    }

    public void setEditMode(boolean on, boolean discard) throws IllegalStateException, InterruptedException {
        setEditMode(null, on, discard);
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    protected void setHasPreviewedAlreadyIfNeeded() {
        this.mHasPreviewedAlready = this.mCurrentDurationMs >= ((long) this.mSession.getConfig().maxDuration);
    }

    public void addExtraSegment(RecordSegment segment, Bitmap lastBitmap) {
        this.mCurrentDurationMs += segment.getDurationMs();
        long durationUs = this.mCurrentDurationMs * 1000;
        this.mVideoController.setAudioTimestampUs((int) durationUs);
        this.mVideoController.setVideoTimeStampUs((int) durationUs);
        this.mVideoController.onExternalClipAdded(segment.getDurationMs() * 1000);
        this.mRecordingFileDuration = (int) this.mCurrentDurationMs;
        segment.setCameraSetting(this.mVideoController.getCameraSetting());
        changeProgress(this.mCurrentDurationMs);
        this.mSession.add(segment);
        this.mAddedSegments.add(segment);
        this.mEditedSegments.add(segment);
        this.mCurrentRecordingFile.isDirty = true;
        this.mPluginManager.onOfferLastFrame(lastBitmap, getCurrentCameraSetting());
        this.mPluginManager.onSegmentDataChanged(this.mEditedSegments);
        this.mHandler.post(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.7
            @Override // java.lang.Runnable
            public void run() {
                VineRecorder.this.invalidateFinishButton();
            }
        });
    }

    private boolean commitChanges() {
        boolean lastChanged = false;
        SegmentEditorAdapter adapter = this.mEditorAdapter;
        if (adapter != null) {
            adapter.commitDelete();
            RecordSessionManager.cleanUpSegmentData(this.mCurrentRecordingFile.folder, adapter.cleanUpForDeletion());
            int size = this.mEditedSegments.size();
            RecordSegment lastSegment = null;
            if (size > 0) {
                RecordSegment lastSegment2 = this.mEditedSegments.get(size - 1);
                lastSegment = lastSegment2;
            }
            int modifiedSize = adapter.getData().size();
            if (modifiedSize > 0) {
                if (lastSegment != adapter.getData().get(modifiedSize - 1)) {
                    lastChanged = true;
                }
            } else if (lastSegment != null) {
                lastChanged = true;
            }
            this.mEditedSegments.clear();
            this.mEditedSegments.addAll(adapter.getData());
            this.mEditedSegments.addAll(adapter.getDeleted());
            Iterator<RecordSegment> it = adapter.getDeleted().iterator();
            while (it.hasNext()) {
                RecordSegment segment = it.next();
                segment.removed = true;
            }
            RecordSession session = this.mCurrentRecordingFile.getSession();
            try {
                this.mToRemove.addAll(RecordSegment.applyEditedChanges(this.mCurrentRecordingFile.folder, session, this.mEditedSegments));
                swapTimestampsFromSegments(session, this.mEditedSegments);
                this.mCurrentRecordingFile.isDirty = true;
                SLog.d("Changes commited, last segment has changed? {}.", Boolean.valueOf(lastChanged));
            } catch (IOException e) {
                throw new RecordSegmentIOException(e);
            }
        }
        return lastChanged;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustEditBoundaries() {
        final RelativeLayout.LayoutParams thumbListParams;
        if (this.mVideoTexture != null) {
            this.mVideoViewParams = (RelativeLayout.LayoutParams) this.mVideoTexture.getLayoutParams();
            this.mVideoViewParams.width = this.mSize.x;
            this.mVideoViewParams.height = this.mSize.x;
        }
        if (this.mPlayButtonContainer != null) {
            this.mPlayButtonContainer.setLayoutParams(this.mVideoViewParams);
        }
        if (this.mThumbnailOverlay != null) {
            this.mThumbnailOverlay.setLayoutParams(this.mVideoViewParams);
        }
        if (this.mPreviewLoadingOverlay != null) {
            this.mPreviewLoadingOverlay.setLayoutParams(this.mVideoViewParams);
        }
        if (this.mThumbnailList != null) {
            thumbListParams = (RelativeLayout.LayoutParams) this.mThumbnailList.getLayoutParams();
            thumbListParams.setMargins(0, this.mEditorButtonsHeight + this.mSize.x + this.mThumbnailPadding, 0, 0);
        } else {
            thumbListParams = null;
        }
        if (this.mActivity != null) {
            this.mActivity.runOnUiThread(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.8
                @Override // java.lang.Runnable
                public void run() {
                    View view = VineRecorder.this.mVideoTexture;
                    if (view != null) {
                        view.setLayoutParams(VineRecorder.this.mVideoViewParams);
                    }
                    View view2 = VineRecorder.this.mThumbnailList;
                    if (view2 != null) {
                        view2.setLayoutParams(thumbListParams);
                    }
                }
            });
        }
    }

    public boolean isEditing() {
        return this.mEditing && this.mEditorAdapter != null;
    }

    public void saveSession(Runnable onSaveSessionCompleteRunnable, boolean releaseCamera) {
        this.mCanKeepRecording = false;
        stop("saveSession", onSaveSessionCompleteRunnable, releaseCamera, true);
    }

    @Override // co.vine.android.dragsort.DragSortWidget.SelectionChangedListener
    public void onSelectionChanged(int position, boolean listViewClick) throws IllegalStateException, InterruptedException {
        this.mLastSelectedPosition = position;
        SegmentEditorAdapter editor = this.mEditorAdapter;
        if (editor != null) {
            if (position != -1 && position < editor.getCount()) {
                RecordSegment toPlay = (RecordSegment) editor.getItem(position);
                animateTopButtons(0, toPlay);
                if (listViewClick) {
                    pausePreview(false);
                    playPreview(toPlay, true, true, "SelectionChanged");
                } else {
                    pausePreview(true);
                }
                editor.commitDelete();
                this.mPluginManager.onIndividualSegmentClicked(toPlay, this.mVideoTexture);
                return;
            }
            if (editor.getCount() > 0) {
                animateTopButtons(2, null);
                pausePreview(true);
                if (this.mDragSortWidget != null) {
                    this.mDragSortWidget.setFocused(0);
                }
                this.mPluginManager.onIndividualSegmentClicked(null, this.mVideoTexture);
                showThumbnailOverlay(null);
                return;
            }
            animatePlayButton(false);
            animatePreviewSpinner(false);
            hideThumbnailOverlay();
            animateTopButtons(2, null);
            this.mVideoTexture.setVisibility(8);
            this.mPluginManager.onIndividualSegmentClicked(null, this.mVideoTexture);
        }
    }

    public void showThumbnailOverlay(RecordSegment segment) {
        if (this.mThumbnailOverlay != null && (this.mThumbnailOverlay.getVisibility() != 0 || segment != this.mThumbnailSegment)) {
            this.mThumbnailOverlay.setVisibility(0);
            this.mThumbnailOverlay.setAlpha(1.0f);
            if (segment == null && this.mEditorAdapter.getCount() > 0) {
                segment = (RecordSegment) this.mEditorAdapter.getItem(0);
            }
            if (segment != null) {
                Drawable thumbDrawable = segment.getDrawableCopy(this.mActivity);
                this.mThumbnailOverlay.setImageDrawable(thumbDrawable);
            }
        }
        this.mThumbnailSegment = segment;
    }

    public void hideThumbnailOverlayDelayed() {
        getHandler().postDelayed(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.9
            @Override // java.lang.Runnable
            public void run() {
                VineRecorder.this.hideThumbnailOverlay();
            }
        }, 100L);
    }

    public void hideThumbnailOverlay() {
        if (this.mThumbnailOverlay != null && this.mThumbnailOverlay.getVisibility() != 4) {
            this.mThumbnailOverlay.animate().setDuration(this.mShortAnimTime).alpha(0.0f).setListener(new ViewGoneAnimationListener(this.mThumbnailOverlay)).start();
            this.mThumbnailOverlay.setImageDrawable(null);
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
    public void onPrepared(VideoViewInterface view) throws IllegalStateException {
        if (this.mDragSortWidget != null) {
            if (this.mDragSortWidget.hasFloatView()) {
                pausePreview(false);
            }
            hideThumbnailOverlayDelayed();
            animatePreviewSpinner(false);
        }
    }

    public boolean canPickUpFloatView() {
        return (this.mPreviewLoadingOverlay == null || this.mPreviewLoadingOverlay.getVisibility() == 0) ? false : true;
    }

    public void pausePreview(boolean showPlayButton) throws IllegalStateException {
        animatePlayButton(showPlayButton);
        if (this.mRefreshPreviewTask != null) {
            this.mRefreshPreviewTask.cancel(true);
            this.mRefreshPreviewTask = null;
        }
        if (this.mVideoTexture != null) {
            this.mVideoTexture.pause();
            this.mVideoTexture.setAutoPlayOnPrepared(false);
        }
    }

    public void playPreview(RecordSegment toPlay, boolean forceRefresh, boolean editMode, String tag) throws IllegalStateException, InterruptedException {
        animatePlayButton(false);
        refreshVideoView(toPlay, forceRefresh, editMode, toPlay != null, tag);
    }

    public boolean isEditingDirty() {
        return isEditing() && !this.mEditorAdapter.compareTo(this.mEditedSegments);
    }

    public void animateTopButtons(int animation, RecordSegment segment) {
        switch (animation) {
            case 0:
                this.mPluginManager.onIndividualSegmentClicked(segment, this.mVideoTexture);
                break;
            case 2:
                this.mPluginManager.onIndividualSegmentClicked(null, this.mVideoTexture);
                break;
        }
    }

    public void animatePlayButton(boolean visible) {
        if (this.mPlayButton != null && this.mPlayRefreshButton != null && this.mPlayButtonContainer != null) {
            this.mPlayButtonContainer.animate().cancel();
            if (!visible) {
                this.mPlayButtonContainer.animate().alpha(0.0f).setDuration(this.mShortAnimTime).setListener(new ViewGoneAnimationListener(this.mPlayButtonContainer)).start();
                return;
            }
            if (this.mDragSortWidget.getSelection() != -1) {
                this.mPlayButton.setVisibility(8);
                this.mPlayRefreshButton.setVisibility(0);
            } else {
                this.mPlayButton.setVisibility(0);
                this.mPlayRefreshButton.setVisibility(8);
            }
            this.mPlayButtonContainer.setVisibility(0);
            this.mPlayButtonContainer.animate().alpha(1.0f).setDuration(this.mShortAnimTime).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animatePreviewSpinner(boolean visible) {
        animateSpinner(this.mPreviewLoadingOverlay, visible);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateSpinner(View view, boolean visible) {
        if (view != null) {
            view.animate().cancel();
            if (!visible) {
                view.animate().alpha(0.0f).setDuration(this.mShortAnimTime).setListener(new ViewGoneAnimationListener(view)).start();
                return;
            }
            view.setVisibility(0);
            view.setAlpha(0.0f);
            view.animate().alpha(1.0f).setDuration(this.mShortAnimTime).start();
        }
    }

    public void reverseFrames() {
        if (isEditing()) {
            this.mEditorAdapter.reverse();
        }
    }

    public void doOneFrame() {
        if (this.mCurrentRecordingFile != null && !isEditing() && this.mAutoFocusing && startRelativeTime()) {
            this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.10
                @Override // java.lang.Runnable
                public void run() {
                    VineRecorder.this.endRelativeTime();
                }
            }, 25L);
        }
    }

    @Override // co.vine.android.recorder.RegularVineRecorder
    protected void onResumeTaskComplete() {
        ensureLowProfileStatusBar();
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void onResume(String tag) throws IllegalStateException, InterruptedException {
        boolean z = true;
        boolean canKeepRecording = canKeepRecording();
        Object[] objArr = new Object[4];
        objArr[0] = Boolean.valueOf(this.mDelayDialog);
        objArr[1] = Boolean.valueOf(canKeepRecording);
        objArr[2] = Boolean.valueOf(isEditing());
        objArr[3] = Boolean.valueOf(this.finalFile != null);
        CrashUtil.log("Resume VineRecorder: delayDialog: {} canKeepRecording: {} isEditing: {} NullFinalFile: {}.", objArr);
        StaticSizeVideoView videoTexture = new StaticSizeVideoView(this.mActivity);
        videoTexture.setSize(this.mSize.x, this.mSize.x);
        this.mVideoViewContainer.addView(videoTexture, 0);
        this.mVideoTexture = videoTexture;
        if (canKeepRecording) {
            if (!isEditing()) {
                initPreviewSurface();
                if (this.mStartWithEditMode) {
                    setEditMode(true, false);
                    return;
                }
                this.mResumeTask = getOnResumeTask(null, "OnResume_" + tag);
                BasicVineRecorder.OnResumeTask onResumeTask = this.mResumeTask;
                if (!this.mDelayDialog && (!this.mCurrentRecordingFile.isSavedSession || !this.mNeverResumedRecorder)) {
                    z = false;
                }
                onResumeTask.showDialogDelayed = z;
                this.mResumeTask.execute(new Void[0]);
                return;
            }
            setEditMode(true, false);
            return;
        }
        if (this.finalFile != null) {
            this.mOnCompleteConsumer.run();
            return;
        }
        if (this.mStartProgressDialog != null) {
            this.mStartProgressDialog.setMessage(this.mFinishLastSegmentString);
            if (!this.mDelayDialog) {
                try {
                    this.mStartProgressDialog.show();
                } catch (WindowManager.BadTokenException e) {
                }
            }
        }
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void setDiscardChanges(boolean discardChanges) {
        this.mDiscardChanges = discardChanges;
    }

    public void refreshVideoView(RecordSegment segment, boolean forceRefresh, boolean editMode, boolean ignoreTrim, String tag) throws IllegalStateException, InterruptedException {
        releaseSegmentChangeDetector();
        if (editMode) {
            this.mSegmentChangeDetector = new SegmentChangeDetector();
            this.mSegmentChangeThread = new Thread(this.mSegmentChangeDetector);
            this.mSegmentChangeThread.start();
        }
        if (this.mPlayButtonOnClickListener != null) {
            this.mPlayButtonOnClickListener.editMode = editMode;
        }
        if (editMode && this.mEditorAdapter.getCount() == 0) {
            hideThumbnailOverlay();
            if (this.mPlayButton != null) {
                this.mPlayButton.setVisibility(8);
            }
            if (this.mVideoTexture != null) {
                this.mVideoTexture.setVisibility(8);
                animatePreviewSpinner(false);
                return;
            }
            return;
        }
        if (this.mLastPlayingSegment != segment || forceRefresh) {
            if (!editMode || this.mEditorAdapter.getCount() > 0) {
                showThumbnailOverlay(segment);
            }
            if (!editMode) {
                if (this.mCameraView != null) {
                    this.mCameraView.setVisibility(4);
                }
                if (this.mVideoController != null) {
                    this.mVideoController.releaseCameraAndPreview(MessageFormatter.toStringMessage("refreshVideo view {}", tag));
                } else {
                    CrashUtil.log("WARNING: it looks like the controller is already released, but we may still have camera and preview in use.");
                }
            }
            if (segment == null && editMode && this.mDragSortWidget != null) {
                this.mDragSortWidget.setFocused(0);
            }
            if (this.mRefreshPreviewTask != null) {
                this.mRefreshPreviewTask.cancel(true);
            }
            if (segment != null && segment.removed) {
                SLog.i("Do not start refresh task because segment is removed.");
                return;
            } else {
                if (this.mVideoTexture == null) {
                    SLog.i("Do not start refresh task because onPause probably happened.");
                    return;
                }
                animatePreviewSpinner(true);
                this.mRefreshPreviewTask = new RefreshPreviewTask(segment, editMode, ignoreTrim);
                this.mRefreshPreviewTask.execute(new Void[0]);
                return;
            }
        }
        if (this.mVideoTexture != null) {
            this.mVideoTexture.setAutoPlayOnPrepared(true);
            if (this.mVideoTexture.isPaused()) {
                this.mVideoTexture.resume();
            }
        }
        hideThumbnailOverlayDelayed();
        animatePreviewSpinner(false);
    }

    public boolean isFlashOn() {
        return this.mVideoController.isFlashOn();
    }

    public boolean isWithinDurationPercentageOf(double percentage) {
        return (this.mSession == null || this.mSession.getConfig() == null || ((double) this.mCurrentDurationMs) >= ((double) this.mSession.getConfig().maxDuration) * percentage) ? false : true;
    }

    public boolean setAudioTrim(boolean trim) {
        if (this.mVideoController == null) {
            return false;
        }
        this.mVideoController.setAudioTrim(trim);
        return true;
    }

    public SegmentEditorAdapter getEditorAdatper() {
        return this.mEditorAdapter;
    }

    public void changePlayButtonOnClickBehavior(boolean forceRefresh, boolean editMode) {
        this.mPlayButtonOnClickListener.forceRefresh = forceRefresh;
        this.mPlayButtonOnClickListener.editMode = editMode;
    }

    public void saveSegmentIfNeeded(RecordSegment segment) throws IOException {
        segment.saveDataToDiskIfNeeded(this.mCurrentRecordingFile.folder, this.mSession.getVideoData(), this.mSession.getAudioData(), segment);
    }

    private class RefreshPreviewTask extends AsyncTask<Void, Void, Void> {
        private final boolean mEditMode;
        private final boolean mIgnoreTrim;
        private final RecordSegment mSegment;

        public RefreshPreviewTask(RecordSegment segment, boolean editMode, boolean ignoreTrim) {
            this.mSegment = segment;
            this.mEditMode = editMode;
            this.mIgnoreTrim = ignoreTrim;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) throws InterruptedException {
            int percentage;
            if (!isCancelled()) {
                if (this.mSegment != VineRecorder.this.mLastPlayingSegment) {
                    if (this.mSegment == null) {
                        VineRecorder.this.refreshFullPreview();
                        return null;
                    }
                    long start = System.currentTimeMillis();
                    if (!this.mEditMode) {
                        while (VineRecorder.this.mVideoController.wasJustGrabbingDataForSegment()) {
                            SLog.i("Waiting for audio recorder to settle down.");
                            try {
                                Thread.sleep(RecordConfigUtils.AUDIO_WAIT_THRESHOLD_NS / 2);
                            } catch (InterruptedException e) {
                            }
                        }
                        if (this.mSegment.getVideoData().size() == 0) {
                            SLog.i("Waiting for camera frame to come in one more time.");
                            try {
                                Thread.sleep(33L);
                            } catch (InterruptedException e2) {
                            }
                        }
                        do {
                            percentage = this.mSegment.getVideoDataEncodedPercentage();
                            SLog.i("Segment video encoded percentage: {}.", Integer.valueOf(percentage));
                            if (percentage < 100) {
                                try {
                                    Thread.sleep(200L);
                                } catch (InterruptedException e3) {
                                    throw new RuntimeException(e3);
                                }
                            }
                        } while (percentage < 100);
                        if (this.mSegment.getVideoData().size() == 0) {
                            CrashUtil.log("Something wrong is happening, 0 video frames by the end of settling phase.");
                        }
                    }
                    if (!isCancelled()) {
                        VineRecorder.this.mVideoController.makePreview(this.mSegment, false, this.mIgnoreTrim);
                    }
                    SLog.i("Preview waited for {}ms.", Long.valueOf(System.currentTimeMillis() - start));
                    return null;
                }
                if (this.mSegment == null && VineRecorder.this.mEditorAdapter.hasInitialized) {
                    VineRecorder.this.refreshFullPreview();
                    return null;
                }
                return null;
            }
            return null;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            VineRecorder.this.adjustEditBoundaries();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void aVoid) throws IllegalStateException {
            String videoPath;
            SegmentEditorAdapter adapter = VineRecorder.this.mEditorAdapter;
            boolean segmentAdapterIsValid = adapter != null && adapter.getCount() > 0;
            SdkVideoView videoSurface = VineRecorder.this.mVideoTexture;
            if (videoSurface != null) {
                if (!isCancelled() && (!this.mEditMode || segmentAdapterIsValid)) {
                    ViewGroup group = (ViewGroup) videoSurface.getParent();
                    videoSurface.setVisibility(0);
                    if (group != null) {
                        group.setOnClickListener(VineRecorder.this.new PreviewVideoClickListener(this.mEditMode));
                    }
                    if (VineRecorder.this.mDragSortWidget != null) {
                        VineRecorder.this.mDragSortWidget.setContentView(group, new Rect(0, VineRecorder.this.mEditorButtonsHeight, VineRecorder.this.mSize.x, VineRecorder.this.mEditorButtonsHeight + VineRecorder.this.mSize.x));
                    }
                    VineRecorder.this.adjustEditBoundaries();
                    VineRecorder.this.mLastPlayingSegment = this.mSegment;
                    if (this.mSegment == null) {
                        videoPath = VineRecorder.this.mCurrentRecordingFile != null ? VineRecorder.this.mCurrentRecordingFile.getPreviewVideoPath() : null;
                    } else {
                        videoPath = this.mSegment.videoPath;
                    }
                    if (this.mSegment == null || !this.mSegment.isTrimmed()) {
                        VineRecorder.this.preparePlayer(videoPath, -1L, -1L);
                    } else {
                        VineRecorder.this.preparePlayer(videoPath, this.mSegment.getTrimmedVideoStartUs(), this.mSegment.getTrimmedAudioEndUs());
                    }
                    VineRecorder.this.mVideoTexture.setMute(this.mSegment != null && this.mSegment.isSilenced());
                } else {
                    videoSurface.setVisibility(8);
                }
            }
            VineRecorder.this.mRefreshPreviewTask = null;
        }

        @Override // android.os.AsyncTask
        public void onCancelled() {
            VineRecorder.this.animatePreviewSpinner(false);
        }
    }

    public void setVideoTextureSeekPoints(long startUs, long endUs) {
        this.mVideoTextureSeekStartUs = startUs;
        this.mVideoTextureSeekEndUs = endUs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void preparePlayer(String path, long startStartUs, long startEndUs) throws IllegalStateException {
        setVideoTextureSeekPoints(startStartUs, startEndUs);
        final SdkVideoView videoTexture = this.mVideoTexture;
        videoTexture.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.recorder.VineRecorder.13
            @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
            public void onPrepared(VideoViewInterface view) throws IllegalStateException {
                VineRecorder.this.mVideoTexture.setVisibility(0);
                if (VineRecorder.this.mVideoTextureSeekStartUs > 0) {
                    videoTexture.seekTo((int) (VineRecorder.this.mVideoTextureSeekStartUs / 1000));
                }
                VineRecorder.this.animatePreviewSpinner(false);
                VineRecorder.this.hideThumbnailOverlayDelayed();
            }
        });
        videoTexture.setOnCompletionListener(new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.recorder.VineRecorder.14
            @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
            public void onCompletion(VideoViewInterface view) {
                SLog.d("mjama  playback ended!");
                videoTexture.postDelayed(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.14.1
                    @Override // java.lang.Runnable
                    public void run() throws IllegalStateException {
                        int i;
                        SdkVideoView sdkVideoView = videoTexture;
                        if (VineRecorder.this.mVideoTextureSeekStartUs > 0) {
                            i = (int) (VineRecorder.this.mVideoTextureSeekStartUs / 1000);
                        } else {
                            i = 0;
                        }
                        sdkVideoView.seekTo(i);
                        videoTexture.setAutoPlayOnPrepared(true);
                        videoTexture.start();
                    }
                }, 20L);
            }
        });
        if (path != null) {
            videoTexture.setVideoPath(path);
            videoTexture.setAutoPlayOnPrepared(true);
            videoTexture.start();
        }
    }

    public class PreviewVideoClickListener implements View.OnClickListener {
        private final boolean mEditMode;

        public PreviewVideoClickListener(boolean editMode) {
            this.mEditMode = editMode;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws IllegalStateException {
            if (VineRecorder.this.mVideoTexture != null && VineRecorder.this.mVideoTexture.isPlaying()) {
                VineRecorder.this.mPlayButtonOnClickListener.forceRefresh = false;
                VineRecorder.this.mPlayButtonOnClickListener.editMode = this.mEditMode;
                VineRecorder.this.pausePreview(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshFullPreview() throws InterruptedException {
        SLog.i("Refresh full preview.");
        RecordingFile file = getFile();
        RecordSession session = null;
        if (file != null) {
            session = file.getSession();
        }
        if (session != null) {
            ArrayList<RecordSegment> data = this.mEditorAdapter.getData();
            byte[] videoData = new byte[session.getVideoData().length];
            AudioArray audioData = AudioArrays.newInstance(session.getAudioData().getLength(), session.getVersion().getAudioArrayType());
            try {
                this.mVideoController.writeToFile(RecordSegment.applyForEditedChanges(file.folder, session, videoData, audioData, data), videoData, audioData);
                long lastTime = 0;
                for (int i = 0; i < data.size(); i++) {
                    RecordSegment segment = data.get(i);
                    segment.index = i;
                    segment.startTimestamp = lastTime;
                    lastTime += segment.getAudioDurationMs();
                    SLog.i("Timestamp modified to: {}.", Long.valueOf(segment.startTimestamp));
                }
            } catch (IOException e) {
                throw new RecordSegmentIOException(e);
            }
        }
    }

    @Override // co.vine.android.recorder.RegularVineRecorder, co.vine.android.recorder.BasicVineRecorder
    public BaseFinishProcessTask getFinishProcessTask(String tag, Runnable onComplete, boolean releasePreview, boolean saveSession) {
        return this.mFastEncoding ? new RegularVineRecorder.FinishProcessTask(tag, onComplete, releasePreview, saveSession) { // from class: co.vine.android.recorder.VineRecorder.15
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // co.vine.android.recorder.RegularVineRecorder.FinishProcessTask, co.vine.android.recorder.BaseFinishProcessTask, android.os.AsyncTask
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                VineRecorder.this.animateSpinner(VineRecorder.this.mFinishLoadingOverlay, false);
            }

            @Override // co.vine.android.recorder.BaseFinishProcessTask, android.os.AsyncTask
            protected void onPreExecute() {
                super.onPreExecute();
                VineRecorder.this.animateSpinner(VineRecorder.this.mFinishLoadingOverlay, true);
            }
        } : super.getFinishProcessTask(tag, onComplete, releasePreview, saveSession);
    }

    public void writePreviewToFile() {
        try {
            int frameRate = RecordSegment.getFrameRate(this.mEditedSegments);
            SwVineFrameRecorder videoRecorder = RecordConfigUtils.newVideoRecorder(this.mCurrentRecordingFile.getVideoPath() + ".video" + RecordConfigUtils.VIDEO_CONTAINER_EXT, frameRate, 480, this.mUseMp4);
            videoRecorder.start();
            RecordSession recordSession = getFile().getSession();
            SwCombiningRunnable combiningRunnable = SwCombiningRunnable.newInstance(getFile(), false, recordSession.getAudioData(), recordSession.getVideoData(), this.mEditedSegments, videoRecorder, this.mVideoController.getFinishProcessTask());
            combiningRunnable.combineVideos();
        } catch (SwVineFrameRecorder.Exception e) {
            CrashUtil.logException(e, "Cannot start audio/video recorder. ", new Object[0]);
        }
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    protected void invalidateFinishButton() {
        if (this.mCurrentDurationMs < this.mThresholdMs) {
            View view = this.mFinishButton;
            if (view != null) {
                view.setVisibility(8);
                return;
            }
            return;
        }
        onProgressThresholdReached();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0010  */
    @Override // co.vine.android.recorder.RegularVineRecorder, android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized boolean onTouch(android.view.View r2, android.view.MotionEvent r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.isEditing()     // Catch: java.lang.Throwable -> L12
            if (r0 != 0) goto L10
            boolean r0 = super.onTouch(r2, r3)     // Catch: java.lang.Throwable -> L12
            if (r0 == 0) goto L10
            r0 = 1
        Le:
            monitor-exit(r1)
            return r0
        L10:
            r0 = 0
            goto Le
        L12:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.VineRecorder.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    @Override // android.view.View.OnKeyListener
    public synchronized boolean onKey(View v, int keyCode, KeyEvent event) {
        boolean z;
        switch (keyCode) {
            case 27:
                switch (event.getAction()) {
                    case 0:
                        startRelativeTime();
                        break;
                    case 1:
                        endRelativeTime();
                        break;
                }
                z = true;
                break;
            default:
                z = false;
                break;
        }
        return z;
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public BasicVineRecorder.ResumeCameraAsyncTask getSwitchCameraTask() {
        return new RegularVineRecorder.ResumeCameraAsyncTask(true);
    }

    public void onEditorDone(View button) {
        if (this.mEditorDoneAsyncTask == null) {
            this.mEditorDoneAsyncTask = new EditorDoneAsyncTask(button);
            this.mEditorDoneAsyncTask.execute(new Void[0]);
        }
    }

    public void onEditorCancel(View button) throws IllegalStateException, InterruptedException {
        if (this.mReturnToPreview) {
            returnToPreview(true);
        } else {
            setEditMode(button, !this.mEditing, true);
        }
    }

    public class EditorDoneAsyncTask extends AsyncTask<Void, Void, Void> {
        private final View mEditorDoneButton;
        private final boolean mReturnToPreviewNow;
        private boolean mWasIndeterminate;

        public EditorDoneAsyncTask(View editorDoneButton) {
            this.mReturnToPreviewNow = VineRecorder.this.mReturnToPreview && VineRecorder.this.getDurationFromSegments() >= VineRecorder.this.mThresholdMs;
            this.mEditorDoneButton = editorDoneButton;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() throws IllegalStateException, InterruptedException {
            if (VineRecorder.this.mCurrentRecordingFile != null) {
                if (this.mReturnToPreviewNow) {
                    if (VineRecorder.this.mStartProgressDialog != null) {
                        VineRecorder.this.mStartProgressDialog.setMessage(VineRecorder.this.mFinishProgressDialogMessage[VineRecorder.this.mFinishProgressDialogMessage.length - 1]);
                        this.mWasIndeterminate = VineRecorder.this.mStartProgressDialog.isIndeterminate();
                        if (!this.mWasIndeterminate) {
                            VineRecorder.this.mStartProgressDialog.setIndeterminate(true);
                        }
                        VineRecorder.this.mStartProgressDialog.show();
                    }
                    if (VineRecorder.this.mRefreshPreviewTask != null) {
                        VineRecorder.this.mRefreshPreviewTask.cancel(false);
                        return;
                    }
                    return;
                }
                if (!isCancelled()) {
                    VineRecorder.this.setEditMode(this.mEditorDoneButton, VineRecorder.this.mEditing ? false : true, false);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) throws InterruptedException {
            if (this.mReturnToPreviewNow) {
                VineRecorder.this.returnToPreview(false);
                return null;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            if (VineRecorder.this.mCurrentRecordingFile != null) {
                if (this.mReturnToPreviewNow) {
                    VineRecorder.this.animateEditModeControlsOut(true);
                    VineRecorder.this.mPluginManager.onSetEditMode(false, VineRecorder.this.mCurrentRecordingFile != null && VineRecorder.this.mCurrentRecordingFile.hasData());
                    VineRecorder.this.mHandler.removeCallbacks(VineRecorder.this.mVideoPreviewTrimPositionChecker);
                }
                if (VineRecorder.this.mStartProgressDialog != null) {
                    VineRecorder.this.mStartProgressDialog.dismiss();
                    if (!this.mWasIndeterminate) {
                        VineRecorder.this.mStartProgressDialog.setIndeterminate(false);
                    }
                }
            }
            VineRecorder.this.mEditorDoneAsyncTask = null;
        }
    }

    @Override // co.vine.android.recorder.RegularVineRecorder, co.vine.android.recorder.BasicVineRecorder
    @SuppressLint({"ShowToast"})
    public void onUiResumed(Activity activity, Runnable onCompleteAsyncTask, boolean doNotResumeRecorder) {
        super.onUiResumed(activity, onCompleteAsyncTask, doNotResumeRecorder);
        this.mRecordingOptions = activity.findViewById(this.mRecordingOptionsRowId);
        this.mThumbnailList = activity.findViewById(R.id.list);
        this.mThumbnailOverlay = (ImageView) activity.findViewById(this.mThumbnailOverlayId);
        this.mFinishLoadingOverlay = activity.findViewById(this.mFinishLoadingOverlayId);
        this.mPlayButtonContainer = activity.findViewById(this.mPlayButtonContainerId);
        this.mPlayButtonOnClickListener = new PlayButtonOnClickListener();
        this.mPlayButtonContainer.setOnClickListener(this.mPlayButtonOnClickListener);
        this.mPlayButtonContainer.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.recorder.VineRecorder.16
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent event) {
                if (VineRecorder.this.mDragSortWidget != null) {
                    return VineRecorder.this.mDragSortWidget.onTouch((View) VineRecorder.this.mVideoTexture.getParent(), event, VineRecorder.this.mPlayButtonContainer.getLeft(), VineRecorder.this.mPlayButtonContainer.getTop());
                }
                return false;
            }
        });
        this.mFinishButton = activity.findViewById(this.mFinishButtonId);
        this.mFinishButton.setOnClickListener(this.mFinishClicker);
        this.mPlayButton = ((ViewGroup) this.mPlayButtonContainer).getChildAt(0);
        this.mPlayRefreshButton = ((ViewGroup) this.mPlayButtonContainer).getChildAt(1);
        this.mDragSortWidget = (DragSortWidget) activity.findViewById(this.mDragSortWidgetId);
        this.mDragSortWidget.setSelectionChangedListener(this);
        this.mPreviewLoadingOverlay = activity.findViewById(this.mPreviewLoadingOverlayId);
        if (!isEditing() && this.mCurrentDurationMs >= this.mThresholdMs) {
            onProgressThresholdReached();
        }
        this.mProgressView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recorder.VineRecorder.17
            @Override // android.view.View.OnClickListener
            public void onClick(View v) throws IllegalStateException, InterruptedException {
                if (!VineRecorder.this.mIsMessaging) {
                    if (VineRecorder.this.mPluginManager.canEdit()) {
                        VineRecorder.this.setEditMode(VineRecorder.this.mProgressView, !VineRecorder.this.mEditing, false);
                    } else {
                        SLog.i("Plugin has a lock on edit right now.");
                    }
                }
            }
        });
        this.mIsSwitchingCamera = false;
        this.mProgressOverlay = activity.findViewById(this.mProgressOverlayId);
        ((OnMeasureNotifyingRelativeLayout) this.mRootLayoutView).setOnMeasureListener(new OnMeasureNotifyingRelativeLayout.OnMeasureListener() { // from class: co.vine.android.recorder.VineRecorder.18
            @Override // co.vine.android.views.OnMeasureNotifyingRelativeLayout.OnMeasureListener
            public void onMeasure() {
                VineRecorder.this.adjustStaticBoundaries();
            }
        });
        if (!doNotResumeRecorder) {
            onResume("UI Resume");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void returnToPreview(boolean discard) throws InterruptedException {
        if (this.mVideoController != null && this.mCurrentRecordingFile != null) {
            releaseSegmentChangeDetector();
            if (!discard) {
                commitChanges();
            }
            this.mCanKeepRecording = false;
            writePreviewToFile();
            finishRecording();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDurationFromSegments() {
        int duration = 0;
        Iterator<RecordSegment> it = this.mEditorAdapter.getData().iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            if (!segment.removed) {
                duration += segment.getDurationMs();
            }
        }
        return duration;
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void onProgressThresholdReached() {
        View view = this.mFinishButton;
        if (view != null && view.getVisibility() != 0) {
            view.setVisibility(0);
        }
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    @TargetApi(16)
    public void onPause() throws IllegalStateException {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 16 && this.mMediaActionSound != null) {
            this.mMediaActionSound.release();
        }
        EditorDoneAsyncTask task = this.mEditorDoneAsyncTask;
        if (task != null) {
            task.cancel(false);
        }
        this.mVideoViewContainer.removeView(this.mVideoTexture);
        if (this.mVideoTexture != null) {
            this.mVideoTexture.suspend();
            this.mVideoTexture = null;
        }
    }

    public class PlayButtonOnClickListener implements View.OnClickListener {
        boolean forceRefresh = false;
        boolean editMode = false;

        public PlayButtonOnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) throws IllegalStateException, InterruptedException {
            if (VineRecorder.this.mEditorAdapter != null) {
                int selection = VineRecorder.this.mDragSortWidget.getSelection();
                RecordSegment toPlay = null;
                if (selection != -1) {
                    toPlay = (RecordSegment) VineRecorder.this.mEditorAdapter.getItem(VineRecorder.this.mDragSortWidget.getSelection());
                }
                if (toPlay == null) {
                    VineRecorder.this.mLastSelectedPosition = -1;
                    if (VineRecorder.this.mDragSortWidget != null) {
                        VineRecorder.this.mDragSortWidget.setSelection(-1);
                    }
                    VineRecorder.this.animateTopButtons(2, null);
                }
                VineRecorder.this.playPreview(toPlay, this.forceRefresh, this.editMode, "playButton");
            }
        }
    }

    @Override // co.vine.android.recorder.RegularVineRecorder, co.vine.android.recorder.BasicVineRecorder
    public void onUiPaused() {
        releaseSegmentChangeDetector();
        if (this.mDragSortWidget != null) {
            this.mDragSortWidget.releaseReferences();
        }
        try {
            if (this.mRefreshPreviewTask != null) {
                this.mRefreshPreviewTask.cancel(false);
            }
        } catch (Exception e) {
            SLog.e("It's probably detached already.", (Throwable) e);
        }
        if (this.mVideoTexture != null) {
            this.mVideoTexture.setOnErrorListener(null);
            this.mVideoTexture.setOnPreparedListener(null);
        }
        this.mRefreshPreviewTask = null;
        this.mRecordingOptions = null;
        this.mPreviewLoadingOverlay = null;
        this.mFinishLoadingOverlay = null;
        this.mThumbnailList = null;
        if (this.mPlayButtonContainer != null) {
            this.mPlayButtonContainer.setOnTouchListener(null);
            this.mPlayButtonContainer.setOnClickListener(null);
        }
        this.mPlayButtonContainer = null;
        this.mThumbnailSegment = null;
        this.mThumbnailOverlay = null;
        if (this.mDragSortWidget != null) {
            this.mDragSortWidget.setSelectionChangedListener(null);
        }
        this.mDragSortWidget = null;
        if (this.mFinishButton != null) {
            this.mFinishButton.setOnClickListener(null);
        }
        this.mFinishButton = null;
        super.onUiPaused();
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    protected void adjustBoundaries(CameraSetting cs) {
        Activity activity = this.mActivity;
        if (activity != null && cs != null) {
            float previewWidth = cs.originalW;
            if (cs.frontFacing && cs.frontFacingAspectRatio > 0.0f) {
                previewWidth = cs.originalH * cs.frontFacingAspectRatio;
            } else if (cs.backFacingAspectRatio > 0.0f) {
                previewWidth = cs.originalH * cs.backFacingAspectRatio;
            }
            if (cs.frontFacingAspectRatio > 0.0f || cs.backFacingAspectRatio > 0.0f) {
                CrashUtil.logException(new VineLoggingException("Adjusting ratio originalW = " + cs.originalW + " originalH = " + cs.originalH + " hardware Hardware Version: " + Build.DEVICE + " - " + Build.MODEL + " (" + Build.CPU_ABI + "." + Build.HARDWARE + "." + Build.BRAND + "." + Build.PRODUCT + "." + Build.MANUFACTURER + "." + Build.CPU_ABI2 + ")"));
            }
            if (this.mFinishLoadingOverlay != null && this.mVideoTexture != null) {
                final ViewGroup.LayoutParams params = this.mVideoTexture.getLayoutParams();
                params.width = this.mSize.x;
                params.height = this.mSize.x;
                this.mFinishLoadingOverlay.post(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.19
                    @Override // java.lang.Runnable
                    public void run() {
                        if (VineRecorder.this.mFinishLoadingOverlay != null) {
                            VineRecorder.this.mFinishLoadingOverlay.setLayoutParams(params);
                        }
                    }
                });
            }
            float previewHeight = cs.originalH;
            float aspectRatio = previewWidth / previewHeight;
            int surfaceViewHeightPx = (int) (this.mSize.x * aspectRatio);
            if (this.mTopMaskView != null && this.mBottomMaskView != null) {
                final int topMaskHeightPx = this.mTopMaskView.getLayoutParams().height;
                int surfaceViewMidpoint = surfaceViewHeightPx / 2;
                int midpointYOffset = (this.mSize.x / 2) + topMaskHeightPx;
                int surfaceViewYPos = midpointYOffset - surfaceViewMidpoint;
                final RelativeLayout.LayoutParams cameraViewParams = (RelativeLayout.LayoutParams) this.mCameraView.getLayoutParams();
                cameraViewParams.width = this.mSize.x;
                cameraViewParams.height = surfaceViewHeightPx;
                cameraViewParams.setMargins(0, surfaceViewYPos, 0, 0);
                activity.runOnUiThread(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.20
                    @Override // java.lang.Runnable
                    public void run() {
                        View view = VineRecorder.this.mCameraView;
                        if (view != null) {
                            SLog.d("Setting preview sizes to {} {}.", Integer.valueOf(cameraViewParams.width), Integer.valueOf(cameraViewParams.height));
                            view.setLayoutParams(cameraViewParams);
                        }
                        VineRecorder.this.mPluginManager.onAdjustingLayoutParamsComplete(topMaskHeightPx, VineRecorder.this.mSize.x);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustStaticBoundaries() {
        Activity activity = this.mActivity;
        if (activity != null) {
            int topMaskHeightPx = this.mTopMaskView.getLayoutParams().height;
            int rootHeight = this.mRootLayoutView.getMeasuredHeight();
            int bottomMaskHeightPx = (rootHeight - topMaskHeightPx) - this.mSize.x;
            final RelativeLayout.LayoutParams bottomMaskParams = (RelativeLayout.LayoutParams) this.mBottomMaskView.getLayoutParams();
            bottomMaskParams.height = bottomMaskHeightPx;
            activity.runOnUiThread(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.21
                @Override // java.lang.Runnable
                public void run() {
                    View view = VineRecorder.this.mBottomMaskView;
                    if (view != null) {
                        view.setLayoutParams(bottomMaskParams);
                    }
                }
            });
        }
    }

    private void releaseSegmentChangeDetector() throws InterruptedException {
        if (this.mSegmentChangeThread != null) {
            this.mSegmentChangeDetector.runThread = false;
            try {
                this.mSegmentChangeThread.join();
            } catch (InterruptedException e) {
            }
            this.mSegmentChangeThread = null;
        }
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void onProgressMaxReached() {
        if (!this.mHasPreviewedAlready) {
            finish("progressMaxReached");
        }
    }

    public View getProgressView() {
        return this.mProgressView;
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void onCameraReady(RecordController controller) {
        if (this.mActivity != null) {
            this.mHandler.post(this.onCameraReadyRunnable);
            super.onCameraReady(controller);
        }
    }

    @Override // co.vine.android.recorder.RegularVineRecorder, co.vine.android.recorder.BasicVineRecorder
    public void changeProgress(long progress) {
        super.changeProgress(progress);
        if (this.mProgressOverlay != null) {
            if (SystemUtil.isOnMainThread()) {
                this.mChangeProgressOverlayVisibilityRunnable.run();
            } else {
                this.mHandler.post(this.mChangeProgressOverlayVisibilityRunnable);
            }
        }
    }

    public class SegmentChangeDetector implements Runnable {
        public int lastFirstItem = 0;
        public boolean runThread = true;

        public SegmentChangeDetector() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            int position;
            ArrayList<RecordSegment> segments;
            int size;
            while (this.runThread) {
                try {
                    if (VineRecorder.this.mLastSelectedPosition == -1 && VineRecorder.this.mEditorAdapter != null && VineRecorder.this.mVideoTexture != null && VineRecorder.this.mVideoTexture.isPlaying() && (position = VineRecorder.this.mVideoTexture.getCurrentPosition() + 20) > 0 && (size = (segments = VineRecorder.this.mEditorAdapter.getData()).size()) > 0) {
                        int i = size - 1;
                        while (true) {
                            if (i < 0) {
                                break;
                            }
                            if (position < segments.get(i).startTimestamp) {
                                i--;
                            } else if (this.lastFirstItem != i) {
                                this.lastFirstItem = i;
                                Activity activity = VineRecorder.this.mActivity;
                                if (activity != null) {
                                    activity.runOnUiThread(new Runnable() { // from class: co.vine.android.recorder.VineRecorder.SegmentChangeDetector.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            DragSortWidget widget;
                                            if (VineRecorder.this.mLastSelectedPosition == -1 && (widget = VineRecorder.this.mDragSortWidget) != null) {
                                                widget.setFocused(SegmentChangeDetector.this.lastFirstItem);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                    Thread.sleep(40L);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    @TargetApi(16)
    public void playStopRecordingSound() {
        try {
            if (Build.VERSION.SDK_INT >= 16 && this.mMediaActionSound != null && this.mPlaySound) {
                this.mMediaActionSound.play(3);
            }
        } catch (Exception e) {
        }
    }

    @TargetApi(16)
    public void playStartRecordingSound() {
        try {
            if (Build.VERSION.SDK_INT >= 16 && this.mMediaActionSound != null && this.mPlaySound) {
                this.mMediaActionSound.play(2);
            }
        } catch (Exception e) {
        }
    }
}
