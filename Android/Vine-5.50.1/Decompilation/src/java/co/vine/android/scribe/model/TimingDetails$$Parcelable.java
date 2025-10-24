package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class TimingDetails$$Parcelable implements Parcelable, ParcelWrapper<TimingDetails> {
    public static final TimingDetails$$Parcelable$Creator$$6 CREATOR = new TimingDetails$$Parcelable$Creator$$6();
    private TimingDetails timingDetails$$6;

    public TimingDetails$$Parcelable(Parcel parcel$$158) {
        TimingDetails timingDetails$$8;
        if (parcel$$158.readInt() == -1) {
            timingDetails$$8 = null;
        } else {
            timingDetails$$8 = readco_vine_android_scribe_model_TimingDetails(parcel$$158);
        }
        this.timingDetails$$6 = timingDetails$$8;
    }

    public TimingDetails$$Parcelable(TimingDetails timingDetails$$10) {
        this.timingDetails$$6 = timingDetails$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$159, int flags) {
        if (this.timingDetails$$6 == null) {
            parcel$$159.writeInt(-1);
        } else {
            parcel$$159.writeInt(1);
            writeco_vine_android_scribe_model_TimingDetails(this.timingDetails$$6, parcel$$159, flags);
        }
    }

    private TimingDetails readco_vine_android_scribe_model_TimingDetails(Parcel parcel$$160) {
        Double double$$30;
        Double double$$31;
        TimingDetails timingDetails$$7 = new TimingDetails();
        int int$$156 = parcel$$160.readInt();
        if (int$$156 < 0) {
            double$$30 = null;
        } else {
            double$$30 = Double.valueOf(parcel$$160.readDouble());
        }
        timingDetails$$7.duration = double$$30;
        int int$$157 = parcel$$160.readInt();
        if (int$$157 < 0) {
            double$$31 = null;
        } else {
            double$$31 = Double.valueOf(parcel$$160.readDouble());
        }
        timingDetails$$7.startTimestamp = double$$31;
        return timingDetails$$7;
    }

    private void writeco_vine_android_scribe_model_TimingDetails(TimingDetails timingDetails$$9, Parcel parcel$$161, int flags$$70) {
        if (timingDetails$$9.duration == null) {
            parcel$$161.writeInt(-1);
        } else {
            parcel$$161.writeInt(1);
            parcel$$161.writeDouble(timingDetails$$9.duration.doubleValue());
        }
        if (timingDetails$$9.startTimestamp == null) {
            parcel$$161.writeInt(-1);
        } else {
            parcel$$161.writeInt(1);
            parcel$$161.writeDouble(timingDetails$$9.startTimestamp.doubleValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public TimingDetails getParcel() {
        return this.timingDetails$$6;
    }
}
