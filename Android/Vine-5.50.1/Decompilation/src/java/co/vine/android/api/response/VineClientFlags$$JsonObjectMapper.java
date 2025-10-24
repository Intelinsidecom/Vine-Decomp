package co.vine.android.api.response;

import com.bluelinelabs.logansquare.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineClientFlags$$JsonObjectMapper extends JsonMapper<VineClientFlags> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bluelinelabs.logansquare.JsonMapper
    public VineClientFlags parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    public static VineClientFlags _parse(JsonParser jsonParser) throws IOException {
        VineClientFlags instance = new VineClientFlags();
        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            parseField(instance, fieldName, jsonParser);
            jsonParser.skipChildren();
        }
        return instance;
    }

    public static void parseField(VineClientFlags vineClientFlags, String str, JsonParser jsonParser) throws IOException {
        if ("alwaysShowTimeInFeed".equals(str)) {
            vineClientFlags.alwaysShowTimeInFeed = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("apiHost".equals(str)) {
            vineClientFlags.apiHost = jsonParser.getValueAsString(null);
            return;
        }
        if ("audioLatencyUs".equals(str)) {
            vineClientFlags.audioLatencyUs = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Integer.valueOf(jsonParser.getValueAsInt()) : null;
            return;
        }
        if ("autoPlayPreviewVids".equals(str)) {
            vineClientFlags.autoPlayPreviewVids = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("canHidePosts".equals(str)) {
            vineClientFlags.canHidePosts = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("channelsForYouTabEnabled".equals(str)) {
            vineClientFlags.channelsForYouTabEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("useDigitsForPhoneVerification".equals(str)) {
            vineClientFlags.digitsEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("doubleTapToLikeOnPause".equals(str)) {
            vineClientFlags.doubleTapToLikeOnPause = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableAccountSwitchingAndroid".equals(str)) {
            vineClientFlags.enableAccountSwitching = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableEmailVideoImporter".equals(str)) {
            vineClientFlags.enableEmailVideoImporter = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableTwitterFollowingsImport".equals(str)) {
            vineClientFlags.enableFollowerMigration = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableImportMultiSource".equals(str)) {
            vineClientFlags.enableImportMultiSource = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("showLinkTwitterAlert".equals(str)) {
            vineClientFlags.enableLinkTwitterPrompt = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableMultiAttributionsView".equals(str)) {
            vineClientFlags.enableMultiAttributionsView = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enablePlaylistCreationDEV".equals(str)) {
            vineClientFlags.enablePlaylistCreationDEV = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableProfileShare".equals(str)) {
            vineClientFlags.enableProfileShare = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableRecorder2".equals(str)) {
            vineClientFlags.enableRecorder2 = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableRecorder2Marshmallow".equals(str)) {
            vineClientFlags.enableRecorder2Marshmallow = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableRecorderFilters".equals(str)) {
            vineClientFlags.enableRecorderFilters = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableThumbnailTransitionInExplore".equals(str)) {
            vineClientFlags.enableThumbnailTransitionInExplore = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableVideoImporter".equals(str)) {
            vineClientFlags.enableVideoImporter = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enableVideoRemix".equals(str)) {
            vineClientFlags.enableVideoRemix = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("exploreGridEnabled".equals(str)) {
            vineClientFlags.exploreGridEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("exploreGridLetterboxDetectionEnabled".equals(str)) {
            vineClientFlags.exploreGridLetterboxDetectionEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("exploreHost".equals(str)) {
            vineClientFlags.exploreHost = jsonParser.getValueAsString(null);
            return;
        }
        if ("explorePath".equals(str)) {
            vineClientFlags.explorePath = jsonParser.getValueAsString(null);
            return;
        }
        if ("insertionTimelines".equals(str)) {
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                ArrayList<String> arrayList = new ArrayList<>();
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    arrayList.add(jsonParser.getValueAsString(null));
                }
                vineClientFlags.insertionTimelines = arrayList;
                return;
            }
            vineClientFlags.insertionTimelines = null;
            return;
        }
        if ("showLongform".equals(str)) {
            vineClientFlags.longformEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("mediaHost".equals(str)) {
            vineClientFlags.mediaHost = jsonParser.getValueAsString(null);
            return;
        }
        if ("nuxHideChannelPicker".equals(str)) {
            vineClientFlags.nuxHideChannelPicker = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("nuxHideFriendsFinder".equals(str)) {
            vineClientFlags.nuxHideFriendsFinder = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("nuxShowWelcomeFeed".equals(str)) {
            vineClientFlags.nuxShowWelcomeFeed = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("popularTabsEnabled".equals(str)) {
            vineClientFlags.popularTabsEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("prefetchEnabled".equals(str)) {
            vineClientFlags.prefetchEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("profile_sorting".equals(str)) {
            vineClientFlags.profileSortingEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("rtcHost".equals(str)) {
            vineClientFlags.rtcHost = jsonParser.getValueAsString(null);
            return;
        }
        if ("scribeEnabled".equals(str)) {
            vineClientFlags.scribeEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("suggested_banner_pinnable".equals(str)) {
            vineClientFlags.showDiscoverStickyHeader = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("showTwitterFollowCardOnProfile".equals(str)) {
            vineClientFlags.showTwitterFollowCardOnProfile = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("newScreensWithTwitterScreenname".equals(str)) {
            vineClientFlags.showTwitterScreenname = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("similarPosts".equals(str)) {
            vineClientFlags.similarPostsEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("solicitorEnabled".equals(str)) {
            vineClientFlags.solicitorEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("suggestedFeedInject".equals(str)) {
            vineClientFlags.suggestedFeedMosaicInjection = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("suggestedFeedVideoGrid".equals(str)) {
            vineClientFlags.suggestedFeedVideoGrid = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("enablePlaylists".equals(str)) {
            vineClientFlags.theaterModeEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("ttl_s".equals(str)) {
            vineClientFlags.ttlSeconds = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Long.valueOf(jsonParser.getValueAsLong()) : null;
            return;
        }
        if ("enableNewEditScreen".equals(str)) {
            vineClientFlags.useNewEditScreen = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
            return;
        }
        if ("androidSeamless".equals(str)) {
            vineClientFlags.useVinePlayer = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
        } else if ("canViewVideoSources".equals(str)) {
            vineClientFlags.videoRemixConsumptionEnabled = jsonParser.getCurrentToken() != JsonToken.VALUE_NULL ? Boolean.valueOf(jsonParser.getValueAsBoolean()) : null;
        } else if ("welcomeFeedExitUrl".equals(str)) {
            vineClientFlags.welcomeFeedExitUrl = jsonParser.getValueAsString(null);
        }
    }

    @Override // com.bluelinelabs.logansquare.JsonMapper
    public void serialize(VineClientFlags object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, jsonGenerator, writeStartAndEnd);
    }

    public static void _serialize(VineClientFlags object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        if (object.alwaysShowTimeInFeed != null) {
            jsonGenerator.writeBooleanField("alwaysShowTimeInFeed", object.alwaysShowTimeInFeed.booleanValue());
        }
        if (object.apiHost != null) {
            jsonGenerator.writeStringField("apiHost", object.apiHost);
        }
        if (object.audioLatencyUs != null) {
            jsonGenerator.writeNumberField("audioLatencyUs", object.audioLatencyUs.intValue());
        }
        if (object.autoPlayPreviewVids != null) {
            jsonGenerator.writeBooleanField("autoPlayPreviewVids", object.autoPlayPreviewVids.booleanValue());
        }
        if (object.canHidePosts != null) {
            jsonGenerator.writeBooleanField("canHidePosts", object.canHidePosts.booleanValue());
        }
        if (object.channelsForYouTabEnabled != null) {
            jsonGenerator.writeBooleanField("channelsForYouTabEnabled", object.channelsForYouTabEnabled.booleanValue());
        }
        if (object.digitsEnabled != null) {
            jsonGenerator.writeBooleanField("useDigitsForPhoneVerification", object.digitsEnabled.booleanValue());
        }
        if (object.doubleTapToLikeOnPause != null) {
            jsonGenerator.writeBooleanField("doubleTapToLikeOnPause", object.doubleTapToLikeOnPause.booleanValue());
        }
        if (object.enableAccountSwitching != null) {
            jsonGenerator.writeBooleanField("enableAccountSwitchingAndroid", object.enableAccountSwitching.booleanValue());
        }
        if (object.enableEmailVideoImporter != null) {
            jsonGenerator.writeBooleanField("enableEmailVideoImporter", object.enableEmailVideoImporter.booleanValue());
        }
        if (object.enableFollowerMigration != null) {
            jsonGenerator.writeBooleanField("enableTwitterFollowingsImport", object.enableFollowerMigration.booleanValue());
        }
        if (object.enableImportMultiSource != null) {
            jsonGenerator.writeBooleanField("enableImportMultiSource", object.enableImportMultiSource.booleanValue());
        }
        if (object.enableLinkTwitterPrompt != null) {
            jsonGenerator.writeBooleanField("showLinkTwitterAlert", object.enableLinkTwitterPrompt.booleanValue());
        }
        if (object.enableMultiAttributionsView != null) {
            jsonGenerator.writeBooleanField("enableMultiAttributionsView", object.enableMultiAttributionsView.booleanValue());
        }
        if (object.enablePlaylistCreationDEV != null) {
            jsonGenerator.writeBooleanField("enablePlaylistCreationDEV", object.enablePlaylistCreationDEV.booleanValue());
        }
        if (object.enableProfileShare != null) {
            jsonGenerator.writeBooleanField("enableProfileShare", object.enableProfileShare.booleanValue());
        }
        if (object.enableRecorder2 != null) {
            jsonGenerator.writeBooleanField("enableRecorder2", object.enableRecorder2.booleanValue());
        }
        if (object.enableRecorder2Marshmallow != null) {
            jsonGenerator.writeBooleanField("enableRecorder2Marshmallow", object.enableRecorder2Marshmallow.booleanValue());
        }
        if (object.enableRecorderFilters != null) {
            jsonGenerator.writeBooleanField("enableRecorderFilters", object.enableRecorderFilters.booleanValue());
        }
        if (object.enableThumbnailTransitionInExplore != null) {
            jsonGenerator.writeBooleanField("enableThumbnailTransitionInExplore", object.enableThumbnailTransitionInExplore.booleanValue());
        }
        if (object.enableVideoImporter != null) {
            jsonGenerator.writeBooleanField("enableVideoImporter", object.enableVideoImporter.booleanValue());
        }
        if (object.enableVideoRemix != null) {
            jsonGenerator.writeBooleanField("enableVideoRemix", object.enableVideoRemix.booleanValue());
        }
        if (object.exploreGridEnabled != null) {
            jsonGenerator.writeBooleanField("exploreGridEnabled", object.exploreGridEnabled.booleanValue());
        }
        if (object.exploreGridLetterboxDetectionEnabled != null) {
            jsonGenerator.writeBooleanField("exploreGridLetterboxDetectionEnabled", object.exploreGridLetterboxDetectionEnabled.booleanValue());
        }
        if (object.exploreHost != null) {
            jsonGenerator.writeStringField("exploreHost", object.exploreHost);
        }
        if (object.explorePath != null) {
            jsonGenerator.writeStringField("explorePath", object.explorePath);
        }
        List<String> lslocalinsertionTimelines = object.insertionTimelines;
        if (lslocalinsertionTimelines != null) {
            jsonGenerator.writeFieldName("insertionTimelines");
            jsonGenerator.writeStartArray();
            for (String element1 : lslocalinsertionTimelines) {
                if (element1 != null) {
                    jsonGenerator.writeString(element1);
                }
            }
            jsonGenerator.writeEndArray();
        }
        if (object.longformEnabled != null) {
            jsonGenerator.writeBooleanField("showLongform", object.longformEnabled.booleanValue());
        }
        if (object.mediaHost != null) {
            jsonGenerator.writeStringField("mediaHost", object.mediaHost);
        }
        if (object.nuxHideChannelPicker != null) {
            jsonGenerator.writeBooleanField("nuxHideChannelPicker", object.nuxHideChannelPicker.booleanValue());
        }
        if (object.nuxHideFriendsFinder != null) {
            jsonGenerator.writeBooleanField("nuxHideFriendsFinder", object.nuxHideFriendsFinder.booleanValue());
        }
        if (object.nuxShowWelcomeFeed != null) {
            jsonGenerator.writeBooleanField("nuxShowWelcomeFeed", object.nuxShowWelcomeFeed.booleanValue());
        }
        if (object.popularTabsEnabled != null) {
            jsonGenerator.writeBooleanField("popularTabsEnabled", object.popularTabsEnabled.booleanValue());
        }
        if (object.prefetchEnabled != null) {
            jsonGenerator.writeBooleanField("prefetchEnabled", object.prefetchEnabled.booleanValue());
        }
        if (object.profileSortingEnabled != null) {
            jsonGenerator.writeBooleanField("profile_sorting", object.profileSortingEnabled.booleanValue());
        }
        if (object.rtcHost != null) {
            jsonGenerator.writeStringField("rtcHost", object.rtcHost);
        }
        if (object.scribeEnabled != null) {
            jsonGenerator.writeBooleanField("scribeEnabled", object.scribeEnabled.booleanValue());
        }
        if (object.showDiscoverStickyHeader != null) {
            jsonGenerator.writeBooleanField("suggested_banner_pinnable", object.showDiscoverStickyHeader.booleanValue());
        }
        if (object.showTwitterFollowCardOnProfile != null) {
            jsonGenerator.writeBooleanField("showTwitterFollowCardOnProfile", object.showTwitterFollowCardOnProfile.booleanValue());
        }
        if (object.showTwitterScreenname != null) {
            jsonGenerator.writeBooleanField("newScreensWithTwitterScreenname", object.showTwitterScreenname.booleanValue());
        }
        if (object.similarPostsEnabled != null) {
            jsonGenerator.writeBooleanField("similarPosts", object.similarPostsEnabled.booleanValue());
        }
        if (object.solicitorEnabled != null) {
            jsonGenerator.writeBooleanField("solicitorEnabled", object.solicitorEnabled.booleanValue());
        }
        if (object.suggestedFeedMosaicInjection != null) {
            jsonGenerator.writeBooleanField("suggestedFeedInject", object.suggestedFeedMosaicInjection.booleanValue());
        }
        if (object.suggestedFeedVideoGrid != null) {
            jsonGenerator.writeBooleanField("suggestedFeedVideoGrid", object.suggestedFeedVideoGrid.booleanValue());
        }
        if (object.theaterModeEnabled != null) {
            jsonGenerator.writeBooleanField("enablePlaylists", object.theaterModeEnabled.booleanValue());
        }
        if (object.ttlSeconds != null) {
            jsonGenerator.writeNumberField("ttl_s", object.ttlSeconds.longValue());
        }
        if (object.useNewEditScreen != null) {
            jsonGenerator.writeBooleanField("enableNewEditScreen", object.useNewEditScreen.booleanValue());
        }
        if (object.useVinePlayer != null) {
            jsonGenerator.writeBooleanField("androidSeamless", object.useVinePlayer.booleanValue());
        }
        if (object.videoRemixConsumptionEnabled != null) {
            jsonGenerator.writeBooleanField("canViewVideoSources", object.videoRemixConsumptionEnabled.booleanValue());
        }
        if (object.welcomeFeedExitUrl != null) {
            jsonGenerator.writeStringField("welcomeFeedExitUrl", object.welcomeFeedExitUrl);
        }
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
