package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.VineChannel;
import java.util.ArrayList;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineChannel$$Parcelable implements Parcelable, ParcelWrapper<VineChannel> {
    public static final VineChannel$$Parcelable$Creator$$33 CREATOR = new VineChannel$$Parcelable$Creator$$33();
    private VineChannel vineChannel$$0;

    public VineChannel$$Parcelable(Parcel parcel$$351) {
        VineChannel vineChannel$$2;
        if (parcel$$351.readInt() == -1) {
            vineChannel$$2 = null;
        } else {
            vineChannel$$2 = readco_vine_android_api_VineChannel(parcel$$351);
        }
        this.vineChannel$$0 = vineChannel$$2;
    }

    public VineChannel$$Parcelable(VineChannel vineChannel$$4) {
        this.vineChannel$$0 = vineChannel$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$352, int flags) {
        if (this.vineChannel$$0 == null) {
            parcel$$352.writeInt(-1);
        } else {
            parcel$$352.writeInt(1);
            writeco_vine_android_api_VineChannel(this.vineChannel$$0, parcel$$352, flags);
        }
    }

    private VineChannel readco_vine_android_api_VineChannel(Parcel parcel$$353) {
        Boolean boolean$$86;
        VineChannel.TimeLine timeLine$$1;
        VineChannel vineChannel$$1 = new VineChannel();
        vineChannel$$1.backgroundColor = parcel$$353.readString();
        vineChannel$$1.created = parcel$$353.readLong();
        int int$$321 = parcel$$353.readInt();
        if (int$$321 < 0) {
            boolean$$86 = null;
        } else {
            boolean$$86 = Boolean.valueOf(parcel$$353.readInt() == 1);
        }
        vineChannel$$1.showRecent = boolean$$86;
        vineChannel$$1.iconFullUrl = parcel$$353.readString();
        vineChannel$$1.channel = parcel$$353.readString();
        vineChannel$$1.description = parcel$$353.readString();
        vineChannel$$1.retinaIconFullUrl = parcel$$353.readString();
        vineChannel$$1.following = parcel$$353.readInt() == 1;
        vineChannel$$1.exploreRetinaIconFullUrl = parcel$$353.readString();
        if (parcel$$353.readInt() == -1) {
            timeLine$$1 = null;
        } else {
            timeLine$$1 = readco_vine_android_api_VineChannel$TimeLine(parcel$$353);
        }
        vineChannel$$1.timeline = timeLine$$1;
        vineChannel$$1.colorHex = parcel$$353.readInt();
        vineChannel$$1.channelId = parcel$$353.readLong();
        vineChannel$$1.fontColor = parcel$$353.readString();
        vineChannel$$1.secondaryColor = parcel$$353.readString();
        return vineChannel$$1;
    }

    private VineChannel.TimeLine readco_vine_android_api_VineChannel$TimeLine(Parcel parcel$$354) {
        VineChannel.TimeLine timeLine$$0 = new VineChannel.TimeLine();
        timeLine$$0.items = (ArrayList) parcel$$354.readSerializable();
        return timeLine$$0;
    }

    private void writeco_vine_android_api_VineChannel(VineChannel vineChannel$$3, Parcel parcel$$355, int flags$$126) {
        parcel$$355.writeString(vineChannel$$3.backgroundColor);
        parcel$$355.writeLong(vineChannel$$3.created);
        if (vineChannel$$3.showRecent == null) {
            parcel$$355.writeInt(-1);
        } else {
            parcel$$355.writeInt(1);
            parcel$$355.writeInt(vineChannel$$3.showRecent.booleanValue() ? 1 : 0);
        }
        parcel$$355.writeString(vineChannel$$3.iconFullUrl);
        parcel$$355.writeString(vineChannel$$3.channel);
        parcel$$355.writeString(vineChannel$$3.description);
        parcel$$355.writeString(vineChannel$$3.retinaIconFullUrl);
        parcel$$355.writeInt(vineChannel$$3.following ? 1 : 0);
        parcel$$355.writeString(vineChannel$$3.exploreRetinaIconFullUrl);
        if (vineChannel$$3.timeline == null) {
            parcel$$355.writeInt(-1);
        } else {
            parcel$$355.writeInt(1);
            writeco_vine_android_api_VineChannel$TimeLine(vineChannel$$3.timeline, parcel$$355, flags$$126);
        }
        parcel$$355.writeInt(vineChannel$$3.colorHex);
        parcel$$355.writeLong(vineChannel$$3.channelId);
        parcel$$355.writeString(vineChannel$$3.fontColor);
        parcel$$355.writeString(vineChannel$$3.secondaryColor);
    }

    private void writeco_vine_android_api_VineChannel$TimeLine(VineChannel.TimeLine timeLine$$2, Parcel parcel$$356, int flags$$127) {
        parcel$$356.writeSerializable(timeLine$$2.items);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineChannel getParcel() {
        return this.vineChannel$$0;
    }
}
