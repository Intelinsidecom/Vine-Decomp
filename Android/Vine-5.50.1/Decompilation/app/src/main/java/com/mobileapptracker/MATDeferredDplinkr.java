package com.mobileapptracker;

import android.content.Context;

/* loaded from: classes.dex */
class MATDeferredDplinkr {
    private static volatile MATDeferredDplinkr dplinkr;
    private boolean enabled;
    private String advertiserId = null;
    private String conversionKey = null;
    private String packageName = null;
    private String googleAdvertisingId = null;
    private int isLATEnabled = 0;
    private String androidId = null;
    private String userAgent = null;
    private MATDeeplinkListener listener = null;

    private MATDeferredDplinkr() {
    }

    public static synchronized MATDeferredDplinkr initialize(String advertiserId, String conversionKey, String packageName) {
        dplinkr = new MATDeferredDplinkr();
        dplinkr.advertiserId = advertiserId;
        dplinkr.conversionKey = conversionKey;
        dplinkr.packageName = packageName;
        return dplinkr;
    }

    public String getAdvertiserId() {
        return dplinkr.advertiserId;
    }

    public String getConversionKey() {
        return dplinkr.conversionKey;
    }

    public void setPackageName(String packageName) {
        dplinkr.packageName = packageName;
    }

    public String getPackageName() {
        return dplinkr.packageName;
    }

    public void setUserAgent(String userAgent) {
        dplinkr.userAgent = userAgent;
    }

    public String getUserAgent() {
        return dplinkr.userAgent;
    }

    public void setGoogleAdvertisingId(String googleAdvertisingId, int isLATEnabled) {
        dplinkr.googleAdvertisingId = googleAdvertisingId;
        dplinkr.isLATEnabled = isLATEnabled;
    }

    public String getGoogleAdvertisingId() {
        return dplinkr.googleAdvertisingId;
    }

    public int getGoogleAdTrackingLimited() {
        return dplinkr.isLATEnabled;
    }

    public void setAndroidId(String androidId) {
        dplinkr.androidId = androidId;
    }

    public String getAndroidId() {
        return dplinkr.androidId;
    }

    public void setListener(MATDeeplinkListener listener) {
        dplinkr.listener = listener;
    }

    public MATDeeplinkListener getListener() {
        return dplinkr.listener;
    }

    public void enable(boolean enable) {
        this.enabled = enable;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void checkForDeferredDeeplink(Context context, final MATUrlRequester urlRequester) {
        new Thread(new Runnable() { // from class: com.mobileapptracker.MATDeferredDplinkr.1
            @Override // java.lang.Runnable
            public void run() {
                if ((MATDeferredDplinkr.dplinkr.advertiserId == null || MATDeferredDplinkr.dplinkr.conversionKey == null || MATDeferredDplinkr.dplinkr.packageName == null) && MATDeferredDplinkr.this.listener != null) {
                    MATDeferredDplinkr.this.listener.didFailDeeplink("Advertiser ID, conversion key, or package name not set");
                }
                if (MATDeferredDplinkr.dplinkr.googleAdvertisingId == null && MATDeferredDplinkr.dplinkr.androidId == null && MATDeferredDplinkr.this.listener != null) {
                    MATDeferredDplinkr.this.listener.didFailDeeplink("No device identifiers collected");
                }
                urlRequester.requestDeeplink(MATDeferredDplinkr.dplinkr);
            }
        }).start();
    }
}
