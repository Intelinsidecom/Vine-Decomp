package co.vine.android.recorder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import co.vine.android.service.ResourceService;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class RecordingActivityHelper {
    private ServiceConnection mConnection = new ServiceConnection() { // from class: co.vine.android.recorder.RecordingActivityHelper.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            SLog.i("Camera service connected.");
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            SLog.i("Camera service disconnected.");
        }
    };
    public boolean mIsBound;

    public void bindCameraService(Activity activity) {
        if (!this.mIsBound) {
            if (activity.bindService(new Intent(activity, (Class<?>) ResourceService.class), this.mConnection, 1)) {
                this.mIsBound = true;
                return;
            } else {
                CrashUtil.log("Failed to bind camera service.");
                return;
            }
        }
        SLog.d("Camera service already binded. ");
    }

    public void unBindCameraService(Activity activity) {
        if (this.mIsBound) {
            activity.unbindService(this.mConnection);
            this.mIsBound = false;
        }
    }
}
