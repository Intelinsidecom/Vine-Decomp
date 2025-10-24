package co.vine.android.plugin;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import co.vine.android.R;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.VineRecorder;
import co.vine.android.recorder.camera.CameraManager;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.views.FakeKeyEventTouchListener;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avcodec;

/* loaded from: classes.dex */
public class ZoomPlugin extends BaseRecorderPlugin<View, VineRecorder> {
    private int mLastUpdatedZoom;
    private SeekBar mZoomSeek;
    private View mZoomSeekContainer;
    private final SeekBar.OnSeekBarChangeListener mZoomSeekListener;
    private final Runnable mZoomUpdateRunnable;

    public ZoomPlugin() {
        super("Zoom");
        this.mLastUpdatedZoom = -1;
        this.mZoomSeekListener = new ZoomSeekChangeListener();
        this.mZoomUpdateRunnable = new Runnable() { // from class: co.vine.android.plugin.ZoomPlugin.1
            @Override // java.lang.Runnable
            public void run() {
                if (ZoomPlugin.this.mZoomSeek != null && ZoomPlugin.this.mLastUpdatedZoom >= 0) {
                    SLog.d("Zoom progress updated: " + ZoomPlugin.this.mLastUpdatedZoom);
                    ZoomPlugin.this.mZoomSeek.setProgress(ZoomPlugin.this.mLastUpdatedZoom - 1);
                }
                ZoomPlugin.this.requestZoomProgressUpdate();
            }
        };
    }

    private static class ZoomSeekChangeListener implements SeekBar.OnSeekBarChangeListener {
        private ZoomSeekChangeListener() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            CameraManager.getInstance().modifyZoom(seekBar.getProgress() + 1);
        }
    }

    private static class PlusMinusTouchListener extends FakeKeyEventTouchListener {
        private final int mColor;
        private final int mDrawableId;
        private final Resources mRes;
        private final ImageView mTarget;

        /* JADX WARN: Multi-variable type inference failed */
        public PlusMinusTouchListener(Fragment fragment, ImageView target, int res) throws Resources.NotFoundException {
            super(fragment.getActivity(), res == R.drawable.zoom_minus ? avcodec.AV_CODEC_ID_VP9 : avcodec.AV_CODEC_ID_MSS2);
            this.mDrawableId = res;
            this.mRes = fragment.getResources();
            this.mTarget = target;
            if (fragment instanceof RecorderPluginSupportedFragment) {
                this.mColor = ((RecorderPluginSupportedFragment) fragment).getColor(true);
            } else {
                this.mColor = 0;
            }
            changeColor(1);
        }

        @Override // co.vine.android.views.FakeKeyEventTouchListener, android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) throws Resources.NotFoundException {
            changeColor(event.getAction());
            return super.onTouch(v, event);
        }

        private void changeColor(int action) throws Resources.NotFoundException {
            switch (action) {
                case 0:
                    ViewUtil.fillColor(this.mRes, this.mColor, this.mDrawableId, this.mTarget);
                    this.mTarget.setAlpha(1.0f);
                    break;
                case 1:
                    ViewUtil.fillColor(this.mRes, this.mColor, this.mDrawableId, this.mTarget);
                    this.mTarget.setAlpha(0.35f);
                    break;
            }
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin
    public View onLayout(ViewGroup parent, LayoutInflater inflater, Fragment fragment) {
        ViewGroup subOptionsContainer = (ViewGroup) fragment.getView().findViewById(R.id.recording_sub_options);
        this.mZoomSeekContainer = inflater.inflate(R.layout.plugin_zoom_seek_container, subOptionsContainer, false);
        subOptionsContainer.addView(this.mZoomSeekContainer);
        ViewGroup trigger = (ViewGroup) fragment.getView().findViewById(R.id.zoom_minus);
        trigger.setOnTouchListener(new PlusMinusTouchListener(fragment, (ImageView) trigger.getChildAt(0), R.drawable.zoom_minus));
        ViewGroup trigger2 = (ViewGroup) fragment.getView().findViewById(R.id.zoom_plus);
        trigger2.setOnTouchListener(new PlusMinusTouchListener(fragment, (ImageView) trigger2.getChildAt(0), R.drawable.zoom_plus));
        View container = fragment.getView().findViewById(R.id.zoom_seek);
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.width = (int) ((SystemUtil.getDisplaySize(fragment.getActivity()).x * 0.8d) - ((fragment.getResources().getDisplayMetrics().density * 2.0f) * 36.0f));
        container.setLayoutParams(params);
        return null;
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
        return onKeyDown(zooming, zoomingIn);
    }

    public boolean onKeyDown(boolean zooming, boolean zoomingIn) {
        VineRecorder recorder = getRecorder();
        if (recorder == null || !zooming || recorder.isEditing()) {
            return false;
        }
        recorder.modifyZoom(zoomingIn);
        requestZoomProgressUpdate();
        return true;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onResumeCameraAsyncTaskPostExecute() {
        invalidateZoomSlider();
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onResumeAsyncTaskPostExecute() {
        invalidateZoomSlider();
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onPause() {
        super.onPause();
        this.mZoomSeek = null;
    }

    public void requestZoomProgressUpdate() {
        Handler handler;
        if (CameraManager.getInstance().isSmoothZoomingSupported() && (handler = getHandler()) != null) {
            handler.postDelayed(this.mZoomUpdateRunnable, 30L);
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onZoomUpdated(int zoom, boolean stopped) {
        this.mLastUpdatedZoom = zoom;
        SLog.d("Zoom updated: " + zoom);
        if (this.mZoomSeek != null && stopped) {
            this.mLastUpdatedZoom = -1;
            SLog.d("Zoom progress updated: " + zoom);
            this.mZoomSeek.setProgress(zoom - 1);
            Handler handler = getHandler();
            if (handler != null) {
                handler.removeCallbacks(this.mZoomUpdateRunnable);
            }
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onResume(Activity activity) {
        super.onResume(activity);
        this.mZoomSeek = (SeekBar) activity.findViewById(R.id.zoom_seek);
        this.mZoomSeek.setProgress(0);
        this.mZoomSeek.setOnSeekBarChangeListener(this.mZoomSeekListener);
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean zooming;
        VineRecorder r;
        switch (keyCode) {
            case 24:
            case avcodec.AV_CODEC_ID_MSS2 /* 168 */:
                zooming = true;
                break;
            case 25:
            case avcodec.AV_CODEC_ID_VP9 /* 169 */:
                zooming = true;
                break;
            default:
                zooming = false;
                break;
        }
        if (zooming && (r = getRecorder()) != null && !r.isEditing()) {
            r.stopZoom(this.mZoomUpdateRunnable);
            return true;
        }
        return false;
    }

    private void invalidateZoomSlider() {
        VineRecorder r = getRecorder();
        RecordConfigUtils.RecordConfig config = null;
        if (r != null) {
            config = r.getConfig();
        }
        if (config != null && this.mZoomSeekContainer != null) {
            if (config.isZoomButtonEnabled && this.mZoomSeek != null) {
                this.mZoomSeekContainer.setVisibility(0);
                if (config.zoomEnabled && CameraManager.getInstance().isZoomSupported()) {
                    this.mZoomSeek.setEnabled(true);
                    this.mZoomSeek.setMax(CameraManager.getInstance().getMaxZoom() - 1);
                    return;
                } else {
                    this.mZoomSeekContainer.setVisibility(8);
                    this.mZoomSeek.setProgress(0);
                    this.mZoomSeek.setEnabled(false);
                    return;
                }
            }
            this.mZoomSeekContainer.setVisibility(8);
        }
    }
}
