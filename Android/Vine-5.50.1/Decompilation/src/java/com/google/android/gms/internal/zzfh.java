package com.google.android.gms.internal;

import android.location.Location;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

@zzha
/* loaded from: classes.dex */
public final class zzfh implements NativeMediationAdRequest {
    private final int zzBH;
    private final Date zzaW;
    private final Set<String> zzaY;
    private final boolean zzaZ;
    private final Location zzba;
    private final NativeAdOptionsParcel zzpE;
    private final List<String> zzpF;
    private final int zztH;
    private final boolean zztT;

    public zzfh(Date date, int i, Set<String> set, Location location, boolean z, int i2, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list, boolean z2) {
        this.zzaW = date;
        this.zztH = i;
        this.zzaY = set;
        this.zzba = location;
        this.zzaZ = z;
        this.zzBH = i2;
        this.zzpE = nativeAdOptionsParcel;
        this.zzpF = list;
        this.zztT = z2;
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdRequest
    public Date getBirthday() {
        return this.zzaW;
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdRequest
    public int getGender() {
        return this.zztH;
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdRequest
    public Set<String> getKeywords() {
        return this.zzaY;
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdRequest
    public Location getLocation() {
        return this.zzba;
    }

    @Override // com.google.android.gms.ads.mediation.NativeMediationAdRequest
    public NativeAdOptions getNativeAdOptions() {
        if (this.zzpE == null) {
            return null;
        }
        return new NativeAdOptions.Builder().setReturnUrlsForImageAssets(this.zzpE.zzyc).setImageOrientation(this.zzpE.zzyd).setRequestMultipleImages(this.zzpE.zzye).build();
    }

    @Override // com.google.android.gms.ads.mediation.NativeMediationAdRequest
    public boolean isAppInstallAdRequested() {
        return this.zzpF != null && this.zzpF.contains("2");
    }

    @Override // com.google.android.gms.ads.mediation.NativeMediationAdRequest
    public boolean isContentAdRequested() {
        return this.zzpF != null && this.zzpF.contains("1");
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdRequest
    public boolean isDesignedForFamilies() {
        return this.zztT;
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdRequest
    public boolean isTesting() {
        return this.zzaZ;
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdRequest
    public int taggedForChildDirectedTreatment() {
        return this.zzBH;
    }
}
