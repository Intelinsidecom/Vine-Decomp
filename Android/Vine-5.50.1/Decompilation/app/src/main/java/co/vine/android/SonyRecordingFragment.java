package co.vine.android;

/* loaded from: classes.dex */
public class SonyRecordingFragment extends RecordingFragment {
    @Override // co.vine.android.RecordingFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        ((SonyRecordingActivity) getActivity()).setupSonyOverlay();
    }
}
