package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class ExperimentData$$Parcelable implements Parcelable, ParcelWrapper<ExperimentData> {
    public static final ExperimentData$$Parcelable$Creator$$8 CREATOR = new ExperimentData$$Parcelable$Creator$$8();
    private ExperimentData experimentData$$6;

    public ExperimentData$$Parcelable(Parcel parcel$$168) {
        ExperimentData experimentData$$8;
        if (parcel$$168.readInt() == -1) {
            experimentData$$8 = null;
        } else {
            experimentData$$8 = readco_vine_android_scribe_model_ExperimentData(parcel$$168);
        }
        this.experimentData$$6 = experimentData$$8;
    }

    public ExperimentData$$Parcelable(ExperimentData experimentData$$10) {
        this.experimentData$$6 = experimentData$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$169, int flags) {
        if (this.experimentData$$6 == null) {
            parcel$$169.writeInt(-1);
        } else {
            parcel$$169.writeInt(1);
            writeco_vine_android_scribe_model_ExperimentData(this.experimentData$$6, parcel$$169, flags);
        }
    }

    private ExperimentData readco_vine_android_scribe_model_ExperimentData(Parcel parcel$$170) {
        ArrayList<ExperimentValue> list$$17;
        ExperimentValue experimentValue$$9;
        ExperimentData experimentData$$7 = new ExperimentData();
        int int$$158 = parcel$$170.readInt();
        if (int$$158 < 0) {
            list$$17 = null;
        } else {
            list$$17 = new ArrayList<>();
            for (int int$$159 = 0; int$$159 < int$$158; int$$159++) {
                if (parcel$$170.readInt() == -1) {
                    experimentValue$$9 = null;
                } else {
                    experimentValue$$9 = readco_vine_android_scribe_model_ExperimentValue(parcel$$170);
                }
                list$$17.add(experimentValue$$9);
            }
        }
        experimentData$$7.experimentValues = list$$17;
        return experimentData$$7;
    }

    private ExperimentValue readco_vine_android_scribe_model_ExperimentValue(Parcel parcel$$171) {
        ExperimentValue experimentValue$$8 = new ExperimentValue();
        experimentValue$$8.value = parcel$$171.readString();
        experimentValue$$8.key = parcel$$171.readString();
        return experimentValue$$8;
    }

    private void writeco_vine_android_scribe_model_ExperimentData(ExperimentData experimentData$$9, Parcel parcel$$172, int flags$$72) {
        if (experimentData$$9.experimentValues == null) {
            parcel$$172.writeInt(-1);
            return;
        }
        parcel$$172.writeInt(experimentData$$9.experimentValues.size());
        Iterator<ExperimentValue> it = experimentData$$9.experimentValues.iterator();
        while (it.hasNext()) {
            ExperimentValue experimentValue$$10 = it.next();
            if (experimentValue$$10 == null) {
                parcel$$172.writeInt(-1);
            } else {
                parcel$$172.writeInt(1);
                writeco_vine_android_scribe_model_ExperimentValue(experimentValue$$10, parcel$$172, flags$$72);
            }
        }
    }

    private void writeco_vine_android_scribe_model_ExperimentValue(ExperimentValue experimentValue$$11, Parcel parcel$$173, int flags$$73) {
        parcel$$173.writeString(experimentValue$$11.value);
        parcel$$173.writeString(experimentValue$$11.key);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public ExperimentData getParcel() {
        return this.experimentData$$6;
    }
}
