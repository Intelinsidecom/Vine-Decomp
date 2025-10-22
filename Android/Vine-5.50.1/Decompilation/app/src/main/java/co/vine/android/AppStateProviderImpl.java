package co.vine.android;

import android.content.Context;
import android.content.SharedPreferences;
import co.vine.android.cache.video.VideoCache;
import co.vine.android.client.Session;
import co.vine.android.client.SessionManager;
import co.vine.android.recorder.RecordSessionManager;
import co.vine.android.recorder2.model.DraftsManager;
import co.vine.android.scribe.AppStateProvider;
import co.vine.android.scribe.model.ApplicationState;
import co.vine.android.share.utils.TwitterAuthUtil;
import co.vine.android.social.FacebookHelper;
import co.vine.android.util.Util;
import java.io.IOException;

/* loaded from: classes.dex */
public final class AppStateProviderImpl implements AppStateProvider {
    private Context mContext;
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();

    public AppStateProviderImpl(Context context) {
        this.mContext = context;
    }

    @Override // co.vine.android.scribe.AppStateProvider
    public ApplicationState getAppState() {
        ApplicationState appState = new ApplicationState();
        appState.facebookConnected = Boolean.valueOf(FacebookHelper.isFacebookConnected(this.mContext));
        appState.twitterConnected = Boolean.valueOf(TwitterAuthUtil.isTwitterConnected(this.mContext));
        Session activeSession = this.mSessionManager.getCurrentSession();
        if (activeSession == null) {
            appState.loggedInUserId = -1L;
        } else {
            appState.loggedInUserId = Long.valueOf(activeSession.getUserId());
        }
        appState.videoCacheSize = Long.valueOf(VideoCache.getVideoCacheSize());
        try {
            SharedPreferences preferences = Util.getDefaultSharedPrefs(this.mContext);
            if (preferences.getBoolean("useNewRecorder", false)) {
                appState.numDrafts = Long.valueOf(DraftsManager.getDraftsCount(this.mContext));
            } else {
                appState.numDrafts = Long.valueOf(RecordSessionManager.getNumberOfValidSessions(this.mContext, RecordSessionManager.getCurrentVersion(this.mContext)));
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e2) {
        }
        return appState;
    }
}
