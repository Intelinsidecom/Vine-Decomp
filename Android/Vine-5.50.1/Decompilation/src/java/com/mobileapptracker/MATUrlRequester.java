package com.mobileapptracker;

import android.net.Uri;
import android.util.Log;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class MATUrlRequester {
    MATUrlRequester() {
    }

    public void requestDeeplink(MATDeferredDplinkr dplinkr) {
        InputStream is;
        InputStream is2 = null;
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https").authority(String.valueOf(dplinkr.getAdvertiserId()) + ".deeplink.mobileapptracking.com").appendPath("v1").appendPath("link.txt").appendQueryParameter("platform", "android").appendQueryParameter("advertiser_id", dplinkr.getAdvertiserId()).appendQueryParameter("ver", "3.11.4").appendQueryParameter("package_name", dplinkr.getPackageName()).appendQueryParameter("ad_id", dplinkr.getGoogleAdvertisingId() != null ? dplinkr.getGoogleAdvertisingId() : dplinkr.getAndroidId()).appendQueryParameter("user_agent", dplinkr.getUserAgent());
        if (dplinkr.getGoogleAdvertisingId() != null) {
            uri.appendQueryParameter("google_ad_tracking_disabled", Integer.toString(dplinkr.getGoogleAdTrackingLimited()));
        }
        try {
            try {
                URL myurl = new URL(uri.build().toString());
                HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
                conn.setRequestProperty("X-MAT-Key", dplinkr.getConversionKey());
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                boolean error = false;
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    is = conn.getInputStream();
                } else {
                    error = true;
                    is = conn.getErrorStream();
                }
                String deeplink = MATUtils.readStream(is2);
                MATDeeplinkListener listener = dplinkr.getListener();
                if (listener != null) {
                    if (error) {
                        listener.didFailDeeplink(deeplink);
                    } else {
                        listener.didReceiveDeeplink(deeplink);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    is2.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } finally {
            try {
                is2.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:78:0x0121 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected static org.json.JSONObject requestUrl(java.lang.String r14, org.json.JSONObject r15, boolean r16) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 304
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobileapptracker.MATUrlRequester.requestUrl(java.lang.String, org.json.JSONObject, boolean):org.json.JSONObject");
    }

    private static void logResponse(JSONObject response) throws JSONException {
        if (response.length() > 0) {
            try {
                if (response.has("errors") && response.getJSONArray("errors").length() != 0) {
                    String errorMsg = response.getJSONArray("errors").getString(0);
                    Log.d("MobileAppTracker", "Event was rejected by server with error: " + errorMsg);
                } else if (response.has("log_action") && !response.getString("log_action").equals("null") && !response.getString("log_action").equals("false") && !response.getString("log_action").equals("true")) {
                    JSONObject logAction = response.getJSONObject("log_action");
                    if (logAction.has("conversion")) {
                        JSONObject conversion = logAction.getJSONObject("conversion");
                        if (conversion.has("status")) {
                            String status = conversion.getString("status");
                            if (status.equals("rejected")) {
                                String statusCode = conversion.getString("status_code");
                                Log.d("MobileAppTracker", "Event was rejected by server: status code " + statusCode);
                            } else {
                                Log.d("MobileAppTracker", "Event was accepted by server");
                            }
                        }
                    }
                } else if (response.has("options")) {
                    JSONObject options = response.getJSONObject("options");
                    if (options.has("conversion_status")) {
                        String conversionStatus = options.getString("conversion_status");
                        Log.d("MobileAppTracker", "Event was " + conversionStatus + " by server");
                    }
                }
            } catch (JSONException e) {
                Log.d("MobileAppTracker", "Server response status could not be parsed");
                e.printStackTrace();
            }
        }
    }
}
