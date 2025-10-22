package co.vine.android;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.BuildConfig;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.plugin.BaseRecorderPluginManager;
import co.vine.android.recorder.InlineVineRecorder;
import co.vine.android.recorder.ProgressView;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordSession;
import co.vine.android.recorder.RecordSessionManager;
import co.vine.android.recorder.RecordSessionVersion;
import co.vine.android.recorder.RecordingFile;
import co.vine.android.recorder.ViewGoneAnimationListener;
import co.vine.android.recorder.camera.PreviewManager;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.SystemUtil;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public class InlineRecorderManager extends RelativeLayout implements InlineVineRecorder.ProcessingListener {
    private View mCameraView;
    private boolean mCanInline;
    private boolean mDownWasInRecordButton;
    private ProgressView mFinishProgressView;
    private FinishRunnable mFinishRunnable;
    private ConversationFragment mFragment;
    private Handler mHandler;
    private boolean mIsWaitingForResumeThread;
    private boolean mProcessing;
    private InlineVineRecorder mRecorder;
    private View mRecordingButtonContainer;
    private View mRecordingContainer;
    private ResumeThread mResumeThread;
    private Runnable mStartInlineRecording;
    private TextView mSwipeUpText;
    private View mTextToggle;
    private View mTopMask;
    private RecordSessionVersion mVersion;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public InlineRecorderManager(Context context) {
        super(context);
        this.mStartInlineRecording = new StartInlineRecording();
        this.mFinishRunnable = new FinishRunnable();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public InlineRecorderManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mStartInlineRecording = new StartInlineRecording();
        this.mFinishRunnable = new FinishRunnable();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public InlineRecorderManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mStartInlineRecording = new StartInlineRecording();
        this.mFinishRunnable = new FinishRunnable();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mRecordingButtonContainer = findViewById(R.id.record_button_container);
        this.mRecordingContainer = findViewById(R.id.inline_record_root);
        this.mFinishProgressView = (ProgressView) findViewById(R.id.finishing_progress);
        this.mTopMask = findViewById(R.id.top_mask);
        this.mCameraView = findViewById(R.id.cameraView);
    }

    public void initDisabled(ConversationFragment fragment) {
        this.mFragment = fragment;
    }

    public void init(ConversationFragment fragment, ConversationActivity activity, RecordSessionVersion version) {
        this.mFragment = fragment;
        this.mCanInline = RecordConfigUtils.isCapableOfInline(activity);
        View root = this.mFragment.getView();
        this.mTextToggle = root.findViewById(R.id.text_input_toggle);
        this.mSwipeUpText = (TextView) root.findViewById(R.id.swipe_up);
        this.mVersion = version;
        if (this.mCanInline) {
            this.mRecorder = new InlineVineRecorder(version, new BaseRecorderPluginManager(), SystemUtil.getDisplaySize(activity), activity, this, activity.getResources().getDimensionPixelSize(R.dimen.vm_record_margin), R.id.progress_inline, R.id.finishing_progress, R.id.cameraView, R.id.top_mask, R.id.bottom_mask, R.string.opening_camera, R.id.layout_root, activity.getResources().getDrawable(R.drawable.progress_horizontal), AbstractRecordingActivity.getDeviceIssueStringGetter(activity), this.mFragment.getText(R.string.getting_ready), this.mFragment.getText(R.string.finish_processing_1), this.mFragment.getText(R.string.finish_processing_2), this.mFragment.getText(R.string.finish_processing_3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RecordingFile getNewMessageRecordingFile() {
        try {
            RecordSessionManager rsm = this.mVersion.getManager(this.mFragment.getActivity());
            File folder = rsm.createFolderForSession();
            return new RecordingFile(folder, RecordSession.newSession(new RecordConfigUtils.RecordConfig(this.mFragment.getActivity()), this.mVersion), false, false, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        this.mHandler = getHandler();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isInRecordButton(event);
    }

    private boolean isInRecordButton(MotionEvent event) {
        if (this.mFragment == null || this.mFragment.isInTextInputMode()) {
            return false;
        }
        ViewGroup buttonParent = (ViewGroup) this.mRecordingButtonContainer.getParent();
        Rect hitRect = new Rect();
        this.mRecordingButtonContainer.getHitRect(hitRect);
        hitRect.offset(buttonParent.getLeft(), buttonParent.getTop());
        return hitRect.contains((int) event.getX(), (int) event.getY());
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) throws InterruptedException, RemoteException {
        switch (event.getAction()) {
            case 0:
                if (!this.mProcessing && isInRecordButton(event) && this.mRecordingContainer.getVisibility() != 0) {
                    this.mDownWasInRecordButton = true;
                    if (this.mRecorder == null) {
                        return true;
                    }
                    this.mSwipeUpText.setTextColor(getResources().getColor(R.color.solid_light_gray_vm));
                    this.mRecorder.setStarted(true);
                    waitForResume();
                    this.mRecorder.openCamera();
                    this.mHandler.postDelayed(this.mStartInlineRecording, 200L);
                    return true;
                }
                if (this.mRecordingContainer.getVisibility() != 0) {
                    return false;
                }
                this.mDownWasInRecordButton = false;
                this.mRecorder.setStarted(false);
                return true;
            case 1:
                if (this.mDownWasInRecordButton && this.mRecorder == null) {
                    this.mDownWasInRecordButton = false;
                    this.mFragment.launchFullRecord();
                    return true;
                }
                waitForResume();
                this.mHandler.removeCallbacks(this.mStartInlineRecording);
                if (this.mDownWasInRecordButton && event.getEventTime() - event.getDownTime() <= 200 && this.mRecordingContainer.getVisibility() != 0) {
                    this.mDownWasInRecordButton = false;
                    this.mRecorder.setStarted(false);
                    this.mFragment.launchFullRecord();
                    return true;
                }
                if (this.mRecordingContainer.getVisibility() == 0 && this.mDownWasInRecordButton && !this.mRecorder.isFinished()) {
                    if (isInCancelZone(event.getY())) {
                        if (this.mRecorder.isRecordingSegment()) {
                            this.mRecorder.stopRecording(true);
                        } else {
                            this.mRecorder.cancelStartCallback();
                        }
                        this.mRecorder.deleteCurrentDraftFolder("inline action up.");
                        this.mRecorder.swapSession("Inline record", getNewMessageRecordingFile());
                        toggleRecorder(false);
                        this.mTextToggle.setEnabled(true);
                        this.mFinishProgressView.animate().alpha(0.0f).setListener(new ViewGoneAnimationListener(this.mFinishProgressView)).start();
                    } else if (this.mRecorder.isRecordingSegment()) {
                        this.mRecorder.stopRecording(false);
                        this.mRecorder.onFinishPressed();
                    } else {
                        this.mRecorder.stopRecording(true);
                        if (this.mRecorder.hasSessionFile()) {
                            this.mRecorder.deleteCurrentDraftFolder("inline action up.");
                        }
                        this.mRecorder.swapSession("Inline record", getNewMessageRecordingFile());
                        toggleRecorder(false);
                        this.mTextToggle.setEnabled(true);
                        this.mFinishProgressView.animate().alpha(0.0f).setListener(new ViewGoneAnimationListener(this.mFinishProgressView)).start();
                    }
                    this.mTextToggle.setVisibility(0);
                    return true;
                }
                break;
            case 2:
                if (this.mRecorder == null) {
                    return this.mDownWasInRecordButton;
                }
                if (isInCancelZone(event.getY())) {
                    this.mSwipeUpText.setTextColor(this.mFragment.getMyColor());
                } else {
                    this.mSwipeUpText.setTextColor(getResources().getColor(R.color.solid_light_gray_vm));
                }
                return this.mDownWasInRecordButton;
        }
        return false;
    }

    private boolean isInCancelZone(float y) {
        ViewGroup buttonParent = (ViewGroup) this.mRecordingButtonContainer.getParent();
        return y < ((float) (getMeasuredHeight() - (buttonParent.getHeight() * 2)));
    }

    @Override // co.vine.android.recorder.InlineVineRecorder.ProcessingListener
    public void onProcessingStart() {
        toggleRecorder(false);
        this.mProcessing = true;
        this.mRecordingButtonContainer.animate().alpha(0.0f).setListener(new ViewGoneAnimationListener(this.mRecordingButtonContainer));
    }

    @Override // co.vine.android.recorder.InlineVineRecorder.ProcessingListener
    public void onProcessingFinish() {
        this.mProcessing = false;
        this.mRecordingButtonContainer.setAlpha(0.0f);
        this.mRecordingButtonContainer.setVisibility(0);
        this.mRecordingButtonContainer.animate().alpha(1.0f).start();
    }

    private class StartInlineRecording implements Runnable {
        private StartInlineRecording() {
        }

        @Override // java.lang.Runnable
        public void run() {
            InlineRecorderManager.this.toggleRecorder(true);
            InlineRecorderManager.this.mFinishProgressView.setVisibility(0);
            InlineRecorderManager.this.mFinishProgressView.setAlpha(1.0f);
            InlineRecorderManager.this.mFinishProgressView.setProgressRatio(0.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleRecorder(boolean show) {
        if (this.mCanInline) {
            if (show) {
                this.mCameraView.setVisibility(0);
                InlineVineRecorder.startnanos = System.nanoTime();
                this.mRecordingContainer.setAlpha(0.01f);
                this.mRecordingContainer.setVisibility(0);
                this.mRecordingContainer.animate().alpha(1.0f).setDuration(250L).start();
                this.mTopMask.setVisibility(0);
                this.mTextToggle.setVisibility(8);
                this.mTextToggle.setEnabled(false);
                this.mRecorder.setDelayDialog(true);
                this.mRecorder.onResume("start");
                this.mFragment.onRecorderShown();
                return;
            }
            this.mRecordingContainer.animate().alpha(0.0f).setListener(new ViewGoneAnimationListener(this.mRecordingContainer)).start();
            this.mFragment.onRecorderHidden();
            this.mRecordingButtonContainer.setVisibility(0);
        }
    }

    private void waitForResume() {
        if (this.mIsWaitingForResumeThread) {
            try {
                this.mResumeThread.join();
            } catch (InterruptedException e) {
                SLog.e("You can't interrupt this.");
            }
        }
    }

    private class FinishRunnable implements Runnable {
        private FinishRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            InlineRecorderManager.this.mRecordingButtonContainer.setVisibility(0);
            InlineRecorderManager.this.mFragment.prepareUpload();
            InlineRecorderManager.this.mRecorder.deleteCurrentDraftFolder("inline finish.");
            InlineRecorderManager.this.mRecorder.swapSession("Inline record", InlineRecorderManager.this.getNewMessageRecordingFile());
            InlineRecorderManager.this.mTextToggle.setVisibility(0);
            InlineRecorderManager.this.mTextToggle.setEnabled(true);
            InlineRecorderManager.this.mFinishProgressView.animate().alpha(0.0f).setListener(new ViewGoneAnimationListener(InlineRecorderManager.this.mFinishProgressView)).start();
        }
    }

    InlineVineRecorder getRecorder() {
        return this.mRecorder;
    }

    public void onResume() throws IOException {
        if (this.mCanInline) {
            try {
                this.mResumeThread = new ResumeThread();
                this.mRecorder.setCameraView(this.mCameraView);
                this.mRecorder.onUiResumed(this.mFragment.getActivity(), this.mFinishRunnable, true);
                this.mIsWaitingForResumeThread = true;
                this.mResumeThread.start();
            } catch (IOException e) {
                this.mCanInline = false;
                this.mRecorder.release();
                this.mRecorder = null;
                throw e;
            }
        }
    }

    private class ResumeThread extends Thread {
        private RecordingFile mFileToResume;

        public ResumeThread() throws IOException {
            try {
                this.mFileToResume = InlineRecorderManager.this.getNewMessageRecordingFile();
            } catch (RuntimeException e) {
                throw new IOException("Failed to create new recording file.");
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws InterruptedException {
            try {
                Thread.sleep(1500L);
            } catch (InterruptedException e) {
                SLog.e("Failed to sleep wait.");
            }
            long start = System.currentTimeMillis();
            SLog.d("Start resuming for inline.");
            InlineRecorderManager.this.mRecorder.initPreviewSurface();
            InlineRecorderManager.this.mRecorder.swapSession("Inline record", this.mFileToResume);
            this.mFileToResume = null;
            InlineRecorderManager.this.mRecorder.start("Inline record", false, false);
            InlineRecorderManager.this.mIsWaitingForResumeThread = false;
            CrashUtil.log("Inline resume took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
        }
    }

    public void onPaused() {
        PreviewManager.getInstance().cancelPendingPreviews();
        if (this.mCanInline) {
            waitForResume();
            if (this.mRecorder != null) {
                this.mRecorder.setStarted(false);
                this.mRecorder.onUiPaused();
            }
        }
    }

    public void release() {
        if (this.mCanInline) {
            waitForResume();
            this.mRecorder.stopAndDiscardChanges(BuildConfig.BUILD_TYPE, null, true);
            this.mRecorder.release();
        }
    }
}
