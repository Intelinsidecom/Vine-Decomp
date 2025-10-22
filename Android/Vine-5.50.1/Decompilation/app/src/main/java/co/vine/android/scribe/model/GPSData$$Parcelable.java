package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class GPSData$$Parcelable implements Parcelable, ParcelWrapper<GPSData> {
    public static final GPSData$$Parcelable$Creator$$22 CREATOR = new GPSData$$Parcelable$Creator$$22();
    private GPSData gPSData$$9;

    public GPSData$$Parcelable(Parcel parcel$$252) {
        GPSData gPSData$$11;
        if (parcel$$252.readInt() == -1) {
            gPSData$$11 = null;
        } else {
            gPSData$$11 = readco_vine_android_scribe_model_GPSData(parcel$$252);
        }
        this.gPSData$$9 = gPSData$$11;
    }

    public GPSData$$Parcelable(GPSData gPSData$$13) {
        this.gPSData$$9 = gPSData$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$253, int flags) {
        if (this.gPSData$$9 == null) {
            parcel$$253.writeInt(-1);
        } else {
            parcel$$253.writeInt(1);
            writeco_vine_android_scribe_model_GPSData(this.gPSData$$9, parcel$$253, flags);
        }
    }

    private GPSData readco_vine_android_scribe_model_GPSData(Parcel parcel$$254) {
        Double double$$40;
        Double double$$41;
        Double double$$42;
        Double double$$43;
        Double double$$44;
        GPSData gPSData$$10 = new GPSData();
        int int$$206 = parcel$$254.readInt();
        if (int$$206 < 0) {
            double$$40 = null;
        } else {
            double$$40 = Double.valueOf(parcel$$254.readDouble());
        }
        gPSData$$10.altitude = double$$40;
        int int$$207 = parcel$$254.readInt();
        if (int$$207 < 0) {
            double$$41 = null;
        } else {
            double$$41 = Double.valueOf(parcel$$254.readDouble());
        }
        gPSData$$10.verticalAccuracy = double$$41;
        int int$$208 = parcel$$254.readInt();
        if (int$$208 < 0) {
            double$$42 = null;
        } else {
            double$$42 = Double.valueOf(parcel$$254.readDouble());
        }
        gPSData$$10.latitude = double$$42;
        int int$$209 = parcel$$254.readInt();
        if (int$$209 < 0) {
            double$$43 = null;
        } else {
            double$$43 = Double.valueOf(parcel$$254.readDouble());
        }
        gPSData$$10.horizontalAccuracy = double$$43;
        int int$$210 = parcel$$254.readInt();
        if (int$$210 < 0) {
            double$$44 = null;
        } else {
            double$$44 = Double.valueOf(parcel$$254.readDouble());
        }
        gPSData$$10.longitude = double$$44;
        return gPSData$$10;
    }

    private void writeco_vine_android_scribe_model_GPSData(GPSData gPSData$$12, Parcel parcel$$255, int flags$$93) {
        if (gPSData$$12.altitude == null) {
            parcel$$255.writeInt(-1);
        } else {
            parcel$$255.writeInt(1);
            parcel$$255.writeDouble(gPSData$$12.altitude.doubleValue());
        }
        if (gPSData$$12.verticalAccuracy == null) {
            parcel$$255.writeInt(-1);
        } else {
            parcel$$255.writeInt(1);
            parcel$$255.writeDouble(gPSData$$12.verticalAccuracy.doubleValue());
        }
        if (gPSData$$12.latitude == null) {
            parcel$$255.writeInt(-1);
        } else {
            parcel$$255.writeInt(1);
            parcel$$255.writeDouble(gPSData$$12.latitude.doubleValue());
        }
        if (gPSData$$12.horizontalAccuracy == null) {
            parcel$$255.writeInt(-1);
        } else {
            parcel$$255.writeInt(1);
            parcel$$255.writeDouble(gPSData$$12.horizontalAccuracy.doubleValue());
        }
        if (gPSData$$12.longitude == null) {
            parcel$$255.writeInt(-1);
        } else {
            parcel$$255.writeInt(1);
            parcel$$255.writeDouble(gPSData$$12.longitude.doubleValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public GPSData getParcel() {
        return this.gPSData$$9;
    }
}
