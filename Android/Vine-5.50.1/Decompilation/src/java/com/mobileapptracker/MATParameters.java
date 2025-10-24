package com.mobileapptracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import org.json.JSONArray;

/* loaded from: classes.dex */
public class MATParameters {
    private static MATParameters INSTANCE = null;
    private Context mContext;
    private String mPhoneNumberMd5;
    private String mPhoneNumberSha1;
    private String mPhoneNumberSha256;
    private MobileAppTracker mTune;
    private String mUserEmailMd5;
    private String mUserEmailSha1;
    private String mUserEmailSha256;
    private String mUserNameMd5;
    private String mUserNameSha1;
    private String mUserNameSha256;
    private String mAction = null;
    private String mAdvertiserId = null;
    private String mAge = null;
    private String mAllowDups = null;
    private String mAltitude = null;
    private String mAndroidId = null;
    private String mAndroidIdMd5 = null;
    private String mAndroidIdSha1 = null;
    private String mAndroidIdSha256 = null;
    private String mAppAdTracking = null;
    private String mAppName = null;
    private String mAppVersion = null;
    private String mAppVersionName = null;
    private String mConnectionType = null;
    private String mConversionKey = null;
    private String mCountryCode = null;
    private String mCurrencyCode = null;
    private String mDeviceBrand = null;
    private String mDeviceCarrier = null;
    private String mDeviceCpuType = null;
    private String mDeviceCpuSubtype = null;
    private String mDeviceId = null;
    private String mDeviceModel = null;
    private boolean mDebugMode = false;
    private String mExistingUser = null;
    private String mFbUserId = null;
    private String mGender = null;
    private String mGaid = null;
    private String mGaidLimited = null;
    private String mGgUserId = null;
    private String mInstallDate = null;
    private String mInstallerPackage = null;
    private String mLanguage = null;
    private String mLatitude = null;
    private Location mLocation = null;
    private String mLongitude = null;
    private String mMacAddress = null;
    private String mMCC = null;
    private String mMNC = null;
    private String mOsVersion = null;
    private String mPackageName = null;
    private String mPluginName = null;
    private String mPurchaseStatus = null;
    private String mReferralSource = null;
    private String mReferralUrl = null;
    private String mReferrerDelay = null;
    private String mRefId = null;
    private String mRevenue = null;
    private String mScreenDensity = null;
    private String mScreenHeight = null;
    private String mScreenWidth = null;
    private String mSiteId = null;
    private String mTimeZone = null;
    private String mTrackingId = null;
    private String mTrusteId = null;
    private String mTwUserId = null;
    private String mUserAgent = null;
    private JSONArray mUserEmails = null;

    public static MATParameters init(MobileAppTracker tune, Context context, String advertiserId, String conversionKey) {
        if (INSTANCE == null) {
            INSTANCE = new MATParameters();
            INSTANCE.mTune = tune;
            INSTANCE.mContext = context;
            INSTANCE.populateParams(context, advertiserId, conversionKey);
        }
        return INSTANCE;
    }

    public static MATParameters getInstance() {
        return INSTANCE;
    }

    public void clear() {
        INSTANCE = null;
    }

