package co.vine.android.plugin;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.player.SdkVideoView;
import co.vine.android.recorder.BasicVineRecorder;
import co.vine.android.recorder.RecordSegment;
import co.vine.android.recorder.camera.CameraSetting;
import java.util.ArrayList;

/* loaded from: classes.dex */
public interface RecorderPlugin<T extends View, K extends BasicVineRecorder> extends Plugin {
    boolean canEdit();

    void createLayout(ViewGroup viewGroup, LayoutInflater layoutInflater, Fragment fragment);

    void onAdjustingLayoutParamsComplete(int i, int i2);

    void onAnimateEditModeControlsInUI();

    void onAnimateEditModeControlsOutUI();

    void onAutoFocus(int i, int i2);

    boolean onBackButtonPressed(boolean z);

    void onCameraReady(boolean z, boolean z2);

    void onEndRelativeTime(K k);

    void onHideDrafts();

    void onIndividualSegmentClicked(RecordSegment recordSegment, SdkVideoView sdkVideoView);

    void onOfferLastFrame(Bitmap bitmap, CameraSetting cameraSetting);

    void onOfferLastFrame(byte[] bArr, CameraSetting cameraSetting);

    void onOnboardingStepFinished();

    void onRecorderSet(BasicVineRecorder basicVineRecorder);

    void onResumeAsyncTaskPostExecute();

    void onResumeCameraAsyncTaskPostExecute();

    void onSegmentDataChanged(ArrayList<RecordSegment> arrayList);

    void onSessionSwapped();

    boolean onSetEditMode(boolean z, boolean z2);

    void onShowDrafts();

    void onStartDrafts();

    void onStartEditMode();

    void onStartRelativeTime(K k);

    void onZoomUpdated(int i, boolean z);

    void setMessagingMode(boolean z);
}
