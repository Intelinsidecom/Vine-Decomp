package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class VineClientFlags {

    @JsonField(name = {"alwaysShowTimeInFeed"})
    public Boolean alwaysShowTimeInFeed;

    @JsonField(name = {"apiHost"})
    public String apiHost;

    @JsonField(name = {"audioLatencyUs"})
    public Integer audioLatencyUs;

    @JsonField(name = {"autoPlayPreviewVids"})
    public Boolean autoPlayPreviewVids;

    @JsonField(name = {"canHidePosts"})
    public Boolean canHidePosts;

    @JsonField(name = {"channelsForYouTabEnabled"})
    public Boolean channelsForYouTabEnabled;

    @JsonField(name = {"useDigitsForPhoneVerification"})
    public Boolean digitsEnabled;

    @JsonField(name = {"doubleTapToLikeOnPause"})
    public Boolean doubleTapToLikeOnPause;

    @JsonField(name = {"enableAccountSwitchingAndroid"})
    public Boolean enableAccountSwitching;

    @JsonField(name = {"enableEmailVideoImporter"})
    public Boolean enableEmailVideoImporter;

    @JsonField(name = {"enableTwitterFollowingsImport"})
    public Boolean enableFollowerMigration;

    @JsonField(name = {"enableImportMultiSource"})
    public Boolean enableImportMultiSource;

    @JsonField(name = {"showLinkTwitterAlert"})
    public Boolean enableLinkTwitterPrompt;

    @JsonField(name = {"enableMultiAttributionsView"})
    public Boolean enableMultiAttributionsView;

    @JsonField(name = {"enablePlaylistCreationDEV"})
    public Boolean enablePlaylistCreationDEV;

    @JsonField(name = {"enableProfileShare"})
    public Boolean enableProfileShare;

    @JsonField(name = {"enableRecorder2"})
    public Boolean enableRecorder2;

    @JsonField(name = {"enableRecorder2Marshmallow"})
    public Boolean enableRecorder2Marshmallow;

    @JsonField(name = {"enableRecorderFilters"})
    public Boolean enableRecorderFilters;

    @JsonField(name = {"enableThumbnailTransitionInExplore"})
    public Boolean enableThumbnailTransitionInExplore;

    @JsonField(name = {"enableVideoImporter"})
    public Boolean enableVideoImporter;

    @JsonField(name = {"enableVideoRemix"})
    public Boolean enableVideoRemix;

    @JsonField(name = {"exploreGridEnabled"})
    public Boolean exploreGridEnabled;

    @JsonField(name = {"exploreGridLetterboxDetectionEnabled"})
    public Boolean exploreGridLetterboxDetectionEnabled;

    @JsonField(name = {"exploreHost"})
    public String exploreHost;

    @JsonField(name = {"explorePath"})
    public String explorePath;

    @JsonField(name = {"insertionTimelines"})
    public ArrayList<String> insertionTimelines;

    @JsonField(name = {"showLongform"})
    public Boolean longformEnabled;

    @JsonField(name = {"mediaHost"})
    public String mediaHost;

    @JsonField(name = {"nuxHideChannelPicker"})
    public Boolean nuxHideChannelPicker;

    @JsonField(name = {"nuxHideFriendsFinder"})
    public Boolean nuxHideFriendsFinder;

    @JsonField(name = {"nuxShowWelcomeFeed"})
    public Boolean nuxShowWelcomeFeed;

    @JsonField(name = {"popularTabsEnabled"})
    public Boolean popularTabsEnabled;

    @JsonField(name = {"prefetchEnabled"})
    public Boolean prefetchEnabled;

    @JsonField(name = {"profile_sorting"})
    public Boolean profileSortingEnabled;

    @JsonField(name = {"rtcHost"})
    public String rtcHost;

    @JsonField(name = {"scribeEnabled"})
    public Boolean scribeEnabled;

    @JsonField(name = {"suggested_banner_pinnable"})
    public Boolean showDiscoverStickyHeader;

    @JsonField(name = {"showTwitterFollowCardOnProfile"})
    public Boolean showTwitterFollowCardOnProfile;

    @JsonField(name = {"newScreensWithTwitterScreenname"})
    public Boolean showTwitterScreenname;

    @JsonField(name = {"similarPosts"})
    public Boolean similarPostsEnabled;

    @JsonField(name = {"solicitorEnabled"})
    public Boolean solicitorEnabled;

    @JsonField(name = {"suggestedFeedInject"})
    public Boolean suggestedFeedMosaicInjection;

    @JsonField(name = {"suggestedFeedVideoGrid"})
    public Boolean suggestedFeedVideoGrid;

    @JsonField(name = {"enablePlaylists"})
    public Boolean theaterModeEnabled;

    @JsonField(name = {"ttl_s"})
    public Long ttlSeconds;

    @JsonField(name = {"enableNewEditScreen"})
    public Boolean useNewEditScreen;

    @JsonField(name = {"androidSeamless"})
    public Boolean useVinePlayer;

    @JsonField(name = {"canViewVideoSources"})
    public Boolean videoRemixConsumptionEnabled;

    @JsonField(name = {"welcomeFeedExitUrl"})
    public String welcomeFeedExitUrl;
}
