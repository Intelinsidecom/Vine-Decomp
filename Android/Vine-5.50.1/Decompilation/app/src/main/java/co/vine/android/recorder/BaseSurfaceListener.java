package co.vine.android.recorder;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import co.vine.android.recorder.SurfaceController;
import co.vine.android.recorder.camera.PreviewManager;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class BaseSurfaceListener implements SurfaceController.SurfaceListener {
    private final ParentHolder mParentHolder;
    private final RecordController mRecordController;
    private final RecordState mState;

    public BaseSurfaceListener(ParentHolder parentHolder, RecordController recordController, RecordState state) {
        this.mParentHolder = parentHolder;
        this.mRecordController = recordController;
        this.mState = state;
    }

    @Override // co.vine.android.recorder.SurfaceController.SurfaceListener
    public void onSurfaceUpdated(SurfaceTexture surfaceTexture, Surface surface) {
        PreviewManager previewManager = PreviewManager.getInstance();
        if (!this.mState.isStarted() && previewManager.isPreviewing()) {
            SLog.d("Surface updated. mCameraManager is previewing...");
            try {
                previewManager.stopPreview();
            } catch (Exception e) {
            }
            if (!previewManager.isPreviewing()) {
                this.mRecordController.startPreview();
            }
        }
    }

    @Override // co.vine.android.recorder.SurfaceController.SurfaceListener
    public void onSurfaceCreated(SurfaceTexture surfaceTexture, Surface surface) {
        this.mParentHolder.getParent().onSurfaceReady(this.mRecordController);
    }

    @Override // co.vine.android.recorder.SurfaceController.SurfaceListener
    public void onSurfaceDestroyed(SurfaceTexture surfaceTexture, Surface surface) {
    }
}
