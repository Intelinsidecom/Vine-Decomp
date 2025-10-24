package co.vine.android.recorder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import co.vine.android.plugin.BaseRecorderPluginManager;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.util.SystemUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public abstract class RegularVineRecorder extends BasicVineRecorder implements View.OnTouchListener {
    public float density;
    protected int mBottomMaskId;
    protected View mBottomMaskView;
    protected int mCameraViewResourceId;
    protected DeviceIssueStringGetter mDeviceNotSupportedString;
    protected CharSequence mFinishLastSegmentString;
    protected ProgressDialog mFinishProgressDialog;
    protected CharSequence[] mFinishProgressDialogMessage;
    private CharSequence mImportingString;
    private boolean mIsHidingFinishProgressDialog;
    protected ProgressDialog mOpenCameraDialog;
    protected CharSequence mOpenCameraResource;
    private Drawable mProgressDrawable;
    protected ProgressView mProgressView;
    protected int mProgressViewResourceId;
    protected int mRootLayoutId;
    protected View mRootLayoutView;
    protected Point mSize;
    protected ProgressDialog mStartProgressDialog;
    protected CharSequence mStartProgressDialogMessage;
    protected Toast mToast;
    protected int mTopMaskId;
    protected View mTopMaskView;

    public interface DeviceIssueStringGetter {
        String getString(DeviceIssue deviceIssue);
    }

    public RegularVineRecorder(int thresholdMs, BaseRecorderPluginManager manager, Point screenSize, Activity activity, int progressViewResourceId, int cameraViewResourceId, int topMaskId, int bottomMaskId, int openCameraString, int rootLayoutId, Drawable progressDrawable, DeviceIssueStringGetter deviceNotSupportedString, boolean startWithFrontFacingCamera, RecordSessionVersion version, CharSequence importingString, CharSequence... messages) {
        super(thresholdMs, manager, activity, startWithFrontFacingCamera, version == RecordSessionVersion.HW, version);
        this.mStartProgressDialogMessage = messages[0];
        this.mFinishProgressDialogMessage = messages;
        this.mOpenCameraResource = activity.getText(openCameraString);
        this.mSize = screenSize;
        this.density = activity.getResources().getDisplayMetrics().density;
        this.mProgressViewResourceId = progressViewResourceId;
        this.mCameraViewResourceId = cameraViewResourceId;
        this.mRootLayoutId = rootLayoutId;
        this.mDeviceNotSupportedString = deviceNotSupportedString;
        this.mTopMaskId = topMaskId;
        this.mBottomMaskId = bottomMaskId;
        this.mImportingString = importingString;
        this.mProgressDrawable = progressDrawable;
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    protected void showCameraFailedToast() {
        this.mHandler.post(new Runnable() { // from class: co.vine.android.recorder.RegularVineRecorder.1
            @Override // java.lang.Runnable
            public void run() {
                if (RegularVineRecorder.this.mToast != null) {
                    RegularVineRecorder.this.mToast.show();
                }
            }
        });
    }

    protected class ResumeCameraAsyncTask extends BasicVineRecorder.ResumeCameraAsyncTask {
        public ResumeCameraAsyncTask(boolean switchCamera) {
            super(switchCamera);
        }

        @Override // co.vine.android.recorder.BasicVineRecorder.ResumeCameraAsyncTask, android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            if (RegularVineRecorder.this.mOpenCameraDialog != null) {
                RegularVineRecorder.this.mOpenCameraDialog.show();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // co.vine.android.recorder.BasicVineRecorder.ResumeCameraAsyncTask, android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (RegularVineRecorder.this.mOpenCameraDialog != null) {
                RegularVineRecorder.this.mOpenCameraDialog.dismiss();
            }
        }
    }

    protected class OnResumeTask extends BasicVineRecorder.OnResumeTask {
        public OnResumeTask(View view, String tag) {
            super(view, tag);
        }

        @Override // co.vine.android.recorder.BasicVineRecorder.OnResumeTask
        protected void publishFinishLastSegmentProgress() {
            publishProgress(new CharSequence[]{RegularVineRecorder.this.mFinishLastSegmentString});
        }

        @Override // co.vine.android.recorder.BasicVineRecorder.OnResumeTask
        protected void publishStartProgres() {
            publishProgress(new CharSequence[]{RegularVineRecorder.this.mStartProgressDialogMessage});
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(CharSequence... values) {
            if (RegularVineRecorder.this.mStartProgressDialog != null) {
                if (!this.showDialogDelayed && !RegularVineRecorder.this.mStartProgressDialog.isShowing()) {
                    RegularVineRecorder.this.mStartProgressDialog.show();
                }
                RegularVineRecorder.this.mStartProgressDialog.setMessage(values[0]);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // co.vine.android.recorder.BasicVineRecorder.OnResumeTask, android.os.AsyncTask
        public void onPostExecute(RecordingFile needHashTask) {
            super.onPostExecute(needHashTask);
            if (RegularVineRecorder.this.mStartProgressDialog != null && !isCancelled()) {
                RegularVineRecorder.this.mStartProgressDialog.dismiss();
            }
            RegularVineRecorder.this.onResumeTaskComplete();
        }
    }

    protected void onResumeTaskComplete() {
    }

    public CharSequence getImportingString() {
        return this.mImportingString;
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void changeProgress(long progress) {
        ProgressView view = this.mProgressView;
        if (view != null && this.mSession != null) {
            view.setProgressRatio(progress / (1.0f * this.mSession.getConfig().maxDuration));
            if (SystemUtil.isOnMainThread()) {
                view.invalidate();
            } else {
                view.postInvalidate();
            }
        }
    }

    public static String getBottomMaskHeightPref(boolean frontFacing) {
        return "bottomMaskHeightPx" + frontFacing;
    }

    @Override // android.view.View.OnTouchListener
    public synchronized boolean onTouch(View v, MotionEvent event) {
        boolean z = true;
        synchronized (this) {
            int action = event.getAction();
            if (this.mCurrentRecordingFile != null && this.mEnabled) {
                if (this.mAutoFocusing) {
                    switch (action) {
                        case 0:
                            float axisX = event.getAxisValue(0);
                            if (axisX > 0.05f * this.mSize.x && axisX < 0.95f * this.mSize.x) {
                                startRelativeTime();
                                break;
                            }
                            break;
                        case 1:
                            endRelativeTime();
                            break;
                    }
                } else {
                    switch (action) {
                        case 1:
                            changeFocusTo(event.getAxisValue(0), event.getAxisValue(1));
                            break;
                    }
                }
            } else {
                z = false;
            }
        }
        return z;
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void onUiPaused() {
        if (this.mCameraView != null) {
            this.mCameraView.setOnTouchListener(null);
        }
        try {
            if (this.mFinishProgressDialog != null) {
                this.mFinishProgressDialog.dismiss();
            }
            if (this.mStartProgressDialog != null) {
                this.mStartProgressDialog.dismiss();
            }
            if (this.mOpenCameraDialog != null) {
                this.mOpenCameraDialog.dismiss();
            }
        } catch (Exception e) {
            SLog.e("It's probably detached already.", (Throwable) e);
        }
        this.mStartProgressDialog = null;
        this.mFinishProgressDialog = null;
        this.mOpenCameraDialog = null;
        this.mTopMaskView = null;
        this.mBottomMaskView = null;
        this.mProgressView = null;
        this.mToast = null;
        super.onUiPaused();
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    protected boolean onStop() {
        if (this.mFastEncoding) {
            if (!this.mIsHidingFinishProgressDialog) {
                this.mIsHidingFinishProgressDialog = true;
                return true;
            }
            SLog.d("Stop twice? wtf.");
            return false;
        }
        final ProgressDialog dialog = this.mFinishProgressDialog;
        if (dialog != null && !dialog.isShowing()) {
            this.mActivity.runOnUiThread(new Runnable() { // from class: co.vine.android.recorder.RegularVineRecorder.2
                @Override // java.lang.Runnable
                public void run() {
                    dialog.show();
                }
            });
            return true;
        }
        SLog.d("Stop twice? wtf.");
        return false;
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public BaseFinishProcessTask getFinishProcessTask(String tag, Runnable onComplete, boolean releasePreview, boolean saveSession) {
        return new FinishProcessTask(tag, onComplete, releasePreview, saveSession);
    }

    public class FinishProcessTask extends BasicVineRecorder.FinishProcessTask {
        public FinishProcessTask(String tag, Runnable onComplete, boolean releasePreview, boolean saveSession) {
            super(tag, onComplete, releasePreview, saveSession);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // co.vine.android.recorder.BaseFinishProcessTask, android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RegularVineRecorder.this.mIsHidingFinishProgressDialog = false;
            ProgressDialog dialog = RegularVineRecorder.this.mFinishProgressDialog;
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... values) {
            ProgressDialog dialog = RegularVineRecorder.this.mFinishProgressDialog;
            if (dialog != null && dialog.isShowing()) {
                if (RegularVineRecorder.this.mFinishProgressDialogMessage != null && RegularVineRecorder.this.mFinishProgressDialogMessage.length > 2 && values[0].intValue() >= (100 / (RegularVineRecorder.this.mFinishProgressDialogMessage.length - 1)) * this.mCurrentMessage) {
                    if (this.mCurrentMessage + 1 < RegularVineRecorder.this.mFinishProgressDialogMessage.length) {
                        dialog.setMessage(RegularVineRecorder.this.mFinishProgressDialogMessage[this.mCurrentMessage + 1]);
                    }
                    this.mCurrentMessage++;
                }
                dialog.setProgress(values[0].intValue());
            }
        }
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    protected Runnable getOnDeviceIssueAction(final DeviceIssue issue) {
        return new Runnable() { // from class: co.vine.android.recorder.RegularVineRecorder.3
            @Override // java.lang.Runnable
            public void run() {
                RegularVineRecorder.this.mHandler.post(new Runnable() { // from class: co.vine.android.recorder.RegularVineRecorder.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (RegularVineRecorder.this.mActivity != null) {
                            Toast.makeText(RegularVineRecorder.this.mActivity, RegularVineRecorder.this.mDeviceNotSupportedString.getString(issue), 0).show();
                            RegularVineRecorder.this.mActivity.finish();
                        }
                    }
                });
            }
        };
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public void stopAndDiscardChanges(String tag, Runnable onComplete, boolean releasePreview) {
        setDiscardChanges(true);
        this.mFinishProcessRunnable.run(tag, releasePreview, false);
        if (onComplete != null) {
            onComplete.run();
        }
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    @SuppressLint({"ShowToast"})
    public void onUiResumed(Activity activity, Runnable onCompleteAsyncTask, boolean doNotResumeRecorder) {
        super.onUiResumed(activity, onCompleteAsyncTask, doNotResumeRecorder);
        this.mFinishProgressDialog = new ProgressDialog(activity, 2);
        this.mFinishProgressDialog.setMessage(this.mFinishProgressDialogMessage[1]);
        this.mFinishProgressDialog.setMax(100);
        this.mFinishProgressDialog.setCancelable(false);
        this.mFinishProgressDialog.setIndeterminate(false);
        this.mFinishProgressDialog.setProgressStyle(1);
        this.mFinishProgressDialog.setProgressDrawable(this.mProgressDrawable);
        this.mStartProgressDialog = new ProgressDialog(activity, 2);
        this.mOpenCameraDialog = new ProgressDialog(activity, 2);
        this.mOpenCameraDialog.setMessage(this.mOpenCameraResource);
        this.mOpenCameraDialog.setCancelable(false);
        this.mRootLayoutView = activity.findViewById(this.mRootLayoutId);
        this.mCameraView = activity.findViewById(this.mCameraViewResourceId);
        this.mCameraView.setVisibility(0);
        this.mCameraView.setOnTouchListener(this);
        this.mTopMaskView = activity.findViewById(this.mTopMaskId);
        this.mBottomMaskView = activity.findViewById(this.mBottomMaskId);
        this.mProgressView = (ProgressView) activity.findViewById(this.mProgressViewResourceId);
        this.mCameraView.setOnTouchListener(this);
        this.mProgressView.setKeepScreenOn(true);
        this.mToast = Toast.makeText(activity, this.mDeviceNotSupportedString.getString(DeviceIssue.CANNOT_CONNECT_TO_CAMERA), 0);
    }

    @Override // co.vine.android.recorder.BasicVineRecorder
    public BasicVineRecorder.OnResumeTask getOnResumeTask(View view, String tag) {
        return new OnResumeTask(view, tag);
    }
}
