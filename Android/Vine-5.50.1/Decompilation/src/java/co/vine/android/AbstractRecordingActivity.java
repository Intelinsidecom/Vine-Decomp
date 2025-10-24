package co.vine.android;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.DraftFragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.DraftViewPager;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.animation.MoveResizeAnimator;
import co.vine.android.animation.SimpleAnimationListener;
import co.vine.android.animation.SimpleAnimatorListener;
import co.vine.android.animation.SmoothAnimator;
import co.vine.android.client.AppController;
import co.vine.android.player.SdkVideoView;
import co.vine.android.plugin.BaseRecorderPluginManager;
import co.vine.android.plugin.CameraSwitcherPlugin;
import co.vine.android.plugin.DeleteLastSegmentPlugin;
import co.vine.android.plugin.DraftPlugin;
import co.vine.android.plugin.FocusPlugin;
import co.vine.android.plugin.GhostPlugin;
import co.vine.android.plugin.GridPlugin;
import co.vine.android.plugin.ImportOnboardPlugin;
import co.vine.android.plugin.ImportPlugin;
import co.vine.android.plugin.IndividualEditPlugin;
import co.vine.android.plugin.MoreToolPlugin;
import co.vine.android.plugin.RecorderPlugin;
import co.vine.android.plugin.SecretModePlugin;
import co.vine.android.plugin.TimeLapsePlugin;
import co.vine.android.plugin.ZoomPlugin;
import co.vine.android.recorder.ByteBufferQueue;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordSessionManager;
import co.vine.android.recorder.RecordSessionVersion;
import co.vine.android.recorder.RecordingActivityHelper;
import co.vine.android.recorder.RecordingFile;
import co.vine.android.recorder.RegularVineRecorder;
import co.vine.android.recorder.ViewGoneAnimationListener;
import co.vine.android.recorder.VineRecorder;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.recordingui.RecordStateHolder;
import co.vine.android.service.VineUploadService;
import co.vine.android.share.activities.PostShareParameters;
import co.vine.android.util.AppTrackingUtil;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.FloatingViewUtils;
import co.vine.android.util.IntentionalObjectCounter;
import co.vine.android.util.MediaUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.UploadManager;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.DotIndicators;
import co.vine.android.widget.DragUpToDeleteContainer;
import co.vine.android.widgets.PromptDialogFragment;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(14)
/* loaded from: classes.dex */
public abstract class AbstractRecordingActivity extends BaseActionBarActivity implements DraftViewPager.OnPageChangeListener, SmoothAnimator.AnimationListener, ByteBufferQueue.MemoryResponder, RecordStateHolder, DragUpToDeleteContainer.DragUpListener {
    private static final ArrayList<WeakReference<Object>> sQueues = new ArrayList<>();
    private DraftPagerImpl mAdapter;
    private Thread mAddToUploadThread;
    private View mAnimationPreviewOverlay;
    private WeakReference<DraftCameraPreviewFragment> mCameraFragment;
    private Animation mCameraIconFadeIn;
    private View mCameraIconOverlay;
    private View mCameraIconOverlayImage;
    protected int mColor;
    protected long mConversationRowId;
    public long mCurrentDuration;
    protected Fragment mCurrentFragment;
    int mCurrentPage;
    private RecordSessionManager.RecordSessionInfo mCurrentSession;
    protected long mDirectUserId;
    private DotIndicators mDots;
    private View mDraftFullMask;
    private View mDraftMaskLeft;
    private View mDraftMaskParent;
    private View mDraftMaskRight;
    private View mDraftMaskTop;
    private View mDraftRoot;
    private View mDraftTrashBackground;
    private View mDraftTrashButton;
    private View mDraftTrashContainer;
    private DragUpToDeleteContainer mDragUpToDeleteView;
    private boolean mFirstDraftLaunch;
    private boolean mFirstPageSet;
    private boolean mHasPreviewedAlready;
    private boolean mHasStartedRelativeTimeFromHardwareButton;
    private boolean mIsGoingToRecord;
    protected boolean mIsMessaging;
    private boolean mIsResumed;
    private ImageView mLargeThumbnailOverlay;
    private SetSelectedRunnable mOnPageIdleRunnable;
    private int mPageScrollState;
    private PostShareParameters mPostShareParameters;
    int mPreviewDimen;
    private View mPreviewOverlay;
    float mPreviewRatio;
    public int mProgressContainerWidth;
    private View mProgressOverlay;
    private PromptDialogFragment mPromptDialog;
    private String mRecipientUsername;
    private int mRegularUiMode;
    private Point mScreenSize;
    private ArrayList<RecordSessionManager.RecordSessionInfo> mSessions;
    private int mSideMaskWidth;
    private Animation mSlowFadeIn;
    private String mStartSessionId;
    private int mTopMaskHeight;
    private Bitmap mTopOverlay;
    protected String mUploadFile;
    private RecordSessionVersion mVersion;
    private DraftViewPager mViewPager;
    private final IntentionalObjectCounter mIntentionalObjectCounter = new IntentionalObjectCounter("recorder", this);
    private final Handler mHandler = new Handler();
    private final SparseArray<WeakReference<DraftFragment>> mDraftFragments = new SparseArray<>();
    private ArrayList<SdkVideoView> mVideoViews = new ArrayList<>();
    private int mStep = -1;
    private boolean mDeleteWasDrag = false;
    private final View.OnClickListener mEmptyClicker = new View.OnClickListener() { // from class: co.vine.android.AbstractRecordingActivity.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
        }
    };
    private boolean mAnimatingIntoDrafts = false;
    final View.OnTouchListener mOnMaskTouchListesner = new View.OnTouchListener() { // from class: co.vine.android.AbstractRecordingActivity.2
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };
    private final RecordingActivityHelper mHelper = new RecordingActivityHelper();
    private final PromptDialogFragment.OnDialogDoneListener mDeleteDialogDoneListener = new PromptDialogFragment.OnDialogDoneListener() { // from class: co.vine.android.AbstractRecordingActivity.7
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) throws Resources.NotFoundException {
            switch (which) {
                case -2:
                    AbstractRecordingActivity.this.showCurrentFragment();
                    AbstractRecordingActivity.this.mDraftTrashButton.setActivated(false);
                    AbstractRecordingActivity.this.mDraftTrashBackground.setActivated(false);
                    break;
                case -1:
                    AbstractRecordingActivity.this.handleDelete();
                    AbstractRecordingActivity.this.mDraftTrashButton.setActivated(false);
                    AbstractRecordingActivity.this.mDraftTrashBackground.setActivated(false);
                    break;
            }
        }
    };
    private final PromptDialogFragment.OnDialogDoneListener mUnsavedChangesDialogDoneListener = new PromptDialogFragment.OnDialogDoneListener() { // from class: co.vine.android.AbstractRecordingActivity.8
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (which) {
                case -2:
                    if (AbstractRecordingActivity.this.currentlyHoldsRecordingFragment()) {
                        RecordingFragment frag = (RecordingFragment) AbstractRecordingActivity.this.mCurrentFragment;
                        frag.setDiscardChangesOnStop();
                        frag.playStopSound();
                    }
                    AbstractRecordingActivity.this.discardUpload();
                    AbstractRecordingActivity.this.setResult(100);
                    AbstractRecordingActivity.this.finish();
                    break;
                case -1:
                    if (AbstractRecordingActivity.this.currentlyHoldsRecordingFragment()) {
                        AbstractRecordingActivity.this.setResult(100);
                        ((RecordingFragment) AbstractRecordingActivity.this.mCurrentFragment).saveSessionAndQuit();
                        break;
                    }
                    break;
            }
        }
    };
    private final Runnable mOnResumeDraftRunnable = new Runnable() { // from class: co.vine.android.AbstractRecordingActivity.13
        @Override // java.lang.Runnable
        public void run() {
            DraftFragment fragment;
            if (AbstractRecordingActivity.this.mAdapter.getCount() != 1 && AbstractRecordingActivity.this.mCurrentPage != AbstractRecordingActivity.this.mAdapter.getCount() - 1) {
                AbstractRecordingActivity.this.mCurrentSession = (RecordSessionManager.RecordSessionInfo) AbstractRecordingActivity.this.mSessions.get(AbstractRecordingActivity.this.mCurrentPage);
                WeakReference<DraftFragment> f = (WeakReference) AbstractRecordingActivity.this.mDraftFragments.get(AbstractRecordingActivity.this.mCurrentPage);
                if (f != null && (fragment = f.get()) != null) {
                    fragment.setSelected(true);
                }
            }
        }
    };

    private RecordSessionVersion getVersion() {
        if (this.mVersion == null) {
            this.mVersion = RecordSessionManager.getCurrentVersion(this);
        }
        return this.mVersion;
    }

    public static Intent getIntentForGeneric(Context context, int flags, String tag) {
        return getIntent(context, flags, tag, false, -1L, -1L, null);
    }

    @Override // co.vine.android.recorder.ByteBufferQueue.MemoryResponder
    public void requestForMoreMemory() {
        try {
            AppController.getInstance(this).clearImageCacheFromMemory();
        } catch (RuntimeException e) {
            CrashUtil.log("Failed to clear image cache memory.");
        }
    }

    public static Intent getIntentForConversation(Context context, int flags, String tag, long conversationRowId, String username) {
        return getIntent(context, flags, tag, true, conversationRowId, -1L, username);
    }

    private static Intent getIntent(Context context, int flags, String tag, boolean isMessaging, long conversationRowId, long directUserId, String recipientName) {
        Intent intent = new Intent(context, (Class<?>) RecordingActivity.class);
        intent.setAction(tag);
        if (flags > 0) {
            intent.setFlags(flags);
        }
        intent.putExtra("messaging", isMessaging);
        intent.putExtra("conv_row_id", conversationRowId);
        intent.putExtra("direct_id", directUserId);
        intent.putExtra("recipient_username", recipientName);
        return intent;
    }

    @Override // co.vine.android.BaseActionBarActivity
    public void preSetContentView() {
        if (!CameraManager.hasCamera()) {
            Util.showNoCameraToast(this);
            finish();
        }
    }

    public AbstractRecordingActivity() {
        if (sQueues.size() > 20) {
            sQueues.clear();
        }
        sQueues.add(new WeakReference<>(this));
        int n = 0;
        Iterator<WeakReference<Object>> it = sQueues.iterator();
        while (it.hasNext()) {
            WeakReference<Object> queue = it.next();
            if (queue.get() != null) {
                n++;
            }
        }
        CrashUtil.log("[mem] Current RecordingActivity queue count: {}.", Integer.valueOf(n));
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        Bundle extras;
        Intent intent = getIntent();
        String action = null;
        if (intent != null) {
            action = intent.getAction();
        }
        CrashUtil.log("AbstractRecordingActivity {} pid: {}, action tag {}.", this, Integer.valueOf(Process.myPid()), action);
        this.mIntentionalObjectCounter.add();
        FlurryUtils.trackRecordingStart();
        super.onCreate(savedInstanceState, R.layout.record_draft, false, true);
        hideActionBar();
        this.mHelper.bindCameraService(this);
        this.mScreenSize = SystemUtil.getDisplaySize(this);
        if (SystemUtil.getMemoryRatio(this, true) < 1.0d) {
            Util.showCenteredToast(this, R.string.unsupported_feature_recording);
            finish();
            return;
        }
        this.mVersion = RecordSessionManager.getCurrentVersion(this);
        if (savedInstanceState == null) {
            if (intent != null && (extras = intent.getExtras()) != null) {
                this.mIsMessaging = extras.getBoolean("messaging");
                this.mConversationRowId = extras.getLong("conv_row_id", -1L);
                this.mTopOverlay = (Bitmap) extras.getParcelable("arg_top_overlay");
                this.mDirectUserId = extras.getLong("direct_id", -1L);
                this.mRecipientUsername = extras.getString("recipient_username");
            }
            try {
                toRecord(true, false, null);
            } catch (InvalidStateException e) {
                throw new RuntimeException(e);
            }
        }
        this.mSessionManager.resetSessions(this);
        startService(VineUploadService.getClearNotificationsIntent(this));
        Resources resources = getResources();
        this.mPreviewRatio = 0.85f;
        this.mDraftRoot = findViewById(R.id.drafts_root);
        this.mDraftRoot.setVisibility(8);
        this.mViewPager = (DraftViewPager) findViewById(R.id.pager);
        this.mDots = (DotIndicators) findViewById(R.id.dots);
        this.mPreviewDimen = (int) (this.mScreenSize.x * this.mPreviewRatio);
        this.mPreviewOverlay = findViewById(R.id.preview_overlay);
        this.mProgressOverlay = findViewById(R.id.progress_overlay);
        this.mDraftMaskTop = findViewById(R.id.toDraftTopMask);
        this.mDraftMaskTop.setOnClickListener(this.mEmptyClicker);
        this.mDraftMaskLeft = findViewById(R.id.toDraftLeftMask);
        this.mDraftMaskLeft.setOnClickListener(this.mEmptyClicker);
        this.mDraftMaskRight = findViewById(R.id.toDraftRightMask);
        this.mDraftMaskRight.setOnClickListener(this.mEmptyClicker);
        this.mDraftMaskParent = findViewById(R.id.toDraftParent);
        this.mDraftTrashContainer = findViewById(R.id.draftsTrashContainer);
        this.mDraftTrashBackground = findViewById(R.id.draftsTrashBackground);
        this.mDraftTrashButton = findViewById(R.id.draftsTrashButton);
        this.mDraftTrashButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.AbstractRecordingActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (AbstractRecordingActivity.this.mCurrentPage != AbstractRecordingActivity.this.mDraftFragments.size()) {
                    AbstractRecordingActivity.this.mDeleteWasDrag = false;
                    AbstractRecordingActivity.this.showDeleteDialog();
                }
            }
        });
        this.mDraftTrashButton.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.AbstractRecordingActivity.4
            boolean hasMovedOut = false;

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        if (AbstractRecordingActivity.this.mCurrentPage != AbstractRecordingActivity.this.mDraftFragments.size()) {
                            AbstractRecordingActivity.this.mDraftTrashBackground.setActivated(true);
                            this.hasMovedOut = false;
                        }
                        return false;
                    case 1:
                        AbstractRecordingActivity.this.mDraftTrashBackground.setActivated(false);
                        return false;
                    case 2:
                        float x = event.getX();
                        float y = event.getY();
                        boolean activate = x >= 0.0f && x <= ((float) AbstractRecordingActivity.this.mDraftTrashButton.getWidth()) && y >= 0.0f && y <= ((float) AbstractRecordingActivity.this.mDraftTrashButton.getHeight());
                        if (!activate) {
                            this.hasMovedOut = true;
                        }
                        AbstractRecordingActivity.this.mDraftTrashBackground.setActivated(activate && !this.hasMovedOut);
                        AbstractRecordingActivity.this.mDraftTrashButton.setActivated(activate && !this.hasMovedOut);
                        return false;
                    default:
                        return false;
                }
            }
        });
        this.mDragUpToDeleteView = (DragUpToDeleteContainer) findViewById(R.id.upDragger);
        this.mDragUpToDeleteView.setVisibility(8);
        this.mDragUpToDeleteView.setInteractionListner(this);
        this.mDraftTrashContainer.setVisibility(8);
        int draftProgDimen = resources.getDimensionPixelOffset(R.dimen.draft_progress_view_height);
        int progressDimen = resources.getDimensionPixelOffset(R.dimen.progress_view_height);
        this.mTopMaskHeight = progressDimen + draftProgDimen;
        this.mSideMaskWidth = (this.mScreenSize.x / 2) - (this.mPreviewDimen / 2);
        View pagerLeftMask = findViewById(R.id.pagerLeftMask);
        ViewGroup.LayoutParams params = pagerLeftMask.getLayoutParams();
        params.width = (int) (this.mPreviewRatio + (this.mScreenSize.x * ((1.0f - this.mPreviewRatio) / 2.0f)));
        pagerLeftMask.setLayoutParams(params);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("state_fp")) {
                this.mFirstPageSet = savedInstanceState.getBoolean("state_fp");
            }
            if (savedInstanceState.containsKey("first_launch")) {
                this.mFirstDraftLaunch = savedInstanceState.getBoolean("first_launch");
            } else {
                this.mFirstDraftLaunch = true;
            }
        } else {
            this.mFirstDraftLaunch = true;
        }
        int bottomRowDimen = resources.getDimensionPixelOffset(R.dimen.draft_dots_height);
        ViewGroup.LayoutParams viewPagerLayout = this.mViewPager.getLayoutParams();
        viewPagerLayout.height = this.mScreenSize.x + progressDimen + bottomRowDimen;
        this.mViewPager.setLayoutParams(viewPagerLayout);
        int dotsHeight = resources.getDimensionPixelOffset(R.dimen.draft_dots_height);
        this.mDots.setY(this.mScreenSize.x + progressDimen + (dotsHeight / 4));
        this.mLargeThumbnailOverlay = (ImageView) findViewById(R.id.large_thumbnail_overlay);
        this.mAnimationPreviewOverlay = findViewById(R.id.animation_preview_overlay);
        this.mCameraIconOverlay = findViewById(R.id.draft_camera_icon_overlay);
        this.mCameraIconOverlay.setVisibility(8);
        this.mCameraIconOverlayImage = findViewById(R.id.preview_icon);
        this.mDraftFullMask = findViewById(R.id.draft_full_mask);
        this.mSlowFadeIn = AnimationUtils.loadAnimation(this, R.anim.slow_fade_in);
        this.mCameraIconFadeIn = AnimationUtils.loadAnimation(this, R.anim.slow_fade_in);
        if (!this.mIsMessaging) {
            SharedPreferences prefs = getSharedPreferences("capture", 0);
            int launchCount = prefs.getInt("recorder_launch_count", 0) + 1;
            prefs.edit().putInt("recorder_launch_count", launchCount).apply();
        }
    }

    private BaseRecorderPluginManager getRecorderPluginManager() {
        RecordingFragment recordingFragment = getActiveRecordingFragment();
        if (recordingFragment != null) {
            return recordingFragment.getPluginManager();
        }
        return null;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        AppTrackingUtil.sendAppOpenedMessage(this);
        BaseRecorderPluginManager pm = getRecorderPluginManager();
        if (pm != null) {
            pm.onResume(this);
        }
        this.mIsResumed = true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        BaseRecorderPluginManager pm = getRecorderPluginManager();
        if (pm != null) {
            pm.onPause();
        }
        this.mIsResumed = false;
        if (this.mPromptDialog != null && this.mPromptDialog.isVisible()) {
            this.mPromptDialog.dismiss();
        }
        releasePlayers();
    }

    public Point getScreenSize() {
        return this.mScreenSize;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("state_fp", this.mFirstPageSet);
        outState.putBoolean("first_launch", this.mFirstDraftLaunch);
    }

    @Override // android.support.v4.view.DraftViewPager.OnPageChangeListener
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override // co.vine.android.widget.DragUpToDeleteContainer.DragUpListener
    public boolean prepareForPickup() {
        WeakReference<DraftFragment> fragment;
        if (!this.mAnimatingIntoDrafts && (fragment = this.mDraftFragments.get(this.mCurrentPage)) != null) {
            DraftFragment ref = fragment.get();
            ref.mCanUnhide = false;
            ref.pausePlayer();
            ref.showImage();
        }
        return !this.mAnimatingIntoDrafts;
    }

    @Override // co.vine.android.widget.DragUpToDeleteContainer.DragUpListener
    public void viewPickedUp() {
        hideCurrentFragment();
    }

    @Override // co.vine.android.widget.DragUpToDeleteContainer.DragUpListener
    public void viewDestroyFinished() {
        this.mDraftTrashButton.setActivated(false);
        this.mDraftTrashBackground.setActivated(false);
    }

    private void hideCurrentFragment() {
        DraftFragment ref;
        WeakReference<DraftFragment> fragment = this.mDraftFragments.get(this.mCurrentPage);
        if (fragment != null && (ref = fragment.get()) != null) {
            ref.pausePlayer();
            View toHide = ref.getView();
            if (toHide != null) {
                toHide.setVisibility(8);
            }
        }
    }

    public String makeSureUploadIsReady() throws InterruptedException {
        if (this.mAddToUploadThread != null) {
            try {
                this.mAddToUploadThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return this.mUploadFile;
    }

    @Override // co.vine.android.widget.DragUpToDeleteContainer.DragUpListener
    public boolean viewMoved(int viewTopY) {
        boolean lightTrash = viewTopY < 0;
        this.mDraftTrashBackground.setActivated(lightTrash);
        this.mDraftTrashButton.setActivated(viewTopY < this.mDraftTrashContainer.getHeight());
        return lightTrash;
    }

    @Override // co.vine.android.widget.DragUpToDeleteContainer.DragUpListener
    public boolean viewDropped(int viewTopY) {
        boolean delete = viewTopY <= 0;
        if (delete && this.mCurrentPage != this.mDraftFragments.size()) {
            this.mDeleteWasDrag = true;
            showDeleteDialog();
        } else {
            this.mDraftTrashButton.setActivated(false);
        }
        return delete;
    }

    @Override // co.vine.android.widget.DragUpToDeleteContainer.DragUpListener
    public void viewLanded() {
        showCurrentFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCurrentFragment() {
        DraftFragment ref;
        WeakReference<DraftFragment> fragment = this.mDraftFragments.get(this.mCurrentPage);
        if (fragment != null && (ref = fragment.get()) != null) {
            ref.mCanUnhide = true;
            ref.resumePlayer();
            View v = ref.getView();
            if (v != null) {
                v.setVisibility(0);
            }
        }
    }

    public PromptDialogFragment createDialogForPlugins(int dialogId) {
        if (((-16777216) & dialogId) != 0) {
            throw new IllegalArgumentException("Invalid dialog id: " + dialogId);
        }
        this.mPromptDialog = PromptDialogFragment.newInstance(ViewCompat.MEASURED_STATE_MASK + dialogId);
        return this.mPromptDialog;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        BaseRecorderPluginManager pm = getRecorderPluginManager();
        if (pm != null) {
            pm.onCreateOptionsMenu(menu);
            return true;
        }
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        BaseRecorderPluginManager pm = getRecorderPluginManager();
        if (pm == null || !pm.onOptionsItemSelected(item)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void showDialog(PromptDialogFragment dialog) {
        if (this.mPromptDialog != dialog) {
            this.mPromptDialog = dialog;
        }
        try {
            this.mPromptDialog.show(getFragmentManager());
        } catch (IllegalStateException e) {
        }
    }

    public static RegularVineRecorder.DeviceIssueStringGetter getDeviceIssueStringGetter(Context context) {
        return new DeviceIssueStringGetter(context);
    }

    private class SetSelectedRunnable implements Runnable {
        public final DraftFragment fragmentToSetSelectedFalse;
        public final DraftFragment fragmentToSetSelectedTrue;

        public SetSelectedRunnable(DraftFragment fragmentToSetSelectedFalse, DraftFragment fragmentToSetSelectedTrue) {
            this.fragmentToSetSelectedFalse = fragmentToSetSelectedFalse;
            this.fragmentToSetSelectedTrue = fragmentToSetSelectedTrue;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.fragmentToSetSelectedFalse != null) {
                this.fragmentToSetSelectedFalse.setSelected(false);
            }
            if (this.fragmentToSetSelectedTrue != null) {
                this.fragmentToSetSelectedTrue.setSelected(true);
            }
        }
    }

    @Override // android.support.v4.view.DraftViewPager.OnPageChangeListener
    public void onPageSelected(int i) {
        DraftFragment ref;
        View fragView;
        View toSet;
        WeakReference<DraftFragment> fragment;
        DraftFragment ref2;
        WeakReference<DraftFragment> fragment2;
        DraftFragment ref3;
        if (this.mSessions != null) {
            DraftFragment fragmentToSetSelectedFalse = null;
            DraftFragment fragmentToSetSelectedTrue = null;
            boolean isPageStateIdle = this.mPageScrollState == 0;
            int numDrafts = this.mSessions.size();
            if (i == -1) {
                if (this.mCurrentPage <= numDrafts && (fragment2 = this.mDraftFragments.get(this.mCurrentPage)) != null && (ref3 = fragment2.get()) != null) {
                    if (isPageStateIdle) {
                        ref3.setSelected(false);
                    } else {
                        fragmentToSetSelectedFalse = ref3;
                    }
                }
            } else {
                this.mDots.setActiveDot(i);
                if (i < numDrafts) {
                    if (this.mCurrentPage != -1 && this.mCurrentPage <= numDrafts && (fragment = this.mDraftFragments.get(this.mCurrentPage)) != null && (ref2 = fragment.get()) != null) {
                        if (isPageStateIdle) {
                            ref2.setSelected(false);
                        } else {
                            fragmentToSetSelectedFalse = ref2;
                        }
                    }
                    WeakReference<DraftFragment> fragment3 = this.mDraftFragments.get(i);
                    if (fragment3 != null && (ref = fragment3.get()) != null && (fragView = ref.getView()) != null && (toSet = fragView.findViewById(R.id.draft_container)) != null && this.mDragUpToDeleteView != null) {
                        this.mDragUpToDeleteView.setView(toSet, this.mViewPager.getLeftOfSelectedFragment());
                        if (!this.mAnimatingIntoDrafts || i < numDrafts) {
                            this.mDraftTrashContainer.setVisibility(0);
                            this.mDraftTrashContainer.animate().alpha(1.0f).start();
                        }
                        this.mHandler.removeCallbacks(this.mOnResumeDraftRunnable);
                        if (this.mPageScrollState == 0) {
                            ref.setSelected(true);
                        } else {
                            fragmentToSetSelectedTrue = ref;
                        }
                    }
                    this.mCurrentSession = this.mSessions.get(i);
                } else {
                    if (this.mCurrentPage != -1) {
                        WeakReference<DraftFragment> fragment4 = this.mDraftFragments.get(this.mCurrentPage);
                        if (fragment4 != null) {
                            DraftFragment prev = fragment4.get();
                            if (prev != null) {
                                if (isPageStateIdle) {
                                    prev.setSelected(false);
                                } else {
                                    fragmentToSetSelectedFalse = prev;
                                }
                            }
                        } else {
                            SLog.d("Fragment {} is null: size: {}.", Integer.valueOf(this.mCurrentPage), Integer.valueOf(this.mDraftFragments.size()));
                        }
                    }
                    if (this.mCameraFragment != null && this.mCameraFragment.get() != null) {
                        this.mHandler.removeCallbacks(this.mOnResumeDraftRunnable);
                    }
                    this.mCurrentSession = null;
                    this.mDragUpToDeleteView.setView(null, this.mViewPager.getLeftOfSelectedFragment());
                    this.mDraftTrashContainer.animate().alpha(0.0f).setListener(new SimpleAnimatorListener() { // from class: co.vine.android.AbstractRecordingActivity.5
                        @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator a) {
                            AbstractRecordingActivity.this.mDraftTrashContainer.setVisibility(8);
                            AbstractRecordingActivity.this.mDraftTrashContainer.animate().setListener(null);
                        }
                    }).start();
                }
                this.mCurrentPage = i;
            }
            if (fragmentToSetSelectedFalse != null || fragmentToSetSelectedTrue != null) {
                this.mOnPageIdleRunnable = new SetSelectedRunnable(fragmentToSetSelectedFalse, fragmentToSetSelectedTrue);
            }
        }
    }

    @Override // android.support.v4.view.DraftViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
        this.mPageScrollState = i;
        if (i == 0 && this.mOnPageIdleRunnable != null) {
            this.mOnPageIdleRunnable.run();
            this.mOnPageIdleRunnable = null;
        }
    }

    public void initMasks(View topMask, final View bottomMask) {
        topMask.setOnTouchListener(this.mOnMaskTouchListesner);
        bottomMask.setOnTouchListener(this.mOnMaskTouchListesner);
        SharedPreferences prefs = getSharedPreferences("capture", 0);
        int bottomMaskHeightPx = prefs.getInt(VineRecorder.getBottomMaskHeightPref(RecordConfigUtils.isDefaultFrontFacing()), 0);
        if (bottomMaskHeightPx > 0) {
            final RelativeLayout.LayoutParams bottomMaskParams = (RelativeLayout.LayoutParams) bottomMask.getLayoutParams();
            bottomMaskParams.height = bottomMaskHeightPx;
            runOnUiThread(new Runnable() { // from class: co.vine.android.AbstractRecordingActivity.6
                @Override // java.lang.Runnable
                public void run() {
                    bottomMask.setLayoutParams(bottomMaskParams);
                }
            });
        }
    }

    protected boolean currentlyHoldsRecordingFragment() {
        return this.mCurrentFragment != null && (this.mCurrentFragment instanceof RecordingFragment);
    }

    public void onRecorderBackPressed(View v) throws InterruptedException {
        try {
            onBackPressed();
        } catch (IllegalStateException e) {
        }
    }

    public void showUnSavedChangesToSessionDialog(PromptDialogFragment.OnDialogDoneListener listener) {
        RecordingFragment fragment = getActiveRecordingFragment();
        if (fragment != null) {
            this.mPromptDialog = PromptDialogFragment.newInstance(0);
            this.mPromptDialog.setMessage(fragment.isSavedSession() ? R.string.save_draft_old_confirm : R.string.save_draft_new_confirm);
            this.mPromptDialog.setPositiveButton(R.string.save);
            this.mPromptDialog.setNegativeButton(R.string.discard_changes);
            this.mPromptDialog.setListener(listener);
            try {
                this.mPromptDialog.show(getFragmentManager());
            } catch (IllegalStateException e) {
            }
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() throws InterruptedException {
        makeSureUploadIsReady();
        try {
            if (this.mStep == -1) {
                super.onBackPressed();
            } else {
                BaseRecorderPluginManager pm = getRecorderPluginManager();
                if (pm == null || !pm.onBackPressed()) {
                    if (isDraftsShowing()) {
                        hideDrafts(true);
                        releasePlayers();
                        if (this.mCurrentFragment instanceof RecordingFragment) {
                            ((RecordingFragment) this.mCurrentFragment).resumeFromDraft();
                        }
                    } else if (currentlyHoldsRecordingFragment()) {
                        RecordingFragment fragment = (RecordingFragment) this.mCurrentFragment;
                        boolean isEditing = fragment.isEditing();
                        if (!fragment.onBackPressed(isEditing)) {
                            if (!isEditing && !fragment.isSessionModified()) {
                                fragment.setDiscardChangesOnStop();
                                setResult(100);
                                fragment.playStopSound();
                                super.onBackPressed();
                            } else if (isEditing && !fragment.isEditingDirty()) {
                                fragment.playStopSound();
                                fragment.discardEditing();
                            } else if (!this.mIsMessaging) {
                                showUnSavedChangesToSessionDialog(this.mUnsavedChangesDialogDoneListener);
                            } else {
                                setResult(100);
                                super.onBackPressed();
                            }
                        }
                    } else if (this.mCurrentFragment instanceof RecordingPreviewFragment) {
                        ((RecordingPreviewFragment) this.mCurrentFragment).previewToRecord(this, false);
                    }
                }
            }
        } catch (IllegalStateException e) {
        }
    }

    public void onBackPressed(View v) throws InterruptedException {
        onBackPressed();
    }

    protected void discardUpload() {
        if (this.mUploadFile != null) {
            SLog.d("Upload discarded.");
            UploadManager.removeFromUploadQueue(this, this.mUploadFile);
        }
    }

    public void hideDrafts(final boolean animate) {
        CrashUtil.log("Fading away drafts.");
        AnimationSet as = new AnimationSet(true);
        if (animate) {
            as.addAnimation(ViewUtil.makeResizeAnimation(ViewUtil.ResizeAnimationType.COLLAPSE_HEIGHT, this.mDraftMaskTop, 0));
            as.addAnimation(ViewUtil.makeResizeAnimation(ViewUtil.ResizeAnimationType.COLLAPSE_WIDTH, this.mDraftMaskLeft, 0));
            as.addAnimation(ViewUtil.makeResizeAnimation(ViewUtil.ResizeAnimationType.COLLAPSE_WIDTH, this.mDraftMaskRight, 0));
        }
        as.addAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        as.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.AbstractRecordingActivity.9
            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                if (animate) {
                    AbstractRecordingActivity.this.mDraftMaskParent.setVisibility(0);
                } else {
                    AbstractRecordingActivity.this.mDraftMaskParent.setVisibility(8);
                    AbstractRecordingActivity.this.mDraftRoot.setVisibility(8);
                }
            }

            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                AbstractRecordingActivity.this.hideDraftsInternal();
            }
        });
        as.setDuration(300L);
        this.mDragUpToDeleteView.setView(null, this.mViewPager.getLeftOfSelectedFragment());
        this.mDraftRoot.startAnimation(as);
        this.mDraftTrashContainer.animate().cancel();
        this.mDraftTrashContainer.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideDraftsInternal() {
        this.mDraftRoot.setVisibility(8);
        this.mProgressOverlay.setVisibility(8);
        this.mPreviewOverlay.setVisibility(8);
        this.mProgressOverlay.setVisibility(8);
        this.mDragUpToDeleteView.setVisibility(8);
        if (this.mCurrentFragment instanceof RecordingFragment) {
            ((RecordingFragment) this.mCurrentFragment).getPluginManager().onHideDrafts();
        }
    }

    public boolean isDraftsShowing() {
        return (this.mDraftRoot == null || this.mDraftRoot.getVisibility() == 8) ? false : true;
    }

    public void showDrafts(String sessionId) throws Resources.NotFoundException {
        try {
            CrashUtil.log("Started showing drafts.");
            final boolean goToCamera = "camera_preview".equals(sessionId);
            this.mAnimatingIntoDrafts = true;
            this.mStartSessionId = sessionId;
            this.mFirstPageSet = false;
            reload(RecordSessionManager.getValidSessions(this, getVersion()));
            this.mDraftFullMask.setVisibility(0);
            this.mDraftMaskParent.setVisibility(0);
            this.mCameraIconOverlayImage.setVisibility(8);
            Resources r = getResources();
            int progressDimen = r.getDimensionPixelSize(R.dimen.progress_view_height);
            int draftProgressDimen = r.getDimensionPixelOffset(R.dimen.draft_progress_view_height);
            int topMaskHeight = this.mTopMaskHeight;
            if (goToCamera) {
                topMaskHeight += draftProgressDimen;
            }
            if (!goToCamera && (this.mCurrentFragment instanceof RecordingFragment)) {
                ImageView thumb = this.mLargeThumbnailOverlay;
                RelativeLayout.LayoutParams thumbnailParams = (RelativeLayout.LayoutParams) thumb.getLayoutParams();
                thumbnailParams.width = this.mScreenSize.x;
                thumbnailParams.height = this.mScreenSize.x;
                thumbnailParams.leftMargin = 0;
                thumbnailParams.topMargin = progressDimen;
                thumb.setLayoutParams(thumbnailParams);
                String path = ((RecordingFragment) this.mCurrentFragment).getThumbnailPath();
                BitmapDrawable drawable = new BitmapDrawable(getResources(), path);
                ViewUtil.setBackground(thumb, drawable);
                thumb.setVisibility(0);
            } else if (goToCamera) {
                this.mCameraIconOverlay.setVisibility(0);
                RelativeLayout.LayoutParams iconParams = (RelativeLayout.LayoutParams) this.mCameraIconOverlay.getLayoutParams();
                iconParams.width = this.mScreenSize.x;
                iconParams.height = this.mScreenSize.x;
                iconParams.topMargin = progressDimen;
                iconParams.leftMargin = 0;
                this.mCameraIconOverlay.setLayoutParams(iconParams);
            }
            this.mViewPager.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.AbstractRecordingActivity.10
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    if (goToCamera) {
                        AbstractRecordingActivity.this.mCameraIconOverlay.setVisibility(8);
                        return false;
                    }
                    AbstractRecordingActivity.this.mLargeThumbnailOverlay.setVisibility(8);
                    AbstractRecordingActivity.this.mAnimationPreviewOverlay.setVisibility(8);
                    return false;
                }
            });
            AnimationSet as = new AnimationSet(true);
            as.addAnimation(ViewUtil.makeResizeAnimation(ViewUtil.ResizeAnimationType.EXPAND_HEIGHT, this.mDraftMaskTop, topMaskHeight));
            as.addAnimation(ViewUtil.makeResizeAnimation(ViewUtil.ResizeAnimationType.EXPAND_WIDTH, this.mDraftMaskLeft, this.mSideMaskWidth));
            as.addAnimation(ViewUtil.makeResizeAnimation(ViewUtil.ResizeAnimationType.EXPAND_WIDTH, this.mDraftMaskRight, this.mSideMaskWidth));
            as.setAnimationListener(new AnonymousClass11(goToCamera));
            as.setDuration(300L);
            setCurrentSession();
            this.mDragUpToDeleteView.setVisibility(0);
            if (!goToCamera && (this.mCurrentFragment instanceof RecordingFragment)) {
                View progress = ((RecordingFragment) this.mCurrentFragment).getProgressView();
                if (progress != null) {
                    FloatingViewUtils.buildFloatingViewFor(this, progress, this.mAnimationPreviewOverlay, progress.getWidth(), 0, 0);
                }
                this.mAnimationPreviewOverlay.setVisibility(0);
            }
            if (goToCamera) {
                new MoveResizeAnimator(7, this.mCameraIconOverlay, this.mCameraIconOverlay, (int) ((this.mScreenSize.x * (1.0f - this.mPreviewRatio)) / 2.0f), progressDimen + (draftProgressDimen * 2), this.mPreviewRatio, this.mPreviewRatio, 300, this, null).start();
            } else {
                new MoveResizeAnimator(6, this.mAnimationPreviewOverlay, this.mAnimationPreviewOverlay, (int) ((this.mScreenSize.x * (1.0f - this.mPreviewRatio)) / 2.0f), progressDimen + draftProgressDimen, this.mPreviewRatio, 0.5d, 300, this, null).start();
                new MoveResizeAnimator(5, this.mLargeThumbnailOverlay, this.mLargeThumbnailOverlay, (int) ((this.mScreenSize.x * (1.0f - this.mPreviewRatio)) / 2.0f), progressDimen + (draftProgressDimen * 2), this.mPreviewRatio, this.mPreviewRatio, 300, this, null).start();
            }
            this.mDraftRoot.startAnimation(as);
        } catch (IOException e) {
            CrashUtil.logException(e, "Error refresh reloading drafts.", new Object[0]);
        } catch (Exception e2) {
            CrashUtil.logException(e2, "Error showing reloading drafts.", new Object[0]);
        }
    }

    /* renamed from: co.vine.android.AbstractRecordingActivity$11, reason: invalid class name */
    class AnonymousClass11 extends SimpleAnimationListener {
        final /* synthetic */ boolean val$goToCamera;

        AnonymousClass11(boolean z) {
            this.val$goToCamera = z;
        }

        @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            AbstractRecordingActivity.this.mHandler.removeCallbacks(AbstractRecordingActivity.this.mOnResumeDraftRunnable);
            AbstractRecordingActivity.this.mHandler.postDelayed(AbstractRecordingActivity.this.mOnResumeDraftRunnable, 300L);
        }

        @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            if (AbstractRecordingActivity.this.mCurrentPage != AbstractRecordingActivity.this.mDraftFragments.size()) {
                AbstractRecordingActivity.this.mDraftTrashContainer.setAlpha(0.0f);
                AbstractRecordingActivity.this.mDraftTrashContainer.setVisibility(0);
                if (!AbstractRecordingActivity.this.isResuming()) {
                    AbstractRecordingActivity.this.mDraftTrashContainer.animate().alpha(1.0f).start();
                }
            }
            AbstractRecordingActivity.this.mSlowFadeIn.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.AbstractRecordingActivity.11.1
                @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation2) {
                    if (AnonymousClass11.this.val$goToCamera) {
                        AbstractRecordingActivity.this.mCameraIconOverlayImage.setVisibility(0);
                        AbstractRecordingActivity.this.mCameraIconOverlayImage.setAlpha(0.0f);
                        AbstractRecordingActivity.this.mCameraIconOverlayImage.animate().alpha(1.0f).setDuration(AbstractRecordingActivity.this.mSlowFadeIn.getDuration()).setListener(new ViewGoneAnimationListener(AbstractRecordingActivity.this.mCameraIconOverlayImage)).start();
                        AbstractRecordingActivity.this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.AbstractRecordingActivity.11.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AbstractRecordingActivity.this.mCameraIconOverlay.setVisibility(8);
                            }
                        }, AbstractRecordingActivity.this.mCameraIconFadeIn.getDuration());
                    }
                }

                @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation2) {
                    AbstractRecordingActivity.this.mAnimatingIntoDrafts = false;
                    AbstractRecordingActivity.this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.AbstractRecordingActivity.11.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!AnonymousClass11.this.val$goToCamera) {
                                AbstractRecordingActivity.this.mLargeThumbnailOverlay.setVisibility(8);
                                AbstractRecordingActivity.this.mAnimationPreviewOverlay.setVisibility(8);
                            }
                        }
                    }, 300L);
                }
            });
            AbstractRecordingActivity.this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.AbstractRecordingActivity.11.2
                @Override // java.lang.Runnable
                public void run() {
                    AbstractRecordingActivity.this.mDraftFullMask.setVisibility(8);
                }
            }, 150L);
            AbstractRecordingActivity.this.mViewPager.setVisibility(0);
            AbstractRecordingActivity.this.mViewPager.startAnimation(AbstractRecordingActivity.this.mSlowFadeIn);
            AbstractRecordingActivity.this.mPageScrollState = 0;
            AbstractRecordingActivity.this.mDraftRoot.setVisibility(0);
            if (AbstractRecordingActivity.this.mCurrentFragment instanceof RecordingFragment) {
                ((RecordingFragment) AbstractRecordingActivity.this.mCurrentFragment).getPluginManager().onShowDrafts();
            }
            AbstractRecordingActivity.this.mDraftMaskParent.setVisibility(8);
            AbstractRecordingActivity.this.mDraftMaskTop.setVisibility(8);
            AbstractRecordingActivity.this.mDraftMaskLeft.setVisibility(8);
            AbstractRecordingActivity.this.mDraftMaskRight.setVisibility(8);
        }
    }

    public void toRecord(boolean isNew, boolean startWithEdit, RecordingFile fileToUse) throws InvalidStateException {
        this.mStep = 0;
        this.mFirstPageSet = false;
        onPageSelected(-1);
        boolean fromPreview = fileToUse != null;
        Object[] objArr = new Object[3];
        objArr[0] = Boolean.valueOf(isNew);
        objArr[1] = Boolean.valueOf(startWithEdit);
        objArr[2] = Boolean.valueOf(fileToUse != null);
        CrashUtil.log("To recording fragment: {} {} {}", objArr);
        if (fromPreview) {
            discardUpload();
        }
        RecordingFragment fragment = createRecordingFragment();
        Bundle args = new Bundle();
        args.putParcelable("arg_top_overlay", this.mTopOverlay);
        args.putParcelable("screen_size", this.mScreenSize);
        args.putBoolean("is_messaging", this.mIsMessaging);
        args.putString("recipient_username", this.mRecipientUsername);
        this.mColor = Util.getDefaultSharedPrefs(this).getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
        args.putInt("color", this.mColor);
        fragment.setArguments(args);
        fragment.setFileFileToUse(fileToUse);
        fragment.setStartWithEdit(startWithEdit);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isNew) {
            ft.add(R.id.fragment_container, fragment);
        } else {
            ft.replace(R.id.fragment_container, fragment);
        }
        try {
            ft.commit();
            this.mCurrentFragment = fragment;
            this.mRegularUiMode = getWindow().getDecorView().getSystemUiVisibility();
        } catch (IllegalStateException e) {
            throw new InvalidStateException(e);
        }
    }

    public void swapFolder(String tag, File folder) {
        if (this.mCurrentFragment instanceof RecordingFragment) {
            RecordingFragment rf = (RecordingFragment) this.mCurrentFragment;
            if (!rf.isResuming()) {
                rf.swapFolder(tag, folder);
            }
        }
    }

    public static class InvalidStateException extends Exception {
        public InvalidStateException(Exception e) {
            super(e);
        }
    }

    public void toPreview(String source, final RecordingFile finalFile, final String thumbNailPath, final MediaUtil.GenerateThumbnailsRunnable grabThumbnailRunnable) throws InvalidStateException {
        RecordingPreviewFragment fragment;
        CrashUtil.log("From {} to preview fragment: {} {}", source, finalFile, thumbNailPath);
        getWindow().getDecorView().setSystemUiVisibility(this.mRegularUiMode);
        try {
            String reference = finalFile.folder.getName();
            Cursor cursor = UploadManager.getReferenceCursor(this, reference);
            if (cursor != null && cursor.moveToFirst()) {
                CrashUtil.log("User have edited the vine already, discard until new one comes in.");
                startService(VineUploadService.getDiscardIntent(this, cursor.getString(1)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            CrashUtil.log("Failed to delete previous vines.");
        }
        if (BuildUtil.isIsHwEncodingEnabled()) {
            this.mAddToUploadThread = new Thread() { // from class: co.vine.android.AbstractRecordingActivity.12
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() throws IOException {
                    if (grabThumbnailRunnable != null) {
                        grabThumbnailRunnable.run();
                    }
                    if (AppController.getInstance(AbstractRecordingActivity.this.getApplicationContext()).isLoggedIn()) {
                        try {
                            AbstractRecordingActivity.this.mUploadFile = UploadManager.addToUploadQueue(AbstractRecordingActivity.this.getApplicationContext(), finalFile.version.name(), finalFile.getVideoPath(), thumbNailPath, finalFile.folder.getName(), finalFile.getSession().getMetaData().toString(), true, AbstractRecordingActivity.this.mConversationRowId, null);
                        } catch (Exception e2) {
                            throw new RuntimeException(e2);
                        }
                    }
                    AbstractRecordingActivity.this.mAddToUploadThread = null;
                }
            };
            this.mAddToUploadThread.start();
        } else if (AppController.getInstance(this).isLoggedIn()) {
            try {
                this.mUploadFile = UploadManager.addToUploadQueue(this, finalFile.version.name(), finalFile.getVideoPath(), thumbNailPath, finalFile.folder.getName(), finalFile.getSession().getMetaData().toString(), true, this.mConversationRowId, null);
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
        if (this.mCurrentFragment instanceof RecordingFragment) {
            this.mCurrentFragment.onPause();
        }
        if (BuildUtil.isIsHwEncodingEnabled()) {
            fragment = createRecordingPreviewFragmentHw(finalFile, thumbNailPath);
        } else {
            fragment = createRecordingPreviewFragmentSw(finalFile, thumbNailPath);
        }
        fragment.setFinalFile(finalFile);
        this.mStep = 1;
        this.mHasPreviewedAlready = true;
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            if (this.mCurrentFragment instanceof RecordingFragment) {
                ((RecordingFragment) this.mCurrentFragment).release();
            }
            this.mCurrentFragment = fragment;
        } catch (IllegalStateException e3) {
            throw new InvalidStateException(e3);
        }
    }

    public void onFinishPressed(View v) {
        if (this.mCurrentFragment instanceof RecordingFragment) {
            ((RecordingFragment) this.mCurrentFragment).onFinishPressed(v);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        BaseRecorderPluginManager pm = getRecorderPluginManager();
        if (pm != null) {
            pm.onStart(this);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        BaseRecorderPluginManager pm = getRecorderPluginManager();
        if (pm != null) {
            pm.onStop(this);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.mCurrentFragment instanceof RecordingPreviewFragment) {
            this.mCurrentFragment.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == 1 && currentlyHoldsRecordingFragment()) {
            this.mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == -1) {
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private RecordingFragment getActiveRecordingFragment() {
        if (!isDraftsShowing() && this.mStep == 0 && (this.mCurrentFragment instanceof RecordingFragment)) {
            return (RecordingFragment) this.mCurrentFragment;
        }
        return null;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        BaseRecorderPluginManager pluginManager;
        boolean actionCompleted = false;
        RecordingFragment recordingFragment = getActiveRecordingFragment();
        if (recordingFragment != null && (pluginManager = recordingFragment.getPluginManager()) != null) {
            actionCompleted = pluginManager.onKeyDown(keyCode, event);
        }
        if (actionCompleted || keyCode == 80) {
            return true;
        }
        if (recordingFragment != null && keyCode == 27) {
            this.mHasStartedRelativeTimeFromHardwareButton = true;
            recordingFragment.startRelativeTime();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        BaseRecorderPluginManager pluginManager;
        boolean actionCompleted = false;
        RecordingFragment recordingFragment = getActiveRecordingFragment();
        if (recordingFragment != null && (pluginManager = recordingFragment.getPluginManager()) != null) {
            actionCompleted = pluginManager.onKeyUp(keyCode, event);
        }
        if (actionCompleted || keyCode == 80) {
            return true;
        }
        if (recordingFragment != null && keyCode == 27 && this.mHasStartedRelativeTimeFromHardwareButton) {
            recordingFragment.endRelativeTime();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean hasPreviewedAlready() {
        return this.mHasPreviewedAlready;
    }

    public void deleteCurrentDraft() {
        int nextIndex;
        DraftFragment frag;
        try {
            if (this.mCurrentPage == 0) {
                nextIndex = 0;
            } else {
                nextIndex = this.mCurrentPage - 1;
            }
            this.mCurrentPage = -1;
            int current = this.mViewPager.getCurrentItem();
            WeakReference<DraftFragment> fragRef = this.mDraftFragments.get(current);
            if (fragRef != null && (frag = fragRef.get()) != null) {
                frag.setExpired(true);
            }
            if (this.mCurrentSession != null) {
                RecordSessionManager.deleteSession(this.mCurrentSession.folder, "deleteCurrentDraft");
            }
            FlurryUtils.trackAbandonedStage("draft");
            this.mStartSessionId = null;
            ArrayList<RecordSessionManager.RecordSessionInfo> sessions = RecordSessionManager.getValidSessions(this, getVersion());
            reload(sessions);
            this.mSessions = sessions;
            this.mViewPager.requestLayout();
            this.mViewPager.invalidate();
            this.mAdapter.notifyDataSetChanged();
            this.mViewPager.setCurrentItem(nextIndex, false);
            onPageSelected(nextIndex);
        } catch (IOException e) {
            SLog.e("Failed to delete current draft.", (Throwable) e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releasePlayers() {
        for (int i = 0; i < this.mDraftFragments.size(); i++) {
            WeakReference<DraftFragment> r = this.mDraftFragments.get(this.mDraftFragments.keyAt(i));
            DraftFragment ref = r.get();
            if (ref != null) {
                ref.setSelected(false);
                ref.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildAndShowFloatingView(DraftFragment ref) throws Resources.NotFoundException {
        releasePlayers();
        ref.showImage();
        int[] previewLocation = new int[2];
        View thumbnailView = ref.getThumbnailView();
        thumbnailView.getLocationOnScreen(previewLocation);
        int yOffset = getResources().getDimensionPixelOffset(R.dimen.draft_overlay_offset);
        FloatingViewUtils.buildFloatingViewFor(this, thumbnailView, this.mPreviewOverlay, thumbnailView.getWidth(), previewLocation[0], previewLocation[1] - yOffset);
        int[] progressLocation = new int[2];
        View progressView = ref.getProgressView();
        progressView.getLocationOnScreen(progressLocation);
        FloatingViewUtils.buildFloatingViewFor(this, progressView, this.mProgressOverlay, progressView.getWidth(), progressLocation[0], progressLocation[1] - yOffset);
        this.mPreviewOverlay.setVisibility(0);
        this.mProgressOverlay.setVisibility(0);
        this.mDraftTrashContainer.setVisibility(8);
        this.mViewPager.setVisibility(8);
    }

    private void reload(ArrayList<RecordSessionManager.RecordSessionInfo> sessions) {
        int size = sessions.size();
        if (size > 9) {
            this.mPromptDialog = PromptDialogFragment.newInstance(3);
            this.mPromptDialog.setMessage(R.string.too_many_drafts);
            this.mPromptDialog.setPositiveButton(R.string.ok);
            this.mPromptDialog.show(getFragmentManager());
            sessions.remove(size - 1);
        }
        this.mSessions = sessions;
        this.mAdapter = new DraftPagerImpl(getSupportFragmentManager());
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.setOnPageChangeListener(this);
        this.mViewPager.setOffscreenPageLimit(this.mAdapter.getCount() + 1);
        this.mDots.setNumberOfDots(sessions.size());
        this.mDots.invalidate();
    }

    @Override // co.vine.android.animation.SmoothAnimator.AnimationListener
    public void onAnimationFinish(int id, Object tag) throws Resources.NotFoundException {
        if (this.mIsResumed) {
            switch (id) {
                case 1:
                    Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
                    this.mDraftRoot.startAnimation(fadeOut);
                    hideDrafts(false);
                    swapFolder("PreviewAnimationFinish", (File) tag);
                    this.mIsGoingToRecord = false;
                    break;
                case 3:
                    deleteCurrentDraft();
                    this.mPreviewOverlay.setVisibility(8);
                    break;
                case 4:
                    hideDrafts(false);
                    swapFolder("CameraAnimationFinish", null);
                    this.mIsGoingToRecord = false;
                    break;
            }
        }
    }

    public synchronized void addPlayerToPool(SdkVideoView videoView) {
        this.mVideoViews.add(videoView);
    }

    public synchronized void releaseOtherPlayers(SdkVideoView videoView) {
        Iterator<SdkVideoView> it = this.mVideoViews.iterator();
        while (it.hasNext()) {
            SdkVideoView v = it.next();
            if (v != videoView) {
                v.suspend();
            }
        }
        this.mVideoViews.clear();
    }

    private class DraftPagerImpl extends DraftFragmentStatePagerAdapter {
        public DraftPagerImpl(FragmentManager fm) {
            super(fm);
        }

        @Override // android.support.v4.view.DraftPagerAdapter
        public int getCount() {
            return AbstractRecordingActivity.this.mSessions.size() + 1;
        }

        @Override // android.support.v4.app.DraftFragmentStatePagerAdapter
        public Fragment getItem(int i) {
            if (i != AbstractRecordingActivity.this.mSessions.size()) {
                RecordSessionManager.RecordSessionInfo session = (RecordSessionManager.RecordSessionInfo) AbstractRecordingActivity.this.mSessions.get(i);
                final DraftFragment fragment = new DraftFragment();
                fragment.setArguments(i == 0 && !AbstractRecordingActivity.this.mFirstPageSet, session.video.getPath(), session.thumb.getPath(), session.folder, session.meta.getProgress(), AbstractRecordingActivity.this.mPreviewDimen);
                fragment.setListener(new View.OnClickListener() { // from class: co.vine.android.AbstractRecordingActivity.DraftPagerImpl.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) throws Resources.NotFoundException {
                        if (v.getId() == R.id.videoViewContainer && !AbstractRecordingActivity.this.mAnimatingIntoDrafts && !AbstractRecordingActivity.this.isResuming()) {
                            CrashUtil.log("User clicked on fragment to resume.");
                            AbstractRecordingActivity.this.mIsGoingToRecord = true;
                            AbstractRecordingActivity.this.buildAndShowFloatingView(fragment);
                            AbstractRecordingActivity.this.releasePlayers();
                            float fullSize = AbstractRecordingActivity.this.mScreenSize.x;
                            float ratio = fullSize / AbstractRecordingActivity.this.mPreviewOverlay.getLayoutParams().width;
                            new MoveResizeAnimator(2, AbstractRecordingActivity.this.mProgressOverlay, 0, ratio, 2.0d, 300, null, v.getTag()).start();
                            new MoveResizeAnimator(1, AbstractRecordingActivity.this.mPreviewOverlay, AbstractRecordingActivity.this.mProgressOverlay.getLayoutParams().height * 2, ratio, ratio, 300, AbstractRecordingActivity.this, v.getTag()).start();
                        }
                    }
                });
                AbstractRecordingActivity.this.mDraftFragments.put(i, new WeakReference(fragment));
                return fragment;
            }
            DraftCameraPreviewFragment fragment2 = new DraftCameraPreviewFragment();
            fragment2.setDimen(AbstractRecordingActivity.this.mPreviewDimen);
            AbstractRecordingActivity.this.mCameraFragment = new WeakReference(fragment2);
            return fragment2;
        }

        @Override // android.support.v4.view.DraftPagerAdapter
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                super.destroyItem(container, position, object);
                if (object instanceof DraftFragment) {
                    FragmentManager m = AbstractRecordingActivity.this.getSupportFragmentManager();
                    FragmentTransaction ft = m.beginTransaction();
                    ft.remove((DraftFragment) object);
                    ft.commit();
                }
            } catch (IllegalStateException e) {
                CrashUtil.log("Failed to remove draft, it's probably not there any more.");
            }
        }

        @Override // android.support.v4.view.DraftPagerAdapter
        public int getItemPosition(Object object) {
            if (object instanceof DraftFragment) {
                DraftFragment frag = (DraftFragment) object;
                if (frag.isExpired()) {
                    return -2;
                }
                return -1;
            }
            return super.getItemPosition(object);
        }

        @Override // android.support.v4.view.DraftPagerAdapter
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            if (AbstractRecordingActivity.this.mFirstDraftLaunch) {
                AbstractRecordingActivity.this.mFirstDraftLaunch = !AbstractRecordingActivity.this.setCurrentSession();
            }
        }
    }

    public void showDeleteDialog() {
        PromptDialogFragment p = PromptDialogFragment.newInstance(1);
        p.setListener(this.mDeleteDialogDoneListener);
        p.setMessage(R.string.delete_draft_confirm);
        p.setPositiveButton(R.string.delete_yes);
        p.setNegativeButton(R.string.cancel);
        p.show(getFragmentManager());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setCurrentSession() {
        DraftFragment ref;
        int i = 0;
        if (this.mViewPager.isDrawn() && !this.mFirstPageSet) {
            if (TextUtils.isEmpty(this.mStartSessionId) || "camera_preview".equals(this.mStartSessionId)) {
                this.mViewPager.setCurrentItem(this.mAdapter.getCount() - 1, false);
                this.mFirstPageSet = true;
                return true;
            }
            Iterator<RecordSessionManager.RecordSessionInfo> it = this.mSessions.iterator();
            while (it.hasNext()) {
                RecordSessionManager.RecordSessionInfo session = it.next();
                if (this.mStartSessionId.equals(session.folder.getName())) {
                    this.mViewPager.setCurrentItem(i, false);
                    this.mCurrentPage = i;
                    this.mFirstPageSet = true;
                    WeakReference<DraftFragment> item = this.mDraftFragments.get(this.mCurrentPage);
                    View toSet = null;
                    if (item != null && (ref = item.get()) != null) {
                        toSet = ref.getView().findViewById(R.id.draft_container);
                    }
                    this.mDragUpToDeleteView.setView(toSet, this.mViewPager.getLeftOfSelectedFragment());
                    return true;
                }
                i++;
            }
        }
        return false;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.mHelper.unBindCameraService(this);
        BaseRecorderPluginManager pm = getRecorderPluginManager();
        if (pm != null) {
            pm.onDestroy();
        }
        try {
            this.mIntentionalObjectCounter.release();
            int count = this.mIntentionalObjectCounter.getCount();
            if (count == 0) {
                CrashUtil.log("Clean up folders because we are the last one.");
                getVersion().getManager(this).cleanUnusedFolders();
            } else if (count > 1) {
                CrashUtil.logException(new VineLoggingException("Double instance violation, but it's ok."));
            }
        } catch (IOException e) {
            CrashUtil.logException(e);
        }
        this.mCurrentFragment = null;
        CrashUtil.log("[mem] AbstractRecordingActivity {} Destroyed.", this);
        FlurryUtils.trackRecordingDestroy();
        View root = findViewById(R.id.recording_root);
        try {
            unbindDrawables(root);
        } catch (Exception e2) {
            SLog.e("Failed to remove all drawables, but FINE.");
        }
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDelete() throws Resources.NotFoundException {
        DraftFragment nextFragment;
        boolean noAnimationDelete = false;
        WeakReference<DraftFragment> currentFragmentRef = this.mDraftFragments.get(this.mCurrentPage);
        if (currentFragmentRef != null) {
            DraftFragment currentFragment = currentFragmentRef.get();
            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.quick_fade_out);
            final int offset = getResources().getDimensionPixelOffset(R.dimen.draft_overlay_offset);
            final int margin = TypedValue.complexToDimensionPixelOffset(6, getResources().getDisplayMetrics());
            if (this.mCurrentPage > 0) {
                final DraftFragment prevFragment = this.mDraftFragments.get(this.mCurrentPage - 1).get();
                if (prevFragment != null && currentFragment != null) {
                    final View prevView = prevFragment.getThumbnailView();
                    final int[] previewLocation = new int[2];
                    prevView.getLocationOnScreen(previewLocation);
                    SimpleAnimationListener l = new SimpleAnimationListener() { // from class: co.vine.android.AbstractRecordingActivity.14
                        @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
                        public void onAnimationEnd(Animation animation) {
                            FloatingViewUtils.buildFloatingViewFor(AbstractRecordingActivity.this, prevView, AbstractRecordingActivity.this.mPreviewOverlay, prevView.getWidth(), previewLocation[0], previewLocation[1] - offset);
                            AbstractRecordingActivity.this.mPreviewOverlay.setVisibility(0);
                            new MoveResizeAnimator(3, AbstractRecordingActivity.this.mPreviewOverlay, AbstractRecordingActivity.this.mPreviewOverlay, ((int) (AbstractRecordingActivity.this.mScreenSize.x * (1.0f - AbstractRecordingActivity.this.mPreviewRatio))) - (margin * 5), previewLocation[1] - offset, 1.0d, 1.0d, 300, AbstractRecordingActivity.this, prevFragment.getView()).start();
                        }
                    };
                    fadeOut.setAnimationListener(l);
                    View view = prevFragment.getView();
                    if (view != null) {
                        view.setVisibility(4);
                    }
                    View view2 = currentFragment.getView();
                    if (view2 != null && !this.mDeleteWasDrag) {
                        view2.startAnimation(fadeOut);
                    } else if (view2 != null) {
                        l.onAnimationEnd(fadeOut);
                    }
                } else {
                    noAnimationDelete = true;
                }
            } else if (this.mCurrentPage == 0) {
                if (this.mSessions.size() == 1) {
                    deleteCurrentDraft();
                    hideDrafts(true);
                    swapFolder("Delete", null);
                } else {
                    WeakReference<DraftFragment> nextFragmentRef = this.mDraftFragments.get(this.mCurrentPage + 1);
                    if (nextFragmentRef != null && (nextFragment = nextFragmentRef.get()) != null && currentFragment != null) {
                        View nextView = nextFragment.getThumbnailView();
                        int[] nextLocation = new int[2];
                        nextView.getLocationOnScreen(nextLocation);
                        try {
                            FloatingViewUtils.buildFloatingViewFor(this, nextView, this.mPreviewOverlay, nextView.getWidth(), nextLocation[0], nextLocation[1] - offset);
                        } catch (Exception e) {
                            CrashUtil.logException(e);
                        }
                        this.mPreviewOverlay.setVisibility(0);
                        final MoveResizeAnimator animator = new MoveResizeAnimator(3, this.mPreviewOverlay, this.mPreviewOverlay, ((int) (this.mScreenSize.x * (1.0f - this.mPreviewRatio))) - (margin * 4), nextLocation[1] - offset, 1.0d, 1.0d, 300, this, nextFragment.getView());
                        fadeOut.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.AbstractRecordingActivity.15
                            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
                            public void onAnimationEnd(Animation animation) {
                                animator.start();
                            }
                        });
                        View view3 = nextFragment.getView();
                        if (view3 != null) {
                            view3.setVisibility(4);
                        }
                        View view4 = currentFragment.getView();
                        if (view4 != null && !this.mDeleteWasDrag) {
                            view4.startAnimation(fadeOut);
                        } else {
                            animator.start();
                        }
                    } else {
                        noAnimationDelete = true;
                    }
                }
            }
        } else {
            noAnimationDelete = true;
        }
        if (noAnimationDelete) {
            deleteCurrentDraft();
        }
    }

    public void cameraPreviewToRecorder(View iconView, View previewIcon) throws Resources.NotFoundException {
        if (!isResuming()) {
            this.mIsGoingToRecord = true;
            float fullSize = this.mScreenSize.x;
            float ratio = fullSize / iconView.getLayoutParams().width;
            int top = getResources().getDimensionPixelOffset(R.dimen.progress_view_height);
            int topOffset = getResources().getDimensionPixelOffset(R.dimen.draft_overlay_offset);
            int[] location = new int[2];
            iconView.getLocationInWindow(location);
            previewIcon.setVisibility(8);
            FloatingViewUtils.buildFloatingViewFor(this, iconView, this.mPreviewOverlay, iconView.getWidth(), location[0], location[1] - topOffset);
            this.mPreviewOverlay.setVisibility(0);
            new MoveResizeAnimator(4, this.mPreviewOverlay, this.mPreviewOverlay, 0, top, ratio, ratio, 300, this, iconView).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isResuming() {
        if (this.mCurrentFragment instanceof RecordingFragment) {
            return this.mIsGoingToRecord || ((RecordingFragment) this.mCurrentFragment).isResuming();
        }
        return this.mIsGoingToRecord;
    }

    protected RecordingPreviewFragment createRecordingPreviewFragmentSw(RecordingFile finalFile, String thumbNailPath) {
        return RecordingPreviewFragment.newInstance(finalFile.getVideoPath(), this.mUploadFile, thumbNailPath, this.mIsMessaging, this.mConversationRowId, this.mDirectUserId, isFromSony(), this.mColor, this.mRecipientUsername, null);
    }

    protected RecordingPreviewFragment createRecordingPreviewFragmentHw(RecordingFile finalFile, String thumbNailPath) {
        return RecordingPreviewFragment.newInstance(finalFile.getVideoPath(), null, thumbNailPath, this.mIsMessaging, this.mConversationRowId, this.mDirectUserId, isFromSony(), this.mColor, this.mRecipientUsername, null);
    }

    public ArrayList<RecorderPlugin<?, VineRecorder>> getPluginList() {
        ArrayList<RecorderPlugin<?, VineRecorder>> list = new ArrayList<>();
        if (this.mIsMessaging) {
            list.add(new CameraSwitcherPlugin());
            list.add(new GridPlugin());
            list.add(new FocusPlugin());
            list.add(new GhostPlugin());
        } else {
            list.add(new SecretModePlugin());
            list.add(new IndividualEditPlugin());
            list.add(new ImportPlugin());
            list.add(new DeleteLastSegmentPlugin());
            list.add(MoreToolPlugin.newInstance());
            list.add(new CameraSwitcherPlugin());
            list.add(new DraftPlugin());
            list.add(new ImportOnboardPlugin());
            if (RecordConfigUtils.RecordConfig.SHOULD_SHOW_ZOOM_SLIDER) {
                list.add(new ZoomPlugin());
            }
            if (SLog.sLogsOn) {
                list.add(new TimeLapsePlugin());
            }
        }
        return list;
    }

    @Override // co.vine.android.recordingui.RecordStateHolder
    public PostShareParameters getPostShareParameters() {
        return this.mPostShareParameters;
    }

    @Override // co.vine.android.recordingui.RecordStateHolder
    public void setPostShareParameters(PostShareParameters params) {
        this.mPostShareParameters = params;
    }

    public boolean isFromSony() {
        return false;
    }

    protected RecordingFragment createRecordingFragment() {
        return new RecordingFragment();
    }
}
