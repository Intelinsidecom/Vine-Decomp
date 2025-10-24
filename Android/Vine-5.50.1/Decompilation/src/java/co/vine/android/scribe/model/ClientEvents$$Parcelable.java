package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class ClientEvents$$Parcelable implements Parcelable, ParcelWrapper<ClientEvents> {
    public static final ClientEvents$$Parcelable$Creator$$3 CREATOR = new ClientEvents$$Parcelable$Creator$$3();
    private ClientEvents clientEvents$$0;

    public ClientEvents$$Parcelable(Parcel parcel$$69) {
        ClientEvents clientEvents$$2;
        if (parcel$$69.readInt() == -1) {
            clientEvents$$2 = null;
        } else {
            clientEvents$$2 = readco_vine_android_scribe_model_ClientEvents(parcel$$69);
        }
        this.clientEvents$$0 = clientEvents$$2;
    }

    public ClientEvents$$Parcelable(ClientEvents clientEvents$$4) {
        this.clientEvents$$0 = clientEvents$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$70, int flags) {
        if (this.clientEvents$$0 == null) {
            parcel$$70.writeInt(-1);
        } else {
            parcel$$70.writeInt(1);
            writeco_vine_android_scribe_model_ClientEvents(this.clientEvents$$0, parcel$$70, flags);
        }
    }

    private ClientEvents readco_vine_android_scribe_model_ClientEvents(Parcel parcel$$71) {
        ArrayList<ClientEvent> list$$7;
        ClientEvent clientEvent$$6;
        ClientEvents clientEvents$$1 = new ClientEvents();
        int int$$67 = parcel$$71.readInt();
        if (int$$67 < 0) {
            list$$7 = null;
        } else {
            list$$7 = new ArrayList<>();
            for (int int$$68 = 0; int$$68 < int$$67; int$$68++) {
                if (parcel$$71.readInt() == -1) {
                    clientEvent$$6 = null;
                } else {
                    clientEvent$$6 = readco_vine_android_scribe_model_ClientEvent(parcel$$71);
                }
                list$$7.add(clientEvent$$6);
            }
        }
        clientEvents$$1.events = list$$7;
        return clientEvents$$1;
    }

    private ClientEvent readco_vine_android_scribe_model_ClientEvent(Parcel parcel$$72) {
        ApplicationState applicationState$$4;
        AppNavigation appNavigation$$4;
        EventDetails eventDetails$$4;
        DeviceData deviceData$$4;
        ClientEvent clientEvent$$5 = new ClientEvent();
        if (parcel$$72.readInt() == -1) {
            applicationState$$4 = null;
        } else {
            applicationState$$4 = readco_vine_android_scribe_model_ApplicationState(parcel$$72);
        }
        clientEvent$$5.appState = applicationState$$4;
        if (parcel$$72.readInt() == -1) {
            appNavigation$$4 = null;
        } else {
            appNavigation$$4 = readco_vine_android_scribe_model_AppNavigation(parcel$$72);
        }
        clientEvent$$5.navigation = appNavigation$$4;
        clientEvent$$5.clientId = parcel$$72.readString();
        if (parcel$$72.readInt() == -1) {
            eventDetails$$4 = null;
        } else {
            eventDetails$$4 = readco_vine_android_scribe_model_EventDetails(parcel$$72);
        }
        clientEvent$$5.eventDetails = eventDetails$$4;
        if (parcel$$72.readInt() == -1) {
            deviceData$$4 = null;
        } else {
            deviceData$$4 = readco_vine_android_scribe_model_DeviceData(parcel$$72);
        }
        clientEvent$$5.deviceData = deviceData$$4;
        clientEvent$$5.eventType = parcel$$72.readString();
        return clientEvent$$5;
    }

    private ApplicationState readco_vine_android_scribe_model_ApplicationState(Parcel parcel$$73) {
        ExperimentData experimentData$$4;
        Boolean boolean$$10;
        Boolean boolean$$11;
        Double double$$15;
        Long long$$19;
        Long long$$20;
        Long long$$21;
        ApplicationState applicationState$$3 = new ApplicationState();
        if (parcel$$73.readInt() == -1) {
            experimentData$$4 = null;
        } else {
            experimentData$$4 = readco_vine_android_scribe_model_ExperimentData(parcel$$73);
        }
        applicationState$$3.activeExperiments = experimentData$$4;
        int int$$71 = parcel$$73.readInt();
        if (int$$71 < 0) {
            boolean$$10 = null;
        } else {
            boolean$$10 = Boolean.valueOf(parcel$$73.readInt() == 1);
        }
        applicationState$$3.twitterConnected = boolean$$10;
        applicationState$$3.applicationStatus = parcel$$73.readString();
        int int$$72 = parcel$$73.readInt();
        if (int$$72 < 0) {
            boolean$$11 = null;
        } else {
            boolean$$11 = Boolean.valueOf(parcel$$73.readInt() == 1);
        }
        applicationState$$3.facebookConnected = boolean$$11;
        int int$$73 = parcel$$73.readInt();
        if (int$$73 < 0) {
            double$$15 = null;
        } else {
            double$$15 = Double.valueOf(parcel$$73.readDouble());
        }
        applicationState$$3.lastLaunchTimestamp = double$$15;
        applicationState$$3.edition = parcel$$73.readString();
        applicationState$$3.abConnected = parcel$$73.readInt() == 1;
        int int$$74 = parcel$$73.readInt();
        if (int$$74 < 0) {
            long$$19 = null;
        } else {
            long$$19 = Long.valueOf(parcel$$73.readLong());
        }
        applicationState$$3.videoCacheSize = long$$19;
        int int$$75 = parcel$$73.readInt();
        if (int$$75 < 0) {
            long$$20 = null;
        } else {
            long$$20 = Long.valueOf(parcel$$73.readLong());
        }
        applicationState$$3.loggedInUserId = long$$20;
        int int$$76 = parcel$$73.readInt();
        if (int$$76 < 0) {
            long$$21 = null;
        } else {
            long$$21 = Long.valueOf(parcel$$73.readLong());
        }
        applicationState$$3.numDrafts = long$$21;
        return applicationState$$3;
    }

    private ExperimentData readco_vine_android_scribe_model_ExperimentData(Parcel parcel$$74) {
        ArrayList<ExperimentValue> list$$8;
        ExperimentValue experimentValue$$5;
        ExperimentData experimentData$$3 = new ExperimentData();
        int int$$69 = parcel$$74.readInt();
        if (int$$69 < 0) {
            list$$8 = null;
        } else {
            list$$8 = new ArrayList<>();
            for (int int$$70 = 0; int$$70 < int$$69; int$$70++) {
                if (parcel$$74.readInt() == -1) {
                    experimentValue$$5 = null;
                } else {
                    experimentValue$$5 = readco_vine_android_scribe_model_ExperimentValue(parcel$$74);
                }
                list$$8.add(experimentValue$$5);
            }
        }
        experimentData$$3.experimentValues = list$$8;
        return experimentData$$3;
    }

    private ExperimentValue readco_vine_android_scribe_model_ExperimentValue(Parcel parcel$$75) {
        ExperimentValue experimentValue$$4 = new ExperimentValue();
        experimentValue$$4.value = parcel$$75.readString();
        experimentValue$$4.key = parcel$$75.readString();
        return experimentValue$$4;
    }

    private AppNavigation readco_vine_android_scribe_model_AppNavigation(Parcel parcel$$76) {
        AppNavigation appNavigation$$3 = new AppNavigation();
        appNavigation$$3.view = parcel$$76.readString();
        appNavigation$$3.timelineApiUrl = parcel$$76.readString();
        appNavigation$$3.ui_element = parcel$$76.readString();
        appNavigation$$3.captureSourceSection = parcel$$76.readString();
        appNavigation$$3.searchQuery = parcel$$76.readString();
        appNavigation$$3.isNewSearchView = parcel$$76.readInt() == 1;
        appNavigation$$3.section = parcel$$76.readString();
        appNavigation$$3.subview = parcel$$76.readString();
        appNavigation$$3.filtering = parcel$$76.readString();
        return appNavigation$$3;
    }

    private EventDetails readco_vine_android_scribe_model_EventDetails(Parcel parcel$$77) {
        AlertDetails alertDetails$$4;
        TimingDetails timingDetails$$4;
        VideoImportDetails videoImportDetails$$4;
        ShareDetails shareDetails$$4;
        LaunchDetails launchDetails$$9;
        HTTPRequestDetails hTTPRequestDetails$$4;
        PlaybackSummaryDetails playbackSummaryDetails$$4;
        HTTPPerformanceData hTTPPerformanceData$$4;
        ArrayList<Item> list$$11;
        Item item$$5;
        Double double$$22;
        EventDetails eventDetails$$3 = new EventDetails();
        if (parcel$$77.readInt() == -1) {
            alertDetails$$4 = null;
        } else {
            alertDetails$$4 = readco_vine_android_scribe_model_AlertDetails(parcel$$77);
        }
        eventDetails$$3.alert = alertDetails$$4;
        if (parcel$$77.readInt() == -1) {
            timingDetails$$4 = null;
        } else {
            timingDetails$$4 = readco_vine_android_scribe_model_TimingDetails(parcel$$77);
        }
        eventDetails$$3.timing = timingDetails$$4;
        if (parcel$$77.readInt() == -1) {
            videoImportDetails$$4 = null;
        } else {
            videoImportDetails$$4 = readco_vine_android_scribe_model_VideoImportDetails(parcel$$77);
        }
        eventDetails$$3.videoImportDetails = videoImportDetails$$4;
        if (parcel$$77.readInt() == -1) {
            shareDetails$$4 = null;
        } else {
            shareDetails$$4 = readco_vine_android_scribe_model_ShareDetails(parcel$$77);
        }
        eventDetails$$3.share = shareDetails$$4;
        if (parcel$$77.readInt() == -1) {
            launchDetails$$9 = null;
        } else {
            launchDetails$$9 = readco_vine_android_scribe_model_LaunchDetails(parcel$$77);
        }
        eventDetails$$3.launch = launchDetails$$9;
        if (parcel$$77.readInt() == -1) {
            hTTPRequestDetails$$4 = null;
        } else {
            hTTPRequestDetails$$4 = readco_vine_android_scribe_model_HTTPRequestDetails(parcel$$77);
        }
        eventDetails$$3.httpRequestDetails = hTTPRequestDetails$$4;
        if (parcel$$77.readInt() == -1) {
            playbackSummaryDetails$$4 = null;
        } else {
            playbackSummaryDetails$$4 = readco_vine_android_scribe_model_PlaybackSummaryDetails(parcel$$77);
        }
        eventDetails$$3.playbackSummary = playbackSummaryDetails$$4;
        if (parcel$$77.readInt() == -1) {
            hTTPPerformanceData$$4 = null;
        } else {
            hTTPPerformanceData$$4 = readco_vine_android_scribe_model_HTTPPerformanceData(parcel$$77);
        }
        eventDetails$$3.httpPerformanceData = hTTPPerformanceData$$4;
        int int$$102 = parcel$$77.readInt();
        if (int$$102 < 0) {
            list$$11 = null;
        } else {
            list$$11 = new ArrayList<>();
            for (int int$$103 = 0; int$$103 < int$$102; int$$103++) {
                if (parcel$$77.readInt() == -1) {
                    item$$5 = null;
                } else {
                    item$$5 = readco_vine_android_scribe_model_Item(parcel$$77);
                }
                list$$11.add(item$$5);
            }
        }
        eventDetails$$3.items = list$$11;
        int int$$122 = parcel$$77.readInt();
        if (int$$122 < 0) {
            double$$22 = null;
        } else {
            double$$22 = Double.valueOf(parcel$$77.readDouble());
        }
        eventDetails$$3.timestamp = double$$22;
        return eventDetails$$3;
    }

    private AlertDetails readco_vine_android_scribe_model_AlertDetails(Parcel parcel$$78) {
        AlertDetails alertDetails$$3 = new AlertDetails();
        alertDetails$$3.name = parcel$$78.readString();
        alertDetails$$3.action = parcel$$78.readString();
        return alertDetails$$3;
    }

    private TimingDetails readco_vine_android_scribe_model_TimingDetails(Parcel parcel$$79) {
        Double double$$16;
        Double double$$17;
        TimingDetails timingDetails$$3 = new TimingDetails();
        int int$$77 = parcel$$79.readInt();
        if (int$$77 < 0) {
            double$$16 = null;
        } else {
            double$$16 = Double.valueOf(parcel$$79.readDouble());
        }
        timingDetails$$3.duration = double$$16;
        int int$$78 = parcel$$79.readInt();
        if (int$$78 < 0) {
            double$$17 = null;
        } else {
            double$$17 = Double.valueOf(parcel$$79.readDouble());
        }
        timingDetails$$3.startTimestamp = double$$17;
        return timingDetails$$3;
    }

    private VideoImportDetails readco_vine_android_scribe_model_VideoImportDetails(Parcel parcel$$80) {
        VideoImportDetails videoImportDetails$$3 = new VideoImportDetails();
        videoImportDetails$$3.result = parcel$$80.readString();
        return videoImportDetails$$3;
    }

    private ShareDetails readco_vine_android_scribe_model_ShareDetails(Parcel parcel$$81) {
        ArrayList<String> list$$9;
        ArrayList<VMRecipient> list$$10;
        VMRecipient vMRecipient$$5;
        Boolean boolean$$15;
        ShareDetails shareDetails$$3 = new ShareDetails();
        int int$$79 = parcel$$81.readInt();
        if (int$$79 < 0) {
            list$$9 = null;
        } else {
            list$$9 = new ArrayList<>();
            for (int int$$80 = 0; int$$80 < int$$79; int$$80++) {
                list$$9.add(parcel$$81.readString());
            }
        }
        shareDetails$$3.shareTargets = list$$9;
        shareDetails$$3.postId = parcel$$81.readString();
        int int$$81 = parcel$$81.readInt();
        if (int$$81 < 0) {
            list$$10 = null;
        } else {
            list$$10 = new ArrayList<>();
            for (int int$$82 = 0; int$$82 < int$$81; int$$82++) {
                if (parcel$$81.readInt() == -1) {
                    vMRecipient$$5 = null;
                } else {
                    vMRecipient$$5 = readco_vine_android_scribe_model_VMRecipient(parcel$$81);
                }
                list$$10.add(vMRecipient$$5);
            }
        }
        shareDetails$$3.messageRecipients = list$$10;
        int int$$87 = parcel$$81.readInt();
        if (int$$87 < 0) {
            boolean$$15 = null;
        } else {
            boolean$$15 = Boolean.valueOf(parcel$$81.readInt() == 1);
        }
        shareDetails$$3.hasComment = boolean$$15;
        return shareDetails$$3;
    }

    private VMRecipient readco_vine_android_scribe_model_VMRecipient(Parcel parcel$$82) {
        Boolean boolean$$12;
        Boolean boolean$$13;
        UserDetails userDetails$$5;
        VMRecipient vMRecipient$$4 = new VMRecipient();
        int int$$83 = parcel$$82.readInt();
        if (int$$83 < 0) {
            boolean$$12 = null;
        } else {
            boolean$$12 = Boolean.valueOf(parcel$$82.readInt() == 1);
        }
        vMRecipient$$4.isPhone = boolean$$12;
        int int$$84 = parcel$$82.readInt();
        if (int$$84 < 0) {
            boolean$$13 = null;
        } else {
            boolean$$13 = Boolean.valueOf(parcel$$82.readInt() == 1);
        }
        vMRecipient$$4.isEmail = boolean$$13;
        if (parcel$$82.readInt() == -1) {
            userDetails$$5 = null;
        } else {
            userDetails$$5 = readco_vine_android_scribe_model_UserDetails(parcel$$82);
        }
        vMRecipient$$4.user = userDetails$$5;
        return vMRecipient$$4;
    }

    private UserDetails readco_vine_android_scribe_model_UserDetails(Parcel parcel$$83) {
        Boolean boolean$$14;
        Long long$$22;
        UserDetails userDetails$$4 = new UserDetails();
        int int$$85 = parcel$$83.readInt();
        if (int$$85 < 0) {
            boolean$$14 = null;
        } else {
            boolean$$14 = Boolean.valueOf(parcel$$83.readInt() == 1);
        }
        userDetails$$4.following = boolean$$14;
        int int$$86 = parcel$$83.readInt();
        if (int$$86 < 0) {
            long$$22 = null;
        } else {
            long$$22 = Long.valueOf(parcel$$83.readLong());
        }
        userDetails$$4.userId = long$$22;
        return userDetails$$4;
    }

    private LaunchDetails readco_vine_android_scribe_model_LaunchDetails(Parcel parcel$$84) {
        LaunchDetails launchDetails$$8 = new LaunchDetails();
        launchDetails$$8.webSrc = parcel$$84.readString();
        return launchDetails$$8;
    }

    private HTTPRequestDetails readco_vine_android_scribe_model_HTTPRequestDetails(Parcel parcel$$85) {
        Integer integer$$6;
        Integer integer$$7;
        HTTPRequestDetails hTTPRequestDetails$$3 = new HTTPRequestDetails();
        hTTPRequestDetails$$3.method = parcel$$85.readString();
        int int$$88 = parcel$$85.readInt();
        if (int$$88 < 0) {
            integer$$6 = null;
        } else {
            integer$$6 = Integer.valueOf(parcel$$85.readInt());
        }
        hTTPRequestDetails$$3.apiError = integer$$6;
        int int$$89 = parcel$$85.readInt();
        if (int$$89 < 0) {
            integer$$7 = null;
        } else {
            integer$$7 = Integer.valueOf(parcel$$85.readInt());
        }
        hTTPRequestDetails$$3.httpStatus = integer$$7;
        hTTPRequestDetails$$3.osErrorDetails = parcel$$85.readString();
        hTTPRequestDetails$$3.networkError = parcel$$85.readString();
        hTTPRequestDetails$$3.url = parcel$$85.readString();
        return hTTPRequestDetails$$3;
    }

    private PlaybackSummaryDetails readco_vine_android_scribe_model_PlaybackSummaryDetails(Parcel parcel$$86) {
        Float float$$5;
        Float float$$6;
        Integer integer$$8;
        Float float$$7;
        Float float$$8;
        Float float$$9;
        PlaybackSummaryDetails playbackSummaryDetails$$3 = new PlaybackSummaryDetails();
        int int$$90 = parcel$$86.readInt();
        if (int$$90 < 0) {
            float$$5 = null;
        } else {
            float$$5 = Float.valueOf(parcel$$86.readFloat());
        }
        playbackSummaryDetails$$3.videoEndTime = float$$5;
        int int$$91 = parcel$$86.readInt();
        if (int$$91 < 0) {
            float$$6 = null;
        } else {
            float$$6 = Float.valueOf(parcel$$86.readFloat());
        }
        playbackSummaryDetails$$3.videoStarttime = float$$6;
        int int$$92 = parcel$$86.readInt();
        if (int$$92 < 0) {
            integer$$8 = null;
        } else {
            integer$$8 = Integer.valueOf(parcel$$86.readInt());
        }
        playbackSummaryDetails$$3.playbackInterruptions = integer$$8;
        int int$$93 = parcel$$86.readInt();
        if (int$$93 < 0) {
            float$$7 = null;
        } else {
            float$$7 = Float.valueOf(parcel$$86.readFloat());
        }
        playbackSummaryDetails$$3.timeSpentPlaying = float$$7;
        int int$$94 = parcel$$86.readInt();
        if (int$$94 < 0) {
            float$$8 = null;
        } else {
            float$$8 = Float.valueOf(parcel$$86.readFloat());
        }
        playbackSummaryDetails$$3.timeSpentBuffering = float$$8;
        int int$$95 = parcel$$86.readInt();
        if (int$$95 < 0) {
            float$$9 = null;
        } else {
            float$$9 = Float.valueOf(parcel$$86.readFloat());
        }
        playbackSummaryDetails$$3.timeSpentPaused = float$$9;
        return playbackSummaryDetails$$3;
    }

    private HTTPPerformanceData readco_vine_android_scribe_model_HTTPPerformanceData(Parcel parcel$$87) {
        Double double$$18;
        Long long$$23;
        Long long$$24;
        Double double$$19;
        Double double$$20;
        Double double$$21;
        HTTPPerformanceData hTTPPerformanceData$$3 = new HTTPPerformanceData();
        int int$$96 = parcel$$87.readInt();
        if (int$$96 < 0) {
            double$$18 = null;
        } else {
            double$$18 = Double.valueOf(parcel$$87.readDouble());
        }
        hTTPPerformanceData$$3.duration = double$$18;
        int int$$97 = parcel$$87.readInt();
        if (int$$97 < 0) {
            long$$23 = null;
        } else {
            long$$23 = Long.valueOf(parcel$$87.readLong());
        }
        hTTPPerformanceData$$3.bytesReceived = long$$23;
        int int$$98 = parcel$$87.readInt();
        if (int$$98 < 0) {
            long$$24 = null;
        } else {
            long$$24 = Long.valueOf(parcel$$87.readLong());
        }
        hTTPPerformanceData$$3.bytesSent = long$$24;
        int int$$99 = parcel$$87.readInt();
        if (int$$99 < 0) {
            double$$19 = null;
        } else {
            double$$19 = Double.valueOf(parcel$$87.readDouble());
        }
        hTTPPerformanceData$$3.durationToRequestSent = double$$19;
        int int$$100 = parcel$$87.readInt();
        if (int$$100 < 0) {
            double$$20 = null;
        } else {
            double$$20 = Double.valueOf(parcel$$87.readDouble());
        }
        hTTPPerformanceData$$3.startTimestamp = double$$20;
        int int$$101 = parcel$$87.readInt();
        if (int$$101 < 0) {
            double$$21 = null;
        } else {
            double$$21 = Double.valueOf(parcel$$87.readDouble());
        }
        hTTPPerformanceData$$3.durationToFirstByte = double$$21;
        return hTTPPerformanceData$$3;
    }

    private Item readco_vine_android_scribe_model_Item(Parcel parcel$$88) {
        PostOrRepostDetails postOrRepostDetails$$4;
        ActivityDetails activityDetails$$4;
        MosaicDetails mosaicDetails$$5;
        SuggestionDetails suggestionDetails$$4;
        MosaicDetails mosaicDetails$$6;
        CommentDetails commentDetails$$4;
        ItemPosition itemPosition$$4;
        TagDetails tagDetails$$4;
        UserDetails userDetails$$6;
        Item item$$4 = new Item();
        if (parcel$$88.readInt() == -1) {
            postOrRepostDetails$$4 = null;
        } else {
            postOrRepostDetails$$4 = readco_vine_android_scribe_model_PostOrRepostDetails(parcel$$88);
        }
        item$$4.postOrRepost = postOrRepostDetails$$4;
        item$$4.reference = parcel$$88.readString();
        item$$4.itemType = parcel$$88.readString();
        if (parcel$$88.readInt() == -1) {
            activityDetails$$4 = null;
        } else {
            activityDetails$$4 = readco_vine_android_scribe_model_ActivityDetails(parcel$$88);
        }
        item$$4.activity = activityDetails$$4;
        if (parcel$$88.readInt() == -1) {
            mosaicDetails$$5 = null;
        } else {
            mosaicDetails$$5 = readco_vine_android_scribe_model_MosaicDetails(parcel$$88);
        }
        item$$4.postMosaic = mosaicDetails$$5;
        if (parcel$$88.readInt() == -1) {
            suggestionDetails$$4 = null;
        } else {
            suggestionDetails$$4 = readco_vine_android_scribe_model_SuggestionDetails(parcel$$88);
        }
        item$$4.suggestion = suggestionDetails$$4;
        if (parcel$$88.readInt() == -1) {
            mosaicDetails$$6 = null;
        } else {
            mosaicDetails$$6 = readco_vine_android_scribe_model_MosaicDetails(parcel$$88);
        }
        item$$4.userMosaic = mosaicDetails$$6;
        if (parcel$$88.readInt() == -1) {
            commentDetails$$4 = null;
        } else {
            commentDetails$$4 = readco_vine_android_scribe_model_CommentDetails(parcel$$88);
        }
        item$$4.comment = commentDetails$$4;
        if (parcel$$88.readInt() == -1) {
            itemPosition$$4 = null;
        } else {
            itemPosition$$4 = readco_vine_android_scribe_model_ItemPosition(parcel$$88);
        }
        item$$4.position = itemPosition$$4;
        if (parcel$$88.readInt() == -1) {
            tagDetails$$4 = null;
        } else {
            tagDetails$$4 = readco_vine_android_scribe_model_TagDetails(parcel$$88);
        }
        item$$4.tag = tagDetails$$4;
        if (parcel$$88.readInt() == -1) {
            userDetails$$6 = null;
        } else {
            userDetails$$6 = readco_vine_android_scribe_model_UserDetails(parcel$$88);
        }
        item$$4.user = userDetails$$6;
        return item$$4;
    }

    private PostOrRepostDetails readco_vine_android_scribe_model_PostOrRepostDetails(Parcel parcel$$89) {
        Long long$$25;
        Long long$$26;
        Long long$$27;
        Boolean boolean$$16;
        Long long$$28;
        Byline byline$$4;
        Boolean boolean$$17;
        Boolean boolean$$18;
        PostOrRepostDetails postOrRepostDetails$$3 = new PostOrRepostDetails();
        int int$$104 = parcel$$89.readInt();
        if (int$$104 < 0) {
            long$$25 = null;
        } else {
            long$$25 = Long.valueOf(parcel$$89.readLong());
        }
        postOrRepostDetails$$3.postAuthorId = long$$25;
        postOrRepostDetails$$3.longformId = parcel$$89.readString();
        int int$$105 = parcel$$89.readInt();
        if (int$$105 < 0) {
            long$$26 = null;
        } else {
            long$$26 = Long.valueOf(parcel$$89.readLong());
        }
        postOrRepostDetails$$3.repostId = long$$26;
        int int$$106 = parcel$$89.readInt();
        if (int$$106 < 0) {
            long$$27 = null;
        } else {
            long$$27 = Long.valueOf(parcel$$89.readLong());
        }
        postOrRepostDetails$$3.repostAuthorId = long$$27;
        int int$$107 = parcel$$89.readInt();
        if (int$$107 < 0) {
            boolean$$16 = null;
        } else {
            boolean$$16 = Boolean.valueOf(parcel$$89.readInt() == 1);
        }
        postOrRepostDetails$$3.hasSimilarPosts = boolean$$16;
        int int$$108 = parcel$$89.readInt();
        if (int$$108 < 0) {
            long$$28 = null;
        } else {
            long$$28 = Long.valueOf(parcel$$89.readLong());
        }
        postOrRepostDetails$$3.postId = long$$28;
        if (parcel$$89.readInt() == -1) {
            byline$$4 = null;
        } else {
            byline$$4 = readco_vine_android_scribe_model_Byline(parcel$$89);
        }
        postOrRepostDetails$$3.byline = byline$$4;
        int int$$115 = parcel$$89.readInt();
        if (int$$115 < 0) {
            boolean$$17 = null;
        } else {
            boolean$$17 = Boolean.valueOf(parcel$$89.readInt() == 1);
        }
        postOrRepostDetails$$3.liked = boolean$$17;
        int int$$116 = parcel$$89.readInt();
        if (int$$116 < 0) {
            boolean$$18 = null;
        } else {
            boolean$$18 = Boolean.valueOf(parcel$$89.readInt() == 1);
        }
        postOrRepostDetails$$3.reposted = boolean$$18;
        return postOrRepostDetails$$3;
    }

    private Byline readco_vine_android_scribe_model_Byline(Parcel parcel$$90) {
        ArrayList<Long> list$$12;
        Long long$$29;
        ArrayList<Long> list$$13;
        Long long$$30;
        Byline byline$$3 = new Byline();
        byline$$3.detailedDescription = parcel$$90.readString();
        byline$$3.actionTitle = parcel$$90.readString();
        int int$$109 = parcel$$90.readInt();
        if (int$$109 < 0) {
            list$$12 = null;
        } else {
            list$$12 = new ArrayList<>();
            for (int int$$110 = 0; int$$110 < int$$109; int$$110++) {
                int int$$111 = parcel$$90.readInt();
                if (int$$111 < 0) {
                    long$$29 = null;
                } else {
                    long$$29 = Long.valueOf(parcel$$90.readLong());
                }
                list$$12.add(long$$29);
            }
        }
        byline$$3.postIds = list$$12;
        int int$$112 = parcel$$90.readInt();
        if (int$$112 < 0) {
            list$$13 = null;
        } else {
            list$$13 = new ArrayList<>();
            for (int int$$113 = 0; int$$113 < int$$112; int$$113++) {
                int int$$114 = parcel$$90.readInt();
                if (int$$114 < 0) {
                    long$$30 = null;
                } else {
                    long$$30 = Long.valueOf(parcel$$90.readLong());
                }
                list$$13.add(long$$30);
            }
        }
        byline$$3.userIds = list$$13;
        byline$$3.description = parcel$$90.readString();
        byline$$3.iconUrl = parcel$$90.readString();
        byline$$3.body = parcel$$90.readString();
        byline$$3.actionIconUrl = parcel$$90.readString();
        return byline$$3;
    }

    private ActivityDetails readco_vine_android_scribe_model_ActivityDetails(Parcel parcel$$91) {
        Long long$$31;
        Integer integer$$9;
        ActivityDetails activityDetails$$3 = new ActivityDetails();
        int int$$117 = parcel$$91.readInt();
        if (int$$117 < 0) {
            long$$31 = null;
        } else {
            long$$31 = Long.valueOf(parcel$$91.readLong());
        }
        activityDetails$$3.activityId = long$$31;
        int int$$118 = parcel$$91.readInt();
        if (int$$118 < 0) {
            integer$$9 = null;
        } else {
            integer$$9 = Integer.valueOf(parcel$$91.readInt());
        }
        activityDetails$$3.nMore = integer$$9;
        activityDetails$$3.activityType = parcel$$91.readString();
        return activityDetails$$3;
    }

    private MosaicDetails readco_vine_android_scribe_model_MosaicDetails(Parcel parcel$$92) {
        MosaicDetails mosaicDetails$$4 = new MosaicDetails();
        mosaicDetails$$4.mosaicType = parcel$$92.readString();
        mosaicDetails$$4.link = parcel$$92.readString();
        return mosaicDetails$$4;
    }

    private SuggestionDetails readco_vine_android_scribe_model_SuggestionDetails(Parcel parcel$$93) {
        SuggestionDetails suggestionDetails$$3 = new SuggestionDetails();
        suggestionDetails$$3.suggestedQuery = parcel$$93.readString();
        return suggestionDetails$$3;
    }

    private CommentDetails readco_vine_android_scribe_model_CommentDetails(Parcel parcel$$94) {
        Long long$$32;
        Long long$$33;
        CommentDetails commentDetails$$3 = new CommentDetails();
        int int$$119 = parcel$$94.readInt();
        if (int$$119 < 0) {
            long$$32 = null;
        } else {
            long$$32 = Long.valueOf(parcel$$94.readLong());
        }
        commentDetails$$3.commentId = long$$32;
        int int$$120 = parcel$$94.readInt();
        if (int$$120 < 0) {
            long$$33 = null;
        } else {
            long$$33 = Long.valueOf(parcel$$94.readLong());
        }
        commentDetails$$3.authorId = long$$33;
        return commentDetails$$3;
    }

    private ItemPosition readco_vine_android_scribe_model_ItemPosition(Parcel parcel$$95) {
        Integer integer$$10;
        ItemPosition itemPosition$$3 = new ItemPosition();
        int int$$121 = parcel$$95.readInt();
        if (int$$121 < 0) {
            integer$$10 = null;
        } else {
            integer$$10 = Integer.valueOf(parcel$$95.readInt());
        }
        itemPosition$$3.offset = integer$$10;
        return itemPosition$$3;
    }

    private TagDetails readco_vine_android_scribe_model_TagDetails(Parcel parcel$$96) {
        TagDetails tagDetails$$3 = new TagDetails();
        tagDetails$$3.tagId = parcel$$96.readString();
        return tagDetails$$3;
    }

    private DeviceData readco_vine_android_scribe_model_DeviceData(Parcel parcel$$97) {
        GPSData gPSData$$4;
        Boolean boolean$$19;
        ArrayList<String> list$$14;
        Double double$$28;
        Long long$$34;
        MobileRadioDetails mobileRadioDetails$$4;
        Long long$$35;
        Double double$$29;
        DeviceData deviceData$$3 = new DeviceData();
        if (parcel$$97.readInt() == -1) {
            gPSData$$4 = null;
        } else {
            gPSData$$4 = readco_vine_android_scribe_model_GPSData(parcel$$97);
        }
        deviceData$$3.gpsData = gPSData$$4;
        deviceData$$3.orientation = parcel$$97.readString();
        deviceData$$3.os = parcel$$97.readString();
        deviceData$$3.timezone = parcel$$97.readString();
        deviceData$$3.deviceName = parcel$$97.readString();
        int int$$128 = parcel$$97.readInt();
        if (int$$128 < 0) {
            boolean$$19 = null;
        } else {
            boolean$$19 = Boolean.valueOf(parcel$$97.readInt() == 1);
        }
        deviceData$$3.otherAudioIsPlaying = boolean$$19;
        deviceData$$3.manufacturer = parcel$$97.readString();
        int int$$129 = parcel$$97.readInt();
        if (int$$129 < 0) {
            list$$14 = null;
        } else {
            list$$14 = new ArrayList<>();
            for (int int$$130 = 0; int$$130 < int$$129; int$$130++) {
                list$$14.add(parcel$$97.readString());
            }
        }
        deviceData$$3.languageCodes = list$$14;
        int int$$131 = parcel$$97.readInt();
        if (int$$131 < 0) {
            double$$28 = null;
        } else {
            double$$28 = Double.valueOf(parcel$$97.readDouble());
        }
        deviceData$$3.brightness = double$$28;
        deviceData$$3.osVersion = parcel$$97.readString();
        int int$$132 = parcel$$97.readInt();
        if (int$$132 < 0) {
            long$$34 = null;
        } else {
            long$$34 = Long.valueOf(parcel$$97.readLong());
        }
        deviceData$$3.bytesFree = long$$34;
        deviceData$$3.browser = parcel$$97.readString();
        deviceData$$3.browserVersion = parcel$$97.readString();
        deviceData$$3.deviceModel = parcel$$97.readString();
        deviceData$$3.internetAccessType = parcel$$97.readString();
        if (parcel$$97.readInt() == -1) {
            mobileRadioDetails$$4 = null;
        } else {
            mobileRadioDetails$$4 = readco_vine_android_scribe_model_MobileRadioDetails(parcel$$97);
        }
        deviceData$$3.radioDetails = mobileRadioDetails$$4;
        int int$$134 = parcel$$97.readInt();
        if (int$$134 < 0) {
            long$$35 = null;
        } else {
            long$$35 = Long.valueOf(parcel$$97.readLong());
        }
        deviceData$$3.bytesAvailable = long$$35;
        int int$$135 = parcel$$97.readInt();
        if (int$$135 < 0) {
            double$$29 = null;
        } else {
            double$$29 = Double.valueOf(parcel$$97.readDouble());
        }
        deviceData$$3.batteryLevel = double$$29;
        return deviceData$$3;
    }

    private GPSData readco_vine_android_scribe_model_GPSData(Parcel parcel$$98) {
        Double double$$23;
        Double double$$24;
        Double double$$25;
        Double double$$26;
        Double double$$27;
        GPSData gPSData$$3 = new GPSData();
        int int$$123 = parcel$$98.readInt();
        if (int$$123 < 0) {
            double$$23 = null;
        } else {
            double$$23 = Double.valueOf(parcel$$98.readDouble());
        }
        gPSData$$3.altitude = double$$23;
        int int$$124 = parcel$$98.readInt();
        if (int$$124 < 0) {
            double$$24 = null;
        } else {
            double$$24 = Double.valueOf(parcel$$98.readDouble());
        }
        gPSData$$3.verticalAccuracy = double$$24;
        int int$$125 = parcel$$98.readInt();
        if (int$$125 < 0) {
            double$$25 = null;
        } else {
            double$$25 = Double.valueOf(parcel$$98.readDouble());
        }
        gPSData$$3.latitude = double$$25;
        int int$$126 = parcel$$98.readInt();
        if (int$$126 < 0) {
            double$$26 = null;
        } else {
            double$$26 = Double.valueOf(parcel$$98.readDouble());
        }
        gPSData$$3.horizontalAccuracy = double$$26;
        int int$$127 = parcel$$98.readInt();
        if (int$$127 < 0) {
            double$$27 = null;
        } else {
            double$$27 = Double.valueOf(parcel$$98.readDouble());
        }
        gPSData$$3.longitude = double$$27;
        return gPSData$$3;
    }

    private MobileRadioDetails readco_vine_android_scribe_model_MobileRadioDetails(Parcel parcel$$99) {
        Integer integer$$11;
        MobileRadioDetails mobileRadioDetails$$3 = new MobileRadioDetails();
        mobileRadioDetails$$3.mobileNetworkOperatorName = parcel$$99.readString();
        mobileRadioDetails$$3.mobileNetworkOperatorCountryCode = parcel$$99.readString();
        mobileRadioDetails$$3.mobileSimProviderName = parcel$$99.readString();
        mobileRadioDetails$$3.radioStatus = parcel$$99.readString();
        int int$$133 = parcel$$99.readInt();
        if (int$$133 < 0) {
            integer$$11 = null;
        } else {
            integer$$11 = Integer.valueOf(parcel$$99.readInt());
        }
        mobileRadioDetails$$3.signalStrength = integer$$11;
        mobileRadioDetails$$3.mobileNetworkOperatorCode = parcel$$99.readString();
        mobileRadioDetails$$3.mobileSimProviderIsoCountryCode = parcel$$99.readString();
        mobileRadioDetails$$3.mobileSimProviderCode = parcel$$99.readString();
        mobileRadioDetails$$3.mobileNetworkOperatorIsoCountryCode = parcel$$99.readString();
        return mobileRadioDetails$$3;
    }

    private void writeco_vine_android_scribe_model_ClientEvents(ClientEvents clientEvents$$3, Parcel parcel$$100, int flags$$30) {
        if (clientEvents$$3.events == null) {
            parcel$$100.writeInt(-1);
            return;
        }
        parcel$$100.writeInt(clientEvents$$3.events.size());
        Iterator<ClientEvent> it = clientEvents$$3.events.iterator();
        while (it.hasNext()) {
            ClientEvent clientEvent$$7 = it.next();
            if (clientEvent$$7 == null) {
                parcel$$100.writeInt(-1);
            } else {
                parcel$$100.writeInt(1);
                writeco_vine_android_scribe_model_ClientEvent(clientEvent$$7, parcel$$100, flags$$30);
            }
        }
    }

    private void writeco_vine_android_scribe_model_ClientEvent(ClientEvent clientEvent$$8, Parcel parcel$$101, int flags$$31) {
        if (clientEvent$$8.appState == null) {
            parcel$$101.writeInt(-1);
        } else {
            parcel$$101.writeInt(1);
            writeco_vine_android_scribe_model_ApplicationState(clientEvent$$8.appState, parcel$$101, flags$$31);
        }
        if (clientEvent$$8.navigation == null) {
            parcel$$101.writeInt(-1);
        } else {
            parcel$$101.writeInt(1);
            writeco_vine_android_scribe_model_AppNavigation(clientEvent$$8.navigation, parcel$$101, flags$$31);
        }
        parcel$$101.writeString(clientEvent$$8.clientId);
        if (clientEvent$$8.eventDetails == null) {
            parcel$$101.writeInt(-1);
        } else {
            parcel$$101.writeInt(1);
            writeco_vine_android_scribe_model_EventDetails(clientEvent$$8.eventDetails, parcel$$101, flags$$31);
        }
        if (clientEvent$$8.deviceData == null) {
            parcel$$101.writeInt(-1);
        } else {
            parcel$$101.writeInt(1);
            writeco_vine_android_scribe_model_DeviceData(clientEvent$$8.deviceData, parcel$$101, flags$$31);
        }
        parcel$$101.writeString(clientEvent$$8.eventType);
    }

    private void writeco_vine_android_scribe_model_ApplicationState(ApplicationState applicationState$$5, Parcel parcel$$102, int flags$$32) {
        if (applicationState$$5.activeExperiments == null) {
            parcel$$102.writeInt(-1);
        } else {
            parcel$$102.writeInt(1);
            writeco_vine_android_scribe_model_ExperimentData(applicationState$$5.activeExperiments, parcel$$102, flags$$32);
        }
        if (applicationState$$5.twitterConnected == null) {
            parcel$$102.writeInt(-1);
        } else {
            parcel$$102.writeInt(1);
            parcel$$102.writeInt(applicationState$$5.twitterConnected.booleanValue() ? 1 : 0);
        }
        parcel$$102.writeString(applicationState$$5.applicationStatus);
        if (applicationState$$5.facebookConnected == null) {
            parcel$$102.writeInt(-1);
        } else {
            parcel$$102.writeInt(1);
            parcel$$102.writeInt(applicationState$$5.facebookConnected.booleanValue() ? 1 : 0);
        }
        if (applicationState$$5.lastLaunchTimestamp == null) {
            parcel$$102.writeInt(-1);
        } else {
            parcel$$102.writeInt(1);
            parcel$$102.writeDouble(applicationState$$5.lastLaunchTimestamp.doubleValue());
        }
        parcel$$102.writeString(applicationState$$5.edition);
        parcel$$102.writeInt(applicationState$$5.abConnected ? 1 : 0);
        if (applicationState$$5.videoCacheSize == null) {
            parcel$$102.writeInt(-1);
        } else {
            parcel$$102.writeInt(1);
            parcel$$102.writeLong(applicationState$$5.videoCacheSize.longValue());
        }
        if (applicationState$$5.loggedInUserId == null) {
            parcel$$102.writeInt(-1);
        } else {
            parcel$$102.writeInt(1);
            parcel$$102.writeLong(applicationState$$5.loggedInUserId.longValue());
        }
        if (applicationState$$5.numDrafts == null) {
            parcel$$102.writeInt(-1);
        } else {
            parcel$$102.writeInt(1);
            parcel$$102.writeLong(applicationState$$5.numDrafts.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_ExperimentData(ExperimentData experimentData$$5, Parcel parcel$$103, int flags$$33) {
        if (experimentData$$5.experimentValues == null) {
            parcel$$103.writeInt(-1);
            return;
        }
        parcel$$103.writeInt(experimentData$$5.experimentValues.size());
        Iterator<ExperimentValue> it = experimentData$$5.experimentValues.iterator();
        while (it.hasNext()) {
            ExperimentValue experimentValue$$6 = it.next();
            if (experimentValue$$6 == null) {
                parcel$$103.writeInt(-1);
            } else {
                parcel$$103.writeInt(1);
                writeco_vine_android_scribe_model_ExperimentValue(experimentValue$$6, parcel$$103, flags$$33);
            }
        }
    }

    private void writeco_vine_android_scribe_model_ExperimentValue(ExperimentValue experimentValue$$7, Parcel parcel$$104, int flags$$34) {
        parcel$$104.writeString(experimentValue$$7.value);
        parcel$$104.writeString(experimentValue$$7.key);
    }

    private void writeco_vine_android_scribe_model_AppNavigation(AppNavigation appNavigation$$5, Parcel parcel$$105, int flags$$35) {
        parcel$$105.writeString(appNavigation$$5.view);
        parcel$$105.writeString(appNavigation$$5.timelineApiUrl);
        parcel$$105.writeString(appNavigation$$5.ui_element);
        parcel$$105.writeString(appNavigation$$5.captureSourceSection);
        parcel$$105.writeString(appNavigation$$5.searchQuery);
        parcel$$105.writeInt(appNavigation$$5.isNewSearchView ? 1 : 0);
        parcel$$105.writeString(appNavigation$$5.section);
        parcel$$105.writeString(appNavigation$$5.subview);
        parcel$$105.writeString(appNavigation$$5.filtering);
    }

    private void writeco_vine_android_scribe_model_EventDetails(EventDetails eventDetails$$5, Parcel parcel$$106, int flags$$36) {
        if (eventDetails$$5.alert == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(1);
            writeco_vine_android_scribe_model_AlertDetails(eventDetails$$5.alert, parcel$$106, flags$$36);
        }
        if (eventDetails$$5.timing == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(1);
            writeco_vine_android_scribe_model_TimingDetails(eventDetails$$5.timing, parcel$$106, flags$$36);
        }
        if (eventDetails$$5.videoImportDetails == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(1);
            writeco_vine_android_scribe_model_VideoImportDetails(eventDetails$$5.videoImportDetails, parcel$$106, flags$$36);
        }
        if (eventDetails$$5.share == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(1);
            writeco_vine_android_scribe_model_ShareDetails(eventDetails$$5.share, parcel$$106, flags$$36);
        }
        if (eventDetails$$5.launch == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(1);
            writeco_vine_android_scribe_model_LaunchDetails(eventDetails$$5.launch, parcel$$106, flags$$36);
        }
        if (eventDetails$$5.httpRequestDetails == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(1);
            writeco_vine_android_scribe_model_HTTPRequestDetails(eventDetails$$5.httpRequestDetails, parcel$$106, flags$$36);
        }
        if (eventDetails$$5.playbackSummary == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(1);
            writeco_vine_android_scribe_model_PlaybackSummaryDetails(eventDetails$$5.playbackSummary, parcel$$106, flags$$36);
        }
        if (eventDetails$$5.httpPerformanceData == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(1);
            writeco_vine_android_scribe_model_HTTPPerformanceData(eventDetails$$5.httpPerformanceData, parcel$$106, flags$$36);
        }
        if (eventDetails$$5.items == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(eventDetails$$5.items.size());
            for (Item item$$6 : eventDetails$$5.items) {
                if (item$$6 == null) {
                    parcel$$106.writeInt(-1);
                } else {
                    parcel$$106.writeInt(1);
                    writeco_vine_android_scribe_model_Item(item$$6, parcel$$106, flags$$36);
                }
            }
        }
        if (eventDetails$$5.timestamp == null) {
            parcel$$106.writeInt(-1);
        } else {
            parcel$$106.writeInt(1);
            parcel$$106.writeDouble(eventDetails$$5.timestamp.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_AlertDetails(AlertDetails alertDetails$$5, Parcel parcel$$107, int flags$$37) {
        parcel$$107.writeString(alertDetails$$5.name);
        parcel$$107.writeString(alertDetails$$5.action);
    }

    private void writeco_vine_android_scribe_model_TimingDetails(TimingDetails timingDetails$$5, Parcel parcel$$108, int flags$$38) {
        if (timingDetails$$5.duration == null) {
            parcel$$108.writeInt(-1);
        } else {
            parcel$$108.writeInt(1);
            parcel$$108.writeDouble(timingDetails$$5.duration.doubleValue());
        }
        if (timingDetails$$5.startTimestamp == null) {
            parcel$$108.writeInt(-1);
        } else {
            parcel$$108.writeInt(1);
            parcel$$108.writeDouble(timingDetails$$5.startTimestamp.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_VideoImportDetails(VideoImportDetails videoImportDetails$$5, Parcel parcel$$109, int flags$$39) {
        parcel$$109.writeString(videoImportDetails$$5.result);
    }

    private void writeco_vine_android_scribe_model_ShareDetails(ShareDetails shareDetails$$5, Parcel parcel$$110, int flags$$40) {
        if (shareDetails$$5.shareTargets == null) {
            parcel$$110.writeInt(-1);
        } else {
            parcel$$110.writeInt(shareDetails$$5.shareTargets.size());
            for (String string$$2 : shareDetails$$5.shareTargets) {
                parcel$$110.writeString(string$$2);
            }
        }
        parcel$$110.writeString(shareDetails$$5.postId);
        if (shareDetails$$5.messageRecipients == null) {
            parcel$$110.writeInt(-1);
        } else {
            parcel$$110.writeInt(shareDetails$$5.messageRecipients.size());
            for (VMRecipient vMRecipient$$6 : shareDetails$$5.messageRecipients) {
                if (vMRecipient$$6 == null) {
                    parcel$$110.writeInt(-1);
                } else {
                    parcel$$110.writeInt(1);
                    writeco_vine_android_scribe_model_VMRecipient(vMRecipient$$6, parcel$$110, flags$$40);
                }
            }
        }
        if (shareDetails$$5.hasComment == null) {
            parcel$$110.writeInt(-1);
        } else {
            parcel$$110.writeInt(1);
            parcel$$110.writeInt(shareDetails$$5.hasComment.booleanValue() ? 1 : 0);
        }
    }

    private void writeco_vine_android_scribe_model_VMRecipient(VMRecipient vMRecipient$$7, Parcel parcel$$111, int flags$$41) {
        if (vMRecipient$$7.isPhone == null) {
            parcel$$111.writeInt(-1);
        } else {
            parcel$$111.writeInt(1);
            parcel$$111.writeInt(vMRecipient$$7.isPhone.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$7.isEmail == null) {
            parcel$$111.writeInt(-1);
        } else {
            parcel$$111.writeInt(1);
            parcel$$111.writeInt(vMRecipient$$7.isEmail.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$7.user == null) {
            parcel$$111.writeInt(-1);
        } else {
            parcel$$111.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(vMRecipient$$7.user, parcel$$111, flags$$41);
        }
    }

    private void writeco_vine_android_scribe_model_UserDetails(UserDetails userDetails$$7, Parcel parcel$$112, int flags$$42) {
        if (userDetails$$7.following == null) {
            parcel$$112.writeInt(-1);
        } else {
            parcel$$112.writeInt(1);
            parcel$$112.writeInt(userDetails$$7.following.booleanValue() ? 1 : 0);
        }
        if (userDetails$$7.userId == null) {
            parcel$$112.writeInt(-1);
        } else {
            parcel$$112.writeInt(1);
            parcel$$112.writeLong(userDetails$$7.userId.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_LaunchDetails(LaunchDetails launchDetails$$10, Parcel parcel$$113, int flags$$43) {
        parcel$$113.writeString(launchDetails$$10.webSrc);
    }

    private void writeco_vine_android_scribe_model_HTTPRequestDetails(HTTPRequestDetails hTTPRequestDetails$$5, Parcel parcel$$114, int flags$$44) {
        parcel$$114.writeString(hTTPRequestDetails$$5.method);
        if (hTTPRequestDetails$$5.apiError == null) {
            parcel$$114.writeInt(-1);
        } else {
            parcel$$114.writeInt(1);
            parcel$$114.writeInt(hTTPRequestDetails$$5.apiError.intValue());
        }
        if (hTTPRequestDetails$$5.httpStatus == null) {
            parcel$$114.writeInt(-1);
        } else {
            parcel$$114.writeInt(1);
            parcel$$114.writeInt(hTTPRequestDetails$$5.httpStatus.intValue());
        }
        parcel$$114.writeString(hTTPRequestDetails$$5.osErrorDetails);
        parcel$$114.writeString(hTTPRequestDetails$$5.networkError);
        parcel$$114.writeString(hTTPRequestDetails$$5.url);
    }

    private void writeco_vine_android_scribe_model_PlaybackSummaryDetails(PlaybackSummaryDetails playbackSummaryDetails$$5, Parcel parcel$$115, int flags$$45) {
        if (playbackSummaryDetails$$5.videoEndTime == null) {
            parcel$$115.writeInt(-1);
        } else {
            parcel$$115.writeInt(1);
            parcel$$115.writeFloat(playbackSummaryDetails$$5.videoEndTime.floatValue());
        }
        if (playbackSummaryDetails$$5.videoStarttime == null) {
            parcel$$115.writeInt(-1);
        } else {
            parcel$$115.writeInt(1);
            parcel$$115.writeFloat(playbackSummaryDetails$$5.videoStarttime.floatValue());
        }
        if (playbackSummaryDetails$$5.playbackInterruptions == null) {
            parcel$$115.writeInt(-1);
        } else {
            parcel$$115.writeInt(1);
            parcel$$115.writeInt(playbackSummaryDetails$$5.playbackInterruptions.intValue());
        }
        if (playbackSummaryDetails$$5.timeSpentPlaying == null) {
            parcel$$115.writeInt(-1);
        } else {
            parcel$$115.writeInt(1);
            parcel$$115.writeFloat(playbackSummaryDetails$$5.timeSpentPlaying.floatValue());
        }
        if (playbackSummaryDetails$$5.timeSpentBuffering == null) {
            parcel$$115.writeInt(-1);
        } else {
            parcel$$115.writeInt(1);
            parcel$$115.writeFloat(playbackSummaryDetails$$5.timeSpentBuffering.floatValue());
        }
        if (playbackSummaryDetails$$5.timeSpentPaused == null) {
            parcel$$115.writeInt(-1);
        } else {
            parcel$$115.writeInt(1);
            parcel$$115.writeFloat(playbackSummaryDetails$$5.timeSpentPaused.floatValue());
        }
    }

    private void writeco_vine_android_scribe_model_HTTPPerformanceData(HTTPPerformanceData hTTPPerformanceData$$5, Parcel parcel$$116, int flags$$46) {
        if (hTTPPerformanceData$$5.duration == null) {
            parcel$$116.writeInt(-1);
        } else {
            parcel$$116.writeInt(1);
            parcel$$116.writeDouble(hTTPPerformanceData$$5.duration.doubleValue());
        }
        if (hTTPPerformanceData$$5.bytesReceived == null) {
            parcel$$116.writeInt(-1);
        } else {
            parcel$$116.writeInt(1);
            parcel$$116.writeLong(hTTPPerformanceData$$5.bytesReceived.longValue());
        }
        if (hTTPPerformanceData$$5.bytesSent == null) {
            parcel$$116.writeInt(-1);
        } else {
            parcel$$116.writeInt(1);
            parcel$$116.writeLong(hTTPPerformanceData$$5.bytesSent.longValue());
        }
        if (hTTPPerformanceData$$5.durationToRequestSent == null) {
            parcel$$116.writeInt(-1);
        } else {
            parcel$$116.writeInt(1);
            parcel$$116.writeDouble(hTTPPerformanceData$$5.durationToRequestSent.doubleValue());
        }
        if (hTTPPerformanceData$$5.startTimestamp == null) {
            parcel$$116.writeInt(-1);
        } else {
            parcel$$116.writeInt(1);
            parcel$$116.writeDouble(hTTPPerformanceData$$5.startTimestamp.doubleValue());
        }
        if (hTTPPerformanceData$$5.durationToFirstByte == null) {
            parcel$$116.writeInt(-1);
        } else {
            parcel$$116.writeInt(1);
            parcel$$116.writeDouble(hTTPPerformanceData$$5.durationToFirstByte.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_Item(Item item$$7, Parcel parcel$$117, int flags$$47) {
        if (item$$7.postOrRepost == null) {
            parcel$$117.writeInt(-1);
        } else {
            parcel$$117.writeInt(1);
            writeco_vine_android_scribe_model_PostOrRepostDetails(item$$7.postOrRepost, parcel$$117, flags$$47);
        }
        parcel$$117.writeString(item$$7.reference);
        parcel$$117.writeString(item$$7.itemType);
        if (item$$7.activity == null) {
            parcel$$117.writeInt(-1);
        } else {
            parcel$$117.writeInt(1);
            writeco_vine_android_scribe_model_ActivityDetails(item$$7.activity, parcel$$117, flags$$47);
        }
        if (item$$7.postMosaic == null) {
            parcel$$117.writeInt(-1);
        } else {
            parcel$$117.writeInt(1);
            writeco_vine_android_scribe_model_MosaicDetails(item$$7.postMosaic, parcel$$117, flags$$47);
        }
        if (item$$7.suggestion == null) {
            parcel$$117.writeInt(-1);
        } else {
            parcel$$117.writeInt(1);
            writeco_vine_android_scribe_model_SuggestionDetails(item$$7.suggestion, parcel$$117, flags$$47);
        }
        if (item$$7.userMosaic == null) {
            parcel$$117.writeInt(-1);
        } else {
            parcel$$117.writeInt(1);
            writeco_vine_android_scribe_model_MosaicDetails(item$$7.userMosaic, parcel$$117, flags$$47);
        }
        if (item$$7.comment == null) {
            parcel$$117.writeInt(-1);
        } else {
            parcel$$117.writeInt(1);
            writeco_vine_android_scribe_model_CommentDetails(item$$7.comment, parcel$$117, flags$$47);
        }
        if (item$$7.position == null) {
            parcel$$117.writeInt(-1);
        } else {
            parcel$$117.writeInt(1);
            writeco_vine_android_scribe_model_ItemPosition(item$$7.position, parcel$$117, flags$$47);
        }
        if (item$$7.tag == null) {
            parcel$$117.writeInt(-1);
        } else {
            parcel$$117.writeInt(1);
            writeco_vine_android_scribe_model_TagDetails(item$$7.tag, parcel$$117, flags$$47);
        }
        if (item$$7.user == null) {
            parcel$$117.writeInt(-1);
        } else {
            parcel$$117.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(item$$7.user, parcel$$117, flags$$47);
        }
    }

    private void writeco_vine_android_scribe_model_PostOrRepostDetails(PostOrRepostDetails postOrRepostDetails$$5, Parcel parcel$$118, int flags$$48) {
        if (postOrRepostDetails$$5.postAuthorId == null) {
            parcel$$118.writeInt(-1);
        } else {
            parcel$$118.writeInt(1);
            parcel$$118.writeLong(postOrRepostDetails$$5.postAuthorId.longValue());
        }
        parcel$$118.writeString(postOrRepostDetails$$5.longformId);
        if (postOrRepostDetails$$5.repostId == null) {
            parcel$$118.writeInt(-1);
        } else {
            parcel$$118.writeInt(1);
            parcel$$118.writeLong(postOrRepostDetails$$5.repostId.longValue());
        }
        if (postOrRepostDetails$$5.repostAuthorId == null) {
            parcel$$118.writeInt(-1);
        } else {
            parcel$$118.writeInt(1);
            parcel$$118.writeLong(postOrRepostDetails$$5.repostAuthorId.longValue());
        }
        if (postOrRepostDetails$$5.hasSimilarPosts == null) {
            parcel$$118.writeInt(-1);
        } else {
            parcel$$118.writeInt(1);
            parcel$$118.writeInt(postOrRepostDetails$$5.hasSimilarPosts.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$5.postId == null) {
            parcel$$118.writeInt(-1);
        } else {
            parcel$$118.writeInt(1);
            parcel$$118.writeLong(postOrRepostDetails$$5.postId.longValue());
        }
        if (postOrRepostDetails$$5.byline == null) {
            parcel$$118.writeInt(-1);
        } else {
            parcel$$118.writeInt(1);
            writeco_vine_android_scribe_model_Byline(postOrRepostDetails$$5.byline, parcel$$118, flags$$48);
        }
        if (postOrRepostDetails$$5.liked == null) {
            parcel$$118.writeInt(-1);
        } else {
            parcel$$118.writeInt(1);
            parcel$$118.writeInt(postOrRepostDetails$$5.liked.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$5.reposted == null) {
            parcel$$118.writeInt(-1);
        } else {
            parcel$$118.writeInt(1);
            parcel$$118.writeInt(postOrRepostDetails$$5.reposted.booleanValue() ? 1 : 0);
        }
    }

    private void writeco_vine_android_scribe_model_Byline(Byline byline$$5, Parcel parcel$$119, int flags$$49) {
        parcel$$119.writeString(byline$$5.detailedDescription);
        parcel$$119.writeString(byline$$5.actionTitle);
        if (byline$$5.postIds == null) {
            parcel$$119.writeInt(-1);
        } else {
            parcel$$119.writeInt(byline$$5.postIds.size());
            Iterator<Long> it = byline$$5.postIds.iterator();
            while (it.hasNext()) {
                Long long$$36 = it.next();
                if (long$$36 == null) {
                    parcel$$119.writeInt(-1);
                } else {
                    parcel$$119.writeInt(1);
                    parcel$$119.writeLong(long$$36.longValue());
                }
            }
        }
        if (byline$$5.userIds == null) {
            parcel$$119.writeInt(-1);
        } else {
            parcel$$119.writeInt(byline$$5.userIds.size());
            Iterator<Long> it2 = byline$$5.userIds.iterator();
            while (it2.hasNext()) {
                Long long$$37 = it2.next();
                if (long$$37 == null) {
                    parcel$$119.writeInt(-1);
                } else {
                    parcel$$119.writeInt(1);
                    parcel$$119.writeLong(long$$37.longValue());
                }
            }
        }
        parcel$$119.writeString(byline$$5.description);
        parcel$$119.writeString(byline$$5.iconUrl);
        parcel$$119.writeString(byline$$5.body);
        parcel$$119.writeString(byline$$5.actionIconUrl);
    }

    private void writeco_vine_android_scribe_model_ActivityDetails(ActivityDetails activityDetails$$5, Parcel parcel$$120, int flags$$50) {
        if (activityDetails$$5.activityId == null) {
            parcel$$120.writeInt(-1);
        } else {
            parcel$$120.writeInt(1);
            parcel$$120.writeLong(activityDetails$$5.activityId.longValue());
        }
        if (activityDetails$$5.nMore == null) {
            parcel$$120.writeInt(-1);
        } else {
            parcel$$120.writeInt(1);
            parcel$$120.writeInt(activityDetails$$5.nMore.intValue());
        }
        parcel$$120.writeString(activityDetails$$5.activityType);
    }

    private void writeco_vine_android_scribe_model_MosaicDetails(MosaicDetails mosaicDetails$$7, Parcel parcel$$121, int flags$$51) {
        parcel$$121.writeString(mosaicDetails$$7.mosaicType);
        parcel$$121.writeString(mosaicDetails$$7.link);
    }

    private void writeco_vine_android_scribe_model_SuggestionDetails(SuggestionDetails suggestionDetails$$5, Parcel parcel$$122, int flags$$52) {
        parcel$$122.writeString(suggestionDetails$$5.suggestedQuery);
    }

    private void writeco_vine_android_scribe_model_CommentDetails(CommentDetails commentDetails$$5, Parcel parcel$$123, int flags$$53) {
        if (commentDetails$$5.commentId == null) {
            parcel$$123.writeInt(-1);
        } else {
            parcel$$123.writeInt(1);
            parcel$$123.writeLong(commentDetails$$5.commentId.longValue());
        }
        if (commentDetails$$5.authorId == null) {
            parcel$$123.writeInt(-1);
        } else {
            parcel$$123.writeInt(1);
            parcel$$123.writeLong(commentDetails$$5.authorId.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_ItemPosition(ItemPosition itemPosition$$5, Parcel parcel$$124, int flags$$54) {
        if (itemPosition$$5.offset == null) {
            parcel$$124.writeInt(-1);
        } else {
            parcel$$124.writeInt(1);
            parcel$$124.writeInt(itemPosition$$5.offset.intValue());
        }
    }

    private void writeco_vine_android_scribe_model_TagDetails(TagDetails tagDetails$$5, Parcel parcel$$125, int flags$$55) {
        parcel$$125.writeString(tagDetails$$5.tagId);
    }

    private void writeco_vine_android_scribe_model_DeviceData(DeviceData deviceData$$5, Parcel parcel$$126, int flags$$56) {
        if (deviceData$$5.gpsData == null) {
            parcel$$126.writeInt(-1);
        } else {
            parcel$$126.writeInt(1);
            writeco_vine_android_scribe_model_GPSData(deviceData$$5.gpsData, parcel$$126, flags$$56);
        }
        parcel$$126.writeString(deviceData$$5.orientation);
        parcel$$126.writeString(deviceData$$5.os);
        parcel$$126.writeString(deviceData$$5.timezone);
        parcel$$126.writeString(deviceData$$5.deviceName);
        if (deviceData$$5.otherAudioIsPlaying == null) {
            parcel$$126.writeInt(-1);
        } else {
            parcel$$126.writeInt(1);
            parcel$$126.writeInt(deviceData$$5.otherAudioIsPlaying.booleanValue() ? 1 : 0);
        }
        parcel$$126.writeString(deviceData$$5.manufacturer);
        if (deviceData$$5.languageCodes == null) {
            parcel$$126.writeInt(-1);
        } else {
            parcel$$126.writeInt(deviceData$$5.languageCodes.size());
            for (String string$$3 : deviceData$$5.languageCodes) {
                parcel$$126.writeString(string$$3);
            }
        }
        if (deviceData$$5.brightness == null) {
            parcel$$126.writeInt(-1);
        } else {
            parcel$$126.writeInt(1);
            parcel$$126.writeDouble(deviceData$$5.brightness.doubleValue());
        }
        parcel$$126.writeString(deviceData$$5.osVersion);
        if (deviceData$$5.bytesFree == null) {
            parcel$$126.writeInt(-1);
        } else {
            parcel$$126.writeInt(1);
            parcel$$126.writeLong(deviceData$$5.bytesFree.longValue());
        }
        parcel$$126.writeString(deviceData$$5.browser);
        parcel$$126.writeString(deviceData$$5.browserVersion);
        parcel$$126.writeString(deviceData$$5.deviceModel);
        parcel$$126.writeString(deviceData$$5.internetAccessType);
        if (deviceData$$5.radioDetails == null) {
            parcel$$126.writeInt(-1);
        } else {
            parcel$$126.writeInt(1);
            writeco_vine_android_scribe_model_MobileRadioDetails(deviceData$$5.radioDetails, parcel$$126, flags$$56);
        }
        if (deviceData$$5.bytesAvailable == null) {
            parcel$$126.writeInt(-1);
        } else {
            parcel$$126.writeInt(1);
            parcel$$126.writeLong(deviceData$$5.bytesAvailable.longValue());
        }
        if (deviceData$$5.batteryLevel == null) {
            parcel$$126.writeInt(-1);
        } else {
            parcel$$126.writeInt(1);
            parcel$$126.writeDouble(deviceData$$5.batteryLevel.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_GPSData(GPSData gPSData$$5, Parcel parcel$$127, int flags$$57) {
        if (gPSData$$5.altitude == null) {
            parcel$$127.writeInt(-1);
        } else {
            parcel$$127.writeInt(1);
            parcel$$127.writeDouble(gPSData$$5.altitude.doubleValue());
        }
        if (gPSData$$5.verticalAccuracy == null) {
            parcel$$127.writeInt(-1);
        } else {
            parcel$$127.writeInt(1);
            parcel$$127.writeDouble(gPSData$$5.verticalAccuracy.doubleValue());
        }
        if (gPSData$$5.latitude == null) {
            parcel$$127.writeInt(-1);
        } else {
            parcel$$127.writeInt(1);
            parcel$$127.writeDouble(gPSData$$5.latitude.doubleValue());
        }
        if (gPSData$$5.horizontalAccuracy == null) {
            parcel$$127.writeInt(-1);
        } else {
            parcel$$127.writeInt(1);
            parcel$$127.writeDouble(gPSData$$5.horizontalAccuracy.doubleValue());
        }
        if (gPSData$$5.longitude == null) {
            parcel$$127.writeInt(-1);
        } else {
            parcel$$127.writeInt(1);
            parcel$$127.writeDouble(gPSData$$5.longitude.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_MobileRadioDetails(MobileRadioDetails mobileRadioDetails$$5, Parcel parcel$$128, int flags$$58) {
        parcel$$128.writeString(mobileRadioDetails$$5.mobileNetworkOperatorName);
        parcel$$128.writeString(mobileRadioDetails$$5.mobileNetworkOperatorCountryCode);
        parcel$$128.writeString(mobileRadioDetails$$5.mobileSimProviderName);
        parcel$$128.writeString(mobileRadioDetails$$5.radioStatus);
        if (mobileRadioDetails$$5.signalStrength == null) {
            parcel$$128.writeInt(-1);
        } else {
            parcel$$128.writeInt(1);
            parcel$$128.writeInt(mobileRadioDetails$$5.signalStrength.intValue());
        }
        parcel$$128.writeString(mobileRadioDetails$$5.mobileNetworkOperatorCode);
        parcel$$128.writeString(mobileRadioDetails$$5.mobileSimProviderIsoCountryCode);
        parcel$$128.writeString(mobileRadioDetails$$5.mobileSimProviderCode);
        parcel$$128.writeString(mobileRadioDetails$$5.mobileNetworkOperatorIsoCountryCode);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public ClientEvents getParcel() {
        return this.clientEvents$$0;
    }
}
