package co.vine.android;

import co.vine.android.recorder2.model.Draft;

/* loaded from: classes.dex */
public interface RecordingNavigationController {

    public enum DraftAction {
        SAVE,
        DISCARD_DELETE,
        DISCARD_REVERT
    }

    void goToCaptureFromDrafts(Draft draft);

    void goToPreview();

    void handleDraftAction(DraftAction draftAction, boolean z);

    void toggleEditMode();
}
