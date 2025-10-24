package co.vine.android.plugin;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import co.vine.android.player.SdkVideoView;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.RecordSegment;
import co.vine.android.recorder.camera.CameraSetting;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: classes.dex */
public class BaseRecorderPluginManager<T extends BasicVineRecorder> extends BasePluginManager<RecorderPlugin<?, T>> implements RecorderPluginManager<T> {
    private WeakReference<T> mRecorder;

    public BaseRecorderPluginManager() {
    }

    public BaseRecorderPluginManager(Collection<RecorderPlugin<?, T>> plugins) {
        super(plugins);
    }

    public void setRecorder(T recorder) {
        this.mRecorder = new WeakReference<>(recorder);
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onRecorderSet(recorder);
        }
    }

    @Override // co.vine.android.plugin.RecorderPluginManager
    public T getRecorder() {
        if (this.mRecorder == null) {
            return null;
        }
        T recorder = this.mRecorder.get();
        return recorder;
    }

    public void createLayouts(ViewGroup parentView, LayoutInflater inflater, Fragment fragment) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.createLayout(parentView, inflater, fragment);
        }
    }

    public boolean onBackPressed(boolean isEditing) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            if (plugin.onBackButtonPressed(isEditing)) {
                return true;
            }
        }
        return false;
    }

    public void onStartRelativeTime(T recorder) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin<?, T> plugin = (RecorderPlugin) it.next();
            plugin.onStartRelativeTime(recorder);
        }
    }

    public void onEndRelativeTime(T recorder) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin<?, T> plugin = (RecorderPlugin) it.next();
            plugin.onEndRelativeTime(recorder);
        }
    }

    public void onCameraReady(boolean frontFacing, boolean autoFocusSet) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onCameraReady(frontFacing, autoFocusSet);
        }
    }

    public boolean onSetEditMode(boolean editing, boolean hasData) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            if (plugin.onSetEditMode(editing, hasData)) {
                return true;
            }
        }
        return false;
    }

    public void onStartEditMode() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onStartEditMode();
        }
    }

    @Override // co.vine.android.plugin.RecorderPluginManager
    public void onStartDrafts() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onStartDrafts();
        }
    }

    public void onAutoFocus(int x, int y) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onAutoFocus(x, y);
        }
    }

    public void onSessionSwapped() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onSessionSwapped();
        }
    }

    public void onHideDrafts() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onHideDrafts();
        }
    }

    public void onShowDrafts() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onShowDrafts();
        }
    }

    public void setMessagingMode(boolean isMessaging) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.setMessagingMode(isMessaging);
        }
    }

    public void onResumeCameraAsyncTaskPostExecute() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onResumeCameraAsyncTaskPostExecute();
        }
    }

    public void onResumeAsyncTaskPostExecute() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onResumeAsyncTaskPostExecute();
        }
    }

    public void onZoomUpdated(int zoom, boolean stopped) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onZoomUpdated(zoom, stopped);
        }
    }

    public void onOfferLastFrame(byte[] array, CameraSetting cameraSetting) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onOfferLastFrame(array, cameraSetting);
        }
    }

    public void onOfferLastFrame(Bitmap buffer, CameraSetting cameraSetting) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onOfferLastFrame(buffer, cameraSetting);
        }
    }

    public void onSegmentDataChanged(ArrayList<RecordSegment> editedSegments) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onSegmentDataChanged(editedSegments);
        }
    }

    public void onIndividualSegmentClicked(RecordSegment segment, SdkVideoView player) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onIndividualSegmentClicked(segment, player);
        }
    }

    public void onOnboardingStepFinished() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onOnboardingStepFinished();
        }
    }

    public void onAnimateEditModeControlsInUI() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onAnimateEditModeControlsInUI();
        }
    }

    public void onAnimateEditModeControlsOutUI() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onAnimateEditModeControlsOutUI();
        }
    }

    public boolean canEdit() {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            if (!plugin.canEdit()) {
                return false;
            }
        }
        return true;
    }

    public void onAdjustingLayoutParamsComplete(int topMaskHeightPx, int size) {
        Iterator it = this.mPlugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = (RecorderPlugin) it.next();
            plugin.onAdjustingLayoutParamsComplete(topMaskHeightPx, size);
        }
    }
}
