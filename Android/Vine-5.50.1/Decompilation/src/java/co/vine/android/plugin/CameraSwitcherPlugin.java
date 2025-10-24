package co.vine.android.plugin;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import co.vine.android.R;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.util.ViewUtil;

/* loaded from: classes.dex */
public class CameraSwitcherPlugin extends BaseToolPlugin<Button> {
    public CameraSwitcherPlugin() {
        super("Camera Switcher");
    }

    private static class CameraSwitcherPluginTouchListener implements View.OnTouchListener {
        private final int color;
        private final Resources res;

        public CameraSwitcherPluginTouchListener(Resources res, int color) {
            this.res = res;
            this.color = color;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) throws Resources.NotFoundException {
            switch (event.getAction()) {
                case 0:
                    ViewUtil.fillColor(this.res, this.color, R.drawable.ic_front_facing, v);
                    v.setAlpha(1.0f);
                    break;
                case 1:
                    ViewUtil.fillColor(this.res, this.color, R.drawable.ic_front_facing, v);
                    v.setAlpha(0.35f);
                    break;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.plugin.BaseToolPlugin
    public Button onLayoutInflated(LinearLayout view, LayoutInflater inflater, Fragment fragment) throws Resources.NotFoundException {
        Button button = (Button) inflater.inflate(R.layout.plugin_tool_regular_button, (ViewGroup) view, false);
        button.setBackgroundResource(R.drawable.ic_front_facing);
        if (fragment instanceof RecorderPluginSupportedFragment) {
            Resources res = fragment.getResources();
            ViewUtil.fillColor(res, this.mPrimaryColor, R.drawable.ic_front_facing, button);
            button.setOnTouchListener(new CameraSwitcherPluginTouchListener(res, this.mPrimaryColor));
        } else {
            button.setOnTouchListener(BaseToolPlugin.createOnToolTouchedListener());
        }
        button.setAlpha(0.35f);
        return button;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onCameraReady(boolean frontFacing, boolean autoFocusSet) {
        Button child = getInflatedChild();
        if (child != null) {
            child.setAlpha(frontFacing ? 1.0f : 0.35f);
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onResume(Activity activity) {
        super.onResume(activity);
        LinearLayout view = getView();
        BasicVineRecorder recorder = getRecorder();
        if (view != null && recorder != null) {
            if (!RecordConfigUtils.getGenericConfig(activity).cameraSwitchEnabled) {
                view.setVisibility(8);
            } else {
                view.setOnClickListener(this);
            }
            if (recorder.canSwitchCamera() && view.getVisibility() != 8) {
                view.setVisibility(0);
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        BasicVineRecorder recorder = getRecorder();
        if (recorder != null) {
            recorder.switchCamera();
        }
    }
}