    @SuppressLint({"NewApi"})
    private synchronized boolean populateParams(Context context, String advertiserId, String conversionKey) {
        boolean z;
        int width;
        int height;
        try {
            setAdvertiserId(advertiserId.trim());
            setConversionKey(conversionKey.trim());
            setCurrencyCode("USD");
            new Thread(new GetGAID(context)).start();
            calculateUserAgent();
            String packageName = context.getPackageName();
            setPackageName(packageName);
            PackageManager pm = context.getPackageManager();
            try {
                ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
                setAppName(pm.getApplicationLabel(ai).toString());
                String appFile = pm.getApplicationInfo(packageName, 0).sourceDir;
                long insdate = new File(appFile).lastModified();
                long installDate = new Date(insdate).getTime() / 1000;
                setInstallDate(Long.toString(installDate));
            } catch (PackageManager.NameNotFoundException e) {
            }
            try {
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                setAppVersion(Integer.toString(pi.versionCode));
                setAppVersionName(pi.versionName);
            } catch (PackageManager.NameNotFoundException e2) {
                setAppVersion("0");
            }
            setInstaller(pm.getInstallerPackageName(packageName));
            setDeviceModel(Build.MODEL);
            setDeviceBrand(Build.MANUFACTURER);
            setDeviceCpuType(System.getProperty("os.arch"));
            setOsVersion(Build.VERSION.RELEASE);
            float density = context.getResources().getDisplayMetrics().density;
            setScreenDensity(Float.toString(density));
            Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point size = new Point();
            if (Build.VERSION.SDK_INT >= 17) {
                display.getRealSize(size);
                width = size.x;
                height = size.y;
            } else if (Build.VERSION.SDK_INT >= 13) {
                display.getSize(size);
                width = size.x;
                height = size.y;
            } else {
                width = display.getWidth();
                height = display.getHeight();
            }
            setScreenWidth(Integer.toString(width));
            setScreenHeight(Integer.toString(height));
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo mWifi = connManager.getNetworkInfo(1);
            if (mWifi.isConnected()) {
                setConnectionType("wifi");
            } else {
                setConnectionType("mobile");
            }
            setLanguage(Locale.getDefault().getLanguage());
            setCountryCode(Locale.getDefault().getCountry());
            setTimeZone(TimeZone.getDefault().getDisplayName(false, 0, Locale.US));
            TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            if (tm != null) {
                if (tm.getNetworkCountryIso() != null) {
                    setCountryCode(tm.getNetworkCountryIso());
                }
                setDeviceCarrier(tm.getNetworkOperatorName());
                String networkOperator = tm.getNetworkOperator();
                if (networkOperator != null) {
                    try {
                        String mcc = networkOperator.substring(0, 3);
                        String mnc = networkOperator.substring(3);
                        setMCC(mcc);
                        setMNC(mnc);
                    } catch (IndexOutOfBoundsException e3) {
                    }
                }
            } else {
                setCountryCode(Locale.getDefault().getCountry());
            }
            String matId = getMatId();
            if (matId == null || matId.length() == 0) {
                setMatId(UUID.randomUUID().toString());
            }
            z = true;
        } catch (Exception e4) {
            Log.d("MobileAppTracker", "MobileAppTracking params initialization failed");
            e4.printStackTrace();
            z = false;
        }
        return z;
    }

