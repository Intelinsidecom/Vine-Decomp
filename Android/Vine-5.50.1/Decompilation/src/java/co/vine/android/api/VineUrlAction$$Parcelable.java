package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineUrlAction$$Parcelable implements Parcelable, ParcelWrapper<VineUrlAction> {
    public static final VineUrlAction$$Parcelable$Creator$$39 CREATOR = new VineUrlAction$$Parcelable$Creator$$39();
    private VineUrlAction vineUrlAction$$3;

    public VineUrlAction$$Parcelable(Parcel parcel$$391) {
        VineUrlAction vineUrlAction$$5;
        if (parcel$$391.readInt() == -1) {
            vineUrlAction$$5 = null;
        } else {
            vineUrlAction$$5 = readco_vine_android_api_VineUrlAction(parcel$$391);
        }
        this.vineUrlAction$$3 = vineUrlAction$$5;
    }

    public VineUrlAction$$Parcelable(VineUrlAction vineUrlAction$$7) {
        this.vineUrlAction$$3 = vineUrlAction$$7;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$392, int flags) {
        if (this.vineUrlAction$$3 == null) {
            parcel$$392.writeInt(-1);
        } else {
            parcel$$392.writeInt(1);
            writeco_vine_android_api_VineUrlAction(this.vineUrlAction$$3, parcel$$392, flags);
        }
    }

    private VineUrlAction readco_vine_android_api_VineUrlAction(Parcel parcel$$393) {
        VineUrlAction vineUrlAction$$4 = new VineUrlAction();
        vineUrlAction$$4.reference = parcel$$393.readString();
        vineUrlAction$$4.closeable = parcel$$393.readInt() == 1;
        vineUrlAction$$4.actionTitle = parcel$$393.readString();
        vineUrlAction$$4.originUrl = parcel$$393.readString();
        vineUrlAction$$4.backgroundVideoUrl = parcel$$393.readString();
        vineUrlAction$$4.description = parcel$$393.readString();
        vineUrlAction$$4.title = parcel$$393.readString();
        vineUrlAction$$4.actionIconUrl = parcel$$393.readString();
        vineUrlAction$$4.type = parcel$$393.readString();
        vineUrlAction$$4.actionLink = parcel$$393.readString();
        vineUrlAction$$4.backgroundImageUrl = parcel$$393.readString();
        return vineUrlAction$$4;
    }

    private void writeco_vine_android_api_VineUrlAction(VineUrlAction vineUrlAction$$6, Parcel parcel$$394, int flags$$137) {
        parcel$$394.writeString(vineUrlAction$$6.reference);
        parcel$$394.writeInt(vineUrlAction$$6.closeable ? 1 : 0);
        parcel$$394.writeString(vineUrlAction$$6.actionTitle);
        parcel$$394.writeString(vineUrlAction$$6.originUrl);
        parcel$$394.writeString(vineUrlAction$$6.backgroundVideoUrl);
        parcel$$394.writeString(vineUrlAction$$6.description);
        parcel$$394.writeString(vineUrlAction$$6.title);
        parcel$$394.writeString(vineUrlAction$$6.actionIconUrl);
        parcel$$394.writeString(vineUrlAction$$6.type);
        parcel$$394.writeString(vineUrlAction$$6.actionLink);
        parcel$$394.writeString(vineUrlAction$$6.backgroundImageUrl);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineUrlAction getParcel() {
        return this.vineUrlAction$$3;
    }
}
