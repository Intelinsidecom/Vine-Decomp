package co.vine.android.recordingui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import co.vine.android.R;
import co.vine.android.RecordingNavigationController;
import co.vine.android.dragsort.DragSortWidget;
import co.vine.android.player.SdkVideoView;
import co.vine.android.recorder.RegularProgressView;
import co.vine.android.recorder2.InvalidateGhostListener;
import co.vine.android.recorder2.RecordController;
import co.vine.android.recorder2.RecordControllerImpl;
import co.vine.android.recorder2.SegmentEditorAdapter;
import co.vine.android.recordingui.TrimmedVideoPlaybackRecordingMode;
import co.vine.android.views.HorizontalListView;
import co.vine.android.views.RangeSeekBar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class EditMode extends TrimmedVideoPlaybackRecordingMode implements DragSortWidget.RemoveListener, DragSortWidget.SelectionChangedListener, RecordControllerImpl.RecordingEventListener, RangeSeekBar.OnRangeSeekBarChangeListener {
    private static final ColorDrawable PRESSED_STATE_COLOR = new ColorDrawable(-13092808);
    private Activity mActivity;
    private View mAllDeletedOverlay;
    private View mBackButton;
    private View mButtonsDeselected;
    private View mButtonsSelected;
    private ArrayList<Long> mCurrentKeyFrames;
    private ImageView mDeleteClipButton;
    private DragSortWidget mDragSortWidget;
    private ImageView mDuplicateClipButton;
    private SegmentEditorAdapter mEditorAdapter;
    private InvalidateGhostListener mGhostListener;
    private HorizontalListView mListView;
    private RegularProgressView mProgressView;
    private RecordController mRecordController;
    private RecordingNavigationController mRecordingNavigationController;
    private View mSaveButton;
    private ImageView mSilenceButton;
    private RangeSeekBar mTrimmer;

    public EditMode(Activity activity, InvalidateGhostListener ghostListener, SdkVideoView videoView, RecordController controller, RecordingNavigationController viewController) throws IllegalStateException, Resources.NotFoundException {
        super(videoView);
        this.mCurrentKeyFrames = null;
        this.mActivity = activity;
        this.mRecordController = controller;
        this.mRecordingNavigationController = viewController;
        this.mGhostListener = ghostListener;
        this.mDragSortWidget = (DragSortWidget) activity.findViewById(R.id.thumbnail_list);
        this.mListView = (HorizontalListView) activity.findViewById(android.R.id.list);
        int clipProgressColor = activity.getResources().getColor(R.color.timebar_selected_color);
        this.mProgressView = (RegularProgressView) activity.findViewById(R.id.edit_progress);
        this.mProgressView.setSelectedColor(clipProgressColor);
        this.mTrimmer = new RangeSeekBar((NinePatchDrawable) activity.getResources().getDrawable(R.drawable.scrub_left), (NinePatchDrawable) activity.getResources().getDrawable(R.drawable.scrub_right), 0, 100, activity, clipProgressColor, activity.getResources().getColor(R.color.black_thirty_five_percent));
        this.mTrimmer.setVisibility(8);
        this.mTrimmer.setOnRangeSeekBarChangeListener(this);
        ViewGroup videoContainer = (ViewGroup) activity.findViewById(R.id.video_container);
        int height = activity.getResources().getDimensionPixelSize(R.dimen.trimmer_height);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, height);
        params.gravity = 81;
        params.bottomMargin = activity.getResources().getDimensionPixelOffset(R.dimen.trimmer_margin);
        this.mTrimmer.setLayoutParams(params);
        videoContainer.addView(this.mTrimmer);
        this.mAllDeletedOverlay = activity.findViewById(R.id.all_deleted_overlay);
        this.mButtonsDeselected = activity.findViewById(R.id.editorButtons);
        this.mButtonsSelected = activity.findViewById(R.id.individual_edit_tools_container);
        this.mDeleteClipButton = (ImageView) activity.findViewById(R.id.trashCanButtonIndividual);
        this.mDeleteClipButton.setOnTouchListener(new SelectedClipButtonTouchListener() { // from class: co.vine.android.recordingui.EditMode.1
            @Override // co.vine.android.recordingui.EditMode.SelectedClipButtonTouchListener
            public void onClick() {
                EditMode.this.mDragSortWidget.deleteSelection();
            }
        });
        updateIndividualButtonTouchBackground(this.mDeleteClipButton, false);
        this.mSilenceButton = (ImageView) activity.findViewById(R.id.silenceButton);
        this.mSilenceButton.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.recordingui.EditMode.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) throws IllegalStateException {
                switch (motionEvent.getAction()) {
                    case 0:
                        EditMode.this.updateSilenceButtonDisplay(EditMode.this.mRecordController.isSegmentSilenced(EditMode.this.mDragSortWidget.getSelection()) ? false : true, true);
                        return true;
                    case 1:
                        boolean contains = new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                        int selection = EditMode.this.mDragSortWidget.getSelection();
                        if (contains) {
                            EditMode.this.mRecordController.toggleSilenceSegment(selection);
                        }
                        boolean silenced = EditMode.this.mRecordController.isSegmentSilenced(selection);
                        EditMode.this.updateSilenceButtonDisplay(silenced, false);
                        EditMode.this.mVideoView.setMute(silenced);
                        return true;
                    case 2:
                        boolean contains2 = new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                        EditMode.this.updateSilenceButtonDisplay(EditMode.this.mRecordController.isSegmentSilenced(EditMode.this.mDragSortWidget.getSelection()) ^ contains2, contains2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        updateSilenceButtonDisplay(false, false);
        this.mDuplicateClipButton = (ImageView) activity.findViewById(R.id.duplicateButton);
        this.mDuplicateClipButton.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.recordingui.EditMode.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean canDupe = EditMode.this.mRecordController.canDuplicateSegment(EditMode.this.mDragSortWidget.getSelection());
                switch (motionEvent.getAction()) {
                    case 0:
                        EditMode.this.updateDuplicateButtonDisplay(canDupe, true);
                        return true;
                    case 1:
                        boolean contains = new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                        if (contains && canDupe) {
                            EditMode.this.mRecordController.duplicateSegment(EditMode.this.mDragSortWidget.getSelection(), true);
                            EditMode.this.mEditorAdapter.notifyDataSetChanged();
                        }
                        EditMode.this.updateDuplicateButtonDisplay(EditMode.this.mRecordController.canDuplicateSegment(EditMode.this.mDragSortWidget.getSelection()), false);
                        return true;
                    case 2:
                        boolean contains2 = new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                        EditMode.this.updateDuplicateButtonDisplay(canDupe, contains2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        updateIndividualButtonTouchBackground(this.mDuplicateClipButton, false);
        this.mSaveButton = activity.findViewById(R.id.editorDoneButton);
        this.mSaveButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.EditMode.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                EditMode.this.mRecordController.endEditing(false);
                EditMode.this.mRecordingNavigationController.toggleEditMode();
            }
        });
        this.mBackButton = activity.findViewById(R.id.editorCancelButton);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.EditMode.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws IllegalStateException {
                EditMode.this.onBackPressed();
            }
        });
        setupEditing();
    }

    @Override // co.vine.android.recorder2.RecordControllerImpl.RecordingEventListener
    public void onRecordingProgressChanged(float ratio, boolean overMinDuration) {
        this.mProgressView.setProgressRatio(ratio);
        if (!this.mRecordController.draftHasSegment()) {
            this.mAllDeletedOverlay.setVisibility(0);
            this.mVideoView.setVisibility(8);
            return;
        }
        this.mAllDeletedOverlay.setVisibility(8);
        this.mVideoView.setVisibility(0);
        int selection = this.mDragSortWidget.getSelection();
        if (selection != -1) {
            float[] segmentBounds = this.mRecordController.getSegmentRatioBounds(selection);
            this.mProgressView.setSelectedSection(segmentBounds[0], segmentBounds[1]);
        }
    }

    @Override // co.vine.android.recorder2.RecordControllerImpl.RecordingEventListener
    public void onMaxDurationReached() {
    }

    @Override // co.vine.android.views.RangeSeekBar.OnRangeSeekBarChangeListener
    public void onRangeSeekBarTouchDown(RangeSeekBar rangeSeekBar, int minValue, int maxValue) {
    }

    @Override // co.vine.android.views.RangeSeekBar.OnRangeSeekBarChangeListener
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, int minValue, int maxValue, boolean byUser) throws IllegalStateException {
        int position = this.mDragSortWidget.getSelection();
        this.mRecordController.setSegmentTrimPoints(position, minValue, maxValue);
        this.mVideoView.seekTo(minValue + 100);
        updateDuplicateButtonDisplay(this.mRecordController.canDuplicateSegment(position), false);
    }

    @Override // co.vine.android.views.RangeSeekBar.OnRangeSeekBarChangeListener
    public void onRangeSeekBarTouchUp(RangeSeekBar rangeSeekBar, int minValue, int maxValue) {
        int closestKeyframePercent = getClosestKeyframe(minValue);
        this.mTrimmer.setSelectedMinValue(closestKeyframePercent);
        int correctedMaxMs = getCorrectedMaxValue(minValue, maxValue);
        this.mTrimmer.setSelectedMaxValue(correctedMaxMs);
        int position = this.mDragSortWidget.getSelection();
        this.mRecordController.setSegmentTrimPoints(position, closestKeyframePercent, correctedMaxMs);
        updateDuplicateButtonDisplay(this.mRecordController.canDuplicateSegment(position), false);
    }

    @Override // co.vine.android.views.RangeSeekBar.OnRangeSeekBarChangeListener
    public void onRangeSeekBarLongPressHappened() {
    }

    private int getCorrectedMaxValue(int minMs, int maxMs) {
        if (maxMs - minMs < 33) {
            return minMs + 33;
        }
        return maxMs;
    }

    private int getClosestKeyframe(int fingerValue) {
        long closestKeyframeTimestamp = 0;
        long minDistance = 2147483647L;
        Iterator<Long> it = this.mCurrentKeyFrames.iterator();
        while (it.hasNext()) {
            long keyFrameTimestamp = it.next().longValue();
            long distance = Math.abs(fingerValue - keyFrameTimestamp);
            if (distance < minDistance) {
                closestKeyframeTimestamp = keyFrameTimestamp;
                minDistance = distance;
            }
        }
        return (int) closestKeyframeTimestamp;
    }

    private abstract class SelectedClipButtonTouchListener implements View.OnTouchListener {
        public abstract void onClick();

        private SelectedClipButtonTouchListener() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case 0:
                    EditMode.this.updateIndividualButtonTouchBackground(view, true);
                    return true;
                case 1:
                    EditMode.this.updateIndividualButtonTouchBackground(view, false);
                    boolean contains = new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                    if (!contains) {
                        return true;
                    }
                    onClick();
                    return true;
                case 2:
                    boolean contains2 = new Rect(0, 0, view.getWidth(), view.getHeight()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                    EditMode.this.updateIndividualButtonTouchBackground(view, contains2);
                    return true;
                default:
                    return false;
            }
        }
    }

    public void updateIndividualButtonTouchBackground(View view, boolean enabled) {
        if (enabled) {
            view.setBackground(PRESSED_STATE_COLOR);
            view.setAlpha(1.0f);
        } else {
            view.setBackground(null);
            view.setAlpha(0.5f);
        }
    }

    public void showDiscardChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
        builder.setMessage(R.string.cancel_edited_changes_confirm);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() { // from class: co.vine.android.recordingui.EditMode.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                EditMode.this.mRecordController.endEditing(-1 != i);
                EditMode.this.mRecordingNavigationController.toggleEditMode();
            }
        };
        builder.setPositiveButton(R.string.save, listener);
        builder.setNegativeButton(R.string.discard_changes, listener);
        builder.show();
    }

    public void setupEditing() throws IllegalStateException {
        this.mEditorAdapter = new SegmentEditorAdapter(this.mRecordController.getDraft(), this.mActivity, this.mGhostListener, this.mActivity.getResources().getDisplayMetrics().density);
        this.mDragSortWidget.setAdapter(this.mEditorAdapter);
        this.mDragSortWidget.setRemoveListener(this);
        this.mDragSortWidget.setSelectionChangedListener(this);
        this.mDragSortWidget.setFloatViewInteractionListener(new DragSortWidget.FloatViewInteractionListener() { // from class: co.vine.android.recordingui.EditMode.7
            @Override // co.vine.android.dragsort.DragSortWidget.FloatViewInteractionListener
            public boolean canPickUpFloatView() {
                return true;
            }

            @Override // co.vine.android.dragsort.DragSortWidget.FloatViewInteractionListener
            public void floatViewPickedUp(int item) {
            }

            @Override // co.vine.android.dragsort.DragSortWidget.FloatViewInteractionListener
            public void floatViewMoved() {
            }

            @Override // co.vine.android.dragsort.DragSortWidget.FloatViewInteractionListener
            public boolean floatViewDropped(int to) {
                return true;
            }

            @Override // co.vine.android.dragsort.DragSortWidget.FloatViewInteractionListener
            public void floatViewLanded(int to) {
            }
        });
        this.mEditorAdapter.notifyDataSetChanged();
        this.mDragSortWidget.setSelection(-1);
        this.mDragSortWidget.setFocused(0);
        this.mVideoView.setVideoPath(this.mRecordController.getDraftFullVideoPath());
        this.mVideoView.setAutoPlayOnPrepared(true);
    }

    @Override // co.vine.android.dragsort.DragSortWidget.SelectionChangedListener
    public void onSelectionChanged(int position, boolean listViewClick) throws IllegalStateException {
        if (position != -1) {
            showSegmentEditMode(position);
        } else {
            showFullVideoEditMode();
        }
    }

    private void showSegmentEditMode(int position) throws IllegalStateException {
        boolean silenced = this.mRecordController.isSegmentSilenced(position);
        this.mButtonsDeselected.setVisibility(8);
        this.mButtonsSelected.setVisibility(0);
        this.mTrimmer.setAbsoluteMaxValue(Integer.valueOf((int) this.mRecordController.getSegmentUntrimmedDurationMS(position)));
        long[] segmentTrimBounds = this.mRecordController.getSegmentTrimBounds(position);
        this.mTrimmer.setSelectedMinValue((int) segmentTrimBounds[0]);
        this.mTrimmer.setSelectedMaxValue((int) segmentTrimBounds[1]);
        this.mTrimmer.setVisibility(0);
        this.mVideoView.setVideoPath(this.mRecordController.getVideoPathForSegmentAtPosition(position));
        this.mVideoView.setMute(silenced);
        this.mVideoView.setLooping(false);
        this.mVideoView.seekTo((int) (segmentTrimBounds[0] + 100));
        startMonitoringVideoPlaybackForTrim(new TrimmedVideoPlaybackRecordingMode.TrimPointPositionProvider() { // from class: co.vine.android.recordingui.EditMode.8
            @Override // co.vine.android.recordingui.TrimmedVideoPlaybackRecordingMode.TrimPointPositionProvider
            public long getStartPositionMS() {
                return EditMode.this.mTrimmer.getSelectedMinValue();
            }

            @Override // co.vine.android.recordingui.TrimmedVideoPlaybackRecordingMode.TrimPointPositionProvider
            public long getEndPositionMS() {
                return EditMode.this.mTrimmer.getSelectedMaxValue();
            }
        });
        this.mCurrentKeyFrames = null;
        try {
            this.mCurrentKeyFrames = this.mRecordController.getSegmentKeyframeTimestampsMS(position);
        } catch (IOException e) {
        }
        if (this.mCurrentKeyFrames == null) {
            this.mCurrentKeyFrames = new ArrayList<>();
            this.mCurrentKeyFrames.add(0L);
        }
        float[] segmentBounds = this.mRecordController.getSegmentRatioBounds(position);
        this.mProgressView.setSelectedSection(segmentBounds[0], segmentBounds[1]);
        updateSilenceButtonDisplay(silenced, false);
        updateDuplicateButtonDisplay(this.mRecordController.canDuplicateSegment(position), false);
    }

    private void showFullVideoEditMode() throws IllegalStateException {
        this.mVideoView.stopPlayback();
        if (this.mRecordController.draftHasSegment()) {
            generateAndPlayFullPreview();
        }
        this.mVideoView.setOnCompletionListener(null);
        this.mVideoView.setLooping(true);
        this.mProgressView.setSelectedSection(0.0f, 0.0f);
        this.mTrimmer.setVisibility(8);
        stopMonitoringVideoPlaybackForTrim();
        this.mVideoView.setVideoPath(null);
        this.mVideoView.setMute(false);
        this.mButtonsDeselected.setVisibility(0);
        this.mButtonsSelected.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDuplicateButtonDisplay(boolean canDupe, boolean touchOn) {
        if (!canDupe) {
            this.mDuplicateClipButton.setAlpha(0.17f);
        } else {
            this.mDuplicateClipButton.setAlpha(touchOn ? 1.0f : 0.5f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSilenceButtonDisplay(boolean on, boolean touchon) {
        if (on) {
            this.mSilenceButton.setAlpha(1.0f);
        } else {
            this.mSilenceButton.setAlpha(0.5f);
        }
        if (touchon) {
            this.mSilenceButton.setBackground(PRESSED_STATE_COLOR);
        } else {
            this.mSilenceButton.setBackground(null);
        }
    }

    private void generateAndPlayFullPreview() {
        this.mRecordController.generateFullVideo(new Runnable() { // from class: co.vine.android.recordingui.EditMode.9
            @Override // java.lang.Runnable
            public void run() throws IllegalStateException {
                if (EditMode.this.mVideoView.getVisibility() == 0) {
                    EditMode.this.playFullDraft();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playFullDraft() throws IllegalStateException {
        this.mVideoView.setVideoPath(this.mRecordController.getDraftFullVideoPath());
        this.mVideoView.setMute(false);
    }

    public void show() throws IllegalStateException {
        setupEditing();
        this.mListView.setVisibility(0);
        this.mVideoView.setVisibility(0);
        this.mButtonsDeselected.setVisibility(0);
        this.mProgressView.setVisibility(0);
        this.mProgressView.setSelectedSection(0.0f, 0.0f);
        this.mRecordController.setRecordingEventListener(this);
        this.mRecordController.startEditing();
    }

    public void hide() throws IllegalStateException {
        this.mListView.setVisibility(8);
        this.mVideoView.stopPlayback();
        this.mVideoView.setVisibility(8);
        this.mButtonsDeselected.setVisibility(8);
        this.mAllDeletedOverlay.setVisibility(8);
        this.mProgressView.setVisibility(4);
        stopMonitoringVideoPlaybackForTrim();
        this.mDragSortWidget.setSelection(-1);
    }

    public void onBackPressed() throws IllegalStateException {
        if (this.mButtonsSelected.getVisibility() == 0) {
            this.mDragSortWidget.setSelection(-1);
            onSelectionChanged(-1, false);
        } else if (this.mRecordController.editorDraftHasChanged()) {
            showDiscardChangesDialog();
        } else {
            this.mRecordController.endEditing(false);
            this.mRecordingNavigationController.toggleEditMode();
        }
    }

    @Override // co.vine.android.dragsort.DragSortWidget.RemoveListener
    public void remove(int which) {
        this.mRecordController.removeSegmentAt(which);
        this.mEditorAdapter.notifyDataSetChanged();
    }

    public void onPause() throws IllegalStateException {
        this.mVideoView.pause();
    }

    public void onResume() throws IllegalStateException {
        this.mVideoView.resume();
    }
}
