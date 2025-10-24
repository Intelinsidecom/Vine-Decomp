package co.vine.android.scribe;

import co.vine.android.api.VinePost;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.Item;
import co.vine.android.scribe.util.ScribeUtils;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class UIEventScribeLogger {

    public enum SwipeDirection {
        LEFT,
        RIGHT
    }

    public static void onSkipWelcomeFeed(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.WELCOME_FEED_DONE, new Item[0]);
    }

    public static void onFinishWelcomeFeed(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.WELCOME_FEED_LAST_CELL, new Item[0]);
    }

    public static void onTheaterExit(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider, boolean swipe) {
        if (swipe) {
            logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.PLAYLIST_DISMISS_SWIPE, new Item[0]);
        } else {
            logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.PLAYLIST_DISMISS_X, new Item[0]);
        }
    }

    public static void onTheaterHoldToReloop(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider, VinePost post) {
        logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.PLAYLIST_HOLD_TO_LOOP, ScribeUtils.getItemFromPost(post));
    }

    public static void onTheaterSwipe(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider, SwipeDirection direction) {
        if (direction == SwipeDirection.LEFT) {
            logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.PLAYLIST_SWIPE_LEFT, new Item[0]);
        } else if (direction == SwipeDirection.RIGHT) {
            logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.PLAYLIST_SWIPE_RIGHT, new Item[0]);
        }
    }

    public static void onFollowOnTwitterCardClick(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.FOLLOW_ON_TWITTER_CARD, new Item[0]);
    }

    public static void onLaunchTwitterFromFollowCard(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.LAUNCH_TWITTER_FROM_FOLLOW_CARD, new Item[0]);
    }

    public static void twitterHandleTap(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.TWITTER_HANDLE_TAP, new Item[0]);
    }

    public static void openTwitterTap(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.OPEN_TWITTER_TAP, new Item[0]);
    }

    public static void twitterLinkTap(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.TWITTER_LINK, new Item[0]);
    }

    public static void onRecommendationCarouselSwipe(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider, SwipeDirection direction) {
        if (direction == SwipeDirection.LEFT) {
            logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.REC_CAROUSEL_LEFT_SWIPE, new Item[0]);
        } else if (direction == SwipeDirection.RIGHT) {
            logUIEvent(logger, appStateProvider, appNavProvider, AppNavigation.UIElements.REC_CAROUSEL_RIGHT_SWIPE, new Item[0]);
        }
    }

    private static void logUIEvent(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider, AppNavigation.UIElements uiElement, Item... items) {
        ClientEvent event = logger.getDefaultClientEvent();
        event.appState = appStateProvider.getAppState();
        event.navigation = appNavProvider.getAppNavigation();
        event.eventType = "ui_event";
        event.navigation.ui_element = uiElement.toString();
        if (items.length > 0) {
            event.eventDetails.items = new ArrayList(Arrays.asList(items));
        }
        logger.logClientEvent(event);
    }
}
