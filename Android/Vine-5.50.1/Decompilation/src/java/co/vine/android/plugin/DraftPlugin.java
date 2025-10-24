package co.vine.android.plugin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.vine.android.AbstractRecordingActivity;
import co.vine.android.R;
import co.vine.android.RecordingFragment;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.RecordSessionManager;
import co.vine.android.recorder.VineRecorder;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.MediaUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widgets.PromptDialogFragment;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;

/* loaded from: classes.dex */
public class DraftPlugin extends BaseRecorderPlugin<View, VineRecorder> {
    private final PromptDialogFragment.OnDialogDoneListener mDirtyDialogDoneListener;
    private View mDraftOverlayIcon;
    private int mDraftUpgradeCount;
    private TextView mDraftsButton;
    private Animation mFadeIn;
    private final Animation.AnimationListener mFadeInListener;
    private Animation mFadeOut;
    private final Animation.AnimationListener mFadeOutListener;
    private boolean mIsDraftShowing;
    private boolean mIsDraftUpgradeAnimationRunning;
    private boolean mIsEditing;
    private View mNoDraftsOverlay;
    private int mNumDrafts;
    final View.OnTouchListener mOnNoDraftOverlayTouchListener;
    private View mRecordingOptions;
    private final Runnable mStartDraftsRunnable;
    private ProgressBar mUpgradeSpinner;

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public boolean onSetEditMode(boolean editing, boolean hasData) {
        this.mIsEditing = editing;
        return this.mIsDraftShowing;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onResume(Activity activity) {
        super.onResume(activity);
        if ((activity instanceof AbstractRecordingActivity) && !((AbstractRecordingActivity) activity).isDraftsShowing()) {
            refreshDraftNumber();
        }
    }

    public DraftPlugin() {
        super("draft");
        this.mFadeOutListener = new Animation.AnimationListener() { // from class: co.vine.android.plugin.DraftPlugin.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                DraftPlugin.this.mDraftOverlayIcon.setVisibility(8);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                DraftPlugin.this.mDraftsButton.setAlpha(0.35f);
                DraftPlugin.this.mNoDraftsOverlay.setVisibility(8);
                DraftPlugin.this.mNoDraftsOverlay.setOnTouchListener(null);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }
        };
        this.mFadeInListener = new Animation.AnimationListener() { // from class: co.vine.android.plugin.DraftPlugin.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                DraftPlugin.this.mNoDraftsOverlay.setVisibility(0);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }
        };
        this.mOnNoDraftOverlayTouchListener = new View.OnTouchListener() { // from class: co.vine.android.plugin.DraftPlugin.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                DraftPlugin.this.mFadeOut.setAnimationListener(DraftPlugin.this.mFadeOutListener);
                DraftPlugin.this.mNoDraftsOverlay.startAnimation(DraftPlugin.this.mFadeOut);
                DraftPlugin.this.mDraftsButton.setAlpha(0.35f);
                return true;
            }
        };
        this.mDirtyDialogDoneListener = new PromptDialogFragment.OnDialogDoneListener() { // from class: co.vine.android.plugin.DraftPlugin.5
            @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
            public void onDialogDone(DialogInterface dialog, int id, int which) {
                switch (which) {
                    case -2:
                        DraftPlugin.this.startDrafts(false);
                        break;
                    case -1:
                        DraftPlugin.this.startDrafts(true);
                        break;
                    default:
                        DraftPlugin.this.mIsDraftShowing = false;
                        break;
                }
            }
        };
        this.mStartDraftsRunnable = new Runnable() { // from class: co.vine.android.plugin.DraftPlugin.6
            @Override // java.lang.Runnable
            public void run() throws Resources.NotFoundException, IOException {
                String strValueOf;
                AbstractRecordingActivity activity = (AbstractRecordingActivity) DraftPlugin.this.getActivity();
                VineRecorder recorder = DraftPlugin.this.getRecorder();
                if (activity != null && recorder != null) {
                    DraftPlugin.this.getManager().onStartDrafts();
                    if (recorder.grabThumbnailsRunnable != null) {
                        MediaUtil.GenerateThumbnailsRunnable r = recorder.grabThumbnailsRunnable;
                        r.run();
                        recorder.grabThumbnailsRunnable = null;
                    }
                    if (recorder.isSavedSession()) {
                        strValueOf = recorder.getFile().folder.getName();
                    } else {
                        strValueOf = String.valueOf("camera_preview");
                    }
                    activity.showDrafts(strValueOf);
                }
            }
        };
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin
    public void onDraftUpgradeNumberChanged(int count) {
        SLog.d("Draft upgrade number changed with count {}", Integer.valueOf(count));
        this.mDraftUpgradeCount = count;
        if (this.mIsDraftUpgradeAnimationRunning) {
            if (count > 0) {
                this.mUpgradeSpinner.setVisibility(0);
                this.mDraftsButton.setVisibility(8);
            } else {
                this.mUpgradeSpinner.setVisibility(8);
                this.mDraftsButton.setVisibility(0);
            }
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onHideDrafts() {
        this.mIsDraftShowing = false;
        refreshDraftNumber();
        this.mRecordingOptions.setAlpha(0.0f);
        this.mRecordingOptions.setVisibility(0);
        this.mRecordingOptions.animate().alpha(1.0f).start();
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onShowDrafts() {
        this.mIsDraftShowing = true;
        this.mRecordingOptions.setVisibility(8);
    }

    private void refreshDraftNumber() {
        try {
            BasicVineRecorder recorder = getRecorder();
            if (recorder != null && recorder.hasSessionFile()) {
                int numDrafts = RecordSessionManager.getNumberOfValidSessions(recorder);
                if (numDrafts > 0) {
                    if (numDrafts > 9) {
                        numDrafts--;
                    }
                    this.mDraftsButton.setText(String.valueOf(numDrafts));
                    this.mDraftsButton.setBackgroundResource(R.drawable.ic_drafts);
                } else {
                    this.mDraftsButton.setText("");
                    this.mDraftsButton.setBackgroundResource(R.drawable.ic_save);
                }
                this.mNumDrafts = numDrafts;
            }
        } catch (IOException e) {
            CrashUtil.logException(e);
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void setMessagingMode(boolean isMessaging) {
        View view;
        if (isMessaging && (view = getView()) != null) {
            view.setVisibility(8);
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public boolean canEdit() {
        return !this.mIsDraftShowing;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin
    public View onLayout(ViewGroup parent, LayoutInflater inflater, Fragment fragment) {
        this.mFadeIn = AnimationUtils.loadAnimation(fragment.getActivity(), R.anim.fade_in);
        this.mFadeOut = AnimationUtils.loadAnimation(fragment.getActivity(), R.anim.fade_out);
        View draftButton = inflater.inflate(R.layout.plugin_drafts, parent, false);
        this.mUpgradeSpinner = (ProgressBar) draftButton.findViewById(R.id.upgrade_spinner);
        this.mDraftsButton = (TextView) draftButton.findViewById(R.id.drafts_button);
        this.mDraftsButton.setAlpha(0.35f);
        this.mDraftsButton.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.plugin.DraftPlugin.4
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) throws Resources.NotFoundException {
                switch (event.getAction()) {
                    case 0:
                        DraftPlugin.this.mDraftsButton.setAlpha(1.0f);
                        break;
                    case 1:
                        DraftPlugin.this.mDraftsButton.setAlpha(0.35f);
                        DraftPlugin.this.onClick(null);
                        break;
                }
                return true;
            }
        });
        View view = fragment.getView();
        ViewGroup topView = (ViewGroup) view.findViewById(R.id.thumbnail_list);
        inflater.inflate(R.layout.plugin_no_drafts_overlay, topView, true);
        this.mRecordingOptions = view.findViewById(R.id.recording_options);
        this.mNoDraftsOverlay = view.findViewById(R.id.no_drafts_overlay);
        Resources res = fragment.getResources();
        View sadface = this.mNoDraftsOverlay.findViewById(R.id.no_drafts_overlay_face);
        float progressHeight = res.getDimensionPixelOffset(R.dimen.progress_view_height);
        float sadfaceHeight = res.getDimensionPixelOffset(R.dimen.draft_sadface_height);
        sadface.setY(((SystemUtil.getDisplaySize(fragment.getActivity()).x / 2) + progressHeight) - sadfaceHeight);
        View sadfaceText = this.mNoDraftsOverlay.findViewById(R.id.no_drafts_text);
        float spacing = res.getDimensionPixelOffset(R.dimen.draft_sadface_spacing);
        sadfaceText.setY(sadface.getY() + spacing);
        this.mDraftOverlayIcon = view.findViewById(R.id.no_draft_overlay_draft_button);
        return draftButton;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDrafts(boolean save) {
        VineRecorder recorder;
        SLog.d("Start Drafts: {}.", Boolean.valueOf(save));
        Fragment fragment = getFragment();
        if ((fragment instanceof RecordingFragment) && (recorder = getRecorder()) != null) {
            if (save) {
                FlurryUtils.trackSaveSession("drafts");
                recorder.saveSession(this.mStartDraftsRunnable, false);
            } else {
                ((RecordingFragment) fragment).setFileFileToUse(null);
                recorder.stopAndDiscardChanges("StartDrafts", this.mStartDraftsRunnable, false);
            }
        }
    }

    public void onClick(View v) throws Resources.NotFoundException {
        VineRecorder recorder = getRecorder();
        if (!this.mIsEditing && recorder != null && !recorder.isEditing() && !recorder.isCurrentlyRecording()) {
            if (this.mDraftUpgradeCount > 0) {
                FlurryUtils.trackSessionSwitchPressedOnDraftUpgrade(this.mDraftUpgradeCount);
                Handler handler = getHandler();
                if (handler != null) {
                    handler.post(new Runnable() { // from class: co.vine.android.plugin.DraftPlugin.7
                        @Override // java.lang.Runnable
                        public void run() {
                            if (DraftPlugin.this.mActivity != null) {
                                Util.showCenteredToast(DraftPlugin.this.mActivity, R.string.draft_upgrade_in_process);
                            }
                            if (!DraftPlugin.this.mIsDraftUpgradeAnimationRunning) {
                                DraftPlugin.this.mIsDraftUpgradeAnimationRunning = true;
                                DraftPlugin.this.onDraftUpgradeNumberChanged(DraftPlugin.this.mDraftUpgradeCount);
                            }
                        }
                    });
                    return;
                }
                return;
            }
            FlurryUtils.trackSessionSwitchPressed();
            boolean dirty = recorder.isSessionDirty();
            if (this.mNumDrafts > 0 || dirty) {
                if (this.mActivity instanceof AbstractRecordingActivity) {
                    this.mIsDraftShowing = true;
                    if (dirty) {
                        ((AbstractRecordingActivity) this.mActivity).showUnSavedChangesToSessionDialog(this.mDirtyDialogDoneListener);
                        return;
                    } else {
                        startDrafts(false);
                        return;
                    }
                }
                return;
            }
            if (this.mActivity != null) {
                this.mDraftOverlayIcon.setVisibility(0);
                int[] location = new int[2];
                this.mDraftsButton.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                this.mDraftOverlayIcon.setX(x);
                int draftOverlayOffset = this.mActivity.getResources().getDimensionPixelOffset(R.dimen.draft_overlay_offset);
                this.mDraftOverlayIcon.setY(y - draftOverlayOffset);
                this.mNoDraftsOverlay.setOnTouchListener(this.mOnNoDraftOverlayTouchListener);
                this.mFadeIn.setAnimationListener(this.mFadeInListener);
                this.mNoDraftsOverlay.startAnimation(this.mFadeIn);
            }
        }
    }
}