    private void calculateUserAgent() {
        String userAgent = System.getProperty("http.agent", "");
        if (!TextUtils.isEmpty(userAgent)) {
            setUserAgent(userAgent);
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new GetWebViewUserAgent(this.mContext));
        }
    }

    private class GetGAID implements Runnable {
        private final WeakReference<Context> weakContext;

        public GetGAID(Context context) {
            this.weakContext = new WeakReference<>(context);
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            try {
                Class[] adIdMethodParams = {Context.class};
                Method adIdMethod = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient").getDeclaredMethod("getAdvertisingIdInfo", Context.class);
                Object adInfo = adIdMethod.invoke(null, this.weakContext.get());
                Method getIdMethod = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info").getDeclaredMethod("getId", new Class[0]);
                String adId = (String) getIdMethod.invoke(adInfo, new Object[0]);
                Method getLATMethod = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info").getDeclaredMethod("isLimitAdTrackingEnabled", new Class[0]);
                boolean isLAT = ((Boolean) getLATMethod.invoke(adInfo, new Object[0])).booleanValue();
                if (MATParameters.this.mTune.params == null) {
                    MATParameters.this.setGoogleAdvertisingId(adId);
                    int intLimit = isLAT ? 1 : 0;
                    MATParameters.this.setGoogleAdTrackingLimited(Integer.toString(intLimit));
                }
                MATParameters.this.mTune.setGoogleAdvertisingId(adId, isLAT);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("MobileAppTracker", "MAT SDK failed to get Google Advertising Id, collecting ANDROID_ID instead");
                if (MATParameters.this.mTune.params == null) {
                    MATParameters.this.setAndroidId(Settings.Secure.getString(this.weakContext.get().getContentResolver(), "android_id"));
                }
                MATParameters.this.mTune.setAndroidId(Settings.Secure.getString(this.weakContext.get().getContentResolver(), "android_id"));
            }
        }
    }

    @SuppressLint({"NewApi"})
    private class GetWebViewUserAgent implements Runnable {
        private final WeakReference<Context> weakContext;

        public GetWebViewUserAgent(Context context) {
            this.weakContext = new WeakReference<>(context);
        }

        @Override // java.lang.Runnable
        public void run() throws ClassNotFoundException {
            try {
                Class.forName("android.os.AsyncTask");
                if (Build.VERSION.SDK_INT < 17) {
                    WebView wv = new WebView(this.weakContext.get());
                    MATParameters.this.setUserAgent(wv.getSettings().getUserAgentString());
                    wv.destroy();
                } else {
                    MATParameters.this.setUserAgent(WebSettings.getDefaultUserAgent(this.weakContext.get()));
                }
            } catch (Exception e) {
            } catch (VerifyError e2) {
            }
        }
    }

    public synchronized String getAction() {
        return this.mAction;
    }

    public synchronized void setAction(String action) {
        this.mAction = action;
    }

    public synchronized String getAdvertiserId() {
        return this.mAdvertiserId;
    }

    public synchronized void setAdvertiserId(String advertiserId) {
        this.mAdvertiserId = advertiserId;
    }

    public synchronized String getAge() {
        return this.mAge;
    }

    public synchronized void setAge(String age) {
        this.mAge = age;
    }

    public synchronized String getAllowDuplicates() {
        return this.mAllowDups;
    }

    public synchronized void setAllowDuplicates(String allowDuplicates) {
        this.mAllowDups = allowDuplicates;
    }

    public synchronized String getAltitude() {
        return this.mAltitude;
    }

    public synchronized void setAltitude(String altitude) {
        this.mAltitude = altitude;
    }

    public synchronized String getAndroidId() {
        return this.mAndroidId;
    }

    public synchronized void setAndroidId(String androidId) {
        this.mAndroidId = androidId;
    }

    public synchronized String getAndroidIdMd5() {
        return this.mAndroidIdMd5;
    }

    public synchronized void setAndroidIdMd5(String androidIdMd5) {
        this.mAndroidIdMd5 = androidIdMd5;
    }

    public synchronized String getAndroidIdSha1() {
        return this.mAndroidIdSha1;
    }

    public synchronized void setAndroidIdSha1(String androidIdSha1) {
        this.mAndroidIdSha1 = androidIdSha1;
    }

    public synchronized String getAndroidIdSha256() {
        return this.mAndroidIdSha256;
    }

    public synchronized void setAndroidIdSha256(String androidIdSha256) {
        this.mAndroidIdSha256 = androidIdSha256;
    }

    public synchronized String getAppAdTrackingEnabled() {
        return this.mAppAdTracking;
    }

    public synchronized void setAppAdTrackingEnabled(String adTrackingEnabled) {
        this.mAppAdTracking = adTrackingEnabled;
    }

    public synchronized String getAppName() {
        return this.mAppName;
    }

    public synchronized void setAppName(String app_name) {
        this.mAppName = app_name;
    }

    public synchronized String getAppVersion() {
        return this.mAppVersion;
    }

    public synchronized void setAppVersion(String appVersion) {
        this.mAppVersion = appVersion;
    }

    public synchronized String getAppVersionName() {
        return this.mAppVersionName;
    }

    public synchronized void setAppVersionName(String appVersionName) {
        this.mAppVersionName = appVersionName;
    }

    public synchronized String getConnectionType() {
        return this.mConnectionType;
    }

    public synchronized void setConnectionType(String connection_type) {
        this.mConnectionType = connection_type;
    }

    public synchronized String getConversionKey() {
        return this.mConversionKey;
    }

    public synchronized void setConversionKey(String conversionKey) {
        this.mConversionKey = conversionKey;
    }

    public synchronized String getCountryCode() {
        return this.mCountryCode;
    }

    public synchronized void setCountryCode(String countryCode) {
        this.mCountryCode = countryCode;
    }

    public synchronized String getCurrencyCode() {
        return this.mCurrencyCode;
    }

    public synchronized void setCurrencyCode(String currencyCode) {
        this.mCurrencyCode = currencyCode;
    }

    public synchronized String getDeviceBrand() {
        return this.mDeviceBrand;
    }

    public synchronized void setDeviceBrand(String deviceBrand) {
        this.mDeviceBrand = deviceBrand;
    }

    public synchronized String getDeviceCarrier() {
        return this.mDeviceCarrier;
    }

    public synchronized void setDeviceCarrier(String carrier) {
        this.mDeviceCarrier = carrier;
    }

    public synchronized String getDeviceCpuType() {
        return this.mDeviceCpuType;
    }

    public synchronized void setDeviceCpuType(String cpuType) {
        this.mDeviceCpuType = cpuType;
    }

    public synchronized String getDeviceCpuSubtype() {
        return this.mDeviceCpuSubtype;
    }

    public synchronized void setDeviceCpuSubtype(String cpuType) {
        this.mDeviceCpuSubtype = cpuType;
    }

    public synchronized String getDeviceId() {
        return this.mDeviceId;
    }

    public synchronized void setDeviceId(String deviceId) {
        this.mDeviceId = deviceId;
    }

    public synchronized String getDeviceModel() {
        return this.mDeviceModel;
    }

    public synchronized void setDeviceModel(String model) {
        this.mDeviceModel = model;
    }

    public synchronized boolean getDebugMode() {
        return this.mDebugMode;
    }

    public synchronized void setDebugMode(boolean debug) {
        this.mDebugMode = debug;
    }

    public synchronized String getExistingUser() {
        return this.mExistingUser;
    }

    public synchronized void setExistingUser(String existingUser) {
        this.mExistingUser = existingUser;
    }

    public synchronized String getFacebookUserId() {
        return this.mFbUserId;
    }

    public synchronized void setFacebookUserId(String fb_user_id) {
        this.mFbUserId = fb_user_id;
    }

    public synchronized String getGender() {
        return this.mGender;
    }

    public synchronized void setGender(MATGender gender) {
        if (gender == MATGender.MALE) {
            this.mGender = "0";
        } else if (gender == MATGender.FEMALE) {
            this.mGender = "1";
        } else {
            this.mGender = "";
        }
    }

    public synchronized String getGoogleAdvertisingId() {
        return this.mGaid;
    }

    public synchronized void setGoogleAdvertisingId(String adId) {
        this.mGaid = adId;
    }

    public synchronized String getGoogleAdTrackingLimited() {
        return this.mGaidLimited;
    }

    public synchronized void setGoogleAdTrackingLimited(String limited) {
        this.mGaidLimited = limited;
    }

    public synchronized String getGoogleUserId() {
        return this.mGgUserId;
    }

    public synchronized void setGoogleUserId(String google_user_id) {
        this.mGgUserId = google_user_id;
    }

    public synchronized String getInstallDate() {
        return this.mInstallDate;
    }

    public synchronized void setInstallDate(String installDate) {
        this.mInstallDate = installDate;
    }

    public synchronized String getInstaller() {
        return this.mInstallerPackage;
    }

    public synchronized void setInstaller(String installer) {
        this.mInstallerPackage = installer;
    }

    public synchronized String getInstallReferrer() {
        return MATUtils.getStringFromSharedPreferences(this.mContext, "mat_referrer");
    }

    public synchronized void setInstallReferrer(String installReferrer) {
        MATUtils.saveToSharedPreferences(this.mContext, "mat_referrer", installReferrer);
    }

    public synchronized String getIsPayingUser() {
        return MATUtils.getStringFromSharedPreferences(this.mContext, "mat_is_paying_user");
    }

    public synchronized void setIsPayingUser(String isPayingUser) {
        MATUtils.saveToSharedPreferences(this.mContext, "mat_is_paying_user", isPayingUser);
    }

    public synchronized String getLanguage() {
        return this.mLanguage;
    }

    public synchronized void setLanguage(String language) {
        this.mLanguage = language;
    }

    public synchronized String getLastOpenLogId() {
        return MATUtils.getStringFromSharedPreferences(this.mContext, "mat_log_id_last_open");
    }

    public synchronized void setLastOpenLogId(String logId) {
        MATUtils.saveToSharedPreferences(this.mContext, "mat_log_id_last_open", logId);
    }

    public synchronized String getLatitude() {
        return this.mLatitude;
    }

    public synchronized void setLatitude(String latitude) {
        this.mLatitude = latitude;
    }

    public synchronized Location getLocation() {
        return this.mLocation;
    }

    public synchronized void setLocation(Location location) {
        this.mLocation = location;
    }

    public synchronized String getLongitude() {
        return this.mLongitude;
    }

    public synchronized void setLongitude(String longitude) {
        this.mLongitude = longitude;
    }

    public synchronized String getMacAddress() {
        return this.mMacAddress;
    }

    public synchronized void setMacAddress(String mac_address) {
        this.mMacAddress = mac_address;
    }

    public synchronized String getMatId() {
        return this.mContext.getSharedPreferences("mat_id", 0).contains("mat_id") ? this.mContext.getSharedPreferences("mat_id", 0).getString("mat_id", "") : MATUtils.getStringFromSharedPreferences(this.mContext, "mat_id");
    }

    public synchronized void setMatId(String matId) {
        MATUtils.saveToSharedPreferences(this.mContext, "mat_id", matId);
    }

    public synchronized String getMCC() {
        return this.mMCC;
    }

    public synchronized void setMCC(String mcc) {
        this.mMCC = mcc;
    }

    public synchronized String getMNC() {
        return this.mMNC;
    }

    public synchronized void setMNC(String mnc) {
        this.mMNC = mnc;
    }

    public synchronized String getOpenLogId() {
        return MATUtils.getStringFromSharedPreferences(this.mContext, "mat_log_id_open");
    }

    public synchronized void setOpenLogId(String logId) {
        MATUtils.saveToSharedPreferences(this.mContext, "mat_log_id_open", logId);
    }

    public synchronized String getOsVersion() {
        return this.mOsVersion;
    }

    public synchronized void setOsVersion(String osVersion) {
        this.mOsVersion = osVersion;
    }

    public synchronized String getPackageName() {
        return this.mPackageName;
    }

    public synchronized void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public synchronized String getPhoneNumber() {
        return MATUtils.getStringFromSharedPreferences(this.mContext, "mat_phone_number");
    }

    public synchronized void setPhoneNumber(String phoneNumber) {
        MATUtils.saveToSharedPreferences(this.mContext, "mat_phone_number", phoneNumber);
        setPhoneNumberMd5(MATUtils.md5(phoneNumber));
        setPhoneNumberSha1(MATUtils.sha1(phoneNumber));
        setPhoneNumberSha256(MATUtils.sha256(phoneNumber));
    }

    public synchronized String getPhoneNumberMd5() {
        return this.mPhoneNumberMd5;
    }

    public synchronized void setPhoneNumberMd5(String phoneNumberMd5) {
        this.mPhoneNumberMd5 = phoneNumberMd5;
    }

    public synchronized String getPhoneNumberSha1() {
        return this.mPhoneNumberSha1;
    }

    public synchronized void setPhoneNumberSha1(String phoneNumberSha1) {
        this.mPhoneNumberSha1 = phoneNumberSha1;
    }

    public synchronized String getPhoneNumberSha256() {
        return this.mPhoneNumberSha256;
    }

    public synchronized void setPhoneNumberSha256(String phoneNumberSha256) {
        this.mPhoneNumberSha256 = phoneNumberSha256;
    }

    public synchronized String getPluginName() {
        return this.mPluginName;
    }

    public synchronized void setPluginName(String pluginName) {
        this.mPluginName = null;
    }

    public synchronized String getPurchaseStatus() {
        return this.mPurchaseStatus;
    }

    public synchronized void setPurchaseStatus(String purchaseStatus) {
        this.mPurchaseStatus = purchaseStatus;
    }

    public synchronized String getReferralSource() {
        return this.mReferralSource;
    }

    public synchronized void setReferralSource(String referralPackage) {
        this.mReferralSource = referralPackage;
    }

    public synchronized String getReferralUrl() {
        return this.mReferralUrl;
    }

    public synchronized void setReferralUrl(String referralUrl) {
        this.mReferralUrl = referralUrl;
    }

    public synchronized String getReferrerDelay() {
        return this.mReferrerDelay;
    }

    public synchronized void setReferrerDelay(long referrerDelay) {
        this.mReferrerDelay = Long.toString(referrerDelay);
    }

    public synchronized String getRefId() {
        return this.mRefId;
    }

    public synchronized void setRefId(String refId) {
        this.mRefId = refId;
    }

    public synchronized String getRevenue() {
        return this.mRevenue;
    }

    public synchronized void setRevenue(String revenue) {
        this.mRevenue = revenue;
    }

    public synchronized String getScreenDensity() {
        return this.mScreenDensity;
    }

    public synchronized void setScreenDensity(String density) {
        this.mScreenDensity = density;
    }

    public synchronized String getScreenHeight() {
        return this.mScreenHeight;
    }

    public synchronized void setScreenHeight(String screenheight) {
        this.mScreenHeight = screenheight;
    }

    public synchronized String getScreenWidth() {
        return this.mScreenWidth;
    }

    public synchronized void setScreenWidth(String screenwidth) {
        this.mScreenWidth = screenwidth;
    }

    public synchronized String getSdkVersion() {
        return "3.11.4";
    }

    public synchronized String getSiteId() {
        return this.mSiteId;
    }

    public synchronized void setSiteId(String siteId) {
        this.mSiteId = siteId;
    }

    public synchronized String getTimeZone() {
        return this.mTimeZone;
    }

    public synchronized void setTimeZone(String timeZone) {
        this.mTimeZone = timeZone;
    }

    public synchronized String getTrackingId() {
        return this.mTrackingId;
    }

    public synchronized void setTrackingId(String trackingId) {
        this.mTrackingId = trackingId;
    }

    public synchronized String getTRUSTeId() {
        return this.mTrusteId;
    }

    public synchronized void setTRUSTeId(String tpid) {
        this.mTrusteId = tpid;
    }

    public synchronized String getTwitterUserId() {
        return this.mTwUserId;
    }

    public synchronized void setTwitterUserId(String twitter_user_id) {
        this.mTwUserId = twitter_user_id;
    }

    public synchronized String getUserAgent() {
        return this.mUserAgent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setUserAgent(String userAgent) {
        this.mUserAgent = userAgent;
    }

    public synchronized String getUserEmail() {
        return MATUtils.getStringFromSharedPreferences(this.mContext, "mat_user_email");
    }

    public synchronized void setUserEmail(String userEmail) {
        MATUtils.saveToSharedPreferences(this.mContext, "mat_user_email", userEmail);
        setUserEmailMd5(MATUtils.md5(userEmail));
        setUserEmailSha1(MATUtils.sha1(userEmail));
        setUserEmailSha256(MATUtils.sha256(userEmail));
    }

    public synchronized String getUserEmailMd5() {
        return this.mUserEmailMd5;
    }

    public synchronized void setUserEmailMd5(String userEmailMd5) {
        this.mUserEmailMd5 = userEmailMd5;
    }

    public synchronized String getUserEmailSha1() {
        return this.mUserEmailSha1;
    }

    public synchronized void setUserEmailSha1(String userEmailSha1) {
        this.mUserEmailSha1 = userEmailSha1;
    }

    public synchronized String getUserEmailSha256() {
        return this.mUserEmailSha256;
    }

    public synchronized void setUserEmailSha256(String userEmailSha256) {
        this.mUserEmailSha256 = userEmailSha256;
    }

    public synchronized JSONArray getUserEmails() {
        return this.mUserEmails;
    }

    public synchronized void setUserEmails(String[] emails) {
        this.mUserEmails = new JSONArray();
        for (String str : emails) {
            this.mUserEmails.put(str);
        }
    }

    public synchronized String getUserId() {
        return MATUtils.getStringFromSharedPreferences(this.mContext, "mat_user_id");
    }

    public synchronized void setUserId(String user_id) {
        MATUtils.saveToSharedPreferences(this.mContext, "mat_user_id", user_id);
    }

    public synchronized String getUserName() {
        return MATUtils.getStringFromSharedPreferences(this.mContext, "mat_user_name");
    }

    public synchronized void setUserName(String userName) {
        MATUtils.saveToSharedPreferences(this.mContext, "mat_user_name", userName);
        setUserNameMd5(MATUtils.md5(userName));
        setUserNameSha1(MATUtils.sha1(userName));
        setUserNameSha256(MATUtils.sha256(userName));
    }

    public synchronized String getUserNameMd5() {
        return this.mUserNameMd5;
    }

    public synchronized void setUserNameMd5(String userNameMd5) {
        this.mUserNameMd5 = userNameMd5;
    }

    public synchronized String getUserNameSha1() {
        return this.mUserNameSha1;
    }

    public synchronized void setUserNameSha1(String userNameSha1) {
        this.mUserNameSha1 = userNameSha1;
    }

    public synchronized String getUserNameSha256() {
        return this.mUserNameSha256;
    }

    public synchronized void setUserNameSha256(String userNameSha256) {
        this.mUserNameSha256 = userNameSha256;
    }
}
