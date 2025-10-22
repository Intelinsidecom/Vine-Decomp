package co.vine.android.recordingui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import co.vine.android.ActivityResultHandler;
import co.vine.android.R;
import co.vine.android.RecordingNavigationController;
import co.vine.android.RecordingPreviewFragment;
import co.vine.android.api.VineSource;
import co.vine.android.player.SdkVideoView;
import co.vine.android.recorder2.RecordController;
import co.vine.android.recorder2.RecordControllerImpl;
import co.vine.android.recorder2.model.Draft;
import co.vine.android.recorder2.model.DraftsManager;
import co.vine.android.recorder2.model.Segment;
import co.vine.android.recordingui.CaptureMode;
import co.vine.android.share.activities.PostShareParameters;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.UploadManager;
import com.edisonwang.android.slog.SLog;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes.dex */
public class CameraCaptureActivity extends ActionBarActivity implements RecordingNavigationController, CaptureMode.ActivityStarterForResult, RecordStateHolder {
    private CaptureMode mCaptureMode;
    private DraftsMode mDraftsMode;
    private EditMode mEditMode;
    private MultiEditMode mMultiEditMode;
    private PostShareParameters mPostShareParameters;
    private RecordingPreviewFragment mPreviewFragment;
    private RecordController mRecordController;
    private RecordingModeState mRecordingModeState;
    private HashMap<Integer, ActivityResultHandler> mResultHandlers = new HashMap<>();
    private boolean mGeneratingPreview = false;
    private boolean mReturnToPreviewFromEdit = false;

    public enum RecordingModeState {
        CAPTURE,
        EDIT,
        DRAFTS,
        PREVIEW,
        MULTI_EDIT
    }

