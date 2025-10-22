package co.vine.android.scribe;

import co.vine.android.api.VinePost;
import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.Item;
import co.vine.android.scribe.model.PlaybackSummaryDetails;
import co.vine.android.scribe.util.ScribeUtils;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class LongformPostEventLogger {
    private final AppNavigationProvider mAppNavProvider;
    private final AppStateProvider mAppStateProvider;
    private final ScribeLogger mLogger;

    public LongformPostEventLogger(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        this.mAppStateProvider = appStateProvider;
        this.mAppNavProvider = appNavProvider;
        this.mLogger = logger;
    }

    public void longformPlayed(VinePost post) {
        logLongformEvent("longform_played", post);
    }

    public void longformPlayCompleted(VinePost post) {
        logLongformEvent("longform_post_play_completed", post);
    }

    public void longformPlaybackEnded(VinePost post, long startMs, long endMs, long bufferingMs, long playingMs, long pausedMs, int interruptions) {
        PlaybackSummaryDetails playbackSummary = new PlaybackSummaryDetails();
        playbackSummary.playbackInterruptions = Integer.valueOf(interruptions);
        playbackSummary.timeSpentBuffering = Float.valueOf(bufferingMs / 1000.0f);
        playbackSummary.timeSpentPlaying = Float.valueOf(playingMs / 1000.0f);
        playbackSummary.timeSpentPaused = Float.valueOf(pausedMs / 1000.0f);
        playbackSummary.videoStarttime = Float.valueOf(startMs / 1000.0f);
        playbackSummary.videoEndTime = Float.valueOf(endMs / 1000.0f);
        logLongformEvent("longform_playback_ended", post, playbackSummary);
    }

    private void logLongformEvent(String eventType, VinePost post) {
        logLongformEvent(eventType, post, null);
    }

    private void logLongformEvent(String eventType, VinePost post, PlaybackSummaryDetails playbackSummary) {
        ClientEvent event = this.mLogger.getDefaultClientEvent();
        event.appState = this.mAppStateProvider.getAppState();
        event.navigation = this.mAppNavProvider.getAppNavigation();
        event.navigation.timelineApiUrl = post.originUrl;
        event.eventType = eventType;
        event.eventDetails.items = new ArrayList();
        Item scribeItem = ScribeUtils.getItemFromPost(post);
        if (scribeItem != null) {
            event.eventDetails.items.add(scribeItem);
        }
        if (playbackSummary != null) {
            event.eventDetails.playbackSummary = playbackSummary;
        }
        this.mLogger.logClientEvent(event);
    }
}
