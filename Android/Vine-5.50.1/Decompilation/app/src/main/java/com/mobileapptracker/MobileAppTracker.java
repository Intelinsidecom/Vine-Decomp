package com.mobileapptracker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;
import com.mobileapptracker.MATEventQueue.Add;
import com.mobileapptracker.MATEventQueue.Dump;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MobileAppTracker {
    private static volatile MobileAppTracker tune = null;
    private final String IV = "heF9BATUfWuISyO8";
    private boolean debugMode;
    private MATDeferredDplinkr dplinkr;
    private MATEncryption encryption;
    protected MATEventQueue eventQueue;
    private boolean fbLogging;
    private boolean firstSession;
    boolean gotGaid;
    boolean gotReferrer;
    private long initTime;
    protected boolean initialized;
    protected boolean isRegistered;
    protected Context mContext;
    private MATPreloadData mPreloadData;
    protected BroadcastReceiver networkStateReceiver;
    boolean notifiedPool;
    protected MATParameters params;
    ExecutorService pool;
    protected ExecutorService pubQueue;
    private long referrerTime;
    private MATResponse tuneListener;
    protected MATTestRequest tuneRequest;
    private MATUrlRequester urlRequester;

    protected MobileAppTracker() {
    }

    public static synchronized MobileAppTracker getInstance() {
        return tune;
    }

    public static synchronized MobileAppTracker init(Context context, String advertiserId, String conversionKey) {
        if (tune == null) {
            tune = new MobileAppTracker();
            tune.mContext = context.getApplicationContext();
            tune.pubQueue = Executors.newSingleThreadExecutor();
            tune.initAll(advertiserId, conversionKey);
        }
        return tune;
    }

    protected void initAll(String advertiserId, String conversionKey) {
        this.dplinkr = MATDeferredDplinkr.initialize(advertiserId, conversionKey, this.mContext.getPackageName());
        this.params = MATParameters.init(this, this.mContext, advertiserId, conversionKey);
        initLocalVariables(conversionKey);
        this.eventQueue = new MATEventQueue(this.mContext, this);
        dumpQueue();
        this.networkStateReceiver = new BroadcastReceiver() { // from class: com.mobileapptracker.MobileAppTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (MobileAppTracker.this.isRegistered) {
                    MobileAppTracker.this.dumpQueue();
                }
            }
        };
        if (this.isRegistered) {
            try {
                this.mContext.unregisterReceiver(this.networkStateReceiver);
            } catch (IllegalArgumentException e) {
            }
            this.isRegistered = false;
        }
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver(this.networkStateReceiver, filter);
        this.isRegistered = true;
        this.initialized = true;
    }

    private void initLocalVariables(String key) {
        this.pool = Executors.newSingleThreadExecutor();
        this.urlRequester = new MATUrlRequester();
        this.encryption = new MATEncryption(key.trim(), "heF9BATUfWuISyO8");
        this.initTime = System.currentTimeMillis();
        this.gotReferrer = !this.mContext.getSharedPreferences("com.mobileapptracking", 0).getString("mat_referrer", "").equals("");
        this.firstSession = true;
        this.initialized = false;
        this.isRegistered = false;
        this.debugMode = false;
        this.fbLogging = false;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void addEventToQueue(String link, String data, JSONObject postBody, boolean firstSession) {
        ExecutorService executorService = this.pool;
        MATEventQueue mATEventQueue = this.eventQueue;
        mATEventQueue.getClass();
        executorService.execute(mATEventQueue.new Add(link, data, postBody, firstSession));
    }

    protected void dumpQueue() {
        if (isOnline(this.mContext)) {
            ExecutorService executorService = this.pool;
            MATEventQueue mATEventQueue = this.eventQueue;
            mATEventQueue.getClass();
            executorService.execute(mATEventQueue.new Dump());
        }
    }

    public void measureSession() {
        this.notifiedPool = false;
        measureEvent(new MATEvent("session"));
        if (this.debugMode) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.2
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(MobileAppTracker.this.mContext, "TUNE measureSession called", 1).show();
                }
            });
        }
    }

    public void measureEvent(String eventName) {
        measureEvent(new MATEvent(eventName));
    }

    public void measureEvent(int eventId) {
        measureEvent(new MATEvent(eventId));
    }

    public void measureEvent(final MATEvent eventData) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.3
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.measure(eventData);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void measure(MATEvent eventData) {
        if (this.initialized) {
            dumpQueue();
            this.params.setAction("conversion");
            Date runDate = new Date();
            if (eventData.getEventName() != null) {
                String eventName = eventData.getEventName();
                if (this.fbLogging) {
                    MATFBBridge.logEvent(eventData);
                }
                if (!eventName.equals("close")) {
                    if (eventName.equals("open") || eventName.equals("install") || eventName.equals("update") || eventName.equals("session")) {
                        this.params.setAction("session");
                        new Date(runDate.getTime() + 60000);
                    }
                }
            }
            if (eventData.getRevenue() > 0.0d) {
                this.params.setIsPayingUser("1");
            }
            String link = MATUrlBuilder.buildLink(eventData, this.mPreloadData, this.debugMode);
            String data = MATUrlBuilder.buildDataUnencrypted(eventData);
            JSONArray eventItemsJson = new JSONArray();
            if (eventData.getEventItems() != null) {
                for (int i = 0; i < eventData.getEventItems().size(); i++) {
                    eventItemsJson.put(eventData.getEventItems().get(i).toJSON());
                }
            }
            JSONObject postBody = MATUrlBuilder.buildBody(eventItemsJson, eventData.getReceiptData(), eventData.getReceiptSignature(), this.params.getUserEmails());
            if (this.tuneRequest != null) {
                this.tuneRequest.constructedRequest(link, data, postBody);
            }
            addEventToQueue(link, data, postBody, this.firstSession);
            this.firstSession = false;
            dumpQueue();
            if (this.tuneListener != null) {
                this.tuneListener.enqueuedActionWithRefId(eventData.getRefId());
            }
        }
    }

    protected boolean makeRequest(String link, String data, JSONObject postBody) throws JSONException, IOException {
        if (this.debugMode) {
            Log.d("MobileAppTracker", "Sending event to server...");
        }
        String encData = MATUrlBuilder.updateAndEncryptData(data, this.encryption);
        String fullLink = String.valueOf(link) + "&data=" + encData;
        JSONObject response = MATUrlRequester.requestUrl(fullLink, postBody, this.debugMode);
        if (response == null) {
            if (this.tuneListener == null) {
                return true;
            }
            this.tuneListener.didFailWithError(response);
            return true;
        }
        if (!response.has("success")) {
            if (this.debugMode) {
                Log.d("MobileAppTracker", "Request failed, event will remain in queue");
            }
            return false;
        }
        if (this.tuneListener != null) {
            boolean success = false;
            try {
                if (response.getString("success").equals("true")) {
                    success = true;
                }
                if (success) {
                    this.tuneListener.didSucceedWithData(response);
                } else {
                    this.tuneListener.didFailWithError(response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            String eventType = response.getString("site_event_type");
            if (!eventType.equals("open")) {
                return true;
            }
            String logId = response.getString("log_id");
            if (getOpenLogId().equals("")) {
                this.params.setOpenLogId(logId);
            }
            this.params.setLastOpenLogId(logId);
            return true;
        } catch (JSONException e2) {
            return true;
        }
    }

    public String getAction() {
        return this.params.getAction();
    }

    public String getAdvertiserId() {
        return this.params.getAdvertiserId();
    }

    public int getAge() {
        return Integer.parseInt(this.params.getAge());
    }

    public double getAltitude() {
        return Double.parseDouble(this.params.getAltitude());
    }

    public String getAndroidId() {
        return this.params.getAndroidId();
    }

    public boolean getAppAdTrackingEnabled() throws NumberFormatException {
        int adTrackingEnabled = Integer.parseInt(this.params.getAppAdTrackingEnabled());
        return adTrackingEnabled == 1;
    }

    public String getAppName() {
        return this.params.getAppName();
    }

    public int getAppVersion() {
        return Integer.parseInt(this.params.getAppVersion());
    }

    public String getConnectionType() {
        return this.params.getConnectionType();
    }

    public String getCountryCode() {
        return this.params.getCountryCode();
    }

    public String getCurrencyCode() {
        return this.params.getCurrencyCode();
    }

    public String getDeviceBrand() {
        return this.params.getDeviceBrand();
    }

    public String getDeviceCarrier() {
        return this.params.getDeviceCarrier();
    }

    public String getDeviceId() {
        return this.params.getDeviceId();
    }

    public String getDeviceModel() {
        return this.params.getDeviceModel();
    }

    public boolean getExistingUser() throws NumberFormatException {
        int intExisting = Integer.parseInt(this.params.getExistingUser());
        return intExisting == 1;
    }

    public String getFacebookUserId() {
        return this.params.getFacebookUserId();
    }

    public MATGender getGender() {
        String gender = this.params.getGender();
        if (gender.equals("0")) {
            return MATGender.MALE;
        }
        if (gender.equals("1")) {
            return MATGender.FEMALE;
        }
        return MATGender.UNKNOWN;
    }

    public String getGoogleAdvertisingId() {
        return this.params.getGoogleAdvertisingId();
    }

    public boolean getGoogleAdTrackingLimited() throws NumberFormatException {
        int intLimited = Integer.parseInt(this.params.getGoogleAdTrackingLimited());
        return intLimited != 0;
    }

    public String getGoogleUserId() {
        return this.params.getGoogleUserId();
    }

    public long getInstallDate() {
        return Long.parseLong(this.params.getInstallDate());
    }

    public String getInstallReferrer() {
        return this.params.getInstallReferrer();
    }

    public boolean getIsPayingUser() {
        String isPayingUser = this.params.getIsPayingUser();
        return isPayingUser.equals("1");
    }

    public String getLanguage() {
        return this.params.getLanguage();
    }

    public String getLastOpenLogId() {
        return this.params.getLastOpenLogId();
    }

    public double getLatitude() {
        return Double.parseDouble(this.params.getLatitude());
    }

    public double getLongitude() {
        return Double.parseDouble(this.params.getLongitude());
    }

    public String getMacAddress() {
        return this.params.getMacAddress();
    }

    public String getMatId() {
        return this.params.getMatId();
    }

    public String getMCC() {
        return this.params.getMCC();
    }

    public String getMNC() {
        return this.params.getMNC();
    }

    public String getOpenLogId() {
        return this.params.getOpenLogId();
    }

    public String getOsVersion() {
        return this.params.getOsVersion();
    }

    public String getPackageName() {
        return this.params.getPackageName();
    }

    public String getPluginName() {
        return this.params.getPluginName();
    }

    public String getReferralSource() {
        return this.params.getReferralSource();
    }

    public String getReferralUrl() {
        return this.params.getReferralUrl();
    }

    public String getRefId() {
        return this.params.getRefId();
    }

    public Double getRevenue() {
        return Double.valueOf(Double.parseDouble(this.params.getRevenue()));
    }

    public String getScreenDensity() {
        return this.params.getScreenDensity();
    }

    public String getScreenHeight() {
        return this.params.getScreenHeight();
    }

    public String getScreenWidth() {
        return this.params.getScreenWidth();
    }

    public String getSDKVersion() {
        return this.params.getSdkVersion();
    }

    public String getSiteId() {
        return this.params.getSiteId();
    }

    public String getTRUSTeId() {
        return this.params.getTRUSTeId();
    }

    public String getTwitterUserId() {
        return this.params.getTwitterUserId();
    }

    public String getUserAgent() {
        return this.params.getUserAgent();
    }

    public String getUserEmail() {
        return this.params.getUserEmail();
    }

    public String getUserId() {
        return this.params.getUserId();
    }

    public String getUserName() {
        return this.params.getUserName();
    }

    public void setAdvertiserId(final String advertiserId) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.4
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setAdvertiserId(advertiserId);
            }
        });
    }

    public void setAge(final int age) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.5
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setAge(Integer.toString(age));
            }
        });
    }

    public void setAltitude(final double altitude) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.6
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setAltitude(Double.toString(altitude));
            }
        });
    }

    public void setAndroidId(String androidId) {
        if (this.dplinkr != null) {
            this.dplinkr.setAndroidId(androidId);
            requestDeeplink();
        }
        if (this.params != null) {
            this.params.setAndroidId(androidId);
        }
    }

    public void setAndroidIdMd5(final String androidIdMd5) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.7
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setAndroidIdMd5(androidIdMd5);
            }
        });
    }

    public void setAndroidIdSha1(final String androidIdSha1) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.8
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setAndroidIdSha1(androidIdSha1);
            }
        });
    }

    public void setAndroidIdSha256(final String androidIdSha256) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.9
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setAndroidIdSha256(androidIdSha256);
            }
        });
    }

    public void setAppAdTrackingEnabled(final boolean adTrackingEnabled) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.10
            @Override // java.lang.Runnable
            public void run() {
                if (adTrackingEnabled) {
                    MobileAppTracker.this.params.setAppAdTrackingEnabled(Integer.toString(1));
                } else {
                    MobileAppTracker.this.params.setAppAdTrackingEnabled(Integer.toString(0));
                }
            }
        });
    }

    public void setConversionKey(final String conversionKey) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.11
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setConversionKey(conversionKey);
            }
        });
    }

    public void setCurrencyCode(final String currency_code) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.12
            @Override // java.lang.Runnable
            public void run() {
                if (currency_code == null || currency_code.equals("")) {
                    MobileAppTracker.this.params.setCurrencyCode("USD");
                } else {
                    MobileAppTracker.this.params.setCurrencyCode(currency_code);
                }
            }
        });
    }

    public void setDeeplinkListener(MATDeeplinkListener listener) {
        this.dplinkr.setListener(listener);
    }

    public void setDeviceBrand(final String deviceBrand) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.13
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setDeviceBrand(deviceBrand);
            }
        });
    }

    public void setDeviceId(final String deviceId) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.14
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setDeviceId(deviceId);
            }
        });
    }

    public void setDeviceModel(final String deviceModel) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.15
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setDeviceModel(deviceModel);
            }
        });
    }

    public void setExistingUser(final boolean existing) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.16
            @Override // java.lang.Runnable
            public void run() {
                if (existing) {
                    MobileAppTracker.this.params.setExistingUser(Integer.toString(1));
                } else {
                    MobileAppTracker.this.params.setExistingUser(Integer.toString(0));
                }
            }
        });
    }

    public void setFacebookUserId(final String fb_user_id) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.17
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setFacebookUserId(fb_user_id);
            }
        });
    }

    public void setGender(final MATGender gender) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.18
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setGender(gender);
            }
        });
    }

    public void setGoogleAdvertisingId(String adId, boolean isLATEnabled) {
        int intLimit = isLATEnabled ? 1 : 0;
        if (this.dplinkr != null) {
            this.dplinkr.setGoogleAdvertisingId(adId, intLimit);
            requestDeeplink();
        }
        if (this.params != null) {
            this.params.setGoogleAdvertisingId(adId);
            this.params.setGoogleAdTrackingLimited(Integer.toString(intLimit));
        }
        this.gotGaid = true;
        if (this.gotReferrer && !this.notifiedPool) {
            synchronized (this.pool) {
                this.pool.notifyAll();
                this.notifiedPool = true;
            }
        }
    }

    public void setGoogleUserId(final String google_user_id) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.19
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setGoogleUserId(google_user_id);
            }
        });
    }

    public void setInstallReferrer(final String referrer) {
        this.gotReferrer = true;
        this.referrerTime = System.currentTimeMillis();
        if (this.params != null) {
            this.params.setReferrerDelay(this.referrerTime - this.initTime);
        }
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.20
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setInstallReferrer(referrer);
            }
        });
    }

    public void setIsPayingUser(final boolean isPayingUser) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.21
            @Override // java.lang.Runnable
            public void run() {
                if (isPayingUser) {
                    MobileAppTracker.this.params.setIsPayingUser(Integer.toString(1));
                } else {
                    MobileAppTracker.this.params.setIsPayingUser(Integer.toString(0));
                }
            }
        });
    }

    public void setLatitude(final double latitude) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.22
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setLatitude(Double.toString(latitude));
            }
        });
    }

    public void setLocation(final Location location) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.23
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setLocation(location);
            }
        });
    }

    public void setLongitude(final double longitude) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.24
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setLongitude(Double.toString(longitude));
            }
        });
    }

    public void setMacAddress(final String macAddress) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.25
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setMacAddress(macAddress);
            }
        });
    }

    public void setMATResponse(MATResponse listener) {
        this.tuneListener = listener;
    }

    public void setOsVersion(final String osVersion) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.26
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setOsVersion(osVersion);
            }
        });
    }

    public void setPackageName(final String packageName) {
        this.dplinkr.setPackageName(packageName);
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.27
            @Override // java.lang.Runnable
            public void run() {
                if (packageName == null || packageName.equals("")) {
                    MobileAppTracker.this.params.setPackageName(MobileAppTracker.this.mContext.getPackageName());
                } else {
                    MobileAppTracker.this.params.setPackageName(packageName);
                }
            }
        });
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.28
            @Override // java.lang.Runnable
            public void run() throws NumberFormatException {
                String phoneNumberDigits = phoneNumber.replaceAll("\\D+", "");
                StringBuilder digitsBuilder = new StringBuilder();
                for (int i = 0; i < phoneNumberDigits.length(); i++) {
                    int numberParsed = Integer.parseInt(String.valueOf(phoneNumberDigits.charAt(i)));
                    digitsBuilder.append(numberParsed);
                }
                MobileAppTracker.this.params.setPhoneNumber(digitsBuilder.toString());
            }
        });
    }

    public void setPreloadedApp(MATPreloadData preloadData) {
        this.mPreloadData = preloadData;
    }

    public void setReferralSources(final Activity act) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.29
            @Override // java.lang.Runnable
            public void run() {
                Uri uri;
                MobileAppTracker.this.params.setReferralSource(act.getCallingPackage());
                Intent intent = act.getIntent();
                if (intent != null && (uri = intent.getData()) != null) {
                    MobileAppTracker.this.params.setReferralUrl(uri.toString());
                }
            }
        });
    }

    public void setReferralUrl(final String url) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.30
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setReferralUrl(url);
            }
        });
    }

    public void setSiteId(final String siteId) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.31
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setSiteId(siteId);
            }
        });
    }

    public void setTRUSTeId(final String tpid) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.32
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setTRUSTeId(tpid);
            }
        });
    }

    public void setTwitterUserId(final String twitter_user_id) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.33
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setTwitterUserId(twitter_user_id);
            }
        });
    }

    public void setUserEmail(final String userEmail) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.34
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setUserEmail(userEmail);
            }
        });
    }

    public void setUserId(final String userId) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.35
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setUserId(userId);
            }
        });
    }

    public void setUserName(final String userName) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.36
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setUserName(userName);
            }
        });
    }

    public void setPluginName(final String plugin_name) {
        if (Arrays.asList(MATConstants.PLUGIN_NAMES).contains(plugin_name)) {
            this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.37
                @Override // java.lang.Runnable
                public void run() {
                    MobileAppTracker.this.params.setPluginName(plugin_name);
                }
            });
        } else if (this.debugMode) {
            throw new IllegalArgumentException("Plugin name not acceptable");
        }
    }

    public void setAllowDuplicates(final boolean allow) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.38
            @Override // java.lang.Runnable
            public void run() {
                if (allow) {
                    MobileAppTracker.this.params.setAllowDuplicates(Integer.toString(1));
                } else {
                    MobileAppTracker.this.params.setAllowDuplicates(Integer.toString(0));
                }
            }
        });
        if (allow) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.39
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(MobileAppTracker.this.mContext, "TUNE Allow Duplicate Requests Enabled, do not release with this enabled!!", 1).show();
                }
            });
        }
    }

    public void setDebugMode(final boolean debug) {
        this.debugMode = debug;
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.40
            @Override // java.lang.Runnable
            public void run() {
                MobileAppTracker.this.params.setDebugMode(debug);
            }
        });
        if (debug) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.41
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(MobileAppTracker.this.mContext, "TUNE Debug Mode Enabled, do not release with this enabled!!", 1).show();
                }
            });
        }
    }

    public void setEmailCollection(final boolean collectEmail) {
        this.pubQueue.execute(new Runnable() { // from class: com.mobileapptracker.MobileAppTracker.42
            @Override // java.lang.Runnable
            public void run() {
                boolean accountPermission = MobileAppTracker.this.mContext.checkCallingOrSelfPermission("android.permission.GET_ACCOUNTS") == 0;
                if (collectEmail && accountPermission) {
                    Account[] accounts = AccountManager.get(MobileAppTracker.this.mContext).getAccountsByType("com.google");
                    if (accounts.length > 0) {
                        MobileAppTracker.this.params.setUserEmail(accounts[0].name);
                    }
                    HashMap<String, String> emailMap = new HashMap<>();
                    Account[] accounts2 = AccountManager.get(MobileAppTracker.this.mContext).getAccounts();
                    for (Account account : accounts2) {
                        if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                            emailMap.put(account.name, account.type);
                        }
                    }
                    Set<String> emailKeys = emailMap.keySet();
                    String[] emailArr = (String[]) emailKeys.toArray(new String[emailKeys.size()]);
                    MobileAppTracker.this.params.setUserEmails(emailArr);
                }
            }
        });
    }

    public void setFacebookEventLogging(boolean logging, Context context, boolean limitEventAndDataUsage) {
        this.fbLogging = logging;
        if (logging) {
            MATFBBridge.startLogger(context, limitEventAndDataUsage);
        }
    }

    public void checkForDeferredDeeplink(MATDeeplinkListener listener) {
        setDeeplinkListener(listener);
        if (firstInstall()) {
            this.dplinkr.enable(true);
        } else {
            this.dplinkr.enable(false);
        }
        if (this.dplinkr.getGoogleAdvertisingId() != null || this.dplinkr.getAndroidId() != null) {
            requestDeeplink();
        }
    }

    private void requestDeeplink() {
        if (this.dplinkr.isEnabled()) {
            this.dplinkr.setUserAgent(this.params.getUserAgent());
            this.dplinkr.checkForDeferredDeeplink(this.mContext, this.urlRequester);
        }
    }

    private boolean firstInstall() {
        SharedPreferences installed = this.mContext.getSharedPreferences("com.mobileapptracking", 0);
        if (installed.contains("mat_installed")) {
            return false;
        }
        installed.edit().putBoolean("mat_installed", true).commit();
        return true;
    }
}
