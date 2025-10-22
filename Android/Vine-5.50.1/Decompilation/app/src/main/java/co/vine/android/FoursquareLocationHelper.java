package co.vine.android;

import android.net.Uri;
import android.text.TextUtils;
import com.edisonwang.android.slog.SLog;
import com.mobileapptracker.MATEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/* loaded from: classes.dex */
public final class FoursquareLocationHelper {
    private static final byte[] CLIENT_ID = {-49, -66, -62, -29, -64, -65, -56, -68, -48, -62, -68, -56, -32, -69, -29, -56, -31, -27, -27, -32, -67, -58, -61, -59, -62, -51, -30, -55, -60, -69, -64, -47, -27, -54, -61, -65, -66, -47, -66, -53, -62, -44, -68, -52, -54, -69, -67, -63};
    private static final byte[] CLIENT_SECRET = {-46, -67, -45, -59, -48, -58, -58, -28, -57, -32, -48, -29, -67, -49, -67, -55, -54, -30, -51, -44, -46, -31, -65, -53, -60, -67, -47, -46, -29, -28, -49, -44, -54, -45, -54, -61, -59, -49, -52, -46, -57, -44, -61, -54, -66, -49, -60, -58};

    public static StringBuilder getFoursquareURL(double latitude, double longitude, String query) throws UnsupportedEncodingException {
        String coordinates = String.format("%f,%f", Double.valueOf(latitude), Double.valueOf(longitude));
        String encodedQuery = null;
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            SLog.e("Exception while encoding query for foursquare", (Throwable) ex);
        }
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority("api.foursquare.com").appendPath("v2").appendPath("venues").appendPath(MATEvent.SEARCH).appendQueryParameter("client_id", VineXForm.xform(CLIENT_ID, (byte) 21)).appendQueryParameter("client_secret", VineXForm.xform(CLIENT_SECRET, (byte) 21)).appendQueryParameter("ll", coordinates).appendQueryParameter("v", "20130815");
        if (!TextUtils.isEmpty(encodedQuery)) {
            builder.appendQueryParameter("query", encodedQuery);
        }
        return new StringBuilder(builder.build().toString());
    }
}
