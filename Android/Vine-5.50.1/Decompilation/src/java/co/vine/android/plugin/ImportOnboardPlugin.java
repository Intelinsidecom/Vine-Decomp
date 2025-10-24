package co.vine.android.plugin;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import co.vine.android.CameraOnboardHelper;
import co.vine.android.R;
import co.vine.android.animation.SimpleAnimationListener;
import co.vine.android.recorder.VineRecorder;

/* loaded from: classes.dex */
public class ImportOnboardPlugin extends BaseRecorderPlugin<View, VineRecorder> {
    private ViewGroup mImportOnboardOverlay;
    private final Animation.AnimationListener mImportOnboardingFadeListener;
    private boolean mIsFadingOutAlready;
    private boolean mIsOnboarding;

    public ImportOnboardPlugin() {
        super("Import Onboard");
        this.mImportOnboardingFadeListener = new SimpleAnimationListener() { // from class: co.vine.android.plugin.ImportOnboardPlugin.2
            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                ImportOnboardPlugin.this.mIsFadingOutAlready = true;
            }

            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (ImportOnboardPlugin.this.mImportOnboardOverlay != null) {
                    ImportOnboardPlugin.this.mImportOnboardOverlay.setVisibility(8);
                    ImportOnboardPlugin.this.mImportOnboardOverlay.setOnTouchListener(null);
                }
            }
        };
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin
    public View onLayout(ViewGroup parent, LayoutInflater inflater, Fragment fragment) {
        this.mActivity = fragment.getActivity();
        this.mIsOnboarding = !CameraOnboardHelper.getHasShownOverlay(this.mActivity);
        if (this.mIsOnboarding) {
            ViewGroup rootView = (ViewGroup) fragment.getView().findViewById(R.id.root_layout);
            this.mImportOnboardOverlay = (ViewGroup) inflater.inflate(R.layout.plugin_import_onboard, rootView, false);
            rootView.addView(this.mImportOnboardOverlay);
            CameraOnboardHelper.setHasShownOverlay(this.mActivity, true);
            return null;
        }
        return null;
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onPause() throws Resources.NotFoundException {
        super.onPause();
        if (this.mIsOnboarding) {
            fadeOutOnboardOverlay();
            CameraOnboardHelper.setHasShownOverlay(this.mActivity, true);
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onActivityCreated(Activity activity) {
        super.onActivityCreated(activity);
        if (this.mIsOnboarding) {
            this.mImportOnboardOverlay.setVisibility(0);
            this.mIsFadingOutAlready = false;
            this.mImportOnboardOverlay.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.plugin.ImportOnboardPlugin.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) throws Resources.NotFoundException {
                    ImportOnboardPlugin.this.fadeOutOnboardOverlay();
                    return true;
                }
            });
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public boolean onSetEditMode(boolean editing, boolean hasData) throws Resources.NotFoundException {
        if (editing) {
            fadeOutOnboardOverlay();
        }
        return super.onSetEditMode(editing, hasData);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeOutOnboardOverlay() throws Resources.NotFoundException {
        if (this.mIsOnboarding && this.mImportOnboardOverlay != null && this.mImportOnboardOverlay.getVisibility() == 0 && !this.mIsFadingOutAlready) {
            if (this.mActivity != null) {
                Animation fadeOut = AnimationUtils.loadAnimation(this.mActivity, R.anim.fade_out);
                fadeOut.setAnimationListener(this.mImportOnboardingFadeListener);
                this.mImportOnboardOverlay.startAnimation(fadeOut);
                return;
            }
            this.mImportOnboardingFadeListener.onAnimationEnd(null);
        }
    }

    @Override // co.vine.android.plugin.BaseRecorderPlugin, co.vine.android.plugin.RecorderPlugin
    public void onOnboardingStepFinished() throws Resources.NotFoundException {
        fadeOutOnboardOverlay();
    }
}
