package co.vine.android.client;

import android.os.Bundle;
import co.vine.android.api.ComplaintMenuOption;
import co.vine.android.api.FoursquareVenue;
import co.vine.android.api.SearchResult;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TwitterUser;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VineComment;
import co.vine.android.api.VineEverydayNotification;
import co.vine.android.api.VineLogin;
import co.vine.android.api.VineNotificationSetting;
import co.vine.android.api.VinePagedData;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineRTCConversation;
import co.vine.android.api.VineSingleNotification;
import co.vine.android.api.VineUser;
import co.vine.android.api.response.PagedActivityResponse;
import co.vine.android.api.response.VineEditions;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.model.VineTag;
import co.vine.android.network.HttpResult;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.service.PendingAction;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class AppSessionListener {
    private boolean mEnabled;

    public void onGetUsersComplete(Session session, String reqId, int statusCode, String reasonPhrase, int count, ArrayList<VineUser> users, int nextPage, int previousPage, String anchor) {
    }

    public void onGetEditionsComplete(int statusCode, VineEditions editions) {
    }

    public void onGetTwitterUserComplete(String reqId, int statusCode, String reasonPhrase, TwitterUser user, VineLogin login) {
    }

    public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
    }

    public void onPhotoImageError(ImageKey key, HttpResult result) {
    }

    public void onGetTwitterFriendsComplete(String reqId, int statusCode, String reasonPhrase, int count, ArrayList<VineUser> users) {
    }

    public void onGetFacebookFriendsComplete(String reqId, int statusCode, String reasonPhrase, int count, ArrayList<VineUser> users) {
    }

    public void onResetPasswordComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
    }

    public void onVideoPathError(VideoKey key, HttpResult result) {
    }

    public void onGetCommentsComplete(String reqId, int statusCode, String reasonPhrase, int nextPage, String anchor, ArrayList<VineComment> comments) {
    }

    public void onPostCommentComplete(String reqId, int statusCode, String reasonPhrase, long postId, String comment, VineComment vc) {
    }

    public void onGetUsersMeComplete(String reqId, int statusCode, String reasonPhrase, long sessionOwnerId, VineUser meUser, UrlCachePolicy policy) {
    }

    public void onGetTimeLineComplete(String reqId, int statusCode, String reasonPhrase, int type, int count, boolean memory, ArrayList<TimelineItem> items, String tag, int pageType, int next, int previous, String anchor, String backAnchor, boolean userInitiated, int size, String title, UrlCachePolicy cachePolicy, boolean network, Bundle bundle) {
    }

    public void onGetActivityComplete(String reqId, int statusCode, String reasonPhrase, VinePagedData<VineEverydayNotification> notifications, PagedActivityResponse.Data followRequests, UrlCachePolicy policyUsed) {
    }

    public void onGetActivityCountComplete(String reqId, int statusCode, String reasonPhrase, int activityCount, int followRequestCount, int messagesCount, UrlCachePolicy policyUsed) {
    }

    public void onGetAddressFriendsComplete(String reqId, int statusCode, String reasonPhrase, int count, ArrayList<VineUser> users) {
    }

    public void onRemoveUsersComplete(String reqId) {
    }

    public void onGetUserComplete(String reqId, int statusCode, String reasonPhrase, VineUser user, UrlCachePolicy policy) {
    }

    public void onUpdateProfileComplete(String reqId, int statusCode, String reasonPhrase, String avatarUri) {
    }

    public void onUpdateEditionComplete(String reqId, int statusCode, String reasonPhrase, String edition) {
    }

    public void onReportPostComplete(String reqId, int statusCode, String reasonPhrase, long postId) {
    }

    public void onDeletePostComplete(String reqId, long postId, int statusCode, String reasonPhrase) {
    }

    public void onHidePostComplete(String reqId, long postId, int statusCode, String reasonPhrase) {
    }

    public void onConnectTwitterComplete(String reqId, int statusCode, String reasonPhrase, String username, String token, String secret, long userId) {
    }

    public void onDisconnectTwitterComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onGetSinglePostComplete(String reqId, int statusCode, String reasonPhrase, VinePost post) {
    }

    public void onGetPostIdComplete(String reqId, int statusCode, String reasonPhrase, long postId) {
    }

    public void onUserSearchComplete(String reqId, int statusCode, String reasonPhrase, int count, int nextPage, int previousPage, ArrayList<VineUser> users) {
    }

    public void onTagSearchComplete(String reqId, int statusCode, String reasonPhrase, int nextPage, String anchor, ArrayList<VineTag> tags) {
    }

    public void onClearCacheComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onSendFacebookTokenComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onUpdateFollowEditorsPicksComplete(String reqId, int statusCode, String reasonPhrase, boolean isFollowing) {
    }

    public void onBlockUserComplete(String reqId, int statusCode, String reasonPhrase, boolean userBlocked, String blockedUsername) {
    }

    public void onUnblockUserComplete(String reqId, int statusCode, String reasonPhrase, boolean userUnblocked, String blockedUsername) {
    }

    public void onReportUserComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onRespondFollowRequestComplete(String reqId, int statusCode, String reasonPhrase, boolean accept, long userId) {
    }

    public void onUpdateExplicitComplete(String reqId, int statusCode, String reasonPhrase, boolean explicit) {
    }

    public void onUpdatePrivateComplete(String reqId, int statusCode, String reasonPhrase, boolean priv) {
    }

    public void onGetChannelsComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineChannel> channels) {
    }

    public void onGcmRegistrationComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
    }

    public void onEnableUserRepostsComplete(String reqId, int statusCode, String reasonPhrase, boolean success) {
    }

    public void onDisableUserRepostsComplete(String reqId, int statusCode, String reasonPhrase, boolean success) {
    }

    public void onDeactivateAccountComplete(String reqId, int statusCode, String reasonPhrase, boolean success) {
    }

    public void onLowMemory() {
    }

    public void onTrimMemory(int level) {
    }

    public void onGetMessageInboxComplete(String reqId, int statusCode, String reasonPhrase, int count) {
    }

    public void onCaptchaRequired(String reqId, String captchaUrl, PendingAction actionToRetry) {
    }

    public void onPhoneVerificationRequired(String reqId, String verifyMsg, PendingAction action) {
    }

    public void onGetUserIdComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
    }

    public void onProfilePhotoUpdatedComplete(String reqId, int statusCode, String reasonPhrase, String avatarUrl) {
    }

    public void onGetConversationComplete(String reqId, int statusCode, String reasonPhrase, long conversationObjectId, int responseCode, boolean empty) {
    }

    public void onGetConversationRowIdComplete(long recipientId, boolean isRecipientAVineUser, long conversationObjectId, String username, boolean amFollowing) {
    }

    public void onGetConversationRemoteIdComplete(long conversationId) {
    }

    public void onRequestPhoneVerificationComplete(String reqId, int statusCode, String reasonPhrase, String phone) {
    }

    public void onVerifyPhoneNumberComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onVerifyEmailComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onRequestEmailDownload(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onRequestEmailVerificationComplete(String reqId, int statusCode, String reasonPhrase, String email) {
    }

    public void onIgnoreConversationComplete(String reqId, int statusCode, String reasonPhrase, long conversationObjectId) {
    }

    public void onDeleteConversationComplete(String reqId, int statusCode, String reasonPhrase, long aLong) {
    }

    public void onMergeNotificationComplete(VineSingleNotification lastNotification, ArrayList<VineSingleNotification> notifications) {
    }

    public void onWebSocketConnectComplete() {
    }

    public void onWebSocketDisconnected() {
    }

    public void onWebSocketError() {
    }

    public void onDeleteCommentComplete(String reqId, int statusCode, String reasonPhrase, long commentId) {
    }

    public void onSpamCommentComplete(String reqId, int statusCode, String reasonPhrase, long commentId) {
    }

    public void onReceiveRTCMessage(ArrayList<VineRTCConversation> data) {
    }

    public void onUpdateAcceptOonComplete(String reqId, int statusCode, String reasonPhrase, boolean acceptOon) {
    }

    public void onUpdateEnableAddressBookComplete(String reqId, int statusCode, String reasonPhrase, boolean enableAddressBook) {
    }

    public void onPostVideoComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onUpdateDiscoverability(String reqId, int statusCode, String reasonPhrase, int type, boolean enable) {
    }

    public void onGetNotificationSettingsComplete(String reqId, ArrayList<VineNotificationSetting> notifSettings) {
    }

    public void onSaveNotificationSettingsComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onFollowChannelComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onFavoriteUserComplete(String reqId, int statusCode, String reasonPhrase) {
    }

    public void onFetchFavoritePeopleComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineUser> users, int nextPage, int prevPage, String anchor) {
    }

    public void onFetchSuggestedFavoritesComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineUser> users) {
    }

    public void onFetchOnboardingSuggestedFavoritesComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineUser> users) {
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void onTumblrLoginComplete(String reqId, int statusCode, String reasonPhrase, String oauthToken, String oauthTokenSecret) {
    }

    public void onFoursquareLocationFetchComplete(int respCode, ArrayList<FoursquareVenue> resp) {
    }

    public void onComplaintMenu(String reqId, int statusCode, ArrayList<ComplaintMenuOption> complaintMenu) {
    }

    public void onSearchPostsComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VinePost> posts, String anchor, String searchUrl) {
    }

    public void onFetchSearchSuggestionsComplete(String reqId, int statusCode, String reasonPhrase, SearchResult results, String searcUrl) {
    }

    public void onFetchSearchTypeaheadComplete(String reqId, int statusCode, String reasonPhrase, String query, SearchResult results, String searchUrl) {
    }

    public void onFetchSearchResultsComplete(String reqId, int statusCode, String reasonPhrase, String query, SearchResult results, String searchUrl) {
    }
}
