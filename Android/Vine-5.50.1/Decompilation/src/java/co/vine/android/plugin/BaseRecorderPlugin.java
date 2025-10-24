package co.vine.android.plugin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.player.SdkVideoView;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.RecordSegment;
import co.vine.android.recorder.camera.CameraSetting;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class BaseRecorderPlugin<T extends View, K extends BasicVineRecorder> extends BasePlugin implements RecorderPlugin<T, K> {
    protected Activity mActivity;
    private WeakReference<Fragment> mFragment;
    private WeakReference<T> mView;

    public abstract T onLayout(ViewGroup viewGroup, LayoutInflater layoutInflater, Fragment fragment);

    public BaseRecorderPlugin(String name) {
        super(name);
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void createLayout(ViewGroup parent, LayoutInflater inflater, Fragment fragment) {
        this.mFragment = new WeakReference<>(fragment);
        View viewOnLayout = onLayout(parent, inflater, fragment);
        if (viewOnLayout != null) {
            parent.addView(viewOnLayout);
            this.mView = new WeakReference<>(viewOnLayout);
        } else {
            this.mView = null;
        }
    }

    public Fragment getFragment() {
        if (this.mFragment != null) {
            return this.mFragment.get();
        }
        return null;
    }

    public T getView() {
        if (this.mView != null) {
            return this.mView.get();
        }
        return null;
    }

    @Override // co.vine.android.plugin.BasePlugin
    public Handler getHandler() {
        if (this.mManager != null) {
            return this.mManager.getHandler();
        }
        return null;
    }

    public K getRecorder() {
        RecorderPluginManager<K> manager = getManager();
        if (manager == null) {
            return null;
        }
        return (K) manager.getRecorder();
    }

    public RecorderPluginManager<K> getManager() {
        return (RecorderPluginManager) this.mManager;
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onActivityCreated(Activity activity) {
        super.onActivityCreated(activity);
        this.mActivity = activity;
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onResume(Activity activity) {
        this.mActivity = activity;
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public void onPause() {
        this.mActivity = null;
    }

    @Override // co.vine.android.plugin.BasePlugin, co.vine.android.plugin.Plugin
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return false;
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public boolean onBackButtonPressed(boolean isEditing) {
        return false;
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onCameraReady(boolean frontFacing, boolean autoFocusSet) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public boolean onSetEditMode(boolean editing, boolean hasData) {
        return false;
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onStartEditMode() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onStartDrafts() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onAutoFocus(int x, int y) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onSessionSwapped() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onStartRelativeTime(K recorder) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onEndRelativeTime(K recorder) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onHideDrafts() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onShowDrafts() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onRecorderSet(BasicVineRecorder recorder) {
    }

    public void onDraftUpgradeNumberChanged(int count) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void setMessagingMode(boolean isMessaging) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onResumeCameraAsyncTaskPostExecute() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onResumeAsyncTaskPostExecute() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onZoomUpdated(int zoom, boolean stopped) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onOfferLastFrame(byte[] array, CameraSetting cameraSetting) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onSegmentDataChanged(ArrayList<RecordSegment> editedSegments) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onOfferLastFrame(Bitmap bitmap, CameraSetting cameraSetting) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onIndividualSegmentClicked(RecordSegment segment, SdkVideoView player) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onOnboardingStepFinished() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onAnimateEditModeControlsInUI() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onAnimateEditModeControlsOutUI() {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public boolean canEdit() {
        return true;
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onAdjustingLayoutParamsComplete(int topMaskHeightPx, int size) {
    }
}
