package co.vine.android.scribe;

import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.VideoImportDetails;

/* loaded from: classes.dex */
public final class VideoImportCompletedScribeLogger {
    public static void logImportSuccess(ScribeLogger logger, AppStateProvider appStateProvider) {
        logImportCompleted(logger, appStateProvider, VideoImportDetails.VideoImportResult.SUCCESS);
    }

    public static void logImportCancelled(ScribeLogger logger, AppStateProvider appStateProvider) {
        logImportCompleted(logger, appStateProvider, VideoImportDetails.VideoImportResult.CANCELLED);
    }

    public static void logImportTrimError(ScribeLogger logger, AppStateProvider appStateProvider) {
        logImportCompleted(logger, appStateProvider, VideoImportDetails.VideoImportResult.ERROR_TRIM);
    }

    public static void logImportProcessingError(ScribeLogger logger, AppStateProvider appStateProvider) {
        logImportCompleted(logger, appStateProvider, VideoImportDetails.VideoImportResult.ERROR_PROCESSING);
    }

    private static void logImportCompleted(ScribeLogger logger, AppStateProvider appStateProvider, VideoImportDetails.VideoImportResult result) {
        ClientEvent event = logger.getDefaultClientEvent();
        event.appState = appStateProvider.getAppState();
        event.eventType = "video_import_completed";
        event.eventDetails.videoImportDetails = new VideoImportDetails();
        event.eventDetails.videoImportDetails.result = result.toString();
        logger.logClientEvent(event);
    }
}
