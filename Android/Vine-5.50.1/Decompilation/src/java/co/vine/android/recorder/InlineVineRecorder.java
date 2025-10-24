package co.vine.android.recorder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RelativeLayout;
import co.vine.android.plugin.BaseRecorderPluginManager;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.RegularVineRecorder;
import co.vine.android.recorder.camera.CameraSetting;
import com.edisonwang.android.slog.SLog;

@TargetApi(14)
/* loaded from: classes.dex */
public class InlineVineRecorder extends RegularVineRecorder {
    private static long DELAY_WAIT_FOR_EXPOSURE = 350;
    public static long startnanos = 0;
    private boolean mCameraSuccessfullyOpened;
    private ProgressView mFinishProgressView;
    private final int mFinishProgressViewResourceId;
    private final int mPaddingSize;
    private ProcessingListener mProcessingListener;
    private Runnable mStartRelativeTimeRunnable;
    private boolean mStarted;
    private boolean mStopping;

    public interface ProcessingListener {
        void onProcessingFinish();

        void onProcessingStart();
    }

    public void setStarted(boolean started) {
        this.mStarted = started;
    }

    public void cancelStartCallback() {
        this.mHandler.removeCallbacks(this.mStartRelativeTimeRunnable);
    }

    public InlineVineRecorder(RecordSessionVersion version, BaseRecorderPluginManager manager, Point screenSize, Activity activity, ProcessingListener listener, int recorderPadding, int progressViewResourceId, int finishProgressViewResourceId, int cameraViewResourceId, int topMaskId, int bottomMaskId, int openCameraString, int rootLayoutId, Drawable progressDrawable, RegularVineRecorder.DeviceIssueStringGetter deviceNotSupportedString, CharSequence... messages) {
        super(66, manager, screenSize, activity, progressViewResourceId, cameraViewResourceId, topMaskId, bottomMaskId, openCameraString, rootLayoutId, progressDrawable, deviceNotSupportedString, true, version, null, messages);
        this.mCameraSuccessfullyOpened = false;
        this.mStopping = false;
        this.mStartRelativeTimeRunnable = new Runnable() { // from class: co.vine.android.recorder.InlineVineRecorder.1
            @Override // java.lang.Runnable
            public void run() {
                InlineVineRecorder.this.startRelativeTime();
            }
        };
        this.mProcessingListener = listener;
        this.mPaddingSize = recorderPadding;
        this.mFinishProgressViewResourceId = finishProgressViewResourceId;
    }

    public void stopRecording(boolean cancel) throws InterruptedException {
        cancelStartCallback();
        if (this.mCurrentSegment != null) {
            endRelativeTime();
        }
        if (cancel && this.mVideoController != null) {
            this.mVideoController.stop(true, true);
        }
    }

    public void openCamera() {
        boolean z = false;
        if (this.mVideoController != null && (this.mVideoController.openDefaultCamera(this.mFrontFacing, false) || this.mVideoController.isCameraReady())) {
            z = true;
        }
        this.mCameraSuccessfullyOpened = z;
    }

    @Override // co.vine.android.recorder.RegularVineRecorder, co.vine.android.recorder.BasicVineRecorder
    public void onUiResumed(Activity activity, Runnable onCompleteAsyncTask, boolean doNotResumeRecorder) {
        super.onUiResumed(activity, onCompleteAsyncTask, doNotResumeRecorder);
        this.mFinishProgressView = (ProgressView) activity.findViewById(this.mFinishProgressViewResourceId);
        if (!doNotResumeRecorder) {
            setDelayDialog(false);
            onResume("UI Resume");
        }
    }

    @Override // co.vine.android.recorder.RegularVineRecorder, co.vine.android.recorder.BasicVineRecorder
    public void onUiPaused() {
        super.onUiPaused();
        this.mStopping = false;
    }

