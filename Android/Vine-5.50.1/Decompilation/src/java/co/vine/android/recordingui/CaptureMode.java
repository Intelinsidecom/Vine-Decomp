package co.vine.android.recordingui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.vine.android.ActivityResultHandler;
import co.vine.android.ImportTrimCropActivity;
import co.vine.android.ImportVideoTabActivity;
import co.vine.android.R;
import co.vine.android.RecordingNavigationController;
import co.vine.android.RecordingTouchClickListener;
import co.vine.android.animation.SimpleAnimationListener;
import co.vine.android.player.SdkVideoView;
import co.vine.android.recorder.RegularProgressView;
import co.vine.android.recorder2.ImportProgressListener;
import co.vine.android.recorder2.InvalidateGhostListener;
import co.vine.android.recorder2.RecordController;
import co.vine.android.recorder2.RecordControllerImpl;
import co.vine.android.recorder2.gles.Texture2dProgram;
import co.vine.android.recorder2.model.DraftsManager;
import co.vine.android.recorder2.model.ImportVideoInfo;
import co.vine.android.recordingui.TrimmedVideoPlaybackRecordingMode;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.ToolDrawerLinearLayout;
import co.vine.android.widget.TouchInterceptingRelativeLayout;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CaptureMode extends TrimmedVideoPlaybackRecordingMode implements ActivityResultHandler, ImportProgressListener, InvalidateGhostListener, RecordControllerImpl.RecordingEventListener {
    private final Activity mActivity;
    private final ActivityStarterForResult mActivityStarter;
    private ImageView mBackspaceButton;
    public View.OnTouchListener mBackspaceInterceptTouchListener;
    private View mCameraDirectionToggle;
    private int mCountFinishedImport;
    private int mCountImport;
    private int mDeleteHighlightColor;
    private View mDraftButtonContainer;
    private ImageView mDraftButtonImageView;
    private TextView mDraftButtonTextView;
    private View mDrawerNib;
    private View mDrawerToggle;
    private View mFinishArrow;
    private View mFinishButton;
    private ProgressBar mFinishProgressSpinner;
    private ImageView mFlashToggle;
    private AnimationSet mFocusAnimationSet;
    private AlphaAnimation mFocusDismissAnimation;
    private View mFocusIndicator;
    private boolean mFocusModeOn;
    private ImageView mFocusToggle;
    private final GLSurfaceView mGLView;
    private View mGhostOverlay;
    private ImageView mGhostToggle;
    private View mGridOverlay;
    private ImageView mGridToggle;
    private ImageView mImportButton;
    private ProgressDialog mImportProgressDialog;
    private boolean mOnboardingEnabled;
    private View mOnboardingTooltip;
    private RegularProgressView mProgressView;
    private View mProgressViewContainer;
    private RecordController mRecordController;
    private boolean mRecordingEnabled;
    private final RecordingNavigationController mRecordingNavigationController;
    private View mRecordingOptions;
    private TouchInterceptingRelativeLayout mRootInterceptTouchView;
    private ToolDrawerLinearLayout mToolDrawer;
    private float mTotalProgress;
    private int mTouchDownX;
    private int mTouchDownY;
    private int mVineGreen;

    public interface ActivityStarterForResult {
        void startActivityForResult(Intent intent, int i, ActivityResultHandler activityResultHandler);
    }

    public CaptureMode(Activity activity, ActivityStarterForResult activityStarter, GLSurfaceView glView, SdkVideoView videoView, RecordController recordController, RecordingNavigationController recordingNavigationController) {
        super(videoView);
        this.mFocusModeOn = false;
        this.mRecordingEnabled = true;
        this.mBackspaceInterceptTouchListener = new View.OnTouchListener() { // from class: co.vine.android.recordingui.CaptureMode.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent e) throws IllegalStateException {
                switch (e.getAction()) {
                    case 1:
                        Rect backspaceLocation = ViewUtil.getViewPositionInAncestorWithId(CaptureMode.this.mBackspaceButton, R.id.fragment_container);
                        if (backspaceLocation.contains((int) e.getX(), (int) e.getY())) {
                            CaptureMode.this.mRecordController.deleteLastSegment();
                        }
                        CaptureMode.this.resetBackspaceState();
                    case 0:
                    case 2:
                    default:
                        return true;
                }
            }
        };
        this.mTouchDownY = 0;
        this.mTouchDownX = 0;
        this.mActivity = activity;
        this.mActivityStarter = activityStarter;
        this.mRecordingNavigationController = recordingNavigationController;
        this.mRecordController = recordController;
        this.mVideoView = (SdkVideoView) activity.findViewById(R.id.video_view);
        this.mRootInterceptTouchView = (TouchInterceptingRelativeLayout) this.mActivity.findViewById(R.id.fragment_container);
        this.mRootInterceptTouchView.setOnInterceptTouchesListener(this.mBackspaceInterceptTouchListener);
        this.mGLView = glView;
        this.mGLView.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.recordingui.CaptureMode.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        if (CaptureMode.this.mFocusModeOn) {
                            CaptureMode.this.setFocusPointAndAnimateIndicator((int) event.getX(), (int) event.getY());
                            return true;
                        }
                        if (CaptureMode.this.mRecordingEnabled) {
                            CaptureMode.this.mRecordController.startRecording();
                        }
                        CaptureMode.this.mTouchDownY = (int) event.getY();
                        CaptureMode.this.mTouchDownX = (int) event.getX();
                        return true;
                    case 1:
                        if (CaptureMode.this.mFocusModeOn) {
                            return true;
                        }
                        Texture2dProgram.sVertOffset = 0.0f;
                        Texture2dProgram.sHorizOffset = 0.0f;
                        if (!CaptureMode.this.mRecordingEnabled) {
                            return true;
                        }
                        CaptureMode.this.mRecordController.endRecording();
                        return true;
                    case 2:
                        int deltay = (int) (event.getY() - CaptureMode.this.mTouchDownY);
                        int deltax = (int) (event.getX() - CaptureMode.this.mTouchDownX);
                        float percenty = deltay / CaptureMode.this.mGLView.getHeight();
                        float percentx = deltax / CaptureMode.this.mGLView.getWidth();
                        Texture2dProgram.sVertOffset = -percenty;
                        Texture2dProgram.sHorizOffset = percentx;
                    default:
                        return false;
                }
            }
        });
        Resources res = activity.getResources();
        this.mVineGreen = res.getColor(R.color.vine_green);
        this.mDeleteHighlightColor = res.getColor(R.color.plugin_delete_selected_segment);
        this.mProgressView = (RegularProgressView) this.mActivity.findViewById(R.id.progress);
        this.mProgressView.setColor(this.mVineGreen);
        this.mProgressView.setSelectedColor(this.mDeleteHighlightColor);
        this.mProgressView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CaptureMode.this.mRecordController.isCurrentlyIdle() && CaptureMode.this.mRecordController.draftHasSegment()) {
                    CaptureMode.this.mRecordingNavigationController.toggleEditMode();
                }
            }
        });
        this.mFinishProgressSpinner = (ProgressBar) activity.findViewById(R.id.finish_progress);
        this.mFinishButton = activity.findViewById(R.id.finish_button);
        this.mFinishArrow = activity.findViewById(R.id.finish_arrow);
        this.mProgressViewContainer = activity.findViewById(R.id.progress_container);
        setupTooltip();
        setupRecordingOptions();
        setupDrawerAndContents();
        setupFilters();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupFilters() {
        if (ClientFlagsHelper.isRecorderFilteringEnabled(this.mActivity)) {
            this.mActivity.findViewById(R.id.filters).setVisibility(0);
            TextView recordButton = (TextView) this.mActivity.findViewById(R.id.toggleRecord);
            final TextView none = (TextView) this.mActivity.findViewById(R.id.none);
            final TextView rgbOneDir = (TextView) this.mActivity.findViewById(R.id.rgbonedir);
            final TextView rgbSpread = (TextView) this.mActivity.findViewById(R.id.rgbspread);
            final TextView lens = (TextView) this.mActivity.findViewById(R.id.lens);
            final TextView emboss = (TextView) this.mActivity.findViewById(R.id.emboss);
            final TextView blur = (TextView) this.mActivity.findViewById(R.id.blur);
            final TextView sharpen = (TextView) this.mActivity.findViewById(R.id.sharpen);
            final TextView edge = (TextView) this.mActivity.findViewById(R.id.edge);
            final TextView mirror = (TextView) this.mActivity.findViewById(R.id.mirror);
            final TextView time = (TextView) this.mActivity.findViewById(R.id.time);
            final TextView twist = (TextView) this.mActivity.findViewById(R.id.twist);
            final TextView[] all = {none, rgbOneDir, rgbSpread, lens, emboss, blur, sharpen, edge, mirror, time, twist};
            setupActivated(none, all);
            this.mRecordController.changeFilter(0);
            recordButton.setActivated(this.mRecordingEnabled);
            none.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.4
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(0);
                    CaptureMode.this.setupActivated(none, all);
                }
            });
            rgbOneDir.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.5
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(1);
                    CaptureMode.this.setupActivated(rgbOneDir, all);
                }
            });
            rgbSpread.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.6
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(6);
                    CaptureMode.this.setupActivated(rgbSpread, all);
                }
            });
            lens.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.7
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(7);
                    CaptureMode.this.setupActivated(lens, all);
                }
            });
            twist.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.8
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(10);
                    CaptureMode.this.setupActivated(twist, all);
                }
            });
            emboss.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.9
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(5);
                    CaptureMode.this.setupActivated(emboss, all);
                }
            });
            blur.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.10
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(2);
                    CaptureMode.this.setupActivated(blur, all);
                }
            });
            sharpen.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.11
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(3);
                    CaptureMode.this.setupActivated(sharpen, all);
                }
            });
            edge.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.12
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(4);
                    CaptureMode.this.setupActivated(edge, all);
                }
            });
            mirror.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.13
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(8);
                    CaptureMode.this.setupActivated(mirror, all);
                }
            });
            time.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.14
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordController.changeFilter(9);
                    CaptureMode.this.setupActivated(time, all);
                }
            });
            recordButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.15
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    CaptureMode.this.mRecordingEnabled = !CaptureMode.this.mRecordingEnabled;
                    v.setActivated(CaptureMode.this.mRecordingEnabled);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupActivated(TextView activated, TextView[] all) {
        int length = all.length;
        for (int i = 0; i < length; i++) {
            TextView t = all[i];
            t.setActivated(t == activated);
        }
    }

    private void setupTooltip() {
        this.mOnboardingTooltip = this.mActivity.findViewById(R.id.onboarding);
        final SharedPreferences onboardingPrefs = CommonUtil.getDefaultSharedPrefs(this.mActivity);
        this.mOnboardingEnabled = onboardingPrefs.getBoolean("camera_onboarding_enabled", true);
        if (this.mOnboardingEnabled) {
            this.mOnboardingTooltip.setVisibility(0);
            this.mOnboardingTooltip.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.recordingui.CaptureMode.16
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    CaptureMode.this.mOnboardingEnabled = false;
                    CaptureMode.this.mOnboardingTooltip.setVisibility(4);
                    onboardingPrefs.edit().putBoolean("camera_onboarding_enabled", false).commit();
                    return false;
                }
            });
        } else {
            this.mOnboardingTooltip.setVisibility(4);
        }
    }

    private void setupRecordingOptions() {
        this.mRecordingOptions = this.mActivity.findViewById(R.id.recording_options);
        this.mImportButton = (ImageView) this.mActivity.findViewById(R.id.import_button);
        this.mImportButton.setAlpha(0.5f);
        this.mImportButton.setOnTouchListener(new RecordingTouchClickListener() { // from class: co.vine.android.recordingui.CaptureMode.17
            @Override // co.vine.android.RecordingTouchClickListener
            protected void onClick() {
                if (!CaptureMode.this.mRecordController.isRecording()) {
                    CaptureMode.this.importVideo(null, null);
                }
            }
        });
        this.mBackspaceButton = (ImageView) this.mActivity.findViewById(R.id.delete_last_segment);
        this.mBackspaceButton.setAlpha(0.17f);
        this.mBackspaceButton.setOnTouchListener(new RecordingTouchClickListener() { // from class: co.vine.android.recordingui.CaptureMode.18
            @Override // co.vine.android.RecordingTouchClickListener
            protected void updateButtonDisplay(View view, boolean on) {
                if (on && CaptureMode.this.mRecordController.draftHasSegment()) {
                    CaptureMode.this.mBackspaceButton.setAlpha(1.0f);
                    CaptureMode.this.mBackspaceButton.setImageResource(R.drawable.ic_backspace_pressed);
                } else if (!on && CaptureMode.this.mRecordController.draftHasSegment()) {
                    CaptureMode.this.mBackspaceButton.setImageResource(R.drawable.ic_backspace_default);
                    CaptureMode.this.mBackspaceButton.setAlpha(0.5f);
                } else if (!on && !CaptureMode.this.mRecordController.draftHasSegment()) {
                    CaptureMode.this.mBackspaceButton.setImageResource(R.drawable.ic_backspace_default);
                    CaptureMode.this.mBackspaceButton.setAlpha(0.17f);
                }
            }

            @Override // co.vine.android.RecordingTouchClickListener
            protected void onClick() throws IllegalStateException {
                if (CaptureMode.this.mRecordController.isCurrentlyIdle() && CaptureMode.this.mRecordController.draftHasSegment()) {
                    float[] segmentBounds = CaptureMode.this.mRecordController.getSegmentRatioBounds(CaptureMode.this.mRecordController.getSegmentCount() - 1);
                    CaptureMode.this.mProgressView.setSelectedSection(segmentBounds[0], segmentBounds[1]);
                    CaptureMode.this.mVideoView.setVisibility(0);
                    CaptureMode.this.mVideoView.setVideoPath(CaptureMode.this.mRecordController.getVideoPathForLastSegment());
                    final long[] trimPoints = CaptureMode.this.mRecordController.getSegmentTrimBounds(CaptureMode.this.mRecordController.getSegmentCount() - 1);
                    CaptureMode.this.startMonitoringVideoPlaybackForTrim(new TrimmedVideoPlaybackRecordingMode.TrimPointPositionProvider() { // from class: co.vine.android.recordingui.CaptureMode.18.1
                        @Override // co.vine.android.recordingui.TrimmedVideoPlaybackRecordingMode.TrimPointPositionProvider
                        public long getStartPositionMS() {
                            return trimPoints[0];
                        }

                        @Override // co.vine.android.recordingui.TrimmedVideoPlaybackRecordingMode.TrimPointPositionProvider
                        public long getEndPositionMS() {
                            return trimPoints[1];
                        }
                    });
                    CaptureMode.this.mGLView.setVisibility(8);
                    CaptureMode.this.mBackspaceButton.setAlpha(1.0f);
                    CaptureMode.this.mBackspaceButton.setImageResource(R.drawable.ic_backspace_pressed);
                    CaptureMode.this.mRootInterceptTouchView.setInterceptTouches(true);
                }
            }
        });
        this.mCameraDirectionToggle = this.mActivity.findViewById(R.id.camera_toggle);
        this.mCameraDirectionToggle.setAlpha(0.5f);
        this.mCameraDirectionToggle.setOnTouchListener(new RecordingTouchClickListener() { // from class: co.vine.android.recordingui.CaptureMode.19
            @Override // co.vine.android.RecordingTouchClickListener
            protected void updateButtonDisplay(View view, boolean on) {
                boolean alphaOn = on || CaptureMode.this.mRecordController.isCameraFrontFacing();
                CaptureMode.this.mCameraDirectionToggle.setAlpha(alphaOn ? 1.0f : 0.5f);
            }

            @Override // co.vine.android.RecordingTouchClickListener
            protected void onClick() {
                if (!CaptureMode.this.mRecordController.isRecording()) {
                    boolean ffc = CaptureMode.this.mRecordController.switchCamera();
                    CaptureMode.this.mCameraDirectionToggle.setAlpha(ffc ? 1.0f : 0.5f);
                    CaptureMode.this.updateToggleDisplay(CaptureMode.this.mFlashToggle, false);
                    CaptureMode.this.mFocusToggle.setImageResource(ffc ? R.drawable.ic_focus_disabled : R.drawable.ic_focus);
                    CaptureMode.this.mFlashToggle.setAlpha(CaptureMode.this.mRecordController.cameraHasFlash() ? 0.5f : 0.17f);
                    CaptureMode.this.setupFilters();
                    CaptureMode.this.mToolDrawer.invalidate();
                }
            }
        });
        this.mDraftButtonTextView = (TextView) this.mActivity.findViewById(R.id.drafts_button_count);
        this.mDraftButtonImageView = (ImageView) this.mActivity.findViewById(R.id.drafts_button);
        this.mDraftButtonContainer = this.mActivity.findViewById(R.id.drafts_button_container);
        this.mDraftButtonContainer.setAlpha(0.5f);
        this.mDraftButtonContainer.setOnTouchListener(new RecordingTouchClickListener() { // from class: co.vine.android.recordingui.CaptureMode.20
            @Override // co.vine.android.RecordingTouchClickListener
            protected void onClick() {
                CaptureMode.this.handleDraftAction(true);
            }
        });
    }

    public void showProgressSpinner() {
        this.mFinishProgressSpinner.post(new Runnable() { // from class: co.vine.android.recordingui.CaptureMode.21
            @Override // java.lang.Runnable
            public void run() {
                CaptureMode.this.mFinishProgressSpinner.setVisibility(0);
                CaptureMode.this.mFinishArrow.setVisibility(8);
            }
        });
    }

    public void hideProgressSpinner() {
        this.mFinishProgressSpinner.post(new Runnable() { // from class: co.vine.android.recordingui.CaptureMode.22
            @Override // java.lang.Runnable
            public void run() {
                CaptureMode.this.mFinishProgressSpinner.setVisibility(8);
                CaptureMode.this.mFinishArrow.setVisibility(0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetBackspaceState() throws IllegalStateException {
        this.mVideoView.stopPlayback();
        stopMonitoringVideoPlaybackForTrim();
        this.mVideoView.setVideoPath(null);
        this.mVideoView.setVisibility(8);
        this.mGLView.setVisibility(0);
        this.mProgressView.setSelectedSection(0.0f, 0.0f);
        this.mActivity.findViewById(R.id.fragment_container).setOnClickListener(null);
        this.mBackspaceButton.setImageResource(R.drawable.ic_backspace_default);
        this.mRootInterceptTouchView.setInterceptTouches(false);
        if (this.mRecordController.draftHasSegment()) {
            this.mBackspaceButton.setAlpha(0.5f);
        } else {
            this.mBackspaceButton.setAlpha(0.17f);
        }
    }

    public void setupDrawerAndContents() {
        this.mToolDrawer = (ToolDrawerLinearLayout) this.mActivity.findViewById(R.id.recording_sub_options);
        this.mDrawerNib = this.mActivity.findViewById(R.id.drawer_nib);
        this.mDrawerToggle = this.mActivity.findViewById(R.id.drawer_toggle);
        this.mDrawerToggle.setAlpha(0.5f);
        ((View) this.mDrawerToggle.getParent()).setOnTouchListener(new RecordingTouchClickListener() { // from class: co.vine.android.recordingui.CaptureMode.23
            @Override // co.vine.android.RecordingTouchClickListener
            protected void updateButtonDisplay(View view, boolean on) {
                boolean onAlpha = on || CaptureMode.this.mToolDrawer.isOpen();
                CaptureMode.this.mDrawerToggle.setAlpha(onAlpha ? 1.0f : 0.5f);
            }

            @Override // co.vine.android.RecordingTouchClickListener
            protected void onClick() {
                CaptureMode.this.mToolDrawer.toggle(new Runnable() { // from class: co.vine.android.recordingui.CaptureMode.23.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!CaptureMode.this.mToolDrawer.isOpen()) {
                            CaptureMode.this.mDrawerNib.setVisibility(8);
                        }
                    }
                });
                if (CaptureMode.this.mToolDrawer.isOpen()) {
                    CaptureMode.this.mDrawerNib.setVisibility(0);
                }
                CaptureMode.this.mDrawerToggle.setAlpha(CaptureMode.this.mToolDrawer.isOpen() ? 1.0f : 0.5f);
            }
        });
        this.mGridOverlay = this.mActivity.findViewById(R.id.grid);
        this.mGridToggle = (ImageView) this.mActivity.findViewById(R.id.grid_toggle);
        updateToggleDisplay(this.mGridToggle, false);
        this.mGridToggle.setOnTouchListener(new RecordingTouchClickListener() { // from class: co.vine.android.recordingui.CaptureMode.24
            @Override // co.vine.android.RecordingTouchClickListener
            protected void updateButtonDisplay(View view, boolean on) {
                CaptureMode.this.updateToggleDisplay(CaptureMode.this.mGridToggle, on || CaptureMode.this.mGridOverlay.getVisibility() == 0);
            }

            @Override // co.vine.android.RecordingTouchClickListener
            public void onClick() {
                if (CaptureMode.this.mGridOverlay.getVisibility() == 0) {
                    CaptureMode.this.mGridOverlay.setVisibility(4);
                } else {
                    CaptureMode.this.mGridOverlay.setVisibility(0);
                }
                CaptureMode.this.updateToggleDisplay(CaptureMode.this.mGridToggle, CaptureMode.this.mGridOverlay.getVisibility() == 0);
            }
        });
        this.mFocusToggle = (ImageView) this.mActivity.findViewById(R.id.focus_toggle);
        updateToggleDisplay(this.mFocusToggle, false);
        this.mFocusIndicator = this.mActivity.findViewById(R.id.focus_indicator);
        setupFocusAnimation();
        this.mFocusToggle.setOnTouchListener(new RecordingTouchClickListener() { // from class: co.vine.android.recordingui.CaptureMode.25
            @Override // co.vine.android.RecordingTouchClickListener
            protected void updateButtonDisplay(View view, boolean on) {
                if (!CaptureMode.this.mRecordController.isCameraFrontFacing()) {
                    CaptureMode.this.updateToggleDisplay(CaptureMode.this.mFocusToggle, on || CaptureMode.this.mFocusModeOn);
                }
            }

            @Override // co.vine.android.RecordingTouchClickListener
            public void onClick() {
                if (!CaptureMode.this.mRecordController.isRecording() && !CaptureMode.this.mRecordController.isCameraFrontFacing()) {
                    if (!CaptureMode.this.mFocusModeOn) {
                        CaptureMode.this.mFocusModeOn = true;
                    } else {
                        CaptureMode.this.mFocusModeOn = false;
                    }
                    CaptureMode.this.updateToggleDisplay(CaptureMode.this.mFocusToggle, CaptureMode.this.mFocusModeOn);
                }
            }
        });
        this.mGhostOverlay = this.mActivity.findViewById(R.id.ghost_overlay);
        this.mGhostToggle = (ImageView) this.mActivity.findViewById(R.id.ghost_toggle);
        updateToggleDisplay(this.mGhostToggle, false);
        this.mGhostToggle.setOnTouchListener(new RecordingTouchClickListener() { // from class: co.vine.android.recordingui.CaptureMode.26
            @Override // co.vine.android.RecordingTouchClickListener
            protected void updateButtonDisplay(View view, boolean on) {
                CaptureMode.this.updateToggleDisplay(CaptureMode.this.mGhostToggle, on || CaptureMode.this.mGhostOverlay.getVisibility() == 0);
            }

            @Override // co.vine.android.RecordingTouchClickListener
            public void onClick() {
                if (CaptureMode.this.mGhostOverlay.getVisibility() == 0) {
                    CaptureMode.this.mGhostOverlay.setVisibility(8);
                } else {
                    CaptureMode.this.mGhostOverlay.setVisibility(0);
                    CaptureMode.this.invalidateGhost();
                }
                CaptureMode.this.updateToggleDisplay(CaptureMode.this.mGhostToggle, CaptureMode.this.mGhostOverlay.getVisibility() == 0);
            }
        });
        this.mFlashToggle = (ImageView) this.mActivity.findViewById(R.id.flash_toggle);
        updateToggleDisplay(this.mFlashToggle, false);
        this.mFlashToggle.setOnTouchListener(new RecordingTouchClickListener() { // from class: co.vine.android.recordingui.CaptureMode.27
            @Override // co.vine.android.RecordingTouchClickListener
            protected void updateButtonDisplay(View view, boolean on) {
                if (CaptureMode.this.mRecordController.cameraHasFlash()) {
                    CaptureMode.this.updateToggleDisplay(CaptureMode.this.mFlashToggle, on || CaptureMode.this.mRecordController.isFlashOn());
                } else {
                    CaptureMode.this.updateToggleDisplay(CaptureMode.this.mFlashToggle, false);
                    CaptureMode.this.mFlashToggle.setAlpha(0.17f);
                }
            }

            @Override // co.vine.android.RecordingTouchClickListener
            public void onClick() {
                if (CaptureMode.this.mRecordController.cameraHasFlash()) {
                    boolean flashOn = CaptureMode.this.mRecordController.toggleFlash();
                    CaptureMode.this.updateToggleDisplay(CaptureMode.this.mFlashToggle, flashOn);
                } else {
                    CaptureMode.this.mFlashToggle.setAlpha(0.17f);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFocusPointAndAnimateIndicator(int x, int y) {
        View indicator = this.mFocusIndicator;
        boolean showIndicator = this.mRecordController.setFocusPoint(x, y);
        if (indicator != null && showIndicator) {
            int width = indicator.getMeasuredWidth();
            int height = indicator.getMeasuredHeight();
            indicator.layout(x - (width / 2), y - (height / 2), (width / 2) + x, (height / 2) + y);
            indicator.setVisibility(0);
            indicator.startAnimation(this.mFocusAnimationSet);
        }
    }

    public void setupFocusAnimation() {
        this.mFocusDismissAnimation = new AlphaAnimation(1.0f, 0.0f);
        this.mFocusDismissAnimation.setDuration(300L);
        this.mFocusDismissAnimation.setFillAfter(true);
        this.mFocusDismissAnimation.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.recordingui.CaptureMode.28
            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (CaptureMode.this.mFocusIndicator != null) {
                    CaptureMode.this.mFocusIndicator.setVisibility(4);
                }
            }
        });
        ViewGroup.LayoutParams indicatorParams = this.mFocusIndicator.getLayoutParams();
        final ScaleAnimation focusResizeAnimation = new ScaleAnimation(1.0f, 0.85f, 1.0f, 0.85f, indicatorParams.width / 2, indicatorParams.height / 2);
        focusResizeAnimation.setRepeatMode(2);
        focusResizeAnimation.setRepeatCount(1);
        focusResizeAnimation.setDuration(80L);
        focusResizeAnimation.setFillAfter(true);
        focusResizeAnimation.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.recordingui.CaptureMode.29
            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                CaptureMode.this.mFocusIndicator.startAnimation(CaptureMode.this.mFocusDismissAnimation);
            }
        });
        this.mFocusAnimationSet = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        this.mFocusAnimationSet.addAnimation(animation);
        Animation animation2 = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, indicatorParams.width / 2, indicatorParams.height / 2);
        this.mFocusAnimationSet.addAnimation(animation2);
        this.mFocusAnimationSet.setFillAfter(true);
        this.mFocusAnimationSet.setDuration(300L);
        this.mFocusAnimationSet.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.recordingui.CaptureMode.30
            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation3) {
                CaptureMode.this.mFocusIndicator.startAnimation(focusResizeAnimation);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateToggleDisplay(ImageView toggle, boolean on) {
        if (on) {
            Drawable d = toggle.getDrawable();
            d.setColorFilter(this.mVineGreen, PorterDuff.Mode.SRC_ATOP);
            toggle.setAlpha(1.0f);
        } else {
            Drawable d2 = toggle.getDrawable();
            d2.clearColorFilter();
            toggle.setAlpha(0.5f);
        }
    }

    @Override // co.vine.android.recorder2.InvalidateGhostListener
    public void invalidateGhost() {
        this.mActivity.runOnUiThread(new Runnable() { // from class: co.vine.android.recordingui.CaptureMode.31
            @Override // java.lang.Runnable
            public void run() {
                Drawable ghost = CaptureMode.this.mRecordController.createGhostDrawable();
                CaptureMode.this.mGhostOverlay.setBackground(ghost);
            }
        });
    }

    @Override // co.vine.android.recorder2.RecordControllerImpl.RecordingEventListener
    public void onRecordingProgressChanged(final float ratio, final boolean overMinDuration) {
        this.mActivity.runOnUiThread(new Runnable() { // from class: co.vine.android.recordingui.CaptureMode.32
            @Override // java.lang.Runnable
            public void run() {
                CaptureMode.this.mProgressView.setProgressRatio(ratio);
                if (ratio > 0.0f) {
                    CaptureMode.this.mBackspaceButton.setAlpha(0.5f);
                } else {
                    CaptureMode.this.mBackspaceButton.setAlpha(0.17f);
                }
                CaptureMode.this.mFinishButton.setVisibility(overMinDuration ? 0 : 8);
                CaptureMode.this.updateDraftButton();
            }
        });
    }

    @Override // co.vine.android.recorder2.RecordControllerImpl.RecordingEventListener
    public void onMaxDurationReached() {
        this.mRecordingNavigationController.goToPreview();
    }

    public void show() {
        this.mRecordController.setRecordingEventListener(this);
        this.mGLView.setVisibility(0);
        this.mRecordingOptions.setVisibility(0);
        this.mProgressView.setVisibility(0);
        this.mToolDrawer.setVisibility(4);
        this.mProgressViewContainer.setVisibility(0);
        this.mFinishProgressSpinner.setVisibility(8);
        this.mOnboardingTooltip.setVisibility(this.mOnboardingEnabled ? 0 : 8);
        DraftsManager.loadDraftsAsync(this.mActivity, new DraftsManager.OnDraftsLoadedListener() { // from class: co.vine.android.recordingui.CaptureMode.33
            @Override // co.vine.android.recorder2.model.DraftsManager.OnDraftsLoadedListener
            public void onDraftsLoaded(int count) {
                CaptureMode.this.mActivity.runOnUiThread(new Runnable() { // from class: co.vine.android.recordingui.CaptureMode.33.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CaptureMode.this.updateDraftButton();
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDraftButton() {
        int draftCount = DraftsManager.getDraftsCount();
        if (this.mRecordController.draftCanBeSaved() || draftCount == 0) {
            this.mDraftButtonImageView.setImageResource(R.drawable.ic_save);
            this.mDraftButtonTextView.setVisibility(8);
        } else {
            this.mDraftButtonImageView.setImageResource(R.drawable.ic_drafts);
            this.mDraftButtonTextView.setText(String.valueOf(draftCount));
            this.mDraftButtonTextView.setVisibility(0);
        }
    }

    public void hide() {
        this.mGLView.setVisibility(8);
        this.mFocusModeOn = false;
        updateToggleDisplay(this.mFocusToggle, false);
        this.mGridOverlay.setVisibility(8);
        updateToggleDisplay(this.mGridToggle, false);
        this.mGhostOverlay.setVisibility(8);
        updateToggleDisplay(this.mGhostToggle, false);
        this.mRecordController.turnOffFlash();
        updateToggleDisplay(this.mFlashToggle, false);
        this.mRecordingOptions.setVisibility(8);
        this.mProgressView.setVisibility(8);
        this.mToolDrawer.setVisibility(8);
        this.mDrawerToggle.setAlpha(0.5f);
        this.mDrawerNib.setVisibility(8);
        if (this.mToolDrawer.isOpen()) {
            this.mToolDrawer.toggle(null);
        }
        this.mProgressViewContainer.setVisibility(8);
        this.mFinishProgressSpinner.setVisibility(8);
        this.mOnboardingTooltip.setVisibility(8);
    }

    public void onBackPressed() {
        handleDraftAction(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDraftAction(boolean goToDrafts) {
        if (this.mRecordController.isCurrentlyIdle()) {
            if (this.mRecordController.draftCanBeSaved()) {
                showDiscardDialog(goToDrafts);
            } else if (this.mRecordController.draftHasBeenSaved()) {
                this.mRecordingNavigationController.handleDraftAction(RecordingNavigationController.DraftAction.DISCARD_REVERT, goToDrafts);
            } else {
                this.mRecordingNavigationController.handleDraftAction(RecordingNavigationController.DraftAction.DISCARD_DELETE, goToDrafts);
            }
        }
    }

    private void showDiscardDialog(final boolean goToDrafts) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
        final boolean isNew = !this.mRecordController.draftHasBeenSaved();
        final boolean isEmpty = !this.mRecordController.draftHasSegment();
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() { // from class: co.vine.android.recordingui.CaptureMode.34
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    if (isEmpty) {
                        CaptureMode.this.mRecordingNavigationController.handleDraftAction(RecordingNavigationController.DraftAction.DISCARD_DELETE, goToDrafts);
                        return;
                    } else {
                        CaptureMode.this.mRecordingNavigationController.handleDraftAction(RecordingNavigationController.DraftAction.SAVE, goToDrafts);
                        return;
                    }
                }
                if (isNew) {
                    CaptureMode.this.mRecordingNavigationController.handleDraftAction(RecordingNavigationController.DraftAction.DISCARD_DELETE, goToDrafts);
                } else {
                    CaptureMode.this.mRecordingNavigationController.handleDraftAction(RecordingNavigationController.DraftAction.DISCARD_REVERT, goToDrafts);
                }
            }
        };
        if (isNew) {
            builder.setMessage(R.string.save_draft_new_confirm);
        } else {
            builder.setMessage(R.string.save_draft_old_confirm);
        }
        builder.setPositiveButton(R.string.save, listener);
        builder.setNegativeButton(R.string.discard_changes, listener);
        builder.show();
    }

    public void importVideo(Uri inUri, String sourcePostId) {
        if (this.mActivity != null && this.mRecordController.isCurrentlyIdle()) {
            int totalDurationMs = (int) this.mRecordController.getCurrentDuration();
            int maxDurationMs = (int) this.mRecordController.getMaxDuration();
            if (maxDurationMs - totalDurationMs > 200) {
                if (ClientFlagsHelper.isMultiSourceImportEnabled(this.mActivity) && inUri == null) {
                    this.mActivityStarter.startActivityForResult(new Intent(this.mActivity, (Class<?>) ImportVideoTabActivity.class), 23, this);
                    return;
                }
                Intent intent = new Intent(this.mActivity, (Class<?>) ImportTrimCropActivity.class);
                intent.putExtra("current_duration", totalDurationMs);
                intent.putExtra("max_duration", maxDurationMs);
                intent.setData(inUri);
                intent.putExtra("extra_source_post_id", sourcePostId);
                this.mActivityStarter.startActivityForResult(intent, 1, this);
            }
        }
    }

    @Override // co.vine.android.ActivityResultHandler
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<ImportVideoInfo> videos;
        if (resultCode == -1) {
            this.mImportProgressDialog = new ProgressDialog(this.mActivity, 2);
            this.mImportProgressDialog.setMessage(this.mActivity.getString(R.string.importing));
            this.mImportProgressDialog.setMax(100);
            this.mImportProgressDialog.setProgressDrawable(this.mActivity.getResources().getDrawable(R.drawable.progress_horizontal));
            this.mImportProgressDialog.setCancelable(false);
            this.mImportProgressDialog.setIndeterminate(false);
            this.mImportProgressDialog.setProgressStyle(1);
            this.mImportProgressDialog.show();
            this.mCountFinishedImport = 0;
            this.mTotalProgress = 0.0f;
            if (requestCode == 1) {
                this.mCountImport = 1;
                long startMicros = data.getLongExtra("trim_start_time_usec", 0L);
                long endMicros = data.getLongExtra("trim_end_time_usec", 0L);
                Point cropOrigin = (Point) data.getParcelableExtra("crop_origin");
                String sourcePostId = data.getStringExtra("extra_source_post_id");
                Uri inUri = data.getData();
                this.mRecordController.importVideo(inUri, cropOrigin, startMicros, endMicros, this, sourcePostId);
                return;
            }
            if (requestCode == 23 && (videos = (ArrayList) data.getSerializableExtra("extra_selected_videos")) != null && !videos.isEmpty()) {
                this.mCountImport = videos.size();
                this.mRecordController.importVideos(videos, this);
            }
        }
    }

    @Override // co.vine.android.recorder2.ImportProgressListener
    public void onImportProgressChanged(float incrementPercent) {
        this.mTotalProgress += incrementPercent;
        this.mImportProgressDialog.setProgress((int) (this.mTotalProgress / this.mCountImport));
    }

    @Override // co.vine.android.recorder2.ImportProgressListener
    public void onImportProgressFinished() {
        this.mCountFinishedImport++;
        if (this.mCountFinishedImport >= this.mCountImport) {
            this.mImportProgressDialog.dismiss();
            this.mCountImport = 0;
            this.mCountFinishedImport = 0;
            this.mTotalProgress = 0.0f;
        }
    }
}
