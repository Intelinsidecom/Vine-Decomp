package co.vine.android.scribe;

import android.content.Context;
import co.vine.android.cache.video.VideoCache;
import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.TimingDetails;

/* loaded from: classes.dex */
public final class VideoDownloadDurationScribeLogger implements VideoCache.VideoDownloadDurationListener {
    private AppStateProvider mAppStateGetter;
    private Context mContext;

    public VideoDownloadDurationScribeLogger(Context context, AppStateProvider appStateGetter) {
        this.mContext = context;
        this.mAppStateGetter = appStateGetter;
    }

    @Override // co.vine.android.cache.video.VideoCache.VideoDownloadDurationListener
    public void onVideoDownloaded(double downloadStartSecondsSinceEpoch, double downloadDurationSeconds) {
        ScribeLogger logger = ScribeLoggerSingleton.getInstance(this.mContext);
        ClientEvent event = logger.getDefaultClientEvent();
        event.appState = this.mAppStateGetter.getAppState();
        event.eventDetails.timestamp = Double.valueOf(downloadStartSecondsSinceEpoch);
        event.eventType = "video_downloaded";
        event.eventDetails.timing = new TimingDetails();
        event.eventDetails.timing.startTimestamp = Double.valueOf(downloadStartSecondsSinceEpoch);
        event.eventDetails.timing.duration = Double.valueOf(downloadDurationSeconds);
        logger.logClientEvent(event);
    }
}
