package co.vine.android.recorder2;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import co.vine.android.recorder2.camera.CameraController;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class SetCameraSurfaceTextureHandler extends Handler {
    private WeakReference<CameraController> mWeakCameraController;

    public SetCameraSurfaceTextureHandler(CameraController cameraController) {
        this.mWeakCameraController = new WeakReference<>(cameraController);
    }

    public void invalidateHandler() {
        this.mWeakCameraController.clear();
    }

    @Override // android.os.Handler
    public void handleMessage(Message inputMessage) throws IOException {
        int what = inputMessage.what;
        SLog.d("CameraHandler [" + this + "]: what=" + what);
        CameraController manager = this.mWeakCameraController.get();
        if (manager == null) {
            SLog.d("CameraHandler.handleMessage: controller is null");
            return;
        }
        switch (what) {
            case 0:
                manager.handleSetSurfaceTexture((SurfaceTexture) inputMessage.obj);
                return;
            default:
                throw new RuntimeException("unknown msg " + what);
        }
    }
}
