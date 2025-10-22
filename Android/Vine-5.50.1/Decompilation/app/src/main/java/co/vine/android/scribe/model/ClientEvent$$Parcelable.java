package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class ClientEvent$$Parcelable implements Parcelable, ParcelWrapper<ClientEvent> {
    public static final ClientEvent$$Parcelable$Creator$$1 CREATOR = new ClientEvent$$Parcelable$Creator$$1();
    private ClientEvent clientEvent$$0;

    public ClientEvent$$Parcelable(Parcel parcel$$5) {
        ClientEvent clientEvent$$2;
        if (parcel$$5.readInt() == -1) {
            clientEvent$$2 = null;
        } else {
            clientEvent$$2 = readco_vine_android_scribe_model_ClientEvent(parcel$$5);
        }
        this.clientEvent$$0 = clientEvent$$2;
    }

    public ClientEvent$$Parcelable(ClientEvent clientEvent$$4) {
        this.clientEvent$$0 = clientEvent$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$6, int flags) {
        if (this.clientEvent$$0 == null) {
            parcel$$6.writeInt(-1);
        } else {
            parcel$$6.writeInt(1);
            writeco_vine_android_scribe_model_ClientEvent(this.clientEvent$$0, parcel$$6, flags);
        }
    }

    private ClientEvent readco_vine_android_scribe_model_ClientEvent(Parcel parcel$$7) {
        ApplicationState applicationState$$1;
        AppNavigation appNavigation$$1;
        EventDetails eventDetails$$1;
        DeviceData deviceData$$1;
        ClientEvent clientEvent$$1 = new ClientEvent();
        if (parcel$$7.readInt() == -1) {
            applicationState$$1 = null;
        } else {
            applicationState$$1 = readco_vine_android_scribe_model_ApplicationState(parcel$$7);
        }
        clientEvent$$1.appState = applicationState$$1;
        if (parcel$$7.readInt() == -1) {
            appNavigation$$1 = null;
        } else {
            appNavigation$$1 = readco_vine_android_scribe_model_AppNavigation(parcel$$7);
        }
        clientEvent$$1.navigation = appNavigation$$1;
        clientEvent$$1.clientId = parcel$$7.readString();
        if (parcel$$7.readInt() == -1) {
            eventDetails$$1 = null;
        } else {
            eventDetails$$1 = readco_vine_android_scribe_model_EventDetails(parcel$$7);
        }
        clientEvent$$1.eventDetails = eventDetails$$1;
        if (parcel$$7.readInt() == -1) {
            deviceData$$1 = null;
        } else {
            deviceData$$1 = readco_vine_android_scribe_model_DeviceData(parcel$$7);
        }
        clientEvent$$1.deviceData = deviceData$$1;
        clientEvent$$1.eventType = parcel$$7.readString();
        return clientEvent$$1;
    }

    private ApplicationState readco_vine_android_scribe_model_ApplicationState(Parcel parcel$$8) {
        ExperimentData experimentData$$1;
        Boolean boolean$$0;
        Boolean boolean$$1;
        Double double$$0;
        Long long$$0;
        Long long$$1;
        Long long$$2;
        ApplicationState applicationState$$0 = new ApplicationState();
        if (parcel$$8.readInt() == -1) {
            experimentData$$1 = null;
        } else {
            experimentData$$1 = readco_vine_android_scribe_model_ExperimentData(parcel$$8);
        }
        applicationState$$0.activeExperiments = experimentData$$1;
        int int$$2 = parcel$$8.readInt();
        if (int$$2 < 0) {
            boolean$$0 = null;
        } else {
            boolean$$0 = Boolean.valueOf(parcel$$8.readInt() == 1);
        }
        applicationState$$0.twitterConnected = boolean$$0;
        applicationState$$0.applicationStatus = parcel$$8.readString();
        int int$$3 = parcel$$8.readInt();
        if (int$$3 < 0) {
            boolean$$1 = null;
        } else {
            boolean$$1 = Boolean.valueOf(parcel$$8.readInt() == 1);
        }
        applicationState$$0.facebookConnected = boolean$$1;
        int int$$4 = parcel$$8.readInt();
        if (int$$4 < 0) {
            double$$0 = null;
        } else {
            double$$0 = Double.valueOf(parcel$$8.readDouble());
        }
        applicationState$$0.lastLaunchTimestamp = double$$0;
        applicationState$$0.edition = parcel$$8.readString();
        applicationState$$0.abConnected = parcel$$8.readInt() == 1;
        int int$$5 = parcel$$8.readInt();
        if (int$$5 < 0) {
            long$$0 = null;
        } else {
            long$$0 = Long.valueOf(parcel$$8.readLong());
        }
        applicationState$$0.videoCacheSize = long$$0;
        int int$$6 = parcel$$8.readInt();
        if (int$$6 < 0) {
            long$$1 = null;
        } else {
            long$$1 = Long.valueOf(parcel$$8.readLong());
        }
        applicationState$$0.loggedInUserId = long$$1;
        int int$$7 = parcel$$8.readInt();
        if (int$$7 < 0) {
            long$$2 = null;
        } else {
            long$$2 = Long.valueOf(parcel$$8.readLong());
        }
        applicationState$$0.numDrafts = long$$2;
        return applicationState$$0;
    }

    private ExperimentData readco_vine_android_scribe_model_ExperimentData(Parcel parcel$$9) {
        ArrayList<ExperimentValue> list$$0;
        ExperimentValue experimentValue$$1;
        ExperimentData experimentData$$0 = new ExperimentData();
        int int$$0 = parcel$$9.readInt();
        if (int$$0 < 0) {
            list$$0 = null;
        } else {
            list$$0 = new ArrayList<>();
            for (int int$$1 = 0; int$$1 < int$$0; int$$1++) {
                if (parcel$$9.readInt() == -1) {
                    experimentValue$$1 = null;
                } else {
                    experimentValue$$1 = readco_vine_android_scribe_model_ExperimentValue(parcel$$9);
                }
                list$$0.add(experimentValue$$1);
            }
        }
        experimentData$$0.experimentValues = list$$0;
        return experimentData$$0;
    }

    private ExperimentValue readco_vine_android_scribe_model_ExperimentValue(Parcel parcel$$10) {
        ExperimentValue experimentValue$$0 = new ExperimentValue();
        experimentValue$$0.value = parcel$$10.readString();
        experimentValue$$0.key = parcel$$10.readString();
        return experimentValue$$0;
    }

    private AppNavigation readco_vine_android_scribe_model_AppNavigation(Parcel parcel$$11) {
        AppNavigation appNavigation$$0 = new AppNavigation();
        appNavigation$$0.view = parcel$$11.readString();
        appNavigation$$0.timelineApiUrl = parcel$$11.readString();
        appNavigation$$0.ui_element = parcel$$11.readString();
        appNavigation$$0.captureSourceSection = parcel$$11.readString();
        appNavigation$$0.searchQuery = parcel$$11.readString();
        appNavigation$$0.isNewSearchView = parcel$$11.readInt() == 1;
        appNavigation$$0.section = parcel$$11.readString();
        appNavigation$$0.subview = parcel$$11.readString();
        appNavigation$$0.filtering = parcel$$11.readString();
        return appNavigation$$0;
    }

    private EventDetails readco_vine_android_scribe_model_EventDetails(Parcel parcel$$12) {
        AlertDetails alertDetails$$1;
        TimingDetails timingDetails$$1;
        VideoImportDetails videoImportDetails$$1;
        ShareDetails shareDetails$$1;
        LaunchDetails launchDetails$$1;
        HTTPRequestDetails hTTPRequestDetails$$1;
        PlaybackSummaryDetails playbackSummaryDetails$$1;
        HTTPPerformanceData hTTPPerformanceData$$1;
        ArrayList<Item> list$$3;
        Item item$$1;
        Double double$$7;
        EventDetails eventDetails$$0 = new EventDetails();
        if (parcel$$12.readInt() == -1) {
            alertDetails$$1 = null;
        } else {
            alertDetails$$1 = readco_vine_android_scribe_model_AlertDetails(parcel$$12);
        }
        eventDetails$$0.alert = alertDetails$$1;
        if (parcel$$12.readInt() == -1) {
            timingDetails$$1 = null;
        } else {
            timingDetails$$1 = readco_vine_android_scribe_model_TimingDetails(parcel$$12);
        }
        eventDetails$$0.timing = timingDetails$$1;
        if (parcel$$12.readInt() == -1) {
            videoImportDetails$$1 = null;
        } else {
            videoImportDetails$$1 = readco_vine_android_scribe_model_VideoImportDetails(parcel$$12);
        }
        eventDetails$$0.videoImportDetails = videoImportDetails$$1;
        if (parcel$$12.readInt() == -1) {
            shareDetails$$1 = null;
        } else {
            shareDetails$$1 = readco_vine_android_scribe_model_ShareDetails(parcel$$12);
        }
        eventDetails$$0.share = shareDetails$$1;
        if (parcel$$12.readInt() == -1) {
            launchDetails$$1 = null;
        } else {
            launchDetails$$1 = readco_vine_android_scribe_model_LaunchDetails(parcel$$12);
        }
        eventDetails$$0.launch = launchDetails$$1;
        if (parcel$$12.readInt() == -1) {
            hTTPRequestDetails$$1 = null;
        } else {
            hTTPRequestDetails$$1 = readco_vine_android_scribe_model_HTTPRequestDetails(parcel$$12);
        }
        eventDetails$$0.httpRequestDetails = hTTPRequestDetails$$1;
        if (parcel$$12.readInt() == -1) {
            playbackSummaryDetails$$1 = null;
        } else {
            playbackSummaryDetails$$1 = readco_vine_android_scribe_model_PlaybackSummaryDetails(parcel$$12);
        }
        eventDetails$$0.playbackSummary = playbackSummaryDetails$$1;
        if (parcel$$12.readInt() == -1) {
            hTTPPerformanceData$$1 = null;
        } else {
            hTTPPerformanceData$$1 = readco_vine_android_scribe_model_HTTPPerformanceData(parcel$$12);
        }
        eventDetails$$0.httpPerformanceData = hTTPPerformanceData$$1;
        int int$$33 = parcel$$12.readInt();
        if (int$$33 < 0) {
            list$$3 = null;
        } else {
            list$$3 = new ArrayList<>();
            for (int int$$34 = 0; int$$34 < int$$33; int$$34++) {
                if (parcel$$12.readInt() == -1) {
                    item$$1 = null;
                } else {
                    item$$1 = readco_vine_android_scribe_model_Item(parcel$$12);
                }
                list$$3.add(item$$1);
            }
        }
        eventDetails$$0.items = list$$3;
        int int$$53 = parcel$$12.readInt();
        if (int$$53 < 0) {
            double$$7 = null;
        } else {
            double$$7 = Double.valueOf(parcel$$12.readDouble());
        }
        eventDetails$$0.timestamp = double$$7;
        return eventDetails$$0;
    }

    private AlertDetails readco_vine_android_scribe_model_AlertDetails(Parcel parcel$$13) {
        AlertDetails alertDetails$$0 = new AlertDetails();
        alertDetails$$0.name = parcel$$13.readString();
        alertDetails$$0.action = parcel$$13.readString();
        return alertDetails$$0;
    }

    private TimingDetails readco_vine_android_scribe_model_TimingDetails(Parcel parcel$$14) {
        Double double$$1;
        Double double$$2;
        TimingDetails timingDetails$$0 = new TimingDetails();
        int int$$8 = parcel$$14.readInt();
        if (int$$8 < 0) {
            double$$1 = null;
        } else {
            double$$1 = Double.valueOf(parcel$$14.readDouble());
        }
        timingDetails$$0.duration = double$$1;
        int int$$9 = parcel$$14.readInt();
        if (int$$9 < 0) {
            double$$2 = null;
        } else {
            double$$2 = Double.valueOf(parcel$$14.readDouble());
        }
        timingDetails$$0.startTimestamp = double$$2;
        return timingDetails$$0;
    }

    private VideoImportDetails readco_vine_android_scribe_model_VideoImportDetails(Parcel parcel$$15) {
        VideoImportDetails videoImportDetails$$0 = new VideoImportDetails();
        videoImportDetails$$0.result = parcel$$15.readString();
        return videoImportDetails$$0;
    }

    private ShareDetails readco_vine_android_scribe_model_ShareDetails(Parcel parcel$$16) {
        ArrayList<String> list$$1;
        ArrayList<VMRecipient> list$$2;
        VMRecipient vMRecipient$$1;
        Boolean boolean$$5;
        ShareDetails shareDetails$$0 = new ShareDetails();
        int int$$10 = parcel$$16.readInt();
        if (int$$10 < 0) {
            list$$1 = null;
        } else {
            list$$1 = new ArrayList<>();
            for (int int$$11 = 0; int$$11 < int$$10; int$$11++) {
                list$$1.add(parcel$$16.readString());
            }
        }
        shareDetails$$0.shareTargets = list$$1;
        shareDetails$$0.postId = parcel$$16.readString();
        int int$$12 = parcel$$16.readInt();
        if (int$$12 < 0) {
            list$$2 = null;
        } else {
            list$$2 = new ArrayList<>();
            for (int int$$13 = 0; int$$13 < int$$12; int$$13++) {
                if (parcel$$16.readInt() == -1) {
                    vMRecipient$$1 = null;
                } else {
                    vMRecipient$$1 = readco_vine_android_scribe_model_VMRecipient(parcel$$16);
                }
                list$$2.add(vMRecipient$$1);
            }
        }
        shareDetails$$0.messageRecipients = list$$2;
        int int$$18 = parcel$$16.readInt();
        if (int$$18 < 0) {
            boolean$$5 = null;
        } else {
            boolean$$5 = Boolean.valueOf(parcel$$16.readInt() == 1);
        }
        shareDetails$$0.hasComment = boolean$$5;
        return shareDetails$$0;
    }

    private VMRecipient readco_vine_android_scribe_model_VMRecipient(Parcel parcel$$17) {
        Boolean boolean$$2;
        Boolean boolean$$3;
        UserDetails userDetails$$1;
        VMRecipient vMRecipient$$0 = new VMRecipient();
        int int$$14 = parcel$$17.readInt();
        if (int$$14 < 0) {
            boolean$$2 = null;
        } else {
            boolean$$2 = Boolean.valueOf(parcel$$17.readInt() == 1);
        }
        vMRecipient$$0.isPhone = boolean$$2;
        int int$$15 = parcel$$17.readInt();
        if (int$$15 < 0) {
            boolean$$3 = null;
        } else {
            boolean$$3 = Boolean.valueOf(parcel$$17.readInt() == 1);
        }
        vMRecipient$$0.isEmail = boolean$$3;
        if (parcel$$17.readInt() == -1) {
            userDetails$$1 = null;
        } else {
            userDetails$$1 = readco_vine_android_scribe_model_UserDetails(parcel$$17);
        }
        vMRecipient$$0.user = userDetails$$1;
        return vMRecipient$$0;
    }

    private UserDetails readco_vine_android_scribe_model_UserDetails(Parcel parcel$$18) {
        Boolean boolean$$4;
        Long long$$3;
        UserDetails userDetails$$0 = new UserDetails();
        int int$$16 = parcel$$18.readInt();
        if (int$$16 < 0) {
            boolean$$4 = null;
        } else {
            boolean$$4 = Boolean.valueOf(parcel$$18.readInt() == 1);
        }
        userDetails$$0.following = boolean$$4;
        int int$$17 = parcel$$18.readInt();
        if (int$$17 < 0) {
            long$$3 = null;
        } else {
            long$$3 = Long.valueOf(parcel$$18.readLong());
        }
        userDetails$$0.userId = long$$3;
        return userDetails$$0;
    }

    private LaunchDetails readco_vine_android_scribe_model_LaunchDetails(Parcel parcel$$19) {
        LaunchDetails launchDetails$$0 = new LaunchDetails();
        launchDetails$$0.webSrc = parcel$$19.readString();
        return launchDetails$$0;
    }

    private HTTPRequestDetails readco_vine_android_scribe_model_HTTPRequestDetails(Parcel parcel$$20) {
        Integer integer$$0;
        Integer integer$$1;
        HTTPRequestDetails hTTPRequestDetails$$0 = new HTTPRequestDetails();
        hTTPRequestDetails$$0.method = parcel$$20.readString();
        int int$$19 = parcel$$20.readInt();
        if (int$$19 < 0) {
            integer$$0 = null;
        } else {
            integer$$0 = Integer.valueOf(parcel$$20.readInt());
        }
        hTTPRequestDetails$$0.apiError = integer$$0;
        int int$$20 = parcel$$20.readInt();
        if (int$$20 < 0) {
            integer$$1 = null;
        } else {
            integer$$1 = Integer.valueOf(parcel$$20.readInt());
        }
        hTTPRequestDetails$$0.httpStatus = integer$$1;
        hTTPRequestDetails$$0.osErrorDetails = parcel$$20.readString();
        hTTPRequestDetails$$0.networkError = parcel$$20.readString();
        hTTPRequestDetails$$0.url = parcel$$20.readString();
        return hTTPRequestDetails$$0;
    }

    private PlaybackSummaryDetails readco_vine_android_scribe_model_PlaybackSummaryDetails(Parcel parcel$$21) {
        Float float$$0;
        Float float$$1;
        Integer integer$$2;
        Float float$$2;
        Float float$$3;
        Float float$$4;
        PlaybackSummaryDetails playbackSummaryDetails$$0 = new PlaybackSummaryDetails();
        int int$$21 = parcel$$21.readInt();
        if (int$$21 < 0) {
            float$$0 = null;
        } else {
            float$$0 = Float.valueOf(parcel$$21.readFloat());
        }
        playbackSummaryDetails$$0.videoEndTime = float$$0;
        int int$$22 = parcel$$21.readInt();
        if (int$$22 < 0) {
            float$$1 = null;
        } else {
            float$$1 = Float.valueOf(parcel$$21.readFloat());
        }
        playbackSummaryDetails$$0.videoStarttime = float$$1;
        int int$$23 = parcel$$21.readInt();
        if (int$$23 < 0) {
            integer$$2 = null;
        } else {
            integer$$2 = Integer.valueOf(parcel$$21.readInt());
        }
        playbackSummaryDetails$$0.playbackInterruptions = integer$$2;
        int int$$24 = parcel$$21.readInt();
        if (int$$24 < 0) {
            float$$2 = null;
        } else {
            float$$2 = Float.valueOf(parcel$$21.readFloat());
        }
        playbackSummaryDetails$$0.timeSpentPlaying = float$$2;
        int int$$25 = parcel$$21.readInt();
        if (int$$25 < 0) {
            float$$3 = null;
        } else {
            float$$3 = Float.valueOf(parcel$$21.readFloat());
        }
        playbackSummaryDetails$$0.timeSpentBuffering = float$$3;
        int int$$26 = parcel$$21.readInt();
        if (int$$26 < 0) {
            float$$4 = null;
        } else {
            float$$4 = Float.valueOf(parcel$$21.readFloat());
        }
        playbackSummaryDetails$$0.timeSpentPaused = float$$4;
        return playbackSummaryDetails$$0;
    }

    private HTTPPerformanceData readco_vine_android_scribe_model_HTTPPerformanceData(Parcel parcel$$22) {
        Double double$$3;
        Long long$$4;
        Long long$$5;
        Double double$$4;
        Double double$$5;
        Double double$$6;
        HTTPPerformanceData hTTPPerformanceData$$0 = new HTTPPerformanceData();
        int int$$27 = parcel$$22.readInt();
        if (int$$27 < 0) {
            double$$3 = null;
        } else {
            double$$3 = Double.valueOf(parcel$$22.readDouble());
        }
        hTTPPerformanceData$$0.duration = double$$3;
        int int$$28 = parcel$$22.readInt();
        if (int$$28 < 0) {
            long$$4 = null;
        } else {
            long$$4 = Long.valueOf(parcel$$22.readLong());
        }
        hTTPPerformanceData$$0.bytesReceived = long$$4;
        int int$$29 = parcel$$22.readInt();
        if (int$$29 < 0) {
            long$$5 = null;
        } else {
            long$$5 = Long.valueOf(parcel$$22.readLong());
        }
        hTTPPerformanceData$$0.bytesSent = long$$5;
        int int$$30 = parcel$$22.readInt();
        if (int$$30 < 0) {
            double$$4 = null;
        } else {
            double$$4 = Double.valueOf(parcel$$22.readDouble());
        }
        hTTPPerformanceData$$0.durationToRequestSent = double$$4;
        int int$$31 = parcel$$22.readInt();
        if (int$$31 < 0) {
            double$$5 = null;
        } else {
            double$$5 = Double.valueOf(parcel$$22.readDouble());
        }
        hTTPPerformanceData$$0.startTimestamp = double$$5;
        int int$$32 = parcel$$22.readInt();
        if (int$$32 < 0) {
            double$$6 = null;
        } else {
            double$$6 = Double.valueOf(parcel$$22.readDouble());
        }
        hTTPPerformanceData$$0.durationToFirstByte = double$$6;
        return hTTPPerformanceData$$0;
    }

    private Item readco_vine_android_scribe_model_Item(Parcel parcel$$23) {
        PostOrRepostDetails postOrRepostDetails$$1;
        ActivityDetails activityDetails$$1;
        MosaicDetails mosaicDetails$$1;
        SuggestionDetails suggestionDetails$$1;
        MosaicDetails mosaicDetails$$2;
        CommentDetails commentDetails$$1;
        ItemPosition itemPosition$$1;
        TagDetails tagDetails$$1;
        UserDetails userDetails$$2;
        Item item$$0 = new Item();
        if (parcel$$23.readInt() == -1) {
            postOrRepostDetails$$1 = null;
        } else {
            postOrRepostDetails$$1 = readco_vine_android_scribe_model_PostOrRepostDetails(parcel$$23);
        }
        item$$0.postOrRepost = postOrRepostDetails$$1;
        item$$0.reference = parcel$$23.readString();
        item$$0.itemType = parcel$$23.readString();
        if (parcel$$23.readInt() == -1) {
            activityDetails$$1 = null;
        } else {
            activityDetails$$1 = readco_vine_android_scribe_model_ActivityDetails(parcel$$23);
        }
        item$$0.activity = activityDetails$$1;
        if (parcel$$23.readInt() == -1) {
            mosaicDetails$$1 = null;
        } else {
            mosaicDetails$$1 = readco_vine_android_scribe_model_MosaicDetails(parcel$$23);
        }
        item$$0.postMosaic = mosaicDetails$$1;
        if (parcel$$23.readInt() == -1) {
            suggestionDetails$$1 = null;
        } else {
            suggestionDetails$$1 = readco_vine_android_scribe_model_SuggestionDetails(parcel$$23);
        }
        item$$0.suggestion = suggestionDetails$$1;
        if (parcel$$23.readInt() == -1) {
            mosaicDetails$$2 = null;
        } else {
            mosaicDetails$$2 = readco_vine_android_scribe_model_MosaicDetails(parcel$$23);
        }
        item$$0.userMosaic = mosaicDetails$$2;
        if (parcel$$23.readInt() == -1) {
            commentDetails$$1 = null;
        } else {
            commentDetails$$1 = readco_vine_android_scribe_model_CommentDetails(parcel$$23);
        }
        item$$0.comment = commentDetails$$1;
        if (parcel$$23.readInt() == -1) {
            itemPosition$$1 = null;
        } else {
            itemPosition$$1 = readco_vine_android_scribe_model_ItemPosition(parcel$$23);
        }
        item$$0.position = itemPosition$$1;
        if (parcel$$23.readInt() == -1) {
            tagDetails$$1 = null;
        } else {
            tagDetails$$1 = readco_vine_android_scribe_model_TagDetails(parcel$$23);
        }
        item$$0.tag = tagDetails$$1;
        if (parcel$$23.readInt() == -1) {
            userDetails$$2 = null;
        } else {
            userDetails$$2 = readco_vine_android_scribe_model_UserDetails(parcel$$23);
        }
        item$$0.user = userDetails$$2;
        return item$$0;
    }

    private PostOrRepostDetails readco_vine_android_scribe_model_PostOrRepostDetails(Parcel parcel$$24) {
        Long long$$6;
        Long long$$7;
        Long long$$8;
        Boolean boolean$$6;
        Long long$$9;
        Byline byline$$1;
        Boolean boolean$$7;
        Boolean boolean$$8;
        PostOrRepostDetails postOrRepostDetails$$0 = new PostOrRepostDetails();
        int int$$35 = parcel$$24.readInt();
        if (int$$35 < 0) {
            long$$6 = null;
        } else {
            long$$6 = Long.valueOf(parcel$$24.readLong());
        }
        postOrRepostDetails$$0.postAuthorId = long$$6;
        postOrRepostDetails$$0.longformId = parcel$$24.readString();
        int int$$36 = parcel$$24.readInt();
        if (int$$36 < 0) {
            long$$7 = null;
        } else {
            long$$7 = Long.valueOf(parcel$$24.readLong());
        }
        postOrRepostDetails$$0.repostId = long$$7;
        int int$$37 = parcel$$24.readInt();
        if (int$$37 < 0) {
            long$$8 = null;
        } else {
            long$$8 = Long.valueOf(parcel$$24.readLong());
        }
        postOrRepostDetails$$0.repostAuthorId = long$$8;
        int int$$38 = parcel$$24.readInt();
        if (int$$38 < 0) {
            boolean$$6 = null;
        } else {
            boolean$$6 = Boolean.valueOf(parcel$$24.readInt() == 1);
        }
        postOrRepostDetails$$0.hasSimilarPosts = boolean$$6;
        int int$$39 = parcel$$24.readInt();
        if (int$$39 < 0) {
            long$$9 = null;
        } else {
            long$$9 = Long.valueOf(parcel$$24.readLong());
        }
        postOrRepostDetails$$0.postId = long$$9;
        if (parcel$$24.readInt() == -1) {
            byline$$1 = null;
        } else {
            byline$$1 = readco_vine_android_scribe_model_Byline(parcel$$24);
        }
        postOrRepostDetails$$0.byline = byline$$1;
        int int$$46 = parcel$$24.readInt();
        if (int$$46 < 0) {
            boolean$$7 = null;
        } else {
            boolean$$7 = Boolean.valueOf(parcel$$24.readInt() == 1);
        }
        postOrRepostDetails$$0.liked = boolean$$7;
        int int$$47 = parcel$$24.readInt();
        if (int$$47 < 0) {
            boolean$$8 = null;
        } else {
            boolean$$8 = Boolean.valueOf(parcel$$24.readInt() == 1);
        }
        postOrRepostDetails$$0.reposted = boolean$$8;
        return postOrRepostDetails$$0;
    }

    private Byline readco_vine_android_scribe_model_Byline(Parcel parcel$$25) {
        ArrayList<Long> list$$4;
        Long long$$10;
        ArrayList<Long> list$$5;
        Long long$$11;
        Byline byline$$0 = new Byline();
        byline$$0.detailedDescription = parcel$$25.readString();
        byline$$0.actionTitle = parcel$$25.readString();
        int int$$40 = parcel$$25.readInt();
        if (int$$40 < 0) {
            list$$4 = null;
        } else {
            list$$4 = new ArrayList<>();
            for (int int$$41 = 0; int$$41 < int$$40; int$$41++) {
                int int$$42 = parcel$$25.readInt();
                if (int$$42 < 0) {
                    long$$10 = null;
                } else {
                    long$$10 = Long.valueOf(parcel$$25.readLong());
                }
                list$$4.add(long$$10);
            }
        }
        byline$$0.postIds = list$$4;
        int int$$43 = parcel$$25.readInt();
        if (int$$43 < 0) {
            list$$5 = null;
        } else {
            list$$5 = new ArrayList<>();
            for (int int$$44 = 0; int$$44 < int$$43; int$$44++) {
                int int$$45 = parcel$$25.readInt();
                if (int$$45 < 0) {
                    long$$11 = null;
                } else {
                    long$$11 = Long.valueOf(parcel$$25.readLong());
                }
                list$$5.add(long$$11);
            }
        }
        byline$$0.userIds = list$$5;
        byline$$0.description = parcel$$25.readString();
        byline$$0.iconUrl = parcel$$25.readString();
        byline$$0.body = parcel$$25.readString();
        byline$$0.actionIconUrl = parcel$$25.readString();
        return byline$$0;
    }

    private ActivityDetails readco_vine_android_scribe_model_ActivityDetails(Parcel parcel$$26) {
        Long long$$12;
        Integer integer$$3;
        ActivityDetails activityDetails$$0 = new ActivityDetails();
        int int$$48 = parcel$$26.readInt();
        if (int$$48 < 0) {
            long$$12 = null;
        } else {
            long$$12 = Long.valueOf(parcel$$26.readLong());
        }
        activityDetails$$0.activityId = long$$12;
        int int$$49 = parcel$$26.readInt();
        if (int$$49 < 0) {
            integer$$3 = null;
        } else {
            integer$$3 = Integer.valueOf(parcel$$26.readInt());
        }
        activityDetails$$0.nMore = integer$$3;
        activityDetails$$0.activityType = parcel$$26.readString();
        return activityDetails$$0;
    }

    private MosaicDetails readco_vine_android_scribe_model_MosaicDetails(Parcel parcel$$27) {
        MosaicDetails mosaicDetails$$0 = new MosaicDetails();
        mosaicDetails$$0.mosaicType = parcel$$27.readString();
        mosaicDetails$$0.link = parcel$$27.readString();
        return mosaicDetails$$0;
    }

    private SuggestionDetails readco_vine_android_scribe_model_SuggestionDetails(Parcel parcel$$28) {
        SuggestionDetails suggestionDetails$$0 = new SuggestionDetails();
        suggestionDetails$$0.suggestedQuery = parcel$$28.readString();
        return suggestionDetails$$0;
    }

    private CommentDetails readco_vine_android_scribe_model_CommentDetails(Parcel parcel$$29) {
        Long long$$13;
        Long long$$14;
        CommentDetails commentDetails$$0 = new CommentDetails();
        int int$$50 = parcel$$29.readInt();
        if (int$$50 < 0) {
            long$$13 = null;
        } else {
            long$$13 = Long.valueOf(parcel$$29.readLong());
        }
        commentDetails$$0.commentId = long$$13;
        int int$$51 = parcel$$29.readInt();
        if (int$$51 < 0) {
            long$$14 = null;
        } else {
            long$$14 = Long.valueOf(parcel$$29.readLong());
        }
        commentDetails$$0.authorId = long$$14;
        return commentDetails$$0;
    }

    private ItemPosition readco_vine_android_scribe_model_ItemPosition(Parcel parcel$$30) {
        Integer integer$$4;
        ItemPosition itemPosition$$0 = new ItemPosition();
        int int$$52 = parcel$$30.readInt();
        if (int$$52 < 0) {
            integer$$4 = null;
        } else {
            integer$$4 = Integer.valueOf(parcel$$30.readInt());
        }
        itemPosition$$0.offset = integer$$4;
        return itemPosition$$0;
    }

    private TagDetails readco_vine_android_scribe_model_TagDetails(Parcel parcel$$31) {
        TagDetails tagDetails$$0 = new TagDetails();
        tagDetails$$0.tagId = parcel$$31.readString();
        return tagDetails$$0;
    }

    private DeviceData readco_vine_android_scribe_model_DeviceData(Parcel parcel$$32) {
        GPSData gPSData$$1;
        Boolean boolean$$9;
        ArrayList<String> list$$6;
        Double double$$13;
        Long long$$15;
        MobileRadioDetails mobileRadioDetails$$1;
        Long long$$16;
        Double double$$14;
        DeviceData deviceData$$0 = new DeviceData();
        if (parcel$$32.readInt() == -1) {
            gPSData$$1 = null;
        } else {
            gPSData$$1 = readco_vine_android_scribe_model_GPSData(parcel$$32);
        }
        deviceData$$0.gpsData = gPSData$$1;
        deviceData$$0.orientation = parcel$$32.readString();
        deviceData$$0.os = parcel$$32.readString();
        deviceData$$0.timezone = parcel$$32.readString();
        deviceData$$0.deviceName = parcel$$32.readString();
        int int$$59 = parcel$$32.readInt();
        if (int$$59 < 0) {
            boolean$$9 = null;
        } else {
            boolean$$9 = Boolean.valueOf(parcel$$32.readInt() == 1);
        }
        deviceData$$0.otherAudioIsPlaying = boolean$$9;
        deviceData$$0.manufacturer = parcel$$32.readString();
        int int$$60 = parcel$$32.readInt();
        if (int$$60 < 0) {
            list$$6 = null;
        } else {
            list$$6 = new ArrayList<>();
            for (int int$$61 = 0; int$$61 < int$$60; int$$61++) {
                list$$6.add(parcel$$32.readString());
            }
        }
        deviceData$$0.languageCodes = list$$6;
        int int$$62 = parcel$$32.readInt();
        if (int$$62 < 0) {
            double$$13 = null;
        } else {
            double$$13 = Double.valueOf(parcel$$32.readDouble());
        }
        deviceData$$0.brightness = double$$13;
        deviceData$$0.osVersion = parcel$$32.readString();
        int int$$63 = parcel$$32.readInt();
        if (int$$63 < 0) {
            long$$15 = null;
        } else {
            long$$15 = Long.valueOf(parcel$$32.readLong());
        }
        deviceData$$0.bytesFree = long$$15;
        deviceData$$0.browser = parcel$$32.readString();
        deviceData$$0.browserVersion = parcel$$32.readString();
        deviceData$$0.deviceModel = parcel$$32.readString();
        deviceData$$0.internetAccessType = parcel$$32.readString();
        if (parcel$$32.readInt() == -1) {
            mobileRadioDetails$$1 = null;
        } else {
            mobileRadioDetails$$1 = readco_vine_android_scribe_model_MobileRadioDetails(parcel$$32);
        }
        deviceData$$0.radioDetails = mobileRadioDetails$$1;
        int int$$65 = parcel$$32.readInt();
        if (int$$65 < 0) {
            long$$16 = null;
        } else {
            long$$16 = Long.valueOf(parcel$$32.readLong());
        }
        deviceData$$0.bytesAvailable = long$$16;
        int int$$66 = parcel$$32.readInt();
        if (int$$66 < 0) {
            double$$14 = null;
        } else {
            double$$14 = Double.valueOf(parcel$$32.readDouble());
        }
        deviceData$$0.batteryLevel = double$$14;
        return deviceData$$0;
    }

    private GPSData readco_vine_android_scribe_model_GPSData(Parcel parcel$$33) {
        Double double$$8;
        Double double$$9;
        Double double$$10;
        Double double$$11;
        Double double$$12;
        GPSData gPSData$$0 = new GPSData();
        int int$$54 = parcel$$33.readInt();
        if (int$$54 < 0) {
            double$$8 = null;
        } else {
            double$$8 = Double.valueOf(parcel$$33.readDouble());
        }
        gPSData$$0.altitude = double$$8;
        int int$$55 = parcel$$33.readInt();
        if (int$$55 < 0) {
            double$$9 = null;
        } else {
            double$$9 = Double.valueOf(parcel$$33.readDouble());
        }
        gPSData$$0.verticalAccuracy = double$$9;
        int int$$56 = parcel$$33.readInt();
        if (int$$56 < 0) {
            double$$10 = null;
        } else {
            double$$10 = Double.valueOf(parcel$$33.readDouble());
        }
        gPSData$$0.latitude = double$$10;
        int int$$57 = parcel$$33.readInt();
        if (int$$57 < 0) {
            double$$11 = null;
        } else {
            double$$11 = Double.valueOf(parcel$$33.readDouble());
        }
        gPSData$$0.horizontalAccuracy = double$$11;
        int int$$58 = parcel$$33.readInt();
        if (int$$58 < 0) {
            double$$12 = null;
        } else {
            double$$12 = Double.valueOf(parcel$$33.readDouble());
        }
        gPSData$$0.longitude = double$$12;
        return gPSData$$0;
    }

    private MobileRadioDetails readco_vine_android_scribe_model_MobileRadioDetails(Parcel parcel$$34) {
        Integer integer$$5;
        MobileRadioDetails mobileRadioDetails$$0 = new MobileRadioDetails();
        mobileRadioDetails$$0.mobileNetworkOperatorName = parcel$$34.readString();
        mobileRadioDetails$$0.mobileNetworkOperatorCountryCode = parcel$$34.readString();
        mobileRadioDetails$$0.mobileSimProviderName = parcel$$34.readString();
        mobileRadioDetails$$0.radioStatus = parcel$$34.readString();
        int int$$64 = parcel$$34.readInt();
        if (int$$64 < 0) {
            integer$$5 = null;
        } else {
            integer$$5 = Integer.valueOf(parcel$$34.readInt());
        }
        mobileRadioDetails$$0.signalStrength = integer$$5;
        mobileRadioDetails$$0.mobileNetworkOperatorCode = parcel$$34.readString();
        mobileRadioDetails$$0.mobileSimProviderIsoCountryCode = parcel$$34.readString();
        mobileRadioDetails$$0.mobileSimProviderCode = parcel$$34.readString();
        mobileRadioDetails$$0.mobileNetworkOperatorIsoCountryCode = parcel$$34.readString();
        return mobileRadioDetails$$0;
    }

    private void writeco_vine_android_scribe_model_ClientEvent(ClientEvent clientEvent$$3, Parcel parcel$$35, int flags$$1) {
        if (clientEvent$$3.appState == null) {
            parcel$$35.writeInt(-1);
        } else {
            parcel$$35.writeInt(1);
            writeco_vine_android_scribe_model_ApplicationState(clientEvent$$3.appState, parcel$$35, flags$$1);
        }
        if (clientEvent$$3.navigation == null) {
            parcel$$35.writeInt(-1);
        } else {
            parcel$$35.writeInt(1);
            writeco_vine_android_scribe_model_AppNavigation(clientEvent$$3.navigation, parcel$$35, flags$$1);
        }
        parcel$$35.writeString(clientEvent$$3.clientId);
        if (clientEvent$$3.eventDetails == null) {
            parcel$$35.writeInt(-1);
        } else {
            parcel$$35.writeInt(1);
            writeco_vine_android_scribe_model_EventDetails(clientEvent$$3.eventDetails, parcel$$35, flags$$1);
        }
        if (clientEvent$$3.deviceData == null) {
            parcel$$35.writeInt(-1);
        } else {
            parcel$$35.writeInt(1);
            writeco_vine_android_scribe_model_DeviceData(clientEvent$$3.deviceData, parcel$$35, flags$$1);
        }
        parcel$$35.writeString(clientEvent$$3.eventType);
    }

    private void writeco_vine_android_scribe_model_ApplicationState(ApplicationState applicationState$$2, Parcel parcel$$36, int flags$$2) {
        if (applicationState$$2.activeExperiments == null) {
            parcel$$36.writeInt(-1);
        } else {
            parcel$$36.writeInt(1);
            writeco_vine_android_scribe_model_ExperimentData(applicationState$$2.activeExperiments, parcel$$36, flags$$2);
        }
        if (applicationState$$2.twitterConnected == null) {
            parcel$$36.writeInt(-1);
        } else {
            parcel$$36.writeInt(1);
            parcel$$36.writeInt(applicationState$$2.twitterConnected.booleanValue() ? 1 : 0);
        }
        parcel$$36.writeString(applicationState$$2.applicationStatus);
        if (applicationState$$2.facebookConnected == null) {
            parcel$$36.writeInt(-1);
        } else {
            parcel$$36.writeInt(1);
            parcel$$36.writeInt(applicationState$$2.facebookConnected.booleanValue() ? 1 : 0);
        }
        if (applicationState$$2.lastLaunchTimestamp == null) {
            parcel$$36.writeInt(-1);
        } else {
            parcel$$36.writeInt(1);
            parcel$$36.writeDouble(applicationState$$2.lastLaunchTimestamp.doubleValue());
        }
        parcel$$36.writeString(applicationState$$2.edition);
        parcel$$36.writeInt(applicationState$$2.abConnected ? 1 : 0);
        if (applicationState$$2.videoCacheSize == null) {
            parcel$$36.writeInt(-1);
        } else {
            parcel$$36.writeInt(1);
            parcel$$36.writeLong(applicationState$$2.videoCacheSize.longValue());
        }
        if (applicationState$$2.loggedInUserId == null) {
            parcel$$36.writeInt(-1);
        } else {
            parcel$$36.writeInt(1);
            parcel$$36.writeLong(applicationState$$2.loggedInUserId.longValue());
        }
        if (applicationState$$2.numDrafts == null) {
            parcel$$36.writeInt(-1);
        } else {
            parcel$$36.writeInt(1);
            parcel$$36.writeLong(applicationState$$2.numDrafts.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_ExperimentData(ExperimentData experimentData$$2, Parcel parcel$$37, int flags$$3) {
        if (experimentData$$2.experimentValues == null) {
            parcel$$37.writeInt(-1);
            return;
        }
        parcel$$37.writeInt(experimentData$$2.experimentValues.size());
        Iterator<ExperimentValue> it = experimentData$$2.experimentValues.iterator();
        while (it.hasNext()) {
            ExperimentValue experimentValue$$2 = it.next();
            if (experimentValue$$2 == null) {
                parcel$$37.writeInt(-1);
            } else {
                parcel$$37.writeInt(1);
                writeco_vine_android_scribe_model_ExperimentValue(experimentValue$$2, parcel$$37, flags$$3);
            }
        }
    }

    private void writeco_vine_android_scribe_model_ExperimentValue(ExperimentValue experimentValue$$3, Parcel parcel$$38, int flags$$4) {
        parcel$$38.writeString(experimentValue$$3.value);
        parcel$$38.writeString(experimentValue$$3.key);
    }

    private void writeco_vine_android_scribe_model_AppNavigation(AppNavigation appNavigation$$2, Parcel parcel$$39, int flags$$5) {
        parcel$$39.writeString(appNavigation$$2.view);
        parcel$$39.writeString(appNavigation$$2.timelineApiUrl);
        parcel$$39.writeString(appNavigation$$2.ui_element);
        parcel$$39.writeString(appNavigation$$2.captureSourceSection);
        parcel$$39.writeString(appNavigation$$2.searchQuery);
        parcel$$39.writeInt(appNavigation$$2.isNewSearchView ? 1 : 0);
        parcel$$39.writeString(appNavigation$$2.section);
        parcel$$39.writeString(appNavigation$$2.subview);
        parcel$$39.writeString(appNavigation$$2.filtering);
    }

    private void writeco_vine_android_scribe_model_EventDetails(EventDetails eventDetails$$2, Parcel parcel$$40, int flags$$6) {
        if (eventDetails$$2.alert == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(1);
            writeco_vine_android_scribe_model_AlertDetails(eventDetails$$2.alert, parcel$$40, flags$$6);
        }
        if (eventDetails$$2.timing == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(1);
            writeco_vine_android_scribe_model_TimingDetails(eventDetails$$2.timing, parcel$$40, flags$$6);
        }
        if (eventDetails$$2.videoImportDetails == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(1);
            writeco_vine_android_scribe_model_VideoImportDetails(eventDetails$$2.videoImportDetails, parcel$$40, flags$$6);
        }
        if (eventDetails$$2.share == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(1);
            writeco_vine_android_scribe_model_ShareDetails(eventDetails$$2.share, parcel$$40, flags$$6);
        }
        if (eventDetails$$2.launch == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(1);
            writeco_vine_android_scribe_model_LaunchDetails(eventDetails$$2.launch, parcel$$40, flags$$6);
        }
        if (eventDetails$$2.httpRequestDetails == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(1);
            writeco_vine_android_scribe_model_HTTPRequestDetails(eventDetails$$2.httpRequestDetails, parcel$$40, flags$$6);
        }
        if (eventDetails$$2.playbackSummary == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(1);
            writeco_vine_android_scribe_model_PlaybackSummaryDetails(eventDetails$$2.playbackSummary, parcel$$40, flags$$6);
        }
        if (eventDetails$$2.httpPerformanceData == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(1);
            writeco_vine_android_scribe_model_HTTPPerformanceData(eventDetails$$2.httpPerformanceData, parcel$$40, flags$$6);
        }
        if (eventDetails$$2.items == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(eventDetails$$2.items.size());
            for (Item item$$2 : eventDetails$$2.items) {
                if (item$$2 == null) {
                    parcel$$40.writeInt(-1);
                } else {
                    parcel$$40.writeInt(1);
                    writeco_vine_android_scribe_model_Item(item$$2, parcel$$40, flags$$6);
                }
            }
        }
        if (eventDetails$$2.timestamp == null) {
            parcel$$40.writeInt(-1);
        } else {
            parcel$$40.writeInt(1);
            parcel$$40.writeDouble(eventDetails$$2.timestamp.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_AlertDetails(AlertDetails alertDetails$$2, Parcel parcel$$41, int flags$$7) {
        parcel$$41.writeString(alertDetails$$2.name);
        parcel$$41.writeString(alertDetails$$2.action);
    }

    private void writeco_vine_android_scribe_model_TimingDetails(TimingDetails timingDetails$$2, Parcel parcel$$42, int flags$$8) {
        if (timingDetails$$2.duration == null) {
            parcel$$42.writeInt(-1);
        } else {
            parcel$$42.writeInt(1);
            parcel$$42.writeDouble(timingDetails$$2.duration.doubleValue());
        }
        if (timingDetails$$2.startTimestamp == null) {
            parcel$$42.writeInt(-1);
        } else {
            parcel$$42.writeInt(1);
            parcel$$42.writeDouble(timingDetails$$2.startTimestamp.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_VideoImportDetails(VideoImportDetails videoImportDetails$$2, Parcel parcel$$43, int flags$$9) {
        parcel$$43.writeString(videoImportDetails$$2.result);
    }

    private void writeco_vine_android_scribe_model_ShareDetails(ShareDetails shareDetails$$2, Parcel parcel$$44, int flags$$10) {
        if (shareDetails$$2.shareTargets == null) {
            parcel$$44.writeInt(-1);
        } else {
            parcel$$44.writeInt(shareDetails$$2.shareTargets.size());
            for (String string$$0 : shareDetails$$2.shareTargets) {
                parcel$$44.writeString(string$$0);
            }
        }
        parcel$$44.writeString(shareDetails$$2.postId);
        if (shareDetails$$2.messageRecipients == null) {
            parcel$$44.writeInt(-1);
        } else {
            parcel$$44.writeInt(shareDetails$$2.messageRecipients.size());
            for (VMRecipient vMRecipient$$2 : shareDetails$$2.messageRecipients) {
                if (vMRecipient$$2 == null) {
                    parcel$$44.writeInt(-1);
                } else {
                    parcel$$44.writeInt(1);
                    writeco_vine_android_scribe_model_VMRecipient(vMRecipient$$2, parcel$$44, flags$$10);
                }
            }
        }
        if (shareDetails$$2.hasComment == null) {
            parcel$$44.writeInt(-1);
        } else {
            parcel$$44.writeInt(1);
            parcel$$44.writeInt(shareDetails$$2.hasComment.booleanValue() ? 1 : 0);
        }
    }

    private void writeco_vine_android_scribe_model_VMRecipient(VMRecipient vMRecipient$$3, Parcel parcel$$45, int flags$$11) {
        if (vMRecipient$$3.isPhone == null) {
            parcel$$45.writeInt(-1);
        } else {
            parcel$$45.writeInt(1);
            parcel$$45.writeInt(vMRecipient$$3.isPhone.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$3.isEmail == null) {
            parcel$$45.writeInt(-1);
        } else {
            parcel$$45.writeInt(1);
            parcel$$45.writeInt(vMRecipient$$3.isEmail.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$3.user == null) {
            parcel$$45.writeInt(-1);
        } else {
            parcel$$45.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(vMRecipient$$3.user, parcel$$45, flags$$11);
        }
    }

    private void writeco_vine_android_scribe_model_UserDetails(UserDetails userDetails$$3, Parcel parcel$$46, int flags$$12) {
        if (userDetails$$3.following == null) {
            parcel$$46.writeInt(-1);
        } else {
            parcel$$46.writeInt(1);
            parcel$$46.writeInt(userDetails$$3.following.booleanValue() ? 1 : 0);
        }
        if (userDetails$$3.userId == null) {
            parcel$$46.writeInt(-1);
        } else {
            parcel$$46.writeInt(1);
            parcel$$46.writeLong(userDetails$$3.userId.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_LaunchDetails(LaunchDetails launchDetails$$2, Parcel parcel$$47, int flags$$13) {
        parcel$$47.writeString(launchDetails$$2.webSrc);
    }

    private void writeco_vine_android_scribe_model_HTTPRequestDetails(HTTPRequestDetails hTTPRequestDetails$$2, Parcel parcel$$48, int flags$$14) {
        parcel$$48.writeString(hTTPRequestDetails$$2.method);
        if (hTTPRequestDetails$$2.apiError == null) {
            parcel$$48.writeInt(-1);
        } else {
            parcel$$48.writeInt(1);
            parcel$$48.writeInt(hTTPRequestDetails$$2.apiError.intValue());
        }
        if (hTTPRequestDetails$$2.httpStatus == null) {
            parcel$$48.writeInt(-1);
        } else {
            parcel$$48.writeInt(1);
            parcel$$48.writeInt(hTTPRequestDetails$$2.httpStatus.intValue());
        }
        parcel$$48.writeString(hTTPRequestDetails$$2.osErrorDetails);
        parcel$$48.writeString(hTTPRequestDetails$$2.networkError);
        parcel$$48.writeString(hTTPRequestDetails$$2.url);
    }

    private void writeco_vine_android_scribe_model_PlaybackSummaryDetails(PlaybackSummaryDetails playbackSummaryDetails$$2, Parcel parcel$$49, int flags$$15) {
        if (playbackSummaryDetails$$2.videoEndTime == null) {
            parcel$$49.writeInt(-1);
        } else {
            parcel$$49.writeInt(1);
            parcel$$49.writeFloat(playbackSummaryDetails$$2.videoEndTime.floatValue());
        }
        if (playbackSummaryDetails$$2.videoStarttime == null) {
            parcel$$49.writeInt(-1);
        } else {
            parcel$$49.writeInt(1);
            parcel$$49.writeFloat(playbackSummaryDetails$$2.videoStarttime.floatValue());
        }
        if (playbackSummaryDetails$$2.playbackInterruptions == null) {
            parcel$$49.writeInt(-1);
        } else {
            parcel$$49.writeInt(1);
            parcel$$49.writeInt(playbackSummaryDetails$$2.playbackInterruptions.intValue());
        }
        if (playbackSummaryDetails$$2.timeSpentPlaying == null) {
            parcel$$49.writeInt(-1);
        } else {
            parcel$$49.writeInt(1);
            parcel$$49.writeFloat(playbackSummaryDetails$$2.timeSpentPlaying.floatValue());
        }
        if (playbackSummaryDetails$$2.timeSpentBuffering == null) {
            parcel$$49.writeInt(-1);
        } else {
            parcel$$49.writeInt(1);
            parcel$$49.writeFloat(playbackSummaryDetails$$2.timeSpentBuffering.floatValue());
        }
        if (playbackSummaryDetails$$2.timeSpentPaused == null) {
            parcel$$49.writeInt(-1);
        } else {
            parcel$$49.writeInt(1);
            parcel$$49.writeFloat(playbackSummaryDetails$$2.timeSpentPaused.floatValue());
        }
    }

    private void writeco_vine_android_scribe_model_HTTPPerformanceData(HTTPPerformanceData hTTPPerformanceData$$2, Parcel parcel$$50, int flags$$16) {
        if (hTTPPerformanceData$$2.duration == null) {
            parcel$$50.writeInt(-1);
        } else {
            parcel$$50.writeInt(1);
            parcel$$50.writeDouble(hTTPPerformanceData$$2.duration.doubleValue());
        }
        if (hTTPPerformanceData$$2.bytesReceived == null) {
            parcel$$50.writeInt(-1);
        } else {
            parcel$$50.writeInt(1);
            parcel$$50.writeLong(hTTPPerformanceData$$2.bytesReceived.longValue());
        }
        if (hTTPPerformanceData$$2.bytesSent == null) {
            parcel$$50.writeInt(-1);
        } else {
            parcel$$50.writeInt(1);
            parcel$$50.writeLong(hTTPPerformanceData$$2.bytesSent.longValue());
        }
        if (hTTPPerformanceData$$2.durationToRequestSent == null) {
            parcel$$50.writeInt(-1);
        } else {
            parcel$$50.writeInt(1);
            parcel$$50.writeDouble(hTTPPerformanceData$$2.durationToRequestSent.doubleValue());
        }
        if (hTTPPerformanceData$$2.startTimestamp == null) {
            parcel$$50.writeInt(-1);
        } else {
            parcel$$50.writeInt(1);
            parcel$$50.writeDouble(hTTPPerformanceData$$2.startTimestamp.doubleValue());
        }
        if (hTTPPerformanceData$$2.durationToFirstByte == null) {
            parcel$$50.writeInt(-1);
        } else {
            parcel$$50.writeInt(1);
            parcel$$50.writeDouble(hTTPPerformanceData$$2.durationToFirstByte.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_Item(Item item$$3, Parcel parcel$$51, int flags$$17) {
        if (item$$3.postOrRepost == null) {
            parcel$$51.writeInt(-1);
        } else {
            parcel$$51.writeInt(1);
            writeco_vine_android_scribe_model_PostOrRepostDetails(item$$3.postOrRepost, parcel$$51, flags$$17);
        }
        parcel$$51.writeString(item$$3.reference);
        parcel$$51.writeString(item$$3.itemType);
        if (item$$3.activity == null) {
            parcel$$51.writeInt(-1);
        } else {
            parcel$$51.writeInt(1);
            writeco_vine_android_scribe_model_ActivityDetails(item$$3.activity, parcel$$51, flags$$17);
        }
        if (item$$3.postMosaic == null) {
            parcel$$51.writeInt(-1);
        } else {
            parcel$$51.writeInt(1);
            writeco_vine_android_scribe_model_MosaicDetails(item$$3.postMosaic, parcel$$51, flags$$17);
        }
        if (item$$3.suggestion == null) {
            parcel$$51.writeInt(-1);
        } else {
            parcel$$51.writeInt(1);
            writeco_vine_android_scribe_model_SuggestionDetails(item$$3.suggestion, parcel$$51, flags$$17);
        }
        if (item$$3.userMosaic == null) {
            parcel$$51.writeInt(-1);
        } else {
            parcel$$51.writeInt(1);
            writeco_vine_android_scribe_model_MosaicDetails(item$$3.userMosaic, parcel$$51, flags$$17);
        }
        if (item$$3.comment == null) {
            parcel$$51.writeInt(-1);
        } else {
            parcel$$51.writeInt(1);
            writeco_vine_android_scribe_model_CommentDetails(item$$3.comment, parcel$$51, flags$$17);
        }
        if (item$$3.position == null) {
            parcel$$51.writeInt(-1);
        } else {
            parcel$$51.writeInt(1);
            writeco_vine_android_scribe_model_ItemPosition(item$$3.position, parcel$$51, flags$$17);
        }
        if (item$$3.tag == null) {
            parcel$$51.writeInt(-1);
        } else {
            parcel$$51.writeInt(1);
            writeco_vine_android_scribe_model_TagDetails(item$$3.tag, parcel$$51, flags$$17);
        }
        if (item$$3.user == null) {
            parcel$$51.writeInt(-1);
        } else {
            parcel$$51.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(item$$3.user, parcel$$51, flags$$17);
        }
    }

    private void writeco_vine_android_scribe_model_PostOrRepostDetails(PostOrRepostDetails postOrRepostDetails$$2, Parcel parcel$$52, int flags$$18) {
        if (postOrRepostDetails$$2.postAuthorId == null) {
            parcel$$52.writeInt(-1);
        } else {
            parcel$$52.writeInt(1);
            parcel$$52.writeLong(postOrRepostDetails$$2.postAuthorId.longValue());
        }
        parcel$$52.writeString(postOrRepostDetails$$2.longformId);
        if (postOrRepostDetails$$2.repostId == null) {
            parcel$$52.writeInt(-1);
        } else {
            parcel$$52.writeInt(1);
            parcel$$52.writeLong(postOrRepostDetails$$2.repostId.longValue());
        }
        if (postOrRepostDetails$$2.repostAuthorId == null) {
            parcel$$52.writeInt(-1);
        } else {
            parcel$$52.writeInt(1);
            parcel$$52.writeLong(postOrRepostDetails$$2.repostAuthorId.longValue());
        }
        if (postOrRepostDetails$$2.hasSimilarPosts == null) {
            parcel$$52.writeInt(-1);
        } else {
            parcel$$52.writeInt(1);
            parcel$$52.writeInt(postOrRepostDetails$$2.hasSimilarPosts.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$2.postId == null) {
            parcel$$52.writeInt(-1);
        } else {
            parcel$$52.writeInt(1);
            parcel$$52.writeLong(postOrRepostDetails$$2.postId.longValue());
        }
        if (postOrRepostDetails$$2.byline == null) {
            parcel$$52.writeInt(-1);
        } else {
            parcel$$52.writeInt(1);
            writeco_vine_android_scribe_model_Byline(postOrRepostDetails$$2.byline, parcel$$52, flags$$18);
        }
        if (postOrRepostDetails$$2.liked == null) {
            parcel$$52.writeInt(-1);
        } else {
            parcel$$52.writeInt(1);
            parcel$$52.writeInt(postOrRepostDetails$$2.liked.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$2.reposted == null) {
            parcel$$52.writeInt(-1);
        } else {
            parcel$$52.writeInt(1);
            parcel$$52.writeInt(postOrRepostDetails$$2.reposted.booleanValue() ? 1 : 0);
        }
    }

    private void writeco_vine_android_scribe_model_Byline(Byline byline$$2, Parcel parcel$$53, int flags$$19) {
        parcel$$53.writeString(byline$$2.detailedDescription);
        parcel$$53.writeString(byline$$2.actionTitle);
        if (byline$$2.postIds == null) {
            parcel$$53.writeInt(-1);
        } else {
            parcel$$53.writeInt(byline$$2.postIds.size());
            Iterator<Long> it = byline$$2.postIds.iterator();
            while (it.hasNext()) {
                Long long$$17 = it.next();
                if (long$$17 == null) {
                    parcel$$53.writeInt(-1);
                } else {
                    parcel$$53.writeInt(1);
                    parcel$$53.writeLong(long$$17.longValue());
                }
            }
        }
        if (byline$$2.userIds == null) {
            parcel$$53.writeInt(-1);
        } else {
            parcel$$53.writeInt(byline$$2.userIds.size());
            Iterator<Long> it2 = byline$$2.userIds.iterator();
            while (it2.hasNext()) {
                Long long$$18 = it2.next();
                if (long$$18 == null) {
                    parcel$$53.writeInt(-1);
                } else {
                    parcel$$53.writeInt(1);
                    parcel$$53.writeLong(long$$18.longValue());
                }
            }
        }
        parcel$$53.writeString(byline$$2.description);
        parcel$$53.writeString(byline$$2.iconUrl);
        parcel$$53.writeString(byline$$2.body);
        parcel$$53.writeString(byline$$2.actionIconUrl);
    }

    private void writeco_vine_android_scribe_model_ActivityDetails(ActivityDetails activityDetails$$2, Parcel parcel$$54, int flags$$20) {
        if (activityDetails$$2.activityId == null) {
            parcel$$54.writeInt(-1);
        } else {
            parcel$$54.writeInt(1);
            parcel$$54.writeLong(activityDetails$$2.activityId.longValue());
        }
        if (activityDetails$$2.nMore == null) {
            parcel$$54.writeInt(-1);
        } else {
            parcel$$54.writeInt(1);
            parcel$$54.writeInt(activityDetails$$2.nMore.intValue());
        }
        parcel$$54.writeString(activityDetails$$2.activityType);
    }

    private void writeco_vine_android_scribe_model_MosaicDetails(MosaicDetails mosaicDetails$$3, Parcel parcel$$55, int flags$$21) {
        parcel$$55.writeString(mosaicDetails$$3.mosaicType);
        parcel$$55.writeString(mosaicDetails$$3.link);
    }

    private void writeco_vine_android_scribe_model_SuggestionDetails(SuggestionDetails suggestionDetails$$2, Parcel parcel$$56, int flags$$22) {
        parcel$$56.writeString(suggestionDetails$$2.suggestedQuery);
    }

    private void writeco_vine_android_scribe_model_CommentDetails(CommentDetails commentDetails$$2, Parcel parcel$$57, int flags$$23) {
        if (commentDetails$$2.commentId == null) {
            parcel$$57.writeInt(-1);
        } else {
            parcel$$57.writeInt(1);
            parcel$$57.writeLong(commentDetails$$2.commentId.longValue());
        }
        if (commentDetails$$2.authorId == null) {
            parcel$$57.writeInt(-1);
        } else {
            parcel$$57.writeInt(1);
            parcel$$57.writeLong(commentDetails$$2.authorId.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_ItemPosition(ItemPosition itemPosition$$2, Parcel parcel$$58, int flags$$24) {
        if (itemPosition$$2.offset == null) {
            parcel$$58.writeInt(-1);
        } else {
            parcel$$58.writeInt(1);
            parcel$$58.writeInt(itemPosition$$2.offset.intValue());
        }
    }

    private void writeco_vine_android_scribe_model_TagDetails(TagDetails tagDetails$$2, Parcel parcel$$59, int flags$$25) {
        parcel$$59.writeString(tagDetails$$2.tagId);
    }

    private void writeco_vine_android_scribe_model_DeviceData(DeviceData deviceData$$2, Parcel parcel$$60, int flags$$26) {
        if (deviceData$$2.gpsData == null) {
            parcel$$60.writeInt(-1);
        } else {
            parcel$$60.writeInt(1);
            writeco_vine_android_scribe_model_GPSData(deviceData$$2.gpsData, parcel$$60, flags$$26);
        }
        parcel$$60.writeString(deviceData$$2.orientation);
        parcel$$60.writeString(deviceData$$2.os);
        parcel$$60.writeString(deviceData$$2.timezone);
        parcel$$60.writeString(deviceData$$2.deviceName);
        if (deviceData$$2.otherAudioIsPlaying == null) {
            parcel$$60.writeInt(-1);
        } else {
            parcel$$60.writeInt(1);
            parcel$$60.writeInt(deviceData$$2.otherAudioIsPlaying.booleanValue() ? 1 : 0);
        }
        parcel$$60.writeString(deviceData$$2.manufacturer);
        if (deviceData$$2.languageCodes == null) {
            parcel$$60.writeInt(-1);
        } else {
            parcel$$60.writeInt(deviceData$$2.languageCodes.size());
            for (String string$$1 : deviceData$$2.languageCodes) {
                parcel$$60.writeString(string$$1);
            }
        }
        if (deviceData$$2.brightness == null) {
            parcel$$60.writeInt(-1);
        } else {
            parcel$$60.writeInt(1);
            parcel$$60.writeDouble(deviceData$$2.brightness.doubleValue());
        }
        parcel$$60.writeString(deviceData$$2.osVersion);
        if (deviceData$$2.bytesFree == null) {
            parcel$$60.writeInt(-1);
        } else {
            parcel$$60.writeInt(1);
            parcel$$60.writeLong(deviceData$$2.bytesFree.longValue());
        }
        parcel$$60.writeString(deviceData$$2.browser);
        parcel$$60.writeString(deviceData$$2.browserVersion);
        parcel$$60.writeString(deviceData$$2.deviceModel);
        parcel$$60.writeString(deviceData$$2.internetAccessType);
        if (deviceData$$2.radioDetails == null) {
            parcel$$60.writeInt(-1);
        } else {
            parcel$$60.writeInt(1);
            writeco_vine_android_scribe_model_MobileRadioDetails(deviceData$$2.radioDetails, parcel$$60, flags$$26);
        }
        if (deviceData$$2.bytesAvailable == null) {
            parcel$$60.writeInt(-1);
        } else {
            parcel$$60.writeInt(1);
            parcel$$60.writeLong(deviceData$$2.bytesAvailable.longValue());
        }
        if (deviceData$$2.batteryLevel == null) {
            parcel$$60.writeInt(-1);
        } else {
            parcel$$60.writeInt(1);
            parcel$$60.writeDouble(deviceData$$2.batteryLevel.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_GPSData(GPSData gPSData$$2, Parcel parcel$$61, int flags$$27) {
        if (gPSData$$2.altitude == null) {
            parcel$$61.writeInt(-1);
        } else {
            parcel$$61.writeInt(1);
            parcel$$61.writeDouble(gPSData$$2.altitude.doubleValue());
        }
        if (gPSData$$2.verticalAccuracy == null) {
            parcel$$61.writeInt(-1);
        } else {
            parcel$$61.writeInt(1);
            parcel$$61.writeDouble(gPSData$$2.verticalAccuracy.doubleValue());
        }
        if (gPSData$$2.latitude == null) {
            parcel$$61.writeInt(-1);
        } else {
            parcel$$61.writeInt(1);
            parcel$$61.writeDouble(gPSData$$2.latitude.doubleValue());
        }
        if (gPSData$$2.horizontalAccuracy == null) {
            parcel$$61.writeInt(-1);
        } else {
            parcel$$61.writeInt(1);
            parcel$$61.writeDouble(gPSData$$2.horizontalAccuracy.doubleValue());
        }
        if (gPSData$$2.longitude == null) {
            parcel$$61.writeInt(-1);
        } else {
            parcel$$61.writeInt(1);
            parcel$$61.writeDouble(gPSData$$2.longitude.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_MobileRadioDetails(MobileRadioDetails mobileRadioDetails$$2, Parcel parcel$$62, int flags$$28) {
        parcel$$62.writeString(mobileRadioDetails$$2.mobileNetworkOperatorName);
        parcel$$62.writeString(mobileRadioDetails$$2.mobileNetworkOperatorCountryCode);
        parcel$$62.writeString(mobileRadioDetails$$2.mobileSimProviderName);
        parcel$$62.writeString(mobileRadioDetails$$2.radioStatus);
        if (mobileRadioDetails$$2.signalStrength == null) {
            parcel$$62.writeInt(-1);
        } else {
            parcel$$62.writeInt(1);
            parcel$$62.writeInt(mobileRadioDetails$$2.signalStrength.intValue());
        }
        parcel$$62.writeString(mobileRadioDetails$$2.mobileNetworkOperatorCode);
        parcel$$62.writeString(mobileRadioDetails$$2.mobileSimProviderIsoCountryCode);
        parcel$$62.writeString(mobileRadioDetails$$2.mobileSimProviderCode);
        parcel$$62.writeString(mobileRadioDetails$$2.mobileNetworkOperatorIsoCountryCode);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public ClientEvent getParcel() {
        return this.clientEvent$$0;
    }
}
