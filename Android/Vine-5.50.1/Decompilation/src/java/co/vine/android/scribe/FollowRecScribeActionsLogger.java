package co.vine.android.scribe;

import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.scribe.model.ClientEvent;

/* loaded from: classes.dex */
public class FollowRecScribeActionsLogger extends FollowScribeActionsLogger {
    public FollowRecScribeActionsLogger(FollowScribeActionsLogger logger) {
        super(logger.mLogger, logger.mAppStateProvider, logger.mAppNavProvider);
    }

    @Override // co.vine.android.scribe.FollowScribeActionsLogger
    protected ClientEvent getEvent(String eventType) {
        ClientEvent event = super.getEvent(eventType);
        event.navigation.subview = AppNavigation.Subviews.SUGGESTIONS.toString();
        return event;
    }
}
