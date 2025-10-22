package co.vine.android.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import co.vine.android.api.response.VineClientFlags;
import co.vine.android.cache.CacheFactory;
import co.vine.android.cache.text.TextCache;
import co.vine.android.network.HeaderInjecter;
import co.vine.android.network.NetworkOperation;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.CrossConstants;
import com.edisonwang.android.slog.SLog;
import com.google.gdata.util.common.base.PercentEscaper;

/* loaded from: classes.dex */
public class VineAPI implements HeaderInjecter {
    private static PercentEscaper encoder;
    private static VineAPI mApi;
    private static SessionKeyGetter sSessionKeyGetter;
    private final TextCache mApiResponseCache;
    private String mBaseUrl;
    private String mConfigUrl;
    private final Context mContext;
    private String mExploreUrl;
    private String mMediaUrl;
    private String mRtcUrl;
    private String mTwitterUrl;
    private String mXVineClientHeader;

    public interface SessionKeyGetter {
        String getSessionKey(Context context);
    }

    private VineAPI(Context context, Resources res) {
        String clientSuffix;
        this.mContext = context;
        this.mApiResponseCache = CacheFactory.newTextCache(context, this, 10485760);
        refreshHostUrls(null, res);
        String versionName = CommonUtil.getPackageVersionName(context);
        if (!TextUtils.isEmpty(versionName)) {
            if (CrossConstants.IS_EXPLORE) {
                clientSuffix = "-gb";
            } else if (CrossConstants.VINE_PLAYER_ENABLED) {
                clientSuffix = "";
            } else {
                clientSuffix = "-ns";
            }
            this.mXVineClientHeader = "android" + clientSuffix + "/" + versionName;
            return;
        }
        this.mXVineClientHeader = null;
    }

    public void refreshHostUrls(VineClientFlags clientFlags, Resources resources) {
        String savedBaseHost;
        String savedRtcHost;
        String savedMediaHost;
        String savedExploreHost;
        String savedExplorePath;
        Context context = this.mContext;
        if (resources == null) {
            resources = this.mContext.getResources();
        }
        if (clientFlags == null) {
            SharedPreferences prefs = CommonUtil.getDefaultSharedPrefs(context);
            savedBaseHost = prefs.getString("client_flags_api_host", null);
            savedRtcHost = prefs.getString("client_flags_rtc_host", null);
            savedMediaHost = prefs.getString("client_flags_media_host", null);
            savedExploreHost = prefs.getString("client_flags_explore_host", null);
            savedExplorePath = prefs.getString("client_flags_explore_path", null);
        } else {
            savedBaseHost = clientFlags.apiHost;
            savedRtcHost = clientFlags.rtcHost;
            savedMediaHost = clientFlags.mediaHost;
            savedExploreHost = clientFlags.exploreHost;
            savedExplorePath = clientFlags.explorePath;
        }
        this.mBaseUrl = savedBaseHost != null ? "https://" + savedBaseHost : resources.getString(CrossConstants.RES_BASE_URL);
        this.mTwitterUrl = resources.getString(CrossConstants.RES_TWITTER_API);
        this.mRtcUrl = savedRtcHost != null ? "https://" + savedRtcHost : resources.getString(CrossConstants.RES_RTC_URL);
        this.mMediaUrl = savedMediaHost != null ? "https://" + savedMediaHost : resources.getString(CrossConstants.RES_MEDIA_URL);
        this.mExploreUrl = savedExploreHost != null ? "http://" + savedExploreHost : resources.getString(CrossConstants.RES_EXPLORE_URL);
        if (!TextUtils.isEmpty(savedExplorePath)) {
            this.mExploreUrl += savedExplorePath;
        } else {
            this.mExploreUrl += resources.getString(CrossConstants.RES_EXPLORE_URL_PATH);
        }
        this.mConfigUrl = resources.getString(CrossConstants.RES_CONFIG_URL);
        SLog.d("hosts=" + this.mBaseUrl + ", " + this.mRtcUrl + ", " + this.mExploreUrl + ", " + this.mMediaUrl);
    }

    public static void setSessionKeyGetter(SessionKeyGetter sessionKeyGetter) {
        sSessionKeyGetter = sessionKeyGetter;
    }

