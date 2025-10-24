package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class HTTPPerformanceData$$Parcelable implements Parcelable, ParcelWrapper<HTTPPerformanceData> {
    public static final HTTPPerformanceData$$Parcelable$Creator$$26 CREATOR = new HTTPPerformanceData$$Parcelable$Creator$$26();
    private HTTPPerformanceData hTTPPerformanceData$$9;

    public HTTPPerformanceData$$Parcelable(Parcel parcel$$314) {
        HTTPPerformanceData hTTPPerformanceData$$11;
        if (parcel$$314.readInt() == -1) {
            hTTPPerformanceData$$11 = null;
        } else {
            hTTPPerformanceData$$11 = readco_vine_android_scribe_model_HTTPPerformanceData(parcel$$314);
        }
        this.hTTPPerformanceData$$9 = hTTPPerformanceData$$11;
    }

    public HTTPPerformanceData$$Parcelable(HTTPPerformanceData hTTPPerformanceData$$13) {
        this.hTTPPerformanceData$$9 = hTTPPerformanceData$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$315, int flags) {
        if (this.hTTPPerformanceData$$9 == null) {
            parcel$$315.writeInt(-1);
        } else {
            parcel$$315.writeInt(1);
            writeco_vine_android_scribe_model_HTTPPerformanceData(this.hTTPPerformanceData$$9, parcel$$315, flags);
        }
    }

    private HTTPPerformanceData readco_vine_android_scribe_model_HTTPPerformanceData(Parcel parcel$$316) {
        Double double$$52;
        Long long$$83;
        Long long$$84;
        Double double$$53;
        Double double$$54;
        Double double$$55;
        HTTPPerformanceData hTTPPerformanceData$$10 = new HTTPPerformanceData();
        int int$$268 = parcel$$316.readInt();
        if (int$$268 < 0) {
            double$$52 = null;
        } else {
            double$$52 = Double.valueOf(parcel$$316.readDouble());
        }
        hTTPPerformanceData$$10.duration = double$$52;
        int int$$269 = parcel$$316.readInt();
        if (int$$269 < 0) {
            long$$83 = null;
        } else {
            long$$83 = Long.valueOf(parcel$$316.readLong());
        }
        hTTPPerformanceData$$10.bytesReceived = long$$83;
        int int$$270 = parcel$$316.readInt();
        if (int$$270 < 0) {
            long$$84 = null;
        } else {
            long$$84 = Long.valueOf(parcel$$316.readLong());
        }
        hTTPPerformanceData$$10.bytesSent = long$$84;
        int int$$271 = parcel$$316.readInt();
        if (int$$271 < 0) {
            double$$53 = null;
        } else {
            double$$53 = Double.valueOf(parcel$$316.readDouble());
        }
        hTTPPerformanceData$$10.durationToRequestSent = double$$53;
        int int$$272 = parcel$$316.readInt();
        if (int$$272 < 0) {
            double$$54 = null;
        } else {
            double$$54 = Double.valueOf(parcel$$316.readDouble());
        }
        hTTPPerformanceData$$10.startTimestamp = double$$54;
        int int$$273 = parcel$$316.readInt();
        if (int$$273 < 0) {
            double$$55 = null;
        } else {
            double$$55 = Double.valueOf(parcel$$316.readDouble());
        }
        hTTPPerformanceData$$10.durationToFirstByte = double$$55;
        return hTTPPerformanceData$$10;
    }

    private void writeco_vine_android_scribe_model_HTTPPerformanceData(HTTPPerformanceData hTTPPerformanceData$$12, Parcel parcel$$317, int flags$$118) {
        if (hTTPPerformanceData$$12.duration == null) {
            parcel$$317.writeInt(-1);
        } else {
            parcel$$317.writeInt(1);
            parcel$$317.writeDouble(hTTPPerformanceData$$12.duration.doubleValue());
        }
        if (hTTPPerformanceData$$12.bytesReceived == null) {
            parcel$$317.writeInt(-1);
        } else {
            parcel$$317.writeInt(1);
            parcel$$317.writeLong(hTTPPerformanceData$$12.bytesReceived.longValue());
        }
        if (hTTPPerformanceData$$12.bytesSent == null) {
            parcel$$317.writeInt(-1);
        } else {
            parcel$$317.writeInt(1);
            parcel$$317.writeLong(hTTPPerformanceData$$12.bytesSent.longValue());
        }
        if (hTTPPerformanceData$$12.durationToRequestSent == null) {
            parcel$$317.writeInt(-1);
        } else {
            parcel$$317.writeInt(1);
            parcel$$317.writeDouble(hTTPPerformanceData$$12.durationToRequestSent.doubleValue());
        }
        if (hTTPPerformanceData$$12.startTimestamp == null) {
            parcel$$317.writeInt(-1);
        } else {
            parcel$$317.writeInt(1);
            parcel$$317.writeDouble(hTTPPerformanceData$$12.startTimestamp.doubleValue());
        }
        if (hTTPPerformanceData$$12.durationToFirstByte == null) {
            parcel$$317.writeInt(-1);
        } else {
            parcel$$317.writeInt(1);
            parcel$$317.writeDouble(hTTPPerformanceData$$12.durationToFirstByte.doubleValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public HTTPPerformanceData getParcel() {
        return this.hTTPPerformanceData$$9;
    }
}