    @Override // co.vine.android.recordingui.CaptureMode.ActivityStarterForResult
    public void startActivityForResult(Intent data, int requestCode, ActivityResultHandler handler) {
        this.mResultHandlers.put(Integer.valueOf(requestCode), handler);
        startActivityForResult(data, requestCode);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityResultHandler handler = this.mResultHandlers.remove(Integer.valueOf(requestCode));
        if (this.mPreviewFragment != null && (requestCode == 1995 || requestCode == 1990)) {
            this.mPreviewFragment.onActivityResult(requestCode, resultCode, data);
        } else if (handler != null) {
            handler.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            DraftsManager.loadDrafts(this);
        } catch (IOException e) {
        } catch (ClassNotFoundException e2) {
        }
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(1);
        window.addFlags(128);
        setContentView(R.layout.activity_camera_capture);
        final SdkVideoView videoView = (SdkVideoView) findViewById(R.id.video_view);
        videoView.setAutoPlayOnPrepared(true);
        videoView.setLooping(true);
        videoView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.recordingui.CameraCaptureActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws IllegalStateException {
                if (videoView.isInPlaybackState()) {
                    if (videoView.isPaused()) {
                        videoView.resume();
                    } else {
                        videoView.pause();
                    }
                }
            }
        });
        GLSurfaceView glView = (GLSurfaceView) findViewById(R.id.cameraPreview_surfaceView);
        this.mRecordController = new RecordControllerImpl(this, glView);
        this.mCaptureMode = new CaptureMode(this, this, glView, videoView, this.mRecordController, this);
        this.mEditMode = new EditMode(this, this.mCaptureMode, videoView, this.mRecordController, this);
        this.mMultiEditMode = new MultiEditMode(this.mRecordController, this, this, this.mCaptureMode);
        this.mDraftsMode = new DraftsMode(this, this.mRecordController, this);
        this.mRecordController.setGhostInvalidatedListener(this.mCaptureMode);
        this.mRecordController.setRecordingEventListener(this.mCaptureMode);
        this.mRecordingModeState = RecordingModeState.CAPTURE;
    }

    @Override // co.vine.android.RecordingNavigationController
    public void toggleEditMode() throws IllegalStateException {
        if (this.mRecordingModeState == RecordingModeState.CAPTURE && this.mRecordController.draftHasSegment()) {
            generateFullVideo(new Runnable() { // from class: co.vine.android.recordingui.CameraCaptureActivity.2
                @Override // java.lang.Runnable
                public void run() throws IllegalStateException, IllegalAccessException, Resources.NotFoundException, IllegalArgumentException, InvocationTargetException {
                    CameraCaptureActivity.this.mRecordingModeState = RecordingModeState.EDIT;
                    if (ClientFlagsHelper.useNewEditScreen(CameraCaptureActivity.this.getApplicationContext())) {
                        CameraCaptureActivity.this.mMultiEditMode.show();
                    } else {
                        CameraCaptureActivity.this.mEditMode.show();
                    }
                    CameraCaptureActivity.this.mCaptureMode.hide();
                }
            });
            return;
        }
        if (!this.mReturnToPreviewFromEdit || !this.mRecordController.hasRecordedMinimumDuration()) {
            this.mReturnToPreviewFromEdit = false;
            this.mRecordingModeState = RecordingModeState.CAPTURE;
            this.mEditMode.hide();
            this.mMultiEditMode.hide();
            this.mCaptureMode.show();
            return;
        }
        this.mReturnToPreviewFromEdit = false;
        this.mEditMode.hide();
        this.mMultiEditMode.hide();
        this.mCaptureMode.hide();
        goToPreview();
    }

    @Override // co.vine.android.RecordingNavigationController
    public void goToPreview() {
        prepareToGoToPreview();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() throws IllegalStateException {
        super.onResume();
        if (getIntent() != null && getIntent().getData() != null) {
            Uri vineToRemix = getIntent().getData();
            getIntent().setData(null);
            this.mCaptureMode.importVideo(vineToRemix, getIntent().getStringExtra("extra_source_post_id"));
        }
        AspectRatioFrameLayout layout = (AspectRatioFrameLayout) findViewById(R.id.cameraPreview_afl);
        layout.setAspectRatio(1.0f);
        AspectRatioFrameLayout layout2 = (AspectRatioFrameLayout) findViewById(R.id.video_container);
        layout2.setAspectRatio(1.0f);
        this.mRecordController.onResume();
        if (this.mRecordingModeState == RecordingModeState.EDIT) {
            this.mEditMode.onResume();
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() throws IllegalStateException {
        super.onPause();
        this.mRecordController.onPause();
        if (this.mRecordingModeState == RecordingModeState.EDIT) {
            this.mEditMode.onPause();
        } else if (this.mRecordingModeState == RecordingModeState.MULTI_EDIT) {
            this.mMultiEditMode.onPause();
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        SLog.d("onDestroy");
        super.onDestroy();
        this.mRecordController.onDestroy();
    }

    public void prepareToGoToPreview() {
        generateFullVideo(new Runnable() { // from class: co.vine.android.recordingui.CameraCaptureActivity.3
            @Override // java.lang.Runnable
            public void run() {
                CameraCaptureActivity.this.goToPreview(CameraCaptureActivity.this.mRecordController.getDraftFullVideoPath(), CameraCaptureActivity.this.mRecordController.getDraft().getThumbnailPath(), CameraCaptureActivity.this.mRecordController.getDraft());
            }
        });
    }

    public void generateFullVideo(final Runnable onCompleteRunnable) {
        if (this.mRecordController.isCurrentlyIdle() && !this.mGeneratingPreview) {
            this.mGeneratingPreview = true;
            this.mCaptureMode.showProgressSpinner();
            this.mRecordController.generateFullVideo(new Runnable() { // from class: co.vine.android.recordingui.CameraCaptureActivity.4
                @Override // java.lang.Runnable
                public void run() {
                    CameraCaptureActivity.this.mGeneratingPreview = false;
                    CameraCaptureActivity.this.mCaptureMode.hideProgressSpinner();
                    onCompleteRunnable.run();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goToPreview(String videoPath, String thumbnailPath, Draft draft) {
        ArrayList<Segment> segments = draft.getSegments();
        ArrayList<VineSource> sources = new ArrayList<>();
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            if (StringUtils.isNotBlank(segment.getSourcePostId())) {
                long[] time = draft.getSegmentTimestampBounds(i);
                VineSource source = VineSource.create(2, 0L, "", "", "", segment.getSourcePostId(), time[0], time[1], segment.getTrimmedDurationMS());
                sources.add(source);
            }
        }
        try {
            String upload = UploadManager.addToUploadQueue(this, "HW_2_MP4", videoPath, thumbnailPath, this.mRecordController.getDraft().getDirectoryName(), "", false, -1L, sources);
            runOnUiThread(new Runnable() { // from class: co.vine.android.recordingui.CameraCaptureActivity.5
                @Override // java.lang.Runnable
                public void run() {
                    CameraCaptureActivity.this.mCaptureMode.hide();
                }
            });
            this.mPreviewFragment = RecordingPreviewFragment.newInstance(videoPath, upload, thumbnailPath, false, -1L, -1L, false, 0, null, sources);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.mPreviewFragment).commit();
            this.mRecordingModeState = RecordingModeState.PREVIEW;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override // co.vine.android.RecordingNavigationController
    public void handleDraftAction(RecordingNavigationController.DraftAction action, final boolean goToDrafts) throws IllegalStateException, Resources.NotFoundException {
        switch (action) {
            case SAVE:
                generateFullVideo(new Runnable() { // from class: co.vine.android.recordingui.CameraCaptureActivity.6
                    @Override // java.lang.Runnable
                    public void run() throws IllegalStateException, Resources.NotFoundException {
                        try {
                            CameraCaptureActivity.this.mRecordController.saveDraft();
                        } catch (IOException e) {
                        }
                        if (goToDrafts) {
                            CameraCaptureActivity.this.showDraftsOrCreateNewDraft(CameraCaptureActivity.this.mRecordController.getDraft());
                        } else {
                            CameraCaptureActivity.this.finish();
                        }
                    }
                });
                break;
            case DISCARD_DELETE:
                try {
                    this.mRecordController.deleteDraft();
                } catch (IOException e) {
                }
                if (goToDrafts) {
                    showDraftsOrCreateNewDraft(null);
                    break;
                } else {
                    finish();
                    break;
                }
            case DISCARD_REVERT:
                this.mRecordController.revertToLoadedDraft();
                generateFullVideo(new Runnable() { // from class: co.vine.android.recordingui.CameraCaptureActivity.7
                    @Override // java.lang.Runnable
                    public void run() throws IllegalStateException, Resources.NotFoundException {
                        if (goToDrafts) {
                            CameraCaptureActivity.this.showDraftsOrCreateNewDraft(CameraCaptureActivity.this.mRecordController.getDraft());
                        } else {
                            CameraCaptureActivity.this.finish();
                        }
                    }
                });
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDraftsOrCreateNewDraft(Draft draft) throws IllegalStateException, Resources.NotFoundException {
        try {
            DraftsManager.loadDrafts(this);
        } catch (Exception e) {
        }
        boolean hasDrafts = DraftsManager.getDraftsCount() > 0;
        if (!hasDrafts) {
            Toast.makeText(this, R.string.no_drafts, 0).show();
            this.mRecordController.createFreshDraft();
            return;
        }
        this.mRecordingModeState = RecordingModeState.DRAFTS;
        this.mCaptureMode.hide();
        this.mEditMode.hide();
        this.mMultiEditMode.hide();
        this.mDraftsMode.show(draft);
    }

    @Override // co.vine.android.RecordingNavigationController
    public void goToCaptureFromDrafts(Draft draft) throws IllegalStateException {
        this.mRecordingModeState = RecordingModeState.CAPTURE;
        if (draft != null) {
            this.mRecordController.setDraft(draft);
        } else {
            this.mRecordController.createFreshDraft();
        }
        this.mDraftsMode.hide();
        this.mEditMode.hide();
        this.mMultiEditMode.hide();
        this.mCaptureMode.show();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() throws IllegalStateException {
        onBackPressed(null);
    }

    public void onBackPressed(View unused) throws IllegalStateException {
        if (this.mRecordingModeState == RecordingModeState.EDIT) {
            this.mEditMode.onBackPressed();
            return;
        }
        if (this.mRecordingModeState == RecordingModeState.CAPTURE) {
            this.mCaptureMode.onBackPressed();
            return;
        }
        if (this.mRecordingModeState == RecordingModeState.DRAFTS) {
            this.mDraftsMode.onBackPressed();
            return;
        }
        if (this.mRecordingModeState == RecordingModeState.PREVIEW) {
            this.mRecordingModeState = RecordingModeState.CAPTURE;
            this.mPreviewFragment.previewToRecord(this, false);
            this.mEditMode.hide();
            this.mMultiEditMode.hide();
            this.mDraftsMode.hide();
            this.mCaptureMode.show();
            removePreviewFragment();
        }
    }

    private void removePreviewFragment() {
        getSupportFragmentManager().beginTransaction().remove(this.mPreviewFragment).commit();
    }

    public void returnFromPreview(boolean startWithEdit) throws IllegalStateException, IllegalAccessException, Resources.NotFoundException, IllegalArgumentException, InvocationTargetException {
        removePreviewFragment();
        if (startWithEdit) {
            this.mRecordingModeState = RecordingModeState.EDIT;
            this.mCaptureMode.hide();
            this.mDraftsMode.hide();
            if (ClientFlagsHelper.useNewEditScreen(this)) {
                this.mMultiEditMode.show();
            } else {
                this.mEditMode.show();
            }
            this.mReturnToPreviewFromEdit = true;
            return;
        }
        this.mRecordingModeState = RecordingModeState.CAPTURE;
        this.mEditMode.hide();
        this.mMultiEditMode.hide();
        this.mDraftsMode.hide();
        this.mCaptureMode.show();
    }

    public void onFinishPressed(View v) {
        prepareToGoToPreview();
    }

    @Override // co.vine.android.recordingui.RecordStateHolder
    public PostShareParameters getPostShareParameters() {
        return this.mPostShareParameters;
    }

    @Override // co.vine.android.recordingui.RecordStateHolder
    public void setPostShareParameters(PostShareParameters params) {
        this.mPostShareParameters = params;
    }

    public static Intent getRemixVideoIntent(Context context, String vineLocalPath, String sourcePostId) {
        Intent intent = new Intent(context, (Class<?>) CameraCaptureActivity.class);
        intent.setData(new Uri.Builder().scheme("file").path(vineLocalPath).build());
        intent.putExtra("extra_source_post_id", sourcePostId);
        return intent;
    }
}
