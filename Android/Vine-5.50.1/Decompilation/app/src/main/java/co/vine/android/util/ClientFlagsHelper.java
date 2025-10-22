package co.vine.android.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import co.vine.android.PersistentPreference;
import co.vine.android.api.response.VineClientFlags;
import co.vine.android.util.ClientFlags.ClientFlag;
import co.vine.android.util.ClientFlags.FeatureStatus;
import co.vine.android.util.ClientFlags.Flag;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public class ClientFlagsHelper {
    private static final Flag<Boolean> sEnableRecorder2Marshmallow = new ClientFlag("client_flag_enable_recorder2_marshmallow", false, new GregorianCalendar(2016, 10, 17).getTime(), "Enable recorder2 for API higher than Marshmallow 23", FeatureStatus.READY_FOR_PRODUCTION);
    private static final Flag<Boolean> sAccountSwitching = new ClientFlag("client_flags_account_switching", false, new GregorianCalendar(2016, 9, 22).getTime(), "Allow multiple accounts to be logged in at once, and enable the account switching menu.", FeatureStatus.NOT_READY);
    private static final Flag<Boolean> sCanHidePosts = new ClientFlag("client_flags_can_hide_posts", false, new GregorianCalendar(2016, 8, 30).getTime(), "Allows user to hide post instead of deleting it.", FeatureStatus.READY_FOR_PRODUCTION);
    private static final Flag<Boolean> sFollowerMigration = new ClientFlag("client_flag_enable_follower_migration", false, new GregorianCalendar(2016, 12, 6).getTime(), "Enable the Follow on Twitter tool.", FeatureStatus.READY_FOR_PRODUCTION);
    private static final Flag<Boolean> sLinkTwitterPrompt = new ClientFlag("client_flag_enable_link_twitter_prompt", false, new GregorianCalendar(2016, 12, 6).getTime(), "Enable the prompt to link Twitter.", FeatureStatus.READY_FOR_PRODUCTION);
    private static final Flag<Boolean> sMultiAttributions = new ClientFlag("client_flag_multi_attributions_view", true, new GregorianCalendar(2016, 9, 16).getTime(), "Allow the user to use the new attributions view when pausing an vine.", FeatureStatus.READY_FOR_PRODUCTION);
    private static final Flag<Boolean> sShowTwitterFollowCard = new ClientFlag("client_flag_show_twitter_follow_card", false, new GregorianCalendar(2016, 9, 26).getTime(), "Prompt vine user to follow user on twitter in vine", FeatureStatus.READY_FOR_PRODUCTION);
    private static final Flag<Boolean> sEnableVideoRemix = new ClientFlag("client_flag_enable_video_remix", false, new GregorianCalendar(2016, 10, 6).getTime(), "Allow users to remix a vine from timeline option menu", FeatureStatus.NOT_READY);
    private static final Flag<Boolean> sNewEditScreen = new ClientFlag("client_flag_new_edit_flow", false, new GregorianCalendar(2016, 10, 4).getTime(), "Enables the new edit flow", FeatureStatus.NOT_READY);
    private static final Flag<Boolean> sEnableDownloader = new ClientFlag("client_flags_enable_downloader", false, new GregorianCalendar(2016, 11, 22).getTime(), "enable downloader", FeatureStatus.READY_FOR_PRODUCTION);
    private static final Flag<Boolean> sEnableEmailDownloader = new ClientFlag("client_flags_enable_email_downloader", false, new GregorianCalendar(2016, 11, 22).getTime(), "enable email downloader", FeatureStatus.READY_FOR_PRODUCTION);
    public static final String[] overrideFlags = {"enable_flags_override", "override_client_flags_always_show_time", "override_client_flags_auto_play_preview_vids", sCanHidePosts.getOverrideKey(), "override_client_flags_channel_for_you_tab", "override_client_flags_discover_sticky_header", "override_client_flags_explore_grid", sMultiAttributions.getOverrideKey(), "override_clients_flag_import_multi_source", "override_client_flags_playlist", "override_client_flags_enable_profile_share", "override_client_flags_filters", "override_client_flags_recorder2", sEnableRecorder2Marshmallow.getOverrideKey(), "override_client_flags_longform", "override_client_flags_nux_hide_channel_picker", "override_client_flags_nux_hide_friends_finder", "override_client_flags_nux_show_welcome_feed", "override_client_flags_popular_tabs", "override_client_flags_show_twitter_screenname", "override_client_flags_suggested_feed_mosaic_injection", "override_clients_flag_suggested_feed_video_grid", "override_client_flags_theater_mode", "override_client_flags_enable_thumbnail_transition", "override_client_flags_enable_letterbox_detection_for_grid_explore", sAccountSwitching.getOverrideKey(), sFollowerMigration.getOverrideKey(), sLinkTwitterPrompt.getOverrideKey(), sShowTwitterFollowCard.getOverrideKey(), sEnableVideoRemix.getOverrideKey(), sNewEditScreen.getOverrideKey(), sEnableDownloader.getOverrideKey(), sEnableEmailDownloader.getOverrideKey()};

    public static boolean isClientFlagsTtlInRange(Context context) {
        SharedPreferences prefs = CommonUtil.getDefaultSharedPrefs(context);
        long lastChangedMillis = prefs.getLong("client_flags_last_changed_millis", 0L);
        long ttlMillis = prefs.getLong("client_flags_ttl", 0L);
        return System.currentTimeMillis() - lastChangedMillis < ttlMillis;
    }

    private static void putIfNotNull(SharedPreferences.Editor editor, String preferenceName, Boolean value) {
        if (value != null) {
            editor.putBoolean(preferenceName, value.booleanValue());
        }
    }

    public static void updateClientFlags(Context context, VineClientFlags clientFlags) {
        SharedPreferences.Editor editor = CommonUtil.getDefaultSharedPrefs(context).edit();
        long ttlMillis = clientFlags.ttlSeconds.longValue() * 1000;
        editor.putLong("client_flags_ttl", ttlMillis);
        editor.putLong("client_flags_last_changed_millis", System.currentTimeMillis());
        editor.putString("client_flags_api_host", clientFlags.apiHost);
        editor.putString("client_flags_explore_host", clientFlags.exploreHost);
        editor.putString("client_flags_explore_path", clientFlags.explorePath);
        editor.putString("client_flags_media_host", clientFlags.mediaHost);
        editor.putString("client_flags_rtc_host", clientFlags.rtcHost);
        editor.putString("client_flags_welcome_feed_exit_url", clientFlags.welcomeFeedExitUrl);
        editor.putBoolean("client_flags_use_vine_layer", clientFlags.useVinePlayer.booleanValue());
        editor.putBoolean("client_flags_double_tap_to_like_on_pause", clientFlags.doubleTapToLikeOnPause.booleanValue());
        editor.putBoolean("client_flags_prefetch_enabled", clientFlags.prefetchEnabled.booleanValue());
        editor.putInt("client_flags_audio_latency", clientFlags.audioLatencyUs.intValue());
        sCanHidePosts.setServerValue(context, clientFlags.canHidePosts);
        putIfNotNull(editor, "client_flags_channel_for_you_tab", clientFlags.channelsForYouTabEnabled);
        putIfNotNull(editor, "client_flags_digits_enabled", clientFlags.digitsEnabled);
        putIfNotNull(editor, "client_flags_discover_sticky_header", clientFlags.showDiscoverStickyHeader);
        putIfNotNull(editor, "client_flags_explore_grid", clientFlags.exploreGridEnabled);
        putIfNotNull(editor, "clients_flag_import_multi_source", clientFlags.enableImportMultiSource);
        sMultiAttributions.setServerValue(context, clientFlags.enableMultiAttributionsView);
        putIfNotNull(editor, "client_flags_playlist", clientFlags.enablePlaylistCreationDEV);
        putIfNotNull(editor, "client_flags_enable_profile_share", clientFlags.enableProfileShare);
        putIfNotNull(editor, "client_flags_filters", clientFlags.enableRecorderFilters);
        putIfNotNull(editor, "client_flags_recorder2", clientFlags.enableRecorder2);
        sEnableRecorder2Marshmallow.setServerValue(context, clientFlags.enableRecorder2Marshmallow);
        putIfNotNull(editor, "client_flags_longform", clientFlags.longformEnabled);
        putIfNotNull(editor, "client_flags_nux_hide_channel_picker", clientFlags.nuxHideChannelPicker);
        putIfNotNull(editor, "client_flags_nux_hide_friends_finder", clientFlags.nuxHideFriendsFinder);
        putIfNotNull(editor, "client_flags_nux_show_welcome_feed", clientFlags.nuxShowWelcomeFeed);
        putIfNotNull(editor, "profile_sorting", clientFlags.profileSortingEnabled);
        putIfNotNull(editor, "client_flags_popular_tabs", clientFlags.popularTabsEnabled);
        putIfNotNull(editor, "client_flags_scribe_enabled", clientFlags.scribeEnabled);
        putIfNotNull(editor, "client_flags_show_twitter_screenname", clientFlags.showTwitterScreenname);
        putIfNotNull(editor, "client_flags_similar_posts", clientFlags.similarPostsEnabled);
        putIfNotNull(editor, "client_flags_solicitor_enabled", clientFlags.solicitorEnabled);
        putIfNotNull(editor, "client_flags_suggested_feed_mosaic_injection", clientFlags.suggestedFeedMosaicInjection);
        putIfNotNull(editor, "client_flags_theater_mode", clientFlags.theaterModeEnabled);
        putIfNotNull(editor, "client_flags_video_remix_consumption", clientFlags.videoRemixConsumptionEnabled);
        putIfNotNull(editor, "clients_flag_suggested_feed_video_grid", clientFlags.suggestedFeedVideoGrid);
        putIfNotNull(editor, "client_flags_auto_play_preview_vids", clientFlags.autoPlayPreviewVids);
        putIfNotNull(editor, "client_flags_enable_thumbnail_transition", clientFlags.enableThumbnailTransitionInExplore);
        putIfNotNull(editor, "client_flags_enable_letterbox_detection_for_grid_explore", clientFlags.exploreGridLetterboxDetectionEnabled);
        sAccountSwitching.setServerValue(context, clientFlags.enableAccountSwitching);
        sFollowerMigration.setServerValue(context, clientFlags.enableFollowerMigration);
        sLinkTwitterPrompt.setServerValue(context, clientFlags.enableLinkTwitterPrompt);
        sShowTwitterFollowCard.setServerValue(context, clientFlags.showTwitterFollowCardOnProfile);
        sEnableDownloader.setServerValue(context, clientFlags.enableVideoImporter);
        sEnableEmailDownloader.setServerValue(context, clientFlags.enableEmailVideoImporter);
        sEnableVideoRemix.setServerValue(context, clientFlags.enableVideoRemix);
        sNewEditScreen.setServerValue(context, clientFlags.useNewEditScreen);
        if (clientFlags.insertionTimelines != null && !clientFlags.insertionTimelines.isEmpty()) {
            Set<String> timelineResources = new HashSet<>();
            Iterator<String> it = clientFlags.insertionTimelines.iterator();
            while (it.hasNext()) {
                String timelineResource = it.next();
                timelineResources.add(timelineResource);
            }
            editor.putStringSet("client_flags_similar_posts_insertion_timelines", timelineResources);
        }
        editor.apply();
    }

    public static void updateOverrideClientFlagValue(Context context, String flag, boolean flagValue) {
        SharedPreferences.Editor editor = PersistentPreference.CLIENT_FLAGS_OVERRIDE.getPref(context).edit();
        editor.putBoolean(flag, flagValue);
        editor.apply();
    }

    public static boolean getOverrideClientFlagValue(Context context, String flag) {
        if (!PersistentPreference.CLIENT_FLAGS_OVERRIDE.getPref(context).contains(flag) && !"enable_flags_override".equals(flag)) {
            updateOverrideClientFlagValue(context, flag, CommonUtil.getDefaultSharedPrefs(context).getBoolean(flag.substring("override_".length()), false));
        }
        return PersistentPreference.CLIENT_FLAGS_OVERRIDE.getPref(context).getBoolean(flag, false);
    }

    public static boolean getUseVinePlayer(Context context) {
        return CommonUtil.getDefaultSharedPrefs(context).getBoolean("client_flags_use_vine_layer", false);
    }

    public static boolean isChannelForYouTabEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_channel_for_you_tab", DefaultFeatureFlags.FEATURE_CHANNEL_FOR_YOU_TAB);
    }

    public static boolean isExploreGridEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_explore_grid", DefaultFeatureFlags.FEATURE_EXPLORE_GRID);
    }

    public static boolean isThumbnailTransitionEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_enable_thumbnail_transition", DefaultFeatureFlags.FEATURE_ENABLE_THUMBNAIL_TRANSITION);
    }

    public static boolean isLetterboxDetectionForExploreEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_enable_letterbox_detection_for_grid_explore", DefaultFeatureFlags.FEATURE_ENABLE_LETTERBOX_DETECTION_FOR_GRID_EXPLORE);
    }

    public static boolean isDigitsEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_digits_enabled", DefaultFeatureFlags.DIGITS_PHONE_VERIFICATION_ENABLED);
    }

    public static boolean isLongformEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_longform", DefaultFeatureFlags.FEATURE_LONGFORM_ENABLED);
    }

    public static boolean isTheaterModeEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_theater_mode", DefaultFeatureFlags.FEATURE_THEATER_MODE_ENABLED);
    }

    public static boolean isProfileShareEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_enable_profile_share", DefaultFeatureFlags.FEATURE_PROFILE_SHARE);
    }

    public static boolean isPopularTabsEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_popular_tabs", DefaultFeatureFlags.FEATURE_ENABLE_POPULAR_TABS);
    }

    public static boolean isRecorder2Enabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_recorder2", DefaultFeatureFlags.FEATURE_RECORDER2);
    }

    public static boolean isRecorder2EnabledMarshmallow(Context context) {
        return sEnableRecorder2Marshmallow.getValue(context).booleanValue();
    }

    public static boolean isRecorderFilteringEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_filters", DefaultFeatureFlags.FEATURE_RECORDER_FILTERS);
    }

    public static boolean isMultiSourceImportEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "clients_flag_import_multi_source", DefaultFeatureFlags.FEATURE_IMPORT_MULTI_SOURCE);
    }

    public static boolean isShowTwitterScreennameEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_show_twitter_screenname", DefaultFeatureFlags.FEATURE_TWITTER_IDENTITY);
    }

    public static boolean isNuxHideChannelPickerEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_nux_hide_channel_picker", DefaultFeatureFlags.FEATURE_NUX_HIDE_CHANNEL_PICKER);
    }

    public static boolean isNuxHideFriendsFinderEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_nux_hide_friends_finder", DefaultFeatureFlags.FEATURE_NUX_HIDE_FRIENDS_FINDER);
    }

    public static boolean isNuxShowWelcomeFeedEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_nux_show_welcome_feed", DefaultFeatureFlags.FEATURE_NUX_SHOW_WELCOME_FEED);
    }

    public static boolean isCanHidePostsEnabled(Context context) {
        return sCanHidePosts.getValue(context).booleanValue();
    }

    public static boolean isSuggestedFeedMosaicInjectionEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_suggested_feed_mosaic_injection", DefaultFeatureFlags.FEATURE_SUGGESTED_FEED_MOSAIC_INJECTION);
    }

    public static boolean isVideoRemixConsumptionEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_video_remix_consumption", DefaultFeatureFlags.VIDEO_REMIX_CONSUMPTION_ENABLED);
    }

    public static boolean isPlaylistEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_playlist", DefaultFeatureFlags.FEATURE_PLAYLIST);
    }

    public static String getWelcomeFeedExitUrl(Context context) {
        return CommonUtil.getDefaultSharedPrefs(context).getString("client_flags_welcome_feed_exit_url", "vine://home");
    }

    public static int getAudioLatency(Application appContext, int defaultArtificialLatencyUs) {
        return CommonUtil.getDefaultSharedPrefs(appContext).getInt("client_flags_audio_latency", defaultArtificialLatencyUs);
    }

    public static long getLastCheckMillis(Context context) {
        return CommonUtil.getDefaultSharedPrefs(context).getLong("client_flags_last_checked_millis", 0L);
    }

    public static boolean doubleTapToLikeOnPause(Context context) {
        return CommonUtil.getDefaultSharedPrefs(context).getBoolean("client_flags_double_tap_to_like_on_pause", true);
    }

    public static boolean scribeEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_scribe_enabled", DefaultFeatureFlags.FEATURE_SCRIBE_ENABLED);
    }

    public static boolean prefetchEnabled(Context context) {
        return CommonUtil.getDefaultSharedPrefs(context).getBoolean("client_flags_prefetch_enabled", true);
    }

    public static boolean profileSortingEnabled(Context context) {
        return CommonUtil.getDefaultSharedPrefs(context).getBoolean("profile_sorting", false);
    }

    public static void setLastCheckMillis(Context context) {
        CommonUtil.getDefaultSharedPrefs(context).edit().putLong("client_flags_last_checked_millis", System.currentTimeMillis()).apply();
    }

    public static boolean solicitorEnabled(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_solicitor_enabled", DefaultFeatureFlags.FEATURE_SOLICITOR_ENABLED);
    }

    public static boolean showDiscoverStickyHeader(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_discover_sticky_header", DefaultFeatureFlags.FEATURE_DISCOVER_STICKY_HEADER);
    }

    public static boolean suggestedFeedVideoGrid(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "clients_flag_suggested_feed_video_grid", DefaultFeatureFlags.FEATURE_SUGGESTED_FEED_VIDEO_GRID);
    }

    public static boolean autoPlayPreviewVids(Context context) {
        return FeatureFlagResolver.resolveBooleanFlag(context, "client_flags_auto_play_preview_vids", DefaultFeatureFlags.FEATURE_AUTO_PLAY_PREVIEW_VIDS);
    }

    public static boolean multiAttributionsView(Context context) {
        return sMultiAttributions.getValue(context).booleanValue();
    }

    public static boolean isAccountSwitchingEnabled(Context context) {
        return sAccountSwitching.getValue(context).booleanValue();
    }

    public static boolean isFollowerMigrationEnabled(Context context) {
        return sFollowerMigration.getValue(context).booleanValue();
    }

    public static boolean isLinkTwitterPromptEnabled(Context context) {
        return sLinkTwitterPrompt.getValue(context).booleanValue();
    }

    public static boolean showTwitterFollowCard(Context context) {
        return sShowTwitterFollowCard.getValue(context).booleanValue();
    }

    public static boolean enableDownloader(Context context) {
        return sEnableDownloader.getValue(context).booleanValue();
    }

    public static boolean enableEmailDownloader(Context context) {
        return sEnableEmailDownloader.getValue(context).booleanValue();
    }

    public static boolean enableVideoRemix(Context context) {
        return sEnableVideoRemix.getValue(context).booleanValue();
    }

    public static boolean useNewEditScreen(Context context) {
        return sNewEditScreen.getValue(context).booleanValue();
    }

    public static boolean hostsDidChange(Context context, VineClientFlags clientFlags) {
        SharedPreferences prefs = CommonUtil.getDefaultSharedPrefs(context);
        String apiHost = prefs.getString("client_flags_api_host", null);
        String rtcHost = prefs.getString("client_flags_rtc_host", null);
        String mediaHost = prefs.getString("client_flags_media_host", null);
        String exploreHost = prefs.getString("client_flags_explore_host", null);
        if (apiHost != null) {
            if (!apiHost.equals(clientFlags.apiHost)) {
                return true;
            }
        } else if (clientFlags.apiHost != null) {
            return true;
        }
        if (rtcHost != null) {
            if (!rtcHost.equals(clientFlags.rtcHost)) {
                return true;
            }
        } else if (clientFlags.rtcHost != null) {
            return true;
        }
        if (mediaHost != null) {
            if (!mediaHost.equals(clientFlags.mediaHost)) {
                return true;
            }
        } else if (clientFlags.mediaHost != null) {
            return true;
        }
        return exploreHost != null ? !exploreHost.equals(clientFlags.exploreHost) : clientFlags.exploreHost != null;
    }

    static boolean isClientFlagRateLimited(Context context) {
        long lastChecked = getLastCheckMillis(context);
        boolean rateLimitClientFlagsFetch = System.currentTimeMillis() - lastChecked < 300000;
        return rateLimitClientFlagsFetch || isClientFlagsTtlInRange(context);
    }
}