    @Override // co.vine.android.recorder.RegularVineRecorder, co.vine.android.recorder.BasicVineRecorder
    public BasicVineRecorder.OnResumeTask getOnResumeTask(View view, String tag) {
        return new RegularVineRecorder.OnResumeTask(null, tag) { // from class: co.vine.android.recorder.InlineVineRecorder.2
            @Override // co.vine.android.recorder.BasicVineRecorder.OnResumeTask
            protected boolean onMakingSureCameraReady(RecordController controller) {
                return InlineVineRecorder.this.mCameraSuccessfullyOpened;
            }
        };
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    protected void adjustBoundaries(CameraSetting cs) {
        Activity activity = this.mActivity;
        if (activity != null && cs != null) {
            float previewWidth = cs.originalW;
            float previewHeight = cs.originalH;
            float aspectRatio = previewWidth / previewHeight;
            int cameraViewWidth = this.mSize.x - (this.mPaddingSize * 2);
            int cameraViewHeight = (int) (cameraViewWidth * aspectRatio);
            if (this.mTopMaskView != null && this.mBottomMaskView != null) {
                int bottomMaskHeightPx = this.mBottomMaskView.getMeasuredHeight();
                final RelativeLayout.LayoutParams topMaskParams = (RelativeLayout.LayoutParams) this.mTopMaskView.getLayoutParams();
                topMaskParams.bottomMargin = bottomMaskHeightPx + cameraViewWidth;
                final RelativeLayout.LayoutParams cameraViewParams = (RelativeLayout.LayoutParams) this.mCameraView.getLayoutParams();
                cameraViewParams.width = cameraViewWidth;
                cameraViewParams.height = cameraViewHeight;
                cameraViewParams.leftMargin = this.mPaddingSize;
                this.mActivity.runOnUiThread(new Runnable() { // from class: co.vine.android.recorder.InlineVineRecorder.3
                    @Override // java.lang.Runnable
                    public void run() {
                        View view = InlineVineRecorder.this.mTopMaskView;
                        if (view != null) {
                            view.setLayoutParams(topMaskParams);
                        }
                        View view2 = InlineVineRecorder.this.mCameraView;
                        if (view2 != null) {
                            view2.setLayoutParams(cameraViewParams);
                        }
                    }
                });
            }
        }
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void onProgressMaxReached() {
        finish("progressMaxReached");
    }

    @Override // co.vine.android.recorder.RegularVineRecorder, co.vine.android.recorder.BasicVineRecorder
    public BaseFinishProcessTask getFinishProcessTask(String tag, Runnable onComplete, boolean releasePreview, boolean saveSession) {
        return new InlineFinishProcessTask(tag, onComplete, releasePreview, saveSession);
    }

    @Override // co.vine.android.recorder.RegularVineRecorder, co.vine.android.recorder.BasicVineRecorder
    protected boolean onStop() {
        boolean result = !this.mStopping;
        this.mStopping = true;
        return result;
    }

    public class InlineFinishProcessTask extends BasicVineRecorder.FinishProcessTask {
        public InlineFinishProcessTask(String tag, Runnable onComplete, boolean releasePreview, boolean saveSession) {
            super(tag, onComplete, releasePreview, saveSession);
        }

        @Override // co.vine.android.recorder.BaseFinishProcessTask, android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            SLog.d("Finishing Inline preExec");
            InlineVineRecorder.this.mProcessingListener.onProcessingStart();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // co.vine.android.recorder.BasicVineRecorder.FinishProcessTask, android.os.AsyncTask
        public Void doInBackground(Void... params) {
            SLog.d("Finishing Inline");
            return super.doInBackground(params);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // co.vine.android.recorder.BaseFinishProcessTask, android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SLog.d("Finishing Inline postExec");
            InlineVineRecorder.this.mProcessingListener.onProcessingFinish();
            InlineVineRecorder.this.mStopping = false;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... values) {
            if (InlineVineRecorder.this.mFinishProgressView != null) {
                InlineVineRecorder.this.mFinishProgressView.setProgressRatio(values[0].intValue() / 100.0f);
            }
        }
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void receivedFirstFrameAfterStartingPreview() {
        if (this.mStarted && this.mCurrentSegment == null) {
            long endnannos = System.nanoTime();
            SLog.b("inline launch time: {}ms", Long.valueOf((endnannos - startnanos) / 1000000));
            startnanos = endnannos;
            this.mStarted = false;
            this.mHandler.postDelayed(this.mStartRelativeTimeRunnable, DELAY_WAIT_FOR_EXPOSURE);
        }
    }
}
