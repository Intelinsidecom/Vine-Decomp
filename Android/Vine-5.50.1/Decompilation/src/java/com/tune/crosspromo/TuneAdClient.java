package com.tune.crosspromo;

import android.net.Uri;
import com.mobileapptracker.MobileAppTracker;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class TuneAdClient {
    private static String advertiserId;
    private static String apiUrl;
    private static boolean customMode;
    private static TuneAdUtils utils;

    public static void init(String advertiserId2) {
        apiUrl = "api.cp.tune.com/api/v1/ads";
        utils = TuneAdUtils.getInstance();
        advertiserId = advertiserId2;
    }

    public static void logView(final TuneAdView adView, final JSONObject adParams) {
        if (MobileAppTracker.isOnline(utils.getContext())) {
            utils.getLogThread().execute(new Runnable() { // from class: com.tune.crosspromo.TuneAdClient.3
                @Override // java.lang.Runnable
                public void run() throws IOException {
                    Uri.Builder builder = TuneAdClient.customMode ? Uri.parse("http://" + TuneAdClient.apiUrl + "/api/v1/ads/view").buildUpon() : Uri.parse("https://" + TuneAdClient.advertiserId + ".event." + TuneAdClient.apiUrl + "/view").buildUpon();
                    builder.appendQueryParameter("requestId", adView.requestId);
                    TuneAdClient.logEvent(builder.build().toString(), adParams);
                }
            });
        }
    }

    public static void logClick(final TuneAdView adView, final JSONObject adParams) {
        if (MobileAppTracker.isOnline(utils.getContext())) {
            utils.getLogThread().execute(new Runnable() { // from class: com.tune.crosspromo.TuneAdClient.4
                @Override // java.lang.Runnable
                public void run() throws IOException {
                    Uri.Builder builder = TuneAdClient.customMode ? Uri.parse("http://" + TuneAdClient.apiUrl + "/api/v1/ads/click").buildUpon() : Uri.parse("https://" + TuneAdClient.advertiserId + ".click." + TuneAdClient.apiUrl + "/click").buildUpon();
                    builder.appendQueryParameter("requestId", adView.requestId);
                    TuneAdClient.logEvent(builder.build().toString(), adParams);
                }
            });
        }
    }

    public static void logClose(final TuneAdView adView, final JSONObject adParams) {
        if (MobileAppTracker.isOnline(utils.getContext())) {
            utils.getLogThread().execute(new Runnable() { // from class: com.tune.crosspromo.TuneAdClient.5
                @Override // java.lang.Runnable
                public void run() throws IOException {
                    Uri.Builder builder = TuneAdClient.customMode ? Uri.parse("http://" + TuneAdClient.apiUrl + "/api/v1/ads/close").buildUpon() : Uri.parse("https://" + TuneAdClient.advertiserId + ".event." + TuneAdClient.apiUrl + "/close").buildUpon();
                    builder.appendQueryParameter("requestId", adView.requestId);
                    TuneAdClient.logEvent(builder.build().toString(), adParams);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logEvent(String url, JSONObject adParams) throws IOException {
        InputStream is = null;
        try {
            try {
                URL myurl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
                conn.setReadTimeout(60000);
                conn.setConnectTimeout(60000);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                os.write(adParams.toString().getBytes("UTF-8"));
                os.close();
                conn.connect();
                conn.getInputStream();
                if (0 != 0) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                if (0 != 0) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    is.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
    }
}
