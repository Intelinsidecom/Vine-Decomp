package com.google.android.gms.ads.internal.client;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zzha;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public final class AdRequestParcel implements SafeParcelable {
    public static final zzg CREATOR = new zzg();
    public final Bundle extras;
    public final int versionCode;
    public final Bundle zztA;
    public final Bundle zztB;
    public final List<String> zztC;
    public final String zztD;
    public final String zztE;
    public final boolean zztF;
    public final long zztq;
    public final int zztr;
    public final List<String> zzts;
    public final boolean zztt;
    public final int zztu;
    public final boolean zztv;
    public final String zztw;
    public final SearchAdRequestParcel zztx;
    public final Location zzty;
    public final String zztz;

    public AdRequestParcel(int versionCode, long birthday, Bundle extras, int gender, List<String> keywords, boolean isTestDevice, int tagForChildDirectedTreatment, boolean manualImpressionsEnabled, String publisherProvidedId, SearchAdRequestParcel searchAdRequestParcel, Location location, String contentUrl, Bundle networkExtras, Bundle customTargeting, List<String> categoryExclusions, String requestAgent, String requestPackage, boolean isDesignedForFamilies) {
        this.versionCode = versionCode;
        this.zztq = birthday;
        this.extras = extras == null ? new Bundle() : extras;
        this.zztr = gender;
        this.zzts = keywords;
        this.zztt = isTestDevice;
        this.zztu = tagForChildDirectedTreatment;
        this.zztv = manualImpressionsEnabled;
        this.zztw = publisherProvidedId;
        this.zztx = searchAdRequestParcel;
        this.zzty = location;
        this.zztz = contentUrl;
        this.zztA = networkExtras;
        this.zztB = customTargeting;
        this.zztC = categoryExclusions;
        this.zztD = requestAgent;
        this.zztE = requestPackage;
        this.zztF = isDesignedForFamilies;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (!(other instanceof AdRequestParcel)) {
            return false;
        }
        AdRequestParcel adRequestParcel = (AdRequestParcel) other;
        return this.versionCode == adRequestParcel.versionCode && this.zztq == adRequestParcel.zztq && zzw.equal(this.extras, adRequestParcel.extras) && this.zztr == adRequestParcel.zztr && zzw.equal(this.zzts, adRequestParcel.zzts) && this.zztt == adRequestParcel.zztt && this.zztu == adRequestParcel.zztu && this.zztv == adRequestParcel.zztv && zzw.equal(this.zztw, adRequestParcel.zztw) && zzw.equal(this.zztx, adRequestParcel.zztx) && zzw.equal(this.zzty, adRequestParcel.zzty) && zzw.equal(this.zztz, adRequestParcel.zztz) && zzw.equal(this.zztA, adRequestParcel.zztA) && zzw.equal(this.zztB, adRequestParcel.zztB) && zzw.equal(this.zztC, adRequestParcel.zztC) && zzw.equal(this.zztD, adRequestParcel.zztD) && zzw.equal(this.zztE, adRequestParcel.zztE) && this.zztF == adRequestParcel.zztF;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.versionCode), Long.valueOf(this.zztq), this.extras, Integer.valueOf(this.zztr), this.zzts, Boolean.valueOf(this.zztt), Integer.valueOf(this.zztu), Boolean.valueOf(this.zztv), this.zztw, this.zztx, this.zzty, this.zztz, this.zztA, this.zztB, this.zztC, this.zztD, this.zztE, Boolean.valueOf(this.zztF));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzg.zza(this, out, flags);
    }
}
