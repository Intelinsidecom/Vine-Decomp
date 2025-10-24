package com.google.android.gms.ads.internal.request;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzha;
import java.util.Collections;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public final class AdResponseParcel implements SafeParcelable {
    public static final zzh CREATOR = new zzh();
    public String body;
    public final int errorCode;
    public final int orientation;
    public final int versionCode;
    public final List<String> zzAQ;
    public final List<String> zzAR;
    public final long zzAU;
    private AdRequestInfoParcel zzBu;
    public final String zzDE;
    public final long zzGM;
    public final boolean zzGN;
    public final long zzGO;
    public final List<String> zzGP;
    public final String zzGQ;
    public final long zzGR;
    public final String zzGS;
    public final boolean zzGT;
    public final String zzGU;
    public final String zzGV;
    public final boolean zzGW;
    public final boolean zzGX;
    public final boolean zzGY;
    public final int zzGZ;
    public final boolean zzGy;
    public LargeParcelTeleporter zzHa;
    public String zzHb;
    public String zzHc;
    public final boolean zztY;
    public boolean zztZ;

    public AdResponseParcel(int errorCode) {
        this(14, null, null, null, errorCode, null, -1L, false, -1L, null, -1L, -1, null, -1L, null, false, null, null, false, false, false, true, false, 0, null, null, null, false);
    }

    public AdResponseParcel(int errorCode, long refreshIntervalInMillis) {
        this(14, null, null, null, errorCode, null, -1L, false, -1L, null, refreshIntervalInMillis, -1, null, -1L, null, false, null, null, false, false, false, true, false, 0, null, null, null, false);
    }

    AdResponseParcel(int versionCode, String baseUrl, String body, List<String> clickUrls, int errorCode, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, boolean isJavascriptTag, String passbackUrl, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, LargeParcelTeleporter bodyTeleporter, String csiLatencyInfo, String gwsQueryId, boolean isFluid) {
        StringParcel stringParcel;
        this.versionCode = versionCode;
        this.zzDE = baseUrl;
        this.body = body;
        this.zzAQ = clickUrls != null ? Collections.unmodifiableList(clickUrls) : null;
        this.errorCode = errorCode;
        this.zzAR = impressionUrls != null ? Collections.unmodifiableList(impressionUrls) : null;
        this.zzGM = interstitialTimeoutInMillis;
        this.zzGN = isMediation;
        this.zzGO = mediationConfigCacheTimeInMillis;
        this.zzGP = manualTrackingUrls != null ? Collections.unmodifiableList(manualTrackingUrls) : null;
        this.zzAU = refreshIntervalInMillis;
        this.orientation = orientation;
        this.zzGQ = adSizeString;
        this.zzGR = fetchTime;
        this.zzGS = debugDialog;
        this.zzGT = isJavascriptTag;
        this.zzGU = passbackUrl;
        this.zzGV = activeViewJSON;
        this.zzGW = isCustomRenderAllowed;
        this.zztY = isNative;
        this.zzGy = useHTTPS;
        this.zzGX = contentUrlOptedOut;
        this.zzGY = isPrefetched;
        this.zzGZ = panTokenStatus;
        this.zzHa = bodyTeleporter;
        this.zzHb = csiLatencyInfo;
        this.zzHc = gwsQueryId;
        if (this.body == null && this.zzHa != null && (stringParcel = (StringParcel) this.zzHa.zza(StringParcel.CREATOR)) != null && !TextUtils.isEmpty(stringParcel.zzgm())) {
            this.body = stringParcel.zzgm();
        }
        this.zztZ = isFluid;
    }

    public AdResponseParcel(AdRequestInfoParcel adRequestInfo, String baseUrl, String body, List<String> clickUrls, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, String gwsQueryId, boolean isFluid) {
        this(14, baseUrl, body, clickUrls, -2, impressionUrls, interstitialTimeoutInMillis, isMediation, mediationConfigCacheTimeInMillis, manualTrackingUrls, refreshIntervalInMillis, orientation, adSizeString, fetchTime, debugDialog, false, null, activeViewJSON, isCustomRenderAllowed, isNative, useHTTPS, contentUrlOptedOut, isPrefetched, panTokenStatus, null, null, gwsQueryId, isFluid);
        this.zzBu = adRequestInfo;
    }

    public AdResponseParcel(AdRequestInfoParcel adRequestInfo, String baseUrl, String body, List<String> clickUrls, List<String> impressionUrls, long interstitialTimeoutInMillis, boolean isMediation, long mediationConfigCacheTimeInMillis, List<String> manualTrackingUrls, long refreshIntervalInMillis, int orientation, String adSizeString, long fetchTime, String debugDialog, boolean isJavascriptTag, String passbackUrl, String activeViewJSON, boolean isCustomRenderAllowed, boolean isNative, boolean useHTTPS, boolean contentUrlOptedOut, boolean isPrefetched, int panTokenStatus, String gwsQueryId, boolean isFluid) {
        this(14, baseUrl, body, clickUrls, -2, impressionUrls, interstitialTimeoutInMillis, isMediation, mediationConfigCacheTimeInMillis, manualTrackingUrls, refreshIntervalInMillis, orientation, adSizeString, fetchTime, debugDialog, isJavascriptTag, passbackUrl, activeViewJSON, isCustomRenderAllowed, isNative, useHTTPS, contentUrlOptedOut, isPrefetched, panTokenStatus, null, null, gwsQueryId, isFluid);
        this.zzBu = adRequestInfo;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (this.zzBu != null && this.zzBu.versionCode >= 9 && !TextUtils.isEmpty(this.body)) {
            this.zzHa = new LargeParcelTeleporter(new StringParcel(this.body));
            this.body = null;
        }
        zzh.zza(this, out, flags);
    }
}
