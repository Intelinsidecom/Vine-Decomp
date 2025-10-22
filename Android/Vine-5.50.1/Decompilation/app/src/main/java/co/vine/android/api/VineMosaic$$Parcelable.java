package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineMosaic$$Parcelable implements Parcelable, ParcelWrapper<VineMosaic> {
    public static final VineMosaic$$Parcelable$Creator$$41 CREATOR = new VineMosaic$$Parcelable$Creator$$41();
    private VineMosaic vineMosaic$$3;

    public VineMosaic$$Parcelable(Parcel parcel$$401) {
        VineMosaic vineMosaic$$5;
        if (parcel$$401.readInt() == -1) {
            vineMosaic$$5 = null;
        } else {
            vineMosaic$$5 = readco_vine_android_api_VineMosaic(parcel$$401);
        }
        this.vineMosaic$$3 = vineMosaic$$5;
    }

    public VineMosaic$$Parcelable(VineMosaic vineMosaic$$7) {
        this.vineMosaic$$3 = vineMosaic$$7;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$402, int flags) {
        if (this.vineMosaic$$3 == null) {
            parcel$$402.writeInt(-1);
        } else {
            parcel$$402.writeInt(1);
            writeco_vine_android_api_VineMosaic(this.vineMosaic$$3, parcel$$402, flags);
        }
    }

    private VineMosaic readco_vine_android_api_VineMosaic(Parcel parcel$$403) {
        Boolean boolean$$92;
        VineMosaic vineMosaic$$4 = new VineMosaic();
        vineMosaic$$4.reference = parcel$$403.readString();
        vineMosaic$$4.timelineItemType = (TimelineItemType) parcel$$403.readSerializable();
        int int$$345 = parcel$$403.readInt();
        if (int$$345 < 0) {
            boolean$$92 = null;
        } else {
            boolean$$92 = Boolean.valueOf(parcel$$403.readInt() == 1);
        }
        vineMosaic$$4.pinnable = boolean$$92;
        vineMosaic$$4.avatarUrl = parcel$$403.readString();
        vineMosaic$$4.mosaicItems = (ArrayList) parcel$$403.readSerializable();
        vineMosaic$$4.originUrl = parcel$$403.readString();
        vineMosaic$$4.link = parcel$$403.readString();
        vineMosaic$$4.mosaicType = parcel$$403.readString();
        vineMosaic$$4.description = parcel$$403.readString();
        vineMosaic$$4.title = parcel$$403.readString();
        vineMosaic$$4.type = parcel$$403.readString();
        return vineMosaic$$4;
    }

    private void writeco_vine_android_api_VineMosaic(VineMosaic vineMosaic$$6, Parcel parcel$$404, int flags$$139) {
        parcel$$404.writeString(vineMosaic$$6.reference);
        parcel$$404.writeSerializable(vineMosaic$$6.timelineItemType);
        if (vineMosaic$$6.pinnable == null) {
            parcel$$404.writeInt(-1);
        } else {
            parcel$$404.writeInt(1);
            parcel$$404.writeInt(vineMosaic$$6.pinnable.booleanValue() ? 1 : 0);
        }
        parcel$$404.writeString(vineMosaic$$6.avatarUrl);
        parcel$$404.writeSerializable(vineMosaic$$6.mosaicItems);
        parcel$$404.writeString(vineMosaic$$6.originUrl);
        parcel$$404.writeString(vineMosaic$$6.link);
        parcel$$404.writeString(vineMosaic$$6.mosaicType);
        parcel$$404.writeString(vineMosaic$$6.description);
        parcel$$404.writeString(vineMosaic$$6.title);
        parcel$$404.writeString(vineMosaic$$6.type);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineMosaic getParcel() {
        return this.vineMosaic$$3;
    }
}
