package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordSessionManager;
import co.vine.android.recordingui.CameraCaptureActivity;
import co.vine.android.service.ResourceService;
import co.vine.android.service.VineUploadService;
import co.vine.android.util.UploadManager;
import co.vine.android.util.Util;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;

/* loaded from: classes.dex */
public class AppImpl implements AppInterface {
    private static AppImpl sInstance;

    private AppImpl() {
    }

    public static AppImpl getInstance() {
        if (sInstance == null) {
            sInstance = new AppImpl();
        }
        return sInstance;
    }

    @Override // co.vine.android.AppInterface
    public Intent getRecordingIntent(Activity activity, int flags, String callingActivity) {
        return AbstractRecordingActivity.getIntentForGeneric(activity, flags, callingActivity);
    }

    public Intent getClearNotificationsIntent(Context c) {
        return VineUploadService.getClearNotificationsIntent(c);
    }

    public Intent getDiscardAllIntent(Context c) {
        return VineUploadService.getDiscardAllIntent(c);
    }

    public Intent getNotifyFailedIntent(Context c) {
        return VineUploadService.getNotifyFailedIntent(c);
    }

    public void startCapture(Activity activity) {
        Intent intent;
        Context context;
        SharedPreferences preferences;
        String tag = "Base options: " + activity.getClass().getName();
        boolean useNewRecorder = false;
        if (Util.recorder2EnabledAndSupported(activity) && !(useNewRecorder = (preferences = Util.getDefaultSharedPrefs((context = activity.getApplicationContext()))).getBoolean("useNewRecorder", false))) {
            try {
                if (RecordSessionManager.getNumberOfValidSessions(context, RecordSessionManager.getCurrentVersion(context)) == 0) {
                    useNewRecorder = true;
                    preferences.edit().putBoolean("useNewRecorder", true).apply();
                }
            } catch (IOException e) {
                SLog.w("Failed to load legacy drafts");
            }
        }
        if (useNewRecorder) {
            intent = new Intent(activity, (Class<?>) CameraCaptureActivity.class);
        } else {
            intent = AbstractRecordingActivity.getIntentForGeneric(activity, 131072, tag);
        }
        Util.startActionOnRecordingAvailable(activity, intent, 0);
    }

    public void appendDebugInfo(Context c, StringBuilder debugInfo, boolean hideSensitiveData) {
        RecordConfigUtils.RecordConfig config = RecordConfigUtils.getGenericConfig(c);
        debugInfo.append("\n\nDefault Recording Configuration: ");
        debugInfo.append("\nRecording enabled: " + config.recordingEnabled);
        debugInfo.append("\nZoom enabled: " + config.zoomEnabled);
        debugInfo.append("\nCamera Switch enabled: " + config.cameraSwitchEnabled);
        debugInfo.append("\nFlash Switch enabled: " + config.flashSwitchEnabled);
        debugInfo.append("\nBuffer Pre-allocation enabled: " + config.preAllocateBuffer);
        debugInfo.append("\nBuffer count: " + config.bufferCount);
        debugInfo.append("\nPreview width: " + config.previewWidth);
        debugInfo.append("\nTarget Frame Rate: " + config.targetFrameRate);
        if (!hideSensitiveData) {
            debugInfo.append("\nProcess path: " + config.processDir.getPath());
        }
        debugInfo.append("\nTarget Size: " + config.targetSize);
        RecordConfigUtils.RecordConfig config2 = RecordConfigUtils.getGenericConfig(c);
        debugInfo.append("\n\nDefault Message Recording Configuration: ");
        debugInfo.append("\nBuffer count: " + config2.bufferCount);
        debugInfo.append("\nPreview width: " + config2.previewWidth);
        debugInfo.append("\nPreview height: " + config2.previewHeight);
        debugInfo.append("\nTarget Frame Rate: " + config2.targetFrameRate);
        debugInfo.append("\nTarget Size: " + config2.targetSize);
    }

    public boolean doAddWidget(Context c, AccountManager am, Account acc) {
        return RecordConfigUtils.loadWasEverSuccessful(c) && VineAccountHelper.needsAddwidget(am, acc);
    }

    public void startUploadsListActivity(Activity a) {
        UploadsListActivity.start(a);
    }

    public void startCameraService(Activity parent) {
        parent.startService(new Intent(parent, (Class<?>) ResourceService.class));
    }

    public void setupWidget(Activity a) {
        CameraWidgetConfigureActivity.setupShortcut(a);
    }

    public void clearUploadCaptchas(Context applicationContext) {
        UploadManager.clearUploadCaptchas(applicationContext);
    }
}
