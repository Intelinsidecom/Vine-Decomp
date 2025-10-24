package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class MobileRadioDetails$$Parcelable implements Parcelable, ParcelWrapper<MobileRadioDetails> {
    public static final MobileRadioDetails$$Parcelable$Creator$$15 CREATOR = new MobileRadioDetails$$Parcelable$Creator$$15();
    private MobileRadioDetails mobileRadioDetails$$6;

    public MobileRadioDetails$$Parcelable(Parcel parcel$$211) {
        MobileRadioDetails mobileRadioDetails$$8;
        if (parcel$$211.readInt() == -1) {
            mobileRadioDetails$$8 = null;
        } else {
            mobileRadioDetails$$8 = readco_vine_android_scribe_model_MobileRadioDetails(parcel$$211);
        }
        this.mobileRadioDetails$$6 = mobileRadioDetails$$8;
    }

    public MobileRadioDetails$$Parcelable(MobileRadioDetails mobileRadioDetails$$10) {
        this.mobileRadioDetails$$6 = mobileRadioDetails$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$212, int flags) {
        if (this.mobileRadioDetails$$6 == null) {
            parcel$$212.writeInt(-1);
        } else {
            parcel$$212.writeInt(1);
            writeco_vine_android_scribe_model_MobileRadioDetails(this.mobileRadioDetails$$6, parcel$$212, flags);
        }
    }

    private MobileRadioDetails readco_vine_android_scribe_model_MobileRadioDetails(Parcel parcel$$213) {
        Integer integer$$16;
        MobileRadioDetails mobileRadioDetails$$7 = new MobileRadioDetails();
        mobileRadioDetails$$7.mobileNetworkOperatorName = parcel$$213.readString();
        mobileRadioDetails$$7.mobileNetworkOperatorCountryCode = parcel$$213.readString();
        mobileRadioDetails$$7.mobileSimProviderName = parcel$$213.readString();
        mobileRadioDetails$$7.radioStatus = parcel$$213.readString();
        int int$$174 = parcel$$213.readInt();
        if (int$$174 < 0) {
            integer$$16 = null;
        } else {
            integer$$16 = Integer.valueOf(parcel$$213.readInt());
        }
        mobileRadioDetails$$7.signalStrength = integer$$16;
        mobileRadioDetails$$7.mobileNetworkOperatorCode = parcel$$213.readString();
        mobileRadioDetails$$7.mobileSimProviderIsoCountryCode = parcel$$213.readString();
        mobileRadioDetails$$7.mobileSimProviderCode = parcel$$213.readString();
        mobileRadioDetails$$7.mobileNetworkOperatorIsoCountryCode = parcel$$213.readString();
        return mobileRadioDetails$$7;
    }

    private void writeco_vine_android_scribe_model_MobileRadioDetails(MobileRadioDetails mobileRadioDetails$$9, Parcel parcel$$214, int flags$$83) {
        parcel$$214.writeString(mobileRadioDetails$$9.mobileNetworkOperatorName);
        parcel$$214.writeString(mobileRadioDetails$$9.mobileNetworkOperatorCountryCode);
        parcel$$214.writeString(mobileRadioDetails$$9.mobileSimProviderName);
        parcel$$214.writeString(mobileRadioDetails$$9.radioStatus);
        if (mobileRadioDetails$$9.signalStrength == null) {
            parcel$$214.writeInt(-1);
        } else {
            parcel$$214.writeInt(1);
            parcel$$214.writeInt(mobileRadioDetails$$9.signalStrength.intValue());
        }
        parcel$$214.writeString(mobileRadioDetails$$9.mobileNetworkOperatorCode);
        parcel$$214.writeString(mobileRadioDetails$$9.mobileSimProviderIsoCountryCode);
        parcel$$214.writeString(mobileRadioDetails$$9.mobileSimProviderCode);
        parcel$$214.writeString(mobileRadioDetails$$9.mobileNetworkOperatorIsoCountryCode);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public MobileRadioDetails getParcel() {
        return this.mobileRadioDetails$$6;
    }
}
