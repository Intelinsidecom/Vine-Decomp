package co.vine.android;

import android.content.Context;
import co.vine.android.recorder.DeviceIssue;
import co.vine.android.recorder.RegularVineRecorder;

/* loaded from: classes.dex */
public class DeviceIssueStringGetter implements RegularVineRecorder.DeviceIssueStringGetter {
    private final String mCannotConnectToCamera;
    private final String mGenericError;
    private final String mNotEnoughSpace;
    private final String mStorageNotReady;

    public DeviceIssueStringGetter(Context context) {
        this.mCannotConnectToCamera = context.getString(R.string.camera_failed_to_open);
        this.mNotEnoughSpace = context.getString(R.string.failed_to_start_recording_space);
        this.mGenericError = context.getString(R.string.unsupported_feature_recording);
        this.mStorageNotReady = context.getString(R.string.storage_not_ready);
    }

    private String getGenericError(DeviceIssue issue) {
        return this.mGenericError + " (" + issue.description + ")";
    }

    @Override // co.vine.android.recorder.RegularVineRecorder.DeviceIssueStringGetter
    public String getString(DeviceIssue issue) {
        switch (issue) {
            case CANNOT_CONNECT_TO_CAMERA:
                return this.mCannotConnectToCamera;
            case CAMERA_NULL_FRAMES:
            case RS_NOT_SUPPORTED:
            case LIBS_NOT_COMPATIBLE:
                return getGenericError(issue);
            case STORAGE_NOT_READY:
                return this.mStorageNotReady;
            case NOT_ENOUGH_SPACE:
                return this.mNotEnoughSpace;
            default:
                return this.mGenericError;
        }
    }
}
