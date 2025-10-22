package co.vine.android.scribe;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import co.vine.android.scribe.model.DeviceData;
import co.vine.android.scribe.model.MobileRadioDetails;
import java.util.ArrayList;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class DeviceDataUtil {
    private static String getInternetAccessType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return null;
        }
        if (!networkInfo.isConnected()) {
            return DeviceData.InternetAccessType.UNREACHABLE.toString();
        }
        if (networkInfo.getType() == 1) {
            return DeviceData.InternetAccessType.WIFI.toString();
        }
        if (networkInfo.getType() == 0 || networkInfo.getType() == 4 || networkInfo.getType() == 5 || networkInfo.getType() == 2 || networkInfo.getType() == 3) {
            return DeviceData.InternetAccessType.MOBILE.toString();
        }
        if (networkInfo.getType() == 9) {
            return DeviceData.InternetAccessType.HARDLINE.toString();
        }
        return DeviceData.InternetAccessType.OTHER.toString();
    }

    private static Double getBatteryLevel(Context context) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent batteryStatus = context.registerReceiver(null, intentFilter);
        if (batteryStatus == null) {
            return null;
        }
        int level = batteryStatus.getIntExtra("level", -1);
        int scale = batteryStatus.getIntExtra("scale", -1);
        return Double.valueOf(level / scale);
    }

    private static MobileRadioDetails getRadioDetails(Context context) {
        MobileRadioDetails radioDetails = new MobileRadioDetails();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        radioDetails.mobileNetworkOperatorIsoCountryCode = telephonyManager.getNetworkCountryIso();
        radioDetails.mobileNetworkOperatorCode = telephonyManager.getNetworkOperator();
        radioDetails.mobileNetworkOperatorName = telephonyManager.getNetworkOperatorName();
        return radioDetails;
    }

    public static DeviceData getDeviceData(Context context) {
        DeviceData deviceData = new DeviceData();
        Configuration configuration = context.getResources().getConfiguration();
        deviceData.os = "Android OS";
        deviceData.osVersion = Build.VERSION.RELEASE;
        deviceData.manufacturer = Build.MANUFACTURER;
        deviceData.deviceName = Build.DEVICE;
        deviceData.deviceModel = Build.MODEL;
        deviceData.radioDetails = getRadioDetails(context);
        deviceData.internetAccessType = getInternetAccessType(context);
        int orientation = configuration.orientation;
        if (orientation == 2) {
            deviceData.orientation = DeviceData.Orientation.LANDSCAPE.toString();
        } else if (orientation == 1) {
            deviceData.orientation = DeviceData.Orientation.PORTRAIT.toString();
        }
        deviceData.timezone = TimeZone.getDefault().getID();
        deviceData.batteryLevel = getBatteryLevel(context);
        try {
            deviceData.brightness = Double.valueOf(Settings.System.getInt(context.getContentResolver(), "screen_brightness"));
        } catch (Settings.SettingNotFoundException e) {
        }
        deviceData.languageCodes = new ArrayList();
        String currentLanguage = configuration.locale.getLanguage();
        deviceData.languageCodes.add(currentLanguage);
        return deviceData;
    }
}
