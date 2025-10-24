package co.vine.android;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import co.vine.android.widgets.PromptDialogFragment;
import com.googlecode.javacv.cpp.opencv_core;
import com.sonymobile.camera.addon.capturingmode.CapturingModeSelector;

/* loaded from: classes.dex */
public class SonyRecordingActivity extends AbstractRecordingActivity {
    private final PromptDialogFragment.OnDialogDoneListener mListener = new PromptDialogFragment.OnDialogDoneListener() { // from class: co.vine.android.SonyRecordingActivity.5
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (which) {
                case opencv_core.CV_StsInternal /* -3 */:
                case -2:
                    if (SonyRecordingActivity.this.currentlyHoldsRecordingFragment()) {
                        ((RecordingFragment) SonyRecordingActivity.this.mCurrentFragment).setDiscardChangesOnStop();
                    }
                    SonyRecordingActivity.this.discardUpload();
                    SonyRecordingActivity.this.showSonyOverlay();
                    break;
                default:
                    if (SonyRecordingActivity.this.currentlyHoldsRecordingFragment()) {
                        ((RecordingFragment) SonyRecordingActivity.this.mCurrentFragment).saveSession();
                    }
                    SonyRecordingActivity.this.showSonyOverlay();
                    break;
            }
        }
    };
    private CapturingModeSelector mSonyCapturingModeSelector;
    private int mSonyFilterColor;
    private String mSonyModeName;
    private ImageView mSonyModeSelectorButton;
    private ViewGroup mSonySelectorContainer;

    @Override // co.vine.android.AbstractRecordingActivity, co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        this.mSonyFilterColor = res.getColor(R.color.sony_filtered_color);
        this.mSonyModeName = res.getString(R.string.app_name);
    }

    @Override // co.vine.android.AbstractRecordingActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        setupSonyOverlay();
    }

    @Override // co.vine.android.AbstractRecordingActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() throws InterruptedException {
        if (this.mSonyCapturingModeSelector != null && this.mSonyCapturingModeSelector.isOpened()) {
            this.mSonyCapturingModeSelector.close();
            this.mSonyModeSelectorButton.setVisibility(0);
            onHideSonyOverlay();
            return;
        }
        super.onBackPressed();
    }

    @Override // co.vine.android.AbstractRecordingActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        if (this.mSonyCapturingModeSelector != null) {
            this.mSonyCapturingModeSelector.release();
            this.mSonyCapturingModeSelector = null;
        }
        super.onPause();
    }

    @Override // co.vine.android.AbstractRecordingActivity, co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        if (this.mSonyModeSelectorButton != null) {
            this.mSonyModeSelectorButton.setOnClickListener(null);
            this.mSonyModeSelectorButton.setOnTouchListener(null);
        }
        super.onDestroy();
    }

    @Override // co.vine.android.AbstractRecordingActivity
    public boolean isFromSony() {
        return true;
    }

    public void setupSonyOverlay() {
        this.mSonyModeSelectorButton = (ImageView) findViewById(R.id.sony_button);
        if (this.mSonyModeSelectorButton != null) {
            ViewGroup vg = (ViewGroup) findViewById(R.id.root_layout);
            if (vg != null && this.mSonySelectorContainer == null) {
                this.mSonySelectorContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.sony_container, vg, false);
                vg.addView(this.mSonySelectorContainer);
            }
            this.mSonyModeSelectorButton.setVisibility(0);
            this.mSonyModeSelectorButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.SonyRecordingActivity.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    RecordingFragment fragment;
                    if (SonyRecordingActivity.this.mSonyCapturingModeSelector != null && (fragment = (RecordingFragment) SonyRecordingActivity.this.mCurrentFragment) != null) {
                        boolean isEditing = fragment.isEditing();
                        if (!isEditing && !fragment.isSessionModified()) {
                            fragment.setDiscardChangesOnStop();
                            SonyRecordingActivity.this.showSonyOverlay();
                        } else if (!isEditing || fragment.isEditingDirty()) {
                            SonyRecordingActivity.this.showUnSavedChangesToSessionDialog(SonyRecordingActivity.this.mListener);
                        } else {
                            fragment.discardEditing();
                            SonyRecordingActivity.this.showSonyOverlay();
                        }
                    }
                }
            });
            this.mSonyModeSelectorButton.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.SonyRecordingActivity.2
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    SonyRecordingActivity.this.mSonyModeSelectorButton.onTouchEvent(event);
                    if (SonyRecordingActivity.this.mSonyModeSelectorButton.isPressed()) {
                        SonyRecordingActivity.this.mSonyModeSelectorButton.setColorFilter(SonyRecordingActivity.this.mSonyFilterColor);
                        return true;
                    }
                    SonyRecordingActivity.this.mSonyModeSelectorButton.clearColorFilter();
                    return true;
                }
            });
            try {
                this.mSonyCapturingModeSelector = new CapturingModeSelector(this, this.mSonySelectorContainer);
            } catch (IllegalAccessError | NoClassDefFoundError e) {
                CrashUtil.logException(new VineLoggingException("Unable to create Sony Capture Mode selector."));
            }
            if (this.mSonyCapturingModeSelector != null) {
                this.mSonyCapturingModeSelector.setOnModeSelectListener(new CapturingModeSelector.OnModeSelectListener() { // from class: co.vine.android.SonyRecordingActivity.3
                    public void onModeSelect(String s) {
                        if (SonyRecordingActivity.this.mSonyCapturingModeSelector != null) {
                            SonyRecordingActivity.this.mSonyCapturingModeSelector.close();
                        }
                        SonyRecordingActivity.this.finish();
                    }
                });
                this.mSonyCapturingModeSelector.setOnModeFinishListener(new CapturingModeSelector.OnModeFinishListener() { // from class: co.vine.android.SonyRecordingActivity.4
                    public void onModeFinish() {
                        if (SonyRecordingActivity.this.mSonyCapturingModeSelector != null) {
                            SonyRecordingActivity.this.mSonyCapturingModeSelector.close();
                            SonyRecordingActivity.this.mSonyModeSelectorButton.setVisibility(0);
                        }
                    }
                });
                this.mSonyModeSelectorButton.setVisibility(0);
                return;
            }
            this.mSonyModeSelectorButton.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSonyOverlay() {
        try {
            if (this.mSonyCapturingModeSelector != null) {
                this.mSonyCapturingModeSelector.open(this.mSonyModeName);
            }
            if (this.mSonyModeSelectorButton != null) {
                this.mSonyModeSelectorButton.setVisibility(4);
                this.mSonyModeSelectorButton.clearColorFilter();
            }
            onShowSonyOverlay();
        } catch (IllegalAccessError | NoClassDefFoundError e) {
            Util.showCenteredToast(this, R.string.unsupported_feature);
            CrashUtil.logOrThrowInDebug(e);
        }
    }

    @Override // co.vine.android.AbstractRecordingActivity
    protected RecordingFragment createRecordingFragment() {
        return new SonyRecordingFragment();
    }

    private void onHideSonyOverlay() {
        View recordingOptions = findViewById(R.id.recording_options);
        View captureX = findViewById(R.id.capture_x);
        View progressBar = findViewById(R.id.progress);
        if (recordingOptions != null) {
            recordingOptions.setVisibility(0);
        }
        if (captureX != null) {
            captureX.setVisibility(0);
        }
        if (progressBar != null) {
            progressBar.setVisibility(0);
        }
    }

    private void onShowSonyOverlay() {
        View recordingOptions = findViewById(R.id.recording_options);
        View captureX = findViewById(R.id.capture_x);
        View progressBar = findViewById(R.id.progress);
        if (recordingOptions != null) {
            recordingOptions.setVisibility(4);
        }
        if (captureX != null) {
            captureX.setVisibility(4);
        }
        if (progressBar != null) {
            progressBar.setVisibility(4);
        }
    }
}
