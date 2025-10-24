package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineClientFlags$$Parcelable implements Parcelable, ParcelWrapper<VineClientFlags> {
    public static final VineClientFlags$$Parcelable$Creator$$27 CREATOR = new VineClientFlags$$Parcelable$Creator$$27();
    private VineClientFlags vineClientFlags$$0;

    public VineClientFlags$$Parcelable(Parcel parcel$$319) {
        VineClientFlags vineClientFlags$$2;
        if (parcel$$319.readInt() == -1) {
            vineClientFlags$$2 = null;
        } else {
            vineClientFlags$$2 = readco_vine_android_api_response_VineClientFlags(parcel$$319);
        }
        this.vineClientFlags$$0 = vineClientFlags$$2;
    }

    public VineClientFlags$$Parcelable(VineClientFlags vineClientFlags$$4) {
        this.vineClientFlags$$0 = vineClientFlags$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$320, int flags) {
        if (this.vineClientFlags$$0 == null) {
            parcel$$320.writeInt(-1);
        } else {
            parcel$$320.writeInt(1);
            writeco_vine_android_api_response_VineClientFlags(this.vineClientFlags$$0, parcel$$320, flags);
        }
    }

    private VineClientFlags readco_vine_android_api_response_VineClientFlags(Parcel parcel$$321) {
        Boolean boolean$$45;
        Boolean boolean$$46;
        Boolean boolean$$47;
        Integer integer$$25;
        Boolean boolean$$48;
        Boolean boolean$$49;
        Boolean boolean$$50;
        Boolean boolean$$51;
        ArrayList<String> list$$29;
        Boolean boolean$$52;
        Long long$$85;
        Boolean boolean$$53;
        Boolean boolean$$54;
        Boolean boolean$$55;
        Boolean boolean$$56;
        Boolean boolean$$57;
        Boolean boolean$$58;
        Boolean boolean$$59;
        Boolean boolean$$60;
        Boolean boolean$$61;
        Boolean boolean$$62;
        Boolean boolean$$63;
        Boolean boolean$$64;
        Boolean boolean$$65;
        Boolean boolean$$66;
        Boolean boolean$$67;
        Boolean boolean$$68;
        Boolean boolean$$69;
        Boolean boolean$$70;
        Boolean boolean$$71;
        Boolean boolean$$72;
        Boolean boolean$$73;
        Boolean boolean$$74;
        Boolean boolean$$75;
        Boolean boolean$$76;
        Boolean boolean$$77;
        Boolean boolean$$78;
        Boolean boolean$$79;
        Boolean boolean$$80;
        Boolean boolean$$81;
        Boolean boolean$$82;
        Boolean boolean$$83;
        Boolean boolean$$84;
        Boolean boolean$$85;
        VineClientFlags vineClientFlags$$1 = new VineClientFlags();
        int int$$274 = parcel$$321.readInt();
        if (int$$274 < 0) {
            boolean$$45 = null;
        } else {
            boolean$$45 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.scribeEnabled = boolean$$45;
        int int$$275 = parcel$$321.readInt();
        if (int$$275 < 0) {
            boolean$$46 = null;
        } else {
            boolean$$46 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.suggestedFeedVideoGrid = boolean$$46;
        int int$$276 = parcel$$321.readInt();
        if (int$$276 < 0) {
            boolean$$47 = null;
        } else {
            boolean$$47 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.useNewEditScreen = boolean$$47;
        int int$$277 = parcel$$321.readInt();
        if (int$$277 < 0) {
            integer$$25 = null;
        } else {
            integer$$25 = Integer.valueOf(parcel$$321.readInt());
        }
        vineClientFlags$$1.audioLatencyUs = integer$$25;
        int int$$278 = parcel$$321.readInt();
        if (int$$278 < 0) {
            boolean$$48 = null;
        } else {
            boolean$$48 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.profileSortingEnabled = boolean$$48;
        int int$$279 = parcel$$321.readInt();
        if (int$$279 < 0) {
            boolean$$49 = null;
        } else {
            boolean$$49 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableAccountSwitching = boolean$$49;
        int int$$280 = parcel$$321.readInt();
        if (int$$280 < 0) {
            boolean$$50 = null;
        } else {
            boolean$$50 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableMultiAttributionsView = boolean$$50;
        int int$$281 = parcel$$321.readInt();
        if (int$$281 < 0) {
            boolean$$51 = null;
        } else {
            boolean$$51 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.exploreGridEnabled = boolean$$51;
        int int$$282 = parcel$$321.readInt();
        if (int$$282 < 0) {
            list$$29 = null;
        } else {
            list$$29 = new ArrayList<>();
            for (int int$$283 = 0; int$$283 < int$$282; int$$283++) {
                list$$29.add(parcel$$321.readString());
            }
        }
        vineClientFlags$$1.insertionTimelines = list$$29;
        int int$$284 = parcel$$321.readInt();
        if (int$$284 < 0) {
            boolean$$52 = null;
        } else {
            boolean$$52 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.showTwitterFollowCardOnProfile = boolean$$52;
        int int$$285 = parcel$$321.readInt();
        if (int$$285 < 0) {
            long$$85 = null;
        } else {
            long$$85 = Long.valueOf(parcel$$321.readLong());
        }
        vineClientFlags$$1.ttlSeconds = long$$85;
        int int$$286 = parcel$$321.readInt();
        if (int$$286 < 0) {
            boolean$$53 = null;
        } else {
            boolean$$53 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableImportMultiSource = boolean$$53;
        int int$$287 = parcel$$321.readInt();
        if (int$$287 < 0) {
            boolean$$54 = null;
        } else {
            boolean$$54 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.nuxHideChannelPicker = boolean$$54;
        int int$$288 = parcel$$321.readInt();
        if (int$$288 < 0) {
            boolean$$55 = null;
        } else {
            boolean$$55 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.alwaysShowTimeInFeed = boolean$$55;
        vineClientFlags$$1.mediaHost = parcel$$321.readString();
        int int$$289 = parcel$$321.readInt();
        if (int$$289 < 0) {
            boolean$$56 = null;
        } else {
            boolean$$56 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableFollowerMigration = boolean$$56;
        int int$$290 = parcel$$321.readInt();
        if (int$$290 < 0) {
            boolean$$57 = null;
        } else {
            boolean$$57 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableVideoRemix = boolean$$57;
        vineClientFlags$$1.apiHost = parcel$$321.readString();
        vineClientFlags$$1.rtcHost = parcel$$321.readString();
        int int$$291 = parcel$$321.readInt();
        if (int$$291 < 0) {
            boolean$$58 = null;
        } else {
            boolean$$58 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.canHidePosts = boolean$$58;
        int int$$292 = parcel$$321.readInt();
        if (int$$292 < 0) {
            boolean$$59 = null;
        } else {
            boolean$$59 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableThumbnailTransitionInExplore = boolean$$59;
        int int$$293 = parcel$$321.readInt();
        if (int$$293 < 0) {
            boolean$$60 = null;
        } else {
            boolean$$60 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.popularTabsEnabled = boolean$$60;
        int int$$294 = parcel$$321.readInt();
        if (int$$294 < 0) {
            boolean$$61 = null;
        } else {
            boolean$$61 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.showTwitterScreenname = boolean$$61;
        int int$$295 = parcel$$321.readInt();
        if (int$$295 < 0) {
            boolean$$62 = null;
        } else {
            boolean$$62 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.exploreGridLetterboxDetectionEnabled = boolean$$62;
        vineClientFlags$$1.welcomeFeedExitUrl = parcel$$321.readString();
        int int$$296 = parcel$$321.readInt();
        if (int$$296 < 0) {
            boolean$$63 = null;
        } else {
            boolean$$63 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.channelsForYouTabEnabled = boolean$$63;
        int int$$297 = parcel$$321.readInt();
        if (int$$297 < 0) {
            boolean$$64 = null;
        } else {
            boolean$$64 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableLinkTwitterPrompt = boolean$$64;
        int int$$298 = parcel$$321.readInt();
        if (int$$298 < 0) {
            boolean$$65 = null;
        } else {
            boolean$$65 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.prefetchEnabled = boolean$$65;
        int int$$299 = parcel$$321.readInt();
        if (int$$299 < 0) {
            boolean$$66 = null;
        } else {
            boolean$$66 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.autoPlayPreviewVids = boolean$$66;
        int int$$300 = parcel$$321.readInt();
        if (int$$300 < 0) {
            boolean$$67 = null;
        } else {
            boolean$$67 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enablePlaylistCreationDEV = boolean$$67;
        int int$$301 = parcel$$321.readInt();
        if (int$$301 < 0) {
            boolean$$68 = null;
        } else {
            boolean$$68 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableRecorder2Marshmallow = boolean$$68;
        int int$$302 = parcel$$321.readInt();
        if (int$$302 < 0) {
            boolean$$69 = null;
        } else {
            boolean$$69 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableRecorderFilters = boolean$$69;
        int int$$303 = parcel$$321.readInt();
        if (int$$303 < 0) {
            boolean$$70 = null;
        } else {
            boolean$$70 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableRecorder2 = boolean$$70;
        int int$$304 = parcel$$321.readInt();
        if (int$$304 < 0) {
            boolean$$71 = null;
        } else {
            boolean$$71 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.solicitorEnabled = boolean$$71;
        int int$$305 = parcel$$321.readInt();
        if (int$$305 < 0) {
            boolean$$72 = null;
        } else {
            boolean$$72 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.theaterModeEnabled = boolean$$72;
        int int$$306 = parcel$$321.readInt();
        if (int$$306 < 0) {
            boolean$$73 = null;
        } else {
            boolean$$73 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.digitsEnabled = boolean$$73;
        int int$$307 = parcel$$321.readInt();
        if (int$$307 < 0) {
            boolean$$74 = null;
        } else {
            boolean$$74 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableVideoImporter = boolean$$74;
        int int$$308 = parcel$$321.readInt();
        if (int$$308 < 0) {
            boolean$$75 = null;
        } else {
            boolean$$75 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.nuxHideFriendsFinder = boolean$$75;
        int int$$309 = parcel$$321.readInt();
        if (int$$309 < 0) {
            boolean$$76 = null;
        } else {
            boolean$$76 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.showDiscoverStickyHeader = boolean$$76;
        int int$$310 = parcel$$321.readInt();
        if (int$$310 < 0) {
            boolean$$77 = null;
        } else {
            boolean$$77 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.suggestedFeedMosaicInjection = boolean$$77;
        int int$$311 = parcel$$321.readInt();
        if (int$$311 < 0) {
            boolean$$78 = null;
        } else {
            boolean$$78 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.nuxShowWelcomeFeed = boolean$$78;
        int int$$312 = parcel$$321.readInt();
        if (int$$312 < 0) {
            boolean$$79 = null;
        } else {
            boolean$$79 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.similarPostsEnabled = boolean$$79;
        vineClientFlags$$1.explorePath = parcel$$321.readString();
        int int$$313 = parcel$$321.readInt();
        if (int$$313 < 0) {
            boolean$$80 = null;
        } else {
            boolean$$80 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.videoRemixConsumptionEnabled = boolean$$80;
        int int$$314 = parcel$$321.readInt();
        if (int$$314 < 0) {
            boolean$$81 = null;
        } else {
            boolean$$81 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.useVinePlayer = boolean$$81;
        int int$$315 = parcel$$321.readInt();
        if (int$$315 < 0) {
            boolean$$82 = null;
        } else {
            boolean$$82 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableProfileShare = boolean$$82;
        int int$$316 = parcel$$321.readInt();
        if (int$$316 < 0) {
            boolean$$83 = null;
        } else {
            boolean$$83 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.doubleTapToLikeOnPause = boolean$$83;
        int int$$317 = parcel$$321.readInt();
        if (int$$317 < 0) {
            boolean$$84 = null;
        } else {
            boolean$$84 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.longformEnabled = boolean$$84;
        int int$$318 = parcel$$321.readInt();
        if (int$$318 < 0) {
            boolean$$85 = null;
        } else {
            boolean$$85 = Boolean.valueOf(parcel$$321.readInt() == 1);
        }
        vineClientFlags$$1.enableEmailVideoImporter = boolean$$85;
        vineClientFlags$$1.exploreHost = parcel$$321.readString();
        return vineClientFlags$$1;
    }

    private void writeco_vine_android_api_response_VineClientFlags(VineClientFlags vineClientFlags$$3, Parcel parcel$$322, int flags$$119) {
        if (vineClientFlags$$3.scribeEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.scribeEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.suggestedFeedVideoGrid == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.suggestedFeedVideoGrid.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.useNewEditScreen == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.useNewEditScreen.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.audioLatencyUs == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.audioLatencyUs.intValue());
        }
        if (vineClientFlags$$3.profileSortingEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.profileSortingEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableAccountSwitching == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableAccountSwitching.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableMultiAttributionsView == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableMultiAttributionsView.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.exploreGridEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.exploreGridEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.insertionTimelines == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(vineClientFlags$$3.insertionTimelines.size());
            Iterator<String> it = vineClientFlags$$3.insertionTimelines.iterator();
            while (it.hasNext()) {
                String string$$7 = it.next();
                parcel$$322.writeString(string$$7);
            }
        }
        if (vineClientFlags$$3.showTwitterFollowCardOnProfile == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.showTwitterFollowCardOnProfile.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.ttlSeconds == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeLong(vineClientFlags$$3.ttlSeconds.longValue());
        }
        if (vineClientFlags$$3.enableImportMultiSource == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableImportMultiSource.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.nuxHideChannelPicker == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.nuxHideChannelPicker.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.alwaysShowTimeInFeed == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.alwaysShowTimeInFeed.booleanValue() ? 1 : 0);
        }
        parcel$$322.writeString(vineClientFlags$$3.mediaHost);
        if (vineClientFlags$$3.enableFollowerMigration == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableFollowerMigration.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableVideoRemix == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableVideoRemix.booleanValue() ? 1 : 0);
        }
        parcel$$322.writeString(vineClientFlags$$3.apiHost);
        parcel$$322.writeString(vineClientFlags$$3.rtcHost);
        if (vineClientFlags$$3.canHidePosts == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.canHidePosts.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableThumbnailTransitionInExplore == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableThumbnailTransitionInExplore.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.popularTabsEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.popularTabsEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.showTwitterScreenname == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.showTwitterScreenname.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.exploreGridLetterboxDetectionEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.exploreGridLetterboxDetectionEnabled.booleanValue() ? 1 : 0);
        }
        parcel$$322.writeString(vineClientFlags$$3.welcomeFeedExitUrl);
        if (vineClientFlags$$3.channelsForYouTabEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.channelsForYouTabEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableLinkTwitterPrompt == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableLinkTwitterPrompt.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.prefetchEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.prefetchEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.autoPlayPreviewVids == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.autoPlayPreviewVids.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enablePlaylistCreationDEV == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enablePlaylistCreationDEV.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableRecorder2Marshmallow == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableRecorder2Marshmallow.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableRecorderFilters == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableRecorderFilters.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableRecorder2 == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableRecorder2.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.solicitorEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.solicitorEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.theaterModeEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.theaterModeEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.digitsEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.digitsEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableVideoImporter == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableVideoImporter.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.nuxHideFriendsFinder == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.nuxHideFriendsFinder.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.showDiscoverStickyHeader == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.showDiscoverStickyHeader.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.suggestedFeedMosaicInjection == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.suggestedFeedMosaicInjection.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.nuxShowWelcomeFeed == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.nuxShowWelcomeFeed.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.similarPostsEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.similarPostsEnabled.booleanValue() ? 1 : 0);
        }
        parcel$$322.writeString(vineClientFlags$$3.explorePath);
        if (vineClientFlags$$3.videoRemixConsumptionEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.videoRemixConsumptionEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.useVinePlayer == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.useVinePlayer.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableProfileShare == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableProfileShare.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.doubleTapToLikeOnPause == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.doubleTapToLikeOnPause.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.longformEnabled == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.longformEnabled.booleanValue() ? 1 : 0);
        }
        if (vineClientFlags$$3.enableEmailVideoImporter == null) {
            parcel$$322.writeInt(-1);
        } else {
            parcel$$322.writeInt(1);
            parcel$$322.writeInt(vineClientFlags$$3.enableEmailVideoImporter.booleanValue() ? 1 : 0);
        }
        parcel$$322.writeString(vineClientFlags$$3.exploreHost);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineClientFlags getParcel() {
        return this.vineClientFlags$$0;
    }
}
