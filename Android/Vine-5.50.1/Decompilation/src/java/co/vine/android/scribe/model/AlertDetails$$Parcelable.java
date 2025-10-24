package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class AlertDetails$$Parcelable implements Parcelable, ParcelWrapper<AlertDetails> {
    public static final AlertDetails$$Parcelable$Creator$$9 CREATOR = new AlertDetails$$Parcelable$Creator$$9();
    private AlertDetails alertDetails$$6;

    public AlertDetails$$Parcelable(Parcel parcel$$175) {
        AlertDetails alertDetails$$8;
        if (parcel$$175.readInt() == -1) {
            alertDetails$$8 = null;
        } else {
            alertDetails$$8 = readco_vine_android_scribe_model_AlertDetails(parcel$$175);
        }
        this.alertDetails$$6 = alertDetails$$8;
    }

    public AlertDetails$$Parcelable(AlertDetails alertDetails$$10) {
        this.alertDetails$$6 = alertDetails$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$176, int flags) {
        if (this.alertDetails$$6 == null) {
            parcel$$176.writeInt(-1);
        } else {
            parcel$$176.writeInt(1);
            writeco_vine_android_scribe_model_AlertDetails(this.alertDetails$$6, parcel$$176, flags);
        }
    }

    private AlertDetails readco_vine_android_scribe_model_AlertDetails(Parcel parcel$$177) {
        AlertDetails alertDetails$$7 = new AlertDetails();
        alertDetails$$7.name = parcel$$177.readString();
        alertDetails$$7.action = parcel$$177.readString();
        return alertDetails$$7;
    }

    private void writeco_vine_android_scribe_model_AlertDetails(AlertDetails alertDetails$$9, Parcel parcel$$178, int flags$$74) {
        parcel$$178.writeString(alertDetails$$9.name);
        parcel$$178.writeString(alertDetails$$9.action);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public AlertDetails getParcel() {
        return this.alertDetails$$6;
    }
}