    public static VineAPI getInstance(Context context) {
        if (mApi == null) {
            Context appContext = context.getApplicationContext();
            Resources res = appContext.getResources();
            if (res == null) {
                res = context.getResources();
            }
            if (res == null) {
                throw new InvalidContextForInitializationException("Context does not have resources attached, Application Context is: " + context.getApplicationContext().getClass().getName());
            }
            mApi = new VineAPI(appContext, res);
        }
        return mApi;
    }

    public TextCache getCacheStorage() {
        return this.mApiResponseCache;
    }

    public static class InvalidContextForInitializationException extends IllegalStateException {
        public InvalidContextForInitializationException(String s) {
            super(s);
        }
    }

    public String getBaseUrl() {
        return this.mBaseUrl;
    }

    public String getTwitterUrl() {
        return this.mTwitterUrl;
    }

    public String getRtcUrl() {
        return this.mRtcUrl;
    }

    public String getMediaUrl() {
        return this.mMediaUrl;
    }

    public String getExploreUrl() {
        return this.mExploreUrl;
    }

    public String getConfigUrl() {
        return this.mConfigUrl;
    }

    @Override // co.vine.android.network.HeaderInjecter
    public void addClientHeaders(NetworkOperation request) {
        if (!TextUtils.isEmpty(this.mXVineClientHeader)) {
            request.addHeader("X-Vine-Client", this.mXVineClientHeader);
            String language = CommonUtil.getLocale();
            if (!TextUtils.isEmpty(language)) {
                request.addHeader("Accept-Language", language);
            }
            if (!CrossConstants.IS_PRODUCTION) {
                request.addHeader("X-Vine-Auth", getAuthHeaderSecret());
            }
            if (SLog.sLogsOn && !TextUtils.isEmpty(CrossConstants.DEV_HEADER)) {
                request.addHeader(CrossConstants.DEV_HEADER, "1");
            }
        }
    }

    public String getVineClientHeader() {
        return this.mXVineClientHeader;
    }

    public void addSessionKeyAuthHeader(NetworkOperation op) {
        op.addHeader("vine-session-id", sSessionKeyGetter.getSessionKey(this.mContext));
    }

    public static void addSessionKeyAuthHeader(NetworkOperation op, String key) {
        op.addHeader("vine-session-id", key);
    }

    public static void addContentTypeHeader(NetworkOperation op, String contentType) {
        op.addHeader("content-type", contentType);
    }

    public static void addPollingHeader(NetworkOperation op, boolean isPolling) {
        op.addHeader("X-Vine-Polling", String.valueOf(isPolling));
    }

    public String getAuthHeaderSecret() {
        return this.mContext.getResources().getString(CrossConstants.RES_APP_SECRET);
    }

    public StringBuilder buildUrl(Object... paths) {
        return buildUponUrl(this.mBaseUrl, paths);
    }

    public static StringBuilder buildUponUrl(String baseUrl, Object... paths) {
        StringBuilder sb = new StringBuilder(baseUrl);
        if (paths != null) {
            for (Object path : paths) {
                sb.append('/');
                sb.append(path);
            }
        }
        return sb;
    }

    public static void addParam(StringBuilder sb, String name, int value) {
        prefixParam(sb, name).append(value);
    }

    public static void addParam(StringBuilder sb, String name, long value) {
        prefixParam(sb, name).append(value);
    }

    public static void addParam(StringBuilder sb, String name, String value) {
        prefixParam(sb, name).append(encode(value));
    }

    private static StringBuilder prefixParam(StringBuilder sb, String name) {
        if (sb.indexOf("?") == -1) {
            sb.append('?');
        } else {
            sb.append('&');
        }
        return sb.append(name).append('=');
    }

    public static String encode(String text) {
        if (text == null) {
            return "";
        }
        if (encoder == null) {
            encoder = new PercentEscaper("-._~", false);
        }
        return encoder.escape(text);
    }

    public static StringBuilder addAnchor(StringBuilder url, String anchor, String anchorType) {
        if (anchor != null) {
            addParam(url, anchorType, anchor);
        }
        return url;
    }

    public static StringBuilder addAnchor(StringBuilder url, String anchor) {
        addAnchor(url, anchor, "anchor");
        return url;
    }

    public static StringBuilder addBackAnchor(StringBuilder url, String backAnchor) {
        addAnchor(url, backAnchor, "backAnchor");
        return url;
    }
}
