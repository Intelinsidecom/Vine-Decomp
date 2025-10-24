package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.VineChannel;
import java.util.ArrayList;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineChannel$TimeLine$$Parcelable implements Parcelable, ParcelWrapper<VineChannel.TimeLine> {
    public static final VineChannel$TimeLine$$Parcelable$Creator$$54 CREATOR = new VineChannel$TimeLine$$Parcelable$Creator$$54();
    private VineChannel.TimeLine timeLine$$3;

    public VineChannel$TimeLine$$Parcelable(Parcel parcel$$470) {
        VineChannel.TimeLine timeLine$$5;
        if (parcel$$470.readInt() == -1) {
            timeLine$$5 = null;
        } else {
            timeLine$$5 = readco_vine_android_api_VineChannel$TimeLine(parcel$$470);
        }
        this.timeLine$$3 = timeLine$$5;
    }

    public VineChannel$TimeLine$$Parcelable(VineChannel.TimeLine timeLine$$7) {
        this.timeLine$$3 = timeLine$$7;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$471, int flags) {
        if (this.timeLine$$3 == null) {
            parcel$$471.writeInt(-1);
        } else {
            parcel$$471.writeInt(1);
            writeco_vine_android_api_VineChannel$TimeLine(this.timeLine$$3, parcel$$471, flags);
        }
    }

    private VineChannel.TimeLine readco_vine_android_api_VineChannel$TimeLine(Parcel parcel$$472) {
        VineChannel.TimeLine timeLine$$4 = new VineChannel.TimeLine();
        timeLine$$4.items = (ArrayList) parcel$$472.readSerializable();
        return timeLine$$4;
    }

    private void writeco_vine_android_api_VineChannel$TimeLine(VineChannel.TimeLine timeLine$$6, Parcel parcel$$473, int flags$$154) {
        parcel$$473.writeSerializable(timeLine$$6.items);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineChannel.TimeLine getParcel() {
        return this.timeLine$$3;
    }
}
