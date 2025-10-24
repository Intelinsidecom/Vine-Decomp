package com.tune.crosspromo;

import com.mobileapptracker.MATGender;
import java.util.Date;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class TuneAdParams {
    public int adHeightLandscape;
    public int adHeightPortrait;
    public int adWidthLandscape;
    public int adWidthPortrait;
    private String advertiserId;
    private String altitude;
    private String androidId;
    private String appName;
    private String appVersion;
    private Date birthDate;
    private String connectionType;
    private String countryCode;
    private String currentOrientation;
    private JSONObject customTargets;
    public boolean debugMode;
    private String deviceBrand;
    private String deviceCarrier;
    private String deviceCpuType;
    private String deviceModel;
    private String facebookUserId;
    private MATGender gender;
    private String googleAdId;
    private boolean googleIsLATEnabled;
    private String googleUserId;
    private String installDate;
    private String installReferrer;
    private String installer;
    private String keyCheck;
    private Set<String> keywords;
    private String language;
    private String lastOpenLogId;
    private String latitude;
    private String longitude;
    private TuneAdOrientation mOrientation;
    private String mPlacement;
    private String matId;
    private String mcc;
    private String mnc;
    private String osVersion;
    private String packageName;
    private boolean payingUser;
    private String pluginName;
    private String referralSource;
    private String referralUrl;
    private JSONObject refs;
    private float screenDensity;
    private int screenHeight;
    private int screenWidth;
    private String sdkVersion;
    private String timeZone;
    private String twitterUserId;
    private String userAgent;
    private String userEmailMd5;
    private String userEmailSha1;
    private String userEmailSha256;
    private String userId;
    private String userNameMd5;
    private String userNameSha1;
    private String userNameSha256;
    private String userPhoneMd5;
    private String userPhoneSha1;
    private String userPhoneSha256;

    public JSONObject toJSON() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject app = new JSONObject().put("advertiserId", this.advertiserId).put("keyCheck", this.keyCheck).put("name", this.appName).put("version", this.appVersion).put("installDate", this.installDate).put("installReferrer", this.installReferrer).put("installer", this.installer).put("referralSource", this.referralSource).put("referralUrl", this.referralUrl).put("package", this.packageName);
            JSONObject device = new JSONObject().put("altitude", this.altitude).put("connectionType", this.connectionType).put("country", this.countryCode).put("deviceBrand", this.deviceBrand).put("deviceCarrier", this.deviceCarrier).put("deviceCpuType", this.deviceCpuType).put("deviceModel", this.deviceModel).put("language", this.language).put("latitude", this.latitude).put("longitude", this.longitude).put("mcc", this.mcc).put("mnc", this.mnc).put("os", "Android").put("osVersion", this.osVersion).put("timezone", this.timeZone).put("userAgent", this.userAgent);
            JSONObject ids = new JSONObject();
            ids.put("androidId", this.androidId);
            ids.put("gaid", this.googleAdId);
            ids.put("googleAdTrackingDisabled", this.googleIsLATEnabled);
            ids.put("matId", this.matId);
            JSONObject screen = new JSONObject().put("density", this.screenDensity).put("height", this.screenHeight).put("width", this.screenWidth);
            JSONObject sizes = new JSONObject();
            if (this.mOrientation.equals(TuneAdOrientation.ALL)) {
                JSONObject portrait = new JSONObject().put("width", this.adWidthPortrait).put("height", this.adHeightPortrait);
                JSONObject landscape = new JSONObject().put("width", this.adWidthLandscape).put("height", this.adHeightLandscape);
                sizes.put("portrait", portrait).put("landscape", landscape);
            } else if (this.mOrientation.equals(TuneAdOrientation.PORTRAIT_ONLY)) {
                JSONObject portrait2 = new JSONObject().put("width", this.adWidthPortrait).put("height", this.adHeightPortrait);
                sizes.put("portrait", portrait2);
            } else if (this.mOrientation.equals(TuneAdOrientation.LANDSCAPE_ONLY)) {
                JSONObject landscape2 = new JSONObject().put("width", this.adWidthLandscape).put("height", this.adHeightLandscape);
                sizes.put("landscape", landscape2);
            }
            JSONObject user = new JSONObject();
            if (this.birthDate != null) {
                user.put("birthDate", Long.toString(this.birthDate.getTime() / 1000));
            }
            user.put("facebookUserId", this.facebookUserId);
            user.put("gender", this.gender);
            user.put("googleUserId", this.googleUserId);
            if (this.keywords != null) {
                JSONArray keywordArr = new JSONArray();
                for (String keyword : this.keywords) {
                    keywordArr.put(keyword);
                }
                user.put("keywords", keywordArr);
            }
            user.put("payingUser", this.payingUser);
            user.put("twitterUserId", this.twitterUserId);
            user.put("userEmailMd5", this.userEmailMd5);
            user.put("userEmailSha1", this.userEmailSha1);
            user.put("userEmailSha256", this.userEmailSha256);
            if (this.userId != null && this.userId.length() != 0) {
                user.put("userId", this.userId);
            }
            user.put("userNameMd5", this.userNameMd5);
            user.put("userNameSha1", this.userNameSha1);
            user.put("userNameSha256", this.userNameSha256);
            user.put("userPhoneMd5", this.userPhoneMd5);
            user.put("userPhoneSha1", this.userPhoneSha1);
            user.put("userPhoneSha256", this.userPhoneSha256);
            jSONObject.put("currentOrientation", this.currentOrientation);
            jSONObject.put("debugMode", this.debugMode);
            jSONObject.put("sdkVersion", this.sdkVersion);
            jSONObject.put("plugin", this.pluginName);
            jSONObject.put("lastOpenLogId", this.lastOpenLogId);
            jSONObject.put("app", app);
            jSONObject.put("device", device);
            jSONObject.put("ids", ids);
            jSONObject.put("screen", screen);
            jSONObject.put("sizes", sizes);
            jSONObject.put(PropertyConfiguration.USER, user);
            jSONObject.put("targets", this.customTargets);
            jSONObject.put("refs", this.refs);
            jSONObject.put("placement", this.mPlacement);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}
