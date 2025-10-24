package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class EventDetails$$Parcelable implements Parcelable, ParcelWrapper<EventDetails> {
    public static final EventDetails$$Parcelable$Creator$$24 CREATOR = new EventDetails$$Parcelable$Creator$$24();
    private EventDetails eventDetails$$6;

    public EventDetails$$Parcelable(Parcel parcel$$262) {
        EventDetails eventDetails$$8;
        if (parcel$$262.readInt() == -1) {
            eventDetails$$8 = null;
        } else {
            eventDetails$$8 = readco_vine_android_scribe_model_EventDetails(parcel$$262);
        }
        this.eventDetails$$6 = eventDetails$$8;
    }

    public EventDetails$$Parcelable(EventDetails eventDetails$$10) {
        this.eventDetails$$6 = eventDetails$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$263, int flags) {
        if (this.eventDetails$$6 == null) {
            parcel$$263.writeInt(-1);
        } else {
            parcel$$263.writeInt(1);
            writeco_vine_android_scribe_model_EventDetails(this.eventDetails$$6, parcel$$263, flags);
        }
    }

    private EventDetails readco_vine_android_scribe_model_EventDetails(Parcel parcel$$264) {
        AlertDetails alertDetails$$12;
        TimingDetails timingDetails$$12;
        VideoImportDetails videoImportDetails$$12;
        ShareDetails shareDetails$$7;
        LaunchDetails launchDetails$$12;
        HTTPRequestDetails hTTPRequestDetails$$12;
        PlaybackSummaryDetails playbackSummaryDetails$$7;
        HTTPPerformanceData hTTPPerformanceData$$7;
        ArrayList<Item> list$$24;
        Item item$$14;
        Double double$$51;
        EventDetails eventDetails$$7 = new EventDetails();
        if (parcel$$264.readInt() == -1) {
            alertDetails$$12 = null;
        } else {
            alertDetails$$12 = readco_vine_android_scribe_model_AlertDetails(parcel$$264);
        }
        eventDetails$$7.alert = alertDetails$$12;
        if (parcel$$264.readInt() == -1) {
            timingDetails$$12 = null;
        } else {
            timingDetails$$12 = readco_vine_android_scribe_model_TimingDetails(parcel$$264);
        }
        eventDetails$$7.timing = timingDetails$$12;
        if (parcel$$264.readInt() == -1) {
            videoImportDetails$$12 = null;
        } else {
            videoImportDetails$$12 = readco_vine_android_scribe_model_VideoImportDetails(parcel$$264);
        }
        eventDetails$$7.videoImportDetails = videoImportDetails$$12;
        if (parcel$$264.readInt() == -1) {
            shareDetails$$7 = null;
        } else {
            shareDetails$$7 = readco_vine_android_scribe_model_ShareDetails(parcel$$264);
        }
        eventDetails$$7.share = shareDetails$$7;
        if (parcel$$264.readInt() == -1) {
            launchDetails$$12 = null;
        } else {
            launchDetails$$12 = readco_vine_android_scribe_model_LaunchDetails(parcel$$264);
        }
        eventDetails$$7.launch = launchDetails$$12;
        if (parcel$$264.readInt() == -1) {
            hTTPRequestDetails$$12 = null;
        } else {
            hTTPRequestDetails$$12 = readco_vine_android_scribe_model_HTTPRequestDetails(parcel$$264);
        }
        eventDetails$$7.httpRequestDetails = hTTPRequestDetails$$12;
        if (parcel$$264.readInt() == -1) {
            playbackSummaryDetails$$7 = null;
        } else {
            playbackSummaryDetails$$7 = readco_vine_android_scribe_model_PlaybackSummaryDetails(parcel$$264);
        }
        eventDetails$$7.playbackSummary = playbackSummaryDetails$$7;
        if (parcel$$264.readInt() == -1) {
            hTTPPerformanceData$$7 = null;
        } else {
            hTTPPerformanceData$$7 = readco_vine_android_scribe_model_HTTPPerformanceData(parcel$$264);
        }
        eventDetails$$7.httpPerformanceData = hTTPPerformanceData$$7;
        int int$$238 = parcel$$264.readInt();
        if (int$$238 < 0) {
            list$$24 = null;
        } else {
            list$$24 = new ArrayList<>();
            for (int int$$239 = 0; int$$239 < int$$238; int$$239++) {
                if (parcel$$264.readInt() == -1) {
                    item$$14 = null;
                } else {
                    item$$14 = readco_vine_android_scribe_model_Item(parcel$$264);
                }
                list$$24.add(item$$14);
            }
        }
        eventDetails$$7.items = list$$24;
        int int$$258 = parcel$$264.readInt();
        if (int$$258 < 0) {
            double$$51 = null;
        } else {
            double$$51 = Double.valueOf(parcel$$264.readDouble());
        }
        eventDetails$$7.timestamp = double$$51;
        return eventDetails$$7;
    }

    private AlertDetails readco_vine_android_scribe_model_AlertDetails(Parcel parcel$$265) {
        AlertDetails alertDetails$$11 = new AlertDetails();
        alertDetails$$11.name = parcel$$265.readString();
        alertDetails$$11.action = parcel$$265.readString();
        return alertDetails$$11;
    }

    private TimingDetails readco_vine_android_scribe_model_TimingDetails(Parcel parcel$$266) {
        Double double$$45;
        Double double$$46;
        TimingDetails timingDetails$$11 = new TimingDetails();
        int int$$213 = parcel$$266.readInt();
        if (int$$213 < 0) {
            double$$45 = null;
        } else {
            double$$45 = Double.valueOf(parcel$$266.readDouble());
        }
        timingDetails$$11.duration = double$$45;
        int int$$214 = parcel$$266.readInt();
        if (int$$214 < 0) {
            double$$46 = null;
        } else {
            double$$46 = Double.valueOf(parcel$$266.readDouble());
        }
        timingDetails$$11.startTimestamp = double$$46;
        return timingDetails$$11;
    }

    private VideoImportDetails readco_vine_android_scribe_model_VideoImportDetails(Parcel parcel$$267) {
        VideoImportDetails videoImportDetails$$11 = new VideoImportDetails();
        videoImportDetails$$11.result = parcel$$267.readString();
        return videoImportDetails$$11;
    }

    private ShareDetails readco_vine_android_scribe_model_ShareDetails(Parcel parcel$$268) {
        ArrayList<String> list$$22;
        ArrayList<VMRecipient> list$$23;
        VMRecipient vMRecipient$$14;
        Boolean boolean$$37;
        ShareDetails shareDetails$$6 = new ShareDetails();
        int int$$215 = parcel$$268.readInt();
        if (int$$215 < 0) {
            list$$22 = null;
        } else {
            list$$22 = new ArrayList<>();
            for (int int$$216 = 0; int$$216 < int$$215; int$$216++) {
                list$$22.add(parcel$$268.readString());
            }
        }
        shareDetails$$6.shareTargets = list$$22;
        shareDetails$$6.postId = parcel$$268.readString();
        int int$$217 = parcel$$268.readInt();
        if (int$$217 < 0) {
            list$$23 = null;
        } else {
            list$$23 = new ArrayList<>();
            for (int int$$218 = 0; int$$218 < int$$217; int$$218++) {
                if (parcel$$268.readInt() == -1) {
                    vMRecipient$$14 = null;
                } else {
                    vMRecipient$$14 = readco_vine_android_scribe_model_VMRecipient(parcel$$268);
                }
                list$$23.add(vMRecipient$$14);
            }
        }
        shareDetails$$6.messageRecipients = list$$23;
        int int$$223 = parcel$$268.readInt();
        if (int$$223 < 0) {
            boolean$$37 = null;
        } else {
            boolean$$37 = Boolean.valueOf(parcel$$268.readInt() == 1);
        }
        shareDetails$$6.hasComment = boolean$$37;
        return shareDetails$$6;
    }

    private VMRecipient readco_vine_android_scribe_model_VMRecipient(Parcel parcel$$269) {
        Boolean boolean$$34;
        Boolean boolean$$35;
        UserDetails userDetails$$20;
        VMRecipient vMRecipient$$13 = new VMRecipient();
        int int$$219 = parcel$$269.readInt();
        if (int$$219 < 0) {
            boolean$$34 = null;
        } else {
            boolean$$34 = Boolean.valueOf(parcel$$269.readInt() == 1);
        }
        vMRecipient$$13.isPhone = boolean$$34;
        int int$$220 = parcel$$269.readInt();
        if (int$$220 < 0) {
            boolean$$35 = null;
        } else {
            boolean$$35 = Boolean.valueOf(parcel$$269.readInt() == 1);
        }
        vMRecipient$$13.isEmail = boolean$$35;
        if (parcel$$269.readInt() == -1) {
            userDetails$$20 = null;
        } else {
            userDetails$$20 = readco_vine_android_scribe_model_UserDetails(parcel$$269);
        }
        vMRecipient$$13.user = userDetails$$20;
        return vMRecipient$$13;
    }

    private UserDetails readco_vine_android_scribe_model_UserDetails(Parcel parcel$$270) {
        Boolean boolean$$36;
        Long long$$68;
        UserDetails userDetails$$19 = new UserDetails();
        int int$$221 = parcel$$270.readInt();
        if (int$$221 < 0) {
            boolean$$36 = null;
        } else {
            boolean$$36 = Boolean.valueOf(parcel$$270.readInt() == 1);
        }
        userDetails$$19.following = boolean$$36;
        int int$$222 = parcel$$270.readInt();
        if (int$$222 < 0) {
            long$$68 = null;
        } else {
            long$$68 = Long.valueOf(parcel$$270.readLong());
        }
        userDetails$$19.userId = long$$68;
        return userDetails$$19;
    }

    private LaunchDetails readco_vine_android_scribe_model_LaunchDetails(Parcel parcel$$271) {
        LaunchDetails launchDetails$$11 = new LaunchDetails();
        launchDetails$$11.webSrc = parcel$$271.readString();
        return launchDetails$$11;
    }

    private HTTPRequestDetails readco_vine_android_scribe_model_HTTPRequestDetails(Parcel parcel$$272) {
        Integer integer$$20;
        Integer integer$$21;
        HTTPRequestDetails hTTPRequestDetails$$11 = new HTTPRequestDetails();
        hTTPRequestDetails$$11.method = parcel$$272.readString();
        int int$$224 = parcel$$272.readInt();
        if (int$$224 < 0) {
            integer$$20 = null;
        } else {
            integer$$20 = Integer.valueOf(parcel$$272.readInt());
        }
        hTTPRequestDetails$$11.apiError = integer$$20;
        int int$$225 = parcel$$272.readInt();
        if (int$$225 < 0) {
            integer$$21 = null;
        } else {
            integer$$21 = Integer.valueOf(parcel$$272.readInt());
        }
        hTTPRequestDetails$$11.httpStatus = integer$$21;
        hTTPRequestDetails$$11.osErrorDetails = parcel$$272.readString();
        hTTPRequestDetails$$11.networkError = parcel$$272.readString();
        hTTPRequestDetails$$11.url = parcel$$272.readString();
        return hTTPRequestDetails$$11;
    }

    private PlaybackSummaryDetails readco_vine_android_scribe_model_PlaybackSummaryDetails(Parcel parcel$$273) {
        Float float$$10;
        Float float$$11;
        Integer integer$$22;
        Float float$$12;
        Float float$$13;
        Float float$$14;
        PlaybackSummaryDetails playbackSummaryDetails$$6 = new PlaybackSummaryDetails();
        int int$$226 = parcel$$273.readInt();
        if (int$$226 < 0) {
            float$$10 = null;
        } else {
            float$$10 = Float.valueOf(parcel$$273.readFloat());
        }
        playbackSummaryDetails$$6.videoEndTime = float$$10;
        int int$$227 = parcel$$273.readInt();
        if (int$$227 < 0) {
            float$$11 = null;
        } else {
            float$$11 = Float.valueOf(parcel$$273.readFloat());
        }
        playbackSummaryDetails$$6.videoStarttime = float$$11;
        int int$$228 = parcel$$273.readInt();
        if (int$$228 < 0) {
            integer$$22 = null;
        } else {
            integer$$22 = Integer.valueOf(parcel$$273.readInt());
        }
        playbackSummaryDetails$$6.playbackInterruptions = integer$$22;
        int int$$229 = parcel$$273.readInt();
        if (int$$229 < 0) {
            float$$12 = null;
        } else {
            float$$12 = Float.valueOf(parcel$$273.readFloat());
        }
        playbackSummaryDetails$$6.timeSpentPlaying = float$$12;
        int int$$230 = parcel$$273.readInt();
        if (int$$230 < 0) {
            float$$13 = null;
        } else {
            float$$13 = Float.valueOf(parcel$$273.readFloat());
        }
        playbackSummaryDetails$$6.timeSpentBuffering = float$$13;
        int int$$231 = parcel$$273.readInt();
        if (int$$231 < 0) {
            float$$14 = null;
        } else {
            float$$14 = Float.valueOf(parcel$$273.readFloat());
        }
        playbackSummaryDetails$$6.timeSpentPaused = float$$14;
        return playbackSummaryDetails$$6;
    }

    private HTTPPerformanceData readco_vine_android_scribe_model_HTTPPerformanceData(Parcel parcel$$274) {
        Double double$$47;
        Long long$$69;
        Long long$$70;
        Double double$$48;
        Double double$$49;
        Double double$$50;
        HTTPPerformanceData hTTPPerformanceData$$6 = new HTTPPerformanceData();
        int int$$232 = parcel$$274.readInt();
        if (int$$232 < 0) {
            double$$47 = null;
        } else {
            double$$47 = Double.valueOf(parcel$$274.readDouble());
        }
        hTTPPerformanceData$$6.duration = double$$47;
        int int$$233 = parcel$$274.readInt();
        if (int$$233 < 0) {
            long$$69 = null;
        } else {
            long$$69 = Long.valueOf(parcel$$274.readLong());
        }
        hTTPPerformanceData$$6.bytesReceived = long$$69;
        int int$$234 = parcel$$274.readInt();
        if (int$$234 < 0) {
            long$$70 = null;
        } else {
            long$$70 = Long.valueOf(parcel$$274.readLong());
        }
        hTTPPerformanceData$$6.bytesSent = long$$70;
        int int$$235 = parcel$$274.readInt();
        if (int$$235 < 0) {
            double$$48 = null;
        } else {
            double$$48 = Double.valueOf(parcel$$274.readDouble());
        }
        hTTPPerformanceData$$6.durationToRequestSent = double$$48;
        int int$$236 = parcel$$274.readInt();
        if (int$$236 < 0) {
            double$$49 = null;
        } else {
            double$$49 = Double.valueOf(parcel$$274.readDouble());
        }
        hTTPPerformanceData$$6.startTimestamp = double$$49;
        int int$$237 = parcel$$274.readInt();
        if (int$$237 < 0) {
            double$$50 = null;
        } else {
            double$$50 = Double.valueOf(parcel$$274.readDouble());
        }
        hTTPPerformanceData$$6.durationToFirstByte = double$$50;
        return hTTPPerformanceData$$6;
    }

    private Item readco_vine_android_scribe_model_Item(Parcel parcel$$275) {
        PostOrRepostDetails postOrRepostDetails$$15;
        ActivityDetails activityDetails$$15;
        MosaicDetails mosaicDetails$$13;
        SuggestionDetails suggestionDetails$$15;
        MosaicDetails mosaicDetails$$14;
        CommentDetails commentDetails$$15;
        ItemPosition itemPosition$$15;
        TagDetails tagDetails$$15;
        UserDetails userDetails$$21;
        Item item$$13 = new Item();
        if (parcel$$275.readInt() == -1) {
            postOrRepostDetails$$15 = null;
        } else {
            postOrRepostDetails$$15 = readco_vine_android_scribe_model_PostOrRepostDetails(parcel$$275);
        }
        item$$13.postOrRepost = postOrRepostDetails$$15;
        item$$13.reference = parcel$$275.readString();
        item$$13.itemType = parcel$$275.readString();
        if (parcel$$275.readInt() == -1) {
            activityDetails$$15 = null;
        } else {
            activityDetails$$15 = readco_vine_android_scribe_model_ActivityDetails(parcel$$275);
        }
        item$$13.activity = activityDetails$$15;
        if (parcel$$275.readInt() == -1) {
            mosaicDetails$$13 = null;
        } else {
            mosaicDetails$$13 = readco_vine_android_scribe_model_MosaicDetails(parcel$$275);
        }
        item$$13.postMosaic = mosaicDetails$$13;
        if (parcel$$275.readInt() == -1) {
            suggestionDetails$$15 = null;
        } else {
            suggestionDetails$$15 = readco_vine_android_scribe_model_SuggestionDetails(parcel$$275);
        }
        item$$13.suggestion = suggestionDetails$$15;
        if (parcel$$275.readInt() == -1) {
            mosaicDetails$$14 = null;
        } else {
            mosaicDetails$$14 = readco_vine_android_scribe_model_MosaicDetails(parcel$$275);
        }
        item$$13.userMosaic = mosaicDetails$$14;
        if (parcel$$275.readInt() == -1) {
            commentDetails$$15 = null;
        } else {
            commentDetails$$15 = readco_vine_android_scribe_model_CommentDetails(parcel$$275);
        }
        item$$13.comment = commentDetails$$15;
        if (parcel$$275.readInt() == -1) {
            itemPosition$$15 = null;
        } else {
            itemPosition$$15 = readco_vine_android_scribe_model_ItemPosition(parcel$$275);
        }
        item$$13.position = itemPosition$$15;
        if (parcel$$275.readInt() == -1) {
            tagDetails$$15 = null;
        } else {
            tagDetails$$15 = readco_vine_android_scribe_model_TagDetails(parcel$$275);
        }
        item$$13.tag = tagDetails$$15;
        if (parcel$$275.readInt() == -1) {
            userDetails$$21 = null;
        } else {
            userDetails$$21 = readco_vine_android_scribe_model_UserDetails(parcel$$275);
        }
        item$$13.user = userDetails$$21;
        return item$$13;
    }

    private PostOrRepostDetails readco_vine_android_scribe_model_PostOrRepostDetails(Parcel parcel$$276) {
        Long long$$71;
        Long long$$72;
        Long long$$73;
        Boolean boolean$$38;
        Long long$$74;
        Byline byline$$13;
        Boolean boolean$$39;
        Boolean boolean$$40;
        PostOrRepostDetails postOrRepostDetails$$14 = new PostOrRepostDetails();
        int int$$240 = parcel$$276.readInt();
        if (int$$240 < 0) {
            long$$71 = null;
        } else {
            long$$71 = Long.valueOf(parcel$$276.readLong());
        }
        postOrRepostDetails$$14.postAuthorId = long$$71;
        postOrRepostDetails$$14.longformId = parcel$$276.readString();
        int int$$241 = parcel$$276.readInt();
        if (int$$241 < 0) {
            long$$72 = null;
        } else {
            long$$72 = Long.valueOf(parcel$$276.readLong());
        }
        postOrRepostDetails$$14.repostId = long$$72;
        int int$$242 = parcel$$276.readInt();
        if (int$$242 < 0) {
            long$$73 = null;
        } else {
            long$$73 = Long.valueOf(parcel$$276.readLong());
        }
        postOrRepostDetails$$14.repostAuthorId = long$$73;
        int int$$243 = parcel$$276.readInt();
        if (int$$243 < 0) {
            boolean$$38 = null;
        } else {
            boolean$$38 = Boolean.valueOf(parcel$$276.readInt() == 1);
        }
        postOrRepostDetails$$14.hasSimilarPosts = boolean$$38;
        int int$$244 = parcel$$276.readInt();
        if (int$$244 < 0) {
            long$$74 = null;
        } else {
            long$$74 = Long.valueOf(parcel$$276.readLong());
        }
        postOrRepostDetails$$14.postId = long$$74;
        if (parcel$$276.readInt() == -1) {
            byline$$13 = null;
        } else {
            byline$$13 = readco_vine_android_scribe_model_Byline(parcel$$276);
        }
        postOrRepostDetails$$14.byline = byline$$13;
        int int$$251 = parcel$$276.readInt();
        if (int$$251 < 0) {
            boolean$$39 = null;
        } else {
            boolean$$39 = Boolean.valueOf(parcel$$276.readInt() == 1);
        }
        postOrRepostDetails$$14.liked = boolean$$39;
        int int$$252 = parcel$$276.readInt();
        if (int$$252 < 0) {
            boolean$$40 = null;
        } else {
            boolean$$40 = Boolean.valueOf(parcel$$276.readInt() == 1);
        }
        postOrRepostDetails$$14.reposted = boolean$$40;
        return postOrRepostDetails$$14;
    }

    private Byline readco_vine_android_scribe_model_Byline(Parcel parcel$$277) {
        ArrayList<Long> list$$25;
        Long long$$75;
        ArrayList<Long> list$$26;
        Long long$$76;
        Byline byline$$12 = new Byline();
        byline$$12.detailedDescription = parcel$$277.readString();
        byline$$12.actionTitle = parcel$$277.readString();
        int int$$245 = parcel$$277.readInt();
        if (int$$245 < 0) {
            list$$25 = null;
        } else {
            list$$25 = new ArrayList<>();
            for (int int$$246 = 0; int$$246 < int$$245; int$$246++) {
                int int$$247 = parcel$$277.readInt();
                if (int$$247 < 0) {
                    long$$75 = null;
                } else {
                    long$$75 = Long.valueOf(parcel$$277.readLong());
                }
                list$$25.add(long$$75);
            }
        }
        byline$$12.postIds = list$$25;
        int int$$248 = parcel$$277.readInt();
        if (int$$248 < 0) {
            list$$26 = null;
        } else {
            list$$26 = new ArrayList<>();
            for (int int$$249 = 0; int$$249 < int$$248; int$$249++) {
                int int$$250 = parcel$$277.readInt();
                if (int$$250 < 0) {
                    long$$76 = null;
                } else {
                    long$$76 = Long.valueOf(parcel$$277.readLong());
                }
                list$$26.add(long$$76);
            }
        }
        byline$$12.userIds = list$$26;
        byline$$12.description = parcel$$277.readString();
        byline$$12.iconUrl = parcel$$277.readString();
        byline$$12.body = parcel$$277.readString();
        byline$$12.actionIconUrl = parcel$$277.readString();
        return byline$$12;
    }

    private ActivityDetails readco_vine_android_scribe_model_ActivityDetails(Parcel parcel$$278) {
        Long long$$77;
        Integer integer$$23;
        ActivityDetails activityDetails$$14 = new ActivityDetails();
        int int$$253 = parcel$$278.readInt();
        if (int$$253 < 0) {
            long$$77 = null;
        } else {
            long$$77 = Long.valueOf(parcel$$278.readLong());
        }
        activityDetails$$14.activityId = long$$77;
        int int$$254 = parcel$$278.readInt();
        if (int$$254 < 0) {
            integer$$23 = null;
        } else {
            integer$$23 = Integer.valueOf(parcel$$278.readInt());
        }
        activityDetails$$14.nMore = integer$$23;
        activityDetails$$14.activityType = parcel$$278.readString();
        return activityDetails$$14;
    }

    private MosaicDetails readco_vine_android_scribe_model_MosaicDetails(Parcel parcel$$279) {
        MosaicDetails mosaicDetails$$12 = new MosaicDetails();
        mosaicDetails$$12.mosaicType = parcel$$279.readString();
        mosaicDetails$$12.link = parcel$$279.readString();
        return mosaicDetails$$12;
    }

    private SuggestionDetails readco_vine_android_scribe_model_SuggestionDetails(Parcel parcel$$280) {
        SuggestionDetails suggestionDetails$$14 = new SuggestionDetails();
        suggestionDetails$$14.suggestedQuery = parcel$$280.readString();
        return suggestionDetails$$14;
    }

    private CommentDetails readco_vine_android_scribe_model_CommentDetails(Parcel parcel$$281) {
        Long long$$78;
        Long long$$79;
        CommentDetails commentDetails$$14 = new CommentDetails();
        int int$$255 = parcel$$281.readInt();
        if (int$$255 < 0) {
            long$$78 = null;
        } else {
            long$$78 = Long.valueOf(parcel$$281.readLong());
        }
        commentDetails$$14.commentId = long$$78;
        int int$$256 = parcel$$281.readInt();
        if (int$$256 < 0) {
            long$$79 = null;
        } else {
            long$$79 = Long.valueOf(parcel$$281.readLong());
        }
        commentDetails$$14.authorId = long$$79;
        return commentDetails$$14;
    }

    private ItemPosition readco_vine_android_scribe_model_ItemPosition(Parcel parcel$$282) {
        Integer integer$$24;
        ItemPosition itemPosition$$14 = new ItemPosition();
        int int$$257 = parcel$$282.readInt();
        if (int$$257 < 0) {
            integer$$24 = null;
        } else {
            integer$$24 = Integer.valueOf(parcel$$282.readInt());
        }
        itemPosition$$14.offset = integer$$24;
        return itemPosition$$14;
    }

    private TagDetails readco_vine_android_scribe_model_TagDetails(Parcel parcel$$283) {
        TagDetails tagDetails$$14 = new TagDetails();
        tagDetails$$14.tagId = parcel$$283.readString();
        return tagDetails$$14;
    }

    private void writeco_vine_android_scribe_model_EventDetails(EventDetails eventDetails$$9, Parcel parcel$$284, int flags$$95) {
        if (eventDetails$$9.alert == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(1);
            writeco_vine_android_scribe_model_AlertDetails(eventDetails$$9.alert, parcel$$284, flags$$95);
        }
        if (eventDetails$$9.timing == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(1);
            writeco_vine_android_scribe_model_TimingDetails(eventDetails$$9.timing, parcel$$284, flags$$95);
        }
        if (eventDetails$$9.videoImportDetails == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(1);
            writeco_vine_android_scribe_model_VideoImportDetails(eventDetails$$9.videoImportDetails, parcel$$284, flags$$95);
        }
        if (eventDetails$$9.share == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(1);
            writeco_vine_android_scribe_model_ShareDetails(eventDetails$$9.share, parcel$$284, flags$$95);
        }
        if (eventDetails$$9.launch == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(1);
            writeco_vine_android_scribe_model_LaunchDetails(eventDetails$$9.launch, parcel$$284, flags$$95);
        }
        if (eventDetails$$9.httpRequestDetails == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(1);
            writeco_vine_android_scribe_model_HTTPRequestDetails(eventDetails$$9.httpRequestDetails, parcel$$284, flags$$95);
        }
        if (eventDetails$$9.playbackSummary == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(1);
            writeco_vine_android_scribe_model_PlaybackSummaryDetails(eventDetails$$9.playbackSummary, parcel$$284, flags$$95);
        }
        if (eventDetails$$9.httpPerformanceData == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(1);
            writeco_vine_android_scribe_model_HTTPPerformanceData(eventDetails$$9.httpPerformanceData, parcel$$284, flags$$95);
        }
        if (eventDetails$$9.items == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(eventDetails$$9.items.size());
            for (Item item$$15 : eventDetails$$9.items) {
                if (item$$15 == null) {
                    parcel$$284.writeInt(-1);
                } else {
                    parcel$$284.writeInt(1);
                    writeco_vine_android_scribe_model_Item(item$$15, parcel$$284, flags$$95);
                }
            }
        }
        if (eventDetails$$9.timestamp == null) {
            parcel$$284.writeInt(-1);
        } else {
            parcel$$284.writeInt(1);
            parcel$$284.writeDouble(eventDetails$$9.timestamp.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_AlertDetails(AlertDetails alertDetails$$13, Parcel parcel$$285, int flags$$96) {
        parcel$$285.writeString(alertDetails$$13.name);
        parcel$$285.writeString(alertDetails$$13.action);
    }

    private void writeco_vine_android_scribe_model_TimingDetails(TimingDetails timingDetails$$13, Parcel parcel$$286, int flags$$97) {
        if (timingDetails$$13.duration == null) {
            parcel$$286.writeInt(-1);
        } else {
            parcel$$286.writeInt(1);
            parcel$$286.writeDouble(timingDetails$$13.duration.doubleValue());
        }
        if (timingDetails$$13.startTimestamp == null) {
            parcel$$286.writeInt(-1);
        } else {
            parcel$$286.writeInt(1);
            parcel$$286.writeDouble(timingDetails$$13.startTimestamp.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_VideoImportDetails(VideoImportDetails videoImportDetails$$13, Parcel parcel$$287, int flags$$98) {
        parcel$$287.writeString(videoImportDetails$$13.result);
    }

    private void writeco_vine_android_scribe_model_ShareDetails(ShareDetails shareDetails$$8, Parcel parcel$$288, int flags$$99) {
        if (shareDetails$$8.shareTargets == null) {
            parcel$$288.writeInt(-1);
        } else {
            parcel$$288.writeInt(shareDetails$$8.shareTargets.size());
            for (String string$$5 : shareDetails$$8.shareTargets) {
                parcel$$288.writeString(string$$5);
            }
        }
        parcel$$288.writeString(shareDetails$$8.postId);
        if (shareDetails$$8.messageRecipients == null) {
            parcel$$288.writeInt(-1);
        } else {
            parcel$$288.writeInt(shareDetails$$8.messageRecipients.size());
            for (VMRecipient vMRecipient$$15 : shareDetails$$8.messageRecipients) {
                if (vMRecipient$$15 == null) {
                    parcel$$288.writeInt(-1);
                } else {
                    parcel$$288.writeInt(1);
                    writeco_vine_android_scribe_model_VMRecipient(vMRecipient$$15, parcel$$288, flags$$99);
                }
            }
        }
        if (shareDetails$$8.hasComment == null) {
            parcel$$288.writeInt(-1);
        } else {
            parcel$$288.writeInt(1);
            parcel$$288.writeInt(shareDetails$$8.hasComment.booleanValue() ? 1 : 0);
        }
    }

    private void writeco_vine_android_scribe_model_VMRecipient(VMRecipient vMRecipient$$16, Parcel parcel$$289, int flags$$100) {
        if (vMRecipient$$16.isPhone == null) {
            parcel$$289.writeInt(-1);
        } else {
            parcel$$289.writeInt(1);
            parcel$$289.writeInt(vMRecipient$$16.isPhone.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$16.isEmail == null) {
            parcel$$289.writeInt(-1);
        } else {
            parcel$$289.writeInt(1);
            parcel$$289.writeInt(vMRecipient$$16.isEmail.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$16.user == null) {
            parcel$$289.writeInt(-1);
        } else {
            parcel$$289.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(vMRecipient$$16.user, parcel$$289, flags$$100);
        }
    }

    private void writeco_vine_android_scribe_model_UserDetails(UserDetails userDetails$$22, Parcel parcel$$290, int flags$$101) {
        if (userDetails$$22.following == null) {
            parcel$$290.writeInt(-1);
        } else {
            parcel$$290.writeInt(1);
            parcel$$290.writeInt(userDetails$$22.following.booleanValue() ? 1 : 0);
        }
        if (userDetails$$22.userId == null) {
            parcel$$290.writeInt(-1);
        } else {
            parcel$$290.writeInt(1);
            parcel$$290.writeLong(userDetails$$22.userId.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_LaunchDetails(LaunchDetails launchDetails$$13, Parcel parcel$$291, int flags$$102) {
        parcel$$291.writeString(launchDetails$$13.webSrc);
    }

    private void writeco_vine_android_scribe_model_HTTPRequestDetails(HTTPRequestDetails hTTPRequestDetails$$13, Parcel parcel$$292, int flags$$103) {
        parcel$$292.writeString(hTTPRequestDetails$$13.method);
        if (hTTPRequestDetails$$13.apiError == null) {
            parcel$$292.writeInt(-1);
        } else {
            parcel$$292.writeInt(1);
            parcel$$292.writeInt(hTTPRequestDetails$$13.apiError.intValue());
        }
        if (hTTPRequestDetails$$13.httpStatus == null) {
            parcel$$292.writeInt(-1);
        } else {
            parcel$$292.writeInt(1);
            parcel$$292.writeInt(hTTPRequestDetails$$13.httpStatus.intValue());
        }
        parcel$$292.writeString(hTTPRequestDetails$$13.osErrorDetails);
        parcel$$292.writeString(hTTPRequestDetails$$13.networkError);
        parcel$$292.writeString(hTTPRequestDetails$$13.url);
    }

    private void writeco_vine_android_scribe_model_PlaybackSummaryDetails(PlaybackSummaryDetails playbackSummaryDetails$$8, Parcel parcel$$293, int flags$$104) {
        if (playbackSummaryDetails$$8.videoEndTime == null) {
            parcel$$293.writeInt(-1);
        } else {
            parcel$$293.writeInt(1);
            parcel$$293.writeFloat(playbackSummaryDetails$$8.videoEndTime.floatValue());
        }
        if (playbackSummaryDetails$$8.videoStarttime == null) {
            parcel$$293.writeInt(-1);
        } else {
            parcel$$293.writeInt(1);
            parcel$$293.writeFloat(playbackSummaryDetails$$8.videoStarttime.floatValue());
        }
        if (playbackSummaryDetails$$8.playbackInterruptions == null) {
            parcel$$293.writeInt(-1);
        } else {
            parcel$$293.writeInt(1);
            parcel$$293.writeInt(playbackSummaryDetails$$8.playbackInterruptions.intValue());
        }
        if (playbackSummaryDetails$$8.timeSpentPlaying == null) {
            parcel$$293.writeInt(-1);
        } else {
            parcel$$293.writeInt(1);
            parcel$$293.writeFloat(playbackSummaryDetails$$8.timeSpentPlaying.floatValue());
        }
        if (playbackSummaryDetails$$8.timeSpentBuffering == null) {
            parcel$$293.writeInt(-1);
        } else {
            parcel$$293.writeInt(1);
            parcel$$293.writeFloat(playbackSummaryDetails$$8.timeSpentBuffering.floatValue());
        }
        if (playbackSummaryDetails$$8.timeSpentPaused == null) {
            parcel$$293.writeInt(-1);
        } else {
            parcel$$293.writeInt(1);
            parcel$$293.writeFloat(playbackSummaryDetails$$8.timeSpentPaused.floatValue());
        }
    }

    private void writeco_vine_android_scribe_model_HTTPPerformanceData(HTTPPerformanceData hTTPPerformanceData$$8, Parcel parcel$$294, int flags$$105) {
        if (hTTPPerformanceData$$8.duration == null) {
            parcel$$294.writeInt(-1);
        } else {
            parcel$$294.writeInt(1);
            parcel$$294.writeDouble(hTTPPerformanceData$$8.duration.doubleValue());
        }
        if (hTTPPerformanceData$$8.bytesReceived == null) {
            parcel$$294.writeInt(-1);
        } else {
            parcel$$294.writeInt(1);
            parcel$$294.writeLong(hTTPPerformanceData$$8.bytesReceived.longValue());
        }
        if (hTTPPerformanceData$$8.bytesSent == null) {
            parcel$$294.writeInt(-1);
        } else {
            parcel$$294.writeInt(1);
            parcel$$294.writeLong(hTTPPerformanceData$$8.bytesSent.longValue());
        }
        if (hTTPPerformanceData$$8.durationToRequestSent == null) {
            parcel$$294.writeInt(-1);
        } else {
            parcel$$294.writeInt(1);
            parcel$$294.writeDouble(hTTPPerformanceData$$8.durationToRequestSent.doubleValue());
        }
        if (hTTPPerformanceData$$8.startTimestamp == null) {
            parcel$$294.writeInt(-1);
        } else {
            parcel$$294.writeInt(1);
            parcel$$294.writeDouble(hTTPPerformanceData$$8.startTimestamp.doubleValue());
        }
        if (hTTPPerformanceData$$8.durationToFirstByte == null) {
            parcel$$294.writeInt(-1);
        } else {
            parcel$$294.writeInt(1);
            parcel$$294.writeDouble(hTTPPerformanceData$$8.durationToFirstByte.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_Item(Item item$$16, Parcel parcel$$295, int flags$$106) {
        if (item$$16.postOrRepost == null) {
            parcel$$295.writeInt(-1);
        } else {
            parcel$$295.writeInt(1);
            writeco_vine_android_scribe_model_PostOrRepostDetails(item$$16.postOrRepost, parcel$$295, flags$$106);
        }
        parcel$$295.writeString(item$$16.reference);
        parcel$$295.writeString(item$$16.itemType);
        if (item$$16.activity == null) {
            parcel$$295.writeInt(-1);
        } else {
            parcel$$295.writeInt(1);
            writeco_vine_android_scribe_model_ActivityDetails(item$$16.activity, parcel$$295, flags$$106);
        }
        if (item$$16.postMosaic == null) {
            parcel$$295.writeInt(-1);
        } else {
            parcel$$295.writeInt(1);
            writeco_vine_android_scribe_model_MosaicDetails(item$$16.postMosaic, parcel$$295, flags$$106);
        }
        if (item$$16.suggestion == null) {
            parcel$$295.writeInt(-1);
        } else {
            parcel$$295.writeInt(1);
            writeco_vine_android_scribe_model_SuggestionDetails(item$$16.suggestion, parcel$$295, flags$$106);
        }
        if (item$$16.userMosaic == null) {
            parcel$$295.writeInt(-1);
        } else {
            parcel$$295.writeInt(1);
            writeco_vine_android_scribe_model_MosaicDetails(item$$16.userMosaic, parcel$$295, flags$$106);
        }
        if (item$$16.comment == null) {
            parcel$$295.writeInt(-1);
        } else {
            parcel$$295.writeInt(1);
            writeco_vine_android_scribe_model_CommentDetails(item$$16.comment, parcel$$295, flags$$106);
        }
        if (item$$16.position == null) {
            parcel$$295.writeInt(-1);
        } else {
            parcel$$295.writeInt(1);
            writeco_vine_android_scribe_model_ItemPosition(item$$16.position, parcel$$295, flags$$106);
        }
        if (item$$16.tag == null) {
            parcel$$295.writeInt(-1);
        } else {
            parcel$$295.writeInt(1);
            writeco_vine_android_scribe_model_TagDetails(item$$16.tag, parcel$$295, flags$$106);
        }
        if (item$$16.user == null) {
            parcel$$295.writeInt(-1);
        } else {
            parcel$$295.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(item$$16.user, parcel$$295, flags$$106);
        }
    }

    private void writeco_vine_android_scribe_model_PostOrRepostDetails(PostOrRepostDetails postOrRepostDetails$$16, Parcel parcel$$296, int flags$$107) {
        if (postOrRepostDetails$$16.postAuthorId == null) {
            parcel$$296.writeInt(-1);
        } else {
            parcel$$296.writeInt(1);
            parcel$$296.writeLong(postOrRepostDetails$$16.postAuthorId.longValue());
        }
        parcel$$296.writeString(postOrRepostDetails$$16.longformId);
        if (postOrRepostDetails$$16.repostId == null) {
            parcel$$296.writeInt(-1);
        } else {
            parcel$$296.writeInt(1);
            parcel$$296.writeLong(postOrRepostDetails$$16.repostId.longValue());
        }
        if (postOrRepostDetails$$16.repostAuthorId == null) {
            parcel$$296.writeInt(-1);
        } else {
            parcel$$296.writeInt(1);
            parcel$$296.writeLong(postOrRepostDetails$$16.repostAuthorId.longValue());
        }
        if (postOrRepostDetails$$16.hasSimilarPosts == null) {
            parcel$$296.writeInt(-1);
        } else {
            parcel$$296.writeInt(1);
            parcel$$296.writeInt(postOrRepostDetails$$16.hasSimilarPosts.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$16.postId == null) {
            parcel$$296.writeInt(-1);
        } else {
            parcel$$296.writeInt(1);
            parcel$$296.writeLong(postOrRepostDetails$$16.postId.longValue());
        }
        if (postOrRepostDetails$$16.byline == null) {
            parcel$$296.writeInt(-1);
        } else {
            parcel$$296.writeInt(1);
            writeco_vine_android_scribe_model_Byline(postOrRepostDetails$$16.byline, parcel$$296, flags$$107);
        }
        if (postOrRepostDetails$$16.liked == null) {
            parcel$$296.writeInt(-1);
        } else {
            parcel$$296.writeInt(1);
            parcel$$296.writeInt(postOrRepostDetails$$16.liked.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$16.reposted == null) {
            parcel$$296.writeInt(-1);
        } else {
            parcel$$296.writeInt(1);
            parcel$$296.writeInt(postOrRepostDetails$$16.reposted.booleanValue() ? 1 : 0);
        }
    }

    private void writeco_vine_android_scribe_model_Byline(Byline byline$$14, Parcel parcel$$297, int flags$$108) {
        parcel$$297.writeString(byline$$14.detailedDescription);
        parcel$$297.writeString(byline$$14.actionTitle);
        if (byline$$14.postIds == null) {
            parcel$$297.writeInt(-1);
        } else {
            parcel$$297.writeInt(byline$$14.postIds.size());
            Iterator<Long> it = byline$$14.postIds.iterator();
            while (it.hasNext()) {
                Long long$$80 = it.next();
                if (long$$80 == null) {
                    parcel$$297.writeInt(-1);
                } else {
                    parcel$$297.writeInt(1);
                    parcel$$297.writeLong(long$$80.longValue());
                }
            }
        }
        if (byline$$14.userIds == null) {
            parcel$$297.writeInt(-1);
        } else {
            parcel$$297.writeInt(byline$$14.userIds.size());
            Iterator<Long> it2 = byline$$14.userIds.iterator();
            while (it2.hasNext()) {
                Long long$$81 = it2.next();
                if (long$$81 == null) {
                    parcel$$297.writeInt(-1);
                } else {
                    parcel$$297.writeInt(1);
                    parcel$$297.writeLong(long$$81.longValue());
                }
            }
        }
        parcel$$297.writeString(byline$$14.description);
        parcel$$297.writeString(byline$$14.iconUrl);
        parcel$$297.writeString(byline$$14.body);
        parcel$$297.writeString(byline$$14.actionIconUrl);
    }

    private void writeco_vine_android_scribe_model_ActivityDetails(ActivityDetails activityDetails$$16, Parcel parcel$$298, int flags$$109) {
        if (activityDetails$$16.activityId == null) {
            parcel$$298.writeInt(-1);
        } else {
            parcel$$298.writeInt(1);
            parcel$$298.writeLong(activityDetails$$16.activityId.longValue());
        }
        if (activityDetails$$16.nMore == null) {
            parcel$$298.writeInt(-1);
        } else {
            parcel$$298.writeInt(1);
            parcel$$298.writeInt(activityDetails$$16.nMore.intValue());
        }
        parcel$$298.writeString(activityDetails$$16.activityType);
    }

    private void writeco_vine_android_scribe_model_MosaicDetails(MosaicDetails mosaicDetails$$15, Parcel parcel$$299, int flags$$110) {
        parcel$$299.writeString(mosaicDetails$$15.mosaicType);
        parcel$$299.writeString(mosaicDetails$$15.link);
    }

    private void writeco_vine_android_scribe_model_SuggestionDetails(SuggestionDetails suggestionDetails$$16, Parcel parcel$$300, int flags$$111) {
        parcel$$300.writeString(suggestionDetails$$16.suggestedQuery);
    }

    private void writeco_vine_android_scribe_model_CommentDetails(CommentDetails commentDetails$$16, Parcel parcel$$301, int flags$$112) {
        if (commentDetails$$16.commentId == null) {
            parcel$$301.writeInt(-1);
        } else {
            parcel$$301.writeInt(1);
            parcel$$301.writeLong(commentDetails$$16.commentId.longValue());
        }
        if (commentDetails$$16.authorId == null) {
            parcel$$301.writeInt(-1);
        } else {
            parcel$$301.writeInt(1);
            parcel$$301.writeLong(commentDetails$$16.authorId.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_ItemPosition(ItemPosition itemPosition$$16, Parcel parcel$$302, int flags$$113) {
        if (itemPosition$$16.offset == null) {
            parcel$$302.writeInt(-1);
        } else {
            parcel$$302.writeInt(1);
            parcel$$302.writeInt(itemPosition$$16.offset.intValue());
        }
    }

    private void writeco_vine_android_scribe_model_TagDetails(TagDetails tagDetails$$16, Parcel parcel$$303, int flags$$114) {
        parcel$$303.writeString(tagDetails$$16.tagId);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public EventDetails getParcel() {
        return this.eventDetails$$6;
    }
}
