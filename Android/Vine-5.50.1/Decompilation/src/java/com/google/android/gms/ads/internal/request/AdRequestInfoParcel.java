package com.google.android.gms.ads.internal.request;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Messenger;
import android.os.Parcel;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzha;
import java.util.Collections;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public final class AdRequestInfoParcel implements SafeParcelable {
    public static final zzf CREATOR = new zzf();
    public final ApplicationInfo applicationInfo;
    public final int versionCode;
    public final int zzGA;
    public final int zzGB;
    public final float zzGC;
    public final String zzGD;
    public final long zzGE;
    public final String zzGF;
    public final List<String> zzGG;
    public final List<String> zzGH;
    public final long zzGI;
    public final CapabilityParcel zzGJ;
    public final String zzGK;
    public final Bundle zzGp;
    public final AdRequestParcel zzGq;
    public final PackageInfo zzGr;
    public final String zzGs;
    public final String zzGt;
    public final String zzGu;
    public final Bundle zzGv;
    public final int zzGw;
    public final Bundle zzGx;
    public final boolean zzGy;
    public final Messenger zzGz;
    public final String zzqO;
    public final String zzqP;
    public final VersionInfoParcel zzqR;
    public final AdSizeParcel zzqV;
    public final NativeAdOptionsParcel zzrj;
    public final List<String> zzrl;

    @zzha
    public static final class zza {
        public final ApplicationInfo applicationInfo;
        public final int zzGA;
        public final int zzGB;
        public final float zzGC;
        public final String zzGD;
        public final long zzGE;
        public final String zzGF;
        public final List<String> zzGG;
        public final List<String> zzGH;
        public final CapabilityParcel zzGJ;
        public final String zzGK;
        public final Bundle zzGp;
        public final AdRequestParcel zzGq;
        public final PackageInfo zzGr;
        public final String zzGt;
        public final String zzGu;
        public final Bundle zzGv;
        public final int zzGw;
        public final Bundle zzGx;
        public final boolean zzGy;
        public final Messenger zzGz;
        public final String zzqO;
        public final String zzqP;
        public final VersionInfoParcel zzqR;
        public final AdSizeParcel zzqV;
        public final NativeAdOptionsParcel zzrj;
        public final List<String> zzrl;

        public zza(Bundle bundle, AdRequestParcel adRequestParcel, AdSizeParcel adSizeParcel, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, VersionInfoParcel versionInfoParcel, Bundle bundle2, List<String> list, List<String> list2, Bundle bundle3, boolean z, Messenger messenger, int i, int i2, float f, String str4, long j, String str5, List<String> list3, String str6, NativeAdOptionsParcel nativeAdOptionsParcel, CapabilityParcel capabilityParcel, String str7) {
            this.zzGp = bundle;
            this.zzGq = adRequestParcel;
            this.zzqV = adSizeParcel;
            this.zzqP = str;
            this.applicationInfo = applicationInfo;
            this.zzGr = packageInfo;
            this.zzGt = str2;
            this.zzGu = str3;
            this.zzqR = versionInfoParcel;
            this.zzGv = bundle2;
            this.zzGy = z;
            this.zzGz = messenger;
            this.zzGA = i;
            this.zzGB = i2;
            this.zzGC = f;
            if (list == null || list.size() <= 0) {
                if (adSizeParcel.zzua) {
                    this.zzGw = 4;
                } else {
                    this.zzGw = 0;
                }
                this.zzrl = null;
                this.zzGH = null;
            } else {
                this.zzGw = 3;
                this.zzrl = list;
                this.zzGH = list2;
            }
            this.zzGx = bundle3;
            this.zzGD = str4;
            this.zzGE = j;
            this.zzGF = str5;
            this.zzGG = list3;
            this.zzqO = str6;
            this.zzrj = nativeAdOptionsParcel;
            this.zzGJ = capabilityParcel;
            this.zzGK = str7;
        }
    }

    AdRequestInfoParcel(int versionCode, Bundle adPositionBundle, AdRequestParcel adRequest, AdSizeParcel adSize, String adUnitId, ApplicationInfo applicationInfo, PackageInfo packageInfo, String querySpamSignals, String sequenceNumber, String sessionId, VersionInfoParcel versionInfo, Bundle stats, int nativeVersion, List<String> nativeTemplates, Bundle contentInfo, boolean useHTTPS, Messenger prefetchMessenger, int screenWidth, int screenHeight, float screenDensity, String viewHierarchy, long correlationId, String requestId, List<String> experimentIds, String slotId, NativeAdOptionsParcel nativeAdOptions, List<String> nativeCustomTemplateIds, long connectionStartTime, CapabilityParcel capabilityParcel, String anchorStatus) {
        this.versionCode = versionCode;
        this.zzGp = adPositionBundle;
        this.zzGq = adRequest;
        this.zzqV = adSize;
        this.zzqP = adUnitId;
        this.applicationInfo = applicationInfo;
        this.zzGr = packageInfo;
        this.zzGs = querySpamSignals;
        this.zzGt = sequenceNumber;
        this.zzGu = sessionId;
        this.zzqR = versionInfo;
        this.zzGv = stats;
        this.zzGw = nativeVersion;
        this.zzrl = nativeTemplates;
        this.zzGH = nativeCustomTemplateIds == null ? Collections.emptyList() : Collections.unmodifiableList(nativeCustomTemplateIds);
        this.zzGx = contentInfo;
        this.zzGy = useHTTPS;
        this.zzGz = prefetchMessenger;
        this.zzGA = screenWidth;
        this.zzGB = screenHeight;
        this.zzGC = screenDensity;
        this.zzGD = viewHierarchy;
        this.zzGE = correlationId;
        this.zzGF = requestId;
        this.zzGG = experimentIds == null ? Collections.emptyList() : Collections.unmodifiableList(experimentIds);
        this.zzqO = slotId;
        this.zzrj = nativeAdOptions;
        this.zzGI = connectionStartTime;
        this.zzGJ = capabilityParcel;
        this.zzGK = anchorStatus;
    }

    public AdRequestInfoParcel(Bundle adPositionBundle, AdRequestParcel adRequest, AdSizeParcel adSize, String adUnitId, ApplicationInfo applicationInfo, PackageInfo packageInfo, String querySpamSignals, String sequenceNumber, String sessionId, VersionInfoParcel versionInfo, Bundle stats, int nativeVersion, List<String> nativeTemplates, List<String> nativeCustomTemplateIds, Bundle contentInfo, boolean useHTTPS, Messenger prefetchMessenger, int screenWidth, int screenHeight, float screenDensity, String viewHierarchy, long correlationId, String requestId, List<String> experimentIds, String slotId, NativeAdOptionsParcel nativeAdOptionsParcel, long connectionStartTime, CapabilityParcel capabilityParcel, String anchorStatus) {
        this(12, adPositionBundle, adRequest, adSize, adUnitId, applicationInfo, packageInfo, querySpamSignals, sequenceNumber, sessionId, versionInfo, stats, nativeVersion, nativeTemplates, contentInfo, useHTTPS, prefetchMessenger, screenWidth, screenHeight, screenDensity, viewHierarchy, correlationId, requestId, experimentIds, slotId, nativeAdOptionsParcel, nativeCustomTemplateIds, connectionStartTime, capabilityParcel, anchorStatus);
    }

    public AdRequestInfoParcel(zza partialAdRequestInfo, String querySpamSignals, long connectionStartTime) {
        this(partialAdRequestInfo.zzGp, partialAdRequestInfo.zzGq, partialAdRequestInfo.zzqV, partialAdRequestInfo.zzqP, partialAdRequestInfo.applicationInfo, partialAdRequestInfo.zzGr, querySpamSignals, partialAdRequestInfo.zzGt, partialAdRequestInfo.zzGu, partialAdRequestInfo.zzqR, partialAdRequestInfo.zzGv, partialAdRequestInfo.zzGw, partialAdRequestInfo.zzrl, partialAdRequestInfo.zzGH, partialAdRequestInfo.zzGx, partialAdRequestInfo.zzGy, partialAdRequestInfo.zzGz, partialAdRequestInfo.zzGA, partialAdRequestInfo.zzGB, partialAdRequestInfo.zzGC, partialAdRequestInfo.zzGD, partialAdRequestInfo.zzGE, partialAdRequestInfo.zzGF, partialAdRequestInfo.zzGG, partialAdRequestInfo.zzqO, partialAdRequestInfo.zzrj, connectionStartTime, partialAdRequestInfo.zzGJ, partialAdRequestInfo.zzGK);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }
}
