package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class MosaicDetails$$Parcelable implements Parcelable, ParcelWrapper<MosaicDetails> {
    public static final MosaicDetails$$Parcelable$Creator$$42 CREATOR = new MosaicDetails$$Parcelable$Creator$$42();
    private MosaicDetails mosaicDetails$$16;

    public MosaicDetails$$Parcelable(Parcel parcel$$406) {
        MosaicDetails mosaicDetails$$18;
        if (parcel$$406.readInt() == -1) {
            mosaicDetails$$18 = null;
        } else {
            mosaicDetails$$18 = readco_vine_android_scribe_model_MosaicDetails(parcel$$406);
        }
        this.mosaicDetails$$16 = mosaicDetails$$18;
    }

    public MosaicDetails$$Parcelable(MosaicDetails mosaicDetails$$20) {
        this.mosaicDetails$$16 = mosaicDetails$$20;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$407, int flags) {
        if (this.mosaicDetails$$16 == null) {
            parcel$$407.writeInt(-1);
        } else {
            parcel$$407.writeInt(1);
            writeco_vine_android_scribe_model_MosaicDetails(this.mosaicDetails$$16, parcel$$407, flags);
        }
    }

    private MosaicDetails readco_vine_android_scribe_model_MosaicDetails(Parcel parcel$$408) {
        MosaicDetails mosaicDetails$$17 = new MosaicDetails();
        mosaicDetails$$17.mosaicType = parcel$$408.readString();
        mosaicDetails$$17.link = parcel$$408.readString();
        return mosaicDetails$$17;
    }

    private void writeco_vine_android_scribe_model_MosaicDetails(MosaicDetails mosaicDetails$$19, Parcel parcel$$409, int flags$$140) {
        parcel$$409.writeString(mosaicDetails$$19.mosaicType);
        parcel$$409.writeString(mosaicDetails$$19.link);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public MosaicDetails getParcel() {
        return this.mosaicDetails$$16;
    }
}
