package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class DeviceData$$Parcelable implements Parcelable, ParcelWrapper<DeviceData> {
    public static final DeviceData$$Parcelable$Creator$$18 CREATOR = new DeviceData$$Parcelable$Creator$$18();
    private DeviceData deviceData$$6;

    public DeviceData$$Parcelable(Parcel parcel$$226) {
        DeviceData deviceData$$8;
        if (parcel$$226.readInt() == -1) {
            deviceData$$8 = null;
        } else {
            deviceData$$8 = readco_vine_android_scribe_model_DeviceData(parcel$$226);
        }
        this.deviceData$$6 = deviceData$$8;
    }

    public DeviceData$$Parcelable(DeviceData deviceData$$10) {
        this.deviceData$$6 = deviceData$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$227, int flags) {
        if (this.deviceData$$6 == null) {
            parcel$$227.writeInt(-1);
        } else {
            parcel$$227.writeInt(1);
            writeco_vine_android_scribe_model_DeviceData(this.deviceData$$6, parcel$$227, flags);
        }
    }

    private DeviceData readco_vine_android_scribe_model_DeviceData(Parcel parcel$$228) {
        GPSData gPSData$$7;
        Boolean boolean$$29;
        ArrayList<String> list$$19;
        Double double$$38;
        Long long$$55;
        MobileRadioDetails mobileRadioDetails$$12;
        Long long$$56;
        Double double$$39;
        DeviceData deviceData$$7 = new DeviceData();
        if (parcel$$228.readInt() == -1) {
            gPSData$$7 = null;
        } else {
            gPSData$$7 = readco_vine_android_scribe_model_GPSData(parcel$$228);
        }
        deviceData$$7.gpsData = gPSData$$7;
        deviceData$$7.orientation = parcel$$228.readString();
        deviceData$$7.os = parcel$$228.readString();
        deviceData$$7.timezone = parcel$$228.readString();
        deviceData$$7.deviceName = parcel$$228.readString();
        int int$$182 = parcel$$228.readInt();
        if (int$$182 < 0) {
            boolean$$29 = null;
        } else {
            boolean$$29 = Boolean.valueOf(parcel$$228.readInt() == 1);
        }
        deviceData$$7.otherAudioIsPlaying = boolean$$29;
        deviceData$$7.manufacturer = parcel$$228.readString();
        int int$$183 = parcel$$228.readInt();
        if (int$$183 < 0) {
            list$$19 = null;
        } else {
            list$$19 = new ArrayList<>();
            for (int int$$184 = 0; int$$184 < int$$183; int$$184++) {
                list$$19.add(parcel$$228.readString());
            }
        }
        deviceData$$7.languageCodes = list$$19;
        int int$$185 = parcel$$228.readInt();
        if (int$$185 < 0) {
            double$$38 = null;
        } else {
            double$$38 = Double.valueOf(parcel$$228.readDouble());
        }
        deviceData$$7.brightness = double$$38;
        deviceData$$7.osVersion = parcel$$228.readString();
        int int$$186 = parcel$$228.readInt();
        if (int$$186 < 0) {
            long$$55 = null;
        } else {
            long$$55 = Long.valueOf(parcel$$228.readLong());
        }
        deviceData$$7.bytesFree = long$$55;
        deviceData$$7.browser = parcel$$228.readString();
        deviceData$$7.browserVersion = parcel$$228.readString();
        deviceData$$7.deviceModel = parcel$$228.readString();
        deviceData$$7.internetAccessType = parcel$$228.readString();
        if (parcel$$228.readInt() == -1) {
            mobileRadioDetails$$12 = null;
        } else {
            mobileRadioDetails$$12 = readco_vine_android_scribe_model_MobileRadioDetails(parcel$$228);
        }
        deviceData$$7.radioDetails = mobileRadioDetails$$12;
        int int$$188 = parcel$$228.readInt();
        if (int$$188 < 0) {
            long$$56 = null;
        } else {
            long$$56 = Long.valueOf(parcel$$228.readLong());
        }
        deviceData$$7.bytesAvailable = long$$56;
        int int$$189 = parcel$$228.readInt();
        if (int$$189 < 0) {
            double$$39 = null;
        } else {
            double$$39 = Double.valueOf(parcel$$228.readDouble());
        }
        deviceData$$7.batteryLevel = double$$39;
        return deviceData$$7;
    }

    private GPSData readco_vine_android_scribe_model_GPSData(Parcel parcel$$229) {
        Double double$$33;
        Double double$$34;
        Double double$$35;
        Double double$$36;
        Double double$$37;
        GPSData gPSData$$6 = new GPSData();
        int int$$177 = parcel$$229.readInt();
        if (int$$177 < 0) {
            double$$33 = null;
        } else {
            double$$33 = Double.valueOf(parcel$$229.readDouble());
        }
        gPSData$$6.altitude = double$$33;
        int int$$178 = parcel$$229.readInt();
        if (int$$178 < 0) {
            double$$34 = null;
        } else {
            double$$34 = Double.valueOf(parcel$$229.readDouble());
        }
        gPSData$$6.verticalAccuracy = double$$34;
        int int$$179 = parcel$$229.readInt();
        if (int$$179 < 0) {
            double$$35 = null;
        } else {
            double$$35 = Double.valueOf(parcel$$229.readDouble());
        }
        gPSData$$6.latitude = double$$35;
        int int$$180 = parcel$$229.readInt();
        if (int$$180 < 0) {
            double$$36 = null;
        } else {
            double$$36 = Double.valueOf(parcel$$229.readDouble());
        }
        gPSData$$6.horizontalAccuracy = double$$36;
        int int$$181 = parcel$$229.readInt();
        if (int$$181 < 0) {
            double$$37 = null;
        } else {
            double$$37 = Double.valueOf(parcel$$229.readDouble());
        }
        gPSData$$6.longitude = double$$37;
        return gPSData$$6;
    }

    private MobileRadioDetails readco_vine_android_scribe_model_MobileRadioDetails(Parcel parcel$$230) {
        Integer integer$$18;
        MobileRadioDetails mobileRadioDetails$$11 = new MobileRadioDetails();
        mobileRadioDetails$$11.mobileNetworkOperatorName = parcel$$230.readString();
        mobileRadioDetails$$11.mobileNetworkOperatorCountryCode = parcel$$230.readString();
        mobileRadioDetails$$11.mobileSimProviderName = parcel$$230.readString();
        mobileRadioDetails$$11.radioStatus = parcel$$230.readString();
        int int$$187 = parcel$$230.readInt();
        if (int$$187 < 0) {
            integer$$18 = null;
        } else {
            integer$$18 = Integer.valueOf(parcel$$230.readInt());
        }
        mobileRadioDetails$$11.signalStrength = integer$$18;
        mobileRadioDetails$$11.mobileNetworkOperatorCode = parcel$$230.readString();
        mobileRadioDetails$$11.mobileSimProviderIsoCountryCode = parcel$$230.readString();
        mobileRadioDetails$$11.mobileSimProviderCode = parcel$$230.readString();
        mobileRadioDetails$$11.mobileNetworkOperatorIsoCountryCode = parcel$$230.readString();
        return mobileRadioDetails$$11;
    }

    private void writeco_vine_android_scribe_model_DeviceData(DeviceData deviceData$$9, Parcel parcel$$231, int flags$$86) {
        if (deviceData$$9.gpsData == null) {
            parcel$$231.writeInt(-1);
        } else {
            parcel$$231.writeInt(1);
            writeco_vine_android_scribe_model_GPSData(deviceData$$9.gpsData, parcel$$231, flags$$86);
        }
        parcel$$231.writeString(deviceData$$9.orientation);
        parcel$$231.writeString(deviceData$$9.os);
        parcel$$231.writeString(deviceData$$9.timezone);
        parcel$$231.writeString(deviceData$$9.deviceName);
        if (deviceData$$9.otherAudioIsPlaying == null) {
            parcel$$231.writeInt(-1);
        } else {
            parcel$$231.writeInt(1);
            parcel$$231.writeInt(deviceData$$9.otherAudioIsPlaying.booleanValue() ? 1 : 0);
        }
        parcel$$231.writeString(deviceData$$9.manufacturer);
        if (deviceData$$9.languageCodes == null) {
            parcel$$231.writeInt(-1);
        } else {
            parcel$$231.writeInt(deviceData$$9.languageCodes.size());
            for (String string$$4 : deviceData$$9.languageCodes) {
                parcel$$231.writeString(string$$4);
            }
        }
        if (deviceData$$9.brightness == null) {
            parcel$$231.writeInt(-1);
        } else {
            parcel$$231.writeInt(1);
            parcel$$231.writeDouble(deviceData$$9.brightness.doubleValue());
        }
        parcel$$231.writeString(deviceData$$9.osVersion);
        if (deviceData$$9.bytesFree == null) {
            parcel$$231.writeInt(-1);
        } else {
            parcel$$231.writeInt(1);
            parcel$$231.writeLong(deviceData$$9.bytesFree.longValue());
        }
        parcel$$231.writeString(deviceData$$9.browser);
        parcel$$231.writeString(deviceData$$9.browserVersion);
        parcel$$231.writeString(deviceData$$9.deviceModel);
        parcel$$231.writeString(deviceData$$9.internetAccessType);
        if (deviceData$$9.radioDetails == null) {
            parcel$$231.writeInt(-1);
        } else {
            parcel$$231.writeInt(1);
            writeco_vine_android_scribe_model_MobileRadioDetails(deviceData$$9.radioDetails, parcel$$231, flags$$86);
        }
        if (deviceData$$9.bytesAvailable == null) {
            parcel$$231.writeInt(-1);
        } else {
            parcel$$231.writeInt(1);
            parcel$$231.writeLong(deviceData$$9.bytesAvailable.longValue());
        }
        if (deviceData$$9.batteryLevel == null) {
            parcel$$231.writeInt(-1);
        } else {
            parcel$$231.writeInt(1);
            parcel$$231.writeDouble(deviceData$$9.batteryLevel.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_GPSData(GPSData gPSData$$8, Parcel parcel$$232, int flags$$87) {
        if (gPSData$$8.altitude == null) {
            parcel$$232.writeInt(-1);
        } else {
            parcel$$232.writeInt(1);
            parcel$$232.writeDouble(gPSData$$8.altitude.doubleValue());
        }
        if (gPSData$$8.verticalAccuracy == null) {
            parcel$$232.writeInt(-1);
        } else {
            parcel$$232.writeInt(1);
            parcel$$232.writeDouble(gPSData$$8.verticalAccuracy.doubleValue());
        }
        if (gPSData$$8.latitude == null) {
            parcel$$232.writeInt(-1);
        } else {
            parcel$$232.writeInt(1);
            parcel$$232.writeDouble(gPSData$$8.latitude.doubleValue());
        }
        if (gPSData$$8.horizontalAccuracy == null) {
            parcel$$232.writeInt(-1);
        } else {
            parcel$$232.writeInt(1);
            parcel$$232.writeDouble(gPSData$$8.horizontalAccuracy.doubleValue());
        }
        if (gPSData$$8.longitude == null) {
            parcel$$232.writeInt(-1);
        } else {
            parcel$$232.writeInt(1);
            parcel$$232.writeDouble(gPSData$$8.longitude.doubleValue());
        }
    }

    private void writeco_vine_android_scribe_model_MobileRadioDetails(MobileRadioDetails mobileRadioDetails$$13, Parcel parcel$$233, int flags$$88) {
        parcel$$233.writeString(mobileRadioDetails$$13.mobileNetworkOperatorName);
        parcel$$233.writeString(mobileRadioDetails$$13.mobileNetworkOperatorCountryCode);
        parcel$$233.writeString(mobileRadioDetails$$13.mobileSimProviderName);
        parcel$$233.writeString(mobileRadioDetails$$13.radioStatus);
        if (mobileRadioDetails$$13.signalStrength == null) {
            parcel$$233.writeInt(-1);
        } else {
            parcel$$233.writeInt(1);
            parcel$$233.writeInt(mobileRadioDetails$$13.signalStrength.intValue());
        }
        parcel$$233.writeString(mobileRadioDetails$$13.mobileNetworkOperatorCode);
        parcel$$233.writeString(mobileRadioDetails$$13.mobileSimProviderIsoCountryCode);
        parcel$$233.writeString(mobileRadioDetails$$13.mobileSimProviderCode);
        parcel$$233.writeString(mobileRadioDetails$$13.mobileNetworkOperatorIsoCountryCode);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public DeviceData getParcel() {
        return this.deviceData$$6;
    }
}
