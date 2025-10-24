package co.vine.android.plugin;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.VineRecorder;
import co.vine.android.util.Util;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avcodec;

/* loaded from: classes.dex */
public class SecretModePlugin extends BaseRecorderPlugin<View, VineRecorder> {
    private static final int[] SECRETS = {25, 24, 24, 24, 24, 24, 25};
    private boolean isSecretModeOn;
    private int mCurrentSecret;
    private int mCurrentSteadyCount;
    private SecretModes mSecretMode;
    private Toast mSecretToast;

    private enum SecretModes {
        STOP_MOTION,
        WHITE_BALANCE,
        EXPOSURE,
        SCENE_MODE,
        COLOR_EFFECTS,
        ANTI_BANDING,
        IMAGE_STAB
    }

    static /* synthetic */ int access$008(SecretModePlugin x0) {
        int i = x0.mCurrentSteadyCount;
        x0.mCurrentSteadyCount = i + 1;
        return i;
    }

    public SecretModePlugin() {
        super("Secret Mode");
        this.mCurrentSecret = 0;
        this.isSecretModeOn = false;
        this.mCurrentSteadyCount = 0;
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean zoomingIn;
        boolean zooming;
        switch (keyCode) {
            case 24:
            case avcodec.AV_CODEC_ID_MSS2 /* 168 */:
                zoomingIn = true;
                zooming = true;
                break;
            case 25:
            case avcodec.AV_CODEC_ID_VP9 /* 169 */:
                zoomingIn = false;
                zooming = true;
                break;
            default:
                zooming = false;
                zoomingIn = false;
                break;
        }
        return onKeyDown(keyCode, zooming, zoomingIn);
    }

    public boolean onKeyDown(int keyCode, boolean zooming, boolean zoomingIn) {
        if (!this.isSecretModeOn) {
            if (keyCode == SECRETS[this.mCurrentSecret] && SLog.sLogsOn) {
                this.mCurrentSecret++;
                if (this.mCurrentSecret >= SECRETS.length) {
                    if (this.mActivity != null) {
                        enableSecretMode(Util.showCenteredToast(this.mActivity, "Secret mode enabled."));
                    }
                    this.isSecretModeOn = true;
                    this.mCurrentSecret = 0;
                    return true;
                }
            } else {
                this.mCurrentSecret = 0;
            }
        } else if (zooming) {
            VineRecorder recorder = getRecorder();
            if (recorder == null) {
                return true;
            }
            if (!recorder.isEditing()) {
                if (zoomingIn) {
                    doSecretVolumeUp();
                    return true;
                }
                doSecretVolumeDown();
                return true;
            }
            Util.showCenteredToast(this.mActivity, "Reversed.");
            reverseFrames();
            return true;
        }
        return false;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public boolean onBackButtonPressed(boolean isEditing) {
        if (!this.isSecretModeOn) {
            return false;
        }
        changeSecretMode();
        return true;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin
    public View onLayout(ViewGroup parent, LayoutInflater inflater, Fragment fragment) {
        return null;
    }

    public void changeSecretMode() {
        if (this.mSecretMode != null) {
            SecretModes[] values = SecretModes.values();
            this.mSecretMode = values[(this.mSecretMode.ordinal() + 1) % values.length];
            this.mSecretToast.setText("Secret Mode: " + this.mSecretMode.name());
            this.mSecretToast.show();
        }
    }

    public void enableSecretMode(Toast toast) {
        this.mSecretToast = toast;
        this.mSecretMode = SecretModes.STOP_MOTION;
    }

    public void doSecretVolumeUp() {
        VineRecorder recorder = getRecorder();
        if (recorder != null && recorder.canKeepRecording()) {
            switch (this.mSecretMode) {
                case STOP_MOTION:
                    doOneFrame();
                    break;
                case WHITE_BALANCE:
                    recorder.modifyWhiteBalance(true);
                    break;
                case EXPOSURE:
                    recorder.modifyExposure(true);
                    break;
                case SCENE_MODE:
                    recorder.modifySceneMode(true);
                    break;
                case COLOR_EFFECTS:
                    recorder.modifyColorEffects(true);
                    break;
                case ANTI_BANDING:
                    recorder.modifyAntiBanding(true);
                    break;
                case IMAGE_STAB:
                    recorder.switchImageStabilization();
                    break;
            }
        }
    }

    public void doOneFrame() {
        VineRecorder recorder = getRecorder();
        if (recorder != null && recorder.canKeepRecording()) {
            recorder.doOneFrame();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStopMotion() {
        Handler handler;
        BasicVineRecorder recorder = getRecorder();
        if (recorder != null && recorder.canKeepRecording() && (handler = getHandler()) != null && this.mCurrentSteadyCount < 40) {
            handler.postDelayed(new Runnable() { // from class: co.vine.android.plugin.SecretModePlugin.1
                @Override // java.lang.Runnable
                public void run() {
                    SecretModePlugin.this.doOneFrame();
                    SecretModePlugin.access$008(SecretModePlugin.this);
                    SecretModePlugin.this.doStopMotion();
                }
            }, 50L);
        }
    }

    public void reverseFrames() {
        VineRecorder recorder = getRecorder();
        if (recorder != null) {
            recorder.reverseFrames();
        }
    }

    public void doSecretVolumeDown() {
        VineRecorder recorder = getRecorder();
        if (recorder != null) {
            switch (this.mSecretMode) {
                case STOP_MOTION:
                    doStopMotion();
                    break;
                case WHITE_BALANCE:
                    recorder.modifyWhiteBalance(false);
                    break;
                case EXPOSURE:
                    recorder.modifyExposure(false);
                    break;
                case SCENE_MODE:
                    recorder.modifySceneMode(false);
                    break;
                case COLOR_EFFECTS:
                    recorder.modifyColorEffects(false);
                    break;
                case ANTI_BANDING:
                    recorder.modifyAntiBanding(false);
                    break;
                case IMAGE_STAB:
                    recorder.switchImageStabilization();
                    break;
            }
        }
    }
}
