package co.vine.android.util.analytics;

import android.content.Context;
import android.view.View;
import co.vine.android.analytics.FlurryEvent;
import co.vine.android.cache.CacheKey;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import com.flurry.android.FlurryAgent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class FlurryUtils {
    private static boolean isSignupFailed;
    private static boolean sIsProduction;
    private static boolean sRecordingCamera;
    private static boolean sRecordingFocus;
    private static boolean sRecordingGhost;
    private static boolean sRecordingGrid;
    private static boolean sRecordingSession;
    private static final HashMap<String, String> mLoadingTime = new HashMap<>();
    private static long sLastPostIdDoubleTapped = -1;
    private static int sCountDoubleTapped = 0;

    public static void start(Context context, String versionName, boolean isProduction, long userId) {
        try {
            FlurryAgent.onStartSession(context, "K5MVVCSN2MQ89JDRWKGY");
            FlurryAgent.setReportLocation(false);
            FlurryAgent.setUseHttps(true);
            if (userId > 0) {
                FlurryAgent.setUserId(String.valueOf(userId));
            }
            FlurryAgent.setVersionName(versionName);
            sIsProduction = isProduction;
        } catch (Exception e) {
            CrashUtil.logException(e);
        }
    }

    public static void stop(Context context) {
        try {
            FlurryAgent.onEndSession(context);
        } catch (Exception e) {
            CrashUtil.logException(e);
            throw new RuntimeException(e);
        }
    }

    public static void trackLockOutSessionCount() {
        if (isSignupFailed) {
            isSignupFailed = false;
            FlurryEvent.log("SignUpLockOut");
        }
    }

    public static void trackRespondTime(String host, String path, long firstByte, long durationMs, boolean isVideo) {
        if (sIsProduction) {
            FlurryEvent event = new FlurryEvent("Response_" + (isVideo ? "video" : "generic"));
            if (isVideo) {
                event.add("host", host);
            } else {
                event.add("host", host).add("path", path);
            }
            event.add("statusTime", getBucketedTime(firstByte)).add("totalTime", getBucketedTime(durationMs)).log();
        }
    }

    public static void trackDoubleTap(long postId) {
        if (postId == sLastPostIdDoubleTapped) {
            sCountDoubleTapped++;
            FlurryEvent.log("doubleTap", "count", Integer.valueOf(sCountDoubleTapped));
        } else {
            FlurryEvent.log("doubleTap", "count", 0);
        }
        sLastPostIdDoubleTapped = postId;
    }

    private static String getBucketedTime(long time) {
        return time < 1000 ? String.valueOf((time / 100) * 100) + "ms" : String.valueOf(Math.max(((time / 500) * 500) / 1000.0d, 1.0d) + "s");
    }

    public static void trackValidPullToRefreshRelease(String source) {
        FlurryEvent.log("ValidPullToRefreshRelease", "source", source);
    }

    public static void trackLoginSuccess(boolean isTwitter) {
        FlurryEvent.log("LoginSuccess", "isTwitter", Boolean.valueOf(isTwitter));
    }

    public static void trackLoginFailure(boolean isTwitter, int statusCode) {
        FlurryEvent.log("LoginFailure", "isTwitter", Boolean.valueOf(isTwitter), "statusCode", Integer.valueOf(statusCode));
    }

    public static void trackTimeLinePageScroll(String name, int page) {
        FlurryEvent.log("TimeLinePageScroll_" + name, "page", Integer.valueOf(page));
    }

    public static void onSignupSuccess(boolean twitterSignup) {
        isSignupFailed = false;
        FlurryEvent.log("SignupSuccess", "isTwitter", Boolean.valueOf(twitterSignup));
    }

    public static void onSignupWithPreinstallSuccess(String target) {
        FlurryEvent.log("SignUpWithPreinstall", "target", target);
    }

    public static void onSignupFailure(boolean twitterSignup, int errorCode, String errorMessage, int statusCode) {
        isSignupFailed = true;
        FlurryEvent event = new FlurryEvent("SignupFailure");
        if (errorMessage != null) {
            event.add("errorCode", Integer.valueOf(errorCode)).add("message", errorMessage);
        }
        event.add("isTwitter", Boolean.valueOf(twitterSignup)).add("statusCode", Integer.valueOf(statusCode)).log();
    }

    public static void trackLikePost(long postId, String source) {
        FlurryEvent.log("Like", "postId", Long.valueOf(postId), "Source View", source);
    }

    public static void trackUnLikePost(String source) {
        FlurryEvent.log("UnLike", "Source View", source);
    }

    public static void trackRevine(long postId, String source) {
        FlurryEvent.log("Revine", "postId", Long.valueOf(postId), "Source View", source);
    }

    public static void trackUnRevine(String source) {
        FlurryEvent.log("UnRevine", "Source View", source);
    }

    public static void trackGetUser(boolean self) {
        FlurryEvent.log("ProfileFetch", "self", Boolean.valueOf(self));
    }

    public static void trackResetPassword() {
        FlurryEvent.log("ResetPassword");
    }

    public static void trackGetComments() {
        FlurryEvent.log("GetComments");
    }

    public static void trackGetEditions() {
        FlurryEvent.log("GetEditions");
    }

    public static void trackPostComment() {
        FlurryEvent.log("PostComment");
    }

    public static void trackDeleteComment() {
        FlurryEvent.log("DeleteComment");
    }

    public static void trackConnectFacebook() {
        FlurryEvent.log("ConnectFacebook");
    }

    public static void trackChangedEdition() {
        FlurryEvent.log("ChangedEdition");
    }

    public static void trackSearchUsers() {
        FlurryEvent.log("SearchUser");
    }

    public static void trackSearchTags() {
        FlurryEvent.log("SearchTags");
    }

    public static void trackBlockUser() {
        FlurryEvent.log("BlockUser");
    }

    public static void trackUnBlockUser() {
        FlurryEvent.log("UnBlockUser");
    }

    public static void trackReportUser() {
        FlurryEvent.log("ReportUser");
    }

    public static void trackSharePost(String network, long postId) {
        FlurryEvent.log("SharePost_" + network, "postId", Long.valueOf(postId));
    }

    public static void trackReportPost() {
        FlurryEvent.log("ReportPost");
    }

    public static void trackTabChange(String tag) {
        FlurryEvent.log("Page_" + tag);
    }

    public static void trackPost(boolean checked) {
        FlurryEvent.log("Post", "Vine", Boolean.valueOf(checked));
    }

    public static void trackChannelChange(String channelDetails) {
        FlurryEvent.log("PostChannelChange", "channelDetails", channelDetails);
    }

    public static void trackAbandonedStage(String stage) {
        FlurryEvent.log("Abandon", "stage", stage);
    }

    public static void trackAbandonedTier(String tier, long timeTaken, long maxKnownStopTime) {
        SLog.d("Post abandoned for {}, had to wait {}ms and {}ms.", tier, Long.valueOf(timeTaken), Long.valueOf(maxKnownStopTime));
        FlurryEvent.log("Post_Abandon_" + tier, "waitOnCamera", Long.valueOf(timeTaken), "waitOnProcessing", Long.valueOf(maxKnownStopTime));
    }

    public static void trackPostTier(String tier, long timeTaken, long maxKnownStopTime) {
        FlurryEvent.log("Post_Posted_" + tier, "waitOnCamera", Long.valueOf(timeTaken), "waitOnProcessing", Long.valueOf(maxKnownStopTime));
    }

    public static void trackShareProfile() {
        FlurryEvent.log("ShareProfile");
    }

    public static void trackCreateProfileShortCut() {
        FlurryEvent.log("CreateProfileShortCut");
    }

    public static void trackVisitSettings(String source) {
        FlurryEvent.log("VisitSettings", "source", source);
    }

    public static void trackChangedName() {
        FlurryEvent.log("Settings_ChangedName");
    }

    public static void trackChangedDescription() {
        FlurryEvent.log("Settings_ChangedDescription");
    }

    public static void trackChangedLocation() {
        FlurryEvent.log("Settings_ChangedLocation");
    }

    public static void trackChangedEmail() {
        FlurryEvent.log("Settings_ChangedEmail");
    }

    public static void trackVisitFindFriends(String source) {
        FlurryEvent.log("VisitFindFriends", "source", source);
    }

    public static void trackFindFriendsAddressCount(int count) {
        FlurryEvent.log("FindFriendsAddressResultsCount", "Result Count", Integer.valueOf(count));
    }

    public static void trackFindFriendsTwitterCount(int count) {
        FlurryEvent.log("FindFriendsTwitterResultsCount", "Result Count", Integer.valueOf(count));
    }

    public static void trackFindFriendsFacebookCount(int count) {
        FlurryEvent.log("FindFriendsFacebookResultsCount", "Result Count", Integer.valueOf(count));
    }

    public static void trackFindFriendsAddressFailure() {
        FlurryEvent.log("FindFriendsAddressFailure");
    }

    public static void trackFindFriendsTwitterFailure() {
        FlurryEvent.log("FindFriendsTwitterFailure");
    }

    public static void trackFindFriendsFacebookFailure() {
        FlurryEvent.log("FindFriendsFacebookFailure");
    }

    public static void trackAddressNewFollowingCount(String count) {
        FlurryEvent.log("FindFriendsAddressNewFollowing", "Count", count);
    }

    public static void trackTwitterNewFollowingCount(String count) {
        FlurryEvent.log("FindFriendsTwitterNewFollowing", "Count", count);
    }

    public static void trackFacebookNewFollowingCount(String count) {
        FlurryEvent.log("FindFriendsFacebookNewFollowing", "Count", count);
    }

    public static void trackVisitSearch(String source) {
        FlurryEvent.log("VisitSearch", "source", source);
    }

    public static void trackVisitInbox(String source) {
        FlurryEvent.log("VisitInbox", "source", source);
    }

    public static void trackGoogleBotCrawl() {
        FlurryEvent.log("GoogleBotCrawl");
    }

    public static void trackGoogleView(boolean isLoggedIn, boolean isFromApp) {
        FlurryEvent.log("GoogleView", "logged_in", String.valueOf(isLoggedIn), "in_app", String.valueOf(isFromApp));
    }

    public static void trackLogout() {
        FlurryEvent.log("Logout");
    }

    public static void trackRecordingStart() {
        sRecordingCamera = false;
        sRecordingFocus = false;
        sRecordingGrid = false;
        sRecordingGhost = false;
        sRecordingSession = false;
        FlurryEvent.log("RecordingStart");
    }

    public static void trackRecordingDestroy() {
    }

    public static void trackGridSwitchPressed() {
        if (!sRecordingGrid) {
            sRecordingGrid = true;
            FlurryEvent.log("RecordingGrid");
        }
    }

    public static void trackGhostSwitchPressed(View v) {
        if (v != null && !sRecordingGhost) {
            sRecordingGhost = true;
            FlurryEvent.log("RecordingGhost");
        }
    }

    public static void trackSessionSwitchPressed() {
        if (!sRecordingSession) {
            sRecordingSession = true;
            FlurryEvent.log("RecordingSession");
        }
    }

    public static void trackSessionSwitchPressedOnDraftUpgrade(int count) {
        if (!sRecordingSession) {
            sRecordingSession = true;
            FlurryEvent.log("RecordingSessionDraftUpgrading", "count", Integer.valueOf(count));
        }
    }

    public static void trackAttribution() {
        FlurryEvent.log("Attribution");
    }

    public static void trackDeactivateAccount() {
        FlurryEvent.log("DeactivateAccount");
    }

    public static void trackTos() {
        FlurryEvent.log("TermsOfServiceClicked");
    }

    public static void trackInvite(String type, String source) {
        FlurryEvent.log("Invite_" + type, "source", source);
    }

    public static void trackPrivacyPolicy() {
        FlurryEvent.log("PrivacyPolicy");
    }

    public static void trackContentControls() {
        FlurryEvent.log("ContentControls");
    }

    public static void trackNotificationSettings() {
        FlurryEvent.log("NotificationSettings");
    }

    public static void trackSaveSession(String source) {
        FlurryEvent.log("SaveSession", "source", source);
    }

    public static void trackPreviewAction(String action) {
        FlurryEvent.log("PreviewAction", "action", action);
    }

    public static void trackProfileImageClick(boolean self) {
        FlurryEvent.log("ProfileImageClick", "self", Boolean.valueOf(self));
    }

    public static void trackFilterProfileReposts(boolean hideReposts, boolean isFollowing, boolean isMe) {
        String eventName = hideReposts ? "ProfileRepostsHidden" : "ProfileRepostsShown";
        FlurryEvent.log(eventName, "Is Following", Boolean.valueOf(isFollowing), "Is Me", Boolean.valueOf(isMe));
    }

    public static void trackCameraWidgetAdded() {
        FlurryEvent.log("CameraWidgetAdded");
    }

    public static void trackCameraWidgetRemoved() {
        FlurryEvent.log("CameraWidgetRemoved");
    }

    public static void trackNotificationReceived(String type) {
        FlurryEvent.log("notificationReceived", "type", type);
    }

    public static void trackNotificationShown(String type) {
        FlurryEvent.log("notificationShown", "type", type);
    }

    public static void trackNotificationClicked(String type) {
        FlurryEvent.log("notificationClicked", "type", type);
    }

    public static void trackNotificationDisabled() {
        FlurryEvent.log("notificationDisabled");
    }

    public static void trackVineLoopWatched() {
        FlurryEvent.log("VineLoopWatched");
    }

    public static void trackNuxStarted(String type) {
        FlurryEvent.log("nux_started", "type", type);
    }

    public static void trackNuxScreenDisplayed(String nuxScreen) {
        FlurryEvent.log("nux_screenDisplayed", "nuxScreen", nuxScreen);
    }

    public static void trackNuxAccountCreated(String accountType) {
        FlurryEvent.log("nux_accountCreated", "accountType", accountType);
    }

    public static void trackNuxChannelFollows(ArrayList<String> channelIds) {
        FlurryEvent.log("nux_channelFollowTotalFollowed", "numChannels", Integer.valueOf(channelIds.size()));
        Iterator<String> it = channelIds.iterator();
        while (it.hasNext()) {
            String channelId = it.next();
            FlurryEvent.log("nux_channelFollow", "channelId", channelId);
        }
    }

    public static void trackNuxCompleted() {
        FlurryEvent.log("nux_completed");
    }

    public static void trackOnboardingShown(String onboardId) {
        FlurryEvent.log("onboarding_seen_" + onboardId);
    }

    public static void trackOnboardingDismissed(String option) {
        FlurryEvent.log("onboarding_dismissed_" + option);
    }

    public static void trackPrefetchStart(long lastStart) {
        if (lastStart > 0) {
            FlurryEvent.log("prefetch_start", "since_last_start", Long.valueOf(System.currentTimeMillis() - lastStart));
        } else {
            FlurryEvent.log("prefetch_start", "since_last_start", Integer.MAX_VALUE);
        }
    }

    public static void trackPrefetchEnd(long duration) {
        FlurryEvent.log("prefetch_end", "duration", Long.valueOf(duration));
    }

    public static void trackVideoLoadHits(boolean wasSyncedRecently, CacheKey.CacheState cacheState) {
        FlurryEvent.log("VideoLoad_" + cacheState.name(), "syncedRecently", Boolean.valueOf(wasSyncedRecently));
    }
}
