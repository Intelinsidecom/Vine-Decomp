package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class TimelineItemWrapper$$Parcelable implements Parcelable, ParcelWrapper<TimelineItemWrapper> {
    public static final TimelineItemWrapper$$Parcelable$Creator$$37 CREATOR = new TimelineItemWrapper$$Parcelable$Creator$$37();
    private TimelineItemWrapper timelineItemWrapper$$0;

    public TimelineItemWrapper$$Parcelable(Parcel parcel$$375) {
        TimelineItemWrapper timelineItemWrapper$$2;
        if (parcel$$375.readInt() == -1) {
            timelineItemWrapper$$2 = null;
        } else {
            timelineItemWrapper$$2 = readco_vine_android_api_TimelineItemWrapper(parcel$$375);
        }
        this.timelineItemWrapper$$0 = timelineItemWrapper$$2;
    }

    public TimelineItemWrapper$$Parcelable(TimelineItemWrapper timelineItemWrapper$$4) {
        this.timelineItemWrapper$$0 = timelineItemWrapper$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$376, int flags) {
        if (this.timelineItemWrapper$$0 == null) {
            parcel$$376.writeInt(-1);
        } else {
            parcel$$376.writeInt(1);
            writeco_vine_android_api_TimelineItemWrapper(this.timelineItemWrapper$$0, parcel$$376, flags);
        }
    }

    private TimelineItemWrapper readco_vine_android_api_TimelineItemWrapper(Parcel parcel$$377) {
        VineUrlAction vineUrlAction$$1;
        VineMosaic vineMosaic$$1;
        VineSolicitor vineSolicitor$$1;
        TimelineItemWrapper timelineItemWrapper$$1 = new TimelineItemWrapper();
        timelineItemWrapper$$1.feed = (VineFeed) parcel$$377.readParcelable(TimelineItemWrapper$$Parcelable.class.getClassLoader());
        timelineItemWrapper$$1.post = (VinePost) parcel$$377.readParcelable(TimelineItemWrapper$$Parcelable.class.getClassLoader());
        if (parcel$$377.readInt() == -1) {
            vineUrlAction$$1 = null;
        } else {
            vineUrlAction$$1 = readco_vine_android_api_VineUrlAction(parcel$$377);
        }
        timelineItemWrapper$$1.urlAction = vineUrlAction$$1;
        if (parcel$$377.readInt() == -1) {
            vineMosaic$$1 = null;
        } else {
            vineMosaic$$1 = readco_vine_android_api_VineMosaic(parcel$$377);
        }
        timelineItemWrapper$$1.mosaic = vineMosaic$$1;
        if (parcel$$377.readInt() == -1) {
            vineSolicitor$$1 = null;
        } else {
            vineSolicitor$$1 = readco_vine_android_api_VineSolicitor(parcel$$377);
        }
        timelineItemWrapper$$1.solicitor = vineSolicitor$$1;
        return timelineItemWrapper$$1;
    }

    private VineUrlAction readco_vine_android_api_VineUrlAction(Parcel parcel$$378) {
        VineUrlAction vineUrlAction$$0 = new VineUrlAction();
        vineUrlAction$$0.reference = parcel$$378.readString();
        vineUrlAction$$0.closeable = parcel$$378.readInt() == 1;
        vineUrlAction$$0.actionTitle = parcel$$378.readString();
        vineUrlAction$$0.originUrl = parcel$$378.readString();
        vineUrlAction$$0.backgroundVideoUrl = parcel$$378.readString();
        vineUrlAction$$0.description = parcel$$378.readString();
        vineUrlAction$$0.title = parcel$$378.readString();
        vineUrlAction$$0.actionIconUrl = parcel$$378.readString();
        vineUrlAction$$0.type = parcel$$378.readString();
        vineUrlAction$$0.actionLink = parcel$$378.readString();
        vineUrlAction$$0.backgroundImageUrl = parcel$$378.readString();
        return vineUrlAction$$0;
    }

    private VineMosaic readco_vine_android_api_VineMosaic(Parcel parcel$$379) {
        Boolean boolean$$87;
        VineMosaic vineMosaic$$0 = new VineMosaic();
        vineMosaic$$0.reference = parcel$$379.readString();
        vineMosaic$$0.timelineItemType = (TimelineItemType) parcel$$379.readSerializable();
        int int$$336 = parcel$$379.readInt();
        if (int$$336 < 0) {
            boolean$$87 = null;
        } else {
            boolean$$87 = Boolean.valueOf(parcel$$379.readInt() == 1);
        }
        vineMosaic$$0.pinnable = boolean$$87;
        vineMosaic$$0.avatarUrl = parcel$$379.readString();
        vineMosaic$$0.mosaicItems = (ArrayList) parcel$$379.readSerializable();
        vineMosaic$$0.originUrl = parcel$$379.readString();
        vineMosaic$$0.link = parcel$$379.readString();
        vineMosaic$$0.mosaicType = parcel$$379.readString();
        vineMosaic$$0.description = parcel$$379.readString();
        vineMosaic$$0.title = parcel$$379.readString();
        vineMosaic$$0.type = parcel$$379.readString();
        return vineMosaic$$0;
    }

    private VineSolicitor readco_vine_android_api_VineSolicitor(Parcel parcel$$380) {
        VineSolicitor vineSolicitor$$0 = new VineSolicitor();
        vineSolicitor$$0.reference = parcel$$380.readString();
        vineSolicitor$$0.buttonText = parcel$$380.readString();
        vineSolicitor$$0.closeable = parcel$$380.readInt() == 1;
        vineSolicitor$$0.originUrl = parcel$$380.readString();
        vineSolicitor$$0.description = parcel$$380.readString();
        vineSolicitor$$0.completeTitle = parcel$$380.readString();
        vineSolicitor$$0.type = parcel$$380.readString();
        vineSolicitor$$0.title = parcel$$380.readString();
        vineSolicitor$$0.dismissText = parcel$$380.readString();
        vineSolicitor$$0.completeDescription = parcel$$380.readString();
        vineSolicitor$$0.completeButton = parcel$$380.readString();
        vineSolicitor$$0.completeExplanation = parcel$$380.readString();
        return vineSolicitor$$0;
    }

    private void writeco_vine_android_api_TimelineItemWrapper(TimelineItemWrapper timelineItemWrapper$$3, Parcel parcel$$381, int flags$$132) {
        parcel$$381.writeParcelable(timelineItemWrapper$$3.feed, flags$$132);
        parcel$$381.writeParcelable(timelineItemWrapper$$3.post, flags$$132);
        if (timelineItemWrapper$$3.urlAction == null) {
            parcel$$381.writeInt(-1);
        } else {
            parcel$$381.writeInt(1);
            writeco_vine_android_api_VineUrlAction(timelineItemWrapper$$3.urlAction, parcel$$381, flags$$132);
        }
        if (timelineItemWrapper$$3.mosaic == null) {
            parcel$$381.writeInt(-1);
        } else {
            parcel$$381.writeInt(1);
            writeco_vine_android_api_VineMosaic(timelineItemWrapper$$3.mosaic, parcel$$381, flags$$132);
        }
        if (timelineItemWrapper$$3.solicitor == null) {
            parcel$$381.writeInt(-1);
        } else {
            parcel$$381.writeInt(1);
            writeco_vine_android_api_VineSolicitor(timelineItemWrapper$$3.solicitor, parcel$$381, flags$$132);
        }
    }

    private void writeco_vine_android_api_VineUrlAction(VineUrlAction vineUrlAction$$2, Parcel parcel$$382, int flags$$133) {
        parcel$$382.writeString(vineUrlAction$$2.reference);
        parcel$$382.writeInt(vineUrlAction$$2.closeable ? 1 : 0);
        parcel$$382.writeString(vineUrlAction$$2.actionTitle);
        parcel$$382.writeString(vineUrlAction$$2.originUrl);
        parcel$$382.writeString(vineUrlAction$$2.backgroundVideoUrl);
        parcel$$382.writeString(vineUrlAction$$2.description);
        parcel$$382.writeString(vineUrlAction$$2.title);
        parcel$$382.writeString(vineUrlAction$$2.actionIconUrl);
        parcel$$382.writeString(vineUrlAction$$2.type);
        parcel$$382.writeString(vineUrlAction$$2.actionLink);
        parcel$$382.writeString(vineUrlAction$$2.backgroundImageUrl);
    }

    private void writeco_vine_android_api_VineMosaic(VineMosaic vineMosaic$$2, Parcel parcel$$383, int flags$$134) {
        parcel$$383.writeString(vineMosaic$$2.reference);
        parcel$$383.writeSerializable(vineMosaic$$2.timelineItemType);
        if (vineMosaic$$2.pinnable == null) {
            parcel$$383.writeInt(-1);
        } else {
            parcel$$383.writeInt(1);
            parcel$$383.writeInt(vineMosaic$$2.pinnable.booleanValue() ? 1 : 0);
        }
        parcel$$383.writeString(vineMosaic$$2.avatarUrl);
        parcel$$383.writeSerializable(vineMosaic$$2.mosaicItems);
        parcel$$383.writeString(vineMosaic$$2.originUrl);
        parcel$$383.writeString(vineMosaic$$2.link);
        parcel$$383.writeString(vineMosaic$$2.mosaicType);
        parcel$$383.writeString(vineMosaic$$2.description);
        parcel$$383.writeString(vineMosaic$$2.title);
        parcel$$383.writeString(vineMosaic$$2.type);
    }

    private void writeco_vine_android_api_VineSolicitor(VineSolicitor vineSolicitor$$2, Parcel parcel$$384, int flags$$135) {
        parcel$$384.writeString(vineSolicitor$$2.reference);
        parcel$$384.writeString(vineSolicitor$$2.buttonText);
        parcel$$384.writeInt(vineSolicitor$$2.closeable ? 1 : 0);
        parcel$$384.writeString(vineSolicitor$$2.originUrl);
        parcel$$384.writeString(vineSolicitor$$2.description);
        parcel$$384.writeString(vineSolicitor$$2.completeTitle);
        parcel$$384.writeString(vineSolicitor$$2.type);
        parcel$$384.writeString(vineSolicitor$$2.title);
        parcel$$384.writeString(vineSolicitor$$2.dismissText);
        parcel$$384.writeString(vineSolicitor$$2.completeDescription);
        parcel$$384.writeString(vineSolicitor$$2.completeButton);
        parcel$$384.writeString(vineSolicitor$$2.completeExplanation);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public TimelineItemWrapper getParcel() {
        return this.timelineItemWrapper$$0;
    }
}
