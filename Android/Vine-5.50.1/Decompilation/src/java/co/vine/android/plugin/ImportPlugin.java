package co.vine.android.plugin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import co.vine.android.AppStateProviderSingleton;
import co.vine.android.CameraOnboardHelper;
import co.vine.android.ImportTrimCropActivity;
import co.vine.android.R;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.ExtractorTranscoder;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordSegment;
import co.vine.android.recorder.RecordingFile;
import co.vine.android.recorder.VineRecorder;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.VideoImportCompletedScribeLogger;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.TooltipView;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ImportPlugin extends BaseToolPlugin<ImageButton> implements View.OnClickListener, TooltipView.Listener {
    private boolean mBlockEditMode;
    private ImageButton mImportButton;
    private final int[] mLock;
    private ViewGroup mParentView;
    private ProgressDialog mProgressDialog;
    private final Runnable mToolTipRunnable;
    private TooltipView mTooltip;
    private ExtractorTranscoder mTranscoder;
    private boolean mWasToolTipHidden;

    public ImportPlugin() {
        super("Import");
        this.mLock = new int[0];
        this.mToolTipRunnable = new Runnable() { // from class: co.vine.android.plugin.ImportPlugin.2
            @Override // java.lang.Runnable
            public void run() {
                ViewGroup parent = ImportPlugin.this.mParentView;
                TooltipView tooltip = ImportPlugin.this.mTooltip;
                if (parent != null && tooltip != null && !tooltip.hasBeenHidden()) {
                    tooltip.updatePosition(parent, parent.getWidth() / 4);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tooltip.getLayoutParams();
                    params.topMargin = tooltip.getResources().getDimensionPixelOffset(R.dimen.tooltipNegativeMargin);
                    tooltip.setLayoutParams(params);
                    tooltip.show();
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // co.vine.android.plugin.BaseToolPlugin
    public ImageButton onLayoutInflated(LinearLayout parent, LayoutInflater inflater, Fragment fragment) throws Resources.NotFoundException {
        this.mActivity = fragment.getActivity();
        ImageButton button = (ImageButton) inflater.inflate(R.layout.plugin_option_image_button, (ViewGroup) parent, false);
        button.setOnClickListener(this);
        button.setImageResource(R.drawable.ic_import);
        if (fragment instanceof RecorderPluginSupportedFragment) {
            ViewUtil.fillColor(fragment.getResources(), ((RecorderPluginSupportedFragment) fragment).getColor(true), R.drawable.ic_import, button);
        }
        button.setAlpha(0.35f);
        button.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.plugin.ImportPlugin.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getAlpha() != 0.175f) {
                    switch (event.getAction()) {
                        case 0:
                            view.setAlpha(1.0f);
                            break;
                        case 1:
                            view.setAlpha(0.35f);
                            break;
                    }
                    return false;
                }
                return false;
            }
        });
        this.mShouldOnboard = CameraOnboardHelper.getLastCompletedStep(this.mActivity) == null;
        if (this.mShouldOnboard && fragment.getView() != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
            params.addRule(3, R.id.recording_options);
            TooltipView tooltip = new TooltipView(fragment.getActivity());
            tooltip.setText(R.string.camera_onboarding_grab_video_from_camera_roll);
            tooltip.setListener(this);
            RelativeLayout bottomMask = (RelativeLayout) fragment.getView().findViewById(R.id.bottom_mask);
            bottomMask.addView(tooltip, params);
            this.mTooltip = tooltip;
            this.mParentView = parent;
        }
        this.mImportButton = button;
        return button;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        BasicVineRecorder recorder = getRecorder();
        if (this.mActivity != null && recorder != null && recorder.canKeepRecording() && !recorder.isCurrentlyRecording()) {
            int totalDurationMs = recorder.getEditedDurationMs();
            RecordConfigUtils.RecordConfig config = recorder.getConfig();
            if (config != null) {
                int maxDurationMs = config.maxDuration;
                if (maxDurationMs - totalDurationMs > 200) {
                    Intent intent = new Intent(this.mActivity, (Class<?>) ImportTrimCropActivity.class);
                    intent.putExtra("current_duration", totalDurationMs);
                    intent.putExtra("max_duration", maxDurationMs);
                    this.mBlockEditMode = true;
                    this.mActivity.startActivityForResult(intent, 1);
                    if (hideToolTip()) {
                        String lastStep = CameraOnboardHelper.getLastCompletedStep(this.mActivity);
                        if (!"delete".equals(lastStep) && !"delete_2".equals(lastStep) && !"more_tools".equals(lastStep)) {
                            CameraOnboardHelper.setLastCompletedStep(this.mActivity, "grab");
                        }
                        this.mShouldOnboard = false;
                    }
                }
            }
        }
    }

    @Override // co.vine.android.widget.TooltipView.Listener
    public void onTooltipTapped() {
        CameraOnboardHelper.setLastCompletedStep(this.mActivity, "grab");
    }

    private boolean hideToolTip() {
        if (this.mTooltip == null || this.mTooltip.hasBeenHidden()) {
            return false;
        }
        this.mTooltip.hide();
        return true;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onCameraReady(boolean frontFacing, boolean autoFocusSet) {
        synchronized (this.mLock) {
            if (this.mProgressDialog != null) {
                this.mProgressDialog.show();
            }
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onResume(Activity activity) {
        VineRecorder recorder;
        super.onResume(activity);
        synchronized (this.mLock) {
            if (this.mProgressDialog != null && (recorder = getRecorder()) != null) {
                recorder.setDelayDialog(true);
            }
            if (this.mShouldOnboard) {
                showToopTip(2000L);
            }
        }
    }

    private void showToopTip(long delay) {
        ViewGroup parent = this.mParentView;
        if (parent != null) {
            parent.removeCallbacks(this.mToolTipRunnable);
            parent.postDelayed(this.mToolTipRunnable, delay);
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onSegmentDataChanged(ArrayList<RecordSegment> editedSegments) {
        final VineRecorder recorder = getRecorder();
        if (recorder != null) {
            recorder.postOnHandlerLoop(new Runnable() { // from class: co.vine.android.plugin.ImportPlugin.3
                @Override // java.lang.Runnable
                public void run() {
                    int duration = recorder.getEditedDurationMs();
                    RecordConfigUtils.RecordConfig config = recorder.getConfig();
                    if (config != null) {
                        if (config.maxDuration - duration < 200) {
                            ImportPlugin.this.mImportButton.setAlpha(0.175f);
                        } else {
                            ImportPlugin.this.mImportButton.setAlpha(0.35f);
                        }
                    }
                }
            });
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public boolean onSetEditMode(boolean editing, boolean hasData) {
        if (editing) {
            if (hideToolTip()) {
                this.mWasToolTipHidden = true;
            }
        } else if (this.mWasToolTipHidden) {
            showToopTip(500L);
        }
        return this.mBlockEditMode;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == 1) {
            VineRecorder recorder = getRecorder();
            if (recorder == null) {
                throw new IllegalStateException("No recorder to handle this import!");
            }
            ImportAsyncTask importTask = new ImportAsyncTask(activity, recorder, data);
            importTask.execute(new Void[0]);
            return true;
        }
        this.mBlockEditMode = false;
        return false;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onPause() {
        super.onPause();
        synchronized (this.mLock) {
            if (this.mProgressDialog != null) {
                this.mProgressDialog.dismiss();
            }
        }
    }

    private class ImportAsyncTask extends AsyncTask<Void, Void, Void> implements DialogInterface.OnCancelListener, ExtractorTranscoder.ExtractorTranscoderListener {
        private final Activity context;
        private final Intent data;
        private int mLastProgress;
        private final VineRecorder recorder;

        public ImportAsyncTask(Activity activity, VineRecorder r, Intent intent) {
            this.context = activity;
            this.recorder = r;
            this.data = intent;
            synchronized (ImportPlugin.this.mLock) {
                ImportPlugin.this.mProgressDialog = new ProgressDialog(this.context, 2);
                ImportPlugin.this.mProgressDialog.setIndeterminate(false);
                ImportPlugin.this.mProgressDialog.setCancelable(true);
                ImportPlugin.this.mProgressDialog.setProgressStyle(1);
                ImportPlugin.this.mProgressDialog.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_horizontal));
                ImportPlugin.this.mProgressDialog.setOnCancelListener(this);
            }
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            synchronized (ImportPlugin.this.mLock) {
                if (ImportPlugin.this.mProgressDialog != null) {
                    ImportPlugin.this.mProgressDialog.setMax(100);
                    ImportPlugin.this.mProgressDialog.setMessage(this.recorder.getImportingString());
                    ImportPlugin.this.mProgressDialog.show();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) throws Throwable {
            try {
                RecordingFile file = this.recorder.getFile();
                long startMicros = this.data.getLongExtra("trim_start_time_usec", 0L);
                long endMicros = this.data.getLongExtra("trim_end_time_usec", 0L);
                Point cropOrigin = (Point) this.data.getParcelableExtra("crop_origin");
                Uri inUri = this.data.getData();
                SLog.d("Import result detected: url {} startMicros {}, endMicros {}", inUri, Long.valueOf(startMicros), Long.valueOf(endMicros));
                RecordSegment segment = new RecordSegment(this.recorder.getCurrentDuration()).setCameraSetting(this.recorder.getCurrentCameraSetting());
                ImportPlugin.this.mTranscoder = new ExtractorTranscoder();
                ImportPlugin.this.mTranscoder.transcode(this.context, file.folder, this.recorder.getSesion(), segment, inUri, file.createSegmentVideoPath(), startMicros, endMicros, cropOrigin, this);
                ImportPlugin.this.mBlockEditMode = false;
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // co.vine.android.recorder.ExtractorTranscoder.ExtractorTranscoderListener
        public void onProgressUpdate(int progressOverOneHundred) {
            if (this.mLastProgress < progressOverOneHundred) {
                this.mLastProgress = progressOverOneHundred;
                synchronized (ImportPlugin.this.mLock) {
                    if (ImportPlugin.this.mProgressDialog != null) {
                        ImportPlugin.this.mProgressDialog.setProgress(progressOverOneHundred);
                    }
                }
            }
        }

        @Override // co.vine.android.recorder.ExtractorTranscoder.ExtractorTranscoderListener
        public void onFinishedTranscoding(RecordSegment segment, Bitmap lastBitmap) {
            onProgressUpdate(100);
            segment.setIsImported(true);
            this.recorder.addExtraSegment(segment, lastBitmap);
            synchronized (ImportPlugin.this.mLock) {
                if (ImportPlugin.this.mProgressDialog != null) {
                    ImportPlugin.this.mProgressDialog.dismiss();
                    ImportPlugin.this.mProgressDialog = null;
                }
            }
            if (ImportPlugin.this.mActivity != null) {
                VideoImportCompletedScribeLogger.logImportSuccess(ScribeLoggerSingleton.getInstance(ImportPlugin.this.mActivity), AppStateProviderSingleton.getInstance(ImportPlugin.this.mActivity));
            }
        }

        @Override // co.vine.android.recorder.ExtractorTranscoder.ExtractorTranscoderListener
        public void onError(Exception e) {
            CrashUtil.logException(e, "Import exception", new Object[0]);
            ImportPlugin.this.getHandler().post(new Runnable() { // from class: co.vine.android.plugin.ImportPlugin.ImportAsyncTask.1
                @Override // java.lang.Runnable
                public void run() {
                    if (ImportPlugin.this.mProgressDialog != null) {
                        ImportPlugin.this.mProgressDialog.dismiss();
                        ImportPlugin.this.mProgressDialog = null;
                    }
                    Util.showCenteredToast(ImportAsyncTask.this.context, ImportAsyncTask.this.context.getString(R.string.error_import_video_unknown_error));
                }
            });
            if (ImportPlugin.this.mActivity != null) {
                VideoImportCompletedScribeLogger.logImportProcessingError(ScribeLoggerSingleton.getInstance(ImportPlugin.this.mActivity), AppStateProviderSingleton.getInstance(ImportPlugin.this.mActivity));
            }
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialog) {
            if (ImportPlugin.this.mProgressDialog != null) {
                ImportPlugin.this.mProgressDialog.dismiss();
                ImportPlugin.this.mProgressDialog = null;
            }
            if (ImportPlugin.this.mTranscoder != null) {
                ImportPlugin.this.mTranscoder.cancel();
            }
            ImportPlugin.this.mBlockEditMode = false;
            if (ImportPlugin.this.mActivity != null) {
                VideoImportCompletedScribeLogger.logImportCancelled(ScribeLoggerSingleton.getInstance(ImportPlugin.this.mActivity), AppStateProviderSingleton.getInstance(ImportPlugin.this.mActivity));
            }
        }
    }
}
