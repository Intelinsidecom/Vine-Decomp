package co.vine.android.scribe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.List;

@JsonObject
/* loaded from: classes.dex */
public class DeviceData {

    @JsonField(name = {"battery_level"})
    public Double batteryLevel;

    @JsonField(name = {"brightness"})
    public Double brightness;

    @JsonField(name = {"browser"})
    public String browser;

    @JsonField(name = {"browser_version"})
    public String browserVersion;

    @JsonField(name = {"bytes_available"})
    public Long bytesAvailable;

    @JsonField(name = {"bytes_free"})
    public Long bytesFree;

    @JsonField(name = {"device_model"})
    public String deviceModel;

    @JsonField(name = {"device_name"})
    public String deviceName;

    @JsonField(name = {"location"})
    public GPSData gpsData;

    @JsonField(name = {"internet_access_type"})
    public String internetAccessType;

    @JsonField(name = {"language_codes"})
    public List<String> languageCodes;

    @JsonField(name = {"manufacturer"})
    public String manufacturer;

    @JsonField(name = {"orientation"})
    public String orientation;

    @JsonField(name = {"os"})
    public String os;

    @JsonField(name = {"os_version"})
    public String osVersion;

    @JsonField(name = {"other_audio_is_playing"})
    public Boolean otherAudioIsPlaying;

    @JsonField(name = {"radio_details"})
    public MobileRadioDetails radioDetails;

    @JsonField(name = {"timezone"})
    public String timezone;

    public enum InternetAccessType {
        UNREACHABLE,
        MOBILE,
        WIFI,
        HARDLINE,
        OTHER
    }

    public enum Orientation {
        PORTRAIT,
        LANDSCAPE,
        FACE_UP
    }
}
