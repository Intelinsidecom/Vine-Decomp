package co.vine.android.service.components;

import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.authentication.AuthenticationComponent;
import co.vine.android.service.components.clientconfig.ClientConfigUpdateComponent;
import co.vine.android.service.components.deviceboot.DeviceBootComponent;
import co.vine.android.service.components.feedactions.FeedActionsComponent;
import co.vine.android.service.components.inject.FeedInjectFetchComponent;
import co.vine.android.service.components.longform.LongformComponent;
import co.vine.android.service.components.loopreporting.LoopReportingComponent;
import co.vine.android.service.components.postactions.PostActionsComponent;
import co.vine.android.service.components.prefetch.PrefetchComponent;
import co.vine.android.service.components.recommendations.RecommendationsComponent;
import co.vine.android.service.components.remoterequestcontrol.RemoteRequestControlComponent;
import co.vine.android.service.components.settings.SettingsComponent;
import co.vine.android.service.components.settings.SettingsListener;
import co.vine.android.service.components.share.ShareComponent;
import co.vine.android.service.components.suggestions.SuggestionsComponent;
import co.vine.android.service.components.timelinefetch.TimelineFetchComponent;
import co.vine.android.service.components.userinteraction.UserInteractionsComponent;

/* loaded from: classes.dex */
public enum Components {
    AUTHENTICATION(new AuthenticationComponent()),
    CLIENT_CONFIG_UPDATE(new ClientConfigUpdateComponent()),
    DEVICE_BOOT(new DeviceBootComponent()),
    LOOP_REPORTING(new LoopReportingComponent()),
    POST_ACTIONS(new PostActionsComponent()),
    FEED_ACTIONS(new FeedActionsComponent()),
    PREFETCH(new PrefetchComponent()),
    REMOTE_REQUEST_CONTROL(new RemoteRequestControlComponent()),
    SUGGESTIONS(new SuggestionsComponent()),
    USER_INTERACTIONS(new UserInteractionsComponent()),
    SHARE(new ShareComponent()),
    INJECT(new FeedInjectFetchComponent()),
    SETTINGS(new NotifiableComponent<SettingsComponent.Actions, SettingsListener>() { // from class: co.vine.android.service.components.settings.SettingsComponent

        protected enum Actions {
            FETCH_HOME_FEED_SETTINGS,
            SAVE_HOME_FEED_SETTINGS
        }

        @Override // co.vine.android.service.components.VineServiceComponent
        public void registerActions(VineServiceActionMapProvider.Builder builder) {
            registerAsActionCode(builder, Actions.FETCH_HOME_FEED_SETTINGS, new HomeFeedSettingsFetchAction(), new HomeFeedSettingsFetchActionNotifier(this.mListeners));
            registerAsActionCode(builder, Actions.SAVE_HOME_FEED_SETTINGS, new HomeFeedSettingsSaveAction(), new HomeFeedSettingsSaveActionNotifier(this.mListeners));
        }
    }),
    TIMELINE_FETCH(new TimelineFetchComponent()),
    LONGFORM(new LongformComponent()),
    RECOMMENDATIONS(new RecommendationsComponent());

    private final VineServiceComponent mComponent;

    Components(VineServiceComponent component) {
        this.mComponent = component;
    }

    public void register(VineServiceActionMapProvider.Builder builder) {
        this.mComponent.registerActions(builder);
    }

    public static DeviceBootComponent deviceBootComponent() {
        return (DeviceBootComponent) DEVICE_BOOT.mComponent;
    }

    public static AuthenticationComponent authComponent() {
        return (AuthenticationComponent) AUTHENTICATION.mComponent;
    }

    public static PrefetchComponent prefetchComponent() {
        return (PrefetchComponent) PREFETCH.mComponent;
    }

    public static ClientConfigUpdateComponent clientConfigUpdateComponent() {
        return (ClientConfigUpdateComponent) CLIENT_CONFIG_UPDATE.mComponent;
    }

    public static RemoteRequestControlComponent remoteRequestControlComponent() {
        return (RemoteRequestControlComponent) REMOTE_REQUEST_CONTROL.mComponent;
    }

    public static LoopReportingComponent loopReportingComponent() {
        return (LoopReportingComponent) LOOP_REPORTING.mComponent;
    }

    public static PostActionsComponent postActionsComponent() {
        return (PostActionsComponent) POST_ACTIONS.mComponent;
    }

    public static FeedActionsComponent feedActionsComponent() {
        return (FeedActionsComponent) FEED_ACTIONS.mComponent;
    }

    public static SuggestionsComponent suggestionsComponent() {
        return (SuggestionsComponent) SUGGESTIONS.mComponent;
    }

    public static UserInteractionsComponent userInteractionsComponent() {
        return (UserInteractionsComponent) USER_INTERACTIONS.mComponent;
    }

    public static ShareComponent shareComponent() {
        return (ShareComponent) SHARE.mComponent;
    }

    public static FeedInjectFetchComponent injectComponent() {
        return (FeedInjectFetchComponent) INJECT.mComponent;
    }

    public static TimelineFetchComponent timelineFetchComponent() {
        return (TimelineFetchComponent) TIMELINE_FETCH.mComponent;
    }

    public static LongformComponent longformComponent() {
        return (LongformComponent) LONGFORM.mComponent;
    }

    public static RecommendationsComponent recommendationsComponent() {
        return (RecommendationsComponent) RECOMMENDATIONS.mComponent;
    }
}
