package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class ExperimentValue$$Parcelable implements Parcelable, ParcelWrapper<ExperimentValue> {
    public static final ExperimentValue$$Parcelable$Creator$$13 CREATOR = new ExperimentValue$$Parcelable$Creator$$13();
    private ExperimentValue experimentValue$$12;

    public ExperimentValue$$Parcelable(Parcel parcel$$197) {
        ExperimentValue experimentValue$$14;
        if (parcel$$197.readInt() == -1) {
            experimentValue$$14 = null;
        } else {
            experimentValue$$14 = readco_vine_android_scribe_model_ExperimentValue(parcel$$197);
        }
        this.experimentValue$$12 = experimentValue$$14;
    }

    public ExperimentValue$$Parcelable(ExperimentValue experimentValue$$16) {
        this.experimentValue$$12 = experimentValue$$16;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$198, int flags) {
        if (this.experimentValue$$12 == null) {
            parcel$$198.writeInt(-1);
        } else {
            parcel$$198.writeInt(1);
            writeco_vine_android_scribe_model_ExperimentValue(this.experimentValue$$12, parcel$$198, flags);
        }
    }

    private ExperimentValue readco_vine_android_scribe_model_ExperimentValue(Parcel parcel$$199) {
        ExperimentValue experimentValue$$13 = new ExperimentValue();
        experimentValue$$13.value = parcel$$199.readString();
        experimentValue$$13.key = parcel$$199.readString();
        return experimentValue$$13;
    }

    private void writeco_vine_android_scribe_model_ExperimentValue(ExperimentValue experimentValue$$15, Parcel parcel$$200, int flags$$79) {
        parcel$$200.writeString(experimentValue$$15.value);
        parcel$$200.writeString(experimentValue$$15.key);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public ExperimentValue getParcel() {
        return this.experimentValue$$12;
    }
}
