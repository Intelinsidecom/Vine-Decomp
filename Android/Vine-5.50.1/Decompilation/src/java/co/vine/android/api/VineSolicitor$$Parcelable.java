package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineSolicitor$$Parcelable implements Parcelable, ParcelWrapper<VineSolicitor> {
    public static final VineSolicitor$$Parcelable$Creator$$40 CREATOR = new VineSolicitor$$Parcelable$Creator$$40();
    private VineSolicitor vineSolicitor$$3;

    public VineSolicitor$$Parcelable(Parcel parcel$$396) {
        VineSolicitor vineSolicitor$$5;
        if (parcel$$396.readInt() == -1) {
            vineSolicitor$$5 = null;
        } else {
            vineSolicitor$$5 = readco_vine_android_api_VineSolicitor(parcel$$396);
        }
        this.vineSolicitor$$3 = vineSolicitor$$5;
    }

    public VineSolicitor$$Parcelable(VineSolicitor vineSolicitor$$7) {
        this.vineSolicitor$$3 = vineSolicitor$$7;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$397, int flags) {
        if (this.vineSolicitor$$3 == null) {
            parcel$$397.writeInt(-1);
        } else {
            parcel$$397.writeInt(1);
            writeco_vine_android_api_VineSolicitor(this.vineSolicitor$$3, parcel$$397, flags);
        }
    }

    private VineSolicitor readco_vine_android_api_VineSolicitor(Parcel parcel$$398) {
        VineSolicitor vineSolicitor$$4 = new VineSolicitor();
        vineSolicitor$$4.reference = parcel$$398.readString();
        vineSolicitor$$4.buttonText = parcel$$398.readString();
        vineSolicitor$$4.closeable = parcel$$398.readInt() == 1;
        vineSolicitor$$4.originUrl = parcel$$398.readString();
        vineSolicitor$$4.description = parcel$$398.readString();
        vineSolicitor$$4.completeTitle = parcel$$398.readString();
        vineSolicitor$$4.type = parcel$$398.readString();
        vineSolicitor$$4.title = parcel$$398.readString();
        vineSolicitor$$4.dismissText = parcel$$398.readString();
        vineSolicitor$$4.completeDescription = parcel$$398.readString();
        vineSolicitor$$4.completeButton = parcel$$398.readString();
        vineSolicitor$$4.completeExplanation = parcel$$398.readString();
        return vineSolicitor$$4;
    }

    private void writeco_vine_android_api_VineSolicitor(VineSolicitor vineSolicitor$$6, Parcel parcel$$399, int flags$$138) {
        parcel$$399.writeString(vineSolicitor$$6.reference);
        parcel$$399.writeString(vineSolicitor$$6.buttonText);
        parcel$$399.writeInt(vineSolicitor$$6.closeable ? 1 : 0);
        parcel$$399.writeString(vineSolicitor$$6.originUrl);
        parcel$$399.writeString(vineSolicitor$$6.description);
        parcel$$399.writeString(vineSolicitor$$6.completeTitle);
        parcel$$399.writeString(vineSolicitor$$6.type);
        parcel$$399.writeString(vineSolicitor$$6.title);
        parcel$$399.writeString(vineSolicitor$$6.dismissText);
        parcel$$399.writeString(vineSolicitor$$6.completeDescription);
        parcel$$399.writeString(vineSolicitor$$6.completeButton);
        parcel$$399.writeString(vineSolicitor$$6.completeExplanation);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineSolicitor getParcel() {
        return this.vineSolicitor$$3;
    }
}
