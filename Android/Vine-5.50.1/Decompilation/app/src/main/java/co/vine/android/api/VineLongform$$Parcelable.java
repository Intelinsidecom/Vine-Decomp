package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineLongform$$Parcelable implements Parcelable, ParcelWrapper<VineLongform> {
    public static final VineLongform$$Parcelable$Creator$$43 CREATOR = new VineLongform$$Parcelable$Creator$$43();
    private VineLongform vineLongform$$0;

    public VineLongform$$Parcelable(Parcel parcel$$411) {
        VineLongform vineLongform$$2;
        if (parcel$$411.readInt() == -1) {
            vineLongform$$2 = null;
        } else {
            vineLongform$$2 = readco_vine_android_api_VineLongform(parcel$$411);
        }
        this.vineLongform$$0 = vineLongform$$2;
    }

    public VineLongform$$Parcelable(VineLongform vineLongform$$4) {
        this.vineLongform$$0 = vineLongform$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$412, int flags) {
        if (this.vineLongform$$0 == null) {
            parcel$$412.writeInt(-1);
        } else {
            parcel$$412.writeInt(1);
            writeco_vine_android_api_VineLongform(this.vineLongform$$0, parcel$$412, flags);
        }
    }

    private VineLongform readco_vine_android_api_VineLongform(Parcel parcel$$413) {
        VineLongform vineLongform$$1 = new VineLongform();
        vineLongform$$1.duration = parcel$$413.readFloat();
        vineLongform$$1.longformId = parcel$$413.readString();
        vineLongform$$1.videoUrl = parcel$$413.readString();
        vineLongform$$1.aspectRatio = parcel$$413.readFloat();
        vineLongform$$1.thumbnailUrl = parcel$$413.readString();
        return vineLongform$$1;
    }

    private void writeco_vine_android_api_VineLongform(VineLongform vineLongform$$3, Parcel parcel$$414, int flags$$141) {
        parcel$$414.writeFloat(vineLongform$$3.duration);
        parcel$$414.writeString(vineLongform$$3.longformId);
        parcel$$414.writeString(vineLongform$$3.videoUrl);
        parcel$$414.writeFloat(vineLongform$$3.aspectRatio);
        parcel$$414.writeString(vineLongform$$3.thumbnailUrl);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineLongform getParcel() {
        return this.vineLongform$$0;
    }
}
